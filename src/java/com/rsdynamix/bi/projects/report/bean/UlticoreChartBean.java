/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.bi.projects.report.bean;

import com.rsd.projects.menus.FinultimateCommons;
import com.rsd.projects.menus.MenuManagerBean;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.bi.projects.web.commons.bean.ApplicationInitializer;
import com.rsdynamix.bi.reports.servlet.ReportAttributes;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.report.entities.ChartDimension;
import com.rsdynamix.projects.report.entities.ChartType;
import com.rsdynamix.projects.report.entities.PlotOrientationType;
import com.rsdynamix.projects.report.entities.UlticoreChartEntity;
import com.rsdynamix.projects.report.entities.UlticoreReportEntity;
import com.rsdynamix.projects.report.entities.UlticoreVerticalFieldEntity;
import com.rsdynamix.bi.projects.web.commons.bean.ReportFormat;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailBean;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import com.rsdynamix.projects.security.bean.PrivilegeBean;
import com.rsdynamix.projects.security.bean.UserManagerBean;
import com.rsdynamix.projects.security.entities.MenuEntity;
import com.rsdynamix.projects.security.entities.UserAccountEntity;
import com.rsdynamix.projects.ulticoreparser.constants.Constants;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.TableOrder;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import java.sql.SQLException;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;

/**
 *
 * @author root
 */
@SessionScoped
@ManagedBean(name = "ulticoreChartBean")
public class UlticoreChartBean implements Serializable {

    private static final String CHART_TEMPLATE_ID_KEY = "chart_tmplat_id";
    /**/
    private static final String CHART_TEMPLATE_ID_DEFAULT = "1";
    /**/
    private static final String VERTICAL_FIELD_ID_KEY = "vert_fld_id";
    /**/
    private static final String VERTICAL_FIELD_ID_DEFAULT = "1";
    /**/
    private UlticoreChartEntity ultiChart;
    private List<UlticoreChartEntity> ultiChartList;
    private List<UlticoreReportEntity> ultiReportList;
    private String verticalFieldName;
    private HttpSession servletSession;
    private List<SelectItem> plotOrientationItemList;
    private List<SelectItem> chartTypeItemList;
    private List<SelectItem> dimensionItemList;
    //
    private StreamedContent chartGraphicImage;
    private String processingResourceUrl;
    private String reportExportFormat;

    public UlticoreChartBean() {
        ultiChart = new UlticoreChartEntity();

        ultiChartList = new ArrayList<UlticoreChartEntity>();
        ultiReportList = new ArrayList<UlticoreReportEntity>();
        verticalFieldName = "";

        processingResourceUrl = "";

        populatePlotOrientation();
        populateChartTypes();
        populateDimension();

        loadChartTemplateList();
    }

    public void populateDimension() {
        dimensionItemList = new ArrayList<SelectItem>();

        SelectItem item = new SelectItem();
        item.setValue(ChartDimension.TWO_DIMENSION);
        item.setLabel(ChartDimension.TWO_DIMENSION.toString());
        dimensionItemList.add(item);

        item = new SelectItem();
        item.setValue(ChartDimension.THREE_DIMENSION);
        item.setLabel(ChartDimension.THREE_DIMENSION.toString());
        dimensionItemList.add(item);
    }

    public void populatePlotOrientation() {
        plotOrientationItemList = new ArrayList<SelectItem>();

        SelectItem item = new SelectItem();
        item.setValue(PlotOrientationType.HORIZONTAL);
        item.setLabel(PlotOrientationType.HORIZONTAL.toString());
        plotOrientationItemList.add(item);

        item = new SelectItem();
        item.setValue(PlotOrientationType.VERTICAL);
        item.setLabel(PlotOrientationType.VERTICAL.toString());
        plotOrientationItemList.add(item);
    }

