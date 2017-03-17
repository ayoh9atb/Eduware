/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.resultprocess.pojos;


/**
 *
 * @author p-aniah
 */
public class StudentScoreReport {

    public String courseTitle;
    public String courseCode;
    public double courseScore;
    public String courseGrade;
    public double scorePoint;
    public double creditLoad;
    public double gradePoint;
    public String classPosition;
    public String sessionPeriod;
    private transient TermTierType semesterTier;

    public StudentScoreReport() {
        courseTitle = "";
        courseCode = "";
        courseScore = 0;
        courseGrade = "";
        scorePoint = 0;
        creditLoad = 0;
        gradePoint = 0;
        classPosition = "";
        sessionPeriod = "";
    }

    public boolean equals(Object o) {
        return ((o instanceof StudentScoreReport) &&
                (((StudentScoreReport)o).getCourseCode().equals(this.getCourseCode())) &&
                (((StudentScoreReport)o).getSessionPeriod().equals(this.getSessionPeriod())));
    }

    public void clearNulls() {

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
     * @return the courseScore
     */
    public double getCourseScore() {
        return courseScore;
    }

    /**
     * @param courseScore the courseScore to set
     */
    public void setCourseScore(double courseScore) {
        this.courseScore = courseScore;
    }

    /**
     * @return the courseGrade
     */
    public String getCourseGrade() {
        return courseGrade;
    }

    /**
     * @param courseGrade the courseGrade to set
     */
    public void setCourseGrade(String courseGrade) {
        this.courseGrade = courseGrade;
    }

    /**
     * @return the scorePoint
     */
    public double getScorePoint() {
        return scorePoint;
    }

    /**
     * @param scorePoint the scorePoint to set
     */
    public void setScorePoint(double scorePoint) {
        this.scorePoint = scorePoint;
    }

    /**
     * @return the creditLoad
     */
    public double getCreditLoad() {
        return creditLoad;
    }

    /**
     * @param creditLoad the creditLoad to set
     */
    public void setCreditLoad(double creditLoad) {
        this.creditLoad = creditLoad;
    }

    /**
     * @return the gradePoint
     */
    public double getGradePoint() {
        return gradePoint;
    }

    /**
     * @param gradePoint the gradePoint to set
     */
    public void setGradePoint(double gradePoint) {
        this.gradePoint = gradePoint;
    }

    /**
     * @return the classPosition
     */
    public String getClassPosition() {
        return classPosition;
    }

    /**
     * @param classPosition the classPosition to set
     */
    public void setClassPosition(String classPosition) {
        this.classPosition = classPosition;
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
    public TermTierType getSemesterTier() {
        return semesterTier;
    }

    /**
     * @param semesterTier the semesterTier to set
     */
    public void setSemesterTier(TermTierType semesterTier) {
        this.semesterTier = semesterTier;
    }
}
