package com.cotton.impress.service.impl;

import com.cotton.base.common.RestResponse;
import com.cotton.base.service.impl.BaseServiceImpl;
import com.cotton.impress.model.MemberFriend;
import com.cotton.impress.service.MemberFriendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class MemberFriendServiceImpl extends BaseServiceImpl<MemberFriend> implements MemberFriendService{


    @Override
    public boolean addFriend(long memberId, long friendMemberId) {

        MemberFriend memberFriend1 = new MemberFriend();
        memberFriend1.setMemberId(memberId);
        memberFriend1.setFriendMemberId(friendMemberId);
        memberFriend1.setStatus("normal");

        //如果存在好友关系不再添加
        if(queryList(memberFriend1).size() > 0){
            return true;
        }

        //添加好友
        if(insert(memberFriend1)) {
            MemberFriend memberFriend2 = new MemberFriend();
            memberFriend2.setMemberId(friendMemberId);
            memberFriend2.setFriendMemberId(memberId);
            memberFriend2.setStatus("normal");
            if(insert(memberFriend2)){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean deleteFriend(long memberId, long friendMemberId) {

        MemberFriend model = new MemberFriend();
        model.setMemberId(memberId);
        model.setFriendMemberId(friendMemberId);
        model.setStatus("normal");

        List<MemberFriend> memberFriendList = queryList(model);

        if (memberFriendList != null) {

            for (MemberFriend memberFriend : memberFriendList) {
                memberFriend.setStatus("delete");
                memberFriend.setUpdateAt(new Date());
                update(memberFriend);
            }
        }

        model.setMemberId(friendMemberId);
        model.setFriendMemberId(memberId);

        memberFriendList = queryList(model);

        if (memberFriendList != null) {

            for (MemberFriend memberFriend : memberFriendList) {
                memberFriend.setStatus("delete");
                memberFriend.setUpdateAt(new Date());
                update(memberFriend);
            }
        }
        return true;
    }
}
