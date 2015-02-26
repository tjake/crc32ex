package com.github.tjake;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.BlockingQueue;

/**
 * Creates a different crc32 depending on JDK version.
 *
 * You can specify a crc32 implementation for JDK != 8
 */
public class CRC32Factory
{
    private static final boolean JDK8;

    public static final CRC32Factory defaultFactory = new CRC32Factory(PureJavaCrc32.class);

    static
    {
        boolean jdk8 = false;
        ClassLoader cl = null;
        try
        {
            if (System.getSecurityManager() == null)
            {
                cl = BlockingQueue.class.getClassLoader();
            } else
            {
                cl = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
                {
                    @Override
                    public ClassLoader run()
                    {
                        return BlockingQueue.class.getClassLoader();
                    }
                });
            }

            Class.forName("java.util.concurrent.CompletableFuture", false, cl);

            jdk8 = true;
        } catch (Exception e)
        {
        }
        JDK8 = jdk8;
    }

    private final Class<? extends ICRC32> jdk7class;

    public CRC32Factory(Class<? extends ICRC32> jdk7class)
    {
        assert jdk7class != null;
        this.jdk7class = jdk7class;
    }

    public ICRC32 create()
    {
        if (JDK8)
        {
            return new CRC32Ex();
        }
        else
        {
            try
            {
                return jdk7class.newInstance();
            }
            catch (InstantiationException e)
            {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
