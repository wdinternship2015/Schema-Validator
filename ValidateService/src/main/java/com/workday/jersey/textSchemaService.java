package com.workday.jersey;

import java.io.InputStream;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;


import org.glassfish.jersey.media.multipart.FormDataMultiPart;
 
/**
 * Text Schema Service provider for http requests
 * 
 * Input requirement: MediaType.MULTIPART_FORM_DATA
 * 		- field "inputFile", type File
 * 		- field "schemaFile", type File
 * 		- field "direction", type text
 * 
 * Supported transformation:
 * 		-text file to xml.  Requires String "txt to xml" for param "direction"
 * 		-xml file to text.  Requires String "xml to txt" for param "direction"
 * 
 * 	url: /ValidateService/webapi/runSchema/byFileName
 * 
 * Transformation processing error messages are generated by TextSchemaProcessor
 * 
 * @author Elisa Yan
 * @author Britney Wong
 * 
 * @since 7.1.2015
 */
@Path("/runSchema")
public class textSchemaService {
	final int SUCCESS = 200;
	final int CLIENT_FAIL = 400;
	final int SERVER_ERROR = 500;
	final String TEXT_TO_XML = "txt to xml";
	final String XML_TO_TEXT = "xml to txt";
		
	/**
	 * Processes input and schema processing requests
	 * @param formParams FormDataMultipart object in the Request body
	 * @return Response with result as a formatted string
	 */
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)    
	@Produces(MediaType.TEXT_PLAIN)
	public Response processByFileName(FormDataMultiPart formParams) {
		int statusInt;
		String outputString;
		ResponseBuilder response;
		
		InputStream inputIs = formParams.getField("inputFile").getValueAs(InputStream.class);
		InputStream schemaIs =formParams.getField("schemaFile").getValueAs(InputStream.class);
		String inputFileName = formParams.getField("inputFile").getFormDataContentDisposition().getFileName();
		String direction = formParams.getField("direction").getValue();
		
		if(isDirectionValid(inputFileName, direction)) {

			try{
				if (inputIs == null || schemaIs == null || direction == null) {
					statusInt = CLIENT_FAIL;
					outputString = "Can't process input parameters";
				} else if (direction.equalsIgnoreCase(TEXT_TO_XML)) {
					statusInt = SUCCESS;
					outputString = TransformationProcess.txtToXML(inputIs, schemaIs);
				} else if (direction.equalsIgnoreCase(XML_TO_TEXT)) {
					statusInt = SUCCESS;
					outputString = TransformationProcess.xmlToText(inputIs, schemaIs);
				} else {
					statusInt = CLIENT_FAIL;
					outputString = "Invalid direction";
				}
			} catch (Exception ex){
				statusInt = SERVER_ERROR;
				outputString = ex.getMessage();
			}
		} else {
			statusInt = CLIENT_FAIL;
			outputString = "Invalid direction.  \nInput file type does not match transformation direction.";
		}
		response = Response.status(statusInt).entity(outputString);
		
		//CORS HttpResponse header
		response.header("Access-Control-Allow-Origin", "*");
		response.header("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, X-XSRF-TOKEN");
		response.header("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
		
		//End of Header
			    	
	    return response.build();
	}
	
	
	/**
	 * Private helper method checking compatibility of transformation direction and input type
	 * @param inputFileName extension is used for comparison with direction
	 * @param direction used for validating the input type
	 * @return true if input file extension is consistent with transformation direction, false otherwise
	 */
	private boolean isDirectionValid(String inputFileName, String direction) {
		if (direction.equalsIgnoreCase(TEXT_TO_XML)){
			return inputFileName.contains(".txt");
		} else if (direction.equalsIgnoreCase(XML_TO_TEXT)) {
			return inputFileName.contains(".xml");
		}
		return false;
	}
	
}