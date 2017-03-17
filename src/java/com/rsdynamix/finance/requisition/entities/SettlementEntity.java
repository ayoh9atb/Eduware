/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.finance.requisition.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.finance.requisition.entities.SettlementType;
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
@Table(name = "POLICY_SETTLEMENT")
public class SettlementEntity extends AbstractEntity implements Serializable {
    
    @Id
    @Column(name = "SETTLEMENT_ID", columnDefinition="NUMBER(12)")
    private long settlementID;
    //
    @Column(name = "REFERENCE_NUMBER", columnDefinition="VARCHAR2(20)")
    private String referenceNumber;
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
    @Column(name = "SETTLEMENT_TYPE", columnDefinition="NUMBER(1)")
    @Enumerated(EnumType.ORDINAL)
    private SettlementType settlementType;
    //
    @Column(name = "INSTALMENT_NUMBER", columnDefinition="NUMBER(10)")
    private int instalmentNumber;
    //
    @Column(name = "INSTAL_START_DATE", columnDefinition="DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date instalStartDate;
    //
    @Column(name = "INSTAL_END_DATE", columnDefinition="DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date instalEndDate;
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
    @Column(name = "ENTRY_DATE", columnDefinition="DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date entryDate;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "PAYOUT_STATUS", columnDefinition = "NUMBER(1)")
    private PayoutStatus payoutStatus;
    //
    private transient String payoutStatusDesc;
    //
    private transient int payeeTypeID;
    //
    private transient String clientName;
    //
    private transient String bankName;
    //
    private transient String currencyCode;
    //
    private transient AbstractEntity businessEntity;
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public SettlementEntity () {
        settlementID = 0;
        referenceNumber = "";
        settlementCode = "";
        settlementAmount = 0;
        instalmentNumber = 0;
        expenseDesc = "";
        
        debitAccountCode = "";
        creditAccountCode = "";
        
        instalStartDate = null;
        instalEndDate = null;
        
        payoutStatus = PayoutStatus.NONE;
        payoutStatusDesc = "";
        
        entryDate = null;
        
        bankCode = "";
        accountName = "";
        accountNumber = "";
        
        payeeTypeID = 0;
        clientName = "";
        bankName = "";
        currencyCode = "";
    }
    
    public boolean equals(Object o) {
        return ((o instanceof SettlementEntity) 
                && (((SettlementEntity)o).getReferenceNumber().equals(this.getReferenceNumber()))
                && (((SettlementEntity)o).getInstalmentNumber() == this.getInstalmentNumber()));
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
     * @return the instalmentNumber
     */
    public int getInstalmentNumber() {
        return instalmentNumber;
    }

    /**
     * @param instalmentNumber the instalmentNumber to set
     */
    public void setInstalmentNumber(int instalmentNumber) {
        this.instalmentNumber = instalmentNumber;
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
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
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
     * @return the instalStartDate
     */
    public Date getInstalStartDate() {
        return instalStartDate;
    }

    /**
     * @param instalStartDate the instalStartDate to set
     */
    public void setInstalStartDate(Date instalStartDate) {
        this.instalStartDate = instalStartDate;
    }

    /**
     * @return the instalEndDate
     */
    public Date getInstalEndDate() {
        return instalEndDate;
    }

    /**
     * @param instalEndDate the instalEndDate to set
     */
    public void setInstalEndDate(Date instalEndDate) {
        this.instalEndDate = instalEndDate;
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
        if(settlementTypeOrd == SettlementType.PROCUREMENT_PAYOUT.ordinal()) {
            this.setSettlementType(SettlementType.PROCUREMENT_PAYOUT);
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
     * @return the businessEntity
     */
    public AbstractEntity getBusinessEntity() {
        return businessEntity;
    }

    /**
     * @param businessEntity the businessEntity to set
     */
    public void setBusinessEntity(AbstractEntity businessEntity) {
        this.businessEntity = businessEntity;
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
