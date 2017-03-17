package com.rsdynamix.academics.exam.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(ExamQuestionEntity.class)
public class ExamQuestionEntity_ { 

    public static volatile SingularAttribute<ExamQuestionEntity, Long> examPaperID;
    public static volatile SingularAttribute<ExamQuestionEntity, String> approver;
    public static volatile SingularAttribute<ExamQuestionEntity, Long> questionID;
    public static volatile SingularAttribute<ExamQuestionEntity, Integer> questionRankID;
    public static volatile SingularAttribute<ExamQuestionEntity, String> questionStatement;
    public static volatile SingularAttribute<ExamQuestionEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<ExamQuestionEntity, Integer> questionNumber;

}