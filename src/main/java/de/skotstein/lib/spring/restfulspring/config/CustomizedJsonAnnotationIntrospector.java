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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import de.skotstein.lib.spring.restfulspring.model.representations.IgnoreForJson;

/**
 * Custom {@link JacksonAnnotationIntrospector} that instructs the {@link ObjectMapper} to ignore members which are annotated by {@link IgnoreForJson} or {@link JsonIgnore}
 * for serialization or deserialization. Set this annotation introspector on the {@link ObjectMapper} used for JSON serialization/deserialization. 
 */
public class CustomizedJsonAnnotationIntrospector extends JacksonAnnotationIntrospector{
    
    @Override
    public boolean hasIgnoreMarker(AnnotatedMember m) {
        IgnoreForJson explicitIgnorerMarker = m.getAnnotation(IgnoreForJson.class);
        if(!Objects.isNull(explicitIgnorerMarker)){
            return true;
        }
        JsonIgnore ignorerMarker = m.getAnnotation(JsonIgnore.class);
        if(!Objects.isNull(ignorerMarker)){
            return true;
        }
        return false;
    }
}

