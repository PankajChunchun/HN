package com.assignment.hackersnews.newslist;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;
import com.assignment.dataprovider.source.NewsItem;
import com.assignment.hackersnews.BuildConfig;
import com.assignment.hackersnews.R;
import com.assignment.hackersnews.commentlist.CommentListActivity;
import org.junit.*;
import org.junit.runner.*;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

/**
 * Created by Pankaj Kumar on 01/04/18.
 * EAT | DRINK | CODE
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class NewsListActivityTest {

    NewsListActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity(NewsListActivity.class)
                .create()
                .resume()
                .get();

        activity.getDeps();
    }

    @Test
    public void shouldNotBeNull() throws Exception {
        junit.framework.Assert.assertNotNull(activity);
    }

    @Test
    public void validateIfContainerPresent() {
        FrameLayout container = (FrameLayout) activity.findViewById(R.id.fragment_container);
        Assert.assertNotNull("Container for fragment is not null", container);
    }

    @Test
    public void validateIfNewsListBeingShown() {
        NewsListFragment fragment = (NewsListFragment) activity.getSupportFragmentManager().
                findFragmentByTag(NewsListFragment.class.getSimpleName());

        Assert.assertTrue("Fragment Added", fragment.isAdded());
    }

    @Test
    public void shouldHaveNewsListFragment() throws Exception {
        Assert.assertNotNull(activity.getSupportFragmentManager()
                .findFragmentByTag(NewsListFragment.class.getSimpleName()));
    }

    @Test
    public void showCommentsList() {
        NewsListFragment fragment = (NewsListFragment) activity.getSupportFragmentManager().
                findFragmentByTag(NewsListFragment.class.getSimpleName());
        RecyclerView recyclerView = (RecyclerView) fragment.getView().findViewById(R.id.newscontent_list);
        NewsListAdapter adapter = (NewsListAdapter) recyclerView.getAdapter();

        int size = adapter.getNewsItemList().size();
        NewsItem item;
        int position = -1;
        for (int i = 0; i < size; i++) {
            item = adapter.getNewsItemList().get(i);
            if (item.getKids() != null && item.getKids().size() > 0) {
                position = i;
                break;
            }
        }

        if (position != -1) {
            recyclerView.getChildAt(position).performClick();
            Intent expectedIntent = new Intent(activity, CommentListActivity.class);
            ShadowActivity shadowActivity = Shadows.shadowOf(activity);
            Intent actualIntent = shadowActivity.getNextStartedActivity();

            Assert.assertTrue(actualIntent.filterEquals(expectedIntent));
        }
    }

    @Test
    public void titleIsCorrect() throws Exception {
        Assert.assertTrue(activity.getTitle().toString().equals("HackersNews"));
    }
}