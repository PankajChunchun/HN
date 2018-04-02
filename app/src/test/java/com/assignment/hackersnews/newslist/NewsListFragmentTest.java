package com.assignment.hackersnews.newslist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.assignment.dataprovider.source.NewsItem;
import com.assignment.hackersnews.BuildConfig;
import com.assignment.hackersnews.R;
import com.assignment.hackersnews.commentlist.CommentListActivity;
import com.assignment.hackersnews.util.Constants;
import com.assignment.hackersnews.util.HNTestUtils;
import com.assignment.hackersnews.util.RobolectricUtils;
import java.util.ArrayList;
import java.util.List;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowIntent;
import org.robolectric.shadows.ShadowToast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pankaj Kumar on 02/04/18.
 * EAT | DRINK | CODE
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NewsListFragmentTest {

    private DummyNewsListFragment fragment;
    private LinearLayoutManager mockLayoutManager;
    private CommentListActivity commentListActivity;

    @Before
    public void setUp() {
        mockLayoutManager = Mockito.mock(LinearLayoutManager.class);

        fragment = new DummyNewsListFragment();
        fragment.setLayoutManager(mockLayoutManager);
        fragment = new DummyNewsListFragment();
        RobolectricUtils.startFragment(fragment);

        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_ARGS_ARTICLE, HNTestUtils.getNewsItem());
        commentListActivity = Robolectric.buildActivity(CommentListActivity.class)
                .create(args)
                .resume()
                .get();
    }

    @Test
    public void verifyShowPagedNewsArticle() {

        List<NewsItem> items = HNTestUtils.getNewsItems();
        fragment.showPagedNewsArticle(items, false);

        RecyclerView recyclerView = (RecyclerView) fragment.getView().findViewById(R.id.newscontent_list);
        NewsListAdapter adapter = (NewsListAdapter) recyclerView.getAdapter();

        Assert.assertEquals(adapter.getItemCount(), 2);
        Assert.assertEquals(adapter.getItem(0), items.get(0));

        fragment.showPagedNewsArticle(items, false);
        Assert.assertEquals(adapter.getItemCount(), 4);
        Assert.assertEquals(adapter.getItem(0), items.get(0));
    }

    @Test
    public void verifyShowProgress() {
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) fragment.getView()
                .findViewById(R.id.swipe_refresh_layout);

        fragment.showProgress(true);
        Assert.assertTrue(refreshLayout.isRefreshing());

        fragment.showProgress(false);
        Assert.assertTrue(!refreshLayout.isRefreshing());
    }

    @Test
    public void verifyShowNoNewsFound() {
        fragment.showNoNewsFound();
        assertEquals("No NEWS found", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void verifyOnError() {
        fragment.onError("Error occurred");
        assertEquals("Error occurred", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void verifyShowLoadingMoreProgress() {
        List<NewsItem> items = HNTestUtils.getNewsItems();
        fragment.showPagedNewsArticle(items, true);

        RecyclerView recyclerView = (RecyclerView) fragment.getView().findViewById(R.id.newscontent_list);
        NewsListAdapter adapter = (NewsListAdapter) recyclerView.getAdapter();

        int size = adapter.getItemCount();

        fragment.showLoadingMoreProgress(true);
        int newSize = adapter.getItemCount();
        assertTrue((size + 1) == newSize);

        fragment.showLoadingMoreProgress(false);
        newSize = adapter.getItemCount();
        assertTrue(size == newSize);
    }

    @Test
    public void verifyRetryPageLoad() {
        fragment.retryPageLoad();
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) fragment.getView()
                .findViewById(R.id.swipe_refresh_layout);
        Assert.assertTrue(refreshLayout.isRefreshing());
    }

    @Test
    public void verifyOnShowDetails() {
        NewsItem item = HNTestUtils.getNewsItem();
        fragment.onShowDetails(item);

        Intent startedIntent = Shadows.shadowOf(commentListActivity).getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);
        Assert.assertEquals(CommentListActivity.class, shadowIntent.getIntentClass());
    }

    @Test
    public void verifyOnShowDetails_no_comment_found() {
        NewsItem item = null;
        fragment.onShowDetails(item);
        assertEquals("No Comment Found.", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void verifyOnShowDetails_no_comment_found_kid_null() {
        NewsItem item = HNTestUtils.getNewsItem();
        item.setKids(null);
        fragment.onShowDetails(item);
        assertEquals("No Comment Found.", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void verifyOnShowDetails_no_comment_found_kid_empty() {
        NewsItem item = HNTestUtils.getNewsItem();
        item.setKids(new ArrayList<Integer>(0));
        fragment.onShowDetails(item);
        assertEquals("No Comment Found.", ShadowToast.getTextOfLatestToast());
    }

    public static class DummyNewsListFragment extends NewsListFragment {

        private LinearLayoutManager mockLayoutManager;

        public void setLayoutManager(LinearLayoutManager mockLayoutManager) {
            this.mockLayoutManager = mockLayoutManager;
        }

        public LinearLayoutManager getLayoutManager() {
            return mockLayoutManager;
        }
    }

    @Test
    public void verifyOnSaveInstanceState() {
        Bundle bundle = new Bundle();
        Assert.assertTrue(bundle.size() == 0);
        fragment.onSaveInstanceState(bundle);
        Assert.assertTrue(bundle.size() == 2);
        Assert.assertTrue(bundle.getInt("selected_pos", -100) != -100);
    }
}