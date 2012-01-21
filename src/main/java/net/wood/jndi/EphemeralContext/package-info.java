/*
 * Copyright 2011 Mark H. Wood
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
 * or in jndi.properties.  TODO is this true?</p>
 * 
 * <p>TODO describe the configuration schema</p>
 * 
 * <p>String-valued objects are directly supported.  Support for arbitrary
 * complex objects may be added by providing "PropertyEditor" implementations
 * which interpret the attributes of an {@code object} element and optionally
 * supply custom SAX {@code Handler}s for any content.  The {@code PropertyEditor}
 * is specified by the {@code class} attribute of the {@code object} element.</p>
 */

package net.wood.jndi.EphemeralContext;
