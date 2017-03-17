/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.setup.bean;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.resultprocess.bean.ResultProcessor;
import com.rsdynamix.academics.resultprocess.bean.ResultProcessorImpl;
import com.rsdynamix.academics.resultprocess.pojos.StudentCGPA;
import com.rsdynamix.academics.resultprocess.pojos.SubjectScoreDTO;
import com.rsdynamix.academics.setup.entities.AcademicCourseEntity;
import com.rsdynamix.academics.setup.entities.AcademicDepartmentEntity;
import com.rsdynamix.academics.setup.entities.AcademicScoreEntity;
import com.rsdynamix.academics.setup.entities.AcademicSessionEntity;
import com.rsdynamix.academics.setup.entities.CourseGradeEntity;
import com.rsdynamix.academics.setup.entities.FacultyEntity;
import com.rsdynamix.academics.setup.entities.StudentCourseEntity;
import com.rsdynamix.academics.setup.entities.TestTypeEntity;
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
import com.rsdynamix.academics.resultprocess.pojos.StudentScoreSummary;
import com.rsdynamix.academics.resultprocess.pojos.SubjectScoreSummary;

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "academicScoresBean")
public class AcademicScoresBean {

    private static final String ACADA_SCORE_KEY = "acada_score_id";
    //
    private static final String ACADA_SCORE_DEFAULT = "1";
    //
    private static final String ACADA_GRADE_KEY = "acada_grade_id";
    //
    private static final String ACADA_GRADE_DEFAULT = "1";
    //
    private AcademicScoreEntity courseScore;
    private List<AcademicScoreEntity> courseScoreList;
    private List<SubjectScoreDTO> subjectScoreList;
    private SubjectScoreSummary subjectScoreSummary;
    private double testPointPercent;
    //
    private CourseGradeEntity courseGrade;
    private List<CourseGradeEntity> courseGradeList;
    private List<StudentCGPA> studentCGPAList;
    //
    private StudentScoreSummary studentScoreSummary;
    //
    private String studentNumber;

    public AcademicScoresBean() {
        testPointPercent = 0;

        courseScore = new AcademicScoreEntity();
        courseScoreList = new ArrayList<AcademicScoreEntity>();

        courseGrade = new CourseGradeEntity();
        courseGradeList = new ArrayList<CourseGradeEntity>();
        studentCGPAList = new ArrayList<StudentCGPA>();
        
        subjectScoreSummary = new SubjectScoreSummary();
        studentScoreSummary = new StudentScoreSummary();
        
        studentNumber = "";
    }
    
    public String loadStudentGradeEntryList() {
        return "";
    }
    
    public String deleteGradeEntry() {
        return "";
    }

    public String computeCourseGrades() {
        ResultProcessor processor = new ResultProcessorImpl();

        AcademicGradePointBean gradePointBean = (AcademicGradePointBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicGradePointBean}", AcademicGradePointBean.class);
        if (gradePointBean == null) {
            gradePointBean = new AcademicGradePointBean();
            CommonBean.setBeanToContext("#{applicationScope.academicGradePointBean}", AcademicGradePointBean.class, gradePointBean);
        }
        courseGradeList = processor.processResult(courseScoreList, gradePointBean.getAcademicGradePointList());

        computeStudentCGPA();

        return "success";
    }

    public String computeStudentCGPA() {
        ResultProcessor processor = new ResultProcessorImpl();

        AcademicGradePointBean gradePointBean = (AcademicGradePointBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicGradePointBean}", AcademicGradePointBean.class);
        if (gradePointBean == null) {
            gradePointBean = new AcademicGradePointBean();
            CommonBean.setBeanToContext("#{applicationScope.academicGradePointBean}", AcademicGradePointBean.class, gradePointBean);
        }
        studentCGPAList = processor.buildCGPA(courseGradeList, gradePointBean.getAcademicGradePointList());

        return "";
    }

