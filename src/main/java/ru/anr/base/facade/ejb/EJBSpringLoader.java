/**
 * 
 */
package ru.anr.base.facade.ejb;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Signleton EJB, which loads spring context via {@link SpringEJBInterceptor}
 * interceptor.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 *
 */
@Singleton
@Startup
@Interceptors(SpringEJBInterceptor.class)
public class EJBSpringLoader {

    /**
     * Logger
     */
    private static final Logger logger = LoggerFactory.getLogger(EJBSpringLoader.class);

    /**
     * Reference to holder to avoid class unloading.
     */
    private final EJBContextHolder holder = new EJBContextHolder();

    /**
     * An attempt to inject context
     */
    @Autowired
    private ApplicationContext ctx;

    /**
     * Initialization
     */
    @PostConstruct
    public void init() {

        logger.info("Holder: {}, context loaded: {}", holder, ctx);
    }
}
