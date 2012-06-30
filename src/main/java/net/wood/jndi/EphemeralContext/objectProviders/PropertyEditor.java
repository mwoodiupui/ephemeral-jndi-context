/*
 * Copyright 2011 Mark H. Wood
 */

package net.wood.jndi.EphemeralContext.objectProviders;

import org.xml.sax.Attributes;

/**
 * Provide interpretation of SAX events from initial content.  This enables the
 * plugging in of arbitrary object types by supplying parsing support.
 *
 * @author mhwood
 */
public interface PropertyEditor
{
    /**
     * Interpret the attributes of the XML representation of this type of object.
     * 
     * @param uri
     * @param localName
     * @param qName
     * @param attributes
     * @return instance of the represented object. 
     */
    public Object interpret(String uri, String localName, String qName,
            Attributes attributes);
}
