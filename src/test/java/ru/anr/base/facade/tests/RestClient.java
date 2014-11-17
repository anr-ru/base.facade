/**
 * 
 */
package ru.anr.base.facade.tests;

import java.nio.charset.Charset;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import ru.anr.base.BaseParent;

/**
 * Some lite configured rest-client for test reason.
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
     * Port
     */
    private int port = 8080;

    /**
     * Host
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
     * Building a base url string (server location)
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
     *            Can be relating path (for instance, '/ping') or full path (
     *            {@link #getBaseUrl()} is not used) like http://localhost:9090
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

        return template;
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

        HttpHeaders hh = applyHeaders();
        return rest.exchange(getUri(path), HttpMethod.POST, new HttpEntity<String>(body, hh), String.class);
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

        HttpHeaders hh = applyHeaders();
        return rest.exchange(getUri(path), HttpMethod.PUT, new HttpEntity<String>(body, hh), String.class);
    }

    /**
     * PUT method.
     * 
     * @param path
     *            Relative or absolute path
     * @return Response with a body and state
     */
    public ResponseEntity<String> delete(String path) {

        HttpHeaders hh = applyHeaders();
        return rest.exchange(getUri(path), HttpMethod.DELETE, new HttpEntity<String>(hh), String.class);
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

        HttpHeaders hh = applyHeaders();
        return rest.exchange(getUri(path), HttpMethod.GET, new HttpEntity<String>(hh), String.class, uriVariables);
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
