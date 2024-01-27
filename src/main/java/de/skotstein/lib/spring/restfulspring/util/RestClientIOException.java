package de.skotstein.lib.spring.restfulspring.util;

import org.springframework.http.HttpStatus;

public class RestClientIOException extends RestClientException{

    public RestClientIOException(String message) {
        super(message);
    }

    public RestClientIOException(String message, HttpStatus suggestedHttStatusCode){
        super(message, suggestedHttStatusCode);
    }
}
