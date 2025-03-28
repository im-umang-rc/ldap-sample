package portfolio.umang.service;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.io.IOException;
import java.util.*;

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

    public static String getProperty(String key) {
        return connectionProperties.getProperty(key);
    }

    public static Map<String, Boolean> getUserStatus(String target, String searchString) throws NamingException {
        Hashtable<String, String> environment = new Hashtable<>();
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put(Context.PROVIDER_URL, getProperty(target + ".url"));
        environment.put(Context.SECURITY_AUTHENTICATION, "simple");
        environment.put(Context.SECURITY_PRINCIPAL, getProperty(target + ".user"));
        environment.put(Context.SECURITY_CREDENTIALS, getProperty(target + ".pwd"));
        DirContext context = null;
        Map<String, Boolean> userStatus = new HashMap<>();
        try {
            context = new InitialDirContext(environment);

            String[] attrIDs = {getProperty(target + ".id"), getProperty(target + ".lockAttr")};
            SearchControls searchControls = new SearchControls();
            searchControls.setReturningAttributes(attrIDs);
            searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

            NamingEnumeration<SearchResult> searchResults
                    = context.search(getProperty(target + ".base"), searchString, searchControls);

            while (searchResults.hasMore()) {
                SearchResult result = (SearchResult) searchResults.next();
                Attributes attrs = result.getAttributes();
                userStatus.put(attrs.get(getProperty(target + ".id")).get().toString(), attrs.get(getProperty(target + ".lockAttr")) != null);
            }
            return userStatus;
        } finally {
            if (context != null) context.close();

        }
    }
}
