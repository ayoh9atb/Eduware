/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.dynamo.common.setup.beans;

import com.rsd.projects.menus.FinultimateCommons;
import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import com.rsdynamics.projects.field.operators.CurrencyFieldOperator;
import com.rsdynamics.projects.query.operators.QueryOperators;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.dynamo.setup.entities.CurrencyEntity;
import com.rsdynamix.dynamo.setup.entities.CurrencyOperationType;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsd.projects.menus.ValueListBean;
import com.rsdynamix.tns.util.Constants;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author root
 */
@SessionScoped
@ManagedBean(name = "currencyBean")
public class CurrencyBean implements Serializable {

    public static final String NAIRA_CODE_KEY = "NGN_KEY";
    //
    public static final String NAIRA_CODE_VALUE = "NGN";
    //
    private CurrencyEntity currency;
    private CurrencyEntity baseCurrency;
    private List<CurrencyEntity> currencyList;
    private List<SelectItem> currencyItemList;
    //
    private List<SelectItem> currencyOpTypeItemList;

    public CurrencyBean() {
        currency = new CurrencyEntity();
        baseCurrency = new CurrencyEntity();
        currencyList = new ArrayList<CurrencyEntity>();
        currencyItemList = new ArrayList<SelectItem>();
        currencyOpTypeItemList = new ArrayList<SelectItem>();

        loadCurrencyList();
    }

