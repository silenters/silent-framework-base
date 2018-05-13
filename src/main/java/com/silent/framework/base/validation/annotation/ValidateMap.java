package com.silent.framework.base.validation.annotation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.silent.framework.base.validation.validator.ValidateMapValidator;

/**
 * Map数据验证，包括验证Map是否为空，列表中数据验证是否合格
 * @author TanJin
 * @date 2018年3月8日
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented

@Constraint(validatedBy = ValidateMapValidator.class)
public @interface ValidateMap {

	String message() default "信息格式有误";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
