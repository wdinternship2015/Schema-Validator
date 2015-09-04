package com.workday.jersey.initProcess;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.json.JsonObject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.workday.jersey.schemaFilesAccess.UpdateFromSVN;

/**
 * This classes is run when the application first deploys
 * 
 * @author elisa.yan
 * @since 9.3.2015
 */
public class ServletContextClass implements ServletContextListener {
	
	public static JsonObject schemas;
	private static String adProviderUrl;
	private static String adProviderSocket;
	private static String[] repoFolders;
	private static String quartzExpr;
	private static String scriptsPath;
	
	InputStream inputStream;
	
	public static JsonObject getSchemas() {
		return schemas;
	}
	public static String getAdProviderUrl() {
		return adProviderUrl;
	}

	public static String getAdProviderSocket() {
		return adProviderSocket;
	}

	public static String[] getRepoFolder() {
		return repoFolders;
	}
	
	public static String getScriptsPath() {
		return scriptsPath;
	}
	
	/**
	 * Getting runtime environment and properties variables
	 * @throws IOException
	 */
	private void getPropValues() throws IOException {
		System.out.println("run getPropValues()");
		
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
			quartzExpr = prop.getProperty("quarts").replace('|', ' ');
			
			String folders = prop.getProperty("repoFolders");
			repoFolders = folders.split(",");
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			scriptsPath = classLoader.getResource("scripts/").getPath();
 
		} catch (Exception e) {
			System.out.println("getPropValues Exception: " + e);
			e.printStackTrace();
		} finally {
			inputStream.close();
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

	}

	/**
	 * Initializes the application, retrieving environment variables
	 * and schedule periodic updates from SVN
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {

		try{
			System.out.println("getPropValues()");
			getPropValues();
			
			//sets up job
			JobDetail job = JobBuilder.newJob(UpdateFromSVN.class)
								.withIdentity("updateFromSVN", "group1").build();
			
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity("updateFromSVN", "group1")
					.withSchedule(
						CronScheduleBuilder.cronSchedule(quartzExpr))
					.build();

			//schedule job
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			
			//initializing run
			scheduler.triggerJob(job.getKey());
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}	
}