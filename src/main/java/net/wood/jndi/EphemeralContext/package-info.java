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
/**
 * <p>A simple(?) JNDI Context implementation with no backing store.  Initial
 * content may be loaded at instantiation from an external file, but this
 * content and any changes made afterward are discarded when the instance is
 * finalized.</p>
 * 
 * <p>This was built to provide a lightweight naming context requiring minimal
 * external support.  It was inspired by difficulties with JNDI-dependent
 * application code which sometimes runs in a servlet container and sometimes
 * standalone, and thus needed some way to provide naming outside of the
 * container.  Now this can be done by specifying
 * "-Djava.naming.factory.initial=net.wood.jndi.EphemeralContext.ContextFactory"
 * at runtime.</p>
 * 
 * <p>Initial content is described by a classpath resource which is named by
 * the PROVIDER_URL.  This can be specified with -Djava.naming.provider.url=/some/path
 * or in a jndi.properties.</p>
 * 
 * <p>The initial content is an XML document composed of the following elements.
 *  <dl>
 *   <dt>initialContext</dt>
 *   <dd>The root element of the document.</dd>
 * 
 *   <dt>context</dt>
 *   <dd>A named container of other elements.  The {@code name} attribute is required.</dd>
 * 
 *   <dt>text</dt>
 *   <dd>A String-valued leaf object.  This is currently the only primitive type.
 *      The {@code name} attribute is required.</dd>
 * 
 *   <dt>object</dt>
 *   <dd>An arbitrary object, to be constructed by a named
 *      {@link net.wood.jndi.EphemeralContext.objectProviders.PropertyEditor}.
 *      Attributes:
 *      <dl>
 *          <dt>name</dt><dd>name of the object in the directory
 *          <dt>class</dt><dd>fully-qualified name of the needed {@code PropertyEditor} class.</dd>
 *      </dl>
 *   </dd>
 *  </dl>
 * </p>
 * 
 * <p>A {@code PropertyEditor} implementation for PostgreSQL {@code DataSource}
 * objects is supplied.  See the {@link net.wood.jndi.EphemeralContext.objectProviders.PostgreSQLDataSource}
 * interface if you need to implement other types.</p>
 */

package net.wood.jndi.EphemeralContext;
