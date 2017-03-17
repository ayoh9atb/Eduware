/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.setup.entities;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.dynamo.common.entities.BusinessActionTrailEntity;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author p-aniah
 */
@Entity
@Table(name = "ACADEMIC_TIMETABLE")
public class AcademicTimetableEntity extends AbstractEntity implements Serializable {

    @Id
    @Column(name = "TIMETABLE_ID", columnDefinition = "NUMBER(11)")
    private long timetableID;
    //
    private transient String courseCode;
    private transient double creditLoad;
    private transient String courseTitle;
    @Column(name = "COURSE_ID", columnDefinition = "NUMBER(11)")
    private long courseID;
    //
    private transient String facultyName;
    @Column(name = "FACULTY_ID", columnDefinition = "NUMBER(11)")
    private long facultyID;
    //
    private transient String departmentName;
    @Column(name = "DEPARTMENT_ID", columnDefinition = "NUMBER(11)")
    private long departmentID;
    //
    @Column(name = "DAY_OF_WEEK", columnDefinition = "VARCHAR2(3)")
    private String dayOfWeek;
    //
    @Column(name = "START_TIME", columnDefinition = "VARCHAR2(7)")
    private String startTime;
    //
    @Column(name = "END_TIME", columnDefinition = "VARCHAR2(7)")
    private String endTime;
    //
    private transient String lectureVenueDesc;
    @Column(name = "LECTURE_VENUE_ID", columnDefinition = "NUMBER(5)")
    private long lectureVenueID;
    //
    private transient String sessionPeriod;
    @Column(name = "SESSION_ID", columnDefinition = "NUMBER(11)")
    private long sessionID;
    //
    private transient String employeeName;
    @Column(name = "USERNAME", columnDefinition = "VARCHAR2(65)")
    private String userName;
    //
    @Column(name = "APPROVAL_STATUS_ID", columnDefinition = "NUMBER(3)")
    private int approvalStatusID = 0;
    //
    @Column(name = "APPROVER_NAME", columnDefinition = "Varchar2(60)")
    private String approver = "";
    //
    private transient BusinessActionTrailEntity actionTrail = new BusinessActionTrailEntity();
    //
    private transient String meetingStartTimeHour;
    private transient String meetingStartTimeMinute;
    private transient String meetingStartTimeMeridiem;
    private transient String meetingStopTimeHour;
    private transient String meetingStopTimeMinute;
    private transient String meetingStopTimeMeridiem;

    public AcademicTimetableEntity() {
        timetableID = 0;
        courseTitle = "";
        courseCode = "";
        creditLoad = 0;
        courseID = 0;

        facultyName = "";
        facultyID = 0;
        departmentName = "";
        departmentID = 0;

        dayOfWeek = "";
        startTime = "";
        endTime = "";

        lectureVenueDesc = "";
        lectureVenueID = 0;
        employeeName = "";
        userName = "";

        sessionPeriod = "";
        sessionID = 0;

        meetingStartTimeHour = "";
        meetingStartTimeMinute = "";

        meetingStartTimeMeridiem = "";
        meetingStopTimeHour = "";

        meetingStopTimeMinute = "";
        meetingStopTimeMeridiem = "";
    }

    public boolean equals(Object o) {
        return ((o instanceof AcademicTimetableEntity) &&
                (((AcademicTimetableEntity)o).getCourseCode().equals(this.getCourseCode())) &&
                (((AcademicTimetableEntity)o).getDayOfWeek().equals(this.getDayOfWeek())) &&
                (((AcademicTimetableEntity)o).getSessionPeriod().equals(this.getSessionPeriod())));
    }

    /**
     * @return the timetableID
     */
    public long getTimetableID() {
        return timetableID;
    }

    /**
     * @param timetableID the timetableID to set
     */
    public void setTimetableID(long timetableID) {
        this.timetableID = timetableID;
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
     * @return the facultyName
     */
    public String getFacultyName() {
        return facultyName;
    }

    /**
     * @param facultyName the facultyName to set
     */
    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }

    /**
     * @return the facultyID
     */
    public long getFacultyID() {
        return facultyID;
    }

