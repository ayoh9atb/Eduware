/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.finance.requisition.entities;

import com.codrellica.projects.commons.DateUtil;
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
 *
 * @author p-aniah
 */
@Entity
@Table(name = "GRP_POLICY_SETTLEMENT")
public class GroupSettlementEntity extends AbstractEntity implements Serializable {
    
    @Id
    @Column(name = "SETTLEMENT_ID", columnDefinition="NUMBER(12)")
    private long settlementID;
    //
    @Column(name = "SETTLEMENT_CODE", columnDefinition="VARCHAR2(20)")
    private String settlementCode;
    //
    @Column(name = "SETTLEMENT_NARRATION", columnDefinition="VARCHAR2(150)")
    private String narration;
    //
    @Column(name = "SETTLEMENT_AMOUNT", columnDefinition="DOUBLE PRECISION")
    private double settlementAmount;
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
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public GroupSettlementEntity () {
        settlementID = 0;
        settlementCode = "";
        narration = "";
        settlementAmount = 0;
        
        entryDate = null;
    }
    
    public boolean equals(Object o) {
        return ((o instanceof GroupSettlementEntity) 
                && (DateUtil.isSameDate(((GroupSettlementEntity)o).getEntryDate(), this.getEntryDate())));
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
     * @return the narration
     */
    public String getNarration() {
        return narration;
    }

    /**
     * @param narration the narration to set
     */
    public void setNarration(String narration) {
        this.narration = narration;
    }
    
}
