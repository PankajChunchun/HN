package com.assignment.hackersnews.commentlist;

import com.assignment.dataprovider.source.CommentItem;
import com.assignment.dataprovider.source.CommentItemGroup;
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
public interface CommentListContract {

    interface View extends BaseView<Presenter> {

        void showProgress(boolean progress);

        void showComments(List<CommentItemGroup> comments);

        void showReplyForComment(int commentPositionInAdapter, List<CommentItem> replies);

        void showNoCommentFound();

        void showNoReplyFound(int commentPositionInAdapter);

        void onError(String errorMessage);

        boolean isActive();
    }

    interface Presenter extends BasePresenter {

        void getAllComments(NewsItem item);

        void getReplyForComment(int commentPositionInAdapter, CommentItem item);
    }
}