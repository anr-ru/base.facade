/**
 * 
 */
package ru.anr.base.facade.samples.services;

/**
 * Description ...
 *
 *
 * @author Alexey Romanchuk
 * @created Mar 27, 2017
 *
 */

public interface SampleService {

    /**
     * Do work with specifying exceptions to roll back
     */
    void doWorkNoRollback();

    /**
     * Do work without specifying exceptions to roll back
     */
    void doWork();
}
