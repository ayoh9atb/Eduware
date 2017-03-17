/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.dynamo.common.setup.beans;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import com.rsdynamics.projects.field.operators.PayoutChannelFieldOperator;
import com.rsdynamics.projects.query.operators.QueryOperators;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.dynamo.setup.entities.PayoutChannelEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.tns.util.Constants;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author S3-Systems-NG. 010
 */
@SessionScoped
@ManagedBean(name = "payoutChannelBean")
public class PayoutChannelBean implements Serializable {

    private PayoutChannelEntity payoutChannel;
    private List<PayoutChannelEntity> payoutChannelList;
    private List<SelectItem> selectedPayoutChannelItemList;

    public PayoutChannelBean() {
        payoutChannel = new PayoutChannelEntity();
        reloadPayoutChannelList();
    }

    public void loadPayoutChannelList() {
        if ((payoutChannelList == null) || (payoutChannelList.size() == 0)) {
            reloadPayoutChannelList();
        }
    }

    public void reloadPayoutChannelList() {

        payoutChannelList = new ArrayList<PayoutChannelEntity>();
        selectedPayoutChannelItemList = new ArrayList<SelectItem>();

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
            PayoutChannelEntity criteria = new PayoutChannelEntity();
            List<AbstractEntity> entityList = dataServer.findData(criteria);

            for (AbstractEntity entity : entityList) {
                PayoutChannelEntity bizDivEntity = (PayoutChannelEntity) entity;
                payoutChannelList.add(bizDivEntity);
                addToSelectItemList(bizDivEntity);

                Collections.sort(selectedPayoutChannelItemList, new Comparator<SelectItem>() {
                    public int compare(SelectItem o1, SelectItem o2) {
                        return o1.getLabel().compareTo(o2.getLabel());
                    }
                });

                Collections.sort(payoutChannelList, new Comparator<PayoutChannelEntity>() {
                    public int compare(PayoutChannelEntity o1, PayoutChannelEntity o2) {
                        return o1.getPayoutChannelDesc().compareTo(o2.getPayoutChannelDesc());
                    }
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public void loadPayoutChannelList(BusinessActionTrailEntity businessActionTrail) {
        payoutChannelList = new ArrayList<PayoutChannelEntity>();

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
            PayoutChannelEntity criteria = new PayoutChannelEntity();
            List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());

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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.COMPANY_ID_MENU_ITEM));

            for (AbstractEntity entity : entityList) {
                PayoutChannelEntity bizDivEntity = (PayoutChannelEntity) entity;
                bizDivEntity.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                bizDivEntity.setActivated(bizDivEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                payoutChannelList.add(bizDivEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public void addToSelectItemList(PayoutChannelEntity payoutChannel) {
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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.PAYOUT_CHANNEL_MENU_ITEM));

            if (payoutChannel.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem agencyItem = new SelectItem();
                agencyItem.setValue(payoutChannel.getPayoutChannelID());
                agencyItem.setLabel(payoutChannel.getPayoutChannelName());
                selectedPayoutChannelItemList.add(agencyItem);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem agencyItem = new SelectItem();
            agencyItem.setValue(payoutChannel.getPayoutChannelID());
            agencyItem.setLabel(payoutChannel.getPayoutChannelName());
            selectedPayoutChannelItemList.add(agencyItem);
        }
    }

    public String addPayoutChannel() {

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if ((payoutChannel == null) || (payoutChannel.getPayoutChannelName() == null)
                || (payoutChannel.getPayoutChannelName().trim().isEmpty())) {
            applicationMessageBean.insertMessage("Specify Payout Channel Name", MessageType.ERROR_MESSAGE);
            return "";
        }

        if ((payoutChannel != null) && (payoutChannel.getPayoutChannelName() != null)
                && (!payoutChannel.getPayoutChannelName().trim().equals(""))) {

            if (!payoutChannelList.contains(payoutChannel)) {
                payoutChannelList.add(payoutChannel);
            } else {
                int index = payoutChannelList.indexOf(payoutChannel);
                payoutChannelList.set(index, payoutChannel);
            }
            payoutChannel = new PayoutChannelEntity();
        }

        return "";
    }

    public String savePayoutChannelList() {
        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{applicationScope.applicationPropertyBean}",
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

        try {
            dataServer.beginTransaction();

            for (PayoutChannelEntity identity : payoutChannelList) {
                if (identity.getPayoutChannelID() == 0) {
                    int identityID = Integer.parseInt(appPropBean.getValue(
                            FinultimateConstants.PAYOUT_CHANNEL_ID,
                            FinultimateConstants.ONE_STR, true));
                    identity.setPayoutChannelID(identityID);

                    dataServer.setSelectedTnsName(Constants.FINULTIMATE_PU);
                    dataServer.saveData(identity);
                } else {
                    dataServer.setSelectedTnsName(Constants.FINULTIMATE_PU);
                    dataServer.updateData(identity);
                }
            }
            applicationMessageBean.insertMessage("Payout Channel saved", MessageType.SUCCESS_MESSAGE);

            dataServer.endTransaction();
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.PAYOUT_CHANNEL_MENU_ITEM));

        if (payoutChannel.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((payoutChannel.getApprovalStatusID() == 0) || (payoutChannel.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(payoutChannel.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        payoutChannel.setApprovalStatusID(payoutChannel.getApprovalStatusID() + 1);
                        payoutChannel.setApprover(userManagerBean.getUserAccount().getUserName());

                        try {
                            dataServer.beginTransaction();
                            dataServer.updateData(payoutChannel);
                            dataServer.endTransaction();
                        } catch (SQLException ex) {
                            dataServer.rollbackTransaction();
                            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

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
                } else if (payoutChannel.getApprovalStatusID()
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

    public String deletePayoutChannel() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (payoutChannelList.contains(payoutChannel)) {

                if (payoutChannel.getPayoutChannelID() > 0) {
                    if (payoutChannel.isActivated()) {
                        applicationMessageBean.insertMessage(
                                "This Item has been Activated. Deletion of Activated Items is not allowed",
                                MessageType.ERROR_MESSAGE);

                        return "";
                    }
                    FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                    PayoutChannelEntity criteria = new PayoutChannelEntity();
                    criteria.setPayoutChannelID(payoutChannel.getPayoutChannelID());
                    dataServer.beginTransaction();
                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();

                    for (SelectItem item : selectedPayoutChannelItemList) {
                        if (Integer.parseInt(item.getValue().toString()) == payoutChannel.getPayoutChannelID()) {
                            selectedPayoutChannelItemList.remove(item);
                            break;
                        }
                    }
                }

                payoutChannelList.remove(payoutChannel);
                payoutChannel = new PayoutChannelEntity();
                applicationMessageBean.insertMessage(
                        "Payout Channel deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public PayoutChannelEntity payoutChannelItemSelected(ValueChangeEvent vce) {
        PayoutChannelEntity identity = null;

        if ((vce != null) && (vce.getNewValue() != null)) {
            int identityID = Integer.parseInt(vce.getNewValue().toString());
            identity = findPayoutChannelByID(identityID);
        }

        return identity;
    }

    public void payoutChannelSelected(ValueChangeEvent event) {
        if (event.getComponent() instanceof HtmlSelectBooleanCheckbox) {
            int rowIndex = CommonBean.getCheckBoxEventRowIndex(event);
            payoutChannel = payoutChannelList.get(rowIndex);
            payoutChannel.setSelected(Boolean.parseBoolean(event.getNewValue().toString()));
            if (payoutChannel.isSelected()) {
                CommonBean.deselectOtherItems(payoutChannel, payoutChannelList);
            } else {
                payoutChannel = new PayoutChannelEntity();
            }
        }

    }

    public PayoutChannelEntity findPayoutChannelByID(long pyoutChannelID) {
        PayoutChannelEntity entity = null;

        for (PayoutChannelEntity divEntity : payoutChannelList) {
            if (divEntity.getPayoutChannelID() == pyoutChannelID) {
                entity = divEntity;
                break;
            }
        }

        return entity;
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.PAYOUT_CHANNEL);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(payoutChannel.getPayoutChannelID());
        businessActionTrailBean.loadBusinessActionTrailList();

        if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
            businessActionTrailBean.setBusinessActionTrail(
                    businessActionTrailBean.getBusinessActionTrailList().get(
                            businessActionTrailBean.getBusinessActionTrailList().size() - 1));

            loadPayoutChannelList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            outcome = "payoutchannelaudittrail.jsf";
        } else {
            applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    public String findPayoutChannelByCriteria() {
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

        payoutChannelList.clear();

        boolean hasCriteria = false;

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        PayoutChannelEntity criteria = new PayoutChannelEntity();
        criteria.setQueryOperators(new PayoutChannelFieldOperator());

        if (payoutChannel.getPayoutChannelID() > 0) {
            ((PayoutChannelFieldOperator) criteria.getQueryOperators()).setPayoutChannelID(QueryOperators.EQUALS);
            criteria.setPayoutChannelID(payoutChannel.getPayoutChannelID());
            hasCriteria = true;
        }
        if (payoutChannel.getPayoutChannelName() != null && !payoutChannel.getPayoutChannelName().trim().isEmpty()) {
            ((PayoutChannelFieldOperator) criteria.getQueryOperators()).setPayoutChannelName(QueryOperators.LIKE);
            criteria.setPayoutChannelName("%" + payoutChannel.getPayoutChannelName() + "%");
            hasCriteria = true;
        }

        try {
            if (hasCriteria) {
                List<AbstractEntity> entityList = dataServer.findData(criteria);

                MenuEntity privilege = privilegeBean.findPrivilegeByName(
                        menuManagerBean.getSystemMap().get(MenuManagerBean.BANKS_MENU_ITEM));

                PayoutChannelEntity businessDivisionEntity = null;
                for (AbstractEntity entity : entityList) {
                    businessDivisionEntity = (PayoutChannelEntity) entity;
                    businessDivisionEntity.setActivated(businessDivisionEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                    payoutChannelList.add(businessDivisionEntity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

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

        if (payoutChannelList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    payoutChannelList.get(0).getActionTrail());
            if (batEntity != null) {
                loadPayoutChannelList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadPayoutChannelList(batEntity);
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

        if (payoutChannelList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(payoutChannelList.get(0).getActionTrail());
            if (batEntity != null) {
                loadPayoutChannelList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadPayoutChannelList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        }

        return "";
    }

    public PayoutChannelEntity getPayoutChannel() {
        return this.payoutChannel;
    }

    public void setPayoutChannel(PayoutChannelEntity payoutChannel) {
        this.payoutChannel = payoutChannel;
    }

    public List<PayoutChannelEntity> getPayoutChannelList() {
        return this.payoutChannelList;
    }

    public void setPayoutChannelList(List<PayoutChannelEntity> payoutChannelList) {
        this.payoutChannelList = payoutChannelList;
    }

    /**
     * @return the selectedPayoutChannelItemList
     */
    public List<SelectItem> getSelectedPayoutChannelItemList() {
        return selectedPayoutChannelItemList;
    }

    /**
     * @param selectedPayoutChannelItemList the selectedPayoutChannelItemList to
     * set
     */
    public void setSelectedPayoutChannelItemList(List<SelectItem> selectedPayoutChannelItemList) {
        this.selectedPayoutChannelItemList = selectedPayoutChannelItemList;
    }

}
