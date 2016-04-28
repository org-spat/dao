package org.spat.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.spat.dao.annotation.AutoId;
import org.spat.dao.annotation.Column;
import org.spat.dao.annotation.Id;
import org.spat.dao.annotation.Table;
import org.spat.dao.converter.Converter;

public class Mapping<T> {
	private Class<T> clazz;
	private Database database;
	private String tableName;
	private boolean isAutoId = false;
	private Map<String, ColumnInf> orMap = new HashMap<String, ColumnInf>();
	private Map<String, ColumnInf> roMap = new HashMap<String, ColumnInf>();
	
	protected Mapping(Class<T> clazz,Database database){
		this.clazz = clazz;
		this.database = database;
		bulid();
	}
	
	
	private void bulid(){
		if(clazz.isAnnotationPresent(Table.class)){
			Table table = (Table)clazz.getAnnotation(Table.class);
			this.tableName = table.name();
		}else{
			String name = clazz.getName();
			this.tableName = name.substring(name.lastIndexOf(".") + 1);
		}
		
		this.isAutoId = clazz.isAnnotationPresent(AutoId.class);
		
		Field[] fields = clazz.getDeclaredFields();
		for (Field f : fields) {
			if (f.isAnnotationPresent(Column.class)) {
				ColumnInf cInf = new ColumnInf();
				cInf.field = f;
				cInf.column = (Column) f.getAnnotation(Column.class);
				cInf.setMethod = findSetMethod(f.getName());
				cInf.getMethod = findGetMethod(f.getName());
				cInf.isId = f.isAnnotationPresent(Id.class);
				
				if(!cInf.column.converter().isEmpty()){
					cInf.converter = Converter.getConverter(cInf.column.converter());
				}
				
				orMap.put(f.getName(), cInf);
				roMap.put(cInf.column.name().toLowerCase(), cInf);
			}
		}
	}
	
	public Map<String, ColumnInf> getFieldMap(){
		return orMap;
	}
	
	public Map<String, ColumnInf> getColumnMap(){
		return roMap;
	}
	
	public Database getDatabase() {
		return database;
	}
	
	/**
	 * 得到表名
	 * @return the tableName
	 */
	public String getTableName() {
		return tableName;
	}
	
	/**
	 * 是否是数据库生成Id
	 * @return the isGenerateId
	 */
	public boolean isAutoId() {
		return isAutoId;
	}
	
	
	
	/**
	 * 得到字段和列的对应关系
	 * @param fieldName
	 * @return
	 */
	public String getColumnName(String fieldName) {
		ColumnInf cInf =  orMap.get(fieldName);
		if(cInf==null){
			return "";
		}
		return cInf.column.name();
	}
	
	public String getFieldName(String columnName) {
		ColumnInf cInf =  roMap.get(columnName);
		if(cInf==null){
			return "";
		}
		return cInf.field.getName();
		
	}
	
	
	public ColumnInf getColumnInfo(String column){
		return roMap.get(column.toLowerCase());
	}
	
	public Method getGetMethod(String fieldName){
		ColumnInf cInf = orMap.get(fieldName);
		if(cInf==null){
			return null;
		}
		return cInf.getMethod;
	}
	
	public Method getSetMethod(String fieldName){
		ColumnInf cInf = orMap.get(fieldName);
		if(cInf==null){
			return null;
		}
		return cInf.setMethod;
	}
	
	/**
	 * @return the clazz
	 */
	public Class<T> getMappingClass() {
		return clazz;
	}
	
	private Method findSetMethod(String field){
		String setFunName = "set" + field.substring(0,1).toUpperCase()+ field.substring(1);
		// TODO: 如果没找到，放置null？ set方法重载如何处理？
		Method setterMethod = null;
		for(Method m : clazz.getMethods()){
			if(m.getName().equals(setFunName)){
				setterMethod = m;
			}
		}
		return setterMethod;
	}

	private Method findGetMethod(String field){
		String getFunName = "get" + field.substring(0,1).toUpperCase()+ field.substring(1);
		// TODO: 如果没找到，放置null？ set方法重载如何处理？
		Method getterMethod = null;
		for(Method m : clazz.getMethods()){
			if(m.getName().equals(getFunName)){
				getterMethod = m;
			}
		}
		return getterMethod;
	}
	
	public class ColumnInf{
		public Column column;
		public Field field;
		public Method setMethod;
		public Method getMethod;
		public boolean isId;
		public Converter converter;
	}
	
}
