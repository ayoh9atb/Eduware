/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.setup.entities;

import java.io.Serializable;
import com.rsdynamix.abstractobjects.AbstractEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.sql.Date;

/**
 *
 * @author root
 */
@Entity
@Table(name = "ACADEMIC_HISTORY")
public class AcademicHistoryEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "ACADEMIC_HISTORY_ID", columnDefinition = "NUMBER(10)")
    private long academicHistoryID;
    //
    @Column(name = "STUDENT_NUMBER", columnDefinition = "VARCHAR2(15)")
    private String studentNumber;
    //
    @Column(name = "SCHOOL_ID", columnDefinition = "NUMBER(0)")
    private long schoolID;
    //
    @Column(name = "SCHOOL_COUNTRY_ID", columnDefinition = "NUMBER(10)")
    private long schoolCountryID;
    //
    @Column(name = "STATE_CODE", columnDefinition = "VARCHAR2(3)")
    private String stateCode;
    //
    @Column(name = "CITY_ID", columnDefinition = "NUMBER(10)")
    private long cityID;
    //
    @Column(name = "SCHOOL_STREET_ADDRESS", columnDefinition = "VARCHAR2(100)")
    private String schoolStreetAddress;
    //
    private transient java.util.Date admissionDateTO;
    @Column(name = "ADMISSION_DATE", columnDefinition = "DATE")
    private Date admissionDate;
    //
    private transient java.util.Date completionDateTO;
    @Column(name = "COMPLETION_DATE", columnDefinition = "DATE")
    private Date completionDate;
    //
    @Column(name = "SCHOOL_TIER_TYPE_ID", columnDefinition = "NUMBER(1)")
    private int schoolTierTypeID;
    //
    @Column(name = "CERTIFICATE_ID", columnDefinition = "NUMBER(5)")
    private int certificateID;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    transient private String schoolName;
    //
    transient private String schoolCountryDesc;
    //
    transient private String stateDesc;
    //
    transient private String cityDesc;
    //
    transient private String schoolTierTypeDesc;
    //
    transient private String certificateDesc;

    public AcademicHistoryEntity() {
        academicHistoryID = 0;
        studentNumber = "";
        schoolID = 0;
        schoolCountryID = 0;
        stateCode = "";
        cityID = 0;
        schoolStreetAddress = "";
        schoolTierTypeID = 0;
        certificateID = 0;

        schoolName = "";
        schoolCountryDesc = "";
        stateDesc = "";
        schoolTierTypeDesc = "";
        certificateDesc = "";
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof AcademicHistoryEntity) {
            if((((AcademicHistoryEntity)o).getAcademicHistoryID() > 0) &&
                    (((AcademicHistoryEntity)o).getAcademicHistoryID() == this.getAcademicHistoryID())){
                eqls = true;
            }
        }

        return eqls;
    }

    public long getAcademicHistoryID() {
        return this.academicHistoryID;
    }

    public void setAcademicHistoryID(long academicHistoryID) {
        this.academicHistoryID = academicHistoryID;
    }

    public long getSchoolID() {
        return this.schoolID;
    }

    public void setSchoolID(long schoolID) {
        this.schoolID = schoolID;
    }

    public long getSchoolCountryID() {
        return this.schoolCountryID;
    }

    public void setSchoolCountryID(long schoolCountryID) {
        this.schoolCountryID = schoolCountryID;
    }

    public String getStateCode() {
        return this.stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public long getCityID() {
        return this.cityID;
    }

    public void setCityID(long cityID) {
        this.cityID = cityID;
    }

    public String getSchoolStreetAddress() {
        return this.schoolStreetAddress;
    }

    public void setSchoolStreetAddress(String schoolStreetAddress) {
        this.schoolStreetAddress = schoolStreetAddress;
    }

    public Date getAdmissionDate() {
        return this.admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Date getCompletionDate() {
        return this.completionDate;
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public int getSchoolTierTypeID() {
        return this.schoolTierTypeID;
    }

    public void setSchoolTierTypeID(int schoolTierTypeID) {
        this.schoolTierTypeID = schoolTierTypeID;
    }

    public int getCertificateID() {
        return this.certificateID;
    }

    public void setCertificateID(int certificateID) {
        this.certificateID = certificateID;
    }

    /**
     * @return the schoolName
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * @param schoolName the schoolName to set
     */
    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    /**
     * @return the schoolCountryDesc
     */
    public String getSchoolCountryDesc() {
        return schoolCountryDesc;
    }

    /**
     * @param schoolCountryDesc the schoolCountryDesc to set
     */
    public void setSchoolCountryDesc(String schoolCountryDesc) {
        this.schoolCountryDesc = schoolCountryDesc;
    }

    /**
     * @return the stateDesc
     */
    public String getStateDesc() {
        return stateDesc;
    }

    /**
     * @param stateDesc the stateDesc to set
     */
    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    /**
     * @return the cityDesc
     */
    public String getCityDesc() {
        return cityDesc;
    }

    /**
     * @param cityDesc the cityDesc to set
     */
    public void setCityDesc(String cityDesc) {
        this.cityDesc = cityDesc;
    }

    /**
     * @return the schoolTierTypeDesc
     */
    public String getSchoolTierTypeDesc() {
        return schoolTierTypeDesc;
    }

    /**
     * @param schoolTierTypeDesc the schoolTierTypeDesc to set
     */
    public void setSchoolTierTypeDesc(String schoolTierTypeDesc) {
        this.schoolTierTypeDesc = schoolTierTypeDesc;
    }

    /**
     * @return the certificateDesc
     */
    public String getCertificateDesc() {
        return certificateDesc;
    }

    /**
     * @param certificateDesc the certificateDesc to set
     */
    public void setCertificateDesc(String certificateDesc) {
        this.certificateDesc = certificateDesc;
    }

    /**
     * @return the studentNumber
     */
    public String getStudentNumber() {
        return studentNumber;
    }

    /**
     * @param studentNumber the studentNumber to set
     */
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    /**
     * @return the admissionDateTO
     */
    public java.util.Date getAdmissionDateTO() {
        return admissionDateTO;
    }

    /**
     * @param admissionDateTO the admissionDateTO to set
     */
    public void setAdmissionDateTO(java.util.Date admissionDateTO) {
        this.admissionDateTO = admissionDateTO;
    }

    /**
     * @return the completionDateTO
     */
    public java.util.Date getCompletionDateTO() {
        return completionDateTO;
    }

    /**
     * @param completionDateTO the completionDateTO to set
     */
    public void setCompletionDateTO(java.util.Date completionDateTO) {
        this.completionDateTO = completionDateTO;
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
