package com.conton.base.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.conton.base.common.RestResponse;
import com.conton.base.service.ServiceException;

public class BaseController {
	private Logger logger = LoggerFactory.getLogger(getClass());

	@ExceptionHandler
	@ResponseBody
	public RestResponse<Void> handelException(Exception ex) {
		logger.error("exception", ex);

		if (ex instanceof ServiceException) {
			ServiceException serviceException = (ServiceException) ex;
			return new RestResponse<Void>(serviceException.getCode(),
					serviceException.getMessage());
		} else {
			// TODO 不暴露服务器错误消息
			return new RestResponse<Void>("exception", "服务器出小差了");
		}
	}

/*	protected Map<String, Object> parseJson(String jsonString) {
		GsonBuilder gb = new GsonBuilder();
		Gson g = gb.create();
		Map<String, Object> map = g.fromJson(jsonString,
				new TypeToken<Map<String, String>>() {
				}.getType());
		return map;
	}*/
}
