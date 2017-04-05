package com.conton.impress.web.controller;

import com.conton.base.common.RestResponse;
import com.conton.impress.model.Member;
import com.conton.impress.model.Message;
import com.conton.impress.service.MessageService;
import com.conton.impress.web.PermissionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017-03-23.
 */
@Controller
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;


    /**
     * 获取当前用户未读系统消息总个数(我的消息页面，系统消息的个数)
     * @return
     */
    @RequestMapping(value = "/messageCount")
    @ResponseBody
    public RestResponse<Map<String,Object>> messageCount() {
        RestResponse<Map<String,Object>> restResponse = new RestResponse<Map<String,Object>>();
        Member member = PermissionContext.getMember();

        if(member!=null){
            Message model = new Message();
            model.setToMemberId(member.getId());
            model.setProcessStatus("unprocessed");
            model.setStatus("normal");
            long count = messageService.count(model);
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("count",count);
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(map);

        }else {
            restResponse.setCode("error");
            restResponse.setMessage("未找到当前用户！");
        }

        return restResponse;
    }


    /**
     * 获取当前用户未读系统消息(系统消息页面)
     * @return
     */
    @RequestMapping(value = "/messageList")
    @ResponseBody
    public RestResponse<Map<String,Object>> messageList() {
        RestResponse<Map<String,Object>> restResponse = new RestResponse<Map<String,Object>>();
        Member member = PermissionContext.getMember();

        if(member!=null){
            Map<String,Object> map = new HashMap<String, Object>();
            //TODO:获取好友消息列表


            //获取被赞消息个数
            Message model = new Message();
            model.setToMemberId(member.getId());
            model.setCategory("up");
            model.setProcessStatus("unprocessed");
            model.setStatus("normal");
            long upCount = messageService.count(model);
            map.put("upCount",upCount);

            //获取被评论消息个数
            model.setCategory("comment");
            long commentCount = messageService.count(model);
            map.put("commentCount",commentCount);

            //返回结果
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(map);

        }else {
            restResponse.setCode("error");
            restResponse.setMessage("未找到当前用户！");
        }

        return restResponse;
    }

    /**
     * 读取消息
     * @param messageId
     * @return
     */
    @RequestMapping(value = "/readMessage")
    @ResponseBody
    public RestResponse<Void> readMessage(@RequestParam(required = true) long messageId) {
        RestResponse<Void> restResponse = new RestResponse<Void>();
        Message message = messageService.getById(messageId);

        if (message != null) {
            if (message.getProcessStatus().equals("unprocessed")) {

                message.setProcessStatus("processed");
                messageService.update(message);
            }
            restResponse.setCode(RestResponse.OK);


        } else {
            restResponse.setCode("error");
            restResponse.setMessage("该消息已读！");

        }
        return restResponse;
    }
}
