/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.setup.bean;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.setup.entities.AcademicCourseEntity;
import com.rsdynamix.academics.setup.entities.AcademicDepartmentEntity;
import com.rsdynamix.academics.setup.entities.AcademicResponsibilityEntity;
import com.rsdynamix.academics.setup.entities.AcademicRoleEntity;
import com.rsdynamix.academics.setup.entities.AcademicSessionEntity;
import com.rsdynamix.academics.setup.entities.FacultyEntity;
import com.rsdynamix.academics.setup.entities.StaffResponsibilityEntity;
import com.rsdynamix.academics.setup.entities.SubjectOfStudyEntity;
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
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "academicResponsibilityBean")
public class AcademicResponsibilityBean {

    private static final String ACADA_RESP_KEY = "acada_resp_id";
    //
    private static final String ACADA_RESP_DEFAULT = "1";
    //
    private static final String STAFF_RESP_KEY = "staff_resp_id";
    //
    private static final String STAFF_RESP_DEFAULT = "1";
    //
    private AcademicResponsibilityEntity responsibility;
    private List<AcademicResponsibilityEntity> responsibilityList;
    private List<SelectItem> responsibilityItemList;
    private List<SelectItem> roleItemList;
    //
    private StaffResponsibilityEntity staffResponsibility;
    private List<StaffResponsibilityEntity> staffResponsibilityList;

    public AcademicResponsibilityBean() {
        responsibility = new AcademicResponsibilityEntity();
        responsibilityList = new ArrayList<AcademicResponsibilityEntity>();
        responsibilityItemList = new ArrayList<SelectItem>();
        roleItemList = new ArrayList<SelectItem>();

        staffResponsibility = new StaffResponsibilityEntity();
        staffResponsibilityList = new ArrayList<StaffResponsibilityEntity>();

        loadAcademicResponsibility();
        loadStaffResponsibility();
    }

