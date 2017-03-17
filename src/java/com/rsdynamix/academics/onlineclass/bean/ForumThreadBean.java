package com.rsdynamix.academics.onlineclass.bean;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.onlineclass.entities.ForumEntity;
import com.rsdynamix.academics.onlineclass.entities.ForumThreadEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.codrellica.projects.commons.DateUtil;
import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
@ManagedBean(name = "forumThreadBean")
public class ForumThreadBean {

    private static final String FORUM_THREAD_ID_KEY = "forum_thread_id";
    //
    private static final String FORUM_THREAD_ID_DEFAULT = "1";
    //
    private static final String THREAD_CODE_PRFX_KEY = "fthread_code_id";
    //
    private static final String THREAD_CODE_PRFX_DEFAULT = "OCT";
    //
    private ForumThreadEntity forumThread;
    private List<ForumThreadEntity> forumThreadList;
    private List<SelectItem> forumThreadItemList;
    private List<SelectItem> selectedForumThreadItemList;

    public ForumThreadBean() {
        forumThread = new ForumThreadEntity();
        forumThreadList = new ArrayList<ForumThreadEntity>();
        forumThreadItemList = new ArrayList<SelectItem>();
        selectedForumThreadItemList = new ArrayList<SelectItem>();

        loadForumThreadList();
    }

