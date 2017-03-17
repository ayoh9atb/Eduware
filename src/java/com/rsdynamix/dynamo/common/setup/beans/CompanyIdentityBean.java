/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.dynamo.common.setup.beans;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.setup.entities.BranchEntity;
import com.rsdynamix.dynamo.setup.entities.CompanyIdentityEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.tns.util.Constants;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
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

/**
 *
 * @author patushie@codrellica
 */
@SessionScoped
@ManagedBean(name = "companyIdentityBean")
public class CompanyIdentityBean implements Serializable {

    private CompanyIdentityEntity companyIdentity;
    private List<CompanyIdentityEntity> companyIdentityList;
    private List<SelectItem> companyIdentityItemList;

    public CompanyIdentityBean() {
        companyIdentity = new CompanyIdentityEntity();
        loadCompanyIdentityList();
    }

    public void loadCompanyIdentityList() {
        if ((companyIdentityList == null) || (companyIdentityList.size() == 0)) {
            companyIdentityList = new ArrayList<CompanyIdentityEntity>();
            companyIdentityItemList = new ArrayList<SelectItem>();

            BranchBean branchBean = (BranchBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.branchBean}", BranchBean.class);
            if (branchBean == null) {
                branchBean = new BranchBean();
                CommonBean.setBeanToContext("#{sessionScope.branchBean}", BranchBean.class, branchBean);
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
                CompanyIdentityEntity criteria = new CompanyIdentityEntity();
                List<AbstractEntity> entityList = dataServer.findData(criteria);

                for (AbstractEntity entity : entityList) {
                    CompanyIdentityEntity bizDivEntity = (CompanyIdentityEntity) entity;

                    BranchEntity branch = branchBean.findBranch(bizDivEntity.getHqBranchID());
                    if (branch != null) {
                        bizDivEntity.setHqBranchName(branch.getBranchName());
                    }

                    companyIdentityList.add(bizDivEntity);
                }
                companyIdentity = companyIdentityList.get(0);
            } catch (Exception ex) {
                ex.printStackTrace();
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            }
        }
    }

