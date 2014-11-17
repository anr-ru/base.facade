/**
 * 
 */
package ru.anr.base.facade.ejb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ru.anr.base.BaseParent;

/**
 * A holder of Spring context (static reference). The idea is that @Startup EJB
 * loads necessary context and then stores it inside this class.
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 *
 */
public class EJBContextHolder extends BaseParent {

    /**
     * A reference to Spring context
     */
    private static ApplicationContext ctx;

    // ////////////////////////// Accessing beans /////////////////////////////

    /**
     * Loading a context from specified path. User synchronized market to
     * guarantee one-per-classloader instance.
     * 
     * @param contextPath
     *            Location of spring
     */
    public static synchronized void loadContext(String contextPath) {

        if (ctx == null) {
            /*
             * TODO: is there any way to configure this path smarter?
             */
            AbstractApplicationContext context = new ClassPathXmlApplicationContext(contextPath);
            context.registerShutdownHook();

            setCtx(context); // Storing in a holder forever
        }
    }

    /**
     * Getting bean from context (a short-cut)
     * 
     * @param name
     *            Name of bean
     * @param clazz
     *            Bean class
     * @return Bean instance
     * 
     * @param <S>
     *            Type of bean
     */
    protected static <S> S bean(String name, Class<S> clazz) {

        return ctx.getBean(name, clazz);
    }

    /**
     * Getting bean from context (a short-cut)
     * 
     * @param name
     *            Name of bean
     * @return Bean instance
     * 
     * @param <S>
     *            Type of bean
     */
    @SuppressWarnings("unchecked")
    protected static <S> S bean(String name) {

        return (S) ctx.getBean(name);
    }

    // /////////////////////////////////////////////////////////////////////////
    // /// getters/setters
    // /////////////////////////////////////////////////////////////////////////

    /**
     * Setting a spring contexts
     * 
     * @param applicationContext
     *            the applicationContext to set
     */
    public static void setCtx(ApplicationContext applicationContext) {

        ctx = applicationContext;
    }

    /**
     * @return the ctx
     */
    public static ApplicationContext getCtx() {

        return ctx;
    }
}
