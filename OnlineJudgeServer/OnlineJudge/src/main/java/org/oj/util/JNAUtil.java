package org.oj.util;

import com.sun.jna.Library;
import com.sun.jna.Native;

/* write path /home/<user name>/ */
public class JNAUtil {
	
	interface CLibrary extends Library {
		CLibrary instace = Native.load("ojcore", CLibrary.class);
		
		void judge(String code, String dirPath, String samplePath, int languageType);
	}
	
	private JNAUtil() {}
	
	public static void judge(String code, String dirPath, String samplePath, int languageType) {
		CLibrary.instace.judge(code, dirPath, samplePath, languageType);
	}
	
	
}
