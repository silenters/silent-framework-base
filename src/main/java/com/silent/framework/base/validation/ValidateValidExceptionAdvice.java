package com.silent.framework.base.validation;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.silent.framework.base.bean.BaseCode;
import com.silent.framework.base.bean.ResponseBean;

/**
 * 参数验证异常处理
 * @author TanJin
 * @date 2018年3月8日
 */
@ControllerAdvice
public class ValidateValidExceptionAdvice {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 验证失败异常统一处理
	 * @param 
	 * @return
	 */
	@ExceptionHandler(value=MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseBean<FieldError> validExceptionHandler(MethodArgumentNotValidException e, WebRequest request, HttpServletResponse response) throws IOException {
		FieldError fieldError = new FieldError();
		fieldError.setField(e.getBindingResult().getFieldErrors().get(0).getField());
		fieldError.setMessage(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
		
		ResponseBean<FieldError> responseBean = new ResponseBean<FieldError>();
		responseBean.setCode(BaseCode.PARAM_VALID_ERROR);
		responseBean.setData(fieldError);
        return responseBean;
	}
	
	/**
	 * 验证失败异常统一处理
	 * @param 
	 * @return
	 */
	@ExceptionHandler(value=BindException.class)
	@ResponseBody
    public Object validMethodArgumentNotValidExceptionHandler(BindException e, WebRequest request, HttpServletResponse response) throws IOException {
		FieldError fieldError = new FieldError();
		fieldError.setField(e.getBindingResult().getFieldErrors().get(0).getField());
		fieldError.setMessage(e.getBindingResult().getFieldErrors().get(0).getDefaultMessage());
		
		ResponseBean<FieldError> responseBean = new ResponseBean<FieldError>();
		responseBean.setCode(BaseCode.PARAM_VALID_ERROR);
		responseBean.setData(fieldError);
        return responseBean;
    }
	
	/**
	 * 自定义字段验证失败异常统一处理
	 * @param 
	 * @return
	 */
	@ExceptionHandler(value=FieldValidException.class)
	@ResponseBody
	public Object validObjectExceptionHandler(FieldValidException e, WebRequest request, HttpServletResponse response) {
		ResponseBean<FieldError> responseBean = new ResponseBean<FieldError>();
		responseBean.setCode(BaseCode.PARAM_VALID_ERROR);
		responseBean.setData(e.getFieldErrorList().get(0));
        return responseBean;
	}
	
}
