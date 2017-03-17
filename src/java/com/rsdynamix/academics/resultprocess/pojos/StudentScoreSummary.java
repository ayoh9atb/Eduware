/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.resultprocess.pojos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author p-aniah
 */
public class StudentScoreSummary implements Serializable {

    private int creditLoadTotal;
    private double gradePointTotal;
    private double scorePointTotal;
    private double gradePointAverage;
    private List<StudentSessionScoreBQ> studentSessionScoreList;

    public StudentScoreSummary() {
        creditLoadTotal = 0;
        gradePointTotal = 0;
        scorePointTotal = 0;
        gradePointAverage = 0;
        studentSessionScoreList = new ArrayList<StudentSessionScoreBQ>();
    }

    public void buildScoreSummary(List<StudentScoreReport> studentScoreReportList) {
        studentSessionScoreList = buildSessionCourse(studentScoreReportList);
        computeCGPA();
    }

    public void computeCGPA(){
        if((gradePointTotal > 0) && (creditLoadTotal > 0)) {
            gradePointAverage = gradePointTotal/creditLoadTotal;
        }
    }

    public StudentSessionScoreBQ findStudentSessionScoreBySessionPeriod(String sessionPeriod) {
        StudentSessionScoreBQ sessionScore = null;

        for(StudentSessionScoreBQ score : studentSessionScoreList) {
            if(score.getSessionPeriod().equals(sessionPeriod)) {
                sessionScore = score;
                break;
            }
        }

        return sessionScore;
    }

    public List<StudentSessionScoreBQ> buildSessionCourse(List<StudentScoreReport> studentScoreReportList) {
        List<StudentSessionScoreBQ> sessionCourseBQList = new ArrayList<StudentSessionScoreBQ>();

        List<SessionGroup> courseBySessionList = buildCourseBySessionList(studentScoreReportList);
        for(SessionGroup courseBySession : courseBySessionList) {
            List<StudentScoreReport> filteredScoreList = findSessionScoreList(
                    studentScoreReportList, courseBySession.getSessionPeriod());

            StudentSessionScoreBQ sessionCourseBQ = new StudentSessionScoreBQ();
            StudentSemesterScoreBQ semesterCourseBQ1 = new StudentSemesterScoreBQ();
            StudentSemesterScoreBQ semesterCourseBQ2 = new StudentSemesterScoreBQ();

            for(StudentScoreReport filteredScore : filteredScoreList) {
                if(filteredScore.getSemesterTier() == TermTierType.FIRST) {
                    semesterCourseBQ1.setCreditLoadTotal(semesterCourseBQ1.getCreditLoadTotal() +
                            (int)filteredScore.getCreditLoad());

                    semesterCourseBQ1.setScorePointTotal(semesterCourseBQ1.getScorePointTotal() +
                            filteredScore.getScorePoint());

                    semesterCourseBQ1.setGradePointTotal(semesterCourseBQ1.getGradePointTotal() +
                            filteredScore.getScorePoint() * filteredScore.getCreditLoad());

                    semesterCourseBQ1.setSemesterTier(filteredScore.getSemesterTier().toString());
                    if(!semesterCourseBQ1.getStudentScoreReportList().contains(filteredScore)){
                        semesterCourseBQ1.getStudentScoreReportList().add(filteredScore);
                    }
                } else if(filteredScore.getSemesterTier() == TermTierType.SECOND) {
                    semesterCourseBQ2.setCreditLoadTotal(semesterCourseBQ2.getCreditLoadTotal() +
                            (int)filteredScore.getCreditLoad());

                    semesterCourseBQ2.setScorePointTotal(semesterCourseBQ2.getScorePointTotal() +
                            filteredScore.getScorePoint());

                    semesterCourseBQ2.setGradePointTotal(semesterCourseBQ2.getGradePointTotal() +
                            filteredScore.getScorePoint() * filteredScore.getCreditLoad());

                    semesterCourseBQ2.setSemesterTier(filteredScore.getSemesterTier().toString());
                    if(!semesterCourseBQ2.getStudentScoreReportList().contains(filteredScore)){
                        semesterCourseBQ2.getStudentScoreReportList().add(filteredScore);
                    }
                }
            }
            
            semesterCourseBQ1.computeCGPA();
            semesterCourseBQ2.computeCGPA();

            sessionCourseBQ.setCreditLoadTotal(sessionCourseBQ.getCreditLoadTotal() + semesterCourseBQ1.getCreditLoadTotal());
            sessionCourseBQ.setCreditLoadTotal(sessionCourseBQ.getCreditLoadTotal() + semesterCourseBQ2.getCreditLoadTotal());
            creditLoadTotal += sessionCourseBQ.getCreditLoadTotal();

            sessionCourseBQ.setScorePointTotal(sessionCourseBQ.getScorePointTotal() + semesterCourseBQ1.getScorePointTotal());
            sessionCourseBQ.setScorePointTotal(sessionCourseBQ.getScorePointTotal() + semesterCourseBQ2.getScorePointTotal());
            scorePointTotal += sessionCourseBQ.getScorePointTotal();

            sessionCourseBQ.setGradePointTotal(sessionCourseBQ.getGradePointTotal() + semesterCourseBQ1.getGradePointTotal());
            sessionCourseBQ.setGradePointTotal(sessionCourseBQ.getGradePointTotal() + semesterCourseBQ2.getGradePointTotal());
            gradePointTotal += sessionCourseBQ.getGradePointTotal();

            sessionCourseBQ.setSessionPeriod(courseBySession.getSessionPeriod());

            sessionCourseBQ.getStudentSemesterScoreBQList().add(semesterCourseBQ1);
            sessionCourseBQ.getStudentSemesterScoreBQList().add(semesterCourseBQ2);

            sessionCourseBQ.computeCGPA();

            if(!sessionCourseBQList.contains(sessionCourseBQ)){
                sessionCourseBQList.add(sessionCourseBQ);
            }
        }

        return sessionCourseBQList;
    }

