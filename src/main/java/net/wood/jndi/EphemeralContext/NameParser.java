/*
 * Copyright 2011 Mark H. Wood
 */

package net.wood.jndi.EphemeralContext;

import java.util.Properties;
import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NamingException;

/**
 *
 * @author mhwood
 */
class NameParser implements javax.naming.NameParser
{
    private static final Properties syntax = new Properties();
    static {
        syntax.put("jndi.syntax.direction", "left_to_right");
	syntax.put("jndi.syntax.separator", "/");
        syntax.put("jndi.syntax.ignorecase", "false");
	syntax.put("jndi.syntax.escape", "\\");
	syntax.put("jndi.syntax.beginquote", "'");
        syntax.put("jndi.syntax.beginquote2", "\"");
    }

    public NameParser()
    {
    }

    public Name parse(String name) throws NamingException
    {
        return new CompoundName(name, syntax);
    }
}
