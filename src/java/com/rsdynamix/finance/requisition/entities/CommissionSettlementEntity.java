/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.finance.requisition.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author p-aniah
 */
@Entity
@Table(name = "COMMISSION_SETTLEMENT")
public class CommissionSettlementEntity extends AbstractEntity implements Serializable {
    
    @Id
    @Column(name = "SETTLEMENT_ID", columnDefinition="NUMBER(12)")
    private long settlementID;
    //
    @Column(name = "AGENCY_CODE", columnDefinition="VARCHAR2(20)")
    private String agencyCode;
    //
    @Column(name = "REFERENCE_NUMBER", columnDefinition="VARCHAR2(20)")
    private String referenceNumber;
    //
    @Column(name = "COMMISSION_ID", columnDefinition="NUMBER(12)")
    private long commissionID;
    //
    @Column(name = "SETTLEMENT_CODE", columnDefinition="VARCHAR2(20)")
    private String settlementCode;
    //
    @Column(name = "GRP_SETTLEMENT_CODE", columnDefinition="VARCHAR2(20)")
    private String groupSettlementCode;
    //
    @Column(name = "SETTLEMENT_AMOUNT", columnDefinition="DOUBLE PRECISION")
    private double settlementAmount;
    //
    @Column(name = "EXPENSE_DESC", columnDefinition="VARCHAR2(256)")
    private String expenseDesc;
    //
    @Column(name = "DR_ACCT_CODE", columnDefinition="Varchar2(20)")
    private String debitAccountCode;
    //
    @Column(name = "CR_ACCT_CODE", columnDefinition="Varchar2(20)")
    private String creditAccountCode;
    //
    @Column(name = "BANK_CODE", columnDefinition = "VARCHAR2(10)")
    private String bankCode;
    //
    @Column(name = "ACCOUNT_NAME", columnDefinition = "VARCHAR2(100)")
    private String accountName;
    //
    @Column(name = "ACCT_NUMBER", columnDefinition = "VARCHAR2(25)")
    private String accountNumber;
    //
    @Column(name = "TRANSACT_SRC", columnDefinition = "VARCHAR2(80)")
    private String transactionSource;
    //
    @Column(name = "CURRENCY_CODE", columnDefinition = "VARCHAR2(3)")
    private String currencyCode;
    //
    @Column(name = "ENTRY_DATE", columnDefinition="DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date entryDate;
    //
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "PAYOUT_STATUS", columnDefinition = "NUMBER(1)")
    private PayoutStatus payoutStatus;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    private transient String payoutStatusDesc;
    //
    private transient int payeeTypeID;
    //
    private transient String agentName;
    //
    private transient String bankName;
    //
    private transient String tranSourceCode;
    

    public CommissionSettlementEntity () {
        settlementID = 0;
        agencyCode = "";
        referenceNumber = "";
        settlementCode = "";
        settlementAmount = 0;
        commissionID = 0;
        expenseDesc = "";
        
        transactionSource = "";
        currencyCode = "";
        
        debitAccountCode = "";
        creditAccountCode = "";
        
        entryDate = null;
        groupSettlementCode = "";
        
        bankCode = "";
        accountName = "";
        accountNumber = "";
        
        payeeTypeID = 0;
        agentName = "";
        
        bankName = "";
        tranSourceCode = "";
    }
    
    public boolean equals(Object o) {
        return ((o instanceof CommissionSettlementEntity) 
                && (((CommissionSettlementEntity)o).getAgencyCode().equals(this.getAgencyCode()))
                && (((CommissionSettlementEntity)o).getCommissionID() == this.getCommissionID()));
    }

    /**
     * @return the settlementID
     */
    public long getSettlementID() {
        return settlementID;
    }

    /**
     * @param settlementID the settlementID to set
     */
    public void setSettlementID(long settlementID) {
        this.settlementID = settlementID;
    }

