package com.silent.framework.base.utils;

import java.security.SecureRandom;
import java.util.Random;

/**
 * 用户操作工具类
 * @author TanJin
 * @date 2018年1月3日
 */
public class UserUtils {
	
	/**
	 * 使用用户名设置默认密码
	 * @param 
	 * @return
	 */
	public static String createDefaultPassword(String username, String salt) {
		return encodePassword(username, salt);
	}

	/**
	 * 加密用户密码
	 * @param password
	 * @param salt
	 * @return
	 */
	public static String encodePassword(String password, String salt){
		return MD5.md5(password + "@nebulaera@" + salt);
	}
	
	/**
	 * 生成密码盐
	 * @param 
	 * @return
	 */
	public static String createRandSalt(){
		String all = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		char[] chars = all.toCharArray();
		Random RANDOM = new SecureRandom();
		StringBuilder str = new StringBuilder();
		for (int i=0;i<6;i++){
		  int pos = RANDOM.nextInt(chars.length);
		  str.append(chars[pos]);
		}
		return str.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(encodePassword("mimoAdmin", "jmrVDz"));
	}
}
