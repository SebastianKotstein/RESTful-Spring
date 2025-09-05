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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import de.skotstein.lib.spring.restfulspring.model.representations.HypermediaRepresentation;

@JsonView(HypermediaRepresentation.class)
public abstract class Hypermedia {
    
    public static final String NAMESPACE_XML = "hypermedia";
    public static final String NAMESPACE_XML_URN = "";

    protected List<Hyperlink> hyperlinks = new ArrayList<Hyperlink>();

    @JsonProperty("_links")
    @JacksonXmlElementWrapper(localName = Hypermedia.NAMESPACE_XML+":links")
    @JacksonXmlProperty(localName = Hyperlink.NAMESPACE_XML+":link")
    public List<Hyperlink> getHyperlinks(){
        return hyperlinks;
    }

    /**
     * Adds a hyperlink consisting of the passed link relation and the reference to this {@link Hypermedia} entity.
     * @param rel link relation
     * @param href hypertext reference
     */
    public void addHyperlink(String rel, String href) {
        this.addHyperlink(new Hyperlink(href,rel));
    }

    /**
     * Adds a hyperlink consisting of the passed link relation, the reference, and the request method to this {@link Hypermedia} entity.
     * @param rel link relation
     * @param href hypertext reference
     * @param method request method
     */
    public void addHyperlink(String rel, String href, String method){
        Hyperlink hyperlink = new Hyperlink(href, rel);
        hyperlink.setMethod(method);
        this.addHyperlink(hyperlink);
    }

    /**
     * Adds the passed {@link Hyperlink} to this {@link Hypermedia} entity.
     * @param hyperlink the hyperlink object
     */
    public void addHyperlink(Hyperlink hyperlink){
        hyperlinks.add(hyperlink);
    }

    /**
     * Adds a hyperlink consisting of the passed link relation and a reference that is automatically derived from the passed endpoint definition to this {@link Hypermedia} entity.
     * Set 'expand' to true to substitute path and query parameters of the derived URI with given parameters and embed a static URI as reference. Set 'expand' to false to embed the URI as a template.
     * @see {@link Link#expand(Object...)} for more details about URI template expansion.
     * @param rel link relation
     * @param invocationValue The endpoint definition. Use {@link WebMvcLinkBuilder#methodOn(Class, Object...)} to specify the endpoint.
     * @param expand true to embed a static URI as reference; false to embed the URI as template
     */
    public void addHyperlink(String rel, Object invocationValue, boolean expand) {
        Link link = null;
        if (expand) {
            link = WebMvcLinkBuilder.linkTo(invocationValue).withRel(rel).expand();
        } else {
            link = WebMvcLinkBuilder.linkTo(invocationValue).withRel(rel);
        }
        this.addHyperlink(new Hyperlink(link));
    }

    /**
     * Adds a hyperlink consisting of the passed link relation, a reference that is automatically derived from the passed endpoint definition, and the request method to this {@link Hypermedia} entity.
     * Set 'expand' to true to substitute path and query parameters of the derived URI with given parameters and embed a static URI as reference. Set 'expand' to false to embed the URI as a template.
     * @see {@link Link#expand(Object...)} for more details about URI template expansion.
     * @param rel link relation
     * @param invocationValue The endpoint definition. Use {@link WebMvcLinkBuilder#methodOn(Class, Object...)} to specify the endpoint.
     * @param expand true to embed a static URI as reference; false to embed the URI as template
     * @param method request method
     */
    public void addHyperlink(String rel, Object invocationValue, boolean expand, String method){
        Link link = null;
        if (expand) {
            link = WebMvcLinkBuilder.linkTo(invocationValue).withRel(rel).expand();
        } else {
            link = WebMvcLinkBuilder.linkTo(invocationValue).withRel(rel);
        }
        Hyperlink hyperlink = new Hyperlink(link);
        hyperlink.setMethod(method);
        this.addHyperlink(hyperlink);
    }

    /**
     * Conditionally adds a hyperlink consisting of the passed link relation and the reference to this {@link Hypermedia} entity if a hyperlink having the passed link relation has not been added yet.
     * @param rel link relation
     * @param href hypertext reference
     */
    public void addHyperlinkIfRelNotExisting(String rel, String href){
        if(!hasHyperlink(rel)){
            this.addHyperlink(rel, href);
        }
    }

    /**
     * Conditionally adds a hyperlink consisting of the passed link relation, the reference, and the request method to this {@link Hypermedia} entity if a hyperlink having the passed link relation has not been added yet.
     * @param rel link relation
     * @param href hypertext reference
     * @param method request method
     */
    public void addHyperlinkIfRelNotExisting(String rel, String href, String method){
        if(!hasHyperlink(rel)){
            this.addHyperlink(rel, href, method);
        }
    }

    /**
     * Conditionally adds the passed {@link Hyperlink} to this {@link Hypermedia} entity if a hyperlink having the contained link relation has not been added yet.
     * @param hyperlink the hyperlink object
     */
    public void addHyperlinkIfRelNotExisting(Hyperlink hyperlink){
        if(!hasHyperlink(hyperlink.getRel())){
            hyperlinks.add(hyperlink);
        }
    }

