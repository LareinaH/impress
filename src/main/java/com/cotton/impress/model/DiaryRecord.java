package com.cotton.impress.model;

import com.cotton.base.model.BaseModel;
import java.util.Date;
import javax.persistence.*;

@Table(name = "diary_record")
public class DiaryRecord extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的会员
     */
    private Long memberId;

    /**
     * 选择器【diary：日记， comment：评论， browse：浏览】
     */
    private String selector;

    /**
     * 日记id  当selector= diary时有效
     */
    private Long diaryId;

    /**
     * 冗余数据 日记关联的用户id
     */
    private Long diaryMemberId;

    /**
     * 评论id 当selector=comment是有效
     */
    private Long commentId;

    /**
     * 冗余数据 评论关联的用户id
     */
    private Long commentMemberId;

    /**
     * 分类【up：赞，down：踩，browse：浏览，comment：评论】
     */
    private String category;

    private Double lbsX;

    private Double lbsY;

    /**
     * 状态【normal：正常 delete：删除】
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
     * 获取关联的会员
     *
     * @return memberId - 关联的会员
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * 设置关联的会员
     *
     * @param memberId 关联的会员
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取选择器【diary：日记， comment：评论， browse：浏览】
     *
     * @return selector - 选择器【diary：日记， comment：评论， browse：浏览】
     */
    public String getSelector() {
        return selector;
    }

    /**
     * 设置选择器【diary：日记， comment：评论， browse：浏览】
     *
     * @param selector 选择器【diary：日记， comment：评论， browse：浏览】
     */
    public void setSelector(String selector) {
        this.selector = selector == null ? null : selector.trim();
    }

    /**
     * 获取日记id  当selector= diary时有效
     *
     * @return diaryId - 日记id  当selector= diary时有效
     */
    public Long getDiaryId() {
        return diaryId;
    }

    /**
     * 设置日记id  当selector= diary时有效
     *
     * @param diaryId 日记id  当selector= diary时有效
     */
    public void setDiaryId(Long diaryId) {
        this.diaryId = diaryId;
    }

    /**
     * 获取冗余数据 日记关联的用户id
     *
     * @return diaryMemberId - 冗余数据 日记关联的用户id
     */
    public Long getDiaryMemberId() {
        return diaryMemberId;
    }

    /**
     * 设置冗余数据 日记关联的用户id
     *
     * @param diaryMemberId 冗余数据 日记关联的用户id
     */
    public void setDiaryMemberId(Long diaryMemberId) {
        this.diaryMemberId = diaryMemberId;
    }

    /**
     * 获取评论id 当selector=comment是有效
     *
     * @return commentId - 评论id 当selector=comment是有效
     */
    public Long getCommentId() {
        return commentId;
    }

    /**
     * 设置评论id 当selector=comment是有效
     *
     * @param commentId 评论id 当selector=comment是有效
     */
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    /**
     * 获取冗余数据 评论关联的用户id
     *
     * @return commentMemberId - 冗余数据 评论关联的用户id
     */
    public Long getCommentMemberId() {
        return commentMemberId;
    }

    /**
     * 设置冗余数据 评论关联的用户id
     *
     * @param commentMemberId 冗余数据 评论关联的用户id
     */
    public void setCommentMemberId(Long commentMemberId) {
        this.commentMemberId = commentMemberId;
    }

    /**
     * 获取分类【up：赞，down：踩，browse：浏览，comment：评论】
     *
     * @return category - 分类【up：赞，down：踩，browse：浏览，comment：评论】
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置分类【up：赞，down：踩，browse：浏览，comment：评论】
     *
     * @param category 分类【up：赞，down：踩，browse：浏览，comment：评论】
     */
    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    /**
     * @return lbsX
     */
    public Double getLbsX() {
        return lbsX;
    }

    /**
     * @param lbsX
     */
    public void setLbsX(Double lbsX) {
        this.lbsX = lbsX;
    }

    /**
     * @return lbsY
     */
    public Double getLbsY() {
        return lbsY;
    }

    /**
     * @param lbsY
     */
    public void setLbsY(Double lbsY) {
        this.lbsY = lbsY;
    }

    /**
     * 获取状态【normal：正常 delete：删除】
     *
     * @return status - 状态【normal：正常 delete：删除】
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态【normal：正常 delete：删除】
     *
     * @param status 状态【normal：正常 delete：删除】
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