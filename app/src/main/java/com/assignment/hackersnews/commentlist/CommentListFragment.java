package com.assignment.hackersnews.commentlist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import com.assignment.dataprovider.source.CommentItem;
import com.assignment.dataprovider.source.CommentItemGroup;
import com.assignment.dataprovider.source.NewsItem;
import com.assignment.dataprovider.source.remote.NetworkServiceManager;
import com.assignment.hackersnews.BaseFragment;
import com.assignment.hackersnews.R;
import com.assignment.hackersnews.commentlist.CommentListContract.Presenter;
import com.assignment.hackersnews.util.Constants;
import com.assignment.hackersnews.util.Utils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * {@link android.support.v4.app.Fragment} which shows list of comments or reply into expandable list.
 *
 * Created by Pankaj Kumar on 28/03/18.
 * EAT | DRINK | CODE
 */
public class CommentListFragment extends BaseFragment implements CommentListContract.View {

    /**
     * Key is being used to store state of scrolled position in news list before orientation change
     */
    private static final String KEY_EXPANDED_POS = "selected_pos";
    /**
     * Key is being used to store state of progress state
     */
    private static final String KEY_WAS_PROGRESS_ALIVE = "progress_being_shown";
    @Inject
    public NetworkServiceManager mService;
    private Context mContext;
    private View mRootView;
    private ExpandableListView mExpandableListView;
    private CommentsAdapter mAdapter;
    private Presenter mPresenter;
    private NewsItem mNewsItem;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CommentViewModal mCommentViewModal;
    /**
     * This variable is being used to cached first visible position before orientation change or going to
     * details screen. As well as blocks list updation from ViewModal if service call was made.
     */
    private int mLastItemExpanded = -1;

    public CommentListFragment() {
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
        mRootView = inflater.inflate(R.layout.comments_list_fragment_layout, container, false);

        getDeps().inject(this);
        mPresenter = new CommentListPresenter(mService, this);

        initViews();

        if (savedInstanceState != null) {
            mLastItemExpanded = savedInstanceState.getInt(KEY_EXPANDED_POS, -1);
            mSwipeRefreshLayout.setRefreshing(savedInstanceState.getBoolean(KEY_WAS_PROGRESS_ALIVE, false));
        } else {
            mLastItemExpanded = -1;
        }

        // Check if network call required. On orientation change or coming back from detail screen, we can avoid
        // network call
        if (mLastItemExpanded == -1) {
            // mPresenter.start();
            mPresenter.getAllComments(mNewsItem);
        }

        return mRootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_EXPANDED_POS, 0);
        outState.putBoolean(KEY_WAS_PROGRESS_ALIVE, mSwipeRefreshLayout.isRefreshing());

        // Remember expanded or collapsed state
        // mCommentViewModal.mExpandedParents.clear();
        mCommentViewModal.mExpandedParents.addAll(mAdapter.getExpandedGroups());
    }

    @Override
    public void onResume() {
        super.onResume();

        mCommentViewModal.mDataSet.observe(getActivity(), new Observer<ArrayList<CommentItemGroup>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<CommentItemGroup> cachedDataSet) {
                if (cachedDataSet != null && mLastItemExpanded != -1) {
                    mAdapter.addAll(cachedDataSet);
                    mLastItemExpanded = -1;
                    // Expand groups which were expanded
                    for (int parent : mCommentViewModal.mExpandedParents) {
                        mExpandableListView.expandGroup(parent);
                    }
                    mCommentViewModal.mExpandedParents.clear();
                    // Hide progress if it was being shown
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void initViews() {
        mExpandableListView = (ExpandableListView) mRootView.findViewById(R.id.expandable_list_view);
        mAdapter = new CommentsAdapter(mContext);
        mExpandableListView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setRefreshing(false);

        mNewsItem = getArguments().getParcelable(Constants.KEY_ARGS_ARTICLE);

        mExpandableListView.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(final ExpandableListView parent, final View v, final int groupPosition,
                    final long id) {
                CommentItem parentItem = mAdapter.getGroup(groupPosition);
                if ((parentItem != null) && (parentItem.getKids() != null) && (!parentItem.getKids().isEmpty())) {
                    mPresenter.getReplyForComment(groupPosition, parentItem);
                } else {
                    Utils.notifyUser(mContext, "No Reply Found.");
                    showNoReplyFound(groupPosition);
                }

                return false;
            }
        });

        mExpandableListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(final ExpandableListView parent, final View v, final int groupPosition,
                    final int childPosition, final long id) {
                return false;
            }
        });

        mCommentViewModal = ViewModelProviders.of(getActivity()).get(CommentViewModal.class);
    }

    @Override
    public void showProgress(final boolean progress) {
        mSwipeRefreshLayout.setRefreshing(progress);
    }

    @Override
    public void onError(final String errorMessage) {
        Utils.notifyUser(mContext, errorMessage);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showComments(final List<CommentItemGroup> comments) {
        mAdapter.reset(comments);
        mCommentViewModal.mDataSet.setValue(mAdapter.getList());
    }

    @Override
    public void showReplyForComment(final int commentPositionInAdapter, final List<CommentItem> replies) {
        mAdapter.updateChild(commentPositionInAdapter, replies);
        mCommentViewModal.mDataSet.setValue(mAdapter.getList());
    }

    @Override
    public void showNoCommentFound() {
        Utils.notifyUser(mContext, "No comment found");
    }

    @Override
    public void showNoReplyFound(final int commentPositionInAdapter) {
        mAdapter.updateChild(commentPositionInAdapter, new ArrayList<CommentItem>(0));
        mCommentViewModal.mDataSet.setValue(mAdapter.getList());
    }

    @Override
    public void setPresenter(final CommentListContract.Presenter presenter) {

    }
}