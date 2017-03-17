package com.rsdynamix.academics.setup.bean;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.setup.entities.AcademicSemesterEntity;
import com.rsdynamix.academics.setup.entities.AcademicSessionEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
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
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;

/**
 *
 * @author patushie@codrellica
 */
@SessionScoped
@ManagedBean(name = "academicSemesterBean")
public class AcademicSemesterBean {

    private static final String ACADA_SEMESTER_ID_KEY = "acada_semester_id";
    //
    private static final String ACADA_SEMESTER_ID_DEFAULT = "1";
    //
    private AcademicSemesterEntity academicSemester;
    private List<AcademicSemesterEntity> academicSemesterList;
    private List<SelectItem> academicSemesterItemList;

    public AcademicSemesterBean() {
        academicSemester = new AcademicSemesterEntity();
        academicSemesterList = new ArrayList<AcademicSemesterEntity>();
        academicSemesterItemList = new ArrayList<SelectItem>();

        loaAcademicSemesterList();
    }

    public void loaAcademicSemesterList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
        AcademicSemesterEntity criteria = new AcademicSemesterEntity();

        AcademicSessionBean academicSessionBean = (AcademicSessionBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicSessionBean}", AcademicSessionBean.class);
        if (academicSessionBean == null) {
            academicSessionBean = new AcademicSessionBean();
            CommonBean.setBeanToContext("#{applicationScope.academicSessionBean}", AcademicSessionBean.class, academicSessionBean);
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
            List<AbstractEntity> abstractSemesterList = dataServer.findData(criteria);
            for (AbstractEntity entity : abstractSemesterList) {
                AcademicSemesterEntity semesterEntity = (AcademicSemesterEntity) entity;
                AcademicSessionEntity session = academicSessionBean.findSessionByID(semesterEntity.getSessionID());
                if (session != null) {
                    semesterEntity.setSessionPeriod(session.getSessionPeriod());
                }

                SelectItem item = new SelectItem();
                item.setValue(semesterEntity.getSemesterID());
                item.setLabel(semesterEntity.getSemesterTier());
                item.setDescription(String.valueOf(semesterEntity.getSessionID()));
                getAcademicSemesterItemList().add(item);

                getAcademicSemesterList().add(semesterEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }
    
    public void addToSelectItemList(AcademicSemesterEntity entity) {
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
                item.setValue(entity.getSemesterID());
                item.setLabel(entity.getSemesterTier());
                academicSemesterItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getSemesterID());
            item.setLabel(entity.getSemesterTier());
            academicSemesterItemList.add(item);
        }
    }

    public String addAcademicSemester() {
        if (!getAcademicSemesterList().contains(getAcademicSemester())) {
            if (getAcademicSemester().isCurrentSemester()) {
                List<AcademicSemesterEntity> semesterList = new ArrayList<AcademicSemesterEntity>();
                for (AcademicSemesterEntity semester : getAcademicSemesterList()) {
                    if (semester.isCurrentSemester()) {
                        semester.setCurrentSemester(false);
                    }
                    semesterList.add(semester);
                }
                setAcademicSemesterList(semesterList);
            }

            getAcademicSemesterList().add(getAcademicSemester());
        } else {
            if (getAcademicSemester().isCurrentSemester()) {
                List<AcademicSemesterEntity> semesterList = new ArrayList<AcademicSemesterEntity>();
                for (AcademicSemesterEntity semester : getAcademicSemesterList()) {
                    if (semester.isCurrentSemester()) {
                        semester.setCurrentSemester(false);
                    }
                    semesterList.add(semester);
                }
                setAcademicSemesterList(semesterList);
            }

            int index = getAcademicSemesterList().indexOf(getAcademicSemester());
            getAcademicSemesterList().set(index, getAcademicSemester());
        }
        setAcademicSemester(new AcademicSemesterEntity());

        return "";
    }

