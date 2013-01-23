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
package com.markhwood.jndi.EphemeralContext.objectProviders;

import org.postgresql.ds.PGSimpleDataSource;
import org.xml.sax.Attributes;

/**
 *
 * @author mhwood
 */
public class PostgreSQLDataSource
implements PropertyEditor
{
    public PGSimpleDataSource interpret(String uri, String localName, String qName,
            Attributes attributes)
    {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        String property;

        if (null != (property = attributes.getValue("serverName")))
            ds.setServerName(property);
        if (null != (property = attributes.getValue("databaseName")))
            ds.setDatabaseName(property);
        if (null != (property = attributes.getValue("portNumber")))
            ds.setPortNumber(Integer.valueOf(property));
        if (null != (property = attributes.getValue("user")))
            ds.setUser(property);
        if (null != (property = attributes.getValue("password")))
            ds.setPassword(property);
        /* TODO not defined?
        if (null != (property = attributes.getValue("ssl")))
            ds.setSsl(Boolean.valueOf(property));
        if (null != (property = attributes.getValue("sslFactory")))
            ds.setSslFactory(property);
         */

        return ds;
    }
}
