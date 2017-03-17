/*
 * ActionAuthenticationBean.java
 *
 * Created on July 30, 2009, 4:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rsdynamix.projects.security.bean;

import com.rsdynamix.projects.security.entities.UserAccountEntity;
import com.rsdynamix.projects.security.entities.UserPrivilegeEntity;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlOutputLink;
import javax.faces.component.html.HtmlOutputText;

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "authenticationBean")
public class ActionAuthenticationBean {

    private HtmlOutputLink reportConfigurerLink;
    private HtmlOutputLink chartConfigurerLink;
    private HtmlOutputLink reportTriggersLink;
    private HtmlOutputLink reportSchedulersLink;
    private HtmlOutputLink userReportConfigurerLink;
    private HtmlOutputLink userChartConfigurerLink;
    //Reports Expressions
    private HtmlOutputLink expressionFunctionLink;
    private HtmlOutputLink functionTestLink;
    //Reports Setup
    private HtmlOutputLink departmentSetupLink;
    private HtmlOutputLink reportCategoriesLink;
    private HtmlOutputLink queryFieldLink;
    private HtmlOutputLink reportFieldsLink;
    private HtmlOutputLink innerChartLink;
    private HtmlOutputLink reportParamLink;
    private HtmlOutputLink subreportQueryFieldLink;
    private HtmlOutputLink subreportFieldsLink;
    private HtmlOutputLink subreportParamLink;
    private HtmlOutputLink createRoleLink;
    private HtmlOutputLink createUserLink;
    private HtmlOutputLink changePasswordLink;
    //
    private HtmlOutputLink emailSettingsLink;
    private HtmlOutputLink recepientLink;
    private HtmlOutputLink miscAttributeLink;
    private HtmlOutputLink stockBatchLink;
    private HtmlOutputLink porderItemLink;
    private HtmlOutputLink stockItemUnitLink;
    //
    private HtmlOutputLink claimsAckLetterReportLink;
    private HtmlOutputLink purchaseOrdertemLink;
    private HtmlOutputLink clientContactPersonLink;
    private HtmlOutputLink clientSearchLink;
    private HtmlOutputLink stockItemUnitForSalesLink;
    //
    private HtmlOutputLink academicProfileLink;
    private HtmlOutputLink projectActivitiesLink;
    private HtmlOutputLink projectToolsLink;
    private HtmlOutputLink cvSearchLink;
    private HtmlOutputLink deptResponsibilityLink;
    private HtmlOutputLink salaryGradeLink;
    private HtmlOutputLink salaryStepLink;
    private HtmlOutputLink recruitmentLink;
    private HtmlOutputLink empResponsibilityLink;
    private HtmlOutputLink medicalRecordLink;
    private HtmlOutputLink empPayChangeLink;
    private HtmlOutputLink salaryAdjusterLink;
    //
    private HtmlOutputLink menuLink;
    private HtmlOutputLink roleLink;
    private HtmlOutputLink subsystemLink;
    private HtmlOutputLink userTypeSearchLink;

    /** Creates a new instance of ActionAuthenticationBean */
    public ActionAuthenticationBean() {
        authenticateUser();
    }

    public void authenticateUser() {
        UserManagerBean userBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userBean}", UserManagerBean.class);
        if (userBean == null) {
            userBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userBean}", UserManagerBean.class, userBean);
        }
        boolean hasUsers = true;

        setMenuLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('menuPanel')",
                "Add/Remove Menu", null, true, 0));

        setRoleLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('rolePanel')",
                "Add/Remove Role", null, true, 0));

        setSubsystemLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('subsystemPanel')",
                "Add/Remove Subsystem", null, true, 0));
        //
        setClaimsAckLetterReportLink(getHtmlOutputLink("../reports/underwriting/motorreports/claims_ack_letter.jsf",
                "Claims Acknowledgement Letter<br><br>", null, hasUsers));
        //
        setReportConfigurerLink(getHtmlOutputLink("../reports/reportconfigurer.jsf",
                "Generic Reports<br><br>", null, hasUsers));

        setChartConfigurerLink(getHtmlOutputLink("../reports/chartconfigurer.jsf",
                "Generic Charts<br><br>", null, hasUsers));

        setUserReportConfigurerLink(getHtmlOutputLink("../reports/userreportconfigurer.jsf",
                "Generic Reports<br><br>", null, hasUsers));

        setUserChartConfigurerLink(getHtmlOutputLink("../reports/userchartconfigurer.jsf",
                "Generic Charts<br><br>", null, hasUsers));

        setReportTriggersLink(getHtmlOutputLink("../reports/reporttriggersetup.jsf",
                "Report Triggers Setup<br><br>", null, hasUsers));

        setReportSchedulersLink(getHtmlOutputLink("../reports/reportschedulesetup.jsf",
                "Report Schedule Setup<br><br>", null, hasUsers));

        setExpressionFunctionLink(getHtmlOutputLink("../common/expressionfunction.jsf",
                "Expression Functions<br><br>", null, hasUsers));

        setDepartmentSetupLink(getHtmlOutputLink("../setup/departmentsetup.jsf",
                "Department Setup<br><br>", null, hasUsers));

        setReportCategoriesLink(getHtmlOutputLink("../setup/reportcategorysetup.jsf",
                "Report Category Setup<br><br>", null, hasUsers));

        setFunctionTestLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('functionTestPanel')",
                "Function Test", null, true, 0));

        setQueryFieldLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('queryFieldPanel')",
                "Query Fields", null, true, 0));

        setReportFieldsLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('reportFieldPanel')",
                "Report Fields", null, true, 0));

        setInnerChartLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('innerChartPanel')",
                "Inner Chart", null, true, 0));

        setReportParamLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('reportparamsetupPanel')",
                "Report Parameters", null, true, 0));

        setSubreportQueryFieldLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('subreportQueryFieldPanel')",
                "Query Fields", null, true, 0));

        setSubreportFieldsLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('subreportFieldPanel')",
                "Subreport Fields", null, true, 0));

        setSubreportParamLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('subreportparamsetupPanel')",
                "Subreport Parameters", null, true, 0));

        setCreateRoleLink(getHtmlOutputLink("../security/roles.jsf",
                "Create Roles<br><br>", null, hasUsers));

        setCreateUserLink(getHtmlOutputLink("../security/createuser.jsf",
                "Create Users<br><br>", null, hasUsers));

        setChangePasswordLink(getHtmlOutputLink("../security/changepassword.jsf",
                "Change Password<br><br>", null, hasUsers));

        setPurchaseOrdertemLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('pOrderItemPanel')",
                "Order Items", null, true, 0));

        setMiscAttributeLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('miscAttribPanel')",
                "<b>Miscellaneous Attributes</b>", null, true, 0));

        setClientContactPersonLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('clientContactSetupPanel')",
                "<b>Client Contact</b>", null, true, 0));

        setClientSearchLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('clientSearchPanel')",
                "<b>Search Client</b>", null, true, 0));

        setStockBatchLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('stockBatchPanel')",
                "<b>Stock Batches</b>", null, true, 0));

        setPorderItemLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('porderitemPanel')",
                "<b>Purchase Order Items</b>", null, true, 0));

        setStockItemUnitLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('stockItemUnitPanel')",
                "<b>Stock Transfers</b>", null, true, 0));

        setStockItemUnitForSalesLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('stockItemUnitForSalesPanel')",
                "<b>Stock Sales</b>", null, true, 0));

        setAcademicProfileLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('acadaprofilePanel')",
                "Academic Profile", null, true, 0));

        setProjectActivitiesLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('projectactivitiesPanel')",
                "Project Activities", null, true, 0));

        setProjectToolsLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('projecttoolsPanel')",
                "Project Tools", null, true, 0));

        setCvSearchLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('cvSearchPanel')",
                "Resume Search", null, true, 0));

        setDeptResponsibilityLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('deptResponsibilityPanel')",
                "Department Responsibility", null, true, 0));

        setSalaryGradeLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('salarygradePanel')",
                "Rank/Grade", null, true, 0));

        setSalaryStepLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('salarystepPanel')",
                "Grade Step", null, true, 0));

        setRecruitmentLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('recruitmentPanel')",
                "Recruitment", null, true, 0));

        setEmpResponsibilityLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('empResponsibilityPanel')",
                "Employee Responsibilities", null, true, 0));

        setMedicalRecordLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('medicalRecordPanel')",
                "Medical Records", null, true, 0));

        setEmpPayChangeLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('empPayChangePanel')",
                "Employee Pay Adjustments", null, true, 0));

        setSalaryAdjusterLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('salaryAdjustmentPanel')",
                "Allowances/Deductions", null, true, 0));

        setEmailSettingsLink(getHtmlOutputLink("../common/mailing/mailsetup.jsf",
                "Email Settings<br><br>", userBean.getUserAccount(), false));
        setRecepientLink(getHtmlOutputLink("javascript://", "Recepient Setup", null));
        getRecepientLink().setOnclick("javascript:Richfaces.showModalPanel('recepientSetupPanel')");

        setUserTypeSearchLink(getHtmlOutputLink(
                "javascript:Richfaces.showModalPanel('staffSearchForUserCreatePanel')",
                "Employee Search", null, true, 0));
    }

    public static HtmlOutputLink getHtmlOutputLink(String linkValue, String labelValue, UserAccountEntity userAccount) {
        HtmlOutputLink link = new HtmlOutputLink();
        link.setValue(linkValue);
        link.setTarget("mainFrame");

        HtmlOutputText linkLabel = new HtmlOutputText();
        linkLabel.setEscape(false);
        linkLabel.setStyle("font-family : Verdana,Arial,Helvetica,sans-serif; font-size : 11px;");
        linkLabel.setValue(labelValue);
        link.getChildren().add(linkLabel);

        if (userAccount != null) {
            link.setDisabled(!hasPrivilege(userAccount, labelValue));
        }

        return link;
    }

    public static HtmlOutputLink getHtmlOutputLink(String linkValue, String labelValue, UserAccountEntity userAccount, boolean hasUsers) {
        HtmlOutputLink link = new HtmlOutputLink();
        link.setValue(linkValue);
        link.setTarget("mainFrame");

        HtmlOutputText linkLabel = new HtmlOutputText();
        linkLabel.setEscape(false);
        linkLabel.setStyle("font-family : Verdana,Arial,Helvetica,sans-serif; font-size : 11px;");
        linkLabel.setValue(labelValue);
        link.getChildren().add(linkLabel);

        if (hasUsers) {
            if (userAccount != null) {
                link.setDisabled(!hasPrivilege(userAccount, labelValue));
            } else {
                link.setDisabled(false);
            }
        } else {
            link.setDisabled(false);
        }

        return link;
    }

    public static HtmlOutputLink getHtmlOutputLink(String linkValue, String labelValue,
            UserAccountEntity userAccount, boolean isModalLink, long menuID) {
        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        HtmlOutputLink link = new HtmlOutputLink();
        //link.setTarget("mainFrame");

        if (isModalLink) {
            link.setOnclick(linkValue);
            link.setValue("javascript://");
        } else {
            link.setValue(linkValue);
        }

        HtmlOutputText linkLabel = new HtmlOutputText();
        linkLabel.setEscape(false);
        linkLabel.setStyle("font-family : Verdana,Arial,Helvetica,sans-serif; font-size : 14px;");

        if (!isModalLink) {
            if (!labelValue.contains("<br><br>")) {
                labelValue += "<br><br>";
            }
        }
        linkLabel.setValue(labelValue);

        link.getChildren().add(linkLabel);

        if (userAccount != null) {
            link.setDisabled(!hasPrivilege(userAccount, labelValue));
        }

        return link;
    }

    private static boolean hasPrivilege(UserAccountEntity userAccount, String labelValue) {
        if (labelValue.contains("<br><br>")) {
            labelValue = labelValue.substring(0, labelValue.indexOf("<br><br>")).trim();
        }
        UserPrivilegeEntity privilege = userAccount.findUserPrivilegeByName(labelValue);

        return (privilege != null);
    }

    private static boolean hasPrivilege(UserAccountEntity userAccount, String labelValue, boolean hasUsers) {
        return (hasUsers && hasPrivilege(userAccount, labelValue));
    }

    /**
     * @return the createRoleLink
     */
    public HtmlOutputLink getCreateRoleLink() {
        return createRoleLink;
    }

    /**
     * @param createRoleLink the createRoleLink to set
     */
    public void setCreateRoleLink(HtmlOutputLink createRoleLink) {
        this.createRoleLink = createRoleLink;
    }

    /**
     * @return the createUserLink
     */
    public HtmlOutputLink getCreateUserLink() {
        return createUserLink;
    }

    /**
     * @param createUserLink the createUserLink to set
     */
    public void setCreateUserLink(HtmlOutputLink createUserLink) {
        this.createUserLink = createUserLink;
    }

    /**
     * @return the changePasswordLink
     */
    public HtmlOutputLink getChangePasswordLink() {
        return changePasswordLink;
    }

    /**
     * @param changePasswordLink the changePasswordLink to set
     */
    public void setChangePasswordLink(HtmlOutputLink changePasswordLink) {
        this.changePasswordLink = changePasswordLink;
    }

    /**
     * @return the emailSettingsLink
     */
    public HtmlOutputLink getEmailSettingsLink() {
        return emailSettingsLink;
    }

    /**
     * @param emailSettingsLink the emailSettingsLink to set
     */
    public void setEmailSettingsLink(HtmlOutputLink emailSettingsLink) {
        this.emailSettingsLink = emailSettingsLink;
    }

    /**
     * @return the recepientLink
     */
    public HtmlOutputLink getRecepientLink() {
        return recepientLink;
    }

    /**
     * @param recepientLink the recepientLink to set
     */
    public void setRecepientLink(HtmlOutputLink recepientLink) {
        this.recepientLink = recepientLink;
    }

    /**
     * @return the reportConfigurerLink
     */
    public HtmlOutputLink getReportConfigurerLink() {
        return reportConfigurerLink;
    }

    /**
     * @param reportConfigurerLink the reportConfigurerLink to set
     */
    public void setReportConfigurerLink(HtmlOutputLink reportConfigurerLink) {
        this.reportConfigurerLink = reportConfigurerLink;
    }

    /**
     * @return the chartConfigurerLink
     */
    public HtmlOutputLink getChartConfigurerLink() {
        return chartConfigurerLink;
    }

    /**
     * @param chartConfigurerLink the chartConfigurerLink to set
     */
    public void setChartConfigurerLink(HtmlOutputLink chartConfigurerLink) {
        this.chartConfigurerLink = chartConfigurerLink;
    }

    /**
     * @return the queryFieldLink
     */
    public HtmlOutputLink getQueryFieldLink() {
        return queryFieldLink;
    }

    /**
     * @param queryFieldLink the queryFieldLink to set
     */
    public void setQueryFieldLink(HtmlOutputLink queryFieldLink) {
        this.queryFieldLink = queryFieldLink;
    }

    /**
     * @return the reportFieldsLink
     */
    public HtmlOutputLink getReportFieldsLink() {
        return reportFieldsLink;
    }

    /**
     * @param reportFieldsLink the reportFieldsLink to set
     */
    public void setReportFieldsLink(HtmlOutputLink reportFieldsLink) {
        this.reportFieldsLink = reportFieldsLink;
    }

    /**
     * @return the innerChartLink
     */
    public HtmlOutputLink getInnerChartLink() {
        return innerChartLink;
    }

    /**
     * @param innerChartLink the innerChartLink to set
     */
    public void setInnerChartLink(HtmlOutputLink innerChartLink) {
        this.innerChartLink = innerChartLink;
    }

    /**
     * @return the reportParamLink
     */
    public HtmlOutputLink getReportParamLink() {
        return reportParamLink;
    }

    /**
     * @param reportParamLink the reportParamLink to set
     */
    public void setReportParamLink(HtmlOutputLink reportParamLink) {
        this.reportParamLink = reportParamLink;
    }

    /**
     * @return the reportTriggersLink
     */
    public HtmlOutputLink getReportTriggersLink() {
        return reportTriggersLink;
    }

    /**
     * @param reportTriggersLink the reportTriggersLink to set
     */
    public void setReportTriggersLink(HtmlOutputLink reportTriggersLink) {
        this.reportTriggersLink = reportTriggersLink;
    }

    /**
     * @return the reportSchedulersLink
     */
    public HtmlOutputLink getReportSchedulersLink() {
        return reportSchedulersLink;
    }

    /**
     * @param reportSchedulersLink the reportSchedulersLink to set
     */
    public void setReportSchedulersLink(HtmlOutputLink reportSchedulersLink) {
        this.reportSchedulersLink = reportSchedulersLink;
    }

    /**
     * @return the claimsAckLetterReportLink
     */
    public HtmlOutputLink getClaimsAckLetterReportLink() {
        return claimsAckLetterReportLink;
    }

    /**
     * @param claimsAckLetterReportLink the claimsAckLetterReportLink to set
     */
    public void setClaimsAckLetterReportLink(HtmlOutputLink claimsAckLetterReportLink) {
        this.claimsAckLetterReportLink = claimsAckLetterReportLink;
    }

    /**
     * @return the menuLink
     */
    public HtmlOutputLink getMenuLink() {
        return menuLink;
    }

    /**
     * @param menuLink the menuLink to set
     */
    public void setMenuLink(HtmlOutputLink menuLink) {
        this.menuLink = menuLink;
    }

    /**
     * @return the roleLink
     */
    public HtmlOutputLink getRoleLink() {
        return roleLink;
    }

    /**
     * @param roleLink the roleLink to set
     */
    public void setRoleLink(HtmlOutputLink roleLink) {
        this.roleLink = roleLink;
    }

    /**
     * @return the subsystemLink
     */
    public HtmlOutputLink getSubsystemLink() {
        return subsystemLink;
    }

    /**
     * @param subsystemLink the subsystemLink to set
     */
    public void setSubsystemLink(HtmlOutputLink subsystemLink) {
        this.subsystemLink = subsystemLink;
    }

    /**
     * @return the subreportQueryFieldLink
     */
    public HtmlOutputLink getSubreportQueryFieldLink() {
        return subreportQueryFieldLink;
    }

    /**
     * @param subreportQueryFieldLink the subreportQueryFieldLink to set
     */
    public void setSubreportQueryFieldLink(HtmlOutputLink subreportQueryFieldLink) {
        this.subreportQueryFieldLink = subreportQueryFieldLink;
    }

    /**
     * @return the subreportFieldsLink
     */
    public HtmlOutputLink getSubreportFieldsLink() {
        return subreportFieldsLink;
    }

    /**
     * @param subreportFieldsLink the subreportFieldsLink to set
     */
    public void setSubreportFieldsLink(HtmlOutputLink subreportFieldsLink) {
        this.subreportFieldsLink = subreportFieldsLink;
    }

    /**
     * @return the subreportParamLink
     */
    public HtmlOutputLink getSubreportParamLink() {
        return subreportParamLink;
    }

    /**
     * @param subreportParamLink the subreportParamLink to set
     */
    public void setSubreportParamLink(HtmlOutputLink subreportParamLink) {
        this.subreportParamLink = subreportParamLink;
    }

    /**
     * @return the expressionFunctionLink
     */
    public HtmlOutputLink getExpressionFunctionLink() {
        return expressionFunctionLink;
    }

    /**
     * @param expressionFunctionLink the expressionFunctionLink to set
     */
    public void setExpressionFunctionLink(HtmlOutputLink expressionFunctionLink) {
        this.expressionFunctionLink = expressionFunctionLink;
    }

    /**
     * @return the functionTestLink
     */
    public HtmlOutputLink getFunctionTestLink() {
        return functionTestLink;
    }

    /**
     * @param functionTestLink the functionTestLink to set
     */
    public void setFunctionTestLink(HtmlOutputLink functionTestLink) {
        this.functionTestLink = functionTestLink;
    }

    /**
     * @return the departmentSetupLink
     */
    public HtmlOutputLink getDepartmentSetupLink() {
        return departmentSetupLink;
    }

    /**
     * @param departmentSetupLink the departmentSetupLink to set
     */
    public void setDepartmentSetupLink(HtmlOutputLink departmentSetupLink) {
        this.departmentSetupLink = departmentSetupLink;
    }

    /**
     * @return the reportCategoriesLink
     */
    public HtmlOutputLink getReportCategoriesLink() {
        return reportCategoriesLink;
    }

    /**
     * @param reportCategoriesLink the reportCategoriesLink to set
     */
    public void setReportCategoriesLink(HtmlOutputLink reportCategoriesLink) {
        this.reportCategoriesLink = reportCategoriesLink;
    }

    /**
     * @return the userReportConfigurerLink
     */
    public HtmlOutputLink getUserReportConfigurerLink() {
        return userReportConfigurerLink;
    }

    /**
     * @param userReportConfigurerLink the userReportConfigurerLink to set
     */
    public void setUserReportConfigurerLink(HtmlOutputLink userReportConfigurerLink) {
        this.userReportConfigurerLink = userReportConfigurerLink;
    }

    /**
     * @return the userChartConfigurerLink
     */
    public HtmlOutputLink getUserChartConfigurerLink() {
        return userChartConfigurerLink;
    }

    /**
     * @param userChartConfigurerLink the userChartConfigurerLink to set
     */
    public void setUserChartConfigurerLink(HtmlOutputLink userChartConfigurerLink) {
        this.userChartConfigurerLink = userChartConfigurerLink;
    }

    /**
     * @return the purchaseOrdertemLink
     */
    public HtmlOutputLink getPurchaseOrdertemLink() {
        return purchaseOrdertemLink;
    }

    /**
     * @param purchaseOrdertemLink the purchaseOrdertemLink to set
     */
    public void setPurchaseOrdertemLink(HtmlOutputLink purchaseOrdertemLink) {
        this.purchaseOrdertemLink = purchaseOrdertemLink;
    }

    /**
     * @return the userTypeSearchLink
     */
    public HtmlOutputLink getUserTypeSearchLink() {
        return userTypeSearchLink;
    }

    /**
     * @param userTypeSearchLink the userTypeSearchLink to set
     */
    public void setUserTypeSearchLink(HtmlOutputLink userTypeSearchLink) {
        this.userTypeSearchLink = userTypeSearchLink;
    }

    /**
     * @return the academicProfileLink
     */
    public HtmlOutputLink getAcademicProfileLink() {
        return academicProfileLink;
    }

    /**
     * @param academicProfileLink the academicProfileLink to set
     */
    public void setAcademicProfileLink(HtmlOutputLink academicProfileLink) {
        this.academicProfileLink = academicProfileLink;
    }

    /**
     * @return the projectActivitiesLink
     */
    public HtmlOutputLink getProjectActivitiesLink() {
        return projectActivitiesLink;
    }

    /**
     * @param projectActivitiesLink the projectActivitiesLink to set
     */
    public void setProjectActivitiesLink(HtmlOutputLink projectActivitiesLink) {
        this.projectActivitiesLink = projectActivitiesLink;
    }

    /**
     * @return the projectToolsLink
     */
    public HtmlOutputLink getProjectToolsLink() {
        return projectToolsLink;
    }

    /**
     * @param projectToolsLink the projectToolsLink to set
     */
    public void setProjectToolsLink(HtmlOutputLink projectToolsLink) {
        this.projectToolsLink = projectToolsLink;
    }

    /**
     * @return the cvSearchLink
     */
    public HtmlOutputLink getCvSearchLink() {
        return cvSearchLink;
    }

    /**
     * @param cvSearchLink the cvSearchLink to set
     */
    public void setCvSearchLink(HtmlOutputLink cvSearchLink) {
        this.cvSearchLink = cvSearchLink;
    }

    /**
     * @return the deptResponsibilityLink
     */
    public HtmlOutputLink getDeptResponsibilityLink() {
        return deptResponsibilityLink;
    }

    /**
     * @param deptResponsibilityLink the deptResponsibilityLink to set
     */
    public void setDeptResponsibilityLink(HtmlOutputLink deptResponsibilityLink) {
        this.deptResponsibilityLink = deptResponsibilityLink;
    }

    /**
     * @return the salaryGradeLink
     */
    public HtmlOutputLink getSalaryGradeLink() {
        return salaryGradeLink;
    }

    /**
     * @param salaryGradeLink the salaryGradeLink to set
     */
    public void setSalaryGradeLink(HtmlOutputLink salaryGradeLink) {
        this.salaryGradeLink = salaryGradeLink;
    }

    /**
     * @return the salaryStepLink
     */
    public HtmlOutputLink getSalaryStepLink() {
        return salaryStepLink;
    }

    /**
     * @param salaryStepLink the salaryStepLink to set
     */
    public void setSalaryStepLink(HtmlOutputLink salaryStepLink) {
        this.salaryStepLink = salaryStepLink;
    }

    /**
     * @return the recruitmentLink
     */
    public HtmlOutputLink getRecruitmentLink() {
        return recruitmentLink;
    }

    /**
     * @param recruitmentLink the recruitmentLink to set
     */
    public void setRecruitmentLink(HtmlOutputLink recruitmentLink) {
        this.recruitmentLink = recruitmentLink;
    }

    /**
     * @return the empResponsibilityLink
     */
    public HtmlOutputLink getEmpResponsibilityLink() {
        return empResponsibilityLink;
    }

    /**
     * @param empResponsibilityLink the empResponsibilityLink to set
     */
    public void setEmpResponsibilityLink(HtmlOutputLink empResponsibilityLink) {
        this.empResponsibilityLink = empResponsibilityLink;
    }

    /**
     * @return the medicalRecordLink
     */
    public HtmlOutputLink getMedicalRecordLink() {
        return medicalRecordLink;
    }

    /**
     * @param medicalRecordLink the medicalRecordLink to set
     */
    public void setMedicalRecordLink(HtmlOutputLink medicalRecordLink) {
        this.medicalRecordLink = medicalRecordLink;
    }

    /**
     * @return the empPayChangeLink
     */
    public HtmlOutputLink getEmpPayChangeLink() {
        return empPayChangeLink;
    }

    /**
     * @param empPayChangeLink the empPayChangeLink to set
     */
    public void setEmpPayChangeLink(HtmlOutputLink empPayChangeLink) {
        this.empPayChangeLink = empPayChangeLink;
    }

    /**
     * @return the salaryAdjusterLink
     */
    public HtmlOutputLink getSalaryAdjusterLink() {
        return salaryAdjusterLink;
    }

    /**
     * @param salaryAdjusterLink the salaryAdjusterLink to set
     */
    public void setSalaryAdjusterLink(HtmlOutputLink salaryAdjusterLink) {
        this.salaryAdjusterLink = salaryAdjusterLink;
    }

    /**
     * @return the miscAttributeLink
     */
    public HtmlOutputLink getMiscAttributeLink() {
        return miscAttributeLink;
    }

    /**
     * @param miscAttributeLink the miscAttributeLink to set
     */
    public void setMiscAttributeLink(HtmlOutputLink miscAttributeLink) {
        this.miscAttributeLink = miscAttributeLink;
    }

    /**
     * @return the stockBatchLink
     */
    public HtmlOutputLink getStockBatchLink() {
        return stockBatchLink;
    }

    /**
     * @param stockBatchLink the stockBatchLink to set
     */
    public void setStockBatchLink(HtmlOutputLink stockBatchLink) {
        this.stockBatchLink = stockBatchLink;
    }

    /**
     * @return the porderItemLink
     */
    public HtmlOutputLink getPorderItemLink() {
        return porderItemLink;
    }

    /**
     * @param porderItemLink the porderItemLink to set
     */
    public void setPorderItemLink(HtmlOutputLink porderItemLink) {
        this.porderItemLink = porderItemLink;
    }

    /**
     * @return the stockItemUnitLink
     */
    public HtmlOutputLink getStockItemUnitLink() {
        return stockItemUnitLink;
    }

    /**
     * @param stockItemUnitLink the stockItemUnitLink to set
     */
    public void setStockItemUnitLink(HtmlOutputLink stockItemUnitLink) {
        this.stockItemUnitLink = stockItemUnitLink;
    }

    /**
     * @return the clientContactPersonLink
     */
    public HtmlOutputLink getClientContactPersonLink() {
        return clientContactPersonLink;
    }

    /**
     * @param clientContactPersonLink the clientContactPersonLink to set
     */
    public void setClientContactPersonLink(HtmlOutputLink clientContactPersonLink) {
        this.clientContactPersonLink = clientContactPersonLink;
    }

    /**
     * @return the clientSearchLink
     */
    public HtmlOutputLink getClientSearchLink() {
        return clientSearchLink;
    }

    /**
     * @param clientSearchLink the clientSearchLink to set
     */
    public void setClientSearchLink(HtmlOutputLink clientSearchLink) {
        this.clientSearchLink = clientSearchLink;
    }

    /**
     * @return the stockItemUnitForSalesLink
     */
    public HtmlOutputLink getStockItemUnitForSalesLink() {
        return stockItemUnitForSalesLink;
    }

    /**
     * @param stockItemUnitForSalesLink the stockItemUnitForSalesLink to set
     */
    public void setStockItemUnitForSalesLink(HtmlOutputLink stockItemUnitForSalesLink) {
        this.stockItemUnitForSalesLink = stockItemUnitForSalesLink;
    }
}
