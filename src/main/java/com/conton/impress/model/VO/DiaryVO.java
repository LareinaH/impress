package com.conton.impress.model.VO;

/**
 * Created by Administrator on 2017-03-18.
 */
public class DiaryVO {

    private Long id;

    /**
     * 发布时间
     */
    private String publishTime;

    /**
     * 标签【default：默认， event：事件， mood：心情 ，thing：物件，view： 景色】
     */
    private String tag;

    /**
     * 日记摘要
     */
    private String brief;

    /**
     * 日记首图
     */
    private String firstImage;

    /**
     * 日记高度（冗余字段，用于前端显示）
     */
    private String contentHeight;

    /**
     * 赞的统计数量
     */
    private Integer upCount;

    /**
     * 踩的统计数量
     */
    private Integer downCount;

    /**
     * 评论的统计数量
     */
    private Integer commentCount;

    /**
     * 经度
     */
    private Double lbsX;

    /**
     * 纬度
     */
    private Double lbsY;

    /**
     * 是否阅读状态
     */
    String readStatus;

    /**
     * 是否是好友日记
     */
    boolean bFriendDiary;

    /**
     * 好友姓名
     */
    String friendName;

    /**
     * 头像
     */
    String headPortrait;

    /**
     * 影响力
     */
    String influence;

    public String getContentHeight() {
        return contentHeight;
    }

    public void setContentHeight(String contentHeight) {
        this.contentHeight = contentHeight;
    }

    public boolean isbFriendDiary() {
        return bFriendDiary;
    }

    public void setbFriendDiary(boolean bFriendDiary) {
        this.bFriendDiary = bFriendDiary;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public String getInfluence() {
        return influence;
    }

    public void setInfluence(String influence) {
        this.influence = influence;
    }

    public String getReadStatus() {
        return "unRead";
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
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

    public Double getLbsX() {
        return lbsX;
    }

    public void setLbsX(Double lbsX) {
        this.lbsX = lbsX;
    }

    public Double getLbsY() {
        return lbsY;
    }

    public void setLbsY(Double lbsY) {
        this.lbsY = lbsY;
    }
}
