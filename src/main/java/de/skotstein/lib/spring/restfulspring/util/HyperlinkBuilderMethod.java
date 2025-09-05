package de.skotstein.lib.spring.restfulspring.util;

public interface HyperlinkBuilderMethod {

    public HyperlinkBuilderCommon get();

    public HyperlinkBuilderCommon put();

    public HyperlinkBuilderCommon post();

    public HyperlinkBuilderCommon delete();

    public HyperlinkBuilderCommon options();
}
