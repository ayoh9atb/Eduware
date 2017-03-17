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
@Table(name = "ACADA_SEMESTER")
public class AcademicSemesterEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "SEMESTER_ID", columnDefinition = "NUMBER(10)")
    private long semesterID;
    //
    @Column(name = "SEMESTER_TIER", columnDefinition = "VARCHAR2(6)")
    private String semesterTier;
    //
    @Column(name = "SESSION_ID", columnDefinition = "NUMBER(10)")
    private long sessionID;
    //
    @Column(name = "CURRENT_SEMESTER", columnDefinition = "VARCHAR2(1)")
    private boolean currentSemester;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    transient private String sessionPeriod;

    public AcademicSemesterEntity() {
        semesterID = 0;
        semesterTier = "";
        sessionID = 0;
        sessionPeriod = "";
        currentSemester = false;
        sessionPeriod = "";
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof AcademicSemesterEntity) {
            if((((AcademicSemesterEntity)o).getSemesterID() > 0) 
                    && (((AcademicSemesterEntity)o).getSemesterID() == this.getSemesterID())){
                eqls = true;
            } else {
                eqls = ((AcademicSemesterEntity)o).getSemesterTier().equals(this.getSemesterTier()) &&
                        (((AcademicSemesterEntity)o).getSessionID() == this.getSessionID());
            }
        }

        return eqls;
    }

    public long getSemesterID() {
        return this.semesterID;
    }

    public void setSemesterID(long semesterID) {
        this.semesterID = semesterID;
    }

    public String getSemesterTier() {
        return this.semesterTier;
    }

    public void setSemesterTier(String semesterTier) {
        this.semesterTier = semesterTier;
    }

    public long getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionPeriod() {
        return this.sessionPeriod;
    }

    public void setSessionPeriod(String sessionPeriod) {
        this.sessionPeriod = sessionPeriod;
    }

    public boolean isCurrentSemester() {
        return this.currentSemester;
    }

    public void setCurrentSemester(boolean currentSemester) {
        this.currentSemester = currentSemester;
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
