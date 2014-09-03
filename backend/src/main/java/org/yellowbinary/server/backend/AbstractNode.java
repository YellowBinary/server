package org.yellowbinary.server.backend;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.yellowbinary.server.backend.model.Meta;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Comparator;


public abstract class AbstractNode implements Node {

    private Integer weight = 0;

    private Map<String, List<Node>> children = Maps.newHashMap();

    @Override
    public Integer getWeight() {
        return weight;
    }

    @Override
    public Node setWeight(Integer weight) {
        this.weight = weight;
        return this;
    }

    @Override
    public Set<String> getRegions() {
        return children.keySet();
    }

    @Override
    public List<Node> getChildren(String region) {
        return children.get(region);
    }

    @Override
    public Map<String, List<Node>> getChildren() {
        return children;
    }

    @Override
    public Node addChild(Node node, Meta meta) {
        return addNode(node, true, meta.getRegion(), meta.getWeight());
    }

    @Override
    public Node addChild(Node node, Meta meta, boolean reorderChildrenBelow) {
        return addNode(node, reorderChildrenBelow, meta.getRegion(), meta.getWeight());
    }

    @Override
    public boolean removeChild(Node node) {
        for (String region : getRegions()) {
            List<Node> regionChildren = children.get(region.toLowerCase());
            if (regionChildren.remove(node)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    private Node addNode(Node node, boolean reorderChildrenBelow, String region, int weight) {
        String regionKey = region.toLowerCase();
        if (!children.containsKey(regionKey)) {
            children.put(regionKey, Lists.<Node>newArrayList());
        }
        node.setWeight(weight);
        children.get(regionKey).add(node);
        if (reorderChildrenBelow) {
            Collections.sort(children.get(regionKey), new Comparator<Node>() {
                @Override
                public int compare(Node node11, Node node1) {
                    return node11.getWeight().compareTo(node1.getWeight());
                }
            });
        }
        return node;
    }
}
