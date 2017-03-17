/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.onlineclass.entities;

import java.io.Serializable;
import com.rsdynamix.abstractobjects.AbstractEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.sql.Date;

/**
 *
 * @author root
 */
@Entity
@Table(name = "FORUM_TABLE")
public class ForumEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "FORUM_ID", columnDefinition = "NUMBER(10)")
    private long forumID;
    //
    @Column(name = "FORUM_NAME", columnDefinition = "VARCHAR2(100)")
    private String forumName;
    //
    @Column(name = "CREATOR", columnDefinition = "VARCHAR2(60)")
    private String creator;
    //
    private transient java.util.Date createDateTO;
    @Column(name = "CREATE_DATE", columnDefinition = "DATE")
    private Date createDate;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //

    public ForumEntity() {
        forumID = 0;
        forumName = "";
        creator = "";
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof ForumEntity) {
            if((((ForumEntity)o).getForumID() > 0) 
                    && (((ForumEntity)o).getForumID() == this.getForumID())){
                eqls = true;
            } else {
                eqls = ((ForumEntity)o).getForumName().equals(this.getForumName());
            }
        }

        return eqls;
    }

    public long getForumID() {
        return this.forumID;
    }

    public void setForumID(long forumID) {
        this.forumID = forumID;
    }

    public String getForumName() {
        return this.forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public String getCreator() {
        return this.creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the createDateTO
     */
    public java.util.Date getCreateDateTO() {
        return createDateTO;
    }

    /**
     * @param createDateTO the createDateTO to set
     */
    public void setCreateDateTO(java.util.Date createDateTO) {
        this.createDateTO = createDateTO;
    }

    /**
     * @return the approvalStatusID
     */
    public int getApprovalStatusID() {
        return approvalStatusID;
    }

    /**
     * @param approvalStatusID the approvalStatusID to set
     */
    public void setApprovalStatusID(int approvalStatusID) {
        this.approvalStatusID = approvalStatusID;
    }

    /**
     * @return the approver
     */
    public String getApprover() {
        return approver;
    }

    /**
     * @param approver the approver to set
     */
    public void setApprover(String approver) {
        this.approver = approver;
    }

    /**
     * @return the actionTrail
     */
    public BusinessActionTrailEntity getActionTrail() {
        return actionTrail;
    }

    /**
     * @param actionTrail the actionTrail to set
     */
    public void setActionTrail(BusinessActionTrailEntity actionTrail) {
        this.actionTrail = actionTrail;
    }
}
