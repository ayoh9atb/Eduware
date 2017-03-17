package com.rsdynamix.finance.requisition.entities.reportsPU;

import com.rsdynamix.finance.requisition.entities.PaymentEntity;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(PaymentEntity.class)
public class PaymentEntity_ { 

    public static volatile SingularAttribute<PaymentEntity, String> approver;
    public static volatile SingularAttribute<PaymentEntity, String> bankCode;
    public static volatile SingularAttribute<PaymentEntity, BigDecimal> amount;
    public static volatile SingularAttribute<PaymentEntity, String> staffCode;
    public static volatile SingularAttribute<PaymentEntity, Date> entryDate;
    public static volatile SingularAttribute<PaymentEntity, String> fullName;
    public static volatile SingularAttribute<PaymentEntity, String> chequeNumber;
    public static volatile SingularAttribute<PaymentEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<PaymentEntity, String> requistionNumber;
    public static volatile SingularAttribute<PaymentEntity, Long> paymentTypeID;
    public static volatile SingularAttribute<PaymentEntity, String> paymentCode;
    public static volatile SingularAttribute<PaymentEntity, String> bankSortCode;
    public static volatile SingularAttribute<PaymentEntity, Long> budgetEntryID;
    public static volatile SingularAttribute<PaymentEntity, String> creditAccountCode;
    public static volatile SingularAttribute<PaymentEntity, Date> paymentDate;
    public static volatile SingularAttribute<PaymentEntity, String> currencyCode;
    public static volatile SingularAttribute<PaymentEntity, String> debitAccountCode;

}