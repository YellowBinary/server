package org.yellowbinary.cms.server.core;

import org.yellowbinary.cms.server.core.model.Meta;

import java.util.List;
import java.util.Set;

/**
 * An interface that each module/add-on/plugin should implement if it adds a type with a \@Provides annotation.
 * While the modules/add-ons/plugins handle a rootNode and modify it, it will be of this type. When rendering
 * starts this will be turned into a DecoratedNode.
 *
 * @see org.yellowbinary.cms.server.core.stereotypes.Provides
 */
public interface Node {

    /**
     * The node key (uuid for now) of the node
     *
     * @return a unique key for this node
     */
    String getKey();

    /**
     * The base type of the node
     *
     * @return a page node type
     */
    String getType();

    /**
     * The version of this node
     *
     * @return a version number
     */
    Integer getVersion();


    /**
     * All the available regions stored on this node.
     *
     * @return a set of region names that can be used for showing content
     */
    Set<String> getRegions();

    /**
     * A collection of UIElements that should be rendered on the screen. Regions are determined by the theme variant used.
     *
     * @param region the area of the screen where this element should be rendered
     * @return all uiElements for the region
     */
    List<Element> getElements(String region);

    /**
     * Add an element that should be rendered on the page with preloaded meta information. Regions are determined by the theme variant used.
     *
     *
     * @param element            the element to be rendered
     * @param meta              preloaded/static meta to be used for placing the element in the page
     * @return the newly added Element
     */
    Element addElement(Element element, Meta meta);

    /**
     * Add an element that should be rendered on the page with preloaded meta information. Regions are determined by the theme variant used.
     *
     *
     * @param element            the element to be rendered
     * @param meta              preloaded/static meta to be used for placing the element in the page
     * @param reorderElementsBelow if true then all elements below this new element will be reordered according to their individual weight
     * @return the newly added Element
     */
    Element addElement(Element element, Meta meta, boolean reorderElementsBelow);

    /**
     * Removes an element so it is not rendered. Will force a reordering of all elements below.
     *
     * @param element the element to be rendered
     * @return if an object matching the region and the element could be found and removed
     */
    boolean removeElement(Element element);

    /**
     * Checks if there are any elements added to the node
     */
    boolean hasElements();

}
