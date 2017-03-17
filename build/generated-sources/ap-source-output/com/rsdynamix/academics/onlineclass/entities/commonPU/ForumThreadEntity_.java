package com.rsdynamix.academics.onlineclass.entities.commonPU;

import com.rsdynamix.academics.onlineclass.entities.ForumThreadEntity;
import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(ForumThreadEntity.class)
public class ForumThreadEntity_ { 

    public static volatile SingularAttribute<ForumThreadEntity, Long> forumThreadID;
    public static volatile SingularAttribute<ForumThreadEntity, String> threadCode;
    public static volatile SingularAttribute<ForumThreadEntity, String> approver;
    public static volatile SingularAttribute<ForumThreadEntity, String> threadStatement;
    public static volatile SingularAttribute<ForumThreadEntity, String> threadCreator;
    public static volatile SingularAttribute<ForumThreadEntity, String> threadTitle;
    public static volatile SingularAttribute<ForumThreadEntity, Date> threadCreateDate;
    public static volatile SingularAttribute<ForumThreadEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<ForumThreadEntity, Long> forumID;

}