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
@Table(name = "QUESTION_OPTION")
public class QuestionOptionEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "QUESTION_OPTION_ID", columnDefinition = "NUMBER(12)")
    private long questionOptionID;
    //
    @Column(name = "QUESTION_ID", columnDefinition = "NUMBER(12)")
    private long questionID;
    //
    @Column(name = "EXAM_PAPER_ID", columnDefinition = "NUMBER(12)")
    private long examPaperID;
    //
    @Column(name = "OPTION_TEXT", columnDefinition = "VARCHAR2(255)")
    private String optionText;
    //
    @Column(name = "OPTION_LETTER", columnDefinition = "VARCHAR2(1)")
    private String optionLetter;
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

    public QuestionOptionEntity() {
        questionOptionID = 0;

        questionID = 0;
        examPaperID = 0;

        optionText = "";
        optionLetter = "";
        answer = false;
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof QuestionOptionEntity) {
            if((((QuestionOptionEntity)o).getQuestionOptionID() > 0) &&
                    (((QuestionOptionEntity)o).getQuestionOptionID() == this.getQuestionOptionID())){
                eqls = true;
            } else {
                eqls = ((QuestionOptionEntity)o).getOptionText().equals(this.getOptionText()) &&
                        (((QuestionOptionEntity)o).getQuestionID() == this.getQuestionID());
            }
        }

        return eqls;
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

    public String getOptionText() {
        return this.optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public String getOptionLetter() {
        return this.optionLetter;
    }

    public void setOptionLetter(String optionLetter) {
        this.optionLetter = optionLetter;
    }

    public boolean isAnswer() {
        return this.answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
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
