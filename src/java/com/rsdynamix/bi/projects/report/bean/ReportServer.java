/*
 * ReportServer.java
 *
 * Created on February 8, 2008, 10:27 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rsdynamix.bi.projects.report.bean;

/**
 *
 * @author p-aniah
 */
import com.codrellica.projects.resources.Constants;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import com.rsdynamix.bi.das.reports.handlers.ReportDataSource;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletOutputStream;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JExcelApiExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.jfree.chart.JFreeChart;

public class ReportServer {

    private String templateFileName;
    private String reportTitle;
    private String outputFile;
    private String designFileName;
    private JasperPrint jasperPrint;
    private Map parameters;

    /** Creates a new instance of ReportServer */
    public ReportServer() {
    }

    public void compileReport() {
        try {
            JasperCompileManager.compileReportToFile(getDesignFileName(), getTemplateFileName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void viewReport() {
        try {
            JasperViewer.viewReport(getJasperPrint());
        } catch (Exception ex) {
        }
    }

    Map getParameters() {
        return parameters;
    }

    void setParameters(Map parameters) {
        this.parameters = parameters;
        //this.parameters.put("DataFile", "ReportDataSource.java");
    }

    public void fillReport() {
        Map parameters = getParameters();
        try {
            JasperFillManager.fillReportToFile(getTemplateFileName(), parameters, new ReportDataSource());
        } catch (Exception ex) {
        }
    }

    public void printReport() {
        Map parameters = getParameters();
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, new ReportDataSource());
            JasperPrintManager.printReport(jasperPrint, false);
        } catch (Exception ex) {
        }
    }

    public void exportReportToPDF() {
        Map parameters = getParameters();
        try {
            jasperPrint = JasperFillManager.fillReport(new FileInputStream(new File(getTemplateFileName())), parameters);

            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, getOutputFile());
            exporter.exportReport();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void exportReportToPDF(JRDataSource dataSource) {
        Map parameters = getParameters();
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, dataSource);

            JRExporter exporter = new JRPdfExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, getOutputFile());
            exporter.exportReport();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void exportChartToPDF(JFreeChart chart, ServletOutputStream servletStream, int width, int height) {
        Document document = new Document(new Rectangle(width, height));
        document.open();
        
        try {
            PdfWriter writer = PdfWriter.getInstance(document, servletStream);
            writer.open();
            PdfContentByte cByte = writer.getDirectContent();
            PdfTemplate template = cByte.createTemplate(width, height);

            Graphics2D graph = template.createGraphics(width, height, new DefaultFontMapper());
            Rectangle2D rect = new Rectangle2D.Double(0, 0, width, height);

            chart.draw(graph, rect);
            graph.dispose();
            cByte.addTemplate(template, 0, 0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //document.close();
    }

    public JasperPrint getJasperPdfObject(JRDataSource dataSource) {
        Map parameters = getParameters();
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, dataSource);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return jasperPrint;
    }

    public void exportReportToXML() {
        Map parameters = getParameters();
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, new ReportDataSource());
            JasperExportManager.exportReportToXmlFile(jasperPrint, getOutputFile(), false);
        } catch (Exception ex) {
        }
    }

