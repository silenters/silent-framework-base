package com.silent.framework.base.configurer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 资源上下文，读取Properties文件配置信息
 * @author TanJin
 * @date 2018年1月18日
 */
public class ResourceContext {
	protected static Logger logger = LoggerFactory.getLogger(ResourceContext.class);
	
	/**
	 * 获取类加载器
	 * @return
	 */
	public static ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}
	
	/**
	 * 根据给定的Properties配置文件路径，加载文件中的配置项并返回<br>
	 * 可同时加载多个配置文件，加载顺序根据填写的顺序进行，后加载的相同配置项覆盖前面加载的配置项<br>
	 * 若配置文件在Jar中，则需要填写指定目录。<br>
	 * 若配置文件在外部文件系统，则需要在配置文件路径前加：file:，以便区分
	 * @param configFilePaths properties配置文件路径
	 * @return
	 */
	public static Properties loadResourceConfigProperties(String... configFilePaths) {
		Properties properties = new Properties();
		for(String resource : configFilePaths) {
			URL url = findResource(resource);
			if(null == url) {
				logger.error("[ResourceContext] [Properties] [properties config file not find....] [resource:{}]", resource);
				continue;
			}
			try {
				properties.load(url.openStream());
			} catch (IOException e) {
				logger.error("[ResourceContext] [Properties]", e);
			}
		}
		return properties;
	}
	
	/**
	 * 查找资源
	 * @param resource
	 * @return
	 */
	public static URL findResource(String resource) {
		URL url = findResourceFromLocalFile(resource);
		if(url != null) {
			return url;
		}
		return findResourceInJar(resource);
	}
	
	/**
	 * 获取Jar包内指定资源文件的全路径
	 * @param 
	 * @return
	 */
	public static URL findResourceInJar(String resource) {
		try {
			if(resource == null) {
				return null;
			}
			if(resource.startsWith("classpath:")) {
				resource = resource.replace("classpath:", "");
			}
			URL url = getClassLoader().getResource(resource);
			logger.debug("[ResourceContext] [resource:{} find url:{}]", resource, url == null ? "null" : url.toString());
			return url;
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	/**
	 * 从本地文件系统查找资源，需在资源名前加"file:"标识符，以表示该文件存在在外部文件系统中
	 * @param filename
	 * @return
	 */
	public static URL findResourceFromLocalFile(String resource) {
		try {
			if(resource == null || !resource.startsWith("file:")) {
				return null;
			}
			File file = new File(resource.replace("file:", ""));
			if(file.exists()) {
				return file.toURI().toURL();
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	/**
	 * 查找指定Jar包中的配置文件，该方法用于多个Jar中有相同的名称的资源名称，则可通过该方法过滤出自己想要的资源
	 * @param resource
	 * @param jarName
	 * @return
	 * @throws IOException
	 */
	public static URL findResourceInJar(String resource, String jarName) throws IOException {
		if(resource == null) {
			return null;
		}
		if(resource.startsWith("classpath:")) {
			resource = resource.replace("classpath:", "");
		}
		
		Enumeration<URL> enumeration = getClassLoader().getResources(resource);
		while(enumeration != null && enumeration.hasMoreElements()) {
			URL nextUrl = enumeration.nextElement();
			if(nextUrl.toString().contains(jarName)) {
				logger.debug("[ResourceContext] [resource:{} find in url:{} from jarName:{}]", resource, nextUrl.toString(), jarName);
				return nextUrl;
			} else {
				logger.debug("[ResourceContext] [resource:{} not find in url:{} from jarName:{}]", resource, nextUrl.toString(), jarName);
			}
		}
		return null;
	}
}
