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
package ru.anr.base.facade.web.api;

import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.services.serializer.JSONSerializerImpl;
import ru.anr.base.services.serializer.Serializer;

import java.util.Map;

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

    /**
     * Construction of an object
     *
     * @param client RestClient
     */
    public APIClient(RestClient client) {

        this.client = client;
    }

    /**
     * Performs a wrapped API call with logging all http errors.
     *
     * @param callback The callback with request details
     * @param typeDef  An unspecified type (can be a class or a {@link TypeReference}
     *                 object)
     * @param params   Additional parameters
     * @param <S>      Type of object to use
     * @return The response as an object of the required class
     */
    @SuppressWarnings("unchecked")
    protected <S> S api(ApiCallback callback, Object typeDef, Object... params) {

        try {

            ResponseEntity<String> rs = callback.doAPI(params);
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
            return value;
        } catch (HttpStatusCodeException ex) {
            logger.error("Query error: {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        }
    }

    /**
     * A variant of {@link #api(ApiCallback, Class, Object...)} for the case
     * when we deal with TypeRerefence for more complex types.
     *
     * @param callback The callback
     * @param clazz    The type reference class
     * @param params   Parameters
     * @param <S>      The class
     * @return The resulted value
     */
    public <S> S api(ApiCallback callback, TypeReference<S> clazz, Object... params) {

        return api(callback, (Object) clazz, params);
    }

    /**
     * Performs a wrapped API call
     *
     * @param callback The callback with request details
     * @param clazz    The class to use to get response
     * @param params   Additional parameters
     * @param <S>      Type of object to use
     * @return The response as an object of the required class
     */
    public <S> S api(ApiCallback callback, Class<S> clazz, Object... params) {

        return api(callback, (Object) clazz, params);
    }

    /**
     * A POST command for API
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
     * Uploads a file.
     *
     * @param url         The url used for the uploading
     * @param file        A file
     * @param resultModel A model for the result
     * @param props       A key-value pairs for additional properties to put in the same
     *                    form as the file
     * @param <S>         The class
     * @return A response model
     */
    public <S> S apiUpload(String url, Resource file, Class<S> resultModel, Map<String, Object> props) {

        return api(args -> client.upload(url, file, props), resultModel);
    }

    /**
     * Downloads a file
     *
     * @param url The URL of the resource
     * @return An array of bytes
     */
    public byte[] apiDownload(String url) {
        try {
            return client.download(url).getBody();
        } catch (HttpStatusCodeException ex) {
            logger.error("Query error: {}: {}", ex.getStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        }
    }

    /**
     * A POST command for API
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
     * A POST command for API
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
     * A PUT command for API
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
     * A PUT command for API
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
     * A GET command for API (a TypeReference variant)
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
     * A GET command for API
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
     * The DELETE command for API with a model as a simple class.
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
     * The DELETE command for API with the model as a TypeReference object (usually, for lists of objects).
     *
     * @param url     The API url
     * @param typeRef The type reference descirption
     * @param <S>     The class of the result object
     * @return The resulted object
     */
    public <S> S apiDELETE(String url, TypeReference<S> typeRef) {

        return api(args -> client.delete(url), typeRef);
    }


    /**
     * @return Returns the embedded client for extra settings
     */
    public RestClient getClient() {

        return client;
    }
}
