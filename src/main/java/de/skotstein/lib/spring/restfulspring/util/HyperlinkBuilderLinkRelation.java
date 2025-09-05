package de.skotstein.lib.spring.restfulspring.util;

public interface HyperlinkBuilderLinkRelation {

    public HyperlinkBuilderCommon self();

    public HyperlinkBuilderCommon collection();

    public HyperlinkBuilderCommon item();

    public HyperlinkBuilderCommon update();

    public HyperlinkBuilderCommon create();

    public HyperlinkBuilderCommon delete();

    public HyperlinkBuilderCommon authorization();
}
