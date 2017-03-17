/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.setup.bean;

import com.rsdynamix.abstractobjects.AbstractEntity;
import com.rsdynamix.abstractobjects.QueryFieldGate;
import com.rsdynamix.academics.setup.entities.AcademicTimetableEntity;
import com.rsdynamix.academics.setup.entities.LectureVenueEntity;
import com.rsdynamix.academics.setup.entities.StudentCourseEntity;
import com.rsdynamix.academics.setup.pojos.CourseTimetable;
import com.rsdynamix.academics.setup.pojos.DailyTimetable;
import com.rsdynamix.academics.setup.pojos.StudentCourseTimetable;
import com.rsdynamix.academics.setup.pojos.StudentDailyTimetable;
import com.rsdynamix.projects.binding.FinanceServiceLocator;
import com.codrellica.projects.commons.DateUtil;
import com.codrellica.projects.util.exception.UnprogressiveDatesException;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import com.rsdynamics.projects.field.operators.QueryGateOperators;
import com.rsdynamics.projects.query.operators.QueryOperators;
import com.rsdynamix.abstractobjects.QueryFieldGate;
import com.rsdynamix.dynamo.messages.ApplicationMessageBean;
import com.rsdynamix.dynamo.messages.MessageType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import com.rsdynamics.projects.eao.FinultimatePersistenceRemote;

/**
 *
 * @author p-aniah
 */
@SessionScoped
@ManagedBean(name = "studentTimetableBean")
public class StudentTimetableBean {

    private static final String COURSE_ID_FIELD_NAME = "courseID";
    //
    private AcademicTimetableEntity academicTimetable;
    private List<AcademicTimetableEntity> academicTimetableList;
    private List<SelectItem> hourItemList;
    private List<SelectItem> minuteItemList;
    private List<SelectItem> meridiemItemList;
    private List<SelectItem> lectureVenueItemList;
    //
    private List<StudentCourseTimetable> courseTimetableList;
    private List<StudentDailyTimetable> dailyTimetableList;
    private List<DailyTimetable> todayTimetable;
    private String todayDOW;
    //
    private Map<String, String> weekDayMap;
    private String studentNumber;

    public StudentTimetableBean() {
        academicTimetable = new AcademicTimetableEntity();
        academicTimetableList = new ArrayList<AcademicTimetableEntity>();

        hourItemList = new ArrayList<SelectItem>();
        minuteItemList = new ArrayList<SelectItem>();
        meridiemItemList = new ArrayList<SelectItem>();
        lectureVenueItemList = new ArrayList<SelectItem>();

        courseTimetableList = new ArrayList<StudentCourseTimetable>();
        dailyTimetableList = new ArrayList<StudentDailyTimetable>();
        todayTimetable = new ArrayList<DailyTimetable>();

        studentNumber = "";

        loadLectureVenueList();
    }

