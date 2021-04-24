package com.yc.inter.websocket.client.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	/**
	 * <p>描述：根据相对路径获取一个Properties对象 </p>
	 * <p>日期：2014-12-3 上午08:33:51 </p>
	 * @param filePath 文件相对于项目根路径的相对路径，如：src/jdbc.properties
	 * @return 返回Properties对象，可使用返回对象进行属性获取
	 */
	public static Properties getProperties(String filePath) {
		Properties properties = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(filePath);
			properties = new Properties();
			
			//加载文件
			properties.load(in);		
		} catch (Exception e) {
			logger.error("读取配置文件出错："+e.getMessage(), e);
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				logger.error("关闭文件流出错："+e.getMessage(), e);
			}
		}
		return properties;
	}
	
	/**
	 * 
	 * <p>描述：根据相对路径和配置属性获取一个properties文件中某一个属性值 </p>
	 * <p>日期：2014-12-3 上午08:40:54 </p>
	 * @param filePath 文件相对于项目根路径的相对路径，如：src/jdbc.properties
	 * @param key 配置文件中的属性名字，如：配置文件中存在键值对user=admin，则传入user
	 * @return 返回目标配置文件中属性为参数传入key的属性值
	 */
	public static String getProperty(String filePath, String key){
		Properties properties = getProperties(filePath);
		String property = null;
		if(properties!=null){
			property = properties.getProperty(key);
		}
		return property;
	}

	/**
	 * 
	 * <p>描述：web环境读取配置文件 </p>
	 * <p>日期：2014-12-22 下午03:09:24 </p>
	 * @param clz 类
	 * @param fileName 文件名,不用加后缀
	 * @return 返回配置文件对象
	 * @throws Exception
	 */
	public static Properties getProperties(Class<?> clz, String fileName) {
		Properties properties = null;
		InputStream input = null;
		try {
			input = clz.getClassLoader().getResourceAsStream(fileName+".properties");
			properties = new Properties();
			properties.load(input);
		} catch (Exception e) {
			logger.error("读取配置文件出错："+e.getMessage(), e);
		} finally{
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				logger.error("关闭文件流出错："+e.getMessage(), e);
			}
		}
		return properties;
	}
	
	/**
	 * 
	 * <p>描述：web环境读取配置文件的属性值 </p>
	 * <p>日期：2014-12-22 下午03:09:24 </p>
	 * @param clz 类
	 * @param fileName 文件名,不用加后缀
	 * @param key 要获取的键
	 * @return 返回属性值
	 * @throws Exception
	 */
	public static String getProperty(Class<?> clz, String fileName, String key) {
		String property = null;
		InputStream input = null;
		try {
			input = clz.getClassLoader().getResourceAsStream(fileName+".properties");
			Properties properties = new Properties();
			properties.load(input);
			property = properties.getProperty(key);
		} catch (Exception e) {
			logger.error("读取配置文件出错："+e.getMessage(), e);
		} finally{
			try {
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				logger.error("关闭文件流出错："+e.getMessage(), e);
			}
		}
		return property;
	}
}
