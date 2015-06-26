package com.workday.localtest;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.workday.jersey.TransformationProcess;

public class testTextToXML {

	public static void main(String[] args) {
		String output;
		File schemaFile = new File("/Users/elisa.yan/Documents/text input files/transamerica_inbound_loans.xsd");
		File textFile = new File("/Users/elisa.yan/Documents/text input files/transamerica_expected_text.txt");
		File xmlFile = new File("/Users/elisa.yan/Documents/text input files/TransAmericaLoanExpectedOutput.xml");
	    try{
	    	InputStream schemaIs = new FileInputStream(schemaFile);
	    	InputStream uploadedInputStream = new FileInputStream(textFile);
	    	InputStream xmlStream = new FileInputStream(xmlFile);
	    	output = TransformationProcess.txtToXML(uploadedInputStream, schemaIs);
	    	schemaIs = new FileInputStream(schemaFile);
	    	output += "\n\n" + TransformationProcess.xmlToText(xmlStream, schemaIs);
	    } catch (Exception e){
	    	e.printStackTrace();
	    	output = "didn't make it";
	    } 
	    	System.out.println(output);
	    	
	}
}
