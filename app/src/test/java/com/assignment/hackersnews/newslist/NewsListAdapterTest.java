package com.assignment.hackersnews.newslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.assignment.dataprovider.source.NewsItem;
import com.assignment.hackersnews.BuildConfig;
import com.assignment.hackersnews.R;
import com.assignment.hackersnews.commentlist.CommentListActivity;
import com.assignment.hackersnews.newslist.NewsListAdapter.LoadMoreVH;
import com.assignment.hackersnews.newslist.NewsListAdapter.NewsViewHolder;
import com.assignment.hackersnews.newslist.NewsListAdapter.RowType;
import com.assignment.hackersnews.util.Constants;
import com.assignment.hackersnews.util.HNTestUtils;
import java.util.List;
import org.junit.*;
import org.junit.runner.*;
import org.mockito.*;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowIntent;

/**
 * Created by Pankaj Kumar on 02/04/18.
 * EAT | DRINK | CODE
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NewsListAdapterTest {

    private NewsListAdapter adapter;
    private NewsListAdapter.NewsViewHolder newsViewHolder;
    private CommentListActivity activity;

    @Before
    public void setUp() {
        adapter = new NewsListAdapter(ShadowApplication.getInstance().getApplicationContext());
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_ARGS_ARTICLE, HNTestUtils.getNewsItems().get(0));
        //intent.putExtras(args);
        activity = Robolectric.buildActivity(CommentListActivity.class)
                .create(args)
                .resume()
                .get();
    }

    @Test
    public void itemCount() {
        adapter.addAll(HNTestUtils.getNewsItems());
        Assert.assertEquals(adapter.getItemCount(), 2);
    }

    @Test
    public void getItemAtPosition() {
        List<NewsItem> rows = HNTestUtils.getNewsItems();
        adapter.addAll(rows);
        Assert.assertEquals(adapter.getItem(0), rows.get(0));
        Assert.assertEquals(adapter.getItem(1), rows.get(1));
    }

    @Test
    public void onCreateViewHolder_forAllTypes() {
        RecyclerView.ViewHolder loadMore = adapter.onCreateViewHolder(new FrameLayout(RuntimeEnvironment.application),
                RowType.LOAD_MORE);
        RecyclerView.ViewHolder newsRow = adapter.onCreateViewHolder(new FrameLayout(RuntimeEnvironment.application),
                RowType.NEWS_ROW);

        LayoutInflater inflater = (LayoutInflater) RuntimeEnvironment.application
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newsItemView = inflater.inflate(R.layout.news_list_row_view, null, false);
        NewsViewHolder newsViewActual = adapter.new NewsViewHolder(newsItemView);
        View loadMoreView = inflater.inflate(R.layout.load_more_view, null, false);
        LoadMoreVH loadMoreActual = adapter.new LoadMoreVH(loadMoreView);

        Assert.assertEquals(loadMore.getItemViewType(), loadMoreActual.getItemViewType());
        Assert.assertEquals(newsRow.getItemViewType(), newsViewActual.getItemViewType());
    }

    @Test
    public void onBindViewHolder_LoadMore_ErrorView() {
        List<NewsItem> rows = HNTestUtils.getNewsItems();
        adapter.addAll(rows);
        adapter.addLoadingFooter();

        LayoutInflater inflater = (LayoutInflater) RuntimeEnvironment.application
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View loadMoreView = inflater.inflate(R.layout.load_more_view, null, false);
        LoadMoreVH loadMoreActual = adapter.new LoadMoreVH(loadMoreView);
        adapter.onBindViewHolder(loadMoreActual, adapter.getItemCount() - 1);

        Assert.assertTrue(loadMoreActual.mProgressBar.getVisibility() == View.VISIBLE);

        adapter.showRetry(true, "showing error text");
        TextView errorView = (TextView) loadMoreActual.itemView.findViewById(R.id.loadmore_errortxt);
        Assert.assertTrue(errorView.getVisibility() == View.VISIBLE);
    }

    @Test
    public void onBindViewHolder_setsNewsInfoAndClickEventForRowView() {
        List<NewsItem> rows = HNTestUtils.getNewsItems();
        adapter.addAll(rows);

        LayoutInflater inflater = (LayoutInflater) RuntimeEnvironment.application
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View newsItemView = inflater.inflate(R.layout.news_list_row_view, null, false);
        newsViewHolder = adapter.new NewsViewHolder(newsItemView);
        adapter.onBindViewHolder(newsViewHolder, 0);

        Assert.assertEquals(newsViewHolder.mTitleTv.getText().toString(),
                "My YC app: Dropbox");
        Assert.assertEquals(newsViewHolder.mAddedByTv.getText().toString(),
                "(www.getdropbox.com)");

        newsViewHolder.itemView.performClick();

        CommentListActivity.show(ShadowApplication.getInstance().getApplicationContext(),
                new NewsItem());
        Intent startedIntent = Shadows.shadowOf(activity).getNextStartedActivity();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);
        Assert.assertEquals(CommentListActivity.class, shadowIntent.getIntentClass());
    }

    @Test
    public void verifyAdd_remove_LoadingFooter() {
        List<NewsItem> rows = HNTestUtils.getNewsItems();
        adapter.addAll(rows);
        int currentSize = adapter.getItemCount();
        adapter.addLoadingFooter();
        int newSize = adapter.getItemCount();

        Assert.assertTrue((currentSize < newSize) && (currentSize == newSize - 1));

        adapter.removeLoadingFooter();
        int finalSize = adapter.getItemCount();
        Assert.assertTrue(currentSize == finalSize);
    }

    @Test
    public void verifyRefresh() {
        List<NewsItem> rows = HNTestUtils.getNewsItems();
        adapter.addAll(rows);
        rows.add(HNTestUtils.getNewsItem());
        rows.add(HNTestUtils.getNewsItem());
        rows.add(HNTestUtils.getNewsItem());
        rows.add(HNTestUtils.getNewsItem());

        adapter.refresh(rows);
        Assert.assertTrue(adapter.getItemCount() == 6);
    }

    @Test
    public void verifyshowRetry() {
        List<NewsItem> rows = HNTestUtils.getNewsItems();
        adapter.addAll(rows);

        rows.add(HNTestUtils.getNewsItem());
        rows.add(HNTestUtils.getNewsItem());
        rows.add(HNTestUtils.getNewsItem());
        rows.add(HNTestUtils.getNewsItem());

        adapter.refresh(rows);
        Assert.assertTrue(adapter.getItemCount() == 6);
    }
}
