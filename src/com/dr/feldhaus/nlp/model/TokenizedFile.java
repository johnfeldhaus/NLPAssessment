package com.dr.feldhaus.nlp.model;

import javax.xml.bind.annotation.XmlElement;

public class TokenizedFile {
	@XmlElement
	String file;
	@XmlElement
	TokenizedOutput tokenizedOutput;
	
	public TokenizedFile(String file, TokenizedOutput tokenizedOutput) {
		this.file = file;
		this.tokenizedOutput = tokenizedOutput;
	}
}
