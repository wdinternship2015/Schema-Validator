package com.workday.jersey.schemaFilesAccess;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.workday.jersey.initProcess.ServletContextClass;

/**
 * Utility class providing methods for SVN interactions
 * @author elisa.yan
 * @since 9.9.2015
 */
public class SVNUtil {

	final static Logger logger = LoggerFactory.getLogger(SVNUtil.class);
	
	
	public static void getSchemaNamesFromSVN(String pathToScripts) {
		logger.trace("searching SVN");
		Process newProcess;
		JsonArrayBuilder xsdOptionsBuilder = Json.createArrayBuilder();
		Pattern xsdFileNamePattern = Pattern.compile("([^/]*\\.xsd)$");				
        char space = ' ';
        
      //read and write file names
        String getSchemaNamesScript = "getSchemaFileNames.sh";
        String schemaNamesFile = "schemaFileNames.txt";
        BufferedReader processInstreamReader;
        String outputLine = "";           

      //setting up for capturing file names
        Matcher fileNameMatcher;
        String xsdFileName;
        int fileNameStartIndex;
        
      //setup timer variables for tracking time elapsed
        long searchStart;
        long searchEnd;
        long searchTime;

		try {
	        //setting up for writing to files
			String[] svnRepoFolders = ServletContextClass.getRepoFolder();	        
	        PrintWriter schemaNamesWriter = new PrintWriter(pathToScripts + schemaNamesFile, "UTF-8");
	        logger.trace("opening printwriters for writing to files");
	        	        
	        //for each SVN parent directory
	        for (int i = 0; i < svnRepoFolders.length; i++) {
	        	searchStart = System.currentTimeMillis();
	        	if (svnRepoFolders[i].charAt(svnRepoFolders[i].length()-1) != '/') {
		        	svnRepoFolders[i] += '/';		        	
		        }
	        	logger.trace("run getSchemaFileNames.sh for " + svnRepoFolders[i]);
	        	//run script to get schema file path/names
		        newProcess = Runtime.getRuntime().exec("sh " + pathToScripts + getSchemaNamesScript + space + svnRepoFolders[i]);
		        newProcess.waitFor();
		        searchEnd = System.currentTimeMillis();
		        searchTime = searchEnd - searchStart;
		        logger.debug("search time: " + searchTime + "ms " + svnRepoFolders[i]);
		        processInstreamReader = new BufferedReader(new InputStreamReader(newProcess.getInputStream()));	
	
		      //build schema selection JSON array for each schema file found
		        while ((outputLine = processInstreamReader.readLine())!= null) {
		        	fileNameMatcher = xsdFileNamePattern.matcher(outputLine);
		        	if (fileNameMatcher.find()) {
		        		fileNameStartIndex = fileNameMatcher.start();
		        		xsdFileName = fileNameMatcher.group(1);
		        		xsdOptionsBuilder.add(Json.createObjectBuilder().add("option", xsdFileName).add("svnPathToFile", svnRepoFolders[i] +  outputLine.substring(0, fileNameStartIndex)));
		        	}
		        }	        
		        processInstreamReader.close();	 
	       	}
	        
	        //add retrieval time and write schema selection JSON object to file
	        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	        Date dateobj = new Date();
	        ServletContextClass.svnSchemaNames = Json.createObjectBuilder()
	        							.add("date", df.format(dateobj))
	        							.add("options", xsdOptionsBuilder).build();	
	        logger.info("svnSchemaNames switched to latest search result " + ServletContextClass.svnSchemaNames.getJsonArray("options").size());
	        schemaNamesWriter.print(ServletContextClass.svnSchemaNames);
	        
	        //done writing schema name JSON to file
	        schemaNamesWriter.close();
	        logger.debug("schema updated from SVN: " + df.format(dateobj));
		} catch (Exception e) {
	        e.printStackTrace();
	    } 
	}
	
