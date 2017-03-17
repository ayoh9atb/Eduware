package com.rsdynamix.academics.exam.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(StudentSelectedOptionEntity.class)
public class StudentSelectedOptionEntity_ { 

    public static volatile SingularAttribute<StudentSelectedOptionEntity, Long> examPaperID;
    public static volatile SingularAttribute<StudentSelectedOptionEntity, String> approver;
    public static volatile SingularAttribute<StudentSelectedOptionEntity, Long> questionOptionID;
    public static volatile SingularAttribute<StudentSelectedOptionEntity, Long> questionID;
    public static volatile SingularAttribute<StudentSelectedOptionEntity, Boolean> answer;
    public static volatile SingularAttribute<StudentSelectedOptionEntity, String> studentNumber;
    public static volatile SingularAttribute<StudentSelectedOptionEntity, Long> selectedOptionID;
    public static volatile SingularAttribute<StudentSelectedOptionEntity, Integer> approvalStatusID;

}