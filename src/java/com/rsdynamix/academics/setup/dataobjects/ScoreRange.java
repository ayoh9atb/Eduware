package com.rsdynamix.academics.setup.dataobjects;

public class ScoreRange {
	
	private double lowerLimit;
	private double upperLimit;
	
	public ScoreRange(double lLimit, double uLimit){
		setLowerLimit(lLimit);
		setUpperLimit(uLimit);
	}

	public void setLowerLimit(double lowerLimit) {
		this.lowerLimit = lowerLimit;
	}

	public double getLowerLimit() {
		return lowerLimit;
	}

	public void setUpperLimit(double upperLimit) {
		this.upperLimit = upperLimit;
	}

	public double getUpperLimit() {
		return upperLimit;
	}

}
