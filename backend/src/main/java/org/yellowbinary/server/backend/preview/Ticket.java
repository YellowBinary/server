package org.yellowbinary.server.backend.preview;

import org.joda.time.DateTime;

public interface Ticket {

    String getUserId();

    String getToken();

    DateTime getValidUntilDateTime();

    DateTime getPreviewDateTime();

}
