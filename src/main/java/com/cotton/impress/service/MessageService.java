package com.cotton.impress.service;

import com.cotton.base.service.BaseService;
import com.cotton.impress.model.Message;
import com.cotton.impress.model.VO.MessageVO;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface MessageService extends BaseService<Message> {

    PageInfo<MessageVO> query(int pageNum, int pageSize,Map<String,Object> map);

    List<MessageVO> query(Map<String,Object> map);

}
