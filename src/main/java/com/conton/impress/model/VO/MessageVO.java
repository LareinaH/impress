package com.conton.impress.model.VO;


import com.conton.impress.model.Diary;

import java.util.Date;

public class MessageVO {

    /**
     * ID
     */
    private Long id;

    /**
     * 类别【friend：好友申请，up：点赞，comment：评论】
     */
    private String category;

    /**
     * 消息发送者Id
     */
    private Long fromMemberId;


    /**
     * 消息发送者
     */
    private String fromMemberName;

    /**
     * 消息发送者
     */
    private String fromMemberHeadPortrait;

    /**
     * 创建时间
     */
    private Date createdAt;


    /**
     * 关联的日记 当ctaegory 等于up/comment时候有效
     */
    private Diary diary;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getFromMemberId() {
        return fromMemberId;
    }

    public void setFromMemberId(Long fromMemberId) {
        this.fromMemberId = fromMemberId;
    }

    public String getFromMemberName() {
        return fromMemberName;
    }

    public void setFromMemberName(String fromMemberName) {
        this.fromMemberName = fromMemberName;
    }

    public String getFromMemberHeadPortrait() {
        return fromMemberHeadPortrait;
    }

    public void setFromMemberHeadPortrait(String fromMemberHeadPortrait) {
        this.fromMemberHeadPortrait = fromMemberHeadPortrait;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Diary getDiary() {
        return diary;
    }

    public void setDiary(Diary diary) {
        this.diary = diary;
    }
}
