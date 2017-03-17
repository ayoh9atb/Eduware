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
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author p-aniah
 */
@Entity
@Table(name = "APPROVAL_LIMIT")
public class ApprovalLimitEntity extends AbstractEntity implements Serializable {
    
    @Id
    @Column(name = "APPROVAL_LIMIT_ID", columnDefinition="NUMBER(2)")
    private int approvalLimitID;
    //
    @Column(name = "APPROVAL_STEP_ID", columnDefinition="NUMBER(2)")
    private int approvalStepID;
    //
    @Column(name = "NEXT_APPROVAL_STEP_ID", columnDefinition="NUMBER(2)")
    private int nextApprovalStepID;
    //
    @Column(name = "APPROVAL_LIMIT_AMT", columnDefinition="DOUBLE PRECISION")
    private double approvalLimit;
    //
    private transient WorkFlowType workflowTypeDesc;
    @Column(name = "APPROVAL_TYPE", columnDefinition="NUMBER(1)")
    private int workflowType;
    //
    @Column(name = "APPROVAL_STEP_IMG_FILE", columnDefinition="VARCHAR2(255)")
    private String actionStepImageFileName;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    private transient int finalStepFlag;
    //
    private transient String approvalDesc;

    public ApprovalLimitEntity () {
        approvalLimitID = 0;
        approvalStepID = 0;
        nextApprovalStepID = 0;
        approvalLimit = 0;
        actionStepImageFileName = "";
        approvalDesc = "";
    }

    /**
     * @return the approvalLimitID
     */
    public int getApprovalLimitID() {
        return approvalLimitID;
    }

    /**
     * @param approvalLimitID the approvalLimitID to set
     */
    public void setApprovalLimitID(int approvalLimitID) {
        this.approvalLimitID = approvalLimitID;
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
     * @return the nextApprovalStepID
     */
    public int getNextApprovalStepID() {
        return nextApprovalStepID;
    }

    /**
     * @param nextApprovalStepID the nextApprovalStepID to set
     */
    public void setNextApprovalStepID(int nextApprovalStepID) {
        this.nextApprovalStepID = nextApprovalStepID;
    }

    /**
     * @return the approvalLimit
     */
    public double getApprovalLimit() {
        return approvalLimit;
    }

    /**
     * @param approvalLimit the approvalLimit to set
     */
    public void setApprovalLimit(double approvalLimit) {
        this.approvalLimit = approvalLimit;
    }

    /**
     * @return the actionStepImageFileName
     */
    public String getActionStepImageFileName() {
        return actionStepImageFileName;
    }

    /**
     * @param actionStepImageFileName the actionStepImageFileName to set
     */
    public void setActionStepImageFileName(String actionStepImageFileName) {
        this.actionStepImageFileName = actionStepImageFileName;
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
     * @return the finalStepFlag
     */
    public int getFinalStepFlag() {
        return finalStepFlag;
    }

    /**
     * @param finalStepFlag the finalStepFlag to set
     */
    public void setFinalStepFlag(int finalStepFlag) {
        this.finalStepFlag = finalStepFlag;
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
