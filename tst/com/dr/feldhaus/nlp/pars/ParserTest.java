package com.dr.feldhaus.nlp.pars;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dr.feldhaus.nlp.model.Sentence;
import com.dr.feldhaus.nlp.model.Token;
import com.dr.feldhaus.nlp.model.TokenizedOutput;
import com.dr.feldhaus.nlp.parse.Parser;

public class ParserTest {

	Parser parser = new Parser();

	@Test
	public void givenNullInput_shouldReturnEmptyTokenizedOutput() {
		assertEquals(tokenizedOutput(), parser.parse(null));
	}

	@Test
	public void givenEmptyInput_shouldReturnEmptyTokenizedOutput() {
		assertEquals(tokenizedOutput(), parser.parse(""));
	}

	@Test
	public void givenOneWordInput_shouldReturnExpectedTokenizedOutput() {
		assertEquals(tokenizedOutput(sentence(token("word"))), parser.parse("word"));
	}

	@Test
	public void givenOneWordInput_withTrailingSpace_shouldReturnExpectedTokenizedOutput() {
		assertEquals(tokenizedOutput(sentence(token("word"), token(" "))), parser.parse("word "));
	}

	@Test
	public void givenOneWordInput_withLeadingSpace_shouldReturnExpectedTokenizedOutput() {
		assertEquals(tokenizedOutput(sentence(token(" "), token("word"))), parser.parse(" word"));
	}

	@Test
	public void givenTwoWordInput_shouldReturnExpectedTokenizedOutput() {
		assertEquals(tokenizedOutput(sentence(token("word1"), token(" "), token("word2"))),
				parser.parse("word1 word2"));
	}

	@Test
	public void givenSpecialCharacters_shouldReturnTokensForEachCharacter() {
		assertEquals(
				tokenizedOutput(sentence(token("~"), token("!"), token("@"), token("#"), token("$"), token("%"),
						token("^"), token("&"), token("*"), token("("), token(")"), token("{"), token("}"), token("|"),
						token("<"), token(">"), token("?"), token("/"), token("*"), token("`"), token("["), token("]"),
						token(","), token("."), token("/"), token("-"), token("="))),
				parser.parse("~!@#$%^&*(){}|<>?/*`[],./-="));
	}

	@Test
	public void givenNumberWithDecimal_shouldRecognizeAsOneToken() {
		assertEquals(tokenizedOutput(sentence(token("1.234"))), parser.parse("1.234"));
		assertEquals(tokenizedOutput(sentence(token(".234"))), parser.parse(".234"));
		assertEquals(tokenizedOutput(sentence(token(" "), token(".234"))), parser.parse(" .234"));
	}

	@Test
	public void givenNumberWithPeriodAtEnd_shouldNotRecognizeAsOneToken() {
		assertEquals(tokenizedOutput(sentence(token("1234"), token("."))), parser.parse("1234."));
	}

	@Test
	public void givenNumberWithCommaAtBeginning_shouldNotRecognizeAsOneToken() {
		assertEquals(tokenizedOutput(sentence(token(","), token("1234"))), parser.parse(",1234"));
	}

	@Test
	public void givenNumberWithComma_shouldRecognizeAsOneToken() {
		assertEquals(tokenizedOutput(sentence(token("1,234"))), parser.parse("1,234"));
	}

	@Test
	public void givenTwoSentences_shouldSplitCorrectly() {
		assertEquals(
				tokenizedOutput(
						sentence(token("This"), token(" "), token("is"), token(" "), token("the"), token(" "),
								token("first"), token(".")),
						sentence(token(" "), token("This"), token(" "), token("is"), token(" "), token("the"),
								token(" "), token("second"), token("."))),
				parser.parse("This is the first. This is the second."));
	}

	@Test
	public void givenTwoSentencesWithNumbers_shouldSplitCorrectly() {
		assertEquals(tokenizedOutput(
				sentence(token("This"), token(" "), token("is"), token(" "), token("1.2345"), token(".")),
				sentence(token(" "), token("This"), token(" "), token("is"), token(" "), token("12,345"), token("."))),
				parser.parse("This is 1.2345. This is 12,345."));
	}

	@Test
	public void givenOneSentenceWithEllipsis_shouldOnlyParseOneSentence() {
		assertEquals(tokenizedOutput(sentence(token("This"), token(" "), token("is"), token(" "), token("."),
				token("."), token("."), token(" "), token("just"), token(" "), token("one"), token(" "),
				token("sentence"), token("."))), parser.parse("This is ... just one sentence."));
	}

	@Test
	public void givenTwoSentences_separatedByQuestionMark_shouldSplitCorrectly() {
		assertEquals(
				tokenizedOutput(
						sentence(token("This"), token(" "), token("is"), token(" "), token("the"), token(" "),
								token("first"), token("?")),
						sentence(token(" "), token("This"), token(" "), token("is"), token(" "), token("the"),
								token(" "), token("second"), token("."))),
				parser.parse("This is the first? This is the second."));
	}

	@Test
	public void givenTwoSentences_separatedByExclamationMark_shouldSplitCorrectly() {
		assertEquals(
				tokenizedOutput(
						sentence(token("This"), token(" "), token("is"), token(" "), token("the"), token(" "),
								token("first"), token("!")),
						sentence(token(" "), token("This"), token(" "), token("is"), token(" "), token("the"),
								token(" "), token("second"), token("."))),
				parser.parse("This is the first! This is the second."));
	}

	private TokenizedOutput tokenizedOutput(Sentence... sentences) {
		TokenizedOutput output = new TokenizedOutput();
		for (Sentence s : sentences)
			output.addSentence(s);
		return output;
	}

	private Sentence sentence(Token... tokens) {
		Sentence sentence = new Sentence();
		for (Token t : tokens)
			sentence.addToken(t);
		return sentence;
	}

	private Token token(String text) {
		return new Token(text);
	}
}
