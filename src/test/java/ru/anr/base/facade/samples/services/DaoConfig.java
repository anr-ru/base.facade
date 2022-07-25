package ru.anr.base.facade.samples.services;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.anr.base.dao.config.BaseJTADaoConfig;
import ru.anr.base.facade.ejb.TestUnitFactoryBean;

/**
 * Test JPA configuration, which uses HSQL database datasource.
 *
 * @author Alexey Romanchuk
 * @created Nov 6, 2014
 */
@Configuration
@EnableJpaRepositories(
        basePackages = {"ru.anr.base.samples.dao", "ru.anr.base.dao.repository"},
        repositoryFactoryBeanClass = TestUnitFactoryBean.class)
@EnableTransactionManagement
public class DaoConfig extends BaseJTADaoConfig {
}
