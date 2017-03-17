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
import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author root
 */
@Entity
@Table(name = "ACADA_QUALIFICATION")
public class AcadaQualificationEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "QUALIFICATION_ID", columnDefinition = "NUMBER(10)")
    private long qualificationID;
    //
    @Column(name = "STUDENT_ID", columnDefinition = "NUMBER(10)")
    private long studentID;
    //
    @Column(name = "ACADEMIC_SUBJECT_ID", columnDefinition = "NUMBER(10)")
    private long academicSubjectID;
    //
    @Column(name = "SCORE_POINT", columnDefinition = "DOUBLE PRECISION")
    private BigDecimal scorePoint;
    //
    @Column(name = "SCORE_GRADE", columnDefinition = "VARCHAR2(20)")
    private String scoreGrade;
    //
    @Column(name = "QUALIFICATION_TYPE_ID", columnDefinition = "NUMBER(4)")
    private long qualificationTypeID;
    //
    @Column(name = "QUALIFICATION_DATE", columnDefinition = "DATE")
    private Date qualificationDate;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    transient private String academicSubjectTitle;
    //
    transient private String qualificationTypeDesc;

    public AcadaQualificationEntity() {
        qualificationID = 0;
        studentID = 0;
        academicSubjectID = 0;
        scorePoint = BigDecimal.ZERO;
        scoreGrade = "";
        qualificationTypeID = 0;

        academicSubjectTitle = "";
        qualificationTypeDesc = "";
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof AcadaQualificationEntity) {
            if((((AcadaQualificationEntity)o).getQualificationID() > 0) &&
                    (((AcadaQualificationEntity)o).getQualificationID() == this.getQualificationID())){
                eqls = true;
            }
        }

        return eqls;
    }

    public long getQualificationID() {
        return this.qualificationID;
    }

    public void setQualificationID(long qualificationID) {
        this.qualificationID = qualificationID;
    }

    public long getStudentID() {
        return this.studentID;
    }

    public void setStudentID(long studentID) {
        this.studentID = studentID;
    }

    public long getAcademicSubjectID() {
        return this.academicSubjectID;
    }

    public void setAcademicSubjectID(long academicSubjectID) {
        this.academicSubjectID = academicSubjectID;
    }

    public BigDecimal getScorePoint() {
        return this.scorePoint;
    }

    public void setScorePoint(BigDecimal scorePoint) {
        this.scorePoint = scorePoint;
    }

    public String getScoreGrade() {
        return this.scoreGrade;
    }

    public void setScoreGrade(String scoreGrade) {
        this.scoreGrade = scoreGrade;
    }

    public long getQualificationTypeID() {
        return this.qualificationTypeID;
    }

    public void setQualificationTypeID(long qualificationTypeID) {
        this.qualificationTypeID = qualificationTypeID;
    }

    public Date getQualificationDate() {
        return this.qualificationDate;
    }

    public void setQualificationDate(Date qualificationDate) {
        this.qualificationDate = qualificationDate;
    }

    /**
     * @return the academicSubjectTitle
     */
    public String getAcademicSubjectTitle() {
        return academicSubjectTitle;
    }

    /**
     * @param academicSubjectTitle the academicSubjectTitle to set
     */
    public void setAcademicSubjectTitle(String academicSubjectTitle) {
        this.academicSubjectTitle = academicSubjectTitle;
    }

    /**
     * @return the qualificationTypeDesc
     */
    public String getQualificationTypeDesc() {
        return qualificationTypeDesc;
    }

    /**
     * @param qualificationTypeDesc the qualificationTypeDesc to set
     */
    public void setQualificationTypeDesc(String qualificationTypeDesc) {
        this.qualificationTypeDesc = qualificationTypeDesc;
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
