package com.workday.jersey.schemaFilesAccess;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.json.Json;
import javax.json.JsonReader;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.workday.jersey.initProcess.ServletContextClass;

/**
 * Job object defining the process to be run periodically
 * @author Elisa Yan
 * @since 9.9.2015
 */
public class UpdateFromSVN implements Job {
	
	final static Logger logger = LoggerFactory.getLogger(UpdateFromSVN.class);

	InputStream inputStream;
	
	/**
	 * actions for the process
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			
			SVNUtil.updateSchemas(ServletContextClass.getScriptsPath());
			
			refreshSchemaList();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	/**
	 * Refreshes the list of schema names in memory by retrieving the data from file 
	 * @throws Exception 
	 * @throws IOException
	 */
	public static void refreshSchemaList() throws Exception {
		logger.debug("get svn schema names from file");
		InputStream jsonfile;
		try {
			jsonfile = new FileInputStream(ServletContextClass.getScriptsPath() + "schemaFileNames.txt");
			JsonReader jsonReader = Json.createReader(jsonfile);		
			ServletContextClass.svnSchemaNames = jsonReader.readObject();		
		} catch (FileNotFoundException e) {
			ServletContextClass init = new ServletContextClass();
			init.contextInitialized(null);
			logger.debug("1st attempt faled, run init");
			try{
				jsonfile = new FileInputStream(ServletContextClass.getScriptsPath() + "schemaFileNames.txt");
				JsonReader jsonReader = Json.createReader(jsonfile);		
				ServletContextClass.svnSchemaNames = jsonReader.readObject();			
			} catch (FileNotFoundException ex) {
				logger.error("2nd attempt failed");
				throw new Exception("Get names from file failed. " + ex.getMessage());
			}
			e.printStackTrace();
		}
		
	}
}
