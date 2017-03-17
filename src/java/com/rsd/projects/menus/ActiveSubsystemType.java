/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsd.projects.menus;

/**
 *
 * @author patushie
 */
public enum ActiveSubsystemType {

    NONE("None"),
    
    CRM("CRM"),
    
    HRMS("HRMS"),
    
    EDUWARE("Eduware"),
    
    ACADEMIC_RECORDS("Academic Records"),
    
    LIBRARY_MANAGEMENT("Library Management"),
    
    HEALTH_INFO_SYSTEM("Health Information System"),
    
    CHURCH("Church"),
    
    INVENTORY("Inventory"),
    
    FIXED_ASSET("Fixed Asset"),
    
    PROCUREMENT("Procurement"),
    
    ACCOUNTING("Financials"),
    
    LIFE_INSURANCE("Life Insurance"),
    
    NONLIFE_INSURANCE("Nonlife Insurance"),
    
    ANNUITY_INSURANCE("Annuity Insurance"),
    
    HEALTH_INSURANCE("Health Insurance"),
    
    INVESTMENT("Investments");
    
    private String value;

    ActiveSubsystemType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    public static ActiveSubsystemType getEnum(String value) {
        ActiveSubsystemType enType = null;

        for (ActiveSubsystemType v : values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                enType = v;
            } else {
                throw new IllegalArgumentException();
            }
        }

        return enType;
    }

}
