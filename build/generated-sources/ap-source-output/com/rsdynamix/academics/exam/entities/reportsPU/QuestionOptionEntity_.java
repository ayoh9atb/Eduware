package com.rsdynamix.academics.exam.entities.reportsPU;

import com.rsdynamix.academics.exam.entities.QuestionOptionEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(QuestionOptionEntity.class)
public class QuestionOptionEntity_ { 

    public static volatile SingularAttribute<QuestionOptionEntity, Long> examPaperID;
    public static volatile SingularAttribute<QuestionOptionEntity, String> approver;
    public static volatile SingularAttribute<QuestionOptionEntity, Long> questionOptionID;
    public static volatile SingularAttribute<QuestionOptionEntity, Long> questionID;
    public static volatile SingularAttribute<QuestionOptionEntity, Boolean> answer;
    public static volatile SingularAttribute<QuestionOptionEntity, String> optionLetter;
    public static volatile SingularAttribute<QuestionOptionEntity, String> optionText;
    public static volatile SingularAttribute<QuestionOptionEntity, Integer> approvalStatusID;

}