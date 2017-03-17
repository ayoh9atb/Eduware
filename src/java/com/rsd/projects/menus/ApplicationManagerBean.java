/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsd.projects.menus;

import com.rsdynamix.projects.security.entities.UserAccountEntity;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author patushie
 */
@ApplicationScoped
@ManagedBean(name = "applicationManagerBean")
public class ApplicationManagerBean {
    
    private List<UserAccountEntity> userAccountList;

    public ApplicationManagerBean() {
        userAccountList = new ArrayList<UserAccountEntity>();
    }
    
    

    /**
     * @return the userAccountList
     */
    public List<UserAccountEntity> getUserAccountList() {
        return userAccountList;
    }

    /**
     * @param userAccountList the userAccountList to set
     */
    public void setUserAccountList(List<UserAccountEntity> userAccountList) {
        this.userAccountList = userAccountList;
    }

}
