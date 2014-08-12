package org.yellowbinary.cms.server.core.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.yellowbinary.cms.server.core.Core;
import org.yellowbinary.cms.server.core.Node;
import org.yellowbinary.cms.server.core.NodeNotFoundException;
import org.yellowbinary.cms.server.core.dao.TextDao;
import org.yellowbinary.cms.server.core.model.RootNode;
import org.yellowbinary.cms.server.core.model.content.Text;
import org.yellowbinary.cms.server.core.stereotypes.Interceptor;
import org.yellowbinary.cms.server.core.stereotypes.Provides;

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