	public static String getSchemaContent(String filename, String svnPathToFile) throws Exception {          
        String svnCmd = "svn export";
        String exportTarget = ServletContextClass.getScriptsPath() + "svnFiles";
        char space = ' ';
       //read and write file names
        String exportScript = "exportFile.sh";
        PrintWriter exportScriptWriter = new PrintWriter(ServletContextClass.getScriptsPath() + exportScript, "UTF-8");
        BufferedReader processInstreamReader;
        
        
		try{
			return getSchemaFile(filename);
		} catch (IOException e) {
			logger.info(e.getMessage() + ", exporting from SVN");
			try{
				exportScriptWriter.println("#!/bin/bash");
		        exportScriptWriter.println(svnCmd + space + svnPathToFile + "\"" + filename + "\"" + space + exportTarget);
		        exportScriptWriter.close();
		        Process p = Runtime.getRuntime().exec("sh " + ServletContextClass.getScriptsPath() + exportScript);
				p.waitFor();

				
				logger.info(svnCmd + space + svnPathToFile + "\"" + filename + "\"" + space + exportTarget);
//				Runtime.getRuntime().exec(svnCmd + space + svnPathToFile + space + exportTarget);
//				Process p = Runtime.getRuntime().exec("sh " + ServletContextClass.getScriptsPath() + exportScript + space + svnPathToFile + space + exportTarget);
//				String cmd = svnCmd + space + svnPathToFile +  "\"" + filename + "\""  + space + exportTarget;
//				Process p = Runtime.getRuntime().exec(svnCmd + space + svnPathToFile   + "\"" + filename + "\"" + space + exportTarget);
//				Process p = Runtime.getRuntime().exec(svnCmd + space + svnPathToFile + filename + space + exportTarget);
//				Process p = Runtime.getRuntime().exec(svnCmd + space + svnPathToFile + "\"" + filename + "\"" + space + exportTarget);
//				p.waitFor();
				processInstreamReader = new BufferedReader(new InputStreamReader(p.getInputStream()));	
				String outputLine;
				while ((outputLine = processInstreamReader.readLine())!= null) {
					logger.debug(outputLine);					
				}
				return getSchemaFile(filename);
			} catch (IOException ex) {
				logger.error("SVN export failed: " + ex.getMessage());
				throw new Exception("SVN export failed: " + ex.getMessage());
			}
		}
	}
	
	
	/**
	 * Getting fresh data from SVN
	 * @param pathToScripts run-time path to bash script folder
	 */
	public static void updateSchemas(String pathToScripts) {
		logger.trace("getting information from SVN");
		Process newProcess;
		JsonBuilderFactory jsonBuilderFactory = Json.createBuilderFactory(null);
		JsonArrayBuilder xsdOptsBuilder = jsonBuilderFactory.createArrayBuilder();
		Pattern xsdFileNamePattern = Pattern.compile("([^/]*\\.xsd)$");				
		String schemasNamesStr = "";
        char space = ' ';
        
      //read and write file names
        String exportSchemaScript = "exportSchemaScript.sh";
        String getSchemaNamesScript = "getSchemaFileNames.sh";
        String schemaNamesFile = "schemaFileNames.txt";
        String forkToExportScript = "forkToExport.sh";
        BufferedReader processInstreamReader;
        
      //setting up for writing export script
        String outputLine = "";           
        String svnCmd = "svn export";
        String exportTarget = pathToScripts + "svnFiles --force";
      
      //setting up for capturing file names
        Matcher fileNameMatcher;
        String xsdFileName;
        int fileNameStartIndex;
        
      //setup timer variables for tracking time elapsed
        long searchStart;
        long searchEnd;
        long exportStart;
        long exportEnd;
        long searchTime;
        long exportTime;
             
		try {
	        //setting up for writing to files
			String[] svnRepoFolders = ServletContextClass.getRepoFolder();	        
	        PrintWriter exportScriptWriter = new PrintWriter(pathToScripts + exportSchemaScript, "UTF-8");
	        PrintWriter schemaNamesWriter = new PrintWriter(pathToScripts + schemaNamesFile, "UTF-8");
	        logger.trace("opening printwriters for writing to files");
	        
	        //for each SVN parent directory
	        for (int i = 0; i < svnRepoFolders.length; i++) {
	        	searchStart = System.currentTimeMillis();
	        	if (svnRepoFolders[i].charAt(svnRepoFolders[i].length()-1) != '/') {
		        	svnRepoFolders[i] += '/';		        	
		        }
	        	logger.trace("run getSchemaFileNames.sh for " + svnRepoFolders[i]);
	        	//run script to get schema file path/names
		        newProcess = Runtime.getRuntime().exec("sh " + pathToScripts + getSchemaNamesScript + space + svnRepoFolders[i]);
		        newProcess.waitFor();
		        searchEnd = System.currentTimeMillis();
		        searchTime = searchEnd - searchStart;
		        logger.debug("search time: " + searchTime + "ms " + svnRepoFolders[i]);
		        processInstreamReader = new BufferedReader(new InputStreamReader(newProcess.getInputStream()));	
		        exportScriptWriter.println("#!/bin/bash");
	
		        //build schema selection JSON array and writing svn export script for each schema file found
		        while ((outputLine = processInstreamReader.readLine())!= null) {
		        	fileNameMatcher = xsdFileNamePattern.matcher(outputLine);
		        	if (fileNameMatcher.find()) {
		        		fileNameStartIndex = fileNameMatcher.start();
		        		xsdFileName = fileNameMatcher.group(1);
		        		xsdOptsBuilder.add(jsonBuilderFactory.createObjectBuilder().add("option", xsdFileName));
				        exportScriptWriter.println(svnCmd + space + svnRepoFolders[i] +  outputLine.substring(0, fileNameStartIndex) + "\"" + xsdFileName + "\"" + space + exportTarget);
//				        exportScriptWriter.println("echo " + xsdFileName);
				        logger.trace("write export sh : " + svnRepoFolders[i] +  outputLine.substring(0, fileNameStartIndex) + xsdFileName);
		        	}
		        }	        
		        processInstreamReader.close();	 
		        
		        //add retrieval time and write schema selection JSON object to file
		        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		        Date dateobj = new Date();
		        ServletContextClass.svnSchemaNames = jsonBuilderFactory.createObjectBuilder()
		        							.add("date", df.format(dateobj))
		        							.add("options", xsdOptsBuilder).build();	
		        logger.info("svnSchemaNames switched to latest search result");
		        schemasNamesStr = ServletContextClass.svnSchemaNames.toString();
		        schemaNamesWriter.print(schemasNamesStr);
		        
		        //done writing schema name JSON to file
		        schemaNamesWriter.close();
		        logger.debug("schema updated from SVN: " + df.format(dateobj));
	        }
	        
	        
	        //add retrieval time and write schema selection JSON object to file
	        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	        Date dateobj = new Date();
	        ServletContextClass.svnSchemaNames = jsonBuilderFactory.createObjectBuilder()
	        							.add("date", df.format(dateobj))
	        							.add("options", xsdOptsBuilder).build();	
	        logger.info("svnSchemaNames switched to latest search result");
	        schemasNamesStr = ServletContextClass.svnSchemaNames.toString();
	        schemaNamesWriter.print(schemasNamesStr);
	        
	        //done writing schema name JSON to file
	        schemaNamesWriter.close();
	        logger.debug("schema updated from SVN: " + df.format(dateobj));

	        //done writing export script
	        exportScriptWriter.close();
	        logger.debug("exportScriptWriter close");
	        //run export script
	        exportStart = System.currentTimeMillis();
//	        logger.debug("run export script");
//	        newProcess = Runtime.getRuntime().exec("sh " + pathToScripts + exportSchemaScript);
	        logger.debug("run fork to export script");
	        newProcess = Runtime.getRuntime().exec("sh " + pathToScripts + forkToExportScript + " " + pathToScripts);
	        
	        processInstreamReader = new BufferedReader(new InputStreamReader(newProcess.getInputStream()));	
	        Pattern pidPattern = Pattern.compile("/([^/]*)/");
	        Matcher pidMatcher;
	        while ((outputLine = processInstreamReader.readLine())!= null) {
	        	logger.debug(outputLine);
	        	pidMatcher = pidPattern.matcher(outputLine);
	        	if (pidMatcher.find()) {
	        		ServletContextClass.exportPID = pidMatcher.group(1);
	        		logger.debug("export pid: " + ServletContextClass.exportPID);
	        	}
	        	
	        }
	        newProcess.waitFor();
	        exportEnd = System.currentTimeMillis();
	        exportTime = exportEnd - exportStart;
//	        logger.debug("total export time: " + exportTime + " ms "+ "sh " + pathToScripts + exportSchemaScript);
	        logger.debug("total export time: " + exportTime + " ms "+ "sh " + pathToScripts + forkToExportScript);

		} catch (Exception e) {
	        e.printStackTrace();
	    } 
	}
	
	/**
	 * Get the content of an exported SVN file as a String
	 * @param fileName specified file
	 * @return content of the file as a String
	 * @throws IOException
	 */
	public static String getSchemaFile(String fileName) throws IOException {
		
		byte[] encoded = Files.readAllBytes(Paths.get(ServletContextClass.getScriptsPath() + "svnFiles/" + fileName));
		return new String(encoded, StandardCharsets.UTF_8);				
	
	}
}
