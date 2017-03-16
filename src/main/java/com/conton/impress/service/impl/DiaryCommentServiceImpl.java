package com.conton.impress.service.impl;

import com.conton.base.service.impl.BaseServiceImpl;
import com.conton.impress.model.DiaryComment;
import com.conton.impress.service.DiaryCommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DiaryCommentServiceImpl extends BaseServiceImpl<DiaryComment> implements DiaryCommentService {
}
