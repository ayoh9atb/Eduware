package com.rsdynamix.academics.setup.dataobjects;

public class GradeCriterion {
	
	private char gradeChar;
	private ScoreRange scoreRange;
	
	public GradeCriterion(ScoreRange range, char grade){
		setGradeChar(grade);
		setScoreRange(range);
	}

	public void setGradeChar(char gradeChar) {
		this.gradeChar = gradeChar;
	}

	public char getGradeChar() {
		return gradeChar;
	}

	public void setScoreRange(ScoreRange scoreRange) {
		this.scoreRange = scoreRange;
	}

	public ScoreRange getScoreRange() {
		return scoreRange;
	}

}
