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

import java.net.MalformedURLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

/**
 * Create a java.net.URL entry from the specified {@code value} attribute.
 * <br />
 * {@code <object class='net.wood.jndi.EphemeralContext.propertyEditors.URL'
 *          value='http://www.example.com/foo/bar'/>}
 * @author mhwood
 */
public class URL
implements PropertyEditor
{
    private static final Logger log = LoggerFactory.getLogger(URL.class);

    public Object interpret(String uri, String localName, String qName,
            Attributes attributes)
    {
        String property = attributes.getValue("value");
        java.net.URL result = null;

        if (null == property)
            log.warn("URL with no value attribute");
        else
        {
            try {
                result = new java.net.URL(property);
            } catch (MalformedURLException ex) {
                log.warn("Uninterpretable URL '{}':  {}",
                        property, ex.getMessage());
            }
        }

        log.debug("returning {}", result);
        return result;
    }
}
