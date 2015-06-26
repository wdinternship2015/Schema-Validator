package com.workday.jersey;

import java.io.InputStream;

import com.capeclear.serialization.textschema.TextSchemaProcessor;


/**
 * Class with static methods for schema transformation
 * @author elisa.yan
 *
 */
public class TransformationProcess {

	/**
	 * Converts text from InputStream source to XML according to schema InputStream source
	 * @param textIs InputStream source for text input
	 * @param schemaIs InputStream source for schema input
	 * @return string containing resulting xml
	 * @throws Exception 
	 */
	static public String txtToXML(InputStream textIs, InputStream schemaIs) throws Exception {

		TextSchemaProcessor processor = TextSchemaUtil.getTextSchemaProcessor(schemaIs);
		String xmlString = TextSchemaUtil.textToXMLString(processor, textIs);
		
		return xmlString;
	}
	
	/**
	 * Converts text from InputStream source to XML according to schema InputStream source
	 * @param textIs InputStream source for text input
	 * @param schemaIs InputStream source for schema input
	 * @return string containing resulting xml
	 * @throws Exception 
	 */
	static public String xmlToText(InputStream xmlIs, InputStream schemaIs) throws Exception {

		TextSchemaProcessor processor = TextSchemaUtil.getTextSchemaProcessor(schemaIs);
		String textString = TextSchemaUtil.xmlToTextString(processor, xmlIs);
		
		return textString;
	}
}
