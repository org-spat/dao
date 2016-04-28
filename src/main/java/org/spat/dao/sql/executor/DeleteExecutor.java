package org.spat.dao.sql.executor;

import org.spat.dao.sql.SqlHelper;

public class DeleteExecutor<T> extends Executor<T> {
	private StringBuilder where = new StringBuilder();
	
	@Override
	public Boolean execute() throws Exception {
		genSQL();
		SqlHelper sqlHelper = SqlHelper.get(this.strSQL, getMapping().getDatabase());
		if (params != null) {
			for (Object param : params){
				sqlHelper.addParam(param);
			}
		}
		return sqlHelper.execute();
	}
	
	public DeleteExecutor<T> where(String condition){
		where.append(condition);
		return this;
	}
	
	public DeleteExecutor<T> setParams(Object ...params){
		for (Object param : params) {
			this.params.add(param);
		}
		return this;
	}
	
	private void genSQL() {
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("delete from");
		sbsql.append(getMapping().getTableName());
		if (where.length() > 0) {
			sbsql.append(" where ");
			sbsql.append(this.where.toString());
		}
		this.strSQL = sbsql.toString();
	}
}
