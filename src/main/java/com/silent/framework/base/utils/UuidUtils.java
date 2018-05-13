package com.silent.framework.base.utils;

import java.util.UUID;

/**
 * UUID工具类
 * @author TanJin
 * @date 2017年8月22日
 */
public class UuidUtils {

	/**
	 * 生成UUID
	 * @param 
	 * @return
	 * @date 2017年8月22日
	 */
	public static String create() {
		return UUID.randomUUID().toString();
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString());
	}
}
