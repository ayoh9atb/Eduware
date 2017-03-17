/*
 * PaymentStatusEntity.java
 *
 * Created on July 1, 2009, 3:23 PM
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
 * Entity class PaymentStatusEntity
 * 
 * @author p-aniah
 */
@Entity
@Table(name = "PAYMENT_STATUS")
public class PaymentStatusEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "PAYMENT_STATUS_ID", columnDefinition="Number(10)")
    private int paymentStatusID;
    //
    @Column(name = "PAYMENT_STATUS_DESC", columnDefinition="Varchar2(20)")
    private String paymentStatusDesc;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    
    /** Creates a new instance of PaymentStatusEntity */
    public PaymentStatusEntity() {
        setPaymentStatusID(0);
        setPaymentStatusDesc("");
    }
    
    public boolean equals(Object o){
        return ((o instanceof PaymentStatusEntity)
                && ((PaymentStatusEntity)o).getPaymentStatusID() == this.getPaymentStatusID());
    }
    
    public int getPaymentStatusID() {
        return paymentStatusID;
    }

    public void setPaymentStatusID(int paymentStatusID) {
        this.paymentStatusID = paymentStatusID;
    }

    public String getPaymentStatusDesc() {
        return paymentStatusDesc;
    }

    public void setPaymentStatusDesc(String paymentStatusDesc) {
        this.paymentStatusDesc = paymentStatusDesc;
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
