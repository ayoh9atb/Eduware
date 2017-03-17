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
import com.codrellica.projects.date.DateVO;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;

/**
 *
 * @author root
 */
@Entity
@Table(name = "THREAD_RESPONSE")
public class ThreadResponseEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "RESPONSE_ID", columnDefinition = "NUMBER(12)")
    private long responseID;
    //
    @Column(name = "FORUM_THREAD_ID", columnDefinition = "NUMBER(12)")
    private long forumThreadID;
    //
    @Column(name = "FORUM_ID", columnDefinition = "NUMBER(12)")
    private long forumID;
    //
    @Column(name = "RESPONSE_STATEMENT", columnDefinition = "VARCHAR2(4000)")
    private String responseStatement;
    //
    @Column(name = "RESPONSE_DATE", columnDefinition = "DATE")
    private DateVO responseDate;
    //
    @Column(name = "RESPONSE_CREATOR", columnDefinition = "VARCHAR2(60)")
    private String responseCreator;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public ThreadResponseEntity() {
        responseID = 0;
        forumThreadID = 0;
        responseStatement = "";
        responseCreator = "";
    }

    public long getResponseID() {
        return this.responseID;
    }

    public void setResponseID(long responseID) {
        this.responseID = responseID;
    }

    public long getForumThreadID() {
        return this.forumThreadID;
    }

    public void setForumThreadID(long forumThreadID) {
        this.forumThreadID = forumThreadID;
    }

    public String getResponseStatement() {
        return this.responseStatement;
    }

    public void setResponseStatement(String responseStatement) {
        this.responseStatement = responseStatement;
    }

    public DateVO getResponseDate() {
        return this.responseDate;
    }

    public void setResponseDate(DateVO responseDate) {
        this.responseDate = responseDate;
    }

    public String getResponseCreator() {
        return this.responseCreator;
    }

    public void setResponseCreator(String responseCreator) {
        this.responseCreator = responseCreator;
    }

    /**
     * @return the forumID
     */
    public long getForumID() {
        return forumID;
    }

    /**
     * @param forumID the forumID to set
     */
    public void setForumID(long forumID) {
        this.forumID = forumID;
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
