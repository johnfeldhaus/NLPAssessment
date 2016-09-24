package com.dr.feldhaus.nlp.io;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.dr.feldhaus.nlp.model.TokenizedOutputCollection;

public class XMLWriter {

	public static void write(TokenizedOutputCollection tokenizedOutputCollection, String filepath) throws JAXBException {
		Marshaller jaxbMarshaller = JAXBContext.newInstance(TokenizedOutputCollection.class).createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(tokenizedOutputCollection, new File(filepath));
		jaxbMarshaller.marshal(tokenizedOutputCollection, System.out);
	}
}
