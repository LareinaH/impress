package com.cotton.impress.service;

import com.cotton.base.service.BaseService;
import com.cotton.impress.model.DiaryRecord;

/**
 * Created by Administrator on 2017-03-16.
 */
public interface DiaryRecordService extends BaseService<DiaryRecord> {

    boolean addDiaryRecord(DiaryRecord diaryRecord);
}