    public String addCurrency() {

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (getCurrency().getCurrencyName() == null || getCurrency().getCurrencyName().trim().isEmpty()
                || getCurrency().getCurrencyCode() == null || getCurrency().getCurrencyCode().trim().isEmpty()) {
            applicationMessageBean.insertMessage("Specify Currency and Currency Code", MessageType.ERROR_MESSAGE);
            return "";
        }

        if (getCurrency().getCurrencyCode() != null && !getCurrency().getCurrencyCode().trim().isEmpty()
                && getCurrency().getCurrencyCode().trim().length() > 3) {
            applicationMessageBean.insertMessage("Currency Code cannot be more than 3 characters", MessageType.ERROR_MESSAGE);
            return "";
        }
        if (getCurrency().getCurrencyName() != null && !getCurrency().getCurrencyName().trim().isEmpty() && getCurrency().getCurrencyCode() != null && !getCurrency().getCurrencyCode().trim().isEmpty()) {

            if (!getCurrencyList().contains(getCurrency())) {
                getCurrencyList().add(getCurrency());
            }
            setCurrency(new CurrencyEntity());
        }

        return "";
    }

//    public String currencySelected(ValueChangeEvent vce) {
//        if ((vce != null) && (vce.getNewValue() != null)) {
//            int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
//            currency = findCurrencyByCode(currencyList.get(rowIndex).getCurrencyCode());
//
//            CommonBean.deselectOtherItems(currency, currencyList);
//        }
//
//        return "";
//    }
    public String currencySelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
            currency.setSelected(Boolean.parseBoolean(vce.getNewValue().toString()));
            if (currency.isSelected()) {
                currency = findCurrencyByCode(currencyList.get(rowIndex).getCurrencyCode());

                CommonBean.deselectOtherItems(currency, currencyList);
            } else {
                currency = new CurrencyEntity();
            }
        }

        return "";
    }

    public String currencyMenuSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            currency = findCurrencyByCode(vce.getNewValue().toString());
        }

        return "";
    }

    public CurrencyEntity findCurrencyByCode(String currencyCode) {
        CurrencyEntity curr = null;

        for (CurrencyEntity crncy : getCurrencyList()) {
            if (crncy.getCurrencyCode().equals(currencyCode)) {
                curr = crncy;
                break;
            }
        }

        return curr;
    }

    public List<CurrencyEntity> findNonBaseCurrencyList() {
        List<CurrencyEntity> currList = new ArrayList<CurrencyEntity>();

        for (CurrencyEntity crncy : getCurrencyList()) {
            if (crncy.getOperationType() != CurrencyOperationType.BASE_CURRENCY) {
                currList.add(crncy);
            }
        }

        return currList;
    }

    public String deleteCurrency(ValueChangeEvent vce) {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);
        getCurrencyList().remove(getCurrency());
        applicationMessageBean.insertMessage("Currency has been deleted", MessageType.SUCCESS_MESSAGE);

        return "";
    }

    public String findCurrencyByCriteria() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        currencyList.clear();
        boolean hasCriteria = false;

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        CurrencyEntity criteria = new CurrencyEntity();
        criteria.setQueryOperators(new CurrencyFieldOperator());

        if (currency.getCurrencyName() != null && !currency.getCurrencyName().trim().isEmpty()) {
            ((CurrencyFieldOperator) criteria.getQueryOperators()).setCurrencyName(QueryOperators.LIKE);
            criteria.setCurrencyName("%" + currency.getCurrencyName() + "%");
            hasCriteria = true;
        }
        if (currency.getCurrencyCode() != null && !currency.getCurrencyCode().trim().isEmpty()) {
            ((CurrencyFieldOperator) criteria.getQueryOperators()).setCurrencyCode(QueryOperators.LIKE);
            criteria.setCurrencyCode("%" + currency.getCurrencyCode() + "%");
            hasCriteria = true;
        }
        if (currency.getSubcurrencyName() != null && !currency.getSubcurrencyName().trim().isEmpty()) {
            ((CurrencyFieldOperator) criteria.getQueryOperators()).setSubcurrencyName(QueryOperators.LIKE);
            criteria.setSubcurrencyName("%" + currency.getSubcurrencyName() + "%");
            hasCriteria = true;
        }

        try {
            if (hasCriteria) {
                MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                        "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
                if (menuManagerBean == null) {
                    menuManagerBean = new MenuManagerBean();
                    CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
                }

                PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                        "#{sessionScope.privilegeBean}", PrivilegeBean.class);
                if (privilegeBean == null) {
                    privilegeBean = new PrivilegeBean();
                    CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
                }

                MenuEntity privilege = privilegeBean.findPrivilegeByName(
                        menuManagerBean.getSystemMap().get(MenuManagerBean.CURRENCIES_MENU_ITEM));

                List<AbstractEntity> entityList = dataServer.findData(criteria);
                CurrencyEntity businessDivisionEntity = null;
                for (AbstractEntity entity : entityList) {
                    businessDivisionEntity = (CurrencyEntity) entity;

                    businessDivisionEntity.setActivated(
                            businessDivisionEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                    currencyList.add(businessDivisionEntity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String saveCurrencyList() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{applicationScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        dataServer.beginTransaction();
        try {
            for (CurrencyEntity curncy : getCurrencyList()) {
                if (curncy.getCurrencyId() > 0) {
                    dataServer.updateData(curncy);
                } else {
                    int curID = Integer.parseInt(appPropBean.getValue(
                            FinultimateConstants.CURRENCY_ID,
                            FinultimateConstants.ONE_STR, true));
                    curncy.setCurrencyId(curID);
                    dataServer.saveData(curncy);
                }
            }
            dataServer.endTransaction();
            applicationMessageBean.insertMessage("Currency has been saved", MessageType.SUCCESS_MESSAGE);
        } catch (SQLException ex) {
            dataServer.rollbackTransaction();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }

    public String activate() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}",
                    UserManagerBean.class, userManagerBean);
        }

        BusinessActionTrailBean businessActionTrailBean = (BusinessActionTrailBean) CommonBean.getBeanFromContext(
                "#{sessionScope.businessActionTrailBean}", BusinessActionTrailBean.class);
        if (businessActionTrailBean == null) {
            businessActionTrailBean = new BusinessActionTrailBean();
            CommonBean.setBeanToContext("#{sessionScope.businessActionTrailBean}",
                    BusinessActionTrailBean.class, businessActionTrailBean);
        }

        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        MenuEntity privilege = privilegeBean.findPrivilegeByName(
                menuManagerBean.getSystemMap().get(MenuManagerBean.CURRENCIES_MENU_ITEM));

        if (currency.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((currency.getApprovalStatusID() == 0) || (currency.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(currency.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        currency.setApprovalStatusID(currency.getApprovalStatusID() + 1);
                        currency.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(currency);
                            dataServer.endTransaction();

                            applicationMessageBean.insertMessage("Operation Activated Successfully!", MessageType.SUCCESS_MESSAGE);
                        } catch (SQLException ex) {
                            dataServer.rollbackTransaction();
                            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            == creatorAcct.getRole().getApprovalLevelID()) {
                        applicationMessageBean.insertMessage("You cannot approve an operation performed "
                                + "by another user who has the same approval level as you!",
                                MessageType.ERROR_MESSAGE);
                    } else if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            < creatorAcct.getRole().getApprovalLevelID()) {
                        applicationMessageBean.insertMessage("You cannot approve an operation performed "
                                + "by another user who has a higher approval level as you!",
                                MessageType.ERROR_MESSAGE);
                    }
                } else if (currency.getApprovalStatusID()
                        > userManagerBean.getUserAccount().getRole().getApprovalLevelID()) {
                    applicationMessageBean.insertMessage("This operation has been approved beyond "
                            + "your approval level, your approval is no longer effective!",
                            MessageType.ERROR_MESSAGE);
                }
            } else {
                applicationMessageBean.insertMessage("Access Denied: You do not have approval rights for this operation type!",
                        MessageType.ERROR_MESSAGE);
            }
        } else {
            applicationMessageBean.insertMessage("This operation is already approved. No action performed!",
                    MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String deleteCurrency() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        dataServer.beginTransaction();
        try {
            if (currencyList.contains(currency)) {
                if (!currency.getCurrencyCode().trim().equals("")) {
                    if (currency.isActivated()) {
                        applicationMessageBean.insertMessage(
                                "This Item has been Activated. Deletion of Activated Items is not allowed",
                                MessageType.ERROR_MESSAGE);
                        return "";
                    }
                    CurrencyEntity criteria = new CurrencyEntity();
                    criteria.setCurrencyCode(currency.getCurrencyCode());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();

                    for (SelectItem item : currencyItemList) {
                        if (item.getValue().toString().equals(currency.getCurrencyCode())) {
                            currencyItemList.remove(item);
                            break;
                        }
                    }
                }

                currencyList.remove(currency);
                currency = new CurrencyEntity();
                applicationMessageBean.insertMessage("Currency has been deleted", MessageType.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            dataServer.rollbackTransaction();
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String clearCurrencyList() {
        setCurrencyList(new ArrayList<CurrencyEntity>());
        return "";
    }

    public String loadCurrencyList() {
        if ((currencyList == null) || (currencyList.size() == 0)) {
            reloadCurrencyList();
        }

        return "";
    }

    public String reloadCurrencyList() {
        setCurrencyList(new ArrayList<CurrencyEntity>());

        ValueListBean valueListBean = (ValueListBean) CommonBean.getBeanFromContext(
                "#{sessionScope.valueListBean}", ValueListBean.class);
        if (valueListBean == null) {
            valueListBean = new ValueListBean();
            CommonBean.setBeanToContext("#{sessionScope.valueListBean}", ValueListBean.class, valueListBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        MenuEntity privilege = privilegeBean.findPrivilegeByName(
                menuManagerBean.getSystemMap().get(MenuManagerBean.CURRENCIES_MENU_ITEM));

        try {
            CurrencyEntity criteria = new CurrencyEntity();
            List<AbstractEntity> baseCurrencyList = dataServer.findData(criteria);
            for (AbstractEntity currencyEntity : baseCurrencyList) {
                CurrencyEntity currncy = (CurrencyEntity) currencyEntity;

                currncy.setActivated(currncy.getApprovalStatusID() >= privilege.getApprovedStatusID());
                getCurrencyList().add(currncy);

                if (currncy.getOperationType() == CurrencyOperationType.BASE_CURRENCY) {
                    baseCurrency.setCurrencyCode(currncy.getCurrencyCode());
                    baseCurrency.setCurrencyName(currncy.getCurrencyName());
                    baseCurrency.setCurrencyId(currncy.getCurrencyId());
                    baseCurrency.setOperationType(currncy.getOperationType());
                }

                addToSelectItemList(currncy);
            }

            if (getCurrencyList() != null && getCurrencyList().size() > 0) {

                Collections.sort(getCurrencyList(), new Comparator<CurrencyEntity>() {

                    public int compare(CurrencyEntity o1, CurrencyEntity o2) {
                        return (o1.getCurrencyName().compareTo(o2.getCurrencyName()));
                    }
                });
            }

            if (getCurrencyItemList() != null && getCurrencyItemList().size() > 0) {

                Collections.sort(getCurrencyItemList(), new Comparator<SelectItem>() {

                    public int compare(SelectItem o1, SelectItem o2) {
                        return (new Integer(o1.getDescription()).compareTo(
                                new Integer(o2.getDescription())));
                    }
                });
            }

            if ((getCurrencyItemList() != null) && (getCurrencyItemList().size() > 0)) {
                valueListBean.getValueListMap().put(
                        getCurrencyItemList().getClass().getName(), getCurrencyItemList());

                SelectItem item = new SelectItem();
                item.setLabel("CurrencyEntity Menu");
                item.setValue(getCurrencyItemList().getClass().getName());
                valueListBean.getValueCategoryTypeItemList().add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        currencyOpTypeItemList = new ArrayList<SelectItem>();

        SelectItem item = new SelectItem();
        item.setValue(CurrencyOperationType.BASE_CURRENCY);
        item.setLabel(CurrencyOperationType.BASE_CURRENCY.toString());
        currencyOpTypeItemList.add(item);

        item = new SelectItem();
        item.setValue(CurrencyOperationType.TRANSACTION_CURRENCY);
        item.setLabel(CurrencyOperationType.TRANSACTION_CURRENCY.toString());
        currencyOpTypeItemList.add(item);

        return "";
    }

    public String loadCurrencyList(BusinessActionTrailEntity businessActionTrail) {
        setCurrencyList(new ArrayList<CurrencyEntity>());

        ValueListBean valueListBean = (ValueListBean) CommonBean.getBeanFromContext(
                "#{sessionScope.valueListBean}", ValueListBean.class);
        if (valueListBean == null) {
            valueListBean = new ValueListBean();
            CommonBean.setBeanToContext("#{sessionScope.valueListBean}", ValueListBean.class, valueListBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        try {
            CurrencyEntity criteria = new CurrencyEntity();
            List<AbstractEntity> baseCurrencyList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
            for (AbstractEntity baseCurrency : baseCurrencyList) {
                CurrencyEntity currncy = (CurrencyEntity) baseCurrency;
                currncy.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                getCurrencyList().add(currncy);
            }

            Collections.sort(getCurrencyList(), new Comparator<CurrencyEntity>() {

                public int compare(CurrencyEntity o1, CurrencyEntity o2) {
                    return (o1.getCurrencyName().compareTo(o2.getCurrencyName()));
                }
            });

            if ((getCurrencyItemList() != null) && (getCurrencyItemList().size() > 0)) {
                valueListBean.getValueListMap().put(getCurrencyItemList().getClass().getName(),
                        getCurrencyItemList());

                SelectItem item = new SelectItem();
                item.setLabel("CurrencyEntity Menu");
                item.setValue(getCurrencyItemList().getClass().getName());
                valueListBean.getValueCategoryTypeItemList().add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void addToSelectItemList(CurrencyEntity entity) {
        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
            MenuEntity privilege = privilegeBean.findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.CURRENCIES_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getCurrencyCode());
                item.setLabel(entity.getCurrencyCode());
                item.setDescription(String.valueOf(entity.getCurrencyId()));
                currencyItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getCurrencyCode());
            item.setLabel(entity.getCurrencyCode());
            item.setDescription(String.valueOf(entity.getCurrencyId()));
            currencyItemList.add(item);
        }
    }

    public String clearCache() {
        currency = new CurrencyEntity();

        return "";
    }

    public String loadPreviousHistoricalState() {
        BusinessActionTrailBean businessActionTrailBean = (BusinessActionTrailBean) CommonBean.getBeanFromContext(
                "#{sessionScope.businessActionTrailBean}", BusinessActionTrailBean.class);
        if (businessActionTrailBean == null) {
            businessActionTrailBean = new BusinessActionTrailBean();
            CommonBean.setBeanToContext("#{sessionScope.businessActionTrailBean}",
                    BusinessActionTrailBean.class, businessActionTrailBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (currencyList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    currencyList.get(0).getActionTrail());
            if (batEntity != null) {
                loadCurrencyList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadCurrencyList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        }

        return "";
    }

    public String loadNextHistoricalState() {
        BusinessActionTrailBean businessActionTrailBean = (BusinessActionTrailBean) CommonBean.getBeanFromContext(
                "#{sessionScope.businessActionTrailBean}", BusinessActionTrailBean.class);
        if (businessActionTrailBean == null) {
            businessActionTrailBean = new BusinessActionTrailBean();
            CommonBean.setBeanToContext("#{sessionScope.businessActionTrailBean}",
                    BusinessActionTrailBean.class, businessActionTrailBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (currencyList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(currencyList.get(0).getActionTrail());
            if (batEntity != null) {
                loadCurrencyList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadCurrencyList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        }

        return "";
    }

    public String gotoAuditTrailPage() {
        String outcome = "";

        BusinessActionTrailBean businessActionTrailBean = (BusinessActionTrailBean) CommonBean.getBeanFromContext(
                "#{sessionScope.businessActionTrailBean}", BusinessActionTrailBean.class);
        if (businessActionTrailBean == null) {
            businessActionTrailBean = new BusinessActionTrailBean();
            CommonBean.setBeanToContext("#{sessionScope.businessActionTrailBean}",
                    BusinessActionTrailBean.class, businessActionTrailBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.CURRENCY);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(currency.getCurrencyId());
        businessActionTrailBean.loadBusinessActionTrailList();

        if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
            businessActionTrailBean.setBusinessActionTrail(
                    businessActionTrailBean.getBusinessActionTrailList().get(
                            businessActionTrailBean.getBusinessActionTrailList().size() - 1));

            loadCurrencyList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            outcome = "currencyaudittrail.jsf";

            FinultimateCommons.captureRequestingResource();
        } else {
            applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    /**
     * @return the currency
     */
    public CurrencyEntity getCurrency() {
        return currency;
    }

    /**
     * @param currency the currency to set
     */
    public void setCurrency(CurrencyEntity currency) {
        this.currency = currency;
    }

    /**
     * @return the currencyList
     */
    public List<CurrencyEntity> getCurrencyList() {
        return currencyList;
    }

    /**
     * @param currencyList the currencyList to set
     */
    public void setCurrencyList(List<CurrencyEntity> currencyList) {
        this.currencyList = currencyList;
    }

    /**
     * @return the currencyItemList
     */
    public List<SelectItem> getCurrencyItemList() {
        return currencyItemList;
    }

    /**
     * @param currencyItemList the currencyItemList to set
     */
    public void setCurrencyItemList(List<SelectItem> currencyItemList) {
        this.currencyItemList = currencyItemList;
    }

    /**
     * @return the currencyOpTypeItemList
     */
    public List<SelectItem> getCurrencyOpTypeItemList() {
        return currencyOpTypeItemList;
    }

    /**
     * @param currencyOpTypeItemList the currencyOpTypeItemList to set
     */
    public void setCurrencyOpTypeItemList(List<SelectItem> currencyOpTypeItemList) {
        this.currencyOpTypeItemList = currencyOpTypeItemList;
    }

    /**
     * @return the currencyEntity
     */
    public CurrencyEntity getBaseCurrency() {
        return baseCurrency;
    }

    /**
     * @param baseCurrency the currencyEntity to set
     */
    public void setBaseCurrency(CurrencyEntity baseCurrency) {
        this.baseCurrency = baseCurrency;
    }
}
