package com.assignment.hackersnews.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.Spanned;
import android.widget.Toast;

/**
 * Utility class which provides common utility methods.
 *
 * Created by Pankaj Kumar on 27/03/18.
 * EAT | DRINK | CODE
 */
public final class Utils {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    /**
     * Ensures that an object reference passed as a parameter to the calling
     * method is not null.
     *
     * @param reference    an object reference
     * @param errorMessage the exception message to use if the check fails; will
     *                     be converted to a string using {@link String#valueOf(Object)}
     * @return the non-null reference that was validated
     * @throws NullPointerException if {@code reference} is null
     */
    public static @NonNull
    <T> T checkNotNull(final T reference, final Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    /**
     * Notification to user about some events, errors for a short duration.
     *
     * @param context - {@link Context}
     * @param message - A message which should show to user
     */
    public static void notifyUser(Context context, String message) {
        // This can be changed to dialog
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Check if device has internet connection.
     *
     * <p>
     * This methods needs {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}
     * </p>
     *
     * @return {@code true} if device is connected with internet, {@code false} otherwise.
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isConnected();
    }

    /**
     * Get {@link NetworkInfo} for active network. If internet connection not available it will return null.
     *
     * <p>
     * This methods needs {@code <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />}
     * </p>
     *
     * @return NetworkInfo
     */
    @SuppressLint("MissingPermission")
    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager manager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return null;
        }
        return manager.getActiveNetworkInfo();
    }

    /**
     * Utility which handles {@link Html#fromHtml(String, int)} deprecation and uses
     * version specific method to get {@link Spanned} text.
     *
     * @param html - text with html tags
     * @return - {@link Spanned}
     */
    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    /**
     * Get human readable past time for given milliseconds.
     *
     * @param currentTimeMillis - Current time in milliseconds. Can be used {@link System#currentTimeMillis()}
     * @param inputTimeMillis   - Time in milliseconds
     */
    public static String getTimeAgo(long currentTimeMillis, long inputTimeMillis) {

        if (inputTimeMillis > currentTimeMillis || inputTimeMillis <= 0) {
            return "in the future";
        }

        final long diff = currentTimeMillis - inputTimeMillis;
        if (diff < MINUTE_MILLIS) {
            return "moments ago";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 60 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 2 * HOUR_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            long days = diff / DAY_MILLIS;
            if (days > 365) {
                return days / 365 + " years ago";
            } else {
                return days + " days ago";
            }
        }
    }
}