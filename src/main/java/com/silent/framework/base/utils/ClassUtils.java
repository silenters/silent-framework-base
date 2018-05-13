package com.silent.framework.base.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

/**
 * Bean类Class操作、Field操作、Annotation操作
 * @author TanJin
 * @date 2017年6月13日
 */
public class ClassUtils {
	
	/**
	 * 根据Class及字段名称， 获取指定字段的Field
	 * @param clazz
	 * @param fieldName 字段名称
	 * @return
	 */
	public static Field getField(Class<?> clazz, String fieldName){
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			if(field.getName().equals(fieldName)) {
				return field;
			}
		}
		return null;
	}
	
	/**
	 * 是否为基础字段
	 * @param field
	 * @return
	 */
	public static boolean isBaseField(Field field) {
		Class<?> clazz = field.getType();
		return isBaseField(clazz);
	}
	
	/**
	 * 是否为基础字段
	 * @param object 实体对象
	 * @return
	 */
	public static boolean isBaseField(Class<?> clazz) {
		if(clazz == byte[].class || clazz == Byte[].class || 
		   clazz == short.class || clazz == Short.class || 
		   clazz == int.class || clazz == Integer.class ||
		   clazz == long.class || clazz == Long.class ||
		   clazz == float.class || clazz == Float.class || 
		   clazz == double.class || clazz == Double.class || 
		   clazz == byte.class || clazz == Byte.class || 
		   clazz == char.class || clazz == Character.class ||
		   clazz == boolean.class || clazz == Boolean.class ||
		   clazz == String.class) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取集合类型的泛型Class
	 * @param collectionField 集合字段
	 * @return
	 */
	public static Class<?> getCollectionGenericityClass(Field collectionField) {
		ParameterizedType genericType = (ParameterizedType) collectionField.getGenericType();
		//得到泛型里的class类型对象    
        Class<?> genericClazz = (Class<?>)genericType.getActualTypeArguments()[0];
        return genericClazz;
	}
	
	/**
	 * 获取Map类型的泛型Class<br>
	 * @param mapField Map字段
	 * @param index map泛型位置，0代表Key，1代表Vlaue
	 * @return
	 */
	public static Class<?> getMapGenericityClass(Field mapField, int index) {
		ParameterizedType genericType = (ParameterizedType) mapField.getGenericType();
		//得到泛型里的class类型对象    
        Class<?> genericClazz = (Class<?>)genericType.getActualTypeArguments()[index];
        return genericClazz;
	}
}
