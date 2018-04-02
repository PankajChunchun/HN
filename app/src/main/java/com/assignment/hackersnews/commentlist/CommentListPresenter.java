package com.assignment.hackersnews.commentlist;


import com.assignment.dataprovider.observer.DisposableManager;
import com.assignment.dataprovider.source.CommentItem;
import com.assignment.dataprovider.source.CommentItemGroup;
import com.assignment.dataprovider.source.DataSourceService;
import com.assignment.dataprovider.source.NetworkServiceCallback;
import com.assignment.dataprovider.source.NewsItem;
import com.assignment.dataprovider.source.remote.NetworkError;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rx.subscriptions.CompositeSubscription;

/**
 * Presenter class for comment list view
 *
 * Created by Pankaj Kumar on 28/03/18.
 * EAT | DRINK | CODE
 */
public class CommentListPresenter implements CommentListContract.Presenter {

    private final DataSourceService mDataSourceService;
    private final CommentListContract.View mView;
    private CompositeSubscription mSubscriptions;

    public CommentListPresenter(DataSourceService dataSourceService, CommentListContract.View view) {
        this.mDataSourceService = dataSourceService;
        this.mView = view;
        this.mSubscriptions = new CompositeSubscription();
    }

    public void onStop() {
        DisposableManager.dispose();
    }

    @Override
    public void start() {

    }

    @Override
    public void getAllComments(final NewsItem newsItem) {
        mView.showProgress(true);
        List<Integer> listOfId = newsItem.getKids();
        // Check if result is empty or less than limit
        // If YES, it means no more page request required (As data is already less than requested size or not available)
        // If No, there would be case that more pages exists. enable load more request
        if (listOfId == null || listOfId.isEmpty()) {
            mView.showProgress(false);
            mView.showNoCommentFound();
            return;
        }

        Disposable disposable = mDataSourceService.getCommentWithIds(listOfId, new NetworkServiceCallback() {
            @Override
            public void onSuccess(final Object response) {

                mView.showProgress(false);
                List<CommentItem> items = (List<CommentItem>) response;
                if (items == null) {
                    mView.showNoCommentFound();
                } else {

                    Collections.sort(items);
                    // Add reply childs for all comments if they have
                    List<CommentItemGroup> newDataSet = new ArrayList<>(items.size());
                    CommentItem loderItem = new CommentItem();
                    loderItem.setEmptyView(true);
                    List<CommentItem> emptyList = new ArrayList<>(1);
                    emptyList.add(loderItem);

                    for (CommentItem commentItem : items) {
                        newDataSet.add(new CommentItemGroup(commentItem, emptyList));
                    }

                    mView.showComments(newDataSet);
                }
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
    public void getReplyForComment(final int commentPositionInAdapter, final CommentItem commentItem) {
        // mView.showProgress(true);
        List<Integer> listOfId = commentItem.getKids();
        // Check if result is empty or less than limit
        // If YES, it means no more page request required (As data is already less than requested size or not available)
        // If No, there would be case that more pages exists. enable load more request
        if (listOfId == null || listOfId.isEmpty()) {
            //mView.showProgress(false);
            mView.showNoReplyFound(commentPositionInAdapter);
            return;
        }

        Disposable disposable = mDataSourceService.getCommentWithIds(listOfId, new NetworkServiceCallback() {
            @Override
            public void onSuccess(final Object response) {

                //mView.showProgress(false);

                List<CommentItem> items = (List<CommentItem>) response;
                if (items == null) {
                    mView.showNoReplyFound(commentPositionInAdapter);
                } else {
                    Collections.sort(items);
                    mView.showReplyForComment(commentPositionInAdapter, (List<CommentItem>) response);
                }
            }

            @Override
            public void onError(final NetworkError networkError) {
                // mView.showProgress(false);
                mView.onError(networkError.getAppErrorMessage());
            }
        });

        DisposableManager.add(disposable);
    }
}