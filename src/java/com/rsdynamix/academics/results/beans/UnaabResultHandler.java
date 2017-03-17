package com.rsdynamix.academics.results.beans;

import com.rsdynamix.academics.setup.dataobjects.PositionGroup;
import com.rsdynamix.academics.setup.dataobjects.SubjectScore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UnaabResultHandler extends ResultHandler {

	public static final String ST_TIER = "st";

	public static final String ND_TIER = "nd";

	public static final String RD_TIER = "rd";

	public static final String TH_TIER = "th";

	public UnaabResultHandler() {

	}

	@Override
	protected List<SubjectScore> determinePostion(List<SubjectScore> scoreList) {
		List<SubjectScore> updatedScoreList = new ArrayList<SubjectScore>();

		Collections.sort(scoreList, new Comparator<SubjectScore>() {

			@Override
			public int compare(SubjectScore o1, SubjectScore o2) {
				return (int) (o2.getSubjectScore() - o1.getSubjectScore());
			}

		});

		int position = 1;
		int count = 1;
		
		boolean eof = false;
		while (!eof) {
			SubjectScore score = null;
			if (count <= scoreList.size()) {
				score = scoreList.get(count - 1);
			} else {
				eof = true;
			}

			if (!eof) {
				PositionGroup equalScores = getAllEqualScores(score, scoreList,	count);
				for (SubjectScore eqScore : equalScores.getScoreList()) {
					eqScore.setStudentPosition(position);
					eqScore = getPositionTier(eqScore);

					updatedScoreList.add(eqScore);
				}
				
				position = equalScores.getNextPosition();
				count = equalScores.getNextPosition();
			}
		}

		return updatedScoreList;
	}

	private SubjectScore getPositionTier(SubjectScore score) {
		if (((score.getStudentPosition() == 1) || ((score.getStudentPosition() % 10) == 1))
				&& (String.valueOf(score.getStudentPosition()).endsWith("11"))) {
			score.setPositionTier(score.getStudentPosition() + ST_TIER);
		} else if (((score.getStudentPosition() == 2) || (score
				.getStudentPosition() % 10 == 2))
				&& (String.valueOf(score.getStudentPosition()).endsWith("12"))) {
			score.setPositionTier(score.getStudentPosition() + ND_TIER);
		} else if (((score.getStudentPosition() == 3) || (score
				.getStudentPosition() % 10 == 3))
				&& (String.valueOf(score.getStudentPosition()).endsWith("13"))) {
			score.setPositionTier(score.getStudentPosition() + RD_TIER);
		} else {
			score.setPositionTier(score.getStudentPosition() + TH_TIER);
		}

		return score;
	}

	private PositionGroup getAllEqualScores(SubjectScore score,
			List<SubjectScore> scoreList, int currentPos) {
		PositionGroup equalScores = new PositionGroup();

		int nextPos = currentPos;
		for (SubjectScore subjScore : scoreList) {
			if ((subjScore.getSubjectScore() == score.getSubjectScore())
					&& (!subjScore.getMatricNumber().equals(
							score.getMatricNumber()))
					&& (subjScore.getStudentPosition() == 0)) {
				equalScores.getScoreList().add(subjScore);
				nextPos++;
			}
		}
		equalScores.setNextPosition(nextPos);

		return equalScores;
	}

}
