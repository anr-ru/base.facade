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
package ru.anr.base.facade.ejb;

import javax.ejb.Local;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

/**
 * Abstract authentication manager EJB.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 28, 2014
 *
 */
@Local(AuthenticationManager.class)
public class AbstractAuthenticationBean extends AbstractEJBServiceImpl implements AuthenticationManager {

    /**
     * A reference to a current {@link AuthenticationManager}
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public Authentication authenticate(Authentication authentication) {

        return authenticationManager.authenticate(authentication);
    }
}
