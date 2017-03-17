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
public class StudentDailyTimetable implements Serializable {

    private String dayOfWeek;
    private int totalCreditLoad;
    private int entryCount;
    private List<DailyTimetable> timetable;

    public StudentDailyTimetable() {
        dayOfWeek = "";
        totalCreditLoad = 0;
        entryCount = 0;
        timetable = new ArrayList<DailyTimetable>();
    }

    public void addTimetableEntry(String dayOfWk, DailyTimetable entry) {
        dayOfWeek = dayOfWk;
        timetable.add(entry);
        totalCreditLoad += entry.getCreditLoad();
        entryCount++;
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
     * @return the timetableList
     */
    public List<DailyTimetable> getTimetable() {
        return timetable;
    }

    /**
     * @param timetableList the timetableList to set
     */
    public void setTimetable(List<DailyTimetable> timetableList) {
        this.timetable = timetableList;
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
