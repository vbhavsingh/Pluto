package com.analyzer.restful.err;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.log.analyzer.commons.err.CommonErrorModel;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "no users present in request")
public class CharonRequestException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1678226631458358277L;
	private CommonErrorModel errors;

	public CharonRequestException(String message, CommonErrorModel errors) {
		super(message);
		this.errors = errors;
	}
	
	public CharonRequestException(CommonErrorModel errors) {
		super();
		this.errors = errors;
	}
	
	public CommonErrorModel getErrors() {
		return errors;
	}

}
