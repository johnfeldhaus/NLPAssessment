package com.dr.feldhaus.nlp.io;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.dr.feldhaus.nlp.model.TokenizedOutput;

public class XMLWriter {

	public static void write(TokenizedOutput output, String filepath) throws JAXBException {
		Marshaller jaxbMarshaller = JAXBContext.newInstance(TokenizedOutput.class).createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(output, new File(filepath));
		jaxbMarshaller.marshal(output, System.out);
	}
}
