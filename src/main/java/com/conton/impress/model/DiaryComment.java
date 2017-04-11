package com.conton.impress.model;

import com.conton.base.model.BaseModel;
import java.util.Date;
import javax.persistence.*;

@Table(name = "diary_comment")
public class DiaryComment extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的日记Id
     */
    private Long diaryId;

    /**
     * 引用的父评论的id
     */
    private Long parentId;

    /**
     * 是否是悄悄话【0：不是，1：是】
     */
    private Integer isWhisper;

    /**
     * 评论用户
     */
    private Long commentUserId;

    /**
     * 评论图片地址
     */
    private String image;

    /**
     * 状态【normal:正常 delete 删除】
     */
    private String status;

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
     * 更新时间
     */
    private Date updateAt;

    /**
     * 评论内容
     */
    private String commentText;

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
     * 获取关联的日记Id
     *
     * @return diaryId - 关联的日记Id
     */
    public Long getDiaryId() {
        return diaryId;
    }

    /**
     * 设置关联的日记Id
     *
     * @param diaryId 关联的日记Id
     */
    public void setDiaryId(Long diaryId) {
        this.diaryId = diaryId;
    }

    /**
     * 获取引用的父评论的id
     *
     * @return parentId - 引用的父评论的id
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置引用的父评论的id
     *
     * @param parentId 引用的父评论的id
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取是否是悄悄话【0：不是，1：是】
     *
     * @return isWhisper - 是否是悄悄话【0：不是，1：是】
     */
    public Integer getIsWhisper() {
        return isWhisper;
    }

    /**
     * 设置是否是悄悄话【0：不是，1：是】
     *
     * @param isWhisper 是否是悄悄话【0：不是，1：是】
     */
    public void setIsWhisper(Integer isWhisper) {
        this.isWhisper = isWhisper;
    }

    /**
     * 获取评论用户
     *
     * @return commentUserId - 评论用户
     */
    public Long getCommentUserId() {
        return commentUserId;
    }

    /**
     * 设置评论用户
     *
     * @param commentUserId 评论用户
     */
    public void setCommentUserId(Long commentUserId) {
        this.commentUserId = commentUserId;
    }

    /**
     * 获取评论图片地址
     *
     * @return image - 评论图片地址
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置评论图片地址
     *
     * @param image 评论图片地址
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * 获取状态【normal:正常 delete 删除】
     *
     * @return status - 状态【normal:正常 delete 删除】
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态【normal:正常 delete 删除】
     *
     * @param status 状态【normal:正常 delete 删除】
     */
    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    /**
     * 获取赞数量统计
     *
     * @return upCount - 赞数量统计
     */
    public Integer getUpCount() {
        return upCount;
    }

    /**
     * 设置赞数量统计
     *
     * @param upCount 赞数量统计
     */
    public void setUpCount(Integer upCount) {
        this.upCount = upCount;
    }

    /**
     * 获取踩数量统计
     *
     * @return downCount - 踩数量统计
     */
    public Integer getDownCount() {
        return downCount;
    }

    /**
     * 设置踩数量统计
     *
     * @param downCount 踩数量统计
     */
    public void setDownCount(Integer downCount) {
        this.downCount = downCount;
    }

    /**
     * 获取评论数量统计
     *
     * @return commentCount - 评论数量统计
     */
    public Integer getCommentCount() {
        return commentCount;
    }

    /**
     * 设置评论数量统计
     *
     * @param commentCount 评论数量统计
     */
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
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

    /**
     * 获取评论内容
     *
     * @return commentText - 评论内容
     */
    public String getCommentText() {
        return commentText;
    }

    /**
     * 设置评论内容
     *
     * @param commentText 评论内容
     */
    public void setCommentText(String commentText) {
        this.commentText = commentText == null ? null : commentText.trim();
    }
}