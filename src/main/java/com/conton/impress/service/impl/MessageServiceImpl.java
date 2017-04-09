package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.mapper.MessageMapper;
import com.conton.impress.model.Message;
import com.conton.impress.model.VO.MessageVO;
import com.conton.impress.service.MessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService{

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public PageInfo<MessageVO> query(int pageNum, int pageSize, Map<String, Object> map) {
        if (pageSize > 0) {
            PageHelper.startPage(pageNum, pageSize);
        }
        return new PageInfo<MessageVO>(messageMapper.query(map));
    }

    @Override
    public List<MessageVO> query(Map<String, Object> map) {

        return messageMapper.query(map);
    }
}
