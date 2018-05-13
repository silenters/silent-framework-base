package com.silent.framework.base.validation.validator;

import java.util.Collection;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.silent.framework.base.validation.annotation.NotEmptyObject;

/**
 * 对象不能为空验证处理器
 * @author TanJin
 * @date 2018年3月8日
 */
public class NotEmptyObjectValidator implements ConstraintValidator<NotEmptyObject, Object> {

	@Override
	public void initialize(NotEmptyObject constraintAnnotation) {
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if(value == null){
			return false;
		}
		if(value  instanceof Map){
			return !((Map) value).isEmpty();
		}
		if(value instanceof Collection){
			return !((Collection) value).isEmpty();
		}
		if(value instanceof String){
			return ((String) value).length() > 0;
		}
		return true;
	}

}
