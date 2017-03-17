/*
 * PaymentRequisitionEntity.java
 *
 * Created on July 9, 2009, 6:11 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rsdynamix.finance.requisition.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.codrellica.projects.commons.money.Money;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * Entity class PaymentRequisitionEntity
 *
 * @author p-aniah
 */
@Entity
@Table(name = "PAYMENT_REQ")
public class PaymentRequisitionEntity extends AbstractEntity implements Serializable {
    
    @Id
    @Column(name = "REQUISITION_NO", columnDefinition="Varchar2(20)")
    private String requisitionNumber;
    //
    @Column(name = "TOTAL_REQUISITION_AMT", columnDefinition="Double Precision")
    private BigDecimal totalRequisitionAmount;
    //
    @Column(name = "REQUISITION_DATE", columnDefinition="Date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date requistionDate;
    //
    @Column(name = "PAYMENT_RECEIPT_NO", columnDefinition="Varchar2(15)")
    private String paymentReceiptNumber;
    //
    @Column(name = "PAYMENT_DATE", columnDefinition="Date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date paymentDate;
    //
    @Column(name = "PAYMENT_AMT", columnDefinition="Double Precision")
    private BigDecimal paymentAmount;
    //
    @Column(name = "CLAIM_NO", columnDefinition="Varchar2(20)")
    private String claimNumber;
    //
    @Column(name = "DR_ACCT_CODE", columnDefinition="Varchar2(20)")
    private String debitAccountCode;
    //
    @Column(name = "CR_ACCT_CODE", columnDefinition="Varchar2(20)")
    private String creditAccountCode;
    //
    @Column(name = "NARRATION", columnDefinition="Varchar2(100)")
    private String narrationOfRequisition;
    //
    @Column(name = "PAYEE_TYPE", columnDefinition="Varchar2(50)")
    private String payeeType;
    //
    @Column(name = "PAYEE", columnDefinition="Varchar2(100)")
    private String payee;
    //
    @Column(name = "TOTAL_OUTSTANDING_AMT", columnDefinition="Double Precision")
    private BigDecimal totalOutstandingAmount;
    //
    @Column(name = "SETTLEMENT_TYPE", columnDefinition="NUMBER(1)")
    @Enumerated(EnumType.ORDINAL)
    private SettlementType settlementType;
    //
    @Column(name = "APPROVAL_STATE_ID", columnDefinition="NUMBER(12)")
    private long approvalStateID;
    //
    @Column(name = "APPROVED_BY", columnDefinition="Varchar2(50)")
    private String approver;
    //
    transient private List<RequisitionFlowStateEntity> workFlowStateList;
    //
    transient private Money totalReserveAmount;
    //
    transient private String paymentStatus;
    //
    transient private boolean selected;
    //
    transient private boolean approved;
    //
    transient private String payeeTypeDesc;
    //
    transient private List<ApprovalStepEntity> approvalStepList;
    //
    transient private ApprovalStepEntity currentApprovalStep;
    //
    transient private ApprovalStepEntity lastApprovalStep;
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    
    /** Creates a new instance of PaymentRequisitionEntity */
    public PaymentRequisitionEntity() {
        setRequisitionNumber("");
        setTotalRequisitionAmount(BigDecimal.ZERO);
        setRequistionDate(null);
        
        setPaymentReceiptNumber("");
        setPaymentDate(null);
        setPaymentAmount(BigDecimal.ZERO);
        
        debitAccountCode = "";
        creditAccountCode = "";
        
        setClaimNumber("");
        approvalStateID = 0;
        approver = "";
        
        payeeTypeDesc = "";
        payeeType = "";

        initializeTransient();
    }

    public void initializeTransient() {
        workFlowStateList = new ArrayList<RequisitionFlowStateEntity>();
        totalReserveAmount = new Money();
        paymentStatus = "";
        selected = false;
        approved = false;

        setApprovalStepList(new ArrayList<ApprovalStepEntity>());
        setCurrentApprovalStep(new ApprovalStepEntity());
        setLastApprovalStep(new ApprovalStepEntity());
    }

    public boolean equals(Object o){
        boolean isEq = false;
        if(o instanceof PaymentRequisitionEntity){
            isEq = ((PaymentRequisitionEntity)o).getRequisitionNumber().trim().equals(this.getRequisitionNumber().trim());
        }

        return isEq;
    }

    public void unsetCurrentState(){
        List<RequisitionFlowStateEntity> flowStateList = new ArrayList<RequisitionFlowStateEntity>();
        for(RequisitionFlowStateEntity flowState : getWorkFlowStateList()){
            flowState.setCurrentState(false);
            flowStateList.add(flowState);
        }

        setWorkFlowStateList(flowStateList);
    }

    public RequisitionFlowStateEntity getFlowStateByApprovalID(int approvalStepID){
        RequisitionFlowStateEntity flowState = null;
        
        for(RequisitionFlowStateEntity state : this.getWorkFlowStateList()){
            if(state.getApprovalStateID() == approvalStepID){
                flowState = state;
                break;
            }
        }

        return flowState;
    }

    public void setFlowStateToWorkFlowStateList(RequisitionFlowStateEntity flowState){
        if(this.getWorkFlowStateList().size() > 0){
            int statePos = this.getWorkFlowStateList().indexOf(flowState);
            this.getWorkFlowStateList().set(statePos, flowState);
        }else{
            this.getWorkFlowStateList().add(flowState);
        }
    }

    public RequisitionFlowStateEntity getCurrentFlowState(){
        RequisitionFlowStateEntity flowState = null;
        for(RequisitionFlowStateEntity state : this.getWorkFlowStateList()){
            if(state.isCurrentState()){
                flowState = state;
                break;
            }
        }

        return flowState;
    }

    public RequisitionFlowStateEntity getFlowStateByFlowStateID(int stateID){
        RequisitionFlowStateEntity flowState = null;
        for(RequisitionFlowStateEntity state : this.getWorkFlowStateList()){
            if(state.getWorkFlowStateID() == stateID){
                flowState = state;
                break;
            }
        }

        return flowState;
    }

    public RequisitionFlowStateEntity getPreviousFlowStateByFlowStateID(long stateID){
        RequisitionFlowStateEntity flowState1 = null;
        RequisitionFlowStateEntity flowState2 = null;

        for(RequisitionFlowStateEntity state : this.getWorkFlowStateList()){
            if(state.getWorkFlowStateID() == stateID){
                flowState1 = state;
                break;
            }
        }

        if(flowState1 != null){
            int index = this.getWorkFlowStateList().indexOf(flowState1);
            flowState2 = this.getWorkFlowStateList().get(index-1);
        }

        return flowState2;
    }

    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

    public BigDecimal getTotalRequisitionAmount() {
        return totalRequisitionAmount;
    }

    public void setTotalRequisitionAmount(BigDecimal totalRequisitionAmount) {
        this.totalRequisitionAmount = totalRequisitionAmount;
    }

    public Date getRequistionDate() {
        return requistionDate;
    }

    public void setRequistionDate(Date requistionDate) {
        this.requistionDate = requistionDate;
    }

    public String getPaymentReceiptNumber() {
        return paymentReceiptNumber;
    }

    public void setPaymentReceiptNumber(String paymentReceiptNumber) {
        this.paymentReceiptNumber = paymentReceiptNumber;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }
    
    /**
     * @return the narrationOfRequisition
     */
    public String getNarrationOfRequisition() {
        return narrationOfRequisition;
    }

    /**
     * @param narrationOfRequisition the narrationOfRequisition to set
     */
    public void setNarrationOfRequisition(String narrationOfRequisition) {
        this.narrationOfRequisition = narrationOfRequisition;
    }

    /**
     * @return the payee
     */
    public String getPayee() {
        return payee;
    }

    /**
     * @param payee the payee to set
     */
    public void setPayee(String payee) {
        this.payee = payee;
    }

    /**
     * @return the totalOutstandingAmount
     */
    public BigDecimal getTotalOutstandingAmount() {
        return totalOutstandingAmount;
    }

    /**
     * @param totalOutstandingAmount the totalOutstandingAmount to set
     */
    public void setTotalOutstandingAmount(BigDecimal totalOutstandingAmount) {
        this.totalOutstandingAmount = totalOutstandingAmount;
    }

    /**
     * @return the workFlowStateList
     */
    public List<RequisitionFlowStateEntity> getWorkFlowStateList() {
        return workFlowStateList;
    }

    /**
     * @param workFlowStateList the workFlowStateList to set
     */
    public void setWorkFlowStateList(List<RequisitionFlowStateEntity> workFlowStateList) {
        this.workFlowStateList = workFlowStateList;
    }

    /**
     * @return the totalReserveAmount
     */
    public Money getTotalReserveAmount() {
        return totalReserveAmount;
    }

    /**
     * @param totalReserveAmount the totalReserveAmount to set
     */
    public void setTotalReserveAmount(Money totalReserveAmount) {
        this.totalReserveAmount = totalReserveAmount;
    }

    /**
     * @return the paymentStatus
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * @param paymentStatus the paymentStatus to set
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
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
     * @return the approved
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * @param approved the approved to set
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * @return the approvalStepList
     */
    public List<ApprovalStepEntity> getApprovalStepList() {
        return approvalStepList;
    }

    /**
     * @param approvalStepList the approvalStepList to set
     */
    public void setApprovalStepList(List<ApprovalStepEntity> approvalStepList) {
        this.approvalStepList = approvalStepList;
    }

    /**
     * @return the currentApprovalStep
     */
    public ApprovalStepEntity getCurrentApprovalStep() {
        return currentApprovalStep;
    }

    /**
     * @param currentApprovalStep the currentApprovalStep to set
     */
    public void setCurrentApprovalStep(ApprovalStepEntity currentApprovalStep) {
        this.currentApprovalStep = currentApprovalStep;
    }

    /**
     * @return the lastApprovalStep
     */
    public ApprovalStepEntity getLastApprovalStep() {
        return lastApprovalStep;
    }

    /**
     * @param lastApprovalStep the lastApprovalStep to set
     */
    public void setLastApprovalStep(ApprovalStepEntity lastApprovalStep) {
        this.lastApprovalStep = lastApprovalStep;
    }

    /**
     * @return the approvalStateID
     */
    public long getApprovalStateID() {
        return approvalStateID;
    }

    /**
     * @param approvalStateID the approvalStateID to set
     */
    public void setApprovalStateID(long approvalStateID) {
        this.approvalStateID = approvalStateID;
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
     * @return the debitAccountCode
     */
    public String getDebitAccountCode() {
        return debitAccountCode;
    }

    /**
     * @param debitAccountCode the debitAccountCode to set
     */
    public void setDebitAccountCode(String debitAccountCode) {
        this.debitAccountCode = debitAccountCode;
    }

    /**
     * @return the creditAccountCode
     */
    public String getCreditAccountCode() {
        return creditAccountCode;
    }

    /**
     * @param creditAccountCode the creditAccountCode to set
     */
    public void setCreditAccountCode(String creditAccountCode) {
        this.creditAccountCode = creditAccountCode;
    }

    /**
     * @return the settlementType
     */
    public SettlementType getSettlementType() {
        return settlementType;
    }

    /**
     * @param settlementType the settlementType to set
     */
    public void setSettlementType(SettlementType settlementType) {
        this.settlementType = settlementType;
    }
    
    public void setSettlementType(int settlementTypeOrd) {
        if(settlementTypeOrd == SettlementType.COMMISSION_PAYOUT.ordinal()) {
            this.settlementType = SettlementType.COMMISSION_PAYOUT;
        } else if(settlementTypeOrd == SettlementType.PROCUREMENT_PAYOUT.ordinal()) {
            this.settlementType = SettlementType.PROCUREMENT_PAYOUT;
        }
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

    /**
     * @return the payeeType
     */
    public String getPayeeType() {
        return payeeType;
    }

    /**
     * @param payeeType the payeeType to set
     */
    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
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
    
}
