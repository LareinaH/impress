package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.model.MemberFriend;
import com.conton.impress.service.MemberFriendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberFriendServiceImpl extends BaseServiceImpl<MemberFriend> implements MemberFriendService{
}
