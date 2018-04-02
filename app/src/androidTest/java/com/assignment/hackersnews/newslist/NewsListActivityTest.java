package com.assignment.hackersnews.newslist;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.assignment.hackersnews.R;
import org.junit.*;
import org.junit.runner.*;

/**
 * Created by Pankaj Kumar on 01/04/18.
 * EAT | DRINK | CODE
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewsListActivityTest {
    @Rule
    public ActivityTestRule<NewsListActivity> rule = new ActivityTestRule<>(NewsListActivity.class);

    @Test
    public void shouldUpdateTextAfterButtonClick() {
        Espresso.onView(ViewMatchers.withId(R.id.fragment_container));

        // onView(withId(R.id.text)).check(matches(withText("Hello World!")));
    }
}