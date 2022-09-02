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

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonPropertyOrder({"timestamp","status","error","message","path","_links"})
@JacksonXmlRootElement(localName=Error.NAMESPACE_XML+":error")
public class Error extends Hypermedia{
    
    public static final String NAMESPACE_XML = "error";

    @JacksonXmlProperty(isAttribute = true, localName = "xlmns:"+Error.NAMESPACE_XML)
    private final String XMLNS_ERROR = "";
    
    @JacksonXmlProperty(isAttribute = true, localName = "xlmns:"+Hypermedia.NAMESPACE_XML)
    private final String XMLNS_HYPERMEDIA = Hypermedia.NAMESPACE_XML_URN;

    @JacksonXmlProperty(isAttribute = true, localName = "xlmns:"+Hyperlink.NAMESPACE_XML)
    private final String XMLNS_HYPERLINK = Hyperlink.NAMESPACE_XML_URN;

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private HttpServletRequest request;

    @JsonProperty("timestamp")
    @JacksonXmlProperty(localName = NAMESPACE_XML+":timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonIgnore
    public HttpServletRequest getRequest() {
        return request;
    }

    @JsonProperty("status")
    @JacksonXmlProperty(localName = NAMESPACE_XML+":status")
    public int getStatus() {
        return status;
    }

    @JsonProperty("error")
    @JacksonXmlProperty(localName = NAMESPACE_XML+":error")
    public String getError() {
        return error;
    }

    @JsonProperty("message")
    @JacksonXmlProperty(localName = NAMESPACE_XML+":message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("path")
    @JacksonXmlProperty(localName = NAMESPACE_XML+":path")
    public String getPath() {
        return path;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonIgnore
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

}

