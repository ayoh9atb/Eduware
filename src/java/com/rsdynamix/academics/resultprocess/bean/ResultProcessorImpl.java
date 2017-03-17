/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.resultprocess.bean;

import com.rsdynamix.academics.resultprocess.pojos.CourseBySession;
import com.rsdynamix.academics.resultprocess.pojos.CourseByStudent;
import com.rsdynamix.academics.resultprocess.pojos.StudentCGPA;

import com.rsdynamix.academics.setup.entities.AcademicGradePointEntity;
import com.rsdynamix.academics.setup.entities.AcademicScoreEntity;
import com.rsdynamix.academics.setup.entities.CourseGradeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author p-aniah
 */
public class ResultProcessorImpl implements ResultProcessor {

    public ResultProcessorImpl() {
    }

    public List<StudentCGPA> buildCGPA(List<CourseGradeEntity> courseGradeList,
            List<AcademicGradePointEntity> gradePointList) {
        List<StudentCGPA> computedCGPAList = new ArrayList<StudentCGPA>();
        ResultHandler handler = new UniversityResultHandler();

        List<CourseByStudent> courseStudentList = buildCourseGradeByStudentList(courseGradeList);
        for (CourseByStudent courseStudent : courseStudentList) {
            List<CourseGradeEntity> filteredGradeList = filterGradeByStudentNumber(courseGradeList, courseStudent);
            StudentCGPA computedCGPA = handler.computeCGPA(filteredGradeList, gradePointList);
            if (!computedCGPAList.contains(computedCGPA)) {
                computedCGPAList.add(computedCGPA);
            }
        }

        return computedCGPAList;
    }

    public List<CourseGradeEntity> processResult(List<AcademicScoreEntity> courseScoreList,
            List<AcademicGradePointEntity> gradePointList) {
        List<CourseGradeEntity> gradeList = new ArrayList<CourseGradeEntity>();
        ResultHandler handler = new UniversityResultHandler();

        List<CourseBySession> courseSessionList = buildCourseBySessionList(courseScoreList);
        for (CourseBySession courseSession : courseSessionList) {
            List<CourseGradeEntity> filteredGradeList = new ArrayList<CourseGradeEntity>();

            List<AcademicScoreEntity> sessionFilteredScoreList = filterScoresByCourseSession(courseScoreList, courseSession);
            List<CourseByStudent> courseStudentList = buildCourseByStudentList(sessionFilteredScoreList);
            for (CourseByStudent courseByStudent : courseStudentList) {
                List<AcademicScoreEntity> studentFilteredScoreList = filterScoresByStudentNumber(sessionFilteredScoreList, courseByStudent);

                for (AcademicScoreEntity score : studentFilteredScoreList) {
                    CourseGradeEntity filteredGrade = buildCourseGrade(score);
                    
                    if (!filteredGradeList.contains(filteredGrade)) {
                        filteredGrade.setSubjectScore(computeStudentCourseGrade(studentFilteredScoreList));
                        filteredGradeList.add(filteredGrade);
                    }
                }
            }

            filteredGradeList = handler.processGrade(filteredGradeList, gradePointList);
            filteredGradeList = handler.determinePostion(filteredGradeList);

            for(CourseGradeEntity grade : filteredGradeList) {
                if(!gradeList.contains(grade)) {
                    gradeList.add(grade);
                }
            }
        }

        return gradeList;
    }

    private double computeStudentCourseGrade(List<AcademicScoreEntity> studentFilteredScoreList) {
        double totalScore = 0;

        for (AcademicScoreEntity score : studentFilteredScoreList) {
            totalScore += score.getSubjectScore();
        }

        return totalScore;
    }

    public List<CourseBySession> buildCourseBySessionList(List<AcademicScoreEntity> courseScoreList) {
        List<CourseBySession> courseSessionList = new ArrayList<CourseBySession>();

        for (AcademicScoreEntity score : courseScoreList) {
            CourseBySession courseSession = new CourseBySession();
            courseSession.setCourseID(score.getCourseID());
            courseSession.setSessionID(score.getSessionID());
            if (!courseSessionList.contains(courseSession)) {
                courseSessionList.add(courseSession);
            }
        }

        return courseSessionList;
    }

