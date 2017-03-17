/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.resultprocess.pojos;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author p-aniah
 */
public class StudentCGPA implements Serializable {

    private String studentNumber;
    private BigDecimal gradePointAverage;
    private String classOfDegreeCode;
    private String classOfDegreeDesc;
    private boolean selected;

    public StudentCGPA() {
        studentNumber = "";
        gradePointAverage = BigDecimal.ZERO;
        classOfDegreeCode = "";
        classOfDegreeDesc = "";
    }

    public boolean equals(Object o) {
        return ((o instanceof StudentCGPA) &&
                (((StudentCGPA)o).getStudentNumber().equals(this.getStudentNumber())));
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
     * @return the gradePointAverage
     */
    public BigDecimal getGradePointAverage() {
        return gradePointAverage;
    }

    /**
     * @param gradePointAverage the gradePointAverage to set
     */
    public void setGradePointAverage(BigDecimal gradePointAverage) {
        this.gradePointAverage = gradePointAverage;
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
     * @return the classOfDegreeCode
     */
    public String getClassOfDegreeCode() {
        return classOfDegreeCode;
    }

    /**
     * @param classOfDegreeCode the classOfDegreeCode to set
     */
    public void setClassOfDegreeCode(String classOfDegreeCode) {
        this.classOfDegreeCode = classOfDegreeCode;
    }

    /**
     * @return the classOfDegreeDesc
     */
    public String getClassOfDegreeDesc() {
        return classOfDegreeDesc;
    }

    /**
     * @param classOfDegreeDesc the classOfDegreeDesc to set
     */
    public void setClassOfDegreeDesc(String classOfDegreeDesc) {
        this.classOfDegreeDesc = classOfDegreeDesc;
    }

}
