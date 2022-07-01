package ru.anr.base.facade.samples.ejb;

import org.springframework.jms.core.JmsOperations;
import org.springframework.messaging.support.MessageBuilder;
import ru.anr.base.facade.ejb.AbstractEJBServiceImpl;
import ru.anr.base.samples.dao.MyDao;
import ru.anr.base.samples.domain.Samples;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.jms.Destination;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;

/**
 * An implementation of sample service.
 *
 * @author Alexey Romanchuk
 * @created Nov 11, 2014
 */
@Stateless(name = "MyServiceEJB", mappedName = "ejb/My")
@PersistenceUnits({
        @PersistenceUnit(name = "TestUnit/EntityManagerFactory", unitName = "TestUnit")
})
@Local(MyService.class)
public class MyServiceEJB extends AbstractEJBServiceImpl implements MyService {

    @Override
    public Samples doThat(String x) {

        Samples s = new Samples();
        s.setName(x);

        MyDao dao = bean(MyDao.class);
        s = dao.saveAndFlush(s);

        Destination queue = bean("testQueue", Destination.class);
        JmsOperations jms = bean("jmsTemplate", JmsOperations.class);

        MessageBuilder<String> builder = MessageBuilder.withPayload("HELLO").setHeader("ObjectID", s.getId());
        jms.convertAndSend(queue, builder.build());

        return s;
    }
}
