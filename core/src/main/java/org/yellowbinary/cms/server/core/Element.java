package org.yellowbinary.cms.server.core;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class Element extends AbstractNode {

    public String key;

    public String type;

    private Element parent;

    public Map<String, String> attributes = new WeakHashMap<>();

    private List<Element> children = Lists.newLinkedList();

    private String body;

    private int weight;

    public String getKey() {
        return key;
    }

    public Element setKey(String key) {
        this.key = key;
        return this;
    }

    @Override
    public Integer getVersion() {
        return 0;
    }

    public String getType() {
        return type;
    }

    public Element setType(String type) {
        this.type = type;
        return this;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public Element setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
        return this;
    }

    public Element addAttribute(String key, String value) {
        this.attributes.put(key, value);
        return this;
    }

    public List<Element> getChildren() {
        return children;
    }

    public Element setChildren(List<Element> children) {
        this.children = children;
        return this;
    }

    public Element addChild(Element element) {
        this.children.add(element);
        return this;
    }

    public String getBody() {
        return body;
    }

    public Element setBody(String body) {
        this.body = body;
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public Element setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public Element getParent() {
        return parent;
    }

    public Element setParent(Element parent) {
        this.parent = parent;
        return this;
    }

}
