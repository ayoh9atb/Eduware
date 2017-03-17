package com.rsdynamix.academics.setup.bean;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.setup.entities.AcademicDepartmentEntity;
import com.rsdynamix.academics.setup.entities.FacultyEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
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
@ManagedBean(name = "academicDepartmentBean")
public class AcademicDepartmentBean {

    private static final String DEPT_ID_KEY = "acada_dept_id";
    //
    private static final String DEPT_ID_DEFAULT = "1";
    //
    private AcademicDepartmentEntity academicDepartment;
    private List<AcademicDepartmentEntity> academicDepartmentList;
    private List<SelectItem> academicDepartmentItemList;
    private List<SelectItem> selectedAcademicDepartmentItemList;

    public AcademicDepartmentBean() {
        academicDepartment = new AcademicDepartmentEntity();
        academicDepartmentList = new ArrayList<AcademicDepartmentEntity>();
        academicDepartmentItemList = new ArrayList<SelectItem>();
        selectedAcademicDepartmentItemList = new ArrayList<SelectItem>();

        loaDepartmentList();
    }

    public void loaDepartmentList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        FacultyBean facultyBean = (FacultyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.facultyBean}", FacultyBean.class);
        if (facultyBean == null) {
            facultyBean = new FacultyBean();
            CommonBean.setBeanToContext("#{applicationScope.facultyBean}", FacultyBean.class, facultyBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        AcademicDepartmentEntity criteria = new AcademicDepartmentEntity();

        try {
            List<AbstractEntity> abstractFacultyList = dataServer.findData(criteria);
            for (AbstractEntity entity : abstractFacultyList) {
                AcademicDepartmentEntity deptEntity = (AcademicDepartmentEntity) entity;

                FacultyEntity faculty = facultyBean.findFacultyByID(deptEntity.getFacultyID());
                if (faculty != null) {
                    deptEntity.setFacultyName(faculty.getFacultyName());
                }

                SelectItem item = new SelectItem();
                item.setValue(deptEntity.getDepartmentID());
                item.setLabel(deptEntity.getDepartmentName());
                item.setDescription(String.valueOf(deptEntity.getFacultyID()));
                getAcademicDepartmentItemList().add(item);

                getAcademicDepartmentList().add(deptEntity);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public String addAcademicDepartment() {
        if (!getAcademicDepartmentList().contains(getAcademicDepartment())) {
            FacultyBean facultyBean = (FacultyBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.facultyBean}", FacultyBean.class);
            if (facultyBean == null) {
                facultyBean = new FacultyBean();
                CommonBean.setBeanToContext("#{applicationScope.facultyBean}", FacultyBean.class, facultyBean);
            }

            FacultyEntity faculty = facultyBean.findFacultyByID(getAcademicDepartment().getFacultyID());
            if (faculty != null) {
                getAcademicDepartment().setFacultyName(faculty.getFacultyName());
            }

            getAcademicDepartmentList().add(getAcademicDepartment());
        } else {
            int index = getAcademicDepartmentList().indexOf(getAcademicDepartment());
            getAcademicDepartmentList().set(index, getAcademicDepartment());
        }
        setAcademicDepartment(new AcademicDepartmentEntity());

        return "";
    }

    public String saveAcademicDepartmentList() {
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
            for (AcademicDepartmentEntity dept : getAcademicDepartmentList()) {
                if (dept.getDepartmentID() == 0) {
                    int cmID = Integer.parseInt(appPropBean.getValue(DEPT_ID_KEY,
                            DEPT_ID_DEFAULT, true));
                    
                    dept.setDepartmentID(cmID);
                    dataServer.saveData(dept);
                } else if (dept.getDepartmentID() > 0) {
                    dataServer.updateData(dept);
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

        if (academicDepartment.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((academicDepartment.getApprovalStatusID() == 0) || (academicDepartment.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(academicDepartment.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        academicDepartment.setApprovalStatusID(academicDepartment.getApprovalStatusID() + 1);
                        academicDepartment.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(academicDepartment);
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
                } else if (academicDepartment.getApprovalStatusID()
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

    public String loadDepartmentList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        academicDepartment = new AcademicDepartmentEntity();
        academicDepartmentList = new ArrayList<AcademicDepartmentEntity>();
        academicDepartmentItemList = new ArrayList<SelectItem>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        AcademicDepartmentEntity criteria = new AcademicDepartmentEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            AcademicDepartmentEntity academicDepartment = (AcademicDepartmentEntity) entity;
            academicDepartmentList.add(academicDepartment);
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
            if (academicDepartmentList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        academicDepartmentList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadDepartmentList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadDepartmentList(batEntity);
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
            if (academicDepartmentList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(academicDepartmentList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadDepartmentList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadDepartmentList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.ACADEMIC_DEPARTMENT);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(academicDepartment.getDepartmentID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadDepartmentList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "acadadepartmentaudittrail.jsf";
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

    public String deleteDepartment() {
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
            if (academicDepartmentList.contains(academicDepartment)) {
                if (academicDepartment.getDepartmentID() > 0) {
                    AcademicDepartmentEntity criteria = new AcademicDepartmentEntity();
                    criteria.setDepartmentID(academicDepartment.getDepartmentID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                academicDepartmentList.remove(academicDepartment);
                academicDepartment = new AcademicDepartmentEntity();
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

    public void academicDepartmentItemSelected(ValueChangeEvent vce) {
    }

    public void academicDepartmentSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
    }

    public void facultyItemSelected(long facultyID) {
        selectedAcademicDepartmentItemList = new ArrayList<SelectItem>();

        for (SelectItem item : academicDepartmentItemList) {
            if (Long.parseLong(item.getDescription()) == facultyID) {
                selectedAcademicDepartmentItemList.add(item);
            }
        }
    }

    public AcademicDepartmentEntity findDepartmentByID(long deptID) {
        AcademicDepartmentEntity dept = null;

        for (AcademicDepartmentEntity deptEnt : getAcademicDepartmentList()) {
            if (deptEnt.getDepartmentID() == deptID) {
                dept = deptEnt;
                break;
            }
        }

        return dept;
    }

    public List<AcademicDepartmentEntity> findDepartmentListByFacultyID(int facultyID) {
        List<AcademicDepartmentEntity> deptList = new ArrayList<AcademicDepartmentEntity>();

        for (AcademicDepartmentEntity deptEnt : getAcademicDepartmentList()) {
            if (deptEnt.getFacultyID() == facultyID) {
                deptList.add(deptEnt);
            }
        }

        return deptList;
    }

    public AcademicDepartmentEntity getAcademicDepartment() {
        return this.academicDepartment;
    }

    public void setAcademicDepartment(AcademicDepartmentEntity academicDepartment) {
        this.academicDepartment = academicDepartment;
    }

    public List<AcademicDepartmentEntity> getAcademicDepartmentList() {
        return this.academicDepartmentList;
    }

    public void setAcademicDepartmentList(List<AcademicDepartmentEntity> academicdepartmentList) {
        this.academicDepartmentList = academicdepartmentList;
    }

    public List<SelectItem> getAcademicDepartmentItemList() {
        return this.academicDepartmentItemList;
    }

    public void setAcademicDepartmentItemList(List<SelectItem> academicdepartmentItemList) {
        this.academicDepartmentItemList = academicdepartmentItemList;
    }

    /**
     * @return the selectedAcademicDepartmentItemList
     */
    public List<SelectItem> getSelectedAcademicDepartmentItemList() {
        return selectedAcademicDepartmentItemList;
    }

    /**
     * @param selectedAcademicDepartmentItemList the
     * selectedAcademicDepartmentItemList to set
     */
    public void setSelectedAcademicDepartmentItemList(List<SelectItem> selectedAcademicDepartmentItemList) {
        this.selectedAcademicDepartmentItemList = selectedAcademicDepartmentItemList;
    }
}
