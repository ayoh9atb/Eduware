/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.setup.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author p-aniah
 */
@Entity
@Table(name = "CLASS_OF_DEGREE")
public class ClassOfDegreeEntity extends AbstractEntity implements Serializable {
    
    @Id
    @Column(name = "DEGREE_CLASS_ID", columnDefinition = "NUMBER(1)")
    private long classOfDegreeID;

    @Column(name = "DEGREE_CLASS_SHORT", columnDefinition = "VARCHAR2(2)")
    private String classOfDegreeCode;

    @Column(name = "DEGREE_CLASS_DESC", columnDefinition = "VARCHAR2(30)")
    private String classOfDegreeDesc;
    //
    @Column(name = "LOWER_GRADE_POINT", columnDefinition = "DOUBLE PRECISION")
    private double lowerGradePoint;
    //
    @Column(name = "UPPER_GRADE_POINT", columnDefinition = "DOUBLE PRECISION")
    private double upperGradePoint;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public ClassOfDegreeEntity() {
        classOfDegreeID = 0;
        classOfDegreeCode = "";
        classOfDegreeDesc = "";
        lowerGradePoint = 0;
        upperGradePoint = 0;
    }

    /**
     * @return the classOfDegreeID
     */
    public long getClassOfDegreeID() {
        return classOfDegreeID;
    }

    /**
     * @param classOfDegreeID the classOfDegreeID to set
     */
    public void setClassOfDegreeID(long classOfDegreeID) {
        this.classOfDegreeID = classOfDegreeID;
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

    /**
     * @return the lowerGradePoint
     */
    public double getLowerGradePoint() {
        return lowerGradePoint;
    }

    /**
     * @param lowerGradePoint the lowerGradePoint to set
     */
    public void setLowerGradePoint(double lowerGradePoint) {
        this.lowerGradePoint = lowerGradePoint;
    }

    /**
     * @return the upperGradePoint
     */
    public double getUpperGradePoint() {
        return upperGradePoint;
    }

    /**
     * @param upperGradePoint the upperGradePoint to set
     */
    public void setUpperGradePoint(double upperGradePoint) {
        this.upperGradePoint = upperGradePoint;
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
