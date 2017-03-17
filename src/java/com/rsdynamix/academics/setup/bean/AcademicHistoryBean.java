package com.rsdynamix.academics.setup.bean;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.setup.entities.AcademicHistoryEntity;
import com.rsdynamix.academics.setup.entities.SchoolTierTypeEntity;
import com.rsdynamix.dynamo.common.setup.beans.AddressManagerBean;
import com.rsdynamix.dynamo.setup.entities.CityEntity;
import com.rsdynamix.dynamo.setup.entities.CountryEntity;
import com.rsdynamix.dynamo.setup.entities.StateEntity;
import com.rsdynamix.hrms.cv.entities.AcademicInstitutionEntity;
import com.rsdynamix.hrms.cv.entities.AcademicQualificationEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.codrellica.projects.commons.DateUtil;
import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.hrms.commons.setup.beans.AcademicacademicInstituteBean;
import com.rsdynamix.hrms.commons.setup.beans.AcademicQualificationBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;

/**
 *
 * @author patushie@codrellica
 */
@ApplicationScoped
@ManagedBean(name = "academicHistoryBean")
public class AcademicHistoryBean {

    private static final String ACADA_HISTORY_ID_KEY = "acada_histri_id";
    //
    private static final String ACADA_HISTORY_ID_DEFAULT = "1";
    //
    private AcademicHistoryEntity academicHistory;
    private List<AcademicHistoryEntity> academicHistoryList;
    private List<SelectItem> academicHistoryItemList;

    public AcademicHistoryBean() {
        academicHistory = new AcademicHistoryEntity();
        academicHistoryList = new ArrayList<AcademicHistoryEntity>();
        academicHistoryItemList = new ArrayList<SelectItem>();
    }

    public String searchAcademicHistoryList() {
        if (!getAcademicHistory().getStudentNumber().trim().equals("")) {
            FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

            AcademicQualificationBean academicQualificationBean = (AcademicQualificationBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.academicQualificationBean}", AcademicQualificationBean.class);
            if (academicQualificationBean == null) {
                academicQualificationBean = new AcademicQualificationBean();
                CommonBean.setBeanToContext("#{applicationScope.academicQualificationBean}", AcademicQualificationBean.class, academicQualificationBean);
            }

            AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
            if (addressManagerBean == null) {
                addressManagerBean = new AddressManagerBean();
                CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
            }

            SchoolTierTypeBean schoolTierTypeBean = (SchoolTierTypeBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.schoolTierTypeBean}", SchoolTierTypeBean.class);
            if (schoolTierTypeBean == null) {
                schoolTierTypeBean = new SchoolTierTypeBean();
                CommonBean.setBeanToContext("#{applicationScope.schoolTierTypeBean}", SchoolTierTypeBean.class, schoolTierTypeBean);
            }

            AcademicacademicInstituteBean instBean = (AcademicacademicInstituteBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.academicInstituteBean}", AcademicacademicInstituteBean.class);
            if (instBean != null) {
                instBean = new AcademicacademicInstituteBean();
                CommonBean.setBeanToContext(
                        "#{applicationScope.academicInstituteBean}", AcademicacademicInstituteBean.class, instBean);
            }

            ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
            if (applicationMessageBean == null) {
                applicationMessageBean = new ApplicationMessageBean();
                CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                        ApplicationMessageBean.class, applicationMessageBean);
            }
            applicationMessageBean.insertMessage("", MessageType.NONE);

            AcademicHistoryEntity criteria = new AcademicHistoryEntity();
            criteria.setStudentNumber(getAcademicHistory().getStudentNumber());

            try {
                List<AbstractEntity> abstractThreadList = dataServer.findData(criteria);
                for (AbstractEntity entity : abstractThreadList) {
                    AcademicHistoryEntity threadEntity = (AcademicHistoryEntity) entity;

                    AcademicQualificationEntity qualification = academicQualificationBean.findQualificationByID(threadEntity.getCertificateID());
                    if (qualification != null) {
                        threadEntity.setCertificateDesc(qualification.getQualificationName());
                    }

                    CountryEntity country = addressManagerBean.findCountryByID(threadEntity.getSchoolCountryID());
                    if (country != null) {
                        threadEntity.setSchoolCountryDesc(country.getCountryDesc());
                    }

                    StateEntity state = addressManagerBean.findStateByCode(threadEntity.getStateCode(), threadEntity.getSchoolCountryID());
                    if (state != null) {
                        threadEntity.setStateDesc(state.getStateDesc());
                    }

                    CityEntity city = addressManagerBean.findCityByID(threadEntity.getCityID());
                    if (city != null) {
                        threadEntity.setCityDesc(city.getCityDesc());
                    }

                    SchoolTierTypeEntity schoolTier = schoolTierTypeBean.findSchoolTierByID(threadEntity.getSchoolTierTypeID());
                    if (schoolTier != null) {
                        threadEntity.setSchoolTierTypeDesc(schoolTier.getSchoolTierName());
                    }

                    AcademicInstitutionEntity insitute = instBean.findInstituteByID(threadEntity.getSchoolID());
                    if (insitute != null) {
                        threadEntity.setSchoolName(insitute.getSchoolName());
                    }

                    threadEntity.setAdmissionDateTO(DateUtil.toUtilDate(threadEntity.getAdmissionDate()));
                    threadEntity.setCompletionDateTO(DateUtil.toUtilDate(threadEntity.getCompletionDate()));

                    academicHistoryList.add(threadEntity);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            }
        }

        return "";
    }

