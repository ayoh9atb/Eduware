/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.exam.entities;

import java.io.Serializable;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;

/**
 *
 * @author root
 */
@Entity
@Table(name = "EXAM_QUESTION")
public class ExamQuestionEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "QUESTION_ID", columnDefinition = "NUMBER(12)")
    private long questionID;
    //
    @Column(name = "QUESTION_STATEMENT", columnDefinition = "VARCHAR2(1024)")
    private String questionStatement;
    //
    @Column(name = "QUESTION_NUMBER", columnDefinition = "NUMBER(5)")
    private int questionNumber;
    //
    private transient String examPaperTitle;
    @Column(name = "EXAM_PAPER_ID", columnDefinition = "NUMBER(10)")
    private long examPaperID;
    //
    private transient String questionRankDesc;
    @Column(name = "QUESTION_RANK_ID", columnDefinition = "NUMBER(2)")
    private  int questionRankID;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    private transient List<QuestionOptionEntity> optionList;

    public ExamQuestionEntity() {
        questionID = 0;
        questionStatement = "";
        questionNumber = 0;

        examPaperID = 0;
        questionRankID = 0;

        initializeTransientFields();
    }

    public void initializeTransientFields(){
        questionRankDesc = "";
        examPaperTitle = "";
        setOptionList(new ArrayList<QuestionOptionEntity>());
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof ExamQuestionEntity) {
            if((((ExamQuestionEntity)o).getQuestionID() > 0) 
                    && (((ExamQuestionEntity)o).getQuestionID() == this.getQuestionID())){
                eqls = true;
            } else {
                eqls = ((ExamQuestionEntity)o).getQuestionStatement().equals(this.getQuestionStatement());
            }
        }

        return eqls;
    }

    public long getQuestionID() {
        return this.questionID;
    }

    public void setQuestionID(long questionID) {
        this.questionID = questionID;
    }

    public String getQuestionStatement() {
        return this.questionStatement;
    }

    public void setQuestionStatement(String questionStatement) {
        this.questionStatement = questionStatement;
    }

    public int getQuestionNumber() {
        return this.questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public long getExamPaperID() {
        return this.examPaperID;
    }

    public void setExamPaperID(long examPaperID) {
        this.examPaperID = examPaperID;
    }

    /**
     * @return the examPaperTitle
     */
    public String getExamPaperTitle() {
        return examPaperTitle;
    }

    /**
     * @param examPaperTitle the examPaperTitle to set
     */
    public void setExamPaperTitle(String examPaperTitle) {
        this.examPaperTitle = examPaperTitle;
    }

    /**
     * @return the questionRankDesc
     */
    public String getQuestionRankDesc() {
        return questionRankDesc;
    }

    /**
     * @param questionRankDesc the questionRankDesc to set
     */
    public void setQuestionRankDesc(String questionRankDesc) {
        this.questionRankDesc = questionRankDesc;
    }

    /**
     * @return the questionRankID
     */
    public int getQuestionRankID() {
        return questionRankID;
    }

    /**
     * @param questionRankID the questionRankID to set
     */
    public void setQuestionRankID(int questionRankID) {
        this.questionRankID = questionRankID;
    }

    /**
     * @return the optionList
     */
    public List<QuestionOptionEntity> getOptionList() {
        return optionList;
    }

    /**
     * @param optionList the optionList to set
     */
    public void setOptionList(List<QuestionOptionEntity> optionList) {
        this.optionList = optionList;
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
