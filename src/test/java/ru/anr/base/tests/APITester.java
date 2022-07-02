/*
 * Copyright 2014-2022 the original author or authors.
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
package ru.anr.base.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.springframework.core.io.Resource;
import ru.anr.base.BaseParent;
import ru.anr.base.facade.web.api.APIClient;
import ru.anr.base.facade.web.api.RestClient;

/**
 * A class to facilitate some testing procedures for REST-based APIs.
 *
 * @author Alexey Romanchuk
 * @created May 20, 2016
 */
@Disabled
public class APITester extends BaseParent {

    /**
     * A variant of POST command for API when an entity to submit is provided
     * with the same result type.
     *
     * @param client The rest client
     * @param url    The url
     * @param model  The model to use
     * @param <S>    The type definition for the model
     * @return The resulted model object
     */
    public static <S> S apiPOST(RestClient client, String url, S model) {
        Assertions.assertNotNull(model, "The model cannot be null");
        return new APIClient(client).apiPOST(url, model);
    }

    /**
     * A variant of POST command for API when a {@link TypeReference} result
     * is provided.
     *
     * @param client The REST client
     * @param url    The url of the command
     * @param model  The model to user
     * @param <S>    The class value
     * @return Some resulted value
     */
    public static <S> S apiPOST(RestClient client, String url, Object model, TypeReference<S> result) {
        Assertions.assertNotNull(model, "The model cannot be null");
        return new APIClient(client).apiPOST(url, model, result);
    }

    /**
     * A variant of POST command for API when a class result is provided.
     *
     * @param client           The rest client
     * @param url              The url
     * @param model            The model to use
     * @param returnModelClass The class of the resulted model
     * @param <S>              The type definition for the model
     * @return The resulted model object
     */
    public static <S> S apiPOST(RestClient client, String url, Object model, Class<S> returnModelClass) {
        Assertions.assertNotNull(model, "The model cannot be null");
        Assertions.assertNotNull(returnModelClass, "The class cannot be null");
        return new APIClient(client).apiPOST(url, model, returnModelClass);
    }

    /**
     * A variant of PUT command for API when an entity to submit is provided
     * with the same result type.
     *
     * @param client The rest client
     * @param url    The url
     * @param model  The model to use
     * @param <S>    The type definition for the model
     * @return The resulted model object
     */
    public static <S> S apiPUT(RestClient client, String url, S model) {
        Assertions.assertNotNull(model, "The model class cannot be null");
        return new APIClient(client).apiPUT(url, model);
    }

    /**
     * A variant of PUT command for API when an entity to submit is provided
     * with another class as the result type.
     *
     * @param client           The rest client
     * @param url              The url
     * @param model            The model to use
     * @param returnModelClass The class of the resulted model
     * @param <S>              The type definition for the model
     * @return The resulted model object
     */
    public static <S> S apiPUT(RestClient client, String url, Object model, Class<S> returnModelClass) {
        Assertions.assertNotNull(model, "The model cannot be null");
        Assertions.assertNotNull(returnModelClass, "The class cannot be null");
        return new APIClient(client).apiPUT(url, model, returnModelClass);
    }

    /**
     * A variant of the PUT method with a complex {@link TypeReference}
     * return type.
     *
     * @param client           The REST client
     * @param url              The API url
     * @param model            The request model
     * @param returnModelClass The response model
     * @param <S>              The type model
     * @return The resulted value
     */
    public static <S> S apiPUT(RestClient client, String url, Object model, TypeReference<S> returnModelClass) {
        Assertions.assertNotNull(model, "The model cannot be null");
        Assertions.assertNotNull(returnModelClass, "The class cannot be null");
        return new APIClient(client).apiPUT(url, model, returnModelClass);
    }

    /**
     * A variant of PATCH command for API when an entity to submit is provided
     * with the same result type.
     *
     * @param client The rest client
     * @param url    The url
     * @param model  The model to use
     * @param <S>    The type definition for the model
     * @return The resulted model object
     */
    public static <S> S apiPATCH(RestClient client, String url, S model) {
        Assertions.assertNotNull(model, "The model class cannot be null");
        return new APIClient(client).apiPATCH(url, model);
    }

