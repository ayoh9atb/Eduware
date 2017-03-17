package com.rsdynamix.finance.requisition.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(GroupSettlementEntity.class)
public class GroupSettlementEntity_ { 

    public static volatile SingularAttribute<GroupSettlementEntity, String> approver;
    public static volatile SingularAttribute<GroupSettlementEntity, Date> entryDate;
    public static volatile SingularAttribute<GroupSettlementEntity, String> narration;
    public static volatile SingularAttribute<GroupSettlementEntity, Double> settlementAmount;
    public static volatile SingularAttribute<GroupSettlementEntity, Long> settlementID;
    public static volatile SingularAttribute<GroupSettlementEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<GroupSettlementEntity, String> settlementCode;

}