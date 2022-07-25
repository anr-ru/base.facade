package ru.anr.base.facade.ejb;

import ru.anr.base.dao.config.repository.MultiUnitRepositoryFactoryBean;
import ru.anr.base.dao.repository.BaseRepository;
import ru.anr.base.domain.BaseEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * APIUnit initialization.
 *
 * @param <T> Type of base entity
 * @author Alexey Romanchuk
 * @created Feb 2, 2019
 */
public class TestUnitFactoryBean<T extends BaseEntity> extends MultiUnitRepositoryFactoryBean<T> {

    public TestUnitFactoryBean(Class<? extends BaseRepository<T>> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    @PersistenceContext(unitName = "TestUnit")
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }
}
