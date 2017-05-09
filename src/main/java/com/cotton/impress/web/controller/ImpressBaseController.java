package com.cotton.impress.web.controller;

import com.cotton.base.controller.BaseController;
import com.cotton.impress.model.DiaryRecord;
import com.cotton.impress.model.MemberFriend;
import com.cotton.impress.model.VO.DiaryCommentVO;
import com.cotton.impress.model.VO.DiaryExVO;
import com.cotton.impress.service.DiaryRecordService;
import com.cotton.impress.service.MemberFriendService;
import com.cotton.impress.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;

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

        //自己的日记也当做是好友日记处理
        if (currentMemberId == diaryExVO.getMemberId()) {
            diaryExVO.setbFriendDiary(true);
        } else {
            MemberFriend memberFriendModel = new MemberFriend();
            memberFriendModel.setMemberId(currentMemberId);
            memberFriendModel.setFriendMemberId(diaryExVO.getMemberId());

            List<MemberFriend> memberFriendList = memberFriendService.queryList(memberFriendModel);

            if (memberFriendList != null && !memberFriendList.isEmpty()) {
                diaryExVO.setbFriendDiary(true);
            } else {
                diaryExVO.setbFriendDiary(false);
            }
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
