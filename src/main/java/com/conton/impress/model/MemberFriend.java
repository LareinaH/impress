package com.conton.impress.model;

import com.conton.base.model.BaseModel;
import java.util.Date;
import javax.persistence.*;

@Table(name = "member_friend")
public class MemberFriend extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 好友id
     */
    private Long friendMemberId;

    /**
     * 会员关系状态【normal：好友，delete：好友删除，black：黑名单】
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
     * 获取会员id
     *
     * @return memberId - 会员id
     */
    public Long getMemberId() {
        return memberId;
    }

    /**
     * 设置会员id
     *
     * @param memberId 会员id
     */
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取好友id
     *
     * @return friendMemberId - 好友id
     */
    public Long getFriendMemberId() {
        return friendMemberId;
    }

    /**
     * 设置好友id
     *
     * @param friendMemberId 好友id
     */
    public void setFriendMemberId(Long friendMemberId) {
        this.friendMemberId = friendMemberId;
    }

    /**
     * 获取会员关系状态【normal：好友，delete：好友删除，black：黑名单】
     *
     * @return status - 会员关系状态【normal：好友，delete：好友删除，black：黑名单】
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置会员关系状态【normal：好友，delete：好友删除，black：黑名单】
     *
     * @param status 会员关系状态【normal：好友，delete：好友删除，black：黑名单】
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