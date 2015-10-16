package com.workday.jersey.validationProcessing;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.capeclear.serialization.textschema.TextSchemaException;
import com.capeclear.serialization.textschema.TextSchemaProcessor;

/**
 * TextSchemaProcessor class and method wrapper
 * 
 * @author Elisa Yan
 * @author Britney Wong
 * 
 * @since 7.1.2015
 */
public class TextSchemaUtil {
	
	final static Logger logger = LoggerFactory.getLogger(TextSchemaUtil.class);
	//private String encoding = "Base64";
	/**
	 * Instantiates a TextSchemaProcessor object with schema InputStream
	 * @param schemaIs schema InputStream uploaded from client
	 * @return TextSchemaProcessor object
	 * @throws Exception 
	 */
	static public TextSchemaProcessor getTextSchemaProcessor(InputStream schemaIs) throws Exception {
		try {			
			logger.debug("creating textSchemaProcessor with schema inputstream");
			TextSchemaProcessor processor = new TextSchemaProcessor(schemaIs, null);
			return processor;	
		} catch (TextSchemaException ex) {
			logger.error("creating textSchemaProcessor with schema inputstream failed");
			throw new Exception("Failed to create textSchemaProcessor.    \n" + ex.getMessage());
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
	      Reader textIn = null;
	      try{
	    	  logger.debug("transforming text to xml");
		      textIn = new InputStreamReader(textIs);
		      processor.textToXml( textIn, xmlOut );
		      textIn.close();		      
	      } catch (Exception ex) {
	    	  logger.error("text to xml failed: " + ex.getMessage());
	    	  ex.printStackTrace();
	    	  throw new Exception("Text conversion to XML failed.    \n" + ex.getMessage());
	      } finally {
	    	  textIn.close();
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
	      Reader xmlIn = null;
	      try{
	    	  logger.debug("transforming xml to text");
		      xmlIn = new InputStreamReader(xmlIs);
		      processor.xmlToText(xmlIn, textOut);
		      textOut.close();		      
	      } catch (Exception ex) {
	    	  logger.error("xml to text failed: " + ex.getMessage());
	    	  ex.printStackTrace();
	    	  throw new Exception("XML conversion to text failed. \n" + ex.getMessage());
	      } finally {
	    	  xmlIn.close();
	      }
	      String textString = textOut.toString();
		  return textString;	      
	}
	
	

}
