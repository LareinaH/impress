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
