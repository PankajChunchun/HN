package com.assignment.hackersnews;

import org.junit.*;
import org.junit.runner.*;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * Created by Pankaj Kumar on 02/04/18.
 * EAT | DRINK | CODE
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class HNApplicationTest {

    HNApplication app;

    @Before
    public void setup() {
        app = (HNApplication) RuntimeEnvironment.application.getApplicationContext();
    }

    @Test
    public void shouldInitialisedTextForTest() {
        Assert.assertEquals("HNApplication onCreate called", app.getTextForTest());
    }
}
