package com.dr.feldhaus.nlp;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.dr.feldhaus.nlp.io.FileReader;
import com.dr.feldhaus.nlp.io.XMLWriter;
import com.dr.feldhaus.nlp.model.NamedEntity;
import com.dr.feldhaus.nlp.model.Sentence;
import com.dr.feldhaus.nlp.model.TokenizedFile;
import com.dr.feldhaus.nlp.model.TokenizedOutput;
import com.dr.feldhaus.nlp.model.TokenizedOutputCollection;
import com.dr.feldhaus.nlp.parse.NamedEntityFinder;
import com.dr.feldhaus.nlp.parse.Parser;

public class Main {

	private static final String NEWLINE_REGEX = "\\r?\\n";
	private static final String MACOSX = "__MACOSX";
	private static final String OUTPUT_FILE = "output/step-3-output.xml";
	private static final String NAMED_ENTITY_FILE = "data/NER.txt";
	private static final File ZIP_FILE = new File("data/nlp_data.zip");

	public static void main(String[] args) throws Exception {

		ZipFile zipFile = new ZipFile(ZIP_FILE);
		List<TokenizedFile> tokenizedFiles = new ForkJoinPool(Math.min(10, zipFile.size()))
				.submit(() -> tokenizeFilesParallel(zipFile)).get();
		XMLWriter.write(new TokenizedOutputCollection(tokenizedFiles), OUTPUT_FILE);
	}

	private static List<TokenizedFile> tokenizeFilesParallel(ZipFile zip) throws IOException {
		// Share the NamedEntityFinder between all threads. Once its named entity list has 
		// been initialized it is thread safe
		NamedEntityFinder namedEntityFinder = new NamedEntityFinder();
		namedEntityFinder.setNamedEntities(loadNamedEntities());
		
		return Collections.list(zip.entries()).stream()
				.filter(entry -> !entry.isDirectory() && !entry.getName().startsWith(MACOSX))
				.parallel()
				.map(zipEntry -> tokenizeFile(zip, zipEntry, namedEntityFinder))
				.collect(Collectors.toList());
	}

	private static TokenizedFile tokenizeFile(ZipFile file, ZipEntry entry, NamedEntityFinder namedEntityFinder) {
			try {
				return new TokenizedFile(entry.getName(),
						tokenizeWithNamedEntities(FileReader.read(file, entry), namedEntityFinder));
			} catch (IOException e) {
				System.err.println(String.format("Error reading entry=[%s] from file=[%s]. Reason: %s. Skipping file", entry.getName(), file.getName(), e.getMessage()));
				e.printStackTrace();
				return null;
			}
		
	}

	private static TokenizedOutput tokenizeWithNamedEntities(String input, NamedEntityFinder namedEntityFinder){
		TokenizedOutput outputBeforeNamedEntities = new Parser().parse(input);
		TokenizedOutput output = new TokenizedOutput();
		for (Sentence s : outputBeforeNamedEntities.getSentences()) {
			output.addSentence(new Sentence(namedEntityFinder.findNamedEntities(s.getTokens())));
		}
		return output;
	}

	private static List<NamedEntity> loadNamedEntities() throws IOException {
		String[] rawEntities = FileReader.read(NAMED_ENTITY_FILE).split(NEWLINE_REGEX);
		return Arrays.stream(rawEntities)
				.map(entity -> new Parser().parse(entity)) // Stream<String> to Stream<TokenizedOutput>
				.map(output -> output.getSentences())      // Stream<TokenizedOutput> to Stream<List<Sentence>>
				.flatMap(s -> s.stream()).map(s -> s.getTokens()) //Stream<List<Sentence>> to Stream<List<Token>>
				.map(t -> new NamedEntity(t))              // Stream<List<Token>> to Stream<NamedEntity>
				.collect(Collectors.toList());             // Stream<NamedEntity> to List<NamedEntity>
	}
}