    public void populateChartTypes() {
        chartTypeItemList = new ArrayList<SelectItem>();

        SelectItem item = new SelectItem();
        item.setValue(ChartType.BAR);
        item.setLabel(ChartType.BAR.toString());
        chartTypeItemList.add(item);

        item = new SelectItem();
        item.setValue(ChartType.XY_BAR);
        item.setLabel(ChartType.XY_BAR.toString());
        chartTypeItemList.add(item);

        item = new SelectItem();
        item.setValue(ChartType.STACKED_BAR);
        item.setLabel(ChartType.STACKED_BAR.toString());
        chartTypeItemList.add(item);

        item = new SelectItem();
        item.setValue(ChartType.XY_AREA);
        item.setLabel(ChartType.XY_AREA.toString());
        chartTypeItemList.add(item);

        item = new SelectItem();
        item.setValue(ChartType.XY_LINE);
        item.setLabel(ChartType.XY_LINE.toString());
        chartTypeItemList.add(item);

        item = new SelectItem();
        item.setValue(ChartType.PIE);
        item.setLabel(ChartType.PIE.toString());
        chartTypeItemList.add(item);

        item = new SelectItem();
        item.setValue(ChartType.MULTIPLE_PIE);
        item.setLabel(ChartType.MULTIPLE_PIE.toString());
        chartTypeItemList.add(item);

        item = new SelectItem();
        item.setValue(ChartType.HISTOGRAM);
        item.setLabel(ChartType.HISTOGRAM.toString());
        chartTypeItemList.add(item);
    }

    public String addVerticalField() {
        UlticoreVerticalFieldEntity vField = new UlticoreVerticalFieldEntity();
        vField.setFieldName(verticalFieldName);

        if (!getUltiChart().getVerticalFieldList().contains(this)) {
            getUltiChart().getVerticalFieldList().add(vField);
            verticalFieldName = "";
        }

        return "";
    }

    public String deleteVerticalField() {
        UlticoreVerticalFieldEntity vField = new UlticoreVerticalFieldEntity();
        vField.setFieldName(verticalFieldName);

        if (getUltiChart().getVerticalFieldList().contains(this)) {
            getUltiChart().getVerticalFieldList().remove(vField);
            verticalFieldName = "";
        }

        return "";
    }

    public String addChartTemplate() {
        if (!getUltiChartList().contains(getUltiChart())) {
            getUltiChartList().add(getUltiChart());
        } else {
            int index = getUltiChartList().indexOf(getUltiChart());
            getUltiChartList().set(index, getUltiChart());
        }
        setUltiChart(new UlticoreChartEntity());

        return "";
    }

    public String saveChartTemplateList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

