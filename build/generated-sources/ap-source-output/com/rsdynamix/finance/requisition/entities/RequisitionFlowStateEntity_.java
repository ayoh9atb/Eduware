package com.rsdynamix.finance.requisition.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(RequisitionFlowStateEntity.class)
public class RequisitionFlowStateEntity_ { 

    public static volatile SingularAttribute<RequisitionFlowStateEntity, String> approver;
    public static volatile SingularAttribute<RequisitionFlowStateEntity, String> requisitionNumber;
    public static volatile SingularAttribute<RequisitionFlowStateEntity, Integer> approvalStateID;
    public static volatile SingularAttribute<RequisitionFlowStateEntity, Date> dateReceived;
    public static volatile SingularAttribute<RequisitionFlowStateEntity, String> currentStateFlg;
    public static volatile SingularAttribute<RequisitionFlowStateEntity, Date> dateProcessed;
    public static volatile SingularAttribute<RequisitionFlowStateEntity, String> userNameOfApprovalOfficer;
    public static volatile SingularAttribute<RequisitionFlowStateEntity, String> comment;
    public static volatile SingularAttribute<RequisitionFlowStateEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<RequisitionFlowStateEntity, Long> workFlowStateID;

}