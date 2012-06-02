/*
 * Copyright 2012 Mark H. Wood
 */

package net.wood.jndi.EphemeralContext.propertyEditors;

import java.util.Properties;
import javax.mail.Session;
import org.xml.sax.Attributes;

/**
 * Configure javax.mail.Session objects.
 * 
 * This should work for basic SMTP sessions but is far from complete.
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
        // TODO mail.password
        if ((nAttribute = attributes.getIndex("from")) >= 0)
            sessionProps.setProperty("mail.from", attributes.getValue(nAttribute));
        if (attributes.getIndex("debug") >= 0)
            sessionProps.setProperty("mail.debug", "true");
        // TODO mail.PROTOCOL.host
        // TODO mail.PROTOCOL.user
        // TODO mail.PROTOCOL.port
        Session session = Session.getInstance(sessionProps); // TODO Authenticator
        return session;
    }
    
}
