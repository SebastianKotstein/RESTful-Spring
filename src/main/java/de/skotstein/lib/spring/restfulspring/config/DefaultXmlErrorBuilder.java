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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import org.springframework.http.MediaType;

import de.skotstein.lib.spring.restfulspring.model.entities.Error;

public class DefaultXmlErrorBuilder extends DefaultErrorBuilder{

    private XmlMapper xmlConverter = new XmlMapper();

    @Override
    protected String serializeError(Error error) {
        try {
            return xmlConverter.writeValueAsString(error);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "Conversion error in 'DefaultXmlErrorBuilder'";
        }
    }

    @Override
    protected MediaType getDefaultMediaType() {
        return MediaType.APPLICATION_XML;
    }
    
}

