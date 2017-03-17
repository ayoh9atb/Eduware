package com.rsdynamix.academics.setup.entities.commonPU;

import com.rsdynamix.academics.setup.entities.SubjectOfStudyEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(SubjectOfStudyEntity.class)
public class SubjectOfStudyEntity_ { 

    public static volatile SingularAttribute<SubjectOfStudyEntity, String> approver;
    public static volatile SingularAttribute<SubjectOfStudyEntity, Long> facultyID;
    public static volatile SingularAttribute<SubjectOfStudyEntity, String> subjectTitle;
    public static volatile SingularAttribute<SubjectOfStudyEntity, Long> departmentID;
    public static volatile SingularAttribute<SubjectOfStudyEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<SubjectOfStudyEntity, Long> subjectID;
    public static volatile SingularAttribute<SubjectOfStudyEntity, Integer> numberOfLevels;

}