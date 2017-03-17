/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.projects.security.controller;

import com.rsdynamix.projects.security.bean.UserManagerBean;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author patushie
 */
public class ApplicationRequestFilter implements Filter {

    private FilterConfig config;
    private final String loginpage = "loginpage";
    private String loginpagevalue;

    public void init(FilterConfig filterConfig) throws ServletException {
        this.config = filterConfig;
        loginpagevalue = config.getInitParameter(loginpage);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse hResponse = (HttpServletResponse) response;
        HttpSession session = httpReq.getSession();
        String requestURI = httpReq.getRequestURI();
        
        String userName = httpReq.getParameter("userName");

        boolean exclude = requestURI.contains(loginpagevalue) 
                || requestURI.contains(".css.jsf")
                || requestURI.contains(".js.jsf")
                || requestURI.contains(".png.jsf");

        UserManagerBean userBean = (UserManagerBean) session.getAttribute("userManagerBean");
        String location = httpReq.getContextPath();
        boolean redirect = false;
        if ((userBean == null)
                || (userBean.getUserAccount() == null) 
                || (userBean.getUserAccount().getUserName() == null)
                || (userBean.getUserAccount().getUserName().trim().equals(""))) {
            redirect = true;
        }
        
        if (!exclude) {
            try {
                if (redirect) {
                    location += loginpagevalue;
                    hResponse.sendRedirect(location);
                    //request.getRequestDispatcher("/home.jsf").forward(request, response);
                } else {
                    chain.doFilter(request, response);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
}