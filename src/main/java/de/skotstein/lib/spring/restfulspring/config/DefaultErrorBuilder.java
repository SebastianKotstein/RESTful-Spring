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


import java.text.SimpleDateFormat;
import java.util.Date;

//import javax.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import de.skotstein.lib.spring.restfulspring.model.entities.Error;


public abstract class DefaultErrorBuilder extends AbstractErrorBuilder{

    @Override
    public ResponseEntity<String> build(ResponseStatusException ex, HttpServletRequest request, HttpHeaders httpHeaders) {
        httpHeaders.add("Content-Type", getDefaultMediaType().toString());
        return new ResponseEntity<String>(serializeError(createError(ex, request)),httpHeaders,ex.getStatusCode());
    }

    protected Error createError(ResponseStatusException ex, HttpServletRequest request){
        Error error = new Error();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        error.setTimestamp(formatter.format(new Date()));
        error.setStatus(ex.getStatusCode().value());
        error.setError(ex.getStatusCode().toString());
        error.setMessage(ex.getReason());
        error.setPath(request.getRequestURI());
        error.setRequest(request);
        return error;
    }

    protected abstract String serializeError(Error error);

    /**
     * Returns the media type of the created error representation
     * @return
     */
    protected abstract MediaType getDefaultMediaType();
    
}

