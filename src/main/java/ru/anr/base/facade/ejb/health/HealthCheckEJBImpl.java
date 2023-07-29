/*
 * Copyright 2014-2023 the original author or authors.
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
package ru.anr.base.facade.ejb.health;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.anr.base.facade.ejb.AbstractEJBServiceImpl;

import javax.ejb.Schedule;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * A EJB implementation of {@link HealthCheck} service. To include this service
 * you need to create a new EJB based on this class.
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2016
 */
public class HealthCheckEJBImpl extends AbstractEJBServiceImpl implements HealthCheck {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckEJBImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String check(boolean fail) {
        return HealthCheckUtils.checkWork(fail, getCtx());
    }

    /**
     * Checks the state every 10 minutes and reports to the log file.
     */
    @Schedule(hour = "*", minute = "*/10", second = "0", persistent = false)
    public void onSchedule() {

        ZonedDateTime start = ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(Long.parseLong(this.check(false))), DEFAULT_TIMEZONE);
        ZonedDateTime now = now();

        long hours = ChronoUnit.HOURS.between(start, now);
        long minutes = ChronoUnit.MINUTES.between(start, now);
        long seconds = ChronoUnit.SECONDS.between(start, now);

        seconds = seconds - 60 * minutes;
        minutes = minutes - 60 * hours;

        logger.info("Health check, up time {}", String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
}
