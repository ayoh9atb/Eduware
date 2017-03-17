/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.projects.security.bean;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamics.finance.collections.beans.PaymentCollectionBean;
import com.rsdynamics.finance.collections.beans.BusinessModuleBean;
import com.rsdynamics.finance.gledger.beans.AccountingPeriodBean;
import com.rsdynamics.finance.gledger.beans.BalanceSheetReportBean;
import com.rsdynamics.finance.gledger.beans.FinancialYearBean;
import com.rsdynamics.finance.gledger.beans.GLAccountChartBean;
import com.rsdynamics.finance.gledger.beans.GLAccountGroupBean;
import com.rsdynamics.finance.gledger.beans.GLAccountSectionBean;
import com.rsdynamics.finance.gledger.beans.GLJournalEntryBean;
import com.rsdynamics.finance.gledger.beans.GLTransactionSourceBean;
import com.rsdynamics.finance.gledger.beans.GLTransactionTypeBean;
import com.rsdynamics.finance.gledger.beans.IncomeStmtReportBean;
import com.rsdynamics.finance.gledger.beans.ProfitAndLossReportBean;
import com.rsdynamics.finance.gledger.beans.TrialBalanceReportBean;
import com.rsdynamix.crm.beans.AgencyBean;
import com.rsdynamix.crm.beans.AgencyTypeBean;
import com.rsdynamix.crm.beans.ClientBean;
import com.rsdynamix.crm.beans.IdentityTypeBean;
import com.rsdynamix.crm.beans.ProofOfAddressTypeBean;
import com.rsdynamix.crm.beans.ReferrerTypeBean;
import com.rsdynamix.dynamo.common.setup.beans.AddressManagerBean;
import com.rsdynamix.dynamo.common.setup.beans.BankManagerBean;
import com.rsdynamix.dynamo.common.setup.beans.BranchBean;
import com.rsdynamix.dynamo.common.setup.beans.BusinessDivisionBean;
import com.rsdynamix.dynamo.common.setup.beans.CollectionModeBean;
import com.rsdynamix.dynamo.common.setup.beans.CollectionSourceBean;
import com.rsdynamix.dynamo.common.setup.beans.CurrencyBean;
import com.rsdynamix.dynamo.common.setup.beans.DocumentManagerBean;
import com.rsdynamix.dynamo.common.setup.beans.EmployerBean;
import com.rsdynamix.dynamo.common.setup.beans.ReferrerBean;
import com.rsdynamix.dynamo.common.setup.beans.RelationshipBean;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.finance.requisition.beans.SettlementBean;
import com.rsdynamix.finance.sales.beans.SalesDiscountBean;
import com.rsdynamix.finance.sales.beans.TaxAuthorityBean;
import com.rsdynamix.finance.sales.beans.TaxBean;
import com.rsdynamix.hrms.commons.setup.beans.DepartmentBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.projects.commons.messages.beans.MessageSetupBean;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author  S3-Systems-NG. 010
 */
@RequestScoped
@ManagedBean(name = "sessionDataReinitBean")
public class SessionDataReinitBean {

    public SessionDataReinitBean() {
    }

    public String navigateHome() {
        reinitSessionData();
        return "/home.jsf";
    }

    public String reinitSessionData() {
        String outcome = "";//MenuManagerBean.performSystemStateResetForButtonClick("");

//        if (!outcome.trim().equals(UserManagerBean.LOGIN_PAGE_URL)) {
        HttpServletRequest httpReq = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = httpReq.getSession();
        
        //reinit client session data..
        ClientBean clientBean = (ClientBean) session.getAttribute("clientBean");
        if (clientBean != null) {
            clientBean.clearCache();
        }
        
        //reinit claim session data...
        AgencyBean agencyBean = (AgencyBean) session.getAttribute("agencyBean");
        if (agencyBean != null) {
            agencyBean.clearCache();
        }
        //
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) session.getAttribute("applicationMessageBean");
        if (applicationMessageBean != null) {
            applicationMessageBean.insertMessage("", MessageType.NONE);
        }
        
