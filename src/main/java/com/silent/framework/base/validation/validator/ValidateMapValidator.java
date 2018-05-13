package com.silent.framework.base.validation.validator;

import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.silent.framework.base.validation.Validator;
import com.silent.framework.base.validation.annotation.ValidateMap;

/**
 * 列表不能为空验证处理器，若列表中为实体类，则实体类中标签也将级联判断
 * @author TanJin
 * @date 2018年3月8日
 */
@Component
public class ValidateMapValidator implements ConstraintValidator<ValidateMap, Object> {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private Validator validator;
	
	@Override
	public void initialize(ValidateMap constraintAnnotation) {
	}
	
	@SuppressWarnings({ "rawtypes"})
	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		if(null == object) {
			return false;
		}
		if(object instanceof Map){
			if(null == ((Map) object) || ((Map) object).isEmpty() || ((Map) object).size() <= 0) {
				return false;
			}
			try {
				for(Object key : ((Map) object).keySet()) {
					validator.validate(((Map) object).get(key));
				}
			} catch (Exception e) {
				logger.error("", e);
				return false;
			}
		}
		return true;
	}

}
