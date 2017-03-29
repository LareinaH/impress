package com.conton.impress.model;

import com.conton.base.model.BaseModel;
import java.util.Date;
import javax.persistence.*;

@Table(name = "message")
public class Message extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 类别【friend：好友申请，up：点赞，comment：评论】
     */
    private String category;

    /**
     * 消息发送者
     */
    private Long fromMemberId;

    /**
     * 消息接收者
     */
    private Long toMemberId;

    /**
     * 消息处理状态【unprocessed：未处理，processed：已处理】
     */
    private String processStatus;

    /**
     * 状态【normal：正常，delete：删除】
     */
    private String status;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 修改时间
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
     * 获取类别【friend：好友申请，up：点赞，comment：评论】
     *
     * @return category - 类别【friend：好友申请，up：点赞，comment：评论】
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置类别【friend：好友申请，up：点赞，comment：评论】
     *
     * @param category 类别【friend：好友申请，up：点赞，comment：评论】
     */
    public void setCategory(String category) {
        this.category = category == null ? null : category.trim();
    }

    /**
     * 获取消息发送者
     *
     * @return fromMemberId - 消息发送者
     */
    public Long getFromMemberId() {
        return fromMemberId;
    }

    /**
     * 设置消息发送者
     *
     * @param fromMemberId 消息发送者
     */
    public void setFromMemberId(Long fromMemberId) {
        this.fromMemberId = fromMemberId;
    }

    /**
     * 获取消息接收者
     *
     * @return toMemberId - 消息接收者
     */
    public Long getToMemberId() {
        return toMemberId;
    }

    /**
     * 设置消息接收者
     *
     * @param toMemberId 消息接收者
     */
    public void setToMemberId(Long toMemberId) {
        this.toMemberId = toMemberId;
    }

    /**
     * 获取消息处理状态【unprocessed：未处理，processed：已处理】
     *
     * @return processStatus - 消息处理状态【unprocessed：未处理，processed：已处理】
     */
    public String getProcessStatus() {
        return processStatus;
    }

    /**
     * 设置消息处理状态【unprocessed：未处理，processed：已处理】
     *
     * @param processStatus 消息处理状态【unprocessed：未处理，processed：已处理】
     */
    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus == null ? null : processStatus.trim();
    }

    /**
     * 获取状态【normal：正常，delete：删除】
     *
     * @return status - 状态【normal：正常，delete：删除】
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态【normal：正常，delete：删除】
     *
     * @param status 状态【normal：正常，delete：删除】
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
     * 获取修改时间
     *
     * @return updateAt - 修改时间
     */
    public Date getUpdateAt() {
        return updateAt;
    }

    /**
     * 设置修改时间
     *
     * @param updateAt 修改时间
     */
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}