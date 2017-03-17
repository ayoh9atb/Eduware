package com.rsdynamix.academics.exam.bean;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.exam.entities.ExamPaperEntity;
import com.rsdynamix.academics.setup.bean.AcademicCourseBean;
import com.rsdynamix.academics.setup.bean.SubjectOfStudyBean;
import com.rsdynamix.academics.setup.entities.AcademicCourseEntity;
import com.rsdynamix.academics.setup.entities.SubjectOfStudyEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.codrellica.projects.commons.DateUtil;
import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import java.sql.SQLException;
import java.util.ArrayList;
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
@ManagedBean(name = "examPaperBean")
public class ExamPaperBean {

    private static final String EXAM_PAPER_ID_KEY = "exam_paper_id";
    //
    private static final String EXAM_PAPER_ID_DEFAULT = "1";
    //
    private ExamPaperEntity examPaper;
    private List<ExamPaperEntity> examPaperList;
    private List<SelectItem> examPaperItemList;
    //
    private List<SelectItem> hourItemList;
    private List<SelectItem> minuteItemList;
    private List<SelectItem> meridiemItemList;
    private List<SelectItem> cityItemList;

    public ExamPaperBean() {
        examPaper = new ExamPaperEntity();
        examPaperList = new ArrayList<ExamPaperEntity>();
        examPaperItemList = new ArrayList<SelectItem>();

        hourItemList = new ArrayList<SelectItem>();
        minuteItemList = new ArrayList<SelectItem>();
        meridiemItemList = new ArrayList<SelectItem>();

        initializeTimeItemList();

        loadExamPaperList();
    }

    public void loadExamPaperList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
        ExamPaperEntity criteria = new ExamPaperEntity();

