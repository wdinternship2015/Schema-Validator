package com.workday.jersey;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.workday.jersey.authentication.Credential;
import com.workday.jersey.authentication.LDAPauthenticate;
import com.workday.jersey.initProcess.ServletContextClass;

 
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
		int statusInt;
		String outputString;
		ResponseBuilder response;
	
		LDAPauthenticate authenticate = new LDAPauthenticate();
		
		try {
			authenticate.authenticateUser(credential);
			if (ServletContextClass.getSchemas() == null) {
				ServletContextClass init = new ServletContextClass();
				init.contextInitialized(null);
				System.out.println("run init");
			} 
			
			outputString = ServletContextClass.getSchemas().toString();			
			statusInt = SUCCESS;
			
		} catch (Exception ex) {
			outputString = ex.getMessage();
			statusInt = CLIENT_FAIL;
		}
		
		response = Response.status(statusInt).entity(outputString);
		
		//CORS HttpResponse header
		response.header("Access-Control-Allow-Origin", "*");
		response.header("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, X-XSRF-TOKEN");
		response.header("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
		
		//End of Header
	    return response.build();
	}
	

}