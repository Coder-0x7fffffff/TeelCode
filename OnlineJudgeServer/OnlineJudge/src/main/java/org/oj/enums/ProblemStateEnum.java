package org.oj.enums;

public enum ProblemStateEnum {
	WAITING(0), JUDING(1),
	CE(2), AC(3), RE(4), PC(5), PE(6), WA(7), TLE(8), MLE(9);
	
	private final int value;
	private static final ProblemStateEnum[] values = ProblemStateEnum.values();
	
	private ProblemStateEnum(int value) {
		this.value = value;
	}
	
	public static ProblemStateEnum valueOf(int value) {
		if (value < values.length) {
			return values[value];		
		}
		return null;
	}
	
	public int getValue() {
		return this.value;
	}
	
}