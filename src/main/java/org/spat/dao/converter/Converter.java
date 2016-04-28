package org.spat.dao.converter;

import java.util.HashMap;

/*
 * 将实体中的字段类型转换成数据库中的类型，或者将数据库类型转换成实体类型
 * 
 * 
 * 
 */
public abstract class Converter {
	private static HashMap<String, Converter> CONVERTERS = new HashMap<String, Converter>();
	public static void register(Converter converter,String name){
		CONVERTERS.put(name, converter);
	}
	public static Converter getConverter(String name){
		return CONVERTERS.get(name);
	}
	
	public abstract Object toDB(Class<?> clz,Object value);
	public abstract Object toEntity(Class<?> clz,Object value);
}
