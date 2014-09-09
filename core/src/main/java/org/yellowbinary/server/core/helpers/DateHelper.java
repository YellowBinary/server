package org.yellowbinary.server.core.helpers;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public class DateHelper {

    public static Period parsePeriod(String string) {
        PeriodFormatter format = new PeriodFormatterBuilder().
                appendDays().appendSuffix("d", "d").printZeroRarelyFirst().
                appendHours().appendSuffix("h", "h").printZeroRarelyFirst().
                appendMinutes().appendSuffix("m", "m").
                toFormatter();
        return format.parsePeriod(string);
    }

    public static String toString(Period period) {
        return formatIfNotZero(period.getDays(), "days", "day") +
                formatIfNotZero(period.getHours(), "hours", "hour") +
                formatIfNotZero(period.getMinutes(), "minutes", "minute");
    }

    private static String formatIfNotZero(int value, String plural, String singleton) {
        if (value > 0) {
            if (value > 1) {
                return "" + value + " " + plural;
            }
            return "" + value + " " + singleton;
        }
        return "";
    }

}
