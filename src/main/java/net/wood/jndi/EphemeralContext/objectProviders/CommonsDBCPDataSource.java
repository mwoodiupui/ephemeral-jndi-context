/*
 * Copyright 2011, 2012 Mark H. Wood
 */

package net.wood.jndi.EphemeralContext.objectProviders;

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
