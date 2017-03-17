/*
 * UserManagerBean.java
 *
 * Created on July 16, 2009, 10:28 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rsdynamix.projects.security.bean;

import com.rsd.projects.menus.FinultimateCommons;
import com.codrellica.mail.beans.SendMessageRemote;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.hrms.employ.entities.EmployeeEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.codrellica.projects.commons.AppPropertiesHandler;
import com.rsdynamix.projects.commons.entities.EmailSettingEntity;
import com.rsdynamix.hrms.commons.employ.EmployeeRecruitmentBean;
import com.rsdynamix.hrms.commons.setup.beans.CurriculumVitaeBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.PreviousPasswordEntity;
import com.rsdynamix.projects.security.entities.RoleEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import com.rsdynamix.projects.security.entities.UserCounterEntity;
import com.rsdynamix.projects.security.entities.UserPrivilegeEntity;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.projects.commons.messages.beans.MessageSetupBean;
import com.codrellica.mail.dataobjects.MailObject;
import com.codrellica.mail.dataobjects.MailParameter;
import com.rsd.projects.menus.ApplicationManagerBean;
import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamix.finance.requisition.beans.PaymentRequisitionBean;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import com.rsdynamics.projects.field.operators.UserAccountFieldOperator;
import com.rsdynamics.projects.query.operators.QueryOperators;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.finance.requisition.beans.ActionStageBean;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.security.entities.LoginAuthType;
import com.rsdynamix.projects.security.entities.UserRoleMapEntity;
import com.rsdynamix.tns.util.Constants;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "userManagerBean")
public class UserManagerBean implements Serializable {

    public static final String LOG_OUT_CTRL = "log-out";
    /**/
    public static final String NAVIGATE_TO_SUBSYS_CTRL = "goto-subsys";
    /**/
    public static final String SUBSYS_URL_CTRL = "subsysurl";
    /**/
    public static final String LOG_IN_CTRL = "log-in";
    /**/
    public static final String LOG_IN_CTRL_USER_FRONT = "log-in-user";
    /**/
    public static final String CTRL_PARAM = "ctrl";
    /**/
    public static final String ADMIN_ROLE = "Administrator";
    /**
     * CLAIMS*
     */
    public static final String LOG_OUT_NON_MOTOR_CTRL = "log-out-non-motor";
    /**/
    public static final String LOG_IN_NON_MOTOR_CTRL = "log-in-non-motor";
    /**/
    public static final String ACCT_CREATE_NOTIF_ID_KEY = "acct_create_notif";
    /**/
    public static final String ACCT_CREATE_NOTIF_ID_VALUE = "19";
    /**/
    public static final String PASSWD_RECOVERY_NOTIF_ID_KEY = "passwd_recovery_notif";
    /**/
    public static final String PASSWD_RECOVERY_NOTIF_ID_VALUE = "4";
    /**/
    public static final String PASSWD_CHG_NOTIF_ID_KEY = "passwd_chg_notif";
    /**/
    public static final String PASSWD_CHG_NOTIF_ID_VALUE = "5";
    /**/
    public static final String ACCT_CREATE_HOLDER_PARAM = "accountHolder";
    /**/
    public static final String ACCT_CREATE_UNAME_PARAM = "userName";
    /**/
    public static final String ACCT_CREATE_PASSWD_PARAM = "password";
    /**/
    public static final String ACCT_CREATE_ROLE_PARAM = "roleName";
    /**/
    public static final String PASS_CHG_HOLDER_PARAM = "accountHolder";
    /**/
    public static final String PASS_CHG_USERNAME_PARAM = "username";
    /**/
    public static final String PASS_CHG_NEWPASSWD_PARAM = "newPasswd";
    /**/
    public static final String PASS_CHG_OLDPASSWD_PARAM = "oldPasswd";
    /**/
    public static final String PREV_PASS_ID_KEY = "prev_pass_id";
    /**/
    public static final String PREV_PASS_ID_VALUE = "1";
    /**/
    public static final String PASS_RECOVERY_STAFF_NAME_PARAM = "staffName";
    //
    public static final String PASS_RECOVERY_USER_NAME_PARAM = "userName";
    //
    public static final String USER_ROLE_MAP_ID = "useracct.role.map.id";
    //
    public static final String USER_ACCT_ID = "user.acct.id";
    //
    public static final String PASS_RECOVERY_PASSWORD_PARAM = "password";

    public static final String LOGIN_PAGE_URL = "/security/login.xhtml";
    //
    public static final String PORTAL_LOGIN_PAGE_URL = "http://localhost:8080/FinultimatePortal/security/login.jsf";
    //
    public static final String PORTAL_HOME_PAGE_URL = "http://localhost:8080/FinultimatePortal/home.jsf";
    //
    public static final String HOME_PAGE_URL = "/home.xhtml";
    //
    private static final String USERNAME_KEY = "userName";
    //
    private static final String SUBSYSTEM_NAME_KEY = "subsystem";
    //
    private static final String FINULTIMATE_HRMS_NAME = "finultimateHrms";
    //
    private static final String FINULTIMATE_CRM_NAME = "finultimateCrm";
    //
    private static final String FINULTIMATE_FIN_NAME = "finultimateFinance";
    //
    private UserAccountEntity userAccount;
    private UserAccountEntity account;
    private String oldPassword;
    private String newPassword;
    private String confirmedPassword;
    private String homePage;
    private boolean componentEnabled;
    private ClaimModuleType moduleType;
    //
    private String emailAddress;
    private String passwordRecoveryMssg;
    //
    private String loginMessage;
    //
    private int pendingRequisitionCount;
    private boolean quoteEnabled;
    //
    //private int loginStatus = 0;
    private String userName;
    //
    private List<UserAccountEntity> userAccountList;
    //
    private boolean portalLogin;
    
    private Locale locale;
    
    private ResourceBundle msgResourceBundle;
    private boolean changedLocal; 

    /**
     * Creates a new instance of UserManagerBean
     */
    public UserManagerBean() {
        userAccount = new UserAccountEntity();
        userAccountList = new ArrayList();
        account = new UserAccountEntity();

        oldPassword = "";
        newPassword = "";
        confirmedPassword = "";
        homePage = "";

        passwordRecoveryMssg = "";
        pendingRequisitionCount = 0;
        quoteEnabled = false;

        executePortalModeLogin();
    }
    
    @PostConstruct
    public void init() {
        //System.out.println("Locale State: " + (locale==null?"null": "not null"));
        if(getLocale() == null) {
            setLocale(FacesContext.getCurrentInstance().getExternalContext().getRequestLocale());
            System.out.println("Setting Locale in PostContruct:" + getLocale().getLanguage());
        }
    }

    public void executePortalModeLogin() {
        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        if (request != null) {
            String userName = request.getParameter(USERNAME_KEY);
            String subsystem = request.getParameter(SUBSYSTEM_NAME_KEY);

            if ((userName != null) && (userName.trim().length() > 0)) {
                userAccount.setUserName(userName);
                loginUser();

                portalLogin = true;

                try {
                    if (subsystem.equals(FINULTIMATE_HRMS_NAME)) {
                        response.sendRedirect(CommonBean.getContextRoot() + "/" + menuManagerBean.gotoHRMSSubsystem());
                    } else if (subsystem.equals(FINULTIMATE_FIN_NAME)) {
                        response.sendRedirect(CommonBean.getContextRoot() + "/" + menuManagerBean.gotoFINSubsystem());
                    } else if (subsystem.equals(FINULTIMATE_CRM_NAME)) {
                        response.sendRedirect(CommonBean.getContextRoot() + "/" + menuManagerBean.gotoCRMSubsystem());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public boolean usersAvailable() {
        boolean hasUsers = false;
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
            UserCounterEntity criteria = new UserCounterEntity();
            List<AbstractEntity> entityList = dataServer.findData(criteria);
            if ((entityList != null) && (entityList.size() > 0)) {
                UserCounterEntity entity = (UserCounterEntity) entityList.get(0);
                if (entity.getUserCounter() > 0) {
                    hasUsers = true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return hasUsers;
    }

    public void loadUserAccount() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        UserAccountEntity criteria = new UserAccountEntity();
        criteria.setUserName(getUserAccount().getUserName());
        criteria.setPassword(getUserAccount().getPassword());

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        try {
            List<AbstractEntity> baseUserAccountList = dataServer.findData(criteria);

            if ((baseUserAccountList != null) && (baseUserAccountList.size() > 0)) {
                setUserAccount((UserAccountEntity) baseUserAccountList.get(0));
                getUserAccount().initializeTransients();

                RoleEntity role = new RoleEntity();
                UserPrivilegeEntity privCriteria = new UserPrivilegeEntity();
                privCriteria.setRoleID(getUserAccount().getRoleID());

                List<AbstractEntity> baseUserPrivilegeList = dataServer.findData(privCriteria);
                for (AbstractEntity baseUserPrivilege : baseUserPrivilegeList) {
                    role.getPrivilegeList().add((UserPrivilegeEntity) baseUserPrivilege);
                }
                getUserAccount().setRole(role);

                PreviousPasswordEntity ppCriteria = new PreviousPasswordEntity();
                ppCriteria.setUserName(getUserAccount().getUserName());
                ppCriteria.setPassword(getUserAccount().getPassword());
            } else {
                //TODO treat the mess...
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public String clearAccountCache() {
        account = new UserAccountEntity();
        userAccountList = new ArrayList<UserAccountEntity>();

        return "";
    }

    public void reloadUserAccountList() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        UserAccountEntity user = new UserAccountEntity();

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }

        MenuEntity privilege = privilegeBean.findPrivilegeByName(
                menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_USR_ACCT_MENU_ITEM));

        try {
            List<AbstractEntity> baseUserAccountList = dataServer.findData(user);
            userAccountList.clear();
            if (baseUserAccountList != null && baseUserAccountList.size() > 0) {
                RoleEntity role = null;
                for (AbstractEntity entity : baseUserAccountList) {
                    user = (UserAccountEntity) entity;
                    user.setPassword(null);
                    user.setActivated(user.getApprovalStatusID() >= privilege.getApprovedStatusID());
                    role = privilegeBean.findRoleByRoleID(user.getRoleID());
                    if (role != null) {
                        user.setRole(role);
                    }
                    userAccountList.add(user);
                }

            }

            Collections.sort(userAccountList, new Comparator<UserAccountEntity>() {
                public int compare(UserAccountEntity o1, UserAccountEntity o2) {
                    return (o1.getUserName().compareTo(o2.getUserName()));
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public String findUserAccountByCriteria() {
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

        userAccountList.clear();
        boolean hasCriteria = false;

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        UserAccountEntity criteria = new UserAccountEntity();
        criteria.setQueryOperators(new UserAccountFieldOperator());

        if (!account.getUserName().trim().isEmpty()) {
            ((UserAccountFieldOperator) criteria.getQueryOperators()).setUserName(QueryOperators.LIKE);
            criteria.setUserName("%" + account.getUserName() + "%");
            hasCriteria = true;
        }

        if (!account.getEmailAddress().trim().isEmpty()) {
            ((UserAccountFieldOperator) criteria.getQueryOperators()).setEmailAddress(QueryOperators.LIKE);
            criteria.setEmailAddress("%" + account.getEmailAddress() + "%");
            hasCriteria = true;
        }

        if (account.getRoleID() > 0) {
            ((UserAccountFieldOperator) criteria.getQueryOperators()).setRoleID(QueryOperators.EQUALS);
            criteria.setRoleID(account.getRoleID());
            hasCriteria = true;
        }

        try {
            if (hasCriteria) {
                List<AbstractEntity> entityList = dataServer.findData(criteria);

                MenuEntity privilege = privilegeBean.findPrivilegeByName(
                        menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_USR_ACCT_MENU_ITEM));

                UserAccountEntity userAcct = null;
                if (entityList != null && entityList.size() > 0) {
                    RoleEntity role = null;
                    for (AbstractEntity entity : entityList) {
                        userAcct = (UserAccountEntity) entity;
                        userAcct.setActivated(
                                userAcct.getApprovalStatusID() >= privilege.getApprovedStatusID());

                        userAcct.setPassword(null);

                        UserRoleMapEntity urmCriteria = new UserRoleMapEntity();
                        urmCriteria.setUserName(userAcct.getUserName());

                        List<AbstractEntity> urmEntityList = dataServer.findData(urmCriteria);
                        for (AbstractEntity urmEntity : urmEntityList) {
                            UserRoleMapEntity userRoleMap = (UserRoleMapEntity) urmEntity;
                            role = privilegeBean.findRoleByRoleID(userRoleMap.getRoleID());

                            if ((role != null) && (!userAcct.getRoleList().contains(role))) {
                                userAcct.getRoleList().add(role);
                            }
                        }

                        userAccountList.add(userAcct);
                    }
                }

                Collections.sort(userAccountList, new Comparator<UserAccountEntity>() {
                    public int compare(UserAccountEntity o1, UserAccountEntity o2) {
                        return (o1.getUserName().compareTo(o2.getUserName()));
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public UserAccountEntity loadUserAccount(String userName) {
        UserAccountEntity userAcct = null;

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        UserAccountEntity criteria = new UserAccountEntity();
        criteria.setUserName(userName);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        try {
            List<AbstractEntity> baseUserAccountList = dataServer.findData(criteria);

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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_USR_ACCT_MENU_ITEM));

            if ((baseUserAccountList != null) && (baseUserAccountList.size() > 0)) {
                userAcct = (UserAccountEntity) baseUserAccountList.get(0);

                userAcct.setActivated(userAcct.getApprovalStatusID() >= privilege.getApprovedStatusID());
                userAcct.initializeTransients();

                RoleEntity role = new RoleEntity();
                UserPrivilegeEntity privCriteria = new UserPrivilegeEntity();
                privCriteria.setRoleID(userAcct.getRoleID());

                List<AbstractEntity> baseUserPrivilegeList = dataServer.findData(privCriteria);
                for (AbstractEntity baseUserPrivilege : baseUserPrivilegeList) {
                    role.getPrivilegeList().add((UserPrivilegeEntity) baseUserPrivilege);
                }
                userAcct.setRole(role);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return userAcct;
    }

    public void loadUserAccount(BusinessActionTrailEntity businessActionTrail) throws SQLException, Exception {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        UserAccountEntity criteria = new UserAccountEntity();

        List<AbstractEntity> baseUserAccountList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());

        if ((baseUserAccountList != null) && (baseUserAccountList.size() > 0)) {
            UserAccountEntity userAcct = (UserAccountEntity) baseUserAccountList.get(0);
            userAcct.initializeTransients();

            userAcct.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

            RoleEntity role = new RoleEntity();
            UserPrivilegeEntity privCriteria = new UserPrivilegeEntity();
            privCriteria.setRoleID(getUserAccount().getRoleID());

            List<AbstractEntity> baseUserPrivilegeList = dataServer.findData(privCriteria);
            for (AbstractEntity baseUserPrivilege : baseUserPrivilegeList) {
                role.getPrivilegeList().add((UserPrivilegeEntity) baseUserPrivilege);
            }
            userAcct.setRole(role);

            userAccountList.add(userAcct);
        } else {
            applicationMessageBean.insertMessage("No Record Found For This User Account!", MessageType.ERROR_MESSAGE);
        }
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

        try {
            if (userAccountList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        userAccountList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadUserAccount(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadUserAccount(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
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

        try {
            if (userAccountList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        userAccountList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadUserAccount(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadUserAccount(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.USER_ACCOUNT);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(account.getUserAccountID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadUserAccount((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "userroleaudittrail.jsf";
            } else {
                applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    public String backToCallerPage() {
        return FinultimateCommons.retrieveLastRequestingResource();
    }

    public String passwordRecovery() {
        passwordRecoveryMssg = "";
        //loginStatus = 0;

        UserAccountEntity criteria = new UserAccountEntity();
        criteria.setEmailAddress(getEmailAddress());

        SendMessageRemote mailServer = FinanceServiceLocator.locateMailMessageServer();

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
        List<AbstractEntity> baseUserAccountList = null;
        try {
            baseUserAccountList = dataServer.findData(criteria);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if ((baseUserAccountList != null) && (baseUserAccountList.size() > 0)) {
            UserAccountEntity user = (UserAccountEntity) baseUserAccountList.get(0);

            if (mailServer != null) {
                try {
                    int emailSettingID = Integer.parseInt(AppPropertiesHandler.getValue(
                            PASSWD_RECOVERY_NOTIF_ID_KEY,
                            PASSWD_RECOVERY_NOTIF_ID_VALUE,
                            AppPropertiesHandler.CLAIM_PROPERTIES_FILE, false));

                    MessageSetupBean mailConfigBean = (MessageSetupBean) CommonBean.getBeanFromContext(
                            "#{sessionScope.messageSetupBean}", MessageSetupBean.class);
                    if (mailConfigBean == null) {
                        mailConfigBean = new MessageSetupBean();
                        CommonBean.setBeanToContext("#{sessionScope.messageSetupBean}",
                                MessageSetupBean.class, mailConfigBean);
                    }
                    EmailSettingEntity emailSetting = mailConfigBean.findEmailSettingByID(emailSettingID);

                    if (emailSetting != null) {
                        MailObject mail = new MailObject();
                        mail.setBody(emailSetting.getMailBody());
                        mail = mailConfigBean.populateTypeParameters(mail, emailSetting);

                        MailParameter param = new MailParameter();
                        param.setKey(PASS_RECOVERY_STAFF_NAME_PARAM);
                        param.setValue(user.getUserName());
                        mail.getMailParamterList().add(param);

                        param = new MailParameter();
                        param.setKey(PASS_RECOVERY_USER_NAME_PARAM);
                        param.setValue(user.getUserName());
                        mail.getMailParamterList().add(param);

                        param = new MailParameter();
                        param.setKey(PASS_RECOVERY_PASSWORD_PARAM);
                        param.setValue(user.getPassword());
                        mail.getMailParamterList().add(param);

                        mail.setSubject(emailSetting.getSubject());
                        mail.setRecepientAddress(user.getEmailAddress());
                        mail.setSenderDisplayName(emailSetting.getSenderName());
                        mail.setSenderAddress(emailSetting.getSenderEmail());

                        //dispatchMail(mail);
                        mailServer.dispatchMail(mail);

                        passwordRecoveryMssg = "We have sent you an email to your specified "
                                + "email address. Please check your email and follow the instructions in it.";
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return "";
    }

    public String changePassword() {
        SendMessageRemote mailServer = FinanceServiceLocator.locateMailMessageServer();
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        if (getOldPassword().equals(getUserAccount().getPassword())) {
            if (!getOldPassword().equals(getNewPassword())) {
                PreviousPasswordEntity pPass = new PreviousPasswordEntity();
                pPass.setPassword(getOldPassword());
                pPass.setUserName(getUserAccount().getUserName());
                getUserAccount().setPassword(getNewPassword());

                try {
                    long prevPassID = Long.parseLong(AppPropertiesHandler.getValue(PREV_PASS_ID_KEY,
                            PREV_PASS_ID_VALUE, AppPropertiesHandler.CLAIM_PROPERTIES_FILE, true));
                    pPass.setPasswordID(prevPassID);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                try {
                    dataServer.updateData(getUserAccount());
                    dataServer.saveData(pPass);

                    if (mailServer != null) {
                        int emailSettingID = Integer.parseInt(AppPropertiesHandler.getValue(
                                PASSWD_CHG_NOTIF_ID_KEY,
                                PASSWD_CHG_NOTIF_ID_VALUE,
                                AppPropertiesHandler.CLAIM_PROPERTIES_FILE, false));

                        MessageSetupBean mailConfigBean = (MessageSetupBean) CommonBean.getBeanFromContext(
                                "#{sessionScope.messageSetupBean}", MessageSetupBean.class);
                        if (mailConfigBean == null) {
                            mailConfigBean = new MessageSetupBean();
                            CommonBean.setBeanToContext("#{sessionScope.messageSetupBean}",
                                    MessageSetupBean.class, mailConfigBean);
                        }
                        EmailSettingEntity emailSetting = mailConfigBean.findEmailSettingByID(emailSettingID);

                        if (emailSetting != null) {
                            MailObject mail = new MailObject();
                            mail.setBody(emailSetting.getMailBody());
                            mail = mailConfigBean.populateTypeParameters(mail, emailSetting);

                            MailParameter param = new MailParameter();
                            param.setKey(PASS_CHG_HOLDER_PARAM);
                            param.setValue(getUserAccount().getUserName());
                            mail.getMailParamterList().add(param);

                            param = new MailParameter();
                            param.setKey(PASS_CHG_USERNAME_PARAM);
                            param.setValue(getUserAccount().getUserName());
                            mail.getMailParamterList().add(param);

                            param = new MailParameter();
                            param.setKey(PASS_CHG_NEWPASSWD_PARAM);
                            param.setValue(getNewPassword());
                            mail.getMailParamterList().add(param);

                            param = new MailParameter();
                            param.setKey(PASS_CHG_OLDPASSWD_PARAM);
                            param.setValue(getOldPassword());
                            mail.getMailParamterList().add(param);

                            mail.setSubject(emailSetting.getSubject());
                            mail.setRecepientAddress(getUserAccount().getEmailAddress());
                            mail.setSenderDisplayName(emailSetting.getSenderName());
                            mail.setSenderAddress(emailSetting.getSenderEmail());

                            mailServer.dispatchMail(mail);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                //to do!!
            }
        } else {
            //to do!!
        }

        return "";
    }

    public void roleMenuSelected(ValueChangeEvent vce) {
        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        privilegeBean.roleMenuSelected(vce);
        account.setPayReqApprovalID(privilegeBean.getRole().getPayReqApprovalID());
    }

    public void userRoleSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        account.setRole(account.getRoleList().get(rowIndex));

        CommonBean.deselectOtherItems(account.getRole(), account.getRoleList());
    }

    public void myRoleSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        userAccount.setRole(userAccount.getRoleList().get(rowIndex));

        CommonBean.deselectOtherItems(userAccount.getRole(), userAccount.getRoleList());
    }

    public String removeRoleToUserAccount() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (account.getRoleID() != userAccount.getRoleID()) {
            UserRoleMapEntity criteria = new UserRoleMapEntity();
            criteria.setUserName(account.getUserName());
            criteria.setRoleID(account.getRole().getRoleID());

            try {
                dataServer.beginTransaction();
                dataServer.deleteData(criteria);
                dataServer.endTransaction();
                account.getRoleList().remove(account.getRole());
                applicationMessageBean.insertMessage("Role has been removed from User Account", MessageType.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            applicationMessageBean.insertMessage(
                    "Invalid Operation: You Cannot Alter Your Own Account!", MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String addRoleToUserAccount() {
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

        RoleEntity role = privilegeBean.getRole();

        if ((role != null) && (!account.getRoleList().contains(role)) && (role.getRoleID() > 0)) {
            if (account.getRoleID() != userAccount.getRoleID()) {
                account.getRoleList().add(role);
            } else {
                applicationMessageBean.insertMessage(
                        "Invalid Operation: You Cannot Alter Your Own Account!", MessageType.ERROR_MESSAGE);
            }
        }
        account.getRole().setRoleID(0);

        return "";
    }

    public String switchUserAccountToRole() {
        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        PaymentRequisitionBean paymentRequisitionBean = (PaymentRequisitionBean) CommonBean.getBeanFromContext(
                "#{sessionScope.paymentRequisitionBean}", PaymentRequisitionBean.class);
        if (paymentRequisitionBean == null) {
            paymentRequisitionBean = new PaymentRequisitionBean();
            CommonBean.setBeanToContext("#{sessionScope.paymentRequisitionBean}", PaymentRequisitionBean.class, paymentRequisitionBean);
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

        RoleEntity role = privilegeBean.findRoleByRoleID(userAccount.getRole().getRoleID());
        if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            if (role != null) {
                userAccount.setRole(role);
                userAccount.setRoleID(role.getRoleID());
                userAccount.setPayReqApprovalID(role.getPayReqApprovalID());

                applicationMessageBean.insertMessage("Your User Account '" + userAccount.getUserName()
                        + "' has been switched to the role '"
                        + userAccount.getRole().getRoleName()
                        + "'", MessageType.INFORMATION_MESSAGE);

                paymentRequisitionBean.loadRequisitionOfUser();
                pendingRequisitionCount = paymentRequisitionBean.getPaymentRequisitionList().size();
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
            MenuEntity privilege = privilegeBean.findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_USR_ROLS_MENU_ITEM));

            if ((role != null) && (role.getApprovalStatusID() >= privilege.getApprovedStatusID())) {
                userAccount.setRole(role);
                userAccount.setRoleID(role.getRoleID());
                userAccount.setPayReqApprovalID(role.getPayReqApprovalID());

                applicationMessageBean.insertMessage("Your User Account '" + userAccount.getUserName()
                        + "' has been switched to the role '"
                        + userAccount.getRole().getRoleName()
                        + "'", MessageType.INFORMATION_MESSAGE);

                paymentRequisitionBean.loadRequisitionOfUser();
                pendingRequisitionCount = paymentRequisitionBean.getPaymentRequisitionList().size();
            } else if (role == null) {
                applicationMessageBean.insertMessage(
                        "Sorry, The Role Switched To Doesn't Exist! Please Contact Your System Admin.",
                        MessageType.ERROR_MESSAGE);
            } else {
                applicationMessageBean.insertMessage(
                        "Sorry, The Role Switched To Is Not Active! Please Contact Your System Admin.",
                        MessageType.ERROR_MESSAGE);
            }
        }

        return "";
    }

    public String insertUserAccount() {
        SendMessageRemote mailServer = FinanceServiceLocator.locateMailMessageServer();
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

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

        if ((account.getUserName().trim().length() > 0)
                && (account.getPassword().trim().length() >= 6)
                && (account.getPassword().trim().length() <= 50)
                && (account.getPassword().equals(account.getConfirmedPassword()))
                && (account.getEmailAddress().trim().length() > 0)) {
            try {
                UserAccountEntity userCriteria = new UserAccountEntity();
                userCriteria.setUserName(account.getUserName());

                List<AbstractEntity> entityList = dataServer.findData(userCriteria);
                if ((entityList != null) && (entityList.size() > 0)) {
                    applicationMessageBean.insertMessage(
                            "Duplicate User Creation Error: User Account already exists in the System!", MessageType.ERROR_MESSAGE);
                } else {
                    account.setDeleted(String.valueOf(false));
                    account.setRoleID(account.getRoleList().get(0).getRoleID());

                    RoleEntity role = privilegeBean.findRoleByRoleID(account.getRoleID());
                    if (role != null) {
                        account.setPayReqApprovalID(role.getPayReqApprovalID());
                    }

                    dataServer.beginTransaction();

                    for (RoleEntity acctRole : account.getRoleList()) {
                        UserRoleMapEntity userRoleMap = new UserRoleMapEntity();
                        userRoleMap.setRoleID(acctRole.getRoleID());
                        userRoleMap.setUserName(account.getUserName());
                        userRoleMap.setApprover(userAccount.getUserName());

                        int userRoleMapID = Integer.parseInt(appPropBean.getValue(USER_ROLE_MAP_ID,
                                FinultimateConstants.ONE_STR, true));

                        userRoleMap.setUserRoleMapID(userRoleMapID);

                        dataServer.saveData(userRoleMap);
                    }

                    int userAcctID = Integer.parseInt(appPropBean.getValue(USER_ACCT_ID,
                            FinultimateConstants.ONE_STR, true));

                    account.setUserAccountID(userAcctID);
                    account.setApprover(userAccount.getUserName());

                    dataServer.saveData(account);

                    UserCounterEntity criteria = new UserCounterEntity();
                    criteria.setCounterID(1);
                    entityList = dataServer.findData(criteria);
                    if ((entityList != null) && (entityList.size() > 0)) {
                        UserCounterEntity entity = (UserCounterEntity) entityList.get(0);
                        entity.setUserCounter(entity.getUserCounter() + 1);

                        dataServer.updateData(entity);
                    } else {
                        UserCounterEntity entity = new UserCounterEntity();
                        entity.setCounterID(1);
                        entity.setUserCounter(1);

                        dataServer.saveData(entity);
                    }

                    if (mailServer != null) {
                        int emailSettingID = Integer.parseInt(appPropBean.getValue(
                                ACCT_CREATE_NOTIF_ID_KEY,
                                ACCT_CREATE_NOTIF_ID_VALUE, false));

                        MessageSetupBean mailConfigBean = (MessageSetupBean) CommonBean.getBeanFromContext(
                                "#{sessionScope.messageSetupBean}", MessageSetupBean.class);
                        if (mailConfigBean == null) {
                            mailConfigBean = new MessageSetupBean();
                            CommonBean.setBeanToContext("#{sessionScope.messageSetupBean}",
                                    MessageSetupBean.class, mailConfigBean);
                        }
                        EmailSettingEntity emailSetting = mailConfigBean.findEmailSettingByID(emailSettingID);

                        if (emailSetting != null) {
                            MailObject mail = new MailObject();
                            mail = mailConfigBean.populateTypeParameters(mail, emailSetting);
                            mail.setBody(emailSetting.getMailBody());

                            MailParameter param = new MailParameter();
                            param.setKey(ACCT_CREATE_HOLDER_PARAM);
                            param.setValue(getAccount().getUserName());
                            mail.getMailParamterList().add(param);

                            param = new MailParameter();
                            param.setKey(ACCT_CREATE_UNAME_PARAM);
                            param.setValue(getAccount().getUserName());
                            mail.getMailParamterList().add(param);

                            param = new MailParameter();
                            param.setKey(ACCT_CREATE_PASSWD_PARAM);
                            param.setValue(getAccount().getPassword());
                            mail.getMailParamterList().add(param);

                            param = new MailParameter();
                            param.setKey(ACCT_CREATE_ROLE_PARAM);

                            if (role != null) {
                                param.setValue(role.getRoleName());
                            }
                            mail.getMailParamterList().add(param);

                            mail.setSubject(emailSetting.getSubject());
                            mail.setRecepientAddress(getAccount().getEmailAddress());
                            mail.setSenderDisplayName(emailSetting.getSenderName());
                            mail.setSenderAddress(emailSetting.getSenderEmail());

                            mailServer.dispatchMail(mail);
                        }
                    }
                    applicationMessageBean.insertMessage(
                            "User Account Creation Successful!", MessageType.SUCCESS_MESSAGE);

                    dataServer.endTransaction();
                }
            } catch (SQLException ex) {
                dataServer.rollbackTransaction();
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                dataServer.rollbackTransaction();
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                ex.printStackTrace();
            }

            account = new UserAccountEntity();
        } else if (account.getUserName().trim().length() == 0) {
            applicationMessageBean.insertMessage(
                    "Empty User Name Field Error: User Name Field Cannot Be Empty!", MessageType.ERROR_MESSAGE);
        } else if ((account.getPassword().trim().length() < 6) || (account.getPassword().trim().length() > 50)) {
            applicationMessageBean.insertMessage(
                    "Wrong Password Structure: The Password Length Cannot Be Shorter Than 6, "
                    + "And It Cannot Be Longer Than 50 characters!", MessageType.ERROR_MESSAGE);
        } else if (!account.getPassword().equals(account.getConfirmedPassword())) {
            applicationMessageBean.insertMessage(
                    "Password Not Confirmed: Make Sure That The Value In The 'Password' "
                    + "Field Matches The Value In The 'Confirm Password'!", MessageType.ERROR_MESSAGE);
        } else if (account.getEmailAddress().trim().length() == 0) {
            applicationMessageBean.insertMessage(
                    "Empty Email Field Error: Please Specify A Valid Email For This User!", MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    //public boolean passwordR
    public String modifyUserAccount() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

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

        try {
            UserAccountEntity criteria = new UserAccountEntity();
            criteria.setUserName(account.getUserName());

            List<AbstractEntity> entityList = dataServer.findData(criteria);
            if ((entityList != null) && (entityList.size() > 0)) {
                UserAccountEntity userAcct = (UserAccountEntity) entityList.get(0);

                userAcct.setEnableAlternativeLogin(account.getEnableAlternativeLogin());
                userAcct.setRoleID(account.getRoleID());
                userAcct.setEmailAddress(account.getEmailAddress());

                for (RoleEntity acctRole : account.getRoleList()) {
                    UserRoleMapEntity urmCriteria = new UserRoleMapEntity();
                    urmCriteria.setUserName(userAcct.getUserName());
                    urmCriteria.setRoleID(acctRole.getRoleID());

                    List<AbstractEntity> urmEntityList = dataServer.findData(urmCriteria);
                    if (urmEntityList.size() == 0) {
                        UserRoleMapEntity userRoleMap = new UserRoleMapEntity();
                        userRoleMap.setRoleID(acctRole.getRoleID());
                        userRoleMap.setUserName(account.getUserName());
                        userRoleMap.setApprover(userAccount.getUserName());

                        int userRoleMapID = Integer.parseInt(appPropBean.getValue(USER_ROLE_MAP_ID,
                                FinultimateConstants.ONE_STR, true));

                        userRoleMap.setUserRoleMapID(userRoleMapID);
                        dataServer.saveData(userRoleMap);
                    }
                }

                if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
                    userAcct.setApprovalStatusID(0);
                }

                dataServer.updateData(userAcct);
                applicationMessageBean.insertMessage(
                        "The User Account has been updated", MessageType.SUCCESS_MESSAGE);
            }
            account = new UserAccountEntity();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(
                    "An error occured while attempting to update the User Account", MessageType.ERROR_MESSAGE);
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_USR_ACCT_MENU_ITEM));

        if (account.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((account.getApprovalStatusID() == 0) || (account.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(account.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        account.setApprovalStatusID(account.getApprovalStatusID() + 1);
                        account.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(account);
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
                } else if (account.getApprovalStatusID()
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

    public String loginUser() {
        passwordRecoveryMssg = "";
        String requestedPage = "";

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        ApplicationManagerBean applicationManagerBean = (ApplicationManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.applicationManagerBean}", ApplicationManagerBean.class);
        if (applicationManagerBean == null) {
            applicationManagerBean = new ApplicationManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.applicationManagerBean}",
                    ApplicationManagerBean.class, applicationManagerBean);
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

        userName = userAccount.getUserName();//keep username in session
        //String password = userAccount.getPassword();
        String userPinPlusOTP = userAccount.getPinPlusOneTimePassword();
        userAccount.setUserName(null);//enforce login by login filter
        if (userName == null || userName.trim().isEmpty()) {
            applicationMessageBean.insertMessage("Specify User Name ", MessageType.INFORMATION_MESSAGE);
            userAccount.setPinPlusOneTimePassword(null);
            return "";
        }

        try {
            if ((userName.trim().length() > 0)) {
                UserAccountEntity criteria = new UserAccountEntity();
                criteria.setUserName(userName);

                FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                List<AbstractEntity> baseUserAccountList = dataServer.findData(criteria);
                if (baseUserAccountList.size() > 0) {
                    userAccount = (UserAccountEntity) baseUserAccountList.get(0);

                    if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
                        userAccount.initializeTransients();
                        userAccount.setPinPlusOneTimePassword(userPinPlusOTP);

                        UserRoleMapEntity urmCriteria = new UserRoleMapEntity();
                        urmCriteria.setUserName(userAccount.getUserName());

                        List<AbstractEntity> urmEntityList = dataServer.findData(urmCriteria);
                        for (AbstractEntity urmEntity : urmEntityList) {
                            UserRoleMapEntity userRoleMap = (UserRoleMapEntity) urmEntity;
                            RoleEntity role = privilegeBean.findRoleByRoleID(userRoleMap.getRoleID());
                            if (role != null) {
                                userAccount.setPayReqApprovalID(role.getPayReqApprovalID());
                                userAccount.getRoleList().add(role);
                            }
                        }

                        if (userAccount.getRoleList().size() > 0) {
                            userAccount.setRoleID(userAccount.getRoleList().get(0).getRoleID());
                            userAccount.getRole().setRoleID(userAccount.getRoleList().get(0).getRoleID());
                            userAccount.getRole().setRoleName(userAccount.getRoleList().get(0).getRoleName());
                        }

                        loginMessage = "";

                        PreviousPasswordEntity ppCriteria = new PreviousPasswordEntity();
                        ppCriteria.setUserName(getUserAccount().getUserName());

                        applicationManagerBean.getUserAccountList().add(userAccount);

                        requestedPage = authenticateUser();
                    } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
                        MenuEntity privilege = privilegeBean.findPrivilegeByName(
                                menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_USR_ACCT_MENU_ITEM));

                        if (userAccount.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                            userAccount.initializeTransients();
                            userAccount.setPinPlusOneTimePassword(userPinPlusOTP);

                            privilege = privilegeBean.findPrivilegeByName(
                                    menuManagerBean.getSystemMap().get(MenuManagerBean.NEW_USR_ROLS_MENU_ITEM));

                            UserRoleMapEntity urmCriteria = new UserRoleMapEntity();
                            urmCriteria.setUserName(userAccount.getUserName());

                            List<AbstractEntity> urmEntityList = dataServer.findData(urmCriteria);
                            for (AbstractEntity urmEntity : urmEntityList) {
                                UserRoleMapEntity userRoleMap = (UserRoleMapEntity) urmEntity;

                                RoleEntity role = privilegeBean.findRoleByRoleID(userRoleMap.getRoleID());
                                if (role != null) {
                                    userAccount.setPayReqApprovalID(role.getPayReqApprovalID());
                                    userAccount.getRoleList().add(role);
                                }
                            }

                            if (userAccount.getRoleList().size() > 0) {
                                userAccount.setRoleID(userAccount.getRoleList().get(0).getRoleID());
                                userAccount.getRole().setRoleID(userAccount.getRoleList().get(0).getRoleID());
                                userAccount.getRole().setRoleName(userAccount.getRoleList().get(0).getRoleName());
                            }

                            loginMessage = "";

                            PreviousPasswordEntity ppCriteria = new PreviousPasswordEntity();
                            ppCriteria.setUserName(getUserAccount().getUserName());

                            applicationManagerBean.getUserAccountList().add(userAccount);

                            requestedPage = authenticateUser();
                        } else {
                            applicationMessageBean.insertMessage(
                                    "Sorry, Your User Account Is Not Active On ASIRA. Please Contact Your System Admin.",
                                    MessageType.ERROR_MESSAGE);
                        }
                    }
                } else {
                    loginMessage = "Login Failed! Wrong user login detail, please try again.";
                    applicationMessageBean.insertMessage(loginMessage, MessageType.INFORMATION_MESSAGE);
                }
            } else {
                applicationMessageBean.insertMessage(
                        "Login Failed! Empty login details (The Username Or Password field is empty...)",
                        MessageType.INFORMATION_MESSAGE);
            }
            userAccount.setPinPlusOneTimePassword(null);
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return requestedPage;
    }

    public String authenticateUser() {
        String requestedPage = "/security/login.xhtml";

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}",
                    PrivilegeBean.class, privilegeBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        String userPinPlusOTP = userAccount.getPinPlusOneTimePassword();
        userAccount.setPinPlusOneTimePassword("");
        if (userPinPlusOTP == null || userPinPlusOTP.trim().isEmpty()) {
            applicationMessageBean.insertMessage("Enter your PIN and One Time Password", MessageType.INFORMATION_MESSAGE);
            return "";
        }

        try {
            //LoginAuthenticationService loginAuthenticationService = FinanceServiceLocator.locateLoginAuthenticationServer();

            String result = null;
            if (userAccount.isAlternativeLoginEnabled()) {
                result = null;//loginAuthenticationService.authenticateOnAD(userName, userPinPlusOTP);
            } else {
                result = null;//loginAuthenticationService.authenticateUser(userName, userPinPlusOTP);
            }

            result = "success";//remove this
            //if (result != null && result.trim().equalsIgnoreCase(ThirdPartySystem.LOGIN.getSuccessStatusCode())) {
            if (result != null) {
                requestedPage = "/home.jsf";
                loginMessage = "";

                UserPrivilegeEntity privCriteria = new UserPrivilegeEntity();
                privCriteria.setRoleID(getUserAccount().getRoleID());

                FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                List<AbstractEntity> baseUserPrivilegeList = dataServer.findData(privCriteria);
                for (AbstractEntity baseUserPrivilege : baseUserPrivilegeList) {
                    UserPrivilegeEntity uPriv = (UserPrivilegeEntity) baseUserPrivilege;
                    if ((uPriv.getPrivilegeName() == null) || (uPriv.getPrivilegeName().trim().equals(""))) {
                        MenuEntity priv = privilegeBean.findPrivilegeByID(uPriv.getPrivilegeId());
                        if (priv != null) {
                            uPriv.setPrivilegeName(priv.getMenuLabel());
                        }
                    }
                    getUserAccount().getRole().getPrivilegeList().add(uPriv);
                }

                PaymentRequisitionBean paymentRequisitionBean = (PaymentRequisitionBean) CommonBean.getBeanFromContext(
                        "#{sessionScope.paymentRequisitionBean}", PaymentRequisitionBean.class);
                if (paymentRequisitionBean == null) {
                    paymentRequisitionBean = new PaymentRequisitionBean();
                    CommonBean.setBeanToContext("#{sessionScope.paymentRequisitionBean}", PaymentRequisitionBean.class, paymentRequisitionBean);
                }
                pendingRequisitionCount = paymentRequisitionBean.getPaymentRequisitionList().size();

                applicationMessageBean.insertMessage(loginMessage, MessageType.INFORMATION_MESSAGE);
                userAccount.setUserName(userName);//prevent redirect by filter
            } else {
                applicationMessageBean.insertMessage(
                        "Login Failed! Wrong user login detail, please try again or contact the System Administrator",
                        MessageType.INFORMATION_MESSAGE);
                userAccount.setUserName(null);
            }
        } catch (Exception ex) {
            ex.printStackTrace();

            applicationMessageBean.insertMessage("An error occured, please contact the System Administrator",
                    MessageType.ERROR_MESSAGE);
        }
        requestedPage = "/home.jsf";//remove this....

        return requestedPage;
    }

    public String userFacingLogin() {
        String outcome = "success";

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

        try {
            UserAccountEntity criteria = new UserAccountEntity();
            criteria.setUserName(getUserAccount().getUserName());
            criteria.setPassword(getUserAccount().getPassword());

            FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
            List<AbstractEntity> baseUserAccountList = dataServer.findData(criteria);
            if (baseUserAccountList.size() > 0) {
                setUserAccount((UserAccountEntity) baseUserAccountList.get(0));
                getUserAccount().initializeTransients();

                RoleEntity role = new RoleEntity();
                UserPrivilegeEntity privCriteria = new UserPrivilegeEntity();
                privCriteria.setRoleID(getUserAccount().getRoleID());

                List<AbstractEntity> baseUserPrivilegeList = dataServer.findData(privCriteria);
                for (AbstractEntity baseUserPrivilege : baseUserPrivilegeList) {
                    UserPrivilegeEntity uPriv = (UserPrivilegeEntity) baseUserPrivilege;
                    if ((uPriv.getPrivilegeName() == null) || (uPriv.getPrivilegeName().trim().equals(""))) {
                        MenuEntity priv = privilegeBean.findPrivilegeByID(uPriv.getPrivilegeId());
                        if (priv != null) {
                            uPriv.setPrivilegeName(priv.getMenuLabel());
                        }
                    }
                    role.getPrivilegeList().add(uPriv);
                }
                getUserAccount().setRole(role);

                PreviousPasswordEntity ppCriteria = new PreviousPasswordEntity();
                ppCriteria.setUserName(getUserAccount().getUserName());
                ppCriteria.setPassword(getUserAccount().getPassword());

                outcome = "success";
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    public String logOutUser() {
        ApplicationManagerBean applicationManagerBean = (ApplicationManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.applicationManagerBean}", ApplicationManagerBean.class);
        if (applicationManagerBean == null) {
            applicationManagerBean = new ApplicationManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.applicationManagerBean}",
                    ApplicationManagerBean.class, applicationManagerBean);
        }

        applicationManagerBean.getUserAccountList().remove(getUserAccount());

        HttpSession session = ((HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().getRequest()).getSession(false);
        if (session != null) {
            session.invalidate();
        }

        String logoutLanding = "";
        if (portalLogin) {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            try {
                response.sendRedirect(PORTAL_LOGIN_PAGE_URL);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            logoutLanding = LOGIN_PAGE_URL;
        }

        return LOGIN_PAGE_URL;
    }

    public String gotoSystemHome() {
        String homeLanding = "";

        ApplicationManagerBean applicationManagerBean = (ApplicationManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.applicationManagerBean}", ApplicationManagerBean.class);
        if (applicationManagerBean == null) {
            applicationManagerBean = new ApplicationManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.applicationManagerBean}",
                    ApplicationManagerBean.class, applicationManagerBean);
        }

        applicationManagerBean.getUserAccountList().remove(getUserAccount());

        HttpSession session = ((HttpServletRequest) FacesContext.getCurrentInstance().
                getExternalContext().getRequest()).getSession(false);
        if (session != null) {
            session.invalidate();
        }

        if (portalLogin) {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            try {
                response.sendRedirect(PORTAL_HOME_PAGE_URL);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            homeLanding = HOME_PAGE_URL;
        }

        return homeLanding;
    }

    public void resumeSelectedForEmpSearch(ValueChangeEvent vce) {
        CurriculumVitaeBean cvBean = (CurriculumVitaeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.curriculumVitaeBean}", CurriculumVitaeBean.class);
        if (cvBean == null) {
            cvBean = new CurriculumVitaeBean();
            CommonBean.setBeanToContext(
                    "#{sessionScope.curriculumVitaeBean}", CurriculumVitaeBean.class, cvBean);
        }

        EmployeeRecruitmentBean recruitmentBean = (EmployeeRecruitmentBean) CommonBean.getBeanFromContext(
                "#{sessionScope.recruitmentBean}", EmployeeRecruitmentBean.class);
        if (recruitmentBean == null) {
            recruitmentBean = new EmployeeRecruitmentBean();
            CommonBean.setBeanToContext(
                    "#{sessionScope.recruitmentBean}", EmployeeRecruitmentBean.class, recruitmentBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        cvBean.resumeSelectedForEmpSearch(vce);

        try {
            EmployeeEntity employee = recruitmentBean.findBasicEmployeeByCurrID(
                    cvBean.getCurriculum().getCvID());
            if (employee != null) {
                account.setEmployeeCode(employee.getEmployeeCode());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public String gotoReqApprovialPage() {
        FinultimateCommons.captureRequestingResource();
        String requestingResource = "";

        ActionStageBean actionStageBean = (ActionStageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.actionStageBean}", ActionStageBean.class);
        if (actionStageBean == null) {
            actionStageBean = new ActionStageBean();
            CommonBean.setBeanToContext("#{sessionScope.actionStageBean}",
                    ActionStageBean.class, actionStageBean);
        }

        if ((userAccount.getPayReqApprovalID() / 100) == (actionStageBean.getPayReqApprovedApprovalStepID() - 1)) {
            requestingResource = "/requisition/requisitionmonitor.xhtml";
        } else if ((userAccount.getPayReqApprovalID() / 100) < actionStageBean.getPayReqApprovedApprovalStepID() - 1) {
            requestingResource = "/requisition/approvalpage.xhtml";
        }

        return requestingResource;
    }

    public String gotoEmployeeSearchPage() {
        FinultimateCommons.captureRequestingResource();
        return "employeesearch.xhtml";
    }

    public String returnToRequestingResource() {
        return FinultimateCommons.retrieveLastRequestingResource();
    }

    public String enableQuotePanel() {
        //loginStatus = 0;
        if (quoteEnabled) {
            quoteEnabled = false;
        } else {
            quoteEnabled = true;
        }

        return "";
    }

    public void alternateAuthenticationControlSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            boolean enableAltAuth = Boolean.parseBoolean(vce.getNewValue().toString());
            account.setEnableAlternativeLogin(LoginAuthType.TOKEN);
            if (enableAltAuth) {
                account.setEnableAlternativeLogin(LoginAuthType.ACTIVE_DIRECTORY);
            }
            account.setAlternativeLoginEnabled(enableAltAuth);

        }
    }

    public void userAccountSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            int rowIndex = CommonBean.getComponentEventRowIndex(vce);
            account = userAccountList.get(rowIndex);
            account.setSelected(Boolean.parseBoolean(vce.getNewValue().toString()));

            if (account.isSelected()) {
                deselectUnselectedUserAccounts(account);
            } else {
                account = new UserAccountEntity();
            }

        }
    }

    public void deselectUnselectedUserAccounts(UserAccountEntity userRec) {
        List<UserAccountEntity> userList = new ArrayList<UserAccountEntity>();

        for (UserAccountEntity user : userAccountList) {
            if (!user.getUserName().equals(userRec.getUserName())) {
                user.setSelected(false);
                userList.add(user);
            } else {
                userList.add(user);
            }
        }

        userAccountList = userList;
    }

    public String loadRequisitionOfUser() {
        PaymentRequisitionBean paymentRequisitionBean = (PaymentRequisitionBean) CommonBean.getBeanFromContext(
                "#{sessionScope.paymentRequisitionBean}", PaymentRequisitionBean.class);
        if (paymentRequisitionBean == null) {
            paymentRequisitionBean = new PaymentRequisitionBean();
            CommonBean.setBeanToContext("#{sessionScope.paymentRequisitionBean}",
                    PaymentRequisitionBean.class, paymentRequisitionBean);
        }
        paymentRequisitionBean.loadRequisitionOfUser();

        pendingRequisitionCount = paymentRequisitionBean.getPaymentRequisitionList().size();

        return "";
    }

    public UserAccountEntity getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccountEntity userAccount) {
        this.userAccount = userAccount;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmedPassword() {
        return confirmedPassword;
    }

    public void setConfirmedPassword(String confirmedPassword) {
        this.confirmedPassword = confirmedPassword;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    public String getHomePage() {
        return this.homePage;
    }

    public UserAccountEntity getAccount() {
        return account;
    }

    public void setAccount(UserAccountEntity account) {
        this.account = account;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @return the moduleType
     */
    public ClaimModuleType getModuleType() {
        return moduleType;
    }

    /**
     * @param moduleType the moduleType to set
     */
    public void setModuleType(ClaimModuleType moduleType) {
        this.moduleType = moduleType;
    }

    /**
     * @return the loginMessage
     */
    public String getLoginMessage() {
        return loginMessage;
    }

    /**
     * @param loginMessage the loginMessage to set
     */
    public void setLoginMessage(String loginMessage) {
        this.loginMessage = loginMessage;
    }

    /**
     * @return the passwordRecoveryMssg
     */
    public String getPasswordRecoveryMssg() {
        return passwordRecoveryMssg;
    }

    /**
     * @param passwordRecoveryMssg the passwordRecoveryMssg to set
     */
    public void setPasswordRecoveryMssg(String passwordRecoveryMssg) {
        this.passwordRecoveryMssg = passwordRecoveryMssg;
    }

    /**
     * @return the pendingRequisitionCount
     */
    public int getPendingRequisitionCount() {
        return pendingRequisitionCount;
    }

    /**
     * @param pendingRequisitionCount the pendingRequisitionCount to set
     */
    public void setPendingRequisitionCount(int pendingRequisitionCount) {
        this.pendingRequisitionCount = pendingRequisitionCount;
    }

    /**
     * @return the quoteEnabled
     */
    public boolean isQuoteEnabled() {
        return quoteEnabled;
    }

    /**
     * @param quoteEnabled the quoteEnabled to set
     */
    public void setQuoteEnabled(boolean quoteEnabled) {
        this.quoteEnabled = quoteEnabled;
    }

    /**
     * @return the userAccountList
     */
    public List<UserAccountEntity> getUserAccountList() {
        return userAccountList;
    }

    /**
     * @param userAccountList the userAccountList to set
     */
    public void setUserAccountList(List<UserAccountEntity> userAccountList) {
        this.userAccountList = userAccountList;
    }

    /**
     * @return the portalLogin
     */
    public boolean isPortalLogin() {
        return portalLogin;
    }

    /**
     * @param portalLogin the portalLogin to set
     */
    public void setPortalLogin(boolean portalLogin) {
        this.portalLogin = portalLogin;
    }
    
    public void changeLocale(String loc){
        setLocale(new Locale(loc));
        FacesContext.getCurrentInstance().getViewRoot().setLocale(getLocale());
        
        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}", MenuManagerBean.class, menuManagerBean);
        }
        
        menuManagerBean.setChangedLocale(true);
        changedLocal = true;
        
        System.out.println("Changing Locale = " + loc);
    }
    
    
    public ResourceBundle getMsgResourceBundle()
            throws MissingResourceException {
        if (msgResourceBundle == null || changedLocal) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            msgResourceBundle
                    = facesContext.getApplication().getResourceBundle(
                            facesContext, "msg");
            changedLocal = false;
        }
        return msgResourceBundle;
    }

    public String getMsgResourceBundleString(String resourceBundleKey) {
        try {
            return getMsgResourceBundle().getString(resourceBundleKey);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
