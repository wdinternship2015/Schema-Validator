package com.workday.jersey;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
 * @since 8.28.2015
 */
@Path("/login")
public class authorizationService {

	final Logger logger = LoggerFactory.getLogger(authorizationService.class);
	
	final int SUCCESS = 200;
	final int CLIENT_FAIL = 400;
	final int SERVER_ERROR = 500;
		
	/**
	 * Processes authentication requests
	 * @param credential username and password pairing
	 * @return In successful response, a JSON object containing the names of available schema files from SVN.  If authentication fails, a failure response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)    
	@Produces(MediaType.TEXT_PLAIN)
	public Response authLogin(Credential credential) {
		
		int statusInt;
		String outputString;
		ResponseBuilder response;
	
		LDAPauthenticate authenticate = new LDAPauthenticate();
		
		try {
			if(credential == null) {
				logger.error("credential object is null");
				throw new Exception("Credential is null");
			} 
			authenticate.authenticateUser(credential);
			
			if (ServletContextClass.getSchemas() == null) {
				ServletContextClass init = new ServletContextClass();
				init.contextInitialized(null);
				logger.trace("run init");
			} 
			
			outputString = ServletContextClass.getSchemas().toString();			
			statusInt = SUCCESS;
			
		} catch (Exception ex) {
			outputString = ex.getMessage();
			statusInt = CLIENT_FAIL;
			logger.info(outputString);
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