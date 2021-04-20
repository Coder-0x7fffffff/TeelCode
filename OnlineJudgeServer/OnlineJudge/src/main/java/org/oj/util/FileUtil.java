package org.oj.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.ByteArrayOutputStream;

public class FileUtil {

	private FileUtil() {}

	public static String readFromFile(File file) throws IOException {
		/* try-with-resources */
		try (FileInputStream fileInputStream = new FileInputStream(file);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
			byte[] buffer = new byte[1024];
			int len = 0;
			while (-1 != (len = fileInputStream.read(buffer))) {
				byteArrayOutputStream.write(buffer, 0, len);
			}
			byte[] data = byteArrayOutputStream.toByteArray();
			return new String(data);
		}
	}

	public static String readFromFile(String filePath) throws IOException {
		return readFromFile(new File(filePath));
	}
	
	public static void writeToFile(File file, String content) throws IOException {
		/* try-with-resources */
		try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
			out.write(content);
		}
	}
	
	public static void writeToFile(String filePath, String content) throws IOException {
		writeToFile(new File(filePath), content);
	}

}
