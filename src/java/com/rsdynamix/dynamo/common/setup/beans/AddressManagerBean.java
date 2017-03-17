/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.dynamo.common.setup.beans;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.setup.entities.CityEntity;
import com.rsdynamix.dynamo.setup.entities.CountryEntity;
import com.rsdynamix.dynamo.setup.entities.StateEntity;
import com.rsdynamix.dynamo.setup.entities.TitleEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamics.projects.field.operators.AddressFieldOperator;
import com.rsdynamics.projects.field.operators.TitleFieldOperator;
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
import com.rsdynamix.tns.util.Constants;
import java.sql.SQLException;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;

/**
 *
 * @author ushie
 */
@SessionScoped
@ManagedBean(name = "addressManagerBean")
public class AddressManagerBean implements Serializable {

    private static final String COUNTRY_ID_KEY = "country_id";
    //
    private static final String COUNTRY_ID_DEFAULT = "1";
    //
    public static final String NIGERIA_COUNTRY_ID_KEY = "naija_country_id";
    //
    public static final String NIGERIA_COUNTRY_ID_DEFAULT = "158";
    //
    private static final String STATE_ID_KEY = "state_id";
    //
    private static final String STATE_ID_DEFAULT = "1";
    //
    private static final String CITY_ID_KEY = "city_id";
    //
    private static final String CITY_ID_DEFAULT = "1";
    //
//    @EJB
//    static FinultimatePersistenceRemote dataServer;
    //
    private CountryEntity country;
    private StateEntity state;
    private CityEntity city;
    private TitleEntity title;

    private List<StateEntity> stateList;
    private List<CityEntity> cityList;
    private List<CountryEntity> countryList;
    private List<TitleEntity> titleList;
    //
    private List<SelectItem> countryItemList;
    private List<SelectItem> selectedStateItemList;
    private List<SelectItem> selectedCityItemList;
    //
    private List<SelectItem> selectedStateItemList2;
    private List<SelectItem> selectedCityItemList2;
    private List<SelectItem> selectedStateItemList3;
    private List<SelectItem> selectedCityItemList3;
    private List<SelectItem> titleItemList;
    //
    private List<SelectItem> nigeriaStateItemList;
    private long nigeriaID;
    private long searchCountryID;

    public AddressManagerBean() {
        country = new CountryEntity();
        state = new StateEntity();
        city = new CityEntity();
        title = new TitleEntity();

        nigeriaID = 0;

        countryItemList = new ArrayList<SelectItem>();
        selectedStateItemList = new ArrayList<SelectItem>();
        selectedCityItemList = new ArrayList<SelectItem>();

        nigeriaStateItemList = new ArrayList<SelectItem>();

        selectedStateItemList2 = new ArrayList<SelectItem>();
        selectedCityItemList2 = new ArrayList<SelectItem>();

        selectedStateItemList3 = new ArrayList<SelectItem>();
        selectedCityItemList3 = new ArrayList<SelectItem>();

        loadCountryList();
        loadStateList();
        loadCityList();
        loadTitles();
    }

    public String loadCountryList() {
        if ((getCountryList() == null) || (getCountryList().size() <= 0)) {
            reloadCountryList();
        }

        return "";
    }

