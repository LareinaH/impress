package com.conton.impress.service;

import com.conton.base.service.BaseService;
import com.conton.impress.model.MemberFriend;

/**
 * Created by Administrator on 2017-03-16.
 */
public interface MemberFriendService extends BaseService<MemberFriend> {

    public boolean addFriend(long memberId, long friendMemberId);

}
