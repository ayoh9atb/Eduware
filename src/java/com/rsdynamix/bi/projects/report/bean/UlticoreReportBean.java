/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.bi.projects.report.bean;

import com.codrellica.projects.commons.AppPropertiesHandler;
import com.codrellica.projects.commons.DateUtil;
import com.codrellica.projects.commons.SQLStyle;
import com.rsd.projects.menus.FinultimateCommons;
import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.bi.dynamo.common.setup.beans.ReportCategoryBean;
import com.rsdynamix.bi.projects.scheduler.CourrierScheduler;
import com.rsdynamix.bi.projects.web.commons.bean.ApplicationInitializer;
import com.rsdynamix.bi.projects.web.commons.bean.RendererType;
import com.rsdynamix.projects.report.entities.ReportBodyType;
import com.rsdynamix.bi.projects.web.commons.bean.ReportFormat;
import com.rsdynamix.bi.projects.web.commons.bean.UlticoreReportTableCreator;
import com.rsdynamix.bi.reports.servlet.ReportAttributes;
import com.rsdynamix.dynamo.setup.entities.ReportCategoryEntity;
import com.rsdynamix.hrms.employ.entities.DepartmentEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.hrms.commons.setup.beans.DepartmentBean;
import com.rsdynamix.projects.dynamo.payload.DDTSurrogate;
import com.rsdynamix.projects.report.entities.DefaultSystemGeneratedFields;
import com.rsdynamix.abstractobjects.AbstractQueryParameter;
import com.rsdynamix.projects.report.entities.ReportScheduleEntity;
import com.rsdynamix.abstractobjects.AbstractTemplateDataType;
import com.rsdynamix.projects.report.entities.ReportTriggerEntity;
import com.rsdynamix.projects.report.entities.SQLStatementType;
import com.rsdynamix.projects.report.entities.SubreportQueryParameterEntity;
import com.rsdynamix.projects.report.entities.SubreportTemplateParamEntity;
import com.rsdynamix.abstractobjects.AbstractDataField;
import com.rsdynamix.abstractobjects.AbstractFieldMetaEntity;
import com.rsdynamix.abstractobjects.AbstractTemplateParamEntity;
import com.rsdynamix.abstractobjects.FieldValueType;
import com.rsdynamix.abstractobjects.QueryParameterEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.projects.report.entities.ReportQueryParameterEntity;
import com.rsdynamix.projects.report.entities.ReportTemplateParamEntity;
import com.rsdynamix.projects.report.entities.UlticoreReportEntity;
import com.rsdynamix.projects.report.entities.UlticoreReportFieldMetaEntity;
import com.rsdynamix.projects.report.entities.UlticoreSubreportEntity;
import com.rsdynamix.projects.report.entities.UlticoreSubreportFieldMetaEntity;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import com.rsdynamix.projects.ulticoreparser.beans.DynamoUtil;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.projects.commons.messages.beans.MessageSetupBean;
import com.rsd.projects.menus.ValueListBean;
import com.rsdynamix.tns.util.Constants;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author root
 */
@SessionScoped
@ManagedBean(name = "ulticoreReportBean")
public class UlticoreReportBean {

    private static final String REPORT_TEMPLATE_ID_KEY = "rport_tmplat_id";
    /**/
    private static final String REPORT_TEMPLATE_ID_DEFAULT = "1";
    /**/
    private static final String REPORT_TRIGGER_ID_KEY = "rport_trigger_id";
    /**/
    private static final String REPORT_TRIGGER_ID_DEFAULT = "1";
    /**/
    private static final String REPORT_SCHEDULE_ID_KEY = "rport_schedl_id";
    /**/
    private static final String REPORT_SCHEDULE_ID_DEFAULT = "1";
    /**/
    private static final String REPORT_FIELD_ID_KEY = "rport_fld_id";
    /**/
    private static final String REPORT_FIELD_ID_DEFAULT = "1";
    /**/
    private static final String QUERY_FIELD_ID_KEY = "qry_fld_id";
    /**/
    private static final String QUERY_FIELD_ID_DEFAULT = "1";
    /**/
    private static final String REPORT_PARAM_ID_KEY = "rport_tmplt_param_id";
    /**/
    private static final String REPORT_PARAM_ID_DEFAULT = "1";
    //
    private static final String SUBREPORT_TEMPLATE_ID_KEY = "sub_rport_tmplat_id";
    /**/
    private static final String SUBREPORT_TEMPLATE_ID_DEFAULT = "1";
    //
    private static final String SUBREPORT_FIELD_ID_KEY = "sub_rport_fld_id";
    /**/
    private static final String SUBREPORT_FIELD_ID_DEFAULT = "1";
    /**/
    private static final String SUBREPORT_QUERY_FIELD_ID_KEY = "subrept_qry_fld_id";
    /**/
    private static final String SUBREPORT_QUERY_FIELD_ID_DEFAULT = "1";
    /**/
    private static final String SUBREPORT_PARAM_ID_KEY = "subrport_tmplt_param_id";
    /**/
    private static final String SUBREPORT_PARAM_ID_DEFAULT = "1";
    //
    /**/
    private UlticoreReportEntity ultiReport;
    private UlticoreSubreportEntity ultiSubreport;
    //
    private UlticoreReportFieldMetaEntity reportFieldMeta;
    private UlticoreSubreportFieldMetaEntity subreportFieldMeta;
    //
    private QueryParameterEntity parameter;
    private SubreportQueryParameterEntity subreportParameter;
    //
    private AbstractTemplateParamEntity reportParamter;
    private SubreportTemplateParamEntity subreportParamter;
    //
    private List<UlticoreReportEntity> ultiReportList;
    private List<SelectItem> databaseTableFieldItemList;
    private List<SelectItem> subreportDatabaseTableFieldItemList;
    private RendererType rendererType;
    //
    private ReportTriggerEntity reportTrigger;
    private ReportScheduleEntity reportSchedule;
    //
    private List<ReportTriggerEntity> reportTriggerList;
    private List<ReportScheduleEntity> reportScheduleList;
    //
    private List<SelectItem> ultiReportItemList;
    private List<SelectItem> reportTriggerItemList;
    private List<SelectItem> selectedReportFieldItemList;
    private List<SelectItem> systemGeneratedFieldItemList;
    private List<SelectItem> queryOperatorItemList;
    private List<SelectItem> sqlStmtItemList;
    private List<SelectItem> dataTypeNameItemList;
    private List<SelectItem> reportBodyTypeItemList;
    //
    private DefaultSystemGeneratedFields noDefaultSysGenFieldVal;
    private DataTable dataTable;
    //
    private ReportFormat reportFormat;
    private ReportBodyType reportBodyType;
    private HttpSession servletSession;
    private String sessionID;
    private String reportExportFormat;
    //
    private List<SelectItem> parameterValueTypeItemList;
    private List<SelectItem> fieldValueTypeItemList;
    private boolean derivedReportFieldValue;
    private boolean derivedSubreportFieldValue;
    //
    private boolean hasSubreport;
    private String processingResourceUrl;
    //
    private BusinessActionTrailEntity businessActionTrail;

    public UlticoreReportBean() {
        ultiReport = new UlticoreReportEntity();
        ultiReport.setSqlStatementType(1);//default as Call Statement...
        ultiSubreport = new UlticoreSubreportEntity();

        reportFieldMeta = new UlticoreReportFieldMetaEntity();
        subreportFieldMeta = new UlticoreSubreportFieldMetaEntity();

        parameter = new ReportQueryParameterEntity();
        subreportParameter = new SubreportQueryParameterEntity();

        reportParamter = new ReportTemplateParamEntity();
        subreportParamter = new SubreportTemplateParamEntity();

        ultiReportList = new ArrayList<UlticoreReportEntity>();
        databaseTableFieldItemList = new ArrayList<SelectItem>();
        subreportDatabaseTableFieldItemList = new ArrayList<SelectItem>();

        ultiReportItemList = new ArrayList<SelectItem>();
        selectedReportFieldItemList = new ArrayList<SelectItem>();
        systemGeneratedFieldItemList = new ArrayList<SelectItem>();
        reportTriggerItemList = new ArrayList<SelectItem>();
        dataTypeNameItemList = new ArrayList<SelectItem>();
        reportBodyTypeItemList = new ArrayList<SelectItem>();

        dataTable = new DataTable();
        sessionID = "";

        parameterValueTypeItemList = new ArrayList<SelectItem>();
        noDefaultSysGenFieldVal = DefaultSystemGeneratedFields.NONE;

        reportTrigger = new ReportTriggerEntity();
        reportSchedule = new ReportScheduleEntity();

        reportTriggerList = new ArrayList<ReportTriggerEntity>();
        reportScheduleList = new ArrayList<ReportScheduleEntity>();
        sqlStmtItemList = new ArrayList<SelectItem>();

        fieldValueTypeItemList = new ArrayList<SelectItem>();

        processingResourceUrl = "";
        businessActionTrail = new BusinessActionTrailEntity();

        servletSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        populateOperators();
        loadReportTemplates();
        startReportScheduler();
    }

    public void populateOperators() {
        queryOperatorItemList = new ArrayList<SelectItem>();

        SelectItem item = new SelectItem();
        item.setLabel("=");
        item.setValue("=");
        queryOperatorItemList.add(item);

        item = new SelectItem();
        item.setLabel("<");
        item.setValue("<");
        queryOperatorItemList.add(item);

        item = new SelectItem();
        item.setLabel("<=");
        item.setValue("<=");
        queryOperatorItemList.add(item);

        item = new SelectItem();
        item.setLabel(">");
        item.setValue(">");
        queryOperatorItemList.add(item);

        item = new SelectItem();
        item.setLabel(">=");
        item.setValue(">=");
        queryOperatorItemList.add(item);

        item = new SelectItem();
        item.setLabel("LIKE");
        item.setValue("LIKE");
        queryOperatorItemList.add(item);

        item = new SelectItem();
        item.setLabel("IN");
        item.setValue("IN");
        queryOperatorItemList.add(item);
    }

