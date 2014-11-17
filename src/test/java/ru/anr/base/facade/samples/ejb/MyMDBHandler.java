/**
 * 
 */
package ru.anr.base.facade.samples.ejb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ru.anr.base.facade.ejb.AbstractMessageDrivenHandler;
import ru.anr.base.samples.dao.MyDao;
import ru.anr.base.samples.domain.Samples;

/**
 * Sample implementation of a Message Driven Bean.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 *
 */
@PersistenceUnits({ @PersistenceUnit(name = "TestUnit/EntityManagerFactory", unitName = "TestUnit") })
@MessageDriven(name = "MyMDBHandler", mappedName = "jms/testQueue", activationConfig = { //
@ActivationConfigProperty(propertyName = "endpointExceptionRedeliveryAttempts", propertyValue = "1") })
public class MyMDBHandler extends AbstractMessageDrivenHandler {

    /**
     * A reference to spring DAO bean
     */
    @Autowired
    @Qualifier("mydao")
    private MyDao dao;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMessage(org.springframework.messaging.Message<String> msg) {

        Long id = (Long) msg.getHeaders().get("ObjectID");
        Samples s = dao.findOne(id);

        s.setName(s.getName() + ",xxx");
    }
}