    public String loadAcademicResponsibility() {
        responsibilityList = new ArrayList<AcademicResponsibilityEntity>();
        roleItemList = new ArrayList<SelectItem>();
        responsibilityItemList = new ArrayList<SelectItem>();

        setResponsibilityItemList(new ArrayList<SelectItem>());
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        FacultyBean facultyBean = (FacultyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.facultyBean}", FacultyBean.class);
        if (facultyBean == null) {
            facultyBean = new FacultyBean();
            CommonBean.setBeanToContext("#{applicationScope.facultyBean}", FacultyBean.class, facultyBean);
        }

        AcademicDepartmentBean academicDepartmentBean = (AcademicDepartmentBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class);
        if (academicDepartmentBean == null) {
            academicDepartmentBean = new AcademicDepartmentBean();
            CommonBean.setBeanToContext("#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class, academicDepartmentBean);
        }

        AcademicCourseBean academicCourseBean = (AcademicCourseBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicCourseBean}", AcademicCourseBean.class);
        if (academicCourseBean == null) {
            academicCourseBean = new AcademicCourseBean();
            CommonBean.setBeanToContext("#{applicationScope.academicCourseBean}", AcademicCourseBean.class, academicCourseBean);
        }

        AcademicLevelBean academicLevelBean = (AcademicLevelBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicLevelBean}", AcademicLevelBean.class);
        if (academicLevelBean == null) {
            academicLevelBean = new AcademicLevelBean();
            CommonBean.setBeanToContext("#{applicationScope.academicLevelBean}", AcademicLevelBean.class, academicLevelBean);
        }

        SubjectOfStudyBean subjectOfStudyBean = (SubjectOfStudyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class);
        if (subjectOfStudyBean == null) {
            subjectOfStudyBean = new SubjectOfStudyBean();
            CommonBean.setBeanToContext("#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class, subjectOfStudyBean);
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
            AcademicRoleEntity roleCriteria = new AcademicRoleEntity();
            List<AbstractEntity> entityList = dataServer.findData(roleCriteria);
            for (AbstractEntity entity : entityList) {
                AcademicRoleEntity role = (AcademicRoleEntity) entity;

                SelectItem item = new SelectItem();
                item.setValue(role.getAcademicRoleID());
                item.setLabel(role.getAcademicRoleDesc());
                roleItemList.add(item);
            }

            AcademicResponsibilityEntity criteria = new AcademicResponsibilityEntity();
            entityList = dataServer.findData(criteria);
            for (AbstractEntity entity : entityList) {
                AcademicResponsibilityEntity resp = (AcademicResponsibilityEntity) entity;

                FacultyEntity faculty = facultyBean.findFacultyByID(resp.getFacultyID());
                if (faculty != null) {
                    resp.setFacultyName(faculty.getFacultyName());
                }

                AcademicDepartmentEntity dept = academicDepartmentBean.findDepartmentByID(resp.getDepartmentID());
                if (dept != null) {
                    resp.setDepartmentName(dept.getDepartmentName());
                }

                AcademicCourseEntity course = academicCourseBean.findAcademicCourseByID(resp.getCourseID());
                if (course != null) {
                    resp.setCourseTitle(course.getCourseTitle());
                }

                SelectItem levelItem = academicLevelBean.findAcademicLevelByID((int) resp.getLevelID());
                if (levelItem != null) {
                    resp.setLevelDesc(levelItem.getLabel());
                }

                SubjectOfStudyEntity subject = subjectOfStudyBean.findSubjectOfStudyByID(resp.getSubjectOfStudyID());
                if (subject != null) {
                    resp.setSubjectOfStudyName(subject.getSubjectTitle());
                }

                SelectItem roleItem = findAcademicRoleByID((int) resp.getAcademicRoleID());
                if (roleItem != null) {
                    resp.setAcademicRoleName(roleItem.getLabel());
                }

                responsibilityList.add(resp);
                addToSelectItemList(resp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void addToSelectItemList(AcademicResponsibilityEntity entity) {
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
                item.setValue(entity.getResponsibilityID());
                item.setLabel(entity.getResponsibilityDesc());
                responsibilityItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getResponsibilityID());
            item.setLabel(entity.getResponsibilityDesc());
            responsibilityItemList.add(item);
        }
    }

    public String addAcademicResponsibility() {
        FacultyBean facultyBean = (FacultyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.facultyBean}", FacultyBean.class);
        if (facultyBean == null) {
            facultyBean = new FacultyBean();
            CommonBean.setBeanToContext("#{applicationScope.facultyBean}", FacultyBean.class, facultyBean);
        }

        AcademicDepartmentBean academicDepartmentBean = (AcademicDepartmentBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class);
        if (academicDepartmentBean == null) {
            academicDepartmentBean = new AcademicDepartmentBean();
            CommonBean.setBeanToContext("#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class, academicDepartmentBean);
        }

        AcademicCourseBean academicCourseBean = (AcademicCourseBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicCourseBean}", AcademicCourseBean.class);
        if (academicCourseBean == null) {
            academicCourseBean = new AcademicCourseBean();
            CommonBean.setBeanToContext("#{applicationScope.academicCourseBean}", AcademicCourseBean.class, academicCourseBean);
        }

        AcademicLevelBean academicLevelBean = (AcademicLevelBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicLevelBean}", AcademicLevelBean.class);
        if (academicLevelBean == null) {
            academicLevelBean = new AcademicLevelBean();
            CommonBean.setBeanToContext("#{applicationScope.academicLevelBean}", AcademicLevelBean.class, academicLevelBean);
        }

        SubjectOfStudyBean subjectOfStudyBean = (SubjectOfStudyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class);
        if (subjectOfStudyBean == null) {
            subjectOfStudyBean = new SubjectOfStudyBean();
            CommonBean.setBeanToContext("#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class, subjectOfStudyBean);
        }

        FacultyEntity faculty = facultyBean.findFacultyByID(responsibility.getFacultyID());
        if (faculty != null) {
            responsibility.setFacultyName(faculty.getFacultyName());
        }

        AcademicDepartmentEntity dept = academicDepartmentBean.findDepartmentByID(responsibility.getDepartmentID());
        if (dept != null) {
            responsibility.setDepartmentName(dept.getDepartmentName());
        }

        AcademicCourseEntity course = academicCourseBean.findAcademicCourseByID(responsibility.getCourseID());
        if (course != null) {
            responsibility.setCourseTitle(course.getCourseTitle());
        }

        SelectItem levelItem = academicLevelBean.findAcademicLevelByID((int) responsibility.getLevelID());
        if (levelItem != null) {
            responsibility.setLevelDesc(levelItem.getLabel());
        }

        SubjectOfStudyEntity subject = subjectOfStudyBean.findSubjectOfStudyByID(responsibility.getSubjectOfStudyID());
        if (subject != null) {
            responsibility.setSubjectOfStudyName(subject.getSubjectTitle());
        }

        SelectItem roleItem = findAcademicRoleByID((int) responsibility.getAcademicRoleID());
        if (roleItem != null) {
            responsibility.setAcademicRoleName(roleItem.getLabel());
        }

        responsibilityList.add(responsibility);
        responsibility = new AcademicResponsibilityEntity();

        return "";
    }

    public String saveAcademicResponsibility() {
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
            for (AcademicResponsibilityEntity resp : responsibilityList) {
                if (resp.getResponsibilityID() == 0) {
                    int scoreID = Integer.parseInt(appPropBean.getValue(ACADA_RESP_KEY,
                            ACADA_RESP_DEFAULT, true));
                    resp.setResponsibilityID(scoreID);
                    dataServer.saveData(resp);
                } else if (resp.getResponsibilityID() > 0) {
                    dataServer.updateData(resp);
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

    public String activateAcademicResp() {
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

        if (responsibility.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((responsibility.getApprovalStatusID() == 0) || (responsibility.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(responsibility.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        responsibility.setApprovalStatusID(responsibility.getApprovalStatusID() + 1);
                        responsibility.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(responsibility);
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
                } else if (responsibility.getApprovalStatusID()
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

    public String loadAcademicRespList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        responsibility = new AcademicResponsibilityEntity();
        responsibilityList = new ArrayList<AcademicResponsibilityEntity>();
        responsibilityItemList = new ArrayList<SelectItem>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        AcademicResponsibilityEntity criteria = new AcademicResponsibilityEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            AcademicResponsibilityEntity responsibility = (AcademicResponsibilityEntity) entity;
            responsibilityList.add(responsibility);
        }

        return "";
    }

    public String loadAcademicRespPreviousHistoricalState() {
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
            if (responsibilityList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        responsibilityList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadAcademicRespList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadAcademicRespList(batEntity);
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

    public String loadAcademicRespNextHistoricalState() {
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
            if (responsibilityList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(responsibilityList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadAcademicRespList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadAcademicRespList(batEntity);
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

    public String gotoAcademicRespAuditTrailPage() {
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.CLASS_OF_DEGREE);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(responsibility.getResponsibilityID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadAcademicRespList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "acadarespaudittrail.jsf";
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

    public String deleteAcademicResp() {
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
            if (responsibilityList.contains(responsibility)) {
                if (responsibility.getResponsibilityID() > 0) {
                    AcademicResponsibilityEntity criteria = new AcademicResponsibilityEntity();
                    criteria.setResponsibilityID(responsibility.getResponsibilityID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                responsibilityList.remove(responsibility);
                responsibility = new AcademicResponsibilityEntity();
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

    public String loadStaffResponsibility() {
        staffResponsibilityList = new ArrayList<StaffResponsibilityEntity>();
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
            StaffResponsibilityEntity criteria = new StaffResponsibilityEntity();
            List<AbstractEntity> entityList = dataServer.findData(criteria);
            for (AbstractEntity entity : entityList) {
                StaffResponsibilityEntity staffResp = (StaffResponsibilityEntity) entity;
                staffResponsibilityList.add(staffResp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }
    
    public String addStaffResponsibility() {
        AcademicStaffBean academicStaffBean = (AcademicStaffBean) CommonBean.getBeanFromContext(
                "#{sessionScope.academicStaffBean}", AcademicStaffBean.class);
        if (academicStaffBean == null) {
            academicStaffBean = new AcademicStaffBean();
            CommonBean.setBeanToContext("#{sessionScope.academicStaffBean}", AcademicStaffBean.class, academicStaffBean);
        }

        AcademicSessionBean academicSessionBean = (AcademicSessionBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicSessionBean}", AcademicSessionBean.class);
        if (academicSessionBean == null) {
            academicSessionBean = new AcademicSessionBean();
            CommonBean.setBeanToContext("#{applicationScope.academicSessionBean}", AcademicSessionBean.class, academicSessionBean);
        }

        academicStaffBean.getStaff().setStaffNumber(staffResponsibility.getStaffNumber());
        academicStaffBean.searchForStaff();
        if ((academicStaffBean.getStaffList() != null) && (academicStaffBean.getStaffList().size() > 0)) {
            AcademicSessionEntity session = academicSessionBean.findSessionByID(staffResponsibility.getSessionID());
            if (session != null) {
                staffResponsibility.setSessionDesc(session.getSessionPeriod());
            }

            SelectItem item = findAcademicResponsibilityItemByID((int) staffResponsibility.getResponsibilityID());
            if (item != null) {
                staffResponsibility.setResponsibilityDesc(item.getLabel());
            }

            staffResponsibilityList.add(staffResponsibility);
            staffResponsibility = new StaffResponsibilityEntity();
        } else {
            //TODO Mssg...
        }

        return "";
    }

    public String saveStaffResponsibility() {
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
            for (StaffResponsibilityEntity resp : staffResponsibilityList) {
                if (resp.getStaffResponsibilityID() == 0) {
                    int scoreID = Integer.parseInt(appPropBean.getValue(STAFF_RESP_KEY,
                            STAFF_RESP_DEFAULT, true));
                    resp.setStaffResponsibilityID(scoreID);
                    dataServer.saveData(resp);
                } else if (resp.getStaffResponsibilityID() > 0) {
                    dataServer.updateData(resp);
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
    
    public String activateStaffResp() {
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

        if (staffResponsibility.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((staffResponsibility.getApprovalStatusID() == 0) || (staffResponsibility.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(staffResponsibility.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        staffResponsibility.setApprovalStatusID(staffResponsibility.getApprovalStatusID() + 1);
                        staffResponsibility.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(staffResponsibility);
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
                } else if (staffResponsibility.getApprovalStatusID()
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

    public String loadStaffRespList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        staffResponsibility = new StaffResponsibilityEntity();
        staffResponsibilityList = new ArrayList<StaffResponsibilityEntity>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        StaffResponsibilityEntity criteria = new StaffResponsibilityEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            StaffResponsibilityEntity staffResponsibility = (StaffResponsibilityEntity) entity;
            staffResponsibilityList.add(staffResponsibility);
        }

        return "";
    }

    public String loadStaffRespPreviousHistoricalState() {
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
            if (staffResponsibilityList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        staffResponsibilityList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadStaffRespList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadStaffRespList(batEntity);
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

    public String loadStaffRespNextHistoricalState() {
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
            if (staffResponsibilityList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(staffResponsibilityList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadStaffRespList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadStaffRespList(batEntity);
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

    public String gotoStaffRespAuditTrailPage() {
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.STAFF_RESPONSIBILITY);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(staffResponsibility.getStaffResponsibilityID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadStaffRespList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "staffrespaudittrail.jsf";
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

    public String deleteStaffResp() {
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
            if (staffResponsibilityList.contains(staffResponsibility)) {
                if (staffResponsibility.getStaffResponsibilityID() > 0) {
                    StaffResponsibilityEntity criteria = new StaffResponsibilityEntity();
                    criteria.setStaffResponsibilityID(staffResponsibility.getStaffResponsibilityID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                staffResponsibilityList.remove(staffResponsibility);
                staffResponsibility = new StaffResponsibilityEntity();
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

    public void facultyItemSelected(ValueChangeEvent vce) {
        AcademicDepartmentBean academicDepartmentBean = (AcademicDepartmentBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class);
        if (academicDepartmentBean == null) {
            academicDepartmentBean = new AcademicDepartmentBean();
            CommonBean.setBeanToContext("#{applicationScope.academicDepartmentBean}", 
                    AcademicDepartmentBean.class, academicDepartmentBean);
        }

        long facultyID = Long.parseLong(vce.getNewValue().toString());
        if (facultyID > 0) {
            academicDepartmentBean.facultyItemSelected(facultyID);
        }
    }

    public void departmentItemSelected(ValueChangeEvent vce) {
        long deptID = Long.parseLong(vce.getNewValue().toString());

        if (deptID > 0) {
            SubjectOfStudyBean subjectOfStudyBean = (SubjectOfStudyBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class);
            if (subjectOfStudyBean == null) {
                subjectOfStudyBean = new SubjectOfStudyBean();
                CommonBean.setBeanToContext("#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class, subjectOfStudyBean);
            }

            subjectOfStudyBean.departmentItemSelected(deptID);
        }
    }

    public void responsibilitySelected(ValueChangeEvent vce) {
    }

    public void responsibilityItemSelected(ValueChangeEvent vce) {
    }

    public void subjectItemSelected(ValueChangeEvent vce) {
    }

    public SelectItem findAcademicRoleByID(int roleID) {
        SelectItem item = null;

        for (SelectItem itm : roleItemList) {
            if (Integer.parseInt(itm.getValue().toString()) == roleID) {
                item = itm;
                break;
            }
        }

        return item;
    }

    public SelectItem findAcademicResponsibilityItemByID(int roleID) {
        SelectItem item = null;

        for (SelectItem itm : responsibilityItemList) {
            if (Integer.parseInt(itm.getValue().toString()) == roleID) {
                item = itm;
                break;
            }
        }

        return item;
    }

    public AcademicResponsibilityEntity findAcademicResponsibilityByID(long responsibilityID) {
        AcademicResponsibilityEntity resp = null;

        for (AcademicResponsibilityEntity acResp : responsibilityList) {
            if (acResp.getResponsibilityID() == responsibilityID) {
                resp = acResp;
                break;
            }
        }

        return resp;
    }

    public StaffResponsibilityEntity findStaffResponsibilityByStaffNo(String staffNo) {
        StaffResponsibilityEntity staffResp = null;

        for (StaffResponsibilityEntity resp : staffResponsibilityList) {
            if (resp.getStaffNumber().equals(staffNo)) {
                staffResp = resp;
                break;
            }
        }

        return staffResp;
    }

    /**
     * @return the responsibility
     */
    public AcademicResponsibilityEntity getResponsibility() {
        return responsibility;
    }

    /**
     * @param responsibility the responsibility to set
     */
    public void setResponsibility(AcademicResponsibilityEntity responsibility) {
        this.responsibility = responsibility;
    }

    /**
     * @return the responsibilityList
     */
    public List<AcademicResponsibilityEntity> getResponsibilityList() {
        return responsibilityList;
    }

    /**
     * @param responsibilityList the responsibilityList to set
     */
    public void setResponsibilityList(List<AcademicResponsibilityEntity> responsibilityList) {
        this.responsibilityList = responsibilityList;
    }

    /**
     * @return the staffResponsibilityList
     */
    public List<StaffResponsibilityEntity> getStaffResponsibilityList() {
        return staffResponsibilityList;
    }

    /**
     * @param staffResponsibilityList the staffResponsibilityList to set
     */
    public void setStaffResponsibilityList(List<StaffResponsibilityEntity> staffResponsibilityList) {
        this.staffResponsibilityList = staffResponsibilityList;
    }

    /**
     * @return the responsibilityItemList
     */
    public List<SelectItem> getResponsibilityItemList() {
        return responsibilityItemList;
    }

    /**
     * @param responsibilityItemList the responsibilityItemList to set
     */
    public void setResponsibilityItemList(List<SelectItem> responsibilityItemList) {
        this.responsibilityItemList = responsibilityItemList;
    }

    /**
     * @return the staffResponsibility
     */
    public StaffResponsibilityEntity getStaffResponsibility() {
        return staffResponsibility;
    }

    /**
     * @param staffResponsibility the staffResponsibility to set
     */
    public void setStaffResponsibility(StaffResponsibilityEntity staffResponsibility) {
        this.staffResponsibility = staffResponsibility;
    }

    /**
     * @return the roleItemList
     */
    public List<SelectItem> getRoleItemList() {
        return roleItemList;
    }

    /**
     * @param roleItemList the roleItemList to set
     */
    public void setRoleItemList(List<SelectItem> roleItemList) {
        this.roleItemList = roleItemList;
    }
}
