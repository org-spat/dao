package org.spat.dao.connpool;

import java.util.HashMap;
import java.util.Map;

import org.spat.dao.DBConfig;

public class ConnectionPoolFactory {
	
	private static Map<String, ConnectionPool> POOLS = new HashMap<String, ConnectionPool>();
	
	public synchronized static ConnectionPool createPool(DBConfig config) throws Exception{
		String key = config.getName();
		if(POOLS.containsKey(key)){
			return POOLS.get(key);
		}
		ConnectionPool pool = new ConnectionPool(config);
		POOLS.put(key, pool);
		return pool;
	}

	
	private ConnectionPoolFactory(){
		
	}
}
