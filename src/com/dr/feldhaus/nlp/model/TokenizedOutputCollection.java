package com.dr.feldhaus.nlp.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TokenizedOutputCollection {
	@XmlElement(name="tokenizedFile")
	List<TokenizedFile> fileList;

	public TokenizedOutputCollection() {}
	
	public TokenizedOutputCollection(List<TokenizedFile> fileList) {
		this.fileList = fileList;
	}
}
