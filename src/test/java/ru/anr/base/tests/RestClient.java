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
package ru.anr.base.tests;

import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import ru.anr.base.ApplicationException;
import ru.anr.base.BaseParent;

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
     * A reference to Spring {@link RestTemplate} engine
     */
    private final RestTemplate rest;

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
     * Default constructor
     */
    public RestClient() {

        super();
        rest = initRest(new RestTemplate());
    }

    /**
     * Building a base url string (server location), excluding a printing of
     * standard http ports.
     * 
     * @return String with server location with schema, host and port
     */
    public String getBaseUrl() {

        String portSuffix = (port == 80 || port == 443) ? "" : ":" + port;
        return schema + "://" + host + portSuffix;
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

        return path.contains("://") ? path : getBaseUrl() + (path.charAt(0) == '/' ? path : "/" + path);
    }

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
        if ("https".equals(schema)) {
            template.setRequestFactory(new HttpComponentsClientHttpRequestFactory(buildSSLClient()));

        } else {
            template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        }

        // 2. Error handler
        template.setErrorHandler(new DefaultResponseErrorHandler());

        return template;
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
     * Applying for default headers
     * 
     * @return {@link HttpHeaders} object
     */
    protected HttpHeaders applyHeaders() {

        HttpHeaders hh = new HttpHeaders();
        hh.setContentType(contentType);
        hh.setAccept(list(accept));
        hh.setAcceptCharset(list(Charset.forName("utf-8")));

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

        return exchange(path, HttpMethod.POST, body);
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

        return exchange(path, HttpMethod.PUT, body);
    }

    /**
     * PUT method.
     * 
     * @param path
     *            Relative or absolute path
     * @return Response with a body and state
     */
    public ResponseEntity<String> delete(String path) {

        return exchange(path, HttpMethod.DELETE, null);
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

        return exchange(path, HttpMethod.GET, null, uriVariables);
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
     */
    private ResponseEntity<String> exchange(String path, HttpMethod method, String body, Object... uriVariables) {

        HttpHeaders hh = applyHeaders();
        return rest.exchange(getUri(path), method, new HttpEntity<String>(body, hh), String.class, uriVariables);
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
     */
    public RestTemplate getRest() {

        return rest;
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
}
