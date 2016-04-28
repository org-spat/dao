package org.spat.dao.sql.executor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.Map;

import org.spat.dao.Mapping;
import org.spat.dao.sql.IRowCallbackHandler;
import org.spat.dao.sql.SqlHelper;

public class InsertExecutor<T> extends Executor<T> implements IRowCallbackHandler{

	private T data;
	
	public InsertExecutor(T data){
		this.data = data;
	}
	
	@Override
	public Object execute() throws Exception {
		genSQL();
		SqlHelper sqlHelper = SqlHelper.get(this.strSQL, getMapping().getDatabase());
		if (params != null) {
			sqlHelper.addParam(params.get(0));
			for (int i = 1; i < params.size(); i++) {
				Object value = params.get(i);
				sqlHelper.addParam(value);
			}
		}
		return sqlHelper.executeInsert(this);
	}
	
	private void genSQL() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("insert into ");
		sbsql.append(getMapping().getTableName());
		sbsql.append('(');
		StringBuilder sbvalues = new StringBuilder();
		sbvalues.append('(');
		Map<String, Mapping<T>.ColumnInf> fieldMap = getMapping().getFieldMap();
		for (Map.Entry<String, Mapping<T>.ColumnInf> entry : fieldMap.entrySet()) {  
			Mapping<?>.ColumnInf cInf =entry.getValue();
			if(cInf==null || cInf.column==null){
				continue;
			}
			if (getMapping().isAutoId() && cInf.isId){
				continue;
			}
			sbsql.append("`"+cInf.column.name()+"`");
			sbsql.append(',');
			sbvalues.append("?,");
			
			Method method = cInf.getMethod;
			Object value = method.invoke(data, new Object[] {});
			params.add(value);
		}
		if(sbsql.charAt(sbsql.length()-1)==','){
			sbsql = sbsql.deleteCharAt(sbsql.length()-1);
			sbvalues= sbvalues.deleteCharAt(sbvalues.length()-1);
		}
		sbsql.append(')');
		sbvalues.append(')');
		sbsql.append("values");
		sbsql.append(sbvalues);
		this.strSQL = sbsql.toString();
	}

	/*
	 * 获取自增长ID
	 */
	@Override
	public Object DataRowHandle(ResultSet rs) throws Exception { 
		if(!getMapping().isAutoId()) {
			return null;
		}
		if (!rs.next()) {
			throw new Exception("新增时，未从数据库中获得新增id");
		}
		return rs.getObject(1); 
	}
}
