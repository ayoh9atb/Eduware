/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.finance.requisition.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author p-aniah
 */
@Entity
@Table(name = "APPROVAL_STEP")
public class ApprovalStepEntity extends AbstractEntity implements Serializable {
    
    @Id
    @Column(name = "APPROVAL_STEP_ID", columnDefinition="NUMBER(2)")
    private int approvalStepID;
    //
    @Column(name = "APPROVAL_DESC", columnDefinition="Varchar2(60)")
    private String approvalDesc;
    //
    @Column(name = "APPROVAL_OFFICER_KEY", columnDefinition="NUMBER(4)")
    private int approvalOfficerKey;
    //
    private transient boolean approveForLowerOffices;
    @Column(name = "APPROVE_FOR_LOWER_OFFICES", columnDefinition="NUMBER(1)")
    private int approveForLowerOfficesInt;
    //
    private transient WorkFlowType workflowTypeDesc;
    @Column(name = "APPROVAL_TYPE", columnDefinition="NUMBER(1)")
    private int workflowType;
    //
    @Column(name = "AUTHORIZN_TYPE", columnDefinition="NUMBER(1)")
    @Enumerated(EnumType.ORDINAL)
    private AuthorizationType authorizationType;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    private transient ApprovalLimitEntity approvalLimit;
    //
    private transient java.util.Date lastApprovalDate;
    //
    private transient String userNameOfApprovalOfficer;

    public ApprovalStepEntity () {
        approvalStepID = 0;
        approvalDesc = "";
        approvalOfficerKey = 0;
        approveForLowerOfficesInt = 0;
        
        authorizationType = AuthorizationType.INTERNAL;

        initializeTransients();
    }

    public void initializeTransients() {
        setApprovalLimit(new ApprovalLimitEntity());
        setLastApprovalDate(null);
        setUserNameOfApprovalOfficer("");

        if(getApproveForLowerOfficesInt() == 0) {
            setApproveForLowerOffices(false);
        } else if(getApproveForLowerOfficesInt() == 1) {
            setApproveForLowerOffices(true);
        }
    }
    
    public boolean equals(Object o) {
        return ((o instanceof ApprovalStepEntity) 
                && (((ApprovalStepEntity)o).getApprovalDesc().equals(this.getApprovalDesc())));
    }

    /**
     * @return the approvalStepID
     */
    public int getApprovalStepID() {
        return approvalStepID;
    }

    /**
     * @param approvalStepID the approvalStepID to set
     */
    public void setApprovalStepID(int approvalStepID) {
        this.approvalStepID = approvalStepID;
    }

    /**
     * @return the approvalDesc
     */
    public String getApprovalDesc() {
        return approvalDesc;
    }

    /**
     * @param approvalDesc the approvalDesc to set
     */
    public void setApprovalDesc(String approvalDesc) {
        this.approvalDesc = approvalDesc;
    }

    /**
     * @return the approvalOfficerKey
     */
    public int getApprovalOfficerKey() {
        return approvalOfficerKey;
    }

    /**
     * @param approvalOfficerKey the approvalOfficerKey to set
     */
    public void setApprovalOfficerKey(int approvalOfficerKey) {
        this.approvalOfficerKey = approvalOfficerKey;
    }

    /**
     * @return the approveForLowerOffices
     */
    public boolean isApproveForLowerOffices() {
        return approveForLowerOffices;
    }

    /**
     * @param approveForLowerOffices the approveForLowerOffices to set
     */
    public void setApproveForLowerOffices(boolean approveForLowerOffices) {
        this.approveForLowerOffices = approveForLowerOffices;
    }

    /**
     * @return the approvalLimit
     */
    public ApprovalLimitEntity getApprovalLimit() {
        return approvalLimit;
    }

    /**
     * @param approvalLimit the approvalLimit to set
     */
    public void setApprovalLimit(ApprovalLimitEntity approvalLimit) {
        this.approvalLimit = approvalLimit;
    }

    /**
     * @return the lastApprovalDate
     */
    public java.util.Date getLastApprovalDate() {
        return lastApprovalDate;
    }

    /**
     * @param lastApprovalDate the lastApprovalDate to set
     */
    public void setLastApprovalDate(java.util.Date lastApprovalDate) {
        this.lastApprovalDate = lastApprovalDate;
    }

    /**
     * @return the userNameOfApprovalOfficer
     */
    public String getUserNameOfApprovalOfficer() {
        return userNameOfApprovalOfficer;
    }

    /**
     * @param userNameOfApprovalOfficer the userNameOfApprovalOfficer to set
     */
    public void setUserNameOfApprovalOfficer(String userNameOfApprovalOfficer) {
        this.userNameOfApprovalOfficer = userNameOfApprovalOfficer;
    }

    /**
     * @return the workflowType
     */
    public int getWorkflowType() {
        return workflowType;
    }

    /**
     * @param workflowType the workflowType to set
     */
    public void setWorkflowType(int workflowType) {
        this.workflowType = workflowType;
    }

    /**
     * @return the approveForLowerOfficesInt
     */
    public int getApproveForLowerOfficesInt() {
        return approveForLowerOfficesInt;
    }

    /**
     * @param approveForLowerOfficesInt the approveForLowerOfficesInt to set
     */
    public void setApproveForLowerOfficesInt(int approveForLowerOfficesInt) {
        this.approveForLowerOfficesInt = approveForLowerOfficesInt;
    }

    /**
     * @return the workflowTypeDesc
     */
    public WorkFlowType getWorkflowTypeDesc() {
        return workflowTypeDesc;
    }

    /**
     * @param workflowTypeDesc the workflowTypeDesc to set
     */
    public void setWorkflowTypeDesc(WorkFlowType workflowTypeDesc) {
        this.workflowTypeDesc = workflowTypeDesc;
    }

    /**
     * @return the authorizationType
     */
    public AuthorizationType getAuthorizationType() {
        return authorizationType;
    }

    /**
     * @param authorizationType the authorizationType to set
     */
    public void setAuthorizationType(AuthorizationType authorizationType) {
        this.authorizationType = authorizationType;
    }
    
    public void setAuthorizationType(int authorizationTypeOrd) {
        if(authorizationTypeOrd == AuthorizationType.INTERNAL.ordinal()) {
            this.setAuthorizationType(AuthorizationType.INTERNAL);
        } else if(authorizationTypeOrd == AuthorizationType.EXTERNAL.ordinal()) {
            this.setAuthorizationType(AuthorizationType.EXTERNAL);
        }
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
