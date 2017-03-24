package com.conton.impress.service;

import com.conton.base.service.BaseService;
import com.conton.impress.model.Diary;
import com.conton.impress.model.VO.DiaryDetailVO;

/**
 * Created by Administrator on 2017-03-16.
 */
public interface DiaryService extends BaseService<Diary> {

    DiaryDetailVO getDiaryDetailVObyId(long id);
}
