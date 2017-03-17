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
@Table(name = "SUBJECT_OF_STUDY")
public class SubjectOfStudyEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "SUBJECT_ID", columnDefinition = "NUMBER(10)")
    private long subjectID;
    //
    @Column(name = "SUBJECT_TITLE", columnDefinition = "VARCHAR2(100)")
    private String subjectTitle;
    //
    @Column(name = "DEPARTMENT_ID", columnDefinition = "NUMBER(10)")
    private long departmentID;
    //
    @Column(name = "FACULTY_ID", columnDefinition = "NUMBER(10)")
    private long facultyID;
    //
    @Column(name = "NUMBER_OF_LEVELS", columnDefinition = "NUMBER(1)")
    private int numberOfLevels;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    transient private String facultyName;
    //
    transient private String departmentName;

    public SubjectOfStudyEntity() {
        subjectID = 0;
        subjectTitle = "";
        
        departmentID = 0;
        facultyID = 0;
        numberOfLevels = 0;

        initializeTransientFields();
    }

    public void initializeTransientFields(){
        facultyName = "";
        departmentName = "";
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof SubjectOfStudyEntity) {
            if((((SubjectOfStudyEntity)o).getSubjectID() > 0) && (((SubjectOfStudyEntity)o).getSubjectID() == this.getSubjectID())){
                eqls = true;
            } else {
                eqls = ((SubjectOfStudyEntity)o).getSubjectTitle().equals(this.getSubjectTitle());
            }
        }

        return eqls;
    }

    public long getSubjectID() {
        return this.subjectID;
    }

    public void setSubjectID(long subjectID) {
        this.subjectID = subjectID;
    }

    public String getSubjectTitle() {
        return this.subjectTitle;
    }

    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    public long getDepartmentID() {
        return this.departmentID;
    }

    public void setDepartmentID(long departmentID) {
        this.departmentID = departmentID;
    }

    public int getNumberOfLevels() {
        return this.numberOfLevels;
    }

    public void setNumberOfLevels(int numberOfLevels) {
        this.numberOfLevels = numberOfLevels;
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
