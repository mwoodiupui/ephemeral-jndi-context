/**
 * Copyright (C) 2011, 2012 Mark H. Wood
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
package net.wood.jndi.EphemeralContext.objectProviders;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
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
        extends Authenticator
        implements PropertyEditor
{
    private String username = null;
    private String password = "";

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
        {
            username = attributes.getValue(nAttribute);
            sessionProps.setProperty("mail.user", username);
        }

        if ((nAttribute = attributes.getIndex("password")) >= 0)
            password = attributes.getValue(nAttribute);

        if ((nAttribute = attributes.getIndex("from")) >= 0)
            sessionProps.setProperty("mail.from", attributes.getValue(nAttribute));

        if (attributes.getIndex("debug") >= 0)
            sessionProps.setProperty("mail.debug", "true");
        // TODO mail.PROTOCOL.host
        // TODO mail.PROTOCOL.user
        // TODO mail.PROTOCOL.port

        Session session;
        if (null == username)
            session = Session.getInstance(sessionProps);
        else
            session = Session.getInstance(sessionProps, this);
        return session;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication()
    {
        return new PasswordAuthentication(username, password);
    }
}
