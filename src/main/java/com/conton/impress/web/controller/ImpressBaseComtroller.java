package com.conton.impress.web.controller;

import com.conton.base.controller.BaseController;
import com.conton.impress.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 业务基类
 */
public class ImpressBaseComtroller extends BaseController{

    @Autowired
    public MessageService messageService;


}
