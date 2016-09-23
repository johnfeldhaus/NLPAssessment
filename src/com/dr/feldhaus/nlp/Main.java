package com.dr.feldhaus.nlp;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.bind.JAXBException;

import com.dr.feldhaus.nlp.io.FileReader;
import com.dr.feldhaus.nlp.io.XMLWriter;
import com.dr.feldhaus.nlp.parse.Parser;

public class Main {

	public static void main(String[] args) throws IOException, URISyntaxException, JAXBException {
		XMLWriter.write(new Parser().parse(FileReader.read("data/nlp_data.txt")), "output/step-1-output.xml");
	}
}