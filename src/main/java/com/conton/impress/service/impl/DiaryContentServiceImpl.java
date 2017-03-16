package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.model.DiaryContent;
import com.conton.impress.service.DiaryContentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiaryContentServiceImpl extends BaseServiceImpl<DiaryContent> implements DiaryContentService{
}
