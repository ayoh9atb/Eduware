/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.dynamo.common.setup.beans;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.rsdynamix.projects.common.sequences.beans.ApplicationPropertyBean;
import com.rsdynamix.projects.commons.entities.MiscAttributeEntity;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.tns.util.Constants;
import com.rsdynamics.finance.constants.FinultimateConstants;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "miscAttributeBean")
public class MiscAttributeBean implements Serializable {

    private MiscAttributeEntity miscAttribute;
    private List<MiscAttributeEntity> miscAttributeList;

    public MiscAttributeBean() {
        miscAttribute = new MiscAttributeEntity();
        miscAttributeList = new ArrayList<MiscAttributeEntity>();
    }

    public String loadMiscAttributeList(long objectRefID) {
        miscAttribute = new MiscAttributeEntity();
        miscAttributeList = new ArrayList<MiscAttributeEntity>();

        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        try {
            MiscAttributeEntity criteria = new MiscAttributeEntity();
            criteria.setEntityReferenceID(objectRefID);
            List<AbstractEntity> entityList = dataServer.findData(criteria);
            for (AbstractEntity entity : entityList) {
                MiscAttributeEntity miscAttribute = (MiscAttributeEntity) entity;
                miscAttributeList.add(miscAttribute);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }

        return "";
    }

    public String addMiscAttribute(ActionEvent ae) {
        if (!miscAttributeList.contains(miscAttribute)) {
            miscAttributeList.add(miscAttribute);
        } else {
            int index = miscAttributeList.indexOf(miscAttribute);
            miscAttributeList.set(index, miscAttribute);
        }
        miscAttribute = new MiscAttributeEntity();

        return "";
    }

    public void miscAttributeSelected(ValueChangeEvent vce) {
        int rowIndex = CommonBean.getComponentEventRowIndex(vce);
        miscAttribute = miscAttributeList.get(rowIndex);
    }

    public String saveMiscAttributeList(long objectRefID) {
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

        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        dataServer.beginTransaction();
        try {
            for (MiscAttributeEntity miscAttribute : miscAttributeList) {
                miscAttribute.setEntityReferenceID(objectRefID);

                if (miscAttribute.getAttributeID() == 0) {
                    int miscAttributeID = Integer.parseInt(appPropBean.getValue(
                            FinultimateConstants.MISC_ATTRIBUTE_ID,
                            FinultimateConstants.ONE_STR, true));
                    miscAttribute.setAttributeID(miscAttributeID);

                    dataServer.setSelectedTnsName(Constants.FINULTIMATE_PU);
                    dataServer.saveData(miscAttribute);
                } else {
                    dataServer.setSelectedTnsName(Constants.FINULTIMATE_PU);
                    dataServer.updateData(miscAttribute);
                }
            }
        } catch (SQLException ex) {
            dataServer.rollbackTransaction();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (Exception ex) {
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
            ex.printStackTrace();
        }

        dataServer.endTransaction();

        return "";
    }

    public String clearMiscAttribute(ActionEvent ae) {
        miscAttribute = new MiscAttributeEntity();
        miscAttributeList = new ArrayList<MiscAttributeEntity>();

        return "";
    }

    public MiscAttributeEntity findMiscAttribute(int miscAttributeID) {
        MiscAttributeEntity miscAttribute = null;

        for (MiscAttributeEntity entity : miscAttributeList) {
            if (entity.getAttributeID() == miscAttributeID) {
                miscAttribute = entity;
                break;
            }
        }

        return miscAttribute;
    }

    /**
     * @return the miscAttribute
     */
    public MiscAttributeEntity getMiscAttribute() {
        return miscAttribute;
    }

    /**
     * @param miscAttribute the miscAttribute to set
     */
    public void setMiscAttribute(MiscAttributeEntity miscAttribute) {
        this.miscAttribute = miscAttribute;
    }

    /**
     * @return the miscAttributeList
     */
    public List<MiscAttributeEntity> getMiscAttributeList() {
        return miscAttributeList;
    }

    /**
     * @param miscAttributeList the miscAttributeList to set
     */
    public void setMiscAttributeList(List<MiscAttributeEntity> miscAttributeList) {
        this.miscAttributeList = miscAttributeList;
    }

}
