package com.cotton.impress.model.VO;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class MemberVO {
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
     * 是否已经分享过
     */
    private Integer firstShare;


    public Integer getFirstShare() {
        return firstShare;
    }

    public void setFirstShare(Integer firstShare) {
        this.firstShare = firstShare;
    }

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}