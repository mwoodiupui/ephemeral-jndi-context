/**
 * Copyright (C) 2011, 2012, 2024 Mark H. Wood
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
    @Override
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
