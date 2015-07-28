package com.workday.jersey;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

 
/**
 * Login authorization Service provider for http requests
 * 
 * Input requirement: MediaType.APPLICATION_JSON
 * 		- field "username", type string
 * 		- field "password", type string
 * 
 * 	url: /ValidateService/webapi/login
 * 
 * 
 * @author Elisa Yan
 * @author Britney Wong
 * 
 * @since 7.28.2015
 */
@Path("/login")
public class authorizationService {
	final int SUCCESS = 200;
	final int CLIENT_FAIL = 400;
	final int SERVER_ERROR = 500;
		
	/**
	 * Processes input and schema processing requests
	 * @param formParams FormDataMultipart object in the Request body
	 * @return Response with result as a formatted string
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)    
	@Produces(MediaType.TEXT_PLAIN)
	public Response authLogin(Credential credential) {
		System.out.println("received request");
		System.out.println(credential.toString());
		int statusInt;
		String outputString;
		ResponseBuilder response;
	
		
		if (credential.getUsername().equalsIgnoreCase("admin") && credential.getPassword().equalsIgnoreCase("password")) {
			statusInt = SUCCESS;
			outputString = "ImAToken";
		} else {
			statusInt = CLIENT_FAIL;
			outputString = "http://localhost:8000/app/login.html";
		}
		System.out.println("success: " + statusInt + "\noutputString: " + outputString);
		response = Response.status(statusInt).entity(outputString);
		
		//CORS HttpResponse header
		response.header("Access-Control-Allow-Origin", "*");
		response.header("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, X-XSRF-TOKEN");
		response.header("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
		
		//End of Header
	    return response.build();
	}
	

}