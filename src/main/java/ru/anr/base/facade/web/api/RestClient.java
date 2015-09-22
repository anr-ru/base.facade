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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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

/**
 * Some lite configured rest-client for test purposes.
 *
 *
 * @author Alexey Romanchuk
 * @created Nov 13, 2014
 *
 */

public class RestClient extends BaseParent {

    /**
     * Logger
     */
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
     * Clearing cookie value (remove session)
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
     * Constructor with schema
     * 
     * @param schema
     *            schema Default constructor
     */
    public RestClient(String schema) {

        super();
        setSchema(schema);
        rest = initRest(new RestTemplate());
    }

    /**
     * Constructor with OAuth2 resource
     * 
     * @param resource
     *            OAuth2 protected resource
     */
    public RestClient(OAuth2ProtectedResourceDetails resource) {

        super();
        rest = initRest(new OAuth2RestTemplate(resource));
    }

    /**
     * Constructor with a predefined rest template
     * 
     * @param template
     *            The template to use
     */
    public RestClient(RestTemplate template) {

        super();
        rest = template;
    }

    /**
     * Building a base url string (server location), excluding a printing of
     * standard http ports.
     * 
     * @return String with server location with schema, host and port
     */
    public String getBaseUrl() {

        return UriUtils.getBaseUrl(schema, host, port);
    }

    /**
     * Get final URI of http resource
     * 
     * @param path
     *            Can be a relative path (for instance, '/ping') or a full one (
     *            {@link #getBaseUrl()} is not used) like
     *            http://localhost:9090/ping
     * @return A full path to http resource (included schema, host, port,
     *         relative path)
     */
    public String getUri(String path) {

        return UriUtils.getUri(schema, host, port, path);
    }

    /**
     * Cookie storage
     */
    private final CookieStore store = new BasicCookieStore();

    /**
     * Special initialization of {@link RestTemplate} - used to apply some
     * settings for existing RestTemplates.
     * 
     * @param template
     *            {@link RestTemplate} or its
     * @return Updated RestTemplate
     */
    public RestTemplate initRest(RestTemplate template) {

        // 1. Set up ssl settings
        HttpClient client =
                "https".equals(schema) ? buildSSLClient() : HttpClients.custom().setDefaultCookieStore(store).build();

        template.setRequestFactory(new HttpComponentsClientHttpRequestFactory(client) {

            @Override
            protected void postProcessHttpRequest(HttpUriRequest request) {

                super.postProcessHttpRequest(request);
            }

        });

        // 2. Error handler
        template.setErrorHandler(new DefaultResponseErrorHandler());

        return template;
    }

    /**
     * Getting {@link RestOperations} and its descendants
     * 
     * @param <S>
     *            Class of {@link RestOperations}
     * @return A rest template
     */
    @SuppressWarnings("unchecked")
    public <S extends RestOperations> S ops() {

        return (S) rest;
    }

