package ru.anr.base.facade.samples.ejb;

import ru.anr.base.facade.ejb.mdb.AbstractMessageDrivenHandler;
import ru.anr.base.samples.dao.MyDao;
import ru.anr.base.samples.domain.Samples;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;

/**
 * Sample implementation of a Message Driven Bean.
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 */
@PersistenceUnits({
        @PersistenceUnit(name = "TestUnit/EntityManagerFactory", unitName = "TestUnit")
})
@MessageDriven(name = "MyMDBHandler", mappedName = "jms/testQueue", activationConfig = {
        @ActivationConfigProperty(propertyName = "endpointExceptionRedeliveryAttempts", propertyValue = "1")
})
public class MyMDBHandler extends AbstractMessageDrivenHandler {

    @Override
    protected void onMessage(org.springframework.messaging.Message<String> msg) {

        MyDao dao = bean(MyDao.class);

        Long id = (Long) msg.getHeaders().get("ObjectID");
        Samples s = dao.find(Samples.class, id);

        s.setName(s.getName() + ",xxx");
    }
}
