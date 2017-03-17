/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.bi.projects.report.bean;

import com.rsdynamix.projects.common.reports.UlticoreReportTransporter;
import com.codrellica.projects.commons.DateUtil;
import com.rsdynamix.abstractobjects.AbstractTemplateDataType;
import com.rsdynamix.abstractobjects.AbstractTemplateParamEntity;
import com.rsdynamix.projects.report.entities.UlticoreReportEntity;
import com.rsdynamix.bi.das.reports.handlers.HtmlReportObject;
import com.rsdynamix.projects.report.entities.ReportBodyType;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author p-aniah
 */
public class ReportServlet extends HttpServlet {

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String reportType = request.getParameter(ReportAttributes.REPORT_TYPE_PARAM);
        String printType = request.getParameter(ReportAttributes.PRINT_TYPE);

        if (reportType.equals(ReportAttributes.ADHOC_TYPE)) {
            generateAdhocReport(request, response, printType);
        } else if (reportType.equals(ReportAttributes.CHART_TYPE)) {
            exportChartToPDF(request, response, 700, 475);
        }
    }

    private void generateAdhocReport(HttpServletRequest request, 
            HttpServletResponse response,
            String printType) {
        String reportKey = String.valueOf(request.getSession().getAttribute(
                ReportAttributes.REPORT_KEY_PARAM));
        String reportFormat = String.valueOf(request.getSession().getAttribute(
                ReportAttributes.REPORT_FORMAT_PARAM));
        String reportBodyType = String.valueOf(request.getSession().getAttribute(
                ReportAttributes.REPORT_BODY_TYPE_PARAM));
        UlticoreReportEntity ultiReport = (UlticoreReportEntity) request.getSession().getAttribute(reportKey);
        
        URL srcURL = null;
        try {
            srcURL = new URL(request.getRequestURL().toString());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        InputStream reportStream = null;
        showReport(ultiReport, response, reportStream, reportFormat,
                reportBodyType, printType, srcURL);
    }

    private void showReport(UlticoreReportEntity ultiReport,
            HttpServletResponse response,
            InputStream reportStream,
            String reportFormat, String reportBodyType, String printType, URL srcURL) {
        ServletOutputStream servletOutputStream = null;

        try {
            servletOutputStream = response.getOutputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        UlticoreReportTransporter report = new UlticoreReportTransporter();

        ReportGenerator reportGenerator = new ReportGenerator();

        if (reportFormat.equalsIgnoreCase("PDF")) {
            response.setContentType("application/pdf");
        } else if (reportFormat.equalsIgnoreCase("rtf")) {
            response.setContentType("text/rtf");
        } else if (reportFormat.equalsIgnoreCase("html")) {
            response.setContentType("text/html");
        }

        if (ultiReport.getParameters().isEmpty()) {
            for (AbstractTemplateParamEntity reptParam : ultiReport.getAbstractParameterList()) {
                if (reptParam.getDataType() == AbstractTemplateDataType.DATE_TYPE) {
                    if (reptParam.getParameterValue() != null) {
                        ultiReport.getParameters().put(reptParam.getParameterName(), DateUtil.toUtilDate(reptParam.getParameterValue()));
                    }
                } else if (reptParam.getDataType() == AbstractTemplateDataType.STRING_TYPE) {
                    ultiReport.getParameters().put(reptParam.getParameterName(), reptParam.getParameterValue());
                } else if (reptParam.getDataType() == AbstractTemplateDataType.FLOATING_POINT_TYPE) {
                    ultiReport.getParameters().put(reptParam.getParameterName(),
                            BigDecimal.valueOf(Double.parseDouble(reptParam.getParameterValue())));
                } else if (reptParam.getDataType() == AbstractTemplateDataType.INTEGER_TYPE) {
                    ultiReport.getParameters().put(reptParam.getParameterName(), Double.parseDouble(reptParam.getParameterValue()));
                }
            }
        }

        if (reportBodyType.equals(ReportBodyType.TEMPLATE.toString())) {
            HtmlReportObject htmlReportObject = new HtmlReportObject();
            
            Iterator it = ultiReport.getParameters().entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                htmlReportObject.getParameters().put((String) entry.getKey(), entry.getValue());
            }
            htmlReportObject.setParameters(ultiReport.getParameters());
            htmlReportObject.setReportTitle(ultiReport.getReportTitle());

            htmlReportObject.setTemplateFileName(ultiReport.getTemplateFileName());
            htmlReportObject.setOutputFileName(ultiReport.getTemplateFileName());

            if (reportFormat.equalsIgnoreCase("PDF")) {
                reportGenerator.runReportAsPDF(reportStream, servletOutputStream, htmlReportObject, null);
            } else if (reportFormat.equalsIgnoreCase("html")) {
                reportGenerator.runReportAsHTML(reportStream, servletOutputStream, htmlReportObject, srcURL);
            }
        } else if (reportBodyType.equals(ReportBodyType.INTUITIVE.toString())) {
            reportGenerator.generateNonTemplateReport(reportStream, servletOutputStream, ultiReport, reportFormat);
        }

        try {
            servletOutputStream.flush();
            servletOutputStream.close();
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
        }
    }

    public void exportChartToPDF(HttpServletRequest request, HttpServletResponse response, int width, int height) {
        ServletOutputStream servletOutputStream = null;
        response.setContentType("application/pdf");
        ReportGenerator reportGenerator = new ReportGenerator();

        String chartKey = request.getSession().getAttribute(ReportAttributes.REPORT_KEY_PARAM).toString();
        JFreeChart chart = (JFreeChart) request.getSession().getAttribute(chartKey);

        try {
            servletOutputStream = response.getOutputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        reportGenerator.exportChartToPDF(chart, servletOutputStream, width, height);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
