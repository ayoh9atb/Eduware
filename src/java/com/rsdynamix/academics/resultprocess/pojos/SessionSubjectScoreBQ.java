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
public class SessionSubjectScoreBQ implements Serializable {

    private String sessionPeriod;
    private List<SemesterSubjectScoreBQ> semesterSubjScoreList;

    public SessionSubjectScoreBQ() {
        sessionPeriod = "";
        semesterSubjScoreList = new ArrayList<SemesterSubjectScoreBQ>();
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
     * @return the semesterSubjScoreList
     */
    public List<SemesterSubjectScoreBQ> getSemesterSubjScoreList() {
        return semesterSubjScoreList;
    }

    /**
     * @param semesterSubjScoreList the semesterSubjScoreList to set
     */
    public void setSemesterSubjScoreList(List<SemesterSubjectScoreBQ> semesterSubjScoreList) {
        this.semesterSubjScoreList = semesterSubjScoreList;
    }

}
