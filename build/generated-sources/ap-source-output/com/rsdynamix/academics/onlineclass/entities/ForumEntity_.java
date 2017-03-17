package com.rsdynamix.academics.onlineclass.entities;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-03-14T12:57:08")
@StaticMetamodel(ForumEntity.class)
public class ForumEntity_ { 

    public static volatile SingularAttribute<ForumEntity, String> approver;
    public static volatile SingularAttribute<ForumEntity, String> creator;
    public static volatile SingularAttribute<ForumEntity, String> forumName;
    public static volatile SingularAttribute<ForumEntity, Integer> approvalStatusID;
    public static volatile SingularAttribute<ForumEntity, Long> forumID;
    public static volatile SingularAttribute<ForumEntity, Date> createDate;

}