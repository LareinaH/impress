package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.mapper.DiaryCommentMapper;
import com.conton.impress.mapper.DiaryMapper;
import com.conton.impress.model.Diary;
import com.conton.impress.model.DiaryComment;
import com.conton.impress.model.DiaryRecord;
import com.conton.impress.service.DiaryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiaryRecordServiceImpl extends BaseServiceImpl<DiaryRecord> implements DiaryRecordService {

    @Autowired
    private DiaryMapper diaryMapper;
    @Autowired
    private DiaryCommentMapper diaryCommentMapper;

    @Override
    public boolean addDiaryRecord(DiaryRecord diaryRecord) {

        //1 查找是否已经存在
        //坐标缓存
        Double lbsX = diaryRecord.getLbsX();
        Double lbsY = diaryRecord.getLbsY();
        diaryRecord.setLbsX(null);
        diaryRecord.setLbsX(null);

        DiaryRecord diaryRecord1 = selectOne(diaryRecord);

        if (diaryRecord1 == null) {

            //2 插入日记记录
            diaryRecord.setLbsX(lbsX);
            diaryRecord.setLbsY(lbsY);

            insert(diaryRecord);

            //3 更新统计值
            if (diaryRecord.getSelector().equals("diary")) {
                Diary diary = diaryMapper.selectByPrimaryKey(diaryRecord.getDiaryId());
                if(diary != null){
                    if(diaryRecord.getCategory().equals("up")){
                        diary.setUpCount(diary.getUpCount()+1);
                        diary.setWeight(diary.getWeight()+3);

                    }else if(diaryRecord.getCategory().equals("down")){

                        diary.setDownCount(diary.getDownCount()+1);

                    }else if(diaryRecord.getCategory().equals("browse")){

                        diary.setBrowseCount(diary.getBrowseCount()+1);
                        diary.setWeight(diary.getWeight()+1);

                        //如果是管理员日记
                        if(diaryRecord.getDiaryMemberId() == 1){
                            diary.setInfluence(diary.getInfluence()+1);
                        }
                    }

                    diaryMapper.updateByPrimaryKeySelective(diary);
                }

            } else {
                DiaryComment diaryComment = diaryCommentMapper.selectByPrimaryKey(diaryRecord.getDiaryId());
                if(diaryComment != null){
                    if(diaryRecord.getCategory().equals("up")){
                        diaryComment.setUpCount(diaryComment.getUpCount()+1);

                    }else if(diaryRecord.getCategory().equals("down")){

                        diaryComment.setDownCount(diaryComment.getDownCount()+1);

                    }else if(diaryRecord.getCategory().equals("browse")){

                        //评论不处理浏览
                    }
                }

                diaryCommentMapper.updateByPrimaryKeySelective(diaryComment);
            }
        }
        return true;

    }
}
