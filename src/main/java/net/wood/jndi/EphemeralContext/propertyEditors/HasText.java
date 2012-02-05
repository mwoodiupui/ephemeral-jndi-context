/*
 * Copyright 2012 Mark H. Wood
 */

package net.wood.jndi.EphemeralContext.propertyEditors;

/**
 * Signifies an object which can have character content.
 *
 * @author mhwood
 */
public interface HasText
{
    /**
     * Append a value to the character content.
     *
     * @param what
     */
    public void append(String what);

    /**
     * Get the accumulated character content.
     *
     * @return concatenation of all Strings passed to append().
     */
    public String getValue();
}
