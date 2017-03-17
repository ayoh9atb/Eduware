/*
 * RequisitionFlowStateEntity.java
 *
 * Created on July 1, 2009, 6:39 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rsdynamix.finance.requisition.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Entity class RequisitionFlowStateEntity
 * 
 * 
 * @author p-aniah
 */
@Entity
@Table(name = "REQ_FLOW_STATE")
public class RequisitionFlowStateEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "WORK_FLOW_STATE_ID", columnDefinition = "Number(15)")
    private long workFlowStateID;
    //
    @Column(name = "REQUISITION_NO", columnDefinition = "Varchar2(20)")
    private String requisitionNumber;
    //
    @Column(name = "APPROVAL_STATE_ID", columnDefinition = "Number(10)")
    private int approvalStateID;
    //
    private transient boolean currentState;
    @Column(name = "CURRENT_STATE", columnDefinition = "Varchar2(1)")
    private String currentStateFlg;
    //
    @Column(name = "DATE_RECEIVED", columnDefinition = "Date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateReceived;
    //
    @Column(name = "DATE_PROCESSED", columnDefinition = "Date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateProcessed;
    //
    @Column(name = "APPROVED_BY", columnDefinition = "Varchar2(50)")
    private String userNameOfApprovalOfficer;
    //
    @Column(name = "WF_COMMENT", columnDefinition = "Varchar2(255)")
    private String comment;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    transient private String approvalStateDesc;

    /**
     * Creates a new instance of RequisitionFlowStateEntity
     */
    public RequisitionFlowStateEntity() {
        setRequisitionNumber("");

        setWorkFlowStateID(0L);

        setApprovalStateID(0);
        setCurrentState(false);
        currentStateFlg = "";

        setUserNameOfApprovalOfficer("");

        comment = "";

        approvalStateDesc = "";

        setSuppressAuditTrail(true);
    }

    public void initializeTransient() {
        if (this.getCurrentStateFlg().equals("1")) {
            setCurrentState(true);
        } else if (this.getCurrentStateFlg().equals("0")) {
            setCurrentState(false);
        }
    }

    public boolean equals(Object o) {
        boolean isEq = false;
        
        if (o instanceof RequisitionFlowStateEntity) {
            isEq = ((RequisitionFlowStateEntity) o).getWorkFlowStateID() == this.getWorkFlowStateID();
        }

        return isEq;
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public int getApprovalStateID() {
        return approvalStateID;
    }

    public void setApprovalStateID(int approvalStateID) {
        this.approvalStateID = approvalStateID;
    }

    public Date getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(Date dateReceived) {
        this.dateReceived = dateReceived;
    }

    public Date getDateProcessed() {
        return dateProcessed;
    }

    public void setDateProcessed(Date dateProcessed) {
        this.dateProcessed = dateProcessed;
    }

    public boolean isCurrentState() {
        return currentState;
    }

    public void setCurrentState(boolean currentState) {
        this.currentState = currentState;
        
        if(currentState) {
            setCurrentStateFlg("1");
        } else {
            setCurrentStateFlg("0");
        }
    }

    public String getUserNameOfApprovalOfficer() {
        return this.userNameOfApprovalOfficer;
    }

    public void setUserNameOfApprovalOfficer(String userNameOfApprovalOfficer) {
        this.userNameOfApprovalOfficer = userNameOfApprovalOfficer;
    }

    public long getWorkFlowStateID() {
        return workFlowStateID;
    }

    public void setWorkFlowStateID(long workFlowStateID) {
        this.workFlowStateID = workFlowStateID;
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the approvalStateDesc
     */
    public String getApprovalStateDesc() {
        return approvalStateDesc;
    }

    /**
     * @param approvalStateDesc the approvalStateDesc to set
     */
    public void setApprovalStateDesc(String approvalStateDesc) {
        this.approvalStateDesc = approvalStateDesc;
    }

    /**
     * @return the currentStateFlg
     */
    public String getCurrentStateFlg() {
        return currentStateFlg;
    }

    /**
     * @param currentStateFlg the currentStateFlg to set
     */
    public void setCurrentStateFlg(String currentStateFlg) {
        this.currentStateFlg = currentStateFlg;
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
