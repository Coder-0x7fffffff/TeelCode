package org.oj.enums;

public enum SexTypeEnum {
	MALE(0), FEMALE(1);

	private final int value;
	private static final SexTypeEnum[] values = SexTypeEnum.values();
	
	private SexTypeEnum(int value) {
		this.value = value;
	}
	
	public static SexTypeEnum valueOf(int value) {
		if (value < values.length) {
			return values[value];		
		}
		return null;
	}
	
	public int getValue() {
		return this.value;
	}
	
}