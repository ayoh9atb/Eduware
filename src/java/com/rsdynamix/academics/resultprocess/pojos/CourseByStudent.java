/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.resultprocess.pojos;

import java.io.Serializable;

/**
 *
 * @author p-aniah
 */
public class CourseByStudent implements Serializable {

    private String studentNumber;

    public CourseByStudent() {
        studentNumber = "";
    }

    public boolean equals(Object o) {
        return ((o instanceof CourseByStudent) &&
                (((CourseByStudent)o).getStudentNumber() == this.getStudentNumber()));
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

}
