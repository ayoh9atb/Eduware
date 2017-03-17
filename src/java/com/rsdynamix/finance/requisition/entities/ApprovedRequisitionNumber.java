/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.finance.requisition.entities;

import java.io.Serializable;

/**
 *
 * @author patushie
 */
public class ApprovedRequisitionNumber implements Serializable {

    private String requisitionNumber;

    public ApprovedRequisitionNumber(){
        requisitionNumber = "";
    }

    public boolean equals(Object o){
        return ((o instanceof ApprovedRequisitionNumber) && ((ApprovedRequisitionNumber)o).
                getRequisitionNumber().equalsIgnoreCase(this.getRequisitionNumber()));
    }

    /**
     * @return the requisitionNumber
     */
    public String getRequisitionNumber() {
        return requisitionNumber;
    }

    /**
     * @param requisitionNumber the requisitionNumber to set
     */
    public void setRequisitionNumber(String requisitionNumber) {
        this.requisitionNumber = requisitionNumber;
    }

}