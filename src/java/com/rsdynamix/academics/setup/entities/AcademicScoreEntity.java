package com.rsdynamix.academics.setup.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "COURSE_SCORE")
public class AcademicScoreEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "SUBJECT_SCORE_ID", columnDefinition = "NUMBER(12)")
    private long subjectScoreID;
    //
    private transient String courseCode;
    private transient String courseTitle;
    @Column(name = "COURSE_ID", columnDefinition = "NUMBER(10)")
    private long courseID;
    //
    @Column(name = "SUBJECT_SCORE", columnDefinition = "DOUBLE PRECISION")
    private double subjectScore;
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
    private transient String testTypeDesc;
    @Column(name = "TEST_TYPE_ID", columnDefinition = "NUMBER(1)")
    private int testTypeID;
    //
    @Column(name = "MATRIC_NUMBER", columnDefinition = "VARCHAR2(20)")
    private String matricNumber;
    //
    private transient String sessionPeriod;
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

    public AcademicScoreEntity() {
        courseID = 0;
        subjectScoreID = 0;


        departmentID = 0;
        courseCode = "";
        courseTitle = "";
        departmentName = "";
        facultyName = "";
        facultyID = 0;
        testTypeDesc = "";
        academicLevelDesc = "";

        subjectScore = 0;
        matricNumber = "";
        academicLevel = 0;
        testTypeID = 0;
    }

    public AcademicScoreEntity clone(){
        AcademicScoreEntity instance = new AcademicScoreEntity();

        instance.courseID = courseID;
        instance.subjectScoreID = subjectScoreID;

        instance.departmentID = departmentID;
        instance.courseCode = courseCode;
        instance.courseTitle = courseTitle;
        instance.departmentName = departmentName;
        instance.facultyName = facultyName;
        instance.facultyID = facultyID;
        instance.testTypeDesc = testTypeDesc;
        instance.academicLevelDesc = academicLevelDesc;

        instance.subjectScore = subjectScore;
        instance.matricNumber = matricNumber;
        instance.academicLevel = academicLevel;
        instance.testTypeID = testTypeID;

        return instance;
    }

    public void setSubjectScore(double subjectScore) {
        this.subjectScore = subjectScore;
    }

    public double getSubjectScore() {
        return subjectScore;
    }

    public void setMatricNumber(String matricNumber) {
        this.matricNumber = matricNumber;
    }

    public String getMatricNumber() {
        return matricNumber;
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
     * @return the testTypeID
     */
    public int getTestTypeID() {
        return testTypeID;
    }

    /**
     * @param testTypeID the testTypeID to set
     */
    public void setTestTypeID(int testTypeID) {
        this.testTypeID = testTypeID;
    }

    /**
     * @return the subjectScoreID
     */
    public long getSubjectScoreID() {
        return subjectScoreID;
    }

    /**
     * @param subjectScoreID the subjectScoreID to set
     */
    public void setSubjectScoreID(long subjectScoreID) {
        this.subjectScoreID = subjectScoreID;
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
     * @return the testTypeDesc
     */
    public String getTestTypeDesc() {
        return testTypeDesc;
    }

    /**
     * @param testTypeDesc the testTypeDesc to set
     */
    public void setTestTypeDesc(String testTypeDesc) {
        this.testTypeDesc = testTypeDesc;
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
