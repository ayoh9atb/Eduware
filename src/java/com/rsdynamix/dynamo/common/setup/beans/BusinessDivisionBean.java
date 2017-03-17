/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.dynamo.common.setup.beans;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.setup.entities.BusinessDivisionEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.tns.util.Constants;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamics.projects.field.operators.BusinessDivisionFieldOperator;
import com.rsdynamics.projects.query.operators.QueryOperators;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "businessDivisionBean")
public class BusinessDivisionBean implements Serializable {

    public static final String LIFE_DIVISION = "LIFE";
    //
    public static final String GENERAL_DIVISION = "GENERAL";
    //
    private BusinessDivisionEntity businessDivision;
    private List<BusinessDivisionEntity> businessDivisionList;
    private List<SelectItem> businessDivisionItemList;

    public BusinessDivisionBean() {
        businessDivision = new BusinessDivisionEntity();
        loadBusinessDivisionList();
    }

    public void loadBusinessDivisionList() {
        if ((businessDivisionList == null) || (businessDivisionList.size() == 0)) {
            reloadBusinessDivisionList();
        }
    }

    public void reloadBusinessDivisionList() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {

            businessDivisionList = new ArrayList<BusinessDivisionEntity>();
            businessDivisionItemList = new ArrayList<SelectItem>();

            FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

            BusinessDivisionEntity criteria = new BusinessDivisionEntity();
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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.DIVISION_MENU_ITEM));

            for (AbstractEntity entity : entityList) {
                BusinessDivisionEntity bizDivEntity = (BusinessDivisionEntity) entity;

                bizDivEntity.setActivated(bizDivEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());
                businessDivisionList.add(bizDivEntity);
                addToSelectItemList(bizDivEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public void loadBusinessDivisionList(BusinessActionTrailEntity businessActionTrail) {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            businessDivisionList = new ArrayList<BusinessDivisionEntity>();
            businessDivisionItemList = new ArrayList<SelectItem>();

            FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

            BusinessDivisionEntity criteria = new BusinessDivisionEntity();
            List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());

            for (AbstractEntity entity : entityList) {
                BusinessDivisionEntity bizDivEntity = (BusinessDivisionEntity) entity;
                bizDivEntity.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                businessDivisionList.add(bizDivEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public void addToSelectItemList(BusinessDivisionEntity entity) {
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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.DIVISION_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getBusinessDivisionID());
                item.setLabel(entity.getBusinessDivisionName());
                businessDivisionItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getBusinessDivisionID());
            item.setLabel(entity.getBusinessDivisionName());
            businessDivisionItemList.add(item);
        }
    }

    public String addBusinessDivision() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (businessDivision.getBusinessDivisionName() == null || businessDivision.getBusinessDivisionName().trim().isEmpty()) {
            applicationMessageBean.insertMessage("Specify Business Division", MessageType.ERROR_MESSAGE);
            return "";
        }

        if (!businessDivisionList.contains(businessDivision)) {
            businessDivisionList.add(businessDivision);
        } else {
            int index = businessDivisionList.indexOf(businessDivision);
            businessDivisionList.set(index, businessDivision);
        }
        businessDivision = new BusinessDivisionEntity();

        return "";
    }

    public String saveBusinessDivisionList() {
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
            for (BusinessDivisionEntity bizDivision : businessDivisionList) {
                if (bizDivision.getBusinessDivisionID() == 0) {
                    int bizDivisionID = Integer.parseInt(appPropBean.getValue(
                            FinultimateConstants.BIZ_DIVISION_ID,
                            FinultimateConstants.ONE_STR, true));
                    bizDivision.setBusinessDivisionID(bizDivisionID);

                    dataServer.setSelectedTnsName(Constants.FINULTIMATE_PU);
                    dataServer.saveData(bizDivision);
                } else {
                    dataServer.setSelectedTnsName(Constants.FINULTIMATE_PU);
                    dataServer.updateData(bizDivision);
                }
            }
            applicationMessageBean.insertMessage("Business Division has been saved", MessageType.SUCCESS_MESSAGE);
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.DIVISION_MENU_ITEM));

        if (businessDivision.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((businessDivision.getApprovalStatusID() == 0) || (businessDivision.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(businessDivision.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        businessDivision.setApprovalStatusID(businessDivision.getApprovalStatusID() + 1);
                        businessDivision.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(businessDivision);
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
                } else if (businessDivision.getApprovalStatusID()
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

    public String deleteBusinessDivision() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (businessDivisionList.contains(businessDivision)) {

                if (businessDivision.getBusinessDivisionID() > 0) {
                    if (businessDivision.isActivated()) {
                        applicationMessageBean.insertMessage("This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                        return "";
                    }
                    FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                    BusinessDivisionEntity criteria = new BusinessDivisionEntity();
                    criteria.setBusinessDivisionID(businessDivision.getBusinessDivisionID());
                    dataServer.beginTransaction();
                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();

                    for (SelectItem item : businessDivisionItemList) {
                        if (Integer.parseInt(item.getValue().toString()) == businessDivision.getBusinessDivisionID()) {
                            businessDivisionItemList.remove(item);
                            break;
                        }
                    }
                }

                businessDivisionList.remove(businessDivision);
                businessDivision = new BusinessDivisionEntity();
                applicationMessageBean.insertMessage("Business Division has been deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String findBusinessDivisionByCriteria() {
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

        businessDivisionList.clear();
        boolean hasCriteria = false;

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        BusinessDivisionEntity criteria = new BusinessDivisionEntity();
        criteria.setQueryOperators(new BusinessDivisionFieldOperator());

        if (!businessDivision.getBusinessDivisionName().trim().isEmpty()) {
            ((BusinessDivisionFieldOperator) criteria.getQueryOperators()).setBusinessDivisionName(QueryOperators.LIKE);
            criteria.setBusinessDivisionName("%" + businessDivision.getBusinessDivisionName() + "%");
            hasCriteria = true;
        }

        try {
            if (hasCriteria) {
                List<AbstractEntity> entityList = dataServer.findData(criteria);
                BusinessDivisionEntity businessDivisionEntity = null;

                MenuEntity privilege = privilegeBean.findPrivilegeByName(
                        menuManagerBean.getSystemMap().get(MenuManagerBean.DIVISION_MENU_ITEM));

                for (AbstractEntity entity : entityList) {
                    businessDivisionEntity = (BusinessDivisionEntity) entity;

                    businessDivisionEntity.setActivated(businessDivisionEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                    businessDivisionList.add(businessDivisionEntity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public BusinessDivisionEntity businessDivisionItemSelected(ValueChangeEvent vce) {
        BusinessDivisionEntity division = null;

        if ((vce != null) && (vce.getNewValue() != null)) {
            int divisionID = Integer.parseInt(vce.getNewValue().toString());
            division = findBusinessDivisionByID(divisionID);
        }

        return division;
    }

    public void businessDivisionSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
        businessDivision = businessDivisionList.get(rowIndex);
    }

    public BusinessDivisionEntity findBusinessDivisionByID(long divisionID) {
        BusinessDivisionEntity entity = null;

        for (BusinessDivisionEntity divEntity : businessDivisionList) {
            if (divEntity.getBusinessDivisionID() == divisionID) {
                entity = divEntity;
                break;
            }
        }

        return entity;
    }

    public String clearCache() {
        businessDivision = new BusinessDivisionEntity();

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

        if (businessDivisionList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    businessDivisionList.get(0).getActionTrail());
            if (batEntity != null) {
                loadBusinessDivisionList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadBusinessDivisionList(batEntity);
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

        if (businessDivisionList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean
                    .loadNextHistoricalState(businessDivisionList.get(0).getActionTrail());
            if (batEntity != null) {
                loadBusinessDivisionList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadBusinessDivisionList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.BUSINESS_DIVISION);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(businessDivision.getBusinessDivisionID());
        businessActionTrailBean.loadBusinessActionTrailList();

        if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
            businessActionTrailBean.setBusinessActionTrail(
                    businessActionTrailBean.getBusinessActionTrailList().get(
                            businessActionTrailBean.getBusinessActionTrailList().size() - 1));

            loadBusinessDivisionList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            outcome = "businessdivisionaudittrail.jsf";
        } else {
            applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    public BusinessDivisionEntity getBusinessDivision() {
        return this.businessDivision;
    }

    public void setBusinessDivision(BusinessDivisionEntity businessDivision) {
        this.businessDivision = businessDivision;
    }

    public List<BusinessDivisionEntity> getBusinessDivisionList() {
        return this.businessDivisionList;
    }

    public void setBusinessDivisionList(List<BusinessDivisionEntity> businessDivisionList) {
        this.businessDivisionList = businessDivisionList;
    }

    public List<SelectItem> getBusinessDivisionItemList() {
        return this.businessDivisionItemList;
    }

    public void setBusinessDivisionItemList(List<SelectItem> businessDivisionItemList) {
        this.businessDivisionItemList = businessDivisionItemList;
    }

}
