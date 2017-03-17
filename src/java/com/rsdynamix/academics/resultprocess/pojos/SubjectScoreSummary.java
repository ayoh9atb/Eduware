/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rsdynamix.academics.resultprocess.pojos;

import com.rsdynamix.academics.setup.entities.TermTier;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author p-aniah
 */
public class SubjectScoreSummary implements Serializable {

    private List<SessionSubjectScoreBQ> sessionSubjScoreList;

    public SubjectScoreSummary() {
        sessionSubjScoreList = new ArrayList<SessionSubjectScoreBQ>();
    }
    
    public void buildScoreSummary(List<SubjectScoreDTO> studentScoreReportList) {
        sessionSubjScoreList = buildSessionCourse(studentScoreReportList);
    }

    public List<SessionSubjectScoreBQ> buildSessionCourse(List<SubjectScoreDTO> studentScoreReportList) {
        List<SessionSubjectScoreBQ> sessionCourseBQList = new ArrayList<SessionSubjectScoreBQ>();
        
        List<SessionGroup> courseBySessionList = buildCourseBySessionList(studentScoreReportList);
        for(SessionGroup courseBySession : courseBySessionList) {
            List<SubjectScoreDTO> filteredScoreList = findSessionScoreList(
                    studentScoreReportList, courseBySession.getSessionPeriod());

            SessionSubjectScoreBQ sessionCourseBQ = new SessionSubjectScoreBQ();
            SemesterSubjectScoreBQ semesterCourseBQ1 = new SemesterSubjectScoreBQ();
            SemesterSubjectScoreBQ semesterCourseBQ2 = new SemesterSubjectScoreBQ();

            for(SubjectScoreDTO filteredScore : filteredScoreList) {
                if(filteredScore.getSemesterTier() == TermTier.FIRST) {
                    semesterCourseBQ1.setSemesterTier(filteredScore.getSemesterTier().toString());
                    if(!semesterCourseBQ1.getSubjectScoreList().contains(filteredScore)){
                        semesterCourseBQ1.getSubjectScoreList().add(filteredScore);
                    }
                } else if(filteredScore.getSemesterTier() == TermTier.SECOND) {
                    semesterCourseBQ2.setSemesterTier(filteredScore.getSemesterTier().toString());
                    if(!semesterCourseBQ2.getSubjectScoreList().contains(filteredScore)){
                        semesterCourseBQ2.getSubjectScoreList().add(filteredScore);
                    }
                }
            }
            sessionCourseBQ.setSessionPeriod(courseBySession.getSessionPeriod());

            sessionCourseBQ.getSemesterSubjScoreList().add(semesterCourseBQ1);
            sessionCourseBQ.getSemesterSubjScoreList().add(semesterCourseBQ2);

            if(!sessionCourseBQList.contains(sessionCourseBQ)){
                sessionCourseBQList.add(sessionCourseBQ);
            }
        }

        return sessionCourseBQList;
    }

    public List<SessionGroup> buildCourseBySessionList(List<SubjectScoreDTO> studentScoreReportList) {
        List<SessionGroup> courseSessionList = new ArrayList<SessionGroup>();

        for (SubjectScoreDTO course : studentScoreReportList) {
            SessionGroup courseSession = new SessionGroup();
            courseSession.setSessionPeriod(course.getSessionPeriod());

            if (!courseSessionList.contains(courseSession)) {
                courseSessionList.add(courseSession);
            }
        }

        return courseSessionList;
    }

    public List<SubjectScoreDTO> findSessionScoreList(List<SubjectScoreDTO> reportList, String sessionPeriod){
        List<SubjectScoreDTO> filteredScoreList = new ArrayList<SubjectScoreDTO>();

        for(SubjectScoreDTO score : reportList) {
            if(score.getSessionPeriod().equals(sessionPeriod)) {
                filteredScoreList.add(score);
            }
        }

        return filteredScoreList;
    }

    /**
     * @return the sessionSubjScoreList
     */
    public List<SessionSubjectScoreBQ> getSessionSubjScoreList() {
        return sessionSubjScoreList;
    }

    /**
     * @param sessionSubjScoreList the sessionSubjScoreList to set
     */
    public void setSessionSubjScoreList(List<SessionSubjectScoreBQ> sessionSubjScoreList) {
        this.sessionSubjScoreList = sessionSubjScoreList;
    }

}
