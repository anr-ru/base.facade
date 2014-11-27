/**
 * 
 */
package ru.anr.base.facade.samples.ejb;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.Destination;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsOperations;
import org.springframework.messaging.support.MessageBuilder;

import ru.anr.base.facade.ejb.AbstractEJBServiceImpl;
import ru.anr.base.samples.dao.MyDao;
import ru.anr.base.samples.domain.Samples;

/**
 * Implementation of sample service.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 *
 */
@Stateless(name = "MyServiceEJB", mappedName = "ejb/My")
@PersistenceUnits({ @PersistenceUnit(name = "TestUnit/EntityManagerFactory", unitName = "TestUnit") })
@Local(MyService.class)
public class MyServiceEJB extends AbstractEJBServiceImpl implements MyService {

    /**
     * Reference to a spring jms bean
     */
    @Autowired
    @Qualifier("jmsTemplate")
    private JmsOperations jms;

    /**
     * Destination
     */
    @Autowired
    @Qualifier("queue")
    private Destination queue;

    /**
     * Reference to a dao
     */
    @Autowired
    @Qualifier("mydao")
    private MyDao dao;

    /**
     * {@inheritDoc}
     */
    @Override
    public Samples doThat(String x) {

        Samples s = new Samples();
        s.setName(x);

        s = dao.saveAndFlush(s);

        MessageBuilder<String> builder = MessageBuilder.withPayload("HELLO").setHeader("ObjectID", s.getId());
        jms.convertAndSend(queue, builder.build());

        return s;
    }
}
