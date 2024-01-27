package de.skotstein.lib.spring.restfulspring.util;

import org.springframework.http.HttpStatus;

public class RestClientExpectationFailedException extends RestClientException{
    
    public RestClientExpectationFailedException(String message) {
        super(message);
    }

    public RestClientExpectationFailedException(String message, HttpStatus suggestedHttStatusCode){
        super(message, suggestedHttStatusCode);
    }
}
