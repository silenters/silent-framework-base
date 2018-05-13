package com.silent.framework.base.validation;

import java.util.List;

/**
 * 字段校验异常
 * @author TanJin
 * @date 2018年3月15日
 */
public class FieldValidException extends Exception {
	private static final long serialVersionUID = 1L;

	private List<FieldError> errorList;
	
	public FieldValidException(List<FieldError> errorList) {
		super("object field validation error...");
		this.errorList = errorList;
	}
	
	public List<FieldError> getFieldErrorList() {
		return this.errorList;
	}
	
}
