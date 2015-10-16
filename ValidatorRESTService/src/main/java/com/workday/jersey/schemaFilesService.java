package com.workday.jersey;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.workday.jersey.initProcess.ServletContextClass;
import com.workday.jersey.schemaFilesAccess.SVNUtil;


 
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

	final Logger logger = LoggerFactory.getLogger(schemaFilesService.class);
	
	final int SUCCESS = 200;
	final int CLIENT_FAIL = 400;
	final int SERVER_ERROR = 500;
			
	
	/**
	 * Getting the file content of a specified schema from the available SVN schema files
	 * @param fileName a SVN schema file name
	 * @return the content of the specified file as a String
	 */
	@GET  
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/getSchema")
	public Response getSchemaFileContent(@QueryParam("option") String fileName, @QueryParam("svnPathToFile") String svnPathToFile) {
		logger.info("getting schema content: " + fileName + " from " + svnPathToFile);
		int statusInt;
		String outputString;
		ResponseBuilder response;

		try{
//			outputString = SVNUtil.getSchemaFile(fileName);
			outputString = SVNUtil.getSchemaContent(fileName, svnPathToFile);
			statusInt = SUCCESS;			
		} catch (IOException e) {
			logger.error("IOException on getSchema 1st attempt, " + e.getMessage());
			ServletContextClass init = new ServletContextClass();
			init.contextInitialized(null);
			logger.info("run init");
			try{
				outputString = SVNUtil.getSchemaFile(fileName);
				statusInt = SUCCESS;			
				logger.info("attempt 2 success");
			} catch (Exception ex) {
				logger.debug("IOException on getSchema 2nd attempt, " + ex.getMessage());
				statusInt = SERVER_ERROR;
				outputString = ex.getMessage();
				ex.printStackTrace();
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
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