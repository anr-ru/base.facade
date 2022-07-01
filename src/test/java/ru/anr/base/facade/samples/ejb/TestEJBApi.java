package ru.anr.base.facade.samples.ejb;

import ru.anr.base.facade.ejb.api.AbstractEJBApiCommandFactory;
import ru.anr.base.services.api.APICommandFactory;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;

/**
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 */
@Stateless(name = "TestEJBApi", mappedName = "ejb/TestEJBApi")
@Local(APICommandFactory.class)
@PersistenceUnits({
        @PersistenceUnit(name = "TestUnit/EntityManagerFactory", unitName = "TestUnit")
})
public class TestEJBApi extends AbstractEJBApiCommandFactory {
    /*
     * Empty. Just configuring EJB annotations (jndi name, test persistent
     * units)
     */
}
