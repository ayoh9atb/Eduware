package com.rsdynamix.academics.setup.entities.commonPU;

import com.rsdynamix.academics.setup.entities.AcademicStaffEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(AcademicStaffEntity.class)
public class AcademicStaffEntity_ { 

    public static volatile SingularAttribute<AcademicStaffEntity, String> approver;
    public static volatile SingularAttribute<AcademicStaffEntity, Long> facultyID;
    public static volatile SingularAttribute<AcademicStaffEntity, Long> departmentID;
    public static volatile SingularAttribute<AcademicStaffEntity, String> staffName;
    public static volatile SingularAttribute<AcademicStaffEntity, String> staffNumber;
    public static volatile SingularAttribute<AcademicStaffEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<AcademicStaffEntity, Long> staffID;

}