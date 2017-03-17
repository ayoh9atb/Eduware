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
public class DailyTimetable implements Serializable {

    private String courseTitle;
    private String courseCode;
    private String startTime;
    private String endTime;
    private double creditLoad;
    private String lectureVenue;

    public DailyTimetable() {
        courseTitle = "";
        courseCode = "";
        startTime = "";
        endTime = "";
        creditLoad = 0;
        lectureVenue = "";
    }

    /**
     * @return the courseTitle
     */
    public String getCourseTitle() {
        return courseTitle;
    }

    /**
     * @param courseTitle the courseTitle to set
     */
    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
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
     * @return the stopTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param stopTime the stopTime to set
     */
    public void setEndTime(String stopTime) {
        this.endTime = stopTime;
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

    /**
     * @return the courseCode
     */
    public String getCourseCode() {
        return courseCode;
    }

    /**
     * @param courseCode the courseCode to set
     */
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

}
