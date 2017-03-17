/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.exam.entities;

import java.io.Serializable;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 *
 * @author root
 */
@Entity
@Table(name = "STUDENT_SELECTED_OPTION")
public class StudentSelectedOptionEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "SELECTED_OPTION_ID", columnDefinition = "NUMBER(12)")
    private long selectedOptionID;
    //
    @Column(name = "QUESTION_OPTION_ID", columnDefinition = "NUMBER(12)")
    private long questionOptionID;
    //
    @Column(name = "QUESTION_ID", columnDefinition = "NUMBER(12)")
    private long questionID;
    //
    private transient String examPaperDesc;
    @Column(name = "EXAM_PAPER_ID", columnDefinition = "NUMBER(12)")
    private long examPaperID;
    //
    @Column(name = "STUDENT_NO", columnDefinition = "VARCHAR2(15)")
    private String studentNumber;
    //
    @Column(name = "ANSWER", columnDefinition = "NUMBER(1)")
    private boolean answer;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    transient private String optionLetter;
    //
    transient private String optionText;
    //
    transient private int questionNumber;
    //
    transient private String questionStatement;
    //
    transient private int numberOfPoints;

    public StudentSelectedOptionEntity() {
        selectedOptionID = 0;
        questionOptionID = 0;
        questionID = 0;
        answer = false;

        examPaperID = 0;
        studentNumber = "";
    }

    public void initializeTransientFields(){
        setOptionLetter("");
        setOptionText("");
        setQuestionNumber(0);
        setQuestionStatement("");
        setNumberOfPoints(0);
    }

    public long getSelectedOptionID() {
        return this.selectedOptionID;
    }

    public void setSelectedOptionID(long selectedOptionID) {
        this.selectedOptionID = selectedOptionID;
    }

    public long getQuestionOptionID() {
        return this.questionOptionID;
    }

    public void setQuestionOptionID(long questionOptionID) {
        this.questionOptionID = questionOptionID;
    }

    public long getQuestionID() {
        return this.questionID;
    }

    public void setQuestionID(long questionID) {
        this.questionID = questionID;
    }

    public boolean isAnswer() {
        return this.answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }

    /**
     * @return the optionLetter
     */
    public String getOptionLetter() {
        return optionLetter;
    }

    /**
     * @param optionLetter the optionLetter to set
     */
    public void setOptionLetter(String optionLetter) {
        this.optionLetter = optionLetter;
    }

    /**
     * @return the optionText
     */
    public String getOptionText() {
        return optionText;
    }

    /**
     * @param optionText the optionText to set
     */
    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    /**
     * @return the questionNumber
     */
    public int getQuestionNumber() {
        return questionNumber;
    }

    /**
     * @param questionNumber the questionNumber to set
     */
    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    /**
     * @return the questionStatement
     */
    public String getQuestionStatement() {
        return questionStatement;
    }

    /**
     * @param questionStatement the questionStatement to set
     */
    public void setQuestionStatement(String questionStatement) {
        this.questionStatement = questionStatement;
    }

    /**
     * @return the numberOfPoints
     */
    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    /**
     * @param numberOfPoints the numberOfPoints to set
     */
    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    /**
     * @return the examPaperDesc
     */
    public String getExamPaperDesc() {
        return examPaperDesc;
    }

    /**
     * @param examPaperDesc the examPaperDesc to set
     */
    public void setExamPaperDesc(String examPaperDesc) {
        this.examPaperDesc = examPaperDesc;
    }

    /**
     * @return the examPaperID
     */
    public long getExamPaperID() {
        return examPaperID;
    }

    /**
     * @param examPaperID the examPaperID to set
     */
    public void setExamPaperID(long examPaperID) {
        this.examPaperID = examPaperID;
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
     * @return the approvalStatusID
     */
    public int getApprovalStatusID() {
        return approvalStatusID;
    }

    /**
     * @param approvalStatusID the approvalStatusID to set
     */
    public void setApprovalStatusID(int approvalStatusID) {
        this.approvalStatusID = approvalStatusID;
    }

    /**
     * @return the approver
     */
    public String getApprover() {
        return approver;
    }

    /**
     * @param approver the approver to set
     */
    public void setApprover(String approver) {
        this.approver = approver;
    }

    /**
     * @return the actionTrail
     */
    public BusinessActionTrailEntity getActionTrail() {
        return actionTrail;
    }

    /**
     * @param actionTrail the actionTrail to set
     */
    public void setActionTrail(BusinessActionTrailEntity actionTrail) {
        this.actionTrail = actionTrail;
    }
}
