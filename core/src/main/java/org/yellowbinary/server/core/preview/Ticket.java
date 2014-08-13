package org.yellowbinary.server.core.preview;

import org.joda.time.DateTime;

public interface Ticket {

    String getUserId();

    String getToken();

    DateTime getValidUntilDateTime();

    DateTime getPreviewDateTime();

}
