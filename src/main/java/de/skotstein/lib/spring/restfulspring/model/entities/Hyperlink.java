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

package de.skotstein.lib.spring.restfulspring.model.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import org.springframework.hateoas.Link;

import de.skotstein.lib.spring.restfulspring.model.representations.HypermediaRepresentation;


@JsonView(HypermediaRepresentation.class)
@JacksonXmlRootElement(localName=Hyperlink.NAMESPACE_XML+":hyperlink")
public class Hyperlink {

    public static final String NAMESPACE_XML = "hyperlink";
    
    //@JacksonXmlProperty(isAttribute = true, localName = "xmlns:"+NAMESPACE_XML)
    public static final String NAMESPACE_XML_URN = "";

    public static String SELF_REL = "self";
    public static String COLLECTION_REL = "collection";
    public static String ITEM_REL = "item";
    public static String UPDATE_REL = "update";
    public static String CREATE_REL = "create";
    public static String DELETE_REL = "delete";
    public static String AUTH_REL = "authorization";

    private String href = null;
    private String rel = null;
    private String relHref = null;
    private String method = null;
    private String mimeType = null;

    public Hyperlink(){

    }

    public Hyperlink(String href, String rel){
        this(href,rel,null);
    }

    public Hyperlink(String href, String rel, String relHref){
        this(href,rel,relHref,null,null);
    }

    public Hyperlink(String href, String rel, String relHref, String method, String mimeType){
        this.href = href;
        this.rel = rel;
        this.relHref = relHref;
        this.method = method;
        this.mimeType = mimeType;
    }

    
    public Hyperlink(Link link){
        this(link.getHref(),link.getRel().value(),null);
    }

    public void setHref(String href){
        this.href = href;
    }

    public void setRel(String rel){
        this.rel = rel;
    }

    public void setRelHref(String relHref){
        this.relHref = relHref;
    }

    public void setMimeType(String mimeType){
        this.mimeType = mimeType;
    }

    public void setMethod(String method){
        this.method = method;
    }

    @JsonProperty("href")
    @JacksonXmlProperty(localName = NAMESPACE_XML+":href")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getHref(){
        return this.href;
    }

    @JsonProperty("rel")
    @JacksonXmlProperty(localName = NAMESPACE_XML+":rel")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getRel(){
        return this.rel;
    }

    @JsonProperty("relHref")
    @JacksonXmlProperty(localName = NAMESPACE_XML+":relHref")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getRelHref(){
        return this.relHref;
    }

    @JsonProperty("media")
    @JacksonXmlProperty(localName = NAMESPACE_XML+":media")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getMimeType(){
        return this.mimeType;
    }

    @JsonProperty("method")
    @JacksonXmlProperty(localName = NAMESPACE_XML+":method")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getMethod(){
        return this.method;
    }
}

