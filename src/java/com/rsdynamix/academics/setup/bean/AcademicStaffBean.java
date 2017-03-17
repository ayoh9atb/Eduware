/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.setup.bean;

import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.setup.entities.AcademicDepartmentEntity;
import com.rsdynamix.academics.setup.entities.AcademicStaffEntity;
import com.rsdynamix.academics.setup.entities.FacultyEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.tns.util.Constants;
import com.rsdynamix.abstractobjects.FieldData;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.finance.constants.FinultimateConstants;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import com.rsdynamics.projects.query.operators.QueryRange;
import com.rsdynamics.projects.query.operators.QueryFieldData;

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "academicStaffBean")
public class AcademicStaffBean {

    private static final String STAFF_NO_SERIAL_KEY = "staff_no_serial_id";
    //
    private static final String STAFF_NO_SERIAL_DEFAULT = "1";
    //
    private static final String STAFF_NO_SERIAL_MX_KEY = "staff_no_serial_mx_id";
    //
    private static final String STAFF_NO_SERIAL_MX_DEFAULT = "4";
    //
    private static final String STAFF_NO_PRFX_KEY = "staff_no_prfx_id";
    //
    private static final String STAFF_NO_PRFX_DEFAULT = "UCS";
    //
    private static final String ACADEMIC_STAFF_ROLE_KEY = "acada_staff_role_id";
    //
    private AcademicStaffEntity staff;
    private List<AcademicStaffEntity> staffList;

    public AcademicStaffBean() {
        staff = new AcademicStaffEntity();
        staffList = new ArrayList<AcademicStaffEntity>();
    }

    public String addStaff() {
        if (staff.getPassword().equals(staff.getConfirmedPassword())) {
            FacultyBean facultyBean = (FacultyBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.facultyBean}", FacultyBean.class);
            if (facultyBean == null) {
                facultyBean = new FacultyBean();
                CommonBean.setBeanToContext("#{applicationScope.facultyBean}", FacultyBean.class, facultyBean);
            }

            AcademicDepartmentBean academicDepartmentBean = (AcademicDepartmentBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class);
            if (academicDepartmentBean == null) {
                academicDepartmentBean = new AcademicDepartmentBean();
                CommonBean.setBeanToContext("#{applicationScope.academicDepartmentBean}", 
                        AcademicDepartmentBean.class, academicDepartmentBean);
            }

            FacultyEntity faculty = facultyBean.findFacultyByID(staff.getFacultyID());
            if (faculty != null) {
                staff.setFacultyDesc(faculty.getFacultyName());
            }

            AcademicDepartmentEntity dept = academicDepartmentBean.findDepartmentByID(staff.getDepartmentID());
            if (dept != null) {
                staff.setDepartmentDesc(dept.getDepartmentName());
            }
            staffList.add(staff);

            staff = new AcademicStaffEntity();
        } else {
            //TODO Mssg...
        }

        return "";
    }

