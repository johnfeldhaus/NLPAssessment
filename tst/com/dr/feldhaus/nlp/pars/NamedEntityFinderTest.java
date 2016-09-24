package com.dr.feldhaus.nlp.pars;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import com.dr.feldhaus.nlp.model.NamedEntity;
import com.dr.feldhaus.nlp.model.Token;
import com.dr.feldhaus.nlp.parse.NamedEntityFinder;

public class NamedEntityFinderTest {

	@Test
	public void givenNullTokenList_shouldReturnNull() {
		assertEquals(null, findNamedEntities(null, null));
	}

	@Test
	public void givenNoEntities_shouldReturnCopyOfInputTokens() {
		assertEquals(tokens("a", "b"), findNamedEntities(tokens("a", "b"), null));
	}

	@Test
	public void givenOneEntity_andOneToken_thatMatchCompletely_shouldReturnTokenAsNamedEntity() {
		assertEquals(tokens(namedEntityToken("a")), findNamedEntities(tokens("a"), entities(tokens("a"))));
	}

	@Test
	public void givenOneEntity_andOneToken_thatMatchFirstTokenOnly_shouldNotReturnTokenAsNamedEntity() {
		assertEquals(tokens("a"), findNamedEntities(tokens("a"), entities(tokens("a", " ", "b"))));
	}

	@Test
	public void givenOneEntity_andThreeTokens_thatCombineToMatchEntity_shouldReturnSingleTokenAsNamedEntity() {
		assertEquals(tokens(namedEntityToken("a b")),
				findNamedEntities(tokens("a", " ", "b"), entities(tokens("a", " ", "b"))));
	}

	@Test
	public void givenTwoEntities_thatStartWithTheSameToken_andThreeTokens_thatCombineToMatchEntity_shouldReturnSingleTokenAsNamedEntity() {
		assertEquals(tokens(namedEntityToken("a b")),
				findNamedEntities(tokens("a", " ", "b"), entities(tokens("a", " ", "b"), tokens("a", " ", "c"))));
	}

	@Test
	public void givenTwoEntities_andThreeTokens_thatMatchBothEntities_shouldReturnSingleTokenAsLongestNamedEntity() {
		// "a b" matches named entity "a " and named entity "a b", but our
		// algorithm
		// should choose the longest match
		assertEquals(tokens(namedEntityToken("a b")),
				findNamedEntities(tokens("a", " ", "b"), entities(tokens("a", " "), tokens("a", " ", "b"))));
	}

	@Test
	public void givenOneEntity_thatMatchesTokensInMiddleOfList_shouldReturnCorrectTokenList() {
		assertEquals(tokens(token("a"), token(" "), namedEntityToken("b c"), token(" "), token("d")),
				findNamedEntities(tokens("a", " ", "b", " ", "c", " ", "d"), entities(tokens("b", " ", "c"))));
	}

	@Test
	public void givenMultipleEntities_andMultipleMatches_shouldReturnCorrectTokenList() {
		assertEquals(
				tokens(token("a"), token(" "), namedEntityToken("b c d"), token(" "), token("e"), token(" "),
						token("f"), token(" "), namedEntityToken("g h i j"), token(" "), token("k")),
				findNamedEntities(
						tokens("a", " ", "b", " ", "c", " ", "d", " ", "e", " ", "f", " ", "g", " ", "h", " ", "i", " ",
								"j", " ", "k"),
						entities(tokens("b", " ", "c", " ", "d"), tokens("g", " ", "h", " ", "i", " ", "j"))));
	}

	private List<Token> findNamedEntities(List<Token> tokens, List<NamedEntity> entities) {
		NamedEntityFinder namedEntityFinder = new NamedEntityFinder();
		namedEntityFinder.setNamedEntities(entities);
		return namedEntityFinder.findNamedEntities(tokens);
	}

	private List<Token> tokens(Token... tokens) {
		return Arrays.asList(tokens);
	}

	private List<Token> tokens(String... strings) {
		return Arrays.asList(strings).stream().map(s -> new Token(s)).collect(Collectors.toList());
	}

	private List<NamedEntity> entities(List<Token>... tokenLists) {
		return Arrays.asList(tokenLists).stream().map(tokens -> new NamedEntity(tokens)).collect(Collectors.toList());
	}

	private Token token(String string) {
		return new Token(string);
	}

	private Token namedEntityToken(String string) {
		return new Token(string, true);
	}
}
