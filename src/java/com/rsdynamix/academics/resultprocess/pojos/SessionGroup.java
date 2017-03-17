/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.resultprocess.pojos;

import java.io.Serializable;

/**
 *
 * @author p-aniah
 */
public class SessionGroup implements Serializable {

    private long sessionID;
    private String sessionPeriod;

    public SessionGroup() {
        sessionID = 0;
        sessionPeriod = "";
    }

    public boolean equals(Object o) {
        return ((o instanceof SessionGroup) &&
                (((SessionGroup)o).getSessionPeriod().equals(this.getSessionPeriod())));
    }

    /**
     * @return the sessionID
     */
    public long getSessionID() {
        return sessionID;
    }

    /**
     * @param sessionID the sessionID to set
     */
    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionPeriod() {
        return this.sessionPeriod;
    }

    public void setSessionPeriod(String sessionPeriod) {
        this.sessionPeriod = sessionPeriod;
    }

}
