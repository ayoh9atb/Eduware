/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.finance.requisition.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author p-aniah
 */
@Entity
@Table(name = "PAYMENT")
public class PaymentEntity extends AbstractEntity implements Serializable {
    
    @Id
    @Column(name = "PAYMENT_CODE", columnDefinition="Varchar2(20)")
    private String paymentCode;
    //
    @Column(name = "REQUISTION_NUMBER", columnDefinition="Varchar2(20)")
    private String requistionNumber;
    //
    @Column(name = "BUDGET_ENTRY_ID", columnDefinition = "NUMBER(10)")
    private long budgetEntryID;
    //
    @Column(name = "AMOUNT", columnDefinition="Double Precision")
    private BigDecimal amount;
    //
    @Column(name = "PAYMENT_DATE", columnDefinition="Date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date paymentDate;
    //
    @Column(name = "FULL_NAME", columnDefinition="Varchar2(100)")
    private String fullName;
    //
    @Column(name = "CHEQUE_NUMBER", columnDefinition="Varchar2(20)")
    private String chequeNumber;
    //
    @Column(name = "BANK_CODE", columnDefinition="Varchar2(10)")
    private String bankCode;
    //
    @Column(name = "BANK_SORT_CODE", columnDefinition="Varchar2(10)")
    private String bankSortCode;
    //
    @Column(name = "STAFF_CODE", columnDefinition="Varchar2(20)")
    private String staffCode;
    //
    @Column(name = "CURRENCY_CODE", columnDefinition="Varchar2(3)")
    private String currencyCode;
    //
    @Column(name = "DR_ACCT_CODE", columnDefinition="Varchar2(20)")
    private String debitAccountCode;
    //
    @Column(name = "CR_ACCT_CODE", columnDefinition="Varchar2(20)")
    private String creditAccountCode;
    //
    @Column(name = "PAYMENT_TYPE_ID", columnDefinition = "NUMBER(10)")
    private long paymentTypeID;
    //
    @Column(name = "ENTRY_DATE", columnDefinition="DATE")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date entryDate;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition="NUMBER(3)")
    private int approvalStatusID;
    //
    @Column(name = "APPROVER_NAME", columnDefinition="Varchar2(60)")
    private String approver = "";
    //
    transient private String bank;
    //
    transient private String bankBranch;
    //
    transient private boolean selected;
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public PaymentEntity(){
        paymentCode = "";
        requistionNumber = "";
        budgetEntryID = 0;
        setAmount(BigDecimal.ZERO);
        setPaymentDate(null);
        setFullName("");
        setChequeNumber("");

        setBankCode("");
        setBankSortCode("");

        setStaffCode("");
        currencyCode = "";
        
        debitAccountCode = "";
        creditAccountCode = "";
        entryDate = null;
        
        paymentTypeID = 0;

        bank = "";
        bankBranch = "";
        selected = false;
    }

    public boolean equals(Object o){
        return ((o instanceof PaymentEntity) &&
                ((PaymentEntity)o).getPaymentCode().equals(this.getPaymentCode()));
    }

    /**
     * @return the paymentCode
     */
    public String getPaymentCode() {
        return paymentCode;
    }

    /**
     * @param paymentCode the paymentCode to set
     */
    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    /**
     * @return the requistionNumber
     */
    public String getRequistionNumber() {
        return requistionNumber;
    }

    /**
     * @param requistionNumber the requistionNumber to set
     */
    public void setRequistionNumber(String requistionNumber) {
        this.requistionNumber = requistionNumber;
    }

    /**
     * @return the amount
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * @return the paymentDate
     */
    public Date getPaymentDate() {
        return paymentDate;
    }

    /**
     * @param paymentDate the paymentDate to set
     */
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * @return the fullName
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the chequeNumber
     */
    public String getChequeNumber() {
        return chequeNumber;
    }

    /**
     * @param chequeNumber the chequeNumber to set
     */
    public void setChequeNumber(String chequeNumber) {
        this.chequeNumber = chequeNumber;
    }

    /**
     * @return the staffCode
     */
    public String getStaffCode() {
        return staffCode;
    }

    /**
     * @param staffCode the staffCode to set
     */
    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
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
     * @return the bankSortCode
     */
    public String getBankSortCode() {
        return bankSortCode;
    }

    /**
     * @param bankSortCode the bankSortCode to set
     */
    public void setBankSortCode(String bankSortCode) {
        this.bankSortCode = bankSortCode;
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
     * @return the bank
     */
    public String getBank() {
        return bank;
    }

    /**
     * @param bank the bank to set
     */
    public void setBank(String bank) {
        this.bank = bank;
    }

    /**
     * @return the bankBranch
     */
    public String getBankBranch() {
        return bankBranch;
    }

    /**
     * @param bankBranch the bankBranch to set
     */
    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
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
     * @return the paymentTypeID
     */
    public long getPaymentTypeID() {
        return paymentTypeID;
    }

    /**
     * @param paymentTypeID the paymentTypeID to set
     */
    public void setPaymentTypeID(long paymentTypeID) {
        this.paymentTypeID = paymentTypeID;
    }

    /**
     * @return the budgetEntryID
     */
    public long getBudgetEntryID() {
        return budgetEntryID;
    }

    /**
     * @param budgetEntryID the budgetEntryID to set
     */
    public void setBudgetEntryID(long budgetEntryID) {
        this.budgetEntryID = budgetEntryID;
    }
    
}