    public void loadForumThreadList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ForumBean forumBean = (ForumBean) CommonBean.getBeanFromContext(
                "#{applicationScope.forumBean}", ForumBean.class);
        if (forumBean == null) {
            forumBean = new ForumBean();
            CommonBean.setBeanToContext("#{applicationScope.forumBean}", ForumBean.class, forumBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        ForumThreadEntity criteria = new ForumThreadEntity();

        try {
            List<AbstractEntity> abstractThreadList = dataServer.findData(criteria);
            for (AbstractEntity entity : abstractThreadList) {
                ForumThreadEntity threadEntity = (ForumThreadEntity) entity;

                ForumEntity forum = forumBean.findForumByID(threadEntity.getForumID());
                if (forum != null) {
                    threadEntity.setForumName(forum.getForumName());
                }

                threadEntity.setThreadCreateDateTO(DateUtil.toUtilDate(threadEntity.getThreadCreateDate()));

                SelectItem item = new SelectItem();
                item.setValue(threadEntity.getForumThreadID());
                item.setLabel(threadEntity.getThreadTitle());
                item.setDescription(String.valueOf(threadEntity.getForumID()));
                forumThreadItemList.add(item);

                forumThreadList.add(threadEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public void loadForumThreadItemListOfForum(long forumID) {
        setSelectedForumThreadItemList(new ArrayList<SelectItem>());

        for (SelectItem item : forumThreadItemList) {
            if (Long.parseLong(item.getDescription()) == forumID) {
                getSelectedForumThreadItemList().add(item);
            }
        }
    }

    public String addForumThread() {
        if (!getForumThreadList().contains(getForumThread())) {
            UserManagerBean userBean = (UserManagerBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.userBean}", UserManagerBean.class);
            if (userBean == null) {
                userBean = new UserManagerBean();
                CommonBean.setBeanToContext("#{sessionScope.userBean}", UserManagerBean.class, userBean);
            }

            ForumBean forumBean = (ForumBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.forumBean}", ForumBean.class);
            if (forumBean == null) {
                forumBean = new ForumBean();
                CommonBean.setBeanToContext("#{applicationScope.forumBean}", ForumBean.class, forumBean);
            }

            getForumThread().setThreadCreator(userBean.getUserAccount().getEmployeeCode());
            getForumThread().setThreadCreateDateTO(new Date());

            ForumEntity forum = forumBean.findForumByID(getForumThread().getForumID());
            if (forum != null) {
                getForumThread().setForumName(forum.getForumName());
            }

            getForumThreadList().add(getForumThread());
        } else {
            int index = getForumThreadList().indexOf(getForumThread());
            getForumThreadList().set(index, getForumThread());
        }
        setForumThread(new ForumThreadEntity());

        return "";
    }

    public String saveForumThreadList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext("#{applicationScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{applicationScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        dataServer.beginTransaction();
        try {
            for (ForumThreadEntity forum : getForumThreadList()) {
                forum.setThreadCreateDate(DateUtil.toSQLDate(forum.getThreadCreateDateTO()));
                if (forum.getForumThreadID() == 0) {
                    try {
                        int cmID = Integer.parseInt(appPropBean.getValue(FORUM_THREAD_ID_KEY,
                                FORUM_THREAD_ID_DEFAULT, true));
                        forum.setForumThreadID(cmID);

                        String threadPrefix = appPropBean.getValue(THREAD_CODE_PRFX_KEY, THREAD_CODE_PRFX_DEFAULT, false);
                        forum.setThreadCode(threadPrefix + "/" + DateUtil.getCurrentDateStr().split("/")[2] + "/" + cmID);
                        dataServer.saveData(forum);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (forum.getForumThreadID() > 0) {
                    dataServer.updateData(forum);
                }
            }
            dataServer.endTransaction();
        } catch (SQLException ex) {
            dataServer.rollbackTransaction();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }
    
    public String activate() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}",
                    UserManagerBean.class, userManagerBean);
        }

        BusinessActionTrailBean businessActionTrailBean = (BusinessActionTrailBean) CommonBean.getBeanFromContext(
                "#{sessionScope.businessActionTrailBean}", BusinessActionTrailBean.class);
        if (businessActionTrailBean == null) {
            businessActionTrailBean = new BusinessActionTrailBean();
            CommonBean.setBeanToContext("#{sessionScope.businessActionTrailBean}",
                    BusinessActionTrailBean.class, businessActionTrailBean);
        }

        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        MenuEntity privilege = privilegeBean.findPrivilegeByName(
                menuManagerBean.getSystemMap().get(MenuManagerBean.CR8_CLIENT_MENU_ITEM));

        if (forumThread.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((forumThread.getApprovalStatusID() == 0) || (forumThread.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(forumThread.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        forumThread.setApprovalStatusID(forumThread.getApprovalStatusID() + 1);
                        forumThread.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(forumThread);
                            dataServer.endTransaction();
                        } catch (SQLException ex) {
                            dataServer.rollbackTransaction();
                            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        applicationMessageBean.insertMessage("Operation Activated Successfully!", MessageType.SUCCESS_MESSAGE);
                    } else if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            == creatorAcct.getRole().getApprovalLevelID()) {
                        applicationMessageBean.insertMessage("You cannot approve an operation performed "
                                + "by another user who has the same approval level as you!",
                                MessageType.ERROR_MESSAGE);
                    } else if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            < creatorAcct.getRole().getApprovalLevelID()) {
                        applicationMessageBean.insertMessage("You cannot approve an operation performed "
                                + "by another user who has a higher approval level as you!",
                                MessageType.ERROR_MESSAGE);
                    }
                } else if (forumThread.getApprovalStatusID()
                        > userManagerBean.getUserAccount().getRole().getApprovalLevelID()) {
                    applicationMessageBean.insertMessage("This operation has been approved beyond "
                            + "your approval level, your approval is no longer effective!",
                            MessageType.ERROR_MESSAGE);
                }
            } else {
                applicationMessageBean.insertMessage("Access Denied: You do not have approval rights for this operation type!",
                        MessageType.ERROR_MESSAGE);
            }
        } else {
            applicationMessageBean.insertMessage("This operation is already approved. No action performed!",
                    MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String loadClassOfDegreeList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        forumThread = new ForumThreadEntity();
        forumThreadList = new ArrayList<ForumThreadEntity>();
        forumThreadItemList = new ArrayList<SelectItem>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ForumThreadEntity criteria = new ForumThreadEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            ForumThreadEntity forumThread = (ForumThreadEntity) entity;
            forumThreadList.add(forumThread);
        }

        return "";
    }

    public String loadPreviousHistoricalState() {
        BusinessActionTrailBean businessActionTrailBean = (BusinessActionTrailBean) CommonBean.getBeanFromContext(
                "#{sessionScope.businessActionTrailBean}", BusinessActionTrailBean.class);
        if (businessActionTrailBean == null) {
            businessActionTrailBean = new BusinessActionTrailBean();
            CommonBean.setBeanToContext("#{sessionScope.businessActionTrailBean}",
                    BusinessActionTrailBean.class, businessActionTrailBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (forumThreadList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        forumThreadList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadClassOfDegreeList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadClassOfDegreeList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }

    public String loadNextHistoricalState() {
        BusinessActionTrailBean businessActionTrailBean = (BusinessActionTrailBean) CommonBean.getBeanFromContext(
                "#{sessionScope.businessActionTrailBean}", BusinessActionTrailBean.class);
        if (businessActionTrailBean == null) {
            businessActionTrailBean = new BusinessActionTrailBean();
            CommonBean.setBeanToContext("#{sessionScope.businessActionTrailBean}",
                    BusinessActionTrailBean.class, businessActionTrailBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (forumThreadList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(forumThreadList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadClassOfDegreeList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadClassOfDegreeList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }

    public String gotoAuditTrailPage() {
        String outcome = "";

        BusinessActionTrailBean businessActionTrailBean = (BusinessActionTrailBean) CommonBean.getBeanFromContext(
                "#{sessionScope.businessActionTrailBean}", BusinessActionTrailBean.class);
        if (businessActionTrailBean == null) {
            businessActionTrailBean = new BusinessActionTrailBean();
            CommonBean.setBeanToContext("#{sessionScope.businessActionTrailBean}",
                    BusinessActionTrailBean.class, businessActionTrailBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.FORUM_THREAD);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(forumThread.getForumThreadID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadClassOfDegreeList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "classofdegreeaudittrail.jsf";
            } else {
                applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return outcome;
    }

    public String deleteClassOfDegree() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        dataServer.beginTransaction();
        try {
            if (forumThreadList.contains(forumThread)) {
                if (forumThread.getForumThreadID() > 0) {
                    ForumThreadEntity criteria = new ForumThreadEntity();
                    criteria.setForumThreadID(forumThread.getForumThreadID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                forumThreadList.remove(forumThread);
                forumThread = new ForumThreadEntity();
            }
        } catch (SQLException ex) {
            dataServer.rollbackTransaction();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public ForumThreadEntity findExamPaperByID(long forumID) {
        ForumThreadEntity forum = null;

        for (ForumThreadEntity threadEntity : getForumThreadList()) {
            if (threadEntity.getForumID() == forumID) {
                forum = threadEntity;
                break;
            }
        }

        return forum;
    }

    public void forumThreadItemSelected(ValueChangeEvent vce) {
    }

    public void forumThreadSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
        setForumThread(getForumThreadList().get(rowIndex));
    }

    public void forumItemSelected(ValueChangeEvent vce) {
    }

    public ForumThreadEntity getForumThread() {
        return this.forumThread;
    }

    public void setForumThread(ForumThreadEntity forumthread) {
        this.forumThread = forumthread;
    }

    public List<ForumThreadEntity> getForumThreadList() {
        return this.forumThreadList;
    }

    public void setForumThreadList(List<ForumThreadEntity> forumthreadList) {
        this.forumThreadList = forumthreadList;
    }

    public List<SelectItem> getForumThreadItemList() {
        return this.forumThreadItemList;
    }

    public void setForumThreadItemList(List<SelectItem> forumthreadItemList) {
        this.forumThreadItemList = forumthreadItemList;
    }

    /**
     * @return the selectedForumThreadItemList
     */
    public List<SelectItem> getSelectedForumThreadItemList() {
        return selectedForumThreadItemList;
    }

    /**
     * @param selectedForumThreadItemList the selectedForumThreadItemList to set
     */
    public void setSelectedForumThreadItemList(List<SelectItem> selectedForumThreadItemList) {
        this.selectedForumThreadItemList = selectedForumThreadItemList;
    }
}
