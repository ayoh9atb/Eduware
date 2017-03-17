package com.rsdynamix.academics.exam.bean;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.exam.entities.ExamPaperEntity;
import com.rsdynamix.academics.exam.entities.ExamQuestionEntity;
import com.rsdynamix.academics.exam.entities.QuestionOptionEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;

/**
 *
 * @author patushie@codrellica
 */
@ApplicationScoped
@ManagedBean(name = "examQuestionBean")
public class ExamQuestionBean {

    private static final String EXAM_QUESTN_ID_KEY = "exam_qstn_id";
    //
    private static final String EXAM_QUESTN_ID_DEFAULT = "1";
    //
    private static final String QUESTN_OPTION_ID_KEY = "qstn_optn_id";
    //
    private static final String QUESTN_OPTION_ID_DEFAULT = "1";
    //
    private int questionNumber;
    private ExamQuestionEntity examQuestion;
    private List<ExamQuestionEntity> examQuestionList;
    private QuestionOptionEntity questionOption;
    private List<SelectItem> examQuestionItemList;

    public ExamQuestionBean() {
        questionNumber = 0;
        examQuestion = new ExamQuestionEntity();
        questionOption = new QuestionOptionEntity();
        examQuestionList = new ArrayList<ExamQuestionEntity>();
        examQuestionItemList = new ArrayList<SelectItem>();
    }

    public String searchForQuestions() {
        if (getExamQuestion().getExamPaperID() > 0) {
            examQuestionList = new ArrayList<ExamQuestionEntity>();

            FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

            ExamQuestionEntity criteria = new ExamQuestionEntity();
            criteria.setExamPaperID(getExamQuestion().getExamPaperID());

            ExamPaperBean examPaperBean = (ExamPaperBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.examPaperBean}", ExamPaperBean.class);
            if (examPaperBean == null) {
                examPaperBean = new ExamPaperBean();
                CommonBean.setBeanToContext("#{applicationScope.examPaperBean}", ExamPaperBean.class, examPaperBean);
            }

            QuestionRankBean questionRankBean = (QuestionRankBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.questionRankBean}", QuestionRankBean.class);
            if (questionRankBean == null) {
                questionRankBean = new QuestionRankBean();
                CommonBean.setBeanToContext("#{applicationScope.questionRankBean}", QuestionRankBean.class, questionRankBean);
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
                List<AbstractEntity> abstractQuestionList = dataServer.findData(criteria);
                for (AbstractEntity entity : abstractQuestionList) {
                    ExamQuestionEntity questEntity = (ExamQuestionEntity) entity;
                    questEntity.initializeTransientFields();

                    ExamPaperEntity paper = examPaperBean.findExamPaperByID(questEntity.getExamPaperID());
                    if (paper != null) {
                        questEntity.setExamPaperTitle(paper.getExamPaperTitle());
                    }

                    String rankDesc = questionRankBean.findQuestionRankDescByID(questEntity.getQuestionRankID());
                    if (rankDesc != null) {
                        questEntity.setQuestionRankDesc(rankDesc);
                    }
                    questEntity = searchQuestionOptions(questEntity);

                    examQuestionList.add(questEntity);
                }

                Collections.sort(examQuestionList, new Comparator<ExamQuestionEntity>() {

                    public int compare(ExamQuestionEntity o1, ExamQuestionEntity o2) {
                        return o1.getQuestionNumber() - o2.getQuestionNumber();
                    }
                });
                questionNumber = examQuestionList.get(examQuestionList.size() - 1).getQuestionNumber();
            } catch (Exception ex) {
                ex.printStackTrace();
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            }
        } else {
        }

        return "";
    }

