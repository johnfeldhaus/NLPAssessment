package com.dr.feldhaus.nlp.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

public class Sentence {
	@XmlElementWrapper(name="tokens")
	@XmlElement(name="token")
	List<Token> tokens = new ArrayList<Token>();

	public Sentence() {}
	
	public Sentence(List<Token> tokens) {
		this.tokens = tokens;
	}

	public void addToken(Token token) {
		this.tokens.add(token);
	}

	public boolean isEmpty() {
		return tokens.isEmpty();
	}
	
	public List<Token> getTokens() {
		return tokens;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokens == null) ? 0 : tokens.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sentence other = (Sentence) obj;
		if (tokens == null) {
			if (other.tokens != null)
				return false;
		} else if (!tokens.equals(other.tokens))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + tokens + "]";
	}
}
