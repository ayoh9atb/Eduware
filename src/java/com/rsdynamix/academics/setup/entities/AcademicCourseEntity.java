/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.setup.entities;

import java.io.Serializable;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 *
 * @author root
 */
@Entity
@Table(name = "ACADEMIC_COURSE")
public class AcademicCourseEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "COURSE_ID", columnDefinition = "NUMBER(12)")
    private long courseID;
    //
    @Column(name = "COURSE_CODE", columnDefinition = "VARCHAR2(10)")
    private String courseCode;
    //
    @Column(name = "COURSE_TITLE", columnDefinition = "VARCHAR2(100)")
    private String courseTitle;
    //
    @Column(name = "SUBJECT_ID", columnDefinition = "NUMBER(10)")
    private long subjectID;
    //
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "SEMESTER_TIER", columnDefinition = "NUMBER(1)")
    private TermTier semesterTier;
    //
    @Column(name = "LEVEL_NUMBER", columnDefinition = "NUMBER(3)")
    private int levelNumber;
    //
    @Column(name = "CREDIT_LOAD", columnDefinition = "NUMBER(1)")
    private int creditLoad;
    //
    @Column(name = "COURSE_COST_AMT", columnDefinition = "DOUBLE PRECISION")
    private BigDecimal courseCostAmount;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    transient private String subjectTitle;

    public AcademicCourseEntity() {
        courseID = 0;
        courseCode = "";
        courseTitle = "";
        subjectID = 0;
        levelNumber = 0;
        subjectTitle = "";

        creditLoad = 0;
        courseCostAmount = BigDecimal.ZERO;
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof AcademicCourseEntity) {
            if((((AcademicCourseEntity)o).getCourseID() > 0) && (((AcademicCourseEntity)o).getCourseID() == this.getCourseID())){
                eqls = true;
            } else {
                eqls = ((AcademicCourseEntity)o).getCourseCode().equals(this.getCourseCode());
            }
        }

        return eqls;
    }

    public long getCourseID() {
        return this.courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return this.courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public long getSubjectID() {
        return this.subjectID;
    }

    public void setSubjectID(long subjectID) {
        this.subjectID = subjectID;
    }

    public int getLevelNumber() {
        return this.levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    /**
     * @return the subjectTitle
     */
    public String getSubjectTitle() {
        return subjectTitle;
    }

    /**
     * @param subjectTitle the subjectTitle to set
     */
    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    /**
     * @return the semesterTier
     */
    public TermTier getSemesterTier() {
        return semesterTier;
    }

    /**
     * @param semesterTier the semesterTier to set
     */
    public void setSemesterTier(TermTier semesterTier) {
        this.semesterTier = semesterTier;
    }

    /**
     * @return the courseCostAmount
     */
    public BigDecimal getCourseCostAmount() {
        return courseCostAmount;
    }

    /**
     * @param courseCostAmount the courseCostAmount to set
     */
    public void setCourseCostAmount(BigDecimal courseCostAmount) {
        this.courseCostAmount = courseCostAmount;
    }

    /**
     * @return the creditLoad
     */
    public int getCreditLoad() {
        return creditLoad;
    }

    /**
     * @param creditLoad the creditLoad to set
     */
    public void setCreditLoad(int creditLoad) {
        this.creditLoad = creditLoad;
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
