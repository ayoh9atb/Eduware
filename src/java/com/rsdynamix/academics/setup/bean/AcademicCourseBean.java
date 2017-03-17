package com.rsdynamix.academics.setup.bean;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.setup.entities.AcademicCourseEntity;
import com.rsdynamix.academics.setup.entities.SubjectOfStudyEntity;
import com.rsdynamix.academics.setup.entities.TermTier;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
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
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;

/**
 *
 * @author patushie@codrellica
 */
@SessionScoped
@ManagedBean(name = "academicCourseBean")
public class AcademicCourseBean {

    private static final String ACADEMIC_COURSE_ID_KEY = "acada_course_id";
    //
    private static final String ACADEMIC_COURSE_ID_DEFAULT = "1";
    //
    private static final String MAX_CREDIT_LOAD_ID_KEY = "mx_cred_load";
    //
    private static final String MAX_CREDIT_LOAD_DEFAULT = "30";
    //
    private AcademicCourseEntity academicCourse;
    private List<AcademicCourseEntity> academicCourseList;
    private List<SelectItem> academicCourseItemList;
    private List<SelectItem> selectedAcademicCourseItemList;
    private List<AcademicCourseEntity> selectedAcademicCourseList;
    //
    private int maximumAllowableCredit;

    public AcademicCourseBean() {
        academicCourse = new AcademicCourseEntity();
        academicCourseList = new ArrayList<AcademicCourseEntity>();
        academicCourseItemList = new ArrayList<SelectItem>();
        selectedAcademicCourseItemList = new ArrayList<SelectItem>();

        loadAcademicCourseList();
    }

