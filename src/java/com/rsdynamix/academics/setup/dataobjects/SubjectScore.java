package com.rsdynamix.academics.setup.dataobjects;

import java.util.Collections;

public class SubjectScore {

    private String subjectCode;
    private double subjectScore;
    private int studentPosition;
    private String studentGrade;
    private String matricNumber;
    private String positionTier;

    public SubjectScore() {
        subjectCode = "";
        subjectScore = 0;
        studentPosition = 0;
        studentGrade = "";
        matricNumber = "";
        setPositionTier("");
    }

    public SubjectScore(String subjectCode, double subjectScore, String matricNumber) {
        this.subjectCode = subjectCode;
        this.subjectScore = subjectScore;
        this.matricNumber = matricNumber;
    }

    public String toString() {
        return ("(MATRIC, SUBJ-CODE, SCORE, POSITION, GRADE) ==>> (" + getMatricNumber() + ", " + getSubjectCode() + ", " + getSubjectScore() + ", " + getPositionTier() + ", " + getStudentGrade() + ")");
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectScore(double subjectScore) {
        this.subjectScore = subjectScore;
    }

    public double getSubjectScore() {
        return subjectScore;
    }

    public void setStudentPosition(int studentPosition) {
        this.studentPosition = studentPosition;
    }

    public int getStudentPosition() {
        return studentPosition;
    }

    public void setStudentGrade(String studentGrade) {
        this.studentGrade = studentGrade;
    }

    public String getStudentGrade() {
        return studentGrade;
    }

    public void setMatricNumber(String matricNumber) {
        this.matricNumber = matricNumber;
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public void setPositionTier(String positionTier) {
        this.positionTier = positionTier;
    }

    public String getPositionTier() {
        return positionTier;
    }
}
