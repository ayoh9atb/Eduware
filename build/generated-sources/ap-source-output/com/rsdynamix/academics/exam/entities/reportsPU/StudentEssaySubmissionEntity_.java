package com.rsdynamix.academics.exam.entities.reportsPU;

import com.rsdynamix.academics.exam.entities.StudentEssaySubmissionEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(StudentEssaySubmissionEntity.class)
public class StudentEssaySubmissionEntity_ { 

    public static volatile SingularAttribute<StudentEssaySubmissionEntity, Long> examPaperID;
    public static volatile SingularAttribute<StudentEssaySubmissionEntity, String> approver;
    public static volatile SingularAttribute<StudentEssaySubmissionEntity, Long> submissionID;
    public static volatile SingularAttribute<StudentEssaySubmissionEntity, Long> questionID;
    public static volatile SingularAttribute<StudentEssaySubmissionEntity, String> answerText;
    public static volatile SingularAttribute<StudentEssaySubmissionEntity, String> studentNumber;
    public static volatile SingularAttribute<StudentEssaySubmissionEntity, Double> numberOfPoints;
    public static volatile SingularAttribute<StudentEssaySubmissionEntity, Integer> approvalStatusID;

}