/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.exam.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author root
 */
@Entity
@Table(name = "EXAM_PAPER")
public class ExamPaperEntity extends AbstractEntity implements Serializable {
    //
    @Id
    @Column(name = "EXAM_PAPER_ID", columnDefinition = "NUMBER(12)")
    private long examPaperID;
    //
    @Column(name = "EXAM_PAPER_NUMBER", columnDefinition = "VARCHAR2(20)")
    private String examPaperNumber;
    //
    @Column(name = "EXAM_PAPER_TITLE", columnDefinition = "VARCHAR2(255)")
    private String examPaperTitle;
    //
    private transient java.util.Date paperDateTO;
    @Column(name = "PAPER_DATE", columnDefinition = "DATE")
    private Date paperDate;
    //
    @Column(name = "PAPER_START_TIME", columnDefinition = "VARCHAR2(7)")
    private String paperStartTime;
    //
    @Column(name = "VARIABLE_START_TIME", columnDefinition = "NUMBER(1)")
    private boolean variableStartTime;
    //
    @Column(name = "PAPER_DURATION_IN_MINUTES", columnDefinition = "NUMBER(6)")
    private int paperDurationInMinutes;
    //
    private transient String subjectTitle;
    @Column(name = "SUBJECT_ID", columnDefinition = "NUMBER(10)")
    private long subjectID;
    //
    private transient String courseTitle;
    @Column(name = "COURSE_ID", columnDefinition = "NUMBER(10)")
    private long courseID;

    @Column(name = "NUMBER_OF_QUESTIONS", columnDefinition = "NUMBER(5)")
    private int numberOfQuestion;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    private transient String paperStartTimeHour;
    //
    private transient String paperStartTimeMinute;
    //
    private transient String paperStartTimeMeridiem;
    //
    private transient List<ExamQuestionEntity> questionList;

    public ExamPaperEntity() {
        examPaperID = 0;
        examPaperNumber = "";
        examPaperTitle = "";
        variableStartTime = false;
        paperDurationInMinutes = 0;
        subjectID = 0;
        courseID = 0;
        numberOfQuestion = 0;

        paperStartTimeHour = "";
        paperStartTimeMinute = "";
        paperStartTimeMeridiem = "";
        
        questionList = new ArrayList<ExamQuestionEntity>();

        initializeTransientFields();
    }

    public void initializeTransientFields(){
        paperDateTO = new java.util.Date();
        subjectTitle = "";
        courseTitle = "";
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof ExamPaperEntity) {
            if((((ExamPaperEntity)o).getExamPaperID() > 0) && (((ExamPaperEntity)o).getExamPaperID() == this.getExamPaperID())){
                eqls = true;
            } else {
                eqls = ((ExamPaperEntity)o).getExamPaperTitle().equals(this.getExamPaperTitle());
            }
        }

        return eqls;
    }

    public long getExamPaperID() {
        return this.examPaperID;
    }

    public void setExamPaperID(long examPaperID) {
        this.examPaperID = examPaperID;
    }

    public String getExamPaperNumber() {
        return this.examPaperNumber;
    }

    public void setExamPaperNumber(String examPaperNumber) {
        this.examPaperNumber = examPaperNumber;
    }

    public String getExamPaperTitle() {
        return this.examPaperTitle;
    }

    public void setExamPaperTitle(String examPaperTitle) {
        this.examPaperTitle = examPaperTitle;
    }

    public Date getPaperDate() {
        return this.paperDate;
    }

    public void setPaperDate(Date paperDate) {
        this.paperDate = paperDate;
    }

    public String getPaperStartTime() {
        return this.paperStartTime;
    }

    public void setPaperStartTime(String paperStartTime) {
        this.paperStartTime = paperStartTime;
    }

    /**
     * @return the variableStartTime
     */
    public boolean isVariableStartTime() {
        return variableStartTime;
    }

    /**
     * @param variableStartTime the variableStartTime to set
     */
    public void setVariableStartTime(boolean variableStartTime) {
        this.variableStartTime = variableStartTime;
    }

    /**
     * @return the paperDurationInMinutes
     */
    public int getPaperDurationInMinutes() {
        return paperDurationInMinutes;
    }

    /**
     * @param paperDurationInMinutes the paperDurationInMinutes to set
     */
    public void setPaperDurationInMinutes(int paperDurationInMinutes) {
        this.paperDurationInMinutes = paperDurationInMinutes;
    }

    /**
     * @return the subjectTitle
     */
    public String getSubjectTitle() {
        return subjectTitle;
    }

    /**
     * @param subjectTitle the subjectTitle to set
     */
    public void setSubjectTitle(String subjectTitle) {
        this.subjectTitle = subjectTitle;
    }

    /**
     * @return the subjectID
     */
    public long getSubjectID() {
        return subjectID;
    }

    /**
     * @param subjectID the subjectID to set
     */
    public void setSubjectID(long subjectID) {
        this.subjectID = subjectID;
    }

    /**
     * @return the courseTitle
     */
    public String getCourseTitle() {
        return courseTitle;
    }

    /**
     * @param courseTitle the courseTitle to set
     */
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    /**
     * @return the courseID
     */
    public long getCourseID() {
        return courseID;
    }

    /**
     * @param courseID the courseID to set
     */
    public void setCourseID(long courseID) {
        this.courseID = courseID;
    }

    /**
     * @return the paperDateTO
     */
    public java.util.Date getPaperDateTO() {
        return paperDateTO;
    }

    /**
     * @param paperDateTO the paperDateTO to set
     */
    public void setPaperDateTO(java.util.Date paperDateTO) {
        this.paperDateTO = paperDateTO;
    }

    /**
     * @return the numberOfQuestion
     */
    public int getNumberOfQuestion() {
        return numberOfQuestion;
    }

    /**
     * @param numberOfQuestion the numberOfQuestion to set
     */
    public void setNumberOfQuestion(int numberOfQuestion) {
        this.numberOfQuestion = numberOfQuestion;
    }

    /**
     * @return the paperStartTimeHour
     */
    public String getPaperStartTimeHour() {
        return paperStartTimeHour;
    }

    /**
     * @param paperStartTimeHour the paperStartTimeHour to set
     */
    public void setPaperStartTimeHour(String paperStartTimeHour) {
        this.paperStartTimeHour = paperStartTimeHour;
    }

    /**
     * @return the paperStartTimeMinute
     */
    public String getPaperStartTimeMinute() {
        return paperStartTimeMinute;
    }

    /**
     * @param paperStartTimeMinute the paperStartTimeMinute to set
     */
    public void setPaperStartTimeMinute(String paperStartTimeMinute) {
        this.paperStartTimeMinute = paperStartTimeMinute;
    }

    /**
     * @return the paperStartTimeMeridiem
     */
    public String getPaperStartTimeMeridiem() {
        return paperStartTimeMeridiem;
    }

    /**
     * @param paperStartTimeMeridiem the paperStartTimeMeridiem to set
     */
    public void setPaperStartTimeMeridiem(String paperStartTimeMeridiem) {
        this.paperStartTimeMeridiem = paperStartTimeMeridiem;
    }

    /**
     * @return the questionList
     */
    public List<ExamQuestionEntity> getQuestionList() {
        return questionList;
    }

    /**
     * @param questionList the questionList to set
     */
    public void setQuestionList(List<ExamQuestionEntity> questionList) {
        this.questionList = questionList;
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
