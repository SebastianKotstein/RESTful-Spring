package de.skotstein.lib.spring.restfulspring.util;

public interface RestClientTransition {
    
    public RestClient get() throws RestClientIOException, RestClientRequestException;

    public RestClient post() throws RestClientIOException, RestClientRequestException;

    public RestClient post(byte[] requestContent) throws RestClientIOException, RestClientRequestException;

    public RestClient put(byte[] requestContent) throws RestClientIOException, RestClientRequestException;

    public RestClient delete() throws RestClientIOException, RestClientRequestException;

}

