/**
 *
 */
package ru.anr.base.facade.samples.ejb;

import ru.anr.base.facade.ejb.health.HealthCheck;
import ru.anr.base.facade.ejb.health.HealthCheckEJBImpl;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;

/**
 * Implementation of sample service.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 *
 */
@Stateless(name = "HealthBean", mappedName = "ejb/HealthBean")
@PersistenceUnits({@PersistenceUnit(name = "TestUnit/EntityManagerFactory", unitName = "TestUnit")})
@Local(HealthCheck.class)
public class SampleHealthEJB extends HealthCheckEJBImpl {

}
