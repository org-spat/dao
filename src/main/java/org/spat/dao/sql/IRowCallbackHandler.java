package org.spat.dao.sql;

import java.sql.ResultSet;

public interface IRowCallbackHandler {
	public Object DataRowHandle(ResultSet rs) throws Exception;
}