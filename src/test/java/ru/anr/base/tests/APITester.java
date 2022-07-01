package ru.anr.base.tests;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import ru.anr.base.domain.api.models.ResponseModel;
import ru.anr.base.facade.web.api.APIClient;
import ru.anr.base.facade.web.api.RestClient;

import java.util.Map;
import java.util.function.Function;

/**
 * A class to facilitate some testing procedures for a REST-based API.
 *
 * @author Alexey Romanchuk
 * @created May 20, 2016
 */
@Disabled
public class APITester extends APIClient {

    /**
     * The parent test case
     */
    private final BaseTestCase testcase;

    /**
     * Construction of an object
     *
     * @param parent A parent test case
     */
    public APITester(BaseTestCase parent) {

        super(null);
        this.testcase = parent;
    }

    /**
     * A POST command for API
     *
     * @param client The rest client
     * @param url    The url
     * @param model  The model to use
     * @param <S>    The type definition for the model
     * @return The resulted model object
     */
    @SuppressWarnings("unchecked")
    public <S> S apiPOST(RestClient client, String url, S model) {

        Assertions.assertNotNull(model, "The model cannot be null");
        return api(args -> client.post(url, json.toStr(model)), (Class<S>) model.getClass());
    }

    /**
     * A POST command for the API
     *
     * @param client A rest client
     * @param url    The url of the command
     * @param model  The model to user
     * @param <S>    The class value
     * @return Some resulted value
     */
    public <S> S apiPOST(RestClient client, String url, TypeReference<S> model) {

        Assertions.assertNotNull(model, "The model cannot be null");
        return api(args -> client.post(url, json.toStr(model)), model);
    }

    /**
     * A POST command for API
     *
     * @param client           The rest client
     * @param url              The url
     * @param model            The model to use
     * @param returnModelClass The class of the resulted model
     * @param <S>              The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiPOST(RestClient client, String url, Object model, Class<S> returnModelClass) {

        Assertions.assertNotNull(model, "The model cannot be null");
        Assertions.assertNotNull(returnModelClass, "The class cannot be null");

        return api(args -> client.post(url, json.toStr(model)), returnModelClass);
    }

    /**
     * A PUT command for API
     *
     * @param client The rest client
     * @param url    The url
     * @param model  The model to use
     * @param <S>    The type definition for the model
     * @return The resulted model object
     */
    @SuppressWarnings("unchecked")
    public <S> S apiPUT(RestClient client, String url, S model) {

        Assertions.assertNotNull(model, "The model class cannot be null");
        return api(args -> client.put(url, json.toStr(model)), (Class<S>) model.getClass());
    }

    /**
     * A PUT command for API
     *
     * @param client           The rest client
     * @param url              The url
     * @param model            The model to use
     * @param returnModelClass The class of the resulted model
     * @param <S>              The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiPUT(RestClient client, String url, Object model, Class<S> returnModelClass) {

        Assertions.assertNotNull(model, "The model cannot be null");
        Assertions.assertNotNull(returnModelClass, "The class cannot be null");
        return api(args -> client.put(url, json.toStr(model)), returnModelClass);
    }

    /**
     * A variant of the PUT method with a complex return type
     *
     * @param client           The REST client
     * @param url              The API url
     * @param model            The request model
     * @param returnModelClass The response model
     * @param <S>              The type model
     * @return The resulted value
     */
    public <S> S apiPUT(RestClient client, String url, Object model, TypeReference<S> returnModelClass) {

        Assertions.assertNotNull(model, "The model cannot be null");
        Assertions.assertNotNull(returnModelClass, "The class cannot be null");
        return api(args -> client.put(url, json.toStr(model)), returnModelClass);
    }

    /**
     * A GET command for API (a TypeReference variant)
     *
     * @param client  The rest client
     * @param url     The url
     * @param typeRef The {@link TypeReference} object
     * @param <S>     The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiGET(RestClient client, String url, TypeReference<S> typeRef) {

        Assertions.assertNotNull(typeRef, "The model class cannot be null");
        return api(args -> client.get(url), typeRef);
    }

    /**
     * A GET command for API
     *
     * @param client     The rest client
     * @param url        The url
     * @param modelClass The class of the resulted model
     * @param <S>        The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiGET(RestClient client, String url, Class<S> modelClass) {

        Assertions.assertNotNull(modelClass, "The model cannot be null");
        return api(args -> client.get(url), modelClass);
    }

    /**
     * A Delete command for API
     *
     * @param client     The rest client
     * @param url        The url
     * @param modelClass The class of the resulted model
     * @param <S>        The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiDELETE(RestClient client, String url, Class<S> modelClass) {

        Assertions.assertNotNull(modelClass, "The model cannot be null");
        return api(args -> client.delete(url), modelClass);
    }

    /**
     * A Delete command for API with a type reference object as the result
     *
     * @param client  The REST client
     * @param url     The API url
     * @param typeRef The class of the resulted model
     * @param <S>     The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiDELETE(RestClient client, String url, TypeReference<S> typeRef) {

        Assertions.assertNotNull(typeRef, "The model cannot be null");
        return api(args -> client.delete(url), typeRef);
    }

    /**
     * A Delete command for API
     *
     * @param client       The rest client
     * @param url          The url
     * @param requestModel The request model
     * @param modelClass   The response model class
     * @param <S>          The type definition for the model
     * @return The resulted model object
     */
    public <S> S apiDELETE(RestClient client, String url, Object requestModel, Class<S> modelClass) {

        Assertions.assertNotNull(modelClass, "The response class cannot be null");
        Assertions.assertNotNull(requestModel, "The model cannot be null");

        return api(args -> client.delete(url, json.toStr(requestModel)), modelClass);
    }

    /**
     * Uploads a file.
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
    public <S> S apiUpload(RestClient client, String url, Resource file, Class<S> resultModel,
                           Map<String, Object> props) {

        return api(args -> client.upload(url, file, props), resultModel);
    }

    /**
     * Downloads a file
     *
     * @param client A REST Client
     * @param url    The URL
     * @return The resulted array of bytes
     */
    public byte[] apiDownload(RestClient client, String url) {

        return new APIClient(client).apiDownload(url);
    }

    /**
     * Asserts that the specified API query definitely generates an exception
     * with the message
     *
     * @param callback    The API callback
     * @param expectedMsg The expected message
     * @param params      Parameters to use
     */
    public void assertAPIException(Function<Object[], ResponseEntity<String>> callback, String expectedMsg, Object... params) {
        testcase.assertException(args -> api(callback, ResponseModel.class, params), expectedMsg, params);
    }
}
