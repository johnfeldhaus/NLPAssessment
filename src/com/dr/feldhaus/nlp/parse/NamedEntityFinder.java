package com.dr.feldhaus.nlp.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.dr.feldhaus.nlp.model.NamedEntity;
import com.dr.feldhaus.nlp.model.Token;

public class NamedEntityFinder {

	private Map<Token, List<NamedEntity>> firstTokenToNamedEntityListMap = Collections.emptyMap();
	
	public void setNamedEntities(List<NamedEntity> entities) {
		if(entities == null) return;
		
		// Construct a hash map so we can quickly determine whether a token
		// is potentially the beginning of a named entity
		firstTokenToNamedEntityListMap = new HashMap<Token, List<NamedEntity>>();
		for(NamedEntity e : entities) {
			firstTokenToNamedEntityListMap.putIfAbsent(e.getTokens().get(0), new ArrayList<NamedEntity>());
			firstTokenToNamedEntityListMap.get(e.getTokens().get(0)).add(e);
		}
	}
	
	public List<Token> findNamedEntities(List<Token> tokens) {
		if(tokens == null) return null;
		if(tokens.isEmpty()) return Collections.emptyList();
		
		List<Token> output = new ArrayList<Token>();
		for(int i = 0; i < tokens.size(); i++) {
			NamedEntity match = findEntityMatch(i, tokens);
			if(match != null) {
				output.add(toNamedEntityToken(match));
				// Skip the tokens that were consumed as part of the named entity token
				i += match.getTokens().size()-1;
			}
			else {
				output.add(tokens.get(i));
			}
		}
		return output;
	}

	private NamedEntity findEntityMatch(int index, List<Token> tokens) {
		// If the token doesn't match the first token of any named entities, return
		// straight away
		if(firstTokenToNamedEntityListMap.get(tokens.get(index)) == null) {
			return null;
		}
		
		NamedEntity longestMatch = null;
		for(NamedEntity e : firstTokenToNamedEntityListMap.get(tokens.get(index))) {
			if(isMatch(index, tokens, e) && (longestMatch == null || longestMatch.getTokens().size() < e.getTokens().size())) {
				longestMatch = e;
			}
		}
		
		return longestMatch;
	}
	
	private boolean isMatch(int index, List<Token> tokens, NamedEntity entity) {
		if(index + entity.getTokens().size() > tokens.size()) {
			return false;
		}
		
		List<Token> candidate = new ArrayList<Token>();
		for(int i = 0; i < entity.getTokens().size(); i++) 
			candidate.add(tokens.get(i+index));
		
		return candidate.equals(entity.getTokens());
	}

	private Token toNamedEntityToken(NamedEntity match) {
		return new Token(joinText(match.getTokens()), true);
	}

	private String joinText(List<Token> tokens) {
		return tokens.stream().map(token -> token.getText()).collect(Collectors.joining());
	}
}