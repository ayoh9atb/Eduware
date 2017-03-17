/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.exam.bean;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.exam.entities.QuestionRankEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;

/**
 *
 * @author root
 */
@SessionScoped
@ManagedBean(name = "questionRankBean")
public class QuestionRankBean {

    private static final String QUESTION_RANK_ID_KEY = "questn_rank_id";
    //
    private QuestionRankEntity questionRank;
    private List<QuestionRankEntity> questionRankList;
    private List<SelectItem> questionRankItemList;

    public QuestionRankBean() {
        questionRank = new QuestionRankEntity();
        questionRankList = new ArrayList<QuestionRankEntity>();
        questionRankItemList = new ArrayList<SelectItem>();

        loadQuestionRankList();
    }
    
    public String addQuestionRank() {
        if (!questionRankList.contains(questionRank)) {
            questionRankList.add(questionRank);
        } else {
            int index = questionRankList.indexOf(questionRank);
            questionRankList.set(index, questionRank);
        }
        questionRank = new QuestionRankEntity();

        return "";
    }

    public void loadQuestionRankList() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
        QuestionRankEntity criteria = new QuestionRankEntity();

        try {
            List<AbstractEntity> abstractQuestionRankList = dataServer.findData(criteria);
            for (AbstractEntity entity : abstractQuestionRankList) {
                QuestionRankEntity qrEntity = (QuestionRankEntity) entity;

                SelectItem item = new SelectItem();
                item.setValue(qrEntity.getQuestionRankID());
                item.setLabel(qrEntity.getQuestionRankDesc());
                getQuestionRankItemList().add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }
    
    public void addToSelectItemList(QuestionRankEntity entity) {
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

        if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
            MenuEntity privilege = privilegeBean.findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.DISCNT_SETP_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getQuestionRankID());
                item.setLabel(entity.getQuestionRankDesc());
                questionRankItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getQuestionRankID());
            item.setLabel(entity.getQuestionRankDesc());
            questionRankItemList.add(item);
        }
    }

    public String saveQuestionRankList() {
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
            for (QuestionRankEntity zone : questionRankList) {
                if (zone.getQuestionRankID() == 0) {
                    try {
                        int cmID = Integer.parseInt(appPropBean.getValue(QUESTION_RANK_ID_KEY,
                                FinultimateConstants.ONE_STR, true));
                        zone.setQuestionRankID(cmID);
                        dataServer.saveData(zone);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (zone.getQuestionRankID() > 0) {
                    dataServer.updateData(zone);
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

        if (questionRank.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((questionRank.getApprovalStatusID() == 0) || (questionRank.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(questionRank.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        questionRank.setApprovalStatusID(questionRank.getApprovalStatusID() + 1);
                        questionRank.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(questionRank);
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
                } else if (questionRank.getApprovalStatusID()
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

    public String loadQuestionRankList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        questionRank = new QuestionRankEntity();
        questionRankList = new ArrayList<QuestionRankEntity>();
        questionRankItemList = new ArrayList<SelectItem>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        QuestionRankEntity criteria = new QuestionRankEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            QuestionRankEntity questionRank = (QuestionRankEntity) entity;
            questionRankList.add(questionRank);
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
            if (questionRankList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        questionRankList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadQuestionRankList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadQuestionRankList(batEntity);
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
            if (questionRankList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(questionRankList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadQuestionRankList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadQuestionRankList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.QUESTION_RANK);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(questionRank.getQuestionRankID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadQuestionRankList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "questnrankaudittrail.jsf";
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

    public String deleteQuestionRank() {
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
            if (questionRankList.contains(questionRank)) {
                if (questionRank.getQuestionRankID() > 0) {
                    QuestionRankEntity criteria = new QuestionRankEntity();
                    criteria.setQuestionRankID(questionRank.getQuestionRankID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                questionRankList.remove(questionRank);
                questionRank = new QuestionRankEntity();
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

    public String findQuestionRankDescByID(long rankID) {
        String rank = null;

        for (SelectItem rankItem : getQuestionRankItemList()) {
            if (Long.parseLong(rankItem.getValue().toString()) == rankID) {
                rank = rankItem.getLabel();
                break;
            }
        }

        return rank;
    }

    /**
     * @return the questionRankItemList
     */
    public List<SelectItem> getQuestionRankItemList() {
        return questionRankItemList;
    }

    /**
     * @param questionRankItemList the questionRankItemList to set
     */
    public void setQuestionRankItemList(List<SelectItem> questionRankItemList) {
        this.questionRankItemList = questionRankItemList;
    }

    /**
     * @return the questionRank
     */
    public QuestionRankEntity getQuestionRank() {
        return questionRank;
    }

    /**
     * @param questionRank the questionRank to set
     */
    public void setQuestionRank(QuestionRankEntity questionRank) {
        this.questionRank = questionRank;
    }

    /**
     * @return the questionRankList
     */
    public List<QuestionRankEntity> getQuestionRankList() {
        return questionRankList;
    }

    /**
     * @param questionRankList the questionRankList to set
     */
    public void setQuestionRankList(List<QuestionRankEntity> questionRankList) {
        this.questionRankList = questionRankList;
    }

}
