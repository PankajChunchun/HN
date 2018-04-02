package com.assignment.hackersnews.util;

import static org.robolectric.Shadows.shadowOf;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.text.Spanned;
import com.assignment.hackersnews.util.Utils;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.*;
import org.junit.runner.*;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowConnectivityManager;
import org.robolectric.shadows.ShadowNetworkInfo;

/**
 * Created by Pankaj Kumar on 31/03/18.
 * EAT | DRINK | CODE
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class UtilTest {

    private ConnectivityManager connectivityManager;
    private ShadowNetworkInfo shadowOfActiveNetworkInfo;
    private ShadowConnectivityManager shadowConnectivityManager;

    @Before
    public void setUp() {
        connectivityManager =
                (ConnectivityManager)
                        RuntimeEnvironment.application.getSystemService(Context.CONNECTIVITY_SERVICE);
        shadowConnectivityManager = shadowOf(connectivityManager);
        shadowOfActiveNetworkInfo = shadowOf(connectivityManager.getActiveNetworkInfo());
        Utils util = new Utils();
    }

    @Test
    public void checkNotNullTest() {
        String str = "";
        String result = Utils.checkNotNull(str, "Error Message");
        Assert.assertEquals("", result);
    }

    @Test
    public void checkNotNull1Test() {
        String str = null;
        try {
            Utils.checkNotNull(str, "Null Check");
        } catch (NullPointerException e) {
            Assert.assertEquals(e.getMessage(), "Null Check");
        }
    }

    @Test
    public void notifyUserTest() {
        Utils.notifyUser(ShadowApplication.getInstance().getApplicationContext(), "message");
    }

    @Test
    public void getActiveNetworkInfoTest() {
        Assert.assertEquals(connectivityManager.getActiveNetworkInfo(),
                Utils.getActiveNetworkInfo(ShadowApplication.getInstance().getApplicationContext()));
    }

    @Test
    public void isConnectedTest() {

        Assert.assertEquals(connectivityManager.getActiveNetworkInfo().isConnected(),
                Utils.isConnected(ShadowApplication.getInstance().getApplicationContext()));
    }

    @Config(sdk = Build.VERSION_CODES.N)
    @Test
    public void fomHtml_vNTest() {
        Spanned spanned = Utils.fromHtml("<b>hello");
        Object[] spans = spanned.getSpans(-1, 100, Object.class);
        Assert.assertEquals(1, spans.length);
        Object span = spans[0];
        Assert.assertEquals(0, spanned.getSpanStart(span));
        Assert.assertEquals(5, spanned.getSpanEnd(span));
    }

    @Test
    public void fromHtmlTest() {
        Spanned spanned = Utils.fromHtml("<b>hello");
        Object[] spans = spanned.getSpans(-1, 100, Object.class);
        Assert.assertEquals(1, spans.length);
        Object span = spans[0];
        Assert.assertEquals(0, spanned.getSpanStart(span));
        Assert.assertEquals(5, spanned.getSpanEnd(span));
    }

    @Test
    public void getTimeAgoTest() {
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

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 2000);
        result = Utils.getTimeAgo(now, calendar.getTimeInMillis());
        Assert.assertEquals("in the future", result);

        result = Utils.getTimeAgo(now, 0);
        Assert.assertEquals("in the future", result);
    }
}