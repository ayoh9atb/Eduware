package com.rsdynamix.academics.setup.entities.commonPU;

import com.rsdynamix.academics.setup.entities.AcademicHistoryEntity;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(AcademicHistoryEntity.class)
public class AcademicHistoryEntity_ { 

    public static volatile SingularAttribute<AcademicHistoryEntity, String> approver;
    public static volatile SingularAttribute<AcademicHistoryEntity, Date> admissionDate;
    public static volatile SingularAttribute<AcademicHistoryEntity, Integer> schoolTierTypeID;
    public static volatile SingularAttribute<AcademicHistoryEntity, String> studentNumber;
    public static volatile SingularAttribute<AcademicHistoryEntity, Integer> certificateID;
    public static volatile SingularAttribute<AcademicHistoryEntity, Long> academicHistoryID;
    public static volatile SingularAttribute<AcademicHistoryEntity, Long> cityID;
    public static volatile SingularAttribute<AcademicHistoryEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<AcademicHistoryEntity, String> schoolStreetAddress;
    public static volatile SingularAttribute<AcademicHistoryEntity, Long> schoolCountryID;
    public static volatile SingularAttribute<AcademicHistoryEntity, Long> schoolID;
    public static volatile SingularAttribute<AcademicHistoryEntity, String> stateCode;
    public static volatile SingularAttribute<AcademicHistoryEntity, Date> completionDate;

}