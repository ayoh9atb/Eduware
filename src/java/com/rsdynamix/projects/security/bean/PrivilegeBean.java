/*
 * PrivilegeBean.java
 *
 * Created on July 16, 2009, 11:24 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rsdynamix.projects.security.bean;

import com.rsd.projects.menus.FinultimateCommons;
import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.security.entities.AccessType;
import com.rsdynamix.projects.security.entities.ApplicationSystemEntity;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.RoleEntity;
import com.rsdynamix.projects.security.entities.SubsystemEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import com.rsdynamix.projects.security.entities.UserPrivilegeEntity;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlInputText;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "privilegeBean")
public class PrivilegeBean implements Serializable {

    private static final String PRIV_ID_KEY = "priv_id";
    /**/
    private static final String PRIV_ID_DEFAULT_VALUE = "1";
    /**/
    private static final String MENU_ID_KEY = "menu_id";
    /**/
    private static final String MENU_ID_DEFAULT_VALUE = "1";
    /**/
    private static final String USER_PRIV_ID_KEY = "user_priv_id";
    /**/
    private static final String USER_PRIV_ID_DEFAULT_VALUE = "1";
    /**/
    private static final String ROLE_ID_KEY = "u_role_id";
    /**/
    private static final String ROLE_ID_DEFAULT_VALUE = "1";
    /**/
    private static final String SUBSYSTEM_ID_KEY = "subsys_id";
    /**/
    private static final String SUBSYSTEM_ID_DEFAULT_VALUE = "1";
    /**/
    public static final String MENU_ITEM_OF_SUBSYSTEM_KEY = "subsys_menu_item";
    /**/
    public static final String MENU_ITEM_OF_SUBSYSTEM_VALUE = "2";
    /**/
    private MenuEntity appMenu;
    private List<UserPrivilegeEntity> userPrivilegeList;
    private List<UserPrivilegeEntity> rolePrivilegeList;
    private List<UserPrivilegeEntity> selectedRolePrivilegeList;
    private List<SelectItem> roleItemList;
    private List<SelectItem> applicationSystemItemList;
    private List<SelectItem> subsystemItemList;
    private List<SelectItem> menuItemList;
    private List<MenuEntity> menuList;
    private List<MenuEntity> selectedMenuList;

    private RoleEntity role;
    private List<RoleEntity> roleList;
    private RoleEntity roleClone;

    private SubsystemEntity subsystem;
    private List<SubsystemEntity> subsystemList;
    private List<SubsystemEntity> selectedSubsystemList;
    //
    private ApplicationSystemEntity applicationSystem;
    private List<ApplicationSystemEntity> applicationSystemList;
    //
    private int subsystemMenuID;
    private int applicationSystemID;
    private boolean allPrivileges;

    /**
     * Creates a new instance of PrivilegeBean
     */
    public PrivilegeBean() {
        appMenu = new MenuEntity();
        role = new RoleEntity();
        roleClone = new RoleEntity();

        subsystem = new SubsystemEntity();
        roleList = new ArrayList<RoleEntity>();
        subsystemList = new ArrayList<SubsystemEntity>();
        selectedSubsystemList = new ArrayList<SubsystemEntity>();

        applicationSystem = new ApplicationSystemEntity();
        applicationSystemList = new ArrayList<ApplicationSystemEntity>();

        userPrivilegeList = new ArrayList<UserPrivilegeEntity>();
        rolePrivilegeList = new ArrayList<UserPrivilegeEntity>();
        selectedRolePrivilegeList = new ArrayList<UserPrivilegeEntity>();

        roleItemList = new ArrayList<SelectItem>();
        subsystemItemList = new ArrayList<SelectItem>();
        applicationSystemItemList = new ArrayList<SelectItem>();

        menuItemList = new ArrayList<SelectItem>();
        menuList = new ArrayList<MenuEntity>();
        selectedMenuList = new ArrayList<MenuEntity>();

        allPrivileges = false;
        applicationSystemID = 0;

        loadMenuList();
        loadApplicationSystemList();
        loadSubsystemList();
        loadUserPrivilegeList();
        loadRoleList();
        
        List<MenuEntity> entityList = menuList;
        
        menuList = new ArrayList<MenuEntity>();
        selectedMenuList = new ArrayList<MenuEntity>();
        
        for(MenuEntity entity : entityList) {
            SubsystemEntity subsystem = findSubsystemByID(entity.getSubsystemID());
            if(subsystem != null) {
                entity.setSubsystemName(subsystem.getSubsystemName());
                
                menuList.add(entity);
                selectedMenuList.add(entity);
            }
        }
    }

    public void menuSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        appMenu = selectedMenuList.get(rowIndex);

        CommonBean.deselectOtherItems(appMenu, selectedMenuList);
    }

    public String clearSelections() {
        List<MenuEntity> tempList = new ArrayList<MenuEntity>();

        for (MenuEntity priv : selectedMenuList) {
            priv.setReadOnlyRoleItem(false);
            priv.setReadWriteRoleItem(false);

            tempList.add(priv);
        }
        selectedMenuList = tempList;

        return "";
    }

    public void loadMenuList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        menuList = new ArrayList<MenuEntity>();
        selectedMenuList = new ArrayList<MenuEntity>();

        if (menuList.size() == 0) {
            try {
                MenuEntity criteria = new MenuEntity();
                List<AbstractEntity> baseMenuList = dataServer.findData(criteria);

                if ((baseMenuList != null) && (baseMenuList.size() > 0)) {
                    for (AbstractEntity baseMenu : baseMenuList) {
                        MenuEntity entity = (MenuEntity) baseMenu;
                        menuList.add(entity);
                    }
                    
                    List<MenuEntity> tempMenuList = new ArrayList<MenuEntity>();

                    MenuEntity privilege = findPrivilegeByName(
                            menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_PRIVLG_MENU_ITEM));

                    for (MenuEntity entity : menuList) {
                        entity.setActivated(
                                entity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                        selectedMenuList.add(entity);
                        tempMenuList.add(entity);
                        
                        addToSelectItemList(entity);
                    }
                    
                    menuList = tempMenuList;

                    Collections.sort(selectedMenuList, new Comparator<MenuEntity>() {
                        public int compare(MenuEntity o1, MenuEntity o2) {
                            return ((int) o1.getSubsystemName().compareTo(o2.getSubsystemName()));
                        }
                    });
                }

                subsystemMenuID = Integer.parseInt(appPropBean.getValue(
                        MENU_ITEM_OF_SUBSYSTEM_KEY, MENU_ITEM_OF_SUBSYSTEM_VALUE, false));
            } catch (Exception ex) {
                ex.printStackTrace();
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            }
        }
    }

    public void loadMenuList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        menuList = new ArrayList<MenuEntity>();
        selectedMenuList = new ArrayList<MenuEntity>();

        MenuEntity criteria = new MenuEntity();
        List<AbstractEntity> baseMenuList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());

        if ((baseMenuList != null) && (baseMenuList.size() > 0)) {
            for (AbstractEntity baseMenu : baseMenuList) {
                MenuEntity entity = (MenuEntity) baseMenu;
                entity.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                SubsystemEntity subsystem = findSubsystemByID(entity.getSubsystemID());
                if (subsystem != null) {
                    entity.setSubsystemName(subsystem.getSubsystemName());
                }

                menuList.add(entity);
                selectedMenuList.add(entity);

                Collections.sort(selectedMenuList, new Comparator<MenuEntity>() {
                    public int compare(MenuEntity o1, MenuEntity o2) {
                        return ((int) o1.getSubsystemName().compareTo(o2.getSubsystemName()));
                    }
                });
            }
        }

        setSubsystemMenuID(Integer.parseInt(appPropBean.getValue(
                MENU_ITEM_OF_SUBSYSTEM_KEY, MENU_ITEM_OF_SUBSYSTEM_VALUE, false)));
    }

    public void addToSelectItemList(MenuEntity entity) {
        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
            MenuEntity privilege = findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_PRIVLG_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getMenuID());
                item.setLabel(entity.getMenuLabel());
                menuItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getMenuID());
            item.setLabel(entity.getMenuLabel());
            menuItemList.add(item);
        }
    }

    public void loadRoleList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (roleList.size() == 0) {
            try {
                RoleEntity criteria = new RoleEntity();
                List<AbstractEntity> baseRoleList = dataServer.findData(criteria);

                MenuEntity privilege = findPrivilegeByName(
                        menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_SYS_ROLS_MENU_ITEM));

                if ((baseRoleList != null) && (baseRoleList.size() > 0)) {
                    for (AbstractEntity baseRole : baseRoleList) {

                        baseRole.setActivated(((RoleEntity) baseRole).getApprovalStatusID() >= privilege.getApprovedStatusID());
                        if (!roleList.contains((RoleEntity) baseRole)) {
                            RoleEntity role = (RoleEntity) baseRole;
                            List<UserPrivilegeEntity> userPrivList = findUserPrivilegeListByRoleID(role.getRoleID());
                            role.getPrivilegeList().addAll(userPrivList);
                            roleList.add(role);
                            addToSelectItemList(role);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            }
        }
    }

    public void loadRoleList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        roleList = new ArrayList<RoleEntity>();

        RoleEntity criteria = new RoleEntity();
        List<AbstractEntity> baseRoleList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());

        if ((baseRoleList != null) && (baseRoleList.size() > 0)) {
            for (AbstractEntity baseRole : baseRoleList) {
                if (!roleList.contains((RoleEntity) baseRole)) {
                    RoleEntity role = (RoleEntity) baseRole;
                    role.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                    List<UserPrivilegeEntity> userPrivList = findUserPrivilegeListByRoleID(role.getRoleID());

                    role.getPrivilegeList().addAll(userPrivList);
                    roleList.add(role);
                }
            }
        }
    }

    public void addToSelectItemList(RoleEntity entity) {
        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getRoleID());
            item.setLabel(entity.getRoleName());
            roleItemList.add(item);
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
            MenuEntity privilege = findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_SYS_ROLS_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getRoleID());
                item.setLabel(entity.getRoleName());
                roleItemList.add(item);
            }
        }
    }

    public void loadSubsystemList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        subsystemList = new ArrayList<SubsystemEntity>();
        selectedSubsystemList = new ArrayList<SubsystemEntity>();
        subsystemItemList = new ArrayList<SelectItem>();

        try {
            SubsystemEntity criteria = new SubsystemEntity();
            List<AbstractEntity> baseSubsystemList = dataServer.findData(criteria);

            MenuEntity privilege = findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_SUBSYS_MENU_ITEM));

            if ((baseSubsystemList != null) && (baseSubsystemList.size() > 0)) {
                for (AbstractEntity baseSubsystem : baseSubsystemList) {
                    baseSubsystem.setActivated(
                            ((SubsystemEntity) baseSubsystem).getApprovalStatusID() >= privilege.getApprovedStatusID());

                    if (!getSubsystemList().contains((SubsystemEntity) baseSubsystem)) {
                        SubsystemEntity subsys = (SubsystemEntity) baseSubsystem;

                        ApplicationSystemEntity applicSys = findApplicationSystemByID(subsys.getApplicationSystemID());
                        if (applicSys != null) {
                            subsys.setApplicationName(applicSys.getApplicationName());
                        }

                        subsystemList.add(subsys);
                        selectedSubsystemList.add(subsys);

                        addToSelectItemList(subsys);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public void loadSubsystemList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        subsystemList = new ArrayList<SubsystemEntity>();

        SubsystemEntity criteria = new SubsystemEntity();
        List<AbstractEntity> baseSubsystemList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());

        if ((baseSubsystemList != null) && (baseSubsystemList.size() > 0)) {
            for (AbstractEntity baseSubsystem : baseSubsystemList) {
                if (!getSubsystemList().contains((SubsystemEntity) baseSubsystem)) {
                    SubsystemEntity subsys = (SubsystemEntity) baseSubsystem;
                    subsys.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                    getSubsystemList().add(subsys);
                }
            }
        }
    }

    public void addToSelectItemList(SubsystemEntity entity) {
        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
            MenuEntity privilege = findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_SUBSYS_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getSubsystemID());
                item.setLabel(entity.getSubsystemName());
                subsystemItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getSubsystemID());
            item.setLabel(entity.getSubsystemName());
            subsystemItemList.add(item);
        }
    }

    public String addRole() {
        if (!roleList.contains(role)) {
            roleList.add(role);
        } else {
            int index = roleList.indexOf(role);
            roleList.set(index, role);
        }
        role = new RoleEntity();

        return "";
    }

    public String removeRole() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (getRoleList().contains(role)) {
                UserAccountEntity userCriteria = new UserAccountEntity();
                userCriteria.setRoleID(role.getRoleID());

                List<AbstractEntity> entityList = dataServer.findData(userCriteria);
                if ((entityList != null) && (entityList.size() > 0)) {
                    applicationMessageBean.insertMessage("Not Allowed: The Specified User Role Already Has Users Linked To It!",
                            MessageType.ERROR_MESSAGE);
                } else if (entityList.size() == 0) {
                    RoleEntity criteria = new RoleEntity();
                    criteria.setRoleID(role.getRoleID());

                    dataServer.beginTransaction();

                    UserPrivilegeEntity privCriteria = new UserPrivilegeEntity();
                    privCriteria.setRoleID(role.getRoleID());
                    dataServer.deleteData(privCriteria);

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();

                    getRoleList().remove(role);
                    setRole(new RoleEntity());

                    applicationMessageBean.insertMessage("User Role Deleted Successfully", MessageType.SUCCESS_MESSAGE);
                }
            } else {
                applicationMessageBean.insertMessage("User Role doesn't Exist!", MessageType.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            dataServer.rollbackTransaction();
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String addSubsystem() {
        ApplicationSystemEntity applicSystem = findApplicationSystemByID(subsystem.getApplicationSystemID());
        if (applicSystem != null) {
            subsystem.setApplicationName(applicSystem.getApplicationName());
        }

        if (!subsystemList.contains(subsystem)) {
            subsystemList.add(subsystem);
            selectedSubsystemList.add(subsystem);
        } else {
            int index = subsystemList.indexOf(subsystem);
            subsystemList.set(index, subsystem);

            index = selectedSubsystemList.indexOf(subsystem);
            if (index > -1) {
                selectedSubsystemList.set(index, subsystem);
            }
        }

        subsystem = new SubsystemEntity();

        return "";
    }

    public String removeSubsystem() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (getSubsystemList().contains(getSubsystem())) {
                if (getSubsystem().isActivated()) {
                    applicationMessageBean.insertMessage("This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                    return "";
                }
                SubsystemEntity criteria = new SubsystemEntity();
                criteria.setSubsystemID(getSubsystem().getSubsystemID());
                dataServer.beginTransaction();
                dataServer.deleteData(criteria);
                dataServer.endTransaction();

                getSubsystemList().remove(getSubsystem());
                setSubsystem(new SubsystemEntity());

                applicationMessageBean.insertMessage("Subsystem Deleted Successfully", MessageType.SUCCESS_MESSAGE);
            } else {
                applicationMessageBean.insertMessage("Subsystem doesn't Exist!", MessageType.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String copyToSelectedApplication() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if ((getAppMenu().getMenuLabel() == null) || (getAppMenu().getMenuLabel().trim().equals(""))) {
            HtmlInputText menuTxt = (HtmlInputText) FacesContext.getCurrentInstance().
                    getViewRoot().findComponent("menuForm:menuNameTxt");
            getAppMenu().setMenuLabel(menuTxt.getValue().toString());
        }

        if (getAppMenu().getSubsystemID() > 0) {
            SubsystemEntity subsys = findSubsystemByID(getAppMenu().getSubsystemID());
            if (subsys != null) {
                getAppMenu().setSubsystemName(subsys.getSubsystemName());
            }
        }

        dataServer.beginTransaction();
        try {
            for (MenuEntity appMenu : selectedMenuList) {
                MenuEntity entity = findPrivilegeByNameAndAppID(appMenu.getMenuLabel(), applicationSystemID);
                if (entity == null) {
                    appMenu = appMenu.cloneEntity();
                    appMenu.setApplicationSystemID(applicationSystemID);
                    
                    SubsystemEntity subsystem = findSubsystemByNameAndAppID(appMenu.getSubsystemName(), applicationSystemID);
                    if(subsystem != null) {
                        appMenu.setSubsystemID(subsystem.getSubsystemID());
                    }

                    int menuID = Integer.parseInt(appPropBean.getValue(MENU_ID_KEY,
                            MENU_ID_DEFAULT_VALUE, true));
                    appMenu.setMenuID(menuID);

                    dataServer.saveData(appMenu);
                }
            }

            dataServer.endTransaction();
            applicationMessageBean.insertMessage("Entity list saved successfully.", MessageType.SUCCESS_MESSAGE);
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

    public String addMenu() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if ((getAppMenu().getMenuLabel() == null) || (getAppMenu().getMenuLabel().trim().equals(""))) {
            HtmlInputText menuTxt = (HtmlInputText) FacesContext.getCurrentInstance().
                    getViewRoot().findComponent("menuForm:menuNameTxt");
            getAppMenu().setMenuLabel(menuTxt.getValue().toString());
        }

        if (getAppMenu().getSubsystemID() > 0) {
            SubsystemEntity subsys = findSubsystemByID(getAppMenu().getSubsystemID());
            if (subsys != null) {
                getAppMenu().setSubsystemName(subsys.getSubsystemName());
            }
        }

        dataServer.beginTransaction();
        try {
            appMenu.setApplicationSystemID(subsystem.getApplicationSystemID());
            
            if (appMenu.getMenuID() == 0) {
                int menuID = Integer.parseInt(appPropBean.getValue(MENU_ID_KEY,
                        MENU_ID_DEFAULT_VALUE, true));
                appMenu.setMenuID(menuID);

                dataServer.saveData(appMenu);

                menuList.add(appMenu);
                selectedMenuList.add(appMenu);
            } else {
                dataServer.updateData(appMenu);

                if (menuList.contains(appMenu)) {
                    int index = menuList.indexOf(appMenu);
                    if (index >= 0) {
                        menuList.set(index, appMenu);
                    }
                }

                if (selectedMenuList.contains(appMenu)) {
                    int index = selectedMenuList.indexOf(appMenu);
                    if (index >= 0) {
                        selectedMenuList.set(index, appMenu);
                    }
                } else {
                    selectedMenuList.add(appMenu);
                }
            }

            appMenu = new MenuEntity();
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

    public String activatePrivilege() {
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

        MenuEntity privilege = findPrivilegeByName(
                menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_PRIVLG_MENU_ITEM));

        if (appMenu.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((appMenu.getApprovalStatusID() == 0) || (appMenu.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(appMenu.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        appMenu.setApprovalStatusID(appMenu.getApprovalStatusID() + 1);
                        appMenu.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(appMenu);
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
                } else if (appMenu.getApprovalStatusID()
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

    public String removeMenu() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (menuList.contains(appMenu)) {
                if (appMenu.isActivated()) {
                    applicationMessageBean.insertMessage(
                            "This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                    return "";
                }
                MenuEntity criteria = new MenuEntity();
                criteria.setMenuID(appMenu.getMenuID());
                dataServer.beginTransaction();
                dataServer.deleteData(criteria);
                dataServer.endTransaction();

                menuList.remove(appMenu);
                selectedMenuList.remove(appMenu);
                appMenu = new MenuEntity();

                applicationMessageBean.insertMessage("Menu Item Deleted Successfully", MessageType.SUCCESS_MESSAGE);
            } else {
                applicationMessageBean.insertMessage("Menu Item doesn't Exist!", MessageType.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void roleSelected(ValueChangeEvent vce) {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if ((vce != null) && (vce.getNewValue() != null)) {
                int rowIndex = CommonBean.getComponentEventRowIndex(vce);
                role = roleList.get(rowIndex);
                if (role != null) {
                    rolePrivilegeList = findUserPrivilegeListByRoleID(role.getRoleID());
                } else {
                    rolePrivilegeList = new ArrayList<UserPrivilegeEntity>();
                }
            }

            CommonBean.deselectOtherItems(role, roleList);
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public void roleMenuSelected(ValueChangeEvent vce) {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if ((vce != null) && (vce.getNewValue() != null)) {
                int roleID = Integer.parseInt(vce.getNewValue().toString());
                role = findRoleByRoleID(roleID);
                if (role != null) {
                    rolePrivilegeList = findUserPrivilegeListByRoleID(role.getRoleID());
                } else {
                    rolePrivilegeList = new ArrayList<UserPrivilegeEntity>();
                }
            }

            CommonBean.deselectOtherItems(role, roleList);
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public void roleSelectedForUserPrivCreate(ValueChangeEvent vce) {
        int roleID = Integer.parseInt(vce.getNewValue().toString());

        if (roleID != -1) {
            role = findRoleByRoleID(roleID);
            if (role != null) {
                try {
                    rolePrivilegeList = findUserPrivilegeListByRoleID(role.getRoleID());

                    selectedRolePrivilegeList = new ArrayList<UserPrivilegeEntity>();
                    for (UserPrivilegeEntity privlg : rolePrivilegeList) {
                        selectedRolePrivilegeList.add(privlg);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void subsystemSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        subsystem = selectedSubsystemList.get(rowIndex);

        CommonBean.deselectOtherItems(subsystem, selectedSubsystemList);
    }

    public void subsystemMenuSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            int subsystemID = Integer.parseInt(vce.getNewValue().toString());

            if (subsystemID > -1) {
                selectedMenuList = findPrivilegeListByAppIDAndSubsystemID(subsystem.getApplicationSystemID(), subsystemID);

                selectedRolePrivilegeList = new ArrayList<UserPrivilegeEntity>();
                for (UserPrivilegeEntity priv : rolePrivilegeList) {
                    if (priv.getSubsystemID() == subsystemID) {
                        selectedRolePrivilegeList.add(priv);
                    }
                }
            } else if (subsystemID == -1) {
                selectedMenuList = new ArrayList<MenuEntity>();
                for (MenuEntity priv : menuList) {
                    selectedMenuList.add(priv);
                }

                selectedRolePrivilegeList = new ArrayList<UserPrivilegeEntity>();
                for (UserPrivilegeEntity priv : rolePrivilegeList) {
                    selectedRolePrivilegeList.add(priv);
                }
            }

            Collections.sort(selectedMenuList, new Comparator<MenuEntity>() {
                public int compare(MenuEntity o1, MenuEntity o2) {
                    return ((int) o1.getSubsystemName().compareTo(o2.getSubsystemName()));
                }
            });
        }
    }

    public String saveMenuList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        dataServer.beginTransaction();
        try {
            for (MenuEntity menu : getMenuList()) {
                if (menu.getMenuID() == 0) {
                    int menuID = Integer.parseInt(appPropBean.getValue(MENU_ID_KEY,
                            MENU_ID_DEFAULT_VALUE, true));

                    menu.setMenuID(menuID);

                    dataServer.saveData(menu);
                }
            }
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

    public String loadRolePreviousHistoricalState() {
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

        try {
            if (roleList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadPreviousHistoricalState(roleList.get(0).getActionTrail());

                if (batEntity != null) {
                    loadRoleList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadRoleList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }

    public String loadRoleNextHistoricalState() {
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

        try {
            if (roleList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(roleList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadRoleList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadRoleList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }

    public String gotoRoleAuditTrailPage() {
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

        String outcome = "";

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.USER_ROLE);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(role.getRoleID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadRoleList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "userroleaudittrail.jsf";
            } else {
                applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return outcome;
    }

    public String loadMenuPreviousHistoricalState() {
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

        try {
            if (menuList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(menuList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadMenuList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadMenuList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }

    public String loadMenuNextHistoricalState() {
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

        try {
            if (menuList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(menuList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadMenuList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadMenuList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }

    public String gotoMenuAuditTrailPage() {
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

        String outcome = "";

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.SYSTEM_MENU);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(appMenu.getMenuID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadMenuList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "sysmenuaudittrail.jsf";
            } else {
                applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return outcome;
    }

    public String loadSubsysPreviousHistoricalState() {
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

        try {
            if (subsystemList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        subsystemList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadSubsystemList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadSubsystemList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }

    public String loadSubsysNextHistoricalState() {
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

        try {
            if (subsystemList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        subsystemList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadSubsystemList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadSubsystemList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }

    public String gotoSubsystemAuditTrailPage() {
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

        String outcome = "";

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.SUBSYSTEM);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(subsystem.getSubsystemID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadSubsystemList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "sysmenuaudittrail.jsf";
            } else {
                applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return outcome;
    }

    public String gotoCloneRolePage() {
        FinultimateCommons.captureRequestingResource();
        return "clonerole.jsf";
    }

    public String backToCallerPage() {
        return FinultimateCommons.retrieveLastRequestingResource();
    }

    public String saveRoleList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        List<RoleEntity> rList = new ArrayList<RoleEntity>();
//        List<SelectItem> rItemList = new ArrayList<SelectItem>();

        dataServer.beginTransaction();
        try {
            for (RoleEntity rol : getRoleList()) {
                if (rol.getRoleID() == 0) {
                    long rolID = Long.parseLong(appPropBean.getValue(ROLE_ID_KEY,
                            ROLE_ID_DEFAULT_VALUE, true));

                    rol.setRoleID(rolID);

                    dataServer.saveData(rol);
                } else if (rol.getRoleID() > 0) {
                    dataServer.updateData(rol);
                }
                rList.add(rol);
                addToSelectItemList(rol);
            }

            setRoleList(rList);
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

    public String activateSysRole() {
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

        MenuEntity privilege = findPrivilegeByName(
                menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_SYS_ROLS_MENU_ITEM));

        if (role.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((role.getApprovalStatusID() == 0) || (role.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalStatusID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(role.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalStatusID()
                            > creatorAcct.getRole().getApprovalStatusID()) {
                        role.setApprovalStatusID(role.getApprovalStatusID() + 1);
                        role.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(role);
                        } catch (SQLException ex) {
                            dataServer.rollbackTransaction();
                            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        dataServer.endTransaction();
                        applicationMessageBean.insertMessage("Operation Activated Successfully!", MessageType.SUCCESS_MESSAGE);
                    } else if (userManagerBean.getUserAccount().getRole().getApprovalStatusID()
                            == creatorAcct.getRole().getApprovalStatusID()) {
                        applicationMessageBean.insertMessage("You cannot approve an operation performed "
                                + "by another user who has the same approval level as you!",
                                MessageType.ERROR_MESSAGE);
                    } else if (userManagerBean.getUserAccount().getRole().getApprovalStatusID()
                            < creatorAcct.getRole().getApprovalStatusID()) {
                        applicationMessageBean.insertMessage("You cannot approve an operation performed "
                                + "by another user who has a higher approval level as you!",
                                MessageType.ERROR_MESSAGE);
                    }
                } else if (role.getApprovalStatusID()
                        > userManagerBean.getUserAccount().getRole().getApprovalStatusID()) {
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

    public String saveSubsystemList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        List<SubsystemEntity> sList = new ArrayList<SubsystemEntity>();

        dataServer.beginTransaction();
        try {
            for (SubsystemEntity subs : getSubsystemList()) {
                if (subs.getSubsystemID() == 0) {
                    int subsID = Integer.parseInt(appPropBean.getValue(SUBSYSTEM_ID_KEY,
                            SUBSYSTEM_ID_DEFAULT_VALUE, true));

                    subs.setSubsystemID(subsID);

                    dataServer.saveData(subs);
                } else if (subs.getSubsystemID() > 0) {
                    dataServer.updateData(subs);
                }
                sList.add(subs);
                addToSelectItemList(subs);
            }

            applicationMessageBean.insertMessage("Entity list saved successfully.", MessageType.SUCCESS_MESSAGE);

            setSubsystemList(sList);
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

        MenuEntity privilege = findPrivilegeByName(
                menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_SUBSYS_MENU_ITEM));

        if (subsystem.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((subsystem.getApprovalStatusID() == 0) || (subsystem.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(subsystem.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        subsystem.setApprovalStatusID(subsystem.getApprovalStatusID() + 1);
                        subsystem.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(subsystem);
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
                } else if (subsystem.getApprovalStatusID()
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

    public void loadUserPrivilegeList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (userPrivilegeList.size() == 0) {
            try {
                UserPrivilegeEntity criteria = new UserPrivilegeEntity();
                List<AbstractEntity> baseUserPrivilegeList = dataServer.findData(criteria);

                if ((baseUserPrivilegeList != null) && (baseUserPrivilegeList.size() > 0)) {
                    for (AbstractEntity baseUserPrivilege : baseUserPrivilegeList) {
                        UserPrivilegeEntity uPriv = (UserPrivilegeEntity) baseUserPrivilege;

                        if ((uPriv.getPrivilegeName() == null) || (uPriv.getPrivilegeName().trim().equals(""))) {
                            MenuEntity priv = findPrivilegeByID(uPriv.getPrivilegeId());
                            if (priv != null) {
                                uPriv.setPrivilegeName(priv.getMenuLabel());
                            }
                        }

                        if (!userPrivilegeList.contains(uPriv)) {
                            userPrivilegeList.add(uPriv);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            }
        }
    }

    public List<UserPrivilegeEntity> findUserPrivilegeListByRoleID(long roleID) throws Exception {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        UserPrivilegeEntity criteria = new UserPrivilegeEntity();
        criteria.setRoleID(roleID);

        List<AbstractEntity> baseUserPrivilegeList = dataServer.findData(criteria);
        List<UserPrivilegeEntity> uPrivList = new ArrayList<UserPrivilegeEntity>();

        if ((baseUserPrivilegeList != null) && (baseUserPrivilegeList.size() > 0)) {
            for (AbstractEntity baseUserPrivilege : baseUserPrivilegeList) {
                UserPrivilegeEntity uPriv = (UserPrivilegeEntity) baseUserPrivilege;

                if ((uPriv.getPrivilegeName() == null) || (uPriv.getPrivilegeName().trim().equals(""))) {
                    MenuEntity priv = findPrivilegeByID(uPriv.getPrivilegeId());
                    if (priv != null) {
                        uPriv.setPrivilegeName(priv.getMenuLabel());
                    }
                }

                uPrivList.add(uPriv);
            }
        }

        return uPrivList;
    }

    public UserPrivilegeEntity findUserPrivilegeByID(int privID) {
        UserPrivilegeEntity priv = null;
        for (UserPrivilegeEntity up : getUserPrivilegeList()) {
            if (up.getPrivilegeId() == privID) {
                priv = up;
                break;
            }
        }

        return priv;
    }

    public MenuEntity findPrivilegeByID(long privID) {
        MenuEntity priv = null;
        for (MenuEntity p : menuList) {
            if (p.getMenuID() == privID) {
                priv = p;
                break;
            }
        }

        return priv;
    }

    public MenuEntity findPrivilegeByName(String privName) {
        MenuEntity priv = null;

        for (MenuEntity pMenu : menuList) {
            if (pMenu.getMenuLabel().equals(privName)) {
                priv = pMenu;
                break;
            }
        }

        return priv;
    }

    public MenuEntity findPrivilegeByNameAndAppID(String privName, int applicationID) {
        MenuEntity priv = null;

        for (MenuEntity pMenu : menuList) {
            if ((pMenu.getMenuLabel().equals(privName))
                    && (pMenu.getApplicationSystemID() == applicationID)) {
                priv = pMenu;
                break;
            }
        }

        return priv;
    }

    public List<MenuEntity> findPrivilegeListBySubsystemID(long subsystemID) {
        List<MenuEntity> privList = new ArrayList<MenuEntity>();

        for (MenuEntity priv : menuList) {
            if (priv.getSubsystemID() == subsystemID) {
                privList.add(priv);
            }
        }

        return privList;
    }
    
    public List<MenuEntity> findPrivilegeListByAppIDAndSubsystemID(long applicationID, long subsystemID) {
        List<MenuEntity> privList = new ArrayList<MenuEntity>();

        for (MenuEntity priv : menuList) {
            if ((priv.getSubsystemID() == subsystemID) 
                    && (priv.getApplicationSystemID() == applicationID)) {
                privList.add(priv);
            }
        }

        return privList;
    }

    public RoleEntity findRoleByRoleID(long roleID) {
        RoleEntity rol = null;

        for (RoleEntity rl : getRoleList()) {
            if (rl.getRoleID() == roleID) {
                rol = rl;
                break;
            }
        }

        return rol;
    }

    public SubsystemEntity findSubsystemByID(long subsysID) {
        SubsystemEntity subsystem = null;

        for (SubsystemEntity subsys : getSubsystemList()) {
            if (subsys.getSubsystemID() == subsysID) {
                subsystem = subsys;
                break;
            }
        }

        return subsystem;
    }
    
    public SubsystemEntity findSubsystemByNameAndAppID(String subsystemName, long applicationID) {
        SubsystemEntity subsystem = null;

        for (SubsystemEntity subsys : getSubsystemList()) {
            if ((subsys.getSubsystemName().equalsIgnoreCase(subsystemName)) 
                    && (subsys.getApplicationSystemID() == applicationID)) {
                subsystem = subsys;
                break;
            }
        }

        return subsystem;
    }

    private boolean hasRole(SelectItem roleItem) {
        boolean hasRoleBool = false;

        for (SelectItem item : getRoleItemList()) {
            hasRoleBool = roleItem.getValue().toString().trim().
                    equalsIgnoreCase(item.getValue().toString().trim());

            if (hasRoleBool == true) {
                break;
            }
        }

        return hasRoleBool;
    }

    public List<UserPrivilegeEntity> createUserPrivilege(long roleID) {
        List<UserPrivilegeEntity> uPrivList = new ArrayList<UserPrivilegeEntity>();

        for (MenuEntity priv : getMenuList()) {
            if (priv.isSelected()) {
                UserPrivilegeEntity uPriv = new UserPrivilegeEntity();
                uPriv.setRoleID(roleID);
                uPriv.setPrivilegeName(priv.getMenuLabel());
                uPriv.setPrivilegeId(priv.getMenuID());

                uPrivList.add(uPriv);
            }
        }

        return uPrivList;
    }

    public String selectAllAsReadOnlyPrivileges() {
        userPrivilegeList = new ArrayList<UserPrivilegeEntity>();

        if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            for (MenuEntity priv : selectedMenuList) {
                UserPrivilegeEntity userPriv = new UserPrivilegeEntity();
                userPriv.setPrivilegeName(priv.getMenuLabel());
                userPriv.setPrivilegeId(priv.getMenuID());
                userPriv.setAccessType(AccessType.READONLY.ordinal());
                userPriv.setSubsystemID(priv.getSubsystemID());

                rolePrivilegeList.add(userPriv);
                selectedRolePrivilegeList.add(userPriv);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
            for (MenuEntity priv : selectedMenuList) {
                if (priv.isActivated()) {
                    UserPrivilegeEntity userPriv = new UserPrivilegeEntity();
                    userPriv.setPrivilegeName(priv.getMenuLabel());
                    userPriv.setPrivilegeId(priv.getMenuID());
                    userPriv.setAccessType(AccessType.READONLY.ordinal());
                    userPriv.setSubsystemID(priv.getSubsystemID());

                    rolePrivilegeList.add(userPriv);
                    selectedRolePrivilegeList.add(userPriv);
                }
            }
        }

        return "";
    }

    public String selectAllAsReadWritePrivileges() {
        userPrivilegeList = new ArrayList<UserPrivilegeEntity>();

        if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            for (MenuEntity priv : selectedMenuList) {
                UserPrivilegeEntity userPriv = new UserPrivilegeEntity();
                userPriv.setPrivilegeName(priv.getMenuLabel());
                userPriv.setPrivilegeId(priv.getMenuID());
                userPriv.setAccessType(AccessType.READ_WRITE.ordinal());
                userPriv.setSubsystemID(priv.getSubsystemID());

                rolePrivilegeList.add(userPriv);
                selectedRolePrivilegeList.add(userPriv);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
            for (MenuEntity priv : selectedMenuList) {
                if (priv.isActivated()) {
                    UserPrivilegeEntity userPriv = new UserPrivilegeEntity();
                    userPriv.setPrivilegeName(priv.getMenuLabel());
                    userPriv.setPrivilegeId(priv.getMenuID());
                    userPriv.setAccessType(AccessType.READ_WRITE.ordinal());
                    userPriv.setSubsystemID(priv.getSubsystemID());

                    rolePrivilegeList.add(userPriv);
                    selectedRolePrivilegeList.add(userPriv);
                }
            }
        }

        return "";
    }

    public void readonlyPrivilegeSelected(ValueChangeEvent vce) {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        HtmlSelectBooleanCheckbox checkbox = (HtmlSelectBooleanCheckbox) vce.getComponent();
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
        MenuEntity priv = selectedMenuList.get(rowIndex);

        if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            UserPrivilegeEntity userPriv = new UserPrivilegeEntity();
            userPriv.setPrivilegeName(priv.getMenuLabel());
            userPriv.setPrivilegeId(priv.getMenuID());
            userPriv.setAccessType(AccessType.READONLY.ordinal());
            userPriv.setSubsystemID(priv.getSubsystemID());

            if (checkbox.isSelected()) {
                if (!rolePrivilegeList.contains(userPriv)) {
                    rolePrivilegeList.add(userPriv);
                    selectedRolePrivilegeList.add(userPriv);
                } else {
                    int index = rolePrivilegeList.indexOf(userPriv);
                    rolePrivilegeList.set(index, userPriv);
                    index = selectedRolePrivilegeList.indexOf(userPriv);
                    if (index > -1) {
                        selectedRolePrivilegeList.set(index, userPriv);
                    }
                }
            } else if (rolePrivilegeList.contains(userPriv)) {
                rolePrivilegeList.remove(userPriv);
                selectedRolePrivilegeList.remove(userPriv);
            }

            priv.setReadWriteRoleItem(false);
            getMenuList().set(rowIndex, priv);
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
            if (priv.isActivated()) {
                UserPrivilegeEntity userPriv = new UserPrivilegeEntity();
                userPriv.setPrivilegeName(priv.getMenuLabel());
                userPriv.setPrivilegeId(priv.getMenuID());
                userPriv.setAccessType(AccessType.READONLY.ordinal());
                userPriv.setSubsystemID(priv.getSubsystemID());

                if (checkbox.isSelected()) {
                    if (!rolePrivilegeList.contains(userPriv)) {
                        rolePrivilegeList.add(userPriv);
                        selectedRolePrivilegeList.add(userPriv);
                    } else {
                        int index = rolePrivilegeList.indexOf(userPriv);
                        rolePrivilegeList.set(index, userPriv);
                        index = selectedRolePrivilegeList.indexOf(userPriv);
                        if (index > -1) {
                            selectedRolePrivilegeList.set(index, userPriv);
                        }
                    }
                } else if (rolePrivilegeList.contains(userPriv)) {
                    rolePrivilegeList.remove(userPriv);
                    selectedRolePrivilegeList.remove(userPriv);
                }

                priv.setReadWriteRoleItem(false);
                getMenuList().set(rowIndex, priv);
            } else {
                applicationMessageBean.insertMessage(
                        "Sorry, The Selected Privilege Is Not Active/Approved. It Cannot Be Added. Please Contact The System Admin.",
                        MessageType.ERROR_MESSAGE);
            }
        }
    }

    public void readwritePrivilegeSelected(ValueChangeEvent vce) {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        HtmlSelectBooleanCheckbox checkbox = (HtmlSelectBooleanCheckbox) vce.getComponent();
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
        MenuEntity priv = selectedMenuList.get(rowIndex);

        if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            UserPrivilegeEntity userPriv = new UserPrivilegeEntity();
            userPriv.setPrivilegeName(priv.getMenuLabel());
            userPriv.setPrivilegeId(priv.getMenuID());
            userPriv.setAccessType(AccessType.READ_WRITE.ordinal());
            userPriv.setSubsystemID(priv.getSubsystemID());

            if (checkbox.isSelected()) {
                if (!rolePrivilegeList.contains(userPriv)) {
                    rolePrivilegeList.add(userPriv);
                    selectedRolePrivilegeList.add(userPriv);
                } else {
                    int index = rolePrivilegeList.indexOf(userPriv);
                    rolePrivilegeList.set(index, userPriv);
                    index = selectedRolePrivilegeList.indexOf(userPriv);
                    if (index > -1) {
                        selectedRolePrivilegeList.set(index, userPriv);
                    }
                }
            } else if (rolePrivilegeList.contains(userPriv)) {
                rolePrivilegeList.remove(userPriv);
                selectedRolePrivilegeList.remove(userPriv);
            }

            priv.setReadOnlyRoleItem(false);
            getMenuList().set(rowIndex, priv);
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
            if (priv.isActivated()) {
                UserPrivilegeEntity userPriv = new UserPrivilegeEntity();
                userPriv.setPrivilegeName(priv.getMenuLabel());
                userPriv.setPrivilegeId(priv.getMenuID());
                userPriv.setAccessType(AccessType.READ_WRITE.ordinal());
                userPriv.setSubsystemID(priv.getSubsystemID());

                if (checkbox.isSelected()) {
                    if (!rolePrivilegeList.contains(userPriv)) {
                        rolePrivilegeList.add(userPriv);
                        selectedRolePrivilegeList.add(userPriv);
                    } else {
                        int index = rolePrivilegeList.indexOf(userPriv);
                        rolePrivilegeList.set(index, userPriv);
                        index = selectedRolePrivilegeList.indexOf(userPriv);
                        if (index > -1) {
                            selectedRolePrivilegeList.set(index, userPriv);
                        }
                    }
                } else if (rolePrivilegeList.contains(userPriv)) {
                    rolePrivilegeList.remove(userPriv);
                    selectedRolePrivilegeList.remove(userPriv);
                }

                priv.setReadOnlyRoleItem(false);
                getMenuList().set(rowIndex, priv);
            } else {
                applicationMessageBean.insertMessage(
                        "Sorry, The Selected Privilege Is Not Active/Approved. It Cannot Be Added. Please Contact The System Admin.",
                        MessageType.ERROR_MESSAGE);
            }
        }
    }

    public String cloneRole() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

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
        
        if (roleClone.getRoleID() == role.getRoleID()) {
            applicationMessageBean.insertMessage(
                    "Invalid Operation: You Are Attempting To Clone The Same Role, This Is Not Allowed!",
                    MessageType.ERROR_MESSAGE);
        } else if (userManagerBean.getUserAccount().getRoleID() != role.getRoleID()) {
            roleClone = findRoleByRoleID(roleClone.getRoleID());

            if (roleClone.getPrivilegeList().size() > 0) {
                applicationMessageBean.insertMessage(
                        "Invalid Operation: The Destination Role Already Has Defined Privileges!",
                        MessageType.ERROR_MESSAGE);
            } else if (roleClone.getPrivilegeList().size() == 0) {
                roleClone = role.copyEntityTo(roleClone);
                dataServer.beginTransaction();
                try {
                    for (UserPrivilegeEntity uPriv : roleClone.getPrivilegeList()) {
                        uPriv.setRoleID(roleClone.getRoleID());
                        int privID = Integer.parseInt(appPropBean.getValue(
                                USER_PRIV_ID_KEY,
                                USER_PRIV_ID_DEFAULT_VALUE, true));
                        uPriv.setUserPrivilegeID(privID);

                        dataServer.saveData(uPriv);
                        userPrivilegeList.add(uPriv);
                    }

                    if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
                        roleClone.setApprovalStatusID(0);
                    }

                    dataServer.updateData(roleClone);

                    applicationMessageBean.insertMessage(
                            "The Role '" + roleClone.getRoleName() + "' Successfully Created/Updated.", MessageType.SUCCESS_MESSAGE);
                } catch (SQLException ex) {
                    dataServer.rollbackTransaction();
                    applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                    ex.printStackTrace();
                } catch (Exception ex) {
                    applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
                dataServer.endTransaction();
            } else {
                applicationMessageBean.insertMessage(
                        "Invalid Operation: You Cannot Alter Your Own Role!", MessageType.ERROR_MESSAGE);
            }
        } else if (userManagerBean.getUserAccount().getRoleID() == role.getRoleID()) {
            applicationMessageBean.insertMessage(
                        "Invalid Operation: As Super User, You Cannot Copy Your Role Privileges To Another Role!", MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String saveUserPrivilegeList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

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

        if (userManagerBean.getUserAccount().getRoleID() != role.getRoleID()) {
            try {
                dataServer.beginTransaction();

                for (UserPrivilegeEntity uPriv : rolePrivilegeList) {
                    if (uPriv.getUserPrivilegeID() > 0) {
                        dataServer.updateData(uPriv);
                    } else if (uPriv.getUserPrivilegeID() == 0) {
                        uPriv.setRoleID(role.getRoleID());
                        int privID = Integer.parseInt(appPropBean.getValue(
                                USER_PRIV_ID_KEY,
                                USER_PRIV_ID_DEFAULT_VALUE, true));
                        uPriv.setUserPrivilegeID(privID);

                        dataServer.saveData(uPriv);
                        userPrivilegeList.add(uPriv);
                    }
                }

                if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
                    role.setApprovalStatusID(0);
                }

                dataServer.updateData(role);
                dataServer.endTransaction();

                applicationMessageBean.insertMessage(
                        "The Role '" + role.getRoleName() + "' Successfully Created/Updated.", MessageType.SUCCESS_MESSAGE);
            } catch (SQLException ex) {
                dataServer.rollbackTransaction();
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            applicationMessageBean.insertMessage("Invalid Operation: You Cannot Alter Your Own Role!", MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String removeSelectedPrivileges(ActionEvent ae) {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        int rowIndex = CommonBean.getComponentEventRowIndex(ae);
        UserPrivilegeEntity userPriv = rolePrivilegeList.remove(rowIndex);

        UserPrivilegeEntity criteria = new UserPrivilegeEntity();
        criteria.setUserPrivilegeID(userPriv.getUserPrivilegeID());
        try {
            dataServer.beginTransaction();
            dataServer.deleteData(criteria);
            dataServer.endTransaction();

            List<MenuEntity> tempEntityList = new ArrayList<MenuEntity>();
            for (MenuEntity menu : selectedMenuList) {
                if (userPriv.getPrivilegeId() == menu.getMenuID()) {
                    menu.setReadOnlyRoleItem(false);
                    menu.setReadWriteRoleItem(false);
                }

                tempEntityList.add(menu);
            }

            selectedMenuList = tempEntityList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public String removeSelectedUserPrivileges(ActionEvent ae) {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        int rowIndex = CommonBean.getComponentEventRowIndex(ae);
        UserPrivilegeEntity userPriv = selectedRolePrivilegeList.get(rowIndex);//rolePrivilegeList.remove(rowIndex);
        UserPrivilegeEntity criteria = new UserPrivilegeEntity();
        criteria.setUserPrivilegeID(userPriv.getUserPrivilegeID());
        try {
            dataServer.beginTransaction();

            dataServer.deleteData(criteria);

            List<UserPrivilegeEntity> tempUPEntityList = new ArrayList<UserPrivilegeEntity>();
            for (UserPrivilegeEntity menu : selectedRolePrivilegeList) {
                if (userPriv.getUserPrivilegeID() != menu.getUserPrivilegeID()) {
                    tempUPEntityList.add(menu);
                }
            }
            selectedRolePrivilegeList = tempUPEntityList;

            tempUPEntityList = new ArrayList();
            for (UserPrivilegeEntity menu : rolePrivilegeList) {
                if (userPriv.getUserPrivilegeID() != menu.getUserPrivilegeID()) {
                    tempUPEntityList.add(menu);
                }
            }
            rolePrivilegeList = tempUPEntityList;

            List<MenuEntity> tempEntityList = new ArrayList<MenuEntity>();
            for (MenuEntity menu : selectedMenuList) {
                if (userPriv.getPrivilegeId() == menu.getMenuID()) {
                    menu.setReadOnlyRoleItem(false);
                    menu.setReadWriteRoleItem(false);
                }

                tempEntityList.add(menu);
            }
            selectedMenuList = tempEntityList;

            dataServer.endTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public String removeAllPrivileges() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        UserPrivilegeEntity criteria = new UserPrivilegeEntity();
        criteria.setRoleID(rolePrivilegeList.get(0).getRoleID());

        try {
            dataServer.beginTransaction();
            dataServer.deleteData(criteria);
            dataServer.endTransaction();

            rolePrivilegeList = new ArrayList<UserPrivilegeEntity>();

            List<MenuEntity> tempEntityList = new ArrayList<MenuEntity>();
            for (MenuEntity menu : selectedMenuList) {
                menu.setReadOnlyRoleItem(false);
                menu.setReadWriteRoleItem(false);

                tempEntityList.add(menu);
            }
            selectedMenuList = tempEntityList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public String removeAllUserPrivileges() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        UserPrivilegeEntity criteria = new UserPrivilegeEntity();
        criteria.setRoleID(rolePrivilegeList.get(0).getRoleID());

        try {
            dataServer.beginTransaction();
            dataServer.deleteData(criteria);
            dataServer.endTransaction();

            selectedRolePrivilegeList = new ArrayList<UserPrivilegeEntity>();

            List<MenuEntity> tempEntityList = new ArrayList<MenuEntity>();
            for (MenuEntity menu : selectedMenuList) {
                menu.setReadOnlyRoleItem(false);
                menu.setReadWriteRoleItem(false);

                tempEntityList.add(menu);
            }
            selectedMenuList = tempEntityList;

            tempEntityList = new ArrayList<MenuEntity>();
            for (MenuEntity menu : menuList) {
                menu.setReadOnlyRoleItem(false);
                menu.setReadWriteRoleItem(false);

                tempEntityList.add(menu);
            }
            menuList = tempEntityList;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public String addApplicationSystem() {
        if (!applicationSystemList.contains(applicationSystem)) {
            applicationSystemList.add(applicationSystem);
        } else {
            int index = applicationSystemList.indexOf(applicationSystem);
            applicationSystemList.set(index, applicationSystem);
        }

        applicationSystem = new ApplicationSystemEntity();

        return "";
    }

    public void applicationSystemSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        applicationSystem = applicationSystemList.get(rowIndex);

        CommonBean.deselectOtherItems(applicationSystem, applicationSystemList);
    }

    public void applicationSystemMenuSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            int applicationSystemID = Integer.parseInt(vce.getNewValue().toString());

            selectedSubsystemList = new ArrayList<SubsystemEntity>();
            subsystemItemList = new ArrayList<SelectItem>();

            if (applicationSystemID > -1) {
                selectedSubsystemList = findPrivilegeListByApplicationSystemID(applicationSystemID);
            } else if (applicationSystemID == -1) {
                selectedSubsystemList = new ArrayList<SubsystemEntity>();
                for (SubsystemEntity priv : subsystemList) {
                    selectedSubsystemList.add(priv);
                }
            }

            for (SubsystemEntity entity : selectedSubsystemList) {
                addToSelectItemList(entity);
            }
        }
    }

    public void loadApplicationSystemList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (applicationSystemList.size() == 0) {
            try {
                ApplicationSystemEntity criteria = new ApplicationSystemEntity();
                List<AbstractEntity> baseApplicationSystemList = dataServer.findData(criteria);

                MenuEntity privilege = findPrivilegeByName(
                        menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_SUBSYS_MENU_ITEM));

                if ((baseApplicationSystemList != null) && (baseApplicationSystemList.size() > 0)) {
                    for (AbstractEntity baseApplicationSystem : baseApplicationSystemList) {
                        if (privilege != null) {
                            baseApplicationSystem.setActivated(
                                    ((ApplicationSystemEntity) baseApplicationSystem).getApprovalStatusID() >= privilege.getApprovedStatusID());

                            if (!getApplicationSystemList().contains((ApplicationSystemEntity) baseApplicationSystem)) {
                                ApplicationSystemEntity subsys = (ApplicationSystemEntity) baseApplicationSystem;
                                getApplicationSystemList().add(subsys);
                                addToSelectItemList(subsys);
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            }
        }
    }

    public void loadApplicationSystemList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        applicationSystemList = new ArrayList<ApplicationSystemEntity>();

        ApplicationSystemEntity criteria = new ApplicationSystemEntity();
        List<AbstractEntity> baseApplicationSystemList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());

        if ((baseApplicationSystemList != null) && (baseApplicationSystemList.size() > 0)) {
            for (AbstractEntity baseApplicationSystem : baseApplicationSystemList) {
                if (!getApplicationSystemList().contains((ApplicationSystemEntity) baseApplicationSystem)) {
                    ApplicationSystemEntity subsys = (ApplicationSystemEntity) baseApplicationSystem;
                    subsys.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                    getApplicationSystemList().add(subsys);
                }
            }
        }
    }

    public void addToSelectItemList(ApplicationSystemEntity entity) {
        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
            MenuEntity privilege = findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_APPLIC_SYS_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getApplicationSystemID());
                item.setLabel(entity.getApplicationName());
                applicationSystemItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getApplicationSystemID());
            item.setLabel(entity.getApplicationName());
            applicationSystemItemList.add(item);
        }
    }

    public String saveApplicationSystemList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        List<ApplicationSystemEntity> sList = new ArrayList<ApplicationSystemEntity>();

        dataServer.beginTransaction();
        try {
            for (ApplicationSystemEntity subs : getApplicationSystemList()) {
                if (subs.getApplicationSystemID() == 0) {
                    int subsID = Integer.parseInt(
                            appPropBean.getValue(FinultimateConstants.APPLICATION_SYS_ID_KEY,
                                    FinultimateConstants.ONE_STR, true));

                    subs.setApplicationSystemID(subsID);

                    dataServer.saveData(subs);
                } else if (subs.getApplicationSystemID() > 0) {
                    dataServer.updateData(subs);
                }
                sList.add(subs);
                addToSelectItemList(subs);
            }

            applicationMessageBean.insertMessage("Entity list saved successfully.", MessageType.SUCCESS_MESSAGE);

            setApplicationSystemList(sList);
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

    public String loadApplicationPreviousHistoricalState() {
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

        try {
            if (applicationSystemList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        applicationSystemList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadApplicationSystemList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadApplicationSystemList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }

    public String loadApplicationNextHistoricalState() {
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

        try {
            if (applicationSystemList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        applicationSystemList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadApplicationSystemList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadApplicationSystemList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }

    public String gotoApplicationSystemAuditTrailPage() {
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

        String outcome = "";

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.SUBSYSTEM);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(applicationSystem.getApplicationSystemID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadApplicationSystemList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "sysmenuaudittrail.jsf";
            } else {
                applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return outcome;
    }

    public ApplicationSystemEntity findApplicationSystemByID(long subsysID) {
        ApplicationSystemEntity applicationSystem = null;

        for (ApplicationSystemEntity subsys : getApplicationSystemList()) {
            if (subsys.getApplicationSystemID() == subsysID) {
                applicationSystem = subsys;
                break;
            }
        }

        return applicationSystem;
    }

    public List<SubsystemEntity> findPrivilegeListByApplicationSystemID(long applicationSystemID) {
        List<SubsystemEntity> privList = new ArrayList<SubsystemEntity>();

        for (SubsystemEntity priv : subsystemList) {
            if (priv.getApplicationSystemID() == applicationSystemID) {
                privList.add(priv);
            }
        }

        return privList;
    }

    public String gotoCreateRolePage() {
        FinultimateCommons.captureRequestingResource();
        return "createrole";
    }

    public String gotoCreateSubsystemPage() {
        FinultimateCommons.captureRequestingResource();
        return "createsubsystem";
    }

    public String gotoCreateMenuPage() {
        FinultimateCommons.captureRequestingResource();
        return "createmenu";
    }

    public String backToRolesPage() {
        return FinultimateCommons.retrieveLastRequestingResource();
    }

    public List<UserPrivilegeEntity> getUserPrivilegeList() {
        return userPrivilegeList;
    }

    public void setUserPrivilegeList(List<UserPrivilegeEntity> userPrivilegeList) {
        this.userPrivilegeList = userPrivilegeList;
    }

    public List<SelectItem> getRoleItemList() {
        return roleItemList;
    }

    public void setRoleItemList(List<SelectItem> roleItemList) {
        this.roleItemList = roleItemList;
    }

    public List<UserPrivilegeEntity> getRolePrivilegeList() {
        return rolePrivilegeList;
    }

    public void setRolePrivilegeList(List<UserPrivilegeEntity> rolePrivilegeList) {
        this.rolePrivilegeList = rolePrivilegeList;
    }

    /**
     * @return the menuList
     */
    public List<MenuEntity> getMenuList() {
        return menuList;
    }

    /**
     * @param menuList the menuList to set
     */
    public void setMenuList(List<MenuEntity> menuList) {
        this.menuList = menuList;
    }

    /**
     * @return the menuItemList
     */
    public List<SelectItem> getMenuItemList() {
        return menuItemList;
    }

    /**
     * @param menuItemList the menuItemList to set
     */
    public void setMenuItemList(List<SelectItem> menuItemList) {
        this.menuItemList = menuItemList;
    }

    /**
     * @return the appMenu
     */
    public MenuEntity getAppMenu() {
        return appMenu;
    }

    /**
     * @param appMenu the appMenu to set
     */
    public void setAppMenu(MenuEntity appMenu) {
        this.appMenu = appMenu;
    }

    /**
     * @return the role
     */
    public RoleEntity getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(RoleEntity role) {
        this.role = role;
    }

    /**
     * @return the roleList
     */
    public List<RoleEntity> getRoleList() {
        return roleList;
    }

    /**
     * @param roleList the roleList to set
     */
    public void setRoleList(List<RoleEntity> roleList) {
        this.roleList = roleList;
    }

    /**
     * @return the subsystemMenuID
     */
    public int getSubsystemMenuID() {
        return subsystemMenuID;
    }

    /**
     * @param subsystemMenuID the subsystemMenuID to set
     */
    public void setSubsystemMenuID(int subsystemMenuID) {
        this.subsystemMenuID = subsystemMenuID;
    }

    /**
     * @return the subsystemList
     */
    public List<SubsystemEntity> getSubsystemList() {
        return subsystemList;
    }

    /**
     * @param subsystemList the subsystemList to set
     */
    public void setSubsystemList(List<SubsystemEntity> subsystemList) {
        this.subsystemList = subsystemList;
    }

    /**
     * @return the subsystemItemList
     */
    public List<SelectItem> getSubsystemItemList() {
        return subsystemItemList;
    }

    /**
     * @param subsystemItemList the subsystemItemList to set
     */
    public void setSubsystemItemList(List<SelectItem> subsystemItemList) {
        this.subsystemItemList = subsystemItemList;
    }

    /**
     * @return the subsystem
     */
    public SubsystemEntity getSubsystem() {
        return subsystem;
    }

    /**
     * @param subsystem the subsystem to set
     */
    public void setSubsystem(SubsystemEntity subsystem) {
        this.subsystem = subsystem;
    }

    /**
     * @return the allPrivileges
     */
    public boolean isAllPrivileges() {
        return allPrivileges;
    }

    /**
     * @param allPrivileges the allPrivileges to set
     */
    public void setAllPrivileges(boolean allPrivileges) {
        this.allPrivileges = allPrivileges;
    }

    /**
     * @return the selectedMenuList
     */
    public List<MenuEntity> getSelectedMenuList() {
        return selectedMenuList;
    }

    /**
     * @param selectedMenuList the selectedMenuList to set
     */
    public void setSelectedMenuList(List<MenuEntity> selectedMenuList) {
        this.selectedMenuList = selectedMenuList;
    }

    /**
     * @return the selectedRolePrivilegeList
     */
    public List<UserPrivilegeEntity> getSelectedRolePrivilegeList() {
        return selectedRolePrivilegeList;
    }

    /**
     * @param selectedRolePrivilegeList the selectedRolePrivilegeList to set
     */
    public void setSelectedRolePrivilegeList(List<UserPrivilegeEntity> selectedRolePrivilegeList) {
        this.selectedRolePrivilegeList = selectedRolePrivilegeList;
    }

    /**
     * @return the roleClone
     */
    public RoleEntity getRoleClone() {
        return roleClone;
    }

    /**
     * @param roleClone the roleClone to set
     */
    public void setRoleClone(RoleEntity roleClone) {
        this.roleClone = roleClone;
    }

    /**
     * @return the applicationSystem
     */
    public ApplicationSystemEntity getApplicationSystem() {
        return applicationSystem;
    }

    /**
     * @param applicationSystem the applicationSystem to set
     */
    public void setApplicationSystem(ApplicationSystemEntity applicationSystem) {
        this.applicationSystem = applicationSystem;
    }

    /**
     * @return the applicationSystemList
     */
    public List<ApplicationSystemEntity> getApplicationSystemList() {
        return applicationSystemList;
    }

    /**
     * @param applicationSystemList the applicationSystemList to set
     */
    public void setApplicationSystemList(List<ApplicationSystemEntity> applicationSystemList) {
        this.applicationSystemList = applicationSystemList;
    }

    /**
     * @return the applicationSystemItemList
     */
    public List<SelectItem> getApplicationSystemItemList() {
        return applicationSystemItemList;
    }

    /**
     * @param applicationSystemItemList the applicationSystemItemList to set
     */
    public void setApplicationSystemItemList(List<SelectItem> applicationSystemItemList) {
        this.applicationSystemItemList = applicationSystemItemList;
    }

    /**
     * @return the selectedSubsystemList
     */
    public List<SubsystemEntity> getSelectedSubsystemList() {
        return selectedSubsystemList;
    }

    /**
     * @param selectedSubsystemList the selectedSubsystemList to set
     */
    public void setSelectedSubsystemList(List<SubsystemEntity> selectedSubsystemList) {
        this.selectedSubsystemList = selectedSubsystemList;
    }

    /**
     * @return the applicationSystemID
     */
    public int getApplicationSystemID() {
        return applicationSystemID;
    }

    /**
     * @param applicationSystemID the applicationSystemID to set
     */
    public void setApplicationSystemID(int applicationSystemID) {
        this.applicationSystemID = applicationSystemID;
    }

}
