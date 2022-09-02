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

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;


public abstract class AbstractErrorBuilder {
    
    private List<MediaType> compatibleMediaTypes = new ArrayList<MediaType>();

    /**
     * Returns true, if this instance can build a representation whose media type is compatible with the passed media type
     * @param mediaType
     * @return
     */
    public boolean isCompatibleWith(MediaType mediaType){
        for (MediaType availableMediaType : compatibleMediaTypes) {
            if(mediaType.isCompatibleWith(availableMediaType) || mediaType == availableMediaType) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true, if this instance supports the passed media type
     * @param mediaType
     * @return
     */
    public boolean supportsMediaType(MediaType mediaType){
        for (MediaType availableMediaType : compatibleMediaTypes) {
            if(mediaType == availableMediaType){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a compatible media type to this error builder
     * @param mediaType
     */
    public void addCompatibleMediaType(MediaType mediaType){
        compatibleMediaTypes.add(mediaType);
    }

    public abstract ResponseEntity<String> build(ResponseStatusException ex, HttpServletRequest request, HttpHeaders httpHeaders);   
    
}

