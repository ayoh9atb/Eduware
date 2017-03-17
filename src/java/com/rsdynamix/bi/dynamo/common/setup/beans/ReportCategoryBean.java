/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.bi.dynamo.common.setup.beans;

import com.codrellica.projects.commons.AppPropertiesHandler;
import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.dynamo.setup.entities.ReportCategoryEntity;
import com.rsdynamix.hrms.employ.entities.DepartmentEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.hrms.commons.setup.beans.DepartmentBean;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsd.projects.menus.ValueListBean;
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

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "reportCategoryBean")
public class ReportCategoryBean implements Serializable {

    private static final String REPORT_CATEGORY_ID_KEY = "report_catgry_id";
    //
    private static final String REPORT_CATEGORY_ID_DEFAULT = "1";
    //
    private ReportCategoryEntity reportCategory;
    private List<ReportCategoryEntity> reportCategoryList;
    private List<SelectItem> categoryItemList;
    private List<SelectItem> departmentCategoryItemList;
    //
    private BusinessActionTrailEntity businessActionTrail;

    public ReportCategoryBean() {
        reportCategory = new ReportCategoryEntity();
        reportCategoryList = new ArrayList<ReportCategoryEntity>();
        categoryItemList = new ArrayList<SelectItem>();
        departmentCategoryItemList = new ArrayList<SelectItem>();
        businessActionTrail = new BusinessActionTrailEntity();

        loadCategoryList();
    }

    public String addCategory() {
        if (!getReportCategoryList().contains(getReportCategory())) {
            getReportCategoryList().add(getReportCategory());

            SelectItem item = new SelectItem();
            item.setValue(getReportCategory().getReportCategoryID());
            item.setLabel(getReportCategory().getReportCategoryName());

            getCategoryItemList().add(item);
        }
        setReportCategory(new ReportCategoryEntity());

        return "";
    }

    public void categorySelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        reportCategory = reportCategoryList.get(rowIndex);
        businessActionTrail = new BusinessActionTrailEntity();