    public void loadLectureVenueList() {
        ApplicationMessageBean applicationMessageBean = (ApplicationMessageBean) CommonBean.getBeanFromContext(
                "#{sessionScope.applicationMessageBean}", ApplicationMessageBean.class);
        if (applicationMessageBean == null) {
            applicationMessageBean = new ApplicationMessageBean();
            CommonBean.setBeanToContext("#{sessionScope.applicationMessageBean}",
                    ApplicationMessageBean.class, applicationMessageBean);
        }
        applicationMessageBean.insertMessage("", MessageType.NONE);

        weekDayMap = new HashMap<String, String>();

        weekDayMap.put("SUN", "SUNDAY");
        weekDayMap.put("MON", "MONDAY");
        weekDayMap.put("TUE", "TUESDAY");
        weekDayMap.put("WED", "WEDNESDAY");
        weekDayMap.put("THU", "THURSDAY");
        weekDayMap.put("FRI", "FRIDAY");
        weekDayMap.put("SAT", "SATURDAY");

        LectureVenueEntity criteria = new LectureVenueEntity();
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        try {
            List<AbstractEntity> entityList = dataServer.findData(criteria);
            for (AbstractEntity entity : entityList) {
                LectureVenueEntity lvEntity = (LectureVenueEntity) entity;

                SelectItem item = new SelectItem();
                item.setValue(lvEntity.getLectureVenuID());
                item.setLabel(lvEntity.getLectureVenueDesc());

                lectureVenueItemList.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            applicationMessageBean.insertMessage(ex.getMessage(), MessageType.ERROR_MESSAGE);
        }
    }

    public SelectItem findLectureVenueItemByID(int venueID) {
        SelectItem item = null;

        for (SelectItem sItem : lectureVenueItemList) {
            if (Integer.parseInt(sItem.getValue().toString()) == venueID) {
                item = sItem;
                break;
            }
        }

        return item;
    }

    public String loadStudentCoursesByStudNumber() {
        StudentCourseBean studentCourseBean = (StudentCourseBean) CommonBean.getBeanFromContext(
                "#{sessionScope.studentCourseBean}", StudentCourseBean.class);
        if (studentCourseBean == null) {
            studentCourseBean = new StudentCourseBean();
            CommonBean.setBeanToContext("#{sessionScope.studentCourseBean}", StudentCourseBean.class, studentCourseBean);
        }

        try {
            studentCourseBean.loadStudentCourseList(getStudentNumber());
            loadStudentTimetable(studentCourseBean);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public void loadStudentTimetable(StudentCourseBean studentCourseBean) throws Exception {
        FinultimatePersistenceRemote dataServer = FinanceServiceLocator.locateFinancePersistenceManager();

        AcademicTimetableEntity criteria = new AcademicTimetableEntity();
        for (StudentCourseEntity course : studentCourseBean.getStudentCourseList()) {
            QueryFieldGate queryGate = new QueryFieldGate();
            queryGate.setFieldName(COURSE_ID_FIELD_NAME);
            queryGate.setCriteriaOperator(QueryOperators.EQUALS);
            queryGate.setFieldValue(course.getCourseID());
            queryGate.setGateName(QueryGateOperators.LOGICAL_OR_GATE);
            queryGate.setPosition(0);

            criteria.getQueryGateOperators().getFieldGateList().add(queryGate);
        }

        List<AbstractEntity> entityList = dataServer.findData(criteria);
        for (AbstractEntity entity : entityList) {
            AcademicTimetableEntity timeTableEntry = (AcademicTimetableEntity) entity;

            if (timeTableEntry.getCourseID() > 0) {
                SelectItem venueItem = findLectureVenueItemByID((int) timeTableEntry.getLectureVenueID());
                if (venueItem != null) {
                    timeTableEntry.setLectureVenueDesc(venueItem.getLabel());
                }

                StudentCourseEntity course = studentCourseBean.findCourseByID(timeTableEntry.getCourseID());
                if (course != null) {
                    timeTableEntry.setCourseCode(course.getCourseCode());
                    timeTableEntry.setCourseTitle(course.getCourseTitle());
                }

                try {
                    timeTableEntry.setCreditLoad(DateUtil.computeTimeDifference(
                            timeTableEntry.getStartTime(), timeTableEntry.getEndTime()));
                } catch (UnprogressiveDatesException ex) {
                    ex.printStackTrace();
                }

                if (!timeTableEntry.getCourseCode().trim().equals("")) {
                    academicTimetableList.add(timeTableEntry);
                }
            }
        }

        constructDailyTimetable();
        constructCourseTimetable();
    }

    public void constructDailyTimetable() {
        todayDOW = DateUtil.getWeekDay(true);
        for (Map.Entry<String, String> mapEntry : weekDayMap.entrySet()) {
            StudentDailyTimetable dailyTimetable = new StudentDailyTimetable();

            List<AcademicTimetableEntity> timeTable = findTimetableByDOW(mapEntry.getKey());
            if ((timeTable != null) && (timeTable.size() > 0)) {
                for (AcademicTimetableEntity timeTableEntry : timeTable) {
                    DailyTimetable dailyEntry = new DailyTimetable();

                    dailyEntry.setCourseCode(timeTableEntry.getCourseCode());
                    dailyEntry.setCourseTitle(timeTableEntry.getCourseTitle());
                    dailyEntry.setCreditLoad(timeTableEntry.getCreditLoad());
                    dailyEntry.setLectureVenue(timeTableEntry.getLectureVenueDesc());
                    dailyEntry.setStartTime(timeTableEntry.getStartTime());
                    dailyEntry.setEndTime(timeTableEntry.getEndTime());

                    dailyTimetable.addTimetableEntry(timeTableEntry.getDayOfWeek(), dailyEntry);
                    if (mapEntry.getKey().equals(todayDOW)) {
                        todayTimetable.add(dailyEntry);
                    }
                }

                dailyTimetableList.add(dailyTimetable);
            }
        }
    }

    public void constructCourseTimetable() {
        StudentCourseBean studentCourseBean = (StudentCourseBean) CommonBean.getBeanFromContext(
                "#{sessionScope.studentCourseBean}", StudentCourseBean.class);
        if (studentCourseBean == null) {
            studentCourseBean = new StudentCourseBean();
            CommonBean.setBeanToContext("#{sessionScope.studentCourseBean}", StudentCourseBean.class, studentCourseBean);
        }

        for (StudentCourseEntity course : studentCourseBean.getStudentCourseList()) {
            StudentCourseTimetable courseTimetable = new StudentCourseTimetable();

            List<AcademicTimetableEntity> timeTable = findTimetableByCourseID(course.getCourseID());
            if (timeTable.size() > 0) {
                for (AcademicTimetableEntity timeTableEntry : timeTable) {
                    CourseTimetable courseEntry = new CourseTimetable();

                    courseEntry.setCreditLoad(timeTableEntry.getCreditLoad());
                    courseEntry.setDayOfWeek(timeTableEntry.getDayOfWeek());
                    courseEntry.setStartTime(timeTableEntry.getStartTime());
                    courseEntry.setEndTime(timeTableEntry.getEndTime());
                    courseEntry.setLectureVenue(timeTableEntry.getLectureVenueDesc());

                    courseTimetable.addTimetableEntry(timeTableEntry.getCourseCode(), timeTableEntry.getCourseTitle(), courseEntry);
                }

                courseTimetableList.add(courseTimetable);
            }
        }

    }

    public List<AcademicTimetableEntity> findTimetableByDOW(String weekDay) {
        List<AcademicTimetableEntity> timeTable = new ArrayList<AcademicTimetableEntity>();

        for (AcademicTimetableEntity timeTableEntry : academicTimetableList) {
            if (timeTableEntry.getDayOfWeek().equals(weekDay)) {
                timeTable.add(timeTableEntry);
            }
        }

        return timeTable;
    }

    public List<AcademicTimetableEntity> findTimetableByCourseID(long courseID) {
        List<AcademicTimetableEntity> timeTable = new ArrayList<AcademicTimetableEntity>();

        for (AcademicTimetableEntity timeTableEntry : academicTimetableList) {
            if (timeTableEntry.getCourseID() == courseID) {
                timeTable.add(timeTableEntry);
            }
        }

        return timeTable;
    }

    /**
     * @return the academicTimetable
     */
    public AcademicTimetableEntity getAcademicTimetable() {
        return academicTimetable;
    }

    /**
     * @param academicTimetable the academicTimetable to set
     */
    public void setAcademicTimetable(AcademicTimetableEntity academicTimetable) {
        this.academicTimetable = academicTimetable;
    }

    /**
     * @return the academicTimetableList
     */
    public List<AcademicTimetableEntity> getAcademicTimetableList() {
        return academicTimetableList;
    }

    /**
     * @param academicTimetableList the academicTimetableList to set
     */
    public void setAcademicTimetableList(List<AcademicTimetableEntity> academicTimetableList) {
        this.academicTimetableList = academicTimetableList;
    }

    /**
     * @return the hourItemList
     */
    public List<SelectItem> getHourItemList() {
        return hourItemList;
    }

    /**
     * @param hourItemList the hourItemList to set
     */
    public void setHourItemList(List<SelectItem> hourItemList) {
        this.hourItemList = hourItemList;
    }

    /**
     * @return the minuteItemList
     */
    public List<SelectItem> getMinuteItemList() {
        return minuteItemList;
    }

    /**
     * @param minuteItemList the minuteItemList to set
     */
    public void setMinuteItemList(List<SelectItem> minuteItemList) {
        this.minuteItemList = minuteItemList;
    }

    /**
     * @return the meridiemItemList
     */
    public List<SelectItem> getMeridiemItemList() {
        return meridiemItemList;
    }

    /**
     * @param meridiemItemList the meridiemItemList to set
     */
    public void setMeridiemItemList(List<SelectItem> meridiemItemList) {
        this.meridiemItemList = meridiemItemList;
    }

    /**
     * @return the lectureVenueItemList
     */
    public List<SelectItem> getLectureVenueItemList() {
        return lectureVenueItemList;
    }

    /**
     * @param lectureVenueItemList the lectureVenueItemList to set
     */
    public void setLectureVenueItemList(List<SelectItem> lectureVenueItemList) {
        this.lectureVenueItemList = lectureVenueItemList;
    }

    /**
     * @return the courseTimetableList
     */
    public List<StudentCourseTimetable> getCourseTimetableList() {
        return courseTimetableList;
    }

    /**
     * @param courseTimetableList the courseTimetableList to set
     */
    public void setCourseTimetableList(List<StudentCourseTimetable> courseTimetableList) {
        this.courseTimetableList = courseTimetableList;
    }

    /**
     * @return the dailyTimetableList
     */
    public List<StudentDailyTimetable> getDailyTimetableList() {
        return dailyTimetableList;
    }

    /**
     * @param dailyTimetableList the dailyTimetableList to set
     */
    public void setDailyTimetableList(List<StudentDailyTimetable> dailyTimetableList) {
        this.dailyTimetableList = dailyTimetableList;
    }

    /**
     * @return the todayTimetable
     */
    public List<DailyTimetable> getTodayTimetable() {
        return todayTimetable;
    }

    /**
     * @param todayTimetable the todayTimetable to set
     */
    public void setTodayTimetable(List<DailyTimetable> todayTimetable) {
        this.todayTimetable = todayTimetable;
    }

    /**
     * @return the studentNumber
     */
    public String getStudentNumber() {
        return studentNumber;
    }

    /**
     * @param studentNumber the studentNumber to set
     */
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
}
