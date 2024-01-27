package de.skotstein.lib.spring.restfulspring.util;

import java.util.List;

public interface RestClient {
    
    
    /**
     * Creates and returns a {@link RestClient} instance that uses the passed base as URI prefix for all subsequent requests.
     * Use this method for creating a {@link}, if the remote server advertises hyperlinks containing only the path of a URI, and specify schema, host, and port as base.
     * @param base URI prefix for all subsequent requests, e.g. "https://myapi.com:8081"
     * @return the {@link RestClient}
     */
    public static RestClient create(String base){
        RestClient restClient = new RestClientImpl();
        restClient.withBase(base);
        return restClient;
    }

    /**
     * Creates and returns a {@link RestClient} instance with an empty base path.
     * Use this method for creating a {@link RestClient}, if the remote server advertises hyperlinks containing a full URI (including schema, host, and port)
     * @return the {@link RestClient}
     */
    public static RestClient create(){
        RestClientImpl restClient = new RestClientImpl();
        return restClient;
    }

     /**
     * Sets the key of the JSON array that contains links within a JSON structure. This key is used in a subsequent requests to identify and extract the list of
     * advertised hyperlinks when trying to follow a particular hyperlink by relation.
     * If the key is not specified, its default value will be '_links'
     * @param key the key of the JSON array that contains links within a JSON structure
     * @return the {@link RestClient}
     */
    public RestClient withLinksProperty(String key);

    /**
     * Adds the passed HTTP request header (name and value) to the {@link RestClientImpl}. This header is added to all subsequent HTTP requests. 
     * @param name the header's name
     * @param value the header's value
     * @return the {@link RestClientImpl}
     */
    public RestClient addHeader(String name, String value);

    /**
     * Removes all added HTTP request headers
     * @return the {@link RestClientImpl}
     */
    public RestClient clearHeaders();

    /**
     * Removes all HTTP request headers having the passed name if they are present (optional operation).
     * @param name the header's name
     * @return the {@link RestClientImpl}
     */
    public RestClient removeHeader(String name);

    /**
     * Sets (i.e. adds or overwrites) the 'Accept' request header with the passed MIME-Type.
     * @param mimeType the 'Accept' header's value (MIME-Type)
     * @return the {@link RestClientImpl}
     */
    public RestClient accept(String mimeType);

    /**
     * Sets (i.e. adds or overwrites) the 'Content-Type' request header with the passed MIME-Type.
     * @param mimeType the 'Content-Type' header's value (MIME-Type)
     * @return the {@link RestClientImpl}
     */
    public RestClient contentType(String mimeType);

    /**
     * Sets the base (URI prefix). Pass 'null' to set an empty base path.
     * @param basePath the specified base (URI prefix)
     */
    public RestClient withBase(String basePath);

    public RestClient withRequestException(boolean requestExceptionEnabled);

    public RestClient get(String uri) throws RestClientIOException, RestClientRequestException;

    public RestClient post(String uri) throws RestClientIOException, RestClientRequestException;

    public RestClient post(String uri, byte[] requestContent) throws RestClientIOException, RestClientRequestException;

    public RestClient put(String uri, byte[] requestContent) throws RestClientIOException, RestClientRequestException;

    public RestClient delete(String uri) throws RestClientIOException, RestClientRequestException;

    public RestClientTransition follow(String rel) throws RestClientExpectationFailedException, RestClientSchemaException;

    public RestClientTransition followItem(String collectionKey, String itemSelectorKey, String itemSelectorValue, String rel) throws RestClientExpectationFailedException, RestClientSchemaException, RestClientItemNotFoundException;

    public RestClientResult result();

    public List<RestClientResult> results();
}
