package com.log.analyzer.commons.err;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CommonErrorModel {

	private String errorCode;

	private int httpErrorCode;

	private String reason;

	private String message;

	private String fullTrace;

	public CommonErrorModel(Exception e) {
		setErrors(e);
	}

	public CommonErrorModel(int httpErrorCode, Exception e) {
		this.httpErrorCode = httpErrorCode;
		setErrors(e);
	}

	public CommonErrorModel(String message) {
		this.message = message;
	}

	public CommonErrorModel(int i) {
		this.httpErrorCode = i;
	}

	public CommonErrorModel() {

	}

	private void setErrors(Exception e) {
		if (e != null && e.getCause() != null) {
			this.reason = e.getCause().getMessage();
		} else {
			this.reason = e.toString();
		}
		if (e != null) {
			this.message = e.getMessage();
		}
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		this.fullTrace = sw.toString();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public int getHttpErrorCode() {
		return httpErrorCode;
	}

	public void setHttpErrorCode(int httpErrorCode) {
		this.httpErrorCode = httpErrorCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFullTrace() {
		return fullTrace;
	}

	public void setFullTrace(String fullTrace) {
		this.fullTrace = fullTrace;
	}

	@Override
	public String toString() {
		return "CommonErrorModel [errorCode=" + errorCode + ", httpErrorCode=" + httpErrorCode + ", reason=" + reason + ", message=" + message + ", fullTrace=" + fullTrace + "]";
	}
	
	

}
