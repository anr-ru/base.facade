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

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import ru.anr.base.ApplicationException;
import ru.anr.base.BaseParent;
import ru.anr.base.UriUtils;

import javax.net.ssl.SSLContext;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

/**
 * Some lite configured REST client.
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 */
public class RestClient extends BaseParent {

    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    /**
     * A reference to Spring {@link RestTemplate} engine
     */
    private RestTemplate rest;

    /**
     * Default port
     */
    private int port = 8080;

    /**
     * Default host
     */
    private String host = "localhost";

    /**
     * Http schema (http, https)
     */
    private String schema = "http";

    /**
     * 'Content-Type' header value (what we send)
     */
    private MediaType contentType = MediaType.APPLICATION_JSON;

    /**
     * 'Accept' header value (what we expect to receive)
     */
    private MediaType accept = MediaType.APPLICATION_JSON;

    /**
     * Remove all cookie values (remove the session)
     */
    public void clearCookies() {
        store.clear();
    }

    /**
     * Default constructor
     */
    public RestClient() {
        this("http");
    }

    /**
     * Constructor with the schema
     *
     * @param schema The schema (http or https)
     */
    public RestClient(String schema) {
        super();
        setSchema(schema);
        rest = initRest(new RestTemplate());
    }

    /**
     * Additional headers to pass
     */
    private Map<String, String> headers = toMap();

    /**
     * Constructor with a schema, host and a port
     *
     * @param schema  A schema (http, https)
     * @param host    A host
     * @param port    A port
     * @param headers Additional headers to pass
     */
    public RestClient(String schema, String host, int port, String... headers) {

        super();
        setSchema(schema);
        setHost(host);
        setPort(port);

        this.headers = toMap(headers);
        rest = initRest(new RestTemplate());
    }

    /**
     * Adds a new headers to the headers map
     *
     * @param name  The name of the header
     * @param value The value
     */
    public void addHeader(String name, String value) {
        this.headers.put(name, value);
    }

    /**
     * A constructor with a OAuth2 resource
     *
     * @param resource The OAuth2 resource
     */
    public RestClient(OAuth2ProtectedResourceDetails resource) {
        super();
        rest = initRest(new OAuth2RestTemplate(resource));
    }

    /**
     * A constructor with a predefined REST template
     *
     * @param template The template to use
     */
    public RestClient(RestTemplate template) {
        super();
        rest = template;
    }

    /**
     * Builds the base url string (server location), excluding a printing of
     * standard http ports.
     *
     * @return The resulted string with the server location with the schema,
     * host and port
     */
    public String getBaseUrl() {
        return UriUtils.getBaseUrl(schema, host, port);
    }

    /**
     * Retrieves the final URI of http resource
     *
     * @param path The relative path (e.g. '/ping') or the full path
     *             like 'http://localhost:9090/ping'
     * @return The resulted full path to http resource (included schema, host, port,
     * relative path)
     */
    public String getUri(String path) {
        return UriUtils.getUri(schema, host, port, path);
    }

    /**
     * The cookie storage
     */
    private final CookieStore store = new BasicCookieStore();

