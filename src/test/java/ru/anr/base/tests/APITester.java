/**
 * 
 */
package ru.anr.base.tests;

import org.junit.Assert;
import org.junit.Ignore;

import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.facade.web.api.APIClient;
import ru.anr.base.facade.web.api.ApiCallback;
import ru.anr.base.facade.web.api.RestClient;

import com.fasterxml.jackson.core.type.TypeReference;

/**
 * A class to facilitate some testing procedures for a REST-based API.
 *
 *
 * @author Alexey Romanchuk
 * @created May 20, 2016
 *
 */
@Ignore
public class APITester extends APIClient {

    /**
     * The parent test case
     */
    private BaseTestCase testcase;

    /**
     * Construction of an object
     * 
     * @param parent
     *            A parent test case
     */
    public APITester(BaseTestCase parent) {

        super(null);
        this.testcase = parent;
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

        testcase.assertException(args -> {
            api(callback, ResponseModel.class, params);
        }, expectedMsg, params);
    }
}