    public List<CourseByStudent> buildCourseByStudentList(List<AcademicScoreEntity> courseScoreList) {
        List<CourseByStudent> courseStudentList = new ArrayList<CourseByStudent>();

        for (AcademicScoreEntity score : courseScoreList) {
            CourseByStudent courseStudent = new CourseByStudent();
            courseStudent.setStudentNumber(score.getMatricNumber());
            if (!courseStudentList.contains(courseStudent)) {
                courseStudentList.add(courseStudent);
            }
        }

        return courseStudentList;
    }

    private List<CourseByStudent> buildCourseGradeByStudentList(List<CourseGradeEntity> courseGradeList) {
        List<CourseByStudent> courseStudentList = new ArrayList<CourseByStudent>();

        for (CourseGradeEntity grade : courseGradeList) {
            CourseByStudent courseStudent = new CourseByStudent();
            courseStudent.setStudentNumber(grade.getMatricNumber());
            if (!courseStudentList.contains(courseStudent)) {
                courseStudentList.add(courseStudent);
            }
        }

        return courseStudentList;
    }

    private List<AcademicScoreEntity> filterScoresByCourseSession(
            List<AcademicScoreEntity> courseScoreList,
            CourseBySession courseSession) {
        List<AcademicScoreEntity> filteredScoreList = new ArrayList<AcademicScoreEntity>();

        for (AcademicScoreEntity score : courseScoreList) {
            if ((score.getSessionID() == courseSession.getSessionID()) && (score.getCourseID() == courseSession.getCourseID())) {
                filteredScoreList.add(score);
            }
        }

        return filteredScoreList;
    }

    public List<AcademicScoreEntity> filterScoresByStudentNumber(
            List<AcademicScoreEntity> courseScoreList,
            CourseByStudent courseStudent) {
        List<AcademicScoreEntity> filteredScoreList = new ArrayList<AcademicScoreEntity>();

        for (AcademicScoreEntity score : courseScoreList) {
            if (score.getMatricNumber().equals(courseStudent.getStudentNumber())) {
                filteredScoreList.add(score);
            }
        }

        return filteredScoreList;
    }

    private List<CourseGradeEntity> filterGradeByStudentNumber(
            List<CourseGradeEntity> courseGradeList,
            CourseByStudent courseStudent) {
        List<CourseGradeEntity> filteredGradeList = new ArrayList<CourseGradeEntity>();

        for (CourseGradeEntity grade : courseGradeList) {
            if (grade.getMatricNumber().equals(courseStudent.getStudentNumber())) {
                filteredGradeList.add(grade);
            }
        }

        return filteredGradeList;
    }

    private CourseGradeEntity buildCourseGrade(AcademicScoreEntity score) {
        CourseGradeEntity subjGradeScore = new CourseGradeEntity();

        subjGradeScore.setCourseCode(score.getCourseCode());
        subjGradeScore.setCourseID(score.getCourseID());
        subjGradeScore.setCourseTitle(score.getCourseTitle());
        subjGradeScore.setMatricNumber(score.getMatricNumber());
        subjGradeScore.setSessionID(score.getSessionID());
        subjGradeScore.setSessionPeriod(score.getSessionPeriod());

        subjGradeScore.setFacultyID(score.getFacultyID());
        subjGradeScore.setFacultyName(score.getFacultyName());
        subjGradeScore.setDepartmentID(score.getDepartmentID());
        subjGradeScore.setDepartmentName(score.getDepartmentName());
        subjGradeScore.setAcademicLevel(score.getAcademicLevel());
        subjGradeScore.setAcademicLevelDesc(score.getAcademicLevelDesc());

        return subjGradeScore;
    }
}
