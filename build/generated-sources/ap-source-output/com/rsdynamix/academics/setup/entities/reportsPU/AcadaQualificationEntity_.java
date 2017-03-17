package com.rsdynamix.academics.setup.entities.reportsPU;

import com.rsdynamix.academics.setup.entities.AcadaQualificationEntity;
import java.math.BigDecimal;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(AcadaQualificationEntity.class)
public class AcadaQualificationEntity_ { 

    public static volatile SingularAttribute<AcadaQualificationEntity, Long> studentID;
    public static volatile SingularAttribute<AcadaQualificationEntity, Long> academicSubjectID;
    public static volatile SingularAttribute<AcadaQualificationEntity, String> approver;
    public static volatile SingularAttribute<AcadaQualificationEntity, String> scoreGrade;
    public static volatile SingularAttribute<AcadaQualificationEntity, Long> qualificationID;
    public static volatile SingularAttribute<AcadaQualificationEntity, BigDecimal> scorePoint;
    public static volatile SingularAttribute<AcadaQualificationEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<AcadaQualificationEntity, Date> qualificationDate;
    public static volatile SingularAttribute<AcadaQualificationEntity, Long> qualificationTypeID;

}