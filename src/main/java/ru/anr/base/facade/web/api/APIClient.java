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
package ru.anr.base.facade.web.api;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.services.serializer.JSONSerializerImpl;
import ru.anr.base.services.serializer.Serializer;

import java.util.function.Function;

/**
 * A client implementation for accessing REST API functions.
 *
 * @author Aleksey Melkov
 * @created Aug 9, 2016
 */
public class APIClient {

    private static final Logger logger = LoggerFactory.getLogger(APIClient.class);

    protected Serializer json = new JSONSerializerImpl();

    protected RestClient client;

    private boolean testMode = false;

    private HttpHeaders lastHeaders;

    /**
     * Constructs a new object
     *
     * @param client The REST Client
     */
    public APIClient(RestClient client) {
        this.client = client;
    }

    public APIClient(RestClient client, boolean testMode) {
        this.client = client;
        this.testMode = testMode;
    }

    /**
     * A wrapper for executing REST API calls with logging all http errors and applying all
     * object transformations for the request and the result.
     * <p>
     * This function is considered as a common entry point for all possible
     * RESTful operations.
     * </p>
     *
     * @param callback The callback with request details
     * @param typeDef  The unspecified result type (can be a class or a {@link TypeReference} object)
     * @param params   Additional parameters
     * @param <S>      The Type of object to use
     * @return The response as an object of the required class
     */
    @SuppressWarnings("unchecked")
    public <S> S api(Function<Object[], ResponseEntity<String>> callback, Object typeDef, Object... params) {
        try {
            ResponseEntity<String> rs = callback.apply(params);
            logger.debug("Query result: {}", rs.getBody());

            Object type = (typeDef == null) ? ResponseModel.class : typeDef;
            S value;

            if (typeDef instanceof TypeReference<?>) {
                value = json.fromStr(rs.getBody(), (TypeReference<S>) type);
            } else if (String.class.equals(typeDef)) {
                value = (S) rs.getBody();
            } else {
                value = json.fromStr(rs.getBody(), (Class<S>) type);
            }
            this.lastHeaders = rs.getHeaders();
            return value;
        } catch (HttpStatusCodeException ex) {
            logger.debug("Query error: {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            if (testMode) {
                throw new AssertionError(
                        "HTTP Error (" + ex.getStatusCode() + " - " + ex.getStatusText() +
                                ") / " + ex.getResponseBodyAsString());
            } else {
                throw ex;
            }
        }
    }

    /**
     * A variant of {@link #api(Function, Object, Object...)} for the case
     * when we deal with TypeReference explicitly.
     *
     * @param callback The callback
     * @param typeRef  The type reference class
     * @param params   Parameters
     * @param <S>      The class
     * @return The resulted value
     */
    public <S> S api(Function<Object[], ResponseEntity<String>> callback, TypeReference<S> typeRef, Object... params) {
        return api(callback, (Object) typeRef, params);
    }

    /**
     * Performs a wrapped API call for the class case
     *
     * @param callback The callback with request details
     * @param clazz    The class to use to get response
     * @param params   Additional parameters
     * @param <S>      Type of object to use
     * @return The response as an object of the required class
     */
    public <S> S api(Function<Object[], ResponseEntity<String>> callback, Class<S> clazz, Object... params) {
        return api(callback, (Object) clazz, params);
    }

    /**
     * Uploads a file.
     *
     * @param url         The url used for the uploading
     * @param file        The file (can be a file in memory)
     * @param resultModel The model for the result
     * @param props       Key-value pairs to submit with the form.
     * @param <S>         The class
     * @return The response model
     */
    public <S> S apiUPLOAD(String url, Resource file, Class<S> resultModel, Object... props) {
        return api(args -> client.upload(url, file, props), resultModel);
    }

