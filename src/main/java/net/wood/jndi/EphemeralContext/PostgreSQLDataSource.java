/*
 * Copyright 2011 Mark H. Wood
 */

package net.wood.jndi.EphemeralContext;

import org.postgresql.ds.PGSimpleDataSource;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author mhwood
 */
public class PostgreSQLDataSource
implements PropertyEditor
{
    public DefaultHandler getHandler()
    {
        return null;
    }

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
