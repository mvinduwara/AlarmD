package com.alarmclock.ui.util;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class TimeFormatter {

    private static final DateTimeFormatter FORMAT_24H = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FORMAT_12H = DateTimeFormatter.ofPattern("hh:mm a");

    private TimeFormatter() {
    }

    public static String format(LocalTime time, boolean use24Hour) {
        return time.format(use24Hour ? FORMAT_24H : FORMAT_12H);
    }

    public static String formatCountdown(Duration duration) {
        long totalMinutes = Math.max(duration.toMinutes(), 0);
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;

        if (hours == 0 && minutes == 0) {
            return "less than a minute";
        }

        StringBuilder sb = new StringBuilder();
        if (hours > 0) {
            sb.append(hours).append(hours == 1 ? " hour" : " hours");
        }
        if (minutes > 0) {
            if (sb.length() > 0) {
                sb.append(" ");
            }
            sb.append(minutes).append(minutes == 1 ? " minute" : " minutes");
        }
        return sb.toString();
    }
}