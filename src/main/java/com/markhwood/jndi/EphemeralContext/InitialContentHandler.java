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

import java.util.Stack;
import javax.naming.Context;
import javax.naming.NamingException;

import com.markhwood.jndi.EphemeralContext.objectProviders.HasContent;
import com.markhwood.jndi.EphemeralContext.objectProviders.HasText;
import com.markhwood.jndi.EphemeralContext.objectProviders.PropertyEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX Handler for the initial-content schema.  Extensions
 * for complex types can be plugged in using the {@code object} element.
 *
 * @author mhwood
 */
public class InitialContentHandler
        extends DefaultHandler
{
    /** The current {@code context} element and its ancestry. */
    private final Stack<Frame> handlerStack = new Stack<>();

    /** Usual message sink. */
    private static final Logger log = LoggerFactory.getLogger(InitialContentHandler.class);

    /** File context of the current operations. */
    private Locator location = null;

    /** Prevent 0-argument instantiation, which would produce a broken instance. */
    private InitialContentHandler()
    {
    }

    /**
     * Construct a Handler to fill a given Context.
     * @param rootContext the Context to be filled
     */
    InitialContentHandler(Context rootContext)
    {
        handlerStack.push(new Frame(null, rootContext));
    }

    @Override
    public void setDocumentLocator(Locator loc)
    {
        location = loc;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes)
    {
        String name = attributes.getValue("name");
        Context currentContext = (Context) handlerStack.peek().object;

        if ("text".equalsIgnoreCase(qName))
        {
            log.debug("new text \"{}\"", name);

            handlerStack.push(new Frame(name, new Text()));
        }
        // TODO more primitive types?
        // Container types
        else if ("initialContext".equalsIgnoreCase(qName))
            return;
        else if ("context".equalsIgnoreCase(qName))
        {
            log.debug("new context \"{}\"", name);

            try
            {
                Context newContext = currentContext.createSubcontext(name);
                handlerStack.push(new Frame(name, newContext));
            } catch (NamingException ex)
            {
                log.error("Failed to create a subcontext \"{}\" at {}", name, where());
            }
        }
        else if ("object".equalsIgnoreCase(qName)) // type specifies a property editor
        {
            String type = attributes.getValue("class");
            PropertyEditor editor;

            log.debug("New object \"{}\" class {}", name, type);

            try
            {
                editor = (PropertyEditor) Class.forName(type).newInstance();
                Object object = editor.interpret(uri, localName, qName, attributes);
                handlerStack.push(new Frame(name, object));
            } catch (InstantiationException ex)
            {
                // TODO handle InstantiationException
                log.error("", ex);
            } catch (IllegalAccessException ex)
            {
                // TODO handle IllegalAccessException
                log.error("", ex);
            } catch (ClassNotFoundException ex)
            {
                // TODO handle ClassNotFoundException
                log.error("", ex);
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    {
        Frame completed = handlerStack.pop();
        Frame container;
        if (!handlerStack.empty())
            container= handlerStack.peek();
        else
            container = null;

        if ("text".equalsIgnoreCase(qName))
        {
            Context context = (Context) container.object;
            Text text = (Text) completed.object;
            log.debug("Completed text \"{}\"", completed.name);
            try
            {
                context.bind(completed.name, text.getValue());
            } catch (NamingException ex)
            {
                log.error("", ex);
            }
        }
        else if ("initialContext".equalsIgnoreCase(qName))
        {
            // TODO something to do?
        }
        else if ("context".equalsIgnoreCase(qName)
                || "object".equalsIgnoreCase(qName))
        {
            log.debug("Completed {} \"{}\"", qName, completed.name);
            try {
                log.debug("Adding to {}", container.name);
                ((HasContent) container.object).add(completed.name, completed.object);
            } catch (Exception ex) {
                log.error("Unable to add content", ex);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
    {
        String content = new String(ch, start, length);
        log.debug("Character data at {}:  \"{}\"", where(), content);
        Frame top = handlerStack.peek();
        if (top.object instanceof HasText)
            ((HasText) top.object).append(content);
        else
            log.warn("Character data discarded at {}", where());
    }

    // TODO implement ErrorHandler?  parser.setErrorHandler(this)?

    /**
     * Disclose current location in the input file.
     *
     * @return line/column coordinates.
     */
    private String where()
    {
        if (null == location)
            return "unknown location";
        else
            return "line " + location.getLineNumber() + " column "
                    + location.getColumnNumber();
    }

    /**
     * Object stack frame.
     */
    private class Frame
    {
        private String name;
        private Object object;

        private Frame(String name, Object object)
        {
            this.name = name;
            this.object = object;
        }
    }

    /**
     * Text primitive object.
     */
    private class Text
        implements HasText
    {
        private final StringBuffer value = new StringBuffer(64);

        @Override
        public void append(String more)
        {
            value.append(more);
        }

        @Override
        public String getValue()
        {
            return value.toString();
        }
    }
}
