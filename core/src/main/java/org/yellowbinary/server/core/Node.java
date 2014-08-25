package org.yellowbinary.server.core;

import org.yellowbinary.server.core.model.Meta;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An interface that each module/add-on/plugin should implement if it adds a type with a \@Provides annotation.
 * While the modules/add-ons/plugins handle a rootNode and modify it, it will be of this type. When rendering
 * starts this will be turned into a DecoratedNode.
 *
 * @see org.yellowbinary.server.core.stereotypes.Provides
 */
public interface Node {

    /**
     * The node key (uuid for now) of the node
     *
     * @return a unique key for this node
     */
    String getKey();

    Node setKey(String key);

    /**
     * The base type of the node
     *
     * @return a page node type
     */
    String getType();

    Node setType(String type);

    Integer getWeight();

    Node setWeight(Integer weight);

    /**
     * All the available regions stored on this node.
     *
     * @return a set of region names that can be used for showing content
     */
    Set<String> getRegions();

    /**
     * A collection of children that should be rendered on the screen. Regions are determined by the theme variant used.
     *
     * @param region the area of the screen where this node should be rendered
     * @return all children for the region
     */
    List<Node> getChildren(String region);

    /**
     * Returns all children and their region
     * @return all children
     */
    Map<String, List<Node>> getChildren();

    /**
     * Add an node that should be rendered on the page with preloaded meta information. Regions are determined by the theme variant used.
     *
     *
     * @param node            the node to be rendered
     * @param meta              preloaded/static meta to be used for placing the node in the page
     * @return the newly added Node
     */
    Node addChild(Node node, Meta meta);

    /**
     * Add an node that should be rendered on the page with preloaded meta information. Regions are determined by the theme variant used.
     *
     *
     * @param node            the node to be rendered
     * @param meta              preloaded/static meta to be used for placing the node in the page
     * @param reorderChildrenBelow if true then all children below this new node will be reordered according to their individual weight
     * @return the newly added Node
     */
    Node addChild(Node node, Meta meta, boolean reorderChildrenBelow);

    /**
     * Removes an node so it is not rendered. Will force a reordering of all children below.
     *
     * @param node the node to be rendered
     * @return if an object matching the region and the node could be found and removed
     */
    boolean removeChild(Node node);

    /**
     * Checks if there are any children added to the node
     */
    boolean hasChildren();

    /**
     * Whether this type of node allows children.
     * @return true if allowed
     */
    boolean isChildrenAllowed();
}
