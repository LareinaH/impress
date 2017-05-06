package com.cotton.impress.service.impl;

import com.cotton.base.service.impl.BaseServiceImpl;
import com.cotton.impress.model.DiaryContent;
import com.cotton.impress.service.DiaryContentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiaryContentServiceImpl extends BaseServiceImpl<DiaryContent> implements DiaryContentService{
}
