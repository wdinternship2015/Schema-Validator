package com.workday.jersey;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.workday.jersey.initProcess.ServletContextClass;
import com.workday.jersey.schemaFilesAccess.SVNUtil;
import com.workday.jersey.schemaFilesAccess.UpdateFromSVN;

 
/**
 *  	 
 * Getting a JSON object containing the names of all available schemas from SVN
 * 	url: /ValidateService/webapi/schemas/options
 * 
 * Getting a SVN schema file's content as a string
 *  url: /ValidateService/webapi/schemas/getSchema
 * 
 * @author Elisa Yan
 * 
 * @since 8.25.2015
 */
@Path("schemas")
public class schemaFilesService {
	final int SUCCESS = 200;
	final int CLIENT_FAIL = 400;
	final int SERVER_ERROR = 500;
		
	
	/**
	 * Getting a JSON object containing the names of all available schemas from SVN
	 * @return a JSON object containing schema file names
	 */
	@GET  
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/options")
	public Response getSchemaOptions() {
		System.out.println("getting schema list");
		int statusInt;
		String outputString;
		ResponseBuilder response;

		try{
			
			if (ServletContextClass.getSchemas() == null) {
				UpdateFromSVN.refreshSchemaList();
			/*	ServletContextClass init = new ServletContextClass();
				init.contextInitialized(null);
				System.out.println("run init");
			*/
			} 
			
			outputString = ServletContextClass.getSchemas().toString();			
			
			statusInt = SUCCESS;
			
		} catch (Exception ex) {
			statusInt = SERVER_ERROR;
			outputString = ex.getMessage();
			ex.printStackTrace();
		}
		
		response = Response.status(statusInt).entity(outputString);
		System.out.println(outputString);
		//CORS HttpResponse header
		response.header("Access-Control-Allow-Origin", "*");
		response.header("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, X-XSRF-TOKEN");
		response.header("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
		
		//End of Header
	    return response.build();
	}
	
	
	/**
	 * Getting the file content of a specified schema from the available SVN schema files
	 * @param fileName a SVN schema file name
	 * @return the content of the specified file as a String
	 */
	@GET  
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/getSchema")
	public Response getSchemaFileContent(@QueryParam("fileName") String fileName) {
		System.out.println("getting schema content: " + fileName);
		int statusInt;
		String outputString;
		ResponseBuilder response;

		try{
			outputString = SVNUtil.getSchemaFile(fileName);
			statusInt = SUCCESS;			
		} catch (Exception ex) {
			statusInt = SERVER_ERROR;
			outputString = ex.getMessage();
			ex.printStackTrace();
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