package com.rsdynamix.dynamo.common.entities;

import com.codrellica.projects.commons.DateUtil;
import com.codrellica.projects.commons.SQLStyle;
import com.rsd.projects.menus.FinultimateCommons;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import com.rsdynamics.projects.query.operators.QueryOperators;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.abstractobjects.FieldData;
import com.rsdynamix.bi.dynamo.common.setup.beans.ReportCategoryBean;
import com.rsdynamix.bi.projects.report.bean.UlticoreChartBean;
import com.rsdynamix.bi.projects.report.bean.UlticoreReportBean;
import com.rsdynamix.dynamo.common.setup.beans.AddressManagerBean;
import com.rsdynamix.dynamo.common.setup.beans.BankManagerBean;
import com.rsdynamix.dynamo.common.setup.beans.BranchBean;
import com.rsdynamix.dynamo.common.setup.beans.BusinessDivisionBean;
import com.rsdynamix.dynamo.common.setup.beans.CompanyIdentityBean;
import com.rsdynamix.dynamo.common.setup.beans.CurrencyBean;
import com.rsdynamix.dynamo.common.setup.beans.RelationshipBean;
import com.rsdynamix.hrms.commons.setup.beans.DepartmentBean;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.projects.commons.messages.beans.MessageSetupBean;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import com.rsdynamics.projects.query.operators.QueryRange;
import com.rsdynamics.projects.query.operators.QueryFieldData;

/**
 *
 * @author patushie
 */
@SessionScoped
@ManagedBean(name = "businessActionTrailBean")
public class BusinessActionTrailBean {

    private Date startDate;
    private Date endDate;
    //
    private BusinessActionTrailEntity businessActionTrail;
    private List<BusinessActionTrailEntity> businessActionTrailList;
    private List<SelectItem> businessActionTrailItemList;

    public BusinessActionTrailBean() {
        businessActionTrail = new BusinessActionTrailEntity();
        businessActionTrailList = new ArrayList<BusinessActionTrailEntity>();

        constructBizActionTrailItem();
    }

    public String loadBusinessActionTrailList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
        businessActionTrailList = new ArrayList<BusinessActionTrailEntity>();

        BusinessActionTrailEntity criteria = new BusinessActionTrailEntity();

        boolean hasCriteria = false;

        if (businessActionTrail.getEntityNameType() != null) {
            criteria.setEntityName(findActualEntityNameFromType(businessActionTrail.getEntityNameType()));
            hasCriteria = true;
        }

        if (businessActionTrail.getEntityID() > 0) {
            criteria.setEntityID(businessActionTrail.getEntityID());
            hasCriteria = true;
        }

        if ((startDate != null) && (endDate != null)) {
            Map<String, QueryRange> qeryRangeMap = new HashMap<String, QueryRange>();
            QueryRange queryRange = new QueryRange();
            QueryFieldData queryFieldData = new QueryFieldData();
            FieldData fieldData = new FieldData();

            fieldData.setFieldName("entryDate");
            if (CommonBean.SQL_STYLE == SQLStyle.ORACLE_STYLE) {
                fieldData.setFieldValue(DateUtil.getDateAsString(startDate));
            } else {
                fieldData.setFieldValue(startDate);
            }
            queryFieldData.setFieldData(fieldData);
            queryFieldData.setFieldOperator(QueryOperators.GREATER_OR_EQUALS);
            queryRange.setLowerBoundField(queryFieldData);

            queryFieldData = new QueryFieldData();
            fieldData = new FieldData();

            fieldData.setFieldName("entryDate");
            if (CommonBean.SQL_STYLE == SQLStyle.ORACLE_STYLE) {
                fieldData.setFieldValue(DateUtil.getDateAsString(endDate));
            } else {
                fieldData.setFieldValue(endDate);
            }
            
            queryFieldData.setFieldData(fieldData);
            queryFieldData.setFieldOperator(QueryOperators.LESS_OR_EQUALS);
            queryRange.setUpperBoundField(queryFieldData);

            criteria.getQueryRangeMap().put("entryDate", queryRange);

            hasCriteria = true;
        }

