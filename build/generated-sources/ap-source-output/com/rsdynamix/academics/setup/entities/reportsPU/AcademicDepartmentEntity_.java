package com.rsdynamix.academics.setup.entities.reportsPU;

import com.rsdynamix.academics.setup.entities.AcademicDepartmentEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(AcademicDepartmentEntity.class)
public class AcademicDepartmentEntity_ { 

    public static volatile SingularAttribute<AcademicDepartmentEntity, String> departmentName;
    public static volatile SingularAttribute<AcademicDepartmentEntity, String> approver;
    public static volatile SingularAttribute<AcademicDepartmentEntity, Long> facultyID;
    public static volatile SingularAttribute<AcademicDepartmentEntity, Long> departmentID;
    public static volatile SingularAttribute<AcademicDepartmentEntity, Integer> approvalStatusID;

}