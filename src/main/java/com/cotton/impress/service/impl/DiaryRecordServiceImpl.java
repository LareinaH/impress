package com.cotton.impress.service.impl;

import com.cotton.base.service.impl.BaseServiceImpl;
import com.cotton.impress.mapper.DiaryCommentMapper;
import com.cotton.impress.mapper.DiaryMapper;
import com.cotton.impress.model.Diary;
import com.cotton.impress.model.DiaryComment;
import com.cotton.impress.model.DiaryRecord;
import com.cotton.impress.service.DiaryRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DiaryRecordServiceImpl extends BaseServiceImpl<DiaryRecord> implements DiaryRecordService {

    @Autowired
    private DiaryMapper diaryMapper;
    @Autowired
    private DiaryCommentMapper diaryCommentMapper;

    @Override
    public boolean addDiaryRecord(DiaryRecord diaryRecord) {

        if (diaryRecord.getCategory().equals("browse")) {

            addBrowseRecord(diaryRecord);

        } else {
            addUpOrDownRecord(diaryRecord, diaryRecord.getCategory());

        }
        return true;
    }

    private void addBrowseRecord(DiaryRecord diaryRecord) {

        //1 查找是否已经存在
        //坐标缓存
        Double lbsX = diaryRecord.getLbsX();
        Double lbsY = diaryRecord.getLbsY();
        diaryRecord.setLbsX(null);
        diaryRecord.setLbsY(null);

        List<DiaryRecord> diaryRecordList = queryList(diaryRecord);

        if (diaryRecordList.isEmpty()) {

            //2 插入日记记录
            diaryRecord.setLbsX(lbsX);
            diaryRecord.setLbsY(lbsY);

            insert(diaryRecord);

            //3 更新统计值
            if (diaryRecord.getSelector().equals("diary")) {
                Diary diary = diaryMapper.selectByPrimaryKey(diaryRecord.getDiaryId());
                if (diary != null) {

                    diary.setBrowseCount(diary.getBrowseCount() + 1);
                    diary.setWeight(diary.getWeight() + 1);

                    //如果是管理员日记
                    if (diaryRecord.getDiaryMemberId() == 1) {
                        diary.setInfluence(diary.getInfluence() + 1);
                    }
                }

                diaryMapper.updateByPrimaryKeySelective(diary);
            }
        }
    }


    private void addUpOrDownRecord(DiaryRecord diaryRecord, String type) {

        //坐标缓存
        Double lbsX = diaryRecord.getLbsX();
        Double lbsY = diaryRecord.getLbsY();
        diaryRecord.setLbsX(null);
        diaryRecord.setLbsY(null);

        //查询是否有赞过
        diaryRecord.setCategory("up");
        List<DiaryRecord> diaryRecordUpList = queryList(diaryRecord);

        //查询是否有踩过
        diaryRecord.setCategory("down");
        List<DiaryRecord> diaryRecordDownList = queryList(diaryRecord);


        if (diaryRecordUpList.isEmpty() && diaryRecordDownList.isEmpty()) {  //A 不踩不赞的状态

            //2 插入日记记录
            diaryRecord.setCategory(type);
            //2 插入日记记录
            diaryRecord.setLbsX(lbsX);
            diaryRecord.setLbsY(lbsY);
            insert(diaryRecord);

            //3 更新统计值
            if (diaryRecord.getSelector().equals("diary")) {
                Diary diary = diaryMapper.selectByPrimaryKey(diaryRecord.getDiaryId());
                if (diary != null) {

                    if (type.equals("up")) {

                        diary.setUpCount(diary.getUpCount() + 1);

                        //如果有当天的点赞记录，就不增加权重
                        {
                            Example example = new Example(DiaryRecord.class);
                            Example.Criteria criteria = example.createCriteria();
                            criteria.andEqualTo("memberId", diaryRecord.getMemberId());
                            criteria.andEqualTo("selector", diaryRecord.getSelector());
                            criteria.andEqualTo("diaryId", diaryRecord.getDiaryId());
                            if (diaryRecord.getCommentId() != null) {
                                criteria.andEqualTo("commentId", diaryRecord.getCommentId());
                            }
                            criteria.andEqualTo("category", "up");
                            criteria.andEqualTo("status", "cancel");
                            Calendar calendar = Calendar.getInstance();

                            calendar.setTime(new Date());
                            calendar.set(Calendar.HOUR_OF_DAY, 0);
                            calendar.set(Calendar.MINUTE, 0);
                            calendar.set(Calendar.SECOND, 0);
                            Date start = calendar.getTime();
                            criteria.andGreaterThan("createdAt", start);
                            criteria.andEqualTo("category", "up");
                            List<DiaryRecord> diaryRecordUpTodayList = queryList(example);

                            if (diaryRecordUpTodayList.isEmpty()) {

                                diary.setWeight(diary.getWeight() + 3);
                            }
                        }

                    } else if (type.equals("down")) {
                        diary.setDownCount(diary.getDownCount() + 1);

                    }
                    diaryMapper.updateByPrimaryKeySelective(diary);
                }

            } else {
                DiaryComment diaryComment = diaryCommentMapper.selectByPrimaryKey(diaryRecord.getCommentId());

                if (diaryComment != null) {
                    if (type.equals("up")) {
                        diaryComment.setUpCount(diaryComment.getUpCount() + 1);
                    } else if (type.equals("down")) {
                        diaryComment.setDownCount(diaryComment.getDownCount() + 1);
                    }
                    diaryCommentMapper.updateByPrimaryKeySelective(diaryComment);

                }
            }


        } else if (!diaryRecordUpList.isEmpty()) {    //B 赞过的状态

            if (type.equals("down")) {

                //赞过后踩，将赞清空，更新统计值
                for (DiaryRecord diaryRecord1 : diaryRecordUpList) {
                    diaryRecord1.setStatus("cancel");
                    update(diaryRecord1);
                }

                if (diaryRecord.getSelector().equals("diary")) {
                    Diary diary = diaryMapper.selectByPrimaryKey(diaryRecord.getDiaryId());
                    if (diary != null) {

                        diary.setUpCount((diary.getUpCount() - diaryRecordUpList.size()) > 0 ?
                                (diary.getUpCount() - diaryRecordUpList.size()) : 0);
                        diaryMapper.updateByPrimaryKeySelective(diary);
                    }

                } else {
                    DiaryComment diaryComment = diaryCommentMapper.selectByPrimaryKey(diaryRecord.getCommentId());

                    if (diaryComment != null) {

                        diaryComment.setUpCount((diaryComment.getUpCount() - diaryRecordUpList.size()) > 0 ?
                                (diaryComment.getUpCount() - diaryRecordUpList.size()) : 0);
                        diaryCommentMapper.updateByPrimaryKeySelective(diaryComment);
                    }
                }

            } else if (type.equals("up")) {

                //赞过继续赞不处理
            }


        } else if (!diaryRecordDownList.isEmpty()) {  //C 踩过的状态

            //踩过后 赞取消踩
            if (type.equals("up")) {

                for (DiaryRecord diaryRecord1 : diaryRecordDownList) {
                    diaryRecord1.setStatus("cancel");
                    update(diaryRecord1);
                }

                //更新统计值
                if (diaryRecord.getSelector().equals("diary")) {
                    Diary diary = diaryMapper.selectByPrimaryKey(diaryRecord.getDiaryId());
                    if (diary != null) {
                        diary.setDownCount((diary.getDownCount() - diaryRecordDownList.size()) > 0 ?
                                diary.getDownCount() - diaryRecordDownList.size() : 0);
                        diaryMapper.updateByPrimaryKeySelective(diary);
                    }

                } else {
                    DiaryComment diaryComment = diaryCommentMapper.selectByPrimaryKey(diaryRecord.getCommentId());

                    if (diaryComment != null) {
                        diaryComment.setDownCount((diaryComment.getDownCount() - diaryRecordDownList.size()) > 0 ?
                                (diaryComment.getDownCount() - diaryRecordDownList.size()) : 0);
                        diaryCommentMapper.updateByPrimaryKeySelective(diaryComment);
                    }
                }

            } else if (type.equals("down")) {

                //踩过继续踩不处理
            }

        }
    }

}