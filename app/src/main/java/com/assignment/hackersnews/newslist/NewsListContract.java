package com.assignment.hackersnews.newslist;

import android.support.annotation.NonNull;
import com.assignment.dataprovider.source.NewsItem;
import com.assignment.hackersnews.BasePresenter;
import com.assignment.hackersnews.BaseView;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 *
 * Created by Pankaj Kumar on 28/03/18.
 * EAT | DRINK | CODE
 */
public interface NewsListContract {

    interface View extends BaseView<Presenter> {

        void showProgress(boolean progress);

        void showPagedNewsArticle(List<NewsItem> newPageArticles, boolean onRefresh);

        void showNoNewsFound();

        void onError(String errorMessage);

        boolean isActive();

        void showNewsDetailsUi(NewsItem newsItem);

        void showLoadingMoreProgress(boolean show);
    }

    interface Presenter extends BasePresenter {

        void refreshNewsList();

        void getPagedArticle(boolean refreshing);

        void openNewsDetails(@NonNull NewsItem articleForDetail);

        boolean isLastPage();
    }
}