        //reinit surrender session data...
        AddressManagerBean addressManagerBean = (AddressManagerBean) session.getAttribute("addressManagerBean");
        if (addressManagerBean != null) {
            addressManagerBean.clearCache();
        }
        //reinit surrender session data...
        BranchBean branchBean = (BranchBean) session.getAttribute("branchBean");
        if (branchBean != null) {
            branchBean.clearCache();
        }
        //reinit surrender session data...
        BusinessDivisionBean businessDivisionBean = (BusinessDivisionBean) session.getAttribute("businessDivisionBean");
        if (businessDivisionBean != null) {
            businessDivisionBean.clearCache();
        }
        //reinit surrender session data...
        DepartmentBean departmentBean = (DepartmentBean) session.getAttribute("departmentBean");
        if (departmentBean != null) {
            departmentBean.clearCache();
        }
        //reinit surrender session data...
        RelationshipBean relationshipBean = (RelationshipBean) session.getAttribute("relationshipBean");
        if (relationshipBean != null) {
            relationshipBean.clearCache();
        }
        //reinit surrender session data...
        CurrencyBean currencyBean = (CurrencyBean) session.getAttribute("currencyBean");
        if (currencyBean != null) {
            currencyBean.clearCache();
        }

        EmployerBean employerBean = (EmployerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.employerBean}", EmployerBean.class);
        if (employerBean == null) {
            employerBean = new EmployerBean();
            CommonBean.setBeanToContext("#{sessionScope.employerBean}",
                    EmployerBean.class, employerBean);
        }
        employerBean.clearCache();

        ReferrerBean referrerBean = (ReferrerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.referrerBean}", ReferrerBean.class);
        if (referrerBean == null) {
            referrerBean = new ReferrerBean();
            CommonBean.setBeanToContext("#{sessionScope.referrerBean}",
                    ReferrerBean.class, referrerBean);
        }
        referrerBean.clearCache();
        //reinit surrender session data...
        BankManagerBean bankManagerBean = (BankManagerBean) session.getAttribute("bankManagerBean");
        if (bankManagerBean != null) {
            bankManagerBean.clearCache();
        }
        
        //reinit surrender session data...
        GLTransactionSourceBean glTransactionSourceBean = (GLTransactionSourceBean) session.getAttribute("glTransactionSourceBean");
        if (glTransactionSourceBean != null) {
            glTransactionSourceBean.clearCache();
        }
        //reinit surrender session data...
        FinancialYearBean financialYearBean = (FinancialYearBean) session.getAttribute("financialYearBean");
        if (financialYearBean != null) {
            financialYearBean.clearCache();
        }
        //reinit surrender session data...
        AccountingPeriodBean accountingPeriodBean = (AccountingPeriodBean) session.getAttribute("accountingPeriodBean");
        if (accountingPeriodBean != null) {
            accountingPeriodBean.clearCache();
        }
        //reinit surrender session data...
        GLTransactionTypeBean glTransactionTypeBean = (GLTransactionTypeBean) session.getAttribute("glTransactionTypeBean");
        if (glTransactionTypeBean != null) {
            glTransactionTypeBean.clearCache();
        }
        //reinit surrender session data...
        GLJournalEntryBean glJournalEntryBean = (GLJournalEntryBean) session.getAttribute("glJournalEntryBean");
        if (glJournalEntryBean != null) {
            glJournalEntryBean.clearCache();
        }
        //reinit surrender session data...
        BalanceSheetReportBean balanceSheetReportBean = (BalanceSheetReportBean) session.getAttribute("balanceSheetReportBean");
        if (balanceSheetReportBean != null) {
            balanceSheetReportBean.clearCache();
        }
        //reinit surrender session data...
        TrialBalanceReportBean trialBalanceReportBean = (TrialBalanceReportBean) session.getAttribute("trialBalanceReportBean");
        if (trialBalanceReportBean != null) {
            trialBalanceReportBean.clearCache();
        }
        //reinit surrender session data...
        ProfitAndLossReportBean profitAndLossReportBean = (ProfitAndLossReportBean) session.getAttribute("profitAndLossReportBean");
        if (profitAndLossReportBean != null) {
            profitAndLossReportBean.clearCache();
        }
        //reinit surrender session data...
        IncomeStmtReportBean incomeStmtReportBean = (IncomeStmtReportBean) session.getAttribute("incomeStmtReportBean");
        if (incomeStmtReportBean != null) {
            incomeStmtReportBean.clearCache();
        }
        //reinit surrender session data...
        IdentityTypeBean identityTypeBean = (IdentityTypeBean) session.getAttribute("identityTypeBean");
        if (identityTypeBean != null) {
            identityTypeBean.clearCache();
        }
        //reinit surrender session data...
        ProofOfAddressTypeBean proofOfAddressTypeBean = (ProofOfAddressTypeBean) session.getAttribute("proofOfAddressTypeBean");
        if (proofOfAddressTypeBean != null) {
            proofOfAddressTypeBean.clearCache();
        }
        //reinit surrender session data...
        ReferrerTypeBean referrerTypeBean = (ReferrerTypeBean) session.getAttribute("referrerTypeBean");
        if (referrerTypeBean != null) {
            referrerTypeBean.clearCache();
        }
        //reinit surrender session data...
        AgencyTypeBean agencyTypeBean = (AgencyTypeBean) session.getAttribute("agencyTypeBean");
        if (agencyTypeBean != null) {
            agencyTypeBean.clearCache();
        }
        
