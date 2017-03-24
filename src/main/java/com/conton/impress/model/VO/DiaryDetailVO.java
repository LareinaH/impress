package com.conton.impress.model.VO;

import java.util.List;

public class DiaryDetailVO extends DiaryVO{

    /**
     * 日记正文
     */
    private String content;


    /**
     * 日记评论列表
     */
    List<DiaryCommentVO> diaryCommentVOList;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<DiaryCommentVO> getDiaryCommentVOList() {
        return diaryCommentVOList;
    }

    public void setDiaryCommentVOList(List<DiaryCommentVO> diaryCommentVOList) {
        this.diaryCommentVOList = diaryCommentVOList;
    }
}
