package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.model.DiaryRecord;
import com.conton.impress.service.DiaryRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiaryRecordServiceImpl extends BaseServiceImpl<DiaryRecord> implements DiaryRecordService{
}
