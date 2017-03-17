/*
 * BankManagerBean.java
 *
 * Created on October 13, 2008, 1:43 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rsdynamix.dynamo.common.setup.beans;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.setup.entities.BankEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamics.projects.field.operators.BankFieldOperator;
import com.rsdynamics.projects.query.operators.QueryOperators;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import java.io.Serializable;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.tns.util.Constants;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "bankManagerBean")
public class BankManagerBean implements Serializable {

    private BankEntity bank;
    private List<BankEntity> bankList;
    private List<SelectItem> bankItemList;
    private List<SelectItem> bankBankItemList;
    private List<SelectItem> selectedBankItemList;
    private long searchBankID;

    /**
     * Creates a new instance of BankManagerBean
     */
    public BankManagerBean() {
        bank = new BankEntity();
        setBankList(new ArrayList<BankEntity>());
        setBankItemList(new ArrayList<SelectItem>());

        loadBankData();
    }

    public void selectBankEvent(ValueChangeEvent event) {
        long bankID = 0L;

        if (event.getComponent() instanceof HtmlSelectBooleanCheckbox) {
            int rowIndex = CommonBean.getCheckBoxEventRowIndex(event);
            bank = bankList.get(rowIndex);
            bank.setSelected(Boolean.parseBoolean(event.getNewValue().toString()));
            if (bank.isSelected()) {
                CommonBean.deselectOtherItems(bank, bankList);
            } else {
                bank = new BankEntity();
            }
        } else if (event.getComponent() instanceof HtmlSelectOneMenu) {
            if (event.getNewValue() != null) {
                bankID = Long.parseLong(event.getNewValue().toString());

                BankEntity bnk = findBankByBankID(bankID);

                if (bnk != null) {
                    bank = bnk;
                }
            }
        }
    }

    public String loadBankData() {
        if ((getBankList() == null) || (getBankList().size() == 0)) {
            reloadBankData();
        }

        return "";
    }

    public String reloadBankData() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

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

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        setBankList(new ArrayList<BankEntity>());
        setBankItemList(new ArrayList<SelectItem>());

        try {
            BankEntity criteria = new BankEntity();

            MenuEntity privilege = privilegeBean.findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.BANKS_MENU_ITEM));

            List<AbstractEntity> baseBankList = dataServer.findData(criteria);
            for (AbstractEntity baseBank : baseBankList) {
                BankEntity bnk = (BankEntity) baseBank;
                bnk.setActivated(bnk.getApprovalStatusID() >= privilege.getApprovedStatusID());
                bankList.add(bnk);
                addToSelectItemList(bnk);

                Collections.sort(bankItemList, new Comparator<SelectItem>() {

                    public int compare(SelectItem o1, SelectItem o2) {
                        return o1.getLabel().compareTo(o2.getLabel());
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String loadBankData(BusinessActionTrailEntity businessActionTrail) {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        setBankList(new ArrayList<BankEntity>());
        setBankItemList(new ArrayList<SelectItem>());

        try {
            BankEntity criteria = new BankEntity();

            List<AbstractEntity> baseBankList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
            for (AbstractEntity baseBank : baseBankList) {
                BankEntity bnk = (BankEntity) baseBank;
                bnk.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                bankList.add(bnk);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void addToSelectItemList(BankEntity entity) {
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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.BANKS_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getBankID());
                item.setLabel(entity.getBankName());
                bankItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getBankID());
            item.setLabel(entity.getBankName());
            bankItemList.add(item);
        }
    }

    public String addBank() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (bank.getBankName() == null || bank.getBankName().trim().isEmpty()
                || bank.getBankCode() == null || bank.getBankCode().trim().isEmpty()) {
            applicationMessageBean.insertMessage("Specify Bank Name and Bank Code", MessageType.ERROR_MESSAGE);
            return "";
        }
        if (bank != null && bank.getBankName() != null && !bank.getBankCode().trim().equals("")) {
            if (!getBankList().contains(bank)) {
                getBankList().add(bank);
            } else {
                int index = getBankList().indexOf(bank);
                getBankList().set(index, bank);
            }
            bank = new BankEntity();
        }
        return "";
    }

    public String saveBanks() {
        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
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

        dataServer.beginTransaction();
        try {
            for (BankEntity bank : bankList) {
                if (bank.getBankID() == 0) {
                    int bankID = Integer.parseInt(appPropBean.getValue(
                            FinultimateConstants.BANK_ID,
                            FinultimateConstants.ONE_STR, true));
                    bank.setBankID(bankID);

                    dataServer.saveData(bank);

//                    SelectItem item = new SelectItem();
//                    item.setLabel(bank.getBankName());
//                    item.setValue(bank.getBankID());
//                    bankItemList.add(item);
                } else {
                    dataServer.updateData(bank);
                }
            }
            applicationMessageBean.insertMessage("Bank details have been saved", MessageType.SUCCESS_MESSAGE);
        } catch (SQLException ex) {
            dataServer.rollbackTransaction();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        dataServer.endTransaction();
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.BANKS_MENU_ITEM));

        if (bank.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((bank.getApprovalStatusID() == 0) || (bank.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(bank.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        bank.setApprovalStatusID(bank.getApprovalStatusID() + 1);
                        bank.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(bank);
                        } catch (SQLException ex) {
                            dataServer.rollbackTransaction();
                            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        dataServer.endTransaction();
                        applicationMessageBean.insertMessage("Operation Activated Successfully!", MessageType.SUCCESS_MESSAGE);
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
                } else if (bank.getApprovalStatusID()
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

    public String deleteBank() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);
        try {
            if (bankList.contains(bank)) {

                if (bank.getBankID() > 0) {
                    if (bank.isActivated()) {
                        applicationMessageBean.insertMessage("This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                        return "";
                    }
                    FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                    BankEntity criteria = new BankEntity();
                    criteria.setBankID(bank.getBankID());
                    dataServer.beginTransaction();
                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();

                    for (SelectItem item : bankItemList) {
                        if (Integer.parseInt(item.getValue().toString()) == bank.getBankID()) {
                            bankItemList.remove(item);
                            break;
                        }
                    }
                }

                bankList.remove(bank);
                bank = new BankEntity();
                applicationMessageBean.insertMessage("Bank has been deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String findBankByCriteria() {

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        bankList.clear();
        boolean hasCriteria = false;

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        BankEntity criteria = new BankEntity();
        criteria.setQueryOperators(new BankFieldOperator());

        if (bank.getBankCode() != null && !bank.getBankCode().trim().isEmpty()) {
            ((BankFieldOperator) criteria.getQueryOperators()).setBankCode(QueryOperators.LIKE);
            criteria.setBankCode("%" + bank.getBankCode() + "%");
            hasCriteria = true;
        }
        if (bank.getBankName() != null && !bank.getBankName().trim().isEmpty()) {
            ((BankFieldOperator) criteria.getQueryOperators()).setBankName(QueryOperators.LIKE);
            criteria.setBankName("%" + bank.getBankName() + "%");
            hasCriteria = true;
        }

        try {
            if (hasCriteria) {
                List<AbstractEntity> entityList = dataServer.findData(criteria);

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
                        menuManagerBean.getSystemMap().get(MenuManagerBean.BANKS_MENU_ITEM));

                BankEntity businessDivisionEntity = null;
                for (AbstractEntity entity : entityList) {
                    businessDivisionEntity = (BankEntity) entity;
                    businessDivisionEntity.setActivated(businessDivisionEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                    bankList.add(businessDivisionEntity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public BankEntity findBankByBankID(long bankID) {
        BankEntity bankData = null;
        for (BankEntity bank : getBankList()) {
            if (bank.getBankID() == bankID) {
                bankData = bank;
                break;
            }
        }

        return bankData;
    }

    public BankEntity findBankByBankName(String bankName) {
        BankEntity bankData = null;
        for (BankEntity bank : getBankList()) {
            if (bank.getBankName().trim().equals(bankName.trim())) {
                bankData = bank;
                break;
            }
        }

        return bankData;
    }

    public BankEntity findBankByBankCode(String bankCode) {
        BankEntity bankData = null;

        for (BankEntity bank : getBankList()) {
            try {
                if (bank.getBankID() == Long.parseLong(bankCode)) {
                    bankData = bank;
                    break;
                }
            } catch (Exception ex) {

            }
        }

        return bankData;
    }

    public String clearCache() {
        bank = new BankEntity();

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

        if (bankList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    bankList.get(0).getActionTrail());
            if (batEntity != null) {
                loadBankData(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadBankData(batEntity);
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

        if (bankList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(bankList.get(0).getActionTrail());
            if (batEntity != null) {
                loadBankData(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadBankData(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.BANK);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(bank.getBankID());
        businessActionTrailBean.loadBusinessActionTrailList();

        if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
            businessActionTrailBean.setBusinessActionTrail(
                    businessActionTrailBean.getBusinessActionTrailList().get(
                            businessActionTrailBean.getBusinessActionTrailList().size() - 1));

            loadBankData((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            outcome = "bankaudittrail.jsf";
        } else {
            applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    public List<SelectItem> getBankItemList() {
        return bankItemList;
    }

    public void setBankItemList(List<SelectItem> bankItemList) {
        this.bankItemList = bankItemList;
    }

    public List<BankEntity> getBankList() {
        return bankList;
    }

    public void setBankList(List<BankEntity> bankList) {
        this.bankList = bankList;
    }

    /**
     * @return the bank
     */
    public BankEntity getBank() {
        return bank;
    }

    /**
     * @param bank the bank to set
     */
    public void setBank(BankEntity bank) {
        this.bank = bank;
    }

    /**
     * @return the bankBankItemList
     */
    public List<SelectItem> getBankBankItemList() {
        return bankBankItemList;
    }

    /**
     * @param bankBankItemList the bankBankItemList to set
     */
    public void setBankBankItemList(List<SelectItem> bankBankItemList) {
        this.bankBankItemList = bankBankItemList;
    }

    /**
     * @return the selectedBankItemList
     */
    public List<SelectItem> getSelectedBankItemList() {
        return selectedBankItemList;
    }

    /**
     * @param selectedBankItemList the selectedBankItemList to set
     */
    public void setSelectedBankItemList(List<SelectItem> selectedBankItemList) {
        this.selectedBankItemList = selectedBankItemList;
    }

    /**
     * @return the searchBankID
     */
    public long getSearchBankID() {
        return searchBankID;
    }

    /**
     * @param searchBankID the searchBankID to set
     */
    public void setSearchBankID(long searchBankID) {
        this.searchBankID = searchBankID;
    }
}
