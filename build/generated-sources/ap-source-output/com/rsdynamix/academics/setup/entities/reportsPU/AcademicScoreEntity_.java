package com.rsdynamix.academics.setup.entities.reportsPU;

import com.rsdynamix.academics.setup.entities.AcademicScoreEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(AcademicScoreEntity.class)
public class AcademicScoreEntity_ { 

    public static volatile SingularAttribute<AcademicScoreEntity, String> approver;
    public static volatile SingularAttribute<AcademicScoreEntity, Long> facultyID;
    public static volatile SingularAttribute<AcademicScoreEntity, Integer> academicLevel;
    public static volatile SingularAttribute<AcademicScoreEntity, Integer> testTypeID;
    public static volatile SingularAttribute<AcademicScoreEntity, Long> departmentID;
    public static volatile SingularAttribute<AcademicScoreEntity, Long> subjectScoreID;
    public static volatile SingularAttribute<AcademicScoreEntity, String> matricNumber;
    public static volatile SingularAttribute<AcademicScoreEntity, Long> sessionID;
    public static volatile SingularAttribute<AcademicScoreEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<AcademicScoreEntity, Long> courseID;
    public static volatile SingularAttribute<AcademicScoreEntity, Double> subjectScore;

}