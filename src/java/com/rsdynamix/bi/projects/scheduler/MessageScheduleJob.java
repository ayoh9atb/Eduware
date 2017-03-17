/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.bi.projects.scheduler;

import com.rsdynamix.projects.commons.entities.EmailSettingEntity;
import com.rsdynamix.projects.report.entities.ReportScheduleEntity;
import com.rsdynamix.projects.report.entities.ReportTriggerEntity;
import com.rsdynamix.bi.projects.web.commons.bean.ApplicationInitializer;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.projects.commons.messages.beans.MessageSetupBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;

/**
 *
 * @author patushie
 */
public class MessageScheduleJob implements StatefulJob {

    private MessageSetupBean messageSetupBean;

    /**
     * Creates a new instance of MessageScheduleJob
     */
    public MessageScheduleJob() {
    }

    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            messageSetupBean = (MessageSetupBean) context.getJobDetail().getJobDataMap().get("messageSetupBean");

            long scheduleID = Long.parseLong(context.getJobDetail().getGroup().split("_")[1]);
            ReportScheduleEntity schedule = messageSetupBean.findMessageScheduleByID(scheduleID);
            String attachmentFile = "";
            
            if (schedule != null) {
                ReportTriggerEntity trigger = messageSetupBean.findMessageTriggerByID(schedule.getReportTriggerID());
                EmailSettingEntity messageSetting = messageSetupBean.findMessageByTitle(schedule.getReportTitle());

                if (messageSetting != null) {
                    messageSetupBean.setEmailSetting(messageSetting);
                    messageSetupBean.messageTemplateSelectedForScheduler(messageSetting);

                    messageSetupBean.filterQueryAndSendMessage();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
