/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.finance.requisition.entities;

/**
 *
 * @author ushie
 */
public enum PayoutStatus {
    NONE(""),
    
    PAID("Paid"),
    
    UNPAID("Unpaid"),
    
    UPLOADED("Uploaded"),
    
    FAILED("Failed");

    private final String description;

    private PayoutStatus(String description) {
        this.description = description;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

}
