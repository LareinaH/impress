package com.conton.impress.model.VO;

import java.util.Date;
import java.util.List;

public class DiaryCommentVO {

    private Long id;

    /**
     * 评论内容
     */
    private String commentText;

    /**
     * 引用的父评论的id
     */
    private Long parentId;

    /**
     * 评论用户
     */
    private Long commentUserId;

    /**
     * 评论用户姓名
     */
    private String commentUserName;

    /**
     * 评论用户头像
     */
    private String commentUserHeadPortrait;


    List<DiaryCommentVO> replayList;

    /**
     * 赞数量统计
     */
    private Integer upCount;

    /**
     * 踩数量统计
     */
    private Integer downCount;

    /**
     * 评论数量统计
     */
    private Integer commentCount;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 评论图片地址
     */
    private String image;



    public List<DiaryCommentVO> getReplayList() {
        return replayList;
    }

    public void setReplayList(List<DiaryCommentVO> replayList) {
        this.replayList = replayList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getCommentUserId() {
        return commentUserId;
    }

    public void setCommentUserId(Long commentUserId) {
        this.commentUserId = commentUserId;
    }

    public String getCommentUserName() {
        return commentUserName;
    }

    public void setCommentUserName(String commentUserName) {
        this.commentUserName = commentUserName;
    }

    public String getCommentUserHeadPortrait() {
        return commentUserHeadPortrait;
    }

    public void setCommentUserHeadPortrait(String commentUserHeadPortrait) {
        this.commentUserHeadPortrait = commentUserHeadPortrait;
    }

    public Integer getUpCount() {
        return upCount;
    }

    public void setUpCount(Integer upCount) {
        this.upCount = upCount;
    }

    public Integer getDownCount() {
        return downCount;
    }

    public void setDownCount(Integer downCount) {
        this.downCount = downCount;
    }

    public Integer getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
