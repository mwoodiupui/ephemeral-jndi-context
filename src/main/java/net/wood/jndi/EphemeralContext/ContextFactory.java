/*
 * Copyright 2011 Mark H. Wood
 */
package net.wood.jndi.EphemeralContext;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Stack;
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

/**
 * Simple, hierarchial context built in memory.
 *
 * @author mhwood
 */
public class ContextFactory
        implements InitialContextFactory
{
    private static final String INITIAL_CONTENT_SCHEMA = "initialContext.xsd";

    private static final Logger log = LoggerFactory.getLogger(ContextFactory.class);

    /**
     * Create an EphemeralContext holding the given environment.
     * 
     * @param environment
     * @return
     * @throws NamingException
     */
    public javax.naming.Context getInitialContext(Hashtable<?, ?> environment)
            throws NamingException
    {
        Context initialContext = new Context(environment, null, null);

        String contentPath = (String) initialContext.getEnvironment().get(
                Context.PROVIDER_URL);
        if (null == contentPath)
        {
            log.debug("PROVIDER_URL not specified -- no initial content loaded");
        }
        else
        {
            // Load content
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SchemaFactory sf = SchemaFactory.newInstance(
                    XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream content;
            try {
                URL contentResource = Context.class.getResource(contentPath);
                log.info("Loading initial content from {}", contentResource);
                content = contentResource.openStream();
            } catch (IOException ex) {
                log.error("Could not open initial content:", ex);
                throw new NamingException("Could not open initial content");
            }

            try {
                Schema mySchema = sf.newSchema(this.getClass().getResource(
                        INITIAL_CONTENT_SCHEMA));
                spf.setSchema(mySchema);
                SAXParser parser = spf.newSAXParser();
                parser.parse(content, new SAXHandler(parser, initialContext));
            } catch (IOException ex) {
                log.error("Could not parse initial content:  ", ex);
            } catch (ParserConfigurationException ex) {
                log.error("parsing initial content:", ex);
            } catch (SAXException ex) {
                log.error("parsing initial content:", ex);
            }
        }

        return initialContext;
    }
}