    /**
     * @return the settlementAmount
     */
    public double getSettlementAmount() {
        return settlementAmount;
    }

    /**
     * @param settlementAmount the settlementAmount to set
     */
    public void setSettlementAmount(double settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    /**
     * @return the entryDate
     */
    public Date getEntryDate() {
        return entryDate;
    }

    /**
     * @param entryDate the entryDate to set
     */
    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    /**
     * @return the settlementCode
     */
    public String getSettlementCode() {
        return settlementCode;
    }

    /**
     * @param settlementCode the settlementCode to set
     */
    public void setSettlementCode(String settlementCode) {
        this.settlementCode = settlementCode;
    }

    /**
     * @return the expenseDesc
     */
    public String getExpenseDesc() {
        return expenseDesc;
    }

    /**
     * @param expenseDesc the expenseDesc to set
     */
    public void setExpenseDesc(String expenseDesc) {
        this.expenseDesc = expenseDesc;
    }

    /**
     * @return the agencyCode
     */
    public String getAgencyCode() {
        return agencyCode;
    }

    /**
     * @param agencyCode the agencyCode to set
     */
    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }

    /**
     * @return the commissionID
     */
    public long getCommissionID() {
        return commissionID;
    }

    /**
     * @param commissionID the commissionID to set
     */
    public void setCommissionID(long commissionID) {
        this.commissionID = commissionID;
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
     * @return the payeeTypeID
     */
    public int getPayeeTypeID() {
        return payeeTypeID;
    }

    /**
     * @param payeeTypeID the payeeTypeID to set
     */
    public void setPayeeTypeID(int payeeTypeID) {
        this.payeeTypeID = payeeTypeID;
    }

    /**
     * @return the agentName
     */
    public String getAgentName() {
        return agentName;
    }

    /**
     * @param agentName the agentName to set
     */
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    /**
     * @return the bankCode
     */
    public String getBankCode() {
        return bankCode;
    }

    /**
     * @param bankCode the bankCode to set
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    /**
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return the accountNumber
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName the bankName to set
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * @return the groupSettlementCode
     */
    public String getGroupSettlementCode() {
        return groupSettlementCode;
    }

    /**
     * @param groupSettlementCode the groupSettlementCode to set
     */
    public void setGroupSettlementCode(String groupSettlementCode) {
        this.groupSettlementCode = groupSettlementCode;
    }

    /**
     * @return the referenceNumber
     */
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber the referenceNumber to set
     */
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    /**
     * @return the tranSourceCode
     */
    public String getTranSourceCode() {
        return tranSourceCode;
    }

    /**
     * @param tranSourceCode the tranSourceCode to set
     */
    public void setTranSourceCode(String tranSourceCode) {
        this.tranSourceCode = tranSourceCode;
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

    /**
     * @return the transactionSource
     */
    public String getTransactionSource() {
        return transactionSource;
    }

    /**
     * @param transactionSource the transactionSource to set
     */
    public void setTransactionSource(String transactionSource) {
        this.transactionSource = transactionSource;
    }

    /**
     * @return the currencyCode
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * @param currencyCode the currencyCode to set
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    /**
     * @return the payoutStatus
     */
    public PayoutStatus getPayoutStatus() {
        if(payoutStatus == null ){
            payoutStatus = PayoutStatus.NONE;
        }
        return payoutStatus;
    }

    /**
     * @param payoutStatus the payoutStatus to set
     */
    public void setPayoutStatus(PayoutStatus payoutStatus) {
        this.payoutStatus = payoutStatus;
    }

    /**
     * @return the payoutStatusDesc
     */
    public String getPayoutStatusDesc() {        
        return getPayoutStatus().getDescription();
    }

    /**
     * @param payoutStatusDesc the payoutStatusDesc to set
     */
    public void setPayoutStatusDesc(String payoutStatusDesc) {
        this.payoutStatusDesc = payoutStatusDesc;
    }

    
}
