package com.silent.framework.base.bean;

/**
 * API协议响应数据
 * @author TanJin
 * @date 2017年8月25日
 */
public class ResponseBean<T extends Object> extends ResponseBaseBean {
	
	private T data;
	
	public void buildSuccess(T data) {
		this.data = data;
		buildSuccess();
	}
	
	public void buildSuccess(T data, String msg) {
		this.data = data;
		buildSuccess(msg);
	}
	
	public void buildError(T data, String msg) {
		this.data = data;
		buildError(msg);
	}
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	@Override
	public String toString() {
		return "ResponseBean [data=" + data + ", code=" + getCode() + ", msg=" + getMsg() + "]";
	}
	
}
