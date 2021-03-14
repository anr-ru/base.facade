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
package ru.anr.base.facade.samples.ejb;

import org.springframework.security.authentication.AuthenticationManager;
import ru.anr.base.facade.ejb.AbstractAuthenticationBean;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.PersistenceUnit;
import javax.persistence.PersistenceUnits;

/**
 * Authentication Entry point.
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2014
 */
@Stateless(name = "AuthenticationBean", mappedName = "ejb/authenticationBean")
@Local(AuthenticationManager.class)
@PersistenceUnits({@PersistenceUnit(name = "TestUnit/EntityManagerFactory", unitName = "TestUnit")})
public class AuthenticationBean extends AbstractAuthenticationBean {

}
