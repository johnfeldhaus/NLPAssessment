package com.dr.feldhaus.nlp.io;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {

	public static String read(String path) throws IOException {
		return new String(Files.readAllBytes(Paths.get(path)), Charset.defaultCharset());
	}
}
