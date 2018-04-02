package com.assignment.hackersnews.commentlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.os.Bundle;
import android.widget.ExpandableListView;
import com.assignment.hackersnews.BuildConfig;
import com.assignment.hackersnews.R;
import com.assignment.hackersnews.util.Constants;
import com.assignment.hackersnews.util.HNTestUtils;
import com.assignment.hackersnews.util.RobolectricUtils;
import org.junit.*;
import org.junit.runner.*;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

/**
 * Created by Pankaj Kumar on 02/04/18.
 * EAT | DRINK | CODE
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CommentListFragmentTest {

    private CommentListFragment mFragment;

    @Before
    public void setUp() {
        mFragment = new CommentListFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_ARGS_ARTICLE, HNTestUtils.getNewsItem());
        mFragment.setArguments(args);
        RobolectricUtils.startFragment(mFragment);
    }

    @Test
    public void verifyShowComments() {
        mFragment.showComments(HNTestUtils.getComments());

        ExpandableListView expandableListView = (ExpandableListView) mFragment.getView()
                .findViewById(R.id.expandable_list_view);
        CommentsAdapter adapter = (CommentsAdapter) expandableListView.getExpandableListAdapter();

        int groupCount = adapter.getGroupCount();
        int childCount = adapter.getChildrenCount(0);

        assertTrue(groupCount == 6);
        assertTrue(childCount == 2);

        assertTrue(adapter.getChildrenCount(1) == 1);
        assertTrue(adapter.getChildrenCount(3) == 0);
    }

    @Test
    public void verifyShowReplyForComment() {

        mFragment.showComments(HNTestUtils.getComments());

        ExpandableListView expandableListView = (ExpandableListView) mFragment.getView()
                .findViewById(R.id.expandable_list_view);
        CommentsAdapter adapter = (CommentsAdapter) expandableListView.getExpandableListAdapter();

        assertTrue(adapter.getChildrenCount(3) == 0);

        mFragment.showReplyForComment(3,
                HNTestUtils.getReplies(adapter.getGroup(3).getId()));

        assertTrue(adapter.getChildrenCount(3) == 3);
    }


    @Test
    public void verifyShowNoCommentFound() {
        mFragment.showNoCommentFound();
        assertEquals("No comment found", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void verifyShowNoReplyFound() {

        mFragment.showComments(HNTestUtils.getComments());

        ExpandableListView expandableListView = (ExpandableListView) mFragment.getView()
                .findViewById(R.id.expandable_list_view);
        CommentsAdapter adapter = (CommentsAdapter) expandableListView.getExpandableListAdapter();

        assertTrue(adapter.getChildrenCount(3) == 0);

        mFragment.showNoReplyFound(3);

        assertTrue(adapter.getChildrenCount(3) == 0);
    }

    @Test
    public void verifyOnError() {
        mFragment.onError("Error occurred");
        assertEquals("Error occurred", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void verifyIsActive() {
        assertTrue(mFragment.isActive());
    }
}