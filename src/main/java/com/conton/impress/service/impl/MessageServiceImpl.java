package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.model.Message;
import com.conton.impress.service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageServiceImpl extends BaseServiceImpl<Message> implements MessageService{
}
