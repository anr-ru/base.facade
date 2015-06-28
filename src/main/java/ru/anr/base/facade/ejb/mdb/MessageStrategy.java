/*
 * Copyright 2014 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.anr.base.facade.ejb.mdb;

import java.util.Map;

import org.springframework.messaging.Message;

import ru.anr.base.ApplicationException;
import ru.anr.base.BaseParent;
import ru.anr.base.dao.BaseRepositoryImpl;
import ru.anr.base.dao.repository.BaseRepository;
import ru.anr.base.domain.BaseEntity;
import ru.anr.base.services.pattern.Strategy;
import ru.anr.base.services.pattern.StrategyConfig;
import ru.anr.base.services.pattern.StrategyConfig.StrategyModes;

/**
 * Strategy for message processing.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 21, 2014
 *
 */

public interface MessageStrategy extends Strategy<Message<String>> {

    /**
     * The name for the header with object identifier information
     */
    String OBJECT_ID = "OBJECT_ID";

    /**
     * The name for the header with object class information
     */
    String OBJECT_CLASS = "OBJECT_CLASS";

    /**
     * Finds an object using the provided headers inside of a message
     * 
     * @param msg
     *            The message
     * @param dao
     *            Some {@link BaseRepository}
     * @return The object or null if not found
     * 
     * @param <S>
     *            The object class
     */
    default <S extends BaseEntity> S extractObject(Message<String> msg, BaseRepository<?> dao) {

        try {
            Class<?> clazz = Class.forName(msg.getHeaders().get(OBJECT_CLASS).toString());
            Long id = Long.valueOf(msg.getHeaders().get(OBJECT_ID).toString());

            return dao.find(clazz, id);
        } catch (ClassNotFoundException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Implementation for the case when a strategy is defined by a value of some
     * header
     * 
     * @param m
     *            A message which header is analyzed
     * @param header
     *            The name of the header
     * @param controlValue
     *            A control value to compare with
     * @return {@link StrategyConfig}
     */
    default StrategyConfig headerCheck(Message<String> m, String header, String controlValue) {

        String value = header(m, header, String.class);
        return new StrategyConfig(BaseParent.safeEquals(controlValue, value), m, StrategyModes.TerminateAfter);
    }

    /**
     * Builds the headers required for storing information about an entity
     * 
     * @param o
     *            The entity
     * @return A map which contains the headers
     */
    default Map<String, Object> toHeaders(BaseEntity o) {

        return BaseParent.toMap(OBJECT_ID, o.getId(), OBJECT_CLASS, BaseRepositoryImpl.entityClass(o).getName());
    }

    /**
     * Returns a value of message header which is specified as the parameter
     * 'name'.
     * 
     * @param m
     *            The message
     * @param name
     *            The name of the header
     * @param clazz
     *            The value of the header
     * @return The found header value
     * 
     * @param <S>
     *            Type of the header value
     */
    default <S> S header(Message<String> m, String name, Class<S> clazz) {

        return m.getHeaders().get(name, clazz);
    }
}
