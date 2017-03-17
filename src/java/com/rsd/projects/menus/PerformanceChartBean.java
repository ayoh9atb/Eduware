/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsd.projects.menus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.MeterGaugeChartModel;
import org.primefaces.model.chart.PieChartModel;

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "performanceChartBean")
public class PerformanceChartBean implements Serializable {
    
    private PieChartModel pieModel1;
    private PieChartModel pieModel2;
    //
    private MeterGaugeChartModel meterGaugeModel1;
    private MeterGaugeChartModel meterGaugeModel2;
    //
    private BarChartModel barModel;

    public PerformanceChartBean () {
        createTop5AgentPerformanceModel();
        createProductPerformanceModel();
        createMeterGaugeModels();
    }
     
    private void createTop5AgentPerformanceModel() {
        pieModel2 = new PieChartModel();
         
        pieModel2.set("Hog Robinson", 1580000000);
        pieModel2.set("Osuofia & Co.", 1421000000);
        pieModel2.set("Glanville", 1702000000);
        pieModel2.set("Stella Adams", 921000000);
        pieModel2.set("Dodie Brokers", 456000000);
        
        pieModel2.setTitle("Top 5 Agents/Marketers");
        pieModel2.setLegendPosition("e");
        pieModel2.setFill(false);
        pieModel2.setShowDataLabels(true);
        pieModel2.setDiameter(150);
    }
    
    private MeterGaugeChartModel initMeterGaugeModel() {
        List<Number> intervals = new ArrayList<Number>(){{
            add(20);
            add(50);
            add(120);
            add(220);
        }};
         
        return new MeterGaugeChartModel(140, intervals);
    }
 
    private void createMeterGaugeModels() {
        meterGaugeModel1 = initMeterGaugeModel();
        meterGaugeModel1.setTitle("Target Guage");
        meterGaugeModel1.setGaugeLabel("position/target");
         
        meterGaugeModel2 = initMeterGaugeModel();
        meterGaugeModel2.setTitle("Performance Guage");
        meterGaugeModel2.setSeriesColors("66cc66,93b75f,E7E658,cc6666");
        meterGaugeModel2.setGaugeLabel("position/target");
        meterGaugeModel2.setGaugeLabelPosition("bottom");
        meterGaugeModel2.setShowTickLabels(false);
        meterGaugeModel2.setLabelHeightAdjust(110);
        meterGaugeModel2.setIntervalOuterRadius(100);
    }
    
    private void createProductPerformanceModel() {
        barModel = new BarChartModel();
 
        ChartSeries product = new ChartSeries();
        product.setLabel("NO GUARANTEE");
        product.set("LEVEL", 140000000);
        product.set("INC 5%", 200000000);
        product.set("INC 10%", 421000000);
        product.set("LEVEL, SPOUSE", 450000000);
        barModel.addSeries(product);
        
        product = new ChartSeries();
        product.setLabel("5 YR GUARANTEE");
        product.set("LEVEL", 325000000);
        product.set("INC 5%", 456000000);
        product.set("INC 10%", 460000000);
        product.set("LEVEL, SPOUSE", 500000000);
        barModel.addSeries(product);
        
        product = new ChartSeries();
        product.setLabel("10 YR GUARANTEE");
        product.set("LEVEL", 502000000);
        product.set("INC 5%", 420000000);
        product.set("INC 10%", 320000000);
        product.set("LEVEL, SPOUSE", 580000000);
        barModel.addSeries(product);
         
        barModel.setTitle("Performance By Product");
        barModel.setLegendPosition("e");
        barModel.setStacked(true);
         
        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Guarantee Type");
        xAxis.setMin(0);
        xAxis.setMax(10000000000L);
         
        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Performance");       
    }
    
    public MeterGaugeChartModel getMeterGaugeModel1() {
        return meterGaugeModel1;
    }
     
    public MeterGaugeChartModel getMeterGaugeModel2() {
        return meterGaugeModel2;
    }
    
    public PieChartModel getPieModel1() {
        return pieModel1;
    }
     
    public PieChartModel getPieModel2() {
        return pieModel2;
    }

    /**
     * @return the barModel
     */
    public BarChartModel getBarModel() {
        return barModel;
    }

    /**
     * @param barModel the barModel to set
     */
    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

}