        SubjectOfStudyBean subjectOfStudyBean = (SubjectOfStudyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class);
        if (subjectOfStudyBean == null) {
            subjectOfStudyBean = new SubjectOfStudyBean();
            CommonBean.setBeanToContext("#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class, subjectOfStudyBean);
        }

        AcademicCourseBean academicCourseBean = (AcademicCourseBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicCourseBean}", AcademicCourseBean.class);
        if (academicCourseBean == null) {
            academicCourseBean = new AcademicCourseBean();
            CommonBean.setBeanToContext("#{applicationScope.academicCourseBean}", AcademicCourseBean.class, academicCourseBean);
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
            List<AbstractEntity> abstractFacultyList = dataServer.findData(criteria);
            for (AbstractEntity entity : abstractFacultyList) {
                ExamPaperEntity paperEntity = (ExamPaperEntity) entity;
                paperEntity.initializeTransientFields();

                SubjectOfStudyEntity subject = subjectOfStudyBean.findSubjectOfStudyByID(paperEntity.getSubjectID());
                if (subject != null) {
                    paperEntity.setSubjectTitle(subject.getSubjectTitle());
                }

                AcademicCourseEntity course = academicCourseBean.findAcademicCourseByID(paperEntity.getCourseID());
                if (course != null) {
                    paperEntity.setCourseTitle(course.getCourseTitle());
                }

                paperEntity.setPaperDateTO(DateUtil.toUtilDate(paperEntity.getPaperDate()));

                examPaperList.add(paperEntity);
                addToSelectItemList(paperEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public void addToSelectItemList(ExamPaperEntity entity) {
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
                item.setValue(entity.getExamPaperID());
                item.setLabel(entity.getExamPaperTitle());
                examPaperItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getExamPaperID());
            item.setLabel(entity.getExamPaperTitle());
            examPaperItemList.add(item);
        }
    }

    public String addExamPaper() {
        if (!getExamPaperList().contains(getExamPaper())) {
            getExamPaperList().add(getExamPaper());
        } else {
            int index = getExamPaperList().indexOf(getExamPaper());
            getExamPaperList().set(index, getExamPaper());
        }
        setExamPaper(new ExamPaperEntity());

        return "";
    }

    public String saveExamPaperList() {
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
            for (ExamPaperEntity paper : getExamPaperList()) {
                paper.setPaperDate(DateUtil.toSQLDate(paper.getPaperDateTO()));

                if (paper.getExamPaperID() == 0) {
                    try {
                        int cmID = Integer.parseInt(appPropBean.getValue(EXAM_PAPER_ID_KEY,
                                EXAM_PAPER_ID_DEFAULT, true));
                        paper.setExamPaperID(cmID);
                        dataServer.saveData(paper);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (paper.getExamPaperID() > 0) {
                    dataServer.updateData(paper);
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

        if (examPaper.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((examPaper.getApprovalStatusID() == 0) || (examPaper.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(examPaper.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        examPaper.setApprovalStatusID(examPaper.getApprovalStatusID() + 1);
                        examPaper.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(examPaper);
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
                } else if (examPaper.getApprovalStatusID()
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

    public String loadExamPaperList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        examPaper = new ExamPaperEntity();
        examPaperList = new ArrayList<ExamPaperEntity>();
        examPaperItemList = new ArrayList<SelectItem>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ExamPaperEntity criteria = new ExamPaperEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            ExamPaperEntity examPaper = (ExamPaperEntity) entity;
            examPaperList.add(examPaper);
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
            if (examPaperList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        examPaperList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadExamPaperList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadExamPaperList(batEntity);
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
            if (examPaperList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(examPaperList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadExamPaperList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadExamPaperList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.EXAM_PAPER);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(examPaper.getExamPaperID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadExamPaperList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "exampaperaudittrail.jsf";
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

    public String deleteExamPaper() {
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
            if (examPaperList.contains(examPaper)) {
                if (examPaper.getExamPaperID() > 0) {
                    ExamPaperEntity criteria = new ExamPaperEntity();
                    criteria.setExamPaperID(examPaper.getExamPaperID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                examPaperList.remove(examPaper);
                examPaper = new ExamPaperEntity();
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

    public void paperStartHourSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            if ((getExamPaper().getPaperStartTime().trim().length() == 0)
                    || (!getExamPaper().getPaperStartTime().contains(":"))) {
                getExamPaper().setPaperStartTime(vce.getNewValue().toString());
            } else {
                putServiceStartTimeItemToPosition(vce.getNewValue().toString(), 0);
            }
        }
    }

    public void paperStartMinuteSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            if (!getExamPaper().getPaperStartTime().contains(":")) {
                getExamPaper().setPaperStartTime(getExamPaper().getPaperStartTime() + ":" + vce.getNewValue().toString());
            } else {
                putServiceStartTimeItemToPosition(vce.getNewValue().toString(), 1);
            }
        }
    }

    public void paperStartMeridiemSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            if (getExamPaper().getPaperStartTime().split(":").length == 1) {
                getExamPaper().setPaperStartTime(getExamPaper().getPaperStartTime() + ":00:"
                        + vce.getNewValue().toString());
            } else if (getExamPaper().getPaperStartTime().split(":").length == 2) {
                getExamPaper().setPaperStartTime(getExamPaper().getPaperStartTime() + ":"
                        + vce.getNewValue().toString());
            } else if (getExamPaper().getPaperStartTime().split(":").length == 3) {
                putServiceStartTimeItemToPosition(vce.getNewValue().toString(), 2);
            }
        }
    }

    private void putServiceStartTimeItemToPosition(String timeElement, int pos) {
        String[] timePart = getExamPaper().getPaperStartTime().split(":");
        timePart[pos] = timeElement;
        getExamPaper().setPaperStartTime("");
        for (int i = 0; i <= timePart.length - 1; i++) {
            if (getExamPaper().getPaperStartTime().trim().length() > 0) {
                getExamPaper().setPaperStartTime(getExamPaper().getPaperStartTime() + ":"
                        + timePart[i]);
            } else if (getExamPaper().getPaperStartTime().trim().length() == 0) {
                getExamPaper().setPaperStartTime(timePart[i]);
            }
        }
    }

    private void putServiceStopTimeItemToPosition(String timeElement, int pos) {
        String[] timePart = getExamPaper().getPaperStartTime().split(":");
        timePart[pos] = timeElement;
        getExamPaper().setPaperStartTime("");
        for (int i = 0; i <= timePart.length - 1; i++) {
            if (getExamPaper().getPaperStartTime().trim().length() > 0) {
                getExamPaper().setPaperStartTime(getExamPaper().getPaperStartTime() + ":" + timePart[i]);
            } else if (getExamPaper().getPaperStartTime().trim().length() == 0) {
                getExamPaper().setPaperStartTime(timePart[i]);
            }
        }
    }

    private void initializeTimeItemList() {
        getExamPaper().setPaperStartTime("");
        setHourItemList(populateItemListIncrementally(getHourItemList(), 12, false));
        setMinuteItemList(populateItemListIncrementally(getMinuteItemList(), 59, true));

        SelectItem item = new SelectItem();
        item.setValue("AM");
        item.setLabel("AM");
        getMeridiemItemList().add(item);

        item = new SelectItem();
        item.setValue("PM");
        item.setLabel("PM");
        getMeridiemItemList().add(item);
    }

    private List<SelectItem> populateItemListIncrementally(
            List<SelectItem> itemList, int numberOfItems, boolean beginFromZero) {
        int origin = 0;
        if (!beginFromZero) {
            origin = 1;
        }

        for (int index = origin; index <= numberOfItems; index++) {
            SelectItem item = new SelectItem();
            if (String.valueOf(index).length() == 1) {
                item.setValue("0" + String.valueOf(index));
                item.setLabel("0" + String.valueOf(index));
            } else {
                item.setValue(String.valueOf(index));
                item.setLabel(String.valueOf(index));
            }
            itemList.add(item);
        }

        return itemList;
    }

    public ExamPaperEntity findExamPaperByID(long paperID) {
        ExamPaperEntity paper = null;

        for (ExamPaperEntity paperEntity : getExamPaperList()) {
            if (paperEntity.getExamPaperID() == paperID) {
                paper = paperEntity;
                break;
            }
        }

        return paper;
    }

    public void examPaperItemSelected(ValueChangeEvent vce) {
    }

    public void exampaperSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
    }

    public void subjectItemSelected(ValueChangeEvent vce) {
        long subjectID = Long.parseLong(vce.getNewValue().toString());

        SubjectOfStudyBean subjectOfStudyBean = (SubjectOfStudyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class);
        if (subjectOfStudyBean == null) {
            subjectOfStudyBean = new SubjectOfStudyBean();
            CommonBean.setBeanToContext("#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class, subjectOfStudyBean);
        }

        SubjectOfStudyEntity subject = subjectOfStudyBean.findSubjectOfStudyByID(subjectID);
        if (subject != null) {
            getExamPaper().setSubjectTitle(subject.getSubjectTitle());

            AcademicCourseBean academicCourseBean = (AcademicCourseBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.academicCourseBean}", AcademicCourseBean.class);
            if (academicCourseBean == null) {
                academicCourseBean = new AcademicCourseBean();
                CommonBean.setBeanToContext("#{applicationScope.academicCourseBean}", AcademicCourseBean.class, academicCourseBean);
            }
            academicCourseBean.subjectOfStudyItemSelected(subject.getSubjectID());
        }
    }

    public void courseItemSelected(ValueChangeEvent vce) {
        long courseID = Long.parseLong(vce.getNewValue().toString());

        AcademicCourseBean academicCourseBean = (AcademicCourseBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicCourseBean}", AcademicCourseBean.class);
        if (academicCourseBean == null) {
            academicCourseBean = new AcademicCourseBean();
            CommonBean.setBeanToContext("#{applicationScope.academicCourseBean}", AcademicCourseBean.class, academicCourseBean);
        }

        AcademicCourseEntity course = academicCourseBean.findAcademicCourseByID(courseID);
        if (course != null) {
            getExamPaper().setCourseTitle(course.getCourseTitle());
        }
    }

    public ExamPaperEntity getExamPaper() {
        return this.examPaper;
    }

    public void setExamPaper(ExamPaperEntity exampaper) {
        this.examPaper = exampaper;
    }

    public List<ExamPaperEntity> getExamPaperList() {
        return this.examPaperList;
    }

    public void setExamPaperList(List<ExamPaperEntity> exampaperList) {
        this.examPaperList = exampaperList;
    }

    public List<SelectItem> getExamPaperItemList() {
        return this.examPaperItemList;
    }

    public void setExamPaperItemList(List<SelectItem> exampaperItemList) {
        this.examPaperItemList = exampaperItemList;
    }

    /**
     * @return the hourItemList
     */
    public List<SelectItem> getHourItemList() {
        return hourItemList;
    }

    /**
     * @param hourItemList the hourItemList to set
     */
    public void setHourItemList(List<SelectItem> hourItemList) {
        this.hourItemList = hourItemList;
    }

    /**
     * @return the minuteItemList
     */
    public List<SelectItem> getMinuteItemList() {
        return minuteItemList;
    }

    /**
     * @param minuteItemList the minuteItemList to set
     */
    public void setMinuteItemList(List<SelectItem> minuteItemList) {
        this.minuteItemList = minuteItemList;
    }

    /**
     * @return the meridiemItemList
     */
    public List<SelectItem> getMeridiemItemList() {
        return meridiemItemList;
    }

    /**
     * @param meridiemItemList the meridiemItemList to set
     */
    public void setMeridiemItemList(List<SelectItem> meridiemItemList) {
        this.meridiemItemList = meridiemItemList;
    }

    /**
     * @return the cityItemList
     */
    public List<SelectItem> getCityItemList() {
        return cityItemList;
    }

    /**
     * @param cityItemList the cityItemList to set
     */
    public void setCityItemList(List<SelectItem> cityItemList) {
        this.cityItemList = cityItemList;
    }
}
