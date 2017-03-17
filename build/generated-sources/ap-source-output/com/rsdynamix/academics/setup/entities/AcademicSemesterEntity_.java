package com.rsdynamix.academics.setup.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(AcademicSemesterEntity.class)
public class AcademicSemesterEntity_ { 

    public static volatile SingularAttribute<AcademicSemesterEntity, String> approver;
    public static volatile SingularAttribute<AcademicSemesterEntity, Long> semesterID;
    public static volatile SingularAttribute<AcademicSemesterEntity, Boolean> currentSemester;
    public static volatile SingularAttribute<AcademicSemesterEntity, String> semesterTier;
    public static volatile SingularAttribute<AcademicSemesterEntity, Long> sessionID;
    public static volatile SingularAttribute<AcademicSemesterEntity, Integer> approvalStatusID;

}