    /**
     * Conditionally adds a hyperlink consisting of the passed link relation and a reference that is automatically derived from the passed endpoint definition to this {@link Hypermedia} entity if a hyperlink having the passed link relation has not been added yet.
     * Set 'expand' to true to substitute path and query parameters of the derived URI with given parameters and embed a static URI as reference. Set 'expand' to false to embed the URI as a template.
     * @see {@link Link#expand(Object...)} for more details about URI template expansion.
     * @param rel link relation
     * @param invocationValue The endpoint definition. Use {@link WebMvcLinkBuilder#methodOn(Class, Object...)} to specify the endpoint.
     * @param expand true to embed a static URI as reference; false to embed the URI as template
     */
    public void addHyperlinkIfRelNotExisting(String rel, Object invocationValue, boolean expand){
        if(!hasHyperlink(rel)){
            this.addHyperlink(rel, invocationValue, expand);
        }
    }

    /**
     * Conditionally adds a hyperlink consisting of the passed link relation, a reference that is automatically derived from the passed endpoint definition, and the request method to this {@link Hypermedia} entity if a hyperlink having the passed link relation has not been added yet.
     * Set 'expand' to true to substitute path and query parameters of the derived URI with given parameters and embed a static URI as reference. Set 'expand' to false to embed the URI as a template.
     * @see {@link Link#expand(Object...)} for more details about URI template expansion.
     * @param rel link relation
     * @param invocationValue The endpoint definition. Use {@link WebMvcLinkBuilder#methodOn(Class, Object...)} to specify the endpoint.
     * @param expand true to embed a static URI as reference; false to embed the URI as template
     * @param method request method
     */
    public void addHyperlinkIfRelNotExisting(String rel, Object invocationValue, boolean expand, String method){
        if(!hasHyperlink(rel)){
            this.addHyperlink(rel, invocationValue, expand, method);
        }
    }

    /**
     * Replaces the reference of an existing hyperlink that has the passed link relation.
     * Nothing will happen if no such hyperlink exists.
     * @param rel link relation
     * @param href hypertext reference that replaces the existing reference
     */
    public void replaceHyperlinkIfRelExisting(String rel, String href){
        this.replaceHyperlinkIfRelExisting(rel, href, null);
    }

    /**
     * Replaces the reference of an existing hyperlink that has the passed link relation.
     * Nothing will happen if no such hyperlink exists.
     * @param rel link relation
     * @param href hypertext reference that replaces the existing reference
     * @param method if set the request method is also replaced
     */
    public void replaceHyperlinkIfRelExisting(String rel, String href, String method){
        Hyperlink hyperlink = getHyperlink(rel);
        if(!Objects.isNull(hyperlink)){
            hyperlink.setHref(href);
            if(!Objects.isNull(method)){
                hyperlink.setMethod(method);
            }
        }
    }

    /**
     * Replaces the reference of an existing hyperlink that has the passed link relation.
     * Nothing will happen if no such hyperlink exists.
     * The new reference is automatically derived from the passed endpoint definition
     * Set 'expand' to true to substitute path and query parameters of the derived URI with given parameters and embed a static URI as reference. Set 'expand' to false to embed the URI as a template.
     * @param rel link relation
     * @param invocationValue The endpoint definition. Use {@link WebMvcLinkBuilder#methodOn(Class, Object...)} to specify the endpoint.
     * @param expand true to embed a static URI as reference; false to embed the URI as template
     * @param method request method
     */
    public void replaceHyperlinkIfRelExisting(String rel, Object invocationValue, boolean expand){
        this.replaceHyperlinkIfRelExisting(rel, invocationValue, expand, null);
    }

    /**
     * Replaces the reference of an existing hyperlink that has the passed link relation.
     * Nothing will happen if no such hyperlink exists.
     * The new reference is automatically derived from the passed endpoint definition
     * Set 'expand' to true to substitute path and query parameters of the derived URI with given parameters and embed a static URI as reference. Set 'expand' to false to embed the URI as a template.
     * @param rel link relation
     * @param invocationValue The endpoint definition. Use {@link WebMvcLinkBuilder#methodOn(Class, Object...)} to specify the endpoint.
     * @param expand true to embed a static URI as reference; false to embed the URI as template
     * @param method if set the request method is also replaced
     */
    public void replaceHyperlinkIfRelExisting(String rel, Object invocationValue, boolean expand, String method){
        Hyperlink hyperlink = getHyperlink(rel);
        if(!Objects.isNull(hyperlink)){
            Link link = null;
            if(expand){
                link = WebMvcLinkBuilder.linkTo(invocationValue).withRel(rel).expand();
            }else{
                link = WebMvcLinkBuilder.linkTo(invocationValue).withRel(rel);
            }
            hyperlink.setHref(link.getHref());
            if(!Objects.isNull(method)){
                hyperlink.setMethod(method);
            }
        }
    }

    public boolean hasHyperlink(String rel) {
        return getHyperlink(rel) != null;
    }

    public Hyperlink getHyperlink(String rel) {
        for (Hyperlink hyperlink : hyperlinks) {
            if (hyperlink.getRel().compareTo(rel) == 0) {
                return hyperlink;
            }
        }
        return null;
    }

    public Hyperlink removeHyperlink(String rel){
        for(int i = 0; i < hyperlinks.size(); i++){
            Hyperlink hyperlink = hyperlinks.get(i);
            if(hyperlink.getRel().compareTo(rel)==0){
                hyperlinks.remove(i);
                return hyperlink;
            }
        }
        return null;
    }

    public void clearHyperlinks(){
        this.hyperlinks.clear();
    }
    
}

