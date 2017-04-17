package com.conton.impress.model;

import com.conton.base.model.BaseModel;
import java.util.Date;
import javax.persistence.*;
@Table(name = "member")
public class Member extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户注册极光推送的用户名（唯一值）
     */
    private String uuid;

    /**
     * 凭证
     */
    private String ticket;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 头像
     */
    private String headPortrait;

    /**
     * 电话
     */
    private String cellphone;

    /**
     * 密码
     */
    private String password;

    /**
     * 会员状态【normal 正常 cancle 注销 delete 删除】
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
     * 获取用户注册极光推送的用户名（唯一值）
     *
     * @return uuid - 用户注册极光推送的用户名（唯一值）
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * 设置用户注册极光推送的用户名（唯一值）
     *
     * @param uuid 用户注册极光推送的用户名（唯一值）
     */
    public void setUuid(String uuid) {
        this.uuid = uuid == null ? null : uuid.trim();
    }

    /**
     * 获取凭证
     *
     * @return ticket - 凭证
     */
    public String getTicket() {
        return ticket;
    }

    /**
     * 设置凭证
     *
     * @param ticket 凭证
     */
    public void setTicket(String ticket) {
        this.ticket = ticket == null ? null : ticket.trim();
    }

    /**
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取性别
     *
     * @return sex - 性别
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置性别
     *
     * @param sex 性别
     */
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    /**
     * 获取头像
     *
     * @return headPortrait - 头像
     */
    public String getHeadPortrait() {
        return headPortrait;
    }

    /**
     * 设置头像
     *
     * @param headPortrait 头像
     */
    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait == null ? null : headPortrait.trim();
    }

    /**
     * 获取电话
     *
     * @return cellphone - 电话
     */
    public String getCellphone() {
        return cellphone;
    }

    /**
     * 设置电话
     *
     * @param cellphone 电话
     */
    public void setCellphone(String cellphone) {
        this.cellphone = cellphone == null ? null : cellphone.trim();
    }

    /**
     * 获取密码
     *
     * @return password - 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置密码
     *
     * @param password 密码
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * 获取会员状态【normal 正常 cancle 注销 delete 删除】
     *
     * @return status - 会员状态【normal 正常 cancle 注销 delete 删除】
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置会员状态【normal 正常 cancle 注销 delete 删除】
     *
     * @param status 会员状态【normal 正常 cancle 注销 delete 删除】
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