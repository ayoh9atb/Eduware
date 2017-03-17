package com.rsdynamix.finance.requisition.entities;

import com.rsdynamix.finance.requisition.entities.AuthorizationType;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(ApprovalStepEntity.class)
public class ApprovalStepEntity_ { 

    public static volatile SingularAttribute<ApprovalStepEntity, String> approver;
    public static volatile SingularAttribute<ApprovalStepEntity, Integer> approveForLowerOfficesInt;
    public static volatile SingularAttribute<ApprovalStepEntity, String> approvalDesc;
    public static volatile SingularAttribute<ApprovalStepEntity, AuthorizationType> authorizationType;
    public static volatile SingularAttribute<ApprovalStepEntity, Integer> workflowType;
    public static volatile SingularAttribute<ApprovalStepEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<ApprovalStepEntity, Integer> approvalStepID;
    public static volatile SingularAttribute<ApprovalStepEntity, Integer> approvalOfficerKey;

}