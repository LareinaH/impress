package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.model.Message;
import com.conton.impress.model.VO.MessageVO;
import com.conton.impress.service.MessageService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService{

    @Override
    public PageInfo<MessageVO> query(int pageNum, int pageSize, Map<String, Object> map) {
        return null;
    }
}
