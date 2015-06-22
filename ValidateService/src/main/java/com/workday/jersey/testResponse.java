package com.workday.jersey;


import javax.ws.rs.GET;

import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;


@Path("testResponse")
public class testResponse {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/{id}")
	public Response testGet(@PathParam("id") String id) {
		String serverReturnText = "test output text";
		ResponseBuilder response=Response.status(200).entity(serverReturnText);
		//Below three lines are necessary when you cross domain rest urls in the application. //For example,, if web app and rest server deployed in two different servers. For more
		
		//For more read about CORS HttpRequest
		response.header("Access-Control-Allow-Origin", "*");
		response.header("Access-Control-Allow-Methods", "GET, PUT, OPTIONS, X-XSRF-TOKEN");
		response.header("Access-Control-Allow-Headers", "Cache-Control, Pragma, Origin, Authorization, Content-Type, X-Requested-With");
		
		//End of Header
		
		return response.build();
	
	}

}
