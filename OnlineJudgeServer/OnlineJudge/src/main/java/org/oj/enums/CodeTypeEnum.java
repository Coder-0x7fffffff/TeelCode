package org.oj.enums;

public enum CodeTypeEnum {
	C(0), CPP(1), JAVA(2), PYTHON2(3), PYTHON3(4);
	
	private final int value;
	private static final CodeTypeEnum[] values = CodeTypeEnum.values();
	
	private CodeTypeEnum(int value) {
		this.value = value;
	}
	
	public static CodeTypeEnum valueOf(int value) {
		if (value < values.length) {
			return values[value];		
		}
		return null;
	}

	public int getValue() {
		return this.value;
	}
	
}