    public String reloadCountryList() {
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

        try {
            countryList = new ArrayList<CountryEntity>();
            getCountryItemList().clear();

            FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

            long countryID = 0;

            try {
                countryID = Long.parseLong(appPropBean.getValue(NIGERIA_COUNTRY_ID_KEY,
                        NIGERIA_COUNTRY_ID_DEFAULT, false));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            CountryEntity criteria = new CountryEntity();
            List<AbstractEntity> baseCountryList = dataServer.findData(criteria);

            MenuEntity privilege = privilegeBean.findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.COUNTRIES_MENU_ITEM));

            CountryEntity nigeria = null;
            SelectItem itemNigeria = null;
            for (AbstractEntity baseCountry : baseCountryList) {
                if (countryID == ((CountryEntity) baseCountry).getCountryID()) {
                    nigeria = (CountryEntity) baseCountry;
                    itemNigeria = new SelectItem();
                    itemNigeria.setValue(((CountryEntity) baseCountry).getCountryID());
                    itemNigeria.setLabel(((CountryEntity) baseCountry).getCountryDesc());

                    nigeria.setActivated(
                            nigeria.getApprovalStatusID() >= privilege.getApprovedStatusID());

                    addToSelectItemList(nigeria);
                } else {
                    CountryEntity cntry = (CountryEntity) baseCountry;

                    cntry.setActivated(
                            cntry.getApprovalStatusID() >= privilege.getApprovedStatusID());

                    getCountryList().add(cntry);
                    addToSelectItemList(cntry);
                }
            }

            Collections.sort(getCountryList(), new Comparator<CountryEntity>() {

                public int compare(CountryEntity o1, CountryEntity o2) {
                    return o1.getCountryDesc().compareTo(o2.getCountryDesc());
                }
            });

            Collections.sort(getCountryItemList(), new Comparator<SelectItem>() {

                public int compare(SelectItem o1, SelectItem o2) {
                    return o1.getLabel().compareTo(o2.getLabel());
                }
            });

            if (getCountryItemList().size() == 0) {
                getCountryItemList().add(itemNigeria);
                getCountryList().add(nigeria);
            } else {
                getCountryItemList().add(0, itemNigeria);
                getCountryList().add(0, nigeria);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String loadCountryList(BusinessActionTrailEntity businessActionTrail) {
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

        try {
            countryList = new ArrayList<CountryEntity>();
            getCountryItemList().clear();

            FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

            long countryID = 0;

            try {
                countryID = Long.parseLong(appPropBean.getValue(NIGERIA_COUNTRY_ID_KEY,
                        NIGERIA_COUNTRY_ID_DEFAULT, false));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            CountryEntity criteria = new CountryEntity();
            List<AbstractEntity> baseCountryList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
            CountryEntity nigeria = null;
            SelectItem itemNigeria = null;
            for (AbstractEntity baseCountry : baseCountryList) {
                if (countryID == ((CountryEntity) baseCountry).getCountryID()) {
                    nigeria = (CountryEntity) baseCountry;
                    nigeria.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());
                } else {
                    CountryEntity ctryEntity = (CountryEntity) baseCountry;
                    ctryEntity.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                    getCountryList().add(ctryEntity);
                }
            }

            Collections.sort(getCountryList(), new Comparator<CountryEntity>() {

                public int compare(CountryEntity o1, CountryEntity o2) {
                    return o1.getCountryDesc().compareTo(o2.getCountryDesc());
                }
            });

            getCountryList().set(0, nigeria);

            Collections.sort(getCountryItemList(), new Comparator<SelectItem>() {

                public int compare(SelectItem o1, SelectItem o2) {
                    return o1.getLabel().compareTo(o2.getLabel());
                }
            });

            getCountryList().set(0, nigeria);
            getCountryItemList().set(0, itemNigeria);
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void addToSelectItemList(CountryEntity entity) {
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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.COUNTRIES_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getCountryID());
                item.setLabel(entity.getCountryDesc());
                countryItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getCountryID());
            item.setLabel(entity.getCountryDesc());
            countryItemList.add(item);
        }
    }

    public String loadStateList() {
        if ((stateList == null) || (stateList.size() <= 0)) {
            reloadStateList();
        }

        return "";
    }

    public String reloadStateList() {
        stateList = new ArrayList<StateEntity>();

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

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

        long countryID = 0;
        nigeriaStateItemList = new ArrayList<SelectItem>();

        try {
            countryID = Long.parseLong(appPropBean.getValue(NIGERIA_COUNTRY_ID_KEY,
                    NIGERIA_COUNTRY_ID_DEFAULT, false));

            StateEntity criteria = new StateEntity();
            List<AbstractEntity> baseStateList = dataServer.findData(criteria);

            MenuEntity privilege = privilegeBean.findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.STATES_MENU_ITEM));

            for (AbstractEntity baseState : baseStateList) {
                StateEntity province = (StateEntity) baseState;

                province.setActivated(province.getApprovalStatusID() >= privilege.getApprovedStatusID());
                CountryEntity count = findCountryByID(province.getCountryID());
                if (count != null) {
                    province.setCountryDesc(count.getCountryDesc());
                }

                if (province.getCountryID() == countryID) {
                    addToSelectItemList(province, nigeriaStateItemList);
                }
                stateList.add(province);
            }

            Collections.sort(stateList, new Comparator<StateEntity>() {

                public int compare(StateEntity o1, StateEntity o2) {
                    return ((int) o1.getStateDesc().compareTo(o2.getStateDesc()));
                }
            });

            Collections.sort(nigeriaStateItemList, new Comparator<SelectItem>() {

                public int compare(SelectItem o1, SelectItem o2) {
                    return (o1.getLabel().compareTo(o2.getLabel()));
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String loadStateList(BusinessActionTrailEntity businessActionTrail) {
        stateList = new ArrayList<StateEntity>();

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

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

        long countryID = 0;
        nigeriaStateItemList = new ArrayList<SelectItem>();

        try {
            countryID = Long.parseLong(appPropBean.getValue(NIGERIA_COUNTRY_ID_KEY,
                    NIGERIA_COUNTRY_ID_DEFAULT, false));

            StateEntity criteria = new StateEntity();
            List<AbstractEntity> baseStateList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());

            for (AbstractEntity baseState : baseStateList) {
                StateEntity province = (StateEntity) baseState;
                CountryEntity count = findCountryByID(province.getCountryID());
                count.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                if (count != null) {
                    province.setCountryDesc(count.getCountryDesc());
                }

                stateList.add(province);
            }

            Collections.sort(stateList, new Comparator<StateEntity>() {

                public int compare(StateEntity o1, StateEntity o2) {
                    return ((int) o1.getStateDesc().compareTo(o2.getStateDesc()));
                }
            });

            Collections.sort(nigeriaStateItemList, new Comparator<SelectItem>() {

                public int compare(SelectItem o1, SelectItem o2) {
                    return (o1.getLabel().compareTo(o2.getLabel()));
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void addToSelectItemList(StateEntity entity, List<SelectItem> stateItemList) {
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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.STATES_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getStateCode());
                item.setLabel(entity.getStateDesc());
                stateItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getStateCode());
            item.setLabel(entity.getStateDesc());
            stateItemList.add(item);
        }
    }

    public String loadCityList() {
        if ((cityList == null) || (getCityList().size() <= 0)) {
            reloadCityList();
        }

        return "";
    }

    public String reloadCityList() {
        cityList = new ArrayList<CityEntity>();

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

        try {
            CityEntity criteria = new CityEntity();
            List<AbstractEntity> baseCityList = dataServer.findData(criteria);

            MenuEntity privilege = privilegeBean.findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.CITIES_MENU_ITEM));

            for (AbstractEntity baseCity : baseCityList) {
                CityEntity citi = (CityEntity) baseCity;

                citi.setActivated(citi.getApprovalStatusID() >= privilege.getApprovedStatusID());
                CountryEntity count = findCountryByID(citi.getCountryID());
                if (count != null) {
                    citi.setCountryDesc(count.getCountryDesc());
                }

                StateEntity province = findStateByCode(citi.getStateCode(), citi.getCountryID());
                if (province != null) {
                    citi.setStateDesc(province.getStateDesc());
                }
                cityList.add(citi);
            }

            Collections.sort(cityList, new Comparator<CityEntity>() {

                public int compare(CityEntity o1, CityEntity o2) {
                    return (o1.getCityDesc().compareTo(o2.getCityDesc()));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String loadCityList(BusinessActionTrailEntity businessActionTrail) {
        cityList = new ArrayList<CityEntity>();

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            CityEntity criteria = new CityEntity();
            List<AbstractEntity> baseCityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());

            for (AbstractEntity baseCity : baseCityList) {
                CityEntity citi = (CityEntity) baseCity;
                citi.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                CountryEntity count = findCountryByID(citi.getCountryID());
                if (count != null) {
                    citi.setCountryDesc(count.getCountryDesc());
                }

                StateEntity province = findStateByCode(citi.getStateCode(), citi.getCountryID());
                if (province != null) {
                    citi.setStateDesc(province.getStateDesc());
                }
                cityList.add(citi);
            }

            Collections.sort(cityList, new Comparator<CityEntity>() {

                public int compare(CityEntity o1, CityEntity o2) {
                    return (o1.getCityDesc().compareTo(o2.getCityDesc()));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void addToSelectItemList(CityEntity entity, List<SelectItem> selectedCityItemList) {
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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.CITIES_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getCityID());
                item.setLabel(entity.getCityDesc());
                item.setDescription(entity.getStateCode());
                selectedCityItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getCityID());
            item.setLabel(entity.getCityDesc());
            item.setDescription(entity.getStateCode());
            selectedCityItemList.add(item);
        }
    }

    public String loadTitles() {
        if ((titleItemList == null) || (titleItemList.size() <= 0)) {
            reloadTitles();
        }

        return "";
    }

    public String reloadTitles() {
        titleItemList = new ArrayList<SelectItem>();
        titleList = new ArrayList<TitleEntity>();

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();
        TitleEntity titleCriteria = new TitleEntity();

        try {
            List<AbstractEntity> baseTitleList = dataServer.findData(titleCriteria);

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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.TITLES_MENU_ITEM));

            for (AbstractEntity baseTitle : baseTitleList) {
                TitleEntity title = (TitleEntity) baseTitle;

                title.setActivated(title.getApprovalStatusID() >= privilege.getApprovedStatusID());
                titleList.add(title);
                addToSelectItemList(title);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Collections.sort(getTitleItemList(), new Comparator<SelectItem>() {

            public int compare(SelectItem o1, SelectItem o2) {
                int ret = 0;
                if (o1.getLabel().compareTo(o2.getLabel()) == 1) {
                    return o1.getLabel().compareTo(o2.getLabel());
                }

                return ret;
            }
        });

        return "";
    }

    public String loadTitles(BusinessActionTrailEntity businessActionTrail) {
        titleItemList = new ArrayList<SelectItem>();
        titleList = new ArrayList<TitleEntity>();

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();
        TitleEntity titleCriteria = new TitleEntity();

        try {
            List<AbstractEntity> baseTitleList = dataServer.findMasterData(titleCriteria, businessActionTrail.getEntityMasterID());
            for (AbstractEntity baseTitle : baseTitleList) {
                TitleEntity title = (TitleEntity) baseTitle;
                title.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());

                titleList.add(title);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Collections.sort(getTitleItemList(), new Comparator<SelectItem>() {

            public int compare(SelectItem o1, SelectItem o2) {
                int ret = 0;
                if (o1.getLabel().compareTo(o2.getLabel()) == 1) {
                    return o1.getLabel().compareTo(o2.getLabel());
                }

                return ret;
            }
        });

        return "";
    }

    public void addToSelectItemList(TitleEntity entity) {
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.TITLES_MENU_ITEM));

        if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getCode());
            item.setLabel(entity.getTitleName());
            titleItemList.add(item);
        }
    }

    public String addTitle() {

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (title.getTitleName() == null || title.getTitleName().trim().isEmpty()) {
            applicationMessageBean.insertMessage("Specify Title", MessageType.ERROR_MESSAGE);
            return "";
        }
        if (!titleList.contains(title)) {
            titleList.add(title);
        } else {
            int index = titleList.indexOf(title);
            titleList.set(index, title);
        }
        title = new TitleEntity();

        return "";
    }

    public String saveTitleList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

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

        dataServer.beginTransaction();
        try {
            for (TitleEntity title : getTitleList()) {
                if (title.getCode().trim().length() > 0) {
                    dataServer.setSelectedTnsName(Constants.COMMONS_PU);
                    dataServer.updateData(title);
                } else {
                    int uwPolicyID = Integer.parseInt(appPropBean.getValue(
                            FinultimateConstants.TITLE_ID,
                            FinultimateConstants.ONE_STR,
                            true));

                    title.setCode(String.valueOf(uwPolicyID));

                    dataServer.setSelectedTnsName(Constants.COMMONS_PU);
                    dataServer.saveData(title);
                }
            }
            applicationMessageBean.insertMessage("Title has been saved", MessageType.SUCCESS_MESSAGE);
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

    public String activateTitle() {
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.TITLES_MENU_ITEM));

        if (title.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((title.getApprovalStatusID() == 0) || (title.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(title.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        title.setApprovalStatusID(title.getApprovalStatusID() + 1);
                        title.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(title);
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
                } else if (title.getApprovalStatusID()
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

    public String deleteTitle() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (getTitleList().contains(getTitle())) {
                if (getTitle().getCode().trim().length() > 0) {
                    if (getTitle().isActivated()) {
                        applicationMessageBean.insertMessage("This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                        return "";
                    }
                    FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                    TitleEntity criteria = new TitleEntity();
                    criteria.setCode(getTitle().getCode());
                    dataServer.beginTransaction();
                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                getTitleList().remove(getTitle());
                setTitle(new TitleEntity());
                applicationMessageBean.insertMessage("Title has been deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String findTitleByCriteria() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        titleList.clear();
        boolean hasCriteria = false;

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        TitleEntity criteria = new TitleEntity();
        criteria.setQueryOperators(new TitleFieldOperator());

        if (!title.getTitleName().trim().isEmpty()) {
            ((TitleFieldOperator) criteria.getQueryOperators()).setDescription(QueryOperators.LIKE);
            criteria.setTitleName("%" + title.getTitleName() + "%");
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
                        menuManagerBean.getSystemMap().get(MenuManagerBean.TITLES_MENU_ITEM));

                TitleEntity businessDivisionEntity = null;
                for (AbstractEntity entity : entityList) {
                    businessDivisionEntity = (TitleEntity) entity;

                    businessDivisionEntity.setActivated(businessDivisionEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                    titleList.add(businessDivisionEntity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String findCountryByCriteria() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        countryList.clear();
        boolean hasCriteria = false;

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        CountryEntity criteria = new CountryEntity();
        criteria.setQueryOperators(new AddressFieldOperator());

        if (!country.getCountryDesc().trim().isEmpty()) {
            ((AddressFieldOperator) criteria.getQueryOperators()).setCountryDesc(QueryOperators.LIKE);
            criteria.setCountryDesc("%" + country.getCountryDesc() + "%");
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
                        menuManagerBean.getSystemMap().get(MenuManagerBean.COUNTRIES_MENU_ITEM));

                CountryEntity businessDivisionEntity = null;
                for (AbstractEntity entity : entityList) {
                    businessDivisionEntity = (CountryEntity) entity;
                    businessDivisionEntity.setActivated(businessDivisionEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                    countryList.add(businessDivisionEntity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String findStateByCriteria() {
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

        stateList.clear();
        boolean hasCriteria = false;

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        StateEntity criteria = new StateEntity();
        criteria.setQueryOperators(new AddressFieldOperator());

        if (state.getCountryID() > 0) {
            ((AddressFieldOperator) criteria.getQueryOperators()).setCountryID(QueryOperators.EQUALS);
            criteria.setCountryID(state.getCountryID());
            hasCriteria = true;
        }
        if (state.getStateDesc() != null && !state.getStateDesc().trim().isEmpty()) {
            ((AddressFieldOperator) criteria.getQueryOperators()).setStateDesc(QueryOperators.LIKE);
            criteria.setStateDesc("%" + state.getStateDesc() + "%");
            hasCriteria = true;
        }
        if (state.getStateCode() != null && !state.getStateCode().trim().isEmpty() && !state.getStateCode().equals("NA")) {
            ((AddressFieldOperator) criteria.getQueryOperators()).setStateCode(QueryOperators.LIKE);
            criteria.setStateCode("%" + state.getStateCode() + "%");
            hasCriteria = true;
        }

        try {
            if (hasCriteria) {
                List<AbstractEntity> entityList = dataServer.findData(criteria);

                MenuEntity privilege = privilegeBean.findPrivilegeByName(
                        menuManagerBean.getSystemMap().get(MenuManagerBean.STATES_MENU_ITEM));

                StateEntity businessDivisionEntity = null;
                for (AbstractEntity entity : entityList) {
                    businessDivisionEntity = (StateEntity) entity;

                    businessDivisionEntity.setActivated(
                            businessDivisionEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                    businessDivisionEntity.setCountryDesc(
                            findCountryByID(businessDivisionEntity.getCountryID()).getCountryDesc());

                    stateList.add(businessDivisionEntity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String findCityByCriteria() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        cityList.clear();
        boolean hasCriteria = false;

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        CityEntity criteria = new CityEntity();
        criteria.setQueryOperators(new AddressFieldOperator());

        if (city.getCountryID() > 0) {
            ((AddressFieldOperator) criteria.getQueryOperators()).setCountryID(QueryOperators.EQUALS);
            criteria.setCountryID(city.getCountryID());
            hasCriteria = true;
        }
        if (city.getCityDesc() != null && !city.getCityDesc().trim().isEmpty()) {
            ((AddressFieldOperator) criteria.getQueryOperators()).setCityDesc(QueryOperators.LIKE);
            criteria.setCityDesc("%" + city.getCityDesc() + "%");
            hasCriteria = true;
        }
        if (city.getStateCode() != null && !city.getStateCode().trim().isEmpty() && !city.getStateCode().equals("NA")) {
            ((AddressFieldOperator) criteria.getQueryOperators()).setStateCode(QueryOperators.LIKE);
            criteria.setStateCode("%" + city.getStateCode() + "%");
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
                        menuManagerBean.getSystemMap().get(MenuManagerBean.CITIES_MENU_ITEM));

                CityEntity businessDivisionEntity = null;
                for (AbstractEntity entity : entityList) {
                    businessDivisionEntity = (CityEntity) entity;
                    businessDivisionEntity.setCountryDesc(
                            findCountryByID(businessDivisionEntity.getCountryID()).getCountryDesc());

                    businessDivisionEntity.setStateDesc(findStateByCode(
                            businessDivisionEntity.getStateCode(), businessDivisionEntity.getCountryID()).getStateDesc());

                    businessDivisionEntity.setActivated(
                            businessDivisionEntity.getApprovalStatusID() >= privilege.getApprovedStatusID());

                    cityList.add(businessDivisionEntity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void titleSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        title = titleList.get(rowIndex);
    }

    public SelectItem findTitleItemByTitleCode(String itemCode) {
        SelectItem item = null;

        for (SelectItem title : getTitleItemList()) {
            if (title.getValue().toString().equals(itemCode)) {
                item = title;
                break;
            }
        }

        return item;
    }

    public void countrySelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
            CountryEntity country = countryList.get(rowIndex);
            long countryID = country.getCountryID();
            selectCountry(countryID);
        }
    }

    public void countryMenuSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            long countryID = Integer.parseInt(vce.getNewValue().toString());
            selectCountry(countryID);
        }
    }

    public void countrySelected2(ValueChangeEvent vce) {
        long countryID = Integer.parseInt(vce.getNewValue().toString());
        selectCountry2(countryID);
    }

    public void countrySelected3(ValueChangeEvent vce) {
        long countryID = Integer.parseInt(vce.getNewValue().toString());
        selectCountry3(countryID);
    }

    public void selectCountry(long countryID) {
        country = new CountryEntity();
        
        if (countryID != -1) {
            country = findCountryByID(countryID);
        }
        
        createCountryStateItemListByCountryID(countryID);
    }

    public void selectCountry2(long countryID) {
        setCountry(new CountryEntity());
        if (countryID != -1) {
            setCountry(findCountryByID(countryID));
        }
        createCountryStateItemListByCountryID2(countryID);
    }

    public void selectCountry3(long countryID) {
        setCountry(new CountryEntity());
        if (countryID != -1) {
            setCountry(findCountryByID(countryID));
        }
        createCountryStateItemListByCountryID3(countryID);
    }

    public void stateSelected(ValueChangeEvent vce) {
        int rowIndex = 0;
        String stateCode = null;

        if (vce.getComponent() instanceof HtmlSelectBooleanCheckbox) {
            rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
            state = stateList.get(rowIndex);
            stateCode = state.getStateCode();
        } else if (vce.getComponent() instanceof HtmlSelectOneMenu) {
            if (vce.getNewValue() != null) {
                stateCode = vce.getNewValue().toString();
            }
        }

        if (stateCode != null) {
            stateSelected(stateCode);
        }
    }

    public void stateMenuSelected(ValueChangeEvent vce) {
        String stateCode = vce.getNewValue().toString();
        stateSelected(stateCode);
    }

    public void stateSelected3(ValueChangeEvent vce) {
        String stateCode = vce.getNewValue().toString();
        stateSelected3(stateCode);
    }

    public void stateSelected(String stateCode) {
        createStateCityItemListByStateCode(stateCode);
    }

    public void stateSelected2(String stateCode) {
        createStateCityItemListByStateCode2(stateCode);
    }

    public void stateSelected3(String stateCode) {
        createStateCityItemListByStateCode3(stateCode);
    }

    public void cityMenuSelected(ValueChangeEvent vce) {
        long cityID = Long.parseLong(vce.getNewValue().toString());
    }

    public void citySelected(ValueChangeEvent vce) {
        Long cityID = null;

        if (vce.getComponent() instanceof HtmlSelectBooleanCheckbox) {
            int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
            CityEntity city = cityList.get(rowIndex);
            cityID = city.getCityID();
        } else if (vce.getComponent() instanceof HtmlSelectOneMenu) {
            if (vce.getNewValue() != null) {
                cityID = Long.parseLong(vce.getNewValue().toString());
            }
        }

        if (cityID != null) {
            setCity(findCityByID(cityID));
        }
    }

    public void countrySelectedForStateCreate(ValueChangeEvent vce) {
        int countryID = Integer.parseInt(vce.getNewValue().toString());
        CountryEntity ctry = new CountryEntity();
        if (countryID != -1) {
            ctry = findCountryByID(countryID);
        }
        getState().setCountryDesc(ctry.getCountryDesc());
    }

    public void countrySelectedForCityCreate(ValueChangeEvent vce) {
        int countryID = Integer.parseInt(vce.getNewValue().toString());
        CountryEntity ctry = new CountryEntity();

        if (countryID != -1) {
            ctry = findCountryByID(countryID);
            reloadStateList();
        }

        getCity().setCountryDesc(ctry.getCountryDesc());
        getCity().setCountryID(countryID);

        createCountryStateItemListByCountryID(countryID);
    }

    public void stateSelectedForCityCreate(ValueChangeEvent vce) {
        String stateCode = vce.getNewValue().toString();
        StateEntity state = new StateEntity();
        if (!stateCode.equals("NA")) {
            state = findStateByCode(stateCode, getCity().getCountryID());
        }
        getCity().setStateCode(state.getStateCode());
        getCity().setStateDesc(state.getStateDesc());
    }

    public void createCountryStateItemListByCountryID(long countryID) {
        selectedStateItemList = new ArrayList<SelectItem>();
        boolean matched = false;

        if (getStateList().size() == 0) {
            findCountryStateAndCityList(countryID);

            if (getStateList() != null && getStateList().size() > 0) {
                for (StateEntity st : getStateList()) {
                    if (st.getCountryID() == countryID) {
                        addToSelectItemList(st, selectedStateItemList2);
                    }
                }
            }
        }

        for (StateEntity st : getStateList()) {
            if (st.getCountryID() == countryID) {
                addToSelectItemList(st, selectedStateItemList);
                matched = true;
            }
        }
    }

    public void createCountryStateItemListByCountryID2(long countryID) {
        selectedStateItemList2 = new ArrayList<SelectItem>();

        if (getStateList().size() == 0) {
            findCountryStateAndCityList(countryID);

            if (getStateList() != null && getStateList().size() > 0) {
                for (StateEntity st : getStateList()) {
                    if (st.getCountryID() == countryID) {
                        addToSelectItemList(st, selectedStateItemList2);
                    }
                }
            }
        }

        for (StateEntity st : getStateList()) {
            if (st.getCountryID() == countryID) {
                addToSelectItemList(st, selectedStateItemList2);
            }
        }
    }

    public void createCountryStateItemListByCountryID3(long countryID) {
        selectedStateItemList3 = new ArrayList<SelectItem>();

        for (StateEntity st : getStateList()) {
            if (st.getCountryID() == countryID) {
                addToSelectItemList(st, selectedStateItemList3);
            }
        }

        /*getStateList() could be emptied from a previous search temporary solution?...*/
        if (selectedStateItemList3.size() < 1) {
            if (countryID > 0) {
                findCountryStateAndCityList(countryID);
                if (getStateList() != null && getStateList().size() > 0) {
                    createCountryStateItemListByCountryID3(countryID);
                    if (selectedStateItemList3.size() < 1 && countryID != Integer.parseInt(NIGERIA_COUNTRY_ID_DEFAULT)) {
                        //do nothing.....
                    }
                }
            }
        }

    }

    private void findCountryStateAndCityList(final long countryID) {
        state = new StateEntity();
        state.setCountryID(countryID);
        findStateByCriteria();

        city = new CityEntity();
        city.setCountryID(countryID);
        findCityByCriteria();
    }

    public void createCountryCityItemListByCountryID(long countryID) {
        selectedCityItemList = new ArrayList<SelectItem>();
        for (CityEntity city : getCityList()) {
            if (city.getCountryID() == countryID) {
                addToSelectItemList(city, selectedCityItemList);
            }
        }
    }

    public void createStateCityItemListByStateCode(String stateCode) {
        selectedCityItemList = new ArrayList<SelectItem>();
        for (CityEntity city : getCityList()) {
            if (city.getStateCode().equals(stateCode)) {
                addToSelectItemList(city, selectedCityItemList);
            }
        }
    }

    public void createStateCityItemListByStateCode2(String stateCode) {
        selectedCityItemList2 = new ArrayList<SelectItem>();
        for (CityEntity city : getCityList()) {
            if (city.getStateCode().equals(stateCode)) {
                addToSelectItemList(city, selectedCityItemList2);
            }
        }
    }

    public void createStateCityItemListByStateCode3(String stateCode) {
        selectedCityItemList3 = new ArrayList<SelectItem>();
        for (CityEntity city : getCityList()) {
            if (city.getStateCode().equals(stateCode)) {
                addToSelectItemList(city, selectedCityItemList3);
            }
        }
    }

    public String addCountryToList() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (getCountry().getCountryDesc() == null || getCountry().getCountryDesc().trim().isEmpty()) {
            applicationMessageBean.insertMessage("Specify Country", MessageType.ERROR_MESSAGE);
            return "";
        }
        if (getCountry() != null && !getCountry().getCountryDesc().trim().equals("")) {
            getCountryList().add(getCountry());
        }
        setCountry(new CountryEntity());

        return "";
    }

    public String addStateToList() {

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (getState().getStateDesc() == null || getState().getStateDesc().trim().isEmpty()
                || getState().getStateCode() == null || getState().getStateCode().trim().isEmpty()
                || getState().getCountryID() < 0) {
            applicationMessageBean.insertMessage("Specify Country, State Name and State Code", MessageType.ERROR_MESSAGE);
            return "";
        }

        if (getState().getStateCode() != null && !getState().getStateCode().trim().isEmpty()
                && getState().getStateCode().trim().length() > 3) {
            applicationMessageBean.insertMessage("State Code cannot be more than 3 characters", MessageType.ERROR_MESSAGE);
            return "";
        }
        if ((!getState().getStateDesc().trim().equals(""))
                && ((!getState().getStateCode().trim().equals("")))
                && ((getState().getCountryID() > 0))) {
            getStateList().add(getState());
            setState(new StateEntity());
        }

        return "";
    }

    public String addCityToList() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (getCity().getCityDesc() == null || getCity().getCityDesc().trim().isEmpty()
                || getCity().getStateCode() == null || getCity().getStateCode().trim().isEmpty() || getCity().getStateCode().trim().equals("NA")
                || getCity().getCountryID() < 0) {
            applicationMessageBean.insertMessage("Specify Country, State and City", MessageType.ERROR_MESSAGE);
            return "";
        }

        if ((!getCity().getCityDesc().trim().equals(""))
                && ((!getCity().getStateCode().trim().equals("")))
                && ((getCity().getCountryID() > 0))) {
            getCityList().add(getCity());
            setCity(new CityEntity());
        }

        return "";
    }

    public CountryEntity findCountryByID(long countryID) {
        CountryEntity country = null;

        if (countryID > 0) {
            for (CountryEntity c : getCountryList()) {

                if (c.getCountryID() == countryID) {
                    country = c;
                    break;
                }
            }
        }

        return country;
    }

    public CountryEntity findCountryByName(String countryName) {
        CountryEntity country = null;

        for (CountryEntity c : getCountryList()) {
            if (c.getCountryDesc().equalsIgnoreCase(countryName)) {
                country = c;
                break;
            }
        }

        return country;
    }

    public CityEntity findCityByID(long cityID) {
        CityEntity city = null;

        if (cityID > 0) {
            for (CityEntity c : getCityList()) {
                if (c.getCityID() == cityID) {
                    city = c;
                    break;
                }
            }
        }

        return city;
    }

    public CityEntity findCityByDesc(String cityDesc) {
        CityEntity city = null;

        for (CityEntity c : getCityList()) {
            if (c.getCityDesc().equalsIgnoreCase(cityDesc)) {
                city = c;
                break;
            }
        }

        return city;
    }

    public StateEntity findNigerianStateByCode(String stateCode) {
        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        StateEntity state = null;
        try {
            long countryID = Long.parseLong(appPropBean.getValue(NIGERIA_COUNTRY_ID_KEY,
                    NIGERIA_COUNTRY_ID_DEFAULT, false));
            state = findStateByCode(stateCode, countryID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return state;
    }

    public List<SelectItem> findNigerianStateItemListByCode(String stateCode) {
        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        List<SelectItem> stateItemList = new ArrayList<SelectItem>();
        try {
            long countryID = Long.parseLong(appPropBean.getValue(NIGERIA_COUNTRY_ID_KEY,
                    NIGERIA_COUNTRY_ID_DEFAULT, false));

            for (StateEntity st : getStateList()) {
                if ((st.getStateCode().equals(stateCode)) && (st.getCountryID() == countryID)) {
                    SelectItem item = new SelectItem();
                    item.setValue(st.getStateCode());
                    item.setLabel(st.getStateDesc());

                    stateItemList.add(item);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return stateItemList;
    }

    public StateEntity findStateByCode(String stateCode, long countryID) {
        StateEntity state = null;

        if (stateCode != null && !stateCode.equals("NA") && !stateCode.equals("-1") && !stateCode.isEmpty()) {
            for (StateEntity st : stateList) {
                if ((st.getStateCode().equals(stateCode)) && (st.getCountryID() == countryID)) {
                    state = st;
                    break;
                }
            }
        }

        return state;
    }

    public StateEntity findStateByName(String stateName, long countryID) {
        StateEntity state = null;

        for (StateEntity st : getStateList()) {
            if ((st.getStateDesc().equalsIgnoreCase(stateName)) && (st.getCountryID() == countryID)) {
                state = st;
                break;
            }
        }

        return state;
    }

    public String clearCountryList() {
        countryList = new ArrayList<CountryEntity>();
        return "";
    }

    public String clearStateList() {
        stateList = new ArrayList<StateEntity>();
        return "";
    }

    public String clearCityList() {
        cityList = new ArrayList<CityEntity>();
        return "";
    }

    public String saveCountryList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

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

            for (CountryEntity c : getCountryList()) {
                if (!c.getCountryDesc().trim().equals("")) {
                    if (c.getCountryID() == 0) {
                        long countryID = Long.parseLong(appPropBean.getValue(COUNTRY_ID_KEY,
                                COUNTRY_ID_DEFAULT, true));
                        c.setCountryID(countryID);

                        dataServer.saveData(c);
                    } else {
                        dataServer.updateData(c);
                    }
                }
            }
            reloadCountryList();
            applicationMessageBean.insertMessage("Country has been saved", MessageType.SUCCESS_MESSAGE);
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

    public String activateCountry() {
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.COUNTRIES_MENU_ITEM));

        if (country.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((country.getApprovalStatusID() == 0) || (country.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(country.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        country.setApprovalStatusID(country.getApprovalStatusID() + 1);
                        country.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(country);
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
                } else if (country.getApprovalStatusID()
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

    public String deleteCountry() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);
        try {
            if (countryList.contains(country)) {

                if (country.getCountryID() > 0) {
                    if (country.isActivated()) {
                        applicationMessageBean.insertMessage("This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                        return "";
                    }
                    FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                    CountryEntity criteria = new CountryEntity();
                    criteria.setCountryID(country.getCountryID());
                    dataServer.beginTransaction();
                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();

                    for (SelectItem item : countryItemList) {
                        if (Integer.parseInt(item.getValue().toString()) == country.getCountryID()) {
                            countryItemList.remove(item);
                            break;
                        }
                    }
                }

                countryList.remove(country);
                country = new CountryEntity();
                applicationMessageBean.insertMessage("Country has been deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String saveStateList() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

        dataServer.beginTransaction();
        try {
            for (StateEntity s : getStateList()) {
                StateEntity criteria = new StateEntity();
                criteria.setStateCode(s.getStateCode());
                List<AbstractEntity> baseStateList = dataServer.findData(criteria);
                if ((baseStateList != null) && (baseStateList.size() > 0)) {
                    dataServer.updateData(s);
                } else {
                    dataServer.saveData(s);
                }
            }
            reloadStateList();
            applicationMessageBean.insertMessage("State has been saved", MessageType.SUCCESS_MESSAGE);
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

    public String activateState() {
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.STATES_MENU_ITEM));

        if (state.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((state.getApprovalStatusID() == 0) || (state.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(state.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        state.setApprovalStatusID(state.getApprovalStatusID() + 1);
                        state.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(state);
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
                } else if (state.getApprovalStatusID()
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

    public String deleteState() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (stateList.contains(state)) {

                if (!state.getStateCode().trim().isEmpty()) {
                    if (state.isActivated()) {
                        applicationMessageBean.insertMessage("This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                        return "";
                    }
                    FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                    StateEntity criteria = new StateEntity();
                    criteria.setStateCode(state.getStateCode());
                    dataServer.beginTransaction();
                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();

                    for (SelectItem item : nigeriaStateItemList) {
                        if (item.getValue().toString().equals(state.getStateCode())) {
                            nigeriaStateItemList.remove(item);
                            break;
                        }
                    }
                }

                stateList.remove(state);
                state = new StateEntity();
                applicationMessageBean.insertMessage("State has been deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String saveCityList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

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
            for (CityEntity cty : getCityList()) {
                if (!cty.getCityDesc().trim().equals("")) {
                    if (cty.getCityID() == 0) {
                        long ctyID = Long.parseLong(appPropBean.getValue(CITY_ID_KEY,
                                CITY_ID_DEFAULT, true));
                        cty.setCityID(ctyID);

                        dataServer.saveData(cty);
                    } else {
                        dataServer.updateData(cty);
                    }
                }
            }
            reloadCityList();
            applicationMessageBean.insertMessage("City has been saved", MessageType.SUCCESS_MESSAGE);
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

    public String activateCity() {
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.CITIES_MENU_ITEM));

        if (city.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((city.getApprovalStatusID() == 0) || (city.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(city.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        city.setApprovalStatusID(city.getApprovalStatusID() + 1);
                        city.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(city);
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
                } else if (city.getApprovalStatusID()
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

    public String deleteCity() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (cityList.contains(city)) {

                if (city.getCityID() > 0) {
                    if (city.isActivated()) {
                        applicationMessageBean.insertMessage("This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                        return "";
                    }
                    FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                    CityEntity criteria = new CityEntity();
                    criteria.setCityID(city.getCityID());
                    dataServer.beginTransaction();
                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();

                    for (SelectItem item : selectedCityItemList) {
                        if (Integer.parseInt(item.getValue().toString()) == city.getCityID()) {
                            selectedCityItemList.remove(item);
                            break;
                        }
                    }
                }

                cityList.remove(city);
                city = new CityEntity();
                applicationMessageBean.insertMessage("City has been deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String clearCache() {
        country = new CountryEntity();
        state = new StateEntity();
        city = new CityEntity();
        title = new TitleEntity();

        return "";
    }

    public String loadTitlePreviousHistoricalState() {
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

        if (titleList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean
                    .loadPreviousHistoricalState(titleList.get(0).getActionTrail());
            if (batEntity != null) {
                loadTitles(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadTitles(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        }

        return "";
    }

    public String loadTitleNextHistoricalState() {
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

        if (titleList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(titleList.get(0).getActionTrail());
            if (batEntity != null) {
                loadTitles(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadTitles(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        }

        return "";
    }

    public String gotoTitleAuditTrailPage() {
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.TITLE);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(title.getTitleID());
        businessActionTrailBean.loadBusinessActionTrailList();

        if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
            businessActionTrailBean.setBusinessActionTrail(
                    businessActionTrailBean.getBusinessActionTrailList().get(
                            businessActionTrailBean.getBusinessActionTrailList().size() - 1));

            loadTitles((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            outcome = "titleaudittrail.jsf";
        } else {
            applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    public String loadStatePreviousHistoricalState() {
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

        if (stateList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    stateList.get(0).getActionTrail());
            if (batEntity != null) {
                loadStateList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadStateList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        }

        return "";
    }

    public String loadStateNextHistoricalState() {
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

        if (stateList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(stateList.get(0).getActionTrail());
            if (batEntity != null) {
                loadStateList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadStateList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        }

        return "";
    }

    public String gotoStateAuditTrailPage() {
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.STATE);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(state.getStateID());
        businessActionTrailBean.loadBusinessActionTrailList();

        if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
            businessActionTrailBean.setBusinessActionTrail(
                    businessActionTrailBean.getBusinessActionTrailList().get(
                            businessActionTrailBean.getBusinessActionTrailList().size() - 1));

            loadStateList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            outcome = "stateaudittrail.jsf";
        } else {
            applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    public String loadCountryPreviousHistoricalState() {
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

        if (countryList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    countryList.get(0).getActionTrail());
            if (batEntity != null) {
                loadCountryList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadCountryList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        }

        return "";
    }

    public String loadCountryNextHistoricalState() {
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

        if (countryList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean
                    .loadNextHistoricalState(countryList.get(0).getActionTrail());
            if (batEntity != null) {
                loadCountryList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadCountryList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        }

        return "";
    }

    public String gotoCountryAuditTrailPage() {
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.COUNTRY);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(country.getCountryID());
        businessActionTrailBean.loadBusinessActionTrailList();

        if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
            businessActionTrailBean.setBusinessActionTrail(
                    businessActionTrailBean.getBusinessActionTrailList().get(
                            businessActionTrailBean.getBusinessActionTrailList().size() - 1));

            loadCountryList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            outcome = "countryaudittrail.jsf";
        } else {
            applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    public String loadCityPreviousHistoricalState() {
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

        if (cityList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    cityList.get(0).getActionTrail());
            if (batEntity != null) {
                loadCityList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadCityList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        }

        return "";
    }

    public String loadCityNextHistoricalState() {
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

        if (cityList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(cityList.get(0).getActionTrail());
            if (batEntity != null) {
                loadCityList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadCityList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        }

        return "";
    }

    public String gotoCityAuditTrailPage() {
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.CITY);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(city.getCityID());
        businessActionTrailBean.loadBusinessActionTrailList();

        if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
            businessActionTrailBean.setBusinessActionTrail(
                    businessActionTrailBean.getBusinessActionTrailList().get(
                            businessActionTrailBean.getBusinessActionTrailList().size() - 1));

            loadCityList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            outcome = "cityaudittrail.jsf";
        } else {
            applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    /**
     * @return the countryList
     */
    public List<CountryEntity> getCountryList() {
        return countryList;
    }

    /**
     * @param countryList the countryList to set
     */
    public void setCountryList(List<CountryEntity> countryList) {
        this.setCountryList(countryList);
    }

    /**
     * @return the countryItemList
     */
    public List<SelectItem> getCountryItemList() {
        return countryItemList;
    }

    /**
     * @param countryItemList the countryItemList to set
     */
    public void setCountryItemList(List<SelectItem> countryItemList) {
        this.setCountryItemList(countryItemList);
    }

    /**
     * @return the selectedStateItemList
     */
    public List<SelectItem> getSelectedStateItemList() {
        return selectedStateItemList;
    }

    /**
     * @param selectedStateItemList the selectedStateItemList to set
     */
    public void setSelectedStateItemList(List<SelectItem> selectedStateItemList) {
        this.setSelectedStateItemList(selectedStateItemList);
    }

    /**
     * @return the selectedCityItemList
     */
    public List<SelectItem> getSelectedCityItemList() {
        return selectedCityItemList;
    }

    /**
     * @param selectedCityItemList the selectedCityItemList to set
     */
    public void setSelectedCityItemList(List<SelectItem> selectedCityItemList) {
        this.setSelectedCityItemList(selectedCityItemList);
    }

    /**
     * @return the country
     */
    public CountryEntity getCountry() {
        if (country == null) {
            country = new CountryEntity();
        }
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    /**
     * @return the state
     */
    public StateEntity getState() {
        if (state == null) {
            state = new StateEntity();
        }
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(StateEntity state) {
        this.state = state;
    }

    /**
     * @return the city
     */
    public CityEntity getCity() {
        if (city == null) {
            city = new CityEntity();
        }
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(CityEntity city) {
        this.city = city;
    }

    public List<StateEntity> getStateList() {
        return stateList;
    }

    public void setStateList(List<StateEntity> stateList) {
        this.stateList = stateList;
    }

    public List<CityEntity> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityEntity> cityList) {
        this.cityList = cityList;
    }

    /**
     * @return the titleItemList
     */
    public List<SelectItem> getTitleItemList() {
        return titleItemList;
    }

    /**
     * @param titleItemList the titleItemList to set
     */
    public void setTitleItemList(List<SelectItem> titleItemList) {
        this.titleItemList = titleItemList;
    }

    /**
     * @return the selectedStateItemList2
     */
    public List<SelectItem> getSelectedStateItemList2() {
        return selectedStateItemList2;
    }

    /**
     * @param selectedStateItemList2 the selectedStateItemList2 to set
     */
    public void setSelectedStateItemList2(List<SelectItem> selectedStateItemList2) {
        this.selectedStateItemList2 = selectedStateItemList2;
    }

    /**
     * @return the selectedCityItemList2
     */
    public List<SelectItem> getSelectedCityItemList2() {
        return selectedCityItemList2;
    }

    /**
     * @param selectedCityItemList2 the selectedCityItemList2 to set
     */
    public void setSelectedCityItemList2(List<SelectItem> selectedCityItemList2) {
        this.selectedCityItemList2 = selectedCityItemList2;
    }

    /**
     * @return the nigeriaStateItemList
     */
    public List<SelectItem> getNigeriaStateItemList() {
        return nigeriaStateItemList;
    }

    /**
     * @param nigeriaStateItemList the nigeriaStateItemList to set
     */
    public void setNigeriaStateItemList(List<SelectItem> nigeriaStateItemList) {
        this.nigeriaStateItemList = nigeriaStateItemList;
    }

    /**
     * @return the selectedStateItemList3
     */
    public List<SelectItem> getSelectedStateItemList3() {
        return selectedStateItemList3;
    }

    /**
     * @param selectedStateItemList3 the selectedStateItemList3 to set
     */
    public void setSelectedStateItemList3(List<SelectItem> selectedStateItemList3) {
        this.selectedStateItemList3 = selectedStateItemList3;
    }

    /**
     * @return the selectedCityItemList3
     */
    public List<SelectItem> getSelectedCityItemList3() {
        return selectedCityItemList3;
    }

    /**
     * @param selectedCityItemList3 the selectedCityItemList3 to set
     */
    public void setSelectedCityItemList3(List<SelectItem> selectedCityItemList3) {
        this.selectedCityItemList3 = selectedCityItemList3;
    }

    /**
     * @return the nigeriaID
     */
    public long getNigeriaID() {
        return nigeriaID;
    }

    /**
     * @param nigeriaID the nigeriaID to set
     */
    public void setNigeriaID(long nigeriaID) {
        this.nigeriaID = nigeriaID;
    }

    /**
     * @return the title
     */
    public TitleEntity getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(TitleEntity title) {
        this.title = title;
    }

    /**
     * @return the titleList
     */
    public List<TitleEntity> getTitleList() {
        return titleList;
    }

    /**
     * @param titleList the titleList to set
     */
    public void setTitleList(List<TitleEntity> titleList) {
        this.titleList = titleList;
    }

    /**
     * @return the searchCountryID
     */
    public long getSearchCountryID() {
        return searchCountryID;
    }

    /**
     * @param searchCountryID the searchCountryID to set
     */
    public void setSearchCountryID(long searchCountryID) {
        this.searchCountryID = searchCountryID;
    }
}
