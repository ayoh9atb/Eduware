package com.rsdynamix.academics.setup.entities.commonPU;

import com.rsdynamix.academics.setup.entities.AdmissionType;
import com.rsdynamix.academics.setup.entities.StudentEntity;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(StudentEntity.class)
public class StudentEntity_ { 

    public static volatile SingularAttribute<StudentEntity, String> zipCode;
    public static volatile SingularAttribute<StudentEntity, String> lastName;
    public static volatile SingularAttribute<StudentEntity, String> preferredLanguage;
    public static volatile SingularAttribute<StudentEntity, Integer> divisionDescID;
    public static volatile SingularAttribute<StudentEntity, String> studentNumber;
    public static volatile SingularAttribute<StudentEntity, String> contactStateCode;
    public static volatile SingularAttribute<StudentEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<StudentEntity, Long> facultyID;
    public static volatile SingularAttribute<StudentEntity, String> contactStreetAddress;
    public static volatile SingularAttribute<StudentEntity, String> emailAddress;
    public static volatile SingularAttribute<StudentEntity, Long> subjectOfStudyID;
    public static volatile SingularAttribute<StudentEntity, Integer> yearOfStudy;
    public static volatile SingularAttribute<StudentEntity, AdmissionType> admissionType;
    public static volatile SingularAttribute<StudentEntity, String> pictureFileName;
    public static volatile SingularAttribute<StudentEntity, Date> createDate;
    public static volatile SingularAttribute<StudentEntity, Long> contactCountryID;
    public static volatile SingularAttribute<StudentEntity, Long> contactCityID;
    public static volatile SingularAttribute<StudentEntity, String> approver;
    public static volatile SingularAttribute<StudentEntity, Date> admissionDate;
    public static volatile SingularAttribute<StudentEntity, Date> expectedGraduationDate;
    public static volatile SingularAttribute<StudentEntity, Long> departmentID;
    public static volatile SingularAttribute<StudentEntity, String> sex;
    public static volatile SingularAttribute<StudentEntity, Date> dateOfBirth;
    public static volatile SingularAttribute<StudentEntity, Long> cityID;
    public static volatile SingularAttribute<StudentEntity, Long> countryID;
    public static volatile SingularAttribute<StudentEntity, Long> studentID;
    public static volatile SingularAttribute<StudentEntity, String> firstName;
    public static volatile SingularAttribute<StudentEntity, String> phoneNumber;
    public static volatile SingularAttribute<StudentEntity, Date> graduationDate;
    public static volatile SingularAttribute<StudentEntity, String> streetAddress;
    public static volatile SingularAttribute<StudentEntity, String> stateCode;
    public static volatile SingularAttribute<StudentEntity, String> middleName;
    public static volatile SingularAttribute<StudentEntity, Integer> divisionID;
    public static volatile SingularAttribute<StudentEntity, String> aboutCandidate;

}