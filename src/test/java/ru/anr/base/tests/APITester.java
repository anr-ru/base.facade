/**
 *
 */
package ru.anr.base.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Assert;
import org.junit.Ignore;
import org.springframework.core.io.Resource;
import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.facade.web.api.APIClient;
import ru.anr.base.facade.web.api.ApiCallback;
import ru.anr.base.facade.web.api.RestClient;

import java.util.Map;

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
        return api(args -> client.post(url, json.toStr(model)), (Class<S>) model.getClass());
    }

    /**
     * A POST command for the API
     *
     * @param client
     *            A rest client
     * @param url
     *            The url of the command
     * @param model
     *            The model to user
     * @return Some resulted value
     *
     * @param <S>
     *            The class value
     */
    public <S> S apiPOST(RestClient client, String url, TypeReference<S> model) {

        Assert.assertNotNull("The model cannot be null", model);
        return api(args -> client.post(url, json.toStr(model)), model);
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

        return api(args -> client.post(url, json.toStr(model)), returnModelClass);
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
        return api(args -> client.put(url, json.toStr(model)), (Class<S>) model.getClass());
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
    public <S> S apiPUT(RestClient client, String url, Object model, Class<S> returnModelClass) {

        Assert.assertNotNull("The model cannot be null", model);
        Assert.assertNotNull("The class cannot be null", returnModelClass);
        return api(args -> client.put(url, json.toStr(model)), returnModelClass);
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
     * A Delete command for API
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
     * A Delete command for API with a type reference object as the result
     *
     * @param client
     *            The REST client
     * @param url
     *            The API url
     * @param typeRef
     *            The class of the resulted model
     * @return The resulted model object
     *
     * @param <S>
     *            The type definition for the model
     */
    public <S> S apiDELETE(RestClient client, String url, TypeReference<S> typeRef) {

        Assert.assertNotNull("The model cannot be null", typeRef);
        return api(args -> client.delete(url), typeRef);
    }


    /**
     * A Delete command for API
     *
     * @param client
     *            The rest client
     * @param url
     *            The url
     * @param requestModel
     *            The request model
     * @param modelClass
     *            The response model class
     *
     * @return The resulted model object
     *
     * @param <S>
     *            The type definition for the model
     */
    public <S> S apiDELETE(RestClient client, String url, Object requestModel, Class<S> modelClass) {

        Assert.assertNotNull("The response class cannot be null", modelClass);
        Assert.assertNotNull("The model cannot be null", requestModel);

        return api(args -> client.delete(url, json.toStr(requestModel)), modelClass);
    }

    /**
     * Uploads a file.
     *
     * @param client
     *            A REST client
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
    public <S> S apiUpload(RestClient client, String url, Resource file, Class<S> resultModel,
                           Map<String, Object> props) {

        return api(args -> client.upload(url, file, props), resultModel);
    }

    /**
     * Downloads a file
     *
     * @param client
     *            A REST Client
     * @param url
     *            The URL
     * @return The resulted array of bytes
     */
    public byte[] apiDownload(RestClient client, String url) {

        return new APIClient(client).apiDownload(url);
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
