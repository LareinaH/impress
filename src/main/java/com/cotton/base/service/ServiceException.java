package com.cotton.base.service;

public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 7199992842398112421L;
	private final String code;
	
	public ServiceException(CodeMessage codeMsg) {
		this(codeMsg.getCode(), codeMsg.getMsg(), null);
	}

	public ServiceException(String code, String message) {
		this(code, message, null);
	}

	public ServiceException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
