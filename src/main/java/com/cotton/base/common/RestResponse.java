package com.cotton.base.common;

import java.io.Serializable;

public class RestResponse<T> implements Serializable {
    public static final String OK = "ok";
    public static final String LoginTimeout = "LoginTimeout";
    private static final long serialVersionUID = 4843066638830850455L;

    private String code;
    private String message;
    private T data;

    public RestResponse() {
    }

    public RestResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public RestResponse(T data) {
        this.code = OK;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RestResponse [code=" + code + ", message="
                + message + ", data=" + data + "]";
    }

}
