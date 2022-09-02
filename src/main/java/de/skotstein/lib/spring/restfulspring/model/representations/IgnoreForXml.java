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

package de.skotstein.lib.spring.restfulspring.model.representations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * If annotated to a method of field, the annotated member is ignored by introspection-based serialization/deserialization to/from XML.
 * Note that, in comparison to {@link JsonIgnore}, which affects both XML and JSON, this annotation does only affect serialization/deserialization to/from XML, but is ignored in case of JSON.
 * To achieve this behavior, make sure to set an instance of {@link CustomizedXmlAnnotationIntrospector} as annotation introspector on the object mapper used for XML serialization/deserialization.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface IgnoreForXml {
    
}
