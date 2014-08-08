package org.yellowbinary.cms.server.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.yellowbinary.cms.server.core.model.Meta;
import org.yellowbinary.cms.server.core.model.Release;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An interface that each module/add-on/plugin should implement if it adds a type with a \@Provides annotation.
 * While the modules/add-ons/plugins handle a rootNode and modify it, it will be of this type. When rendering
 * starts this will be turned into a DecoratedNode.
 *
 * @see org.yellowbinary.cms.server.core.stereotypes.Provides
 */
public class Node {

    private String key;

    private Integer version;

    private Release release;

    private String type;

    private Map<String, List<Element>> elements = Maps.newHashMap();

    public Node() {
    }

    /**
     * The node key (uuid for now) of the node
     *
     * @return a unique key for this node
     */
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /**
     * The base type of the node
     *
     * @return a page node type
     */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * The version of this node
     *
     * @return a version number
     */
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * The release this node is connected to. Every change must have a release.
     * @see Release
     * @return a release
     */
    public Release getRelease() {
        return release;
    }

    public void setRelease(Release release) {
        this.release = release;
    }

    /**
     * All the available regions stored on this node.
     *
     * @return a set of region names that can be used for showing content
     */
    public Set<String> getRegions() {
        return elements.keySet();
    }

    /**
     * A collection of UIElements that should be rendered on the screen. Regions are determined by the theme variant used.
     *
     * @param region the area of the screen where this element should be rendered
     * @return all uiElements for the region
     */
    public List<Element> getElements(String region) {
        return Collections.unmodifiableList(elements.get(region));
    }

    /**
     * Add an element that should be rendered on the page with preloaded meta information. Regions are determined by the theme variant used.
     *
     * @param element            the element to be rendered
     * @param meta              preloaded/static meta to be used for placing the element in the page
     * @return the newly added Element
     */
    public Element addElement(Element element, Meta meta) {
        return addElement(element, true, meta.getRegion(), meta.getWeight());
    }

    /**
     * Add an element that should be rendered on the page with preloaded meta information. Regions are determined by the theme variant used.
     *
     *
     * @param element            the element to be rendered
     * @param meta              preloaded/static meta to be used for placing the element in the page
     * @param reorderElementsBelow if true then all elements below this new element will be reordered according to their individual weight
     * @return the newly added Element
     */
    public Element addElement(Element element, Meta meta, boolean reorderElementsBelow) {
        return addElement(element, reorderElementsBelow, meta.getRegion(), meta.getWeight());
    }

    /**
     * Removes an element so it is not rendered. Will force a reordering of all elements below.
     *
     * @param element the element to be rendered
     * @return if an object matching the region and the element could be found and removed
     */
    public boolean removeElement(Element element) {
        for (String region : getRegions()) {
            List<Element> regionElements = elements.get(region.toLowerCase());
            if (regionElements.remove(element)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there are any elements added to the node
     */
    public boolean hasElements() {
        return !elements.isEmpty();
    }

    private Element addElement(Element element, boolean reorderElementsBelow, String region, int weight) {
        String regionKey = region.toLowerCase();
        if (!elements.containsKey(regionKey)) {
            elements.put(regionKey, Lists.<Element>newArrayList());
        }
        element.setWeight(weight);
        elements.get(regionKey).add(element);
        if (reorderElementsBelow) {
            ElementHelper.repositionElements(elements.get(regionKey), element);
        }
        ElementHelper.reorderElements(elements.get(regionKey));
        return element;
    }

}
