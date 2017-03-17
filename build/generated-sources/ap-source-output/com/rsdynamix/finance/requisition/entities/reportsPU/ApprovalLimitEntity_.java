package com.rsdynamix.finance.requisition.entities.reportsPU;

import com.rsdynamix.finance.requisition.entities.ApprovalLimitEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(ApprovalLimitEntity.class)
public class ApprovalLimitEntity_ { 

    public static volatile SingularAttribute<ApprovalLimitEntity, Integer> nextApprovalStepID;
    public static volatile SingularAttribute<ApprovalLimitEntity, String> approver;
    public static volatile SingularAttribute<ApprovalLimitEntity, Integer> approvalLimitID;
    public static volatile SingularAttribute<ApprovalLimitEntity, Double> approvalLimit;
    public static volatile SingularAttribute<ApprovalLimitEntity, Integer> workflowType;
    public static volatile SingularAttribute<ApprovalLimitEntity, String> actionStepImageFileName;
    public static volatile SingularAttribute<ApprovalLimitEntity, Integer> approvalStepID;
    public static volatile SingularAttribute<ApprovalLimitEntity, Integer> approvalStatusID;

}