package com.rsdynamix.academics.setup.bean;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.academics.setup.entities.AcademicDepartmentEntity;
import com.rsdynamix.academics.setup.entities.AdmissionType;
import com.rsdynamix.academics.setup.entities.FacultyEntity;
import com.rsdynamix.academics.setup.entities.StudentEntity;
import com.rsdynamix.academics.setup.entities.SubjectOfStudyEntity;
import com.rsdynamix.dynamo.common.setup.beans.AddressManagerBean;
import com.rsdynamix.dynamo.setup.entities.CityEntity;
import com.rsdynamix.dynamo.setup.entities.CountryEntity;
import com.rsdynamix.dynamo.setup.entities.StateEntity;
import com.rsdynamix.tns.util.Constants;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.codrellica.projects.commons.DateUtil;
import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
@ManagedBean(name = "studentBean")
public class StudentBean {

    private static final String STUDENT_ID_KEY = "studnt_id";
    //
    private static final String STUDENT_ID_DEFAULT = "1";
    //
    private static final String STUDENT_NO_PRFX_KEY = "stud_no_prfx_id";
    //
    private static final String STUDENT_NO_PRFX_DEFAULT = "CEA";
    //
    private static final String MAX_DIG_SEQ_KEY = "mx_dig_seq_id";
    //
    private static final String MAX_DIG_SEQ_DEFAULT = "8";
    //
    private static final String STUDENT_ROLE_KEY = "stud_role_id";
    //
    private StudentEntity student;
    private List<StudentEntity> studentList;
    private List<SelectItem> studentItemList;
    private List<SelectItem> admissionTypeItemList;

    public StudentBean() {
        student = new StudentEntity();
        studentList = new ArrayList<StudentEntity>();
        studentItemList = new ArrayList<SelectItem>();
        admissionTypeItemList = new ArrayList<SelectItem>();

        populateAdmissionTypeItemList();
    }

    public void populateAdmissionTypeItemList() {
        admissionTypeItemList = new ArrayList<SelectItem>();

        SelectItem item = new SelectItem();
        item.setValue(AdmissionType.FULL_TIME);
        item.setLabel(AdmissionType.FULL_TIME.toString());
        admissionTypeItemList.add(item);

        item = new SelectItem();
        item.setValue(AdmissionType.PART_TIME);
        item.setLabel(AdmissionType.PART_TIME.toString());
        admissionTypeItemList.add(item);
    }

