/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.projects.commons.messages.beans;

import com.codrellica.mail.beans.SendMessageBean;
import com.codrellica.mail.beans.SendMessageRemote;
import com.codrellica.mail.dataobjects.MailBodyType;
import com.codrellica.mail.dataobjects.MailObject;
import com.codrellica.mail.dataobjects.MailParameter;
import com.codrellica.mail.dataobjects.MessageContentType;
import com.codrellica.mail.dataobjects.MessageType;
import com.codrellica.mail.resources.ServiceConfig;
import com.codrellica.projects.commons.DateUtil;
import com.rsd.projects.menus.FinultimateCommons;
import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;

import com.rsdynamix.abstractobjects.QueryParameterEntity;
import com.rsdynamix.projects.commons.entities.EmailRecepientEntity;
import com.rsdynamix.projects.commons.entities.EmailSettingEntity;
import com.rsdynamix.projects.commons.entities.RecepientType;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;
import com.rsdynamix.abstractobjects.AbstractDataField;
import com.rsdynamix.abstractobjects.AbstractFieldMetaEntity;
import com.rsdynamix.abstractobjects.AbstractQueryParameter;
import com.rsdynamix.abstractobjects.AbstractTemplateDataType;
import com.rsdynamix.abstractobjects.AbstractTemplateParamEntity;
import com.rsdynamix.abstractobjects.FieldValueType;
import com.rsdynamix.bi.projects.report.bean.UlticoreReportBean;
import com.rsdynamix.bi.projects.scheduler.CourrierScheduler;
import com.rsdynamix.bi.projects.web.commons.bean.ApplicationInitializer;
import com.rsdynamix.bi.projects.web.commons.bean.RendererType;
import com.rsdynamix.bi.projects.web.commons.bean.ReportFormat;
import com.rsdynamix.bi.projects.web.commons.bean.ValueListBean;
import com.rsdynamix.projects.report.entities.ReportBodyType;
import com.rsdynamix.bi.reports.servlet.ReportAttributes;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import com.rsdynamix.dynamo.common.entities.EntityNameType;
import com.rsdynamix.dynamo.common.entities.UserAdminSwitchType;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.projects.commons.entities.MailGatewayConfigEntity;
import com.rsdynamix.projects.dynamo.mvc.context.ContextBuilder;
import com.rsdynamix.projects.dynamo.mvc.context.DynamoContext;
import com.rsdynamix.projects.dynamo.mvc.processors.DynamoRequestDispatcher;
import com.rsdynamix.projects.dynamo.mvc.scopes.ScopeManagerBean;
import com.rsdynamix.projects.message.entities.MessageQueryParameterEntity;
import com.rsdynamix.projects.message.entities.MessageTemplateParamEntity;
import com.rsdynamix.projects.message.entities.UlticoreMessageFieldMetaEntity;
import com.rsdynamix.projects.report.entities.DefaultSystemGeneratedFields;
import com.rsdynamix.projects.report.entities.ReportScheduleEntity;
import com.rsdynamix.projects.report.entities.ReportTriggerEntity;
import com.rsdynamix.projects.report.entities.SQLStatementType;
import com.rsdynamix.projects.report.entities.UlticoreReportEntity;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import com.rsdynamix.projects.ulticoreparser.beans.DynamoUtil;
import com.rsdynamix.projects.ulticoreparser.oop.dataobjects.ReportInstanceBuilderFactoryImpl;
import com.rsdynamix.projects.ulticoreparser.oop.dataobjects.UlticoreInstance;
import com.rsdynamix.projects.web.commons.bean.ActualMessage;
import com.rsdynamix.projects.web.commons.bean.ActualMessage;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.tns.util.Constants;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.html.HtmlSelectBooleanCheckbox;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "messageSetupBean")
public class MessageSetupBean implements Serializable {

    private static final String RECEPIENT_ID_KEY = "rcpnt_id";
    //
    private static final String RECEPIENT_ID_DEFAULT = "1";
    //
    private static final String EMAIL_SETTING_ID_KEY = "email_setin_id";
    //
    private static final String EMAIL_SETTING_ID_DEFAULT = "1";
    //
    private static final String MSSG_TEMPLATE_ID_KEY = "mssg_tmplat_id";
    //
    private static final String MSSG_TEMPLATE_ID_DEFAULT = "1";
    //
    private static final String MSSG_TRIGGER_ID_KEY = "mssg_trigger_id";
    //
    private static final String MSSG_TRIGGER_ID_DEFAULT = "1";
    //
    private static final String MSSG_SCHEDULE_ID_KEY = "mssg_schedl_id";
    //
    private static final String MSSG_SCHEDULE_ID_DEFAULT = "1";
    //
    private static final String MSSG_FIELD_ID_KEY = "mssg_fld_id";
    //
    private static final String MSSG_FIELD_ID_DEFAULT = "1";
    //
    private static final String QUERY_FIELD_ID_KEY = "qry_fld_id";
    //
    private static final String QUERY_FIELD_ID_DEFAULT = "1";
    //
    private static final String MSSG_PARAM_ID_KEY = "mssg_tmplt_param_id";
    //
    private static final String MSSG_PARAM_ID_DEFAULT = "1";
    //
    private static final String RECEPIENT_NAME_KEY = "RCPT_EMAIL";
    //
    private EmailSettingEntity emailSetting;
    private EmailRecepientEntity recepient;
    private List<EmailSettingEntity> emailSettingList;
    //
    private UlticoreMessageFieldMetaEntity messageFieldMeta;
    private QueryParameterEntity parameter;
    private AbstractTemplateParamEntity messageParamter;
    //
    private ReportTriggerEntity messageTrigger;
    private ReportScheduleEntity messageSchedule;
    //
    private List<ReportTriggerEntity> messageTriggerList;
    private List<ReportScheduleEntity> messageScheduleList;
    //
    private List<SelectItem> mailBodyTypeItemList;
    private List<SelectItem> messageContentTypeItemList;
    private List<SelectItem> messageTypeItemList;
    private List<SelectItem> receptionTypeItemList;
    private List<SelectItem> mailSubjectItemList;
    //
    private List<SelectItem> emailSettingItemList;
    private List<SelectItem> messageTriggerItemList;
    private List<SelectItem> selectedReportFieldItemList;
    private List<SelectItem> systemGeneratedFieldItemList;
    private List<SelectItem> queryOperatorItemList;
    private List<SelectItem> sqlStmtItemList;
    private List<SelectItem> dataTypeNameItemList;
    private List<SelectItem> databaseTableFieldItemList;
    //
    private DefaultSystemGeneratedFields noDefaultSysGenFieldVal;
    //
    private com.rsdynamix.bi.projects.web.commons.bean.ReportFormat reportFormat;
    private ReportBodyType reportBodyType;
    private HttpSession servletSession;
    private String sessionID;
    private String reportExportFormat;
    //
    private List<SelectItem> parameterValueTypeItemList;
    private List<SelectItem> fieldValueTypeItemList;
    private boolean derivedMessageFieldValue;
    //
    private String processingResourceUrl;
    private List<ActualMessage> generatedMessageStack;
    //
    private CourrierScheduler scheduler;
    private MailGatewayConfigEntity mailGatewayConfig;

    public MessageSetupBean() {
        emailSetting = new EmailSettingEntity();
        recepient = new EmailRecepientEntity();
        emailSettingList = new ArrayList<EmailSettingEntity>();
        mailSubjectItemList = new ArrayList<SelectItem>();

        messageFieldMeta = new UlticoreMessageFieldMetaEntity();
        parameter = new MessageQueryParameterEntity();
        messageParamter = new MessageTemplateParamEntity();
        databaseTableFieldItemList = new ArrayList<SelectItem>();

        emailSettingItemList = new ArrayList<SelectItem>();
        selectedReportFieldItemList = new ArrayList<SelectItem>();
        systemGeneratedFieldItemList = new ArrayList<SelectItem>();
        messageTriggerItemList = new ArrayList<SelectItem>();
        dataTypeNameItemList = new ArrayList<SelectItem>();

        parameterValueTypeItemList = new ArrayList<SelectItem>();
        noDefaultSysGenFieldVal = DefaultSystemGeneratedFields.NONE;

        messageTrigger = new ReportTriggerEntity();
        messageSchedule = new ReportScheduleEntity();

        messageTriggerList = new ArrayList<ReportTriggerEntity>();
        messageScheduleList = new ArrayList<ReportScheduleEntity>();
        sqlStmtItemList = new ArrayList<SelectItem>();

        fieldValueTypeItemList = new ArrayList<SelectItem>();

        processingResourceUrl = "";

        mailBodyTypeItemList = new ArrayList<SelectItem>();
        SelectItem item = new SelectItem();
        item.setLabel(MailBodyType.STATIC_BODY.toString().replace('_', ' '));
        item.setValue(MailBodyType.STATIC_BODY);
        mailBodyTypeItemList.add(item);

        item = new SelectItem();
        item.setLabel(MailBodyType.TEMPLATE_BODY.toString().replace('_', ' '));
        item.setValue(MailBodyType.TEMPLATE_BODY);
        mailBodyTypeItemList.add(item);

        messageContentTypeItemList = new ArrayList<SelectItem>();
        item = new SelectItem();
        item.setLabel(MessageContentType.TEXT_MESSAGE.toString().replace('_', ' '));
        item.setValue(MessageContentType.TEXT_MESSAGE);
        messageContentTypeItemList.add(item);

        item = new SelectItem();
        item.setLabel(MessageContentType.HTML_MESSAGE.toString().replace('_', ' '));
        item.setValue(MessageContentType.HTML_MESSAGE);
        messageContentTypeItemList.add(item);

        messageTypeItemList = new ArrayList<SelectItem>();
        item = new SelectItem();
        item.setLabel(MessageType.MAIL_MESSAGE.toString().replace('_', ' '));
        item.setValue(MessageType.MAIL_MESSAGE);
        messageTypeItemList.add(item);

        item = new SelectItem();
        item.setLabel(MessageType.SMS_MESSAGE.toString().replace('_', ' '));
        item.setValue(MessageType.SMS_MESSAGE);
        messageTypeItemList.add(item);

        receptionTypeItemList = new ArrayList<SelectItem>();
        item = new SelectItem();
        item.setLabel(RecepientType.TO_TYPE.toString().replace('_', ' '));
        item.setValue(RecepientType.TO_TYPE);
        receptionTypeItemList.add(item);

        item = new SelectItem();
        item.setLabel(RecepientType.CC_TYPE.toString().replace('_', ' '));
        item.setValue(RecepientType.CC_TYPE);
        receptionTypeItemList.add(item);

        item = new SelectItem();
        item.setLabel(RecepientType.BCC_TYPE.toString().replace('_', ' '));
        item.setValue(RecepientType.BCC_TYPE);
        receptionTypeItemList.add(item);

        loadMailSettingList();
        populateOperators();
    }