        CommonBean.deselectOtherItems(reportCategory, reportCategoryList);
    }

    public void categoryMenuSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            reportCategory = findCategoryByID(Integer.parseInt(vce.getNewValue().toString()));
        }
    }

    public ReportCategoryEntity findCategoryByID(int categoryID) {
        ReportCategoryEntity catgry = null;

        for (ReportCategoryEntity rptDept : getReportCategoryList()) {
            if (rptDept.getReportCategoryID() == categoryID) {
                catgry = rptDept;
                break;
            }
        }

        return catgry;
    }

    public String deleteCategory(ValueChangeEvent vce) {
        getReportCategoryList().remove(getReportCategory());

        return "";
    }

    public String saveCategoryList() {
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

        try {
            for (ReportCategoryEntity categry : getReportCategoryList()) {
                if (categry.getReportCategoryID() == 0) {
                    try {
                        int categID = Integer.parseInt(appPropBean.getValue(
                                REPORT_CATEGORY_ID_KEY,
                                REPORT_CATEGORY_ID_DEFAULT, true));

                        categry.setReportCategoryID(categID);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    dataServer.saveData(categry);
                } else {
                    dataServer.updateData(categry);
                }
            }
        } catch (SQLException ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.RPTS_CATGRY_MENU_ITEM));

        if (reportCategory.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((reportCategory.getApprovalStatusID() == 0) || (reportCategory.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(reportCategory.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        reportCategory.setApprovalStatusID(reportCategory.getApprovalStatusID() + 1);
                        reportCategory.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(reportCategory);
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
                } else if (reportCategory.getApprovalStatusID()
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

    public String clearCategoryList() {
        setReportCategoryList(new ArrayList<ReportCategoryEntity>());
        return "";
    }

    public String loadCategoryList() {
        if ((reportCategoryList == null) || (reportCategoryList.size() == 0)) {
            reloadCategoryList();
        }

        return "";
    }

    public String reloadCategoryList() {
        setReportCategoryList(new ArrayList<ReportCategoryEntity>());
        businessActionTrail = new BusinessActionTrailEntity();

        ValueListBean valueListBean = (ValueListBean) CommonBean.getBeanFromContext(
                "#{sessionScope.valueListBean}", ValueListBean.class);
        if (valueListBean == null) {
            valueListBean = new ValueListBean();
            CommonBean.setBeanToContext("#{sessionScope.valueListBean}", ValueListBean.class, valueListBean);
        }

        DepartmentBean departmentBean = (DepartmentBean) CommonBean.getBeanFromContext(
                "#{sessionScope.departmentBean}", DepartmentBean.class);
        if (departmentBean == null) {
            departmentBean = new DepartmentBean();
            CommonBean.setBeanToContext("#{sessionScope.departmentBean}", DepartmentBean.class, departmentBean);
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

        try {
            ReportCategoryEntity criteria = new ReportCategoryEntity();
            List<AbstractEntity> baseCategoryList = dataServer.findData(criteria);

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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.RPTS_CATGRY_MENU_ITEM));

            for (AbstractEntity baseCategory : baseCategoryList) {
                ReportCategoryEntity categry = (ReportCategoryEntity) baseCategory;

                categry.setActivated(categry.getApprovalStatusID() >= privilege.getApprovedStatusID());

                DepartmentEntity department = departmentBean.findDepartmentByID(categry.getDepartmentID());
                if (department != null) {
                    categry.setDepartmentName(department.getDepartmentName());
                }

                getReportCategoryList().add(categry);
                addToSelectItemList(categry);
            }

            Collections.sort(getReportCategoryList(), new Comparator<ReportCategoryEntity>() {

                public int compare(ReportCategoryEntity o1, ReportCategoryEntity o2) {
                    return (o1.getReportCategoryName().compareTo(o2.getReportCategoryName()));
                }
            });

            Collections.sort(getCategoryItemList(), new Comparator<SelectItem>() {

                public int compare(SelectItem o1, SelectItem o2) {
                    return (o1.getLabel().compareTo(o2.getLabel()));
                }
            });

            if ((getCategoryItemList() != null) && (getCategoryItemList().size() > 0)) {
                valueListBean.getValueListMap().put(getCategoryItemList().getClass().getName(),
                        getCategoryItemList());

                SelectItem item = new SelectItem();
                item.setLabel("ReportCategoryEntity Menu");
                item.setValue(getCategoryItemList().getClass().getName());
                valueListBean.getValueCategoryTypeItemList().add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String loadCategoryList(BusinessActionTrailEntity businessActionTrail) {
        reportCategoryList = new ArrayList<ReportCategoryEntity>();

        ValueListBean valueListBean = (ValueListBean) CommonBean.getBeanFromContext(
                "#{sessionScope.valueListBean}", ValueListBean.class);
        if (valueListBean == null) {
            valueListBean = new ValueListBean();
            CommonBean.setBeanToContext("#{sessionScope.valueListBean}", ValueListBean.class, valueListBean);
        }

        DepartmentBean departmentBean = (DepartmentBean) CommonBean.getBeanFromContext(
                "#{sessionScope.departmentBean}", DepartmentBean.class);
        if (departmentBean == null) {
            departmentBean = new DepartmentBean();
            CommonBean.setBeanToContext("#{sessionScope.departmentBean}", DepartmentBean.class, departmentBean);
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

        try {
            ReportCategoryEntity criteria = new ReportCategoryEntity();
            List<AbstractEntity> baseCategoryList = dataServer.findMasterData(criteria, businessActionTrail.getEntityMasterID());

            for (AbstractEntity baseCategory : baseCategoryList) {
                ReportCategoryEntity categry = (ReportCategoryEntity) baseCategory;
                this.businessActionTrail = (BusinessActionTrailEntity)businessActionTrail.cloneEntity();
                
                getReportCategoryList().add(categry);
            }

            Collections.sort(getReportCategoryList(), new Comparator<ReportCategoryEntity>() {

                public int compare(ReportCategoryEntity o1, ReportCategoryEntity o2) {
                    return (o1.getReportCategoryName().compareTo(o2.getReportCategoryName()));
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void addToSelectItemList(ReportCategoryEntity entity) {
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
                menuManagerBean.getSystemMap().get(MenuManagerBean.RPTS_CATGRY_MENU_ITEM));

        if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getReportCategoryID());
            item.setLabel(entity.getReportCategoryName());
            categoryItemList.add(item);
        }
    }

    public void departmentSelectedForCategoryCreate(ValueChangeEvent vce) {
        int departmentID = Integer.parseInt(vce.getNewValue().toString());

        if (departmentID != -1) {
            DepartmentBean departmentBean = (DepartmentBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.departmentBean}", DepartmentBean.class);
            if (departmentBean == null) {
                departmentBean = new DepartmentBean();
                CommonBean.setBeanToContext("#{sessionScope.departmentBean}", DepartmentBean.class, departmentBean);
            }

            DepartmentEntity department = departmentBean.findDepartmentByID(departmentID);
            if (department != null) {
                reportCategory.setDepartmentName(department.getDepartmentName());
            }
        }
    }

    public void departmentSelectedForReportCreate(int departmentID) {
        departmentCategoryItemList = new ArrayList<SelectItem>();

        for (ReportCategoryEntity catEntity : getReportCategoryList()) {
            if (catEntity.getDepartmentID() == departmentID) {
                SelectItem item = new SelectItem();
                item.setValue(catEntity.getReportCategoryID());
                item.setLabel(catEntity.getReportCategoryName());

                departmentCategoryItemList.add(item);
            }
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

        if (reportCategoryList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(businessActionTrail);
            if (batEntity != null) {
                loadCategoryList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    (BusinessActionTrailEntity)businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadCategoryList(batEntity);
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

        if (reportCategoryList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(businessActionTrail);
            if (batEntity != null) {
                loadCategoryList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {            
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                    (BusinessActionTrailEntity)businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadCategoryList(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.REPORT_CATEGORY);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(reportCategory.getReportCategoryID());
        businessActionTrailBean.loadBusinessActionTrailList();

        if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
            businessActionTrailBean.setBusinessActionTrail(
                    businessActionTrailBean.getBusinessActionTrailList().get(
                            businessActionTrailBean.getBusinessActionTrailList().size() - 1));

            loadCategoryList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            outcome = "reportcategoryaudittrail.jsf";
        } else {
            applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    /**
     * @return the reportCategory
     */
    public ReportCategoryEntity getReportCategory() {
        return reportCategory;
    }

    /**
     * @param reportCategory the reportCategory to set
     */
    public void setReportCategory(ReportCategoryEntity reportCategory) {
        this.reportCategory = reportCategory;
    }

    /**
     * @return the reportCategoryList
     */
    public List<ReportCategoryEntity> getReportCategoryList() {
        return reportCategoryList;
    }

    /**
     * @param categoryList the reportCategoryList to set
     */
    public void setReportCategoryList(List<ReportCategoryEntity> categoryList) {
        this.reportCategoryList = categoryList;
    }

    /**
     * @return the categoryItemList
     */
    public List<SelectItem> getCategoryItemList() {
        return categoryItemList;
    }

    /**
     * @param categoryItemList the categoryItemList to set
     */
    public void setCategoryItemList(List<SelectItem> categoryItemList) {
        this.categoryItemList = categoryItemList;
    }

    /**
     * @return the departmentCategoryItemList
     */
    public List<SelectItem> getDepartmentCategoryItemList() {
        return departmentCategoryItemList;
    }

    /**
     * @param departmentCategoryItemList the departmentCategoryItemList to set
     */
    public void setDepartmentCategoryItemList(List<SelectItem> departmentCategoryItemList) {
        this.departmentCategoryItemList = departmentCategoryItemList;
    }

    /**
     * @return the businessActionTrail
     */
    public BusinessActionTrailEntity getBusinessActionTrail() {
        return businessActionTrail;
    }

    /**
     * @param businessActionTrail the businessActionTrail to set
     */
    public void setBusinessActionTrail(BusinessActionTrailEntity businessActionTrail) {
        this.businessActionTrail = businessActionTrail;
    }
}
