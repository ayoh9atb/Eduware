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
import java.util.List;

/**
 *
 * @author p-aniah
 */
public interface ResultProcessor {

    public List<CourseGradeEntity> processResult(List<AcademicScoreEntity> courseScoreList,
            List<AcademicGradePointEntity> gradePointList);

    public List<StudentCGPA> buildCGPA(List<CourseGradeEntity> courseGradeList,
            List<AcademicGradePointEntity> gradePointList);

    public List<CourseBySession> buildCourseBySessionList(List<AcademicScoreEntity> courseScoreList);

    public List<CourseByStudent> buildCourseByStudentList(List<AcademicScoreEntity> courseScoreList);

    public List<AcademicScoreEntity> filterScoresByStudentNumber(List<AcademicScoreEntity> courseScoreList,
            CourseByStudent courseStudent);

}
