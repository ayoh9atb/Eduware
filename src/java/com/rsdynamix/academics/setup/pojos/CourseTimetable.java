/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.setup.pojos;

import java.io.Serializable;

/**
 *
 * @author p-aniah
 */
public class CourseTimetable implements Serializable {

    private String dayOfWeek;
    private String startTime;
    private String endTime;
    private double creditLoad;
    private String lectureVenue;

    public CourseTimetable() {
        dayOfWeek = "";
        startTime = "";
        endTime = "";
        creditLoad = 0;
        lectureVenue = "";
    }

    /**
     * @return the dayOfWeek
     */
    public String getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * @param dayOfWeek the dayOfWeek to set
     */
    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    /**
     * @return the startTime
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the creditLoad
     */
    public double getCreditLoad() {
        return creditLoad;
    }

    /**
     * @param creditLoad the creditLoad to set
     */
    public void setCreditLoad(double creditLoad) {
        this.creditLoad = creditLoad;
    }

    /**
     * @return the lectureVenue
     */
    public String getLectureVenue() {
        return lectureVenue;
    }

    /**
     * @param lectureVenue the lectureVenue to set
     */
    public void setLectureVenue(String lectureVenue) {
        this.lectureVenue = lectureVenue;
    }

}
