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
		ResponseBuilder response;
		
		formParams.getFields();
		
		InputStream inputIs = formParams.getField("inputFile").getValueAs(InputStream.class);
		InputStream schemaIs =formParams.getField("schemaFile").getValueAs(InputStream.class);
		String direction = formParams.getField("direction").getValue();
		//String direction = json.getJSONObject("LabelData").getString("slogan");
		
		try{
			if (inputIs == null || schemaIs == null || direction == null) {
				response=Response.status(CLIENT_FAIL).entity("Can't process input parameters");
			} else if (direction.equalsIgnoreCase(TEXT_TO_XML)) {
				output = TransformationProcess.txtToXML(inputIs, schemaIs);
				response=Response.status(SUCCESS).entity(output);
			} else if (direction.equalsIgnoreCase(XML_TO_TEXT)) {
				output = TransformationProcess.xmlToText(inputIs, schemaIs);
				response=Response.status(SUCCESS).entity(output);
			} else {
				response=Response.status(CLIENT_FAIL).entity("Invalid direction");
			}
		} catch (Exception ex){
			response=Response.status(SERVER_ERROR).entity(ex.getMessage());
		}
		
		
		//CORS HttpResponse header
		response.header("Access-Control-Allow-Origin", "*");
		response.header("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, X-XSRF-TOKEN");
		response.header("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
		
		//End of Header
			    	
	    return response.build();
	}
	
}