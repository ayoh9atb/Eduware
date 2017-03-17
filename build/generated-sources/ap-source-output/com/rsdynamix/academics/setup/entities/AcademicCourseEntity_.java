package com.rsdynamix.academics.setup.entities;

import com.rsdynamix.academics.setup.entities.TermTier;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(AcademicCourseEntity.class)
public class AcademicCourseEntity_ { 

    public static volatile SingularAttribute<AcademicCourseEntity, String> courseTitle;
    public static volatile SingularAttribute<AcademicCourseEntity, String> approver;
    public static volatile SingularAttribute<AcademicCourseEntity, TermTier> semesterTier;
    public static volatile SingularAttribute<AcademicCourseEntity, Integer> creditLoad;
    public static volatile SingularAttribute<AcademicCourseEntity, String> courseCode;
    public static volatile SingularAttribute<AcademicCourseEntity, BigDecimal> courseCostAmount;
    public static volatile SingularAttribute<AcademicCourseEntity, Integer> levelNumber;
    public static volatile SingularAttribute<AcademicCourseEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<AcademicCourseEntity, Long> courseID;
    public static volatile SingularAttribute<AcademicCourseEntity, Long> subjectID;

}