package com.dr.feldhaus.nlp.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TokenizedOutput {
	@XmlElementWrapper(name="sentences")
	@XmlElement(name="sentence")
	List<Sentence> sentences = new ArrayList<Sentence>();

	public void addSentence(Sentence sentence) {
		this.sentences.add(sentence);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((sentences == null) ? 0 : sentences.hashCode());
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
		TokenizedOutput other = (TokenizedOutput) obj;
		if (sentences == null) {
			if (other.sentences != null)
				return false;
		} else if (!sentences.equals(other.sentences))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + sentences + "]";
	}
}
