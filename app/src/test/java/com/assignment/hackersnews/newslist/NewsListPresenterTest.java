package com.assignment.hackersnews.newslist;

import com.assignment.dataprovider.source.DataSourceService;
import com.assignment.dataprovider.source.NewsItem;
import io.reactivex.disposables.CompositeDisposable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Pankaj Kumar on 01/04/18.
 * EAT | DRINK | CODE
 */
public class NewsListPresenterTest {

    /*private NewsListPresenter mNewsListPresenter;
    @Mock
    private DataSourceService service;
    @Mock
    private NewsListContract.View view;

    private CompositeDisposable compositeDisposable;

    @Before
    public void setupNewsListPresenterTest() {
        MockitoAnnotations.initMocks(this);

        mNewsListPresenter = new NewsListPresenter(service, view);

        when(view.isActive()).thenReturn(true);

        compositeDisposable = new CompositeDisposable();
    }

    private static List<NewsItem> getDummyList() {
        ArrayList<NewsItem> items = new ArrayList<>(2);
        NewsItem item = new NewsItem("norvig", 8863,
                Arrays.asList(9224, 8952, 8917, 8884, 8887, 8943, 8869, 8940, 8908, 8958, 9005, 8873, 9671, 9067),
                1175714200, "story", 71, 104, "My YC app: Dropbox - Throw away your USB drive",
                "http://www.getdropbox.com/u/2/screencast.html");

        NewsItem item2 = new NewsItem("mark", 8864,
                Arrays.asList(9224),
                1175714200, "story", 71, 104, "Dropbox - Throw away your USB drive",
                "http://www.getdropbox.com/u/2/screencast.html");
        items.add(item);
        items.add(item2);
        return items;
    }


    @Test
    public void setsThePresenterToViewTest() {
        // Get a reference to the class under test
        mNewsListPresenter = new NewsListPresenter(service, view);

        // Then the presenter is set to the view
        // verify(view).setPresenter(mNewsListPresenter);
    }

    @Test
    public void openNewsDetailsTest() {
        NewsItem item = new NewsItem("norvig", 8863,
                Arrays.asList(9224, 8952, 8917, 8884, 8887, 8943, 8869, 8940, 8908, 8958, 9005, 8873, 9671, 9067),
                1175714200, "story", 71, 104, "My YC app: Dropbox - Throw away your USB drive",
                "http://www.getdropbox.com/u/2/screencast.html");
        mNewsListPresenter.openNewsDetails(item);
        verify(view).showNewsDetailsUi(item);
    }

    @Test
    public void refreshNewsListTest() {
        mNewsListPresenter.refreshNewsList();
        // verify(view).showProgress(true);

        // verify(view).showProgress(false);
    }

    @Test
    public void getPagedArticleTest() {
        mNewsListPresenter.getPagedArticle(true);
        //verify(view).showProgress(true);

        //verify(view).showPagedNewsArticle(null, true);
    }*/

    /*@Test
    public void refreshNewsListTest() {
        NewsItem item = new NewsItem("norvig", 8863,
                Arrays.asList(9224, 8952, 8917, 8884, 8887, 8943, 8869, 8940, 8908, 8958, 9005, 8873, 9671, 9067),
                1175714200, "story", 71, 104, "My YC app: Dropbox - Throw away your USB drive",
                "http://www.getdropbox.com/u/2/screencast.html");

        mNewsListPresenter.refreshNewsList();
        verify(mNewsListPresenter).getPagedArticle(true);
        //verify(view).showNewsDetailsUi(item);
        verify(view).showProgress(true);
        verify(view).showProgress(false);
    }*/
}