/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsdynamix.academics.resultprocess.pojos;

/**
 *
 * @author p-aniah
 */
import com.rsdynamix.academics.setup.entities.CourseGradeEntity;
import java.util.ArrayList;
import java.util.List;

public class PositionGroup {

    private List<CourseGradeEntity> scoreList;
    private int nextPosition;

    public PositionGroup() {
        scoreList = new ArrayList<CourseGradeEntity>();
        nextPosition = 0;
    }

    public void setScoreList(List<CourseGradeEntity> scoreList) {
        this.scoreList = scoreList;
    }

    public List<CourseGradeEntity> getScoreList() {
        return scoreList;
    }

    public void setNextPosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }

    public int getNextPosition() {
        return nextPosition;
    }
}
