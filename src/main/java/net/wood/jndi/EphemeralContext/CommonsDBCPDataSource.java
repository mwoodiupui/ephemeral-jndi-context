/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wood.jndi.EphemeralContext;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author mhwood
 */
public class CommonsDBCPDataSource
implements PropertyEditor
{

    public DefaultHandler getHandler()
    {
        return null;
    }

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
    
}
