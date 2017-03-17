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
@Table(name = "ACADEMIC_STAFF")
public class AcademicStaffEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "STAFF_ID", columnDefinition = "NUMBER(12)")
    private long staffID;
    //
    @Column(name = "STAFF_NUMBER", columnDefinition = "VARCHAR2(20)")
    private String staffNumber;
    //
    @Column(name = "STAFF_NAME", columnDefinition = "VARCHAR2(255)")
    private String staffName;
    //
    private transient String departmentDesc;
    @Column(name = "DEPARTMENT_ID", columnDefinition = "NUMBER(10)")
    private long departmentID;
    //
    private transient String facultyDesc;
    @Column(name = "FACULTY_ID", columnDefinition = "NUMBER(10)")
    private long facultyID;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    private transient String username;
    private transient String password;
    private transient String emailAddress;
    private transient String confirmedPassword;

    public AcademicStaffEntity() {
        staffNumber = "";
        staffName = "";
        departmentDesc = "";
        departmentID = 0;
        facultyDesc = "";
        facultyID = 0;

        username = "";
        password = "";
        emailAddress = "";
        confirmedPassword = "";
    }

    public boolean equals(Object o) {
        return ((o instanceof AcademicStaffEntity) &&
                (((AcademicStaffEntity)o).getStaffNumber().equals(this.getStaffNumber())));
    }

    /**
     * @return the staffNumber
     */
    public String getStaffNumber() {
        return staffNumber;
    }

    /**
     * @param staffNumber the staffNumber to set
     */
    public void setStaffNumber(String staffNumber) {
        this.staffNumber = staffNumber;
    }

    /**
     * @return the staffName
     */
    public String getStaffName() {
        return staffName;
    }

    /**
     * @param staffName the staffName to set
     */
    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    /**
     * @return the departmentDesc
     */
    public String getDepartmentDesc() {
        return departmentDesc;
    }

    /**
     * @param departmentDesc the departmentDesc to set
     */
    public void setDepartmentDesc(String departmentDesc) {
        this.departmentDesc = departmentDesc;
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
     * @return the facultyDesc
     */
    public String getFacultyDesc() {
        return facultyDesc;
    }

    /**
     * @param facultyDesc the facultyDesc to set
     */
    public void setFacultyDesc(String facultyDesc) {
        this.facultyDesc = facultyDesc;
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
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the confirmedPassword
     */
    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    /**
     * @param confirmedPassword the confirmedPassword to set
     */
    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
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

    /**
     * @return the staffID
     */
    public long getStaffID() {
        return staffID;
    }

    /**
     * @param staffID the staffID to set
     */
    public void setStaffID(long staffID) {
        this.staffID = staffID;
    }

}
