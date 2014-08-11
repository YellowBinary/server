package org.yellowbinary.cms.server.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.yellowbinary.cms.server.core.model.Meta;
import org.yellowbinary.cms.server.core.model.Release;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractNode implements Node {

    private Release release;

    private Map<String, List<Element>> elements = Maps.newHashMap();

    @Override
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
