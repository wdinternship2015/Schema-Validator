package com.workday.jersey.initProcess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ValidatorPropertyValues {

	String adProviderUrl;
	String adProviderSocket;
	String repoFoler;
	String directoryPattern;
	
	InputStream inputStream;
 
	public String getAdProviderUrl() {
		return adProviderUrl;
	}

	public String getAdProviderSocket() {
		return adProviderSocket;
	}

	public String getRepoFoler() {
		return repoFoler;
	}

	public String getDirectoryPattern() {
		return directoryPattern;
	}

	public void getPropValues() throws IOException {
 
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
 
			// get the property value and print it out
			adProviderUrl = prop.getProperty("adProviderUrl");
			adProviderSocket = prop.getProperty("adProviderSocket");
			repoFoler = prop.getProperty("repoFoler");
			directoryPattern = prop.getProperty("directoryPattern");
 
			System.out.println("adProviderUrl: " + adProviderUrl);
			System.out.println("adProviderSocket: " + adProviderSocket);
			System.out.println("repoFoler: " + repoFoler);
			System.out.println("directoryPattern: " + directoryPattern);
		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
	}
}
