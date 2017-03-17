package com.rsdynamix.finance.requisition.entities.commonPU;

import com.rsdynamix.finance.requisition.entities.PaymentStatusEntity;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(PaymentStatusEntity.class)
public class PaymentStatusEntity_ { 

    public static volatile SingularAttribute<PaymentStatusEntity, String> approver;
    public static volatile SingularAttribute<PaymentStatusEntity, Integer> paymentStatusID;
    public static volatile SingularAttribute<PaymentStatusEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<PaymentStatusEntity, String> paymentStatusDesc;

}