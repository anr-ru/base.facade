/*
 * Copyright 2014-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package ru.anr.base.facade.ejb;

import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.authentication.BadCredentialsException;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;
import javax.transaction.Transactional;

/**
 * The abstract parent for all EJBs used here. It uses the container-based
 * transaction management.
 * <p>
 * Spring beans can be pulled on its descendants from the injected
 * spring context (see {@link EJBContextHolder}).
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 */
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
@Transactional(dontRollbackOn = {NotFoundException.class, BadCredentialsException.class})
@Interceptors({ExceptionHandlerInterceptor.class})
public abstract class AbstractEJBServiceImpl extends EJBContextHolder {

}
