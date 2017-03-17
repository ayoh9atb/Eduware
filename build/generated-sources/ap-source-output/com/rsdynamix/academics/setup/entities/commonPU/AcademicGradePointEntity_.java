package com.rsdynamix.academics.setup.entities.commonPU;

import com.rsdynamix.academics.setup.entities.AcademicGradePointEntity;
import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(AcademicGradePointEntity.class)
public class AcademicGradePointEntity_ { 

    public static volatile SingularAttribute<AcademicGradePointEntity, String> gradeTitle;
    public static volatile SingularAttribute<AcademicGradePointEntity, String> approver;
    public static volatile SingularAttribute<AcademicGradePointEntity, Integer> gradePoint;
    public static volatile SingularAttribute<AcademicGradePointEntity, String> gradeCharacter;
    public static volatile SingularAttribute<AcademicGradePointEntity, Long> gradePointID;
    public static volatile SingularAttribute<AcademicGradePointEntity, BigDecimal> upperLimit;
    public static volatile SingularAttribute<AcademicGradePointEntity, BigDecimal> lowerLimit;
    public static volatile SingularAttribute<AcademicGradePointEntity, Integer> approvalStatusID;

}