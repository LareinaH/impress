package com.conton.impress.web.controller;

import com.conton.base.common.RestResponse;
import com.conton.base.controller.BaseController;
import com.conton.impress.service.JPushService;
import com.conton.impress.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 业务基类
 */
public class ImpressBaseComtroller extends BaseController{

    @Autowired
    public MessageService messageService;


}
