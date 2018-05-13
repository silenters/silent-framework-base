package com.silent.framework.base.validation.validator;

import java.util.Collection;
import java.util.Iterator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.silent.framework.base.validation.Validator;
import com.silent.framework.base.validation.annotation.ValidateCollection;

/**
 * 列表不能为空验证处理器，若列表中为实体类，则实体类中标签也将级联判断
 * @author TanJin
 * @date 2018年3月8日
 */
@Component
public class ValidateCollectionValidator implements ConstraintValidator<ValidateCollection, Object> {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private Validator validator;
	
	@Override
	public void initialize(ValidateCollection constraintAnnotation) {
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean isValid(Object object, ConstraintValidatorContext context) {
		if(null == object) {
			return false;
		}
		if(object instanceof Collection){
			if(null == ((Collection) object) || ((Collection) object).isEmpty() || ((Collection) object).size() <= 0) {
				return false;
			}
			try {
				Iterator<Object> iterator = ((Collection) object).iterator();
				while (iterator.hasNext()) {
					validator.validate((Object) iterator.next());
				}
			} catch (Exception e) {
				logger.error("", e);
				return false;
			}
		}
		return true;
	}

}
