/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.bi.projects.scheduler;

import com.codrellica.mail.dataobjects.Attachment;
import com.codrellica.mail.dataobjects.MailObject;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.commons.entities.EmailRecepientEntity;
import com.rsdynamix.projects.commons.entities.EmailSettingEntity;
import com.rsdynamix.projects.commons.entities.RecepientType;
import com.rsdynamix.projects.report.entities.ReportScheduleEntity;
import com.rsdynamix.projects.report.entities.ReportTriggerEntity;
import com.rsdynamix.projects.report.entities.UlticoreReportEntity;
import com.rsdynamix.bi.das.reports.handlers.ReportGenerator;
import com.rsdynamix.bi.projects.report.bean.UlticoreReportBean;
import com.rsdynamix.bi.projects.web.commons.bean.ApplicationInitializer;
import com.rsdynamix.bi.reports.servlet.ReportAttributes;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.projects.commons.messages.beans.MessageSetupBean;
import java.io.InputStream;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 *
 * @author root
 */
public class ReportScheduleJob implements StatefulJob {

    private HttpSession servletSession;
    public static final String SERVER_URL = "http://localhost:47893";
    private UlticoreReportBean ulticoreReportBean;
    private MessageSetupBean messageSetupBean;

    /**
     * Creates a new instance of ReportCourrier
     */
    public ReportScheduleJob() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        if (FacesContext.getCurrentInstance() != null) {
            ulticoreReportBean = (UlticoreReportBean) context.getJobDetail().getJobDataMap().get("ulticoreReportBean");
            messageSetupBean = (MessageSetupBean) context.getJobDetail().getJobDataMap().get("messageSetupBean");
        } else {
            ulticoreReportBean = (UlticoreReportBean) ApplicationInitializer.APP_SERVLET_CONTEXT.getAttribute("ulticoreReportBean");
            messageSetupBean = (MessageSetupBean) ApplicationInitializer.APP_SERVLET_CONTEXT.getAttribute("messageSetupBean");
        }

        try {
            long scheduleID = Long.parseLong(context.getJobDetail().getGroup().split("_")[1]);
            ReportScheduleEntity schedule = ulticoreReportBean.findReportScheduleByID(scheduleID);
            String attachmentFile = "";

            if (schedule != null) {
                ReportTriggerEntity trigger = ulticoreReportBean.findReportTriggerByID(schedule.getReportTriggerID());
                UlticoreReportEntity report = ulticoreReportBean.findReportByTitle(schedule.getReportTitle());
                report = getUlticoreReportBean().queryAndLoadReportAutomatically(report);

                if (report.getAbstractFieldList().size() > 0) {
                    try {
                        InputStream reportStream = null;
                        ReportGenerator reportGen = new ReportGenerator();
                        attachmentFile = reportGen.runReportAsPDF(reportStream, null, report, ReportAttributes.FILE_OUTPUT_PRINT);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if ((trigger != null) && (report != null)) {
                        EmailSettingEntity mailSetting = messageSetupBean.findEmailSettingByID(report.getMailTemplateID());
                        if (mailSetting != null) {
                            MailObject mail = new MailObject();
                            mail.setBody(mailSetting.getMailBody());
                            mail = messageSetupBean.populateTypeParameters(mail, mailSetting);

                            mail.setSubject(mailSetting.getSubject());
                            for (EmailRecepientEntity recpt : mailSetting.getRecepientList()) {
                                if (recpt.getRecepientType() == RecepientType.TO_TYPE) {
                                    if (!mail.getRecepientAddress().trim().equals("")) {
                                        mail.setRecepientAddress(mail.getRecepientAddress() + "," + recpt.getRecepientEmail());
                                    } else {
                                        mail.setRecepientAddress(recpt.getRecepientEmail());
                                    }
                                } else if (recpt.getRecepientType() == RecepientType.CC_TYPE) {
                                    mail.getCcList().add(recpt.getRecepientEmail());
                                } else if (recpt.getRecepientType() == RecepientType.BCC_TYPE) {
                                    mail.getBccList().add(recpt.getRecepientEmail());
                                }
                            }

                            mail.setSenderDisplayName(mailSetting.getSenderName());
                            mail.setSenderAddress(mailSetting.getSenderEmail());

                            Attachment fileAttach = new Attachment();
                            fileAttach.setURLPath(true);
                            fileAttach.setAttachmentFilePath(SERVER_URL + "/" + attachmentFile);
                            fileAttach.setFilename(attachmentFile);
                            mail.getAttachmentList().add(fileAttach);

                            try {
                                 FinanceServiceLocator.locateMailMessageServer().dispatchMail(mail);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
     * @return the ulticoreReportBean
     */
    public UlticoreReportBean getUlticoreReportBean() {
        return ulticoreReportBean;
    }

    /**
     * @param ulticoreReportBean the ulticoreReportBean to set
     */
    public void setUlticoreReportBean(UlticoreReportBean ulticoreReportBean) {
        this.ulticoreReportBean = ulticoreReportBean;
    }

    /**
     * @return the messageSetupBean
     */
    public MessageSetupBean getMessageSetupBean() {
        return messageSetupBean;
    }

    /**
     * @param messageSetupBean the messageSetupBean to set
     */
    public void setMessageSetupBean(MessageSetupBean messageSetupBean) {
        this.messageSetupBean = messageSetupBean;
    }

}
