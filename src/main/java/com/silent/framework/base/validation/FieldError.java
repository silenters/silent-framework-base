package com.silent.framework.base.validation;

/**
 * 字段校验错误信息
 * @author TanJin
 * @date 2018年3月15日
 */
public class FieldError {

	private String field;				// 字段名
	private Object value;				// 字段值
	private String message;				// 校验失败提示消息
	
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "FieldError [field=" + field + ", value=" + value + ", message=" + message + "]";
	}
}
