package com.skp.abtest.sample;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

/*
  참조: Attribute 설명 - https://www.computerperformance.co.uk/logon/ldap-attributes-active-directory/
    cn, name, sAMAccountName 모두 동일하였음
    givenName + sn = first name + last name
  Config:
      host: 10.40.29.172
      port: 389
      attribute: cn
      base: OU=Person,DC=SKP,DC=AD
      admin_user: user
      admin_password: password
      ssl: false
 */
public class LDAPTest {
    private static final Logger logger = LoggerFactory.getLogger(LDAPTest.class);

    @Ignore
    @Test
    public void testAuth() throws Exception {
        String user = "skp\\1001291";
        String password = "password";
        String server = "LDAP://10.40.29.172:389";

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, server);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS, password);

        // Create the initial context
        DirContext ctx = new InitialDirContext(env);
        boolean result = ctx != null;
        if(ctx != null)
            ctx.close();
        assertEquals(true, result);
    }

    @Ignore
    @Test
    public void testSearch() throws Exception {
        String user = "skp\\1001291";
        String password = "password";
        String server = "ldap://10.40.29.172:389";

        Hashtable<String, String> env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, server);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS, password);
        LdapContext ctx;

        try {
            ctx = new InitialLdapContext(env, null);
            System.out.println("Connection Success!");
            String returnedAtts[] = {"cn", "name", "sAMAccountName", "givenName", "sn", "dn",
                    "displayName", "mail", "mobile", "department"};

            // SearchBase, SearchFilter
            String searchBase = "OU=Person,DC=SKP,DC=AD";
            String searchFilter = "(&(objectClass=user)(sAMAccountName=1001291))";
//            String searchFilter = "(&(objectClass=user)(givenName=태형*))";

            // Create the search controls
            SearchControls searchCtls = new SearchControls();
            searchCtls.setReturningAttributes(returnedAtts);

            // Specify the search scope
            searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                Attributes attrs = sr.getAttributes();
                Map amap = null;
                if (attrs != null) {
                    amap = new HashMap();
                    NamingEnumeration ne = attrs.getAll();
                    while (ne.hasMore()) {
                        Attribute attr = (Attribute) ne.next();
                        amap.put(attr.getID(), attr.get());
                    }
                    ne.close();
                }

                System.out.println("SearchResult:");
                amap.forEach((k, v) -> {
                    System.out.format("key : %s, value : %s%n", k, v);
                });
            }
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            System.out.println("Connection Fail!");
            e.printStackTrace();
        }
    }

}
