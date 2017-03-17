/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.resultprocess.bean;

import com.rsdynamix.academics.resultprocess.pojos.PositionGroup;
import com.rsdynamix.academics.resultprocess.pojos.StudentCGPA;
import com.rsdynamix.academics.setup.bean.AcademicCourseBean;
import com.rsdynamix.academics.setup.bean.ClassOfDegreeBean;
import com.rsdynamix.academics.setup.entities.AcademicCourseEntity;
import com.rsdynamix.academics.setup.entities.AcademicGradePointEntity;
import com.rsdynamix.academics.setup.entities.ClassOfDegreeEntity;
import com.rsdynamix.academics.setup.entities.CourseGradeEntity;
import com.rsdynamix.projects.web.commons.bean.CommonBean;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@SessionScoped
@ManagedBean(name = "universityResultHandler")
public class UniversityResultHandler extends ResultHandler {

    public static final String ST_TIER = "ST";
    public static final String ND_TIER = "ND";
    public static final String RD_TIER = "RD";
    public static final String TH_TIER = "TH";

    public UniversityResultHandler() {
    }

    @Override
    protected StudentCGPA computeCGPA(List<CourseGradeEntity> gradeList, List<AcademicGradePointEntity> gradePointList) {
        StudentCGPA gradePointAverage = new StudentCGPA();
        gradePointAverage.setStudentNumber(gradeList.get(0).getMatricNumber());

        double courseCount = 0;
        double gradePointTotal = 0;

        AcademicCourseBean academicCourseBean = (AcademicCourseBean) CommonBean.getBeanFromContext(
                "#{applicationScope.academicCourseBean}", AcademicCourseBean.class);
        if (academicCourseBean == null) {
            academicCourseBean = new AcademicCourseBean();
            CommonBean.setBeanToContext("#{applicationScope.academicCourseBean}", AcademicCourseBean.class, academicCourseBean);
        }

        ClassOfDegreeBean classOfDegreeBean = (ClassOfDegreeBean) CommonBean.getBeanFromContext(
                "#{applicationScope.classOfDegreeBean}", ClassOfDegreeBean.class);
        if (classOfDegreeBean == null) {
            classOfDegreeBean = new ClassOfDegreeBean();
            CommonBean.setBeanToContext("#{applicationScope.classOfDegreeBean}", ClassOfDegreeBean.class, classOfDegreeBean);
        }

        for (CourseGradeEntity grade : gradeList) {
            AcademicGradePointEntity gradePoint = getGradeOfScore(grade, gradePointList);
            AcademicCourseEntity course = academicCourseBean.findAcademicCourseByID(grade.getCourseID());
            if (course != null) {
                gradePointTotal += (gradePoint.getGradePoint() * course.getCreditLoad());
                courseCount += course.getCreditLoad();
            }
        }

        double cgpaValue = 0;
        if(courseCount > 0) {
            cgpaValue = gradePointTotal / courseCount;
        }
        gradePointAverage.setGradePointAverage(BigDecimal.valueOf(cgpaValue));

        ClassOfDegreeEntity classOfDegree = classOfDegreeBean.findGradePointByScorePoint(gradePointAverage.getGradePointAverage());
        if(classOfDegree != null) {
            gradePointAverage.setClassOfDegreeCode(classOfDegree.getClassOfDegreeCode());
            gradePointAverage.setClassOfDegreeDesc(classOfDegree.getClassOfDegreeDesc());
        }

        return gradePointAverage;
    }

    @Override
    protected List<CourseGradeEntity> determinePostion(List<CourseGradeEntity> scoreList) {
        List<CourseGradeEntity> updatedScoreList = new ArrayList<CourseGradeEntity>();

        Collections.sort(scoreList, new Comparator<CourseGradeEntity>() {

            @Override
            public int compare(CourseGradeEntity o1, CourseGradeEntity o2) {
                return (int) (o2.getSubjectScore() - o1.getSubjectScore());
            }
        });

        int position = 1;
        int count = 1;

        boolean eof = false;
        while (!eof) {
            CourseGradeEntity score = null;

            if (count <= scoreList.size()) {
                score = scoreList.get(count - 1);
            } else {
                eof = true;
            }

            if (!eof) {
                if (score.getStudentPosition() == 0) {
                    PositionGroup equalScores = getAllEqualScores(score, scoreList, count);
                    for (CourseGradeEntity eqScore : equalScores.getScoreList()) {
                        eqScore.setStudentPosition(position);
                        eqScore = getPositionTier(eqScore);

                        updatedScoreList.add(eqScore);
                    }

                    position = equalScores.getNextPosition();
                    count = equalScores.getNextPosition();
                } else {
                    count++;
                }
            }
        }

        return updatedScoreList;
    }

    private CourseGradeEntity getPositionTier(CourseGradeEntity score) {
        if (((score.getStudentPosition() == 1) || ((score.getStudentPosition() % 10) == 1)) && (!String.valueOf(score.getStudentPosition()).endsWith("11"))) {
            score.setPositionTier(score.getStudentPosition() + ST_TIER);
        } else if (((score.getStudentPosition() == 2) || (score.getStudentPosition() % 10 == 2)) && (!String.valueOf(score.getStudentPosition()).endsWith("12"))) {
            score.setPositionTier(score.getStudentPosition() + ND_TIER);
        } else if (((score.getStudentPosition() == 3) || (score.getStudentPosition() % 10 == 3)) && (!String.valueOf(score.getStudentPosition()).endsWith("13"))) {
            score.setPositionTier(score.getStudentPosition() + RD_TIER);
        } else {
            score.setPositionTier(score.getStudentPosition() + TH_TIER);
        }

        return score;
    }

    private PositionGroup getAllEqualScores(CourseGradeEntity score,
            List<CourseGradeEntity> scoreList, int currentPos) {
        PositionGroup equalScores = new PositionGroup();
        equalScores.getScoreList().add(score);

        int nextPos = currentPos;
        for (CourseGradeEntity subjScore : scoreList) {
            if ((subjScore.getSubjectScore() == score.getSubjectScore()) &&
                    (!subjScore.getMatricNumber().equals(
                    score.getMatricNumber())) && (subjScore.getStudentPosition() == 0)) {
                equalScores.getScoreList().add(subjScore);
                nextPos++;
            }
        }

        equalScores.setNextPosition(++nextPos);

        return equalScores;
    }
}