    /**
     * @param facultyID the facultyID to set
     */
    public void setFacultyID(long facultyID) {
        this.facultyID = facultyID;
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
     * @return the lectureVenueDesc
     */
    public String getLectureVenueDesc() {
        return lectureVenueDesc;
    }

    /**
     * @param lectureVenueDesc the lectureVenueDesc to set
     */
    public void setLectureVenueDesc(String lectureVenueDesc) {
        this.lectureVenueDesc = lectureVenueDesc;
    }

    /**
     * @return the lectureVenueID
     */
    public long getLectureVenueID() {
        return lectureVenueID;
    }

    /**
     * @param lectureVenueID the lectureVenueID to set
     */
    public void setLectureVenueID(long lectureVenueID) {
        this.lectureVenueID = lectureVenueID;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
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

    /**
     * @return the departmentName
     */
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @param departmentName the departmentName to set
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * @return the departmentID
     */
    public long getDepartmentID() {
        return departmentID;
    }

    /**
     * @param departmentID the departmentID to set
     */
    public void setDepartmentID(long departmentID) {
        this.departmentID = departmentID;
    }

    /**
     * @return the meetingStartTimeHour
     */
    public String getMeetingStartTimeHour() {
        return meetingStartTimeHour;
    }

    /**
     * @param meetingStartTimeHour the meetingStartTimeHour to set
     */
    public void setMeetingStartTimeHour(String meetingStartTimeHour) {
        this.meetingStartTimeHour = meetingStartTimeHour;
    }

    /**
     * @return the meetingStartTimeMinute
     */
    public String getMeetingStartTimeMinute() {
        return meetingStartTimeMinute;
    }

    /**
     * @param meetingStartTimeMinute the meetingStartTimeMinute to set
     */
    public void setMeetingStartTimeMinute(String meetingStartTimeMinute) {
        this.meetingStartTimeMinute = meetingStartTimeMinute;
    }

    /**
     * @return the meetingStartTimeMeridiem
     */
    public String getMeetingStartTimeMeridiem() {
        return meetingStartTimeMeridiem;
    }

    /**
     * @param meetingStartTimeMeridiem the meetingStartTimeMeridiem to set
     */
    public void setMeetingStartTimeMeridiem(String meetingStartTimeMeridiem) {
        this.meetingStartTimeMeridiem = meetingStartTimeMeridiem;
    }

    /**
     * @return the meetingStopTimeHour
     */
    public String getMeetingStopTimeHour() {
        return meetingStopTimeHour;
    }

    /**
     * @param meetingStopTimeHour the meetingStopTimeHour to set
     */
    public void setMeetingStopTimeHour(String meetingStopTimeHour) {
        this.meetingStopTimeHour = meetingStopTimeHour;
    }

    /**
     * @return the meetingStopTimeMinute
     */
    public String getMeetingStopTimeMinute() {
        return meetingStopTimeMinute;
    }

    /**
     * @param meetingStopTimeMinute the meetingStopTimeMinute to set
     */
    public void setMeetingStopTimeMinute(String meetingStopTimeMinute) {
        this.meetingStopTimeMinute = meetingStopTimeMinute;
    }

    /**
     * @return the meetingStopTimeMeridiem
     */
    public String getMeetingStopTimeMeridiem() {
        return meetingStopTimeMeridiem;
    }

    /**
     * @param meetingStopTimeMeridiem the meetingStopTimeMeridiem to set
     */
    public void setMeetingStopTimeMeridiem(String meetingStopTimeMeridiem) {
        this.meetingStopTimeMeridiem = meetingStopTimeMeridiem;
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
     * @return the approvalStatusID
     */
    public int getApprovalStatusID() {
        return approvalStatusID;
    }

    /**
     * @param approvalStatusID the approvalStatusID to set
     */
    public void setApprovalStatusID(int approvalStatusID) {
        this.approvalStatusID = approvalStatusID;
    }

    /**
     * @return the approver
     */
    public String getApprover() {
        return approver;
    }

    /**
     * @param approver the approver to set
     */
    public void setApprover(String approver) {
        this.approver = approver;
    }

    /**
     * @return the actionTrail
     */
    public BusinessActionTrailEntity getActionTrail() {
        return actionTrail;
    }

    /**
     * @param actionTrail the actionTrail to set
     */
    public void setActionTrail(BusinessActionTrailEntity actionTrail) {
        this.actionTrail = actionTrail;
    }
}
