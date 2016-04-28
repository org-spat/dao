package org.spat.dao.sql.executor;

import java.util.ArrayList;
import java.util.List;

import org.spat.dao.Mapping;

public abstract class Executor <T> {
	private Mapping<T> mapping;
	protected String strSQL;
	protected List<Object> params = new ArrayList<Object>();
	
	public String getStrSQL() {
		return strSQL;
	}

	public void setStrSQL(String strSQL) {
		this.strSQL = strSQL;
	}

	public List<Object> getParams() {
		return params;
	}

	public void setParams(List<Object> params) {
		this.params = params;
	}

	public Mapping<T> getMapping() {
		return mapping;
	}

	public void setMapping(Mapping<T> mapping) {
		this.mapping = mapping;
	}
	
	public abstract Object execute() throws Exception ;
}
