package de.skotstein.lib.spring.restfulspring.util;


import org.springframework.http.HttpStatus;

public class RestClientItemNotFoundException extends RestClientException{

    public RestClientItemNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public RestClientItemNotFoundException(String message, HttpStatus suggestedHttStatusCode){
        super(message, suggestedHttStatusCode);
    }
}
