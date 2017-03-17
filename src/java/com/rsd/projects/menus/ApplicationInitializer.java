/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsd.projects.menus;

import com.rsdynamix.projects.commons.messages.beans.MessageSetupBean;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author root
 */
public class ApplicationInitializer implements ServletContextListener {

    public static  ServletContext APP_SERVLET_CONTEXT;

    public ApplicationInitializer() {
    }

    public void contextInitialized(ServletContextEvent sce) {
        try {
            APP_SERVLET_CONTEXT = sce.getServletContext();
            ValueListBean valueListBean = new ValueListBean();
            APP_SERVLET_CONTEXT.setAttribute("valueListBean", valueListBean);

            MessageSetupBean mailConfigBean = new MessageSetupBean();
            APP_SERVLET_CONTEXT.setAttribute("messageSetupBean", mailConfigBean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void contextDestroyed(ServletContextEvent sce) {
    }
}
