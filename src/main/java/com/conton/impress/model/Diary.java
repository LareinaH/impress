package com.conton.impress.model;

import com.conton.base.model.BaseModel;
import java.util.Date;
import javax.persistence.*;
@Table(name = "diary")
public class Diary extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 发布者Id
     */
    private Long memberId;

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
     * 是否匿名【0：不匿名，1：匿名】
     */
    private Integer anonymous;

    /**
     * 访问权限【all：所有人可见，excludeFriend：朋友不可见，friend：朋友可见，oneself：仅自己可见】
     */
    private String accessRight;

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
     * 浏览数量统计
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
     * 更新时间
     */
    private Date updateAt;

    /**
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取发布者Id
     *
     * @return memberId - 发布者Id
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * 设置发布者Id
     *
     * @param memberId 发布者Id
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取发布时间
     *
     * @return publishTime - 发布时间
     */
    public String getPublishTime() {
        return publishTime;
    }

    /**
     * 设置发布时间
     *
     * @param publishTime 发布时间
     */
    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime == null ? null : publishTime.trim();
    }

    /**
     * 获取标签【default：默认， event：事件， mood：心情 ，thing：物件，view： 景色】
     *
     * @return tag - 标签【default：默认， event：事件， mood：心情 ，thing：物件，view： 景色】
     */
    public String getTag() {
        return tag;
    }

    /**
     * 设置标签【default：默认， event：事件， mood：心情 ，thing：物件，view： 景色】
     *
     * @param tag 标签【default：默认， event：事件， mood：心情 ，thing：物件，view： 景色】
     */
    public void setTag(String tag) {
        this.tag = tag == null ? null : tag.trim();
    }

    /**
     * 获取日记摘要
     *
     * @return brief - 日记摘要
     */
    public String getBrief() {
        return brief;
    }

    /**
     * 设置日记摘要
     *
     * @param brief 日记摘要
     */
    public void setBrief(String brief) {
        this.brief = brief == null ? null : brief.trim();
    }

    /**
     * 获取日记首图
     *
     * @return firstImage - 日记首图
     */
    public String getFirstImage() {
        return firstImage;
    }

    /**
     * 设置日记首图
     *
     * @param firstImage 日记首图
     */
    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage == null ? null : firstImage.trim();
    }

    /**
     * 获取日记高度（冗余字段，用于前端显示）
     *
     * @return contentHeight - 日记高度（冗余字段，用于前端显示）
     */
    public String getContentHeight() {
        return contentHeight;
    }

    /**
     * 设置日记高度（冗余字段，用于前端显示）
     *
     * @param contentHeight 日记高度（冗余字段，用于前端显示）
     */
    public void setContentHeight(String contentHeight) {
        this.contentHeight = contentHeight == null ? null : contentHeight.trim();
    }

    /**
     * 获取是否匿名【0：不匿名，1：匿名】
     *
     * @return anonymous - 是否匿名【0：不匿名，1：匿名】
     */
    public Integer getAnonymous() {
        return anonymous;
    }

    /**
     * 设置是否匿名【0：不匿名，1：匿名】
     *
     * @param anonymous 是否匿名【0：不匿名，1：匿名】
     */
    public void setAnonymous(Integer anonymous) {
        this.anonymous = anonymous;
    }

    /**
     * 获取访问权限【all：所有人可见，excludeFriend：朋友不可见，friend：朋友可见，oneself：仅自己可见】
     *
     * @return accessRight - 访问权限【all：所有人可见，excludeFriend：朋友不可见，friend：朋友可见，oneself：仅自己可见】
     */
    public String getAccessRight() {
        return accessRight;
    }

    /**
     * 设置访问权限【all：所有人可见，excludeFriend：朋友不可见，friend：朋友可见，oneself：仅自己可见】
     *
     * @param accessRight 访问权限【all：所有人可见，excludeFriend：朋友不可见，friend：朋友可见，oneself：仅自己可见】
     */
    public void setAccessRight(String accessRight) {
        this.accessRight = accessRight == null ? null : accessRight.trim();
    }

    /**
     * 获取赞的统计数量
     *
     * @return upCount - 赞的统计数量
     */
    public Integer getUpCount() {
        return upCount;
    }

    /**
     * 设置赞的统计数量
     *
     * @param upCount 赞的统计数量
     */
    public void setUpCount(Integer upCount) {
        this.upCount = upCount;
    }

    /**
     * 获取踩的统计数量
     *
     * @return downCount - 踩的统计数量
     */
    public Integer getDownCount() {
        return downCount;
    }

    /**
     * 设置踩的统计数量
     *
     * @param downCount 踩的统计数量
     */
    public void setDownCount(Integer downCount) {
        this.downCount = downCount;
    }

    /**
     * 获取评论的统计数量
     *
     * @return commentCount - 评论的统计数量
     */
    public Integer getCommentCount() {
        return commentCount;
    }

    /**
     * 设置评论的统计数量
     *
     * @param commentCount 评论的统计数量
     */
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    /**
     * 获取浏览数量统计
     *
     * @return browseCount - 浏览数量统计
     */
    public Integer getBrowseCount() {
        return browseCount;
    }

    /**
     * 设置浏览数量统计
     *
     * @param browseCount 浏览数量统计
     */
    public void setBrowseCount(Integer browseCount) {
        this.browseCount = browseCount;
    }

    /**
     * 获取经度
     *
     * @return lbsX - 经度
     */
    public Double getLbsX() {
        return lbsX;
    }

    /**
     * 设置经度
     *
     * @param lbsX 经度
     */
    public void setLbsX(Double lbsX) {
        this.lbsX = lbsX;
    }

    /**
     * 获取纬度
     *
     * @return lbsY - 纬度
     */
    public Double getLbsY() {
        return lbsY;
    }

    /**
     * 设置纬度
     *
     * @param lbsY 纬度
     */
    public void setLbsY(Double lbsY) {
        this.lbsY = lbsY;
    }

    /**
     * 获取日记状态【normal 正常 delete 删除】
     *
     * @return status - 日记状态【normal 正常 delete 删除】
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置日记状态【normal 正常 delete 删除】
     *
     * @param status 日记状态【normal 正常 delete 删除】
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 获取创建时间
     *
     * @return createdAt - 创建时间
     */
    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * 设置创建时间
     *
     * @param createdAt 创建时间
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * 获取更新时间
     *
     * @return updateAt - 更新时间
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置更新时间
     *
     * @param updateAt 更新时间
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}