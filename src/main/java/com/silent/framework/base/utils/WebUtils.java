package com.silent.framework.base.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

/**
 * Web 工具类
 */
public class WebUtils {
	
	/**
	 * 获取请求页面路径
	 * @param HttpServletRequest
	 * @return
	 */
	public static String getRequestPage(HttpServletRequest request) {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		int index = requestUri.indexOf(contextPath);
		String requestPage = requestUri.substring(index + contextPath.length() + 1);
		if("/".equals(requestPage.substring(0, 1))) {
			requestPage = requestPage.substring(1, requestPage.length());
		}
		return requestPage;
	}
	
	/**
	 * 获取远程的IP
	 * @param request
	 * @return
	 */
	public static String getRemoteAddress(HttpServletRequest request) {
		if (StringUtils.isNotEmpty(request.getHeader(WebContants.X_DEBUG_IP))) {
			return request.getHeader(WebContants.X_DEBUG_IP);
		} else {
			String remoteAddress = request.getHeader(WebContants.X_FORWARDED_FOR);
			if(StringUtils.isNotEmpty(remoteAddress)) {
				String[] array = remoteAddress.split(",");
				return array.length>0?array[0]:remoteAddress;
			}
			remoteAddress = request.getHeader(WebContants.X_REAL_IP);
			if (StringUtils.isNotEmpty(remoteAddress)) {
				return remoteAddress;
			}
			return request.getRemoteAddr();
		}
	}
    
	/**
	 * 判断是否是Ajax请求
	 * @param request
	 * @return
	 */
	public static boolean isAjax(HttpServletRequest request) {
		return request.getHeader("X-Requested-With") != null;
	}
	
	/**
	 * 读取字节数组
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static byte[] readByte(HttpServletRequest request) throws IOException {
		byte[] data = new byte[1024];
		int len = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream input = request.getInputStream();
		while ((len = input.read(data)) > 0) {
			baos.write(data, 0, len);
		}
		data = baos.toByteArray();
		baos.close();
		return data;
	}
	
	/** 校验IP地址是否合法 
     * @param ipAddr IP地址
     * @return 
     */  
	public static boolean isIpValid(String ipAddr) {
		if(null == ipAddr || "".equals(ipAddr)) {
			return false;
		}
		String[] ipStr = new String[4];
		int[] ipb = new int[4];
		StringTokenizer tokenizer = new StringTokenizer(ipAddr, ".");
		int len = tokenizer.countTokens();

		if (len != 4) {
			return false;
		}
		try {
			int i = 0;
			while (tokenizer.hasMoreTokens()) {
				ipStr[i] = tokenizer.nextToken();
				ipb[i] = (new Integer(ipStr[i])).intValue();

				if (ipb[i] < 0 || ipb[i] > 255) {
					return false;
				}
				i++;
			}
			if (ipb[0] > 0) {
				return true;
			}
		} catch (Exception e) {
			
		}
		return false;
	}
}
