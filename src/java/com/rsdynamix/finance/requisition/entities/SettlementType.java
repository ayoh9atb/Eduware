package com.rsdynamix.finance.requisition.entities;

/**
 *
 * @author patushie
 */
public enum SettlementType {

    NONE("None"),
    
    COMMISSION_PAYOUT("Commission Payout"),
    
    ANNUITY_PAYOUT("Annuity Payout"),
    
    ANNUITY_CLAIM_PAYOUT("Annuity Claim Payout"),
    
    ANNUITY_SURRENDER_PAYOUT("Annuity Surrender Payout"),
    
    EQUITY_FUND_PAYOUT("Equity Fund Payout"),
    
    MONEY_MARKET_FUND_PAYOUT("Money Market Fund Payout"),
    
    BOND_FUND_PAYOUT("Bond Fund Payout"),
    
    PROPERTY_FUND_PAYOUT("Property Fund Payout"),
    
    EQUITY_TRAN_PAYOUT("Equity Tran Payout"),
    
    MONEY_MARKET_TRAN_PAYOUT("Money Market Tran Payout"),
    
    BOND_TRAN_PAYOUT("Bond Tran Payout"),
    
    PROPERTY_TRAN_PAYOUT("Property Tran Payout"),
    
    PROCUREMENT_PAYOUT("Procurement Payout"),
    
    SALES_RETURN_PAYOUT("Sales Return Payout"),
    
    LOAN_PAYOUT ("Loan Payout"),
    
    COINSURANCE_PAYOUT("Coinsurance Payout"),
    
    REINSURANCE_PAYOUT("Reinsurance Payout");
    
    private String value;

    SettlementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    public static SettlementType getEnum(String value) {
        SettlementType enType = null;

        for (SettlementType v : values()) {
            if (v.getValue().equalsIgnoreCase(value)) {
                enType = v;
            } else {
                throw new IllegalArgumentException();
            }
        }

        return enType;
    }

}
