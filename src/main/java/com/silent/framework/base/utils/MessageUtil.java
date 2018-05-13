package com.silent.framework.base.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.entity.ByteArrayEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;


/**
 * 发送消息接口
 * @author TanJin
 * @date 2016-10-25
 */
public class MessageUtil {
	private static Logger logger = LoggerFactory.getLogger(MessageUtil.class);
	
	/**
	 * 发送消息
	 * @param service		// 监控服务，比如 webservices服务、impala、hive等
	 * @param name			// 消息项		必填
	 * @param description	// 消息项描述,简要介绍即可	必填	
	 * @param message		// 消息内容	必填
	 * @param toGroup		// 要发送给的组的标示，需要通过该名称找到要发送的用户列表	必填
	 * @date 2016-10-25
	 */
	public static boolean sendServiceMessageFromWeb(String service, String name, String description, String message, String toGroup) {
		String url = "http://devops.vlifepaper.com/message.api";
		return sendServiceMessageFromWeb("bigdata", service, name, description, message, toGroup, url, null);
	}
	
	/**
	 * 发送消息
	 * @param server		//集群名称	必填
	 * @param name			//服务名称	必填
	 * @param item			//消息项		必填
	 * @param description	//消息项描述,简要介绍即可	必填	
	 * @param message		//消息内容	必填
	 * @param toGroup		//要发送给的组的标示，需要通过该名称找到要发送的用户列表	必填
	 * @param colorMap      //自定义颜色      <br>key:"server","name","item","description","message"<br>value:"#000000"
	 * @date 2016-10-25
	 */
	private static boolean sendServiceMessageFromWeb(String server, String name, String item, String description, String message, String toGroup, String url, Map<String, String> colorMap) {
		if(StringUtils.isEmpty(url)) {
			logger.error("[service_message] [server:{}] [service:{}] [name:{}] [toGroup:{}] [url is empty]", server, name, item, toGroup);
			return false;
		}
		if(StringUtils.isEmpty(toGroup)) {
			logger.error("[service_message] [server:{}] [service:{}] [name:{}] [url:{}] [toGroup is empty]", server, name, item, url);
			return false;
		}
		
		long time = System.currentTimeMillis();
		logger.error("[service_message] [start] [server:{}] [service:{}] [name:{}] [description:{}] [toGroup:{}] [url:{}] [message:{}]", server, name, item, description, toGroup, url, message);
		boolean result = false;
		
		try {
			String check = DigestStringUtils.hash("vlife-" + time + "-message");
			StringBuilder builder = new StringBuilder(url);
			if(builder.lastIndexOf("?")>0) {
				builder.append("&");
			} else {
				builder.append("?");
			}
			builder.append("t=").append(time);
			builder.append("&c=").append(check);
			
			//构建Request数据
			JSONObject requestJson = buildRequest(server, name, item, description, message, toGroup, url, colorMap);
			byte[] request = requestJson.toJSONString().getBytes();
			
			//加密
			CryptionUtil.enDecryption(request);
			ByteArrayEntity entity = new ByteArrayEntity(request);
			byte[] response = HttpClientUtils.post(builder.toString(), entity);
			
			if(response != null) {
				//解密
				CryptionUtil.enDecryption(response);
				JSONObject responseJson = JSONObject.parseObject(new String(response));
				result = "success".equals(responseJson.getString("result"));
			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			logger.error("[service_message] [end] [time:{}] [server:{}] [service:{}] [name:{}] [description:{}] [command:{}] [url:{}]",
					System.currentTimeMillis() - time, server, name, item, description, toGroup, url);
		}
		return result;
	}
	
	/**
	 * 构造请求的json
	 * 
	 * @param server
	 * @param name
	 * @param item
	 * @param description
	 * @param message
	 * @param toGroup
	 * @param url
	 * @param colorMap
	 * @return
	 */
	private static JSONObject buildRequest(String server, String name, String item, String description, String message, String toGroup, String url, Map<String, String> colorMap) {
		String request = url.substring(url.lastIndexOf("/") + 1);
		if (request.equals("custom_message.api")) {
			return buildRequestWithColor(server, name, item, description, message, toGroup, colorMap);
		}
		JSONObject requestJson = new JSONObject();
		requestJson.put("server", server);
		requestJson.put("name", name);
		requestJson.put("item", item);
		requestJson.put("description", description);
		requestJson.put("message", message);
		requestJson.put("command", toGroup);
		return requestJson;
	}

	/**
	 * 构造附带属性的请求json
	 * 
	 * @param server
	 * @param name
	 * @param item
	 * @param description
	 * @param message
	 * @param toGroup
	 * @param url
	 * @param colorMap
	 * @return
	 */
	private static JSONObject buildRequestWithColor(String server, String name, String item, String description, String message, String toGroup, Map<String, String> colorMap) {
		JSONObject requestJson = new JSONObject();
		JSONObject serverObject = new JSONObject();
		serverObject.put("value", server);
		serverObject.put("color", getColorWithKey("server", colorMap));
		requestJson.put("server", serverObject);
		
		JSONObject nameObject = new JSONObject();
		nameObject.put("value", name);
		nameObject.put("color", getColorWithKey("name", colorMap));
		requestJson.put("name", nameObject);
		
		JSONObject itemObject = new JSONObject();
		itemObject.put("value", item);
		itemObject.put("color", getColorWithKey("item", colorMap));
		requestJson.put("item", itemObject);
		
		JSONObject descriptionObject = new JSONObject();
		descriptionObject.put("value", description);
		descriptionObject.put("color", getColorWithKey("description", colorMap));
		requestJson.put("description", descriptionObject);
		
		JSONObject messageObject = new JSONObject();
		messageObject.put("value", message);
		messageObject.put("color", getColorWithKey("message", colorMap));
		requestJson.put("message", messageObject);
		
		JSONObject commandObject = new JSONObject();
		commandObject.put("value", toGroup);
		commandObject.put("color", getColorWithKey("command", colorMap));
		requestJson.put("command", commandObject);
		return requestJson;
	}
	
	/**
	 * 获取特定属性的颜色
	 * 
	 * @param key
	 * @param colorMap
	 * @return
	 */
	private static String getColorWithKey(String key, Map<String, String> colorMap){
		if (colorMap == null || !colorMap.containsKey(key)) {
			return null;
		}
		return colorMap.get(key);
	}
	
	public static void main(String[] args) {
		String server = "mimo";
		String name = "fengguibin";
		String item = "ad_real_amount_hour_test";
		String description = "广告时刻真实投放量 -- 测试";
		String command = "test";
		StringBuilder message = new StringBuilder();
		message.append("<table border=1 width=700>");
		message.append("<thead>");
		message.append("<tr>");
		message.append("<td>").append("广告ID").append("</td>");
		message.append("<td>").append("时刻").append("</td>");
		message.append("<td>").append("真实投放量").append("</td>");
		message.append("<td>").append("投放率").append("</td>");
		message.append("<td>").append("剩余投放量").append("</td>");
		message.append("<td>").append("状态").append("</td>");
		message.append("</tr>");
		message.append("</thead>");
		message.append("<tbody>");
		message.append("<tr>");
		message.append("<td>").append("234").append("</td>");
		message.append("<td>").append("10:00-15:00").append("</td>");
		message.append("<td>").append("234343").append("</td>");
		message.append("<td>").append("34%").append("</td>");
		message.append("<td>").append("2333423").append("</td>");
		message.append("<td>").append("start").append("</td>");
		message.append("</tr>");
		message.append("</tbody>");
		message.append("</table>");

		Map<String, String> colorMap = new ConcurrentHashMap<String, String>();
		colorMap.put("server", "#A4F5B6");
		colorMap.put("name", "#B4BB44");
		colorMap.put("item", "#DDCCBB");
		colorMap.put("description", "#FFAADD");
		String url = "http://localhost:8090/config/custom_message.api";
		try {
			boolean result = MessageUtil.sendServiceMessageFromWeb(server, name, item, description, message.toString(),
					command, url, colorMap);
			System.out.println("================================" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}