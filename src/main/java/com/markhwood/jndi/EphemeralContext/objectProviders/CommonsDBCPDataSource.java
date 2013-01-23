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

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.xml.sax.Attributes;

/**
 *
 * @author mhwood
 */
public class CommonsDBCPDataSource
implements PropertyEditor, HasContent
{
    public Object interpret(String uri, String localName, String qName,
            Attributes attributes)
    {
        // FIXME where does wrapped datasource come in?
        GenericObjectPool connectionPool = new GenericObjectPool(null);
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(
                attributes.getValue("url"),
                attributes.getValue("user"),
                attributes.getValue("password"));
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
                connectionFactory, connectionPool, null, null, false, true);
        PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
        return dataSource;
    }

    public void add(String name, Object o)
    {
        // TODO here is where the wrapped datasource comes in.
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
