package com.conton.impress.service;

import com.conton.base.service.BaseService;
import com.conton.impress.model.DiaryRecord;

/**
 * Created by Administrator on 2017-03-16.
 */
public interface DiaryRecordService extends BaseService<DiaryRecord> {

    boolean addDiaryRecord(DiaryRecord diaryRecord);
}
