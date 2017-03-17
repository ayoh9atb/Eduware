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
@Table(name = "TEST_TYPE")
public class TestTypeEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "TEST_TYPE_ID", columnDefinition = "NUMBER(1)")
    private int testTypeID;
    //
    @Column(name = "TEST_TYPE_DESC", columnDefinition = "VARCHAR2(25)")
    private String testTypeDesc;
    //
    @Column(name = "POINT_PERCENT", columnDefinition = "DOUBLE PRECISION")
    private double pointPercent;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();

    public TestTypeEntity() {
        testTypeID = 0;
        testTypeDesc = "";
        pointPercent = 0;
    }

    /**
     * @return the testTypeID
     */
    public int getTestTypeID() {
        return testTypeID;
    }

    /**
     * @param testTypeID the testTypeID to set
     */
    public void setTestTypeID(int testTypeID) {
        this.testTypeID = testTypeID;
    }

    /**
     * @return the testTypeDesc
     */
    public String getTestTypeDesc() {
        return testTypeDesc;
    }

    /**
     * @param testTypeDesc the testTypeDesc to set
     */
    public void setTestTypeDesc(String testTypeDesc) {
        this.testTypeDesc = testTypeDesc;
    }

    /**
     * @return the pointPercent
     */
    public double getPointPercent() {
        return pointPercent;
    }

    /**
     * @param pointPercent the pointPercent to set
     */
    public void setPointPercent(double pointPercent) {
        this.pointPercent = pointPercent;
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
