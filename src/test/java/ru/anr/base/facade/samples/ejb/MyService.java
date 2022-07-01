package ru.anr.base.facade.samples.ejb;

import ru.anr.base.samples.domain.Samples;

import javax.ejb.Local;

/**
 * Samples EJB service. Using it to check database and JTA intergration to work.
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 */
@Local
public interface MyService {
    /**
     * Sample function
     *
     * @param x Some value
     * @return Some object stored in database
     */
    Samples doThat(String x);
}
