package com.rsdynamix.academics.results.beans;

import com.rsdynamix.academics.setup.dataobjects.GradeCriterion;
import com.rsdynamix.academics.setup.dataobjects.SubjectScore;
import java.util.ArrayList;
import java.util.List;

public abstract class ResultHandler {
	
	public ResultHandler(){
		
	}
	
	protected List<SubjectScore> processGrade(List<SubjectScore> scoreList, List<GradeCriterion> criteria){
		List<SubjectScore> subjScoreList = new ArrayList<SubjectScore>();
		
		for(SubjectScore score : scoreList){
			GradeCriterion grade = getGradeOfScore(score, criteria);
			score.setStudentGrade(String.valueOf(grade.getGradeChar()));
			
			subjScoreList.add(score);
		}
		
		return subjScoreList;
	}
	
	private GradeCriterion getGradeOfScore(SubjectScore score, List<GradeCriterion> criteria){
		GradeCriterion grade = null;
		
		for(GradeCriterion criterion : criteria){
			if((score.getSubjectScore() >= criterion.getScoreRange().getLowerLimit()) && 
					(score.getSubjectScore() <= criterion.getScoreRange().getUpperLimit())){
				grade = criterion;
				break;
			}
		}
		
		return grade;
	}
	
	protected abstract List<SubjectScore> determinePostion(List<SubjectScore> scoreList);
	
}
