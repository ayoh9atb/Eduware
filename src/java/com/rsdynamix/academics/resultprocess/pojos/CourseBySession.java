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
public class CourseBySession implements Serializable {

    private long courseID;
    private long sessionID;

    public CourseBySession() {
        courseID = 0;
        sessionID = 0;
    }

    public boolean equals(Object o) {
        return ((o instanceof CourseBySession) &&
                (((CourseBySession)o).getCourseID() == this.getCourseID()) &&
                (((CourseBySession)o).getSessionID() == this.getSessionID()));
    }

    /**
     * @return the courseID
     */
    public long getCourseID() {
        return courseID;
    }

    /**
     * @param courseID the courseID to set
     */
    public void setCourseID(long courseID) {
        this.courseID = courseID;
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

}