    public String gotoReceipientSetupPage() {
        FinultimateCommons.captureRequestingResource();
        return "recepientSetup.jsf";
    }

    public String backToCallerPage() {
        return FinultimateCommons.retrieveLastRequestingResource();
    }

    public void mailBodySelected(ValueChangeEvent vce) {
    }

    public void messageContentSelected(ValueChangeEvent vce) {
    }

    public void messageTypeSelected(ValueChangeEvent vce) {
    }

    public MailObject populateTypeParameters(MailObject mail, EmailSettingEntity emailSetting) {
        mail.setMailBodyType(emailSetting.getMailBodyType());
        mail.setMessageContentType(emailSetting.getMessageContentType());
        mail.setMessageContentType(emailSetting.getMessageContentType());

        mail.setMessageType(emailSetting.getMessageType());

        return mail;
    }

    public void deselectOtherEmailSettings(int emailID) {
        List<EmailSettingEntity> emailList = new ArrayList<EmailSettingEntity>();
        for (EmailSettingEntity email : getEmailSettingList()) {
            if (email.getEmailSettingID() != emailID) {
                email.setSelected(false);
            }
            emailList.add(email);
        }
        emailSettingList = emailList;
    }

    public void emailSettingChanged(ValueChangeEvent vce) {
        int emailID = Integer.parseInt(vce.getNewValue().toString());
        emailSetting = findEmailSettingByID(emailID);
    }

