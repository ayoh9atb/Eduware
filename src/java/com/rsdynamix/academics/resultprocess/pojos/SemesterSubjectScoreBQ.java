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
public class SemesterSubjectScoreBQ implements Serializable {

    private List<SubjectScoreDTO> subjectScoreList;
    private String semesterTier;

    public SemesterSubjectScoreBQ() {
        subjectScoreList = new ArrayList<SubjectScoreDTO>();
        semesterTier = "";
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

}
