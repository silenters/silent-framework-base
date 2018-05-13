package com.silent.framework.base.validation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.silent.framework.base.utils.ClassUtils;

/**
 * 自定义的手动校验处理类，在原内置手动校验器基础上进行封装
 * @author TanJin
 * @date 2018年3月9日
 */
@Component
public class Validator {

	@Autowired
	private javax.validation.Validator validator;
	
	/**
	 * 验证实体类，若字段验证失败，则抛出 FieldValidException 异常
	 * @param object
	 * @return
	 */
	public void validate(Object object) throws Exception {
		List<FieldError> errorList = this.validateObject(object);
		if(null != errorList && errorList.size() > 0) {
			throw new FieldValidException(errorList);
		}
	}
	
	/**
	 * 字段验证，返回所有验证失败的字段验证结果信息
	 * @param object
	 * @return
	 */
	private List<FieldError> validateObject(Object object) throws Exception {
		List<FieldError> errorList = null;
		Set<ConstraintViolation<Object>> set = validator.validate(object);
		if(null != set && set.size() > 0) {
			errorList = new ArrayList<FieldError>();
			Iterator<ConstraintViolation<Object>> iterator = set.iterator();
			while(iterator.hasNext()) {
				ConstraintViolation<Object> constraintViolation = (ConstraintViolation<Object>) iterator.next();
				Field field = ClassUtils.getField(constraintViolation.getRootBeanClass(), constraintViolation.getPropertyPath().toString());
				field.setAccessible(true);
				Object fieldObject = field.get(object);
				// 基础类型
				if(ClassUtils.isBaseField(field)) {
					errorList.add(this.getFieldError(constraintViolation, fieldObject));
				}
				// Collection
				else if (field.getType() == List.class || field.getType() == Set.class) {
					this.validCollection(field, fieldObject, constraintViolation, errorList);
				}
				// Map
				else if (field.getType() == Map.class) {
					this.validMap(field, fieldObject, constraintViolation, errorList);
				}
				// Class
				else {
					fieldObject = null == fieldObject ? field.getType().newInstance() : fieldObject;
					this.addErrorList(errorList, this.validateObject(fieldObject));
				}
			}
		}
		return errorList;
	}
	
	/**
	 * 集合验证，若集合泛型为基础类型，则返回集合验证失败信息。若集合泛型不为基础类型，则递归验证
	 * @param field 字段信息
	 * @param fieldValue 字段值
	 * @param constraintViolation Spring返回的字段验证信息
	 * @param errorList 字段验证信息列表
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void validCollection(Field field, Object fieldValue, ConstraintViolation<Object> constraintViolation, List<FieldError> errorList) throws Exception {
		// 获取集合泛型Class
		Class<?> genericityClass = ClassUtils.getCollectionGenericityClass(field);
		// 若集合泛型为基础字段则直接返回
		if(ClassUtils.isBaseField(genericityClass)) {
			errorList.add(this.getFieldError(constraintViolation, fieldValue));
		} else if (null == fieldValue || ((Collection) fieldValue).size() <= 0) {
			this.addErrorList(errorList, this.validateObject(genericityClass.newInstance()));
		} else {
			Iterator<Object> fieldIterator = ((Collection) fieldValue).iterator();
			while (fieldIterator.hasNext()) {
				Object o = (Object) fieldIterator.next();
				this.addErrorList(errorList, this.validateObject(o));
			}
		}
	}
	
	/**
	 * Map验证，若集合泛型为基础类型，则返回集合验证失败信息。若集合泛型不为基础类型，则递归验证
	 * @param field 字段信息
	 * @param fieldValue 字段值
	 * @param constraintViolation Spring返回的字段验证信息
	 * @param errorList 字段验证信息列表
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private void validMap(Field field, Object fieldValue, ConstraintViolation<Object> constraintViolation, List<FieldError> errorList) throws Exception {
		// 获取Map泛型Class
		Class<?> genericityClass = ClassUtils.getMapGenericityClass(field, 1);
		// 若集合泛型为基础字段则直接返回
		if(ClassUtils.isBaseField(genericityClass)) {
			errorList.add(this.getFieldError(constraintViolation, fieldValue));
		} else if (null == fieldValue || ((Map) fieldValue).size() <= 0) {
			this.addErrorList(errorList, this.validateObject(genericityClass.newInstance()));
		} else {
			for(Object key : ((Map) fieldValue).keySet()) {
				Object o = ((Map) fieldValue).get(key);
				this.addErrorList(errorList, this.validateObject(o));
			}
		}
	}
	
	/**
	 * 获取校验失败字段错误信息
	 * @param constraintViolation Spring返回的验证实体
	 * @param fieldValue 字段值
	 * @return
	 */
	private FieldError getFieldError(ConstraintViolation<Object> constraintViolation, Object fieldValue) {
		FieldError fieldError = new FieldError();
		fieldError.setField(constraintViolation.getPropertyPath().toString());
		fieldError.setMessage(constraintViolation.getMessage());
		fieldError.setValue(fieldValue);
		return fieldError;
		
	}
	
	private void addErrorList(List<FieldError> errorList, List<FieldError> fieldList) {
		if(null != fieldList && fieldList.size() > 0) {
			errorList.addAll(fieldList);
		}
	}
}
