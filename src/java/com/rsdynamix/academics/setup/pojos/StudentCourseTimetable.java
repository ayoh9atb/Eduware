/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.setup.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author p-aniah
 */
public class StudentCourseTimetable implements Serializable {

    private String courseTitle;
    private String courseCode;
    private int totalCreditLoad;
    private int entryCount;
    private List<CourseTimetable> timetable;

    public StudentCourseTimetable() {
        courseTitle = "";
        courseCode = "";
        totalCreditLoad = 0;
        entryCount = 0;
        timetable = new ArrayList<CourseTimetable>();
    }

    public void addTimetableEntry(String courseCode, String courseTitle, CourseTimetable entry) {
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        timetable.add(entry);
        totalCreditLoad += entry.getCreditLoad();
        entryCount++;
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

    /**
     * @return the totalCreditLoad
     */
    public int getTotalCreditLoad() {
        return totalCreditLoad;
    }

    /**
     * @param totalCreditLoad the totalCreditLoad to set
     */
    public void setTotalCreditLoad(int totalCreditLoad) {
        this.totalCreditLoad = totalCreditLoad;
    }

    /**
     * @return the timetable
     */
    public List<CourseTimetable> getTimetable() {
        return timetable;
    }

    /**
     * @param timetable the timetable to set
     */
    public void setTimetable(List<CourseTimetable> timetable) {
        this.timetable = timetable;
    }

    /**
     * @return the entryCount
     */
    public int getEntryCount() {
        return entryCount;
    }

    /**
     * @param entryCount the entryCount to set
     */
    public void setEntryCount(int entryCount) {
        this.entryCount = entryCount;
    }

}
