package com.workday.jersey.authentication;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import com.workday.jersey.initProcess.ServletContextClass;

public class LDAPauthenticate {
	
//	private final String adProviderUrl = "ldap://10.10.10.5:389";  //636 ssl port for secure LDAP  //adldap
//	private final String adProviderUrl = "ldap://adldap:389";  //636 ssl port for secure LDAP  //adldap controller
	private final String ldap = "ldap://";

	
    public String authenticateUser(final Credential credential) throws Exception {
        try{
            final Hashtable<String, String> env = getEnvironmentTable(credential.getUsername(), credential.getPassword());
            DirContext ctx = new InitialDirContext(env);
            System.out.println("cool: " + ctx.toString());
            String[] attrIds = {"employeeid", "uidnumber"};
            //ctx.getAttributes("cn=elisa.yan,ou=Users,ou=Workday,dc=workdayinternal,dc=com")  //get all attributes
            String attr = ctx.getAttributes("cn=" + credential.getUsername() + ",ou=Users,ou=Workday,dc=workdayinternal,dc=com", attrIds).toString();
            System.out.println(attr);
            ctx.close();
          //  return true;
            return ctx.toString();
        } catch (Exception e) {
              System.out.println("Authentication failed: \n" + e.getMessage());
              throw new Exception(e.getMessage());
        }
        
     //   return false;
    }
    
    private Hashtable<String, String> getEnvironmentTable(final String userName, final String password) {
        final Hashtable<String, String> env = new Hashtable<String, String>(11);
        String ldapUrl = ldap + ServletContextClass.getAdProviderUrl() + ":" + ServletContextClass.getAdProviderSocket();
        env.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUrl);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, getSecurityPrincipal(userName));
        env.put(Context.SECURITY_CREDENTIALS, password);
 //       env.put("com.sun.jndi.ldap.connect.timeout", adTimeoutValueInMilliSec);
        
        return env;
    }
    
    private String getSecurityPrincipal(final String userName) {
        return "cn=" + userName + ",ou=Users,ou=Workday,dc=workdayinternal,dc=com";
    }
}
