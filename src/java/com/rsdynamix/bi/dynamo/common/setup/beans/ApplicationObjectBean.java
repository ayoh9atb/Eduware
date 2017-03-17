/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.bi.dynamo.common.setup.beans;

import com.rsdynamix.dynamo.applicationdata.ApplicationDataHolder;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ushie
 */
public class ApplicationObjectBean implements Serializable {

    private ApplicationDataHolder objectHolder;

    public ApplicationObjectBean() {
        objectHolder = new ApplicationDataHolder();
        populateDataHolderWithApplicationObjects();
    }

    public void populateDataHolderWithApplicationObjects(){
        //objectHolder.addDataObject(new Page());
        //objectHolder.addDataObject(new PageComponent());
    }

    /**
     * @return the objectHolder
     */
    public ApplicationDataHolder getObjectHolder() {
        return objectHolder;
    }

    /**
     * @param objectHolder the objectHolder to set
     */
    public void setObjectHolder(ApplicationDataHolder objectHolder) {
        this.objectHolder = objectHolder;
    }

}
