package com.rsdynamix.academics.exam.entities;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(ExamPaperEntity.class)
public class ExamPaperEntity_ { 

    public static volatile SingularAttribute<ExamPaperEntity, Long> examPaperID;
    public static volatile SingularAttribute<ExamPaperEntity, String> approver;
    public static volatile SingularAttribute<ExamPaperEntity, String> examPaperNumber;
    public static volatile SingularAttribute<ExamPaperEntity, Integer> numberOfQuestion;
    public static volatile SingularAttribute<ExamPaperEntity, Boolean> variableStartTime;
    public static volatile SingularAttribute<ExamPaperEntity, String> examPaperTitle;
    public static volatile SingularAttribute<ExamPaperEntity, Date> paperDate;
    public static volatile SingularAttribute<ExamPaperEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<ExamPaperEntity, Integer> paperDurationInMinutes;
    public static volatile SingularAttribute<ExamPaperEntity, Long> courseID;
    public static volatile SingularAttribute<ExamPaperEntity, Long> subjectID;
    public static volatile SingularAttribute<ExamPaperEntity, String> paperStartTime;

}