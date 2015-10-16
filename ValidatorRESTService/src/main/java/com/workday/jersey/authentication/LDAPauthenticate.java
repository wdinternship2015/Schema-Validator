package com.workday.jersey.authentication;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.workday.jersey.initProcess.ServletContextClass;


/**
 * LDAP authentication processing class
 * @author Elisa Yan
 * @since 9/9/2015
 */
public class LDAPauthenticate {

	final Logger logger = LoggerFactory.getLogger(LDAPauthenticate.class);
	
	
	private final String ldap = "ldap://";
	
	/**
	 * Verifies the credential provided through LDAP server defined in config.properties
	 * @param credential specified username and password pairing
	 * @return string output the values of the attributes defined in attrIds
	 * @throws Exception authentication failed
	 */
    public String authenticateUser(final Credential credential) throws Exception {
        try{
            final Hashtable<String, String> env = getEnvironmentTable(credential.getUsername(), credential.getPassword());
            DirContext ctx = new InitialDirContext(env);
            logger.info("authentication success");
            ctx.close();
            return ctx.toString();
        } catch (Exception e) {
              logger.error("Authentication failed: \n" + e.getMessage());
              throw new Exception("Authentication failed: \n" + e.getMessage());
        }
    }
    
    /**
     * Private helper method that assembles authentication environment table
     * @param userName username to be verified
     * @param password password to be verified
     * @return hashtable containing environment information
     */
    private Hashtable<String, String> getEnvironmentTable(final String userName, final String password) {
        final Hashtable<String, String> env = new Hashtable<String, String>(11);
        String ldapUrl = ldap + ServletContextClass.getAdProviderUrl() + ":" + ServletContextClass.getAdProviderSocket();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, getSecurityPrincipal(userName));
        env.put(Context.SECURITY_CREDENTIALS, password);
 //       env.put("com.sun.jndi.ldap.connect.timeout", adTimeoutValueInMilliSec);
        logger.debug("build environment hashtable success");
        return env;
    }
    
    /**
     * Private helper method that assembles security principal string 
     * @param userName specified username
     * @return security principal string for the specified username
     */
    private String getSecurityPrincipal(final String userName) {
    	logger.trace("construct security principal string: " + "cn=" + userName + ",ou=Users,ou=Workday,dc=workdayinternal,dc=com");
        return "cn=" + userName + ",ou=Users,ou=Workday,dc=workdayinternal,dc=com";
    }
}
