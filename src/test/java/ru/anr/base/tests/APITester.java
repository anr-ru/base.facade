/**
 * 
 */
package ru.anr.base.tests;

import org.junit.Assert;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.core.type.TypeReference;

import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.facade.web.api.RestClient;
import ru.anr.base.services.serializer.JSONSerializerImpl;
import ru.anr.base.services.serializer.Serializer;

/**
 * A class to facilitate some testing procedures for a REST-based API.
 *
 *
 * @author Alexey Romanchuk
 * @created May 20, 2016
 *
 */
@Ignore
public class APITester extends BaseTestCase {

    /**
     * The logger
     */
    private static final Logger logger = LoggerFactory.getLogger(APITester.class);

    /**
     * {@link Serializer}
     */
    private Serializer json = new JSONSerializerImpl();

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
    private <S> S api(ApiCallback callback, Object typeDef, Object... params) {

        try {

            ResponseEntity<String> rs = callback.doAPI(params);
            logger.info("Query result: {}", rs.getBody());

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
    private <S> S api(ApiCallback callback, TypeReference<S> clazz, Object... params) {

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
    private <S> S api(ApiCallback callback, Class<S> clazz, Object... params) {

        return api(callback, (Object) clazz, params);
    }

    /**
     * A POST command for API
     * 
     * @param client
     *            The rest client
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
    public <S> S apiPOST(RestClient client, String url, S model) {

        Assert.assertNotNull("The model cannot be null", model);
        return api(args -> {
            return client.post(url, json.toStr(model));
        }, (Class<S>) model.getClass());
    }

    /**
     * A POST command for API
     * 
     * @param client
     *            The rest client
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
    public <S> S apiPOST(RestClient client, String url, Object model, Class<S> returnModelClass) {

        Assert.assertNotNull("The model cannot be null", model);
        Assert.assertNotNull("The class cannot be null", returnModelClass);

        return api(args -> {
            return client.post(url, json.toStr(model));
        }, returnModelClass);
    }

    /**
     * A PUT command for API
     * 
     * @param client
     *            The rest client
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
    public <S> S apiPUT(RestClient client, String url, S model) {

        Assert.assertNotNull("The model class cannot be null", model);
        return api(args -> {
            return client.put(url, json.toStr(model));
        }, (Class<S>) model.getClass());
    }

    /**
     * A PUT command for API
     * 
     * @param client
     *            The rest client
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
    public <S> S apiPUT(RestClient client, String url, Object model, Class<S> returnModelClass) {

        Assert.assertNotNull("The model cannot be null", model);
        Assert.assertNotNull("The class cannot be null", returnModelClass);
        return api(args -> {
            return client.put(url, json.toStr(model));
        }, (Class<S>) model.getClass());
    }

    /**
     * A GET command for API (a TypeReference variant)
     * 
     * @param client
     *            The rest client
     * @param url
     *            The url
     * @param typeRef
     *            The {@link TypeReference} object
     * @return The resulted model object
     * 
     * @param <S>
     *            The type definition for the model
     */
    public <S> S apiGET(RestClient client, String url, TypeReference<S> typeRef) {

        Assert.assertNotNull("The model class cannot be null", typeRef);
        return api(args -> client.get(url), typeRef);
    }

    /**
     * A GET command for API
     * 
     * @param client
     *            The rest client
     * @param url
     *            The url
     * @param modelClass
     *            The class of the resulted model
     * @return The resulted model object
     * 
     * @param <S>
     *            The type definition for the model
     */
    public <S> S apiGET(RestClient client, String url, Class<S> modelClass) {

        Assert.assertNotNull("The model cannot be null", modelClass);
        return api(args -> client.get(url), modelClass);
    }

    /**
     * A GET command for API
     * 
     * @param client
     *            The rest client
     * @param url
     *            The url
     * @param modelClass
     *            The class of the resulted model
     * @return The resulted model object
     * 
     * @param <S>
     *            The type definition for the model
     */
    public <S> S apiDELETE(RestClient client, String url, Class<S> modelClass) {

        Assert.assertNotNull("The model cannot be null", modelClass);
        return api(args -> client.delete(url), modelClass);
    }

    /**
     * Asserts that the specified API query definitely generates an exception
     * with the message
     * 
     * @param callback
     *            The API callback
     * @param expectedMsg
     *            The expected message
     * @param params
     *            Parameters to use
     */
    public void assertAPIException(ApiCallback callback, String expectedMsg, Object... params) {

        assertException(args -> {
            api(callback, ResponseModel.class, params);
        }, expectedMsg, params);
    }
}
