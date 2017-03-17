package com.rsdynamix.finance.requisition.entities.reportsPU;

import com.rsdynamix.finance.requisition.entities.CommissionSettlementEntity;
import com.rsdynamix.finance.requisition.entities.PayoutStatus;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(CommissionSettlementEntity.class)
public class CommissionSettlementEntity_ { 

    public static volatile SingularAttribute<CommissionSettlementEntity, String> expenseDesc;
    public static volatile SingularAttribute<CommissionSettlementEntity, String> approver;
    public static volatile SingularAttribute<CommissionSettlementEntity, String> transactionSource;
    public static volatile SingularAttribute<CommissionSettlementEntity, String> bankCode;
    public static volatile SingularAttribute<CommissionSettlementEntity, Long> commissionID;
    public static volatile SingularAttribute<CommissionSettlementEntity, String> groupSettlementCode;
    public static volatile SingularAttribute<CommissionSettlementEntity, String> accountName;
    public static volatile SingularAttribute<CommissionSettlementEntity, Date> entryDate;
    public static volatile SingularAttribute<CommissionSettlementEntity, PayoutStatus> payoutStatus;
    public static volatile SingularAttribute<CommissionSettlementEntity, String> agencyCode;
    public static volatile SingularAttribute<CommissionSettlementEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<CommissionSettlementEntity, Long> settlementID;
    public static volatile SingularAttribute<CommissionSettlementEntity, String> accountNumber;
    public static volatile SingularAttribute<CommissionSettlementEntity, String> settlementCode;
    public static volatile SingularAttribute<CommissionSettlementEntity, String> referenceNumber;
    public static volatile SingularAttribute<CommissionSettlementEntity, String> creditAccountCode;
    public static volatile SingularAttribute<CommissionSettlementEntity, Double> settlementAmount;
    public static volatile SingularAttribute<CommissionSettlementEntity, String> currencyCode;
    public static volatile SingularAttribute<CommissionSettlementEntity, String> debitAccountCode;

}