    public void loadAcademicCourseList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        SubjectOfStudyBean subjectOfStudyBean = (SubjectOfStudyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class);
        if (subjectOfStudyBean == null) {
            subjectOfStudyBean = new SubjectOfStudyBean();
            CommonBean.setBeanToContext("#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class, subjectOfStudyBean);
        }

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

        try {
            maximumAllowableCredit = Integer.parseInt(appPropBean.getValue(MAX_CREDIT_LOAD_ID_KEY,
                    MAX_CREDIT_LOAD_DEFAULT, false));

            AcademicCourseEntity criteria = new AcademicCourseEntity();
            List<AbstractEntity> abstractFacultyList = dataServer.findData(criteria);
            for (AbstractEntity entity : abstractFacultyList) {
                AcademicCourseEntity tierEntity = (AcademicCourseEntity) entity;

                SubjectOfStudyEntity subject = subjectOfStudyBean.findSubjectOfStudyByID(tierEntity.getSubjectID());
                if (subject != null) {
                    tierEntity.setSubjectTitle(subject.getSubjectTitle());
                }

                SelectItem item = new SelectItem();
                item.setValue(tierEntity.getCourseID());
                item.setLabel(tierEntity.getCourseTitle());
                getAcademicCourseItemList().add(item);

                getAcademicCourseList().add(tierEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }
    
    public void addToSelectItemList(AcademicCourseEntity entity) {
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
                item.setValue(entity.getCourseID());
                item.setLabel(entity.getCourseTitle());
                academicCourseItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getCourseID());
            item.setLabel(entity.getCourseTitle());
            academicCourseItemList.add(item);
        }
    }

    public String addAcademicCourse() {
        if (!getAcademicCourseList().contains(getAcademicCourse())) {
            getAcademicCourseList().add(getAcademicCourse());
        } else {
            int index = getAcademicCourseList().indexOf(getAcademicCourse());
            getAcademicCourseList().set(index, getAcademicCourse());
        }
        setAcademicCourse(new AcademicCourseEntity());

        return "";
    }

    public void loadAcademicCourseList(int level, TermTier semesterTier) {
        selectedAcademicCourseItemList = new ArrayList<SelectItem>();

        selectedAcademicCourseList = findAcademicCourseByLevelAndSemester(level, semesterTier);
        for (AcademicCourseEntity entity : getSelectedAcademicCourseList()) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getCourseID());
            item.setLabel(entity.getCourseTitle());
            selectedAcademicCourseItemList.add(item);
        }
    }

    public List<AcademicCourseEntity> findAcademicCourseByLevelAndSemester(long level, TermTier semester) {
        List<AcademicCourseEntity> courseList = new ArrayList<AcademicCourseEntity>();

        for (AcademicCourseEntity courseEntity : getAcademicCourseList()) {
            if ((courseEntity.getLevelNumber() == level) && (courseEntity.getSemesterTier() == semester)) {
                courseList.add(courseEntity);
            }
        }

        return courseList;
    }

    public String saveAcademicCourseList() {
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
            for (AcademicCourseEntity course : getAcademicCourseList()) {
                if (course.getCourseID() == 0) {
                    try {
                        int cmID = Integer.parseInt(appPropBean.getValue(ACADEMIC_COURSE_ID_KEY,
                                ACADEMIC_COURSE_ID_DEFAULT, true));
                        course.setCourseID(cmID);
                        dataServer.saveData(course);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (course.getCourseID() > 0) {
                    dataServer.updateData(course);
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

        if (academicCourse.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((academicCourse.getApprovalStatusID() == 0) || (academicCourse.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(academicCourse.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        academicCourse.setApprovalStatusID(academicCourse.getApprovalStatusID() + 1);
                        academicCourse.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(academicCourse);
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
                } else if (academicCourse.getApprovalStatusID()
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

    public String loadAcademicCourseList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        academicCourse = new AcademicCourseEntity();
        academicCourseList = new ArrayList<AcademicCourseEntity>();
        academicCourseItemList = new ArrayList<SelectItem>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        AcademicCourseEntity criteria = new AcademicCourseEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            AcademicCourseEntity academicCourse = (AcademicCourseEntity) entity;
            academicCourseList.add(academicCourse);
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
            if (academicCourseList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        academicCourseList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadAcademicCourseList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadAcademicCourseList(batEntity);
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
            if (academicCourseList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(academicCourseList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadAcademicCourseList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadAcademicCourseList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.ACADEMIC_COURSE);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(academicCourse.getCourseID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadAcademicCourseList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "academiccourseaudittrail.jsf";
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

    public String deleteAcademicCourse() {
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
            if (academicCourseList.contains(academicCourse)) {
                if (academicCourse.getCourseID() > 0) {
                    AcademicCourseEntity criteria = new AcademicCourseEntity();
                    criteria.setCourseID(academicCourse.getCourseID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                academicCourseList.remove(academicCourse);
                academicCourse = new AcademicCourseEntity();
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

    public void academicCourseItemSelected(ValueChangeEvent vce) {
    }

    public void academicCourseSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
    }

    public void semesterTierItemSelected(ValueChangeEvent vce) {

    }

    public void subjectOfStudyItemSelected(ValueChangeEvent vce) {
        long subjectStudyID = Long.parseLong(vce.getNewValue().toString());

        if (subjectStudyID > 0) {
            SubjectOfStudyBean subjectOfStudyBean = (SubjectOfStudyBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class);
            if (subjectOfStudyBean == null) {
                subjectOfStudyBean = new SubjectOfStudyBean();
                CommonBean.setBeanToContext("#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class, subjectOfStudyBean);
            }

            SubjectOfStudyEntity subject = subjectOfStudyBean.findSubjectOfStudyByID(subjectStudyID);
            if (subject != null) {
                getAcademicCourse().setSubjectTitle(subject.getSubjectTitle());
            }
        }
    }

    public void subjectOfStudyItemSelected(long subjectID) {
        selectedAcademicCourseItemList = new ArrayList<SelectItem>();

        for (AcademicCourseEntity courseEntity : getAcademicCourseList()) {
            if (courseEntity.getSubjectID() == subjectID) {
                SelectItem item = new SelectItem();
                item.setValue(courseEntity.getCourseID());
                item.setLabel(courseEntity.getCourseTitle());

                selectedAcademicCourseItemList.add(item);
            }
        }
    }

    public AcademicCourseEntity findAcademicCourseByID(long courseID) {
        AcademicCourseEntity course = null;

        for (AcademicCourseEntity courseEntity : getAcademicCourseList()) {
            if (courseEntity.getCourseID() == courseID) {
                course = courseEntity;
                break;
            }
        }

        return course;
    }

    public AcademicCourseEntity getAcademicCourse() {
        return this.academicCourse;
    }

    public void setAcademicCourse(AcademicCourseEntity academiccourse) {
        this.academicCourse = academiccourse;
    }

    public List<AcademicCourseEntity> getAcademicCourseList() {
        return this.academicCourseList;
    }

    public void setAcademicCourseList(List<AcademicCourseEntity> academiccourseList) {
        this.academicCourseList = academiccourseList;
    }

    public List<SelectItem> getAcademicCourseItemList() {
        return this.academicCourseItemList;
    }

    public void setAcademicCourseItemList(List<SelectItem> academiccourseItemList) {
        this.academicCourseItemList = academiccourseItemList;
    }

    /**
     * @return the selectedAcademicCourseItemList
     */
    public List<SelectItem> getSelectedAcademicCourseItemList() {
        return selectedAcademicCourseItemList;
    }

    /**
     * @param selectedAcademicCourseItemList the selectedAcademicCourseItemList
     * to set
     */
    public void setSelectedAcademicCourseItemList(List<SelectItem> selectedAcademicCourseItemList) {
        this.selectedAcademicCourseItemList = selectedAcademicCourseItemList;
    }

    /**
     * @return the selectedAcademicCourseList
     */
    public List<AcademicCourseEntity> getSelectedAcademicCourseList() {
        return selectedAcademicCourseList;
    }

    /**
     * @param selectedAcademicCourseList the selectedAcademicCourseList to set
     */
    public void setSelectedAcademicCourseList(List<AcademicCourseEntity> selectedAcademicCourseList) {
        this.selectedAcademicCourseList = selectedAcademicCourseList;
    }

    /**
     * @return the maximumAllowableCredit
     */
    public int getMaximumAllowableCredit() {
        return maximumAllowableCredit;
    }

    /**
     * @param maximumAllowableCredit the maximumAllowableCredit to set
     */
    public void setMaximumAllowableCredit(int maximumAllowableCredit) {
        this.maximumAllowableCredit = maximumAllowableCredit;
    }
}
