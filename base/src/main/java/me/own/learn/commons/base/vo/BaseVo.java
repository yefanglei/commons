package me.own.learn.commons.base.vo;

import java.io.Serializable;
import java.util.Date;

public class BaseVo implements Serializable{

	protected boolean deleted;

    protected Date createTime;

    protected Date modifyTime;

    public boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}
