package org.oj.util;

import java.util.HashMap;
import java.util.Map;

import org.oj.enums.CodeTypeEnum;

public class CompileUtil {

	private static final Map<CodeTypeEnum, String> COMPILE_COMMAND_MAP = new HashMap<CodeTypeEnum, String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1814766280440241809L;

		{
			put(CodeTypeEnum.C, "gcc -O2 -w -fmax-errors=3 -std=c11 -lm");
			put(CodeTypeEnum.CPP, "g++ -O2 -w -fmax-errors=3 -std=c++11 -lm");
			put(CodeTypeEnum.JAVA, "javac -encoding UTF8");
			put(CodeTypeEnum.PYTHON2, "python -m py_compile");
			put(CodeTypeEnum.PYTHON3, "python3 -m py_compile");
		}
	};
	
	public static String getCompileCommand(CodeTypeEnum codeType) {
		if (COMPILE_COMMAND_MAP.containsKey(codeType)) {
			return COMPILE_COMMAND_MAP.get(codeType);
		}
		return null;
	}
	
}
