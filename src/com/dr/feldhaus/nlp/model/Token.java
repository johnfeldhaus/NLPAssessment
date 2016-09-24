package com.dr.feldhaus.nlp.model;

import javax.xml.bind.annotation.XmlElement;

public class Token {
	@XmlElement
	String text = "";
	
	@XmlElement
	Boolean isNamedEntity = false;

	public Token(String text, Boolean isNamedEntity) {
		this.text = text;
		this.isNamedEntity = isNamedEntity;
	}

	public Token(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public Boolean isNamedEntity() {
		return isNamedEntity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((isNamedEntity == null) ? 0 : isNamedEntity.hashCode());
		result = prime * result + ((text == null) ? 0 : text.hashCode());
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
		Token other = (Token) obj;
		if (isNamedEntity == null) {
			if (other.isNamedEntity != null)
				return false;
		} else if (!isNamedEntity.equals(other.isNamedEntity))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "{" + text + (isNamedEntity ? "(NamedEntity)" : "") + "}";
	}
}
