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
@Table(name = "FORUM_THREAD")
public class ForumThreadEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "FORUM_THREAD_ID", columnDefinition = "NUMBER(12)")
    private long forumThreadID;
    //
    @Column(name = "FORUM_ID", columnDefinition = "NUMBER(10)")
    private long forumID;
    //
    @Column(name = "THREAD_TITLE", columnDefinition = "VARCHAR2(255)")
    private String threadTitle;
    //
    @Column(name = "THREAD_CODE", columnDefinition = "VARCHAR2(20)")
    private String threadCode;
    //
    @Column(name = "THREAD_CREATOR", columnDefinition = "VARCHAR2(60)")
    private String threadCreator;
    //
    @Column(name = "THREAD_STATEMENT", columnDefinition = "VARCHAR2(4000)")
    private String threadStatement;
    //
    private transient java.util.Date threadCreateDateTO;
    @Column(name = "THREAD_CREATE_DATE", columnDefinition = "DATE")
    private Date threadCreateDate;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    transient private String forumName;

    public ForumThreadEntity() {
        forumThreadID = 0;
        forumID = 0;
        threadTitle = "";
        threadCode = "";
        threadCreator = "";
        threadStatement = "";
        forumName = "";
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof ForumThreadEntity) {
            if((((ForumThreadEntity)o).getForumThreadID() > 0) 
                    && (((ForumThreadEntity)o).getForumThreadID() == this.getForumThreadID())){
                eqls = true;
            } else {
                eqls = ((ForumThreadEntity)o).getThreadCode().equals(this.getThreadCode());
            }
        }

        return eqls;
    }

    public long getForumThreadID() {
        return this.forumThreadID;
    }

    public void setForumThreadID(long forumThreadID) {
        this.forumThreadID = forumThreadID;
    }

    public long getForumID() {
        return this.forumID;
    }

    public void setForumID(long forumID) {
        this.forumID = forumID;
    }

    public String getThreadTitle() {
        return this.threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public String getThreadCode() {
        return this.threadCode;
    }

    public void setThreadCode(String threadCode) {
        this.threadCode = threadCode;
    }

    public String getThreadCreator() {
        return this.threadCreator;
    }

    public void setThreadCreator(String threadCreator) {
        this.threadCreator = threadCreator;
    }

    public String getThreadStatement() {
        return this.threadStatement;
    }

    public void setThreadStatement(String threadStatement) {
        this.threadStatement = threadStatement;
    }

    public Date getThreadCreateDate() {
        return this.threadCreateDate;
    }

    public void setThreadCreateDate(Date threadCreateDate) {
        this.threadCreateDate = threadCreateDate;
    }

    /**
     * @return the forumName
     */
    public String getForumName() {
        return forumName;
    }

    /**
     * @param forumName the forumName to set
     */
    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    /**
     * @return the threadCreateDateTO
     */
    public java.util.Date getThreadCreateDateTO() {
        return threadCreateDateTO;
    }

    /**
     * @param threadCreateDateTO the threadCreateDateTO to set
     */
    public void setThreadCreateDateTO(java.util.Date threadCreateDateTO) {
        this.threadCreateDateTO = threadCreateDateTO;
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