        //reinit surrender session data...
        TaxAuthorityBean taxAuthorityBean = (TaxAuthorityBean) session.getAttribute("taxAuthorityBean");
        if (taxAuthorityBean != null) {
            taxAuthorityBean.clearCache();
        }
        
        //reinit surrender session data...
        TaxBean taxBean = (TaxBean) session.getAttribute("taxBean");
        if (taxBean != null) {
            taxBean.clearCache();
        }
        //reinit surrender session data...
        SalesDiscountBean salesDiscountBean = (SalesDiscountBean) session.getAttribute("salesDiscountBean");
        if (salesDiscountBean != null) {
            salesDiscountBean.clearCache();
        }
        //reinit surrender session data...
        BusinessModuleBean businessModuleBean = (BusinessModuleBean) session.getAttribute("businessModuleBean");
        if (businessModuleBean != null) {
            businessModuleBean.clearCache();
        }
        //reinit surrender session data...
        CollectionModeBean collectionModeBean = (CollectionModeBean) session.getAttribute("collectionModeBean");
        if (collectionModeBean != null) {
            collectionModeBean.clearCache();
        }
        //reinit surrender session data...
        CollectionSourceBean collectionSourceBean = (CollectionSourceBean) session.getAttribute("collectionSourceBean");
        if (collectionSourceBean != null) {
            collectionSourceBean.clearCache();
        }
        //reinit surrender session data...
        GLAccountGroupBean glAccountGroupBean = (GLAccountGroupBean) session.getAttribute("glAccountGroupBean");
        if (glAccountGroupBean != null) {
            glAccountGroupBean.clearCache();
        }
        //reinit surrender session data...
        GLAccountChartBean glAccountChartBean = (GLAccountChartBean) session.getAttribute("glAccountChartBean");
        if (glAccountChartBean != null) {
            glAccountChartBean.clearCache();
        }
        //reinit surrender session data...
        MessageSetupBean messageSetupBean = (MessageSetupBean) session.getAttribute("messageSetupBean");
        if (messageSetupBean != null) {
            messageSetupBean.clearCache();
        }
        //reinit surrender session data...
        UserManagerBean userManagerBean = (UserManagerBean) session.getAttribute("userManagerBean");
        if (userManagerBean != null) {
            userManagerBean.loadRequisitionOfUser();
        }
        //reinit surrender session data...
        DocumentManagerBean documentManagerBean = (DocumentManagerBean) session.getAttribute("documentManagerBean");
        if (documentManagerBean != null) {
            documentManagerBean.clearCache();
        }

        SettlementBean settlementBean = (SettlementBean) session.getAttribute("settlementBean");
        if (settlementBean != null) {
            settlementBean.clearCache();
        }

//        }
        return outcome;
    }

}
