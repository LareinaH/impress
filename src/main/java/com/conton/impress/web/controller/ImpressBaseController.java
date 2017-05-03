package com.conton.impress.web.controller;

import com.conton.base.common.RestResponse;
import com.conton.base.controller.BaseController;
import com.conton.impress.model.DiaryRecord;
import com.conton.impress.model.MemberFriend;
import com.conton.impress.model.VO.DiaryCommentVO;
import com.conton.impress.model.VO.DiaryExVO;
import com.conton.impress.model.VO.DiaryVO;
import com.conton.impress.service.DiaryRecordService;
import com.conton.impress.service.JPushService;
import com.conton.impress.service.MemberFriendService;
import com.conton.impress.service.MessageService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 业务基类
 */
public class ImpressBaseController extends BaseController {

    @Autowired
    public MessageService messageService;

    @Autowired
    private MemberFriendService memberFriendService;
    @Autowired
    private DiaryRecordService diaryRecordService;

    public void buildDiaryExVOInfo(Long currentMemberId, DiaryExVO diaryExVO) {

        //查看是否是好友日记
        MemberFriend memberFriendModel = new MemberFriend();
        memberFriendModel.setMemberId(currentMemberId);
        memberFriendModel.setFriendMemberId(diaryExVO.getMemberId());

        List<MemberFriend> memberFriendList = memberFriendService.queryList(memberFriendModel);

        if (memberFriendList != null && !memberFriendList.isEmpty()) {
            diaryExVO.setbFriendDiary(true);
        } else {
            diaryExVO.setbFriendDiary(false);
        }

        //查看是否阅读/点赞/踩
        DiaryRecord diaryRecordModel = new DiaryRecord();
        diaryRecordModel.setDiaryId(diaryExVO.getId());
        diaryRecordModel.setMemberId(currentMemberId);
        diaryRecordModel.setSelector("diary");
        diaryRecordModel.setStatus("normal");
        List<DiaryRecord> diaryRecordList = diaryRecordService.queryList(diaryRecordModel);

        if (diaryRecordList != null) {
            for (DiaryRecord d : diaryRecordList) {
                if (d.getCategory().equals("up")) {
                    diaryExVO.setUpStatus(true);

                } else if (d.getCategory().equals("down")) {
                    diaryExVO.setDownStatus(true);

                } else if (d.getCategory().equals("browse")) {
                    diaryExVO.setReadStatus(true);
                }
            }
        }

    }

    public void buildCommentVOInfo(Long currentMemberId, DiaryCommentVO diaryCommentVO) {


        //查看是否阅读/点赞/踩
        DiaryRecord diaryRecordModel = new DiaryRecord();
        diaryRecordModel.setDiaryId(diaryCommentVO.getDiaryId());
        diaryRecordModel.setMemberId(currentMemberId);
        diaryRecordModel.setSelector("comment");
        diaryRecordModel.setStatus("normal");
        List<DiaryRecord> diaryRecordList = diaryRecordService.queryList(diaryRecordModel);

        if (diaryRecordList != null) {
            for (DiaryRecord d : diaryRecordList) {
                if (d.getCategory().equals("up")) {
                    diaryCommentVO.setUpStatus(true);

                } else if (d.getCategory().equals("down")) {
                    diaryCommentVO.setDownStatus(true);

                } else if (d.getCategory().equals("browse")) {
                    diaryCommentVO.setReadStatus(true);
                }
            }
        }

    }
}
