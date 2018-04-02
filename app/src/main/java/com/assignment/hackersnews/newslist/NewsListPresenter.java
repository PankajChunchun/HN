package com.assignment.hackersnews.newslist;

import android.support.annotation.NonNull;
import com.assignment.dataprovider.observer.DisposableManager;
import com.assignment.dataprovider.source.DataSourceService;
import com.assignment.dataprovider.source.NetworkServiceCallback;
import com.assignment.dataprovider.source.NewsItem;
import com.assignment.dataprovider.source.remote.NetworkError;
import com.assignment.hackersnews.util.Utils;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import rx.subscriptions.CompositeSubscription;

/**
 * Presenter class for NEWS list view
 *
 * Created by Pankaj Kumar on 28/03/18.
 * EAT | DRINK | CODE
 */
public class NewsListPresenter implements NewsListContract.Presenter {

    private final DataSourceService mDataSourceService;
    private final NewsListContract.View mView;
    private CompositeSubscription mSubscriptions;
    private boolean isLastPage;

    NewsListPresenter(DataSourceService dataSourceService, NewsListContract.View view) {
        this.mDataSourceService = dataSourceService;
        this.mView = view;
        this.mSubscriptions = new CompositeSubscription();
        this.isLastPage = false;
    }

    public void onStop() {
        DisposableManager.dispose();
    }

    @Override
    public void start() {
        refreshNewsList();
    }

    @Override
    public void refreshNewsList() {
        mView.showProgress(true);
        Disposable disposable = mDataSourceService.getNewsIdentifiers(new NetworkServiceCallback() {
            @Override
            public void onSuccess(final Object response) {
                ArrayList<Integer> ids = (ArrayList<Integer>) response;
                PaginatedListStore.INSTANCE.setList(ids);
                PaginatedListStore.INSTANCE.sort();
                mView.showProgress(false);
                isLastPage = false;
                getPagedArticle(true);
            }

            @Override
            public void onError(final NetworkError networkError) {
                mView.showProgress(false);
                mView.onError(networkError.getAppErrorMessage());
            }
        });

        DisposableManager.add(disposable);
    }

    @Override
    public void getPagedArticle(final boolean refreshing) {
        if (isLastPage) {
            return;
        }

        List<Integer> listOfId = PaginatedListStore.INSTANCE.getNextPage();
        // Check if result is empty or less than limit
        // If YES, it means no more page request required (As data is already less than requested size or not available)
        // If No, there would be case that more pages exists. enable load more request
        if (listOfId == null || listOfId.isEmpty()) {
            mView.showLoadingMoreProgress(false);
            isLastPage = true;
            return;
        }

        if (refreshing) {
            mView.showProgress(true);
        }

        mDataSourceService.getNewsWithIds(listOfId, new NetworkServiceCallback() {
            @Override
            public void onSuccess(final Object response) {
                mView.showProgress(false);

                ArrayList<NewsItem> items = (ArrayList) response;

                // Check if result is empty or less than limit
                // If YES, it means no more page request required (As data is already less than requested size or not available)
                // If No, there would be case that more pages exists. enable load more request
                if (items == null || items.isEmpty() || items.size() < PaginatedListStore.DEFAULT_PAGE_SIZE) {
                    mView.showLoadingMoreProgress(false);
                    isLastPage = true;
                } else {
                    mView.showLoadingMoreProgress(true);
                }

                if (items == null || items.isEmpty()) {
                    mView.showNoNewsFound();
                } else {
                    // TODO Implement logic for sorting to show latest news
                    mView.showPagedNewsArticle(items, refreshing);
                }
            }

            @Override
            public void onError(final NetworkError networkError) {
                mView.showProgress(false);
                mView.onError(networkError.getAppErrorMessage());
            }
        });
    }

    @Override
    public boolean isLastPage() {
        return isLastPage;
    }

    @Override
    public void openNewsDetails(@NonNull NewsItem articleForDetail) {
        Utils.checkNotNull(articleForDetail, "News article cannot be null!");
        mView.showNewsDetailsUi(articleForDetail);
    }
}