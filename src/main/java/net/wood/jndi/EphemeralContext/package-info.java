/*
 * Copyright 2011, 2012 Mark H. Wood
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
 *      {@link net.wood.jndi.EphemeralContext.propertyEditors.PropertyEditor}.
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
 * objects is supplied.  See the {@link net.wood.jndi.EphemeralContext.propertyEditors.PropertyEditor}
 * interface if you need to implement other types.</p>
 */

package net.wood.jndi.EphemeralContext;
