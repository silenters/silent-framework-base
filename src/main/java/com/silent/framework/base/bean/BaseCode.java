package com.silent.framework.base.bean;

/**
 * Code值含义
 * @author TanJin
 * @date 2016-3-22
 */
public class BaseCode {

	/**
	 * 执行错误
	 */
	public static final int ERROR = 1;
	
	/**
	 * 执行成功
	 */
	public static final int SUCCESS = 0;

	/**
	 * 登录超时，异步请求登录失败
	 */
	public static final int LOGIN_TIMEOUT = -5;
	
	/**
	 * 请求参数验证失败
	 */
	public static final int PARAM_VALID_ERROR = -2;
	
	/**
	 * Token过期失效
	 */
	public static final int TOKEN_EXPIRE = -3;
	
	/**
	 * 没有访问权限
	 */
	public static final int AUTH_ERROR = -4;
}
