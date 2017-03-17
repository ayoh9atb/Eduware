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
@Table(name = "ACADEMIC_RESPONSIBILITY")
public class AcademicResponsibilityEntity extends AbstractEntity implements Serializable {
    
    @Id
    @Column(name = "RESPONSIBILITY_ID", columnDefinition = "NUMBER(10)")
    private long responsibilityID;
    //
    @Column(name = "RESPONSIBILITY_DESC", columnDefinition = "VARCHAR2(255)")
    private String responsibilityDesc;
    //
    private transient String academicRoleName;
    @Column(name = "ROLE_ID", columnDefinition = "NUMBER(10)")
    private long academicRoleID;
    //
    private transient String facultyName;
    @Column(name = "FACULTY_ID", columnDefinition = "NUMBER(10)")
    private long facultyID;
    //
    private transient String departmentName;
    @Column(name = "DEPARTMENT_ID", columnDefinition = "NUMBER(10)")
    private long departmentID;
    //
    private transient String subjectOfStudyName;
    @Column(name = "SUBJECT_ID", columnDefinition = "NUMBER(10)")
    private long subjectOfStudyID;
    //
    private transient String levelDesc;
    @Column(name = "LEVEL_ID", columnDefinition = "NUMBER(1)")
    private long levelID;
    /**
     * if there is no course-title, then the responsibility is not a
     * course-lecturer responsibility, could be 'ACADEMIC ADVISER' role.
     */
    private transient String courseTitle;
    @Column(name = "COURSE_ID", columnDefinition = "NUMBER(11)")
    private long courseID;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public AcademicResponsibilityEntity() {
        responsibilityID = 0;
        responsibilityDesc = "";
        levelDesc = "";
        levelID = 0;
        courseTitle = "";
        courseID = 0;

        facultyName = "";
        facultyID = 0;

        departmentName = "";
        departmentID = 0;

        subjectOfStudyName = "";
        subjectOfStudyID = 0;
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
     * @return the levelDesc
     */
    public String getLevelDesc() {
        return levelDesc;
    }

    /**
     * @param levelDesc the levelDesc to set
     */
    public void setLevelDesc(String levelDesc) {
        this.levelDesc = levelDesc;
    }

    /**
     * @return the levelID
     */
    public long getLevelID() {
        return levelID;
    }

    /**
     * @param levelID the levelID to set
     */
    public void setLevelID(long levelID) {
        this.levelID = levelID;
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
     * @return the subjectOfStudyName
     */
    public String getSubjectOfStudyName() {
        return subjectOfStudyName;
    }

    /**
     * @param subjectOfStudyName the subjectOfStudyName to set
     */
    public void setSubjectOfStudyName(String subjectOfStudyName) {
        this.subjectOfStudyName = subjectOfStudyName;
    }

    /**
     * @return the subjectOfStudyID
     */
    public long getSubjectOfStudyID() {
        return subjectOfStudyID;
    }

    /**
     * @param subjectOfStudyID the subjectOfStudyID to set
     */
    public void setSubjectOfStudyID(long subjectOfStudyID) {
        this.subjectOfStudyID = subjectOfStudyID;
    }

    /**
     * @return the academicRoleName
     */
    public String getAcademicRoleName() {
        return academicRoleName;
    }

    /**
     * @param academicRoleName the academicRoleName to set
     */
    public void setAcademicRoleName(String academicRoleName) {
        this.academicRoleName = academicRoleName;
    }

    /**
     * @return the academicRoleID
     */
    public long getAcademicRoleID() {
        return academicRoleID;
    }

    /**
     * @param academicRoleID the academicRoleID to set
     */
    public void setAcademicRoleID(long academicRoleID) {
        this.academicRoleID = academicRoleID;
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