        if (hasCriteria) {
            try {
                List<AbstractEntity> entityList = dataServer.findData(criteria);
                for (AbstractEntity entity : entityList) {
                    BusinessActionTrailEntity actionTrail = (BusinessActionTrailEntity) entity;
                    if (!businessActionTrailList.contains(actionTrail)) {
                        businessActionTrailList.add(actionTrail);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return "";
    }

    public void entityNameMenuSelected(ValueChangeEvent vce) {

    }

    public void auditTrailSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            int rowIndex = CommonBean.getComponentEventRowIndex(vce);
            businessActionTrail = businessActionTrailList.get(rowIndex);
            businessActionTrail.setSelected(Boolean.parseBoolean(vce.getNewValue().toString()));

            if (businessActionTrail.isSelected()) {
                CommonBean.deselectOtherItems(businessActionTrail, businessActionTrailList);
            } else {
                this.businessActionTrail = new BusinessActionTrailEntity();
            }
        }
    }

    public BusinessActionTrailEntity loadPreviousHistoricalState(BusinessActionTrailEntity currentState) {
        BusinessActionTrailEntity requiredState = null;

        int index = businessActionTrailList.indexOf(currentState);
        if (index > 0) {
            requiredState = businessActionTrailList.get(index - 1);
            businessActionTrail = requiredState;
        }

        return requiredState;
    }

    public BusinessActionTrailEntity loadNextHistoricalState(BusinessActionTrailEntity currentState) {
        BusinessActionTrailEntity requiredState = null;

        int index = businessActionTrailList.indexOf(currentState);
        if (index < businessActionTrailList.size() - 1) {
            requiredState = businessActionTrailList.get(index + 1);
            businessActionTrail = requiredState;
        }

        return requiredState;
    }

    public String showBusinessActionTrailDetail() {
        String pageName = "";

        FinultimateCommons.captureRequestingResource();

        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}",
                    UserManagerBean.class, userManagerBean);
        }

        BankManagerBean bankManagerBean = (BankManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.bankManagerBean}", BankManagerBean.class);
        if (bankManagerBean == null) {
            bankManagerBean = new BankManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.bankManagerBean}",
                    BankManagerBean.class, bankManagerBean);
        }

        BranchBean branchBean = (BranchBean) CommonBean.getBeanFromContext(
                "#{sessionScope.branchBean}", BranchBean.class);
        if (branchBean == null) {
            branchBean = new BranchBean();
            CommonBean.setBeanToContext("#{sessionScope.branchBean}", BranchBean.class, branchBean);
        }

        AddressManagerBean addressBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.addressManagerBean}", AddressManagerBean.class);
        if (addressBean == null) {
            addressBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.addressManagerBean}", AddressManagerBean.class, addressBean);
        }

        RelationshipBean relationshipBean = (RelationshipBean) CommonBean.getBeanFromContext(
                "#{sessionScope.relationshipBean}", RelationshipBean.class);
        if (relationshipBean == null) {
            relationshipBean = new RelationshipBean();
            CommonBean.setBeanToContext("#{sessionScope.relationshipBean}", RelationshipBean.class, relationshipBean);
        }

        CurrencyBean currencyBean = (CurrencyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.currencyBean}", CurrencyBean.class);
        if (currencyBean == null) {
            currencyBean = new CurrencyBean();
            CommonBean.setBeanToContext("#{sessionScope.currencyBean}", CurrencyBean.class, currencyBean);
        }

        CompanyIdentityBean companyIdentityBean = (CompanyIdentityBean) CommonBean.getBeanFromContext(
                "#{sessionScope.companyIdentityBean}", CompanyIdentityBean.class);
        if (companyIdentityBean == null) {
            companyIdentityBean = new CompanyIdentityBean();
            CommonBean.setBeanToContext("#{sessionScope.companyIdentityBean}", CompanyIdentityBean.class, companyIdentityBean);
        }

        BusinessDivisionBean businessDivisionBean = (BusinessDivisionBean) CommonBean.getBeanFromContext(
                "#{sessionScope.businessDivisionBean}", BusinessDivisionBean.class);
        if (businessDivisionBean == null) {
            businessDivisionBean = new BusinessDivisionBean();
            CommonBean.setBeanToContext("#{sessionScope.businessDivisionBean}", BusinessDivisionBean.class, businessDivisionBean);
        }

        UlticoreChartBean ulticoreChartBean = (UlticoreChartBean) CommonBean.getBeanFromContext(
                "#{sessionScope.ulticoreChartBean}", UlticoreChartBean.class);
        if (ulticoreChartBean == null) {
            ulticoreChartBean = new UlticoreChartBean();
            CommonBean.setBeanToContext("#{sessionScope.ulticoreChartBean}",
                    UlticoreChartBean.class, ulticoreChartBean);
        }

        UlticoreReportBean ulticoreReportBean = (UlticoreReportBean) CommonBean.getBeanFromContext(
                "#{sessionScope.ulticoreReportBean}", UlticoreReportBean.class);
        if (ulticoreReportBean == null) {
            ulticoreReportBean = new UlticoreReportBean();
            CommonBean.setBeanToContext("#{sessionScope.ulticoreReportBean}",
                    UlticoreReportBean.class, ulticoreReportBean);
        }

        DepartmentBean departmentBean = (DepartmentBean) CommonBean.getBeanFromContext(
                "#{sessionScope.departmentBean}", DepartmentBean.class);
        if (departmentBean == null) {
            departmentBean = new DepartmentBean();
            CommonBean.setBeanToContext("#{sessionScope.departmentBean}", DepartmentBean.class, departmentBean);
        }

        ReportCategoryBean categoryBean = (ReportCategoryBean) CommonBean.getBeanFromContext(
                "#{sessionScope.reportCategoryBean}", ReportCategoryBean.class);
        if (categoryBean == null) {
            categoryBean = new ReportCategoryBean();
            CommonBean.setBeanToContext("#{sessionScope.reportCategoryBean}", ReportCategoryBean.class, categoryBean);
        }

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        MessageSetupBean messageSetupBean = (MessageSetupBean) CommonBean.getBeanFromContext("#{sessionScope.messageSetupBean}", MessageSetupBean.class);
        if (messageSetupBean == null) {
            messageSetupBean = new MessageSetupBean();
            CommonBean.setBeanToContext("#{sessionScope.messageSetupBean}", MessageSetupBean.class, messageSetupBean);
        }

        try {
            if (businessActionTrail.getEntityNameType() == EntityNameType.COMPANY_IDENTITY) {
                companyIdentityBean.loadCompanyIdentityList(businessActionTrail);
                pageName = "/setup/companyidentity.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.BRANCH) {
                branchBean.loadBranchList(businessActionTrail);
                pageName = "/setup/branchaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.BUSINESS_DIVISION) {
                businessDivisionBean.loadBusinessDivisionList(businessActionTrail);
                pageName = "/setup/businessdivisionaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.DEPARTMENT) {
                departmentBean.loadDepartment(businessActionTrail);
                pageName = "/setup/departmentaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.REPORT_CATEGORY) {
                categoryBean.loadCategoryList(businessActionTrail);
                pageName = "/setup/reportcategoryaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.RELATIONSHIP) {
                relationshipBean.loadRelationshipList(businessActionTrail);
                pageName = "/setup/relationshipaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.TITLE) {
                addressBean.loadTitles(businessActionTrail);
                pageName = "/setup/titleaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.COUNTRY) {
                addressBean.loadCountryList(businessActionTrail);
                pageName = "/setup/countryaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.STATE) {
                addressBean.loadStateList(businessActionTrail);
                pageName = "/setup/stateaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.CITY) {
                addressBean.loadCityList(businessActionTrail);
                pageName = "/setup/cityaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.CURRENCY) {
                currencyBean.loadCurrencyList(businessActionTrail);
                pageName = "/setup/currencyaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.BANK) {
                bankManagerBean.loadBankData(businessActionTrail);
                pageName = "/setup/bankaudittrail.jsf";
            }  else if (businessActionTrail.getEntityNameType() == EntityNameType.MESSAGING) {
                messageSetupBean.loadMailSettingList(businessActionTrail);
                pageName = "/messaging/mailaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.PAYADVICE) {
                pageName = "PaymentAdviceEntity";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.BI_REPORT) {
                ulticoreReportBean.loadReportTemplates(businessActionTrail);
                pageName = "/reports/reportaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.USER_ACCOUNT) {
                userManagerBean.loadUserAccount(businessActionTrail);
                pageName = "/security/useraccountaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.USER_ROLE) {
                privilegeBean.loadRoleList(businessActionTrail);
                pageName = "/security/userroleaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.SYSTEM_MENU) {
                privilegeBean.loadMenuList(businessActionTrail);
                pageName = "/security/sysmenuaudittrail.jsf";
            } else if (businessActionTrail.getEntityNameType() == EntityNameType.SUBSYSTEM) {
                privilegeBean.loadSubsystemList(businessActionTrail);
                pageName = "/security/subsystemaudittrail.jsf";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return pageName;
    }

    public String findActualEntityNameFromType(EntityNameType entityNameType) {
        String entityName = "";

        if (entityNameType == EntityNameType.COMPANY_IDENTITY) {
            entityName = "CompanyIdentityEntity";
        } else if (entityNameType == EntityNameType.BRANCH) {
            entityName = "BranchEntity";
        } else if (entityNameType == EntityNameType.BUSINESS_DIVISION) {
            entityName = "BusinessDivisionEntity";
        } else if (entityNameType == EntityNameType.DEPARTMENT) {
            entityName = "DepartmentEntity";
        } else if (entityNameType == EntityNameType.RELATIONSHIP) {
            entityName = "RelationshipEntity";
        } else if (entityNameType == EntityNameType.TITLE) {
            entityName = "TitleEntity";
        } else if (entityNameType == EntityNameType.COUNTRY) {
            entityName = "CountryEntity";
        } else if (entityNameType == EntityNameType.STATE) {
            entityName = "StateEntity";
        } else if (entityNameType == EntityNameType.CITY) {
            entityName = "CityEntity";
        } else if (entityNameType == EntityNameType.CURRENCY) {
            entityName = "CurrencyEntity";
        } else if (entityNameType == EntityNameType.BANK) {
            entityName = "BankEntity";
        } else if (entityNameType == EntityNameType.PFA) {
            entityName = "PensionFundAdminEntity";
        } else if (entityNameType == EntityNameType.TRUSTEE) {
            entityName = "TrusteeEntity";
        } else if (entityNameType == EntityNameType.EMPLOYER) {
            entityName = "EmployerEntity";
        } else if (entityNameType == EntityNameType.UNDERWRITER) {
            entityName = "UnderwriterEntity";
        } else if (entityNameType == EntityNameType.REFERRAL) {
            entityName = "ReferrerEntity";
        } else if (entityNameType == EntityNameType.MESSAGING) {
            entityName = "EmailSettingEntity";
        } else if (entityNameType == EntityNameType.TRANSACTION_SOURCE) {
            entityName = "GLTransactionSourceEntity";
        } else if (entityNameType == EntityNameType.ACCOUNT_SECTION) {
            entityName = "GLAccountSectionEntity";
        } else if (entityNameType == EntityNameType.ACCOUNT_GROUP) {
            entityName = "GLAccountGroupEntity";
        } else if (entityNameType == EntityNameType.ACCOUNT_CHART) {
            entityName = "GLAccountChartEntity";
        } else if (entityNameType == EntityNameType.FINANCIAL_YEAR) {
            entityName = "FinancialYearEntity";
        } else if (entityNameType == EntityNameType.ACCOUNT_PERIOD) {
            entityName = "AccountingPeriodEntity";
        } else if (entityNameType == EntityNameType.TRANSACTION_TYPE) {
            entityName = "GLTransactionTypeEntity";
        } else if (entityNameType == EntityNameType.BIZ_TRAN_TYPE_MAP) {
            entityName = "GLTransactionBizActionMappingEntity";
        } else if (entityNameType == EntityNameType.IDENTITY_TYPE) {
            entityName = "IdentityTypeEntity";
        } else if (entityNameType == EntityNameType.PROOF_OF_ADDRESS) {
            entityName = "ProofOfAddressTypeEntity";
        } else if (entityNameType == EntityNameType.AGENT_TYPE) {
            entityName = "AgencyTypeEntity";
        } else if (entityNameType == EntityNameType.AGENT) {
            entityName = "AgencyEntity";
        } else if (entityNameType == EntityNameType.CLIENT) {
            entityName = "ClientRecordEntity";
        } else if (entityNameType == EntityNameType.JOURNAL) {
            entityName = "GLJournalEntity";
        } else if (entityNameType == EntityNameType.BUDGET) {
            entityName = "GLAccountYearlyBudgetEntity";
        } else if (entityNameType == EntityNameType.TAX) {
            entityName = "TaxEntity";
        } else if (entityNameType == EntityNameType.TAX_AUTHORITY) {
            entityName = "TaxAuthorityEntity";
        } else if (entityNameType == EntityNameType.DISCOUNT) {
            entityName = "SalesDiscountEntity";
        } else if (entityNameType == EntityNameType.PRICING) {
            entityName = "PricingSetupEntity";
        } else if (entityNameType == EntityNameType.TARIFF_RATE) {
            entityName = "TariffRateEntity";
        } else if (entityNameType == EntityNameType.GENDER_MORTALITY_RATE) {
            entityName = "GenderMortalityRateEntity";
        } else if (entityNameType == EntityNameType.POLICY) {
            entityName = "PolicyEntity";
        } else if (entityNameType == EntityNameType.CLAIM) {
            entityName = "DeathClaimEntity";
        } else if (entityNameType == EntityNameType.SURRENDER) {
            entityName = "SurrenderEntity";
        } else if (entityNameType == EntityNameType.RECEIPT) {
            entityName = "ReceiptEntity";
        } else if (entityNameType == EntityNameType.PAYADVICE) {
            entityName = "PaymentAdviceEntity";
        } else if (entityNameType == EntityNameType.BUSINESS_MODULE) {
            entityName = "BusinessModuleEntity";
        } else if (entityNameType == EntityNameType.COLLECTION_MODE) {
            entityName = "CollectionModeEntity";
        } else if (entityNameType == EntityNameType.COLLECTION_SOURCE) {
            entityName = "CollectionSourceEntity";
        } else if (entityNameType == EntityNameType.PAYMENT_REQ) {
            entityName = "PaymentRequisitionEntity";
        } else if (entityNameType == EntityNameType.PAYMENT) {
            entityName = "PaymentEntity";
        } else if (entityNameType == EntityNameType.BI_REPORT) {
            entityName = "UlticoreReportEntity";
        } else if (entityNameType == EntityNameType.USER_ACCOUNT) {
            entityName = "UserAccountEntity";
        } else if (entityNameType == EntityNameType.USER_ROLE) {
            entityName = "RoleEntity";
        } else if (entityNameType == EntityNameType.SYSTEM_MENU) {
            entityName = "MenuEntity";
        } else if (entityNameType == EntityNameType.SUBSYSTEM) {
            entityName = "SubsystemEntity";
        } else if (entityNameType == EntityNameType.ESCALATION_RATE) {
            entityName = "EscalationRateEntity";
        }

        return entityName;
    }

    public void constructBizActionTrailItem() {
        businessActionTrailItemList = new ArrayList<SelectItem>();

        SelectItem item = new SelectItem();
        item.setValue(EntityNameType.COMPANY_IDENTITY);
        item.setLabel(EntityNameType.COMPANY_IDENTITY.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.BRANCH);
        item.setLabel(EntityNameType.BRANCH.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.BUSINESS_DIVISION);
        item.setLabel(EntityNameType.BUSINESS_DIVISION.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.DEPARTMENT);
        item.setLabel(EntityNameType.DEPARTMENT.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.RELATIONSHIP);
        item.setLabel(EntityNameType.RELATIONSHIP.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.TITLE);
        item.setLabel(EntityNameType.TITLE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.COUNTRY);
        item.setLabel(EntityNameType.COUNTRY.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.STATE);
        item.setLabel(EntityNameType.STATE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.CITY);
        item.setLabel(EntityNameType.CITY.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.CURRENCY);
        item.setLabel(EntityNameType.CURRENCY.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.BANK);
        item.setLabel(EntityNameType.BANK.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.PFA);
        item.setLabel(EntityNameType.PFA.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.TRUSTEE);
        item.setLabel(EntityNameType.TRUSTEE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.EMPLOYER);
        item.setLabel(EntityNameType.EMPLOYER.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.UNDERWRITER);
        item.setLabel(EntityNameType.UNDERWRITER.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.REFERRAL);
        item.setLabel(EntityNameType.REFERRAL.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.MESSAGING);
        item.setLabel(EntityNameType.MESSAGING.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.TRANSACTION_SOURCE);
        item.setLabel(EntityNameType.TRANSACTION_SOURCE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.ACCOUNT_SECTION);
        item.setLabel(EntityNameType.ACCOUNT_SECTION.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.ACCOUNT_GROUP);
        item.setLabel(EntityNameType.ACCOUNT_GROUP.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.ACCOUNT_CHART);
        item.setLabel(EntityNameType.ACCOUNT_CHART.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.FINANCIAL_YEAR);
        item.setLabel(EntityNameType.FINANCIAL_YEAR.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.ACCOUNT_PERIOD);
        item.setLabel(EntityNameType.ACCOUNT_PERIOD.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.TRANSACTION_TYPE);
        item.setLabel(EntityNameType.TRANSACTION_TYPE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.BIZ_TRAN_TYPE_MAP);
        item.setLabel(EntityNameType.BIZ_TRAN_TYPE_MAP.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.IDENTITY_TYPE);
        item.setLabel(EntityNameType.IDENTITY_TYPE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.PROOF_OF_ADDRESS);
        item.setLabel(EntityNameType.PROOF_OF_ADDRESS.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.AGENT_TYPE);
        item.setLabel(EntityNameType.AGENT_TYPE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.AGENT);
        item.setLabel(EntityNameType.AGENT.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.CLIENT);
        item.setLabel(EntityNameType.CLIENT.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.JOURNAL);
        item.setLabel(EntityNameType.JOURNAL.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.BUDGET);
        item.setLabel(EntityNameType.BUDGET.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.PRODUCT_TYPE);
        item.setLabel(EntityNameType.PRODUCT_TYPE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.TAX);
        item.setLabel(EntityNameType.TAX.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.TAX_AUTHORITY);
        item.setLabel(EntityNameType.TAX_AUTHORITY.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.DISCOUNT);
        item.setLabel(EntityNameType.DISCOUNT.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.PRICING);
        item.setLabel(EntityNameType.PRICING.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.TARIFF_RATE);
        item.setLabel(EntityNameType.TARIFF_RATE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.GENDER_MORTALITY_RATE);
        item.setLabel(EntityNameType.GENDER_MORTALITY_RATE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.POLICY);
        item.setLabel(EntityNameType.POLICY.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.CLAIM);
        item.setLabel(EntityNameType.CLAIM.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.SURRENDER);
        item.setLabel(EntityNameType.SURRENDER.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.RECEIPT);
        item.setLabel(EntityNameType.RECEIPT.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.PAYADVICE);
        item.setLabel(EntityNameType.PAYADVICE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.BUSINESS_MODULE);
        item.setLabel(EntityNameType.BUSINESS_MODULE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.COLLECTION_MODE);
        item.setLabel(EntityNameType.COLLECTION_MODE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.COLLECTION_SOURCE);
        item.setLabel(EntityNameType.COLLECTION_SOURCE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.PAYMENT_REQ);
        item.setLabel(EntityNameType.PAYMENT_REQ.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.PAYMENT);
        item.setLabel(EntityNameType.PAYMENT.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.BI_REPORT);
        item.setLabel(EntityNameType.BI_REPORT.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.USER_ACCOUNT);
        item.setLabel(EntityNameType.USER_ACCOUNT.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.USER_ROLE);
        item.setLabel(EntityNameType.USER_ROLE.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.SYSTEM_MENU);
        item.setLabel(EntityNameType.SYSTEM_MENU.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.SUBSYSTEM);
        item.setLabel(EntityNameType.SUBSYSTEM.toString());
        businessActionTrailItemList.add(item);

        item = new SelectItem();
        item.setValue(EntityNameType.ESCALATION_RATE);
        item.setLabel(EntityNameType.ESCALATION_RATE.toString());
        businessActionTrailItemList.add(item);
    }

    /**
     * @return the businessActionTrail
     */
    public BusinessActionTrailEntity getBusinessActionTrail() {
        return businessActionTrail;
    }

    /**
     * @param businessActionTrail the businessActionTrail to set
     */
    public void setBusinessActionTrail(BusinessActionTrailEntity businessActionTrail) {
        this.businessActionTrail = businessActionTrail;
    }

    /**
     * @return the businessActionTrailList
     */
    public List<BusinessActionTrailEntity> getBusinessActionTrailList() {
        return businessActionTrailList;
    }

    /**
     * @param businessActionTrailList the businessActionTrailList to set
     */
    public void setBusinessActionTrailList(List<BusinessActionTrailEntity> businessActionTrailList) {
        this.businessActionTrailList = businessActionTrailList;
    }

    /**
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the businessActionTrailItemList
     */
    public List<SelectItem> getBusinessActionTrailItemList() {
        return businessActionTrailItemList;
    }

    /**
     * @param businessActionTrailItemList the businessActionTrailItemList to set
     */
    public void setBusinessActionTrailItemList(List<SelectItem> businessActionTrailItemList) {
        this.businessActionTrailItemList = businessActionTrailItemList;
    }
}
