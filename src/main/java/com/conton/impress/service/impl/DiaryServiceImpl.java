package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.model.Diary;
import com.conton.impress.service.DiaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiaryServiceImpl extends BaseServiceImpl<Diary> implements DiaryService{
}
