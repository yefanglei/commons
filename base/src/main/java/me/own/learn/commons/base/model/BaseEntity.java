package me.own.learn.commons.base.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class BaseEntity implements Serializable {

	private static Logger logger = LoggerFactory.getLogger(BaseEntity.class);

    protected boolean deleted;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    protected Date createTime;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    protected Date modifyTime;

    public void onCreated(){
        this.deleted = false;
        this.createTime = new Date();
        this.modifyTime = new Date();
        logger.info("create a new entity:" + this.toString());
    }

    public void onModified(){
        this.modifyTime = new Date();
        logger.info("modify entity to:" + this.toString());
    }

    public void onDelete(){
        this.deleted = true;
        this.modifyTime = new Date();
        logger.info("soft delete entity:" + this.toString());
    }

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