    public void emailSettingSelected(ValueChangeEvent vce) {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        if (((HtmlSelectBooleanCheckbox) vce.getComponent()).isSelected()) {
            int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
            emailSetting = emailSettingList.get(rowIndex);

            try {
                AbstractFieldMetaEntity urfmCriteria = new UlticoreMessageFieldMetaEntity();
                ((UlticoreMessageFieldMetaEntity) urfmCriteria).setMessageID(emailSetting.getEmailSettingID());
                List<AbstractEntity> baseURFMList = dataServer.findData((UlticoreMessageFieldMetaEntity) urfmCriteria);

                QueryParameterEntity rqpCriteria = new MessageQueryParameterEntity();
                ((MessageQueryParameterEntity) rqpCriteria).setMessageID(emailSetting.getEmailSettingID());
                List<AbstractEntity> baseRQPList = dataServer.findData((MessageQueryParameterEntity) rqpCriteria);

                AbstractTemplateParamEntity rtpCriteria = new MessageTemplateParamEntity();
                ((MessageTemplateParamEntity) rtpCriteria).setMessageID(emailSetting.getEmailSettingID());
                List<AbstractEntity> baseRTPList = dataServer.findData((MessageTemplateParamEntity) rtpCriteria);

                if ((baseRQPList != null) && (baseRQPList.size() > 0)) {
                    emailSetting.setQueryParamList(
                            findQueryFieldMetaList(baseRQPList, emailSetting.getEmailSettingID()));
                }

                if ((baseURFMList != null) && (baseURFMList.size() > 0)) {
                    emailSetting.setAbstractFieldMetaList(
                            findMessageFieldMetaList(baseURFMList, emailSetting.getEmailSettingID()));
                }

                if ((baseRTPList != null) && (baseRTPList.size() > 0)) {
                    emailSetting.setAbstractParameterList(
                            findMessageTemplateParamList(baseRTPList, emailSetting.getEmailSettingID()));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                applicationMessageBean.insertMessage(ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
            }

            CommonBean.deselectOtherItems(emailSetting, emailSettingList);
        }
    }

    public void recepientSelected(ValueChangeEvent vce) {
        if (((HtmlSelectBooleanCheckbox) vce.getComponent()).isSelected()) {
            int rowIndex = CommonBean.getCheckBoxEventRowIndex(vce);
            recepient = emailSetting.getRecepientList().get(rowIndex);

            CommonBean.deselectOtherItems(recepient, getEmailSetting().getRecepientList());
        }
    }

    public String addMailSetting() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        if (emailSetting.getQueryExpression().split(" ").length == 1) {
            if (!getEmailSettingList().contains(getEmailSetting())) {
                getEmailSettingList().add(getEmailSetting());
            } else {
                int rowIndex = getEmailSettingList().indexOf(getEmailSetting());
                getEmailSettingList().set(rowIndex, getEmailSetting());
            }
            setEmailSetting(new EmailSettingEntity());
        } else if (emailSetting.getQueryExpression().split(" ").length > 1) {
            applicationMessageBean.insertMessage(
                    "A Procedure/Function Name cannot have spaces.", com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public EmailSettingEntity findEmailSettingByID(int emailSettingID) {
        EmailSettingEntity email = null;

        for (EmailSettingEntity em : getEmailSettingList()) {
            if (em.getEmailSettingID() == emailSettingID) {
                email = em;
                break;
            }
        }

        return email;
    }

    public String loadMailSettingList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        if (getEmailSettingList().size() == 0) {
            EmailSettingEntity emailCriteria = new EmailSettingEntity();
            List<AbstractEntity> baseEmailList = null;

            try {
                baseEmailList = dataServer.findData(emailCriteria);

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

                MenuEntity privilege = privilegeBean.findPrivilegeByName(
                        menuManagerBean.getSystemMap().get(MenuManagerBean.MESSAGING_MENU_ITEM));

                for (AbstractEntity baseEmail : baseEmailList) {
                    EmailSettingEntity emSetting = (EmailSettingEntity) baseEmail;
                    emSetting.setActivated(emSetting.getApprovalStatusID() >= privilege.getApprovedStatusID());

                    SelectItem item = new SelectItem();
                    item.setValue(emSetting.getSubject());
                    item.setLabel(emSetting.getSubject());
                    emailSettingItemList.add(item);

                    getEmailSettingList().add(emSetting);
                    addToSelectItemList(emSetting);
                }

                EmailRecepientEntity recepientCriteria = new EmailRecepientEntity();
                baseEmailList = dataServer.findData(recepientCriteria);
                for (AbstractEntity baseRecepient : baseEmailList) {
                    EmailSettingEntity emSetting = findEmailSettingByID(
                            ((EmailRecepientEntity) baseRecepient).getEmailSettingID());

                    if (emSetting != null) {
                        emSetting.getRecepientList().add((EmailRecepientEntity) baseRecepient);
                        int rowIndex = getEmailSettingList().indexOf(emSetting);

                        getEmailSettingList().set(rowIndex, emSetting);
                    }
                }

                ReportTriggerEntity trggCriteria = new ReportTriggerEntity();
                List<AbstractEntity> baseTriggerList = dataServer.findData(trggCriteria);

                ReportScheduleEntity rschCriteria = new ReportScheduleEntity();
                List<AbstractEntity> baseScheduleList = dataServer.findData(rschCriteria);

                for (AbstractEntity baseTrigger : baseTriggerList) {
                    ReportTriggerEntity trigger = (ReportTriggerEntity) baseTrigger;
                    messageTriggerList.add(trigger);

                    SelectItem item = new SelectItem();
                    item.setValue(trigger.getReportTriggerID());
                    item.setLabel(trigger.getTriggerName());
                    messageTriggerItemList.add(item);
                }

                for (AbstractEntity baseSchedule : baseScheduleList) {
                    ReportScheduleEntity schedule = (ReportScheduleEntity) baseSchedule;

                    ReportTriggerEntity trigger = findMessageTriggerByID(schedule.getReportTriggerID());
                    if (trigger != null) {
                        schedule.setReportTriggerName(trigger.getTriggerName());
                    }
                    messageScheduleList.add(schedule);
                }

                MailGatewayConfigEntity criteria = new MailGatewayConfigEntity();
                List<AbstractEntity> entityList = dataServer.findData(criteria);
                if ((entityList != null) && (entityList.size() > 0)) {
                    mailGatewayConfig = (MailGatewayConfigEntity) entityList.get(0);
                }

                SelectItem item = new SelectItem();
                item.setValue(AbstractTemplateDataType.STRING_TYPE);
                item.setLabel(AbstractTemplateDataType.STRING_TYPE.toString());
                parameterValueTypeItemList.add(item);

                item = new SelectItem();
                item.setValue(AbstractTemplateDataType.INTEGER_TYPE);
                item.setLabel(AbstractTemplateDataType.INTEGER_TYPE.toString());
                parameterValueTypeItemList.add(item);

                item = new SelectItem();
                item.setValue(AbstractTemplateDataType.MONEY_DIGIT_TYPE);
                item.setLabel(AbstractTemplateDataType.MONEY_DIGIT_TYPE.toString());
                getParameterValueTypeItemList().add(item);

                item = new SelectItem();
                item.setValue(AbstractTemplateDataType.MONEY_WORD_TYPE);
                item.setLabel(AbstractTemplateDataType.MONEY_WORD_TYPE.toString());
                getParameterValueTypeItemList().add(item);

                item = new SelectItem();
                item.setValue(AbstractTemplateDataType.FLOATING_POINT_TYPE);
                item.setLabel(AbstractTemplateDataType.FLOATING_POINT_TYPE.toString());
                parameterValueTypeItemList.add(item);

                item = new SelectItem();
                item.setValue(AbstractTemplateDataType.DATE_TYPE);
                item.setLabel(AbstractTemplateDataType.DATE_TYPE.toString());
                parameterValueTypeItemList.add(item);

                item = new SelectItem();
                item.setValue(DefaultSystemGeneratedFields.NONE);
                item.setLabel(DefaultSystemGeneratedFields.NONE.toString());
                systemGeneratedFieldItemList.add(item);

                item = new SelectItem();
                item.setValue(DefaultSystemGeneratedFields.CURRENT_DATE);
                item.setLabel(DefaultSystemGeneratedFields.CURRENT_DATE.toString());
                systemGeneratedFieldItemList.add(item);

                item = new SelectItem();
                item.setValue(DefaultSystemGeneratedFields.CURRENT_TIMESTAMP);
                item.setLabel(DefaultSystemGeneratedFields.CURRENT_TIMESTAMP.toString());
                systemGeneratedFieldItemList.add(item);

                item = new SelectItem();
                item.setValue(DefaultSystemGeneratedFields.CURRENT_USER);
                item.setLabel(DefaultSystemGeneratedFields.CURRENT_USER.toString());
                systemGeneratedFieldItemList.add(item);

                item = new SelectItem();
                item.setValue(DefaultSystemGeneratedFields.INCREMENTAL_COUNT);
                item.setLabel(DefaultSystemGeneratedFields.INCREMENTAL_COUNT.toString());
                systemGeneratedFieldItemList.add(item);

                item = new SelectItem();
                item.setValue(DefaultSystemGeneratedFields.DAY_OF_YEAR);
                item.setLabel(DefaultSystemGeneratedFields.DAY_OF_YEAR.toString());
                systemGeneratedFieldItemList.add(item);

                item = new SelectItem();
                item.setValue(DefaultSystemGeneratedFields.WEEK_OF_YEAR);
                item.setLabel(DefaultSystemGeneratedFields.WEEK_OF_YEAR.toString());
                systemGeneratedFieldItemList.add(item);

                item = new SelectItem();
                item.setValue(SQLStatementType.SELECT_STATEMENT);
                item.setLabel(SQLStatementType.SELECT_STATEMENT.toString());
                sqlStmtItemList.add(item);

                item = new SelectItem();
                item.setValue(SQLStatementType.CALL_STATEMENT);
                item.setLabel(SQLStatementType.CALL_STATEMENT.toString());
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
            } catch (Exception ex) {
                ex.printStackTrace();
                applicationMessageBean.insertMessage(ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
            }
        }

        return "";
    }

    public String loadMailSettingList(BusinessActionTrailEntity businessActionTrail) {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        emailSettingList = new ArrayList<EmailSettingEntity>();

        EmailSettingEntity emailCriteria = new EmailSettingEntity();
        List<AbstractEntity> baseEmailList = null;
        try {
            baseEmailList = dataServer.findMasterData(emailCriteria, businessActionTrail.getEntityMasterID());

            for (AbstractEntity baseEmail : baseEmailList) {
                EmailSettingEntity emSetting = (EmailSettingEntity) baseEmail;
                emSetting.setActionTrail((BusinessActionTrailEntity) businessActionTrail.cloneEntity());
                getEmailSettingList().add(emSetting);
            }

            EmailRecepientEntity recepientCriteria = new EmailRecepientEntity();
            baseEmailList = dataServer.findData(recepientCriteria);
            for (AbstractEntity baseRecepient : baseEmailList) {
                EmailSettingEntity emSetting = findEmailSettingByID(
                        ((EmailRecepientEntity) baseRecepient).getEmailSettingID());

                if (emSetting != null) {
                    emSetting.getRecepientList().add((EmailRecepientEntity) baseRecepient);
                    int rowIndex = getEmailSettingList().indexOf(emSetting);

                    getEmailSettingList().set(rowIndex, emSetting);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public void addToSelectItemList(EmailSettingEntity entity) {
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
                    menuManagerBean.getSystemMap().get(MenuManagerBean.MESSAGING_MENU_ITEM));

            if (entity.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                SelectItem item = new SelectItem();
                item.setValue(entity.getEmailSettingID());
                item.setLabel(entity.getSubject());
                mailSubjectItemList.add(item);
            }
        } else if (CommonBean.USER_ADMIN_SWITCH == UserAdminSwitchType.OFF) {
            SelectItem item = new SelectItem();
            item.setValue(entity.getEmailSettingID());
            item.setLabel(entity.getSubject());
            mailSubjectItemList.add(item);
        }
    }

    public String saveMailSetting() {
        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        try {
            for (EmailSettingEntity email : getEmailSettingList()) {
                if (email.getEmailSettingID() > 0) {
                    dataServer.updateData(email);
                    saveMessageTemplateList(email);
                } else {
                    try {
                        int emailID = Integer.parseInt(appPropBean.getValue(EMAIL_SETTING_ID_KEY,
                                EMAIL_SETTING_ID_DEFAULT, true));
                        email.setEmailSettingID(emailID);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    dataServer.saveData(email);
                    saveMessageTemplateList(email);
                }

                for (EmailRecepientEntity recep : email.getRecepientList()) {
                    recep.setEmailSettingID(email.getEmailSettingID());

                    if (recep.getReceipeintID() == 0) {
                        try {
                            int receiptID = Integer.parseInt(appPropBean.getValue(RECEPIENT_ID_KEY,
                                    RECEPIENT_ID_DEFAULT, true));
                            recep.setReceipeintID(receiptID);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        dataServer.saveData(recep);
                    } else {
                        dataServer.updateData(recep);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public String saveMessageTemplateList(EmailSettingEntity messageSetting) throws SQLException, Exception {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }

        for (QueryParameterEntity param : messageSetting.getQueryParamList()) {
            if (param.getQueryParameterID() == 0) {
                int paramID = Integer.parseInt(appPropBean.getValue(
                        QUERY_FIELD_ID_KEY,
                        QUERY_FIELD_ID_DEFAULT, true));
                param.setQueryParameterID(paramID);
                ((MessageQueryParameterEntity) param).setMessageID(messageSetting.getEmailSettingID());

                dataServer.saveData((MessageQueryParameterEntity) param);
            } else {
                ((MessageQueryParameterEntity) param).setMessageID(messageSetting.getEmailSettingID());
                dataServer.updateData((MessageQueryParameterEntity) param);
            }
        }

        for (AbstractFieldMetaEntity field : messageSetting.getAbstractFieldMetaList()) {
            if (field.getEntityMetaNameID() == 0) {
                int fieldID = Integer.parseInt(appPropBean.getValue(MSSG_FIELD_ID_KEY,
                        MSSG_FIELD_ID_DEFAULT, true));

                field.setEntityMetaNameID(fieldID);
                ((UlticoreMessageFieldMetaEntity) field).setMessageID(messageSetting.getEmailSettingID());

                dataServer.saveData((UlticoreMessageFieldMetaEntity) field);
            } else {
                ((UlticoreMessageFieldMetaEntity) field).setMessageID(messageSetting.getEmailSettingID());
                dataServer.updateData((UlticoreMessageFieldMetaEntity) field);
            }
        }

        for (AbstractTemplateParamEntity param : messageSetting.getAbstractParameterList()) {
            if (param.getParameterID() == 0) {
                int paramID = Integer.parseInt(appPropBean.getValue(MSSG_PARAM_ID_KEY,
                        MSSG_PARAM_ID_DEFAULT, true));
                param.setParameterID(paramID);
                ((MessageTemplateParamEntity) param).setMessageID(messageSetting.getEmailSettingID());

                dataServer.saveData((MessageTemplateParamEntity) param);
            } else {
                ((MessageTemplateParamEntity) param).setMessageID(messageSetting.getEmailSettingID());
                dataServer.updateData((MessageTemplateParamEntity) param);
            }
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
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

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
                menuManagerBean.getSystemMap().get(MenuManagerBean.MESSAGING_MENU_ITEM));

        if (emailSetting.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((emailSetting.getApprovalStatusID() == 0) || (emailSetting.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(emailSetting.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        emailSetting.setApprovalStatusID(emailSetting.getApprovalStatusID() + 1);
                        emailSetting.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(emailSetting);
                        } catch (SQLException ex) {
                            dataServer.rollbackTransaction();
                            applicationMessageBean.insertMessage(
                                    ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        dataServer.endTransaction();
                        applicationMessageBean.insertMessage(
                                "Operation Activated Successfully!", com.rsdynamix.dynamo.messages.MessageType.SUCCESS_MESSAGE);
                    } else if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            == creatorAcct.getRole().getApprovalLevelID()) {
                        applicationMessageBean.insertMessage("You cannot approve an operation performed "
                                + "by another user who has the same approval level as you!",
                                com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
                    } else if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            < creatorAcct.getRole().getApprovalLevelID()) {
                        applicationMessageBean.insertMessage("You cannot approve an operation performed "
                                + "by another user who has a higher approval level as you!",
                                com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
                    }
                } else if (emailSetting.getApprovalStatusID()
                        > userManagerBean.getUserAccount().getRole().getApprovalLevelID()) {
                    applicationMessageBean.insertMessage("This operation has been approved beyond "
                            + "your approval level, your approval is no longer effective!",
                            com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
                }
            } else {
                applicationMessageBean.insertMessage("Access Denied: You do not have approval rights for this operation type!",
                        com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
            }
        } else {
            applicationMessageBean.insertMessage("This operation is already approved. No action performed!",
                    com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String addRecepientSetting() {
        if (!emailSetting.getRecepientList().contains(getRecepient())) {
            emailSetting.getRecepientList().add(getRecepient());
        } else {
            int rowIndex = emailSetting.getRecepientList().indexOf(getRecepient());
            emailSetting.getRecepientList().set(rowIndex, getRecepient());
        }
        setRecepient(new EmailRecepientEntity());

        return "";
    }

    public String removeRecepientSetting() {
        if (emailSetting.getRecepientList().contains(getRecepient())) {
            emailSetting.getRecepientList().remove(getRecepient());
            setRecepient(new EmailRecepientEntity());
        }

        return "";
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

    public String startMessagingScheduler() {
        scheduler = new CourrierScheduler();

        for (ReportScheduleEntity sched : messageScheduleList) {
            if (sched.getReportTriggerID() > 0) {
                ReportTriggerEntity trigger = findMessageTriggerByID(sched.getReportTriggerID());
                if(trigger != null) {
                    scheduler.startSchedulerService(sched, trigger, this);
                }
            }
        }

        return "";
    }

    public String stopMessagingScheduler() {
        try {
            scheduler.stopSchedulerService();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    public String addMessageTrigger() {
        if (!messageTriggerList.contains(messageTrigger)) {
            messageTriggerList.add(messageTrigger);
        } else {
            int index = messageTriggerList.indexOf(messageTrigger);
            messageTriggerList.set(index, messageTrigger);
        }
        messageTrigger = new ReportTriggerEntity();

        return "";
    }

    public String deleteMessageTrigger() {
        if (messageTriggerList.contains(messageTrigger)) {
            messageTriggerList.remove(messageTrigger);
            messageTrigger = new ReportTriggerEntity();
        }

        return "";
    }

    public String saveMessageTriggerList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        try {
            for (ReportTriggerEntity trigger : messageTriggerList) {
                if (trigger.getReportTriggerID() == 0) {
                    try {
                        int cmID = Integer.parseInt(appPropBean.getValue(MSSG_TRIGGER_ID_KEY,
                                MSSG_TRIGGER_ID_DEFAULT, true));
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
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void messageTriggerSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        messageTrigger = messageTriggerList.get(rowIndex);
    }

    public String addMessageSchedule() {
        if (messageSchedule.getReportTriggerID() != -1) {
            ReportTriggerEntity trigger = findMessageTriggerByID(messageSchedule.getReportTriggerID());
            if (trigger != null) {
                messageSchedule.setReportTriggerName(trigger.getTriggerName());
            }
        }

        if (!messageScheduleList.contains(messageSchedule)) {
            messageScheduleList.add(messageSchedule);
        } else {
            int index = messageScheduleList.indexOf(messageSchedule);
            messageScheduleList.set(index, messageSchedule);
        }
        messageSchedule = new ReportScheduleEntity();

        return "";
    }

    public String deleteMessageSchedule() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        try {
            if (messageScheduleList.contains(messageSchedule)) {
                if (messageSchedule.isActivated()) {
                    applicationMessageBean.insertMessage(
                            "This Item has been Activated. Deletion of Activated Items is not allowed",
                            com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
                    return "";
                }
                messageScheduleList.remove(messageSchedule);

                if (messageSchedule.getReportScheduleID() > 0) {
                    ReportScheduleEntity criteria = new ReportScheduleEntity();
                    criteria.setReportScheduleID(messageSchedule.getReportScheduleID());
                    dataServer.beginTransaction();
                    dataServer.deleteData(criteria);
                    dataServer.endTransaction();
                }

                messageParamter = new MessageTemplateParamEntity();

                applicationMessageBean.insertMessage(
                        "Message Schedule has been deleted", com.rsdynamix.dynamo.messages.MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String saveMessageScheduleList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class, appPropBean);
        }

        try {
            for (ReportScheduleEntity trigger : messageScheduleList) {
                if (trigger.getReportScheduleID() == 0) {
                    try {
                        int cmID = Integer.parseInt(appPropBean.getValue(MSSG_SCHEDULE_ID_KEY,
                                MSSG_SCHEDULE_ID_DEFAULT, true));
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
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public ReportScheduleEntity findMessageScheduleByID(long schedID) {
        ReportScheduleEntity schedule = null;

        for (ReportScheduleEntity sched : messageScheduleList) {
            if (sched.getReportScheduleID() == schedID) {
                schedule = sched;
                break;
            }
        }

        return schedule;
    }

    public void messageScheduleSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        messageSchedule = messageScheduleList.get(rowIndex);
    }

    public ReportTriggerEntity findMessageTriggerByID(long triggerID) {
        ReportTriggerEntity trigger = null;

        for (ReportTriggerEntity rept : messageTriggerList) {
            if (rept.getReportTriggerID() == triggerID) {
                trigger = rept;
                break;
            }
        }

        return trigger;
    }

    public String addMessageParameter() {
        if (!emailSetting.getAbstractParameterList().contains(messageParamter)) {
            emailSetting.getAbstractParameterList().add(messageParamter);
            messageParamter = new MessageTemplateParamEntity();
        } else {
            int index = emailSetting.getAbstractParameterList().indexOf(messageParamter);
            emailSetting.getAbstractParameterList().set(index, messageParamter);
            messageParamter = new MessageTemplateParamEntity();
        }

        return "";
    }

    public String deleteMessageParameter() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        try {
            if (emailSetting.getAbstractParameterList().contains(messageParamter)) {
                FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
                AbstractTemplateParamEntity criteria = new MessageTemplateParamEntity();
                criteria.setParameterID(messageParamter.getParameterID());
                dataServer.deleteData((AbstractEntity) criteria);

                emailSetting.getAbstractParameterList().remove(messageParamter);
                messageParamter = new MessageTemplateParamEntity();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public void messageParamSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        messageParamter = emailSetting.getAbstractParameterList().get(rowIndex);

        CommonBean.deselectOtherItems((AbstractEntity) messageParamter, emailSetting.getAbstractParameterList());
    }

    public String moveToQueryPage() {
        return "/message/messagequeryfilter.jsf";
    }

    public List<AbstractFieldMetaEntity> findMessageFieldMetaList(List<AbstractEntity> baseURFMList, long messagrID) {
        List<AbstractFieldMetaEntity> reportFieldMetaList = new ArrayList<AbstractFieldMetaEntity>();

        for (AbstractEntity baseURFM : baseURFMList) {
            AbstractFieldMetaEntity field = (AbstractFieldMetaEntity) baseURFM;

            //Note: the following is done on purpose
            field.setParameterTypeFQN(field.getParameterTypeFQN());

            if (((UlticoreMessageFieldMetaEntity) field).getMessageID() == messagrID) {
                reportFieldMetaList.add(field);
            }
        }

        return reportFieldMetaList;
    }

    public List<QueryParameterEntity> findQueryFieldMetaList(List<AbstractEntity> baseRQPList, long messageID) {
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

            if (((MessageQueryParameterEntity) field).getMessageID() == messageID) {
                SelectItem item = valueListBean.findValueCategoryTypeItem(field.getParameterValueSourceID());
                if (item != null) {
                    field.setParameterValueSourceDesc(item.getLabel());
                }
                reportQueryFieldList.add(field);
            }
        }

        return reportQueryFieldList;
    }

    public List<AbstractTemplateParamEntity> findMessageTemplateParamList(List<AbstractEntity> baseRTPList, long messageID) {
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
            if (((MessageTemplateParamEntity) param).getMessageID() == messageID) {
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

    public void messageTemplateSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        emailSetting = emailSettingList.get(rowIndex);

        messageTemplateSelected(emailSetting);
    }

    public void messageTemplateSelected(EmailSettingEntity emailSetting) {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        try {
            AbstractFieldMetaEntity urfmCriteria = new UlticoreMessageFieldMetaEntity();
            ((UlticoreMessageFieldMetaEntity) urfmCriteria).setMessageID(emailSetting.getEmailSettingID());
            List<AbstractEntity> baseURFMList = dataServer.findData((UlticoreMessageFieldMetaEntity) urfmCriteria);

            QueryParameterEntity rqpCriteria = new MessageQueryParameterEntity();
            ((MessageQueryParameterEntity) rqpCriteria).setMessageID(emailSetting.getEmailSettingID());
            List<AbstractEntity> baseRQPList = dataServer.findData((MessageQueryParameterEntity) rqpCriteria);

            AbstractTemplateParamEntity rtpCriteria = new MessageTemplateParamEntity();
            ((MessageTemplateParamEntity) rtpCriteria).setMessageID(emailSetting.getEmailSettingID());
            List<AbstractEntity> baseRTPList = dataServer.findData((MessageTemplateParamEntity) rtpCriteria);

            if ((baseRQPList != null) && (baseRQPList.size() > 0)) {
                emailSetting.setQueryParamList(
                        findQueryFieldMetaList(baseRQPList, emailSetting.getEmailSettingID()));
            }

            if ((baseURFMList != null) && (baseURFMList.size() > 0)) {
                emailSetting.setAbstractFieldMetaList(findMessageFieldMetaList(baseURFMList, emailSetting.getEmailSettingID()));
            }

            if ((baseRTPList != null) && (baseRTPList.size() > 0)) {
                emailSetting.setAbstractParameterList(findMessageTemplateParamList(baseRTPList, emailSetting.getEmailSettingID()));
            }

            CommonBean.deselectOtherItems(emailSetting, emailSettingList);
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }
    }

    public void messageTemplateSelectedForScheduler(EmailSettingEntity emailSetting) {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        try {
            AbstractFieldMetaEntity urfmCriteria = new UlticoreMessageFieldMetaEntity();
            ((UlticoreMessageFieldMetaEntity) urfmCriteria).setMessageID(emailSetting.getEmailSettingID());
            List<AbstractEntity> baseURFMList = dataServer.findData((UlticoreMessageFieldMetaEntity) urfmCriteria);

            QueryParameterEntity rqpCriteria = new MessageQueryParameterEntity();
            ((MessageQueryParameterEntity) rqpCriteria).setMessageID(emailSetting.getEmailSettingID());
            List<AbstractEntity> baseRQPList = dataServer.findData((MessageQueryParameterEntity) rqpCriteria);

            AbstractTemplateParamEntity rtpCriteria = new MessageTemplateParamEntity();
            ((MessageTemplateParamEntity) rtpCriteria).setMessageID(emailSetting.getEmailSettingID());
            List<AbstractEntity> baseRTPList = dataServer.findData((MessageTemplateParamEntity) rtpCriteria);

            if ((baseRQPList != null) && (baseRQPList.size() > 0)) {
                emailSetting.setQueryParamList(
                        findQueryFieldMetaList(baseRQPList, emailSetting.getEmailSettingID()));
            }

            if ((baseURFMList != null) && (baseURFMList.size() > 0)) {
                emailSetting.setAbstractFieldMetaList(findMessageFieldMetaList(baseURFMList, emailSetting.getEmailSettingID()));
            }

            if ((baseRTPList != null) && (baseRTPList.size() > 0)) {
                emailSetting.setAbstractParameterList(findMessageTemplateParamList(baseRTPList, emailSetting.getEmailSettingID()));
            }

            CommonBean.deselectOtherItems(emailSetting, emailSettingList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void fieldMetaNameSelectedForQueryField(ValueChangeEvent vce) {
        parameter.setParamterValue(vce.getNewValue().toString());
    }

    public void messageContentTypeMenuSelected(ValueChangeEvent vce) {
        String reportTitle = vce.getNewValue().toString();
        emailSetting.setReportName(reportTitle);
    }

    public void messageTemplateMenuSelected(ValueChangeEvent vce) {
        String reportTitle = vce.getNewValue().toString();
        if (!reportTitle.equals("-1")) {
            populateSelectedMessageItemList(reportTitle);
        }
    }

    public void populateSelectedMessageItemList(String reportTitle) {
        EmailSettingEntity report = findMessageByTitle(reportTitle);
        messageTemplateSelected(report);

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
            parameter.setParameterValueSourceDesc(item.getLabel());
        }
    }

    public void parameterDataTypeSelected(ValueChangeEvent vce) {
        String rendererVal = vce.getNewValue().toString();
        parameter.setParameterTypeFQN(rendererVal);
    }

    public void queryFieldSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        parameter = emailSetting.getQueryParamList().get(rowIndex);

        CommonBean.deselectOtherItems((AbstractEntity) parameter, emailSetting.getQueryParamList());
    }

    public void reportFieldSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        messageFieldMeta = (UlticoreMessageFieldMetaEntity) emailSetting.getAbstractFieldMetaList().get(rowIndex);

        CommonBean.deselectOtherItems((AbstractEntity) messageFieldMeta, emailSetting.getAbstractFieldMetaList());
    }

    public String moveToQueryFieldSetupPage() {
        FinultimateCommons.captureRequestingResource();
        return "/messaging/queryfieldSetup.jsf";
    }

    public String moveToMessageFieldSetupPage() {
        FinultimateCommons.captureRequestingResource();
        return "/messaging/messagefieldSetup.jsf";
    }

    public String moveToMessageParamSetupPage() {
        FinultimateCommons.captureRequestingResource();
        return "/messaging/messageparamsetup.jsf";
    }

    public String addQueryField() {
        if (!emailSetting.getQueryParamList().contains(parameter)) {
            emailSetting.getQueryParamList().add(parameter);
        } else {
            int index = emailSetting.getQueryParamList().indexOf(parameter);
            emailSetting.getQueryParamList().set(index, parameter);
        }
        parameter = new MessageQueryParameterEntity();

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
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        try {
            if (emailSetting.getQueryParamList().contains(parameter)) {
                FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

                QueryParameterEntity criteria = new MessageQueryParameterEntity();
                criteria.setQueryParameterID(parameter.getQueryParameterID());
                dataServer.deleteData((AbstractEntity) criteria);

                emailSetting.getQueryParamList().remove(parameter);
                parameter = new MessageQueryParameterEntity();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String loadTableMeta() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
        databaseTableFieldItemList = new ArrayList<SelectItem>();

        String queryString = "EXEC SP_COLUMNS " + emailSetting.getDatabaseTableName();
        try {
            List<String> fieldMetaList = dataServer.getFieldMetaNameList(
                    emailSetting.getTnsEntryKey(), queryString);

            for (String fieldMeta : fieldMetaList) {
                SelectItem item = new SelectItem();
                item.setValue(fieldMeta);
                item.setLabel(fieldMeta);

                databaseTableFieldItemList.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public List<Object> findFieldValueList(String fieldName) {
        int columnCount = 0;

        for (AbstractFieldMetaEntity fieldMeta : emailSetting.getAbstractFieldMetaList()) {
            if (fieldMeta.getEntityFieldName().equals(fieldName)) {
                break;
            }
            columnCount++;
        }

        return getFieldColumnValueList(emailSetting, columnCount);
    }

    public List<Object> getFieldColumnValueList(EmailSettingEntity report, int columnPosition) {
        List<Object> colValueList = new ArrayList<Object>();
        for (List<Object> rowList : report.getObjectFieldList()) {
            colValueList.add(rowList.get(columnPosition));
        }

        return colValueList;
    }

    public EmailSettingEntity findMessageByTitle(String subject) {
        EmailSettingEntity report = null;

        for (EmailSettingEntity rept : emailSettingList) {
            if (rept.getSubject().equals(subject)) {
                report = rept;
                break;
            }
        }

        return report;
    }

    public SelectItem findTableMetaItem(String itemVal) {
        SelectItem item = null;

        for (SelectItem itm : databaseTableFieldItemList) {
            if (itm.getValue().toString().equals(itemVal)) {
                item = itm;
                break;
            }
        }

        return item;
    }

    public String addMessageField() {
        if (!emailSetting.getAbstractFieldMetaList().contains(messageFieldMeta)) {
            emailSetting.getAbstractFieldMetaList().add(messageFieldMeta);
        } else {
            int index = emailSetting.getAbstractFieldMetaList().indexOf(messageFieldMeta);
            emailSetting.getAbstractFieldMetaList().set(index, messageFieldMeta);
        }
        messageFieldMeta = new UlticoreMessageFieldMetaEntity();

        return "";
    }

    public String deleteMessageField() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        try {
            AbstractFieldMetaEntity criteria = new UlticoreMessageFieldMetaEntity();
            criteria.setEntityMetaNameID(messageFieldMeta.getEntityMetaNameID());

            dataServer.deleteData((AbstractEntity) criteria);

            if (emailSetting.getAbstractFieldMetaList().contains(messageFieldMeta)) {
                emailSetting.getAbstractFieldMetaList().remove(messageFieldMeta);
            }
            messageFieldMeta = new UlticoreMessageFieldMetaEntity();
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String addMessageTemplate() {
        if (emailSetting.getSqlStatementType() == null) {
            emailSetting.setSqlStatementType(SQLStatementType.SELECT_STATEMENT);
        }

        if (!emailSettingList.contains(emailSetting)) {
            emailSettingList.add(emailSetting);
        } else {
            int index = emailSettingList.indexOf(emailSetting);
            emailSettingList.set(index, emailSetting);
        }
        emailSetting = new EmailSettingEntity();

        return "";
    }

    public String cloneMessageTemplate() {
        EmailSettingEntity entity = emailSetting.cloneEntity();
        entity.setEmailSettingID(0);

        emailSettingList.add(entity);
        emailSetting = new EmailSettingEntity();

        return "";
    }

    public String moveToSubreportPage() {
        FinultimateCommons.captureRequestingResource();
        return "subreportconfigurer";
    }

    public String deleteMessageTemplate() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        try {
            if (emailSettingList.contains(emailSetting)) {
                if (emailSetting.isActivated()) {
                    applicationMessageBean.insertMessage("This Item has been Activated. Deletion of Activated Items is not allowed", com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
                    return "";
                }
                dataServer.beginTransaction();
                for (QueryParameterEntity rqpEntity : emailSetting.getQueryParamList()) {
                    QueryParameterEntity criteria = new MessageQueryParameterEntity();
                    criteria.setQueryParameterID(rqpEntity.getQueryParameterID());
                    dataServer.deleteData((MessageQueryParameterEntity) criteria);
                }
                parameter = new MessageQueryParameterEntity();

                for (AbstractFieldMetaEntity urfmEntity : emailSetting.getAbstractFieldMetaList()) {
                    AbstractFieldMetaEntity criteria = new UlticoreMessageFieldMetaEntity();
                    criteria.setEntityMetaNameID(urfmEntity.getEntityMetaNameID());

                    dataServer.deleteData((UlticoreMessageFieldMetaEntity) criteria);
                }
                messageFieldMeta = new UlticoreMessageFieldMetaEntity();

                for (AbstractTemplateParamEntity rtpEntity : emailSetting.getAbstractParameterList()) {
                    AbstractTemplateParamEntity criteria = new MessageTemplateParamEntity();
                    criteria.setParameterID(rtpEntity.getParameterID());
                    dataServer.deleteData((MessageTemplateParamEntity) criteria);
                }
                messageParamter = new MessageTemplateParamEntity();

                EmailSettingEntity rptCriteria = new EmailSettingEntity();
                rptCriteria.setEmailSettingID(emailSetting.getEmailSettingID());

                dataServer.deleteData(rptCriteria);

                emailSettingList.remove(emailSetting);
                emailSetting = new EmailSettingEntity();

                dataServer.endTransaction();
                applicationMessageBean.insertMessage("Message Template has been deleted", com.rsdynamix.dynamo.messages.MessageType.SUCCESS_MESSAGE);
            }
        } catch (Exception ex) {
            dataServer.rollbackTransaction();
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String filterQueryAndLoadMessage() {
        String outcome = "messagequeryresult";

        servletSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
        emailSetting.setObjectFieldList(new ArrayList<List<Object>>());

        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}", UserManagerBean.class, userManagerBean);
        }

        UlticoreReportBean ulticoreReportBean = (UlticoreReportBean) CommonBean.getBeanFromContext(
                "#{sessionScope.ulticoreReportBean}", UlticoreReportBean.class);
        if (ulticoreReportBean == null) {
            ulticoreReportBean = new UlticoreReportBean();
            CommonBean.setBeanToContext("#{sessionScope.ulticoreReportBean}", UlticoreReportBean.class, ulticoreReportBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        List<AbstractQueryParameter> qpEntityList = new ArrayList<AbstractQueryParameter>();
        List<QueryParameterEntity> rptQueryEntityList = new ArrayList<QueryParameterEntity>();
        
        for (QueryParameterEntity qpEntity : emailSetting.getQueryParamList()) {
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
        
        emailSetting.setQueryParamList(rptQueryEntityList);

        try {
            if ((emailSetting.getMessageContentType() == MessageContentType.HTML_MESSAGE)
                    && (emailSetting.getMailBody().trim().length() == 0)
                    && (emailSetting.getReportName().trim().length() > 0)) {
                UlticoreReportEntity report = ulticoreReportBean.findReportByTitle(emailSetting.getReportName());
                if (report != null) {
                    report = ulticoreReportBean.reportTemplateSelected(report);
                    report = ulticoreReportBean.queryAndLoadReportAutomatically(report);

                    String htmlContent = executeHtmlMessageFromTemplate(emailSetting, report);

                    MailObject mail = new MailObject();
                    mail.setBody(htmlContent);
                    mail.setSubject(emailSetting.getSubject());
                    mail = populateTypeParameters(mail, emailSetting);
                    
                    ActualMessage actualMessage = new ActualMessage();
                    
                    actualMessage.setMessageSubject(emailSetting.getSubject());
                    actualMessage.setMessageBody(htmlContent);
                    
                    generatedMessageStack.add(actualMessage);
                }
            } else {
                List<List<AbstractDataField>> fieldDataMatrix = null;
                generatedMessageStack = new ArrayList<ActualMessage>();

                dataServer.setSelectedTnsName(Constants.COMMONS_PU);
                fieldDataMatrix = dataServer.findNonORMData(
                        emailSetting, emailSetting.getAbstractFieldMetaList(), qpEntityList, CommonBean.SQL_STYLE);

                SendMessageRemote messageService = FinanceServiceLocator.locateMailMessageServer();

                for (List<AbstractDataField> record : fieldDataMatrix) {
                    MailObject mail = new MailObject();
                    mail.setBody(emailSetting.getMailBody());
                    mail.setSubject(emailSetting.getSubject());
                    mail = populateTypeParameters(mail, emailSetting);

                    ActualMessage actualMessage = new ActualMessage();
                    String actualSubject = mail.getSubject();

                    for (AbstractDataField field : record) {
                        if (field.getFieldName().equals(RECEPIENT_NAME_KEY)) {
                            actualMessage.setRecepientEmail(field.getFieldValue().toString());
                        } else {
                            AbstractFieldMetaEntity fieldMeta = emailSetting.findReportFieldMetaByDBField(field.getFieldName());
                            if (fieldMeta != null) {
                                MailParameter param = new MailParameter();
                                param.setKey(fieldMeta.getEntityFieldName());
                                if (field.getFieldValue() != null) {
                                    param.setValue(field.getFieldValue().toString());
                                }
                                mail.getMailParamterList().add(param);

                                String dunaParamName = "\\" + ServiceConfig.DEFAULT_PARAMETER_START_CHAR
                                        + param.getKey() + ServiceConfig.DEFAULT_PARAMETER_END_CHAR;

                                actualSubject = actualSubject.replaceAll(dunaParamName, param.getValue());
                            }
                        }
                    }

                    actualMessage.setMessageSubject(actualSubject);
                    actualMessage.setMessageBody(messageService.processMailFromTemplate(mail));

                    generatedMessageStack.add(actualMessage);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }
        
        FinultimateCommons.captureRequestingResource();

        return outcome;
    }

    public String executeHtmlMessageFromTemplate(EmailSettingEntity emailSetting, UlticoreReportEntity report) throws Exception {
        String processingResourceUrl = "";

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        servletSession.setAttribute(report.getReportTitle(), report);
        servletSession.setAttribute(ReportAttributes.REPORT_KEY_PARAM, report.getReportTitle());

        reportExportFormat = com.rsdynamix.bi.projects.web.commons.bean.ReportFormat.HTML.toString();

        if (report.getReportBodyType() == ReportBodyType.INTUITIVE) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_BODY_TYPE_PARAM, ReportBodyType.INTUITIVE.toString());

            processingResourceUrl = ReportAttributes.REPORT_TYPE_PARAM + "=INTUITIVE{S}&"
                    + ReportAttributes.PRINT_TYPE + "=" + reportExportFormat + "{S}";
        } else if (report.getReportBodyType() == ReportBodyType.TEMPLATE) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_BODY_TYPE_PARAM, ReportBodyType.TEMPLATE.toString());

            processingResourceUrl = ReportAttributes.TEMPLATE_FILE + "=" + report.getTemplateFileName() + "{S}&"
                    + ReportAttributes.PRINT_TYPE + "=" + reportExportFormat + "{S}";
        } else if (report.getReportBodyType() == ReportBodyType.INTUIT_TEMPLATE) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_BODY_TYPE_PARAM, ReportBodyType.INTUIT_TEMPLATE.toString());

            processingResourceUrl = ReportAttributes.REPORT_TYPE_PARAM + "=INTUITIVE{S}&"
                    + ReportAttributes.TEMPLATE_FILE + "=" + report.getTemplateFileName() + "{S}&"
                    + ReportAttributes.PRINT_TYPE + "=" + reportExportFormat + "{S}";
        }

        DynamoContext.getNewCurrentInstance();
        ScopeManagerBean scopeManager = ScopeManagerBean.getNewInstance();

        ContextBuilder contextBuilder = new ContextBuilder();
        contextBuilder.buildGETParameterMap(processingResourceUrl, report);

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

        contextBuilder.buildContext(CommonBean.getContextRoot(), request, response);

        DynamoUtil dynamoUtil = DynamoUtil.getInstance();

        ReportInstanceBuilderFactoryImpl builderFactory = new ReportInstanceBuilderFactoryImpl();
        UlticoreInstance beanInstance = builderFactory.constructInstanceGraph(report);
        if (beanInstance != null) {
            dynamoUtil.putBeanToContext(beanInstance);
        }

        DynamoRequestDispatcher dispatcher = new DynamoRequestDispatcher();
        processingResourceUrl = dispatcher.executeRequest();

        return processingResourceUrl;
    }

    public String sendMessage() throws Exception {
        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}", UserManagerBean.class, userManagerBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        try {
            filterQueryAndSendMessage();
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String filterQueryAndSendMessage() throws Exception {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
        emailSetting.setObjectFieldList(new ArrayList<List<Object>>());

        List<AbstractQueryParameter> qpEntityList = new ArrayList<AbstractQueryParameter>();
        for (QueryParameterEntity qpEntity : emailSetting.getQueryParamList()) {
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
                emailSetting, emailSetting.getAbstractFieldMetaList(), qpEntityList, CommonBean.SQL_STYLE);

        SendMessageRemote messageService = FinanceServiceLocator.locateMailMessageServer();

        for (List<AbstractDataField> record : fieldDataMatrix) {
            MailObject mail = new MailObject();
            mail.setBody(emailSetting.getMailBody());
            mail.setSubject(emailSetting.getSubject());

            mail.setSenderDisplayName(emailSetting.getSenderName());
            mail.setSenderAddress(emailSetting.getSenderEmail());

            mail.getMailGatewayConfig().setMailServerIPAddress(mailGatewayConfig.getMailServerIPAddress());
            mail.getMailGatewayConfig().setMailServerPortNumber(String.valueOf(mailGatewayConfig.getMailServerPortNumber()));
            mail.getMailGatewayConfig().setPassword(mailGatewayConfig.getPassword());
            mail.getMailGatewayConfig().setSystemUser(mailGatewayConfig.getSystemUser());
            mail.getMailGatewayConfig().setUserName(mailGatewayConfig.getUserName());

            mail = populateTypeParameters(mail, emailSetting);

            for (EmailRecepientEntity recepient : emailSetting.getRecepientList()) {
                if (recepient.getRecepientType() == RecepientType.CC_TYPE) {
                    mail.getCcList().add(recepient.getRecepientEmail());
                } else if (recepient.getRecepientType() == RecepientType.BCC_TYPE) {
                    mail.getBccList().add(recepient.getRecepientEmail());
                } else if (recepient.getRecepientType() == RecepientType.TO_TYPE) {
                    if (mail.getRecepientAddress().trim().length() == 0) {
                        mail.setRecepientAddress(recepient.getRecepientEmail());
                    } else {
                        mail.setRecepientAddress(mail.getRecepientAddress() + "," + recepient.getRecepientEmail());
                    }
                }
            }

            for (AbstractDataField field : record) {
                if (field.getFieldName().equals(RECEPIENT_NAME_KEY)) {
                    if (mail.getRecepientAddress().trim().length() == 0) {
                        mail.setRecepientAddress(field.getFieldValue().toString());
                    } else {
                        mail.setRecepientAddress(mail.getRecepientAddress() + "," + field.getFieldValue().toString());
                    }
                } else {
                    AbstractFieldMetaEntity fieldMeta = emailSetting.findReportFieldMetaByDBField(field.getFieldName());
                    if (fieldMeta != null) {
                        MailParameter param = new MailParameter();
                        param.setKey(fieldMeta.getEntityFieldName());
                        if (field.getFieldValue() != null) {
                            param.setValue(field.getFieldValue().toString());
                        }
                        mail.getMailParamterList().add(param);
                    }
                }
            }

            messageService.dispatchMail(mail);
        }

        return "";
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

    public String queryAndLoadMessage() {
        FinultimateCommons.captureRequestingResource();

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();
        emailSetting.setObjectFieldList(new ArrayList<List<Object>>());

        UserManagerBean userManagerBean = (UserManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.userManagerBean}", UserManagerBean.class);
        if (userManagerBean == null) {
            userManagerBean = new UserManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.userManagerBean}", UserManagerBean.class, userManagerBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }

        List<AbstractQueryParameter> qpEntityList = new ArrayList<AbstractQueryParameter>();
        for (QueryParameterEntity qpEntity : emailSetting.getQueryParamList()) {
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
            if (emailSetting.getAbstractFieldMetaList().size() > 0) {
                List<List<AbstractDataField>> fieldDataMatrix = dataServer.findNonORMData(
                        emailSetting, emailSetting.getAbstractFieldMetaList(), qpEntityList, CommonBean.SQL_STYLE);

                if ((fieldDataMatrix != null) && (fieldDataMatrix.size() > 0)) {
                    for (List<AbstractDataField> fieldList : fieldDataMatrix) {
                        List<Object> reportFieldList = new ArrayList<Object>();
                        for (AbstractDataField field : fieldList) {
                            AbstractFieldMetaEntity fieldMeta = emailSetting.findReportFieldMetaByDBField(field.getFieldName());
                            if (fieldMeta != null) {
                                emailSetting.getParameters().put(fieldMeta.getEntityFieldName(), field.getFieldValue());

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
                        emailSetting.getObjectFieldList().add(reportFieldList);
                    }

                    for (AbstractTemplateParamEntity templateParam : emailSetting.getAbstractParameterList()) {
                        if ((templateParam.getParameterValue() != null)
                                && (!templateParam.getParameterValue().trim().equals(""))) {
                            emailSetting.getParameters().put(templateParam.getParameterName(), templateParam.getParameterValue());
                        }
                    }

                    if (emailSetting.getObjectFieldList().size() > 0) {
                        for (AbstractTemplateParamEntity param : emailSetting.getAbstractParameterList()) {
                            if (param.getDefaultValue() == DefaultSystemGeneratedFields.NONE) {
                                emailSetting.getParameters().put(param.getParameterName(), param.getParameterValue());
                            } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_DATE) {
                                if (param.getDataType() == AbstractTemplateDataType.DATE_TYPE) {
                                    emailSetting.getParameters().put(param.getParameterName(), new Date());
                                } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                                    emailSetting.getParameters().put(param.getParameterName(), DateUtil.getCurrentDateStr());
                                }
                            } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_TIMESTAMP) {
                                if (param.getDataType() == AbstractTemplateDataType.DATE_TYPE) {
                                    emailSetting.getParameters().put(param.getParameterName(), new Timestamp(0));
                                } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                                    emailSetting.getParameters().put(param.getParameterName(), DateUtil.getCurrentDateStr());
                                }
                            } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.CURRENT_USER) {
                                if (userManagerBean != null) {
                                    emailSetting.getParameters().put(param.getParameterName(), userManagerBean.getUserAccount().getUserName());
                                } else {
                                    emailSetting.getParameters().put(param.getParameterName(), "System");
                                }
                            } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.INCREMENTAL_COUNT) {
                                try {
                                    int fieldID = Integer.parseInt(appPropBean.getValue(param.getParameterName(),
                                            MSSG_FIELD_ID_DEFAULT, true));
                                    if (param.getDataType() == AbstractTemplateDataType.INTEGER_TYPE) {
                                        emailSetting.getParameters().put(param.getParameterName(), fieldID);
                                    } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                                        emailSetting.getParameters().put(param.getParameterName(), String.valueOf(fieldID));
                                    }
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.WEEK_OF_YEAR) {
                                if (param.getDataType() == AbstractTemplateDataType.INTEGER_TYPE) {
                                    emailSetting.getParameters().put(param.getParameterName(), DateUtil.getWeekOfYear(new Date()));
                                } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                                    emailSetting.getParameters().put(param.getParameterName(), String.valueOf(DateUtil.getWeekOfYear(new Date())));
                                }
                            } else if (param.getDefaultValue() == DefaultSystemGeneratedFields.DAY_OF_YEAR) {
                                if (param.getDataType() == AbstractTemplateDataType.INTEGER_TYPE) {
                                    emailSetting.getParameters().put(param.getParameterName(), DateUtil.getDayOfYear(new Date()));
                                } else if (param.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                                    emailSetting.getParameters().put(param.getParameterName(), String.valueOf(DateUtil.getDayOfYear(new Date())));
                                }
                            }
                        }

                        FacesContext context = FacesContext.getCurrentInstance();
                        servletSession = (HttpSession) context.getExternalContext().getSession(false);

                        servletSession.setAttribute(emailSetting.getDatabaseTableName(), emailSetting);
                        servletSession.setAttribute(ReportAttributes.REPORT_KEY_PARAM, emailSetting.getDatabaseTableName());
                        servletSession.setAttribute(ReportAttributes.PRINT_TYPE, ReportAttributes.FILE_AND_PAGE_OUTPUT_PRINT);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }

        return "/reports/reportqueryresult.jsf";
    }

    public EmailSettingEntity queryAndLoadMessageAutomatically(EmailSettingEntity report) throws Exception {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }

        report.setObjectFieldList(new ArrayList<List<Object>>());

        List<AbstractQueryParameter> qpEntityList = new ArrayList<AbstractQueryParameter>();
        for (QueryParameterEntity qpEntity : emailSetting.getQueryParamList()) {
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

        List<List<AbstractDataField>> fieldDataMatrix = dataServer.findNonORMData(
                emailSetting, emailSetting.getAbstractFieldMetaList(), qpEntityList, CommonBean.SQL_STYLE);

        if ((fieldDataMatrix != null) && (fieldDataMatrix.size() > 0)) {
            for (List<AbstractDataField> fieldList : fieldDataMatrix) {
                List<Object> reportFieldList = new ArrayList<Object>();
                for (AbstractDataField field : fieldList) {
                    AbstractFieldMetaEntity fieldMeta = emailSetting.findReportFieldMetaByDBField(field.getFieldName());
                    if (fieldMeta != null) {
                        emailSetting.getParameters().put(fieldMeta.getEntityFieldName(), field.getFieldValue());

                        if ((field.getFieldValue() instanceof Float)
                                || (field.getFieldValue() instanceof Double)) {
                            reportFieldList.add(BigDecimal.valueOf((Double) field.getFieldValue()));
                        } else {
                            reportFieldList.add(field.getFieldValue());
                        }
                    }
                }
                emailSetting.getObjectFieldList().add(reportFieldList);
            }

            for (AbstractTemplateParamEntity templateParam : emailSetting.getAbstractParameterList()) {
                if ((templateParam.getParameterValue() != null)
                        && (!templateParam.getParameterValue().trim().equals(""))) {
                    emailSetting.getParameters().put(templateParam.getParameterName(), templateParam.getParameterValue());
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
                                    MSSG_FIELD_ID_DEFAULT, true));
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

    public void messageFormatSelected(ValueChangeEvent vce) {
        if (vce.getNewValue().toString().equals(ReportFormat.PDF.toString())) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_FORMAT_PARAM, ReportFormat.PDF.toString());

            reportExportFormat = ReportFormat.PDF.toString();
        } else if (vce.getNewValue().toString().equals(ReportFormat.HTML.toString())) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_FORMAT_PARAM, ReportFormat.HTML.toString());

            reportExportFormat = ReportFormat.HTML.toString();
        } else if (vce.getNewValue().toString().equals(ReportFormat.RTF.toString())) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_FORMAT_PARAM, ReportFormat.RTF.toString());

            reportExportFormat = ReportFormat.RTF.toString();
        } else if (vce.getNewValue().toString().equals(ReportFormat.XLS.toString())) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_FORMAT_PARAM, ReportFormat.XLS.toString());

            reportExportFormat = ReportFormat.XLS.toString();
        }
    }

    public void messageBodyTypeSelected(ValueChangeEvent vce) {
        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }

        if (vce.getNewValue().toString().equals(ReportBodyType.INTUITIVE.toString())) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_BODY_TYPE_PARAM, ReportBodyType.INTUITIVE.toString());

            processingResourceUrl = CommonBean.getContextRoot() + "/ReportServlet?"
                    + ReportAttributes.REPORT_TYPE_PARAM + "=" + ReportAttributes.ADHOC_TYPE;
        } else if (vce.getNewValue().toString().equals(ReportBodyType.TEMPLATE.toString())) {
            servletSession.setAttribute(
                    ReportAttributes.REPORT_BODY_TYPE_PARAM, ReportBodyType.TEMPLATE.toString());

            try {
//                processingResourceUrl = appPropBean.getValue(FinultimateConstants.BI_RESOURCE_URL_ID,
//                        FinultimateConstants.BI_RESOURCE_URL_DFLT_VALUE, false);

                processingResourceUrl = CommonBean.getContextRoot() + FinultimateConstants.BI_RESOURCE_URL_DFLT_VALUE
                        + "?" + ReportAttributes.REPORT_TYPE_PARAM + "=INTUITIVE{S}";
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public String buildMessageElements() {
        List<AbstractFieldMetaEntity> reportFieldMetaList = new ArrayList<AbstractFieldMetaEntity>();
        List<QueryParameterEntity> queryParamList = new ArrayList<QueryParameterEntity>();
        List<AbstractTemplateParamEntity> reportParamterList = new ArrayList<AbstractTemplateParamEntity>();

        emailSetting.setQueryExpression(emailSetting.getQueryExpression().toUpperCase());

        String[] queryPart = emailSetting.getQueryExpression().split(" WHERE ");
        String queryWithoutFromClause = queryPart[0].substring(0, queryPart[0].indexOf(" FROM "));
        String[] queryColumnList = queryWithoutFromClause.split(",");

        for (String queryColumn : queryColumnList) {
            AbstractFieldMetaEntity fieldMeta = new UlticoreMessageFieldMetaEntity();
            AbstractTemplateParamEntity templateParam = new MessageTemplateParamEntity();

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
                QueryParameterEntity queryParam = new MessageQueryParameterEntity();

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

        emailSetting.setQueryParamList(queryParamList);
        emailSetting.setAbstractFieldMetaList(reportFieldMetaList);
        emailSetting.setAbstractParameterList(reportParamterList);

        return "";
    }

    public void messageFieldValueTypeSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            FieldValueType valueType = (FieldValueType) vce.getNewValue();
            messageFieldMeta.setFieldValueType(valueType);

            if (messageFieldMeta.getFieldValueType() == FieldValueType.DERIVED) {
                if (messageFieldMeta.getEntityFieldName().trim().length() > 0) {
                    messageFieldMeta.setRoutineSourceCode("public function compute"
                            + DynamoUtil.getFieldNameStartingWithAnUpperCaseLetter(
                                    messageFieldMeta.getEntityFieldName().trim()) + "Value() {\n\t\n}\n");
                }
            } else if (messageFieldMeta.getFieldValueType() == FieldValueType.INPUT) {
                messageFieldMeta.setRoutineSourceCode("");
            }
        }
    }

    public void messageFieldNameChanged(ValueChangeEvent vce) {
        messageFieldMeta.setDatabaseFieldName(CommonBean.columnize(vce.getNewValue().toString()));
    }

    public String printReport() {
        return "";
    }

    public String clearCache() {
        emailSetting = new EmailSettingEntity();
        recepient = new EmailRecepientEntity();

        messageFieldMeta = new UlticoreMessageFieldMetaEntity();
        parameter = new MessageQueryParameterEntity();
        messageParamter = new MessageTemplateParamEntity();

        messageTrigger = new ReportTriggerEntity();
        messageSchedule = new ReportScheduleEntity();

        processingResourceUrl = "";

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
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        if (emailSettingList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    emailSettingList.get(0).getActionTrail());
            if (batEntity != null) {
                loadMailSettingList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.",
                        com.rsdynamix.dynamo.messages.MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadPreviousHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadMailSettingList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Previous Items Left.",
                        com.rsdynamix.dynamo.messages.MessageType.INFORMATION_MESSAGE);
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
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        if (emailSettingList.size() > 0) {
            BusinessActionTrailEntity batEntity = businessActionTrailBean
                    .loadNextHistoricalState(emailSettingList.get(0).getActionTrail());
            if (batEntity != null) {
                loadMailSettingList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.",
                        com.rsdynamix.dynamo.messages.MessageType.INFORMATION_MESSAGE);
            }
        } else {
            BusinessActionTrailEntity batEntity = businessActionTrailBean.loadNextHistoricalState(
                    (BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            if (batEntity != null) {
                loadMailSettingList(batEntity);
            } else {
                applicationMessageBean.insertMessage("No Next Items Left.",
                        com.rsdynamix.dynamo.messages.MessageType.INFORMATION_MESSAGE);
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
        applicationMessageBean.insertMessage("", com.rsdynamix.dynamo.messages.MessageType.NONE);

        businessActionTrailBean.getBusinessActionTrail().setEntityNameType(EntityNameType.MESSAGING);
        businessActionTrailBean.getBusinessActionTrail().setEntityID(emailSetting.getEmailSettingID());
        businessActionTrailBean.loadBusinessActionTrailList();

        if (businessActionTrailBean.getBusinessActionTrailList().size() > 0) {
            businessActionTrailBean.setBusinessActionTrail(
                    businessActionTrailBean.getBusinessActionTrailList().get(
                            businessActionTrailBean.getBusinessActionTrailList().size() - 1));

            loadMailSettingList((BusinessActionTrailEntity) businessActionTrailBean.getBusinessActionTrail().cloneEntity());
            outcome = "mailaudittrail.jsf";
        } else {
            applicationMessageBean.insertMessage("No Audit Trail Found For This Entity!", com.rsdynamix.dynamo.messages.MessageType.ERROR_MESSAGE);
        }

        return outcome;
    }

    /**
     * @return the derivedMessageFieldValue
     */
    public boolean isDerivedMessageFieldValue() {
        derivedMessageFieldValue = messageFieldMeta.getFieldValueType() == FieldValueType.DERIVED;
        return derivedMessageFieldValue;
    }

    /**
     * @param derivedMessageFieldValue the derivedMessageFieldValue to set
     */
    public void setDerivedMessageFieldValue(boolean derivedMessageFieldValue) {
        this.derivedMessageFieldValue = derivedMessageFieldValue;
    }

    /**
     * @return the emailSetting
     */
    public EmailSettingEntity getEmailSetting() {
        return emailSetting;
    }

    /**
     * @param emailSetting the emailSetting to set
     */
    public void setEmailSetting(EmailSettingEntity emailSetting) {
        this.emailSetting = emailSetting;
    }

    /**
     * @return the recepient
     */
    public EmailRecepientEntity getRecepient() {
        return recepient;
    }

    /**
     * @param recepient the recepient to set
     */
    public void setRecepient(EmailRecepientEntity recepient) {
        this.recepient = recepient;
    }

    /**
     * @return the emailSettingList
     */
    public List<EmailSettingEntity> getEmailSettingList() {
        return emailSettingList;
    }

    /**
     * @param emailSettingList the emailSettingList to set
     */
    public void setEmailSettingList(List<EmailSettingEntity> emailSettingList) {
        this.emailSettingList = emailSettingList;
    }

    /**
     * @return the mailBodyTypeItemList
     */
    public List<SelectItem> getMailBodyTypeItemList() {
        return mailBodyTypeItemList;
    }

    /**
     * @param mailBodyTypeItemList the mailBodyTypeItemList to set
     */
    public void setMailBodyTypeItemList(List<SelectItem> mailBodyTypeItemList) {
        this.mailBodyTypeItemList = mailBodyTypeItemList;
    }

    /**
     * @return the messageContentTypeItemList
     */
    public List<SelectItem> getMessageContentTypeItemList() {
        return messageContentTypeItemList;
    }

    /**
     * @param messageContentTypeItemList the messageContentTypeItemList to set
     */
    public void setMessageContentTypeItemList(List<SelectItem> messageContentTypeItemList) {
        this.messageContentTypeItemList = messageContentTypeItemList;
    }

    /**
     * @return the messageTypeItemList
     */
    public List<SelectItem> getMessageTypeItemList() {
        return messageTypeItemList;
    }

    /**
     * @param messageTypeItemList the messageTypeItemList to set
     */
    public void setMessageTypeItemList(List<SelectItem> messageTypeItemList) {
        this.messageTypeItemList = messageTypeItemList;
    }

    /**
     * @return the receptionTypeItemList
     */
    public List<SelectItem> getReceptionTypeItemList() {
        return receptionTypeItemList;
    }

    /**
     * @param receptionTypeItemList the receptionTypeItemList to set
     */
    public void setReceptionTypeItemList(List<SelectItem> receptionTypeItemList) {
        this.receptionTypeItemList = receptionTypeItemList;
    }

    /**
     * @return the mailSubjectItemList
     */
    public List<SelectItem> getMailSubjectItemList() {
        return mailSubjectItemList;
    }

    /**
     * @param mailSubjectItemList the mailSubjectItemList to set
     */
    public void setMailSubjectItemList(List<SelectItem> mailSubjectItemList) {
        this.mailSubjectItemList = mailSubjectItemList;
    }

    /**
     * @return the messageFieldMeta
     */
    public UlticoreMessageFieldMetaEntity getMessageFieldMeta() {
        return messageFieldMeta;
    }

    /**
     * @param messageFieldMeta the messageFieldMeta to set
     */
    public void setMessageFieldMeta(UlticoreMessageFieldMetaEntity messageFieldMeta) {
        this.messageFieldMeta = messageFieldMeta;
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
     * @return the messageParamter
     */
    public AbstractTemplateParamEntity getMessageParamter() {
        return messageParamter;
    }

    /**
     * @param messageParamter the messageParamter to set
     */
    public void setMessageParamter(AbstractTemplateParamEntity messageParamter) {
        this.messageParamter = messageParamter;
    }

    /**
     * @return the messageTrigger
     */
    public ReportTriggerEntity getMessageTrigger() {
        return messageTrigger;
    }

    /**
     * @param messageTrigger the messageTrigger to set
     */
    public void setMessageTrigger(ReportTriggerEntity messageTrigger) {
        this.messageTrigger = messageTrigger;
    }

    /**
     * @return the messageSchedule
     */
    public ReportScheduleEntity getMessageSchedule() {
        return messageSchedule;
    }

    /**
     * @param messageSchedule the messageSchedule to set
     */
    public void setMessageSchedule(ReportScheduleEntity messageSchedule) {
        this.messageSchedule = messageSchedule;
    }

    /**
     * @return the messageTriggerList
     */
    public List<ReportTriggerEntity> getMessageTriggerList() {
        return messageTriggerList;
    }

    /**
     * @param messageTriggerList the messageTriggerList to set
     */
    public void setMessageTriggerList(List<ReportTriggerEntity> messageTriggerList) {
        this.messageTriggerList = messageTriggerList;
    }

    /**
     * @return the messageScheduleList
     */
    public List<ReportScheduleEntity> getMessageScheduleList() {
        return messageScheduleList;
    }

    /**
     * @param messageScheduleList the messageScheduleList to set
     */
    public void setMessageScheduleList(List<ReportScheduleEntity> messageScheduleList) {
        this.messageScheduleList = messageScheduleList;
    }

    /**
     * @return the emailSettingItemList
     */
    public List<SelectItem> getEmailSettingItemList() {
        return emailSettingItemList;
    }

    /**
     * @param emailSettingItemList the emailSettingItemList to set
     */
    public void setEmailSettingItemList(List<SelectItem> emailSettingItemList) {
        this.emailSettingItemList = emailSettingItemList;
    }

    /**
     * @return the messageTriggerItemList
     */
    public List<SelectItem> getMessageTriggerItemList() {
        return messageTriggerItemList;
    }

    /**
     * @param messageTriggerItemList the messageTriggerItemList to set
     */
    public void setMessageTriggerItemList(List<SelectItem> messageTriggerItemList) {
        this.messageTriggerItemList = messageTriggerItemList;
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
     * @return the reportFormat
     */
    public com.rsdynamix.bi.projects.web.commons.bean.ReportFormat getReportFormat() {
        return reportFormat;
    }

    /**
     * @param reportFormat the reportFormat to set
     */
    public void setReportFormat(com.rsdynamix.bi.projects.web.commons.bean.ReportFormat reportFormat) {
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
     * @return the generatedMessageStack
     */
    public List<ActualMessage> getGeneratedMessageStack() {
        return generatedMessageStack;
    }

    /**
     * @param generatedMessageStack the generatedMessageStack to set
     */
    public void setGeneratedMessageStack(List<ActualMessage> generatedMessageStack) {
        this.generatedMessageStack = generatedMessageStack;
    }

}