    public String addScoreEntry() {
        courseScoreList = new ArrayList<AcademicScoreEntity>();

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

        StudentCourseBean studentCourseBean = (StudentCourseBean) CommonBean.getBeanFromContext(
                "#{applicationScope.studentCourseBean}", StudentCourseBean.class);
        if (studentCourseBean == null) {
            studentCourseBean = new StudentCourseBean();
            CommonBean.setBeanToContext("#{applicationScope.studentCourseBean}", StudentCourseBean.class, studentCourseBean);
        }

        TestTypeBean testTypeBean = (TestTypeBean) CommonBean.getBeanFromContext(
                "#{applicationScope.testTypeBean}", TestTypeBean.class);
        if (testTypeBean == null) {
            testTypeBean = new TestTypeBean();
            CommonBean.setBeanToContext("#{applicationScope.testTypeBean}", TestTypeBean.class, testTypeBean);
        }

        AcademicLevelBean academicLevelBean = (AcademicLevelBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicLevelBean}", AcademicLevelBean.class);
        if (academicLevelBean == null) {
            academicLevelBean = new AcademicLevelBean();
            CommonBean.setBeanToContext("#{applicationScope.academicLevelBean}", AcademicLevelBean.class, academicLevelBean);
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
            List<StudentCourseEntity> courseList = studentCourseBean.findStudentCourseList(courseScore.getCourseID(), courseScore.getSessionID());
            for (StudentCourseEntity course : courseList) {
                AcademicScoreEntity score = new AcademicScoreEntity();

                score.setMatricNumber(course.getStudentNumber());

                SelectItem item = testTypeBean.findTestTypeItemByID(courseScore.getTestTypeID());
                if (item != null) {
                    score.setTestTypeDesc(item.getLabel());
                    score.setTestTypeID(courseScore.getTestTypeID());
                }

                item = academicLevelBean.findAcademicLevelByID(courseScore.getAcademicLevel());
                if (item != null) {
                    score.setAcademicLevelDesc(item.getLabel());
                    score.setAcademicLevel(courseScore.getAcademicLevel());
                }

                FacultyEntity faculty = facultyBean.findFacultyByID(courseScore.getFacultyID());
                if (faculty != null) {
                    score.setFacultyName(faculty.getFacultyName());
                    score.setFacultyID(courseScore.getFacultyID());
                }

                AcademicDepartmentEntity dept = academicDepartmentBean.findDepartmentByID(courseScore.getDepartmentID());
                if (dept != null) {
                    score.setDepartmentName(dept.getDepartmentName());
                    score.setDepartmentID(courseScore.getDepartmentID());
                }

                AcademicSessionEntity session = academicSessionBean.findSessionByID(courseScore.getSessionID());
                if (session != null) {
                    score.setSessionPeriod(session.getSessionPeriod());
                    score.setSessionID(courseScore.getSessionID());
                }

                AcademicCourseEntity acadaCourse = academicCourseBean.findAcademicCourseByID(courseScore.getCourseID());
                if (acadaCourse != null) {
                    score.setCourseCode(acadaCourse.getCourseCode());
                    score.setCourseTitle(acadaCourse.getCourseTitle());
                    score.setCourseID(courseScore.getCourseID());
                }

                courseScoreList.add(score);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String saveScoreList() {
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
            for (AcademicScoreEntity score : courseScoreList) {
                if (score.getSubjectScoreID() == 0) {
                    int scoreID = Integer.parseInt(appPropBean.getValue(ACADA_SCORE_KEY,
                            ACADA_SCORE_DEFAULT, true));
                    score.setSubjectScoreID(scoreID);
                    dataServer.saveData(score);

                } else if (score.getSubjectScoreID() > 0) {
                    dataServer.updateData(score);
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
    
    public String activateAcademicScore() {
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

        if (courseScore.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((courseScore.getApprovalStatusID() == 0) || (courseScore.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(courseScore.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        courseScore.setApprovalStatusID(courseScore.getApprovalStatusID() + 1);
                        courseScore.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(courseScore);
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
                } else if (courseScore.getApprovalStatusID()
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

    public String loadAcademicScoreList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        courseScore = new AcademicScoreEntity();
        courseScoreList = new ArrayList<AcademicScoreEntity>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        AcademicScoreEntity criteria = new AcademicScoreEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            AcademicScoreEntity courseScore = (AcademicScoreEntity) entity;
            courseScoreList.add(courseScore);
        }

        return "";
    }

    public String loadAcadaScorePreviousHistoricalState() {
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
            if (courseScoreList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        courseScoreList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadAcademicScoreList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadAcademicScoreList(batEntity);
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

    public String loadAcadaScoreNextHistoricalState() {
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
            if (courseScoreList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(courseScoreList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadAcademicScoreList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadAcademicScoreList(batEntity);
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

    public String gotoAcademicScoreAuditTrailPage() {
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.ACADEMIC_SCORE);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(courseScore.getSubjectScoreID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadAcademicScoreList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "acadascoreaudittrail.jsf";
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

    public String deleteAcademicScore() {
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
            if (courseScoreList.contains(courseScore)) {
                if (courseScore.getSubjectScoreID() > 0) {
                    AcademicScoreEntity criteria = new AcademicScoreEntity();
                    criteria.setSubjectScoreID(courseScore.getSubjectScoreID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                courseScoreList.remove(courseScore);
                courseScore = new AcademicScoreEntity();
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

    public String saveGradeList() {
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
            for (CourseGradeEntity grade : courseGradeList) {
                if (grade.getCourseGradeID() == 0) {
                    int scoreID = Integer.parseInt(appPropBean.getValue(ACADA_GRADE_KEY,
                            ACADA_GRADE_DEFAULT, true));
                    grade.setCourseGradeID(scoreID);
                    dataServer.saveData(grade);
                } else if (grade.getCourseGradeID() > 0) {
                    dataServer.updateData(grade);
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
    
    public String activateCourseGrade() {
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

        if (courseGrade.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((courseGrade.getApprovalStatusID() == 0) || (courseGrade.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(courseGrade.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        courseGrade.setApprovalStatusID(courseGrade.getApprovalStatusID() + 1);
                        courseGrade.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(courseGrade);
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
                } else if (courseGrade.getApprovalStatusID()
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

    public String loadCourseGradeList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        courseGrade = new CourseGradeEntity();
        courseGradeList = new ArrayList<CourseGradeEntity>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        CourseGradeEntity criteria = new CourseGradeEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            CourseGradeEntity courseGrade = (CourseGradeEntity) entity;
            courseGradeList.add(courseGrade);
        }

        return "";
    }

    public String loadCourseGradePreviousHistoricalState() {
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
            if (courseGradeList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        courseGradeList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadCourseGradeList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadCourseGradeList(batEntity);
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

    public String loadCourseGradeNextHistoricalState() {
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
            if (courseGradeList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(courseGradeList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadCourseGradeList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadCourseGradeList(batEntity);
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

    public String gotoCourseGradeAuditTrailPage() {
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.COURSE_GRADE);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(courseGrade.getCourseGradeID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadCourseGradeList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "coursegradeaudittrail.jsf";
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

    public String deleteCourseGrade() {
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
            if (courseGradeList.contains(courseGrade)) {
                if (courseGrade.getCourseGradeID() > 0) {
                    CourseGradeEntity criteria = new CourseGradeEntity();
                    criteria.setCourseGradeID(courseGrade.getCourseGradeID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                courseGradeList.remove(courseGrade);
                courseGrade = new CourseGradeEntity();
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

    public String loadScoreEntryList() {
        courseScoreList = new ArrayList<AcademicScoreEntity>();
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

        StudentCourseBean studentCourseBean = (StudentCourseBean) CommonBean.getBeanFromContext(
                "#{applicationScope.studentCourseBean}", StudentCourseBean.class);
        if (studentCourseBean == null) {
            studentCourseBean = new StudentCourseBean();
            CommonBean.setBeanToContext("#{applicationScope.studentCourseBean}", StudentCourseBean.class, studentCourseBean);
        }

        TestTypeBean testTypeBean = (TestTypeBean) CommonBean.getBeanFromContext(
                "#{applicationScope.testTypeBean}", TestTypeBean.class);
        if (testTypeBean == null) {
            testTypeBean = new TestTypeBean();
            CommonBean.setBeanToContext("#{applicationScope.testTypeBean}", TestTypeBean.class, testTypeBean);
        }

        AcademicLevelBean academicLevelBean = (AcademicLevelBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicLevelBean}", AcademicLevelBean.class);
        if (academicLevelBean == null) {
            academicLevelBean = new AcademicLevelBean();
            CommonBean.setBeanToContext("#{applicationScope.academicLevelBean}", AcademicLevelBean.class, academicLevelBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        AcademicScoreEntity criteria = new AcademicScoreEntity();
        boolean hasCriteria = false;

        if (courseScore.getFacultyID() > 0) {
            criteria.setFacultyID(courseScore.getFacultyID());
            hasCriteria = true;
        }

        if (courseScore.getDepartmentID() > 0) {
            criteria.setDepartmentID(courseScore.getDepartmentID());
            hasCriteria = true;
        }

        if (courseScore.getSessionID() > 0) {
            criteria.setSessionID(courseScore.getSessionID());
            hasCriteria = true;
        }

        if (courseScore.getCourseID() > 0) {
            criteria.setCourseID(courseScore.getCourseID());
            hasCriteria = true;
        }

        if (courseScore.getTestTypeID() > 0) {
            criteria.setTestTypeID(courseScore.getTestTypeID());
            hasCriteria = true;
        }

        if (courseScore.getAcademicLevel() > 0) {
            criteria.setAcademicLevel(courseScore.getAcademicLevel());
            hasCriteria = true;
        }

        List<AbstractEntity> entityList = null;
        if (hasCriteria) {
            try {
                entityList = dataServer.findData(criteria);

                for (AbstractEntity entity : entityList) {
                    AcademicScoreEntity score = (AcademicScoreEntity) entity;

                    SelectItem item = testTypeBean.findTestTypeItemByID(score.getTestTypeID());
                    if (item != null) {
                        score.setTestTypeDesc(item.getLabel());
                    }

                    item = academicLevelBean.findAcademicLevelByID(score.getAcademicLevel());
                    if (item != null) {
                        score.setAcademicLevelDesc(item.getLabel());
                    }

                    FacultyEntity faculty = facultyBean.findFacultyByID(score.getFacultyID());
                    if (faculty != null) {
                        score.setFacultyName(faculty.getFacultyName());
                    }

                    AcademicDepartmentEntity dept = academicDepartmentBean.findDepartmentByID(score.getDepartmentID());
                    if (dept != null) {
                        score.setDepartmentName(dept.getDepartmentName());
                    }

                    AcademicSessionEntity session = academicSessionBean.findSessionByID(score.getSessionID());
                    if (session != null) {
                        score.setSessionPeriod(session.getSessionPeriod());
                    }

                    AcademicCourseEntity acadaCourse = academicCourseBean.findAcademicCourseByID(score.getCourseID());
                    if (acadaCourse != null) {
                        score.setCourseCode(acadaCourse.getCourseCode());
                        score.setCourseTitle(acadaCourse.getCourseTitle());
                    }

                    courseScoreList.add(score);
                }
            } catch (Exception ex) {
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        return "";
    }
    
    public String loadStudentScoreEntryList() {
        courseScoreList = new ArrayList<AcademicScoreEntity>();
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

        StudentCourseBean studentCourseBean = (StudentCourseBean) CommonBean.getBeanFromContext(
                "#{applicationScope.studentCourseBean}", StudentCourseBean.class);
        if (studentCourseBean == null) {
            studentCourseBean = new StudentCourseBean();
            CommonBean.setBeanToContext("#{applicationScope.studentCourseBean}", StudentCourseBean.class, studentCourseBean);
        }

        TestTypeBean testTypeBean = (TestTypeBean) CommonBean.getBeanFromContext(
                "#{applicationScope.testTypeBean}", TestTypeBean.class);
        if (testTypeBean == null) {
            testTypeBean = new TestTypeBean();
            CommonBean.setBeanToContext("#{applicationScope.testTypeBean}", TestTypeBean.class, testTypeBean);
        }

        AcademicLevelBean academicLevelBean = (AcademicLevelBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicLevelBean}", AcademicLevelBean.class);
        if (academicLevelBean == null) {
            academicLevelBean = new AcademicLevelBean();
            CommonBean.setBeanToContext("#{applicationScope.academicLevelBean}", AcademicLevelBean.class, academicLevelBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        AcademicScoreEntity criteria = new AcademicScoreEntity();
        boolean hasCriteria = false;

        if (studentNumber.trim().length() > 0) {
            criteria.setMatricNumber(studentNumber);
            hasCriteria = true;
        }
        
        if (courseScore.getFacultyID() > 0) {
            criteria.setFacultyID(courseScore.getFacultyID());
            hasCriteria = true;
        }

        if (courseScore.getDepartmentID() > 0) {
            criteria.setDepartmentID(courseScore.getDepartmentID());
            hasCriteria = true;
        }

        if (courseScore.getSessionID() > 0) {
            criteria.setSessionID(courseScore.getSessionID());
            hasCriteria = true;
        }

        if (courseScore.getCourseID() > 0) {
            criteria.setCourseID(courseScore.getCourseID());
            hasCriteria = true;
        }

        if (courseScore.getTestTypeID() > 0) {
            criteria.setTestTypeID(courseScore.getTestTypeID());
            hasCriteria = true;
        }

        if (courseScore.getAcademicLevel() > 0) {
            criteria.setAcademicLevel(courseScore.getAcademicLevel());
            hasCriteria = true;
        }

        List<AbstractEntity> entityList = null;
        if (hasCriteria) {
            try {
                entityList = dataServer.findData(criteria);

                for (AbstractEntity entity : entityList) {
                    AcademicScoreEntity score = (AcademicScoreEntity) entity;

                    SelectItem item = testTypeBean.findTestTypeItemByID(score.getTestTypeID());
                    if (item != null) {
                        score.setTestTypeDesc(item.getLabel());
                    }

                    item = academicLevelBean.findAcademicLevelByID(score.getAcademicLevel());
                    if (item != null) {
                        score.setAcademicLevelDesc(item.getLabel());
                    }

                    FacultyEntity faculty = facultyBean.findFacultyByID(score.getFacultyID());
                    if (faculty != null) {
                        score.setFacultyName(faculty.getFacultyName());
                    }

                    AcademicDepartmentEntity dept = academicDepartmentBean.findDepartmentByID(score.getDepartmentID());
                    if (dept != null) {
                        score.setDepartmentName(dept.getDepartmentName());
                    }

                    AcademicSessionEntity session = academicSessionBean.findSessionByID(score.getSessionID());
                    if (session != null) {
                        score.setSessionPeriod(session.getSessionPeriod());
                    }

                    AcademicCourseEntity acadaCourse = academicCourseBean.findAcademicCourseByID(score.getCourseID());
                    if (acadaCourse != null) {
                        score.setCourseCode(acadaCourse.getCourseCode());
                        score.setCourseTitle(acadaCourse.getCourseTitle());
                    }

                    courseScoreList.add(score);
                }
            } catch (Exception ex) {
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        return "";
    }

    public String loadGradeEntryList() {
        courseGradeList = new ArrayList<CourseGradeEntity>();
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

        StudentCourseBean studentCourseBean = (StudentCourseBean) CommonBean.getBeanFromContext(
                "#{applicationScope.studentCourseBean}", StudentCourseBean.class);
        if (studentCourseBean == null) {
            studentCourseBean = new StudentCourseBean();
            CommonBean.setBeanToContext("#{applicationScope.studentCourseBean}", StudentCourseBean.class, studentCourseBean);
        }

        TestTypeBean testTypeBean = (TestTypeBean) CommonBean.getBeanFromContext(
                "#{applicationScope.testTypeBean}", TestTypeBean.class);
        if (testTypeBean == null) {
            testTypeBean = new TestTypeBean();
            CommonBean.setBeanToContext("#{applicationScope.testTypeBean}", TestTypeBean.class, testTypeBean);
        }

        AcademicLevelBean academicLevelBean = (AcademicLevelBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicLevelBean}", AcademicLevelBean.class);
        if (academicLevelBean == null) {
            academicLevelBean = new AcademicLevelBean();
            CommonBean.setBeanToContext("#{applicationScope.academicLevelBean}", AcademicLevelBean.class, academicLevelBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        CourseGradeEntity criteria = new CourseGradeEntity();
        boolean hasCriteria = false;

        if (courseGrade.getFacultyID() > 0) {
            criteria.setFacultyID(courseGrade.getFacultyID());
            hasCriteria = true;
        }

        if (courseGrade.getDepartmentID() > 0) {
            criteria.setDepartmentID(courseGrade.getDepartmentID());
            hasCriteria = true;
        }

        if (courseGrade.getSessionID() > 0) {
            criteria.setSessionID(courseGrade.getSessionID());
            hasCriteria = true;
        }

        if (courseGrade.getCourseID() > 0) {
            criteria.setCourseID(courseGrade.getCourseID());
            hasCriteria = true;
        }

        if (courseGrade.getAcademicLevel() > 0) {
            criteria.setAcademicLevel(courseGrade.getAcademicLevel());
            hasCriteria = true;
        }

        if (!courseGrade.getMatricNumber().trim().equals("")) {
            criteria.setMatricNumber(courseGrade.getMatricNumber());
            hasCriteria = true;
        }

        List<AbstractEntity> entityList = null;
        if (hasCriteria) {
            try {
                entityList = dataServer.findData(criteria);

                for (AbstractEntity entity : entityList) {
                    CourseGradeEntity grade = (CourseGradeEntity) entity;

                    SelectItem item = academicLevelBean.findAcademicLevelByID(grade.getAcademicLevel());
                    if (item != null) {
                        grade.setAcademicLevelDesc(item.getLabel());
                    }

                    FacultyEntity faculty = facultyBean.findFacultyByID(grade.getFacultyID());
                    if (faculty != null) {
                        grade.setFacultyName(faculty.getFacultyName());
                    }

                    AcademicDepartmentEntity dept = academicDepartmentBean.findDepartmentByID(grade.getDepartmentID());
                    if (dept != null) {
                        grade.setDepartmentName(dept.getDepartmentName());
                    }

                    AcademicSessionEntity session = academicSessionBean.findSessionByID(grade.getSessionID());
                    if (session != null) {
                        grade.setSessionPeriod(session.getSessionPeriod());
                    }

                    AcademicCourseEntity acadaCourse = academicCourseBean.findAcademicCourseByID(grade.getCourseID());
                    if (acadaCourse != null) {
                        grade.setCourseCode(acadaCourse.getCourseCode());
                        grade.setCourseTitle(acadaCourse.getCourseTitle());
                    }

                    courseGradeList.add(grade);
                }
            } catch (Exception ex) {
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        return "";
    }

    public void testTypeSelected(ValueChangeEvent vce) {
        int testTypeID = Integer.parseInt(vce.getNewValue().toString());

        if (testTypeID > 0) {
            TestTypeBean testTypeBean = (TestTypeBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.testTypeBean}", TestTypeBean.class);
            if (testTypeBean == null) {
                testTypeBean = new TestTypeBean();
                CommonBean.setBeanToContext("#{applicationScope.testTypeBean}", TestTypeBean.class, testTypeBean);
            }

            TestTypeEntity testType = testTypeBean.findTestTypeByID(testTypeID);
            if (testType != null) {
                testPointPercent = testType.getPointPercent();
            }
        }
    }

    public void facultyItemSelected(ValueChangeEvent vce) {
        long facultyID = Long.parseLong(vce.getNewValue().toString());

        if (facultyID > 0) {
            AcademicDepartmentBean academicDepartmentBean = (AcademicDepartmentBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class);
            if (academicDepartmentBean == null) {
                academicDepartmentBean = new AcademicDepartmentBean();
                CommonBean.setBeanToContext("#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class, academicDepartmentBean);
            }

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

    /**
     * @return the courseScore
     */
    public AcademicScoreEntity getCourseScore() {
        return courseScore;
    }

    /**
     * @param courseScore the courseScore to set
     */
    public void setCourseScore(AcademicScoreEntity courseScore) {
        this.courseScore = courseScore;
    }

    /**
     * @return the courseScoreList
     */
    public List<AcademicScoreEntity> getCourseScoreList() {
        return courseScoreList;
    }

    /**
     * @param courseScoreList the courseScoreList to set
     */
    public void setCourseScoreList(List<AcademicScoreEntity> courseScoreList) {
        this.courseScoreList = courseScoreList;
    }

    /**
     * @return the testPointPercent
     */
    public double getTestPointPercent() {
        return testPointPercent;
    }

    /**
     * @param testPointPercent the testPointPercent to set
     */
    public void setTestPointPercent(double testPointPercent) {
        this.testPointPercent = testPointPercent;
    }

    /**
     * @return the courseGrade
     */
    public CourseGradeEntity getCourseGrade() {
        return courseGrade;
    }

    /**
     * @param courseGrade the courseGrade to set
     */
    public void setCourseGrade(CourseGradeEntity courseGrade) {
        this.courseGrade = courseGrade;
    }

    /**
     * @return the courseGradeList
     */
    public List<CourseGradeEntity> getCourseGradeList() {
        return courseGradeList;
    }

    /**
     * @param courseGradeList the courseGradeList to set
     */
    public void setCourseGradeList(List<CourseGradeEntity> courseGradeList) {
        this.courseGradeList = courseGradeList;
    }

    /**
     * @return the studentCGPAList
     */
    public List<StudentCGPA> getStudentCGPAList() {
        return studentCGPAList;
    }

    /**
     * @param studentCGPAList the studentCGPAList to set
     */
    public void setStudentCGPAList(List<StudentCGPA> studentCGPAList) {
        this.studentCGPAList = studentCGPAList;
    }

    /**
     * @return the subjectScoreList
     */
    public List<SubjectScoreDTO> getSubjectScoreList() {
        return subjectScoreList;
    }

    /**
     * @param subjectScoreList the subjectScoreList to set
     */
    public void setSubjectScoreList(List<SubjectScoreDTO> subjectScoreList) {
        this.subjectScoreList = subjectScoreList;
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
     * @return the subjectScoreSummary
     */
    public SubjectScoreSummary getSubjectScoreSummary() {
        return subjectScoreSummary;
    }

    /**
     * @param subjectScoreSummary the subjectScoreSummary to set
     */
    public void setSubjectScoreSummary(SubjectScoreSummary subjectScoreSummary) {
        this.subjectScoreSummary = subjectScoreSummary;
    }

    /**
     * @return the studentScoreSummary
     */
    public StudentScoreSummary getStudentScoreSummary() {
        return studentScoreSummary;
    }

    /**
     * @param studentScoreSummary the studentScoreSummary to set
     */
    public void setStudentScoreSummary(StudentScoreSummary studentScoreSummary) {
        this.studentScoreSummary = studentScoreSummary;
    }
}
