package org.yellowbinary.cms.server.core;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ElementHelper {

    public static <T extends Element> T copyBasicAttributes(Element source, Class<T> targetClass) {
        try {
            T target = targetClass.newInstance();
            copyBasicAttributes(source, target);
            return target;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate target: "+targetClass.getName(), e);
        }

    }

    public static void copyBasicAttributes(Element source, Element target) {
        target.setKey(source.getKey());
        target.setWeight(source.getWeight());
        for (Object attributeKey : source.getAttributes().keySet()) {
            target.addAttribute((String) attributeKey, source.getAttributes().get(attributeKey));
        }
    }

    public static Map<String, String> combineAttributes(Map<String, String> map1, Map<String, String> map2) {
        Map<String, String> result = Maps.newHashMap(map1);
        for (String name : map2.keySet()) {
            if (result.containsKey(name)) {
                result.put(name, result.get(name).concat(" ").concat(map2.get(name)));
            } else {
                result.put(name, map2.get(name));
            }
        }
        return result;
    }

    public static void reorderElements(List<Element> elements) {
        Collections.sort(elements, new Comparator<Element>() {
            @Override
            public int compare(Element element, Element element1) {
                return new Integer(element.getWeight()).compareTo(element1.getWeight());
            }
        });
    }

    public static void repositionElements(List<Element> elements, Element element) {
        for (Element elem : elements) {
            if (elem.getWeight() >= element.getWeight()) {
                elem.setWeight(elem.getWeight() + 1);
            }
        }
    }

}
