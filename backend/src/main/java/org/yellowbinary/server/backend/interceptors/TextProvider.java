package org.yellowbinary.server.backend.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.yellowbinary.server.backend.Backend;
import org.yellowbinary.server.backend.Node;
import org.yellowbinary.server.backend.NodeNotFoundException;
import org.yellowbinary.server.backend.dao.TextDao;
import org.yellowbinary.server.backend.model.RootNode;
import org.yellowbinary.server.backend.model.content.Text;
import org.yellowbinary.server.core.stereotypes.Interceptor;
import org.yellowbinary.server.core.stereotypes.Provides;

import java.util.Map;

@Interceptor
public class TextProvider {

    @Autowired
    private TextDao textDao;

    @Provides(base = Backend.Base.NODE, with = Text.TYPE)
    public Node loadText(RootNode node, String withType, Map<String, Object> args) throws NodeNotFoundException {

        Text text = textDao.findWithIdentifier(node.getKey());
        if (text == null) {
            throw new NodeNotFoundException(node.getKey());
        }
        return text;
    }

}
