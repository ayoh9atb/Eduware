package com.rsdynamix.finance.requisition.entities.reportsPU;

import com.rsdynamix.finance.requisition.entities.PaymentRequisitionEntity;
import com.rsdynamix.finance.requisition.entities.SettlementType;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(PaymentRequisitionEntity.class)
public class PaymentRequisitionEntity_ { 

    public static volatile SingularAttribute<PaymentRequisitionEntity, SettlementType> settlementType;
    public static volatile SingularAttribute<PaymentRequisitionEntity, String> approver;
    public static volatile SingularAttribute<PaymentRequisitionEntity, String> requisitionNumber;
    public static volatile SingularAttribute<PaymentRequisitionEntity, Long> approvalStateID;
    public static volatile SingularAttribute<PaymentRequisitionEntity, String> payeeType;
    public static volatile SingularAttribute<PaymentRequisitionEntity, BigDecimal> paymentAmount;
    public static volatile SingularAttribute<PaymentRequisitionEntity, String> paymentReceiptNumber;
    public static volatile SingularAttribute<PaymentRequisitionEntity, String> payee;
    public static volatile SingularAttribute<PaymentRequisitionEntity, Date> requistionDate;
    public static volatile SingularAttribute<PaymentRequisitionEntity, BigDecimal> totalRequisitionAmount;
    public static volatile SingularAttribute<PaymentRequisitionEntity, String> creditAccountCode;
    public static volatile SingularAttribute<PaymentRequisitionEntity, String> narrationOfRequisition;
    public static volatile SingularAttribute<PaymentRequisitionEntity, BigDecimal> totalOutstandingAmount;
    public static volatile SingularAttribute<PaymentRequisitionEntity, Date> paymentDate;
    public static volatile SingularAttribute<PaymentRequisitionEntity, String> claimNumber;
    public static volatile SingularAttribute<PaymentRequisitionEntity, String> debitAccountCode;

}