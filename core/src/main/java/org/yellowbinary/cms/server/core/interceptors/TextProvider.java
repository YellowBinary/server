package org.yellowbinary.cms.server.core.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.yellowbinary.cms.server.core.Core;
import org.yellowbinary.cms.server.core.Element;
import org.yellowbinary.cms.server.core.Node;
import org.yellowbinary.cms.server.core.NodeNotFoundException;
import org.yellowbinary.cms.server.core.dao.TextDao;
import org.yellowbinary.cms.server.core.model.RootNode;
import org.yellowbinary.cms.server.core.model.Text;
import org.yellowbinary.cms.server.core.stereotypes.Interceptor;
import org.yellowbinary.cms.server.core.stereotypes.OnLoad;
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

    @OnLoad(base = Core.Base.NODE, with = Text.TYPE)
    public Element decorateText(Node node, String withType, Map<String, Object> args) {

        Text text = (Text) node;
        if (text != null) {
            return new Element().setKey(node.getKey()).
                    addChild(new Element().setKey(text.getKey()).setType(Text.TYPE).setBody(text.getValue()));
        }

        //TODO: Handle this somehow, in dev/admin maybe show a Element with a warning message and in prod swallow error?
        return null;
    }

}
