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
@Table(name = "ACADEMIC_SESSION")
public class AcademicSessionEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "SESSION_ID", columnDefinition = "NUMBER(10)")
    private long acadaSessionID;
    //
    @Column(name = "CURRENT_SESSION", columnDefinition = "VARCHAR2(1)")
    private boolean currentSession;
    //
    @Column(name = "SESSION_PERIOD", columnDefinition = "VARCHAR2(10)")
    private String sessionPeriod;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public AcademicSessionEntity() {
        currentSession = false;
        sessionPeriod = "";
        acadaSessionID = 0;
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof AcademicSessionEntity) {
            if((((AcademicSessionEntity)o).getAcadaSessionID() > 0) 
                    && (((AcademicSessionEntity)o).getAcadaSessionID() == this.getAcadaSessionID())){
                eqls = true;
            } else {
                eqls = ((AcademicSessionEntity)o).getSessionPeriod().equals(this.getSessionPeriod());
            }
        }

        return eqls;
    }

    public boolean isCurrentSession() {
        return this.currentSession;
    }

    public void setCurrentSession(boolean currentSession) {
        this.currentSession = currentSession;
    }

    public String getSessionPeriod() {
        return this.sessionPeriod;
    }

    public void setSessionPeriod(String sessionPeriod) {
        this.sessionPeriod = sessionPeriod;
    }

    public long getAcadaSessionID() {
        return this.acadaSessionID;
    }

    public void setAcadaSessionID(long acadaSessionID) {
        this.acadaSessionID = acadaSessionID;
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
