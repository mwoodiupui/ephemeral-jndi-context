/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wood.jndi.EphemeralContext;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author mhwood
 */
public class JMSDestination
implements PropertyEditor
{

    public DefaultHandler getHandler()
    {
        return null;
    }

    public Object interpret(String uri, String localName, String qName,
            Attributes attributes)
    {
        // TODO
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
