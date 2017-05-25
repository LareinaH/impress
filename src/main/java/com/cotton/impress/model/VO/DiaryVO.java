package com.cotton.impress.model.VO;

import java.util.Date;

/**
 * Created by Administrator on 2017-03-18.
 */
public class DiaryVO {

    private Long id;

    /**
     * 发布人
     */
    private Long memberId;

    /**
     * 性别
     */
    private String sex;

    /**
     * 发布时间
     */
    private String publishTime;

    /**
     * 标签【default：默认， event：事件， mood：心情 ，thing：物件，view： 景色】
     */
    private String tag;

    /**
     * 访问权限【all：所有人可见，excludeFriend：朋友不可见，friend：朋友可见，oneself：仅自己可见】
     */
    private String accessRight;

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
     * 浏览的统计数量
     */
    private Integer browseCount;

    /**
     * 经度
     */
    private Double lbsX;

    /**
     * 纬度
     */
    private Double lbsY;

    /**
     * 日记状态【normal 正常 delete 删除】
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createdAt;


    /**
     * 影响力
     */
    private String influence;

    /**
     * 距离
     */
    private Integer distance;


    //连表查询得出的数据

    /**
     * 好友姓名
     */
    private String friendName;

    /**
     * 头像
     */
    private String headPortrait;



    public String getDistanceView(){

        if(distance != null) {

            if (distance < 1000) {
                return String.valueOf(distance) + "m";
            } else if (1000 <= distance && distance < 1000000) {
                return String.format("%.2f",(double) distance / 1000) + "km";
            } else if (1000000 <= distance && distance < 1000000000) {
                return String.format("%.2f",(double) distance / 1000000) + "Mm";
            } else if (1000000000 <= distance) {
                return String.format("%.2f",(double) distance / 1000000000) + "Gm";
            }
        }

        return "0m";
    }


    public String getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(String accessRight) {
        this.accessRight = accessRight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getBrowseCount() {
        return browseCount;
    }

    public void setBrowseCount(Integer browseCount) {
        this.browseCount = browseCount;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getContentHeight() {
        return contentHeight;
    }

    public void setContentHeight(String contentHeight) {
        this.contentHeight = contentHeight;
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
