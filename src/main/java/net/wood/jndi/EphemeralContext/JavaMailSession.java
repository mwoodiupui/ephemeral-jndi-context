/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.wood.jndi.EphemeralContext;

import java.util.Properties;
import javax.mail.Session;
import org.xml.sax.Attributes;

/**
 *
 * @author mhwood
 */
public class JavaMailSession
implements PropertyEditor
{
    public Object interpret(String uri, String localName, String qName,
            Attributes attributes)
    {
        Properties sessionProps = new Properties();
        int nAttribute;
        if ((nAttribute = attributes.getIndex("storeProtocol")) >= 0)
            sessionProps.setProperty("mail.store.protocol", attributes.getValue(nAttribute));
        if ((nAttribute = attributes.getIndex("transferProtocol")) >= 0)
            sessionProps.setProperty("mail.transfer.protocol", attributes.getValue(nAttribute));
        if ((nAttribute = attributes.getIndex("host")) >= 0)
            sessionProps.setProperty("mail.host", attributes.getValue(nAttribute));
        if ((nAttribute = attributes.getIndex("user")) >= 0)
            sessionProps.setProperty("mail.user", attributes.getValue(nAttribute));
        if ((nAttribute = attributes.getIndex("from")) >= 0)
            sessionProps.setProperty("mail.from", attributes.getValue(nAttribute));
        if (attributes.getIndex("debug") >= 0)
            sessionProps.setProperty("mail.debug", "true");
        // TODO mail.PROTOCOL.host
        // TODO mail.PROTOCOL.user
        Session session = Session.getInstance(sessionProps);
        return session;
    }
    
}