    /**
     * A special initialization of {@link RestTemplate} that is used to apply some
     * settings for existing RestTemplates.
     *
     * @param template {@link RestTemplate} or its
     * @return Updated RestTemplate
     */
    public RestTemplate initRest(RestTemplate template) {

        // 1. Set up ssl settings
        HttpClient client = "https".equals(schema) ? buildSSLClient() : buildClient();

        template.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client) {
            @Override
            protected void postProcessHttpRequest(HttpUriRequest request) {
                super.postProcessHttpRequest(request);
            }
        });

        // 2. Error handler
        template.setErrorHandler(new DefaultResponseErrorHandler());
        template.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));

        for (HttpMessageConverter<?> converter : template.getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }
        return template;
    }

    /**
     * Getting {@link RestOperations} and its descendants
     *
     * @param <S> Class of {@link RestOperations}
     * @return A rest template
     */
    @SuppressWarnings("unchecked")
    public <S extends RestOperations> S ops() {
        return (S) rest;
    }

    /**
     * Builds an Apache http client to support untrusted ssl connections. This
     * can be useful for test purposes.
     *
     * @return The resulted Apache {@link HttpClient}
     */
    private HttpClient buildSSLClient() {

        TrustStrategy acceptingTrustStrategy = (certificate, authType) -> true;

        try {
            SSLContextBuilder builder = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy);
            SSLContext ssl = builder.setProtocol("TLS").build();

            SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(ssl, new NoopHostnameVerifier());
            return HttpClients.custom().setDefaultCookieStore(store).setSSLSocketFactory(sf).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    private HttpClient buildClient() {
        return HttpClients.custom().setDefaultCookieStore(store).build();
    }

    /**
     * The Basic authorization header
     */
    private String basicCredentials;

    /**
     * The OAuth2 Authorization header
     */
    private String oauth2Credentials;

    /**
     * Builds the Basic Authorization header
     *
     * @param user     A user
     * @param password A password
     */
    public void setBasicCredentials(String user, String password) {
        String s = user + ":" + password;
        this.basicCredentials = "Basic " + utf8(Base64.getEncoder().encode(utf8(s)));
    }

    /**
     * Cleaning up the Basic Authorization header
     */
    public void cleanBasicCredentials() {
        this.basicCredentials = null;
    }

    /**
     * Cleaning up the OAuth2
     */
    public void cleanOAuth2() {
        this.oauth2Credentials = null;
    }

    /**
     * Builds all defined headers
     *
     * @return {@link HttpHeaders} object
     */
    protected HttpHeaders applyHeaders() {

        HttpHeaders hh = new HttpHeaders();
        if (contentType != null) {
            hh.setContentType(contentType);
        }
        if (accept != null) {
            hh.setAccept(list(accept));
            hh.setAcceptCharset(list(StandardCharsets.UTF_8));
        }

        if (basicCredentials != null) {
            hh.add("Authorization", basicCredentials);
        }
        if (oauth2Credentials != null) {
            hh.add("Authorization", "Bearer " + oauth2Credentials);
        }
        if (headers != null) {
            headers.forEach(hh::add);
        }
        return hh;
    }

    /**
     * The POST method.
     *
     * @param path The relative or the absolute path
     * @param body The request body (as expected by {@link #setContentType(MediaType)})
     * @return The response body
     */
    public ResponseEntity<String> post(String path, String body) {
        return doExchange(path, HttpMethod.POST, body, String.class);
    }

    /**
     * The POST method for form data with empty expected result
     *
     * @param path     The path
     * @param formData Form parameters
     * @return The http status for response
     */
    public ResponseEntity<Void> post(String path, MultiValueMap<String, String> formData) {
        return post(path, formData, Void.class);
    }

    /**
     * The POST method for form data with expected result.
     *
     * @param path     The path
     * @param formData Form parameters
     * @param clazz    The class of the result
     * @param <S>      The type of the result
     * @return The response
     */
    public <S> ResponseEntity<S> post(String path, MultiValueMap<String, String> formData, Class<S> clazz) {
        setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return doExchange(path, HttpMethod.POST, formData, clazz);
    }

    /**
     * The PUT method.
     *
     * @param path The relative or the absolute path
     * @param body Request body (as expected by {@link #setContentType(MediaType)})
     * @return The response
     */
    public ResponseEntity<String> put(String path, String body) {
        return doExchange(path, HttpMethod.PUT, body, String.class);
    }

    /**
     * The PATCH method.
     *
     * @param path The relative or absolute path
     * @param body The request body (as expected by {@link #setContentType(MediaType)})
     * @return The response
     */
    public ResponseEntity<String> patch(String path, String body) {
        return doExchange(path, HttpMethod.PATCH, body, String.class);
    }


    /**
     * The DELETE method.
     *
     * @param path The relative or the absolute path
     * @return The response
     */
    public ResponseEntity<String> delete(String path) {
        return doExchange(path, HttpMethod.DELETE, (String) null, String.class);
    }

    /**
     * The GET method.
     *
     * @param path         The relative or the absolute path
     * @param uriVariables The request variables
     * @return The response
     */
    public ResponseEntity<String> get(String path, Object... uriVariables) {
        return doExchange(path, HttpMethod.GET, (String) null, String.class, uriVariables);
    }

    /**
     * Uploads the given resource (a file or data in memory)
     *
     * @param path     The REST path
     * @param resource Some resource
     * @param props    Additional properties to pass as form values
     * @return The response
     */
    public ResponseEntity<String> upload(String path, Resource resource, Map<String, Object> props) {

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", resource);

        if (props != null) {
            props.forEach((k, v) -> map.add(nullSafe(k), nullSafe(v)));
        }

        HttpHeaders hh = applyHeaders();
        hh.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> e = new HttpEntity<>(map, hh);
        return exchange(path, HttpMethod.POST, e, String.class);
    }

    /**
     * Uploads the given resource (a file or data in memory) when the parameters are given
     * as pairs name/value.
     *
     * @param path     The REST path
     * @param resource Some resource
     * @param props    Additional properties to pass as form values
     * @return The response
     */
    public ResponseEntity<String> upload(String path, Resource resource, Object... props) {
        return upload(path, resource, toMap(props));
    }

    /**
     * Downloads the file bytes from the given resource path.
     *
     * @param path The REST path
     * @return The resulted array of bytes
     */
    public ResponseEntity<byte[]> download(String path) {
        HttpHeaders hh = applyHeaders();
        return exchange(path, HttpMethod.GET, new HttpEntity<>(hh), byte[].class);
    }

    /**
     * A common function for all HTTP commands.
     *
     * @param path         The relative or absolute path
     * @param method       The http method to use
     * @param body         The request body (for PUT/POST/PATCH)
     * @param uriVariables URL params (part of url in GET queries)
     * @param clazz        The response entity class
     * @param <T>          The response entity type
     * @param <S>          The request entity type
     * @return Response
     */
    public <S, T> ResponseEntity<T> doExchange(String path, HttpMethod method, S body, Class<T> clazz,
                                               Object... uriVariables) {
        HttpHeaders hh = applyHeaders();
        return exchange(path, method, new HttpEntity<S>(body, hh), clazz, uriVariables);
    }

    /**
     * A low-level http operation
     *
     * @param path         The path
     * @param method       The http method
     * @param entity       The entity
     * @param clazz        The response class
     * @param uriVariables The set of url parameters
     * @param <S>          The type of the response
     * @param <T>          The type of the request entity
     * @return The response entity
     */
    public <S, T> ResponseEntity<T> exchange(String path, HttpMethod method, HttpEntity<S> entity, Class<T> clazz,
                                             Object... uriVariables) {

        logger.debug("Http body: {}", entity);
        ResponseEntity<T> rs = rest.exchange(getUri(path), method, entity, clazz, uriVariables);

        if (logger.isTraceEnabled()) {
            logger.trace("Cookie: {}", store.getCookies());
        }
        logger.debug("Http response: {}", rs);
        return rs;
    }

    ///////////////////////////////////////////////////////////////////////////
    ///// getters/setters
    ///////////////////////////////////////////////////////////////////////////

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * @return the schema
     */
    public String getSchema() {
        return schema;
    }

    /**
     * @param schema the schema to set
     */
    public void setSchema(String schema) {
        this.schema = schema;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the contentType
     */
    public MediaType getContentType() {
        return contentType;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setContentType(MediaType contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the 'accept' header
     */
    public MediaType getAccept() {

        return accept;
    }

    /**
     * @param accept the 'accept' header to set
     */
    public void setAccept(MediaType accept) {
        this.accept = accept;
    }

    /**
     * @return the cookies
     */
    public List<Cookie> getCookies() {
        return store.getCookies();
    }

    /**
     * @param rest the rest to set
     */
    public void setRest(RestTemplate rest) {
        this.rest = rest;
    }

    /**
     * @param oauth2Credentials the oauth2Credentials to set
     */
    public void setOauth2Credentials(String oauth2Credentials) {
        this.oauth2Credentials = oauth2Credentials;
    }

    /**
     * @return the oauth2Credentials
     */
    public String getOauth2Credentials() {
        return oauth2Credentials;
    }

    /**
     * @param cookies the cookies to set
     */
    public void setCookies(List<Cookie> cookies) {
        cookies.forEach(store::addCookie);
    }
}