    public String addAcademicHistory() {
        if (!getAcademicHistoryList().contains(getAcademicHistory())) {
            AcademicQualificationBean academicQualificationBean = (AcademicQualificationBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.academicQualificationBean}", AcademicQualificationBean.class);
            if (academicQualificationBean == null) {
                academicQualificationBean = new AcademicQualificationBean();
                CommonBean.setBeanToContext("#{applicationScope.academicQualificationBean}", AcademicQualificationBean.class, academicQualificationBean);
            }

            AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
            if (addressManagerBean == null) {
                addressManagerBean = new AddressManagerBean();
                CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
            }

            SchoolTierTypeBean schoolTierTypeBean = (SchoolTierTypeBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.schoolTierTypeBean}", SchoolTierTypeBean.class);
            if (schoolTierTypeBean == null) {
                schoolTierTypeBean = new SchoolTierTypeBean();
                CommonBean.setBeanToContext("#{applicationScope.schoolTierTypeBean}", SchoolTierTypeBean.class, schoolTierTypeBean);
            }

            AcademicQualificationEntity qualification = academicQualificationBean.findQualificationByID(getAcademicHistory().getCertificateID());
            if (qualification != null) {
                getAcademicHistory().setCertificateDesc(qualification.getQualificationName());
            }

            CountryEntity country = addressManagerBean.findCountryByID(getAcademicHistory().getSchoolCountryID());
            if (country != null) {
                getAcademicHistory().setSchoolCountryDesc(country.getCountryDesc());
            }

            StateEntity state = addressManagerBean.findStateByCode(getAcademicHistory().getStateCode(), getAcademicHistory().getSchoolCountryID());
            if (state != null) {
                getAcademicHistory().setStateDesc(state.getStateDesc());
            }

            CityEntity city = addressManagerBean.findCityByID(getAcademicHistory().getCityID());
            if (city != null) {
                getAcademicHistory().setCityDesc(city.getCityDesc());
            }

            SchoolTierTypeEntity schoolTier = schoolTierTypeBean.findSchoolTierByID(getAcademicHistory().getSchoolTierTypeID());
            if (schoolTier != null) {
                getAcademicHistory().setSchoolTierTypeDesc(schoolTier.getSchoolTierName());
            }

            getAcademicHistory().setAdmissionDate(DateUtil.toSQLDate(getAcademicHistory().getAdmissionDateTO()));
            getAcademicHistory().setCompletionDate(DateUtil.toSQLDate(getAcademicHistory().getCompletionDateTO()));

            getAcademicHistoryList().add(getAcademicHistory());
        } else {
            int index = getAcademicHistoryList().indexOf(getAcademicHistory());
            getAcademicHistoryList().set(index, getAcademicHistory());
        }
        setAcademicHistory(new AcademicHistoryEntity());

        return "";
    }

