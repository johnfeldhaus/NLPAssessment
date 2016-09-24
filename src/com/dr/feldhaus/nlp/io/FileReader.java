package com.dr.feldhaus.nlp.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileReader {

	public static String read(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
	}
	
	public static String read(ZipFile zipFile, ZipEntry entry) throws IOException {
		try(Scanner scanner = new Scanner(zipFile.getInputStream(entry))) {
			return scanner.useDelimiter("\\A").next();
		}
	}
}