        ApplicationPropertyBean appPropBean = (ApplicationPropertyBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationPropertyBean}", ApplicationPropertyBean.class);
        if (appPropBean == null) {
            appPropBean = new ApplicationPropertyBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationPropertyBean}",
                    ApplicationPropertyBean.class, appPropBean);
        }

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            for (UlticoreChartEntity uChart : getUltiChartList()) {
                if (uChart.getChartID() == 0) {
                    try {
                        int cmID = Integer.parseInt(appPropBean.getValue(CHART_TEMPLATE_ID_KEY,
                                CHART_TEMPLATE_ID_DEFAULT, true));
                        uChart.setChartID(cmID);

                        for (UlticoreVerticalFieldEntity vField : uChart.getVerticalFieldList()) {
                            int vertFldID = Integer.parseInt(appPropBean.getValue(VERTICAL_FIELD_ID_KEY,
                                    VERTICAL_FIELD_ID_DEFAULT, true));

                            vField.setVerticalFieldID(vertFldID);
                            vField.setChartID(cmID);
                            dataServer.saveData(vField);
                        }

                        dataServer.setSelectedTnsName(FinultimateConstants.COMMONS_CONNECTION);
                        dataServer.saveData(uChart);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    dataServer.setSelectedTnsName(FinultimateConstants.COMMONS_CONNECTION);
                    try {
                        dataServer.updateData(uChart);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
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
        applicationMessageBean.insertMessage("", MessageType.NONE);

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
                menuManagerBean.getSystemMap().get(MenuManagerBean.SETP_CHRTS_MENU_ITEM));

        if (ultiChart.getApprovalStatusID() < privilege.getApprovedStatusID()) {
            if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                if ((ultiChart.getApprovalStatusID() == 0) || (ultiChart.getApprovalStatusID()
                        <= userManagerBean.getUserAccount().getRole().getApprovalLevelID())) {
                    UserAccountEntity creatorAcct = userManagerBean.loadUserAccount(ultiChart.getApprover());
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            > creatorAcct.getRole().getApprovalLevelID()) {
                        ultiChart.setApprovalStatusID(ultiChart.getApprovalStatusID() + 1);
                        ultiChart.setApprover(userManagerBean.getUserAccount().getUserName());

                        dataServer.beginTransaction();
                        try {
                            dataServer.updateData(ultiChart);
                        } catch (SQLException ex) {
                            dataServer.rollbackTransaction();
                            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
                            ex.printStackTrace();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        dataServer.endTransaction();
                    } else if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            == creatorAcct.getRole().getApprovalLevelID()) {
                        applicationMessageBean.insertMessage("You cannot approve an operation performed "
                                + "by another user who has the same approval level as you!",
                                MessageType.ERROR_MESSAGE);
                    } else if (userManagerBean.getUserAccount().getRole().getApprovalLevelID()
                            < creatorAcct.getRole().getApprovalLevelID()) {
                        applicationMessageBean.insertMessage("You cannot approve an operation performed "
                                + "by another user who has a higher approval level as you!",
                                MessageType.ERROR_MESSAGE);
                    }
                } else if (ultiChart.getApprovalStatusID()
                        > userManagerBean.getUserAccount().getRole().getApprovalLevelID()) {
                    applicationMessageBean.insertMessage("This operation has been approved beyond "
                            + "your approval level, your approval is no longer effective!",
                            MessageType.ERROR_MESSAGE);
                }
            } else {
                applicationMessageBean.insertMessage("Access Denied: You do not have approval rights for this operation type!",
                        MessageType.ERROR_MESSAGE);
            }
        } else {
            applicationMessageBean.insertMessage("This operation is already approved. No action performed!",
                    MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String deleteChartTemplate() {
        if (getUltiChartList().contains(getUltiChart())) {
            getUltiChartList().remove(getUltiChart());
            setUltiChart(new UlticoreChartEntity());
        }

        return "";
    }

    public void chartTemplateSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        setUltiChart(ultiChartList.get(rowIndex));

        UlticoreReportBean ulticoreReportBean = null;
        if (FacesContext.getCurrentInstance() != null) {
            ulticoreReportBean = (UlticoreReportBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.ulticoreReportBean}", UlticoreReportBean.class);
            if (ulticoreReportBean == null) {
                ulticoreReportBean = new UlticoreReportBean();
                CommonBean.setBeanToContext("#{sessionScope.ulticoreReportBean}",
                        UlticoreReportBean.class, ulticoreReportBean);
            }
        } else {
            ulticoreReportBean = (UlticoreReportBean) ApplicationInitializer.APP_SERVLET_CONTEXT.getAttribute("ulticoreReportBean");
        }
        ulticoreReportBean.populateSelectedReportItemList(getUltiChart().getReportName());
    }

    public void verticalFieldSelected(ValueChangeEvent vce) {
    }

    public String loadChartTemplateList() {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateCommonsPersistenceManager();

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

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            ultiChartList = new ArrayList<UlticoreChartEntity>();

            MenuEntity privilege = privilegeBean.findPrivilegeByName(
                    menuManagerBean.getSystemMap().get(MenuManagerBean.SETP_CHRTS_MENU_ITEM));

            UlticoreChartEntity urCriteria = new UlticoreChartEntity();
            List<AbstractEntity> baseChartList = dataServer.findData(urCriteria);

            for (AbstractEntity baseChart : baseChartList) {
                UlticoreChartEntity chart = (UlticoreChartEntity) baseChart;

                boolean activated = false;
                if (chart.getApprovalStatusID() >= privilege.getApprovedStatusID()) {
                    activated = true;
                } else {
                    if (userManagerBean.getUserAccount().getRole().getApprovalLevelID() > 0) {
                        activated = true;
                    }
                }

                if (activated) {
                    UlticoreVerticalFieldEntity vfCriteria = new UlticoreVerticalFieldEntity();
                    vfCriteria.setChartID(chart.getChartID());
                    List<AbstractEntity> baseVFList = dataServer.findData(vfCriteria);
                    for (AbstractEntity baseVF : baseVFList) {
                        UlticoreVerticalFieldEntity vField = (UlticoreVerticalFieldEntity) baseVF;
                        chart.getVerticalFieldList().add(vField);
                    }

                    getUltiChartList().add(chart);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String moveToYCoordinatePage() {
        FinultimateCommons.captureRequestingResource();
        return "innerchartconfigurer";
    }

    public String backToMainChartPage() {
        return FinultimateCommons.retrieveLastRequestingResource();
    }

    public String moveToQueryPage() {
        UlticoreReportBean ulticoreReportBean = null;

        if (FacesContext.getCurrentInstance() != null) {
            ulticoreReportBean = (UlticoreReportBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.ulticoreReportBean}", UlticoreReportBean.class);
            if (ulticoreReportBean == null) {
                ulticoreReportBean = new UlticoreReportBean();
                CommonBean.setBeanToContext("#{sessionScope.ulticoreReportBean}",
                        UlticoreReportBean.class, ulticoreReportBean);
            }
        } else {
            ulticoreReportBean = (UlticoreReportBean) ApplicationInitializer.APP_SERVLET_CONTEXT.getAttribute("ulticoreReportBean");
        }

        UlticoreReportEntity reportTemplate = ulticoreReportBean.findReportByTitle(getUltiChart().getReportName());
        ulticoreReportBean.reportTemplateSelected(reportTemplate);
        if (reportTemplate != null) {
            ulticoreReportBean.setUltiReport(reportTemplate);
        }
        FinultimateCommons.captureRequestingResource();

        return "chartqueryfilter";
    }

    public void reportFormatSelected(ValueChangeEvent vce) {
        if ((vce != null) && (vce.getNewValue() != null)) {
            UlticoreReportBean ulticoreReportBean = (UlticoreReportBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.ulticoreReportBean}", UlticoreReportBean.class);
            if (ulticoreReportBean == null) {
                ulticoreReportBean = new UlticoreReportBean();
                CommonBean.setBeanToContext("#{sessionScope.ulticoreReportBean}",
                        UlticoreReportBean.class, ulticoreReportBean);
            }

            if (vce.getNewValue().toString().equals(ReportFormat.PDF.toString())) {
                reportExportFormat = ReportFormat.PDF.toString();
                ulticoreReportBean.setReportExportFormat(reportExportFormat);
            } else if (vce.getNewValue().toString().equals(ReportFormat.HTML.toString())) {
                reportExportFormat = ReportFormat.HTML.toString();
                ulticoreReportBean.setReportExportFormat(reportExportFormat);
            } else if (vce.getNewValue().toString().equals(ReportFormat.RTF.toString())) {
                reportExportFormat = ReportFormat.RTF.toString();
                ulticoreReportBean.setReportExportFormat(reportExportFormat);
            } else if (vce.getNewValue().toString().equals(ReportFormat.XLS.toString())) {
                reportExportFormat = ReportFormat.XLS.toString();
                ulticoreReportBean.setReportExportFormat(reportExportFormat);
            }
        }
    }

    public String queryAndLoadReport() {
        UlticoreReportBean ulticoreReportBean = null;

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        try {
            if (FacesContext.getCurrentInstance() != null) {
                ulticoreReportBean = (UlticoreReportBean) CommonBean.getBeanFromContext(
                        "#{sessionScope.ulticoreReportBean}", UlticoreReportBean.class);
                if (ulticoreReportBean == null) {
                    ulticoreReportBean = new UlticoreReportBean();
                    CommonBean.setBeanToContext("#{sessionScope.ulticoreReportBean}",
                            UlticoreReportBean.class, ulticoreReportBean);
                }
            } else {
                ulticoreReportBean = (UlticoreReportBean) ApplicationInitializer.APP_SERVLET_CONTEXT.getAttribute("ulticoreReportBean");
            }

            String outcome = ulticoreReportBean.queryAndLoadReport();
            if (ulticoreReportBean.getUltiReport().getSubreportList().size() > 0) {
                ulticoreReportBean.setUltiReport(
                        ulticoreReportBean.filterQueryAndLoadReport(ulticoreReportBean.getUltiReport()));

                getUltiChart().setAssociatedReport(ulticoreReportBean.getUltiReport());
            }

            buildChart();

            FacesContext context = FacesContext.getCurrentInstance();
            servletSession = (HttpSession) context.getExternalContext().getSession(false);

            servletSession.setAttribute(String.valueOf(getUltiChart().getChartID()) + "_", getUltiChart());
            getServletSession().setAttribute(ReportAttributes.REPORT_KEY_PARAM, String.valueOf(getUltiChart().getChartID()) + "_");

            servletSession.setAttribute(
                    ReportAttributes.REPORT_BODY_TYPE_PARAM, ReportAttributes.CHART_TYPE);

            processingResourceUrl = CommonBean.getContextRoot() + FinultimateConstants.BI_RESOURCE_URL_DFLT_VALUE
                    + "?" + ReportAttributes.REPORT_TYPE_PARAM + "=INTUITIVE{S}&"
                    + ReportAttributes.TEMPLATE_FILE + "="
                    + ulticoreReportBean.getUltiReport().getTemplateFileName() + "{S}&"
                    + ReportAttributes.PRINT_TYPE + "=" + ulticoreReportBean.getReportExportFormat() + "{S}";
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "chartrenderer";
    }

    public void generateChart(OutputStream out, Object data) {
        JFreeChart chart = null;

        if ((getUltiChart().getChartType() == ChartType.PIE)
                || (getUltiChart().getChartType() == ChartType.MULTIPLE_PIE)) {
            chart = createPieChart();
        } else if ((getUltiChart().getChartType() == ChartType.BAR)
                || (getUltiChart().getChartType() == ChartType.STACKED_BAR)
                || (getUltiChart().getChartType() == ChartType.XY_BAR)) {
            chart = createBarChart();
        }

        BufferedImage buffImg = chart.createBufferedImage(
                700, //width
                475, //height
                BufferedImage.TYPE_INT_RGB, //image type
                null);
        try {
            ImageIO.write(buffImg, "jpeg", out);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String buildChart() {
        JFreeChart chart = null;

        if ((getUltiChart().getChartType() == ChartType.PIE)
                || (getUltiChart().getChartType() == ChartType.MULTIPLE_PIE)) {
            chart = createPieChart();
        } else if ((getUltiChart().getChartType() == ChartType.BAR)
                || (getUltiChart().getChartType() == ChartType.STACKED_BAR)
                || (getUltiChart().getChartType() == ChartType.XY_BAR)) {
            chart = createBarChart();
        }

        try {
            BufferedImage buffImg = chart.createBufferedImage(
                    700, //width
                    475, //height
                    BufferedImage.TYPE_INT_RGB, //image type
                    null);

            byte[] encodedImg = ChartUtilities.encodeAsPNG(buffImg);
            ByteArrayInputStream baiStream = new ByteArrayInputStream(encodedImg);
            chartGraphicImage = new DefaultStreamedContent(baiStream, "image/png");

            String resourceFQFileName = "chartImage.jpg";
            resourceFQFileName = new File(resourceFQFileName).getAbsolutePath();
            resourceFQFileName = resourceFQFileName.replace(
                    Constants.SERVER_CONFIG_FOLDER,
                    Constants.SERVER_CONFIG_FOLDER
                    + File.separator
                    + Constants.DYNAMO_MODULES_FOLDER
                    + File.separator
                    + Constants.CEREBRO_FOLDER
                    + File.separator
                    + Constants.INSURE_BI_IMAGE_FOLDER);

            FileOutputStream fos = new FileOutputStream(resourceFQFileName);
            ImageIO.write(buffImg, "jpeg", fos);
            fos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public JFreeChart createPieChart() {
        JFreeChart chart = null;

        UlticoreReportBean ulticoreReportBean = null;
        if (FacesContext.getCurrentInstance() != null) {
            ulticoreReportBean = (UlticoreReportBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.ulticoreReportBean}", UlticoreReportBean.class);
            if (ulticoreReportBean == null) {
                ulticoreReportBean = new UlticoreReportBean();
                CommonBean.setBeanToContext("#{sessionScope.ulticoreReportBean}",
                        UlticoreReportBean.class, ulticoreReportBean);
            }
        } else {
            ulticoreReportBean = (UlticoreReportBean) ApplicationInitializer.APP_SERVLET_CONTEXT.getAttribute("ulticoreReportBean");
        }

        List<List<Object>> yValueListOfList = new ArrayList<List<Object>>();

        List<Object> xValueList = ulticoreReportBean.findFieldValueList(getUltiChart().getHorizontalFieldName());

        for (UlticoreVerticalFieldEntity vField : getUltiChart().getVerticalFieldList()) {
            List<Object> yValueList = ulticoreReportBean.findFieldValueList(vField.getFieldName());
            yValueListOfList.add(yValueList);
        }

        DefaultPieDataset dataset = new DefaultPieDataset();
        CategoryDataset catDataset = null;
        int colCount = 0;

        for (Object xData : xValueList) {
            if (yValueListOfList.size() == 1) {
                dataset.setValue(xData.toString(), Double.parseDouble(
                        yValueListOfList.get(0).get(colCount++).toString()));
            } else if (yValueListOfList.size() > 1) {
                double[][] yData = new double[yValueListOfList.size()][yValueListOfList.get(0).size()];
                for (int i = 0; i <= yValueListOfList.size() - 1; i++) {
                    for (int j = 0; j <= yValueListOfList.get(i).size() - 1; j++) {
                        yData[i][j] = Double.parseDouble(yValueListOfList.get(i).get(j).toString());
                    }
                }

                catDataset = DatasetUtilities.createCategoryDataset(
                        getUltiChart().getHorizontalFieldName(),
                        getUltiChart().getVerticalFieldName(),
                        yData);
            }
        }

        if (getUltiChart().getChartType() == ChartType.PIE) {
            if (getUltiChart().getDimension() == ChartDimension.THREE_DIMENSION) {
                if (getUltiChart().getVerticalFieldList().size() == 1) {
                    chart = ChartFactory.createPieChart3D(getUltiChart().getChartTitle(), dataset, true, true, false);
                }
            } else if (getUltiChart().getDimension() == ChartDimension.TWO_DIMENSION) {
                chart = ChartFactory.createPieChart(getUltiChart().getChartTitle(), dataset, true, true, false);
            }
        } else if (getUltiChart().getChartType() == ChartType.MULTIPLE_PIE) {
            if (getUltiChart().getDimension() == ChartDimension.THREE_DIMENSION) {
                if (getUltiChart().getPlotOrientation() == PlotOrientationType.HORIZONTAL) {
                    chart = ChartFactory.createMultiplePieChart3D(getUltiChart().getChartTitle(), catDataset, TableOrder.BY_ROW, true, true, false);
                } else if (getUltiChart().getPlotOrientation() == PlotOrientationType.VERTICAL) {
                    chart = ChartFactory.createMultiplePieChart3D(getUltiChart().getChartTitle(), catDataset, TableOrder.BY_COLUMN, true, true, false);
                }
            } else if (getUltiChart().getDimension() == ChartDimension.TWO_DIMENSION) {
                if (getUltiChart().getPlotOrientation() == PlotOrientationType.HORIZONTAL) {
                    chart = ChartFactory.createMultiplePieChart(getUltiChart().getChartTitle(), catDataset, TableOrder.BY_ROW, true, true, false);
                } else if (getUltiChart().getPlotOrientation() == PlotOrientationType.VERTICAL) {
                    chart = ChartFactory.createMultiplePieChart(getUltiChart().getChartTitle(), catDataset, TableOrder.BY_COLUMN, true, true, false);
                }
            }
        }

        return chart;
    }

    public JFreeChart createBarChart() {
        JFreeChart chart = null;

        UlticoreReportBean ulticoreReportBean = null;
        if (FacesContext.getCurrentInstance() != null) {
            ulticoreReportBean = (UlticoreReportBean) CommonBean.getBeanFromContext(
                    "#{sessionScope.ulticoreReportBean}", UlticoreReportBean.class);
            if (ulticoreReportBean == null) {
                ulticoreReportBean = new UlticoreReportBean();
                CommonBean.setBeanToContext("#{sessionScope.ulticoreReportBean}",
                        UlticoreReportBean.class, ulticoreReportBean);
            }
        } else {
            ulticoreReportBean = (UlticoreReportBean) ApplicationInitializer.APP_SERVLET_CONTEXT.getAttribute("ulticoreReportBean");
        }

        List<List<Object>> yValueListOfList = new ArrayList<List<Object>>();

        List<Object> xValueList = ulticoreReportBean.findFieldValueList(getUltiChart().getHorizontalFieldName());

        for (UlticoreVerticalFieldEntity vField : getUltiChart().getVerticalFieldList()) {
            List<Object> yValueList = ulticoreReportBean.findFieldValueList(vField.getFieldName());

            yValueListOfList.add(yValueList);
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        CategoryDataset catDataset = null;
        int colCount = 0;

        for (Object xData : xValueList) {
            if (yValueListOfList.size() == 1) {
                dataset.setValue(
                        Double.parseDouble(yValueListOfList.get(0).get(colCount++).toString()),
                        getUltiChart().getVerticalFieldName(),
                        xData.toString());
            } else if (yValueListOfList.size() > 1) {
                double[][] yData = new double[yValueListOfList.size()][yValueListOfList.get(0).size()];
                for (int i = 0; i <= yValueListOfList.size() - 1; i++) {
                    for (int j = 0; j <= yValueListOfList.get(i).size() - 1; j++) {
                        yData[i][j] = Double.parseDouble(yValueListOfList.get(i).get(j).toString());
                    }
                }

                catDataset = DatasetUtilities.createCategoryDataset(
                        getUltiChart().getHorizontalFieldName(),
                        getUltiChart().getVerticalFieldName(),
                        yData);
            }
        }

        PlotOrientation plotter = PlotOrientation.VERTICAL;
        if (getUltiChart().getPlotOrientation() == PlotOrientationType.HORIZONTAL) {
            plotter = PlotOrientation.HORIZONTAL;
        }

        if (getUltiChart().getChartType() == ChartType.BAR) {
            if (getUltiChart().getDimension() == ChartDimension.THREE_DIMENSION) {
                if (yValueListOfList.size() == 1) {
                    chart = ChartFactory.createBarChart3D(
                            getUltiChart().getChartTitle(),
                            getUltiChart().getHorizontalFieldName(),
                            getUltiChart().getVerticalFieldList().get(0).getFieldName(),
                            dataset, plotter, true, true, false);
                } else if (yValueListOfList.size() > 1) {
                    chart = ChartFactory.createBarChart3D(
                            getUltiChart().getChartTitle(),
                            getUltiChart().getHorizontalFieldName(),
                            getUltiChart().getVerticalFieldList().get(0).getFieldName(),
                            catDataset, plotter, true, true, false);
                }
            } else if (getUltiChart().getDimension() == ChartDimension.TWO_DIMENSION) {
                if (yValueListOfList.size() == 1) {
                    chart = ChartFactory.createBarChart(
                            getUltiChart().getChartTitle(),
                            getUltiChart().getHorizontalFieldName(),
                            getUltiChart().getVerticalFieldList().get(0).getFieldName(),
                            dataset, plotter, true, true, false);
                } else if (yValueListOfList.size() > 1) {
                    chart = ChartFactory.createBarChart(
                            getUltiChart().getChartTitle(),
                            getUltiChart().getHorizontalFieldName(),
                            getUltiChart().getVerticalFieldList().get(0).getFieldName(),
                            catDataset, plotter, true, true, false);
                }
            }
        } else if ((getUltiChart().getChartType() == ChartType.STACKED_BAR) && (catDataset != null)) {
            if (getUltiChart().getDimension() == ChartDimension.THREE_DIMENSION) {
                if (yValueListOfList.size() == 1) {
                    chart = ChartFactory.createStackedBarChart3D(
                            getUltiChart().getChartTitle(),
                            getUltiChart().getHorizontalFieldName(),
                            getUltiChart().getVerticalFieldList().get(0).getFieldName(),
                            dataset, plotter, true, true, false);
                } else if (yValueListOfList.size() > 1) {
                    chart = ChartFactory.createStackedBarChart3D(
                            getUltiChart().getChartTitle(),
                            getUltiChart().getHorizontalFieldName(),
                            getUltiChart().getVerticalFieldList().get(0).getFieldName(),
                            catDataset, plotter, true, true, false);
                }
            } else if (getUltiChart().getDimension() == ChartDimension.TWO_DIMENSION) {
                if (yValueListOfList.size() == 1) {
                    chart = ChartFactory.createStackedBarChart(
                            getUltiChart().getChartTitle(),
                            getUltiChart().getHorizontalFieldName(),
                            getUltiChart().getVerticalFieldList().get(0).getFieldName(),
                            dataset, plotter, true, true, false);
                } else if (yValueListOfList.size() > 1) {
                    chart = ChartFactory.createStackedBarChart(
                            getUltiChart().getChartTitle(),
                            getUltiChart().getHorizontalFieldName(),
                            getUltiChart().getVerticalFieldList().get(0).getFieldName(),
                            catDataset, plotter, true, true, false);
                }
            }
        } else if (getUltiChart().getChartType() == ChartType.XY_BAR) {
            //TODO...
        }

        return chart;
    }

    /**
     * @return the ultiReportList
     */
    public List<UlticoreReportEntity> getUltiReportList() {
        return ultiReportList;
    }

    /**
     * @param ultiReportList the ultiReportList to set
     */
    public void setUltiReportList(List<UlticoreReportEntity> ultiReportList) {
        this.ultiReportList = ultiReportList;
    }

    /**
     * @return the ultiChart
     */
    public UlticoreChartEntity getUltiChart() {
        return ultiChart;
    }

    /**
     * @param ultiChart the ultiChart to set
     */
    public void setUltiChart(UlticoreChartEntity ultiChart) {
        this.ultiChart = ultiChart;
    }

    /**
     * @return the plotOrientationItemList
     */
    public List<SelectItem> getPlotOrientationItemList() {
        return plotOrientationItemList;
    }

    /**
     * @param plotOrientationItemList the plotOrientationItemList to set
     */
    public void setPlotOrientationItemList(List<SelectItem> plotOrientationItemList) {
        this.plotOrientationItemList = plotOrientationItemList;
    }

    /**
     * @return the chartTypeItemList
     */
    public List<SelectItem> getChartTypeItemList() {
        return chartTypeItemList;
    }

    /**
     * @param chartTypeItemList the chartTypeItemList to set
     */
    public void setChartTypeItemList(List<SelectItem> chartTypeItemList) {
        this.chartTypeItemList = chartTypeItemList;
    }

    /**
     * @return the dimensionItemList
     */
    public List<SelectItem> getDimensionItemList() {
        return dimensionItemList;
    }

    /**
     * @param dimensionItemList the dimensionItemList to set
     */
    public void setDimensionItemList(List<SelectItem> dimensionItemList) {
        this.dimensionItemList = dimensionItemList;
    }

    /**
     * @return the ultiChartList
     */
    public List<UlticoreChartEntity> getUltiChartList() {
        return ultiChartList;
    }

    /**
     * @param ultiChartList the ultiChartList to set
     */
    public void setUltiChartList(List<UlticoreChartEntity> ultiChartList) {
        this.ultiChartList = ultiChartList;
    }

    /**
     * @return the verticalFieldName
     */
    public String getVerticalFieldName() {
        return verticalFieldName;
    }

    /**
     * @param verticalFieldName the verticalFieldName to set
     */
    public void setVerticalFieldName(String verticalFieldName) {
        this.verticalFieldName = verticalFieldName;
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
     * @return the chartGraphicImage
     */
    public StreamedContent getChartGraphicImage() {
        return chartGraphicImage;
    }

    /**
     * @param chartGraphicImage the chartGraphicImage to set
     */
    public void setChartGraphicImage(StreamedContent chartGraphicImage) {
        this.chartGraphicImage = chartGraphicImage;
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
}
