/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsd.projects.menus;

import com.rsdynamix.abstractobjects.AbstractAdaptableEntity;
import com.rsdynamix.abstractobjects.AbstractFieldMetaEntity;
import com.rsdynamix.projects.report.entities.UlticoreReportEntity;
import com.rsdynamix.projects.report.entities.UlticoreReportFieldMetaEntity;
import com.rsdynamix.projects.report.entities.UlticoreSubreportEntity;
import com.rsdynamix.projects.report.entities.UlticoreSubreportFieldMetaEntity;
import com.rsdynamix.projects.ulticoreparser.oop.dataobjects.ReportFieldFormatType;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ushie
 */
public class FinultimateCommons {

    public FinultimateCommons() {
        
    }
    
    public static String retrieveLastRequestingResource() {
        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}",
                    MenuManagerBean.class, menuManagerBean);
        }

        return menuManagerBean.retrieveLastRequestingResource();
    }

    public static void captureRequestingResource() {
        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}",
                    MenuManagerBean.class, menuManagerBean);
        }

        menuManagerBean.captureRequestingResource();
    }

    public static void captureRequestingResource(String contextPath) {
        MenuManagerBean menuManagerBean = (MenuManagerBean) CommonBean.getBeanFromContext(
                "#{sessionScope.menuManagerBean}", MenuManagerBean.class);
        if (menuManagerBean == null) {
            menuManagerBean = new MenuManagerBean();
            CommonBean.setBeanToContext("#{sessionScope.menuManagerBean}",
                    MenuManagerBean.class, menuManagerBean);
        }

        menuManagerBean.captureRequestingResource(contextPath);
    }
    
    public static AbstractAdaptableEntity createAbstractField(
            AbstractAdaptableEntity adaptableEntity, String fieldName, Object fieldValue) {
        AbstractFieldMetaEntity fieldMeta = null;
        boolean hasField = false;

        if (adaptableEntity instanceof UlticoreReportEntity) {
            AbstractFieldMetaEntity field = ((UlticoreReportEntity) adaptableEntity).findReportFieldMetaByDBField(fieldName);
            if (field != null) {
                hasField = true;
            } else {
                fieldMeta = new UlticoreReportFieldMetaEntity();
            }
        }

        if (!hasField) {
            fieldMeta.setDatabaseFieldName(CommonBean.columnize(fieldName));
            fieldMeta.setEntityFieldName(fieldName);

            adaptableEntity.getAbstractFieldMetaList().add(fieldMeta);

            if (adaptableEntity.getObjectFieldList().size() > 0) {
                adaptableEntity.getObjectFieldList().get(0).add(fieldValue);
            } else {
                List<Object> fieldRecord = new ArrayList<Object>();
                fieldRecord.add(fieldValue);
                adaptableEntity.getObjectFieldList().add(fieldRecord);
            }
        }

        return adaptableEntity;
    }

    public static AbstractAdaptableEntity createAbstractFieldMeta(
            AbstractAdaptableEntity adaptableEntity, String fieldName) {
        AbstractFieldMetaEntity fieldMeta = null;

        if (adaptableEntity instanceof UlticoreReportEntity) {
            fieldMeta = new UlticoreReportFieldMetaEntity();
        }

        fieldMeta.setDatabaseFieldName(fieldName);
        fieldMeta.setEntityFieldName(fieldName);
        adaptableEntity.getAbstractFieldMetaList().add(fieldMeta);

        return adaptableEntity;
    }

    public static UlticoreReportEntity createReportField(UlticoreReportEntity quoteReport, String fieldName, Object fieldValue) {
        UlticoreReportFieldMetaEntity reportFieldMeta = new UlticoreReportFieldMetaEntity();

        reportFieldMeta.setDatabaseFieldName(CommonBean.columnize(fieldName));
        reportFieldMeta.setEntityFieldName(fieldName);
        quoteReport.getAbstractFieldMetaList().add(reportFieldMeta);

        if (quoteReport.getObjectFieldList().size() > 0) {
            quoteReport.getObjectFieldList().get(0).add(fieldValue);
        } else {
            List<Object> fieldRecord = new ArrayList<Object>();
            fieldRecord.add(fieldValue);
            quoteReport.getObjectFieldList().add(fieldRecord);
        }

        return quoteReport;
    }

    public static UlticoreReportEntity createReportField(
            UlticoreReportEntity quoteReport,
            String fieldName,
            Object fieldValue,
            ReportFieldFormatType fieldFormatType,
            int maxFractionDigits,
            int minFractionDigits) {
        UlticoreReportFieldMetaEntity reportFieldMeta = new UlticoreReportFieldMetaEntity();

        reportFieldMeta.setDatabaseFieldName(CommonBean.columnize(fieldName));
        reportFieldMeta.setEntityFieldName(fieldName);
        reportFieldMeta.setReportFieldFormatType(fieldFormatType);
        reportFieldMeta.setMaxFractionDigits(maxFractionDigits);
        reportFieldMeta.setMinFractionDigits(minFractionDigits);
        quoteReport.getAbstractFieldMetaList().add(reportFieldMeta);

        if (quoteReport.getObjectFieldList().size() > 0) {
            quoteReport.getObjectFieldList().get(0).add(fieldValue);
        } else {
            List<Object> fieldRecord = new ArrayList<Object>();
            fieldRecord.add(fieldValue);
            quoteReport.getObjectFieldList().add(fieldRecord);
        }

        return quoteReport;
    }

    public static UlticoreReportEntity createReportFieldMeta(
            UlticoreReportEntity quoteReport, String fieldName) {
        UlticoreReportFieldMetaEntity reportFieldMeta = new UlticoreReportFieldMetaEntity();

        reportFieldMeta.setDatabaseFieldName(fieldName);
        reportFieldMeta.setEntityFieldName(fieldName);
        quoteReport.getAbstractFieldMetaList().add(reportFieldMeta);

        return quoteReport;
    }

    public static UlticoreReportEntity createReportFieldMeta(
            UlticoreReportEntity quoteReport,
            String fieldName,
            ReportFieldFormatType fieldFormatType,
            int maxFractionDigits,
            int minFractionDigits) {
        UlticoreReportFieldMetaEntity reportFieldMeta = new UlticoreReportFieldMetaEntity();

        reportFieldMeta.setDatabaseFieldName(fieldName);
        reportFieldMeta.setEntityFieldName(fieldName);

        reportFieldMeta.setReportFieldFormatType(fieldFormatType);
        reportFieldMeta.setMaxFractionDigits(maxFractionDigits);
        reportFieldMeta.setMinFractionDigits(minFractionDigits);

        quoteReport.getAbstractFieldMetaList().add(reportFieldMeta);

        return quoteReport;
    }

    public static UlticoreSubreportEntity createSubreportFieldMeta(UlticoreSubreportEntity quoteReport, String fieldName) {
        UlticoreSubreportFieldMetaEntity reportFieldMeta = new UlticoreSubreportFieldMetaEntity();

        reportFieldMeta.setDatabaseFieldName(fieldName);
        reportFieldMeta.setEntityFieldName(fieldName);
        quoteReport.getAbstractFieldMetaList().add(reportFieldMeta);

        return quoteReport;
    }

    public static UlticoreSubreportEntity createSubreportFieldMeta(
            UlticoreSubreportEntity quoteReport,
            String fieldName,
            ReportFieldFormatType fieldFormatType,
            int maxFractionDigits,
            int minFractionDigits) {
        UlticoreSubreportFieldMetaEntity reportFieldMeta = new UlticoreSubreportFieldMetaEntity();

        reportFieldMeta.setDatabaseFieldName(fieldName);
        reportFieldMeta.setEntityFieldName(fieldName);

        reportFieldMeta.setReportFieldFormatType(fieldFormatType);
        reportFieldMeta.setMaxFractionDigits(maxFractionDigits);
        reportFieldMeta.setMinFractionDigits(minFractionDigits);

        quoteReport.getAbstractFieldMetaList().add(reportFieldMeta);

        return quoteReport;
    }

}
