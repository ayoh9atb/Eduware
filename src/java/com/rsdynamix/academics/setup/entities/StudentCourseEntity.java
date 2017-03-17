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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

/**
 *
 * @author root
 */
@Entity
@Table(name = "STUDENT_COURSE")
public class StudentCourseEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "STUDENT_COURSE_ID", columnDefinition = "NUMBER(11)")
    private long studentCourseID;
    //
    transient private String courseCode;
    transient private String courseTitle;
    @Column(name = "COURSE_ID", columnDefinition = "NUMBER(10)")
    private long courseID;
    //
    @Column(name = "STUDENT_ID", columnDefinition = "NUMBER(12)")
    private long studentID;
    //
    @Column(name = "STUDENT_NUMBER", columnDefinition = "VARCHAR2(20)")
    private String studentNumber;
    //
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "SEMESTER_TIER", columnDefinition = "NUMBER(1)")
    private TermTier semesterTier;
    //
    transient private String sessionPeriod;
    @Column(name = "SESSION_ID", columnDefinition = "NUMBER(10)")
    private long sessionID;
    //
    @Column(name = "CREDIT_LOAD", columnDefinition = "NUMBER(1)")
    private int creditLoad;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public StudentCourseEntity() {
        studentCourseID = 0;
        courseID = 0;
        studentID = 0;
        studentNumber = "";
        
        sessionID = 0;
        creditLoad = 0;

        courseCode = "";
        courseTitle = "";
        sessionPeriod = "";
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof StudentCourseEntity) {
            if((((StudentCourseEntity)o).getCourseID() > 0) && (((StudentCourseEntity)o).getCourseID() == this.getCourseID())){
                eqls = true;
            } else {
                eqls = ((StudentCourseEntity)o).getCourseTitle().equals(this.getCourseTitle());
            }
        }

        return eqls;
    }

    public long getStudentCourseID() {
        return this.studentCourseID;
    }

    public void setStudentCourseID(long studentCourseID) {
        this.studentCourseID = studentCourseID;
    }

    public long getCourseID() {
        return this.courseID;
    }

    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    public long getStudentID() {
        return this.studentID;
    }

    public void setStudentID(long studentID) {
        this.studentID = studentID;
    }

    public String getStudentNumber() {
        return this.studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public long getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * @return the courseTitle
     */
    public String getCourseTitle() {
        return courseTitle;
    }

    /**
     * @param courseTitle the courseTitle to set
     */
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    /**
     * @return the sessionPeriod
     */
    public String getSessionPeriod() {
        return sessionPeriod;
    }

    /**
     * @param sessionPeriod the sessionPeriod to set
     */
    public void setSessionPeriod(String sessionPeriod) {
        this.sessionPeriod = sessionPeriod;
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
     * @return the courseCode
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * @param courseCode the courseCode to set
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
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
