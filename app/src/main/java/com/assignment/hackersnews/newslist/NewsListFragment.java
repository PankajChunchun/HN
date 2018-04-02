package com.assignment.hackersnews.newslist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.assignment.dataprovider.source.NewsItem;
import com.assignment.dataprovider.source.remote.NetworkServiceManager;
import com.assignment.hackersnews.BaseFragment;
import com.assignment.hackersnews.R;
import com.assignment.hackersnews.commentlist.CommentListActivity;
import com.assignment.hackersnews.newslist.NewsListAdapter.OnAdapterActionCallback;
import com.assignment.hackersnews.newslist.NewsListContract.Presenter;
import com.assignment.hackersnews.util.Utils;
import java.util.List;
import javax.inject.Inject;

/**
 * {@link android.support.v4.app.Fragment} which holds {@link RecyclerView} to show news list
 *
 * Created by Pankaj Kumar on 28/03/18.
 * EAT | DRINK | CODE
 */
public class NewsListFragment extends BaseFragment implements NewsListContract.View, OnAdapterActionCallback {

    /**
     * Key is being used to store state of scrolled position in news list before orientation change
     */
    private static final String KEY_SELECTED_POS = "selected_pos";
    /**
     * Key is being used to store state of progress state
     */
    private static final String KEY_WAS_PROGRESS_ALIVE = "progress_being_shown";
    @Inject
    public NetworkServiceManager mService;
    private Context mContext;
    private View mRootView;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NewsListAdapter mAdapter;
    private NewsListContract.Presenter mPresenter;
    private boolean isLoading = false;
    private NewsListViewModal mNewsListViewModal;
    /**
     * This variable is being used to cached first visible position before orientation change or going to
     * details screen. As well as blocks list updation from ViewModal if service call was made.
     */
    private int mLastPosition = -1;

    public NewsListFragment() {
        // Requires empty public constructor
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.news_list_fragment_layout, container, false);

        getDeps().inject(this);
        mPresenter = new NewsListPresenter(mService, this);

        initViews();

        if (savedInstanceState != null) {
            mLastPosition = savedInstanceState.getInt(KEY_SELECTED_POS, -1);
            mSwipeRefreshLayout.setRefreshing(savedInstanceState.getBoolean(KEY_WAS_PROGRESS_ALIVE, false));
        } else {
            mLastPosition = -1;
        }

        // Check if network call required. On orientation change or coming back from detail screen, we can avoid
        // network call
        if (mLastPosition == -1) {
            mPresenter.start();
        }

        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        mNewsListViewModal.mNewsList.observe(getActivity(), new Observer<List<NewsItem>>() {
            @Override
            public void onChanged(@Nullable final List<NewsItem> newsItems) {
                if (newsItems != null && mLastPosition != -1) {
                    mAdapter.addAll(newsItems);
                    mLastPosition = -1;
                    // Hide progress if it was being shown
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_POS, mLayoutManager.findFirstVisibleItemPosition());
        outState.putBoolean(KEY_WAS_PROGRESS_ALIVE, mSwipeRefreshLayout.isRefreshing());
    }

    private void initViews() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.newscontent_list);
        mAdapter = new NewsListAdapter(mContext);
        mAdapter.setAdapterActionCallback(this);
        mLayoutManager = new LinearLayoutManager(mContext);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.refreshNewsList();
            }
        });

        mRecyclerView.addOnScrollListener(new PaginationScrollListener(mLayoutManager) {
            @Override
            public void loadMoreItems() {
                loadMore();
            }

            @Override
            public boolean isLastPage() {
                return mPresenter.isLastPage();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        mNewsListViewModal = ViewModelProviders.of(getActivity()).get(NewsListViewModal.class);
    }

    @Override
    public void showProgress(final boolean progress) {
        mSwipeRefreshLayout.setRefreshing(progress);
    }

    @Override
    public void showPagedNewsArticle(final List<NewsItem> newPageArticles, boolean shouldRefresh) {
        if (shouldRefresh) {
            mAdapter.refresh(newPageArticles);
        } else {
            mAdapter.addAll(newPageArticles);
        }
        isLoading = false;
        mNewsListViewModal.mNewsList.setValue(mAdapter.getNewsItemList());
    }

    @Override
    public void showNoNewsFound() {
        isLoading = false;
        Utils.notifyUser(mContext, mContext.getResources().getString(R.string.info_empty_news_list));
    }

    @Override
    public void onError(final String errorMessage) {
        Utils.notifyUser(mContext, errorMessage);
        isLoading = false;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showNewsDetailsUi(final NewsItem newsItem) {
        CommentListActivity.show(mContext, newsItem);
    }

    @Override
    public void setPresenter(final Presenter presenter) {

    }

    @Override
    public void showLoadingMoreProgress(final boolean show) {
        if (show) {
            mAdapter.addLoadingFooter();
        } else {
            mAdapter.removeLoadingFooter();
        }
    }

    @Override
    public void retryPageLoad() {
        loadMore();
    }

    @Override
    public void onShowDetails(final NewsItem newsItem) {
        if ((newsItem != null) && (newsItem.getKids() != null) && (!newsItem.getKids().isEmpty())) {
            mPresenter.openNewsDetails(newsItem);
        } else {
            Utils.notifyUser(mContext, "No Comment Found.");
        }
    }

    private void loadMore() {
        isLoading = true;
        if (Utils.isConnected(mContext)) {
            mPresenter.getPagedArticle(false);
        } else {
            mAdapter.showRetry(true, getResources().getString(R.string.internet_con_err_msg));
        }
    }
}