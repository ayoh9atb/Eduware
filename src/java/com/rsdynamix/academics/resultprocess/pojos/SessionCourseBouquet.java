/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.resultprocess.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author p-aniah
 */
public class SessionCourseBouquet implements Serializable {

    private List<SemesterCourseBouquet> semesterCourseList;
    private String sessionPeriod;
    private long sessionID;
    private int creditLoad;

    public SessionCourseBouquet() {
        semesterCourseList = new ArrayList<SemesterCourseBouquet>();
        sessionPeriod = "";
        sessionID = 0;
        creditLoad = 0;
    }

    public boolean equals(Object o) {
        return ((o instanceof SessionCourseBouquet) &&
                ((SessionCourseBouquet)o).getSessionPeriod().equals(this.getSessionPeriod()));
    }

    public List<SemesterCourseBouquet> getSemesterCourseList() {
        return this.semesterCourseList;
    }

    public void setSemesterCourseList(List<SemesterCourseBouquet> semesterCourseList) {
        this.semesterCourseList = semesterCourseList;
    }

    /**
     * @return the sessionPeriod
     */
    public String getSessionPeriod() {
        return sessionPeriod;
    }

    /**
     * @param sessionPeriod the sessionPeriod to set
     */
    public void setSessionPeriod(String sessionPeriod) {
        this.sessionPeriod = sessionPeriod;
    }

    public long getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    /**
     * @return the creditLoad
     */
    public int getCreditLoad() {
        return creditLoad;
    }

    /**
     * @param creditLoad the creditLoad to set
     */
    public void setCreditLoad(int creditLoad) {
        this.creditLoad = creditLoad;
    }

}
