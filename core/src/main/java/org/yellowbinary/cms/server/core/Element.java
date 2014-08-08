package org.yellowbinary.cms.server.core;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class Element {

    public String id;

    public String type;

    private Element parent;

    public Map<String, String> attributes = new WeakHashMap<>();

    private List<Element> children = Lists.newLinkedList();

    private String body;

    private int weight;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public void addAttribute(String key, String value) {
        this.attributes.put(key, value);
    }

    public List<Element> getChildren() {
        return children;
    }

    public void setChildren(List<Element> children) {
        this.children = children;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }
}
