package ru.anr.base.facade.samples.ejb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.anr.base.facade.ejb.mdb.AbstractMessageDrivenHandler;
import ru.anr.base.samples.dao.MyDao;
import ru.anr.base.samples.domain.Samples;

import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;

/**
 * Sample implementation of a Message Driven Bean.
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 */
@PersistenceUnits({@PersistenceUnit(name = "TestUnit/EntityManagerFactory", unitName = "TestUnit")})
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
        Samples s = dao.find(Samples.class, id);

        s.setName(s.getName() + ",xxx");
    }
}
