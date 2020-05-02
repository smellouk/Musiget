package io.mellouk.common.utils;

import android.annotation.SuppressLint;

import java.util.concurrent.TimeUnit;

public class FormatUtils {
    public FormatUtils() {
    }

    @SuppressLint("DefaultLocale")
    public String toTime(final int milliseconds) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(milliseconds),
                TimeUnit.MILLISECONDS.toSeconds(milliseconds) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliseconds))
        );
    }

    public int toProgress(final int currentPosition, final int duration) {
        return currentPosition * 100 / duration;
    }
}
