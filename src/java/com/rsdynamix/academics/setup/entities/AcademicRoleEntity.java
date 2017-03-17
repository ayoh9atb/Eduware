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
@Table(name = "ACADEMIC_ROLE")
public class AcademicRoleEntity extends AbstractEntity  implements Serializable {
    
    @Id
    @Column(name = "ROLE_ID", columnDefinition = "NUMBER(2)")
    private int academicRoleID;
    //
    @Column(name = "ROLE_DESC", columnDefinition = "VARCHAR2(50)")
    private String academicRoleDesc;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public AcademicRoleEntity() {
        academicRoleID = 0;
        academicRoleDesc = "";
    }

    /**
     * @return the academicRoleID
     */
    public int getAcademicRoleID() {
        return academicRoleID;
    }

    /**
     * @param academicRoleID the academicRoleID to set
     */
    public void setAcademicRoleID(int academicRoleID) {
        this.academicRoleID = academicRoleID;
    }

    /**
     * @return the academicRoleDesc
     */
    public String getAcademicRoleDesc() {
        return academicRoleDesc;
    }

    /**
     * @param academicRoleDesc the academicRoleDesc to set
     */
    public void setAcademicRoleDesc(String academicRoleDesc) {
        this.academicRoleDesc = academicRoleDesc;
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
