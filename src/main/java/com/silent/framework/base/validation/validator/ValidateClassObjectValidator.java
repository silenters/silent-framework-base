package com.silent.framework.base.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.silent.framework.base.validation.Validator;
import com.silent.framework.base.validation.annotation.ValidateClassObject;

/**
 * 对象不能为空验证处理器
 * @author TanJin
 * @date 2018年3月8日
 */
@Component
public class ValidateClassObjectValidator implements ConstraintValidator<ValidateClassObject, Object> {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private Validator validator;
	
	@Override
	public void initialize(ValidateClassObject constraintAnnotation) {
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		if(value == null){
			return false;
		}
		try {
			validator.validate(value);
		} catch (Exception e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

}
