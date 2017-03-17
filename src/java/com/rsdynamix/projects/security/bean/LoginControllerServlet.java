/*
 * LoginControllerServlet.java
 *
 * Created on July 27, 2009, 8:36 AM
 */
package com.rsdynamix.projects.security.bean;

import java.io.*;
import java.net.*;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 *
 * @author p-aniah
 * @version
 */
public class LoginControllerServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (request.getParameter(UserManagerBean.CTRL_PARAM).equals(UserManagerBean.LOG_IN_CTRL)) {
            UserManagerBean userBean = new UserManagerBean();
            if (userBean.usersAvailable()) {
                response.sendRedirect(request.getContextPath() + "/security/login.jsf");
            } else {
                System.out.println("+++++++++++++++++++++++++ redirecting to: " + request.getContextPath() + "/home.jsf");
                response.sendRedirect(request.getContextPath() + "/home.jsf");
            }
        } else if (request.getParameter(UserManagerBean.CTRL_PARAM).equals(UserManagerBean.LOG_OUT_CTRL)) {
            response.sendRedirect(request.getContextPath() + "/security/login.jsf");
        } else {
            UserManagerBean userBean = (UserManagerBean)request
                    .getSession(false).getAttribute("userManagerBean");
            
            if ((userBean != null) 
                    && (userBean.getUserAccount() != null) 
                    && (!userBean.getUserAccount().getUserName().equals(""))) {
                response.sendRedirect(request.getQueryString());
            } else {
                response.sendRedirect(request.getContextPath() + "/security/login.jsf");
            }
        }

        out.close();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
    // </editor-fold>
}
