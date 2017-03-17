package com.rsdynamix.academics.results.beans;

import com.rsdynamix.academics.setup.dataobjects.GradeCriterion;
import com.rsdynamix.academics.setup.dataobjects.ScoreRange;
import com.rsdynamix.academics.setup.dataobjects.SubjectScore;
import java.util.ArrayList;
import java.util.List;

public class ResultProcessor {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ResultHandler handler = new UnaabResultHandler();
		
		List<GradeCriterion> criteria = new ArrayList<GradeCriterion>();
		criteria.add(new GradeCriterion(new ScoreRange(0, 39), 'F'));
		criteria.add(new GradeCriterion(new ScoreRange(40, 44), 'E'));
		criteria.add(new GradeCriterion(new ScoreRange(45, 49), 'D'));
		criteria.add(new GradeCriterion(new ScoreRange(50, 59), 'C'));
		criteria.add(new GradeCriterion(new ScoreRange(60, 69), 'B'));
		criteria.add(new GradeCriterion(new ScoreRange(70, 100), 'A'));
		
		List<SubjectScore> scoreList = new ArrayList<SubjectScore>(); 
		SubjectScore score = new SubjectScore("MATH 001", 67, "0001");
		scoreList.add(score);
		
		score = new SubjectScore("MATH 001", 95, "0002");
		scoreList.add(score);
		
		score = new SubjectScore("MATH 001", 95, "0003");
		scoreList.add(score);
		
		score = new SubjectScore("MATH 001", 59, "0004");
		scoreList.add(score);
		
		score = new SubjectScore("MATH 001", 22, "0005");
		scoreList.add(score);
		
		scoreList = handler.processGrade(scoreList, criteria);
		scoreList = handler.determinePostion(scoreList);
		
		for(SubjectScore sScore : scoreList){
			System.out.println(sScore);
		}
	}

}
