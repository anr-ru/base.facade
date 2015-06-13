/**
 * 
 */
package ru.anr.base.facade.samples.ejb;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;

import ru.anr.base.facade.ejb.EJBSpringLoader;

/**
 * Description ...
 *
 *
 * @author Alexey Romanchuk
 * @created Jun 11, 2015
 *
 */
@Startup
@Singleton
@PersistenceUnits({ @PersistenceUnit(name = "TestUnit/EntityManagerFactory", unitName = "TestUnit") })
public class StartUpEJB extends EJBSpringLoader {

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {

        addQueue("queue", "KEY", "BODY1", "HH", "VALUE");
        addQueue("queue", "KEY", "BODY2");

        super.init();
    }
}
