/*
 * ChequeReport.java
 *
 * Created on August 6, 2009, 9:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rsdynamix.finance.requisition.entities;

import com.rsdynamix.bi.das.reports.handlers.ReportObject;

/**
 *
 * @author p-aniah
 */
public class ChequeReport extends ReportObject{
    
    private String fullName;
    private String amountInWords1;
    private String amountInWords2;
    private String amountInWords3;
    private String chequeDate;
    private String amountInFigure;
    private String chequeID;

    private String sideName1;
    private String sideName2;
    
    /** Creates a new instance of ChequeReport */
    public ChequeReport() {
        fullName = "";
        amountInWords1 = "";
        amountInWords2 = "";
        amountInWords3 = "";
        chequeDate = "";
        amountInFigure = "";

        sideName1 = "";
        sideName2 = "";

        setChequeID("");
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAmountInWords1() {
        return amountInWords1;
    }

    public void setAmountInWords1(String amountInWords1) {
        this.amountInWords1 = amountInWords1;
    }

    public String getAmountInWords2() {
        return amountInWords2;
    }

    public void setAmountInWords2(String amountInWords2) {
        this.amountInWords2 = amountInWords2;
    }

    public String getAmountInWords3() {
        return amountInWords3;
    }

    public void setAmountInWords3(String amountInWords3) {
        this.amountInWords3 = amountInWords3;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getAmountInFigure() {
        return amountInFigure;
    }

    public void setAmountInFigure(String amountInFigure) {
        this.amountInFigure = amountInFigure;
    }

    public String getChequeID() {
        return chequeID;
    }

    public void setChequeID(String chequeID) {
        this.chequeID = chequeID;
    }

    /**
     * @return the sideName1
     */
    public String getSideName1() {
        return sideName1;
    }

    /**
     * @param sideName1 the sideName1 to set
     */
    public void setSideName1(String sideName1) {
        this.sideName1 = sideName1;
    }

    /**
     * @return the sideName2
     */
    public String getSideName2() {
        return sideName2;
    }

    /**
     * @param sideName2 the sideName2 to set
     */
    public void setSideName2(String sideName2) {
        this.sideName2 = sideName2;
    }
    
}
