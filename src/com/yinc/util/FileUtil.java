package com.yinc.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
	public static void writeTxtFile(String Path, String content) {
		try {
			File f = new File(Path);
			if (!f.exists()) {
			    f.createNewFile();
			}
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(f.getAbsoluteFile()));
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
