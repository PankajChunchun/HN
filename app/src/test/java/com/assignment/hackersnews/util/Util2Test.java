package com.assignment.hackersnews.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.*;

/**
 * Created by Pankaj Kumar on 01/04/18.
 * EAT | DRINK | CODE
 */
public class Util2Test {

    @Test
    public void getTimeAgoTest() throws Exception {
        Calendar calendar = GregorianCalendar.getInstance();
        long now = calendar.getTimeInMillis();

        calendar.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND) - 1000);
        String result = Utils.getTimeAgo(now, calendar.getTimeInMillis());
        Assert.assertEquals("moments ago", result);

        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 1);
        result = Utils.getTimeAgo(now, calendar.getTimeInMillis());
        Assert.assertEquals("a minute ago", result);

        calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) - 9);
        result = Utils.getTimeAgo(now, calendar.getTimeInMillis());
        Assert.assertEquals("10 minutes ago", result);

        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - 1);
        result = Utils.getTimeAgo(now, calendar.getTimeInMillis());
        Assert.assertEquals("an hour ago", result);

        calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) - 9);
        result = Utils.getTimeAgo(now, calendar.getTimeInMillis());
        Assert.assertEquals("10 hours ago", result);

        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
        result = Utils.getTimeAgo(now, calendar.getTimeInMillis());
        Assert.assertEquals("yesterday", result);

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 200);
        result = Utils.getTimeAgo(now, calendar.getTimeInMillis());
        Assert.assertEquals("201 days ago", result);

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1000);
        result = Utils.getTimeAgo(now, calendar.getTimeInMillis());
        Assert.assertEquals("3 years ago", result);
    }
}