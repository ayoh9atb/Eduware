/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.resultprocess.pojos;

import com.rsdynamix.academics.setup.bean.AcademicCourseBean;
import com.rsdynamix.academics.setup.bean.SubjectOfStudyBean;
import com.rsdynamix.academics.setup.bean.TestTypeBean;
import com.rsdynamix.academics.setup.entities.AcademicCourseEntity;
import com.rsdynamix.academics.setup.entities.AcademicScoreEntity;
import com.rsdynamix.academics.setup.entities.StudentCourseEntity;
import com.rsdynamix.academics.setup.entities.SubjectOfStudyEntity;
import com.rsdynamix.academics.setup.entities.TermTier;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.List;

/**
 *
 * @author p-aniah
 */
public class SubjectScoreDTO implements Serializable {

    private double examScore;
    private double firstAssessScore;
    private double secondAssessScore;
    private double totalScore;
    private String matricNumber;
    private long courseID;
    private String courseTitle;
    private String courseCode;
    private int academicLevel;
    private String sessionPeriod;
    private long sessionID;
    private TermTier semesterTier;
    private boolean selected;

    public SubjectScoreDTO() {
        examScore = 0;
        firstAssessScore = 0;
        secondAssessScore = 0;
        totalScore = 0;
        matricNumber = "";

        academicLevel = 0;
        courseID = 0;
        courseTitle = "";
        courseCode = "";

        sessionPeriod = "";
        sessionID = 0;

        selected = false;
    }

    public AcademicScoreEntity constructScoreOfTestType(int TestType) {
        AcademicScoreEntity scoreEntity = new AcademicScoreEntity();

        scoreEntity.setAcademicLevel(academicLevel);
        scoreEntity.setCourseCode(courseCode);
        scoreEntity.setCourseID(courseID);
        scoreEntity.setSessionID(sessionID);
        scoreEntity.setTestTypeID(TestType);
        scoreEntity.setSessionPeriod(sessionPeriod);

        if (TestType == TestTypeBean.ASSESSMENT_1_TEST) {
            scoreEntity.setSubjectScore(firstAssessScore);
        } else if (TestType == TestTypeBean.ASSESSMENT_2_TEST) {
            scoreEntity.setSubjectScore(secondAssessScore);
        } else if (TestType == TestTypeBean.EXAMINATION_TEST) {
            scoreEntity.setSubjectScore(examScore);
        }

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

        AcademicCourseEntity course = academicCourseBean.findAcademicCourseByID(courseID);
        if (course != null) {
            SubjectOfStudyEntity subject = subjectOfStudyBean.findSubjectOfStudyByID(course.getSubjectID());
            if(subject != null) {
                scoreEntity.setFacultyID(subject.getFacultyID());
                scoreEntity.setDepartmentID(subject.getDepartmentID());
            }
        }

        return scoreEntity;
    }

    public String toString() {
        String fieldStr = "";

        try {
            Field[] fieldArray = this.getClass().getDeclaredFields();
            for (Field field : fieldArray) {
                fieldStr += "<<" + field.getName() + ">> ::== <" + field.get(this).toString() + ">\n";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return fieldStr;
    }

    public boolean equals(Object o) {
        return ((o instanceof SubjectScoreDTO) &&
                (((SubjectScoreDTO) o).getMatricNumber().equals(this.getMatricNumber())) &&
                (((SubjectScoreDTO) o).getCourseID() == this.getCourseID()));
    }

    /**
     * @return the examScore
     */
    public double getExamScore() {
        return examScore;
    }

    /**
     * @param examScore the examScore to set
     */
    public void setExamScore(double examScore) {
        totalScore += examScore;
        this.examScore = examScore;
    }

    /**
     * @return the firstAssessScore
     */
    public double getFirstAssessScore() {
        return firstAssessScore;
    }

    /**
     * @param firstAssessScore the firstAssessScore to set
     */
    public void setFirstAssessScore(double firstAssessScore) {
        totalScore += firstAssessScore;
        this.firstAssessScore = firstAssessScore;
    }

    /**
     * @return the secondAssessScore
     */
    public double getSecondAssessScore() {
        return secondAssessScore;
    }

    /**
     * @param secondAssessScore the secondAssessScore to set
     */
    public void setSecondAssessScore(double secondAssessScore) {
        totalScore += secondAssessScore;
        this.secondAssessScore = secondAssessScore;
    }

    /**
     * @return the matricNumber
     */
    public String getMatricNumber() {
        return matricNumber;
    }

    /**
     * @param matricNumber the matricNumber to set
     */
    public void setMatricNumber(String matricNumber) {
        this.matricNumber = matricNumber;
    }

    /**
     * @return the selected
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * @param selected the selected to set
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * @return the courseID
     */
    public long getCourseID() {
        return courseID;
    }

    /**
     * @param courseID the courseID to set
     */
    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    /**
     * @return the courseTitle
     */
    public String getCourseTitle() {
        return courseTitle;
    }

    /**
     * @param courseTitle the courseTitle to set
     */
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    /**
     * @return the courseCode
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * @param courseCode the courseCode to set
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    /**
     * @return the totalScore
     */
    public double getTotalScore() {
        return totalScore;
    }

    /**
     * @param totalScore the totalScore to set
     */
    public void setTotalScore(double totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * @return the sessionPeriod
     */
    public String getSessionPeriod() {
        return sessionPeriod;
    }

    /**
     * @param sessionPeriod the sessionPeriod to set
     */
    public void setSessionPeriod(String sessionPeriod) {
        this.sessionPeriod = sessionPeriod;
    }

    /**
     * @return the semesterTier
     */
    public TermTier getSemesterTier() {
        return semesterTier;
    }

    /**
     * @param semesterTier the semesterTier to set
     */
    public void setSemesterTier(TermTier semesterTier) {
        this.semesterTier = semesterTier;
    }

    /**
     * @return the academicLevel
     */
    public int getAcademicLevel() {
        return academicLevel;
    }

    /**
     * @param academicLevel the academicLevel to set
     */
    public void setAcademicLevel(int academicLevel) {
        this.academicLevel = academicLevel;
    }

    /**
     * @return the sessionID
     */
    public long getSessionID() {
        return sessionID;
    }

    /**
     * @param sessionID the sessionID to set
     */
    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }
}
