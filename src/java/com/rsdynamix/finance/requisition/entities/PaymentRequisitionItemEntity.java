/*
 * PaymentRequisitionItemEntity.java
 *
 * Created on July 2, 2009, 12:54 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rsdynamix.finance.requisition.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class PaymentRequisitionItemEntity
 * 
 * 
 * @author p-aniah
 */
@Entity
@Table(name = "PAYMENT_REQ_ITEM")
public class PaymentRequisitionItemEntity extends AbstractEntity implements Serializable {
    
    @Id
    @Column(name = "REQUISITION_ITEM_ID", columnDefinition="Number(15)")
    private long requisitionItemID;
    //
    @Column(name = "REQUISITION_NUMBER", columnDefinition="Varchar2(20)")
    private String requisitionNumber;
    //
    @Column(name = "REQUISITION_AMOUNT", columnDefinition="Double Precision")
    private BigDecimal requisitionAmount;
    //
    @Column(name = "PAYEE", columnDefinition="Varchar2(100)")
    private String payee;
    //
    @Column(name = "RESERVE_TYPE_ID", columnDefinition="Number(2)")
    private long reserveTypeID;
    //
    @Column(name = "RESERVE_DESC", columnDefinition="VARCHAR2(255)")
    private String reserveDesc;
    //
    @Column(name = "CLAIM_NUMBER", columnDefinition="VARCHAR2(20)")
    private String claimNumber;
    //
    @Column(name = "PAYEE_TYPE", columnDefinition="Varchar2(50)")
    private String payeeType;
    //
    @Column(name = "RESERVE_ID", columnDefinition="Number(10)")
    private int reserveID;
    //
    @Column(name = "NARRATION", columnDefinition="Varchar2(100)")
    private String narrationOfRequisition;
    //
    @Column(name = "RESERVE_KEY", columnDefinition="Number(10)")
    private long reserveKey;
    //
    @Column(name = "RESERVE_AMOUNT", columnDefinition="Double Precision")
    private BigDecimal reserveAmount;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    transient private BigDecimal outstandingAmount;
    //
    transient private boolean fullReqPayment;
    //
    transient private boolean selected;
    //
    transient private String payeeTypeDesc;
    //
    transient private String reserveType;
        
    /**
     * Creates a new instance of PaymentRequisitionItemEntity
     */
    public PaymentRequisitionItemEntity() {
        setRequisitionItemID(0L);
        setRequisitionNumber("");
        setRequisitionAmount(BigDecimal.ZERO);

        reserveDesc = "";
        claimNumber = "";

        setPayee("");
        reserveTypeID = 0;
        setPayeeType("");
        
        setReserveID(0);
        setNarrationOfRequisition("");

        outstandingAmount = BigDecimal.ZERO;
        fullReqPayment = false;
        selected = false;
        payeeTypeDesc = "";
        reserveType = "";

        setSuppressAuditTrail(true);
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public BigDecimal getRequisitionAmount() {
        return requisitionAmount;
    }

    public void setRequisitionAmount(BigDecimal requisitionAmount) {
        this.requisitionAmount = requisitionAmount;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }

    public int getReserveID() {
        return reserveID;
    }

    public void setReserveID(int reserveID) {
        this.reserveID = reserveID;
    }

    public String getNarrationOfRequisition() {
        return narrationOfRequisition;
    }

    public void setNarrationOfRequisition(String narrationOfRequisition) {
        this.narrationOfRequisition = narrationOfRequisition;
    }

    public long getRequisitionItemID() {
        return requisitionItemID;
    }

    public void setRequisitionItemID(long requisitionItemID) {
        this.requisitionItemID = requisitionItemID;
    }

    /**
     * @return the reserveKey
     */
    public long getReserveKey() {
        return reserveKey;
    }

    /**
     * @param reserveKey the reserveKey to set
     */
    public void setReserveKey(long reserveKey) {
        this.reserveKey = reserveKey;
    }

    /**
     * @return the reserveAmount
     */
    public BigDecimal getReserveAmount() {
        return reserveAmount;
    }

    /**
     * @param reserveAmount the reserveAmount to set
     */
    public void setReserveAmount(BigDecimal reserveAmount) {
        this.reserveAmount = reserveAmount;
    }

    /**
     * @return the reserveTypeID
     */
    public long getReserveTypeID() {
        return reserveTypeID;
    }

    /**
     * @param reserveTypeID the reserveTypeID to set
     */
    public void setReserveTypeID(long reserveTypeID) {
        this.reserveTypeID = reserveTypeID;
    }

    /**
     * @return the reserveDesc
     */
    public String getReserveDesc() {
        return reserveDesc;
    }

    /**
     * @param reserveDesc the reserveDesc to set
     */
    public void setReserveDesc(String reserveDesc) {
        this.reserveDesc = reserveDesc;
    }

    /**
     * @return the claimNumber
     */
    public String getClaimNumber() {
        return claimNumber;
    }

    /**
     * @param claimNumber the claimNumber to set
     */
    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    /**
     * @return the outstandingAmount
     */
    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    /**
     * @param outstandingAmount the outstandingAmount to set
     */
    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    /**
     * @return the fullReqPayment
     */
    public boolean isFullReqPayment() {
        return fullReqPayment;
    }

    /**
     * @param fullReqPayment the fullReqPayment to set
     */
    public void setFullReqPayment(boolean fullReqPayment) {
        this.fullReqPayment = fullReqPayment;
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the payeeTypeDesc
     */
    public String getPayeeTypeDesc() {
        return payeeTypeDesc;
    }

    /**
     * @param payeeTypeDesc the payeeTypeDesc to set
     */
    public void setPayeeTypeDesc(String payeeTypeDesc) {
        this.payeeTypeDesc = payeeTypeDesc;
    }

    /**
     * @return the reserveType
     */
    public String getReserveType() {
        return reserveType;
    }

    /**
     * @param reserveType the reserveType to set
     */
    public void setReserveType(String reserveType) {
        this.reserveType = reserveType;
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
