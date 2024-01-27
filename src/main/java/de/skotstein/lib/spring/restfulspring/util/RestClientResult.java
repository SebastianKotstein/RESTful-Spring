package de.skotstein.lib.spring.restfulspring.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestClientResult {
    
    private int statusCode = 0;
    private String message = null;
    private String errorContent = null;
    //private JsonNode jsonContent = null;
    private byte[] responseContent = null;
    
    private String uri = null;    
    
    private Map<String, List<String>> responseHeaders;

    public static final int EXCEPTION = 600;
    public static final int IO_EXCEPTION = 601;
    public static final int UNKNOWN_SERVICE_EXCEPTION = 602;
    public static final int MALFORMED_URL_EXCEPTION = 603;
    public static final int JSON_PROCESSING_EXCEPTION = 604;

    public static final int NAVIGATION_ERROR = 700;
    public static final int EMPTY_PAYLOAD = 701;
    public static final int INVALID_SCHEMA = 702;
    public static final int RELATION_NOT_GIVEN = 703;
    public static final int NO_MATCH = 704;

    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getErrorContent() {
        return errorContent;
    }
    public void setErrorContent(String errorContent) {
        this.errorContent = errorContent;
    }

    /*
    public JsonNode getJsonContent() {
        return jsonContent;
    }
    public void setJsonContent(JsonNode jsonContent) {
        this.jsonContent = jsonContent;
    } */

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }
    public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public byte[] getResponseContent() {
        return responseContent;
    }
    public void setResponseContent(byte[] responseContent) {
        this.responseContent = responseContent;
    }

    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    public <T> T toJsonObject(java.lang.Class<T> valueType, ObjectMapper objectMapper) throws JsonProcessingException, IOException, RestClientExpectationFailedException{
        if(!Objects.isNull(getResponseContent())){
            return objectMapper.readValue(getResponseContent(), valueType);
        }else{
            throw new RestClientExpectationFailedException("Cannot query response payload since it is empty.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String asString(){
        if(!Objects.isNull(getResponseContent())){
            return new String(getResponseContent(), StandardCharsets.UTF_8);
        }else{
            return null;
        }
    }
    
}
