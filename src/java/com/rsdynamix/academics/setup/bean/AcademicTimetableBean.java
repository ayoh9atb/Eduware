/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.setup.bean;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.setup.entities.AcademicCourseEntity;
import com.rsdynamix.academics.setup.entities.AcademicDepartmentEntity;
import com.rsdynamix.academics.setup.entities.AcademicSessionEntity;
import com.rsdynamix.academics.setup.entities.AcademicTimetableEntity;
import com.rsdynamix.academics.setup.entities.FacultyEntity;
import com.rsdynamix.academics.setup.entities.LectureVenueEntity;
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
@ManagedBean(name = "academicTimetableBean")
public class AcademicTimetableBean {

    private static final String ACADA_TIMETABLE_ID_KEY = "acada_X_table";
    //
    private static final String ACADA_TIMETABLE_DEFAULT = "1";
    //
    private AcademicTimetableEntity academicTimetable;
    private List<AcademicTimetableEntity> academicTimetableList;
    private List<SelectItem> hourItemList;
    private List<SelectItem> minuteItemList;
    private List<SelectItem> meridiemItemList;
    private List<SelectItem> lectureVenueItemList;

    public AcademicTimetableBean() {
        academicTimetable = new AcademicTimetableEntity();
        academicTimetableList = new ArrayList<AcademicTimetableEntity>();

        hourItemList = new ArrayList<SelectItem>();
        minuteItemList = new ArrayList<SelectItem>();
        meridiemItemList = new ArrayList<SelectItem>();
        lectureVenueItemList = new ArrayList<SelectItem>();

        initializeTimeItemList();
        loadLectureVenueList();
        loadTimetableEntryList();
    }

    public String loadTimetableEntryList() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        academicTimetableList = new ArrayList<AcademicTimetableEntity>();

