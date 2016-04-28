package org.spat.dao;

import org.spat.dao.sql.executor.CountExecutor;
import org.spat.dao.sql.executor.DeleteExecutor;
import org.spat.dao.sql.executor.InsertExecutor;
import org.spat.dao.sql.executor.SelectExecutor;
import org.spat.dao.sql.executor.UpdateExecutor;

public class DataAccess<T> {
	
	private Mapping<T> mapping;
	
	protected Mapping<T> getMapping() {
		return mapping;
	}

	protected void setMapping(Mapping<T> mapping) {
		this.mapping = mapping;
	}

	public SelectExecutor<T> select() {
		SelectExecutor<T> executor = new SelectExecutor<>();
		executor.setMapping(mapping);
		return executor;
	}
	
	public SelectExecutor<T> select(String columns) {
		SelectExecutor<T> executor = new SelectExecutor<>(columns);
		executor.setMapping(mapping);
		return executor;
	}
	
	public DeleteExecutor<T> delete(){
		DeleteExecutor<T> executor = new DeleteExecutor<T>();
		executor.setMapping(mapping);
		return executor;
	}
	
	public InsertExecutor<T> insert(T data){
		InsertExecutor<T> executor = new InsertExecutor<T>(data);
		executor.setMapping(mapping);
		return executor;
	}
	
	public UpdateExecutor<T> update(T data){
		UpdateExecutor<T> executor = new UpdateExecutor<T>(data);
		executor.setMapping(mapping);
		return executor;
	}
	public UpdateExecutor<T> update(){
		UpdateExecutor<T> executor = new UpdateExecutor<T>();
		executor.setMapping(mapping);
		return executor;
	}
	
	public CountExecutor<T> count(){
		CountExecutor<T> executor = new CountExecutor<>();
		executor.setMapping(mapping);
		return executor;
	}
}
