/*
 * Copyright 2011 Mark H. Wood
 */

package net.wood.jndi.EphemeralContext;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Provide interpretation of SAX events from initial content.  This enables the
 * plugging in of arbitrary object types by supplying parsing support.
 *
 * @author mhwood
 */
interface PropertyEditor
{
    /**
     * Supply a SAX handler for the XML representation of this type of object.
     * 
     * @return handler to use within the object.
     */
    public DefaultHandler getHandler();

    /**
     * Interpret the attributes of the XML representation of this type of object.
     * 
     * @param uri
     * @param localName
     * @param qName
     * @param attributes 
     */
    public Object interpret(String uri, String localName, String qName,
            Attributes attributes);
}
