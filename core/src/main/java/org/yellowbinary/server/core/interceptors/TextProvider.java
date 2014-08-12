package org.yellowbinary.server.core.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.yellowbinary.server.core.Core;
import org.yellowbinary.server.core.Node;
import org.yellowbinary.server.core.NodeNotFoundException;
import org.yellowbinary.server.core.dao.TextDao;
import org.yellowbinary.server.core.model.RootNode;
import org.yellowbinary.server.core.model.content.Text;
import org.yellowbinary.server.core.stereotypes.Interceptor;
import org.yellowbinary.server.core.stereotypes.Provides;

import java.util.Map;

@Interceptor
public class TextProvider {

    @Autowired
    private TextDao textDao;

    @Provides(base = Core.Base.NODE, with = Text.TYPE)
    public Node loadText(RootNode node, String withType, Map<String, Object> args) throws NodeNotFoundException {

        Text text = textDao.findWithIdentifier(node.getKey());
        if (text == null) {
            throw new NodeNotFoundException(node.getKey());
        }
        return text;
    }

}
