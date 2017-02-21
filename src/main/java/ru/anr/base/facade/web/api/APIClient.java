/**
 * 
 */
package ru.anr.base.facade.web.api;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.core.type.TypeReference;

import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.services.serializer.JSONSerializerImpl;
import ru.anr.base.services.serializer.Serializer;

/**
 * A class to some procedures for a REST-based API.
 *
 * @author Aleksey Melkov
 * @created Aug 9, 2016
 *
 */
public class APIClient {

    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(APIClient.class);

    /**
     * {@link Serializer}
     */
    protected Serializer json = new JSONSerializerImpl();

    /**
     * The parent test case
     */
    protected RestClient client;

    /**
     * Construction of an object
     * 
     * @param client
     *            RestClient
     */
    public APIClient(RestClient client) {

        this.client = client;
    }

    /**
     * Performs a wrapped API call
     * 
     * @param callback
     *            The callback with request details
     * @param typeDef
     *            An unspecified type (can be a class or a {@link TypeReference}
     *            object)
     * @param params
     *            Additional parameters
     * @return The response as an object of the required class
     * @param <S>
     *            Type of object to use
     */
    @SuppressWarnings("unchecked")
    protected <S> S api(ApiCallback callback, Object typeDef, Object... params) {

        try {

            ResponseEntity<String> rs = callback.doAPI(params);
            logger.debug("Query result: {}", rs.getBody());

            Object type = (typeDef == null) ? ResponseModel.class : typeDef;
            S value = null;

            if (typeDef instanceof TypeReference<?>) {
                value = json.fromStr(rs.getBody(), (TypeReference<S>) type);
            } else if (String.class.equals(typeDef)) {
                value = (S) rs.getBody();
            } else {
                value = json.fromStr(rs.getBody(), (Class<S>) type);
            }
            return value;

        } catch (HttpClientErrorException e1) {
            logger.error("Query client error: {}: {}", e1.getStatusCode(), e1.getResponseBodyAsString());
            throw e1;
        } catch (HttpServerErrorException e2) {
            logger.error("Query server error: {}: {}", e2.getStatusCode(), e2.getResponseBodyAsString());
            throw e2;
        }
    }

    /**
     * The variation of {@link #api(ApiCallback, Class, Object...)} for the case
     * when we deal with TypeRerefence for more complex types.s
     * 
     * @param callback
     *            The callback
     * @param clazz
     *            The typeref class
     * @param params
     *            Parameters
     * @return The resulted value
     * 
     * @param <S>
     *            The class
     */
    protected <S> S api(ApiCallback callback, TypeReference<S> clazz, Object... params) {

        return api(callback, (Object) clazz, params);
    }

    /**
     * Performs a wrapped API call
     * 
     * @param callback
     *            The callback with request details
     * @param clazz
     *            The class to use to get response
     * @param params
     *            Additional parameters
     * @return The response as an object of the required class
     * @param <S>
     *            Type of object to use
     */
    protected <S> S api(ApiCallback callback, Class<S> clazz, Object... params) {

        return api(callback, (Object) clazz, params);
    }

    /**
     * A POST command for API
     * 
     * @param url
     *            The url
     * @param model
     *            The model to use
     * @return The resulted model object
     * 
     * @param <S>
     *            The type definition for the model
     */
    @SuppressWarnings("unchecked")
    public <S> S apiPOST(String url, S model) {

        return api(args -> {
            return client.post(url, json.toStr(model));
        }, (Class<S>) model.getClass());
    }

    /**
     * Uploads a file.
     * 
     * @param url
     *            The url used for the uploading
     * @param file
     *            A file
     * @param resultModel
     *            A model for the result
     * @param props
     *            A key-value pairs for additional properties to put in the same
     *            form as the file
     * @return A response model
     * @param <S>
     *            The class
     */
    public <S> S apiUpload(String url, Resource file, Class<S> resultModel, Map<String, Object> props) {

        return api(args -> client.upload(url, file, props), resultModel);
    }

    /**
     * Downloads a file
     * 
     * @param url
     *            The URL of the resource
     * @return An array of bytes
     */
    public byte[] apiDownload(String url) {

        try {
            return client.download(url).getBody();
        } catch (HttpClientErrorException e1) {
            logger.error("Query client error: {}: {}", e1.getStatusCode(), e1.getResponseBodyAsString());
            throw e1;
        } catch (HttpServerErrorException e2) {
            logger.error("Query server error: {}: {}", e2.getStatusCode(), e2.getResponseBodyAsString());
            throw e2;
        }
    }

    /**
     * A POST command for API
     * 
     * @param url
     *            The url
     * @param model
     *            The model to use
     * @param returnModelClass
     *            The class of the resulted model
     * @return The resulted model object
     * @param <S>
     *            The type definition for the model
     */
    public <S> S apiPOST(String url, Object model, Class<S> returnModelClass) {

        return api(args -> {
            return client.post(url, json.toStr(model));
        }, returnModelClass);
    }

    /**
     * A PUT command for API
     * 
     * @param url
     *            The url
     * @param model
     *            The model to use
     * @return The resulted model object
     * 
     * @param <S>
     *            The type definition for the model
     */
    @SuppressWarnings("unchecked")
    public <S> S apiPUT(String url, S model) {

        return api(args -> {
            return client.put(url, json.toStr(model));
        }, (Class<S>) model.getClass());
    }

    /**
     * A PUT command for API
     * 
     * @param url
     *            The url
     * @param model
     *            The model to use
     * @param returnModelClass
     *            The class of the resulted model
     * @return The resulted model object
     * 
     * @param <S>
     *            The type definition for the model
     */
    @SuppressWarnings("unchecked")
    public <S> S apiPUT(String url, Object model, Class<S> returnModelClass) {

        return api(args -> {
            return client.put(url, json.toStr(model));
        }, (Class<S>) model.getClass());
    }

    /**
     * A GET command for API (a TypeReference variant)
     * 
     * @param url
     *            The url
     * @param typeRef
     *            The {@link TypeReference} object
     * @return The resulted model object
     * 
     * @param <S>
     *            The type definition for the model
     */
    public <S> S apiGET(String url, TypeReference<S> typeRef) {

        return api(args -> client.get(url), typeRef);
    }

    /**
     * A GET command for API
     * 
     * @param url
     *            The url
     * @param modelClass
     *            The class of the resulted model
     * @return The resulted model object
     * 
     * @param <S>
     *            The type definition for the model
     */
    public <S> S apiGET(String url, Class<S> modelClass) {

        return api(args -> client.get(url), modelClass);
    }

    /**
     * A GET command for API
     * 
     * @param url
     *            The url
     * @param modelClass
     *            The class of the resulted model
     * @return The resulted model object
     * 
     * @param <S>
     *            The type definition for the model
     */
    public <S> S apiDELETE(String url, Class<S> modelClass) {

        return api(args -> client.delete(url), modelClass);
    }
}
