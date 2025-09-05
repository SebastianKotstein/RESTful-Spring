package de.skotstein.lib.spring.restfulspring.util;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import de.skotstein.lib.spring.restfulspring.model.entities.Hyperlink;

public interface HyperlinkBuilderCommon {

    public HyperlinkBuilderCommon href(String href);

    public HyperlinkBuilderCommon href(Link link);

    public HyperlinkBuilderCommon staticHref(Object invocationValue);

    public HyperlinkBuilderCommon templatedHref(Object invocationValue);

    public HyperlinkBuilderCommon rel(String rel);

    public HyperlinkBuilderLinkRelation withRel();

    public HyperlinkBuilderCommon method(String method);

    public HyperlinkBuilderCommon method(HttpMethod method);

    public HyperlinkBuilderMethod withMethod();

    public HyperlinkBuilderCommon response(String mimeType);

    public HyperlinkBuilderCommon response(MediaType mimeType);

    public HyperlinkBuilderCommon relHref(String href);

    public HyperlinkBuilderCommon relHref(Link link);

    public HyperlinkBuilderCommon isAuthenticated();

    public HyperlinkBuilderCommon isNotAuthenticated();

    public HyperlinkBuilderCommon hasAuthority(String authority);

    public HyperlinkBuilderCommon hasRole(String role);

    public HyperlinkBuilderCommon hasScope(String scope);

    public HyperlinkBuilderCommon allOf();

    public HyperlinkBuilderCommon anyOf();

    public Hyperlink build();
}
