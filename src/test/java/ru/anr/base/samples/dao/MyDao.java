package ru.anr.base.samples.dao;

import org.springframework.stereotype.Repository;
import ru.anr.base.dao.repository.BaseRepository;
import ru.anr.base.samples.domain.Samples;

/**
 * A sample DAO.
 *
 * @author Alexey Romanchuk
 * @created Nov 12, 2014
 */
@Repository
public interface MyDao extends BaseRepository<Samples> {

}
