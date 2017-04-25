package com.conton.impress.model.VO;

public class DiaryExVO extends DiaryVO {

    /**
     * 是否阅读状态
     */
    boolean readStatus;

    /**
     * 是否赞过
     */
    boolean upStatus;

    /**
     * 是否踩
     */
    boolean downStatus;

    /**
     * 是否是好友日记
     */
    boolean bFriendDiary;


    public boolean isReadStatus() {
        return readStatus;
    }

    public void setReadStatus(boolean readStatus) {
        this.readStatus = readStatus;
    }

    public boolean isUpStatus() {
        return upStatus;
    }

    public void setUpStatus(boolean upStatus) {
        this.upStatus = upStatus;
    }

    public boolean isDownStatus() {
        return downStatus;
    }

    public void setDownStatus(boolean downStatus) {
        this.downStatus = downStatus;
    }

    public boolean isbFriendDiary() {
        return bFriendDiary;
    }

    public void setbFriendDiary(boolean bFriendDiary) {
        this.bFriendDiary = bFriendDiary;
    }
}
