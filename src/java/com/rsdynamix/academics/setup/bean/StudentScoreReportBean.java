/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.setup.bean;

import com.rsdynamix.academics.setup.entities.AcademicCourseEntity;
import com.rsdynamix.academics.setup.entities.AcademicGradePointEntity;
import com.rsdynamix.academics.setup.entities.CourseGradeEntity;
import com.codrellica.projects.commons.DateUtil;
import com.rsdynamix.academics.resultprocess.pojos.StudentScoreReport;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.bi.projects.report.bean.UlticoreReportBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "studentScoreReportBean")
public class StudentScoreReportBean {

    private String studentNumber;
    private String printDate;
    private String startingSession;
    private String endingSession;
    //
    private List<StudentScoreReport> studentScoreReportList;
    private Map<String, Object> paramters;

    public StudentScoreReportBean() {
        studentNumber = "";
        printDate = "";
        startingSession = "";
        endingSession = "";

        studentScoreReportList = new ArrayList<StudentScoreReport>();
        paramters = new HashMap<String, Object>();
    }

    public void buildScoreReport(List<CourseGradeEntity> scoreList,
            String startSession,
            String endSession,
            AcademicGradePointBean gradePointBean) {
        studentScoreReportList = new ArrayList<StudentScoreReport>();

        paramters.put("printDate", DateUtil.getCurrentDateStr());
        paramters.put("studentNumber", scoreList.get(0).getMatricNumber());
        paramters.put("startingSession", startSession);
        paramters.put("endingSession", endSession);
        
        UlticoreReportBean ulticoreReportBean = (UlticoreReportBean) CommonBean.getBeanFromContext(
                "#{sessionScope.ulticoreReportBean}", UlticoreReportBean.class);
        if (ulticoreReportBean == null) {
            ulticoreReportBean = new UlticoreReportBean();
            CommonBean.setBeanToContext("#{sessionScope.ulticoreReportBean}", UlticoreReportBean.class, ulticoreReportBean);
        }

        AcademicCourseBean academicCourseBean = (AcademicCourseBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicCourseBean}", AcademicCourseBean.class);
        if (academicCourseBean == null) {
            academicCourseBean = new AcademicCourseBean();
            CommonBean.setBeanToContext("#{applicationScope.academicCourseBean}", AcademicCourseBean.class, academicCourseBean);
        }

        for(CourseGradeEntity score : scoreList) {
//            StudentScoreReport report = new StudentScoreReport();

//            report.setClassPosition(score.getPositionTier());
//            report.setCourseCode(score.getCourseCode());
//            report.setCourseTitle(score.getCourseTitle());
//            report.setCourseGrade(score.getStudentGrade());
//            report.setCourseScore(score.getSubjectScore());
//            report.setCreditLoad(score.getCreditLoad());
            
            AcademicCourseEntity course = academicCourseBean.findAcademicCourseByID(score.getCourseID());
            if(course != null) {
//                report.setSemesterTier(course.getSemesterTier());
            }

            AcademicGradePointEntity gradePoint = getGradeOfScore(
                    score.getSubjectScore(), gradePointBean.getAcademicGradePointList());
            if(gradePoint != null) {
//                report.setScorePoint(gradePoint.getGradePoint());
            }

//            report.setGradePoint(score.getCreditLoad() * report.getScorePoint());
//            report.setSessionPeriod(score.getSessionPeriod());
//
//            studentScoreReportList.add(report);
        }

//        TableReportTransporter reportTransporter = new TableReportTransporter();
//        reportTransporter.setReportList(studentScoreReportList);
//        reportTransporter.setParameters(paramters);
//        reportTransporter.setTemplateFileName("DynamoFolder/reports/school/StudentAcademicReport.jasper");
//        reportTransporter.setOutputFileName("DynamoFolder/reports/school/StudentReport.rtf");

        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession servletSession = (HttpSession) context.getExternalContext().getSession(false);
        ulticoreReportBean.setServletSession(servletSession);

//        servletSession.setAttribute(ulticoreReportBean.getServletSession().getId(), reportTransporter);
    }

    protected AcademicGradePointEntity getGradeOfScore(double score,
            List<AcademicGradePointEntity> academicGradePointList) {
        AcademicGradePointEntity grade = null;

        for (AcademicGradePointEntity criterion : academicGradePointList) {
            if ((score >= criterion.getLowerLimit().doubleValue()) &&
                    (score <= criterion.getUpperLimit().doubleValue())) {
                grade = criterion;
                break;
            }
        }

        return grade;
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
     * @return the printDate
     */
    public String getPrintDate() {
        return printDate;
    }

    /**
     * @param printDate the printDate to set
     */
    public void setPrintDate(String printDate) {
        this.printDate = printDate;
    }

    /**
     * @return the startingSession
     */
    public String getStartingSession() {
        return startingSession;
    }

    /**
     * @param startingSession the startingSession to set
     */
    public void setStartingSession(String startingSession) {
        this.startingSession = startingSession;
    }

    /**
     * @return the endingSession
     */
    public String getEndingSession() {
        return endingSession;
    }

    /**
     * @param endingSession the endingSession to set
     */
    public void setEndingSession(String endingSession) {
        this.endingSession = endingSession;
    }

    /**
     * @return the studentScoreReportList
     */
    public List<StudentScoreReport> getStudentScoreReportList() {
        return studentScoreReportList;
    }

    /**
     * @param studentScoreReportList the studentScoreReportList to set
     */
    public void setStudentScoreReportList(List<StudentScoreReport> studentScoreReportList) {
        this.studentScoreReportList = studentScoreReportList;
    }

    /**
     * @return the paramters
     */
    public Map<String, Object> getParamters() {
        return paramters;
    }

    /**
     * @param paramters the paramters to set
     */
    public void setParamters(Map<String, Object> paramters) {
        this.paramters = paramters;
    }

}