    public String saveAcademicHistoryList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext("#{applicationScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{applicationScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
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
            for (AcademicHistoryEntity history : getAcademicHistoryList()) {
                if (history.getAcademicHistoryID() == 0) {
                    try {
                        int cmID = Integer.parseInt(appPropBean.getValue(ACADA_HISTORY_ID_KEY,
                                ACADA_HISTORY_ID_DEFAULT, true));
                        history.setAcademicHistoryID(cmID);

                        dataServer.saveData(history);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (history.getAcademicHistoryID() > 0) {
                    dataServer.updateData(history);
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

        if (academicHistory.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((academicHistory.getApprovalStatusID() == 0) || (academicHistory.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(academicHistory.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        academicHistory.setApprovalStatusID(academicHistory.getApprovalStatusID() + 1);
                        academicHistory.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(academicHistory);
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
                } else if (academicHistory.getApprovalStatusID()
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

    public String loadHistoryList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        academicHistory = new AcademicHistoryEntity();
        academicHistoryList = new ArrayList<AcademicHistoryEntity>();
        academicHistoryItemList = new ArrayList<SelectItem>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        AcademicHistoryEntity criteria = new AcademicHistoryEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            AcademicHistoryEntity academicHistory = (AcademicHistoryEntity) entity;
            academicHistoryList.add(academicHistory);
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
            if (academicHistoryList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        academicHistoryList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadHistoryList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadHistoryList(batEntity);
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
            if (academicHistoryList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(academicHistoryList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadHistoryList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadHistoryList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.ACADEMIC_HISTORY);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(academicHistory.getAcademicHistoryID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadHistoryList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "academichistoryaudittrail.jsf";
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

    public String deleteHistory() {
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
            if (academicHistoryList.contains(academicHistory)) {
                if (academicHistory.getAcademicHistoryID() > 0) {
                    AcademicHistoryEntity criteria = new AcademicHistoryEntity();
                    criteria.setAcademicHistoryID(academicHistory.getAcademicHistoryID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                academicHistoryList.remove(academicHistory);
                academicHistory = new AcademicHistoryEntity();
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

    public void academicHistoryItemSelected(ValueChangeEvent vce) {
    }

    public void academicHistorySelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
        setAcademicHistory(getAcademicHistoryList().get(rowIndex));

        AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
        if (addressManagerBean == null) {
            addressManagerBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
        }

        addressManagerBean.selectCountry(getAcademicHistory().getSchoolCountryID());
        addressManagerBean.stateSelected(getAcademicHistory().getStateCode());
    }

    public void schoolCountryItemSelected(ValueChangeEvent vce) {
        AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
        if (addressManagerBean == null) {
            addressManagerBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
        }

        long countryID = Long.parseLong(vce.getNewValue().toString());
        addressManagerBean.selectCountry(countryID);
    }

    public void schoolStateItemSelected(ValueChangeEvent vce) {
        AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
        if (addressManagerBean == null) {
            addressManagerBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
        }

        String stateCode = vce.getNewValue().toString();
        addressManagerBean.stateSelected(stateCode);
    }

    public void instituteMenuSelected(ValueChangeEvent vce) {
        AcademicacademicInstituteBean instBean = (AcademicacademicInstituteBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicInstituteBean}", AcademicacademicInstituteBean.class);
        if (instBean != null) {
            instBean = new AcademicacademicInstituteBean();
            CommonBean.setBeanToContext(
                    "#{applicationScope.academicInstituteBean}", AcademicacademicInstituteBean.class, instBean);
        }

        AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
        if (addressManagerBean == null) {
            addressManagerBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
        }

        long schoolID = Long.parseLong(vce.getNewValue().toString());
        AcademicInstitutionEntity insitute = instBean.findInstituteByID(schoolID);
        if (insitute != null) {
            getAcademicHistory().setSchoolName(insitute.getSchoolName());

            getAcademicHistory().setSchoolCountryID(insitute.getCountryID());
            addressManagerBean.selectCountry(insitute.getCountryID());

            getAcademicHistory().setStateCode(insitute.getStateCode());
            addressManagerBean.stateSelected(insitute.getStateCode());

            getAcademicHistory().setCityID(insitute.getCityID());
            getAcademicHistory().setSchoolStreetAddress(insitute.getSchoolAddress());
        }
    }

    public AcademicHistoryEntity getAcademicHistory() {
        return this.academicHistory;
    }

    public void setAcademicHistory(AcademicHistoryEntity academichistory) {
        this.academicHistory = academichistory;
    }

    public List<AcademicHistoryEntity> getAcademicHistoryList() {
        return this.academicHistoryList;
    }

    public void setAcademicHistoryList(List<AcademicHistoryEntity> academichistoryList) {
        this.academicHistoryList = academichistoryList;
    }

    public List<SelectItem> getAcademicHistoryItemList() {
        return this.academicHistoryItemList;
    }

    public void setAcademicHistoryItemList(List<SelectItem> academichistoryItemList) {
        this.academicHistoryItemList = academichistoryItemList;
    }
}
