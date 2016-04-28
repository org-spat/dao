package org.spat.dao.sql.executor;

import java.sql.ResultSet;

import org.spat.dao.sql.IRowCallbackHandler;
import org.spat.dao.sql.SqlHelper;

public class CountExecutor<T> extends Executor<T> implements IRowCallbackHandler {

	private StringBuilder where = new StringBuilder();
	
	@Override
	public Integer execute() throws Exception {
		genSQL();
		SqlHelper sqlHelper = SqlHelper.get(strSQL, getMapping().getDatabase());
		if (params != null) {
			for (Object param : params)
				sqlHelper.addParam(param);
		}
		Integer result = (Integer) sqlHelper.executeQuery(this);
		return result;
	}

	public CountExecutor<T> where(String condition) {
		where.append(condition);
		return this;
	}

	public CountExecutor<T> setParams(Object... params) {
		for (Object param : params) {
			this.params.add(param);
		}
		return this;
	}


	private void genSQL() {
		StringBuilder sbsql = new StringBuilder();
		sbsql.append("select count(*) from ");
		sbsql.append(getMapping().getTableName());
		if (where.length() > 0) {
			sbsql.append(" where ");
			sbsql.append(this.where.toString());
		}
		this.strSQL = sbsql.toString();
	}

	@Override
	public Object DataRowHandle(ResultSet rs) throws Exception {
		if(!rs.next()){
			return 0;
		}
		return rs.getInt(1);
	}
}