    public void exportReportToXML(JRDataSource dataSource) {
        Map parameters = getParameters();
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, dataSource);
            JRExporter exporter = new JRXmlExporter();
            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, getOutputFile());
            exporter.exportReport();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void exportReportToEmbededXML() {
        Map parameters = getParameters();
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, new ReportDataSource());
            JasperExportManager.exportReportToXmlFile(jasperPrint, getOutputFile(), true);
        } catch (Exception ex) {
        }
    }

    public void exportReportToEmbededXML(JRDataSource dataSource) {
        Map parameters = getParameters();
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, dataSource);
            JasperExportManager.exportReportToXmlFile(jasperPrint, getOutputFile(), true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void exportReportToHTML() {
        Map parameters = getParameters();
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, new ReportDataSource());
            JasperExportManager.exportReportToHtmlFile(jasperPrint, getOutputFile());
        } catch (Exception ex) {
        }
    }

    public void exportReportToHTML(JRDataSource dataSource) {
        Map parameters = getParameters();
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, dataSource);
            JasperExportManager.exportReportToHtmlFile(jasperPrint, getOutputFile());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void exportReportToRTF() {
        File sourceFile = new File(getTemplateFileName());
        try {
            jasperPrint = (JasperPrint) JRLoader.loadObject(sourceFile);
            File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".rtf");
            JRRtfExporter exporter = new JRRtfExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());

            exporter.exportReport();
        } catch (Exception ex) {
        }
    }

    public void exportReportToRTF(JRDataSource dataSource) {
        File sourceFile = new File(getTemplateFileName());
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, dataSource);
            File destFile = new File(getOutputFile());
            JRExporter exporter = new JRRtfExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);

            exporter.exportReport();
        } catch (Exception ex) {
        }
    }

    public void exportReportToXLS() {
        File sourceFile = new File(getTemplateFileName());
        try {
            jasperPrint = (JasperPrint) JRLoader.loadObject(sourceFile);
            File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".xls");
            JRExporter exporter = new JRXlsExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());

            exporter.exportReport();
        } catch (Exception ex) {
        }
    }

    public void exportReportToXLS(JRDataSource dataSource) {
        File sourceFile = new File(getTemplateFileName());
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, dataSource);
            File destFile = new File(getOutputFile());
            JRExporter exporter = new JRXlsExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);

            exporter.exportReport();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void exportReportToJXL() {
        File sourceFile = new File(getTemplateFileName());
        try {
            jasperPrint = (JasperPrint) JRLoader.loadObject(sourceFile);
            File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".jxl.xls");
            JExcelApiExporter exporter = new JExcelApiExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);

            exporter.exportReport();
        } catch (Exception ex) {
        }
    }

    public void exportReportToJXL(JRDataSource dataSource) {
        File sourceFile = new File(getTemplateFileName());
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, dataSource);
            File destFile = new File(getOutputFile());
            JRExporter exporter = new JExcelApiExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);

            exporter.exportReport();
        } catch (Exception ex) {
        }
    }

    public void exportReportToCVS() {
        File sourceFile = new File(getTemplateFileName());
        try {
            jasperPrint = (JasperPrint) JRLoader.loadObject(sourceFile);
            File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".csv");
            JRCsvExporter exporter = new JRCsvExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());

            exporter.exportReport();
        } catch (Exception ex) {
        }
    }

    public void exportReportToCVS(JRDataSource dataSource) {
        File sourceFile = new File(getTemplateFileName());
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, dataSource);
            File destFile = new File(getOutputFile());
            JRExporter exporter = new JRCsvExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);

            exporter.exportReport();
        } catch (Exception ex) {
        }
    }

    public void exportReportToODT() {
        File sourceFile = new File(getTemplateFileName());
        try {
            jasperPrint = (JasperPrint) JRLoader.loadObject(sourceFile);
            File destFile = new File(sourceFile.getParent(), jasperPrint.getName() + ".odt");
            JROdtExporter exporter = new JROdtExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());

            exporter.exportReport();
        } catch (Exception ex) {
        }
    }

    public void exportReportToODT(JRDataSource dataSource) {
        File sourceFile = new File(getTemplateFileName());
        try {
            jasperPrint = JasperFillManager.fillReport(getTemplateFileName(), parameters, dataSource);
            File destFile = new File(getOutputFile());
            JRExporter exporter = new JROdtExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
            exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);

            exporter.exportReport();
        } catch (Exception ex) {
        }
    }

    public void exportReportToRUN() {
        try {
            Map parameters = new HashMap();
            parameters.put("ReportTitle", getReportTitle());

            JasperRunManager.runReportToPdfFile(getTemplateFileName(), parameters, new ReportDataSource());
        } catch (Exception ex) {
        }
    }

    public void exportReportToRUN(JRDataSource dataSource) {
        File sourceFile = new File(getTemplateFileName());
        try {
            Map parameters = new HashMap();
            parameters.put("ReportTitle", getReportTitle());

            JasperRunManager.runReportToPdfFile(getTemplateFileName(), parameters, dataSource);
        } catch (Exception ex) {
        }
    }

    public void exportReportToRUN(InputStream reportStream, ServletOutputStream servletStream, JRDataSource dataSource) {
        try {
            Map parameters = new HashMap();
            JasperRunManager.runReportToPdfStream(reportStream, servletStream, parameters, dataSource);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void exportReportToRUN(InputStream reportStream, ServletOutputStream servletStream, JRDataSource dataSource, Map parameters) {
        try {
            JasperRunManager.runReportToPdfStream(reportStream, servletStream, parameters, dataSource);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getTemplateFileName() {
        return templateFileName;
    }

    public void setTemplateFileName(String templateFileName) {
        this.templateFileName = templateFileName;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getDesignFileName() {
        return designFileName;
    }

    public void setDesignFileName(String designFileName) {
        this.designFileName = designFileName;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }
}
