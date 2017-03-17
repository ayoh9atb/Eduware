package com.rsdynamix.academics.onlineclass.bean;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.onlineclass.entities.ThreadResponseEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;

/**
 *
 * @author patushie@codrellica
 */
@SessionScoped
@ManagedBean(name = "threadResponseBean")
public class ThreadResponseBean {

    private ThreadResponseEntity threadResponse;
    private List<ThreadResponseEntity> threadResponseList;
    private List<SelectItem> threadResponseItemList;

    public ThreadResponseBean() {
        threadResponse = new ThreadResponseEntity();
        threadResponseList = new ArrayList<ThreadResponseEntity>();
        threadResponseItemList = new ArrayList<SelectItem>();
    }

    public String searchThreadResponse() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        ThreadResponseEntity criteria = new ThreadResponseEntity();
        criteria.setForumID(getThreadResponse().getForumID());
        criteria.setForumThreadID(getThreadResponse().getForumThreadID());

        try {
            List<AbstractEntity> abstractResponseList = dataServer.findData(criteria);
            for (AbstractEntity entity : abstractResponseList) {
                ThreadResponseEntity responseEntity = (ThreadResponseEntity) entity;
                getThreadResponseList().add(responseEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void forumItemSelected(ValueChangeEvent vce) {
        ForumThreadBean threadBean = (ForumThreadBean) CommonBean.getBeanFromContext(
                "#{applicationScope.forumThreadBean}", ForumThreadBean.class);
        if (threadBean == null) {
            threadBean = new ForumThreadBean();
            CommonBean.setBeanToContext("#{applicationScope.forumThreadBean}", ForumThreadBean.class, threadBean);
        }

        long forumID = Long.parseLong(vce.getNewValue().toString());
        threadBean.loadForumThreadItemListOfForum(forumID);
    }

    public ThreadResponseEntity getThreadResponse() {
        return this.threadResponse;
    }

    public void setThreadResponse(ThreadResponseEntity threadresponse) {
        this.threadResponse = threadresponse;
    }

    public List<ThreadResponseEntity> getThreadResponseList() {
        return this.threadResponseList;
    }

    public void setThreadResponseList(List<ThreadResponseEntity> threadresponseList) {
        this.threadResponseList = threadresponseList;
    }

    public List<SelectItem> getThreadResponseItemList() {
        return this.threadResponseItemList;
    }

    public void setThreadResponseItemList(List<SelectItem> threadresponseItemList) {
        this.threadResponseItemList = threadresponseItemList;
    }
}
