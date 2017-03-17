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
@Table(name = "LECTURE_VENUE")
public class LectureVenueEntity extends AbstractEntity implements Serializable {
    
    @Id
    @Column(name = "LECTURE_VENUE_ID", columnDefinition = "NUMBER(3)")
    private long lectureVenuID;
    //
    @Column(name = "LECTURE_VENUE_DESC", columnDefinition = "VARCHAR2(100)")
    private String lectureVenueDesc;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public LectureVenueEntity() {
        lectureVenuID = 0;
        lectureVenueDesc = "";
    }

    /**
     * @return the lectureVenuID
     */
    public long getLectureVenuID() {
        return lectureVenuID;
    }

    /**
     * @param lectureVenuID the lectureVenuID to set
     */
    public void setLectureVenuID(long lectureVenuID) {
        this.lectureVenuID = lectureVenuID;
    }

    /**
     * @return the lectureVenueDesc
     */
    public String getLectureVenueDesc() {
        return lectureVenueDesc;
    }

    /**
     * @param lectureVenueDesc the lectureVenueDesc to set
     */
    public void setLectureVenueDesc(String lectureVenueDesc) {
        this.lectureVenueDesc = lectureVenueDesc;
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
