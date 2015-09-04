package com.workday.jersey.validationProcessing;

import java.io.ByteArrayInputStream;

import java.io.StringWriter;

import javax.management.RuntimeErrorException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.*;
import org.xml.sax.InputSource;

/**
 * Class containing public static method to format xml string with appropriate white spaces
 * 
 * @since 7.1.2015
 */
public class PrettyPrintXML {
	
	/**
	 * Formats input xml string by inserting white spaces
	 * @param xml input xml string
	 * @return formatted xml string
	 */
	public static String formatXML(String xml) {
		try {
			Document document = DocumentBuilderFactory
					.newInstance()
					.newDocumentBuilder()
					.parse(new InputSource(new ByteArrayInputStream(xml
							.getBytes("utf-8"))));

			XPath xPath = XPathFactory.newInstance().newXPath();
			NodeList nodeList = (NodeList) xPath.evaluate(
					"//text()[normalize-space()='']", document,
					XPathConstants.NODESET);

			for (int i = 0; i < nodeList.getLength(); ++i) {
				Node node = nodeList.item(i);
				node.getParentNode().removeChild(node);
			}

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");

			StringWriter stringWriter = new StringWriter();
			StreamResult streamResult = new StreamResult(stringWriter);

			transformer.transform(new DOMSource(document), streamResult);

			return stringWriter.toString();
		} catch (Exception e) {
			throw new RuntimeErrorException(new Error("Formatting xml string failed."));
		}
	}
}
