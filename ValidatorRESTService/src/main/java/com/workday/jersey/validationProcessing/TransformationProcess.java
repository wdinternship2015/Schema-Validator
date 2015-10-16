package com.workday.jersey.validationProcessing;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capeclear.serialization.textschema.TextSchemaProcessor;



/**
 * Class with static methods for schema transformation
 * @author Elisa Yan
 * @author Britney Wong
 * 
 * @since 7.1.2015
 */
public class TransformationProcess {
	
	final static Logger logger = LoggerFactory.getLogger(TransformationProcess.class);

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
		logger.info("invoking TextSchemaProcessor methods for transforming text to xml");
		return PrettyPrintXML.formatXML(xmlString);
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
		logger.info("invoking TextSchemaProcessor methods for transforming xml to text");
		return textString;
	}
}
