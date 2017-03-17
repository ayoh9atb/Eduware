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
@Table(name = "COURSE_GRADE")
public class CourseGradeEntity extends AbstractEntity implements Serializable {
    
    @Id
    @Column(name = "COURSE_GRADE_ID", columnDefinition = "NUMBER(12)")
    private long courseGradeID;
    //
    @Column(name = "SUBJECT_SCORE", columnDefinition = "DOUBLE PRECISION")
    private double subjectScore;
    //
    @Column(name = "STUDENT_POSITION", columnDefinition = "NUMBER(12)")
    private int studentPosition;
    //
    @Column(name = "POSITION_TIER", columnDefinition = "VARCHAR2(15)")
    private String positionTier;
    //
    @Column(name = "STUDENT_GRADE", columnDefinition = "VARCHAR2(1)")
    private String studentGrade;
    //
    @Column(name = "MATRIC_NUMBER", columnDefinition = "VARCHAR2(20)")
    private String matricNumber;
    //
    private transient String courseCode;
    private transient String courseTitle;
    @Column(name = "COURSE_ID", columnDefinition = "NUMBER(10)")
    private long courseID;
    //
    private transient String sessionPeriod;
    @Column(name = "SESSION_ID", columnDefinition = "NUMBER(10)")
    private long sessionID;
    //
    private transient String departmentName;
    @Column(name = "DEPARTMENT_ID", columnDefinition = "NUMBER(12)")
    private long departmentID;
    //
    private transient String facultyName;
    @Column(name = "FACULTY_ID", columnDefinition = "NUMBER(10)")
    private long facultyID;
    //
    private transient String academicLevelDesc;
    @Column(name = "ACADEMIC_LEVEL_ID", columnDefinition = "NUMBER(1)")
    private int academicLevel;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    private transient int creditLoad;

    public CourseGradeEntity() {
        courseID = 0;

        courseCode = "";
        courseTitle = "";

        subjectScore = 0;
        studentPosition = 0;
        positionTier = "";

        studentGrade = "";
        matricNumber = "";

        departmentName = "";
        departmentID = 0;
        facultyName = "";
        facultyID = 0;
        academicLevelDesc = "";
        academicLevel = 0;

        creditLoad = 0;
    }

    public boolean equals(Object o) {
        return ((o instanceof CourseGradeEntity) &&
                (((CourseGradeEntity)o).getCourseID() == this.getCourseID()) &&
                (((CourseGradeEntity)o).getSessionID() == this.getSessionID()) &&
                (((CourseGradeEntity)o).getMatricNumber().equals(this.getMatricNumber())));
    }

    /**
     * @return the courseGradeID
     */
    public long getCourseGradeID() {
        return courseGradeID;
    }

    /**
     * @param courseGradeID the courseGradeID to set
     */
    public void setCourseGradeID(long courseGradeID) {
        this.courseGradeID = courseGradeID;
    }

    /**
     * @return the subjectScore
     */
    public double getSubjectScore() {
        return subjectScore;
    }

    /**
     * @param subjectScore the subjectScore to set
     */
    public void setSubjectScore(double subjectScore) {
        this.subjectScore = subjectScore;
    }

    /**
     * @return the studentPosition
     */
    public int getStudentPosition() {
        return studentPosition;
    }

    /**
     * @param studentPosition the studentPosition to set
     */
    public void setStudentPosition(int studentPosition) {
        this.studentPosition = studentPosition;
    }

    /**
     * @return the studentGrade
     */
    public String getStudentGrade() {
        return studentGrade;
    }

    /**
     * @param studentGrade the studentGrade to set
     */
    public void setStudentGrade(String studentGrade) {
        this.studentGrade = studentGrade;
    }

    /**
     * @return the matricNumber
     */
    public String getMatricNumber() {
        return matricNumber;
    }

    /**
     * @param matricNumber the matricNumber to set
     */
    public void setMatricNumber(String matricNumber) {
        this.matricNumber = matricNumber;
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
     * @return the courseID
     */
    public long getCourseID() {
        return courseID;
    }

    /**
     * @param courseID the courseID to set
     */
    public void setCourseID(long courseID) {
        this.courseID = courseID;
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
     * @return the positionTier
     */
    public String getPositionTier() {
        return positionTier;
    }

    /**
     * @param positionTier the positionTier to set
     */
    public void setPositionTier(String positionTier) {
        this.positionTier = positionTier;
    }

    /**
     * @return the departmentName
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @param departmentName the departmentName to set
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * @return the departmentID
     */
    public long getDepartmentID() {
        return departmentID;
    }

    /**
     * @param departmentID the departmentID to set
     */
    public void setDepartmentID(long departmentID) {
        this.departmentID = departmentID;
    }

    /**
     * @return the facultyName
     */
    public String getFacultyName() {
        return facultyName;
    }

    /**
     * @param facultyName the facultyName to set
     */
    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    /**
     * @return the facultyID
     */
    public long getFacultyID() {
        return facultyID;
    }

    /**
     * @param facultyID the facultyID to set
     */
    public void setFacultyID(long facultyID) {
        this.facultyID = facultyID;
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
     * @return the academicLevel
     */
    public int getAcademicLevel() {
        return academicLevel;
    }

    /**
     * @param academicLevel the academicLevel to set
     */
    public void setAcademicLevel(int academicLevel) {
        this.academicLevel = academicLevel;
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
