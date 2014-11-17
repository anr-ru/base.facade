/**
 * 
 */
package ru.anr.base.facade.ejb;

import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

/**
 * An abstract parent for all EJBs used here. Specified CONTAINER
 * transactionality has been added just for understanding what we really relay
 * on.
 *
 * Also added an interceptor, which gives an ability to inject spring beans
 * directly as EJB class fields (with
 * {@link org.springframework.beans.factory.annotation.Autowired} annotation).
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 *
 */
@TransactionManagement(TransactionManagementType.CONTAINER)
@Interceptors(SpringEJBInterceptor.class)
public abstract class AbstractEJBServiceImpl extends EJBContextHolder {

    /**
     * Empty
     */
}
