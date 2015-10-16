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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.workday.jersey.schemaFilesAccess.UpdateFromSVN;
import com.workday.jersey.schemaFilesAccess.searchSVN;

/**
 * This classes is run when the application first deploys
 * 
 * @author elisa.yan
 * @since 9.3.2015
 */
public class ServletContextClass implements ServletContextListener {
	
	final Logger logger = LoggerFactory.getLogger(ServletContextClass.class);
	
	public static JsonObject svnSchemaNames;
	private static String adProviderUrl;
	private static String adProviderSocket;
	private static String[] repoFolders;
	private static String quartzExpr;
	private static String scriptsPath;
	public static String exportPID;
	
	InputStream inputStream;
	
	/**
	 * Accessor for the list of available SVN schemas
	 * @return JSON object containing the list of available SVN schemas
	 */
	public static JsonObject getSchemas() {
		return svnSchemaNames;
	}
	
	/**
	 * Accessor for LDAP provider url
	 * @return LDAP provider url
	 */
	public static String getAdProviderUrl() {
		return adProviderUrl;
	}

	/**
	 * Accessor for LDAP provider server socket
	 * @return LDAP provider server sock
	 */
	public static String getAdProviderSocket() {
		return adProviderSocket;
	}

	/**
	 * Accessor for SVN parents folder where the search for .xsd file should take place
	 * @return array list of SVN parent folders
	 */
	public static String[] getRepoFolder() {
		return repoFolders;
	}
	
	/**
	 * Accessor for path to bash scripts at run-time
	 * @return run-time bash scripts location
	 */
	public static String getScriptsPath() {
		return scriptsPath;
	}
	
	/**
	 * Getting runtime environment and properties variables
	 * @throws IOException
	 */
	private void getPropValues() throws IOException {
		logger.info("get config.properties values");
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
			// get the property value and print it out,                                   
			adProviderUrl = prop.getProperty("adProviderUrl");
			if (adProviderUrl == null || adProviderUrl.length() <= 0) {
				logger.error("cannot read adProviderUrl from config.properties");
				throw new Exception("cannot read adProviderUrl from config.properties");
			} else {
				logger.debug("adProviderUrl " + adProviderUrl);
			}
			
			adProviderSocket = prop.getProperty("adProviderSocket");
			if (adProviderSocket == null || adProviderSocket.length() <= 0) {
				logger.error("cannot read adProviderSocket from config.propertie.");
				throw new Exception("cannot read adProviderSocket from config.propertie.");
			} else {
				logger.debug("adProviderSocket " + adProviderSocket);
			}
			
			quartzExpr = prop.getProperty("quarts").replace('|', ' ');
			if (quartzExpr == null || quartzExpr.length() <= 0) {
				logger.error("cannot read quartzExpr from config.propertie.");
				throw new Exception("cannot read quartzExpr from config.propertie.");
			} else {
				logger.debug("quartzExpr: " + quartzExpr);
			}
			
			String folders = prop.getProperty("repoFolders");
			if (folders == null || folders.length() <= 0) {
				logger.error("cannot read repoFolders from config.propertie.");
				throw new Exception("cannot read repoFolders from config.propertie.");
			} else {
				logger.debug("repoFolders " + folders);
			}
			repoFolders = folders.split(",");
			
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			scriptsPath = classLoader.getResource("scripts/").getPath();
 
		} catch (Exception e) {
			logger.error("getPropValues Exception: " + e);
			e.printStackTrace();
		} finally {
			inputStream.close();
		}
	}
	
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.debug("executing contextDestroyed");
		if (exportPID != null && exportPID.length() >= 0) {
			try {
				Process newProcess = Runtime.getRuntime().exec("kill " + exportPID);
				logger.debug("kill " + exportPID);
			} catch (IOException e) {
				logger.error("kill export process failed: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * Initializes the application, retrieving environment variables
	 * and schedule periodic updates from SVN
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.info("Application launch initialization");
		try{
			getPropValues();
			
			//sets up job
//			JobDetail job = JobBuilder.newJob(UpdateFromSVN.class)
//								.withIdentity("updateFromSVN", "group1").build();
			
			JobDetail job = JobBuilder.newJob(searchSVN.class)
					.withIdentity("searchSVN", "group1").build();
			logger.trace("define svn refresh job");
			Trigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity("searchSVN", "group1")
					.withSchedule(
						CronScheduleBuilder.cronSchedule(quartzExpr))
					.build();
			logger.trace("setup cron trigger");
			//schedule job
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			logger.info("run quartz job scheduler");
			//initializing run
			scheduler.triggerJob(job.getKey());
			logger.info("run updateFromSVN 1st time at application deployment");
		} catch (Exception ex) {
			logger.error("Application launch initialization fail: " + ex.getMessage());
			ex.printStackTrace();
		}
	}	
}