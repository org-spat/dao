package org.spat.dao.sql;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Utils {		
	public static String join(Collection<?> s, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator<?> iter = s.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(delimiter);
            }
        }
        return buffer.toString();
    }
	
	private static Map<Class<?>, Object> beanMap = new HashMap<Class<?>, Object>();
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> clazz){
		Object obj = beanMap.get(clazz);
		if (obj != null) return (T) obj;
		T t;
		try {
			t = clazz.newInstance();
		} catch (InstantiationException e) {
			return null;
		} catch (IllegalAccessException e) {
			return null;
		}
		beanMap.put(clazz, t);
		return t;
	}

	public static String getRootPath(){
		File file = new File(System.getProperty("user.dir"));
		String path = file.getAbsolutePath().replace('\\', '/');
			   path = path.substring(0, path.indexOf('/'));
		return path;
	}
}
