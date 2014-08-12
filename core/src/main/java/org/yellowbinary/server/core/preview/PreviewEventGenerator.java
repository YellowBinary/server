package org.yellowbinary.server.core.preview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yellowbinary.server.core.*;
import org.yellowbinary.server.core.context.Context;
import org.yellowbinary.server.core.event.ProvidesEventGenerator;

@Service
public class PreviewEventGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(PreviewEventGenerator.class);

    @Autowired
    private ProvidesEventGenerator providesEventGenerator;

    public static final String REQUEST_PREVIEW_TOKEN = "yellowbinary.preview.token";

    public Ticket getValidTicket() throws ModuleException, NodeLoadException {
        Ticket ticket = (Ticket) Context.current().getAttribute(REQUEST_PREVIEW_TOKEN);
        if (ticket == null) {
            try {
                ticket = providesEventGenerator.triggerInterceptor(null, Core.Base.PREVIEW, Core.With.PREVIEW_TOKEN);
                if (ticket != null) {
                    Context.current().addAttribute(REQUEST_PREVIEW_TOKEN, ticket);
                }
            } catch (NoSuchProviderException e) {
                LOG.error("No provider for PreviewTicket's available.", e);
            }
        }

        return ticket;
    }

}
