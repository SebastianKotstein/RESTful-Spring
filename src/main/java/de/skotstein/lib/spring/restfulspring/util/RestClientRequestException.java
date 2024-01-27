package de.skotstein.lib.spring.restfulspring.util;

import org.springframework.http.HttpStatus;

public class RestClientRequestException extends RestClientException{
    
    private RestClientResult restClientResult;

    public RestClientRequestException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public RestClientRequestException(String message, HttpStatus suggestedHttStatusCode){
        super(message, suggestedHttStatusCode);
    }

    public RestClientResult getRestClientResult() {
        return restClientResult;
    }

    public void setRestClientResult(RestClientResult restClientResult) {
        this.restClientResult = restClientResult;
    }
}
