package com.github.jifengnan.summer.util.base;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础对象。
 * <p>
 * 在实际工作中，几乎所有对象都需要有以下属性。
 *
 * @author 纪凤楠 2019-03-20
 */
public abstract class BaseObject<ID extends Serializable> {

    private ID id;

    /**
     * 是否被逻辑删除
     */
    private boolean deleted;

    /**
     * 创建人的ID
     */
    private ID createdBy;

    /**
     * 创建时间
     */
    private Date createdOn;

    /**
     * 修改人的ID
     */
    private ID modifiedBy;

    /**
     * 修改时间
     */
    private Date modifiedOn;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public ID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(ID createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public ID getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(ID modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }
}