    /**
     * Downloads a file from the given resource URL.
     *
     * @param url The URL of the resource
     * @return The resulted array of bytes
     */
    public byte[] apiDOWNLOAD(String url) {
        try {
            return client.download(url).getBody();
        } catch (HttpStatusCodeException ex) {
            logger.error("Query error: {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        }
    }

    /**
     * A variant of the POST API command with an entity model and the result of
     * the same type.
     *
     * @param url   The url
     * @param model The model to use
     * @param <S>   The type definition for the model
     * @return The resulted model object
     */
    @SuppressWarnings("unchecked")
    public <S> S apiPOST(String url, S model) {
        return api(args -> client.post(url, json.toStr(model)), (Class<S>) model.getClass());
    }

    /**
     * A variant of the POST API command with an entity model and the given
     * result class.
     *
     * @param url              The url
     * @param model            The model to use
     * @param returnModelClass The class of the resulted model
     * @param <S>              The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiPOST(String url, Object model, Class<S> returnModelClass) {
        return api(args -> client.post(url, json.toStr(model)), returnModelClass);
    }

    /**
     * A variant of the POST API command with an entity model and the given
     * result {@link TypeReference}.
     *
     * @param url     The url
     * @param model   The model to use
     * @param typeRef The {@link TypeReference} object
     * @param <S>     The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiPOST(String url, Object model, TypeReference<S> typeRef) {
        return api(args -> client.post(url, json.toStr(model)), typeRef);
    }

    /**
     * A variant of the PUT API command with an entity model and the same result
     * type.
     *
     * @param url   The url
     * @param model The model to use
     * @param <S>   The type definition for the model
     * @return The resulted model object
     */
    @SuppressWarnings("unchecked")
    public <S> S apiPUT(String url, S model) {
        return api(args -> client.put(url, json.toStr(model)), (Class<S>) model.getClass());
    }

    /**
     * A variant of the PUT API command with an entity model and the given
     * result class.
     *
     * @param url              The url
     * @param model            The model to use
     * @param returnModelClass The class of the resulted model
     * @param <S>              The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiPUT(String url, Object model, Class<S> returnModelClass) {
        return api(args -> client.put(url, json.toStr(model)), returnModelClass);
    }

    /**
     * A variant of the PUT API command with an entity model and the given
     * result {@link TypeReference}
     *
     * @param url        The url
     * @param model      The model to use
     * @param returnType The type of the resulted model
     * @param <S>        The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiPUT(String url, Object model, TypeReference<S> returnType) {
        return api(args -> client.put(url, json.toStr(model)), returnType);
    }


    /**
     * A variant of the PATCH API command with an entity model and the same result
     * type.
     *
     * @param url   The url
     * @param model The model to use
     * @param <S>   The type definition for the model
     * @return The resulted model object
     */
    @SuppressWarnings("unchecked")
    public <S> S apiPATCH(String url, S model) {
        return api(args -> client.patch(url, json.toStr(model)), (Class<S>) model.getClass());
    }

    /**
     * A variant of the PATCH API command with an entity model and the given
     * result class.
     *
     * @param url              The url
     * @param model            The model to use
     * @param returnModelClass The class of the resulted model
     * @param <S>              The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiPATCH(String url, Object model, Class<S> returnModelClass) {
        return api(args -> client.patch(url, json.toStr(model)), returnModelClass);
    }

    /**
     * A variant of the PUT API command with an entity model and the given
     * result {@link TypeReference}
     *
     * @param url        The url
     * @param model      The model to use
     * @param returnType The type of the resulted model
     * @param <S>        The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiPATCH(String url, Object model, TypeReference<S> returnType) {
        return api(args -> client.patch(url, json.toStr(model)), returnType);
    }

    /**
     * A variant of the GET API command with the given result {@link TypeReference}
     *
     * @param url     The url
     * @param typeRef The {@link TypeReference} object
     * @param <S>     The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiGET(String url, TypeReference<S> typeRef) {
        return api(args -> client.get(url), typeRef);
    }

    /**
     * A variant of the GET API command with the given result class.
     *
     * @param url        The url
     * @param modelClass The class of the resulted model
     * @param <S>        The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiGET(String url, Class<S> modelClass) {
        return api(args -> client.get(url), modelClass);
    }

    /**
     * A variant of the DELETE API command with the given result class.
     *
     * @param url        The url
     * @param modelClass The class of the resulted model
     * @param <S>        The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiDELETE(String url, Class<S> modelClass) {
        return api(args -> client.delete(url), modelClass);
    }

    /**
     * The DELETE command for API with the model as a TypeReference object
     * (usually, for lists of objects).
     *
     * @param url     The API url
     * @param typeRef The type reference description
     * @param <S>     The class of the result object
     * @return The resulted object
     */
    public <S> S apiDELETE(String url, TypeReference<S> typeRef) {
        return api(args -> client.delete(url), typeRef);
    }

    /**
     * @return Returns the embedded REST client for extra settings
     */
    public RestClient getClient() {
        return client;
    }

    public HttpHeaders getLastHeaders() {
        return lastHeaders;
    }
}
