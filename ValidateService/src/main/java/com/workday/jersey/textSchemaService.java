package com.workday.jersey;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
 

@Path("/runSchema")
public class textSchemaService {
	int SUCCESS = 200;
	int CLIENT_FAIL = 400;
	int SERVER_ERROR = 500;
	String TEXT_TO_XML = "text to xml";
	String XML_TO_TEXT = "xml to text";
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)    
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/fileArray")
	public Response processFileArray(FormDataMultiPart formParams) {
		
	    String output = null;
		ResponseBuilder response;
		
		formParams.getFields();
		
		List<FormDataBodyPart> fields = formParams.getFields("inputFile");
		InputStream inputIs = fields.get(0).getValueAs(InputStream.class);
		InputStream schemaIs = fields.get(1).getValueAs(InputStream.class);		
		String direction = formParams.getField("direction").getValue();
		
		try{
			if (direction.equalsIgnoreCase(TEXT_TO_XML)) {
				output = TransformationProcess.txtToXML(inputIs, schemaIs);
				response=Response.status(SUCCESS).entity(output);
			} else if (direction.equalsIgnoreCase(XML_TO_TEXT)) {
				output = TransformationProcess.xmlToText(inputIs, schemaIs);
				response=Response.status(SUCCESS).entity(output);
			} else {
				response=Response.status(CLIENT_FAIL).entity("Invalid direction");
			}
		} catch (Exception ex){
			response=Response.status(CLIENT_FAIL).entity(ex.getMessage());
		}	
		
		//CORS HttpResponse header
		response.header("Access-Control-Allow-Origin", "*");
		response.header("Access-Control-Allow-Methods", "GET, PUT, OPTIONS, X-XSRF-TOKEN");
		response.header("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
		
		//End of Header
			    	
	    return response.build();
	}
	
	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)    
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/byFileName")
	public Response processByFileName(FormDataMultiPart formParams) {
	    String output = null;
		String statusJson;
		int statusInt;
		String outputFileName = "";
		String outputString;
		ResponseBuilder response;
		
		InputStream inputIs = formParams.getField("inputFile").getValueAs(InputStream.class);
		InputStream schemaIs =formParams.getField("schemaFile").getValueAs(InputStream.class);
		String inputFileName = formParams.getField("inputFile").getFormDataContentDisposition().getFileName();
		String direction = formParams.getField("direction").getValue();
		
		

		try{
			if (inputIs == null || schemaIs == null || direction == null) {
				statusInt = CLIENT_FAIL;
				statusJson = "false";
				outputString = "Can't process input parameters";
			} else if (direction.equalsIgnoreCase(TEXT_TO_XML)) {
				outputString = TransformationProcess.txtToXML(inputIs, schemaIs);
				statusInt = SUCCESS;
				statusJson = "true";
				outputFileName = getOutputFileName(inputFileName, direction);
			} else if (direction.equalsIgnoreCase(XML_TO_TEXT)) {
				outputString = TransformationProcess.xmlToText(inputIs, schemaIs);
				statusInt = SUCCESS;
				statusJson = "true";
				outputFileName = getOutputFileName(inputFileName, direction);
			} else {
				statusInt = CLIENT_FAIL;
				statusJson = "false";
				outputString = "Invalid direction";
			}
		} catch (Exception ex){
			statusInt = SERVER_ERROR;
			statusJson = "false";
			outputString = ex.getMessage();
			
//			output = JsonUtil.buildResponseJson(statusJson, outputFileName, outputJson);
//			response = Response.status(statusInt).entity(output);

//			response=Response.status(SERVER_ERROR).entity(ex.getMessage());
		}
//		output = JsonUtil.buildResponseJson(statusJson, outputFileName, outputJson);
		output = JsonUtil.buildResponseString(outputFileName, outputString);
		response = Response.status(statusInt).entity(output);
		
		//CORS HttpResponse header
		response.header("Access-Control-Allow-Origin", "*");
		response.header("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, X-XSRF-TOKEN");
		response.header("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
		
		//End of Header
			    	
	    return response.build();
	}
	
	private String getOutputFileName(String inputFileName, String direction) {
		if (inputFileName.length()<=0) {
			return "null";
		}
		int i = inputFileName.indexOf(".");
		if (direction.equalsIgnoreCase(TEXT_TO_XML))  {
			return inputFileName.substring(0, i-1) + "_TextToXML.xml";
		} else if (direction.equalsIgnoreCase(XML_TO_TEXT)) {
			return inputFileName.substring(0, i-1) + "_XMLToText.txt";
		}
		return "";
	}
}