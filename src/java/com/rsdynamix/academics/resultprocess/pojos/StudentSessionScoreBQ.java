/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.resultprocess.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author p-aniah
 */
public class StudentSessionScoreBQ implements Serializable {

    private List<StudentSemesterScoreBQ> studentSemesterScoreBQList;
    private String sessionPeriod;
    private int creditLoadTotal;
    private double gradePointTotal;
    private double scorePointTotal;
    private double gradePointAverage;

    public StudentSessionScoreBQ() {
        studentSemesterScoreBQList = new ArrayList<StudentSemesterScoreBQ>();
        sessionPeriod = "";
        creditLoadTotal = 0;
        gradePointTotal = 0;
        scorePointTotal = 0;
        gradePointAverage = 0;
    }

    public boolean equals(Object o) {
        return ((o instanceof StudentSessionScoreBQ) &&
                ((StudentSessionScoreBQ)o).getSessionPeriod().equals(this.getSessionPeriod()));
    }

    public void computeCGPA(){
        if((gradePointTotal > 0) && (creditLoadTotal > 0)) {
            gradePointAverage = gradePointTotal/creditLoadTotal;
        }
    }

    /**
     * @return the studentSemesterScoreBQList
     */
    public List<StudentSemesterScoreBQ> getStudentSemesterScoreBQList() {
        return studentSemesterScoreBQList;
    }

    /**
     * @param studentSemesterScoreBQList the studentSemesterScoreBQList to set
     */
    public void setStudentSemesterScoreBQList(List<StudentSemesterScoreBQ> studentSemesterScoreBQList) {
        this.studentSemesterScoreBQList = studentSemesterScoreBQList;
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
     * @return the creditLoadTotal
     */
    public int getCreditLoadTotal() {
        return creditLoadTotal;
    }

    /**
     * @param creditLoadTotal the creditLoadTotal to set
     */
    public void setCreditLoadTotal(int creditLoadTotal) {
        this.creditLoadTotal = creditLoadTotal;
    }

    /**
     * @return the gradePointTotal
     */
    public double getGradePointTotal() {
        return gradePointTotal;
    }

    /**
     * @param gradePointTotal the gradePointTotal to set
     */
    public void setGradePointTotal(double gradePointTotal) {
        this.gradePointTotal = gradePointTotal;
    }

    /**
     * @return the scorePointTotal
     */
    public double getScorePointTotal() {
        return scorePointTotal;
    }

    /**
     * @param scorePointTotal the scorePointTotal to set
     */
    public void setScorePointTotal(double scorePointTotal) {
        this.scorePointTotal = scorePointTotal;
    }

    /**
     * @return the gradePointAverage
     */
    public double getGradePointAverage() {
        return gradePointAverage;
    }

    /**
     * @param gradePointAverage the gradePointAverage to set
     */
    public void setGradePointAverage(double gradePointAverage) {
        this.gradePointAverage = gradePointAverage;
    }

}