    public void loadCompanyIdentityList(BusinessActionTrailEntity businessActionTrail) {
        companyIdentityList = new ArrayList<CompanyIdentityEntity>();
        companyIdentityItemList = new ArrayList<SelectItem>();

        BranchBean branchBean = (BranchBean) CommonBean.getBeanFromContext(
                "#{sessionScope.branchBean}", BranchBean.class);
        if (branchBean == null) {
            branchBean = new BranchBean();
            CommonBean.setBeanToContext("#{sessionScope.branchBean}", BranchBean.class, branchBean);
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
            CompanyIdentityEntity criteria = new CompanyIdentityEntity();
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
                CompanyIdentityEntity bizDivEntity = (CompanyIdentityEntity) entity;
                bizDivEntity.setActionTrail((BusinessActionTrailEntity)businessActionTrail.cloneEntity());

                bizDivEntity.setActivated(bizDivEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                BranchEntity branch = branchBean.findBranch(bizDivEntity.getHqBranchID());
                if (branch != null) {
                    bizDivEntity.setHqBranchName(branch.getBranchName());
                }

                companyIdentityList.add(bizDivEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public String addCompanyIdentity() {
        if ((companyIdentity != null) && (companyIdentity.getCompanyName() != null)
                && (!companyIdentity.getCompanyName().trim().equals(""))) {
            BranchBean branchBean = (BranchBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.branchBean}", BranchBean.class);
            if (branchBean == null) {
                branchBean = new BranchBean();
                CommonBean.setBeanToContext("#{sessionScope.branchBean}", BranchBean.class, branchBean);
            }

            BranchEntity branch = branchBean.findBranch(companyIdentity.getHqBranchID());
            if (branch != null) {
                companyIdentity.setHqBranchName(branch.getBranchName());
            }

            if (!companyIdentityList.contains(companyIdentity)) {
                if (companyIdentityList.size() > 0) {
                    companyIdentityList.add(0, companyIdentity);
                } else if (companyIdentityList.size() == 0) {
                    companyIdentityList.add(companyIdentity);
                }
            } else {
                int index = companyIdentityList.indexOf(companyIdentity);
                companyIdentityList.set(index, companyIdentity);
            }
        }

        return "";
    }

    public String saveCompanyIdentityList() {
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

        if ((companyIdentity == null) || (companyIdentity.getCompanyName() == null)
                || (companyIdentity.getCompanyName().trim().isEmpty())) {
            applicationMessageBean.insertMessage("Specify Company Name", MessageType.ERROR_MESSAGE);
            return "";
        }

        addCompanyIdentity();

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        dataServer.beginTransaction();
        try {
            for (CompanyIdentityEntity identity : companyIdentityList) {
                if (identity.getCompanyIdentityID() == 0) {
                    int identityID = Integer.parseInt(appPropBean.getValue(
                            FinultimateConstants.COMPANY_IDENTITY_ID,
                            FinultimateConstants.ONE_STR, true));
                    identity.setCompanyIdentityID(identityID);

                    dataServer.setSelectedTnsName(Constants.FINULTIMATE_PU);
                    dataServer.saveData(identity);
                } else {
                    dataServer.setSelectedTnsName(Constants.FINULTIMATE_PU);
                    dataServer.updateData(identity);
                }
            }
            applicationMessageBean.insertMessage("Company Identity saved", MessageType.SUCCESS_MESSAGE);
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.COMPANY_ID_MENU_ITEM));

        if (companyIdentity.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((companyIdentity.getApprovalStatusID() == 0) || (companyIdentity.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(companyIdentity.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        companyIdentity.setApprovalStatusID(companyIdentity.getApprovalStatusID() + 1);
                        companyIdentity.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(companyIdentity);
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
                } else if (companyIdentity.getApprovalStatusID()
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

    public String deleteCompanyIdentity() {

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (companyIdentityList.contains(companyIdentity)) {

                if (companyIdentity.getCompanyIdentityID() > 0) {
                    if (companyIdentity.isActivated()) {
                        applicationMessageBean.insertMessage("This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                        return "";
                    }
                    FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                    CompanyIdentityEntity criteria = new CompanyIdentityEntity();
                    criteria.setCompanyIdentityID(companyIdentity.getCompanyIdentityID());
                    dataServer.beginTransaction();
                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();

                    for (SelectItem item : companyIdentityItemList) {
                        if (Integer.parseInt(item.getValue().toString()) == companyIdentity.getCompanyIdentityID()) {
                            companyIdentityItemList.remove(item);
                            break;
                        }
                    }
                }

                companyIdentityList.remove(companyIdentity);
                companyIdentity = new CompanyIdentityEntity();
                applicationMessageBean.insertMessage("Company Identity deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public CompanyIdentityEntity companyIdentityItemSelected(ValueChangeEvent vce) {
        CompanyIdentityEntity identity = null;

        if ((vce != null) && (vce.getNewValue() != null)) {
            int identityID = Integer.parseInt(vce.getNewValue().toString());
            identity = findCompanyIdentityByID(identityID);
        }

        return identity;
    }

    public void companyIdentitySelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
        companyIdentity = companyIdentityList.get(rowIndex);
    }

    public CompanyIdentityEntity findCompanyIdentityByID(int identityID) {
        CompanyIdentityEntity entity = null;

        for (CompanyIdentityEntity divEntity : companyIdentityList) {
            if (divEntity.getCompanyIdentityID() == identityID) {
                entity = divEntity;
                break;
            }
        }

        return entity;
    }

    public CompanyIdentityEntity getCompanyIdentity() {
        return this.companyIdentity;
    }

    public void setCompanyIdentity(CompanyIdentityEntity companyIdentity) {
        this.companyIdentity = companyIdentity;
    }

    public List<CompanyIdentityEntity> getCompanyIdentityList() {
        return this.companyIdentityList;
    }

    public void setCompanyIdentityList(List<CompanyIdentityEntity> companyIdentityList) {
        this.companyIdentityList = companyIdentityList;
    }

    public List<SelectItem> getCompanyIdentityItemList() {
        return this.companyIdentityItemList;
    }

    public void setCompanyIdentityItemList(List<SelectItem> companyIdentityItemList) {
        this.companyIdentityItemList = companyIdentityItemList;
    }

}
