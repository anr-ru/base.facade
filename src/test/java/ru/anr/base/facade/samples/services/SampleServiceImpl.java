/**
 * 
 */
package ru.anr.base.facade.samples.services;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.anr.base.services.BaseDataAwareServiceImpl;

/**
 * Description ...
 *
 *
 * @author Alexey Romanchuk
 * @created Mar 27, 2017
 *
 */
@Component
public class SampleServiceImpl extends BaseDataAwareServiceImpl implements SampleService {

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void doWork() {

        throw new BadCredentialsException("Errors");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(noRollbackFor = { BadCredentialsException.class })
    public void doWorkNoRollback() {

        throw new BadCredentialsException("Errors");
    }
}
