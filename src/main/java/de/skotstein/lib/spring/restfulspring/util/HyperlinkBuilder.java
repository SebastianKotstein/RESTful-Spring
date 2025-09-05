package de.skotstein.lib.spring.restfulspring.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import de.skotstein.lib.spring.restfulspring.model.entities.Hyperlink;

public class HyperlinkBuilder implements HyperlinkBuilderCommon, HyperlinkBuilderLinkRelation, HyperlinkBuilderMethod {

    private Hyperlink hyperlink = new Hyperlink();

    private Boolean isAuthenticated = null;
    private boolean allOf = true;
    private Set<String> authorities = null;

    /*************************************** Href ****************************************/

    @Override
    public HyperlinkBuilderCommon href(String href) {
        this.hyperlink.setHref(href);   
        return this;
    }

    @Override
    public HyperlinkBuilderCommon href(Link link) {
        return this.href(link.getHref());
    } 

    @Override
    public HyperlinkBuilderCommon staticHref(Object invocationValue) {
        //we have to add a dummy relation attribute, which is ignored by the called href(...) method
        return this.href(WebMvcLinkBuilder.linkTo(invocationValue).withSelfRel().expand());
    }

    @Override
    public HyperlinkBuilderCommon templatedHref(Object invocationValue) {
        //we have to add a dummy relation attribute, which is ignored by the called href(...) method
        return this.href(WebMvcLinkBuilder.linkTo(invocationValue).withSelfRel());
    }

    /*************************************** Link Relation ****************************************/

    @Override
    public HyperlinkBuilderCommon rel(String rel){
        this.hyperlink.setRel(rel);
        return this;
    }

    @Override
    public HyperlinkBuilderLinkRelation withRel(){
        return this;
    }

    @Override
    public HyperlinkBuilderCommon self() {
        this.hyperlink.setRel(Hyperlink.SELF_REL);
        return this;
    }

    @Override
    public HyperlinkBuilderCommon collection() {
        this.hyperlink.setRel(Hyperlink.COLLECTION_REL);
        return this;
    }

    @Override
    public HyperlinkBuilderCommon item() {
        this.hyperlink.setRel(Hyperlink.ITEM_REL);
        return this;
    }

    @Override
    public HyperlinkBuilderCommon update() {
        this.hyperlink.setRel(Hyperlink.UPDATE_REL);
        return this;
    }

    @Override
    public HyperlinkBuilderCommon create() {
        this.hyperlink.setRel(Hyperlink.CREATE_REL);
        return this;
    }

    @Override
    public HyperlinkBuilderCommon delete() {
        this.hyperlink.setRel(Hyperlink.DELETE_REL);
        return this;
    }

    @Override
    public HyperlinkBuilderCommon authorization() {
        this.hyperlink.setRel(Hyperlink.AUTH_REL);
        return this;
    }

    /*************************************** Method ****************************************/

    @Override
    public HyperlinkBuilderCommon method(String method) {
        this.hyperlink.setMethod(method.toUpperCase()); 
        return this;
    }

    @Override
    public HyperlinkBuilderCommon method(HttpMethod method) {
        return this.method(method.toString());
    }

    @Override
    public HyperlinkBuilderCommon get() {
        return this.method(HttpMethod.GET);
    }

    @Override
    public HyperlinkBuilderCommon put() {
        return this.method(HttpMethod.PUT);
    }

    @Override
    public HyperlinkBuilderCommon post() {
        return this.method(HttpMethod.POST);
    }

    @Override
    public HyperlinkBuilderCommon options() {
        return this.method(HttpMethod.OPTIONS);
    }

    @Override
    public HyperlinkBuilderMethod withMethod() {
        return this;
    }

    /*************************************** Response Media Type ****************************************/

    @Override
    public HyperlinkBuilderCommon response(String mimeType) {
        this.hyperlink.setMimeType(mimeType);
        return this;
    }

    @Override
    public HyperlinkBuilderCommon response(MediaType mimeType) {
        return this.response(mimeType.toString());
    }

    /************************************************* Hyperlink to description of relation *************************/

    @Override
    public HyperlinkBuilderCommon relHref(String href) {
        this.hyperlink.setRelHref(href);
        return this;
    }

    @Override
    public HyperlinkBuilderCommon relHref(Link link) {
        return this.relHref(link.getHref());
    }

    /************************************************* Authentication *************************************************/

    @Override
    public HyperlinkBuilderCommon isAuthenticated() {
        this.isAuthenticated = true;
        return this;
    }

    @Override
    public HyperlinkBuilderCommon isNotAuthenticated() {
        this.isAuthenticated = false;
        return this;
    }

    @Override
    public HyperlinkBuilderCommon hasAuthority(String authority) {
        if(Objects.isNull(authorities)){
            this.authorities = new HashSet<String>();
        }
        this.authorities.add(authority);
        return this;
    }

    @Override
    public HyperlinkBuilderCommon hasRole(String role) {
        if(role.startsWith("ROLE_")){
            return this.hasAuthority(role);
        }else{
            return this.hasAuthority("ROLE_"+role);
        }
    }

    @Override
    public HyperlinkBuilderCommon hasScope(String scope) {
        if(scope.startsWith("SCOPE_")){
            return this.hasAuthority(scope);
        }else{
            return this.hasAuthority("SCOPE_"+scope);
        }
    }

    @Override
    public HyperlinkBuilderCommon allOf() {
        this.allOf = true;
        return this;
    }

    @Override
    public HyperlinkBuilderCommon anyOf() {
        this.allOf = false;
        return this;
    }

    @Override
    public Hyperlink build() {
        if(!Objects.isNull(isAuthenticated) || !Objects.isNull(authorities)){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(!Objects.isNull(isAuthenticated) && isAuthenticated.booleanValue() != authentication.isAuthenticated()){
                return null;
            }
            if(!Objects.isNull(authorities)){
                Set<String> grantedAuthorities = new HashSet<String>();
                for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
                    grantedAuthorities.add(grantedAuthority.getAuthority());
                }

                if(this.allOf){
                    if(!grantedAuthorities.containsAll(authorities)){
                        return null;
                    }
                }else{
                    if(!authorities.stream().anyMatch(grantedAuthorities::contains)){
                        return null;
                    }
                }   
            }

        }
        return hyperlink;
    }
}
