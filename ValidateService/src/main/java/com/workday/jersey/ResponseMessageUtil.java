package com.workday.jersey;

public class ResponseMessageUtil {
	
	static final String objectOpen = "{ ";
	static final String objectClose = "}";
	static final String quote = "\"";
	static final String attrValueIs = "\" : \"";
	static final String nextAttrIs = ",";
	
	static public String buildResponseJson(String success, String fileName, String output) {
		return objectOpen + 
					quote + "success" + attrValueIs + success + quote + nextAttrIs +
					quote + "fileName" + attrValueIs + fileName + quote + nextAttrIs + 
					quote + "outText" + attrValueIs + output + quote +
				objectClose;
	}
	
	static public String buildResponseString(String fileName, String output) {
		return fileName + " " + output;
	}

}
