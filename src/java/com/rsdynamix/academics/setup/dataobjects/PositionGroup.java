package com.rsdynamix.academics.setup.dataobjects;

import java.util.ArrayList;
import java.util.List;

public class PositionGroup {
	
	private List<SubjectScore> scoreList;
	private int nextPosition;
	
	public PositionGroup(){
		scoreList = new ArrayList<SubjectScore>();
		nextPosition = 0;
	}

	public void setScoreList(List<SubjectScore> scoreList) {
		this.scoreList = scoreList;
	}

	public List<SubjectScore> getScoreList() {
		return scoreList;
	}

	public void setNextPosition(int nextPosition) {
		this.nextPosition = nextPosition;
	}

	public int getNextPosition() {
		return nextPosition;
	}

}
