package portfolio.umang.service;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

public class LdapService {

    private static final Properties connectionProperties = new Properties();

    static {
        try {
            System.out.println("Attempting to read properties");
            connectionProperties.load(LdapService.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            System.out.println("Failure reading properties");
        }
    }

    public static List<String> getTargets() {
        return Arrays.asList(connectionProperties.getProperty("servers").split(","));
    }


    public static boolean isUserLocked(String target, String uid) throws NamingException {
        Hashtable<String, String> environment = new Hashtable<>();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, connectionProperties.getProperty(target + ".url"));
        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
        environment.put(Context.SECURITY_PRINCIPAL, connectionProperties.getProperty(target + ".user"));
        environment.put(Context.SECURITY_CREDENTIALS, connectionProperties.getProperty(target + ".pwd"));
        DirContext context = null;
        try {
            context = new InitialDirContext(environment);

            String filter = "(&(objectClass=" + connectionProperties.getProperty(target + ".obj") + ")" +
                    "(" + connectionProperties.getProperty(target + ".id") + "=" + uid + "))";
            String[] attrIDs = {"pwdAccountLockedTime"};
            SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(attrIDs);
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> searchResults
                    = context.search(connectionProperties.getProperty(target + ".base"), filter, searchControls);

            if (searchResults.hasMore()) {
                SearchResult result = (SearchResult) searchResults.next();
                Attributes attrs = result.getAttributes();
                return attrs.get("pwdAccountLockedTime") != null;
            } else return false;
        } finally {
            if (context != null) context.close();
        }
    }
}
