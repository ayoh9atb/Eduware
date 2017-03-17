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
@Table(name = "DEPARTMENT")
public class AcademicDepartmentEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "DEPARTMENT_ID", columnDefinition = "NUMBER(4)")
    private long departmentID;
    //
    @Column(name = "FACULTY_ID", columnDefinition = "NUMBER(2)")
    private long facultyID;
    //
    @Column(name = "DEPARTMENT_NAME", columnDefinition = "VARCHAR2(60)")
    private String departmentName;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    transient private String facultyName;

    public AcademicDepartmentEntity() {
        departmentID = 0;
        facultyID = 0;
        departmentName = "";

        initializeTransients();
    }

    public void initializeTransients(){
        facultyName = "";
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof AcademicDepartmentEntity) {
            if((((AcademicDepartmentEntity)o).getDepartmentID() > 0) 
                    && (((AcademicDepartmentEntity)o).getDepartmentID() == this.getDepartmentID())){
                eqls = true;
            } else {
                eqls = ((AcademicDepartmentEntity)o).getDepartmentName().equals(this.getDepartmentName());
            }
        }

        return eqls;
    }

    public long getDepartmentID() {
        return this.departmentID;
    }

    public void setDepartmentID(long departmentID) {
        this.departmentID = departmentID;
    }

    public long getFacultyID() {
        return this.facultyID;
    }

    public void setFacultyID(long facultyID) {
        this.facultyID = facultyID;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

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
