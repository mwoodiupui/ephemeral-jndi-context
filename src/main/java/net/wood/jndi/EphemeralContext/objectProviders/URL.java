/*
 * Copyright 2012 Mark H. Wood
 */

package net.wood.jndi.EphemeralContext.objectProviders;

import java.net.MalformedURLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

/**
 * Create a java.net.URL entry from the specified {@code value} attribute.
 * <br />
 * {@code <object class='net.wood.jndi.EphemeralContext.propertyEditors.URL'
 *          value='http://www.example.com/foo/bar'/>}
 * @author mhwood
 */
public class URL
implements PropertyEditor
{
    private static final Logger log = LoggerFactory.getLogger(URL.class);

    public Object interpret(String uri, String localName, String qName,
            Attributes attributes)
    {
        String property = attributes.getValue("value");
        java.net.URL result = null;

        if (null == property)
            log.warn("URL with no value attribute");
        else
        {
            try {
                result = new java.net.URL(property);
            } catch (MalformedURLException ex) {
                log.warn("Uninterpretable URL '{}':  {}",
                        property, ex.getMessage());
            }
        }

        log.debug("returning {}", result);
        return result;
    }
}
