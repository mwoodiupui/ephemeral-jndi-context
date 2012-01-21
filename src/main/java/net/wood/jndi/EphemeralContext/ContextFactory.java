/*
 * Copyright 2011 Mark H. Wood
 */
package net.wood.jndi.EphemeralContext;

import java.util.Hashtable;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * Simple, hierarchial context built in memory.
 *
 * @author mhwood
 */
public class ContextFactory
        implements InitialContextFactory
{
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
        return new Context(environment, null, null, true);
    }
}
