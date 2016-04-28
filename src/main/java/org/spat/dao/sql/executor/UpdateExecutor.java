package org.spat.dao.sql.executor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.spat.dao.Mapping;
import org.spat.dao.sql.SqlHelper;

public class UpdateExecutor<T> extends Executor<T> {

	private T data;
	private StringBuilder where = new StringBuilder();
	private StringBuilder sbFields = new StringBuilder();

	public UpdateExecutor(T data) {
		this.data = data;
	}

	public UpdateExecutor() {

	}

	@Override
	public Integer execute() throws Exception {
		if (data != null) {
			genSQL4Data();
		} else {
			if (sbFields.length() == 0) {
				throw new Exception("未设置需要更新的字段信息,格式：da.update().field(\"state\").where(\"id=?\").setParams(1,id)");
			}
			genSQL();
		}

		SqlHelper sqlHelper = SqlHelper.get(this.strSQL, getMapping().getDatabase());
		if (params != null) {
			sqlHelper.addParam(params.get(0));
			for (int i = 1; i < params.size(); i++) {
				Object value = params.get(i);
				sqlHelper.addParam(value);
			}
		}
		return sqlHelper.executeUpdate();
	}

	public UpdateExecutor<T> where(String condition) {
		where.append(condition);
		return this;
	}

	public UpdateExecutor<T> field(String... fields) {
		for (String field : fields) {
			String column = getMapping().getColumnName(field.toLowerCase());
			sbFields.append(column + "=?,");
		}
		if (sbFields.charAt(sbFields.length() - 1) == ',') {
			sbFields.setLength(sbFields.length() - 1);
		}
		return this;
	}

	public UpdateExecutor<T> setParams(Object... params) {
		for (Object param : params) {
			this.params.add(param);
		}
		return this;
	}

	private void genSQL() throws Exception {
		if (sbFields.length() == 0) {
			throw new Exception("未设置需要更新的字段信息");
		}
		if (where.length() == 0) {
			throw new Exception("未设置条件信息");
		}
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("update ");
		sbsql.append(getMapping().getTableName());
		sbsql.append(" set ");
		sbsql.append(sbFields);
		sbsql.append(" where ");
		sbsql.append(where);
		this.strSQL = sbsql.toString();
	}

	private void genSQL4Data() throws Exception {
		if (data == null) {
			throw new Exception("更新对象信息为NULL!");
		}
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("update ");
		sbsql.append(getMapping().getTableName());
		sbsql.append(" set");
		List<Object> whereValue = new ArrayList<Object>();
		Map<String, Mapping<T>.ColumnInf> fieldMap = getMapping().getFieldMap();
		for (Map.Entry<String, Mapping<T>.ColumnInf> entry : fieldMap.entrySet()) {  
			Mapping<?>.ColumnInf cInf =entry.getValue();
			if(cInf==null){
				continue;
			}
			
			Method method = cInf.getMethod;
			Object value = method.invoke(data, new Object[] {});
			if(cInf.converter!=null){
				value = cInf.converter.toDB(cInf.field.getType(), value);
			}

			if (cInf.isId) {
				if (where.length() > 0) {
					where.append(" and " + cInf.column.name() + "=?");
				}else{
					where.append(cInf.column.name() + "=?");
				}
				whereValue.add(value);
			}
			
			if (getMapping().isAutoId() && cInf.isId) {
				continue;
			}
			
			sbsql.append("`" + cInf.column.name() + "`");
			sbsql.append("=?,");
			params.add(value);
		}
		for (Object value : whereValue) {
			params.add(value);
		}
		if (sbsql.charAt(sbsql.length() - 1) == ',') {
			sbsql.setLength(sbsql.length() - 1);
		}
		sbsql.append(" where ");
		sbsql.append(this.where.toString());
		this.strSQL = sbsql.toString();
	}
}