    /**
     * A variant of PATCH command for API when an entity to submit is provided
     * with another class as the result type.
     *
     * @param client           The rest client
     * @param url              The url
     * @param model            The model to use
     * @param returnModelClass The class of the resulted model
     * @param <S>              The type definition for the model
     * @return The resulted model object
     */
    public static <S> S apiPATCH(RestClient client, String url, Object model, Class<S> returnModelClass) {
        Assertions.assertNotNull(model, "The model cannot be null");
        Assertions.assertNotNull(returnModelClass, "The class cannot be null");
        return new APIClient(client).apiPATCH(url, model, returnModelClass);
    }

    /**
     * A variant of the PATCH method with a complex {@link TypeReference}
     * return type.
     *
     * @param client     The REST client
     * @param url        The API url
     * @param model      The request model
     * @param returnType The response model
     * @param <S>        The type model
     * @return The resulted value
     */
    public static <S> S apiPATCH(RestClient client, String url, Object model, TypeReference<S> returnType) {
        Assertions.assertNotNull(model, "The model cannot be null");
        Assertions.assertNotNull(returnType, "The class cannot be null");
        return new APIClient(client).apiPATCH(url, model, returnType);
    }


    /**
     * A variant of the GET command for API with a {@link TypeReference} as the
     * result type.
     *
     * @param client  The rest client
     * @param url     The url
     * @param typeRef The {@link TypeReference} object
     * @param <S>     The type definition for the model
     * @return The resulted model object
     */
    public static <S> S apiGET(RestClient client, String url, TypeReference<S> typeRef) {
        Assertions.assertNotNull(typeRef, "The model class cannot be null");
        return new APIClient(client).apiGET(url, typeRef);
    }

    /**
     * A variant of the GET command for API with the given class as the result type.
     *
     * @param client     The rest client
     * @param url        The url
     * @param modelClass The class of the resulted model
     * @param <S>        The type definition for the model
     * @return The resulted model object
     */
    public static <S> S apiGET(RestClient client, String url, Class<S> modelClass) {
        Assertions.assertNotNull(modelClass, "The model cannot be null");
        return new APIClient(client).apiGET(url, modelClass);
    }

    /**
     * A variant of the DELETE API command with the given class as the result
     * type.
     *
     * @param client     The REST client
     * @param url        The url
     * @param modelClass The class of the resulted model
     * @param <S>        The type definition for the model
     * @return The resulted model object
     */
    public static <S> S apiDELETE(RestClient client, String url, Class<S> modelClass) {
        Assertions.assertNotNull(modelClass, "The model cannot be null");
        return new APIClient(client).apiDELETE(url, modelClass);
    }

    /**
     * A variant of the DELETE API command with the {@link TypeReference}
     * result type.
     *
     * @param client  The REST client
     * @param url     The API url
     * @param typeRef The class of the resulted model
     * @param <S>     The type definition for the model
     * @return The resulted model object
     */
    public static <S> S apiDELETE(RestClient client, String url, TypeReference<S> typeRef) {
        Assertions.assertNotNull(typeRef, "The model cannot be null");
        return new APIClient(client).apiDELETE(url, typeRef);
    }

    /**
     * A variant of the DELETE command when we need to send an entity and expect the
     * given result type.
     *
     * @param client       The rest client
     * @param url          The url
     * @param requestModel The request model
     * @param modelClass   The response model class
     * @param <S>          The type definition for the model
     * @return The resulted model object
     */
    public static <S> S apiDELETE(RestClient client, String url, Object requestModel, Class<S> modelClass) {
        Assertions.assertNotNull(modelClass, "The response class cannot be null");
        Assertions.assertNotNull(requestModel, "The model cannot be null");
        return new APIClient(client).apiDELETE(url, requestModel, modelClass);
    }

    /**
     * Uploads the given resource (from a file or in memory).
     *
     * @param client      A REST client
     * @param url         The url used for the uploading
     * @param file        A file
     * @param resultModel A model for the result
     * @param props       A key-value pairs for additional properties to put in the same
     *                    form as the file
     * @param <S>         The class
     * @return A response model
     */
    public static <S> S apiUPLOAD(RestClient client, String url, Resource file, Class<S> resultModel,
                                  Object... props) {
        Assertions.assertNotNull(file, "Resource not defined");
        Assertions.assertNotNull(resultModel, "Result mode is not defined");
        return new APIClient(client).apiUPLOAD(url, file, resultModel, props);
    }

    /**
     * Downloads a file
     *
     * @param client A REST Client
     * @param url    The URL
     * @return The resulted array of bytes
     */
    public static byte[] apiDOWNLOAD(RestClient client, String url) {
        return new APIClient(client).apiDOWNLOAD(url);
    }
}
