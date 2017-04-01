package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.mapper.DiaryMapper;
import com.conton.impress.mapper.DiaryRecordMapper;
import com.conton.impress.model.Diary;
import com.conton.impress.model.DiaryComment;
import com.conton.impress.model.DiaryRecord;
import com.conton.impress.service.DiaryCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiaryCommentServiceImpl extends BaseServiceImpl<DiaryComment> implements DiaryCommentService {

    @Autowired
    private DiaryRecordMapper diaryRecordMapper;
    @Autowired
    private DiaryMapper diaryMapper;

    @Override
    public boolean addCommentService(DiaryComment diaryComment) {

        Diary diary = diaryMapper.selectByPrimaryKey(diaryComment.getDiaryId());

        if(diary == null){
            return false;
        }

        //1 添加一条评论
        insert(diaryComment);

        //2 添加评论记录
        DiaryRecord diaryRecord = new DiaryRecord();
        diaryRecord.setMemberId(diaryComment.getCommentUserId());
        diaryRecord.setDiaryId(diaryComment.getDiaryId());
        if(diaryComment.getParentId() != null){
            diaryRecord.setSelector("comment");
            diaryRecord.setCommentId(diaryComment.getParentId());
        }
        diaryRecord.setCategory("comment");
        diaryRecord.setStatus("normal");

        diaryRecordMapper.insert(diaryRecord);

        //3 日记评论数+1
        diary.setCommentCount(diary.getCommentCount()+1);
        diaryMapper.updateByPrimaryKeySelective(diary);

        //4 如果是引用父评论 该父评论被评论数+1
        if(diaryComment.getParentId() != null){
            DiaryComment parentComment = getById(diaryComment.getParentId());
            if(parentComment!=null){
                diaryComment.setCommentCount(diaryComment.getCommentCount()+1);
            }
        }
        return true;
    }
}
