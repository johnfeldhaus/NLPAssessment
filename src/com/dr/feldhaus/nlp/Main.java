package com.dr.feldhaus.nlp;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import com.dr.feldhaus.nlp.io.FileReader;
import com.dr.feldhaus.nlp.io.XMLWriter;
import com.dr.feldhaus.nlp.model.NamedEntity;
import com.dr.feldhaus.nlp.model.Sentence;
import com.dr.feldhaus.nlp.model.TokenizedOutput;
import com.dr.feldhaus.nlp.parse.NamedEntityFinder;
import com.dr.feldhaus.nlp.parse.Parser;

public class Main {

	public static void main(String[] args) throws IOException, URISyntaxException, JAXBException {
		XMLWriter.write(findNamedEntities(new Parser().parse(FileReader.read("data/nlp_data.txt"))), "output/step-2-output.xml");
	}

	private static TokenizedOutput findNamedEntities(TokenizedOutput originalOutput) throws IOException {
		NamedEntityFinder namedEntityFinder = new NamedEntityFinder();
		namedEntityFinder.setNamedEntities(loadNamedEntities("data/NER.txt"));
		TokenizedOutput output = new TokenizedOutput();
		for(Sentence s : originalOutput.getSentences()) {
			output.addSentence(new Sentence(namedEntityFinder.findNamedEntities(s.getTokens())));
		}
		return output;
	}

	private static List<NamedEntity> loadNamedEntities(String path) throws IOException {
		String[] rawEntities = FileReader.read(path).split("\\r?\\n");
		return Arrays.stream(rawEntities)
			.map(entity -> new Parser().parse(entity))
			.map(output -> output.getSentences())
			.flatMap(s -> s.stream())
			.map(s -> s.getTokens()).map(t -> new NamedEntity(t))
			.collect(Collectors.toList());
	}
}