package org.spat.dao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class DataAccessFactory {
	
	private static Map<String, DataAccess<?>> map = new ConcurrentHashMap<String, DataAccess<?>>();
	
	/**
	 * 创建
	 * @param <T>
	 * @param t
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> DataAccess<T> get(Class<T> t,Database database){
		String key = database.getName().toLowerCase()+"."+t.getName();
		DataAccess<?> da = map.get(t);
		if (da != null){
			return (DataAccess<T>) da;
		}
		Mapping<T> mapping = new Mapping<T>(t,database);
		DataAccess<T> dal = new DataAccess<T> ();
		dal.setMapping(mapping);                                  
		map.put(key, dal);
		return dal;
	}
}
