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
 * @author root
 */
@Entity
@Table(name = "ACADEMIC_INSTITUTE")
public class AcademicInstitutionEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "SCHOOL_ID", columnDefinition="Number(20)")
    private long schoolID;

    @Column(name = "SCHOOL_NAME", columnDefinition="Varchar2(100)")
    private String schoolName;

    @Column(name = "COUNTRY_ID", columnDefinition="Number(20)")
    private long countryID;

    @Column(name = "STATE_CODE", columnDefinition="Varchar2(5)")
    private String stateCode;

    @Column(name = "CITY_ID", columnDefinition="Number(20)")
    private long cityID;

    @Column(name = "SCHOOL_ADDRESS", columnDefinition="Varchar2(150)")
    private String schoolAddress;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    transient private String country;
    //
    transient private String stateDesc;
    //
    transient private String cityDesc;
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public AcademicInstitutionEntity(){
        schoolID = 0;
        schoolName = "";
        countryID = 0;
        stateCode = "";
        cityID = 0;
        schoolAddress = "";

        initializeTransients();
    }

    public void initializeTransients(){
        country = "";
        stateDesc = "";
        cityDesc = "";
    }

    /**
     * @return the schoolID
     */
    public long getSchoolID() {
        return schoolID;
    }

    /**
     * @param schoolID the schoolID to set
     */
    public void setSchoolID(long schoolID) {
        this.schoolID = schoolID;
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
     * @return the countryID
     */
    public long getCountryID() {
        return countryID;
    }

    /**
     * @param countryID the countryID to set
     */
    public void setCountryID(long countryID) {
        this.countryID = countryID;
    }

    /**
     * @return the stateCode
     */
    public String getStateCode() {
        return stateCode;
    }

    /**
     * @param stateCode the stateCode to set
     */
    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    /**
     * @return the cityID
     */
    public long getCityID() {
        return cityID;
    }

    /**
     * @param cityID the cityID to set
     */
    public void setCityID(long cityID) {
        this.cityID = cityID;
    }

    /**
     * @return the schoolAddress
     */
    public String getSchoolAddress() {
        return schoolAddress;
    }

    /**
     * @param schoolAddress the schoolAddress to set
     */
    public void setSchoolAddress(String schoolAddress) {
        this.schoolAddress = schoolAddress;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
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