    public String saveStaff() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext("#{applicationScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{applicationScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        UserManagerBean userBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userBean}", UserManagerBean.class);
        if (userBean == null) {
            userBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userBean}", UserManagerBean.class, userBean);
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
            for (AcademicStaffEntity stff : staffList) {
                if (stff.getStaffNumber().trim().equals("")) {
                    int staffID = Integer.parseInt(appPropBean.getValue(STAFF_NO_SERIAL_KEY,
                            STAFF_NO_SERIAL_DEFAULT, true));

                    int staffNoMx = Integer.parseInt(appPropBean.getValue(STAFF_NO_SERIAL_MX_KEY,
                            STAFF_NO_SERIAL_MX_DEFAULT, false));

                    String staffNoPrfx = appPropBean.getValue(STAFF_NO_PRFX_KEY,
                            STAFF_NO_PRFX_DEFAULT, false);

                    stff.setStaffNumber(staffNoPrfx + "/" + padString(staffID, staffNoMx));
                    dataServer.saveData(stff);

                    String staffRoleID = appPropBean.getValue(ACADEMIC_STAFF_ROLE_KEY,
                            FinultimateConstants.ONE_STR, false);

                    userBean.getAccount().setUserName(stff.getUsername());
                    userBean.getAccount().setPassword(stff.getPassword());
                    userBean.getAccount().setEmailAddress(stff.getEmailAddress());
                    userBean.getAccount().setEmployeeCode(stff.getStaffNumber());
                    userBean.insertUserAccount();
                } else if (stff.getStaffNumber().trim().equals("")) {
                    dataServer.updateData(stff);
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

        if (staff.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((staff.getApprovalStatusID() == 0) || (staff.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(staff.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        staff.setApprovalStatusID(staff.getApprovalStatusID() + 1);
                        staff.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(staff);
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
                } else if (staff.getApprovalStatusID()
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

    public String loadStaffList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        staff = new AcademicStaffEntity();
        staffList = new ArrayList<AcademicStaffEntity>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        AcademicStaffEntity criteria = new AcademicStaffEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            AcademicStaffEntity staff = (AcademicStaffEntity) entity;
            staffList.add(staff);
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
            if (staffList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        staffList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadStaffList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadStaffList(batEntity);
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
            if (staffList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(staffList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadStaffList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadStaffList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.ACADEMIC_STAFF);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(staff.getStaffID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadStaffList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "academicstaffaudittrail.jsf";
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

    public String deleteStaff() {
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
            if (staffList.contains(staff)) {
                if (staff.getStaffID() > 0) {
                    AcademicStaffEntity criteria = new AcademicStaffEntity();
                    criteria.setStaffID(staff.getStaffID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                staffList.remove(staff);
                staff = new AcademicStaffEntity();
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

    private String padString(int number, int strLen) {
        String paddedStr = String.valueOf(number);

        for (int i = 0; i <= strLen - String.valueOf(number).length(); i++) {
            paddedStr = "0" + paddedStr;
        }

        return paddedStr;
    }

    public String searchForStaff() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        AcademicStaffEntity criteria = new AcademicStaffEntity();

        if (!staff.getStaffNumber().trim().equals("")) {
            criteria.setStaffNumber(staff.getStaffNumber());
        }

        if (!staff.getStaffName().trim().equals("")) {
            Map<String, QueryRange> qeryRangeMap = new HashMap<String, QueryRange>();
            QueryRange queryRange = new QueryRange();
            QueryFieldData queryFieldData = new QueryFieldData();
            FieldData fieldData = new FieldData();

            fieldData.setFieldName(FinultimateConstants.ACADEMIC_STAFF_NAME_FIELD_NAME);
            fieldData.setFieldValue("%"+staff.getStaffName()+"%");
            queryFieldData.setFieldData(fieldData);
            queryFieldData.setFieldOperator(com.rsdynamics.projects.query.operators.QueryOperators.LIKE);
            queryRange.setLowerBoundField(queryFieldData);
            
            criteria.getQueryRangeMap().put(
                    FinultimateConstants.ACADEMIC_STAFF_NAME_FIELD_NAME, queryRange);
        }

        if (staff.getFacultyID() > 0) {
            criteria.setFacultyID(staff.getFacultyID());
        }

        if (staff.getDepartmentID() > 0) {
            criteria.setDepartmentID(staff.getDepartmentID());
        }

        try {
            List<AbstractEntity> entityList = dataServer.findData(criteria);
            for (AbstractEntity entity : entityList) {
                AcademicStaffEntity acadaStaff = (AcademicStaffEntity) entity;
                staffList.add(acadaStaff);
            }
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }

    public void facultyItemSelected(ValueChangeEvent vce) {
        long facultyID = Long.parseLong(vce.getNewValue().toString());

        if (facultyID > 0) {
            AcademicDepartmentBean academicDepartmentBean = (AcademicDepartmentBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class);
            if (academicDepartmentBean == null) {
                academicDepartmentBean = new AcademicDepartmentBean();
                CommonBean.setBeanToContext("#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class, academicDepartmentBean);
            }

            academicDepartmentBean.facultyItemSelected(facultyID);
        }
    }

    public void departmentItemSelected(ValueChangeEvent vce) {
        long deptID = Long.parseLong(vce.getNewValue().toString());

        if (deptID > 0) {
            SubjectOfStudyBean subjectOfStudyBean = (SubjectOfStudyBean) CommonBean.getBeanFromContext(
                    "#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class);
            if (subjectOfStudyBean == null) {
                subjectOfStudyBean = new SubjectOfStudyBean();
                CommonBean.setBeanToContext("#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class, subjectOfStudyBean);
            }

            subjectOfStudyBean.departmentItemSelected(deptID);
        }
    }

    /**
     * @return the staff
     */
    public AcademicStaffEntity getStaff() {
        return staff;
    }

    /**
     * @param staff the staff to set
     */
    public void setStaff(AcademicStaffEntity staff) {
        this.staff = staff;
    }

    /**
     * @return the staffList
     */
    public List<AcademicStaffEntity> getStaffList() {
        return staffList;
    }

    /**
     * @param staffList the staffList to set
     */
    public void setStaffList(List<AcademicStaffEntity> staffList) {
        this.staffList = staffList;
    }
}
