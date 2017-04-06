package com.conton.impress.service;

import com.conton.base.service.BaseService;
import com.conton.impress.model.Message;
import com.conton.impress.model.VO.MessageVO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface MessageService extends BaseService<Message> {

    PageInfo<MessageVO> query(int pageNum, int pageSize,Map<String,Object> map);

}
