package com.conton.impress.service;

import com.conton.base.service.BaseService;
import com.conton.impress.model.Diary;
import com.conton.impress.model.VO.DiaryDetailVO;

/**
 * Created by Administrator on 2017-03-16.
 */
public interface DiaryService extends BaseService<Diary> {

    DiaryDetailVO getDiaryDetailVObyId(long id);

    boolean addDiary(long memberId, String publishTime, String tag, String brief, String firstImage,
                     Integer anonymous, String accessRight, double lbsX, double lbsY,
                     String content);
}
