/*
 * PayeeTypeEntity.java
 *
 * Created on July 1, 2009, 6:14 PM
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
 * Entity class PayeeTypeEntity
 * 
 * @author p-aniah
 */
@Entity
@Table(name = "PAYEE_TYPE")
public class PayeeTypeEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "PAYEE_TYPE_ID", columnDefinition="Number(10)")
    private int payeeTypeID;
    
    @Column(name = "PAYEE_TYPE_DESC", columnDefinition="Varchar2(50)")
    private String payeeTypeDesc;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    
    /** Creates a new instance of PayeeTypeEntity */
    public PayeeTypeEntity() {
        setPayeeTypeID(0);
        setPayeeTypeDesc("");
    }

    public boolean equals(Object o){
        return ((o instanceof PayeeTypeEntity)
                && ((PayeeTypeEntity)o).getPayeeTypeID() == this.getPayeeTypeID());
    }

    public int getPayeeTypeID() {
        return payeeTypeID;
    }

    public void setPayeeTypeID(int payeeTypeID) {
        this.payeeTypeID = payeeTypeID;
    }

    public String getPayeeTypeDesc() {
        return payeeTypeDesc;
    }

    public void setPayeeTypeDesc(String payeeTypeDesc) {
        this.payeeTypeDesc = payeeTypeDesc;
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
