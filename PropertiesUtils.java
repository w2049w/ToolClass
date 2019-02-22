package com.gsafety.starscream.utils.file;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtils {

	private static Properties propeties = new Properties();
	/**
	 * 获取系统中properties文件
	 * @param propertyName
	 * @param clazz          可设置成当前类
	 * @return
	 * @throws IOException 
	 */
	@SuppressWarnings("rawtypes")
	public static Properties getProperties(String propertyName,Class clazz){
		
		Resource resource = new ClassPathResource(propertyName);
		try {
			propeties = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return propeties;
	}
	
	/**
	 * 根据properties文件名和key值获取value值
	 * @param propertyName
	 * @param key
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getPropertiesValue(String propertyName,String key,Class clazz){
		Properties properties = getProperties(propertyName,clazz);
		return properties.getProperty(key);
	}
	
	/**
	 * 根据properties文件名和key值获取value值
	 * @param propertyName
	 * @param key
	 * @return
	 */
	public static String getPropertiesValue(String propertyName,String key){
		Properties properties = getProperties(propertyName,PropertiesUtils.class);
		return properties.getProperty(key);
	}
	
	/**
	 * 根据properties文件名和key值集合获取value值的集合
	 * @param propertyName
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String,String> getPropertiesValue(String propertyName,String[] keys,Class clazz){
		Properties properties = getProperties(propertyName,clazz);
		Map<String,String> resultMap = new LinkedHashMap<String,String>();
		for(String key : keys){
			String value = properties.getProperty(key);
			resultMap.put(key, value);
		}
		return resultMap;
	}
}
