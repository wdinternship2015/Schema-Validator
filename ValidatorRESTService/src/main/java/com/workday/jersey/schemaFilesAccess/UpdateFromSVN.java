package com.workday.jersey.schemaFilesAccess;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.stream.JsonParser;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.workday.jersey.initProcess.ServletContextClass;

public class UpdateFromSVN implements Job {

	InputStream inputStream;
	
	private static String adProviderUrl;
	private static String adProviderSocket;
	private static String repoFolder;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			
			SVNUtil.updateSchemas(ServletContextClass.getScriptsPath());
			
			refreshSchemaList();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
	}
	
	public static void refreshSchemaList() throws IOException, ParseException {
		InputStream jsonfile = new FileInputStream(ServletContextClass.getScriptsPath() + "schemaFileNames.txt");
		JsonReader jsonReader = Json.createReader(jsonfile);
		
		ServletContextClass.schemas = jsonReader.readObject();
	}
}
