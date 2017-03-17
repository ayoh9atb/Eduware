/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.dynamo.common.setup.beans;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.setup.entities.BranchEntity;
import com.rsdynamix.dynamo.setup.entities.CityEntity;
import com.rsdynamix.dynamo.setup.entities.CountryEntity;
import com.rsdynamix.dynamo.setup.entities.StateEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamics.projects.field.operators.BranchFieldOperator;
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
import com.rsdynamix.tns.util.Constants;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
@ManagedBean(name = "branchBean")
public class BranchBean implements Serializable {

//    @EJB
//    FinultimatePersistenceRemote dataServer;
    //
    private BranchEntity branch;
    private List<BranchEntity> branchList;
    private List<BranchEntity> filteredBranchList;
    private List<SelectItem> branchItemList;

    public BranchBean() {
        branch = new BranchEntity();
        branchList = new ArrayList<BranchEntity>();
        filteredBranchList = new ArrayList<BranchEntity>();
        branchItemList = new ArrayList<SelectItem>();

        loadBranchList();
    }

    public String loadBranchList() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        AddressManagerBean addressBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.addressManagerBean}", AddressManagerBean.class);
        if (addressBean == null) {
            addressBean = new AddressManagerBean();
            CommonBean.setBeanToContext(
                    "#{sessionScope.addressManagerBean}", AddressManagerBean.class, addressBean);
        }

        try {
            branchItemList.clear();
            branchList.clear();

            FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
            BranchEntity criteria = new BranchEntity();
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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.BRANCHES_MENU_ITEM));

            for (AbstractEntity entity : entityList) {
                BranchEntity branchEntity = (BranchEntity) entity;

                branchEntity.setActivated(branchEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                CountryEntity country = addressBean.findCountryByID(branchEntity.getCountryID());
                if (country != null) {
                    branchEntity.setCountryName(country.getCountryDesc());
                }

                StateEntity state = addressBean.findStateByCode(
                        branchEntity.getStateCode(), branchEntity.getCountryID());
                if (state != null) {
                    branchEntity.setStateDesc(state.getStateDesc());
                }

                CityEntity city = addressBean.findCityByID(branchEntity.getCityID());
                if (city != null) {
                    branchEntity.setCityDesc(city.getCityDesc());
                }

                branchList.add(branchEntity);
                addToSelectItemList(branchEntity);
            }

            Collections.sort(branchList, new Comparator<BranchEntity>() {

                public int compare(BranchEntity o1, BranchEntity o2) {
                    //return o1.getCountryDesc().compareTo(o2.getCountryDesc());
                    return new Long(o1.getOrderID()).compareTo(new Long(o2.getOrderID()));
                }
            });

            Collections.sort(branchItemList, new Comparator<SelectItem>() {

                public int compare(SelectItem o1, SelectItem o2) {
                    //return o1.getLabel().compareTo(o2.getLabel());
                    return new Long(o1.getDescription()).compareTo(new Long(o2.getDescription()));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String loadBranchList(BusinessActionTrailEntity businessActionTrail) {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        AddressManagerBean addressBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.addressManagerBean}", AddressManagerBean.class);
        if (addressBean == null) {
            addressBean = new AddressManagerBean();
            CommonBean.setBeanToContext(
                    "#{sessionScope.addressManagerBean}", AddressManagerBean.class, addressBean);
        }

        try {
            branchItemList.clear();
            branchList.clear();

            FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
            BranchEntity criteria = new BranchEntity();
            List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
            for (AbstractEntity entity : entityList) {
                BranchEntity branchEntity = (BranchEntity) entity;
                branchEntity.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                CountryEntity country = addressBean.findCountryByID(branchEntity.getCountryID());
                if (country != null) {
                    branchEntity.setCountryName(country.getCountryDesc());
                }

                StateEntity state = addressBean.findStateByCode(
                        branchEntity.getStateCode(), branchEntity.getCountryID());
                if (state != null) {
                    branchEntity.setStateDesc(state.getStateDesc());
                }

                CityEntity city = addressBean.findCityByID(branchEntity.getCityID());
                if (city != null) {
                    branchEntity.setCityDesc(city.getCityDesc());
                }

                branchList.add(branchEntity);

                Collections.sort(branchList, new Comparator<BranchEntity>() {

                    public int compare(BranchEntity o1, BranchEntity o2) {
                        return new Long(o1.getOrderID()).compareTo(new Long(o2.getOrderID()));
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void addToSelectItemList(BranchEntity entity) {
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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.BRANCHES_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getBranchID());
                item.setLabel(entity.getBranchName());
                item.setDescription(String.valueOf(entity.getOrderID()));
                branchItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getBranchID());
            item.setLabel(entity.getBranchName());
            item.setDescription(String.valueOf(entity.getOrderID()));
            branchItemList.add(item);
        }
    }

    public String addBranch() {

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (branch.getBranchName() == null || branch.getBranchName().trim().isEmpty()
                || branch.getBranchCode() == null || branch.getBranchCode().trim().isEmpty()) {
            applicationMessageBean.insertMessage("Specify Branch Name and Branch Code", MessageType.ERROR_MESSAGE);
            return "";
        }

        if (branch.getBranchCode() != null && !branch.getBranchCode().trim().isEmpty()
                && branch.getBranchCode().trim().length() > 6) {
            applicationMessageBean.insertMessage("Branch Code cannot be more than 6 characters", MessageType.ERROR_MESSAGE);
            return "";
        }

        if (branch != null && branch.getBranchName() != null && !branch.getBranchCode().trim().equals("")) {

            AddressManagerBean addressBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.addressManagerBean}", AddressManagerBean.class);
            if (addressBean == null) {
                addressBean = new AddressManagerBean();
                CommonBean.setBeanToContext(
                        "#{sessionScope.addressManagerBean}", AddressManagerBean.class, addressBean);
            }

            if (!getBranchList().contains(getBranch())) {
                CountryEntity country = addressBean.findCountryByID(branch.getCountryID());
                if (country != null) {
                    branch.setCountryName(country.getCountryDesc());
                }

                StateEntity state = addressBean.findStateByCode(
                        branch.getStateCode(), branch.getCountryID());
                if (state != null) {
                    branch.setStateDesc(state.getStateDesc());
                }

                CityEntity city = addressBean.findCityByID(branch.getCityID());
                if (city != null) {
                    branch.setCityDesc(city.getCityDesc());
                }

//                SelectItem item = new SelectItem();
//                item.setLabel(branch.getBranchName());
//                item.setValue(branch.getBranchID());
//                branchItemList.add(item);
                getBranchList().add(branch);
            } else {
                CountryEntity country = addressBean.findCountryByID(branch.getCountryID());
                if (country != null) {
                    branch.setCountryName(country.getCountryDesc());
                }

                StateEntity state = addressBean.findStateByCode(
                        branch.getStateCode(), branch.getCountryID());
                if (state != null) {
                    branch.setStateDesc(state.getStateDesc());
                }

                CityEntity city = addressBean.findCityByID(branch.getCityID());
                if (city != null) {
                    branch.setCityDesc(city.getCityDesc());
                }

                int index = getBranchList().indexOf(branch);
                getBranchList().set(index, branch);
            }
            setBranch(new BranchEntity());
        }

        return "";
    }

    public void branchSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        branch = branchList.get(rowIndex);
    }

    public String saveBranchList() {
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
            for (BranchEntity branch : branchList) {
                if (branch.getBranchID() == 0) {
                    int branchID = Integer.parseInt(appPropBean.getValue(
                            FinultimateConstants.BRANCH_ID,
                            FinultimateConstants.ONE_STR, true));
                    branch.setBranchID(branchID);

                    dataServer.saveData(branch);

//                    SelectItem item = new SelectItem();
//                    item.setLabel(branch.getBranchName());
//                    item.setValue(branch.getBranchID());
//                    branchItemList.add(item);
                } else {
                    dataServer.updateData(branch);
                }
            }
            applicationMessageBean.insertMessage("Branch details have been saved", MessageType.SUCCESS_MESSAGE);
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.BRANCHES_MENU_ITEM));

        if (branch.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((branch.getApprovalStatusID() == 0) || (branch.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(branch.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        branch.setApprovalStatusID(branch.getApprovalStatusID() + 1);
                        branch.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(branch);

                            applicationMessageBean.insertMessage("Operation Activated Successfully!", MessageType.SUCCESS_MESSAGE);
                        } catch (SQLException ex) {
                            dataServer.rollbackTransaction();
                            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        dataServer.endTransaction();
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
                } else if (branch.getApprovalStatusID()
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

    public String deleteBranch() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (getBranchList().contains(branch)) {

                if (branch.getBranchID() > 0) {
                    if (branch.isActivated()) {
                        applicationMessageBean.insertMessage(
                                "This Item has been Activated. Deletion of Activated Items is not allowed",
                                MessageType.ERROR_MESSAGE);
                        return "";
                    }
                    FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                    dataServer.beginTransaction();

                    BranchEntity criteria = new BranchEntity();
                    criteria.setBranchID(branch.getBranchID());
                    dataServer.deleteData(criteria);

                    dataServer.endTransaction();

                    for (SelectItem item : getBranchItemList()) {
                        if (Integer.parseInt(item.getValue().toString()) == branch.getBranchID()) {
                            getBranchItemList().remove(item);
                            break;
                        }
                    }
                }

                getBranchList().remove(branch);
                branch = new BranchEntity();
                applicationMessageBean.insertMessage("Branch has been deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String clearBranch() {
        branch = new BranchEntity();
        branchList = new ArrayList<BranchEntity>();
        branchItemList = new ArrayList<SelectItem>();

        return "";
    }

    public BranchEntity findBranch(int branchID) {
        BranchEntity branch = null;

        for (BranchEntity entity : branchList) {
            if (entity.getBranchID() == branchID) {
                branch = entity;
                break;
            }
        }

        return branch;
    }

    public String findBranchByCriteria() {
        AddressManagerBean addressBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.addressManagerBean}", AddressManagerBean.class);
        if (addressBean == null) {
            addressBean = new AddressManagerBean();
            CommonBean.setBeanToContext(
                    "#{sessionScope.addressManagerBean}", AddressManagerBean.class, addressBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        branchList.clear();
        boolean hasCriteria = false;

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        BranchEntity criteria = new BranchEntity();
        criteria.setQueryOperators(new BranchFieldOperator());

        if (!branch.getBranchCode().trim().isEmpty()) {
            ((BranchFieldOperator) criteria.getQueryOperators()).setBranchCode(QueryOperators.LIKE);
            criteria.setBranchCode("%" + branch.getBranchCode() + "%");
            hasCriteria = true;
        }
        if (!branch.getBranchName().trim().isEmpty()) {
            ((BranchFieldOperator) criteria.getQueryOperators()).setBranchName(QueryOperators.LIKE);
            criteria.setBranchName("%" + branch.getBranchName() + "%");
            hasCriteria = true;
        }
        if (branch.getCityID() > 0) {
            ((BranchFieldOperator) criteria.getQueryOperators()).setCityID(QueryOperators.EQUALS);
            criteria.setCityID(branch.getCityID());
            hasCriteria = true;
        }
        if (branch.getCountryID() > 0) {
            ((BranchFieldOperator) criteria.getQueryOperators()).setCountryID(QueryOperators.EQUALS);
            criteria.setCountryID(branch.getCountryID());
            hasCriteria = true;
        }
        if (branch.getStateCode() != null && !branch.getStateCode().trim().isEmpty()) {
            ((BranchFieldOperator) criteria.getQueryOperators()).setStateCode(QueryOperators.LIKE);
            criteria.setStateCode(branch.getStateCode());
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
                        menuManagerBean.getSystemMap().get(MenuManagerBean.BRANCHES_MENU_ITEM));

                for (AbstractEntity entity : entityList) {
                    BranchEntity branchEntity = (BranchEntity) entity;

                    branchEntity.setActivated(branchEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                    CountryEntity country = addressBean.findCountryByID(branchEntity.getCountryID());
                    if (country != null) {
                        branchEntity.setCountryName(country.getCountryDesc());
                    }

                    StateEntity state = addressBean.findStateByCode(
                            branchEntity.getStateCode(), branchEntity.getCountryID());
                    if (state != null) {
                        branchEntity.setStateDesc(state.getStateDesc());
                    }

                    CityEntity city = addressBean.findCityByID(branchEntity.getCityID());
                    if (city != null) {
                        branchEntity.setCityDesc(city.getCityDesc());
                    }

                    branchList.add(branchEntity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String clearCache() {
        branch = new BranchEntity();

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

        if (branchList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    branchList.get(0).getActionTrail());
            if (batEntity != null) {
                loadBranchList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadBranchList(batEntity);
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

        if (branchList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(branchList.get(0).getActionTrail());
            if (batEntity != null) {
                loadBranchList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadBranchList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.BRANCH);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(branch.getBranchID());
        businessActionTrailBean.loadBusinessActionTrailList();

        if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
            businessActionTrailBean.setBusinessActionTrail(
                    businessActionTrailBean.getBusinessActionTrailList().get(
                            businessActionTrailBean.getBusinessActionTrailList().size() - 1));

            loadBranchList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            outcome = "branchaudittrail.jsf";
        } else {
            applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    /**
     * @return the branch
     */
    public BranchEntity getBranch() {
        return branch;
    }

    /**
     * @param branch the branch to set
     */
    public void setBranch(BranchEntity branch) {
        this.branch = branch;
    }

    /**
     * @return the branchList
     */
    public List<BranchEntity> getBranchList() {
        return branchList;
    }

    /**
     * @param branchList the branchList to set
     */
    public void setBranchList(List<BranchEntity> branchList) {
        this.branchList = branchList;
    }

    /**
     * @return the branchItemList
     */
    public List<SelectItem> getBranchItemList() {
        return branchItemList;
    }

    /**
     * @param branchItemList the branchItemList to set
     */
    public void setBranchItemList(List<SelectItem> branchItemList) {
        this.branchItemList = branchItemList;
    }

    /**
     * @return the filteredBranchList
     */
    public List<BranchEntity> getFilteredBranchList() {
        return filteredBranchList;
    }

    /**
     * @param filteredBranchList the filteredBranchList to set
     */
    public void setFilteredBranchList(List<BranchEntity> filteredBranchList) {
        this.filteredBranchList = filteredBranchList;
    }

}
