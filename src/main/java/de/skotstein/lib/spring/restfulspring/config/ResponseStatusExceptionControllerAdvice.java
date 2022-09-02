/* 
 *  Copyright 2022 Sebastian Kotstein
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.   
 */

package de.skotstein.lib.spring.restfulspring.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.server.ResponseStatusException;

public class ResponseStatusExceptionControllerAdvice {
    
    protected List<AbstractErrorBuilder> errorBuilders = new ArrayList<AbstractErrorBuilder>();
    protected AbstractErrorBuilder defaultErrorBuilder = new DefaultJsonErrorBuilder();

    /**
     * Sets the default {@link AbstractErrorBuilder} that should be used when no other error builder could satisfy an accepted media type.
     * @param errorRepresentationBuilder default representation builder
     */
    public void setDefaultErrorBuilder(AbstractErrorBuilder errorBuilder){
        defaultErrorBuilder = errorBuilder;
    }
    
    /**
     * Adds a {@link AbstractErrorBuilder} that can satisfy the media types defined within the passed instance.
     * @param errorRepresentationBuilder representation builder
     */
    public void addErrorBuilder(AbstractErrorBuilder errorBuilder){
        errorBuilders.add(errorBuilder);
    }
    /**
     * Adds a {@link AbstractErrorBuilder} that can satisfy the passed media type.
     * @param forMediaType satisfiable media type
     * @param AbstractErrorBuilder error builder
     */
    public void addErrorBuilder(MediaType forMediaType, AbstractErrorBuilder errorBuilder){
        errorBuilder.addCompatibleMediaType(forMediaType);
        addErrorBuilder(errorBuilder);
    }

    /**
     * Adds a {@link AbstractErrorBuilder} that can satisfy all of the passed media types.
     * @param forMediaTypes list of satisfiable media types
     * @param errorBuilder representation builder
     */
    public void addErrorBuilder(MediaType forMediaTypes[], AbstractErrorBuilder errorBuilder){
        for (MediaType mediaType : forMediaTypes) {
            errorBuilder.addCompatibleMediaType(mediaType);
        }    
        addErrorBuilder(errorBuilder);
    }

    /**
     * Handles the thrown and passed {@ResponseStatusException} caused in the course of the passed {@link HttpServletRequest}.
     * @param ex
     * @param request
     * @return
     */
    public ResponseEntity<String> handle(ResponseStatusException ex, HttpServletRequest request){
        return this.handle(ex, request, new HttpHeaders());
    }

     /**
     * Handles the thrown and passed {@ResponseStatusException} caused in the course of the passed {@link HttpServletRequest}.
     *  
     * @param ex
     * @param request
     * @param responseHeader
     * @return
     */
    public ResponseEntity<String> handle(ResponseStatusException ex, HttpServletRequest request,HttpHeaders responseHeader){
        //Step 1: determine set of media types accepted by client
        String acceptHeaderValue = request.getHeader("Accept");
        List<MediaType> acceptedMediaTypes = new ArrayList<MediaType>();
        try{
            if(!Objects.isNull(acceptHeaderValue) && !acceptHeaderValue.isEmpty()){
                //parse and sort acceptable media types
                acceptedMediaTypes = MediaType.parseMediaTypes(acceptHeaderValue);
                MediaType.sortBySpecificityAndQuality(acceptedMediaTypes);
            }else{
                //if no acceptable media type is specified, set JSON per default
                acceptedMediaTypes.clear();
                acceptedMediaTypes.add(MediaType.APPLICATION_JSON);
            }
        }catch(Exception e){
            //in case of an error, set JSON per default
            acceptedMediaTypes.clear();
            acceptedMediaTypes.add(MediaType.APPLICATION_JSON);
        }

        //Step 2: iterate over all acceptable media types and choose first matching error representation builder
        for(MediaType acceptedMediaType : acceptedMediaTypes){
            for(AbstractErrorBuilder errorBuilder : errorBuilders){
                if(errorBuilder.isCompatibleWith(acceptedMediaType)){
                    return errorBuilder.build(ex, request,responseHeader);
                }
            }
        }
        //Step 3: if no representation builder has matched, use default representation builder
        if(!Objects.isNull(defaultErrorBuilder)){
            return defaultErrorBuilder.build(ex, request,responseHeader);
        }else{
            return new ResponseEntity<String>("No response status exception handler available.",responseHeader,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> handleNotAcceptableException(HttpMediaTypeNotAcceptableException ex, HttpServletRequest request){
        HttpHeaders httpHeaders = new HttpHeaders();
        String acceptedMediaTypes = MediaType.toString(ex.getSupportedMediaTypes());
        httpHeaders.add("Accept",acceptedMediaTypes);
        ResponseEntity<String> response = this.handle(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, ex.getMessage()), request,httpHeaders); 
        return response;
    }

    public ResponseEntity<String> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException ex, HttpServletRequest request){
        HttpHeaders httpHeaders = new HttpHeaders();
        String allowedMethods = "";
        for(String allowedMethod : ex.getSupportedMethods()){
            if(allowedMethods.isBlank()){
                allowedMethods = allowedMethod;
            }else{
                allowedMethods+=", "+allowedMethod;
            }
        }
        httpHeaders.add("Allow",allowedMethods);

        ResponseEntity<String> response = this.handle(new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage()), request,httpHeaders); 
        return response;
    }
}
