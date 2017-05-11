package com.cotton.impress.service;

import com.cotton.base.service.BaseService;
import com.cotton.impress.model.MemberFriend;

/**
 * Created by Administrator on 2017-03-16.
 */
public interface MemberFriendService extends BaseService<MemberFriend> {

    boolean addFriend(long memberId, long friendMemberId);

    boolean deleteFriend(long memberId, long friendMemberId);
}
