package com.rsdynamix.academics.onlineclass.entities;

import com.codrellica.projects.date.DateVO;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(ThreadResponseEntity.class)
public class ThreadResponseEntity_ { 

    public static volatile SingularAttribute<ThreadResponseEntity, Long> forumThreadID;
    public static volatile SingularAttribute<ThreadResponseEntity, String> responseStatement;
    public static volatile SingularAttribute<ThreadResponseEntity, String> approver;
    public static volatile SingularAttribute<ThreadResponseEntity, String> responseCreator;
    public static volatile SingularAttribute<ThreadResponseEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<ThreadResponseEntity, Long> forumID;
    public static volatile SingularAttribute<ThreadResponseEntity, DateVO> responseDate;
    public static volatile SingularAttribute<ThreadResponseEntity, Long> responseID;

}