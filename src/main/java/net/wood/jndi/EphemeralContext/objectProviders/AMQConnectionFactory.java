/*
 * Copyright 2012 Mark H. Wood
 */

package net.wood.jndi.EphemeralContext.objectProviders;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.xml.sax.Attributes;

/**
 * Create a JMS ConnectionFactory using <a href='http://activemq.apache.org'>Apache ActiveMQ</a>.
 *
 * @author mhwood
 */
public class AMQConnectionFactory
        implements PropertyEditor
{
    public Object interpret(String uri, String localName, String qName,
            Attributes attributes)
    {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        // TODO configure the factory
        String property;
        if (null != (property = attributes.getValue("brokerURL")))
            factory.setBrokerURL(property);
        if (null != (property = attributes.getValue("username")))
            factory.setUserName(property);
        if (null != (property = attributes.getValue("password")))
            factory.setPassword(property);
        // TODO SSL?
        // TODO AMQ XML configurator?
        return factory;
    }
}
