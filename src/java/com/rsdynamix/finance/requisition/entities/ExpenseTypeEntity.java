/*
 * ExpenseTypeEntity.java
 *
 * Created on June 3, 2009, 12:23 PM
 *
 * To change this template, choose Tools | Template Manager
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
 * Entity class ExpenseTypeEntity
 * 
 * @author p-aniah
 */
@Entity
@Table(name = "EXPENSE_TYPE")
public class ExpenseTypeEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "EXPENSE_TYPE_ID", columnDefinition="Number(10)")
    private int expenseTypeID;
    
    @Column(name = "EXPENSE_TYPE_DESC", columnDefinition="Varchar2(100)")
    private String expenseTypeDesc;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    
    /** Creates a new instance of ExpenseTypeEntity */
    public ExpenseTypeEntity() {
        setExpenseTypeID(0);
        setExpenseTypeDesc("");
    }

    public boolean equals(Object o){
        return ((o instanceof ExpenseTypeEntity)
                && ((ExpenseTypeEntity)o).getExpenseTypeID() == this.getExpenseTypeID());
    }

    public int getExpenseTypeID() {
        return expenseTypeID;
    }

    public void setExpenseTypeID(int expenseTypeID) {
        this.expenseTypeID = expenseTypeID;
    }

    public String getExpenseTypeDesc() {
        return expenseTypeDesc;
    }

    public void setExpenseTypeDesc(String expenseTypeDesc) {
        this.expenseTypeDesc = expenseTypeDesc;
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
