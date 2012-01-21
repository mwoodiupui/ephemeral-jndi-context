/*
 * Skeleton provided by NetBeans.  Code specific to the containing package:
 * Copyright 2011 Mark H. Wood
 */

package net.wood.jndi.EphemeralContext;

import java.util.Properties;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigorous Test :-)
     */
    public void testApp() throws NamingException
    {
        Properties jndiEnvironment = new Properties();
        jndiEnvironment.setProperty(Context.INITIAL_CONTEXT_FACTORY,
                this.getClass().getPackage().getName() + ".ContextFactory");
        jndiEnvironment.setProperty(Context.PROVIDER_URL,
                "/net/wood/jndi/EphemeralContext/test.xml");

        // java:comp/env/jdbc/NAME
        javax.naming.Context ic = NamingManager.getInitialContext(jndiEnvironment);
        Context jc = (Context) ic.lookup("java:comp");
        Context ec = (Context) jc.lookup("env");
        Context dc = (Context) ec.lookup("jdbc");

        Object o = dc.lookup("test");
        assertTrue(o instanceof DataSource);

        Object o2 = ic.lookup("java:comp/env/jdbc/test");
        assertTrue(o2 instanceof DataSource);
        
        Object o3 = ec.lookup("jdbc/test");
        assertTrue(o3 instanceof DataSource);

        // Check incorrect but apparently common form
        Context c1 = (Context) ic.lookup("java:/comp/env");
        Object ds1 = c1.lookup("jdbc/test");
        assertTrue(ds1 instanceof DataSource);
    }
}
