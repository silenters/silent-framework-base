package com.silent.framework.base.bean;

public class ResponseBaseBean {
	
	private int code;
	private String msg;
	
	public boolean success() {
		return this.code == BaseCode.SUCCESS;
	}
	
	public boolean error() {
		return this.code != BaseCode.SUCCESS;
	}
	
	public void buildSuccess() {
		this.code = BaseCode.SUCCESS;
	}
	
	public void buildSuccess(String msg) {
		buildSuccess();
		this.msg = msg;
	}
	
	public void buildError() {
		this.code = BaseCode.ERROR;
	}
	
	public void buildError(String msg) {
		buildError();
		this.msg = msg;
	}
	
	public void buildError(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public void buildCode(boolean isSuccess) {
		this.code = isSuccess ? BaseCode.SUCCESS : BaseCode.ERROR;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return "ResponseBaseBean [code=" + code + ", msg=" + msg + "]";
	}
	
}
