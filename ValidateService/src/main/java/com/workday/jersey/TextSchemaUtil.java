package com.workday.jersey;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import com.capeclear.serialization.textschema.TextSchemaException;
import com.capeclear.serialization.textschema.TextSchemaProcessor;


public class TextSchemaUtil {
	//private String encoding = "Base64";
	/**
	 * Instantiates a TextSchemaProcessor object with schema InputStream
	 * @param schemaIs schema InputStream uploaded from client
	 * @return TextSchemaProcessor object
	 * @throws Exception 
	 */
	static public TextSchemaProcessor getTextSchemaProcessor(InputStream schemaIs) throws Exception {
		try {			
			TextSchemaProcessor processor = new TextSchemaProcessor(schemaIs, null);
			return processor;	
		} catch (TextSchemaException ex) {
			throw new Exception("Failed to create textSchemaProcessor. \n" + ex.getMessage());
		}	
	}
	
	/**
	 * converts a text file to xml with specified TextSchemaProcessor
	 * @param processor
	 * @param textIs  text file InputStream uploaded from client
	 * @return
	 * @throws Exception
	 */
	static public String textToXMLString(TextSchemaProcessor processor, InputStream textIs) throws Exception {
		 // translate text input file to XML string
	      StringWriter xmlOut = new StringWriter();
	      try{
		      Reader textIn = new InputStreamReader(textIs);
		      processor.textToXml( textIn, xmlOut );
		      textIn.close();		      
	      } catch (Exception ex) {
	    	  ex.printStackTrace();
	    	  throw new Exception("Text conversion to XML failed. \n" + ex.getMessage());
	      } 
	      String xmlString = xmlOut.toString();
		      return xmlString;	      
	}
	
	/**
	 * converts a xml file to text with specified TextSchemaProcessor
	 * @param processor
	 * @param xmlIs  text file InputStream uploaded from client
	 * @return
	 * @throws Exception 
	 */
	static public String xmlToTextString(TextSchemaProcessor processor, InputStream xmlIs) throws Exception {
		 // translate text input file to XML string
	      StringWriter textOut = new StringWriter();
	      try{
		      Reader xmlIn = new InputStreamReader(xmlIs);
		      processor.xmlToText(xmlIn, textOut);
		      textOut.close();		      
	      } catch (Exception ex) {
	    	  ex.printStackTrace();
	    	  throw new Exception("XML conversion to text failed. \n" + ex.getMessage());
	      } 
	      String textString = textOut.toString();
		      return textString;	      
	}
	
	

}
