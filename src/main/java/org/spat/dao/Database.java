package org.spat.dao;

import java.util.HashMap;
import java.util.Map;

import org.spat.dao.connpool.ConnectionPool;
import org.spat.dao.connpool.ConnectionPoolFactory;

public class Database {
	
	private DBConfig config;
	private ConnectionPool connectionPool;
	private int state = 1;//0:不可用，1:正常 
	
	private static Map<String, Database> DBS = new HashMap<String, Database>();
	
	public static Database get(DBConfig config) throws Exception{
		if(config==null){
			throw new ErrorInputException("DBConfig must be not NULL!");
		}
		if(DBS.containsKey(config.getName().toLowerCase())){
			return DBS.get(config.getName().toLowerCase());
		}
		Database database = new Database(config);
		DBS.put(config.getName().toLowerCase(), database);
		return database;
	}
	
	private Database(DBConfig config) throws Exception{
		this.config = config;
		this.connectionPool = ConnectionPoolFactory.createPool(config);
	}
	
	public String getName() {
		return this.config.getName();
	}
	public DBConfig getConfig() {
		return config;
	}
	public ConnectionPool getConnectionPool() {
		return connectionPool;
	}
	public int getState() {
		return this.state;
	}
	public void setState(int state) {
		this.state = state;
	}
	
	public <T> DataAccess<T> getDA(Class<T> t){
		return DataAccessFactory.get(t, this);
	}
	
	protected boolean checkAvailable(){
		//TODO:ping database for check
		return true;
	}
}
