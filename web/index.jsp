<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="f" uri="http://java.sun.com/jsf/core"%>
<%@taglib prefix="h" uri="http://java.sun.com/jsf/html"%>
<!--

-->

<%! private String contextRoot;%>
<%! private String queryString;%>
<%! private String paramName;%>
<%! private String paramValue;%>

<%
//<jsp:useBean id="userBean" scope="request" class="com.rsdynamix.projects.security.bean.UserManagerBean"/>
contextRoot = request.getContextPath();
queryString = request.getQueryString();

//paramName = userBean.CTRL_PARAM;
//paramValue = userBean.LOG_IN_CTRL;
%>

<%
    //response.sendRedirect(contextRoot+"/LoginControllerServlet?"+paramName+"="+paramValue);
    response.sendRedirect(contextRoot+"/security/login.jsf");
%>
