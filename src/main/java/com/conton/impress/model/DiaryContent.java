package com.conton.impress.model;

import com.conton.base.model.BaseModel;
import java.util.Date;
import javax.persistence.*;

@Table(name = "diary_content")
public class DiaryContent extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的日记id
     */
    private Long diaryId;

    /**
     * 状态【normal:正常 delete: 删除】
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
     * 日记正文
     */
    private String content;

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
     * 获取关联的日记id
     *
     * @return diaryId - 关联的日记id
     */
    public Long getDiaryId() {
        return diaryId;
    }

    /**
     * 设置关联的日记id
     *
     * @param diaryId 关联的日记id
     */
    public void setDiaryId(Long diaryId) {
        this.diaryId = diaryId;
    }

    /**
     * 获取状态【normal:正常 delete: 删除】
     *
     * @return status - 状态【normal:正常 delete: 删除】
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态【normal:正常 delete: 删除】
     *
     * @param status 状态【normal:正常 delete: 删除】
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

    /**
     * 获取日记正文
     *
     * @return content - 日记正文
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置日记正文
     *
     * @param content 日记正文
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}