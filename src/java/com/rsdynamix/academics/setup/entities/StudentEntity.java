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
import java.sql.Date;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 *
 * @author root
 */
@Entity
@Table(name = "STUDENT_RECORD")
public class StudentEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "STUDENT_ID", columnDefinition = "NUMBER(12)")
    private long studentID;
    //
    @Column(name = "STUDENT_NUMBER", columnDefinition = "VARCHAR2(20)")
    private String studentNumber;
    //
    @Column(name = "LAST_NAME", columnDefinition = "VARCHAR2(100)")
    private String lastName;
    //
    @Column(name = "FIRST_NAME", columnDefinition = "VARCHAR2(100)")
    private String firstName;
    //
    @Column(name = "MIDDLE_NAME", columnDefinition = "VARCHAR2(100)")
    private String middleName;
    //
    @Column(name = "YEAR_OF_STUDY", columnDefinition = "NUMBER(1)")
    private int yearOfStudy;
    //
    transient private String countryDesc;
    @Column(name = "COUNTRY_ID", columnDefinition = "NUMBER(10)")
    private long countryID;
    //
    transient private String stateDesc;
    @Column(name = "STATE_CODE", columnDefinition = "VARCHAR2(5)")
    private String stateCode;
    //
    transient private String cityDesc;
    @Column(name = "CITY_ID", columnDefinition = "NUMBER(10)")
    private long cityID;
    //
    @Column(name = "STREET_ADDRESS", columnDefinition = "VARCHAR2(255)")
    private String streetAddress;
    //
    private transient java.util.Date dateOfBirthTO;
    @Column(name = "DATE_OF_BIRTH", columnDefinition = "DATE")
    private Date dateOfBirth;
    //
    private transient java.util.Date admissionDateTO;
    @Column(name = "ADMISSION_DATE", columnDefinition = "DATE")
    private Date admissionDate;
    //
    private transient java.util.Date expectedGraduationDateTO;
    @Column(name = "EXPECTED_GRADUATION_DATE", columnDefinition = "DATE")
    private Date expectedGraduationDate;
    //
    private transient java.util.Date graduationDateTO;
    @Column(name = "GRADUATION_DATE", columnDefinition = "DATE")
    private Date graduationDate;
    //
    private transient String subjectOfStudyName;
    @Column(name = "SUBJECT_OF_STUDY_ID", columnDefinition = "NUMBER(10)")
    private long subjectOfStudyID;
    //
    private transient String departmentName;
    @Column(name = "DEPARTMENT_ID", columnDefinition = "NUMBER(10)")
    private long departmentID;
    //
    private transient String facultyName;
    @Column(name = "FACULTY_ID", columnDefinition = "NUMBER(10)")
    private long facultyID;
    //
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "ADMISSION_TYPE", columnDefinition = "NUMBER(1)")
    private AdmissionType admissionType;
    //
    @Column(name = "EMAIL_ADDRESS", columnDefinition = "VARCHAR2(65)")
    private String emailAddress;
    //
    @Column(name = "MOBILE_PHONE", columnDefinition = "VARCHAR2(20)")
    private String phoneNumber;
    //
    @Column(name = "ZIP_CODE", columnDefinition = "VARCHAR2(10)")
    private String zipCode;
    //
    private transient String contactCountryDesc;
    @Column(name = "CONTACT_COUNTRY_ID", columnDefinition = "NUMBER(10)")
    private long contactCountryID;
    //
    private transient String contactStateDesc;
    @Column(name = "CONTACT_STATE_CODE", columnDefinition = "VARCHAR2(3)")
    private String contactStateCode;
    //
    private transient String contactCityDesc;
    @Column(name = "CONTACT_CITY_ID", columnDefinition = "NUMBER(12)")
    private long contactCityID;
    //
    @Column(name = "CONTACT_STREET_ADDRESS", columnDefinition = "VARCHAR2(255)")
    private String contactStreetAddress;
    //
    @Column(name = "PICTURE_FILE_NAME", columnDefinition = "VARCHAR2(255)")
    private String pictureFileName;
    //
    @Column(name = "CREATE_DATE", columnDefinition = "DATE")
    private Date createDate;
    //
    @Column(name = "SEX", columnDefinition = "VARCHAR2(1)")
    private String sex;
    //
    @Column(name = "PREFERRED_LANGUAGE", columnDefinition = "VARCHAR2(100)")
    private String preferredLanguage;
    //
    @Column(name = "ABOUT_CANDIDATE", columnDefinition = "VARCHAR2(1000)")
    private String aboutCandidate;
    //
    @Column(name = "DIVISION_ID", columnDefinition = "NUMBER(10)")
    private int divisionID;
    //
    @Column(name = "DIVISION_DESC_ID", columnDefinition = "NUMBER(10)")
    private int divisionDescID;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    private transient String username;
    private transient String password;
    private transient String confirmedPassword;
    //
    //SPONSOR INFO
    private transient String sponsorName;
    private transient int sponsorRelationshipID;
    private transient long sponsorCountryID;
    private transient String sponsorStateCode;
    private transient long sponsorCityID;
    private transient String sponsorStreetAddress;
    private transient String sponsorPhoneNumber;
    private transient String sponsorEmailAddress;

    public StudentEntity() {
        studentID = 0;
        studentNumber = "";
        lastName = "";
        firstName = "";
        middleName = "";
        countryID = 0;
        countryDesc = "";
        stateCode = "";
        stateDesc = "";
        cityID = 0;
        cityDesc = "";

        dateOfBirthTO = new java.util.Date();
        admissionDateTO = new java.util.Date();
        expectedGraduationDateTO = new java.util.Date();
        graduationDateTO = new java.util.Date();

        streetAddress = "";

        emailAddress = "";
        phoneNumber = "";

        subjectOfStudyName = "";
        subjectOfStudyID = 0;

        departmentName = "";
        departmentID = 0;

        facultyName = "";
        facultyID = 0;

        contactCountryDesc = "";
        contactCountryID = 0;
        contactStateCode = "";
        contactStateDesc = "";
        contactCityID = 0;
        contactCityDesc = "";
        contactStreetAddress = "";

        username = "";
        password = "";
        confirmedPassword = "";

        zipCode = "";
        pictureFileName = "";
        sex = "";
        preferredLanguage = "";
        aboutCandidate = "";

        divisionID = 0;
        divisionDescID = 0;

        createDate = null;

        //SPONSOR'S INFO
        sponsorName = "";
        sponsorRelationshipID = 0;
        sponsorCountryID = 0;
        sponsorStateCode = "";
        sponsorCityID = 0;
        sponsorStreetAddress = "";
        sponsorPhoneNumber = "";
        sponsorEmailAddress = "";
    }

    public boolean equals(Object o) {
        boolean eqls = false;

        if (o instanceof StudentEntity) {
            eqls = ((!this.getStudentNumber().trim().equals(""))
                    && (((StudentEntity) o).getStudentNumber().equals(this.getStudentNumber())));
        }

        return eqls;
    }

    public String getStudentNumber() {
        return this.studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return this.middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public long getCountryID() {
        return this.countryID;
    }

    public void setCountryID(long countryID) {
        this.countryID = countryID;
    }

    public String getCountryDesc() {
        return this.countryDesc;
    }

    public void setCountryDesc(String countryDesc) {
        this.countryDesc = countryDesc;
    }

    public String getStateCode() {
        return this.stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateDesc() {
        return this.stateDesc;
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public long getCityID() {
        return this.cityID;
    }

    public void setCityID(long cityID) {
        this.cityID = cityID;
    }

    public String getCityDesc() {
        return this.cityDesc;
    }

    public void setCityDesc(String cityDesc) {
        this.cityDesc = cityDesc;
    }

    public String getStreetAddress() {
        return this.streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Date getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @return the dateOfBirthTO
     */
    public java.util.Date getDateOfBirthTO() {
        return dateOfBirthTO;
    }

    /**
     * @param dateOfBirthTO the dateOfBirthTO to set
     */
    public void setDateOfBirthTO(java.util.Date dateOfBirthTO) {
        this.dateOfBirthTO = dateOfBirthTO;
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
     * @return the admissionDate
     */
    public Date getAdmissionDate() {
        return admissionDate;
    }

    /**
     * @param admissionDate the admissionDate to set
     */
    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    /**
     * @return the expectedGraduationDateTO
     */
    public java.util.Date getExpectedGraduationDateTO() {
        return expectedGraduationDateTO;
    }

    /**
     * @param expectedGraduationDateTO the expectedGraduationDateTO to set
     */
    public void setExpectedGraduationDateTO(java.util.Date expectedGraduationDateTO) {
        this.expectedGraduationDateTO = expectedGraduationDateTO;
    }

    /**
     * @return the expectedGraduationDate
     */
    public Date getExpectedGraduationDate() {
        return expectedGraduationDate;
    }

    /**
     * @param expectedGraduationDate the expectedGraduationDate to set
     */
    public void setExpectedGraduationDate(Date expectedGraduationDate) {
        this.expectedGraduationDate = expectedGraduationDate;
    }

    /**
     * @return the graduationDateTO
     */
    public java.util.Date getGraduationDateTO() {
        return graduationDateTO;
    }

    /**
     * @param graduationDateTO the graduationDateTO to set
     */
    public void setGraduationDateTO(java.util.Date graduationDateTO) {
        this.graduationDateTO = graduationDateTO;
    }

    /**
     * @return the graduationDate
     */
    public Date getGraduationDate() {
        return graduationDate;
    }

    /**
     * @param graduationDate the graduationDate to set
     */
    public void setGraduationDate(Date graduationDate) {
        this.graduationDate = graduationDate;
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
     * @return the admissionType
     */
    public AdmissionType getAdmissionType() {
        return admissionType;
    }

    /**
     * @param admissionType the admissionType to set
     */
    public void setAdmissionType(AdmissionType admissionType) {
        this.admissionType = admissionType;
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
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the contactCountryDesc
     */
    public String getContactCountryDesc() {
        return contactCountryDesc;
    }

    /**
     * @param contactCountryDesc the contactCountryDesc to set
     */
    public void setContactCountryDesc(String contactCountryDesc) {
        this.contactCountryDesc = contactCountryDesc;
    }

    /**
     * @return the contactCountryID
     */
    public long getContactCountryID() {
        return contactCountryID;
    }

    /**
     * @param contactCountryID the contactCountryID to set
     */
    public void setContactCountryID(long contactCountryID) {
        this.contactCountryID = contactCountryID;
    }

    /**
     * @return the contactStateCode
     */
    public String getContactStateCode() {
        return contactStateCode;
    }

    /**
     * @param contactStateCode the contactStateCode to set
     */
    public void setContactStateCode(String contactStateCode) {
        this.contactStateCode = contactStateCode;
    }

    /**
     * @return the contactStateDesc
     */
    public String getContactStateDesc() {
        return contactStateDesc;
    }

    /**
     * @param contactStateDesc the contactStateDesc to set
     */
    public void setContactStateDesc(String contactStateDesc) {
        this.contactStateDesc = contactStateDesc;
    }

    /**
     * @return the contactCityID
     */
    public long getContactCityID() {
        return contactCityID;
    }

    /**
     * @param contactCityID the contactCityID to set
     */
    public void setContactCityID(long contactCityID) {
        this.contactCityID = contactCityID;
    }

    /**
     * @return the contactStreetAddress
     */
    public String getContactStreetAddress() {
        return contactStreetAddress;
    }

    /**
     * @param contactStreetAddress the contactStreetAddress to set
     */
    public void setContactStreetAddress(String contactStreetAddress) {
        this.contactStreetAddress = contactStreetAddress;
    }

    /**
     * @return the contactCityDesc
     */
    public String getContactCityDesc() {
        return contactCityDesc;
    }

    /**
     * @param contactCityDesc the contactCityDesc to set
     */
    public void setContactCityDesc(String contactCityDesc) {
        this.contactCityDesc = contactCityDesc;
    }

    /**
     * @return the yearOfStudy
     */
    public int getYearOfStudy() {
        return yearOfStudy;
    }

    /**
     * @param yearOfStudy the yearOfStudy to set
     */
    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
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
     * @return the pictureFileName
     */
    public String getPictureFileName() {
        return pictureFileName;
    }

    /**
     * @param pictureFileName the pictureFileName to set
     */
    public void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
    }

    /**
     * @return the createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * @param createDate the createDate to set
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * @return the zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * @param zipCode the zipCode to set
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * @return the sex
     */
    public String getSex() {
        return sex;
    }

    /**
     * @param sex the sex to set
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * @return the preferredLanguage
     */
    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    /**
     * @param preferredLanguage the preferredLanguage to set
     */
    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    /**
     * @return the aboutCandidate
     */
    public String getAboutCandidate() {
        return aboutCandidate;
    }

    /**
     * @param aboutCandidate the aboutCandidate to set
     */
    public void setAboutCandidate(String aboutCandidate) {
        this.aboutCandidate = aboutCandidate;
    }

    /**
     * @return the divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * @param divisionID the divisionID to set
     */
    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    /**
     * @return the divisionDescID
     */
    public int getDivisionDescID() {
        return divisionDescID;
    }

    /**
     * @param divisionDescID the divisionDescID to set
     */
    public void setDivisionDescID(int divisionDescID) {
        this.divisionDescID = divisionDescID;
    }

    /**
     * @return the sponsorName
     */
    public String getSponsorName() {
        return sponsorName;
    }

    /**
     * @param sponsorName the sponsorName to set
     */
    public void setSponsorName(String sponsorName) {
        this.sponsorName = sponsorName;
    }

    /**
     * @return the sponsorRelationshipID
     */
    public int getSponsorRelationshipID() {
        return sponsorRelationshipID;
    }

    /**
     * @param sponsorRelationshipID the sponsorRelationshipID to set
     */
    public void setSponsorRelationshipID(int sponsorRelationshipID) {
        this.sponsorRelationshipID = sponsorRelationshipID;
    }

    /**
     * @return the sponsorCountryID
     */
    public long getSponsorCountryID() {
        return sponsorCountryID;
    }

    /**
     * @param sponsorCountryID the sponsorCountryID to set
     */
    public void setSponsorCountryID(long sponsorCountryID) {
        this.sponsorCountryID = sponsorCountryID;
    }

    /**
     * @return the sponsorStateCode
     */
    public String getSponsorStateCode() {
        return sponsorStateCode;
    }

    /**
     * @param sponsorStateCode the sponsorStateCode to set
     */
    public void setSponsorStateCode(String sponsorStateCode) {
        this.sponsorStateCode = sponsorStateCode;
    }

    /**
     * @return the sponsorCityID
     */
    public long getSponsorCityID() {
        return sponsorCityID;
    }

    /**
     * @param sponsorCityID the sponsorCityID to set
     */
    public void setSponsorCityID(long sponsorCityID) {
        this.sponsorCityID = sponsorCityID;
    }

    /**
     * @return the sponsorStreetAddress
     */
    public String getSponsorStreetAddress() {
        return sponsorStreetAddress;
    }

    /**
     * @param sponsorStreetAddress the sponsorStreetAddress to set
     */
    public void setSponsorStreetAddress(String sponsorStreetAddress) {
        this.sponsorStreetAddress = sponsorStreetAddress;
    }

    /**
     * @return the sponsorPhoneNumber
     */
    public String getSponsorPhoneNumber() {
        return sponsorPhoneNumber;
    }

    /**
     * @param sponsorPhoneNumber the sponsorPhoneNumber to set
     */
    public void setSponsorPhoneNumber(String sponsorPhoneNumber) {
        this.sponsorPhoneNumber = sponsorPhoneNumber;
    }

    /**
     * @return the sponsorEmailAddress
     */
    public String getSponsorEmailAddress() {
        return sponsorEmailAddress;
    }

    /**
     * @param sponsorEmailAddress the sponsorEmailAddress to set
     */
    public void setSponsorEmailAddress(String sponsorEmailAddress) {
        this.sponsorEmailAddress = sponsorEmailAddress;
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
     * @return the studentID
     */
    public long getStudentID() {
        return studentID;
    }

    /**
     * @param studentID the studentID to set
     */
    public void setStudentID(long studentID) {
        this.studentID = studentID;
    }
}
