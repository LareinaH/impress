package com.cotton.impress.model.VO;

import java.util.List;

public class DiaryDetailVO extends DiaryExVO{

    /**
     * 日记正文
     */
    private String content;


    /**
     * 日记评论列表
     */
    List<DiaryCommentVO> diaryCommentVOList;

    /**
     * 分享出去的url
     */
    private String shareUrl;


    /**
     * 是否已经分享过
     */
    private Integer firstShare;


    public Integer getFirstShare() {
        return firstShare;
    }

    public void setFirstShare(Integer firstShare) {
        this.firstShare = firstShare;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

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
