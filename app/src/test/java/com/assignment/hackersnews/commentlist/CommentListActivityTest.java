package com.assignment.hackersnews.commentlist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import com.assignment.dataprovider.source.NewsItem;
import com.assignment.hackersnews.BuildConfig;
import com.assignment.hackersnews.R;
import com.assignment.hackersnews.util.Constants;
import java.util.Arrays;
import org.junit.*;
import org.junit.runner.*;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowIntent;

/**
 * Created by Pankaj Kumar on 31/03/18.
 * EAT | DRINK | CODE
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CommentListActivityTest {

    CommentListActivity activity;
    ActivityController controller;

    @Before
    public void setUp() {
        NewsItem item = new NewsItem("norvig", 8863,
                Arrays.asList(9224, 8952, 8917, 8884, 8887, 8943, 8869, 8940, 8908, 8958, 9005, 8873, 9671, 9067),
                1175714200, "story", 71, 104, "My YC app: Dropbox - Throw away your USB drive",
                "http://www.getdropbox.com/u/2/screencast.html");
        Intent intent = new Intent(ShadowApplication.getInstance().getApplicationContext(),
                CommentListActivity.class);
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_ARGS_ARTICLE, item);
        intent.putExtras(args);
        controller = Robolectric.buildActivity(CommentListActivity.class, intent);
        activity = (CommentListActivity) controller.create()
                .resume().get();
    }

    @Test
    public void showTest() {
        CommentListActivity.show(ShadowApplication.getInstance().getApplicationContext(),
                new NewsItem());
        Intent startedIntent = Shadows.shadowOf(activity).getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);
        Assert.assertEquals(CommentListActivity.class, shadowIntent.getIntentClass());
    }

    @Test
    public void shouldNotBeNull() {
        junit.framework.Assert.assertNotNull(activity);
    }

    @Test
    public void validateIfContainerPresent() {
        FrameLayout container = (FrameLayout) activity.findViewById(R.id.fragment_container);
        Assert.assertNotNull("Container for fragment is not null", container);
    }

    @Test
    public void validateIfCommentListBeingShown() {
        CommentListFragment fragment = (CommentListFragment) activity.getSupportFragmentManager().
                findFragmentByTag(CommentListFragment.class.getSimpleName());

        Assert.assertTrue("Fragment Added", fragment.isAdded());
    }

    @Test
    public void titleIsCorrect() {
        Assert.assertTrue(activity.getTitle().toString().equals("HackersNews"));
    }


    @Test
    public void validateSavedInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putString("key", "value should persist on orientation change");
        controller.saveInstanceState(bundle).pause().stop().destroy();
        controller = Robolectric.buildActivity(CommentListActivity.class)
                .create(bundle)
                .start()
                .restoreInstanceState(bundle)
                .resume().visible();
        activity = (CommentListActivity) controller.get();

        Assert.assertNotNull("object must not be null", activity);
    }
}