    public String saveAcademicSemesterList() {
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
            for (AcademicSemesterEntity acadaSemester : getAcademicSemesterList()) {
                if (acadaSemester.getSemesterID() == 0) {
                    try {
                        int cmID = Integer.parseInt(appPropBean.getValue(ACADA_SEMESTER_ID_KEY,
                                ACADA_SEMESTER_ID_DEFAULT, true));
                        acadaSemester.setSemesterID(cmID);
                        dataServer.saveData(acadaSemester);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (acadaSemester.getSemesterID() > 0) {
                    dataServer.updateData(acadaSemester);
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

        if (academicSemester.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((academicSemester.getApprovalStatusID() == 0) || (academicSemester.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(academicSemester.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        academicSemester.setApprovalStatusID(academicSemester.getApprovalStatusID() + 1);
                        academicSemester.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(academicSemester);
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
                } else if (academicSemester.getApprovalStatusID()
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

    public String loadSemesterList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        academicSemester = new AcademicSemesterEntity();
        academicSemesterList = new ArrayList<AcademicSemesterEntity>();
        academicSemesterItemList = new ArrayList<SelectItem>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        AcademicSemesterEntity criteria = new AcademicSemesterEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            AcademicSemesterEntity academicSemester = (AcademicSemesterEntity) entity;
            academicSemesterList.add(academicSemester);
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
            if (academicSemesterList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        academicSemesterList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadSemesterList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadSemesterList(batEntity);
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
            if (academicSemesterList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(academicSemesterList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadSemesterList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadSemesterList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.ACADEMIC_SEMESTER);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(academicSemester.getSemesterID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadSemesterList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "acadasemesteraudittrail.jsf";
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

    public String deleteSemester() {
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
            if (academicSemesterList.contains(academicSemester)) {
                if (academicSemester.getSemesterID() > 0) {
                    AcademicSemesterEntity criteria = new AcademicSemesterEntity();
                    criteria.setSemesterID(academicSemester.getSemesterID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                academicSemesterList.remove(academicSemester);
                academicSemester = new AcademicSemesterEntity();
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

    public AcademicSemesterEntity findSemesterByID(long semesterID) {
        AcademicSemesterEntity acadaSession = null;

        for (AcademicSemesterEntity semest : getAcademicSemesterList()) {
            if (semest.getSemesterID() == semesterID) {
                acadaSession = semest;
                break;
            }
        }

        return acadaSession;
    }

    public AcademicSemesterEntity getCurrentSemester() {
        AcademicSemesterEntity acadaSession = null;

        for (AcademicSemesterEntity semest : getAcademicSemesterList()) {
            if (semest.isCurrentSemester()) {
                acadaSession = semest;
                break;
            }
        }

        return acadaSession;
    }

    public void academicSemesterItemSelected(ValueChangeEvent vce) {
    }

    public void academicSessionItemSelected(ValueChangeEvent vce) {
        long sessionID = Long.parseLong(vce.getNewValue().toString());

        if (sessionID > 0) {
            AcademicSessionBean academicSessionBean = (AcademicSessionBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.academicSessionBean}", AcademicSessionBean.class);
            if (academicSessionBean == null) {
                academicSessionBean = new AcademicSessionBean();
                CommonBean.setBeanToContext("#{applicationScope.academicSessionBean}", AcademicSessionBean.class, academicSessionBean);
            }

            AcademicSessionEntity session = academicSessionBean.findSessionByID(sessionID);
            if (session != null) {
                getAcademicSemester().setSessionPeriod(session.getSessionPeriod());
            }
        }
    }

    public void academicSemesterSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
    }

    public AcademicSemesterEntity getAcademicSemester() {
        return this.academicSemester;
    }

    public void setAcademicSemester(AcademicSemesterEntity academicsemester) {
        this.academicSemester = academicsemester;
    }

    public List<AcademicSemesterEntity> getAcademicSemesterList() {
        return this.academicSemesterList;
    }

    public void setAcademicSemesterList(List<AcademicSemesterEntity> academicsemesterList) {
        this.academicSemesterList = academicsemesterList;
    }

    public List<SelectItem> getAcademicSemesterItemList() {
        return this.academicSemesterItemList;
    }

    public void setAcademicSemesterItemList(List<SelectItem> academicsemesterItemList) {
        this.academicSemesterItemList = academicsemesterItemList;
    }
}