        AcademicTimetableEntity criteria = new AcademicTimetableEntity();
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        try {
            List<AbstractEntity> entityList = dataServer.findData(criteria);
            for (AbstractEntity entity : entityList) {
                AcademicTimetableEntity timeEntity = (AcademicTimetableEntity) entity;

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

                AcademicSessionBean academicSessionBean = (AcademicSessionBean) CommonBean.getBeanFromContext(
                        "#{applicationScope.academicSessionBean}", AcademicSessionBean.class);
                if (academicSessionBean == null) {
                    academicSessionBean = new AcademicSessionBean();
                    CommonBean.setBeanToContext("#{applicationScope.academicSessionBean}", AcademicSessionBean.class, academicSessionBean);
                }

                AcademicCourseBean academicCourseBean = (AcademicCourseBean) CommonBean.getBeanFromContext(
                        "#{applicationScope.academicCourseBean}", AcademicCourseBean.class);
                if (academicCourseBean == null) {
                    academicCourseBean = new AcademicCourseBean();
                    CommonBean.setBeanToContext("#{applicationScope.academicCourseBean}", AcademicCourseBean.class, academicCourseBean);
                }

                FacultyEntity faculty = facultyBean.findFacultyByID(timeEntity.getFacultyID());
                if (faculty != null) {
                    timeEntity.setFacultyName(faculty.getFacultyName());
                }

                AcademicDepartmentEntity department = academicDepartmentBean.findDepartmentByID(timeEntity.getDepartmentID());
                if (department != null) {
                    timeEntity.setDepartmentName(department.getDepartmentName());
                }

                AcademicSessionEntity session = academicSessionBean.findSessionByID(timeEntity.getSessionID());
                if (session != null) {
                    timeEntity.setSessionPeriod(session.getSessionPeriod());
                }

                AcademicCourseEntity course = academicCourseBean.findAcademicCourseByID(timeEntity.getCourseID());
                if (course != null) {
                    timeEntity.setCourseCode(course.getCourseCode());
                    timeEntity.setCourseTitle(course.getCourseTitle());
                }

                SelectItem item = findLectureVenueItemByID(timeEntity.getLectureVenueID());
                if (item != null) {
                    timeEntity.setLectureVenueDesc(item.getLabel());
                }

                academicTimetableList.add(timeEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String addTimetableEntry() {
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

        AcademicSessionBean academicSessionBean = (AcademicSessionBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicSessionBean}", AcademicSessionBean.class);
        if (academicSessionBean == null) {
            academicSessionBean = new AcademicSessionBean();
            CommonBean.setBeanToContext("#{applicationScope.academicSessionBean}", AcademicSessionBean.class, academicSessionBean);
        }

        AcademicCourseBean academicCourseBean = (AcademicCourseBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicCourseBean}", AcademicCourseBean.class);
        if (academicCourseBean == null) {
            academicCourseBean = new AcademicCourseBean();
            CommonBean.setBeanToContext("#{applicationScope.academicCourseBean}", AcademicCourseBean.class, academicCourseBean);
        }

        FacultyEntity faculty = facultyBean.findFacultyByID(academicTimetable.getFacultyID());
        if (faculty != null) {
            academicTimetable.setFacultyName(faculty.getFacultyName());
        }

        AcademicDepartmentEntity department = academicDepartmentBean.findDepartmentByID(academicTimetable.getDepartmentID());
        if (department != null) {
            academicTimetable.setDepartmentName(department.getDepartmentName());
        }

        AcademicSessionEntity session = academicSessionBean.findSessionByID(academicTimetable.getSessionID());
        if (session != null) {
            academicTimetable.setSessionPeriod(session.getSessionPeriod());
        }

        AcademicCourseEntity course = academicCourseBean.findAcademicCourseByID(academicTimetable.getCourseID());
        if (course != null) {
            academicTimetable.setCourseCode(course.getCourseCode());
            academicTimetable.setCourseTitle(course.getCourseTitle());
        }

        SelectItem item = findLectureVenueItemByID(academicTimetable.getLectureVenueID());
        if (item != null) {
            academicTimetable.setLectureVenueDesc(item.getLabel());
        }

        if (!academicTimetableList.contains(academicTimetable)) {
            academicTimetableList.add(academicTimetable);
        } else {
            int index = academicTimetableList.indexOf(academicTimetable);
            academicTimetableList.set(index, academicTimetable);
        }

        academicTimetable = new AcademicTimetableEntity();

        return "";
    }

    public String saveTimetableEntryList() {
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

        try {
            for (AcademicTimetableEntity xTable : academicTimetableList) {
                if (xTable.getTimetableID() == 0) {
                    try {
                        long serial = Long.parseLong(appPropBean.getValue(ACADA_TIMETABLE_ID_KEY,
                                ACADA_TIMETABLE_DEFAULT, true));
                        xTable.setTimetableID(serial);

                        dataServer.saveData(xTable);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    dataServer.updateData(xTable);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
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

        if (academicTimetable.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((academicTimetable.getApprovalStatusID() == 0) || (academicTimetable.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(academicTimetable.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        academicTimetable.setApprovalStatusID(academicTimetable.getApprovalStatusID() + 1);
                        academicTimetable.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(academicTimetable);
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
                } else if (academicTimetable.getApprovalStatusID()
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

    public String loadTimeTableList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        academicTimetable = new AcademicTimetableEntity();
        academicTimetableList = new ArrayList<AcademicTimetableEntity>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        AcademicTimetableEntity criteria = new AcademicTimetableEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            AcademicTimetableEntity academicTimetable = (AcademicTimetableEntity) entity;
            academicTimetableList.add(academicTimetable);
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
            if (academicTimetableList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        academicTimetableList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadTimeTableList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadTimeTableList(batEntity);
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
            if (academicTimetableList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(academicTimetableList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadTimeTableList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadTimeTableList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.ACADEMIC_TIMETABLE);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(academicTimetable.getTimetableID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadTimeTableList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "timetableaudittrail.jsf";
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

    public String deleteAcademicTimeTable() {
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
            if (academicTimetableList.contains(academicTimetable)) {
                if (academicTimetable.getTimetableID() > 0) {
                    AcademicTimetableEntity criteria = new AcademicTimetableEntity();
                    criteria.setTimetableID(academicTimetable.getTimetableID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                academicTimetableList.remove(academicTimetable);
                academicTimetable = new AcademicTimetableEntity();
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

    public void loadLectureVenueList() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        LectureVenueEntity criteria = new LectureVenueEntity();
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        try {
            List<AbstractEntity> entityList = dataServer.findData(criteria);
            for (AbstractEntity entity : entityList) {
                LectureVenueEntity lvEntity = (LectureVenueEntity) entity;

                SelectItem item = new SelectItem();
                item.setValue(lvEntity.getLectureVenuID());
                item.setLabel(lvEntity.getLectureVenueDesc());

                lectureVenueItemList.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public SelectItem findLectureVenueItemByID(long venueID) {
        SelectItem item = null;

        for (SelectItem sItem : lectureVenueItemList) {
            if (Integer.parseInt(sItem.getValue().toString()) == venueID) {
                item = sItem;
                break;
            }
        }

        return item;
    }

    public void facultyItemSelected(ValueChangeEvent vce) {
        long facultyID = Long.parseLong(vce.getNewValue().toString());

        AcademicDepartmentBean academicDepartmentBean = (AcademicDepartmentBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class);
        if (academicDepartmentBean == null) {
            academicDepartmentBean = new AcademicDepartmentBean();
            CommonBean.setBeanToContext("#{applicationScope.academicDepartmentBean}",
                    AcademicDepartmentBean.class, academicDepartmentBean);
        }

        academicDepartmentBean.facultyItemSelected(facultyID);
    }

    public void departmentItemSelected(ValueChangeEvent vce) {
        long deptID = Long.parseLong(vce.getNewValue().toString());

        SubjectOfStudyBean subjectOfStudyBean = (SubjectOfStudyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class);
        if (subjectOfStudyBean == null) {
            subjectOfStudyBean = new SubjectOfStudyBean();
            CommonBean.setBeanToContext("#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class, subjectOfStudyBean);
        }

        subjectOfStudyBean.departmentItemSelected(deptID);
    }

    public void lectureStartHourSelected(ValueChangeEvent vce) {
        if (!vce.getNewValue().toString().equals("-1")) {
            if ((academicTimetable.getStartTime().trim().length() == 0)
                    || (!academicTimetable.getStartTime().contains(":"))) {
                academicTimetable.setStartTime(vce.getNewValue().toString());
            } else {
                putServiceStartTimeItemToPosition(vce.getNewValue().toString(), 0);
            }
        }
    }

    public void lectureStopHourSelected(ValueChangeEvent vce) {
        if (!vce.getNewValue().toString().equals("-1")) {
            if ((academicTimetable.getEndTime().trim().length() == 0)
                    || (!academicTimetable.getEndTime().contains(":"))) {
                academicTimetable.setEndTime(vce.getNewValue().toString());
            } else {
                putServiceStopTimeItemToPosition(vce.getNewValue().toString(), 0);
            }
        }
    }

    public void lectureStartMinuteSelected(ValueChangeEvent vce) {
        if (!vce.getNewValue().toString().equals("-1")) {
            if (!academicTimetable.getStartTime().contains(":")) {
                academicTimetable.setStartTime(academicTimetable.getStartTime() + ":" + vce.getNewValue().toString());
            } else {
                putServiceStartTimeItemToPosition(vce.getNewValue().toString(), 1);
            }
        }
    }

    public void lectureStopMinuteSelected(ValueChangeEvent vce) {
        if (!vce.getNewValue().toString().equals("-1")) {
            if (!academicTimetable.getEndTime().contains(":")) {
                academicTimetable.setEndTime(academicTimetable.getEndTime() + ":"
                        + vce.getNewValue().toString());
            } else {
                putServiceStopTimeItemToPosition(vce.getNewValue().toString(), 1);
            }
        }
    }

    private void putServiceStartTimeItemToPosition(String timeElement, int pos) {
        String[] timePart = academicTimetable.getStartTime().split(":");
        timePart[pos] = timeElement;
        academicTimetable.setStartTime("");
        for (int i = 0; i <= timePart.length - 1; i++) {
            if (academicTimetable.getStartTime().trim().length() > 0) {
                academicTimetable.setStartTime(academicTimetable.getStartTime() + ":"
                        + timePart[i]);
            } else if (academicTimetable.getStartTime().trim().length() == 0) {
                academicTimetable.setStartTime(timePart[i]);
            }
        }
    }

    private void putServiceStopTimeItemToPosition(String timeElement, int pos) {
        String[] timePart = academicTimetable.getEndTime().split(":");
        timePart[pos] = timeElement;
        academicTimetable.setEndTime("");
        for (int i = 0; i <= timePart.length - 1; i++) {
            if (academicTimetable.getEndTime().trim().length() > 0) {
                academicTimetable.setEndTime(academicTimetable.getEndTime() + ":" + timePart[i]);
            } else if (academicTimetable.getEndTime().trim().length() == 0) {
                academicTimetable.setEndTime(timePart[i]);
            }
        }
    }

    public void lectureStartMeridiemSelected(ValueChangeEvent vce) {
        if (!vce.getNewValue().toString().equals("-1")) {
            if (academicTimetable.getStartTime().split(":").length == 1) {
                academicTimetable.setStartTime(academicTimetable.getStartTime() + ":00:"
                        + vce.getNewValue().toString());
            } else if (academicTimetable.getStartTime().split(":").length == 2) {
                academicTimetable.setStartTime(academicTimetable.getStartTime() + ":"
                        + vce.getNewValue().toString());
            } else if (academicTimetable.getStartTime().split(":").length == 3) {
                putServiceStartTimeItemToPosition(vce.getNewValue().toString(), 2);
            }
        }
    }

    public void lectureStopMeridiemSelected(ValueChangeEvent vce) {
        if (!vce.getNewValue().toString().equals("-1")) {
            if (academicTimetable.getEndTime().split(":").length == 1) {
                academicTimetable.setEndTime(academicTimetable.getEndTime() + ":00:"
                        + vce.getNewValue().toString());
            } else if (academicTimetable.getEndTime().split(":").length == 2) {
                academicTimetable.setEndTime(academicTimetable.getEndTime() + ":"
                        + vce.getNewValue().toString());
            } else if (academicTimetable.getEndTime().split(":").length == 3) {
                putServiceStopTimeItemToPosition(vce.getNewValue().toString(), 2);
            }
        }
    }

    private void initializeTimeItemList() {
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

    /**
     * @return the academicTimetable
     */
    public AcademicTimetableEntity getAcademicTimetable() {
        return academicTimetable;
    }

    /**
     * @param academicTimetable the academicTimetable to set
     */
    public void setAcademicTimetable(AcademicTimetableEntity academicTimetable) {
        this.academicTimetable = academicTimetable;
    }

    /**
     * @return the academicTimetableList
     */
    public List<AcademicTimetableEntity> getAcademicTimetableList() {
        return academicTimetableList;
    }

    /**
     * @param academicTimetableList the academicTimetableList to set
     */
    public void setAcademicTimetableList(List<AcademicTimetableEntity> academicTimetableList) {
        this.academicTimetableList = academicTimetableList;
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
     * @return the lectureVenueItemList
     */
    public List<SelectItem> getLectureVenueItemList() {
        return lectureVenueItemList;
    }

    /**
     * @param lectureVenueItemList the lectureVenueItemList to set
     */
    public void setLectureVenueItemList(List<SelectItem> lectureVenueItemList) {
        this.lectureVenueItemList = lectureVenueItemList;
    }

}
