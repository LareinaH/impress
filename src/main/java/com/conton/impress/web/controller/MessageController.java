package com.conton.impress.web.controller;

import com.conton.base.common.RestResponse;
import com.conton.impress.model.Diary;
import com.conton.impress.model.Member;
import com.conton.impress.model.Message;
import com.conton.impress.model.VO.DiaryVO;
import com.conton.impress.model.VO.MemberVO;
import com.conton.impress.model.VO.MessageVO;
import com.conton.impress.service.DiaryService;
import com.conton.impress.service.MessageService;
import com.conton.impress.web.PermissionContext;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-03-23.
 */
@Controller
@RequestMapping("/message")
public class MessageController extends ImpressBaseComtroller {


    @Autowired
    private DiaryService diaryService;


    /**
     * 获取当前用户未读系统消息总个数(我的消息页面，系统消息的个数)
     *
     * @return
     */
    @RequestMapping(value = "/messageCount")
    @ResponseBody
    public RestResponse<Map<String, Object>> messageCount() {
        RestResponse<Map<String, Object>> restResponse = new RestResponse<Map<String, Object>>();
        Member member = PermissionContext.getMember();

        if (member != null) {
            Message model = new Message();
            model.setToMemberId(member.getId());
            model.setProcessStatus("unprocessed");
            model.setStatus("normal");
            long count = messageService.count(model);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("count", count);
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(map);

        } else {
            restResponse.setCode("error");
            restResponse.setMessage("未找到当前用户！");
        }

        return restResponse;
    }


    /**
     * 获取当前用户未读系统消息(系统消息页面)
     *
     * @return
     */
    @RequestMapping(value = "/messageList")
    @ResponseBody
    public RestResponse<Map<String, Object>> messageList() {
        RestResponse<Map<String, Object>> restResponse = new RestResponse<Map<String, Object>>();
        Member member = PermissionContext.getMember();

        if (member != null) {
            Map<String, Object> map = new HashMap<String, Object>();

            Map<String, Object> conditionMap = new HashMap<String, Object>();
            conditionMap.put("toMemberId",member.getId());
            conditionMap.put("category", "friend");
            conditionMap.put("status", "normal");
            conditionMap.put("processStatus", "unprocessed");

            List<MessageVO> friendMessageList = messageService.query(conditionMap);

            if (friendMessageList != null && !friendMessageList.isEmpty()) {
                map.put("friend", friendMessageList);
            }

            //获取被赞消息个数
            Message model = new Message();
            model.setToMemberId(member.getId());
            model.setCategory("up");
            model.setProcessStatus("unprocessed");
            model.setStatus("normal");
            long upCount = messageService.count(model);
            map.put("upCount", upCount);

            //获取被评论消息个数
            model.setCategory("comment");
            long commentCount = messageService.count(model);
            map.put("commentCount", commentCount);

            //返回结果
            restResponse.setCode(RestResponse.OK);
            restResponse.setData(map);

        } else {
            restResponse.setCode("error");
            restResponse.setMessage("未找到当前用户！");
        }

        return restResponse;
    }

    /**
     * 获取消息列表（被赞/被评论）
     *
     * @param type     up-被赞 comment-被评论
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/friendUpDiarys")
    @ResponseBody
    public RestResponse<PageInfo<MessageVO>> friendUpDiarys(@RequestParam(required = true) String type,
                                                            @RequestParam(defaultValue = "1") int pageNum,
                                                            @RequestParam(defaultValue = "20") int pageSize) {
        RestResponse<PageInfo<MessageVO>> restResponse = new RestResponse<PageInfo<MessageVO>>();

        Member member = PermissionContext.getMember();
        if(member != null) {
            Map<String, Object> map = new HashMap<String, Object>();

            if (type.equals("up")) {
                map.put("category", "up");
            } else {
                map.put("category", "comment");
            }
            map.put("status", "normal");
            map.put("processStatus", "unprocessed");
            map.put("toMemberId", member.getId());

            PageInfo<MessageVO> messagePageInfo = messageService.query(pageNum, pageSize, map);

            if (messagePageInfo != null) {

                if (messagePageInfo.getList() != null && !messagePageInfo.getList().isEmpty()) {

                    for (MessageVO messageVO : messagePageInfo.getList()) {
                        Long diaryId = 7L;
                        Diary diary = diaryService.getById(diaryId);
                        messageVO.setDiary(diary);
                    }
                }
                restResponse.setCode(RestResponse.OK);
                restResponse.setData(messagePageInfo);
            } else {
                restResponse.setCode("error");
                restResponse.setMessage("消息列表失败！");
            }
        }else {
            restResponse.setCode("error");
            restResponse.setMessage("读取用户信息失败！");
        }
        return restResponse;
    }

    /**
     * 读取消息
     *
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