    public String searchForStudent() {
        studentList = new ArrayList<StudentEntity>();

        AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
        if (addressManagerBean == null) {
            addressManagerBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
        }

        SubjectOfStudyBean subjectOfStudyBean = (SubjectOfStudyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class);
        if (subjectOfStudyBean == null) {
            subjectOfStudyBean = new SubjectOfStudyBean();
            CommonBean.setBeanToContext("#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class, subjectOfStudyBean);
        }

        AcademicDepartmentBean academicDepartmentBean = (AcademicDepartmentBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class);
        if (academicDepartmentBean == null) {
            academicDepartmentBean = new AcademicDepartmentBean();
            CommonBean.setBeanToContext("#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class, academicDepartmentBean);
        }

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

        try {
            if (!getStudent().getStudentNumber().trim().equals("")) {
                FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                StudentEntity criteria = new StudentEntity();
                criteria.setStudentNumber(getStudent().getStudentNumber());

                List<AbstractEntity> abstractStudentList = dataServer.findData(criteria);
                for (AbstractEntity entity : abstractStudentList) {
                    StudentEntity studEntity = (StudentEntity) entity;

                    CountryEntity country = addressManagerBean.findCountryByID(studEntity.getCountryID());
                    if (country != null) {
                        studEntity.setCountryDesc(country.getCountryDesc());
                    }

                    StateEntity state = addressManagerBean.findStateByCode(studEntity.getStateCode(), studEntity.getCountryID());
                    if (state != null) {
                        studEntity.setStateDesc(state.getStateDesc());
                    }

                    CityEntity city = addressManagerBean.findCityByID(studEntity.getCityID());
                    if (city != null) {
                        studEntity.setCityDesc(city.getCityDesc());
                    }

                    country = addressManagerBean.findCountryByID(studEntity.getContactCountryID());
                    if (country != null) {
                        studEntity.setContactCountryDesc(country.getCountryDesc());
                    }

                    state = addressManagerBean.findStateByCode(studEntity.getContactStateCode(), studEntity.getContactCountryID());
                    if (state != null) {
                        studEntity.setContactStateDesc(state.getStateDesc());
                    }

                    city = addressManagerBean.findCityByID(studEntity.getContactCityID());
                    if (city != null) {
                        studEntity.setContactCityDesc(city.getCityDesc());
                    }

                    FacultyEntity faculty = facultyBean.findFacultyByID(studEntity.getFacultyID());
                    if (faculty != null) {
                        studEntity.setFacultyName(faculty.getFacultyName());
                    }

                    AcademicDepartmentEntity dept = academicDepartmentBean.findDepartmentByID(studEntity.getDepartmentID());
                    if (dept != null) {
                        studEntity.setDepartmentName(dept.getDepartmentName());
                    }

                    SubjectOfStudyEntity subject = subjectOfStudyBean.findSubjectOfStudyByID(studEntity.getSubjectOfStudyID());
                    if (subject != null) {
                        studEntity.setSubjectOfStudyName(subject.getSubjectTitle());
                    }

                    studEntity.setDateOfBirthTO(DateUtil.toUtilDate(studEntity.getDateOfBirth()));
                    studEntity.setAdmissionDateTO(DateUtil.toUtilDate(studEntity.getAdmissionDate()));
                    studEntity.setExpectedGraduationDateTO(DateUtil.toUtilDate(studEntity.getExpectedGraduationDate()));

                    SelectItem item = new SelectItem();
                    item.setValue(studEntity.getStudentNumber());
                    item.setLabel(studEntity.getLastName() + " " + studEntity.getFirstName() + " " + studEntity.getMiddleName());
                    getStudentItemList().add(item);

                    getStudentList().add(studEntity);
                }
            }
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        return "";
    }
    
    public void addToSelectItemList(StudentEntity entity) {
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
                item.setValue(entity.getStudentID());
                item.setLabel(entity.getLastName()+" "+entity.getFirstName()+" "+entity.getMiddleName());
                studentItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getStudentID());
            item.setLabel(entity.getLastName()+" "+entity.getFirstName()+" "+entity.getMiddleName());
            studentItemList.add(item);
        }
    }

    public String addStudent() {
        AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
        if (addressManagerBean == null) {
            addressManagerBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
        }

        SubjectOfStudyBean subjectOfStudyBean = (SubjectOfStudyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class);
        if (subjectOfStudyBean == null) {
            subjectOfStudyBean = new SubjectOfStudyBean();
            CommonBean.setBeanToContext("#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class, subjectOfStudyBean);
        }

        AcademicDepartmentBean academicDepartmentBean = (AcademicDepartmentBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class);
        if (academicDepartmentBean == null) {
            academicDepartmentBean = new AcademicDepartmentBean();
            CommonBean.setBeanToContext("#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class, academicDepartmentBean);
        }

        FacultyBean facultyBean = (FacultyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.facultyBean}", FacultyBean.class);
        if (facultyBean == null) {
            facultyBean = new FacultyBean();
            CommonBean.setBeanToContext("#{applicationScope.facultyBean}", FacultyBean.class, facultyBean);
        }

        if (!getStudentList().contains(getStudent())) {
            CountryEntity country = addressManagerBean.findCountryByID(getStudent().getCountryID());
            if (country != null) {
                getStudent().setCountryDesc(country.getCountryDesc());
            }

            StateEntity state = addressManagerBean.findStateByCode(getStudent().getStateCode(), getStudent().getCountryID());
            if (state != null) {
                getStudent().setStateDesc(state.getStateDesc());
            }

            CityEntity city = addressManagerBean.findCityByID(getStudent().getCityID());
            if (city != null) {
                getStudent().setCityDesc(city.getCityDesc());
            }

            country = addressManagerBean.findCountryByID(getStudent().getContactCountryID());
            if (country != null) {
                getStudent().setContactCountryDesc(country.getCountryDesc());
            }

            state = addressManagerBean.findStateByCode(getStudent().getContactStateCode(), getStudent().getContactCountryID());
            if (state != null) {
                getStudent().setContactStateDesc(state.getStateDesc());
            }

            city = addressManagerBean.findCityByID(getStudent().getContactCityID());
            if (city != null) {
                getStudent().setContactCityDesc(city.getCityDesc());
            }

            FacultyEntity faculty = facultyBean.findFacultyByID(getStudent().getFacultyID());
            if (faculty != null) {
                getStudent().setFacultyName(faculty.getFacultyName());
            }

            AcademicDepartmentEntity dept = academicDepartmentBean.findDepartmentByID(getStudent().getDepartmentID());
            if (dept != null) {
                getStudent().setDepartmentName(dept.getDepartmentName());
            }

            SubjectOfStudyEntity subject = subjectOfStudyBean.findSubjectOfStudyByID(getStudent().getSubjectOfStudyID());
            if (subject != null) {
                getStudent().setSubjectOfStudyName(subject.getSubjectTitle());
            }

            getStudent().setDateOfBirth(DateUtil.toSQLDate(getStudent().getDateOfBirthTO()));
            getStudent().setAdmissionDateTO(new Date());
            getStudent().setAdmissionDate(DateUtil.toSQLDate(getStudent().getAdmissionDateTO()));

            getStudent().setExpectedGraduationDateTO(DateUtil.toUtilDate(DateUtil.getEndOfPeriod(
                    DateUtil.getDateAsString(getStudent().getAdmissionDateTO()), (365 * 4))));
            getStudent().setExpectedGraduationDate(DateUtil.toSQLDate(getStudent().getExpectedGraduationDateTO()));

            getStudentList().add(getStudent());
        } else {
            int index = getStudentList().indexOf(getStudent());
            getStudentList().set(index, getStudent());
        }
        setStudent(new StudentEntity());

        return "";
    }

    public String saveStudentList() {
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
            for (StudentEntity stud : getStudentList()) {
                if (stud.getStudentNumber().trim().length() == 0) {
                    try {
                        long serial = Long.parseLong(appPropBean.getValue(STUDENT_ID_KEY,
                                STUDENT_ID_DEFAULT, true));

                        String studNoPrefix = appPropBean.getValue(STUDENT_NO_PRFX_KEY,
                                STUDENT_NO_PRFX_DEFAULT, false);

                        int digitSeqLen = Integer.parseInt(appPropBean.getValue(MAX_DIG_SEQ_KEY,
                                MAX_DIG_SEQ_DEFAULT, false));
                        stud.setStudentNumber(contructStudentNumber(serial, studNoPrefix, digitSeqLen));
                        dataServer.saveData(stud);

                        String studentRoleID = appPropBean.getValue(STUDENT_ROLE_KEY,
                                FinultimateConstants.ONE_STR, false);

                        userBean.getAccount().setUserName(stud.getUsername());
                        userBean.getAccount().setPassword(stud.getPassword());
                        userBean.getAccount().setEmailAddress(stud.getEmailAddress());
                        userBean.getAccount().setEmployeeCode(stud.getStudentNumber());
                        userBean.insertUserAccount();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (stud.getStudentNumber().trim().length() > 0) {
                    dataServer.updateData(stud);
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

        if (student.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((student.getApprovalStatusID() == 0) || (student.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(student.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        student.setApprovalStatusID(student.getApprovalStatusID() + 1);
                        student.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(student);
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
                } else if (student.getApprovalStatusID()
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

    public String loadStudentList(BusinessActionTrailEntity businessActionTrail) throws Exception {
        student = new StudentEntity();
        studentList = new ArrayList<StudentEntity>();
        studentItemList = new ArrayList<SelectItem>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        StudentEntity criteria = new StudentEntity();
        List<AbstractEntity> entityList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());
        for (AbstractEntity entity : entityList) {
            StudentEntity student = (StudentEntity) entity;
            studentList.add(student);
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
            if (studentList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        studentList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadStudentList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadStudentList(batEntity);
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
            if (studentList.size() > 0) {
                BusinessActionTrailEntity batEntity = businessActionTrailBean
                        .loadNextHistoricalState(studentList.get(0).getActionTrail());
                if (batEntity != null) {
                    loadStudentList(batEntity);
                } else {
                    applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
                }
            } else {
                BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                        (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                if (batEntity != null) {
                    loadStudentList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.STUDENT);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(student.getStudentID());
        businessActionTrailBean.loadBusinessActionTrailList();

        try {
            if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
                businessActionTrailBean.setBusinessActionTrail(
                        businessActionTrailBean.getBusinessActionTrailList().get(
                                businessActionTrailBean.getBusinessActionTrailList().size() - 1));

                loadStudentList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
                outcome = "studentaudittrail.jsf";
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

    public String deleteStudent() {
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
            if (studentList.contains(student)) {
                if (student.getStudentID() > 0) {
                    StudentEntity criteria = new StudentEntity();
                    criteria.setStudentID(student.getStudentID());

                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                studentList.remove(student);
                student = new StudentEntity();
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

    private String contructStudentNumber(long serial, String studNoPrefix, int digitSeqLen) {
        String serialStr = String.valueOf(serial);
        for (int i = 0; i <= digitSeqLen - 1; i++) {
            serialStr = "0" + serialStr;
        }

        return studNoPrefix + "/" + DateUtil.getCurrentDateStr().split("/")[DateUtil.YEAR_PART] + "/" + serialStr;
    }

    public void studentItemSelected(ValueChangeEvent vce) {
    }

    public String getStudNoOnStudentSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
        setStudent(getStudentList().get(rowIndex));

        return getStudent().getStudentNumber();
    }

    public void studentSelected(ValueChangeEvent vce) {
        AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
        if (addressManagerBean == null) {
            addressManagerBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
        }

        SubjectOfStudyBean subjectOfStudyBean = (SubjectOfStudyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class);
        if (subjectOfStudyBean == null) {
            subjectOfStudyBean = new SubjectOfStudyBean();
            CommonBean.setBeanToContext("#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class, subjectOfStudyBean);
        }

        AcademicDepartmentBean academicDepartmentBean = (AcademicDepartmentBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class);
        if (academicDepartmentBean == null) {
            academicDepartmentBean = new AcademicDepartmentBean();
            CommonBean.setBeanToContext("#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class, academicDepartmentBean);
        }

        FacultyBean facultyBean = (FacultyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.facultyBean}", FacultyBean.class);
        if (facultyBean == null) {
            facultyBean = new FacultyBean();
            CommonBean.setBeanToContext("#{applicationScope.facultyBean}", FacultyBean.class, facultyBean);
        }

        int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
        setStudent(getStudentList().get(rowIndex));

        addressManagerBean.selectCountry(getStudent().getCountryID());
        addressManagerBean.selectCountry2(getStudent().getContactCountryID());

        addressManagerBean.stateSelected(getStudent().getStateCode());
        addressManagerBean.stateSelected2(getStudent().getContactStateCode());

        academicDepartmentBean.facultyItemSelected(getStudent().getFacultyID());
        subjectOfStudyBean.departmentItemSelected(getStudent().getDepartmentID());
    }

    public void countryItemSelected(ValueChangeEvent vce) {
        AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
        if (addressManagerBean == null) {
            addressManagerBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
        }

        long countryID = Long.parseLong(vce.getNewValue().toString());
        addressManagerBean.selectCountry(countryID);
    }

    public void contactCountryItemSelected(ValueChangeEvent vce) {
        AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
        if (addressManagerBean == null) {
            addressManagerBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
        }

        long countryID = Long.parseLong(vce.getNewValue().toString());
        addressManagerBean.selectCountry2(countryID);
    }

    public void sponsorCountryItemSelected(ValueChangeEvent vce) {
        AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
        if (addressManagerBean == null) {
            addressManagerBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
        }

        long countryID = Long.parseLong(vce.getNewValue().toString());
        addressManagerBean.selectCountry3(countryID);
    }

    public void stateItemSelected(ValueChangeEvent vce) {
        AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
        if (addressManagerBean == null) {
            addressManagerBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
        }

        String stateCode = vce.getNewValue().toString();
        addressManagerBean.stateSelected(stateCode);
    }

    public void contactStateItemSelected(ValueChangeEvent vce) {
        AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
        if (addressManagerBean == null) {
            addressManagerBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
        }

        String stateCode = vce.getNewValue().toString();
        addressManagerBean.stateSelected2(stateCode);
    }

    public void sponsorStateItemSelected(ValueChangeEvent vce) {
        AddressManagerBean addressManagerBean = (AddressManagerBean) CommonBean.getBeanFromContext(
                "#{applicationScope.addressManagerBean}", AddressManagerBean.class);
        if (addressManagerBean == null) {
            addressManagerBean = new AddressManagerBean();
            CommonBean.setBeanToContext("#{applicationScope.addressManagerBean}", AddressManagerBean.class, addressManagerBean);
        }

        String stateCode = vce.getNewValue().toString();
        addressManagerBean.stateSelected3(stateCode);
    }

    public void subjectItemSelected(ValueChangeEvent vce) {

    }

    public void departmentItemSelected(ValueChangeEvent vce) {
        long deptID = Long.parseLong(vce.getNewValue().toString());

        SubjectOfStudyBean subjectOfStudyBean = (SubjectOfStudyBean) CommonBean.getBeanFromContext(
                "#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class);
        if (subjectOfStudyBean == null) {
            subjectOfStudyBean = new SubjectOfStudyBean();
            CommonBean.setBeanToContext("#{applicationScope.subjectOfStudyBean}", SubjectOfStudyBean.class, subjectOfStudyBean);
        }

        subjectOfStudyBean.departmentItemSelected(deptID);
    }

    public void facultyItemSelected(ValueChangeEvent vce) {
        long facultyID = Long.parseLong(vce.getNewValue().toString());

        AcademicDepartmentBean academicDepartmentBean = (AcademicDepartmentBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class);
        if (academicDepartmentBean == null) {
            academicDepartmentBean = new AcademicDepartmentBean();
            CommonBean.setBeanToContext("#{applicationScope.academicDepartmentBean}", AcademicDepartmentBean.class, academicDepartmentBean);
        }

        academicDepartmentBean.facultyItemSelected(facultyID);
    }

    public void relationshipItemSelected(ValueChangeEvent vce) {
    }

    public StudentEntity getStudent() {
        return this.student;
    }

    public void setStudent(StudentEntity student) {
        this.student = student;
    }

    public List<StudentEntity> getStudentList() {
        return this.studentList;
    }

    public void setStudentList(List<StudentEntity> studentList) {
        this.studentList = studentList;
    }

    public List<SelectItem> getStudentItemList() {
        return this.studentItemList;
    }

    public void setStudentItemList(List<SelectItem> studentItemList) {
        this.studentItemList = studentItemList;
    }

    /**
     * @return the admissionTypeItemList
     */
    public List<SelectItem> getAdmissionTypeItemList() {
        return admissionTypeItemList;
    }

    /**
     * @param admissionTypeItemList the admissionTypeItemList to set
     */
    public void setAdmissionTypeItemList(List<SelectItem> admissionTypeItemList) {
        this.admissionTypeItemList = admissionTypeItemList;
    }
}
