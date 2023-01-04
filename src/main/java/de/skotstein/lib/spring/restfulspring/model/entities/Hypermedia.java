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

    public void addHyperlink(String rel, Object invocationValue, boolean expand) {
        if (expand) {
            hyperlinks.add(new Hyperlink(WebMvcLinkBuilder.linkTo(invocationValue).withRel(rel).expand()));
        } else {
            hyperlinks.add(new Hyperlink(WebMvcLinkBuilder.linkTo(invocationValue).withRel(rel)));
        }

    }

    public void addHyperlinkIfRelNotExisting(String rel, Object invocationValue, boolean expand){
        if(!hasHyperlink(rel)){
            if (expand) {
                hyperlinks.add(new Hyperlink(WebMvcLinkBuilder.linkTo(invocationValue).withRel(rel).expand()));
            } else {
                hyperlinks.add(new Hyperlink(WebMvcLinkBuilder.linkTo(invocationValue).withRel(rel)));
            }
        }
    }

    public void replaceHyperlinkIfRelExisting(String rel, Object invocationValue, boolean expand){
        Hyperlink hyperlink = getHyperlink(rel);
        if(!Objects.isNull(hyperlink)){
            Link link = null;
            if(expand){
                link = WebMvcLinkBuilder.linkTo(invocationValue).withRel(rel).expand();
            }else{
                link = WebMvcLinkBuilder.linkTo(invocationValue).withRel(rel);
            }
            hyperlink.setHref(link.getHref());
        }
    }

    public void addHyperlink(String rel, String href) {
       hyperlinks.add(new Hyperlink(href,rel));
    }

    public void addHyperlinkIfRelNotExisting(String rel, String href){
        if(!hasHyperlink(rel)){
            hyperlinks.add(new Hyperlink(href,rel));
        }
    }

    public void replaceHyperlinkIfRelExisting(String rel, String href){
        Hyperlink hyperlink = getHyperlink(rel);
        if(!Objects.isNull(hyperlink)){
            hyperlink.setHref(href);
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

