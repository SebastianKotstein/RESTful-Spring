package de.skotstein.lib.spring.restfulspring.util;

import org.springframework.http.HttpStatus;

public class RestClientException extends Exception{
    
    private HttpStatus suggestedHttStatusCode = HttpStatus.INTERNAL_SERVER_ERROR;

    public HttpStatus getSuggestedHttStatusCode() {
        return suggestedHttStatusCode;
    }

    public void setSuggestedHttStatusCode(HttpStatus suggestedHttStatusCode) {
        this.suggestedHttStatusCode = suggestedHttStatusCode;
    }

    public RestClientException(String message){
        super(message);
    }

    public RestClientException(String message, HttpStatus suggestedHttStatusCode){
        super(message);
        this.suggestedHttStatusCode = suggestedHttStatusCode;
    }
}