    public ExamQuestionEntity searchQuestionOptions(ExamQuestionEntity questEntity) {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (questEntity.getExamPaperID() > 0) {
            questEntity.setOptionList(new ArrayList<QuestionOptionEntity>());

            FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

            QuestionOptionEntity criteria = new QuestionOptionEntity();
            criteria.setExamPaperID(questEntity.getExamPaperID());
            criteria.setQuestionID(questEntity.getQuestionID());

            try {
                List<AbstractEntity> abstractOptionList = dataServer.findData(criteria);
                for (AbstractEntity entity : abstractOptionList) {
                    QuestionOptionEntity optionEntity = (QuestionOptionEntity) entity;

                    questEntity.getOptionList().add(optionEntity);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            }
        } else {
        }

        return questEntity;
    }

    public ExamQuestionEntity searchForQuestionByID(long questionID) {
        ExamQuestionEntity question = null;
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            ExamQuestionEntity questCriteria = new ExamQuestionEntity();
            questCriteria.setQuestionID(questionID);

            List<AbstractEntity> abstractQuestList = dataServer.findData(questCriteria);
            if ((abstractQuestList != null) && (abstractQuestList.size() > 0)) {
                question = (ExamQuestionEntity) abstractQuestList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return question;
    }

    public QuestionOptionEntity searchForQuestionOptionByID(long optionID) {
        QuestionOptionEntity option = null;
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            QuestionOptionEntity optCriteria = new QuestionOptionEntity();
            optCriteria.setQuestionOptionID(optionID);
            List<AbstractEntity> abstractQuestList = dataServer.findData(optCriteria);
            if ((abstractQuestList != null) && (abstractQuestList.size() > 0)) {
                option = (QuestionOptionEntity) abstractQuestList.get(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return option;
    }

    public String addExamQuestion() {
        if (!getExamQuestionList().contains(getExamQuestion())) {
            getExamQuestion().setQuestionNumber(++questionNumber);
            getExamQuestionList().add(getExamQuestion());
        } else {
            int index = getExamQuestionList().indexOf(getExamQuestion());
            getExamQuestionList().set(index, getExamQuestion());
        }
        String paperDesc = getExamQuestion().getExamPaperTitle();
        long paperID = getExamQuestion().getExamPaperID();

        setExamQuestion(new ExamQuestionEntity());

        getExamQuestion().setExamPaperTitle(paperDesc);
        getExamQuestion().setExamPaperID(paperID);

        return "";
    }

    public String addQuestionOption() {
        if (!examQuestion.getOptionList().contains(getQuestionOption())) {
            examQuestion.getOptionList().add(getQuestionOption());
        } else {
            int index = examQuestion.getOptionList().indexOf(getQuestionOption());
            examQuestion.getOptionList().set(index, getQuestionOption());
        }
        setQuestionOption(new QuestionOptionEntity());

        return "";
    }

    public String saveExamQuestionList() {
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
            for (ExamQuestionEntity question : getExamQuestionList()) {
                if (question.getQuestionID() == 0) {
                    int cmID = Integer.parseInt(appPropBean.getValue(EXAM_QUESTN_ID_KEY,
                            EXAM_QUESTN_ID_DEFAULT, true));
                    question.setQuestionID(cmID);
                    dataServer.saveData(question);
                    saveQuestionOptionList(question.getOptionList(), question.getQuestionID(), question.getExamPaperID());
                } else if (question.getQuestionID() > 0) {
                    dataServer.updateData(question);
                    saveQuestionOptionList(question.getOptionList(), question.getQuestionID(), question.getExamPaperID());
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

    public String saveQuestionOptionList(List<QuestionOptionEntity> questOptionList, long questionID, long examPaperID) throws SQLException, Exception {
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

        for (QuestionOptionEntity option : questOptionList) {
            if (option.getQuestionOptionID() == 0) {
                int cmID = Integer.parseInt(appPropBean.getValue(QUESTN_OPTION_ID_KEY, QUESTN_OPTION_ID_DEFAULT, true));
                
                option.setQuestionOptionID(cmID);
                option.setQuestionID(questionID);
                option.setExamPaperID(examPaperID);
                dataServer.saveData(option);
            } else if (option.getQuestionOptionID() > 0) {
                option.setQuestionID(questionID);
                option.setExamPaperID(examPaperID);
                dataServer.updateData(option);
            }
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

        if (examQuestion.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((examQuestion.getApprovalStatusID() == 0) || (examQuestion.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(examQuestion.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        examQuestion.setApprovalStatusID(examQuestion.getApprovalStatusID() + 1);
                        examQuestion.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(examQuestion);
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
                } else if (examQuestion.getApprovalStatusID()
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

    public String loadExamQuestionList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        examQuestion = new ExamQuestionEntity();
        examQuestionList = new ArrayList<ExamQuestionEntity>();
        examQuestionItemList = new ArrayList<SelectItem>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ExamQuestionEntity criteria = new ExamQuestionEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            ExamQuestionEntity examQuestion = (ExamQuestionEntity) entity;
            examQuestionList.add(examQuestion);
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
            if (examQuestionList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        examQuestionList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadExamQuestionList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadExamQuestionList(batEntity);
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
            if (examQuestionList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(examQuestionList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadExamQuestionList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadExamQuestionList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.EXAM_QUESTION);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(examQuestion.getQuestionID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadExamQuestionList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "examquestionaudittrail.jsf";
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

    public String deleteExamQuestion() {
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
            if (examQuestionList.contains(examQuestion)) {
                if (examQuestion.getQuestionID() > 0) {
                    ExamQuestionEntity criteria = new ExamQuestionEntity();
                    criteria.setQuestionID(examQuestion.getQuestionID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                examQuestionList.remove(examQuestion);
                examQuestion = new ExamQuestionEntity();
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

    public void examQuestionItemSelected(ValueChangeEvent vce) {
    }

    public void examQuestionSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);

        examQuestion = getExamQuestionList().get(rowIndex);

        List<QuestionOptionEntity> optionList = findQuestionOptionListByQuestionID(examQuestion.getQuestionID());
        if (optionList.size() > 0) {
            examQuestion.setOptionList(optionList);
        }
    }

    public void examPaperItemSelected(ValueChangeEvent vce) {
        long paperID = Long.parseLong(vce.getNewValue().toString());

        ExamPaperBean examPaperBean = (ExamPaperBean) CommonBean.getBeanFromContext(
                "#{applicationScope.examPaperBean}", ExamPaperBean.class);
        if (examPaperBean == null) {
            examPaperBean = new ExamPaperBean();
            CommonBean.setBeanToContext("#{applicationScope.examPaperBean}", ExamPaperBean.class, examPaperBean);
        }

        ExamPaperEntity paper = examPaperBean.findExamPaperByID(paperID);
        if (paper != null) {
            getExamQuestion().setExamPaperTitle(paper.getExamPaperTitle());
        }
    }

    public void questionRankItemSelected(ValueChangeEvent vce) {
        long rankID = Long.parseLong(vce.getNewValue().toString());

        QuestionRankBean questionRankBean = (QuestionRankBean) CommonBean.getBeanFromContext(
                "#{applicationScope.questionRankBean}", QuestionRankBean.class);
        if (questionRankBean == null) {
            questionRankBean = new QuestionRankBean();
            CommonBean.setBeanToContext("#{applicationScope.questionRankBean}", QuestionRankBean.class, questionRankBean);
        }

        String rankDesc = questionRankBean.findQuestionRankDescByID(rankID);
        if (rankDesc != null) {
            getExamQuestion().setQuestionRankDesc(rankDesc);
        }
    }

    public void questionOptionItemSelected(ValueChangeEvent vce) {
    }

    public void questionOptionSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
        questionOption = getExamQuestion().getOptionList().get(rowIndex);
    }

    public ExamQuestionEntity findExamQuestionByID(long questionID) {
        ExamQuestionEntity question = null;

        for (ExamQuestionEntity questEntity : getExamQuestionList()) {
            if (questEntity.getExamPaperID() == questionID) {
                question = questEntity;
                break;
            }
        }

        return question;
    }

    public QuestionOptionEntity findQuestionOptionByID(long optionID) {
        QuestionOptionEntity option = null;

        for (QuestionOptionEntity optionEntity : examQuestion.getOptionList()) {
            if (optionEntity.getQuestionOptionID() == optionID) {
                option = optionEntity;
                break;
            }
        }

        return option;
    }

    public List<QuestionOptionEntity> findQuestionOptionListByQuestionID(long questionID) {
        List<QuestionOptionEntity> optionList = new ArrayList<QuestionOptionEntity>();

        for (QuestionOptionEntity optionEntity : examQuestion.getOptionList()) {
            if (optionEntity.getQuestionID() == questionID) {
                optionList.add(optionEntity);
            }
        }

        return optionList;
    }

    public ExamQuestionEntity getExamQuestion() {
        return this.examQuestion;
    }

    public void setExamQuestion(ExamQuestionEntity examquestion) {
        this.examQuestion = examquestion;
    }

    public List<ExamQuestionEntity> getExamQuestionList() {
        return this.examQuestionList;
    }

    public void setExamQuestionList(List<ExamQuestionEntity> examquestionList) {
        this.examQuestionList = examquestionList;
    }

    public List<SelectItem> getExamQuestionItemList() {
        return this.examQuestionItemList;
    }

    public void setExamQuestionItemList(List<SelectItem> examquestionItemList) {
        this.examQuestionItemList = examquestionItemList;
    }

    /**
     * @return the questionOption
     */
    public QuestionOptionEntity getQuestionOption() {
        return questionOption;
    }

    /**
     * @param questionOption the questionOption to set
     */
    public void setQuestionOption(QuestionOptionEntity questionOption) {
        this.questionOption = questionOption;
    }
}
