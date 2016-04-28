package org.spat.dao.sql.executor;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.spat.dao.Mapping;
import org.spat.dao.sql.IRowCallbackHandler;
import org.spat.dao.sql.SqlHelper;
import org.spat.dao.sql.OrderByPattern;

public class SelectExecutor<T> extends Executor<T> implements IRowCallbackHandler {

	private StringBuilder where = new StringBuilder();
	private int offset = 0, length = 0;
	private String orderByFidle = "";
	private OrderByPattern orderBypattern;
	private String columns = "*";

	public SelectExecutor() {

	}

	public SelectExecutor(String columns) {
		this.columns = columns;
	}

	@Override
	public T execute() throws Exception {
		List<T> results = execute(0, 1);
		if (results == null || results.size() == 0) {
			return null;
		}
		return results.get(0);
	}

	/*
	 * @param offset:偏移数
	 * 
	 * @param len:读取数量,0标示不限制
	 */
	public List<T> execute(int offset, int len) throws Exception {
		this.offset = offset;
		this.length = len;
		genSQL();
		SqlHelper sqlHelper = SqlHelper.get(strSQL, getMapping().getDatabase());
		if (params != null) {
			for (Object param : params)
				sqlHelper.addParam(param);
		}
		@SuppressWarnings("unchecked")
		List<T> result = (List<T>) sqlHelper.executeQuery(this);
		return result;
	}

	public SelectExecutor<T> where(String condition) {
		where.append(condition);
		return this;
	}

	public SelectExecutor<T> setParams(Object... params) {
		for (Object param : params) {
			this.params.add(param);
		}
		return this;
	}

	public SelectExecutor<T> orderBy(String filed, OrderByPattern pattern) {
		this.orderByFidle = filed;
		this.orderBypattern = pattern;
		return this;
	}

	private void genSQL() {
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("select ");
		sbsql.append(this.columns);
		sbsql.append(" from ");
		sbsql.append(getMapping().getTableName());
		if (where.length() > 0) {
			sbsql.append(" where ");
			sbsql.append(this.where.toString());
		}
		if (orderByFidle.length() > 0) {
			sbsql.append(" order by ");
			sbsql.append(this.orderByFidle);
		}
		if (this.orderBypattern != null) {
			sbsql.append(' ');
			sbsql.append(this.orderBypattern.toString());
		}
		if (this.length > 0) {
			sbsql.append(" limit ?,?");
			params.add(offset);
			params.add(length);
		}
		
		this.strSQL = sbsql.toString();
	}

	@Override
	public Object DataRowHandle(ResultSet rs) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnsCount = rsmd.getColumnCount();
		List<String> columnNameList = new ArrayList<String>();
		for (int i = 0; i < columnsCount; i++) {
			columnNameList.add(rsmd.getColumnLabel(i + 1).toLowerCase());
		}
		List<T> result = new ArrayList<T>();
		while (rs.next()) {
			T bean = getMapping().getMappingClass().newInstance();
			for (String columnName : columnNameList) {
				Mapping<?>.ColumnInf cInf = getMapping().getColumnInfo(columnName);
				if (cInf == null){
					continue;
				}
				Object columnValueObj = rs.getObject(columnName);
				if(cInf.converter!=null){
					columnValueObj = cInf.converter.toEntity(cInf.field.getType(), columnValueObj);
				}
				if(columnValueObj ==null && cInf.field.getType().isPrimitive()){
					columnValueObj = 0;
				}
				Method setterMethod = cInf.setMethod;
				setterMethod.invoke(bean, new Object[] { columnValueObj });
			}
			result.add(bean);
		}
		return result;
	}
}
