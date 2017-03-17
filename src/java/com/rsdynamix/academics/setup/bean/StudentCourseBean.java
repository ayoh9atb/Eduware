package com.rsdynamix.academics.setup.bean;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.resultprocess.pojos.SemesterCourseBouquet;
import com.rsdynamix.academics.resultprocess.pojos.SessionCourseBouquet;
import com.rsdynamix.academics.resultprocess.pojos.SessionGroup;
import com.rsdynamix.academics.setup.entities.AcademicCourseEntity;
import com.rsdynamix.academics.setup.entities.AcademicSemesterEntity;
import com.rsdynamix.academics.setup.entities.AcademicSessionEntity;
import com.rsdynamix.academics.setup.entities.StudentCourseEntity;
import com.rsdynamix.academics.setup.entities.TermTier;
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
@ManagedBean(name = "studentCourseBean")
public class StudentCourseBean {

    private static final String STUD_COURSE_ID_KEY = "stud_course_id";
    //
    private static final String STUD_COURSE_ID_DEFAULT = "1";
    //
    private StudentCourseEntity studentCourse;
    private List<StudentCourseEntity> studentCourseList;
    private List<SessionCourseBouquet> sessionCourseList;
    private List<StudentCourseEntity> studentCourseSelectList;
    private List<String> studentCourseTitleList;
    private int totalCreditLoad;
    private List<String> courseIDList;
    //
    private String studentNumber;

    public StudentCourseBean() {
        studentCourse = new StudentCourseEntity();
        studentCourseList = new ArrayList<StudentCourseEntity>();
        sessionCourseList = new ArrayList<SessionCourseBouquet>();
        studentCourseSelectList = new ArrayList<StudentCourseEntity>();
        studentCourseTitleList = new ArrayList<String>();
        totalCreditLoad = 0;
        courseIDList = new ArrayList<String>();
    }

