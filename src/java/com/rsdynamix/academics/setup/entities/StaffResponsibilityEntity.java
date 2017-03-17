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
@Table(name = "STAFF_RESPONSIBILITY")
public class StaffResponsibilityEntity extends AbstractEntity  implements Serializable {

    @Id
    @Column(name = "STAFF_RESPONSIBILITY_ID", columnDefinition = "NUMBER(10)")
    private long staffResponsibilityID;
    //
    @Column(name = "STAFF_NUMBER", columnDefinition = "VARCHAR2(20)")
    private String staffNumber;
    //
    private transient String responsibilityDesc;
    @Column(name = "RESPONSIBILITY_ID", columnDefinition = "NUMBER(10)")
    private long responsibilityID;
    //
    private transient String sessionDesc;
    @Column(name = "SESSION_ID", columnDefinition = "NUMBER(10)")
    private long sessionID;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public StaffResponsibilityEntity() {
        staffResponsibilityID = 0;
        staffNumber = "";

        responsibilityDesc = "";
        responsibilityID = 0;

        sessionDesc = "";
        sessionID = 0;
    }

    /**
     * @return the staffResponsibilityID
     */
    public long getStaffResponsibilityID() {
        return staffResponsibilityID;
    }

    /**
     * @param staffResponsibilityID the staffResponsibilityID to set
     */
    public void setStaffResponsibilityID(long staffResponsibilityID) {
        this.staffResponsibilityID = staffResponsibilityID;
    }

    /**
     * @return the staffNumber
     */
    public String getStaffNumber() {
        return staffNumber;
    }

    /**
     * @param staffNumber the staffNumber to set
     */
    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    /**
     * @return the responsibilityID
     */
    public long getResponsibilityID() {
        return responsibilityID;
    }

    /**
     * @param responsibilityID the responsibilityID to set
     */
    public void setResponsibilityID(long responsibilityID) {
        this.responsibilityID = responsibilityID;
    }

    /**
     * @return the responsibilityDesc
     */
    public String getResponsibilityDesc() {
        return responsibilityDesc;
    }

    /**
     * @param responsibilityDesc the responsibilityDesc to set
     */
    public void setResponsibilityDesc(String responsibilityDesc) {
        this.responsibilityDesc = responsibilityDesc;
    }

    /**
     * @return the sessionDesc
     */
    public String getSessionDesc() {
        return sessionDesc;
    }

    /**
     * @param sessionDesc the sessionDesc to set
     */
    public void setSessionDesc(String sessionDesc) {
        this.sessionDesc = sessionDesc;
    }

    /**
     * @return the sessionID
     */
    public long getSessionID() {
        return sessionID;
    }

    /**
     * @param sessionID the sessionID to set
     */
    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
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
