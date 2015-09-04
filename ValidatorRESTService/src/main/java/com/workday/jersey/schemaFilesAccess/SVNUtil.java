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
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;

import com.workday.jersey.initProcess.ServletContextClass;


public class SVNUtil {
	

	public static void updateSchemas(String path) {
		Process p;
		JsonBuilderFactory factory = Json.createBuilderFactory(null);
		JsonArrayBuilder xsdOptsBuilder = factory.createArrayBuilder();
		Pattern pattern = Pattern.compile("([^/]*\\.xsd)$");				
		String schemasStr = "";
        char space = ' ';

		try {
	        //setting up for writing to files
			String[] repo = ServletContextClass.getRepoFolder();	        
	        PrintWriter exportScriptWriter = new PrintWriter(path + "exportSchemaScript.sh", "UTF-8");
	        PrintWriter schemaNamesWriter = new PrintWriter(path + "schemaFileNames.txt", "UTF-8");
	        
	        //for each SVN parent directory
	        for (int i = 0; i < repo.length; i++) {
	        	if (repo[i].charAt(repo[i].length()-1) != '/') {
		        	repo[i] += '/';		        	
		        }
	        	
	        	//run script to get schema file path/names
		        p = Runtime.getRuntime().exec("sh " + path + "getSchemaFileNames.sh" + space + repo[i]);
		        p.waitFor();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
	
		        //setting up for writing export script
		        String line = "";           
		        String op = "svn export";
		        String target = path + "svnFiles --force";		
	
		        //build schema selection JSON array and writing svn export script for each schema file found
		        while ((line = reader.readLine())!= null) {
		        	Matcher matcher = pattern.matcher(line);
		        	if (matcher.find()) {
		        		int start = matcher.start();
		        		String optName = matcher.group(1);
		        		xsdOptsBuilder.add(factory.createObjectBuilder()
		        				.add("option", optName));
				        exportScriptWriter.println(op + space + repo[i] +  line.substring(0, start) + "\"" + optName + "\"" + space + target);
				        System.out.println("write export sh : " + repo[i] +  line.substring(0, start) + optName);
		        	}
		        }	        
		        reader.close();	 
	        }
	        
	        //done writing export script
	        exportScriptWriter.close();
	        
	        //run export script
	        p = Runtime.getRuntime().exec("sh " + path + "exportSchemaScript.sh");
	        System.out.println("run export sh: " + "sh " + path + "exportSchemaScript.sh");

	        
	        //add retrieval time and write schema selection JSON object to file
	        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	        Date dateobj = new Date();
	        ServletContextClass.schemas = factory.createObjectBuilder()
	        							.add("date", df.format(dateobj))
	        							.add("options", xsdOptsBuilder).build();	        
	        schemasStr = ServletContextClass.schemas.toString();
	        schemaNamesWriter.print(schemasStr);
	        
	        //done writing schema name JSON to file
	        schemaNamesWriter.close();
	        System.out.println("SVNUtil: " + ServletContextClass.schemas.toString());

		} catch (Exception e) {
	        e.printStackTrace();
	    } 
	}
	
	/**
	 * Get the content of specified file as a String
	 * @param fileName specified file
	 * @return content of the file as a String
	 * @throws IOException
	 */
	public static String getSchemaFile(String fileName) throws IOException {
//		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//		String schemasFolderPath = classLoader.getResource("scripts/svnFiles").getPath();
		
		byte[] encoded = Files.readAllBytes(Paths.get(ServletContextClass.getScriptsPath() + "svnFiles/" + fileName));
		return new String(encoded, StandardCharsets.UTF_8);				
	
	}
}
