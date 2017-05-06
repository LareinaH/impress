package com.cotton.impress.service;

import com.cotton.base.service.BaseService;
import com.cotton.impress.model.DiaryComment;

public interface DiaryCommentService extends BaseService<DiaryComment> {

    boolean addComment(DiaryComment diaryComment);
}
