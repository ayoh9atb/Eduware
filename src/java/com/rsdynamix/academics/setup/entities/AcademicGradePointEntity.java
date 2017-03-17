/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.setup.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author p-aniah
 */
@Entity
@Table(name = "GRADE_POINT")
public class AcademicGradePointEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "GRADE_POINT_ID", columnDefinition = "NUMBER(10)")
    private long gradePointID;
    //
    @Column(name = "GRADE_CHAR", columnDefinition = "VARCHAR2(1)")
    private String gradeCharacter;
    //
    @Column(name = "GRADE_TITLE", columnDefinition = "VARCHAR2(25)")
    private String gradeTitle;
    //
    @Column(name = "LOWER_LIMIT", columnDefinition = "DOUBLE PRECISION")
    private BigDecimal lowerLimit;
    //
    @Column(name = "UPPER_LIMIT", columnDefinition = "DOUBLE PRECISION")
    private BigDecimal upperLimit;
    //
    @Column(name = "GRADE_POINT", columnDefinition = "NUMBER(1)")
    private int gradePoint;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public AcademicGradePointEntity() {
        gradePointID = 0;
        gradeCharacter = "";
        gradeTitle = "";
        lowerLimit = BigDecimal.ZERO;
        upperLimit = BigDecimal.ZERO;
        gradePoint = 0;
    }

    public boolean equals(Object o){
        boolean eqls = false;

        if(o instanceof AcademicGradePointEntity) {
            eqls = ((AcademicGradePointEntity)o).getGradeCharacter().equals(this.getGradeCharacter());
        }

        return eqls;
    }

    /**
     * @return the gradeCharacter
     */
    public String getGradeCharacter() {
        return gradeCharacter;
    }

    /**
     * @param gradeCharacter the gradeCharacter to set
     */
    public void setGradeCharacter(String gradeCharacter) {
        this.gradeCharacter = gradeCharacter;
    }

    /**
     * @return the gradeTitle
     */
    public String getGradeTitle() {
        return gradeTitle;
    }

    /**
     * @param gradeTitle the gradeTitle to set
     */
    public void setGradeTitle(String gradeTitle) {
        this.gradeTitle = gradeTitle;
    }

    /**
     * @return the lowerLimit
     */
    public BigDecimal getLowerLimit() {
        return lowerLimit;
    }

    /**
     * @param lowerLimit the lowerLimit to set
     */
    public void setLowerLimit(BigDecimal lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    /**
     * @return the upperLimit
     */
    public BigDecimal getUpperLimit() {
        return upperLimit;
    }

    /**
     * @param upperLimit the upperLimit to set
     */
    public void setUpperLimit(BigDecimal upperLimit) {
        this.upperLimit = upperLimit;
    }

    /**
     * @return the gradePoint
     */
    public int getGradePoint() {
        return gradePoint;
    }

    /**
     * @param gradePoint the gradePoint to set
     */
    public void setGradePoint(int gradePoint) {
        this.gradePoint = gradePoint;
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

    /**
     * @return the gradePointID
     */
    public long getGradePointID() {
        return gradePointID;
    }

    /**
     * @param gradePointID the gradePointID to set
     */
    public void setGradePointID(long gradePointID) {
        this.gradePointID = gradePointID;
    }
}
