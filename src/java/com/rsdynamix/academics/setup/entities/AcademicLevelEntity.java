/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.setup.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author p-aniah
 */
@Entity
@Table(name = "ACADEMIC_LEVEL")
public class AcademicLevelEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "ACADEMIC_LEVEL_ID", columnDefinition = "NUMBER(1)")
    private int academicLevelID;
    //
    @Column(name = "ACADEMIC_LEVEL_DESC", columnDefinition = "VARCHAR2(20)")
    private String academicLevelDesc;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public AcademicLevelEntity() {
        academicLevelID = 0;
        academicLevelDesc = "";
    }

    /**
     * @return the academicLevelID
     */
    public int getAcademicLevelID() {
        return academicLevelID;
    }

    /**
     * @param academicLevelID the academicLevelID to set
     */
    public void setAcademicLevelID(int academicLevelID) {
        this.academicLevelID = academicLevelID;
    }

    /**
     * @return the academicLevelDesc
     */
    public String getAcademicLevelDesc() {
        return academicLevelDesc;
    }

    /**
     * @param academicLevelDesc the academicLevelDesc to set
     */
    public void setAcademicLevelDesc(String academicLevelDesc) {
        this.academicLevelDesc = academicLevelDesc;
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
