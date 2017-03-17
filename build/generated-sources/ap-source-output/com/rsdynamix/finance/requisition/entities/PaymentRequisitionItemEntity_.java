package com.rsdynamix.finance.requisition.entities;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(PaymentRequisitionItemEntity.class)
public class PaymentRequisitionItemEntity_ { 

    public static volatile SingularAttribute<PaymentRequisitionItemEntity, String> approver;
    public static volatile SingularAttribute<PaymentRequisitionItemEntity, String> requisitionNumber;
    public static volatile SingularAttribute<PaymentRequisitionItemEntity, Long> reserveTypeID;
    public static volatile SingularAttribute<PaymentRequisitionItemEntity, Integer> reserveID;
    public static volatile SingularAttribute<PaymentRequisitionItemEntity, Long> reserveKey;
    public static volatile SingularAttribute<PaymentRequisitionItemEntity, Long> requisitionItemID;
    public static volatile SingularAttribute<PaymentRequisitionItemEntity, String> payeeType;
    public static volatile SingularAttribute<PaymentRequisitionItemEntity, BigDecimal> reserveAmount;
    public static volatile SingularAttribute<PaymentRequisitionItemEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<PaymentRequisitionItemEntity, String> payee;
    public static volatile SingularAttribute<PaymentRequisitionItemEntity, String> reserveDesc;
    public static volatile SingularAttribute<PaymentRequisitionItemEntity, String> narrationOfRequisition;
    public static volatile SingularAttribute<PaymentRequisitionItemEntity, BigDecimal> requisitionAmount;
    public static volatile SingularAttribute<PaymentRequisitionItemEntity, String> claimNumber;

}