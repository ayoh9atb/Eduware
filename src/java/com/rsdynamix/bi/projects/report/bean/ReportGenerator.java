/*
 * ReportGenerator.java
 *
 * Created on February 8, 2008, 10:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.rsdynamix.bi.projects.report.bean;

import com.rsdynamix.bi.das.reports.handlers.UlticoreHTMLReportBuilder;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.html.HtmlWriter;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.rtf.RtfWriter2;
import com.rsdynamix.abstractobjects.AbstractFieldMetaEntity;
import com.rsdynamix.bi.das.reports.handlers.HtmlReportObject;
import com.rsdynamix.projects.dynamo.payload.DDTSurrogate;
import com.rsdynamix.projects.report.entities.UlticoreReportEntity;
import com.rsdynamix.projects.report.entities.UlticoreSubreportEntity;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.jfree.chart.JFreeChart;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xml.sax.InputSource;

/**
 *
 * @author p-aniah
 */
public class ReportGenerator {

    private static final String COMPANY_FOLDER = "CompanyArtifacts";
    //
    private static final String COMPANY_LOGO_IMG = COMPANY_FOLDER + File.separator + "images" + File.separator + "logo.png";

    /**
     * Creates a new instance of ReportGenerator
     */
    public ReportGenerator() {
    }

    public void runReportAsPDF(InputStream reportStream,
            ServletOutputStream servletStream,
            HtmlReportObject reportObject, URL srcURL) {
        UlticoreHTMLReportBuilder reportBuilder = new UlticoreHTMLReportBuilder();
        try {
            String processedText = reportBuilder.processParameterPassing(
                    reportObject.getAsDynamoDispatchTable(), srcURL);

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(
                    new InputSource(new ByteArrayInputStream(processedText.getBytes())));

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(doc, null);
            renderer.layout();

            renderer.createPDF(servletStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void runReportAsHTML(InputStream reportStream,
            ServletOutputStream servletStream,
            HtmlReportObject reportObject, URL srcURL) {
        UlticoreHTMLReportBuilder reportBuilder = new UlticoreHTMLReportBuilder();
        try {
            String processedText = reportBuilder.processParameterPassing(
                    reportObject.getAsDynamoDispatchTable(), srcURL);
            servletStream.print(processedText);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printReportAsPDF(InputStream reportStream,
            ServletOutputStream servletStream,
            HtmlReportObject reportObject, URL srcURL) {
        UlticoreHTMLReportBuilder reportBuilder = new UlticoreHTMLReportBuilder();
        try {
            String processedText = reportBuilder.processParameterPassing(
                    reportObject.getAsDynamoDispatchTable(), srcURL);

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(
                    new InputSource(new ByteArrayInputStream(processedText.getBytes())));

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(doc, null);
            renderer.layout();

            renderer.createPDF(servletStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * intuitive report
     */
    public void generateNonTemplateReport(InputStream reportStream,
            ServletOutputStream servletStream,
            UlticoreReportEntity report, String reportFormat) {
        Document document = new Document(PageSize.A4);

        try {
            if (reportFormat.equalsIgnoreCase("PDF")) {
                PdfWriter.getInstance(document, servletStream);
            } else if (reportFormat.equalsIgnoreCase("rtf")) {
                RtfWriter2.getInstance(document, servletStream);
            } else if (reportFormat.equalsIgnoreCase("html")) {
                HtmlWriter.getInstance(document, servletStream);
            }

            document.open();

            Image image = Image.getInstance(COMPANY_LOGO_IMG);
            image.setAlignment(Element.ALIGN_LEFT);
            image.scalePercent(50);
            document.add(image);

            PdfPCell cell = new PdfPCell(new Paragraph(report.getReportTitle(),
                    new Font(Font.HELVETICA, 12, Font.BOLD)));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(0);
            document.add(cell);

            PdfPTable table = new PdfPTable(report.getAbstractFieldMetaList().size());
            table.setHeaderRows(1);

            cell.setColspan(report.getAbstractFieldMetaList().size());
            table.addCell(cell);

            for (AbstractFieldMetaEntity fieldMeta : report.getAbstractFieldMetaList()) {
                cell = new PdfPCell(new Paragraph(CommonBean.delimitCamelPoints(fieldMeta.getEntityFieldName(), " "),
                        new Font(Font.HELVETICA, 10, Font.BOLD)));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(Color.lightGray);
                table.addCell(cell);
            }

            int rowIndex = 0;
            for (List<Object> fieldList : report.getObjectFieldList()) {
                Color color = (rowIndex % 2) == 0 ? Color.decode("#99C68E") : Color.decode("#FFFFFF");
                for (Object field : fieldList) {
                    cell = new PdfPCell(new Paragraph(field.toString(),
                            new Font(Font.HELVETICA, 11, Font.NORMAL)));
                    cell.setBackgroundColor(color);
                    
                    if ((field instanceof Integer)
                            || (field instanceof Double)
                            || (field instanceof BigDecimal)) {
                        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    } else {
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    }
                    table.addCell(cell);
                }
                rowIndex++;
            }
            document.add(table);

            if (report.getSubreportList().size() > 0) {
                for (UlticoreSubreportEntity subreport : report.getSubreportList()) {
                    document.add(new Phrase("\n\n"));
                    
                    DDTSurrogate tableData = (DDTSurrogate) report.getParameters().get(subreport.getReportTitle());

                    if (tableData != null) {
                        cell = new PdfPCell(new Paragraph(subreport.getReportTitle(),
                                new Font(Font.HELVETICA, 11, Font.BOLD)));
                        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        cell.setBorder(0);
                        document.add(cell);

                        table = new PdfPTable(subreport.getAbstractFieldMetaList().size());
                        table.setHeaderRows(1);

                        cell.setColspan(subreport.getAbstractFieldMetaList().size());
                        table.addCell(cell);

                        for (AbstractFieldMetaEntity fieldMeta : subreport.getAbstractFieldMetaList()) {
                            cell = new PdfPCell(new Paragraph(CommonBean.delimitCamelPoints(fieldMeta.getEntityFieldName(), " "),
                                    new Font(Font.HELVETICA, 10, Font.BOLD)));
                            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell.setBackgroundColor(Color.lightGray);
                            table.addCell(cell);
                        }

                        rowIndex = 0;
                        while (tableData.next()) {
                            Color color = (rowIndex % 2) == 0 ? Color.decode("#99C68E") : Color.decode("#FFFFFF");
                            for (AbstractFieldMetaEntity fieldMeta : subreport.getAbstractFieldMetaList()) {
                                Object field = tableData.getFieldValue(fieldMeta.getEntityFieldName());

                                cell = new PdfPCell(new Paragraph(field.toString(),
                                        new Font(Font.HELVETICA, 11, Font.NORMAL)));
                                cell.setBackgroundColor(color);
                                if ((field instanceof Integer)
                                        || (field instanceof Double)
                                        || (field instanceof BigDecimal)) {
                                    cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                                } else {
                                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                                }
                                table.addCell(cell);
                            }
                            rowIndex++;
                        }

                        document.add(table);
                    }
                }
            }

            document.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void exportChartToPDF(JFreeChart chart, ServletOutputStream servletStream, int width, int height) {
        ReportServer reporter = new ReportServer();
        reporter.exportChartToPDF(chart, servletStream, width, height);
    }

}
