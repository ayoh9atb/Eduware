/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.setup.entities;

import java.io.Serializable;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 *
 * @author root
 */
@Entity
@Table(name = "SCHOOL_TIER_TYPE")
public class SchoolTierTypeEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "SCHOOL_TIER_ID", columnDefinition = "NUMBER(1)")
    private long schoolTierID;
    //
    @Column(name = "SCHOOL_TIER_NAME", columnDefinition = "VARCHAR2(60)")
    private String schoolTierName;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public SchoolTierTypeEntity() {
        schoolTierID = 0;
        schoolTierName = "";
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof SchoolTierTypeEntity) {
            if((((SchoolTierTypeEntity)o).getSchoolTierID() > 0) 
                    && (((SchoolTierTypeEntity)o).getSchoolTierID() == this.getSchoolTierID())){
                eqls = true;
            } else {
                eqls = ((SchoolTierTypeEntity)o).getSchoolTierName().equals(this.getSchoolTierName());
            }
        }

        return eqls;
    }

    public long getSchoolTierID() {
        return this.schoolTierID;
    }

    public void setSchoolTierID(long schoolTierID) {
        this.schoolTierID = schoolTierID;
    }

    public String getSchoolTierName() {
        return this.schoolTierName;
    }

    public void setSchoolTierName(String schoolTierName) {
        this.schoolTierName = schoolTierName;
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
