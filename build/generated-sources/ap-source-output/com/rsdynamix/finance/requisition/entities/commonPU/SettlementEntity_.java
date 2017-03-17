package com.rsdynamix.finance.requisition.entities.commonPU;

import com.rsdynamix.finance.requisition.entities.PayoutStatus;
import com.rsdynamix.finance.requisition.entities.SettlementEntity;
import com.rsdynamix.finance.requisition.entities.SettlementType;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(SettlementEntity.class)
public class SettlementEntity_ { 

    public static volatile SingularAttribute<SettlementEntity, String> expenseDesc;
    public static volatile SingularAttribute<SettlementEntity, SettlementType> settlementType;
    public static volatile SingularAttribute<SettlementEntity, String> approver;
    public static volatile SingularAttribute<SettlementEntity, String> bankCode;
    public static volatile SingularAttribute<SettlementEntity, String> groupSettlementCode;
    public static volatile SingularAttribute<SettlementEntity, String> accountName;
    public static volatile SingularAttribute<SettlementEntity, Date> entryDate;
    public static volatile SingularAttribute<SettlementEntity, Integer> instalmentNumber;
    public static volatile SingularAttribute<SettlementEntity, PayoutStatus> payoutStatus;
    public static volatile SingularAttribute<SettlementEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<SettlementEntity, Long> settlementID;
    public static volatile SingularAttribute<SettlementEntity, String> accountNumber;
    public static volatile SingularAttribute<SettlementEntity, String> settlementCode;
    public static volatile SingularAttribute<SettlementEntity, String> referenceNumber;
    public static volatile SingularAttribute<SettlementEntity, Date> instalStartDate;
    public static volatile SingularAttribute<SettlementEntity, String> creditAccountCode;
    public static volatile SingularAttribute<SettlementEntity, Double> settlementAmount;
    public static volatile SingularAttribute<SettlementEntity, Date> instalEndDate;
    public static volatile SingularAttribute<SettlementEntity, String> debitAccountCode;

}