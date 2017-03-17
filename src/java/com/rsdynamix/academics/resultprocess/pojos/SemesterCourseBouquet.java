/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.resultprocess.pojos;

import com.rsdynamix.academics.setup.entities.StudentCourseEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author p-aniah
 */
public class SemesterCourseBouquet implements Serializable {

    private List<StudentCourseEntity> studentCourseList;
    private String semesterTier;
    private int creditLoad;

    public SemesterCourseBouquet() {
        studentCourseList = new ArrayList<StudentCourseEntity>();
        semesterTier = "";
        creditLoad = 0;
    }

    public List<StudentCourseEntity> getStudentCourseList() {
        return this.studentCourseList;
    }

    public void setStudentCourseList(List<StudentCourseEntity> studentcourseList) {
        this.studentCourseList = studentcourseList;
    }

    /**
     * @return the creditLoad
     */
    public int getCreditLoad() {
        return creditLoad;
    }

    /**
     * @param creditLoad the creditLoad to set
     */
    public void setCreditLoad(int creditLoad) {
        this.creditLoad = creditLoad;
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