    public List<SessionGroup> buildCourseBySessionList(List<StudentScoreReport> studentScoreReportList) {
        List<SessionGroup> courseSessionList = new ArrayList<SessionGroup>();

        for (StudentScoreReport course : studentScoreReportList) {
            SessionGroup courseSession = new SessionGroup();
            courseSession.setSessionPeriod(((StudentScoreReport)course).getSessionPeriod());
            
            if (!courseSessionList.contains(courseSession)) {
                courseSessionList.add(courseSession);
            }
        }

        return courseSessionList;
    }

    public List<StudentScoreReport> findSessionScoreList(List<StudentScoreReport> reportList, String sessionPeriod){
        List<StudentScoreReport> filteredScoreList = new ArrayList<StudentScoreReport>();

        for(StudentScoreReport score : reportList) {
            if(((StudentScoreReport)score).getSessionPeriod().equals(sessionPeriod)) {
                filteredScoreList.add(score);
            }
        }

        return filteredScoreList;
    }

    /**
     * @return the creditLoadTotal
     */
    public int getCreditLoadTotal() {
        return creditLoadTotal;
    }

    /**
     * @param creditLoadTotal the creditLoadTotal to set
     */
    public void setCreditLoadTotal(int creditLoadTotal) {
        this.creditLoadTotal = creditLoadTotal;
    }

    /**
     * @return the gradePointTotal
     */
    public double getGradePointTotal() {
        return gradePointTotal;
    }

    /**
     * @param gradePointTotal the gradePointTotal to set
     */
    public void setGradePointTotal(double gradePointTotal) {
        this.gradePointTotal = gradePointTotal;
    }

    /**
     * @return the scorePointTotal
     */
    public double getScorePointTotal() {
        return scorePointTotal;
    }

    /**
     * @param scorePointTotal the scorePointTotal to set
     */
    public void setScorePointTotal(double scorePointTotal) {
        this.scorePointTotal = scorePointTotal;
    }

    /**
     * @return the gradePointAverage
     */
    public double getGradePointAverage() {
        return gradePointAverage;
    }

    /**
     * @param gradePointAverage the gradePointAverage to set
     */
    public void setGradePointAverage(double gradePointAverage) {
        this.gradePointAverage = gradePointAverage;
    }

    /**
     * @return the studentSessionScoreList
     */
    public List<StudentSessionScoreBQ> getStudentSessionScoreList() {
        return studentSessionScoreList;
    }

    /**
     * @param studentSessionScoreList the studentSessionScoreList to set
     */
    public void setStudentSessionScoreList(List<StudentSessionScoreBQ> studentSessionScoreList) {
        this.studentSessionScoreList = studentSessionScoreList;
    }

}