    public String loadReportTemplates() {
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

        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}",
                    UserManagerBean.class, userManagerBean);
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
            ultiReportList = new ArrayList<UlticoreReportEntity>();
            setParameterValueTypeItemList(new ArrayList<SelectItem>());

            UlticoreReportEntity urCriteria = new UlticoreReportEntity();
            List<AbstractEntity> baseURList = dataServer.findData(urCriteria);

            ReportTriggerEntity trggCriteria = new ReportTriggerEntity();
            List<AbstractEntity> baseTriggerList = dataServer.findData(trggCriteria);

            ReportScheduleEntity rschCriteria = new ReportScheduleEntity();
            List<AbstractEntity> baseScheduleList = dataServer.findData(rschCriteria);

            DepartmentBean departmentBean = (DepartmentBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.departmentBean}", DepartmentBean.class);
            if (departmentBean == null) {
                departmentBean = new DepartmentBean();
                CommonBean.setBeanToContext("#{sessionScope.departmentBean}", DepartmentBean.class, departmentBean);
            }

            ReportCategoryBean categoryBean = (ReportCategoryBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.reportCategoryBean}", ReportCategoryBean.class);
            if (categoryBean == null) {
                categoryBean = new ReportCategoryBean();
                CommonBean.setBeanToContext("#{sessionScope.reportCategoryBean}", ReportCategoryBean.class, categoryBean);
            }

            MenuEntity privilege = privilegeBean.findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.SETP_RPTS_MENU_ITEM));

            for (AbstractEntity baseUR : baseURList) {
                UlticoreReportEntity report = (UlticoreReportEntity) baseUR;
                boolean activated = false;

                if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
                    report.setActivated(true);

                    report.initializeTransients();

                    DepartmentEntity department = departmentBean.findDepartmentByID(report.getDepartmentID());
                    if (department != null) {
                        report.setDepartmentName(department.getDepartmentName());
                    }

                    ReportCategoryEntity category = categoryBean.findCategoryByID(report.getCategoryID());
                    if (category != null) {
                        report.setCategoryName(category.getReportCategoryName());
                    }

                    SelectItem item = new SelectItem();
                    item.setValue(report.getReportTitle());
                    item.setLabel(report.getReportTitle());
                    ultiReportItemList.add(item);

                    getUltiReportList().add(report);
                } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.ON) {
                    if (userManagerBean.getUserAccount().getRole().hasRWPrivilege(
                            menuManagerBean.getSystemMap().get(MenuManagerBean.SETP_RPTS_MENU_ITEM))) {
                        report.setActivated(report.getApprovalStatusID() >= privilege.getApprovedStatusID());

                        report.initializeTransients();

                        DepartmentEntity department = departmentBean.findDepartmentByID(report.getDepartmentID());
                        if (department != null) {
                            report.setDepartmentName(department.getDepartmentName());
                        }

                        ReportCategoryEntity category = categoryBean.findCategoryByID(report.getCategoryID());
                        if (category != null) {
                            report.setCategoryName(category.getReportCategoryName());
                        }

                        SelectItem item = new SelectItem();
                        item.setValue(report.getReportTitle());
                        item.setLabel(report.getReportTitle());
                        ultiReportItemList.add(item);

                        getUltiReportList().add(report);
                    } else {
                        if (report.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                            activated = true;
                        } else if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                            activated = true;
                        }

                        report.setActivated(activated);
                        if (activated) {
                            report.initializeTransients();

                            DepartmentEntity department = departmentBean.findDepartmentByID(report.getDepartmentID());
                            if (department != null) {
                                report.setDepartmentName(department.getDepartmentName());
                            }

                            ReportCategoryEntity category = categoryBean.findCategoryByID(report.getCategoryID());
                            if (category != null) {
                                report.setCategoryName(category.getReportCategoryName());
                            }

                            SelectItem item = new SelectItem();
                            item.setValue(report.getReportTitle());
                            item.setLabel(report.getReportTitle());
                            ultiReportItemList.add(item);

                            getUltiReportList().add(report);
                        }
                    }
                }
            }

            for (AbstractEntity baseTrigger : baseTriggerList) {
                ReportTriggerEntity trigger = (ReportTriggerEntity) baseTrigger;
                getReportTriggerList().add(trigger);

                SelectItem item = new SelectItem();
                item.setValue(trigger.getReportTriggerID());
                item.setLabel(trigger.getTriggerName());
                getReportTriggerItemList().add(item);
            }

            for (AbstractEntity baseSchedule : baseScheduleList) {
                ReportScheduleEntity schedule = (ReportScheduleEntity) baseSchedule;

                ReportTriggerEntity trigger = findReportTriggerByID(schedule.getReportTriggerID());
                if (trigger != null) {
                    schedule.setReportTriggerName(trigger.getTriggerName());
                }
                getReportScheduleList().add(schedule);
            }

            SelectItem item = new SelectItem();
            item.setValue(AbstractTemplateDataType.STRING_TYPE);
            item.setLabel(AbstractTemplateDataType.STRING_TYPE.toString());
            getParameterValueTypeItemList().add(item);

            item = new SelectItem();
            item.setValue(AbstractTemplateDataType.INTEGER_TYPE);
            item.setLabel(AbstractTemplateDataType.INTEGER_TYPE.toString());
            getParameterValueTypeItemList().add(item);

            item = new SelectItem();
            item.setValue(AbstractTemplateDataType.FLOATING_POINT_TYPE);
            item.setLabel(AbstractTemplateDataType.FLOATING_POINT_TYPE.toString());
            getParameterValueTypeItemList().add(item);

            item = new SelectItem();
            item.setValue(AbstractTemplateDataType.MONEY_DIGIT_TYPE);
            item.setLabel(AbstractTemplateDataType.MONEY_DIGIT_TYPE.toString());
            getParameterValueTypeItemList().add(item);

            item = new SelectItem();
            item.setValue(AbstractTemplateDataType.MONEY_WORD_TYPE);
            item.setLabel(AbstractTemplateDataType.MONEY_WORD_TYPE.toString());
            getParameterValueTypeItemList().add(item);

            item = new SelectItem();
            item.setValue(AbstractTemplateDataType.DATE_TYPE);
            item.setLabel(AbstractTemplateDataType.DATE_TYPE.toString());
            getParameterValueTypeItemList().add(item);

            item = new SelectItem();
            item.setValue(DefaultSystemGeneratedFields.NONE);
            item.setLabel(DefaultSystemGeneratedFields.NONE.toString());
            getSystemGeneratedFieldItemList().add(item);

            item = new SelectItem();
            item.setValue(DefaultSystemGeneratedFields.CURRENT_DATE);
            item.setLabel(DefaultSystemGeneratedFields.CURRENT_DATE.toString());
            getSystemGeneratedFieldItemList().add(item);

            item = new SelectItem();
            item.setValue(DefaultSystemGeneratedFields.CURRENT_TIMESTAMP);
            item.setLabel(DefaultSystemGeneratedFields.CURRENT_TIMESTAMP.toString());
            getSystemGeneratedFieldItemList().add(item);

            item = new SelectItem();
            item.setValue(DefaultSystemGeneratedFields.CURRENT_USER);
            item.setLabel(DefaultSystemGeneratedFields.CURRENT_USER.toString());
            getSystemGeneratedFieldItemList().add(item);

            item = new SelectItem();
            item.setValue(DefaultSystemGeneratedFields.INCREMENTAL_COUNT);
            item.setLabel(DefaultSystemGeneratedFields.INCREMENTAL_COUNT.toString());
            getSystemGeneratedFieldItemList().add(item);

            item = new SelectItem();
            item.setValue(DefaultSystemGeneratedFields.DAY_OF_YEAR);
            item.setLabel(DefaultSystemGeneratedFields.DAY_OF_YEAR.toString());
            getSystemGeneratedFieldItemList().add(item);

            item = new SelectItem();
            item.setValue(DefaultSystemGeneratedFields.WEEK_OF_YEAR);
            item.setLabel(DefaultSystemGeneratedFields.WEEK_OF_YEAR.toString());
            getSystemGeneratedFieldItemList().add(item);

            item = new SelectItem();
            item.setValue(SQLStatementType.CALL_STATEMENT);
            item.setLabel(SQLStatementType.CALL_STATEMENT.toString());
            sqlStmtItemList.add(item);

            item = new SelectItem();
            item.setValue(SQLStatementType.SELECT_STATEMENT);
            item.setLabel(SQLStatementType.SELECT_STATEMENT.toString());
            sqlStmtItemList.add(item);

            fieldValueTypeItemList = new ArrayList<SelectItem>();
            item = new SelectItem();
            item.setValue(FieldValueType.INPUT);
            item.setLabel(FieldValueType.INPUT.toString());
            fieldValueTypeItemList.add(item);

            item = new SelectItem();
            item.setValue(FieldValueType.DERIVED);
            item.setLabel(FieldValueType.DERIVED.toString());
            fieldValueTypeItemList.add(item);

            dataTypeNameItemList = new ArrayList<SelectItem>();
            item = new SelectItem();
            item.setValue(Float.class.getName());
            item.setLabel(Float.class.getName());
            dataTypeNameItemList.add(item);

            item = new SelectItem();
            item.setValue(Double.class.getName());
            item.setLabel(Double.class.getName());
            dataTypeNameItemList.add(item);

            item = new SelectItem();
            item.setValue(Integer.class.getName());
            item.setLabel(Integer.class.getName());
            dataTypeNameItemList.add(item);

            item = new SelectItem();
            item.setValue(String.class.getName());
            item.setLabel(String.class.getName());
            dataTypeNameItemList.add(item);

            item = new SelectItem();
            item.setValue(BigDecimal.class.getName());
            item.setLabel(BigDecimal.class.getName());
            dataTypeNameItemList.add(item);

            item = new SelectItem();
            item.setValue(Date.class.getName());
            item.setLabel(Date.class.getName());
            dataTypeNameItemList.add(item);

            item = new SelectItem();
            item.setValue(ReportBodyType.INTUITIVE);
            item.setLabel("PLANE TABLE (INTUITIVE)");
            reportBodyTypeItemList.add(item);

            item = new SelectItem();
            item.setValue(ReportBodyType.TEMPLATE);
            item.setLabel("TEMPLATE BASED");
            reportBodyTypeItemList.add(item);

            item = new SelectItem();
            item.setValue(ReportBodyType.INTUIT_TEMPLATE);
            item.setLabel("INTUITIVE + TEMPLATE");
            reportBodyTypeItemList.add(item);
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String loadReportTemplates(BusinessActionTrailEntity businessActionTrail) {
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
            ultiReportList = new ArrayList<UlticoreReportEntity>();
            setParameterValueTypeItemList(new ArrayList<SelectItem>());

            UlticoreReportEntity urCriteria = new UlticoreReportEntity();
            List<AbstractEntity> baseURList = dataServer.findMasterData(urCriteria, businessActionTrail.getEntityMasterID());

            DepartmentBean departmentBean = (DepartmentBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.departmentBean}", DepartmentBean.class);
            if (departmentBean == null) {
                departmentBean = new DepartmentBean();
                CommonBean.setBeanToContext("#{sessionScope.departmentBean}", DepartmentBean.class, departmentBean);
            }

            ReportCategoryBean categoryBean = (ReportCategoryBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.reportCategoryBean}", ReportCategoryBean.class);
            if (categoryBean == null) {
                categoryBean = new ReportCategoryBean();
                CommonBean.setBeanToContext("#{sessionScope.reportCategoryBean}", ReportCategoryBean.class, categoryBean);
            }

            for (AbstractEntity baseUR : baseURList) {
                UlticoreReportEntity report = (UlticoreReportEntity) baseUR;
                report.initializeTransients();

                this.businessActionTrail = (BusinessActionTrailEntity) businessActionTrail.cloneEntity();

                DepartmentEntity department = departmentBean.findDepartmentByID(report.getDepartmentID());
                if (department != null) {
                    report.setDepartmentName(department.getDepartmentName());
                }

                ReportCategoryEntity category = categoryBean.findCategoryByID(report.getCategoryID());
                if (category != null) {
                    report.setCategoryName(category.getReportCategoryName());
                }

                SelectItem item = new SelectItem();
                item.setValue(report.getReportTitle());
                item.setLabel(report.getReportTitle());
                ultiReportItemList.add(item);

                getUltiReportList().add(report);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void startReportScheduler() {
        MessageSetupBean messageSetupBean = null;
        if (FacesContext.getCurrentInstance() != null) {
            messageSetupBean = (MessageSetupBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.messageSetupBean}", MessageSetupBean.class);
            if (messageSetupBean == null) {
                messageSetupBean = new MessageSetupBean();
                CommonBean.setBeanToContext("#{sessionScope.messageSetupBean}", MessageSetupBean.class, messageSetupBean);
            }
        } else {
            messageSetupBean = (MessageSetupBean) ApplicationInitializer.APP_SERVLET_CONTEXT.getAttribute("messageSetupBean");
        }

        CourrierScheduler scheduler = new CourrierScheduler();
        for (ReportScheduleEntity sched : getReportScheduleList()) {
            if (sched.getReportTriggerID() > 0) {
                ReportTriggerEntity trigger = findReportTriggerByID(sched.getReportTriggerID());
                if (trigger != null) {
                    scheduler.startSchedulerService(sched, trigger, this, messageSetupBean);
                }
            }
        }
    }

    public void departmentSelectedForReportCreate(ValueChangeEvent vce) {
        int departmentID = Integer.parseInt(vce.getNewValue().toString());
        departmentSelectedForReportCreate(departmentID);
    }

    public void departmentSelectedForReportCreate(int departmentID) {
        if (departmentID > -1) {
            DepartmentBean departmentBean = (DepartmentBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.departmentBean}", DepartmentBean.class);
            if (departmentBean == null) {
                departmentBean = new DepartmentBean();
                CommonBean.setBeanToContext("#{sessionScope.departmentBean}", DepartmentBean.class, departmentBean);
            }

            ReportCategoryBean categoryBean = (ReportCategoryBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.reportCategoryBean}", ReportCategoryBean.class);
            if (categoryBean == null) {
                categoryBean = new ReportCategoryBean();
                CommonBean.setBeanToContext("#{sessionScope.reportCategoryBean}", ReportCategoryBean.class, categoryBean);
            }

            DepartmentEntity department = departmentBean.findDepartmentByID(departmentID);
            if (department != null) {
                getUltiReport().setDepartmentName(department.getDepartmentName());
            }

            categoryBean.departmentSelectedForReportCreate(departmentID);
        }
    }

    public void categorySelectedForReportCreate(ValueChangeEvent vce) {
        int categoryID = Integer.parseInt(vce.getNewValue().toString());

        if (categoryID != -1) {
            ReportCategoryBean categoryBean = (ReportCategoryBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.reportCategoryBean}", ReportCategoryBean.class);
            if (categoryBean == null) {
                categoryBean = new ReportCategoryBean();
                CommonBean.setBeanToContext("#{sessionScope.reportCategoryBean}", ReportCategoryBean.class, categoryBean);
            }

            ReportCategoryEntity category = categoryBean.findCategoryByID(categoryID);
            if (category != null) {
                getUltiReport().setCategoryName(category.getReportCategoryName());
            }
        }
    }

    public String addReportTrigger() {
        if (!getReportTriggerList().contains(getReportTrigger())) {
            getReportTriggerList().add(getReportTrigger());
        } else {
            int index = getReportTriggerList().indexOf(getReportTrigger());
            getReportTriggerList().set(index, getReportTrigger());
        }
        setReportTrigger(new ReportTriggerEntity());

        return "";
    }

    public String deleteReportTrigger() {
        if (getReportTriggerList().contains(getReportTrigger())) {
            getReportTriggerList().remove(getReportTrigger());
            setReportTrigger(new ReportTriggerEntity());
            //applicationMessageBean.insertMessage("Report Trigger has been deleted", MessageType.SUCCESS_MESSAGE);
        }

        return "";
    }

    public String saveReportTriggerList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        dataServer.beginTransaction();
        try {
            for (ReportTriggerEntity trigger : getReportTriggerList()) {
                if (trigger.getReportTriggerID() == 0) {
                    try {
                        int cmID = Integer.parseInt(appPropBean.getValue(REPORT_TRIGGER_ID_KEY,
                                REPORT_TRIGGER_ID_DEFAULT, true));
                        trigger.setReportTriggerID(cmID);

                        dataServer.saveData(trigger);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        dataServer.updateData(trigger);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            dataServer.endTransaction();
            applicationMessageBean.insertMessage("Report Trigger has been saved", MessageType.SUCCESS_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void reportTriggerSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        setReportTrigger(getReportTriggerList().get(rowIndex));
    }

    public String addReportSchedule() {
        if (getReportSchedule().getReportTriggerID() != -1) {
            ReportTriggerEntity trigger = findReportTriggerByID(getReportSchedule().getReportTriggerID());
            if (trigger != null) {
                getReportSchedule().setReportTriggerName(trigger.getTriggerName());
            }
        }

        if (!getReportScheduleList().contains(getReportSchedule())) {
            getReportScheduleList().add(getReportSchedule());
        } else {
            int index = getReportScheduleList().indexOf(getReportSchedule());
            getReportScheduleList().set(index, getReportSchedule());
        }
        setReportSchedule(new ReportScheduleEntity());

        return "";
    }

    public String deleteReportSchedule() {
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
            if (getReportScheduleList().contains(getReportSchedule())) {
                getReportScheduleList().remove(getReportSchedule());

                if (getReportSchedule().getReportScheduleID() > 0) {
                    if (getReportSchedule().isActivated()) {
                        applicationMessageBean.insertMessage(
                                "This Item has been Activated. Deletion of Activated Items is not allowed",
                                MessageType.ERROR_MESSAGE);
                        return "";
                    }
                    ReportScheduleEntity criteria = new ReportScheduleEntity();
                    criteria.setReportScheduleID(getReportSchedule().getReportScheduleID());
                    dataServer.beginTransaction();
                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }
                applicationMessageBean.insertMessage("Report Schedule has been deleted", MessageType.SUCCESS_MESSAGE);

                reportParamter = new ReportTemplateParamEntity();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String saveReportScheduleList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        try {

            dataServer.beginTransaction();
            for (ReportScheduleEntity trigger : getReportScheduleList()) {
                if (trigger.getReportScheduleID() == 0) {
                    try {
                        int cmID = Integer.parseInt(appPropBean.getValue(REPORT_SCHEDULE_ID_KEY,
                                REPORT_SCHEDULE_ID_DEFAULT, true));
                        trigger.setReportScheduleID(cmID);

                        dataServer.saveData(trigger);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        dataServer.updateData(trigger);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            dataServer.endTransaction();
            applicationMessageBean.insertMessage("Report Schedule has been saved", MessageType.SUCCESS_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public ReportScheduleEntity findReportScheduleByID(long schedID) {
        ReportScheduleEntity schedule = null;

        for (ReportScheduleEntity sched : getReportScheduleList()) {
            if (sched.getReportScheduleID() == schedID) {
                schedule = sched;
                break;
            }
        }

        return schedule;
    }

    public void reportScheduleSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        setReportSchedule(getReportScheduleList().get(rowIndex));
    }

    public ReportTriggerEntity findReportTriggerByID(long triggerID) {
        ReportTriggerEntity trigger = null;

        for (ReportTriggerEntity rept : getReportTriggerList()) {
            if (rept.getReportTriggerID() == triggerID) {
                trigger = rept;
                break;
            }
        }

        return trigger;
    }
    
    public ReportTriggerEntity findReportTriggerByName(String triggerName) {
        ReportTriggerEntity trigger = null;

        for (ReportTriggerEntity reptTrigger : getReportTriggerList()) {
            if (reptTrigger.getTriggerName().equalsIgnoreCase(triggerName)) {
                trigger = reptTrigger;
                break;
            }
        }

        return trigger;
    }

    public String addReportParameter() {
        if (!getUltiReport().getAbstractParameterList().contains(getReportParamter())) {
            getUltiReport().getAbstractParameterList().add(getReportParamter());
            setReportParamter(new ReportTemplateParamEntity());
        } else {
            int index = getUltiReport().getAbstractParameterList().indexOf(getReportParamter());
            getUltiReport().getAbstractParameterList().set(index, getReportParamter());
            setReportParamter(new ReportTemplateParamEntity());
        }

        return "";
    }

    public String deleteReportParameter() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (getUltiReport().getAbstractParameterList().contains(getReportParamter())) {
                FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();
                AbstractTemplateParamEntity criteria = new ReportTemplateParamEntity();
                criteria.setParameterID(getReportParamter().getParameterID());
                dataServer.beginTransaction();
                dataServer.deleteData((AbstractEntity) criteria);
                dataServer.endTransaction();

                getUltiReport().getAbstractParameterList().remove(getReportParamter());
                setReportParamter(new ReportTemplateParamEntity());
                applicationMessageBean.insertMessage("Report Parameter has been deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String addSubreportParameter() {
        if (!ultiSubreport.getAbstractParameterList().contains(getSubreportParamter())) {
            ultiSubreport.getAbstractParameterList().add(subreportParamter);
        } else {
            int index = getUltiSubreport().getAbstractParameterList().indexOf(getSubreportParamter());
            ultiSubreport.getAbstractParameterList().set(index, subreportParamter);
        }

        setSubreportParamter(new SubreportTemplateParamEntity());

        return "";
    }

    public String deleteSubreportParameter() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (getUltiSubreport().getAbstractParameterList().contains(getSubreportParamter())) {
                if (getSubreportParamter().isActivated()) {
                    applicationMessageBean.insertMessage("This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                    return "";
                }
                FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

                SubreportTemplateParamEntity criteria = new SubreportTemplateParamEntity();
                criteria.setParameterID(getReportParamter().getParameterID());
                dataServer.beginTransaction();
                dataServer.deleteData(criteria);
                dataServer.endTransaction();

                getUltiSubreport().getAbstractParameterList().remove(getSubreportParamter());
                setSubreportParamter(new SubreportTemplateParamEntity());
                applicationMessageBean.insertMessage("Subreport Parameter has been deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void reportParamSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        reportParamter = ultiReport.getAbstractParameterList().get(rowIndex);

        CommonBean.deselectOtherItems((AbstractEntity) reportParamter, ultiReport.getAbstractParameterList());
    }

    public void subreportParamSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        subreportParamter = (SubreportTemplateParamEntity) ultiSubreport.getAbstractParameterList().get(rowIndex);

        CommonBean.deselectOtherItems(subreportParamter, ultiSubreport.getAbstractParameterList());
    }

    public String moveToQueryPage() {
        FinultimateCommons.captureRequestingResource();
        return "/reports/reportqueryfilter.jsf";
    }

    public List<AbstractFieldMetaEntity> findReportFieldMetaList(List<AbstractEntity> baseURFMList, long reportID) {
        List<AbstractFieldMetaEntity> reportFieldMetaList = new ArrayList<AbstractFieldMetaEntity>();

        for (AbstractEntity baseURFM : baseURFMList) {
            AbstractFieldMetaEntity field = (AbstractFieldMetaEntity) baseURFM;

            //Note: the following is done on purpose
            field.setParameterTypeFQN(field.getParameterTypeFQN());

            if (((UlticoreReportFieldMetaEntity) field).getReportID() == reportID) {
                reportFieldMetaList.add(field);
            }
        }

        return reportFieldMetaList;
    }

    public List<QueryParameterEntity> findQueryFieldMetaList(List<AbstractEntity> baseRQPList, long reportID) {
        List<QueryParameterEntity> reportQueryFieldList = new ArrayList<QueryParameterEntity>();

        ValueListBean valueListBean = null;
        if (FacesContext.getCurrentInstance() != null) {
            valueListBean = (ValueListBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.valueListBean}", ValueListBean.class);
            if (valueListBean == null) {
                valueListBean = new ValueListBean();
                CommonBean.setBeanToContext("#{sessionScope.valueListBean}",
                        ValueListBean.class, valueListBean);
            }
        } else {
            valueListBean = (ValueListBean) ApplicationInitializer.APP_SERVLET_CONTEXT.getAttribute("valueListBean");
        }

        for (AbstractEntity baseRQP : baseRQPList) {
            QueryParameterEntity field = (QueryParameterEntity) baseRQP;
            //Note: the following is done on purpose
            field.setParameterTypeFQN(field.getParameterTypeFQN());

            if (((ReportQueryParameterEntity) field).getReportID() == reportID) {
                SelectItem item = valueListBean.findValueCategoryTypeItem(field.getParameterValueSourceID());
                if (item != null) {
                    field.setParameterValueSourceDesc(item.getLabel());
                }
                reportQueryFieldList.add(field);
            }
        }

        return reportQueryFieldList;
    }

    public List<AbstractTemplateParamEntity> findReportTemplateParamList(List<AbstractEntity> baseRTPList, long reportID) {
        List<AbstractTemplateParamEntity> reportTempParamList = new ArrayList<AbstractTemplateParamEntity>();

        UserManagerBean userManagerBean = null;
        if (FacesContext.getCurrentInstance() != null) {
            userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.userManagerBean}", UserManagerBean.class);
            if (userManagerBean == null) {
                userManagerBean = new UserManagerBean();
                CommonBean.setBeanToContext("#{sessionScope.userManagerBean}", UserManagerBean.class, userManagerBean);
            }
        } else {
            userManagerBean = (UserManagerBean) ApplicationInitializer.APP_SERVLET_CONTEXT.getAttribute("userManagerBean");
        }

        for (AbstractEntity baseRTP : baseRTPList) {
            AbstractTemplateParamEntity param = (AbstractTemplateParamEntity) baseRTP;
            if (((ReportTemplateParamEntity) param).getReportID() == reportID) {
                if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_DATE) {
                    param.setParameterValue(DateUtil.getCurrentDateStr());
                } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_TIMESTAMP) {
                    param.setParameterValue(DateUtil.getCurrentDateStr());
                } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_USER) {
                    param.setParameterValue(userManagerBean.getUserAccount().getUserName());
                }

                reportTempParamList.add(param);
            }
        }

        return reportTempParamList;
    }

    public List<UlticoreSubreportFieldMetaEntity> findSubreportFieldMetaList(List<AbstractEntity> baseURFMList, long reportID) {
        List<UlticoreSubreportFieldMetaEntity> reportFieldMetaList = new ArrayList<UlticoreSubreportFieldMetaEntity>();

        for (AbstractEntity baseURFM : baseURFMList) {
            UlticoreSubreportFieldMetaEntity field = (UlticoreSubreportFieldMetaEntity) baseURFM;

            //Note: the following is done on purpose
            field.setParameterTypeFQN(field.getParameterTypeFQN());

            if (field.getSubreportID() == reportID) {
                reportFieldMetaList.add(field);
            }
        }

        return reportFieldMetaList;
    }

    public List<SubreportQueryParameterEntity> findSubreportQueryFieldMetaList(List<AbstractEntity> baseRQPList, long reportID) {
        List<SubreportQueryParameterEntity> reportQueryFieldList = new ArrayList<SubreportQueryParameterEntity>();

        ValueListBean valueListBean = null;
        if (FacesContext.getCurrentInstance() != null) {
            valueListBean = (ValueListBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.valueListBean}", ValueListBean.class);
            if (valueListBean == null) {
                valueListBean = new ValueListBean();
                CommonBean.setBeanToContext("#{sessionScope.valueListBean}",
                        ValueListBean.class, valueListBean);
            }
        } else {
            valueListBean = (ValueListBean) ApplicationInitializer.APP_SERVLET_CONTEXT.getAttribute("valueListBean");
        }

        for (AbstractEntity baseRQP : baseRQPList) {
            SubreportQueryParameterEntity field = (SubreportQueryParameterEntity) baseRQP;

            //Note: the following is done on purpose
            field.setParameterTypeFQN(field.getParameterTypeFQN());

            if (field.getSubreportID() == reportID) {
                SelectItem item = valueListBean.findValueCategoryTypeItem(field.getParameterValueSourceID());
                if (item != null) {
                    field.setParameterValueSourceDesc(item.getLabel());
                }
                reportQueryFieldList.add(field);
            }
        }

        return reportQueryFieldList;
    }

    public List<SubreportTemplateParamEntity> findSubreportTemplateParamList(List<AbstractEntity> baseRTPList, long reportID) {
        List<SubreportTemplateParamEntity> reportTempParamList = new ArrayList<SubreportTemplateParamEntity>();

        UserManagerBean userManagerBean = null;
        if (FacesContext.getCurrentInstance() != null) {
            userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.userManagerBean}", UserManagerBean.class);
            if (userManagerBean == null) {
                userManagerBean = new UserManagerBean();
                CommonBean.setBeanToContext("#{sessionScope.userManagerBean}", UserManagerBean.class, userManagerBean);
            }
        } else {
            userManagerBean = (UserManagerBean) ApplicationInitializer.APP_SERVLET_CONTEXT.getAttribute("userManagerBean");
        }

        for (AbstractEntity baseRTP : baseRTPList) {
            SubreportTemplateParamEntity param = (SubreportTemplateParamEntity) baseRTP;
            if (param.getSubreportID() == reportID) {
                if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_DATE) {
                    param.setParameterValue(DateUtil.getCurrentDateStr());
                } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_TIMESTAMP) {
                    param.setParameterValue(DateUtil.getCurrentDateStr());
                } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_USER) {
                    param.setParameterValue(userManagerBean.getUserAccount().getUserName());
                }

                reportTempParamList.add(param);
            }
        }

        return reportTempParamList;
    }

    public void reportTemplateSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        ultiReport = ultiReportList.get(rowIndex);

        reportTemplateSelected(ultiReport);
    }

    public UlticoreReportEntity reportTemplateSelected(UlticoreReportEntity ultiReport) {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        businessActionTrail = new BusinessActionTrailEntity();

        try {
            AbstractFieldMetaEntity urfmCriteria = new UlticoreReportFieldMetaEntity();
            ((UlticoreReportFieldMetaEntity) urfmCriteria).setReportID(ultiReport.getReportID());
            List<AbstractEntity> baseURFMList = dataServer.findData((UlticoreReportFieldMetaEntity) urfmCriteria);

            QueryParameterEntity rqpCriteria = new ReportQueryParameterEntity();
            ((ReportQueryParameterEntity) rqpCriteria).setReportID(ultiReport.getReportID());
            List<AbstractEntity> baseRQPList = dataServer.findData((ReportQueryParameterEntity) rqpCriteria);

            AbstractTemplateParamEntity rtpCriteria = new ReportTemplateParamEntity();
            ((ReportTemplateParamEntity) rtpCriteria).setReportID(ultiReport.getReportID());
            List<AbstractEntity> baseRTPList = dataServer.findData((ReportTemplateParamEntity) rtpCriteria);

            if ((baseRQPList != null) && (baseRQPList.size() > 0)) {
                ultiReport.setQueryParamList(
                        findQueryFieldMetaList(baseRQPList, ultiReport.getReportID()));
            }

            if ((baseURFMList != null) && (baseURFMList.size() > 0)) {
                ultiReport.setAbstractFieldMetaList(
                        findReportFieldMetaList(baseURFMList, ultiReport.getReportID()));
            }

            if ((baseRTPList != null) && (baseRTPList.size() > 0)) {
                ultiReport.setAbstractParameterList(
                        findReportTemplateParamList(baseRTPList, ultiReport.getReportID()));
            }

            UlticoreSubreportEntity urCriteria = new UlticoreSubreportEntity();
            urCriteria.setReportID(ultiReport.getReportID());
            List<AbstractEntity> baseURList = dataServer.findData(urCriteria);

            if (baseURList.size() > 0) {
                ultiReport.setSubreportList(new ArrayList<UlticoreSubreportEntity>());

                for (AbstractEntity baseUR : baseURList) {
                    UlticoreSubreportEntity report = (UlticoreSubreportEntity) baseUR;
                    report.initializeTransients();

                    SelectItem item = new SelectItem();
                    item.setValue(report.getReportTitle());
                    item.setLabel(report.getReportTitle());
                    ultiReportItemList.add(item);

                    UlticoreSubreportFieldMetaEntity usfmCriteria = new UlticoreSubreportFieldMetaEntity();
                    usfmCriteria.setSubreportID(report.getSubreportID());
                    List<AbstractEntity> baseUSFMList = dataServer.findData(usfmCriteria);

                    SubreportQueryParameterEntity sqpCriteria = new SubreportQueryParameterEntity();
                    sqpCriteria.setSubreportID(report.getSubreportID());
                    List<AbstractEntity> baseSQPList = dataServer.findData(sqpCriteria);

                    SubreportTemplateParamEntity stpCriteria = new SubreportTemplateParamEntity();
                    stpCriteria.setSubreportID(report.getSubreportID());
                    List<AbstractEntity> baseSTPList = dataServer.findData(stpCriteria);

                    if ((baseSQPList != null) && (baseSQPList.size() > 0)) {
                        List<SubreportQueryParameterEntity> subReportParam = findSubreportQueryFieldMetaList(
                                baseSQPList, report.getSubreportID());
                        report.getQueryParamList().addAll(subReportParam);
                    }

                    if ((baseUSFMList != null) && (baseUSFMList.size() > 0)) {
                        report.getAbstractFieldMetaList().addAll(
                                findSubreportFieldMetaList(baseUSFMList, report.getSubreportID()));
                    }

                    if ((baseSTPList != null) && (baseSTPList.size() > 0)) {
                        report.getAbstractParameterList().addAll(
                                findSubreportTemplateParamList(baseSTPList, report.getSubreportID()));
                    }

                    ultiReport.getSubreportList().add(report);
                    hasSubreport = (ultiReport.getSubreportList().size() > 0);
                }
            }

            departmentSelectedForReportCreate(ultiReport.getDepartmentID());

            CommonBean.deselectOtherItems(ultiReport, ultiReportList);
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
        
        return ultiReport;
    }

    public void subreportTemplateSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        ultiSubreport = ultiReport.getSubreportList().get(rowIndex);

        CommonBean.deselectOtherItems(ultiSubreport, ultiReport.getSubreportList());
    }

    public void fieldMetaNameSelectedForQueryField(ValueChangeEvent vce) {
        getParameter().setParamterValue(vce.getNewValue().toString());
    }

    public void reportTemplateMenuSelected(ValueChangeEvent vce) {
        String reportTitle = vce.getNewValue().toString();
        if (!reportTitle.equals("-1")) {
            populateSelectedReportItemList(reportTitle);
        }
    }

    public void populateSelectedReportItemList(String reportTitle) {
        UlticoreReportEntity report = findReportByTitle(reportTitle);
        reportTemplateSelected(report);

        if (report != null) {
            selectedReportFieldItemList = new ArrayList<SelectItem>();

            for (AbstractFieldMetaEntity fieldMeta : report.getAbstractFieldMetaList()) {
                SelectItem item = new SelectItem();
                item.setValue(fieldMeta.getEntityFieldName());
                item.setLabel(fieldMeta.getEntityFieldName());

                selectedReportFieldItemList.add(item);
            }
        }
    }

    public void fieldValueRendererSelected(ValueChangeEvent vce) {
        String rendererVal = vce.getNewValue().toString();

        ValueListBean valueListBean = (ValueListBean) CommonBean.getBeanFromContext(
                "#{sessionScope.valueListBean}", ValueListBean.class);
        if (valueListBean == null) {
            valueListBean = new ValueListBean();
            CommonBean.setBeanToContext("#{sessionScope.valueListBean}",
                    ValueListBean.class, valueListBean);
        }

        SelectItem item = valueListBean.findValueCategoryTypeItem(rendererVal);
        if (item != null) {
            getParameter().setParameterValueSourceDesc(item.getLabel());
        }
    }

    public void parameterDataTypeSelected(ValueChangeEvent vce) {
        String rendererVal = vce.getNewValue().toString();
        getParameter().setParameterTypeFQN(rendererVal);
    }

    public void queryFieldSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        parameter = getUltiReport().getQueryParamList().get(rowIndex);

        CommonBean.deselectOtherItems((AbstractEntity) parameter, getUltiReport().getQueryParamList());
    }

    public void subreportQueryFieldSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        subreportParameter = (SubreportQueryParameterEntity) ultiSubreport.getQueryParamList().get(rowIndex);

        CommonBean.deselectOtherItems(subreportParameter, getUltiSubreport().getQueryParamList());
    }

    public void reportFieldSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        reportFieldMeta = (UlticoreReportFieldMetaEntity) ultiReport.getAbstractFieldMetaList().get(rowIndex);

        CommonBean.deselectOtherItems((AbstractEntity) reportFieldMeta, getUltiReport().getAbstractFieldMetaList());
    }

    public void subreportFieldSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        subreportFieldMeta = (UlticoreSubreportFieldMetaEntity) ultiSubreport.getAbstractFieldMetaList().get(rowIndex);

        CommonBean.deselectOtherItems(subreportFieldMeta, getUltiSubreport().getAbstractFieldMetaList());
    }

    public String moveToQueryFieldSetupPage() {
        FinultimateCommons.captureRequestingResource();
        return "/reports/queryfieldSetup.jsf";
    }

    public String moveToReportFieldSetupPage() {
        FinultimateCommons.captureRequestingResource();
        return "/reports/reportfieldSetup.jsf";
    }

    public String moveToReportParamSetupPage() {
        FinultimateCommons.captureRequestingResource();
        return "/reports/reportparamsetup.jsf";
    }

    public String moveToSubreportQueryFields() {
        FinultimateCommons.captureRequestingResource();
        return "subreportQueryfieldSetup";
    }

    public String moveToSubreportReportFields() {
        FinultimateCommons.captureRequestingResource();
        return "subreportfieldSetup";
    }

    public String moveToSubreportParameters() {
        FinultimateCommons.captureRequestingResource();
        return "subreportparamsetup";
    }

    public String backToSubreportPage() {
        return FinultimateCommons.retrieveLastRequestingResource();
    }

    public String addQueryField() {
        if (!getUltiReport().getQueryParamList().contains(getParameter())) {
            getUltiReport().getQueryParamList().add(getParameter());
        } else {
            int index = getUltiReport().getQueryParamList().indexOf(getParameter());
            getUltiReport().getQueryParamList().set(index, getParameter());
        }
        setParameter(new ReportQueryParameterEntity());

        return "";
    }

    public String addSubreportQueryField() {
        if (!getUltiSubreport().getQueryParamList().contains(getSubreportParameter())) {
            getUltiSubreport().getQueryParamList().add(getSubreportParameter());
        } else {
            int index = getUltiSubreport().getQueryParamList().indexOf(getSubreportParameter());
            getUltiSubreport().getQueryParamList().set(index, getSubreportParameter());
        }
        setSubreportParameter(new SubreportQueryParameterEntity());

        return "";
    }

    public String deleteQueryField() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (getUltiReport().getQueryParamList().contains(getParameter())) {
                FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

                QueryParameterEntity criteria = new ReportQueryParameterEntity();
                criteria.setQueryParameterID(getParameter().getQueryParameterID());
                dataServer.beginTransaction();
                dataServer.deleteData((AbstractEntity) criteria);
                dataServer.endTransaction();

                getUltiReport().getQueryParamList().remove(getParameter());
                setParameter(new ReportQueryParameterEntity());
                applicationMessageBean.insertMessage("Query Field has been deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String deleteSubreportQueryField() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (getUltiSubreport().getQueryParamList().contains(getSubreportParameter())) {
                if (getSubreportParameter().isActivated()) {
                    applicationMessageBean.insertMessage(
                            "This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                    return "";
                }
                FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

                SubreportQueryParameterEntity criteria = new SubreportQueryParameterEntity();
                criteria.setQueryParameterID(getSubreportParameter().getQueryParameterID());
                dataServer.beginTransaction();
                dataServer.deleteData(criteria);
                dataServer.endTransaction();

                getUltiSubreport().getQueryParamList().remove(getSubreportParameter());
                setSubreportParameter(new SubreportQueryParameterEntity());
                applicationMessageBean.insertMessage("Subreport Query Field has been deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String loadTableMeta() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();
        databaseTableFieldItemList = new ArrayList<SelectItem>();

        String queryString = "EXEC SP_COLUMNS " + ultiReport.getDatabaseTableName();
        try {
            List<String> fieldMetaList = dataServer.getFieldMetaNameList(
                    ultiReport.getTnsEntryKey(), queryString);

            for (String fieldMeta : fieldMetaList) {
                SelectItem item = new SelectItem();
                item.setValue(fieldMeta);
                item.setLabel(fieldMeta);

                getDatabaseTableFieldItemList().add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public String loadSubreportTableMeta() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();
        subreportDatabaseTableFieldItemList = new ArrayList<SelectItem>();

        String queryString = "EXEC SP_COLUMNS " + ultiSubreport.getDatabaseTableName();
        try {
            List<String> fieldMetaList = dataServer.getFieldMetaNameList(ultiSubreport.getTnsEntryKey(), queryString);

            for (String fieldMeta : fieldMetaList) {
                SelectItem item = new SelectItem();
                item.setValue(fieldMeta);
                item.setLabel(fieldMeta);

                subreportDatabaseTableFieldItemList.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public List<Object> findFieldValueList(String fieldName) {
        int columnCount = 0;

        for (AbstractFieldMetaEntity fieldMeta : getUltiReport().getAbstractFieldMetaList()) {
            if (fieldMeta.getEntityFieldName().equals(fieldName)) {
                break;
            }
            columnCount++;
        }

        return getFieldColumnValueList(getUltiReport(), columnCount);
    }

    public List<Object> getFieldColumnValueList(UlticoreReportEntity report, int columnPosition) {
        List<Object> colValueList = new ArrayList<Object>();
        for (List<Object> rowList : report.getObjectFieldList()) {
            colValueList.add(rowList.get(columnPosition));
        }

        return colValueList;
    }

    public UlticoreReportEntity findReportByTitle(String reportTitle) {
        UlticoreReportEntity report = null;

        for (UlticoreReportEntity rept : getUltiReportList()) {
            if (rept.getReportTitle().equals(reportTitle)) {
                report = rept;
                break;
            }
        }

        return report;
    }

    public SelectItem findTableMetaItem(String itemVal) {
        SelectItem item = null;

        for (SelectItem itm : getDatabaseTableFieldItemList()) {
            if (itm.getValue().toString().equals(itemVal)) {
                item = itm;
                break;
            }
        }

        return item;
    }

    public String addReportField() {
        if (!getUltiReport().getAbstractFieldMetaList().contains(reportFieldMeta)) {
            getUltiReport().getAbstractFieldMetaList().add(reportFieldMeta);
        } else {
            int index = getUltiReport().getAbstractFieldMetaList().indexOf(reportFieldMeta);
            getUltiReport().getAbstractFieldMetaList().set(index, reportFieldMeta);
        }
        reportFieldMeta = new UlticoreReportFieldMetaEntity();

        return "";
    }

    public String deleteReportField() {
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
            AbstractFieldMetaEntity criteria = new UlticoreReportFieldMetaEntity();
            criteria.setEntityMetaNameID(reportFieldMeta.getEntityMetaNameID());
            if (reportFieldMeta.isActivated()) {
                applicationMessageBean.insertMessage(
                        "This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                return "";
            }

            dataServer.beginTransaction();
            dataServer.deleteData((AbstractEntity) criteria);
            dataServer.endTransaction();

            if (getUltiReport().getAbstractFieldMetaList().contains(reportFieldMeta)) {
                getUltiReport().getAbstractFieldMetaList().remove(reportFieldMeta);
            }
            reportFieldMeta = new UlticoreReportFieldMetaEntity();
            applicationMessageBean.insertMessage("Report Field has been deleted", MessageType.SUCCESS_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String addSubreportField() {
        if (!getUltiSubreport().getAbstractFieldMetaList().contains(subreportFieldMeta)) {
            getUltiSubreport().getAbstractFieldMetaList().add(subreportFieldMeta);
        } else {
            int index = getUltiSubreport().getAbstractFieldMetaList().indexOf(subreportFieldMeta);
            getUltiSubreport().getAbstractFieldMetaList().set(index, subreportFieldMeta);
        }
        subreportFieldMeta = new UlticoreSubreportFieldMetaEntity();

        return "";
    }

    public String deleteSubreportField() {
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
            if (subreportFieldMeta.isActivated()) {
                applicationMessageBean.insertMessage(
                        "This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                return "";
            }
            UlticoreSubreportFieldMetaEntity criteria = new UlticoreSubreportFieldMetaEntity();
            criteria.setEntityMetaNameID(subreportFieldMeta.getEntityMetaNameID());

            dataServer.beginTransaction();
            dataServer.deleteData(criteria);
            dataServer.endTransaction();

            if (getUltiSubreport().getAbstractFieldMetaList().contains(subreportFieldMeta)) {
                getUltiSubreport().getAbstractFieldMetaList().remove(subreportFieldMeta);
            }
            subreportFieldMeta = new UlticoreSubreportFieldMetaEntity();
            applicationMessageBean.insertMessage("Subreport Field has been deleted", MessageType.SUCCESS_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String addReportTemplate() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (ultiReport.getQueryExpression().split(" ").length == 1) {
            if (!ultiReportList.contains(ultiReport)) {
                ultiReportList.add(ultiReport);
            } else {
                int index = ultiReportList.indexOf(ultiReport);
                ultiReportList.set(index, ultiReport);
            }
            ultiReport = new UlticoreReportEntity();
        } else if (ultiReport.getQueryExpression().split(" ").length > 1) {
            applicationMessageBean.insertMessage(
                    "A Procedure/Function Name cannot have spaces.", MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String cloneReportTemplate() {
        UlticoreReportEntity entity = getUltiReport().cloneEntity();
        entity.setReportID(0);
        entity.setSubreportList(cloneSubreportTemplateList(entity));

        getUltiReportList().add(entity);
        setUltiReport(new UlticoreReportEntity());

        return "";
    }

    public String moveToSubreportPage() {
        FinultimateCommons.captureRequestingResource();
        return "subreportconfigurer";
    }

    public String addSubreportTemplate() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        boolean permissible = false;
        if (ultiSubreport.getSqlStatementType() == null) {
            ultiSubreport.setSqlStatementType(SQLStatementType.CALL_STATEMENT);
            permissible = true;
        } else if (ultiSubreport.getSqlStatementType() == SQLStatementType.CALL_STATEMENT) {
            permissible = true;
        } else if (ultiSubreport.getSqlStatementType() == SQLStatementType.SELECT_STATEMENT) {
            permissible = false;
        }

        if (permissible) {
            if (ultiSubreport.getQueryExpression().split(" ").length == 1) {
                if (!ultiReport.getSubreportList().contains(ultiSubreport)) {
                    ultiReport.getSubreportList().add(ultiSubreport);
                } else {
                    int index = ultiReport.getSubreportList().indexOf(ultiSubreport);
                    ultiReport.getSubreportList().set(index, ultiSubreport);
                }

                ultiSubreport = new UlticoreSubreportEntity();
            } else if (ultiSubreport.getQueryExpression().split(" ").length > 1) {
                applicationMessageBean.insertMessage(
                        "A Procedure/Function Name cannot have spaces.", MessageType.ERROR_MESSAGE);
            }
        } else {
            applicationMessageBean.insertMessage(
                    "The Only Allowable SQL Statements In ASIRA are Callable Statements. "
                    + "Please Alter Your Definitions Accordingly.", MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public List<UlticoreSubreportEntity> cloneSubreportTemplateList(UlticoreReportEntity report) {
        List<UlticoreSubreportEntity> subreportList = new ArrayList<UlticoreSubreportEntity>();

        for (UlticoreSubreportEntity subreport : report.getSubreportList()) {
            subreport.setSubreportID(0);
            subreportList.add(subreport);
        }

        return subreportList;
    }

    public String backToMainReportPage() {
        return FinultimateCommons.retrieveLastRequestingResource();
    }

    public String deleteReportTemplate() {
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
            if (getUltiReportList().contains(getUltiReport())) {
                if (getUltiReport().isActivated()) {
                    applicationMessageBean.insertMessage("This Item has been Activated. Deletion of Activated Items is not allowed", MessageType.ERROR_MESSAGE);
                    return "";
                }
                dataServer.beginTransaction();
                for (QueryParameterEntity rqpEntity : ultiReport.getQueryParamList()) {
                    QueryParameterEntity criteria = new ReportQueryParameterEntity();
                    criteria.setQueryParameterID(rqpEntity.getQueryParameterID());
                    dataServer.deleteData((ReportQueryParameterEntity) criteria);
                }
                parameter = new ReportQueryParameterEntity();

                for (AbstractFieldMetaEntity urfmEntity : ultiReport.getAbstractFieldMetaList()) {
                    AbstractFieldMetaEntity criteria = new UlticoreReportFieldMetaEntity();
                    criteria.setEntityMetaNameID(urfmEntity.getEntityMetaNameID());

                    dataServer.deleteData((UlticoreReportFieldMetaEntity) criteria);
                }
                reportFieldMeta = new UlticoreReportFieldMetaEntity();

                for (AbstractTemplateParamEntity rtpEntity : ultiReport.getAbstractParameterList()) {
                    AbstractTemplateParamEntity criteria = new ReportTemplateParamEntity();
                    criteria.setParameterID(rtpEntity.getParameterID());
                    dataServer.deleteData((ReportTemplateParamEntity) criteria);
                }
                setReportParamter(new ReportTemplateParamEntity());

                UlticoreReportEntity rptCriteria = new UlticoreReportEntity();
                rptCriteria.setReportID(ultiReport.getReportID());

                dataServer.deleteData(rptCriteria);
                dataServer.endTransaction();

                ultiReportList.remove(ultiReport);
                ultiReport = new UlticoreReportEntity();
                applicationMessageBean.insertMessage("Report Template has been deleted", MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String saveReportTemplateList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

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

        for (UlticoreReportEntity uReport : getUltiReportList()) {
            if (uReport.getReportID() == 0) {
                try {
                    int cmID = Integer.parseInt(appPropBean.getValue(
                            REPORT_TEMPLATE_ID_KEY,
                            REPORT_TEMPLATE_ID_DEFAULT, true));
                    uReport.setReportID(cmID);

                    for (QueryParameterEntity param : uReport.getQueryParamList()) {
                        int paramID = Integer.parseInt(appPropBean.getValue(
                                QUERY_FIELD_ID_KEY,
                                QUERY_FIELD_ID_DEFAULT, true));
                        param.setQueryParameterID(paramID);
                        ((ReportQueryParameterEntity) param).setReportID(uReport.getReportID());

                        dataServer.saveData((ReportQueryParameterEntity) param);
                    }

                    for (AbstractFieldMetaEntity field : uReport.getAbstractFieldMetaList()) {
                        int fieldID = Integer.parseInt(appPropBean.getValue(
                                REPORT_FIELD_ID_KEY,
                                REPORT_FIELD_ID_DEFAULT, true));
                        field.setEntityMetaNameID(fieldID);
                        ((UlticoreReportFieldMetaEntity) field).setReportID(uReport.getReportID());

                        dataServer.saveData((UlticoreReportFieldMetaEntity) field);
                    }

                    for (AbstractTemplateParamEntity param : uReport.getAbstractParameterList()) {
                        int paramID = Integer.parseInt(appPropBean.getValue(
                                REPORT_PARAM_ID_KEY,
                                REPORT_PARAM_ID_DEFAULT, true));
                        param.setParameterID(paramID);
                        ((ReportTemplateParamEntity) param).setReportID(uReport.getReportID());

                        dataServer.saveData((ReportTemplateParamEntity) param);
                    }
                    saveSubreportTemplateList(uReport, dataServer);

                    dataServer.saveData(uReport);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    for (QueryParameterEntity param : uReport.getQueryParamList()) {
                        if (param.getQueryParameterID() == 0) {
                            int paramID = Integer.parseInt(appPropBean.getValue(
                                    QUERY_FIELD_ID_KEY,
                                    QUERY_FIELD_ID_DEFAULT, true));
                            param.setQueryParameterID(paramID);
                            ((ReportQueryParameterEntity) param).setReportID(uReport.getReportID());

                            dataServer.saveData((ReportQueryParameterEntity) param);
                        } else {
                            dataServer.updateData((ReportQueryParameterEntity) param);
                        }
                    }

                    for (AbstractFieldMetaEntity field : uReport.getAbstractFieldMetaList()) {
                        if (field.getEntityMetaNameID() == 0) {
                            int fieldID = Integer.parseInt(appPropBean.getValue(
                                    REPORT_FIELD_ID_KEY,
                                    REPORT_FIELD_ID_DEFAULT, true));

                            field.setEntityMetaNameID(fieldID);
                            ((UlticoreReportFieldMetaEntity) field).setReportID(uReport.getReportID());

                            dataServer.saveData((UlticoreReportFieldMetaEntity) field);
                        } else {
                            dataServer.updateData((UlticoreReportFieldMetaEntity) field);
                        }
                    }

                    for (AbstractTemplateParamEntity param : uReport.getAbstractParameterList()) {
                        if (param.getParameterID() == 0) {
                            int paramID = Integer.parseInt(appPropBean.getValue(
                                    REPORT_PARAM_ID_KEY,
                                    REPORT_PARAM_ID_DEFAULT, true));
                            param.setParameterID(paramID);
                            ((ReportTemplateParamEntity) param).setReportID(uReport.getReportID());

                            dataServer.saveData((ReportTemplateParamEntity) param);
                        } else {
                            dataServer.updateData((ReportTemplateParamEntity) param);
                        }
                    }
                    saveSubreportTemplateList(uReport, dataServer);

                    dataServer.updateData(uReport);

                    dataServer.endTransaction();
                    applicationMessageBean.insertMessage("Report Template has been saved", MessageType.SUCCESS_MESSAGE);
                } catch (Exception ex) {
                    dataServer.rollbackTransaction();
                    ex.printStackTrace();
                    applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                }
            }
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

        PrivilegeBean privilegeBean = (PrivilegeBean) CommonBean.getBeanFromContext(
                "#{sessionScope.privilegeBean}", PrivilegeBean.class);
        if (privilegeBean == null) {
            privilegeBean = new PrivilegeBean();
            CommonBean.setBeanToContext("#{sessionScope.privilegeBean}", PrivilegeBean.class, privilegeBean);
        }

        MenuEntity privilege = privilegeBean.findPrivilegeByName(
                menuManagerBean.getSystemMap().get(MenuManagerBean.SETP_RPTS_MENU_ITEM));

        if (ultiReport.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((ultiReport.getApprovalStatusID() == 0) || (ultiReport.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(ultiReport.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        ultiReport.setApprovalStatusID(ultiReport.getApprovalStatusID() + 1);
                        ultiReport.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(ultiReport);
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
                } else if (ultiReport.getApprovalStatusID()
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

    public String saveSubreportTemplateList(
            UlticoreReportEntity report,
            FinultimatePersistenceRemote dataServer) {
        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }

        for (UlticoreSubreportEntity subreport : report.getSubreportList()) {
            if (subreport.getSubreportID() == 0) {
                subreport.setReportID(report.getReportID());

                try {
                    int cmID = Integer.parseInt(appPropBean.getValue(
                            SUBREPORT_TEMPLATE_ID_KEY,
                            SUBREPORT_TEMPLATE_ID_DEFAULT, true));
                    subreport.setSubreportID(cmID);

                    for (QueryParameterEntity param : subreport.getQueryParamList()) {
                        int paramID = Integer.parseInt(appPropBean.getValue(
                                SUBREPORT_QUERY_FIELD_ID_KEY,
                                SUBREPORT_QUERY_FIELD_ID_DEFAULT, true));

                        param.setQueryParameterID(paramID);
                        ((SubreportQueryParameterEntity) param).setSubreportID(subreport.getSubreportID());

                        dataServer.saveData((SubreportQueryParameterEntity) param);
                    }

                    for (AbstractFieldMetaEntity field : subreport.getAbstractFieldMetaList()) {
                        int fieldID = Integer.parseInt(appPropBean.getValue(
                                SUBREPORT_FIELD_ID_KEY,
                                SUBREPORT_FIELD_ID_DEFAULT, true));

                        field.setEntityMetaNameID(fieldID);
                        ((UlticoreSubreportFieldMetaEntity) field).setSubreportID(subreport.getSubreportID());

                        dataServer.saveData((UlticoreSubreportFieldMetaEntity) field);
                    }

                    for (AbstractTemplateParamEntity param : subreport.getAbstractParameterList()) {
                        int paramID = Integer.parseInt(appPropBean.getValue(
                                SUBREPORT_PARAM_ID_KEY,
                                SUBREPORT_PARAM_ID_DEFAULT, true));

                        param.setParameterID(paramID);
                        ((SubreportTemplateParamEntity) param).setSubreportID(subreport.getSubreportID());

                        dataServer.saveData((SubreportTemplateParamEntity) param);
                    }

                    dataServer.saveData(subreport);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                try {
                    for (QueryParameterEntity param : subreport.getQueryParamList()) {
                        if (param.getQueryParameterID() == 0) {
                            int paramID = Integer.parseInt(appPropBean.getValue(
                                    SUBREPORT_QUERY_FIELD_ID_KEY,
                                    SUBREPORT_QUERY_FIELD_ID_DEFAULT, true));

                            param.setQueryParameterID(paramID);
                            ((SubreportQueryParameterEntity) param).setSubreportID(subreport.getSubreportID());

                            dataServer.saveData((SubreportQueryParameterEntity) param);
                        } else {
                            dataServer.updateData((SubreportQueryParameterEntity) param);
                        }
                    }

                    for (AbstractFieldMetaEntity field : subreport.getAbstractFieldMetaList()) {
                        if (field.getEntityMetaNameID() == 0) {
                            int fieldID = Integer.parseInt(appPropBean.getValue(
                                    SUBREPORT_FIELD_ID_KEY,
                                    SUBREPORT_FIELD_ID_DEFAULT, true));

                            field.setEntityMetaNameID(fieldID);
                            ((UlticoreSubreportFieldMetaEntity) field).setSubreportID(subreport.getSubreportID());

                            dataServer.saveData((UlticoreSubreportFieldMetaEntity) field);
                        } else {
                            dataServer.updateData((UlticoreSubreportFieldMetaEntity) field);
                        }
                    }

                    for (AbstractTemplateParamEntity param : subreport.getAbstractParameterList()) {
                        if (param.getParameterID() == 0) {
                            int paramID = Integer.parseInt(appPropBean.getValue(
                                    SUBREPORT_PARAM_ID_KEY,
                                    SUBREPORT_PARAM_ID_DEFAULT, true));

                            param.setParameterID(paramID);
                            ((SubreportTemplateParamEntity) param).setSubreportID(subreport.getSubreportID());

                            dataServer.saveData((SubreportTemplateParamEntity) param);
                        } else {
                            dataServer.updateData((SubreportTemplateParamEntity) param);
                        }
                    }

                    dataServer.updateData(subreport);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return "";
    }

    public String filterQueryAndLoadReport() {
        String outcome = "";

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();
        getUltiReport().setObjectFieldList(new ArrayList<List<Object>>());

        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}", UserManagerBean.class, userManagerBean);
        }

        UlticoreReportTableCreator ulticoreReportTableCreator = (UlticoreReportTableCreator) CommonBean.getBeanFromContext(
                "#{sessionScope.ulticoreReportTableCreator}", UlticoreReportTableCreator.class);
        if (ulticoreReportTableCreator == null) {
            ulticoreReportTableCreator = new UlticoreReportTableCreator();
            CommonBean.setBeanToContext("#{sessionScope.ulticoreReportTableCreator}",
                    UlticoreReportTableCreator.class, ulticoreReportTableCreator);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        if (ultiReport.getSqlStatementType() == SQLStatementType.CALL_STATEMENT) {
            List<AbstractQueryParameter> qpEntityList = new ArrayList<AbstractQueryParameter>();
            for (QueryParameterEntity qpEntity : ultiReport.getQueryParamList()) {
                AbstractQueryParameter rqParam = new AbstractQueryParameter();

                rqParam.setOperator(qpEntity.getOperator());
                rqParam.setParameterDefltValue(qpEntity.getParameterDefltValue());
                rqParam.setParameterName(qpEntity.getParameterName());
                rqParam.setParameterPosition(qpEntity.getParameterPosition());

                rqParam.setParameterPredeterminedValue(qpEntity.getParameterPredeterminedValue());
                rqParam.setParameterValueSourceDesc(qpEntity.getParameterValueSourceDesc());
                rqParam.setParameterValueSourceID(qpEntity.getParameterValueSourceID());
                rqParam.setParamterValue(qpEntity.getParamterValue());

                qpEntityList.add(rqParam);
            }

            try {
                List<List<AbstractDataField>> fieldDataMatrix = null;
                if (qpEntityList.size() > 0) {
                    dataServer.setSelectedTnsName(Constants.COMMONS_PU);
                    fieldDataMatrix = dataServer.findNonORMData(
                            ultiReport, ultiReport.getAbstractFieldMetaList(), qpEntityList, CommonBean.SQL_STYLE);
                }

                ultiReport.setObjectFieldList(new ArrayList<List<Object>>());
                if ((fieldDataMatrix != null) && (fieldDataMatrix.size() > 0)) {
                    for (List<AbstractDataField> fieldList : fieldDataMatrix) {
                        List<Object> reportFieldList = new ArrayList<Object>();
                        for (AbstractDataField field : fieldList) {
                            AbstractFieldMetaEntity fieldMeta = ultiReport.findReportFieldMetaByDBField(field.getFieldName());
                            if (fieldMeta != null) {
                                if ((field.getFieldValue() instanceof String) && (field.getFieldValue().toString().contains("&"))) {
                                    field.setFieldValue(field.getFieldValue().toString().replaceAll("&", "&amp;"));
                                }
                                ultiReport.getParameters().put(fieldMeta.getEntityFieldName(), field.getFieldValue());

                                if ((field.getFieldValue() instanceof Float)
                                        || (field.getFieldValue() instanceof Double)) {
                                    if (field.getFieldValue() instanceof Float) {
                                        reportFieldList.add(BigDecimal.valueOf((Float) field.getFieldValue()));
                                    } else {
                                        reportFieldList.add(BigDecimal.valueOf((Double) field.getFieldValue()));
                                    }
                                } else {
                                    reportFieldList.add(field.getFieldValue());
                                }
                            }
                        }
                        ultiReport.getObjectFieldList().add(reportFieldList);
                    }

                    ultiReport = filterQueryAndLoadReport(ultiReport);

                    ulticoreReportTableCreator.setBizObjectData(getUltiReport());
                    ulticoreReportTableCreator.createDynamicColumns();

                    FacesContext context = FacesContext.getCurrentInstance();
                    servletSession = (HttpSession) context.getExternalContext().getSession(false);

                    servletSession.setAttribute(ultiReport.getReportTitle(), ultiReport);
                    servletSession.setAttribute(
                            ReportAttributes.REPORT_KEY_PARAM, ultiReport.getReportTitle());

                    servletSession.setAttribute(
                            ReportAttributes.PRINT_TYPE, ReportAttributes.PAGE_OUTPUT_PRINT);

                    outcome = "reportqueryresult";
                } else {
                    ultiReport = filterQueryAndLoadReport(ultiReport);

                    ulticoreReportTableCreator.setBizObjectData(getUltiReport());
                    ulticoreReportTableCreator.createDynamicColumns();

                    FacesContext context = FacesContext.getCurrentInstance();
                    servletSession = (HttpSession) context.getExternalContext().getSession(false);

                    servletSession.setAttribute(getUltiReport().getReportTitle(), getUltiReport());
                    servletSession.setAttribute(
                            ReportAttributes.REPORT_KEY_PARAM, getUltiReport().getReportTitle());

                    servletSession.setAttribute(
                            ReportAttributes.PRINT_TYPE, ReportAttributes.PAGE_OUTPUT_PRINT);

                    outcome = "reportqueryresult";
                }

                for (AbstractTemplateParamEntity templateParam : ultiReport.getAbstractParameterList()) {
                    if ((templateParam.getParameterValue() != null)
                            && (!templateParam.getParameterValue().trim().equals(""))) {
                        ultiReport.getParameters().put(
                                templateParam.getParameterName(), templateParam.getParameterValue());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            }
        } else {
            applicationMessageBean.insertMessage(
                    "Error On Report Template '" + ultiReport.getReportTitle() + "': "
                    + "The Only Allowable SQL Statements In ASIRA are Callable Statements. "
                    + "Please Alter Your Definitions Accordingly.", MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    public UlticoreReportEntity filterQueryAndLoadReport(UlticoreReportEntity report) throws Exception {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}",
                    UserManagerBean.class, userManagerBean);
        }

        List<UlticoreSubreportEntity> tempSubreportList = new ArrayList<UlticoreSubreportEntity>();
        for (UlticoreSubreportEntity subreport : report.getSubreportList()) {
            if (subreport.getSqlStatementType() == SQLStatementType.SELECT_STATEMENT) {
                throw new Exception("Error On Subreport Template '" + subreport.getReportTitle() + "': "
                        + "The Only Allowable SQL Statements In ASIRA are Callable Statements. "
                        + "Please Alter Your Definitions Accordingly.");
            }

            List<AbstractQueryParameter> qpEntityList = new ArrayList<AbstractQueryParameter>();
            for (QueryParameterEntity qpEntity : subreport.getQueryParamList()) {
                AbstractQueryParameter rqParam = new AbstractQueryParameter();

                rqParam.setOperator(qpEntity.getOperator());
                rqParam.setParameterDefltValue(qpEntity.getParameterDefltValue());
                rqParam.setParameterName(qpEntity.getParameterName());
                rqParam.setParameterPosition(qpEntity.getParameterPosition());
                rqParam.setParameterPredeterminedValue(qpEntity.getParameterPredeterminedValue());
                rqParam.setParameterValueSourceDesc(qpEntity.getParameterValueSourceDesc());
                rqParam.setParameterValueSourceID(qpEntity.getParameterValueSourceID());
                rqParam.setParamterValue(qpEntity.getParamterValue());

                qpEntityList.add(rqParam);
            }

            dataServer.setSelectedTnsName(Constants.COMMONS_PU);
            List<List<AbstractDataField>> fieldDataMatrix = dataServer.findNonORMData(
                    subreport, subreport.getAbstractFieldMetaList(), qpEntityList, CommonBean.SQL_STYLE);

            List<String> columnNameList = new ArrayList<String>();
            List<List<String>> columnDataTable = new ArrayList<List<String>>();

            if ((fieldDataMatrix != null) && (fieldDataMatrix.size() > 0)) {
                int i = 0;
                for (List<AbstractDataField> fieldList : fieldDataMatrix) {
                    List<String> columnDataRow = new ArrayList<String>();

                    for (AbstractDataField field : fieldList) {
                        AbstractFieldMetaEntity fieldMeta = subreport.findReportFieldMetaByDBField(field.getFieldName());
                        if (fieldMeta != null) {
                            if (i == 0) {
                                columnNameList.add(fieldMeta.getEntityFieldName());
                            }

                            if (field.getFieldValue() != null) {
                                if ((field.getFieldValue() instanceof String)
                                        && (field.getFieldValue().toString().contains("&"))) {
                                    field.setFieldValue(field.getFieldValue().toString().replaceAll("&", "&amp;"));
                                }
                                columnDataRow.add(field.getFieldValue().toString().trim());
                            } else {
                                columnDataRow.add("");
                            }
                        }
                    }

                    columnDataTable.add(columnDataRow);
                    i++;
                }
                DDTSurrogate tableData = new DDTSurrogate();

                tableData.setColumnName(columnNameList.toArray(new String[columnNameList.size()]));
                tableData.setData(new String[columnDataTable.size()][columnNameList.size()]);

                i = 0;
                for (List<String> tupleData : columnDataTable) {
                    tableData.getData()[i++] = tupleData.toArray(new String[tupleData.size()]);
                }

                report.getParameters().put(subreport.getReportTitle(), tableData);
            }

            tempSubreportList.add(subreport);
        }

        report.setSubreportList(tempSubreportList);

        return report;
    }

    public UlticoreReportEntity constructSubreportGraph(UlticoreReportEntity report) {
        List<UlticoreSubreportEntity> tempSubreportList = new ArrayList<UlticoreSubreportEntity>();
        for (UlticoreSubreportEntity subreport : report.getSubreportList()) {
            List<String> columnNameList = new ArrayList<String>();
            List<List<String>> columnDataTable = new ArrayList<List<String>>();

            int i = 0;
            for (List<Object> fieldList : subreport.getObjectFieldList()) {
                List<String> columnDataRow = new ArrayList<String>();

                int columnIndex = 0;
                for (Object field : fieldList) {
                    AbstractFieldMetaEntity fieldMeta = subreport.getAbstractFieldMetaList().get(columnIndex++);
                    if (fieldMeta != null) {
                        if (i == 0) {
                            columnNameList.add(fieldMeta.getEntityFieldName());
                        }

                        if (field != null) {
                            if ((field instanceof String)
                                    && (field.toString().contains("&"))) {
                                field = field.toString().replaceAll("&", "&amp;");
                            }
                            columnDataRow.add(field.toString().trim());
                        } else {
                            columnDataRow.add("");
                        }
                    }
                }

                columnDataTable.add(columnDataRow);
                i++;
            }
            DDTSurrogate tableData = new DDTSurrogate();

            tableData.setColumnName(columnNameList.toArray(new String[columnNameList.size()]));
            tableData.setData(new String[columnDataTable.size()][columnNameList.size()]);

            i = 0;
            for (List<String> tupleData : columnDataTable) {
                tableData.getData()[i++] = tupleData.toArray(new String[tupleData.size()]);
            }

            report.getParameters().put(subreport.getReportTitle(), tableData);

            tempSubreportList.add(subreport);
        }

        report.setSubreportList(tempSubreportList);

        return report;
    }

    public boolean hasField(List<AbstractDataField> fieldList, String fieldName) {
        boolean found = false;

        for (AbstractDataField field : fieldList) {
            if (field.getFieldName().trim().equals(fieldName)) {
                found = true;
                break;
            }
        }

        return found;
    }

    public String queryAndLoadReport() {
        FinultimateCommons.captureRequestingResource();

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();
        getUltiReport().setObjectFieldList(new ArrayList<List<Object>>());

        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}", UserManagerBean.class, userManagerBean);
        }

        UlticoreReportTableCreator ulticoreReportTableCreator = (UlticoreReportTableCreator) CommonBean.getBeanFromContext(
                "#{sessionScope.ulticoreReportTableCreator}", UlticoreReportTableCreator.class);
        if (ulticoreReportTableCreator == null) {
            ulticoreReportTableCreator = new UlticoreReportTableCreator();
            CommonBean.setBeanToContext("#{sessionScope.ulticoreReportTableCreator}", UlticoreReportTableCreator.class, ulticoreReportTableCreator);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }

        List<AbstractQueryParameter> qpEntityList = new ArrayList<AbstractQueryParameter>();
        for (QueryParameterEntity qpEntity : ultiReport.getQueryParamList()) {
            AbstractQueryParameter rqParam = new AbstractQueryParameter();

            rqParam.setOperator(qpEntity.getOperator());
            rqParam.setParameterDefltValue(qpEntity.getParameterDefltValue());
            rqParam.setParameterName(qpEntity.getParameterName());
            rqParam.setParameterPosition(qpEntity.getParameterPosition());
            rqParam.setParameterPredeterminedValue(qpEntity.getParameterPredeterminedValue());
            rqParam.setParameterValueSourceDesc(qpEntity.getParameterValueSourceDesc());
            rqParam.setParameterValueSourceID(qpEntity.getParameterValueSourceID());
            rqParam.setParamterValue(qpEntity.getParamterValue());

            qpEntityList.add(rqParam);
        }

        try {
            if (ultiReport.getAbstractFieldMetaList().size() > 0) {
                List<List<AbstractDataField>> fieldDataMatrix = dataServer.findNonORMData(
                        ultiReport, ultiReport.getAbstractFieldMetaList(), qpEntityList, CommonBean.SQL_STYLE);

                if ((fieldDataMatrix != null) && (fieldDataMatrix.size() > 0)) {
                    for (List<AbstractDataField> fieldList : fieldDataMatrix) {
                        List<Object> reportFieldList = new ArrayList<Object>();
                        for (AbstractDataField field : fieldList) {
                            AbstractFieldMetaEntity fieldMeta = ultiReport.findReportFieldMetaByDBField(field.getFieldName());
                            if (fieldMeta != null) {
                                ultiReport.getParameters().put(fieldMeta.getEntityFieldName(), field.getFieldValue());

                                if ((field.getFieldValue() instanceof Float)
                                        || (field.getFieldValue() instanceof Double)) {
                                    if (field.getFieldValue() instanceof Float) {
                                        reportFieldList.add(BigDecimal.valueOf((Float) field.getFieldValue()));
                                    } else {
                                        reportFieldList.add(BigDecimal.valueOf((Double) field.getFieldValue()));
                                    }
                                } else {
                                    reportFieldList.add(field.getFieldValue());
                                }
                            }
                        }
                        ultiReport.getObjectFieldList().add(reportFieldList);
                    }

                    for (AbstractTemplateParamEntity templateParam : ultiReport.getAbstractParameterList()) {
                        if ((templateParam.getParameterValue() != null)
                                && (!templateParam.getParameterValue().trim().equals(""))) {
                            ultiReport.getParameters().put(templateParam.getParameterName(), templateParam.getParameterValue());
                        }
                    }

                    if (getUltiReport().getObjectFieldList().size() > 0) {
                        for (AbstractTemplateParamEntity param : getUltiReport().getAbstractParameterList()) {
                            if (param.getDefaultValue() == DefaultSystemGeneratedFields.NONE) {
                                getUltiReport().getParameters().put(param.getParameterName(), param.getParameterValue());
                            } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_DATE) {
                                if (param.getDataType() == AbstractTemplateDataType.DATE_TYPE) {
                                    getUltiReport().getParameters().put(param.getParameterName(), new Date());
                                } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                                    getUltiReport().getParameters().put(param.getParameterName(), DateUtil.getCurrentDateStr());
                                }
                            } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_TIMESTAMP) {
                                if (param.getDataType() == AbstractTemplateDataType.DATE_TYPE) {
                                    getUltiReport().getParameters().put(param.getParameterName(), new Timestamp(0));
                                } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                                    getUltiReport().getParameters().put(param.getParameterName(), DateUtil.getCurrentDateStr());
                                }
                            } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_USER) {
                                if (userManagerBean != null) {
                                    getUltiReport().getParameters().put(param.getParameterName(), userManagerBean.getUserAccount().getUserName());
                                } else {
                                    getUltiReport().getParameters().put(param.getParameterName(), "System");
                                }
                            } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.INCREMENTAL_COUNT) {
                                try {
                                    int fieldID = Integer.parseInt(appPropBean.getValue(param.getParameterName(),
                                            REPORT_FIELD_ID_DEFAULT, true));
                                    if (param.getDataType() == AbstractTemplateDataType.INTEGER_TYPE) {
                                        getUltiReport().getParameters().put(param.getParameterName(), fieldID);
                                    } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                                        getUltiReport().getParameters().put(param.getParameterName(), String.valueOf(fieldID));
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.WEEK_OF_YEAR) {
                                if (param.getDataType() == AbstractTemplateDataType.INTEGER_TYPE) {
                                    getUltiReport().getParameters().put(param.getParameterName(), DateUtil.getWeekOfYear(new Date()));
                                } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                                    getUltiReport().getParameters().put(param.getParameterName(), String.valueOf(DateUtil.getWeekOfYear(new Date())));
                                }
                            } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.DAY_OF_YEAR) {
                                if (param.getDataType() == AbstractTemplateDataType.INTEGER_TYPE) {
                                    getUltiReport().getParameters().put(param.getParameterName(), DateUtil.getDayOfYear(new Date()));
                                } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                                    getUltiReport().getParameters().put(param.getParameterName(), String.valueOf(DateUtil.getDayOfYear(new Date())));
                                }
                            }
                        }

                        ulticoreReportTableCreator.setBizObjectData(getUltiReport());
                        ulticoreReportTableCreator.createDynamicColumns();

                        FacesContext context = FacesContext.getCurrentInstance();
                        setServletSession((HttpSession) context.getExternalContext().getSession(false));

                        servletSession.setAttribute(getUltiReport().getDatabaseTableName(), getUltiReport());
                        servletSession.setAttribute(ReportAttributes.REPORT_KEY_PARAM, getUltiReport().getDatabaseTableName());
                        servletSession.setAttribute(ReportAttributes.PRINT_TYPE, ReportAttributes.FILE_AND_PAGE_OUTPUT_PRINT);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "/reports/reportqueryresult.jsf";
    }

    public UlticoreReportEntity queryAndLoadReportAutomatically(UlticoreReportEntity report) throws Exception {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }

        report.setObjectFieldList(new ArrayList<List<Object>>());

        List<AbstractQueryParameter> qpEntityList = new ArrayList<AbstractQueryParameter>();
        List<QueryParameterEntity> rptQueryEntityList = new ArrayList<QueryParameterEntity>();
        
        for (QueryParameterEntity qpEntity : report.getQueryParamList()) {
            AbstractQueryParameter rqParam = new AbstractQueryParameter();

            rqParam.setOperator(qpEntity.getOperator());
            rqParam.setParameterDefltValue(qpEntity.getParameterDefltValue());
            rqParam.setParameterName(qpEntity.getParameterName());
            rqParam.setParameterPosition(qpEntity.getParameterPosition());
            rqParam.setParameterPredeterminedValue(qpEntity.getParameterPredeterminedValue());
            rqParam.setParameterValueSourceDesc(qpEntity.getParameterValueSourceDesc());
            rqParam.setParameterValueSourceID(qpEntity.getParameterValueSourceID());
            
            if(qpEntity.getParamterValue() != null) {
                rqParam.setParamterValue(qpEntity.getParamterValue());
            } else if(qpEntity.getParameterPredeterminedValue() != null) {
                rqParam.setParamterValue(qpEntity.getParameterPredeterminedValue());
                qpEntity.setParamterValue(qpEntity.getParameterPredeterminedValue());
            }
            
            qpEntityList.add(rqParam);
            rptQueryEntityList.add(qpEntity);
        }
        
        report.setQueryParamList(rptQueryEntityList);
        
        List<List<AbstractDataField>> fieldDataMatrix = dataServer.findNonORMData(
                report, report.getAbstractFieldMetaList(), qpEntityList, CommonBean.SQL_STYLE);

        if ((fieldDataMatrix != null) && (fieldDataMatrix.size() > 0)) {
            for (List<AbstractDataField> fieldList : fieldDataMatrix) {
                List<Object> reportFieldList = new ArrayList<Object>();
                for (AbstractDataField field : fieldList) {
                    AbstractFieldMetaEntity fieldMeta = report.findReportFieldMetaByDBField(field.getFieldName());
                    if (fieldMeta != null) {
                        report.getParameters().put(fieldMeta.getEntityFieldName(), field.getFieldValue());

                        if ((field.getFieldValue() instanceof Float)
                                || (field.getFieldValue() instanceof Double)) {
                            if(field.getFieldValue() instanceof Float) {
                                reportFieldList.add(BigDecimal.valueOf((Float) field.getFieldValue()));
                            } else {
                                reportFieldList.add(BigDecimal.valueOf((Double) field.getFieldValue()));
                            }
                        } else {
                            reportFieldList.add(field.getFieldValue());
                        }
                    }
                }
                report.getObjectFieldList().add(reportFieldList);
            }

            for (AbstractTemplateParamEntity templateParam : report.getAbstractParameterList()) {
                if ((templateParam.getParameterValue() != null)
                        && (!templateParam.getParameterValue().trim().equals(""))) {
                    report.getParameters().put(templateParam.getParameterName(), templateParam.getParameterValue());
                }
            }

            if (report.getObjectFieldList().size() > 0) {
                for (AbstractTemplateParamEntity param : report.getAbstractParameterList()) {
                    if (param.getDefaultValue() == DefaultSystemGeneratedFields.NONE) {
                        report.getParameters().put(param.getParameterName(), param.getParameterValue());
                    } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_DATE) {
                        if (param.getDataType() == AbstractTemplateDataType.DATE_TYPE) {
                            report.getParameters().put(param.getParameterName(), new Date());
                        } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                            report.getParameters().put(param.getParameterName(), DateUtil.getCurrentDateStr());
                        }
                    } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_TIMESTAMP) {
                        if (param.getDataType() == AbstractTemplateDataType.DATE_TYPE) {
                            report.getParameters().put(param.getParameterName(), new Timestamp(0));
                        } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                            report.getParameters().put(param.getParameterName(), DateUtil.getCurrentDateStr());
                        }
                    } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_USER) {
                        report.getParameters().put(param.getParameterName(), "System");
                    } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.INCREMENTAL_COUNT) {
                        try {
                            int fieldID = Integer.parseInt(appPropBean.getValue(param.getParameterName(),
                                    REPORT_FIELD_ID_DEFAULT, true));
                            if (param.getDataType() == AbstractTemplateDataType.INTEGER_TYPE) {
                                report.getParameters().put(param.getParameterName(), fieldID);
                            } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                                report.getParameters().put(param.getParameterName(), String.valueOf(fieldID));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.WEEK_OF_YEAR) {
                        if (param.getDataType() == AbstractTemplateDataType.INTEGER_TYPE) {
                            report.getParameters().put(param.getParameterName(), DateUtil.getWeekOfYear(new Date()));
                        } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                            report.getParameters().put(param.getParameterName(), String.valueOf(DateUtil.getWeekOfYear(new Date())));
                        }
                    } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.DAY_OF_YEAR) {
                        if (param.getDataType() == AbstractTemplateDataType.INTEGER_TYPE) {
                            report.getParameters().put(param.getParameterName(), DateUtil.getDayOfYear(new Date()));
                        } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                            report.getParameters().put(param.getParameterName(), String.valueOf(DateUtil.getDayOfYear(new Date())));
                        }
                    }
                }
            }
        }

        return report;
    }

    public void reportFormatSelected(ValueChangeEvent vce) {
        servletSession.setAttribute(ReportAttributes.REPORT_KEY_PARAM, ultiReport.getReportTitle());
        servletSession.setAttribute(ReportAttributes.PRINT_TYPE, ReportAttributes.PAGE_OUTPUT_PRINT);

        reportExportFormat = vce.getNewValue().toString();

        UlticoreChartBean ulticoreChartBean = (UlticoreChartBean) CommonBean.getBeanFromContext(
                "#{sessionScope.ulticoreChartBean}", UlticoreChartBean.class);
        if (ulticoreChartBean == null) {
            ulticoreChartBean = new UlticoreChartBean();
            CommonBean.setBeanToContext("#{sessionScope.ulticoreChartBean}",
                    UlticoreChartBean.class, ulticoreChartBean);
        }

        if (ultiReport.getReportBodyType() == ReportBodyType.INTUITIVE) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_BODY_TYPE_PARAM, ReportBodyType.INTUITIVE.toString());

            processingResourceUrl = CommonBean.getContextRoot() + FinultimateConstants.BI_RESOURCE_URL_DFLT_VALUE
                    + "?" + ReportAttributes.REPORT_TYPE_PARAM + "=INTUITIVE{S}&"
                    + ReportAttributes.PRINT_TYPE + "=" + reportExportFormat + "{S}";
        } else if (ultiReport.getReportBodyType() == ReportBodyType.TEMPLATE) {
            servletSession.setAttribute(ReportAttributes.REPORT_BODY_TYPE_PARAM, ReportBodyType.TEMPLATE.toString());

            processingResourceUrl = CommonBean.getContextRoot() + FinultimateConstants.BI_RESOURCE_URL_DFLT_VALUE
                    + "?" + ReportAttributes.TEMPLATE_FILE + "=" + ultiReport.getTemplateFileName() + "{S}&"
                    + ReportAttributes.PRINT_TYPE + "=" + reportExportFormat + "{S}";
        } else if (ultiReport.getReportBodyType() == ReportBodyType.INTUIT_TEMPLATE) {
            servletSession.setAttribute(ReportAttributes.REPORT_BODY_TYPE_PARAM, ReportBodyType.INTUIT_TEMPLATE.toString());

            processingResourceUrl = CommonBean.getContextRoot() + FinultimateConstants.BI_RESOURCE_URL_DFLT_VALUE
                    + "?" + ReportAttributes.REPORT_TYPE_PARAM + "=INTUITIVE{S}&"
                    + ReportAttributes.TEMPLATE_FILE + "=" + ultiReport.getTemplateFileName() + "{S}&"
                    + ReportAttributes.PRINT_TYPE + "=" + reportExportFormat + "{S}";
        }

        if (vce.getNewValue().toString().equals(ReportFormat.PDF.toString())) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_FORMAT_PARAM, ReportFormat.PDF.toString());

            reportExportFormat = ReportFormat.PDF.toString();
            ulticoreChartBean.setReportExportFormat(reportExportFormat);
        } else if (vce.getNewValue().toString().equals(ReportFormat.HTML.toString())) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_FORMAT_PARAM, ReportFormat.HTML.toString());

            reportExportFormat = ReportFormat.HTML.toString();
            ulticoreChartBean.setReportExportFormat(reportExportFormat);
        } else if (vce.getNewValue().toString().equals(ReportFormat.RTF.toString())) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_FORMAT_PARAM, ReportFormat.RTF.toString());

            reportExportFormat = ReportFormat.RTF.toString();
            ulticoreChartBean.setReportExportFormat(reportExportFormat);
        } else if (vce.getNewValue().toString().equals(ReportFormat.XLS.toString())) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_FORMAT_PARAM, ReportFormat.XLS.toString());

            reportExportFormat = ReportFormat.XLS.toString();
            ulticoreChartBean.setReportExportFormat(reportExportFormat);
        }
    }

    public void reportBodyTypeSelected(ValueChangeEvent vce) {
        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }

        UlticoreChartBean ulticoreChartBean = (UlticoreChartBean) CommonBean.getBeanFromContext(
                "#{sessionScope.ulticoreChartBean}", UlticoreChartBean.class);
        if (ulticoreChartBean == null) {
            ulticoreChartBean = new UlticoreChartBean();
            CommonBean.setBeanToContext("#{sessionScope.ulticoreChartBean}",
                    UlticoreChartBean.class, ulticoreChartBean);
        }

        if (vce.getNewValue().toString().equals(ReportBodyType.INTUITIVE.toString())) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_BODY_TYPE_PARAM, ReportBodyType.INTUITIVE.toString());

            processingResourceUrl = CommonBean.getContextRoot() + FinultimateConstants.BI_RESOURCE_URL_DFLT_VALUE
                    + "?" + ReportAttributes.REPORT_TYPE_PARAM + "=INTUITIVE{S}&"
                    + ReportAttributes.PRINT_TYPE + "=" + reportExportFormat + "{S}";
        } else if (vce.getNewValue().toString().equals(ReportBodyType.TEMPLATE.toString())) {
            servletSession.setAttribute(ReportAttributes.REPORT_BODY_TYPE_PARAM, ReportBodyType.TEMPLATE.toString());

            processingResourceUrl = CommonBean.getContextRoot() + FinultimateConstants.BI_RESOURCE_URL_DFLT_VALUE
                    + "?" + ReportAttributes.TEMPLATE_FILE + "=" + ultiReport.getTemplateFileName() + "{S}&"
                    + ReportAttributes.PRINT_TYPE + "=" + ulticoreChartBean.getReportExportFormat() + "{S}";
        } else if (vce.getNewValue().toString().equals(ReportBodyType.INTUIT_TEMPLATE.toString())) {
            servletSession.setAttribute(ReportAttributes.REPORT_BODY_TYPE_PARAM, ReportBodyType.TEMPLATE.toString());

            processingResourceUrl = CommonBean.getContextRoot() + FinultimateConstants.BI_RESOURCE_URL_DFLT_VALUE
                    + "?" + ReportAttributes.REPORT_TYPE_PARAM + "=INTUITIVE{S}&"
                    + ReportAttributes.TEMPLATE_FILE + "=" + ultiReport.getTemplateFileName() + "{S}&"
                    + ReportAttributes.PRINT_TYPE + "=" + ulticoreChartBean.getReportExportFormat() + "{S}";
        }
    }

    public String buildReportElements() {
        List<AbstractFieldMetaEntity> reportFieldMetaList = new ArrayList<AbstractFieldMetaEntity>();
        List<QueryParameterEntity> queryParamList = new ArrayList<QueryParameterEntity>();
        List<AbstractTemplateParamEntity> reportParamterList = new ArrayList<AbstractTemplateParamEntity>();

        ultiReport.setQueryExpression(ultiReport.getQueryExpression().toUpperCase());

        String[] queryPart = ultiReport.getQueryExpression().split(" WHERE ");
        String queryWithoutFromClause = queryPart[0].substring(0, queryPart[0].indexOf(" FROM "));
        String[] queryColumnList = queryWithoutFromClause.split(",");

        for (String queryColumn : queryColumnList) {
            AbstractFieldMetaEntity fieldMeta = new UlticoreReportFieldMetaEntity();
            AbstractTemplateParamEntity templateParam = new ReportTemplateParamEntity();

            if (queryColumn.contains(" AS ")) {
                queryColumn = queryColumn.substring(queryColumn.indexOf(" AS ") + " AS ".length()).trim();
            }

            if (queryColumn.contains(".")) {
                queryColumn = queryColumn.substring(queryColumn.indexOf(".") + 1);
            }

            fieldMeta.setDatabaseFieldName(queryColumn);
            fieldMeta.setEntityFieldName(queryColumn);
            reportFieldMetaList.add(fieldMeta);

            templateParam.setParameterName(queryColumn);
            if (queryColumn.endsWith("_DATE")) {
                templateParam.setDataType(AbstractTemplateDataType.DATE_TYPE);
            } else {
                templateParam.setDataType(AbstractTemplateDataType.STRING_TYPE);
            }
            reportParamterList.add(templateParam);
        }

        if (queryPart.length == 2) {
            String[] conditionClausePart = queryPart[1].split(" AND ");
            int paramPosition = 1;

            for (String clauseCondition : conditionClausePart) {
                QueryParameterEntity queryParam = new ReportQueryParameterEntity();

                String[] conditionPart = clauseCondition.split(" ");

                if (conditionPart[0].contains(".")) {
                    String namePart = conditionPart[0].substring(conditionPart[0].indexOf(".") + 1);
                    queryParam.setParameterName(namePart);
                } else {
                    queryParam.setParameterName(conditionPart[0].trim());
                }
                queryParam.setOperator(conditionPart[1].trim());

                if (queryParam.getParameterName().endsWith("_DATE")) {
                    queryParam.setParameterValueSourceID(RendererType.DATE_FIELD_NAME.toString());
                    queryParam.setParameterValueSourceDesc(RendererType.DATE_FIELD_NAME.toString());
                    queryParam.setParameterTypeFQN(java.util.Date.class.getName());
                } else {
                    queryParam.setParameterValueSourceID(RendererType.INPUT_TEXT_NAME.toString());
                    queryParam.setParameterValueSourceDesc(RendererType.INPUT_TEXT_NAME.toString());
                    queryParam.setParameterTypeFQN(String.class.getName());
                }

                String paramSurrogate = conditionPart[2].trim();
                if ((paramSurrogate.startsWith("{@")) && (paramSurrogate.endsWith("}"))) {
                    paramSurrogate = paramSurrogate.substring(
                            paramSurrogate.indexOf("{@") + 2, paramSurrogate.indexOf("}"));

                    queryParam.setParameterPredeterminedValue(paramSurrogate);
                } else if ((paramSurrogate.startsWith("'{@")) && (paramSurrogate.endsWith("}'"))) {
                    paramSurrogate = paramSurrogate.substring(
                            paramSurrogate.indexOf("'{@") + 3, paramSurrogate.indexOf("}'"));

                    queryParam.setParameterPredeterminedValue(paramSurrogate);
                } else {
                    queryParam.setParameterPredeterminedValue(paramSurrogate);
                }

                queryParam.setParameterPosition(paramPosition++);
                queryParamList.add(queryParam);
            }
        }

        ultiReport.setQueryParamList(queryParamList);
        ultiReport.setAbstractFieldMetaList(reportFieldMetaList);
        ultiReport.setAbstractParameterList(reportParamterList);

        return "";
    }

    public String buildSubreportElements() {
        List<AbstractFieldMetaEntity> reportFieldMetaList = new ArrayList<AbstractFieldMetaEntity>();
        List<QueryParameterEntity> queryParamList = new ArrayList<QueryParameterEntity>();
        List<AbstractTemplateParamEntity> reportParamterList = new ArrayList<AbstractTemplateParamEntity>();

        ultiSubreport.setQueryExpression(ultiSubreport.getQueryExpression().toUpperCase());

        String[] queryPart = ultiSubreport.getQueryExpression().split(" WHERE ");
        String queryWithoutFromClause = queryPart[0].substring(0, queryPart[0].indexOf(" FROM "));
        String[] queryColumnList = queryWithoutFromClause.split(",");

        for (String queryColumn : queryColumnList) {
            AbstractFieldMetaEntity fieldMeta = new UlticoreSubreportFieldMetaEntity();
            AbstractTemplateParamEntity templateParam = new SubreportTemplateParamEntity();

            if (queryColumn.contains(" AS ")) {
                queryColumn = queryColumn.substring(queryColumn.indexOf(" AS ") + " AS ".length()).trim();
            }

            if (queryColumn.contains(".")) {
                queryColumn = queryColumn.substring(queryColumn.indexOf(".") + 1);
            }

            fieldMeta.setDatabaseFieldName(queryColumn);
            fieldMeta.setEntityFieldName(queryColumn);
            reportFieldMetaList.add(fieldMeta);

            templateParam.setParameterName(queryColumn);
            if (queryColumn.endsWith("_DATE")) {
                templateParam.setDataType(AbstractTemplateDataType.DATE_TYPE);
            } else {
                templateParam.setDataType(AbstractTemplateDataType.STRING_TYPE);
            }
            reportParamterList.add(templateParam);
        }

        if (queryPart.length == 2) {
            String[] conditionClausePart = queryPart[1].split(" AND ");
            int paramPosition = 1;

            for (String clauseCondition : conditionClausePart) {
                QueryParameterEntity queryParam = new SubreportQueryParameterEntity();

                String[] conditionPart = clauseCondition.split(" ");

                if (conditionPart[0].contains(".")) {
                    String namePart = conditionPart[0].substring(conditionPart[0].indexOf(".") + 1);
                    queryParam.setParameterName(namePart);
                } else {
                    queryParam.setParameterName(conditionPart[0].trim());
                }
                queryParam.setOperator(conditionPart[1].trim());

                if (queryParam.getParameterName().endsWith("_DATE")) {
                    queryParam.setParameterValueSourceID(RendererType.DATE_FIELD_NAME.toString());
                    queryParam.setParameterValueSourceDesc(RendererType.DATE_FIELD_NAME.toString());
                    queryParam.setParameterTypeFQN(java.util.Date.class.getName());
                } else {
                    queryParam.setParameterValueSourceID(RendererType.INPUT_TEXT_NAME.toString());
                    queryParam.setParameterValueSourceDesc(RendererType.INPUT_TEXT_NAME.toString());
                    queryParam.setParameterTypeFQN(String.class.getName());
                }

                String paramSurrogate = conditionPart[2].trim();
                if ((paramSurrogate.startsWith("{@")) && (paramSurrogate.endsWith("}"))) {
                    paramSurrogate = paramSurrogate.substring(
                            paramSurrogate.indexOf("{@") + 2, paramSurrogate.indexOf("}"));

                    queryParam.setParameterPredeterminedValue(paramSurrogate);
                } else if ((paramSurrogate.startsWith("'{@")) && (paramSurrogate.endsWith("}'"))) {
                    paramSurrogate = paramSurrogate.substring(
                            paramSurrogate.indexOf("'{@") + 3, paramSurrogate.indexOf("}'"));

                    queryParam.setParameterPredeterminedValue(paramSurrogate);
                } else {
                    queryParam.setParameterPredeterminedValue(paramSurrogate);
                }

                queryParam.setParameterPosition(paramPosition++);
                queryParamList.add(queryParam);
            }
        }

        ultiSubreport.setQueryParamList(queryParamList);
        ultiSubreport.setAbstractFieldMetaList(reportFieldMetaList);
        ultiSubreport.setAbstractParameterList(reportParamterList);

        return "";
    }

    public void reportFieldValueTypeSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            FieldValueType valueType = (FieldValueType) vce.getNewValue();
            reportFieldMeta.setFieldValueType(valueType);

            if (reportFieldMeta.getFieldValueType() == FieldValueType.DERIVED) {
                if (reportFieldMeta.getEntityFieldName().trim().length() > 0) {
                    reportFieldMeta.setRoutineSourceCode("public function compute"
                            + DynamoUtil.getFieldNameStartingWithAnUpperCaseLetter(
                                    reportFieldMeta.getEntityFieldName().trim()) + "Value() {\n\t\n}\n");
                }
            } else if (reportFieldMeta.getFieldValueType() == FieldValueType.INPUT) {
                reportFieldMeta.setRoutineSourceCode("");
            }
        }
    }

    public void subreportFieldValueTypeSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            FieldValueType valueType = (FieldValueType) vce.getNewValue();
            subreportFieldMeta.setFieldValueType(valueType);

            if (subreportFieldMeta.getFieldValueType() == FieldValueType.DERIVED) {
                if (subreportFieldMeta.getEntityFieldName().trim().length() > 0) {
                    subreportFieldMeta.setRoutineSourceCode("public function compute"
                            + DynamoUtil.getFieldNameStartingWithAnUpperCaseLetter(
                                    subreportFieldMeta.getEntityFieldName().trim()) + "Value() {\n\t\n}\n");
                }
            } else if (subreportFieldMeta.getFieldValueType() == FieldValueType.INPUT) {
                subreportFieldMeta.setRoutineSourceCode("");
            }
        }
    }

    public void reportFieldNameChanged(ValueChangeEvent vce) {
        reportFieldMeta.setDatabaseFieldName(CommonBean.columnize(vce.getNewValue().toString()));
    }

    public void subreportFieldNameChanged(ValueChangeEvent vce) {
        subreportFieldMeta.setDatabaseFieldName(CommonBean.columnize(vce.getNewValue().toString()));
    }

    public String printReport() {
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

        if (ultiReportList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    businessActionTrail);
            if (batEntity != null) {
                loadReportTemplates(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadReportTemplates(batEntity);
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

        if (ultiReportList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean
                    .loadNextHistoricalState(businessActionTrail);
            if (batEntity != null) {
                loadReportTemplates(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.", MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadReportTemplates(batEntity);
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

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.BI_REPORT);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(ultiReport.getReportID());
        businessActionTrailBean.loadBusinessActionTrailList();

        if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
            businessActionTrailBean.setBusinessActionTrail(
                    businessActionTrailBean.getBusinessActionTrailList().get(
                            businessActionTrailBean.getBusinessActionTrailList().size() - 1));

            loadReportTemplates((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            outcome = "reportaudittrail.jsf";
        } else {
            applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    /**
     * @return the derivedReportFieldValue
     */
    public boolean isDerivedReportFieldValue() {
        derivedReportFieldValue = reportFieldMeta.getFieldValueType() == FieldValueType.DERIVED;
        return derivedReportFieldValue;
    }

    /**
     * @param derivedReportFieldValue the derivedReportFieldValue to set
     */
    public void setDerivedReportFieldValue(boolean derivedReportFieldValue) {
        this.derivedReportFieldValue = derivedReportFieldValue;
    }

    /**
     * @return the derivedSubreportFieldValue
     */
    public boolean isDerivedSubreportFieldValue() {
        derivedSubreportFieldValue = subreportFieldMeta.getFieldValueType() == FieldValueType.DERIVED;
        return derivedSubreportFieldValue;
    }

    /**
     * @param derivedSubreportFieldValue the derivedSubreportFieldValue to set
     */
    public void setDerivedSubreportFieldValue(boolean derivedSubreportFieldValue) {
        this.derivedSubreportFieldValue = derivedSubreportFieldValue;
    }

    /**
     * @return the ultiReport
     */
    public UlticoreReportEntity getUltiReport() {
        return ultiReport;
    }

    /**
     * @param ultiReport the ultiReport to set
     */
    public void setUltiReport(UlticoreReportEntity ultiReport) {
        this.ultiReport = ultiReport;
    }

    /**
     * @return the reportFieldMeta
     */
    public AbstractFieldMetaEntity getReportFieldMeta() {
        return reportFieldMeta;
    }

    /**
     * @param reportFieldMeta the reportFieldMeta to set
     */
    public void setReportFieldMeta(UlticoreReportFieldMetaEntity reportFieldMeta) {
        this.reportFieldMeta = reportFieldMeta;
    }

    /**
     * @return the parameter
     */
    public QueryParameterEntity getParameter() {
        return parameter;
    }

    /**
     * @param parameter the parameter to set
     */
    public void setParameter(QueryParameterEntity parameter) {
        this.parameter = parameter;
    }

    /**
     * @return the ultiReportList
     */
    public List<UlticoreReportEntity> getUltiReportList() {
        return ultiReportList;
    }

    /**
     * @param ultiReportList the ultiReportList to set
     */
    public void setUltiReportList(List<UlticoreReportEntity> ultiReportList) {
        this.ultiReportList = ultiReportList;
    }

    /**
     * @return the databaseTableFieldItemList
     */
    public List<SelectItem> getDatabaseTableFieldItemList() {
        return databaseTableFieldItemList;
    }

    /**
     * @param databaseTableFieldItemList the databaseTableFieldItemList to set
     */
    public void setDatabaseTableFieldItemList(List<SelectItem> databaseTableFieldItemList) {
        this.databaseTableFieldItemList = databaseTableFieldItemList;
    }

    /**
     * @return the rendererType
     */
    public RendererType getRendererType() {
        return rendererType;
    }

    /**
     * @param rendererType the rendererType to set
     */
    public void setRendererType(RendererType rendererType) {
        this.rendererType = rendererType;
    }

    /**
     * @return the dataTable
     */
    public DataTable getDataTable() {
        return dataTable;
    }

    /**
     * @param dataTable the dataTable to set
     */
    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    /**
     * @return the reportFormat
     */
    public ReportFormat getReportFormat() {
        return reportFormat;
    }

    /**
     * @param reportFormat the reportFormat to set
     */
    public void setReportFormat(ReportFormat reportFormat) {
        this.reportFormat = reportFormat;
    }

    /**
     * @return the reportBodyType
     */
    public ReportBodyType getReportBodyType() {
        return reportBodyType;
    }

    /**
     * @param reportBodyType the reportBodyType to set
     */
    public void setReportBodyType(ReportBodyType reportBodyType) {
        this.reportBodyType = reportBodyType;
    }

    /**
     * @return the servletSession
     */
    public HttpSession getServletSession() {
        return servletSession;
    }

    /**
     * @param servletSession the servletSession to set
     */
    public void setServletSession(HttpSession servletSession) {
        this.servletSession = servletSession;
    }

    /**
     * @return the sessionID
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     * @param sessionID the sessionID to set
     */
    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * @return the ultiReportItemList
     */
    public List<SelectItem> getUltiReportItemList() {
        return ultiReportItemList;
    }

    /**
     * @param ultiReportItemList the ultiReportItemList to set
     */
    public void setUltiReportItemList(List<SelectItem> ultiReportItemList) {
        this.ultiReportItemList = ultiReportItemList;
    }

    /**
     * @return the selectedReportFieldItemList
     */
    public List<SelectItem> getSelectedReportFieldItemList() {
        return selectedReportFieldItemList;
    }

    /**
     * @param selectedReportFieldItemList the selectedReportFieldItemList to set
     */
    public void setSelectedReportFieldItemList(List<SelectItem> selectedReportFieldItemList) {
        this.selectedReportFieldItemList = selectedReportFieldItemList;
    }

    /**
     * @return the reportParamter
     */
    public AbstractTemplateParamEntity getReportParamter() {
        return reportParamter;
    }

    /**
     * @param reportParamter the reportParamter to set
     */
    public void setReportParamter(AbstractTemplateParamEntity reportParamter) {
        this.reportParamter = reportParamter;
    }

    /**
     * @return the parameterValueTypeItemList
     */
    public List<SelectItem> getParameterValueTypeItemList() {
        return parameterValueTypeItemList;
    }

    /**
     * @param parameterValueTypeItemList the parameterValueTypeItemList to set
     */
    public void setParameterValueTypeItemList(List<SelectItem> parameterValueTypeItemList) {
        this.parameterValueTypeItemList = parameterValueTypeItemList;
    }

    /**
     * @return the systemGeneratedFieldItemList
     */
    public List<SelectItem> getSystemGeneratedFieldItemList() {
        return systemGeneratedFieldItemList;
    }

    /**
     * @param systemGeneratedFieldItemList the systemGeneratedFieldItemList to
     * set
     */
    public void setSystemGeneratedFieldItemList(List<SelectItem> systemGeneratedFieldItemList) {
        this.systemGeneratedFieldItemList = systemGeneratedFieldItemList;
    }

    /**
     * @return the noDefaultSysGenFieldVal
     */
    public DefaultSystemGeneratedFields getNoDefaultSysGenFieldVal() {
        return noDefaultSysGenFieldVal;
    }

    /**
     * @param noDefaultSysGenFieldVal the noDefaultSysGenFieldVal to set
     */
    public void setNoDefaultSysGenFieldVal(DefaultSystemGeneratedFields noDefaultSysGenFieldVal) {
        this.noDefaultSysGenFieldVal = noDefaultSysGenFieldVal;
    }

    /**
     * @return the reportTrigger
     */
    public ReportTriggerEntity getReportTrigger() {
        return reportTrigger;
    }

    /**
     * @param reportTrigger the reportTrigger to set
     */
    public void setReportTrigger(ReportTriggerEntity reportTrigger) {
        this.reportTrigger = reportTrigger;
    }

    /**
     * @return the reportSchedule
     */
    public ReportScheduleEntity getReportSchedule() {
        return reportSchedule;
    }

    /**
     * @param reportSchedule the reportSchedule to set
     */
    public void setReportSchedule(ReportScheduleEntity reportSchedule) {
        this.reportSchedule = reportSchedule;
    }

    /**
     * @return the reportTriggerList
     */
    public List<ReportTriggerEntity> getReportTriggerList() {
        return reportTriggerList;
    }

    /**
     * @param reportTriggerList the reportTriggerList to set
     */
    public void setReportTriggerList(List<ReportTriggerEntity> reportTriggerList) {
        this.reportTriggerList = reportTriggerList;
    }

    /**
     * @return the reportScheduleList
     */
    public List<ReportScheduleEntity> getReportScheduleList() {
        return reportScheduleList;
    }

    /**
     * @param reportScheduleList the reportScheduleList to set
     */
    public void setReportScheduleList(List<ReportScheduleEntity> reportScheduleList) {
        this.reportScheduleList = reportScheduleList;
    }

    /**
     * @return the reportTriggerItemList
     */
    public List<SelectItem> getReportTriggerItemList() {
        return reportTriggerItemList;
    }

    /**
     * @param reportTriggerItemList the reportTriggerItemList to set
     */
    public void setReportTriggerItemList(List<SelectItem> reportTriggerItemList) {
        this.reportTriggerItemList = reportTriggerItemList;
    }

    /**
     * @return the ultiSubreport
     */
    public UlticoreSubreportEntity getUltiSubreport() {
        return ultiSubreport;
    }

    /**
     * @param ultiSubreport the ultiSubreport to set
     */
    public void setUltiSubreport(UlticoreSubreportEntity ultiSubreport) {
        this.ultiSubreport = ultiSubreport;
    }

    /**
     * @return the subreportDatabaseTableFieldItemList
     */
    public List<SelectItem> getSubreportDatabaseTableFieldItemList() {
        return subreportDatabaseTableFieldItemList;
    }

    /**
     * @param subreportDatabaseTableFieldItemList the
     * subreportDatabaseTableFieldItemList to set
     */
    public void setSubreportDatabaseTableFieldItemList(List<SelectItem> subreportDatabaseTableFieldItemList) {
        this.subreportDatabaseTableFieldItemList = subreportDatabaseTableFieldItemList;
    }

    /**
     * @return the subreportParameter
     */
    public SubreportQueryParameterEntity getSubreportParameter() {
        return subreportParameter;
    }

    /**
     * @param subreportParameter the subreportParameter to set
     */
    public void setSubreportParameter(SubreportQueryParameterEntity subreportParameter) {
        this.subreportParameter = subreportParameter;
    }

    /**
     * @return the subreportFieldMeta
     */
    public UlticoreSubreportFieldMetaEntity getSubreportFieldMeta() {
        return subreportFieldMeta;
    }

    /**
     * @param subreportFieldMeta the subreportFieldMeta to set
     */
    public void setSubreportFieldMeta(UlticoreSubreportFieldMetaEntity subreportFieldMeta) {
        this.subreportFieldMeta = subreportFieldMeta;
    }

    /**
     * @return the subreportParamter
     */
    public SubreportTemplateParamEntity getSubreportParamter() {
        return subreportParamter;
    }

    /**
     * @param subreportParamter the subreportParamter to set
     */
    public void setSubreportParamter(SubreportTemplateParamEntity subreportParamter) {
        this.subreportParamter = subreportParamter;
    }

    /**
     * @return the queryOperatorItemList
     */
    public List<SelectItem> getQueryOperatorItemList() {
        return queryOperatorItemList;
    }

    /**
     * @param queryOperatorItemList the queryOperatorItemList to set
     */
    public void setQueryOperatorItemList(List<SelectItem> queryOperatorItemList) {
        this.queryOperatorItemList = queryOperatorItemList;
    }

    /**
     * @return the hasSubreport
     */
    public boolean isHasSubreport() {
        return hasSubreport;
    }

    /**
     * @param hasSubreport the hasSubreport to set
     */
    public void setHasSubreport(boolean hasSubreport) {
        this.hasSubreport = hasSubreport;
    }

    /**
     * @return the sqlStmtItemList
     */
    public List<SelectItem> getSqlStmtItemList() {
        return sqlStmtItemList;
    }

    /**
     * @param sqlStmtItemList the sqlStmtItemList to set
     */
    public void setSqlStmtItemList(List<SelectItem> sqlStmtItemList) {
        this.sqlStmtItemList = sqlStmtItemList;
    }

    /**
     * @return the dataTypeNameItemList
     */
    public List<SelectItem> getDataTypeNameItemList() {
        return dataTypeNameItemList;
    }

    /**
     * @param dataTypeNameItemList the dataTypeNameItemList to set
     */
    public void setDataTypeNameItemList(List<SelectItem> dataTypeNameItemList) {
        this.dataTypeNameItemList = dataTypeNameItemList;
    }

    /**
     * @return the processingResourceUrl
     */
    public String getProcessingResourceUrl() {
        return processingResourceUrl;
    }

    /**
     * @param processingResourceUrl the processingResourceUrl to set
     */
    public void setProcessingResourceUrl(String processingResourceUrl) {
        this.processingResourceUrl = processingResourceUrl;
    }

    /**
     * @return the reportExportFormat
     */
    public String getReportExportFormat() {
        return reportExportFormat;
    }

    /**
     * @param reportExportFormat the reportExportFormat to set
     */
    public void setReportExportFormat(String reportExportFormat) {
        this.reportExportFormat = reportExportFormat;
    }

    /**
     * @return the fieldValueTypeItemList
     */
    public List<SelectItem> getFieldValueTypeItemList() {
        return fieldValueTypeItemList;
    }

    /**
     * @param fieldValueTypeItemList the fieldValueTypeItemList to set
     */
    public void setFieldValueTypeItemList(List<SelectItem> fieldValueTypeItemList) {
        this.fieldValueTypeItemList = fieldValueTypeItemList;
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

    /**
     * @return the reportBodyTypeItemList
     */
    public List<SelectItem> getReportBodyTypeItemList() {
        return reportBodyTypeItemList;
    }

    /**
     * @param reportBodyTypeItemList the reportBodyTypeItemList to set
     */
    public void setReportBodyTypeItemList(List<SelectItem> reportBodyTypeItemList) {
        this.reportBodyTypeItemList = reportBodyTypeItemList;
    }

}
