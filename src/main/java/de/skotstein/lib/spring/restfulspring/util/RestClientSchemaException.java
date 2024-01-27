package de.skotstein.lib.spring.restfulspring.util;

import org.springframework.http.HttpStatus;

public class RestClientSchemaException extends RestClientException{

    public RestClientSchemaException(String message) {
        super(message);
    }

    public RestClientSchemaException(String message, HttpStatus suggestedHttStatusCode){
        super(message, suggestedHttStatusCode);
    }
}
