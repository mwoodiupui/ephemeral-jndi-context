/*
 * Copyright 2012 Mark H. Wood
 */

package net.wood.jndi.EphemeralContext.propertyEditors;

/**
 * Signifies an object which can have non-character content.
 *
 * @author mhwood
 */
public interface HasContent
{
    /**
     * Append an object to this object's non-character content.
     *
     * @param o New content for this object.
     */
    public void add(String name, Object o) throws Exception;
}
