/*
 * Copyright 2011 Mark H. Wood
 */

package net.wood.jndi.EphemeralContext;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;
import javax.naming.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A JNDI context built in memory.  It does not represent a live naming service.
 * 
 * Of note:  {@code java.naming.provider.url} is not interpreted as a URL by this
 * provider; instead it names a resource looked up on the classpath and used to
 * provide initial content.
 *
 * @author mhwood
 */
class Context implements javax.naming.Context, InitialContentHandler.HasContent
{
    private final HashMap<String, Context> subContexts = new HashMap<String, Context>();

    private final HashMap<String, Object> leaves = new HashMap<String, Object>();

    private final Hashtable environment;

    private final Context parent;

    protected static final NameParser myParser = new NameParser();

    private  final String myName;

    private static final String SEPARATOR = "/";

    private static final Logger log = LoggerFactory.getLogger(Context.class);

    private Context()
    {
        environment = new Hashtable();
        parent = null;
        myName = null;
    }

    /**
     * Create a functional Context.
     * 
     * @param environment
     * @param parent Context which contains this one.
     * @param myName Name of this Context.  Not null.
     * @param load Load initial content from an external file?
     */
    protected Context(Hashtable environment, Context parent,
            String myName)
    {
        if (null == environment)
            this.environment = new Hashtable();
        else
            this.environment = (Hashtable) environment.clone();

        this.parent = parent;

        this.myName = myName;
    }

    public Object lookup(Name name) throws NamingException
    {
        if (null == name)
            throw new NamingException("null name");

        if (name.isEmpty())
            return new Context(environment, parent, myName);

        // If a supporteded URL, trim off the scheme
        String myPart = name.get(0);
        if (myPart.startsWith("java:"))
        {
            myPart = myPart.substring(5);
            // Some people think "java:/comp" is correct, which leaves us with
            // an empty myPart here.  Compensate by ignoring the now empty prefix.
            if (myPart.isEmpty())
            {
                log.warn("Use 'java:comp/', not 'java:/comp/'!  " +
                        "See http://docs.oracle.com/javaee/1.4/tutorial/doc/Resources2.html");
                return lookup(name.getSuffix(1));
            }
        }

        // Simple?
        if (name.size() == 1)
        {
            log.debug("Simple name:  {}", name);
            if (leaves.containsKey(myPart))
                return leaves.get(myPart);
            else if (subContexts.containsKey(myPart))
                return subContexts.get(myPart);
            else
                throw new NameNotFoundException(myPart);
        }
        else
        {
            log.debug("Multipart name {} -- myPart:  {}", name, myPart);
            if (subContexts.containsKey(myPart))
            {
                Object result = subContexts.get(myPart).lookup(name.getSuffix(1));
                // TODO if (result instanceof(Reference) blah blah
                return result;
            }
            else
                throw new NameNotFoundException(myPart);
        }
    }

    public Object lookup(String name) throws NamingException
    {
        return lookup(new CompositeName(name));
    }

    public void bind(Name name, Object o) throws NamingException
    {
        if (null == name)
            throw new NamingException("null name");

        if (name.isEmpty())
            throw new NamingException("name is empty");

        String myPart = name.get(0);
        if (name.size() == 1)
        {
            if (!leaves.containsKey(myPart))
                leaves.put(myPart, o);
            else
                throw new NamingException("already bound:  " + myPart);
        }
        else
        {
            throw new NamingException("multipart names not supported");
            // TODO what about binding multipart names?
            //if (subContexts.containsKey(elements[0]))
            //    subContexts.get(elements[0]).bind(elements[1], o);
            //else
            //    throw new NamingException("not found:  " + elements[0]);
        }
    }

    public void bind(String name, Object o) throws NamingException
    {
        bind(new CompositeName(name), o);
    }

    public void rebind(Name name, Object o) throws NamingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void rebind(String name, Object o) throws NamingException
    {
        rebind(new CompositeName(name), o);
    }

    public void unbind(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void unbind(String name) throws NamingException
    {
        unbind(new CompositeName(name));
    }

    public void rename(Name name, Name name1) throws NamingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void rename(String name, String name1) throws NamingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NamingEnumeration<NameClassPair> list(String name) throws NamingException
    {
        return list(new CompositeName(name));
    }

    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public NamingEnumeration<Binding> listBindings(String name) throws NamingException
    {
        return listBindings(new CompositeName(name));
    }

    public void destroySubcontext(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void destroySubcontext(String name) throws NamingException
    {
        destroySubcontext(new CompositeName(name));
    }

    public Context createSubcontext(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Context createSubcontext(String name) throws NamingException
    {
        if (null == name)
            throw new NamingException("null name");

        // TODO handle URLs too
        String[] elements = name.split("/", 2);
        if (elements.length == 1)
        {
            if (!subContexts.containsKey(name))
            {
                Context nc = new Context(environment, this, name);
                subContexts.put(name, nc);
                return nc;
            }
            else
                throw new NamingException("context exists:  " + name);
        }
        else
        {
            if (!subContexts.containsKey(elements[0]))
                return subContexts.get(elements[0]).createSubcontext(elements[1]);
            else
                throw new NamingException("not found:  " + elements[0]);
        }
    }

    public Object lookupLink(Name name) throws NamingException
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object lookupLink(String name) throws NamingException
    {
        return lookupLink(new CompositeName(name));
    }

    public NameParser getNameParser(Name name) throws NamingException
    {
        // TODO check name?  Does this need to hand off to federated providers?
        return myParser;
    }

    public NameParser getNameParser(String name) throws NamingException
    {
        return getNameParser(new CompositeName(name));
    }

    public Name composeName(Name name, Name prefix) throws NamingException
    {
        if (null == name || null == prefix)
            throw new NamingException("Name components may not be null");

        // TODO prefix might not be in this namespace
        return new CompositeName().addAll(prefix).addAll(name);
    }

    public String composeName(String name, String prefix) throws NamingException
    {
        if (null == name || null == prefix)
            throw new NamingException("Name components may not be null");

        // TODO prefix might not be in this namespace
        return name + SEPARATOR + prefix;
    }

    public Object addToEnvironment(String key, Object value) throws NamingException
    {
        Object oldValue = environment.get(key);
        environment.put(key, value);
        return oldValue;
    }

    public Object removeFromEnvironment(String key) throws NamingException
    {
        return environment.remove(key);
    }

    public Hashtable getEnvironment() throws NamingException
    {
        return (Hashtable) environment.clone();
    }

    public void close() throws NamingException
    {
        // Nothing to do
    }

    public String getNameInNamespace() throws NamingException
    {
        if (null == parent)
            return "";
        
        return composeName(myName, parent.getNameInNamespace());
    }

    public void add(String name, Object o)
            throws Exception
    {
        if (!(o instanceof Context))
        {
            try {
                bind(name, o);
            } catch (NamingException ex) {
                log.error("Unable to bind {}", ex);
            }
        }
    }
}
