package org.yellowbinary.cms.server.core.preview;

import org.joda.time.DateTime;

public interface Ticket {

    boolean isValid();

    String userId();

    String token();

    DateTime validUntil();

    DateTime preview();

}
