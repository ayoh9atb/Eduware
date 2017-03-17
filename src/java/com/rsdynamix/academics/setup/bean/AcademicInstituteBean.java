/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.setup.bean;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.setup.beans.AddressManagerBean;
import com.rsdynamix.dynamo.setup.entities.CityEntity;
import com.rsdynamix.dynamo.setup.entities.CountryEntity;
import com.rsdynamix.dynamo.setup.entities.StateEntity;
import com.rsdynamix.hrms.cv.entities.AcademicInstitutionEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsd.projects.menus.ValueListBean;
//import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import com.rsdynamix.tns.util.Constants;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
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

/**
 *
 * @author root
 */
@SessionScoped
@ManagedBean(name = "academicInstituteBean")
public class AcademicInstituteBean {

    private static final String SCHOOL_ID_KEY = "institute_id";
    /**/
    private static final String SCHOOL_ID_DEFAULT = "1";
    /**/
    private AcademicInstitutionEntity institute;
    private List<AcademicInstitutionEntity> instituteList;
    private List<SelectItem> selectedInstituteItemList;

    public AcademicInstituteBean() {
        institute = new AcademicInstitutionEntity();
        instituteList = new ArrayList<AcademicInstitutionEntity>();
        selectedInstituteItemList = new ArrayList<SelectItem>();

        loadInstitute();
    }

    public String addInstitute() {
        if (!getInstitute().getSchoolName().trim().equals("")) {
            AddressManagerBean addressBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.addressManagerBean}", AddressManagerBean.class);
            if (addressBean == null) {
                addressBean = new AddressManagerBean();
                CommonBean.setBeanToContext(
                        "#{sessionScope.addressManagerBean}", AddressManagerBean.class, addressBean);
            }

            CountryEntity country = addressBean.findCountryByID(getInstitute().getCountryID());
            getInstitute().setCountry(country.getCountryDesc());

            StateEntity state = addressBean.findStateByCode(getInstitute().getStateCode(), getInstitute().getCountryID());
            getInstitute().setStateDesc(state.getStateDesc());

            CityEntity city = addressBean.findCityByID(getInstitute().getCityID());
            getInstitute().setCityDesc(city.getCityDesc());

            getInstituteList().add(getInstitute());
            setInstitute(new AcademicInstitutionEntity());
        }

