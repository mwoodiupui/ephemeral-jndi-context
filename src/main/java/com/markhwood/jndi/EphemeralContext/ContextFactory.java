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
package com.markhwood.jndi.EphemeralContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Simple, hierarchial context built in memory.
 *
 * @author mhwood
 */
public class ContextFactory
        implements InitialContextFactory
{
    private static final String INITIAL_CONTENT_SCHEMA = "initialContext.xsd";

    private static final Logger LOG = LoggerFactory.getLogger(ContextFactory.class);

    /**
     * Create an EphemeralContext holding the given environment.
     *
     * @param environment properties to be set on the Context.
     * @return an initial Context.
     * @throws NamingException if the initial content could not be loaded.
     */
    @Override
    public javax.naming.Context getInitialContext(Hashtable<?, ?> environment)
            throws NamingException
    {
        Context initialContext = new Context(environment, null, null);

        String contentPath = (String) initialContext.getEnvironment().get(
                Context.PROVIDER_URL);
        if (null == contentPath)
        {
            LOG.debug("PROVIDER_URL not specified -- no initial content loaded");
        }
        else
        {
            // Load content
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(
                    XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream content;
            try {
                URL contentResource = getClass().getResource(contentPath);
                LOG.info("Loading initial content from {}", contentResource);
                content = contentResource.openStream();
            } catch (IOException ex) {
                LOG.error("Could not open initial content:", ex);
                throw new NamingException("Could not open initial content");
            }

            try {
                Schema mySchema = schemaFactory.newSchema(getClass().getResource(
                        INITIAL_CONTENT_SCHEMA));
                parserFactory.setSchema(mySchema);
                SAXParser parser = parserFactory.newSAXParser();
                DefaultHandler saxHandler = new InitialContentHandler(initialContext);
                parser.parse(content, saxHandler);
            } catch (IOException ex) {
                LOG.error("Could not parse initial content:  ", ex);
            } catch (ParserConfigurationException | SAXException ex) {
                LOG.error("parsing initial content:", ex);
            }
        }

        return initialContext;
    }
}
