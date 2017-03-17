package com.rsdynamix.academics.setup.entities.reportsPU;

import com.rsdynamix.academics.setup.entities.StudentCourseEntity;
import com.rsdynamix.academics.setup.entities.TermTier;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(StudentCourseEntity.class)
public class StudentCourseEntity_ { 

    public static volatile SingularAttribute<StudentCourseEntity, String> approver;
    public static volatile SingularAttribute<StudentCourseEntity, Long> studentID;
    public static volatile SingularAttribute<StudentCourseEntity, TermTier> semesterTier;
    public static volatile SingularAttribute<StudentCourseEntity, String> studentNumber;
    public static volatile SingularAttribute<StudentCourseEntity, Integer> creditLoad;
    public static volatile SingularAttribute<StudentCourseEntity, Long> studentCourseID;
    public static volatile SingularAttribute<StudentCourseEntity, Long> sessionID;
    public static volatile SingularAttribute<StudentCourseEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<StudentCourseEntity, Long> courseID;

}