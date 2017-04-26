package com.conton.impress.service;

import com.conton.base.service.BaseService;
import com.conton.impress.model.Diary;
import com.conton.impress.model.VO.DiaryDetailVO;
import com.conton.impress.model.VO.DiaryExVO;
import com.conton.impress.model.VO.DiaryVO;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * Created by Administrator on 2017-03-16.
 */
public interface DiaryService extends BaseService<Diary> {

    DiaryDetailVO getDiaryDetailVObyId(long id);

    boolean addDiary(long memberId, String sex, String publishTime, String tag, String brief, String firstImage,String contentHeight,
                     Integer anonymous, String accessRight, double lbsX, double lbsY,
                     String content);

    boolean editDiary(Diary diary, String content);


    PageInfo<DiaryVO> queryAboutDiaryList(int pageNum, int pageSize,Map<String,Object> map);

    PageInfo<DiaryVO> querySunDiaryList(int pageNum, int pageSize,Map<String,Object> map);

    DiaryExVO convertDiaryVO2DiaryExVO(Long currentMemberId, DiaryVO diaryVO);


    void resetWeight();

}
