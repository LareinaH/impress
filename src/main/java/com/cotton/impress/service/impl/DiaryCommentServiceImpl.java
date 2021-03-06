package com.cotton.impress.service.impl;

import com.cotton.base.service.impl.BaseServiceImpl;
import com.cotton.impress.mapper.DiaryMapper;
import com.cotton.impress.mapper.DiaryRecordMapper;
import com.cotton.impress.model.Diary;
import com.cotton.impress.model.DiaryComment;
import com.cotton.impress.model.DiaryRecord;
import com.cotton.impress.service.DiaryCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class DiaryCommentServiceImpl extends BaseServiceImpl<DiaryComment> implements DiaryCommentService {

    @Autowired
    private DiaryRecordMapper diaryRecordMapper;
    @Autowired
    private DiaryMapper diaryMapper;

    @Override
    public boolean addComment(DiaryComment diaryComment) {

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
        }else {
            diaryRecord.setSelector("diary");
        }
        diaryRecord.setCategory("comment");
        diaryRecord.setStatus("normal");
        diaryRecord.setCreatedAt(new Date());

        diaryRecordMapper.insert(diaryRecord);

        if(diaryComment.getIsWhisper() == 0) {

            //3.1 日记评论数+1 悄悄话日记评论数不增加
            diary.setCommentCount(diary.getCommentCount() + 1);
        }
        //3.2 日记每增加一条评论，权重+5
        diary.setWeight(diary.getWeight()+5);
        diaryMapper.updateByPrimaryKeySelective(diary);

        //4 如果是引用父评论 该父评论被评论数+1
        if(diaryComment.getParentId() != null){
            DiaryComment parentComment = getById(diaryComment.getParentId());
            if(parentComment!=null){
                parentComment.setCommentCount(parentComment.getCommentCount()+1);
                update(parentComment);
            }
        }
        return true;
    }
}