    /**
     * Configuring an apache client to support untrusted ssl connections. This
     * can be useful for test purposes only.
     * 
     * @return Apache {@link HttpClient}
     */
    private HttpClient buildSSLClient() {

        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {

            @Override
            public boolean isTrusted(X509Certificate[] certificate, String authType) {

                return true;
            }
        };

        try {

            SSLContextBuilder sslBuilder = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy);
            SSLContext sslContext = sslBuilder.useTLS().build();

            SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslContext, new AllowAllHostnameVerifier());
            return HttpClients.custom().setSSLSocketFactory(sf).build();

        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException ex) {
            throw new ApplicationException(ex);
        }
    }

    /**
     * Basic authorization header
     */
    private String basicCredentials;

    /**
     * Setting Basic Authorization header to apply
     * 
     * @param user
     *            A user
     * @param password
     *            A password
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
     * Applying for default headers
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
            hh.setAcceptCharset(list(Charset.forName("utf-8")));
        }

        if (basicCredentials != null) {
            hh.add("Authorization", basicCredentials);
        }

        return hh;
    }

    /**
     * POST method.
     * 
     * @param path
     *            Relative or absolute path
     * @param body
     *            Request body (as expected by
     *            {@link #setContentType(MediaType)}), default "application/json
     * @return Response with a body and state
     */
    public ResponseEntity<String> post(String path, String body) {

        return doExchange(path, HttpMethod.POST, body, String.class);
    }

    /**
     * POST for a form
     * 
     * @param path
     *            Path to resource
     * @param formData
     *            Form params
     * @return Http status for response
     */
    public ResponseEntity<Void> post(String path, MultiValueMap<String, String> formData) {

        setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return doExchange(getUri(path), HttpMethod.POST, formData, (Class<Void>) null);
    }

    /**
     * Performing GET query with redirect to some location
     * 
     * @param path
     *            Path for query
     * @return Redirected url
     */
    public String getRedirect(String path) {

        ResponseEntity<Void> response = doExchange(getUri(path), HttpMethod.GET, (String) null, Void.class);
        URI location = response.getHeaders().getLocation();

        try {
            return URLDecoder.decode(location.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Could not decode URL", e);
        }
    }

    /**
     * PUT method.
     * 
     * @param path
     *            Relative or absolute path
     * @param body
     *            Request body (as expected by
     *            {@link #setContentType(MediaType)}), default "application/json
     * @return Response with a body and state
     */
    public ResponseEntity<String> put(String path, String body) {

        return doExchange(path, HttpMethod.PUT, body, String.class);
    }

    /**
     * PUT method.
     * 
     * @param path
     *            Relative or absolute path
     * @return Response with a body and state
     */
    public ResponseEntity<String> delete(String path) {

        return doExchange(path, HttpMethod.DELETE, (String) null, String.class);
    }

    /**
     * GET method.
     * 
     * @param path
     *            Relative or absolute path
     * @param uriVariables
     *            Request variable according to Spring
     *            {@link org.springframework.web.util.UriTemplate#expand(Object...)}
     *            rules.
     * @return Response with a body and state
     */
    public ResponseEntity<String> get(String path, Object... uriVariables) {

        return doExchange(path, HttpMethod.GET, (String) null, String.class, uriVariables);
    }

    /**
     * Performs uploading a resource
     * 
     * @param path
     *            The REST path
     * @param resource
     *            Some resource
     * @return A resulted string
     */
    public ResponseEntity<String> upload(String path, Resource resource) {

        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", resource);

        HttpHeaders hh = applyHeaders();
        hh.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<LinkedMultiValueMap<String, Object>> e =
                new HttpEntity<LinkedMultiValueMap<String, Object>>(map, hh);

        return exchange(path, HttpMethod.POST, e, String.class);
    }

    /**
     * Performs downloading a file
     * 
     * @param path
     *            A REST path
     * @return An array of bytes
     */
    public ResponseEntity<byte[]> download(String path) {

        HttpHeaders headers = applyHeaders();
        headers.setAccept(list(MediaType.APPLICATION_OCTET_STREAM));

        return exchange(path, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);
    }

    /**
     * General representation for all rest operations
     * 
     * @param path
     *            Relative or absolute path to resource
     * @param method
     *            http method to use
     * @param body
     *            Request body (for PUT/POST)
     * @param uriVariables
     *            uri params (part of url in GET queries)
     * @return Response
     * 
     * @param clazz
     *            Response entity class
     * @param <T>
     *            Response entity type
     * @param <S>
     *            Request entity type
     */
    public <S, T> ResponseEntity<T> doExchange(String path, HttpMethod method, S body, Class<T> clazz,
            Object... uriVariables) {

        HttpHeaders hh = applyHeaders();
        return exchange(path, method, new HttpEntity<S>(body, hh), clazz, uriVariables);
    }

    /**
     * A low-level http-exchange operation
     * 
     * @param path
     *            A rest path
     * @param method
     *            An http method
     * @param entity
     *            An entity
     * @param clazz
     *            A response class
     * @param uriVariables
     *            A set of url parameters
     * @return A response entity
     * 
     * @param <S>
     *            A type of the response
     * @param <T>
     *            A type of the request entity
     */
    public <S, T> ResponseEntity<T> exchange(String path, HttpMethod method, HttpEntity<S> entity, Class<T> clazz,
            Object... uriVariables) {

        ResponseEntity<T> rs = rest.exchange(getUri(path), method, entity, clazz, uriVariables);

        logger.trace("Cookie: {}", store.getCookies());
        logger.debug("Http response: {}", rs);

        return rs;
    }

    // /////////////////////////////////////////////////////////////////////////
    // /// getters/setters
    // /////////////////////////////////////////////////////////////////////////

    /**
     * @return the port
     */
    public int getPort() {

        return port;
    }

    /**
     * @param port
     *            the port to set
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
     * @param schema
     *            the schema to set
     */
    public void setSchema(String schema) {

        this.schema = schema;
    }

    /**
     * @return the rest
     * 
     * @param <S>
     *            Interface type
     */
    @SuppressWarnings("unchecked")
    public <S extends RestOperations> S getRest() {

        return (S) rest;
    }

    /**
     * @return the host
     */
    public String getHost() {

        return host;
    }

    /**
     * @param host
     *            the host to set
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
     * @param contentType
     *            the contentType to set
     */
    public void setContentType(MediaType contentType) {

        this.contentType = contentType;
    }

    /**
     * @return the accept
     */
    public MediaType getAccept() {

        return accept;
    }

    /**
     * @param accept
     *            the accept to set
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
     * @param rest
     *            the rest to set
     */
    public void setRest(RestTemplate rest) {

        this.rest = rest;
    }

    /**
     * @param cookies
     *            the cookies to set
     */
    public void setCookies(List<Cookie> cookies) {

        cookies.forEach(c -> store.addCookie(c));
    }
}
