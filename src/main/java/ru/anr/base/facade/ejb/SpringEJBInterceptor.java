/**
 * 
 */
package ru.anr.base.facade.ejb;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

/**
 * This interceptor performs initialial context loading from resource with
 * specific predifined name.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 *
 */

public class SpringEJBInterceptor extends SpringBeanAutowiringInterceptor {

    /**
     * {@inheritDoc}
     */
    @Override
    protected BeanFactory getBeanFactory(Object target) {

        if (EJBContextHolder.getCtx() == null) { // lazy loading
            /*
             * TODO: is there any way to specify this path smarter?
             */
            EJBContextHolder.loadContext("classpath:/ejb-context.xml");
        }
        return EJBContextHolder.getCtx().getAutowireCapableBeanFactory();
    }
}