        return "";
    }

    public String saveInstituteList() {
        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
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

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        dataServer.beginTransaction();
        try {
            for (AcademicInstitutionEntity school : getInstituteList()) {
                if (school.getSchoolID() == 0) {
                    int cmID = Integer.parseInt(appPropBean.getValue(SCHOOL_ID_KEY,
                            SCHOOL_ID_DEFAULT, true));
                    school.setSchoolID(cmID);

                    dataServer.setSelectedTnsName(Constants.FINULTIMATE_PU);
                    dataServer.saveData(school);
                } else if (school.getSchoolID() > 0) {
                    dataServer.updateData(school);
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.CR8_CLIENT_MENU_ITEM));

        if (institute.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((institute.getApprovalStatusID() == 0) || (institute.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(institute.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        institute.setApprovalStatusID(institute.getApprovalStatusID() + 1);
                        institute.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(institute);
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
                } else if (institute.getApprovalStatusID()
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

    public String loadInstitute(BusinessActionTrailEntity businessActionTrail) throws Exception {
        setInstituteList(new ArrayList<AcademicInstitutionEntity>());
        setSelectedInstituteItemList(new ArrayList<SelectItem>());

        ValueListBean valueListBean = (ValueListBean) CommonBean.getBeanFromContext(
                "#{sessionScope.valueListBean}", ValueListBean.class);
        if (valueListBean == null) {
            valueListBean = new ValueListBean();
            CommonBean.setBeanToContext("#{sessionScope.valueListBean}", ValueListBean.class, valueListBean);
        }

        AddressManagerBean addressBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.addressManagerBean}", AddressManagerBean.class);
        if (addressBean == null) {
            addressBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}", AddressManagerBean.class, addressBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        AcademicInstitutionEntity criteria = new AcademicInstitutionEntity();
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        List<AbstractEntity> baseInstituteList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity baseInstitute : baseInstituteList) {
            AcademicInstitutionEntity acInst = (AcademicInstitutionEntity) baseInstitute;
            acInst.initializeTransients();

            CountryEntity country = addressBean.findCountryByID(acInst.getCountryID());
            acInst.setCountry(country.getCountryDesc());

            StateEntity state = addressBean.findStateByCode(acInst.getStateCode(), acInst.getCountryID());
            if (state != null) {
                acInst.setStateDesc(state.getStateDesc());
            }

            CityEntity city = addressBean.findCityByID(acInst.getCityID());
            if (city != null) {
                acInst.setCityDesc(city.getCityDesc());
            }

            getInstituteList().add(acInst);

            Collections.sort(getInstituteList(), new Comparator<AcademicInstitutionEntity>() {

                public int compare(AcademicInstitutionEntity o1, AcademicInstitutionEntity o2) {
                    return o1.getSchoolName().compareTo(o2.getSchoolName());
                }
            });
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

        try {
            if (instituteList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        instituteList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadInstitute(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadInstitute(batEntity);
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
            if (instituteList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(instituteList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadInstitute(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadInstitute(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.ACADEMIC_INSTITUTION);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(institute.getSchoolID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadInstitute((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "instituteaudittrail.jsf";
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

    public String deleteInstitute() {
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
            if (instituteList.contains(institute)) {
                if (institute.getSchoolID() > 0) {
                    AcademicInstitutionEntity criteria = new AcademicInstitutionEntity();
                    criteria.setSchoolID(institute.getSchoolID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                instituteList.remove(institute);
                institute = new AcademicInstitutionEntity();
            }
        } catch (SQLException ex) {
            dataServer.rollbackTransaction();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void instituteSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        setInstitute(getInstituteList().get(rowIndex));
    }

    public void instituteMenuSelected(ValueChangeEvent vce) {
        int schoolID = Integer.parseInt(vce.getNewValue().toString());
        setInstitute(findInstituteByID(schoolID));
    }

    public String loadInstitute() {
        setInstituteList(new ArrayList<AcademicInstitutionEntity>());
        setSelectedInstituteItemList(new ArrayList<SelectItem>());

        ValueListBean valueListBean = (ValueListBean) CommonBean.getBeanFromContext(
                "#{sessionScope.valueListBean}", ValueListBean.class);
        if (valueListBean == null) {
            valueListBean = new ValueListBean();
            CommonBean.setBeanToContext("#{sessionScope.valueListBean}", ValueListBean.class, valueListBean);
        }

        AddressManagerBean addressBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.addressManagerBean}", AddressManagerBean.class);
        if (addressBean == null) {
            addressBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.addressManagerBean}", AddressManagerBean.class, addressBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        AcademicInstitutionEntity criteria = new AcademicInstitutionEntity();
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        try {
            List<AbstractEntity> baseInstituteList = dataServer.findData(criteria);
            for (AbstractEntity baseInstitute : baseInstituteList) {
                AcademicInstitutionEntity acInst = (AcademicInstitutionEntity) baseInstitute;
                acInst.initializeTransients();

                CountryEntity country = addressBean.findCountryByID(acInst.getCountryID());
                acInst.setCountry(country.getCountryDesc());

                StateEntity state = addressBean.findStateByCode(acInst.getStateCode(), acInst.getCountryID());
                if (state != null) {
                    acInst.setStateDesc(state.getStateDesc());
                }

                CityEntity city = addressBean.findCityByID(acInst.getCityID());
                if (city != null) {
                    acInst.setCityDesc(city.getCityDesc());
                }

                getInstituteList().add(acInst);

                SelectItem item = new SelectItem();
                item.setLabel(acInst.getSchoolName());
                item.setValue(acInst.getSchoolID());
                getSelectedInstituteItemList().add(item);

                Collections.sort(getSelectedInstituteItemList(), new Comparator<SelectItem>() {

                    public int compare(SelectItem o1, SelectItem o2) {
                        return o1.getLabel().compareTo(o2.getLabel());
                    }
                });

                Collections.sort(getInstituteList(), new Comparator<AcademicInstitutionEntity>() {

                    public int compare(AcademicInstitutionEntity o1, AcademicInstitutionEntity o2) {
                        return o1.getSchoolName().compareTo(o2.getSchoolName());
                    }
                });
            }

            if ((getSelectedInstituteItemList() != null) && (getSelectedInstituteItemList().size() > 0)) {
                valueListBean.getValueListMap().put(getSelectedInstituteItemList().getClass().getName(),
                        getSelectedInstituteItemList());

                SelectItem item = new SelectItem();
                item.setLabel("Academic Institute Menu");
                item.setValue(getSelectedInstituteItemList().getClass().getName());
                valueListBean.getValueCategoryTypeItemList().add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void addToSelectItemList(AcademicInstitutionEntity entity) {
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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.DISCNT_SETP_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getSchoolID());
                item.setLabel(entity.getSchoolName());
                selectedInstituteItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getSchoolID());
            item.setLabel(entity.getSchoolName());
            selectedInstituteItemList.add(item);
        }
    }

    public AcademicInstitutionEntity findInstituteByID(long schoolID) {
        AcademicInstitutionEntity school = null;

        for (AcademicInstitutionEntity sch : getInstituteList()) {
            if (sch.getSchoolID() == schoolID) {
                school = sch;
                break;
            }
        }

        return school;
    }

    public SelectItem findInstituteItemByID(long zoneID) {
        SelectItem zoneItem = null;

        for (SelectItem acInstItem : getSelectedInstituteItemList()) {
            if (Long.parseLong(acInstItem.getValue().toString()) == zoneID) {
                zoneItem = acInstItem;
                break;
            }
        }

        return zoneItem;
    }

    /**
     * @return the institute
     */
    public AcademicInstitutionEntity getInstitute() {
        return institute;
    }

    /**
     * @param institute the institute to set
     */
    public void setInstitute(AcademicInstitutionEntity institute) {
        this.institute = institute;
    }

    /**
     * @return the instituteList
     */
    public List<AcademicInstitutionEntity> getInstituteList() {
        return instituteList;
    }

    /**
     * @param instituteList the instituteList to set
     */
    public void setInstituteList(List<AcademicInstitutionEntity> instituteList) {
        this.instituteList = instituteList;
    }

    /**
     * @return the selectedInstituteItemList
     */
    public List<SelectItem> getSelectedInstituteItemList() {
        return selectedInstituteItemList;
    }

    /**
     * @param selectedInstituteItemList the selectedInstituteItemList to set
     */
    public void setSelectedInstituteItemList(List<SelectItem> selectedInstituteItemList) {
        this.selectedInstituteItemList = selectedInstituteItemList;
    }
}
