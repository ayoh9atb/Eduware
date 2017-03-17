/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsd.projects.menus;

import com.codrellica.projects.commons.DateUtil;
import com.rsdynamix.abstractobjects.AbstractDataField;
import com.rsdynamix.abstractobjects.AbstractFieldMetaEntity;
import com.rsdynamix.abstractobjects.PersistConstraintType;
import com.rsdynamix.abstractobjects.SequenceOrientationType;
import com.rsdynamix.abstractobjects.UlticoreLOVFieldMetaEntity;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamix.projects.web.commons.bean.RendererType;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

/**
 *
 * @author root
 */
@ApplicationScoped
@ManagedBean(name = "valueListBean")
public class ValueListBean {

    private Map<String, List<SelectItem>> valueListMap;
    private Map<String, List<SelectItem>> selectedValueListMap;
    private List<SelectItem> valueCategoryTypeItemList;
    private List<SelectItem> valueEntityItemList;

    public ValueListBean(){
        valueListMap = new HashMap<String, List<SelectItem>>();
        valueCategoryTypeItemList = new ArrayList<SelectItem>();

        SelectItem item = new SelectItem();
        item.setValue(RendererType.NONE);
        item.setLabel(RendererType.NONE.toString());
        valueCategoryTypeItemList.add(item);

        item = new SelectItem();
        item.setValue(RendererType.INPUT_TEXT_NAME);
        item.setLabel(RendererType.INPUT_TEXT_NAME.toString());
        valueCategoryTypeItemList.add(item);

        item = new SelectItem();
        item.setValue(RendererType.INPUT_TEXT_AREA_NAME);
        item.setLabel(RendererType.INPUT_TEXT_AREA_NAME.toString());
        valueCategoryTypeItemList.add(item);

        item = new SelectItem();
        item.setValue(RendererType.DATE_FIELD_NAME);
        item.setLabel(RendererType.DATE_FIELD_NAME.toString());
        valueCategoryTypeItemList.add(item);

        item = new SelectItem();
        item.setValue(RendererType.SELECT_ONE_MENU);
        item.setLabel(RendererType.SELECT_ONE_MENU.toString());
        valueCategoryTypeItemList.add(item);
    }

    public void abstractFieldValueSelected(ValueChangeEvent vce) {
        
    }

    public SelectItem findValueCategoryTypeItem(String itemValue) {
        SelectItem item = new SelectItem();

        for (SelectItem itm : getValueCategoryTypeItemList()) {
            if (itm.getValue().toString().equals(itemValue)) {
                item = itm;
                break;
            }
        }

        return item;
    }

    public SelectItem findSourceItemByKeyAndValue(String valueSourceKey, Object value) {
        SelectItem sourceItem = null;

        if (value != null) {
            List<SelectItem> sourceItemList = valueListMap.get(valueSourceKey);
            for (SelectItem item : sourceItemList) {
                if (value instanceof Integer) {
                    int value1 = Integer.parseInt(item.getValue().toString());
                    int value2 = Integer.parseInt(value.toString());

                    if (value1 == value2) {
                        sourceItem = item;
                        break;
                    }
                } else if (value instanceof Double) {
                    double value1 = Double.parseDouble(item.getValue().toString());
                    double value2 = Double.parseDouble(value.toString());

                    if (value1 == value2) {
                        sourceItem = item;
                        break;
                    }
                } else if (value instanceof String) {
                    if (item.getValue().toString().equals(value.toString())) {
                        sourceItem = item;
                        break;
                    }
                } else if (value instanceof Date) {
                    if (DateUtil.isSameDate((Date) item.getValue(), (Date) value)) {
                        sourceItem = item;
                        break;
                    }
                }
            }
        }

        return sourceItem;
    }

    /**
     * @return the valueListMap
     */
    public Map<String, List<SelectItem>> getValueListMap() {
        return valueListMap;
    }

    /**
     * @param valueListMap the valueListMap to set
     */
    public void setValueListMap(Map<String, List<SelectItem>> valueListMap) {
        this.valueListMap = valueListMap;
    }

    /**
     * @return the valueCategoryTypeItemList
     */
    public List<SelectItem> getValueCategoryTypeItemList() {
        return valueCategoryTypeItemList;
    }

    /**
     * @param valueCategoryTypeItemList the valueCategoryTypeItemList to set
     */
    public void setValueCategoryTypeItemList(List<SelectItem> valueCategoryTypeItemList) {
        this.valueCategoryTypeItemList = valueCategoryTypeItemList;
    }

    /**
     * @return the selectedValueListMap
     */
    public Map<String, List<SelectItem>> getSelectedValueListMap() {
        return selectedValueListMap;
    }

    /**
     * @param selectedValueListMap the selectedValueListMap to set
     */
    public void setSelectedValueListMap(Map<String, List<SelectItem>> selectedValueListMap) {
        this.selectedValueListMap = selectedValueListMap;
    }

    /**
     * @return the valueEntityItemList
     */
    public List<SelectItem> getValueEntityItemList() {
        return valueEntityItemList;
    }

    /**
     * @param valueEntityItemList the valueEntityItemList to set
     */
    public void setValueEntityItemList(List<SelectItem> valueEntityItemList) {
        this.valueEntityItemList = valueEntityItemList;
    }

}
