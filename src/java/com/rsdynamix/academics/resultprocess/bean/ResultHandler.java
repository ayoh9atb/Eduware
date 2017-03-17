/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.resultprocess.bean;

import com.rsdynamix.academics.resultprocess.pojos.StudentCGPA;
import com.rsdynamix.academics.setup.entities.AcademicGradePointEntity;
import com.rsdynamix.academics.setup.entities.CourseGradeEntity;
import java.util.ArrayList;
import java.util.List;

public abstract class ResultHandler {

    public ResultHandler() {
    }

    protected List<CourseGradeEntity> processGrade(List<CourseGradeEntity> scoreList,
            List<AcademicGradePointEntity> academicGradePointList) {
        List<CourseGradeEntity> subjScoreList = new ArrayList<CourseGradeEntity>();

        for (CourseGradeEntity score : scoreList) {
            AcademicGradePointEntity grade = getGradeOfScore(score, academicGradePointList);
            score.setStudentGrade(grade.getGradeCharacter());

            subjScoreList.add(score);
        }

        return subjScoreList;
    }

    protected AcademicGradePointEntity getGradeOfScore(CourseGradeEntity score,
            List<AcademicGradePointEntity> academicGradePointList) {
        AcademicGradePointEntity grade = null;

        for (AcademicGradePointEntity criterion : academicGradePointList) {
            if (((int)score.getSubjectScore() >= criterion.getLowerLimit().doubleValue()) &&
                    ((int)score.getSubjectScore() <= criterion.getUpperLimit().doubleValue())) {
                grade = criterion;
                break;
            }
        }

        return grade;
    }

    protected abstract List<CourseGradeEntity> determinePostion(List<CourseGradeEntity> scoreList);

    protected abstract StudentCGPA computeCGPA(
            List<CourseGradeEntity> scoreList,
            List<AcademicGradePointEntity> gradePointList);
}
