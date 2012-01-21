/*
 * Copyright 2011 Mark H. Wood
 */

package net.wood.jndi.EphemeralContext;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.xml.parsers.SAXParser;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX Handler for the fixed portions of the initial-content schema.  Extensions
 * for complex types can be plugged in using the {@code object} element.
 *
 * @author mhwood
 */
public class SAXHandler
extends DefaultHandler
{
    /** The parser which uses this handler. */
    // FIXME "there can be only one"
    private final SAXParser parser;

    /** The current {@code context} element and its ancestry */
    private final List<Context> contextStack = new ArrayList<Context>(16);

    /** Remember the name of an element requiring character content. */
    private String nameToBind = null;

    /** Prevent nullary instantiation */
    private SAXHandler()
    {
        parser = null;
    }

    /**
     * 
     * @param parser the SAXParser which will call this handler.
     * @param rootContext the Context to be filled
     */
    SAXHandler(SAXParser parser, Context rootContext)
    {
        this.parser = parser;
        contextStack.add(rootContext);
    }
    
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes)
    {
        nameToBind = null;
        String name = attributes.getValue("name");
        Context currentContext = contextStack.get(contextStack.size() - 1);

        if ("initialContext".equalsIgnoreCase(qName))
            return;
        else if ("context".equalsIgnoreCase(qName))
        {
            try
            {
                Context newContext = currentContext.createSubcontext(name);
                contextStack.add(newContext);
            } catch (NamingException ex)
            {
                Logger.getLogger(SAXHandler.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
        else if ("text".equalsIgnoreCase(qName))
        {
            nameToBind = attributes.getValue("name");
        }
        // TODO more primitive types?
        else // type specifies a property editor
        {
            String type = attributes.getValue("class");
            PropertyEditor editor;
            try
            {
                editor = (PropertyEditor) Class.forName(type).newInstance();
                DefaultHandler newHandler = editor.getHandler();
                if (null != newHandler)
                    parser.getXMLReader().setContentHandler(newHandler);
                Object object = editor.interpret(uri, localName, qName, attributes);
                currentContext.bind(name, object);
            } catch (InstantiationException ex)
            {
                // TODO handle InstantiationException
                Logger.getLogger(SAXHandler.class.getName()).log(Level.SEVERE,
                        null, ex);
            } catch (IllegalAccessException ex)
            {
                // TODO handle IllegalAccessException
                Logger.getLogger(SAXHandler.class.getName()).log(Level.SEVERE,
                        null, ex);
            } catch (ClassNotFoundException ex)
            {
                // TODO handle ClassNotFoundException
                Logger.getLogger(SAXHandler.class.getName()).log(Level.SEVERE,
                        null, ex);
            } catch (SAXException ex) {
                // TODO handle SAXException
            } catch (NamingException ex) {
                // TODO handle NamingException
            }
        }
    }
    
    @Override
    public void endElement(String uri, String localName, String qName)
    {
        if ("context".equalsIgnoreCase(qName))
            contextStack.remove(contextStack.size()-1);
    }
    
    @Override
    public void characters(char[] ch, int start, int length)
    {
        if (null != nameToBind)
        {
            String content = new String(ch, start, length);
            try
            {
                contextStack.get(contextStack.size() - 1).bind(nameToBind, content);
            } catch (NamingException ex)
            {
                // TODO handle NamingException
                Logger.getLogger(SAXHandler.class.getName()).log(Level.SEVERE,
                        null, ex);
            }
        }
    }
}
