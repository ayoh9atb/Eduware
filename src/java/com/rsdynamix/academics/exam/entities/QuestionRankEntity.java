/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.exam.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author root
 */
@Entity
@Table(name = "QUESTION_RANK")
public class QuestionRankEntity extends AbstractEntity implements Serializable {
    
    @Id
    @Column(name = "QUESTION_RANK_ID", columnDefinition = "NUMBER(2)")
    private int questionRankID;
    //
    @Column(name = "QUESTION_RANK_DESC", columnDefinition = "VARCHAR2(50)")
    private String questionRankDesc;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //

    public QuestionRankEntity(){
        questionRankID = 0;
        questionRankDesc = "";
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof QuestionRankEntity) {
            if((((QuestionRankEntity)o).getQuestionRankID() > 0) 
                    && (((QuestionRankEntity)o).getQuestionRankID() == this.getQuestionRankID())){
                eqls = true;
            } else {
                eqls = ((QuestionRankEntity)o).getQuestionRankDesc().equals(this.getQuestionRankDesc());
            }
        }

        return eqls;
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
