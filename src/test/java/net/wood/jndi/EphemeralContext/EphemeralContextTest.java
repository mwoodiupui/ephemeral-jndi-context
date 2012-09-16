/**
 * Copyright (C) 2011, 2012 Mark H. Wood
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.wood.jndi.EphemeralContext;

import java.net.URL;
import javax.jms.ConnectionFactory;
import javax.mail.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Unit tests for EphemeralContext.
 */
public class EphemeralContextTest 
{
    /**
     * Test basic lookup, path traversal, returned object types.
     */
    @Test
    public void testPostgreSQLDataSource()
            throws NamingException
    {
        // initial-context factory, URL supplied externally
        javax.naming.Context ic = new InitialContext(null);

        // java:comp/env/jdbc/NAME
        Context jc = (Context) ic.lookup("java:comp");
        Context ec = (Context) jc.lookup("env");
        Context dc = (Context) ec.lookup("jdbc");

        Object o = dc.lookup("test");
        assertTrue("Expecting a DataSource", o instanceof DataSource);

        Object o2 = ic.lookup("java:comp/env/jdbc/test");
        assertTrue("Expecting a DataSource", o2 instanceof DataSource);

        Object o3 = ec.lookup("jdbc/test");
        assertTrue("Expecting a DataSource", o3 instanceof DataSource);

        // Check incorrect but apparently common form
        Context c1 = (Context) ic.lookup("java:/comp/env");
        Object ds1 = c1.lookup("jdbc/test");
        assertTrue("Expecting a DataSource", ds1 instanceof DataSource);

        // TODO test creation of new objects in the directory

        // TODO test deletion of directory objects

        // TODO test for nonexistent object
    }

    @Test
    public void testMail() throws NamingException
    {
        // initial-context factory, URL supplied externally
        javax.naming.Context ic = new InitialContext(null);

        // java:comp/env/mail/Session
        Object om = ic.lookup("java:comp/env/mail/Session");
        assertTrue("Expecting a mail Session", om instanceof Session);
    }

    @Test
    public void testURL() throws NamingException
    {
        // initial-context factory, URL supplied externally
        javax.naming.Context ic = new InitialContext(null);

        // java:comp/env/url/Example
        Object ou = ic.lookup("java:comp/env/url/Example");
        assertTrue("Expecting a URL", ou instanceof URL);
        System.out.println("java:comp/env/url/Example = " + (URL)ou);

        Object ou2 = ic.lookup("java:comp/env/url/BadExample");
        assertNull("Invalid URL should return null object", ou2);
    }

    @Test
    public void testJMS() throws NamingException
    {
        // initial-context factory, URL supplied externally
        javax.naming.Context ic = new InitialContext(null);

        // java:comp/env/jms/Example
        Object of = ic.lookup("java:comp/env/jms/Factory");
        assertTrue("Expecting a ConnectionFactory", of instanceof ConnectionFactory);
    }
}
