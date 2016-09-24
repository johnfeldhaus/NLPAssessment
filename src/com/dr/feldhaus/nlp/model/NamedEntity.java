package com.dr.feldhaus.nlp.model;

import java.util.List;

public class NamedEntity {
	List<Token> tokens;

	public NamedEntity(List<Token> tokens) {
		this.tokens = tokens;
	}

	public List<Token> getTokens() {
		return tokens;
	}	
}
