package com.workday.jersey;

/**
 * Utility class with static methods for building response messages
 * 
 * @author Elisa Yan
 * @author Britney Wong
 * 
 * @since 7.1.2015
 */
public class ResponseMessageUtil {
	
	static final String objectOpen = "{ ";
	static final String objectClose = "}";
	static final String quote = "\"";
	static final String attrValueIs = "\" : \"";
	static final String nextAttrIs = ",";
	
	/**
	 * Build JSON response message
	 * @param success true if request is processed as intended, false otherwise
	 * @param fileName suggested file name for the output
	 * @param output transformation result string
	 * @return response message body
	 */
	static public String buildResponseJson(String success, String fileName, String output) {
		return objectOpen + 
					quote + "success" + attrValueIs + success + quote + nextAttrIs +
					quote + "fileName" + attrValueIs + fileName + quote + nextAttrIs + 
					quote + "outText" + attrValueIs + output + quote +
				objectClose;
	}
	
	/**
	 * Build String response message
	 * @param fileName suggested file name for the output
	 * @param output transformation result string
	 * @return response message body
	 */
	static public String buildResponseString(String fileName, String output) {
		return fileName + " " + output;
	}

}
