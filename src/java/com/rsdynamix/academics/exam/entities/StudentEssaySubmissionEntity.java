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
@Table(name = "STUDENT_ESSAY_SUBMISSION")
public class StudentEssaySubmissionEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "SUBMISSION_ID", columnDefinition = "NUMBER(12)")
    private long submissionID;
    //
    @Column(name = "QUESTION_ID", columnDefinition = "NUMBER(12)")
    private long questionID;
    //
    private transient String examPaperTitle;
    @Column(name = "EXAM_PAPER_ID", columnDefinition = "NUMBER(12)")
    private long examPaperID;
    //
    @Column(name = "STUDENT_NO", columnDefinition = "VARCHAR2(15)")
    private String studentNumber;
    //
    @Column(name = "NUMBER_OF_POINTS", columnDefinition = "DOUBLE PRECISION")
    private double numberOfPoints;
    //
    @Column(name = "ANSWER_TEXT", columnDefinition = "VARCHAR2(4000)")
    private String answerText;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //

    public StudentEssaySubmissionEntity() {
        submissionID = 0;
        questionID = 0;
        numberOfPoints = 0;
        answerText = "";

        examPaperID = 0;
        studentNumber = "";
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof StudentEssaySubmissionEntity) {
            if((((StudentEssaySubmissionEntity)o).getSubmissionID() > 0) &&
                    (((StudentEssaySubmissionEntity)o).getSubmissionID() == this.getSubmissionID())){
                eqls = true;
            }
        }

        return eqls;
    }

    public long getSubmissionID() {
        return this.submissionID;
    }

    public void setSubmissionID(long submissionID) {
        this.submissionID = submissionID;
    }

    public long getQuestionID() {
        return this.questionID;
    }

    public void setQuestionID(long questionID) {
        this.questionID = questionID;
    }

    public double getNumberOfPoints() {
        return this.numberOfPoints;
    }

    public void setNumberOfPoints(double numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    public String getAnswerText() {
        return this.answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
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