    public String loadStudentCoursesByStudNumber() {
        try {
            StudentCourseBean.this.loadStudentCourseList(studentNumber);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public String loadSemesterCourseList() {
        StudentBean studentBean = (StudentBean) CommonBean.getBeanFromContext(
                "#{sessionScope.studentBean}", StudentBean.class);
        if (studentBean == null) {
            studentBean = new StudentBean();
            CommonBean.setBeanToContext("#{sessionScope.studentBean}", StudentBean.class, studentBean);
        }

        AcademicSemesterBean academicSemesterBean = (AcademicSemesterBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicSemesterBean}", AcademicSemesterBean.class);
        if (academicSemesterBean == null) {
            academicSemesterBean = new AcademicSemesterBean();
            CommonBean.setBeanToContext("#{applicationScope.academicSemesterBean}", AcademicSemesterBean.class, academicSemesterBean);
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

        TermTier semesterTier = null;
        AcademicSemesterEntity currentSemester = academicSemesterBean.getCurrentSemester();

        if (currentSemester != null) {
            String semester = currentSemester.getSemesterTier();
            if (semester.equals("FIRST")) {
                semesterTier = TermTier.FIRST;
            } else if (semester.equals("SECOND")) {
                semesterTier = TermTier.SECOND;
            }
            academicCourseBean.loadAcademicCourseList(studentBean.getStudent().getYearOfStudy(), semesterTier);
        } else {
            applicationMessageBean.insertMessage("", MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void loadStudentCourseList(String studentNumber) throws Exception {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        AcademicCourseBean academicCourseBean = (AcademicCourseBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicCourseBean}", AcademicCourseBean.class);
        if (academicCourseBean == null) {
            academicCourseBean = new AcademicCourseBean();
            CommonBean.setBeanToContext("#{applicationScope.academicCourseBean}", AcademicCourseBean.class, academicCourseBean);
        }

        AcademicSessionBean academicSessionBean = (AcademicSessionBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicSessionBean}", AcademicSessionBean.class);
        if (academicSessionBean == null) {
            academicSessionBean = new AcademicSessionBean();
            CommonBean.setBeanToContext("#{applicationScope.academicSessionBean}", AcademicSessionBean.class, academicSessionBean);
        }

        StudentCourseEntity criteria = new StudentCourseEntity();
        criteria.setStudentNumber(studentNumber);

        List<AbstractEntity> abstractCourseList = dataServer.findData(criteria);
        for (AbstractEntity entity : abstractCourseList) {
            StudentCourseEntity tierEntity = (StudentCourseEntity) entity;
            AcademicCourseEntity acadaCourse = academicCourseBean.findAcademicCourseByID(tierEntity.getCourseID());
            if (acadaCourse != null) {
                tierEntity.setCourseCode(acadaCourse.getCourseCode());
                tierEntity.setCourseTitle(acadaCourse.getCourseTitle());
            }

            AcademicSessionEntity session = academicSessionBean.findSessionByID(tierEntity.getSessionID());
            if (session != null) {
                tierEntity.setSessionPeriod(session.getSessionPeriod());
            }

            getStudentCourseList().add(tierEntity);
        }

        sessionCourseList = buildSessionCourse(getStudentCourseList());
    }

    public List<SessionCourseBouquet> buildSessionCourse(List<StudentCourseEntity> courseList) {
        List<SessionCourseBouquet> sessionCourseBQList = new ArrayList<SessionCourseBouquet>();

        List<SessionGroup> courseBySessionList = buildCourseBySessionList(courseList);
        for (SessionGroup courseBySession : courseBySessionList) {
            List<StudentCourseEntity> filteredCourseList = findSessionCourseList(
                    courseList, courseBySession.getSessionID());
            SessionCourseBouquet sessionCourseBQ = new SessionCourseBouquet();
            SemesterCourseBouquet semesterCourseBQ1 = new SemesterCourseBouquet();
            SemesterCourseBouquet semesterCourseBQ2 = new SemesterCourseBouquet();
            for (StudentCourseEntity filteredCourse : filteredCourseList) {
                if (filteredCourse.getSemesterTier() == TermTier.FIRST) {
                    semesterCourseBQ1.setCreditLoad(semesterCourseBQ1.getCreditLoad() + filteredCourse.getCreditLoad());

                    semesterCourseBQ1.setSemesterTier(filteredCourse.getSemesterTier().toString());
                    semesterCourseBQ1.getStudentCourseList().add(filteredCourse);
                } else if (filteredCourse.getSemesterTier() == TermTier.SECOND) {
                    semesterCourseBQ2.setCreditLoad(semesterCourseBQ2.getCreditLoad() + filteredCourse.getCreditLoad());

                    semesterCourseBQ2.setSemesterTier(filteredCourse.getSemesterTier().toString());
                    semesterCourseBQ2.getStudentCourseList().add(filteredCourse);
                }
            }
            sessionCourseBQ.setCreditLoad(sessionCourseBQ.getCreditLoad() + semesterCourseBQ1.getCreditLoad());
            sessionCourseBQ.setCreditLoad(sessionCourseBQ.getCreditLoad() + semesterCourseBQ2.getCreditLoad());

            sessionCourseBQ.setSessionID(courseBySession.getSessionID());
            sessionCourseBQ.setSessionPeriod(courseBySession.getSessionPeriod());

            sessionCourseBQ.getSemesterCourseList().add(semesterCourseBQ1);
            sessionCourseBQ.getSemesterCourseList().add(semesterCourseBQ2);

            if (!sessionCourseBQList.contains(sessionCourseBQ)) {
                sessionCourseBQList.add(sessionCourseBQ);
            }
        }

        return sessionCourseBQList;
    }

    public List<SessionGroup> buildCourseBySessionList(List<StudentCourseEntity> courseList) {
        List<SessionGroup> courseSessionList = new ArrayList<SessionGroup>();

        for (StudentCourseEntity course : courseList) {
            SessionGroup courseSession = new SessionGroup();
            courseSession.setSessionID(course.getSessionID());
            courseSession.setSessionPeriod(course.getSessionPeriod());

            if (!courseSessionList.contains(courseSession)) {
                courseSessionList.add(courseSession);
            }
        }

        return courseSessionList;
    }

    public List<StudentCourseEntity> findSessionCourseList(List<StudentCourseEntity> courseList, long sessionID) {
        List<StudentCourseEntity> filteredCourseList = new ArrayList<StudentCourseEntity>();

        for (StudentCourseEntity course : courseList) {
            if (course.getSessionID() == sessionID) {
                filteredCourseList.add(course);
            }
        }

        return filteredCourseList;
    }

    public List<StudentCourseEntity> findStudentCourseList(long courseID, long sessionID) throws Exception {
        List<StudentCourseEntity> courseList = new ArrayList<StudentCourseEntity>();

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
        StudentCourseEntity criteria = new StudentCourseEntity();
        criteria.setCourseID(courseID);
        criteria.setSessionID(sessionID);

        List<AbstractEntity> abstractCourseList = dataServer.findData(criteria);
        for (AbstractEntity entity : abstractCourseList) {
            StudentCourseEntity tierEntity = (StudentCourseEntity) entity;
            courseList.add(tierEntity);
        }

        return courseList;
    }

    public String addStudentCourse() {
        if (!getStudentCourseList().contains(getStudentCourse())) {
            getStudentCourseList().add(getStudentCourse());
        } else {
            int index = getStudentCourseList().indexOf(getStudentCourse());
            getStudentCourseList().set(index, getStudentCourse());
        }
        setStudentCourse(new StudentCourseEntity());
        return "";
    }

    public String saveStudentCourseList() {
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
            for (StudentCourseEntity course : getStudentCourseList()) {
                if (course.getCourseID() == 0) {
                    try {
                        int cmID = Integer.parseInt(appPropBean.getValue(STUD_COURSE_ID_KEY,
                                STUD_COURSE_ID_DEFAULT, true));
                        course.setStudentCourseID(cmID);
                        dataServer.saveData(course);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (course.getStudentCourseID() > 0) {
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

        if (studentCourse.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((studentCourse.getApprovalStatusID() == 0) || (studentCourse.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(studentCourse.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        studentCourse.setApprovalStatusID(studentCourse.getApprovalStatusID() + 1);
                        studentCourse.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(studentCourse);
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
                } else if (studentCourse.getApprovalStatusID()
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

    public String loadStudentCourseList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        studentCourse = new StudentCourseEntity();
        studentCourseList = new ArrayList<StudentCourseEntity>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        StudentCourseEntity criteria = new StudentCourseEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            StudentCourseEntity studentCourse = (StudentCourseEntity) entity;
            studentCourseList.add(studentCourse);
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
            if (studentCourseList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        studentCourseList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadStudentCourseList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadStudentCourseList(batEntity);
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
            if (studentCourseList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(studentCourseList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadStudentCourseList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadStudentCourseList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.STUDENT_COURSE);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(studentCourse.getStudentCourseID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadStudentCourseList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "unitofmeasureaudittrail.jsf";
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

    public String deleteStudentCourse() {
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
            if (studentCourseList.contains(studentCourse)) {
                if (studentCourse.getStudentCourseID() > 0) {
                    StudentCourseEntity criteria = new StudentCourseEntity();
                    criteria.setStudentCourseID(studentCourse.getStudentCourseID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                studentCourseList.remove(studentCourse);
                studentCourse = new StudentCourseEntity();
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

    public StudentCourseEntity findCourseByID(long courseID) {
        StudentCourseEntity stCourse = null;

        for (StudentCourseEntity course : getStudentCourseList()) {
            if (course.getCourseID() == courseID) {
                stCourse = course;
                break;
            }
        }

        return stCourse;
    }

    public void studentCourseItemSelected(ValueChangeEvent vce) {
    }

    public void studentCourseSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
    }

    public StudentCourseEntity getStudentCourse() {
        return this.studentCourse;
    }

    public void setStudentCourse(StudentCourseEntity studentcourse) {
        this.studentCourse = studentcourse;
    }

    public List<StudentCourseEntity> getStudentCourseList() {
        return this.studentCourseList;
    }

    public void setStudentCourseList(List<StudentCourseEntity> studentcourseList) {
        this.studentCourseList = studentcourseList;
    }

    /**
     * @return the sessionCourseList
     */
    public List<SessionCourseBouquet> getSessionCourseList() {
        return sessionCourseList;
    }

    /**
     * @param sessionCourseList the sessionCourseList to set
     */
    public void setSessionCourseList(List<SessionCourseBouquet> sessionCourseList) {
        this.sessionCourseList = sessionCourseList;
    }

    /**
     * @return the studentNumber
     */
    public String getStudentNumber() {
        return studentNumber;
    }

    /**
     * @param studentNumber the studentNumber to set
     */
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    /**
     * @return the studentCourseSelectList
     */
    public List<StudentCourseEntity> getStudentCourseSelectList() {
        return studentCourseSelectList;
    }

    /**
     * @param studentCourseSelectList the studentCourseSelectList to set
     */
    public void setStudentCourseSelectList(List<StudentCourseEntity> studentCourseSelectList) {
        this.studentCourseSelectList = studentCourseSelectList;
    }

    /**
     * @return the studentCourseTitleList
     */
    public List<String> getStudentCourseTitleList() {
        return studentCourseTitleList;
    }

    /**
     * @param studentCourseTitleList the studentCourseTitleList to set
     */
    public void setStudentCourseTitleList(List<String> studentCourseTitleList) {
        this.studentCourseTitleList = studentCourseTitleList;
    }

    /**
     * @return the totalCreditLoad
     */
    public int getTotalCreditLoad() {
        return totalCreditLoad;
    }

    /**
     * @param totalCreditLoad the totalCreditLoad to set
     */
    public void setTotalCreditLoad(int totalCreditLoad) {
        this.totalCreditLoad = totalCreditLoad;
    }

    /**
     * @return the courseIDList
     */
    public List<String> getCourseIDList() {
        return courseIDList;
    }

    /**
     * @param courseIDList the courseIDList to set
     */
    public void setCourseIDList(List<String> courseIDList) {
        this.courseIDList = courseIDList;
    }

}
