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
public class StudentSemesterScoreBQ implements Serializable {

    private List<StudentScoreReport> studentScoreReportList;
    private String semesterTier;
    private int creditLoadTotal;
    private double gradePointTotal;
    private double scorePointTotal;
    private double gradePointAverage;

    public StudentSemesterScoreBQ() {
        studentScoreReportList = new ArrayList<StudentScoreReport>();
        semesterTier = "";
        creditLoadTotal = 0;
        gradePointTotal = 0;
        scorePointTotal = 0;
        gradePointAverage = 0;
    }

    public void computeCGPA(){
        if((gradePointTotal > 0) && (creditLoadTotal > 0)) {
            gradePointAverage = gradePointTotal/creditLoadTotal;
        }
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
     * @return the semesterTier
     */
    public String getSemesterTier() {
        return semesterTier;
    }

    /**
     * @param semesterTier the semesterTier to set
     */
    public void setSemesterTier(String semesterTier) {
        this.semesterTier = semesterTier;
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
