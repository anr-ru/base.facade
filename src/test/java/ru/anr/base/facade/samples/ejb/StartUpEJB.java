package ru.anr.base.facade.samples.ejb;

import ru.anr.base.facade.ejb.EJBStartUpLoader;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;

/**
 * Description ...
 *
 * @author Alexey Romanchuk
 * @created Jun 11, 2015
 */
@Startup
@Singleton
@PersistenceUnits({
        @PersistenceUnit(name = "TestUnit/EntityManagerFactory", unitName = "TestUnit")
})
public class StartUpEJB extends EJBStartUpLoader {

    @Override
    @PostConstruct
    public void init() {

        addQueue("queue", "KEY", "BODY1", "HH", "VALUE");
        addQueue("queue", "KEY", "BODY2");

        super.init();
    }
}
