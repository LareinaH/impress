package com.cotton.base.service;

public enum CodeMessage {
	NUMBER_GT_ZERO("number_gt_0","数量需要大于零"),
	ID_IS_NULL("id_is_null","编号不能为空");

	private String code;
	private String msg;
	private CodeMessage(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	

}
