package com.assignment.hackersnews.commentlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.assignment.dataprovider.source.CommentItem;
import com.assignment.dataprovider.source.CommentItemGroup;
import com.assignment.hackersnews.BuildConfig;
import com.assignment.hackersnews.R;
import com.assignment.hackersnews.commentlist.CommentsAdapter.ChildViewHolder;
import com.assignment.hackersnews.commentlist.CommentsAdapter.GroupViewHolder;
import com.assignment.hackersnews.util.HNTestUtils;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.*;
import org.junit.runner.*;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

/**
 * Created by Pankaj Kumar on 02/04/18.
 * EAT | DRINK | CODE
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class CommentsAdapterTest {

    private CommentsAdapter mCommentsAdapter;

    @Before
    public void setUp() {
        mCommentsAdapter = new CommentsAdapter(ShadowApplication.getInstance().getApplicationContext());
    }

    @Test
    public void verifyGetChild() {

        mCommentsAdapter.addAll(HNTestUtils.getComments());
        CommentItem commentItem = mCommentsAdapter.getChild(0, 0);

        assertNotNull(commentItem);
        assertTrue(commentItem.getId() == 123);

        CommentItem item = new CommentItem("norvig", 4,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 8863, "comment item3", false);
        CommentItemGroup groupItem = new CommentItemGroup(item, null);
        mCommentsAdapter.addAll(Arrays.asList(groupItem));

        commentItem = mCommentsAdapter.getChild(mCommentsAdapter.getGroupCount() - 1,
                0);
        assertNull(commentItem);
    }

    @Test
    public void verifyGetChildId() {
        mCommentsAdapter.addAll(HNTestUtils.getComments());
        assertTrue(mCommentsAdapter.getChildId(0, 0) == 0);
        assertTrue(mCommentsAdapter.getChildId(0, 1) == 1);
    }

    @Test
    public void verifyGetGroupId() {
        mCommentsAdapter.addAll(HNTestUtils.getComments());
        assertTrue(mCommentsAdapter.getGroupId(0) == 0);
        assertTrue(mCommentsAdapter.getGroupId(1) == 1);
    }

    @Test
    public void verifyAddAll() {
        mCommentsAdapter.reset(new ArrayList<CommentItemGroup>(0));
        int size = mCommentsAdapter.getGroupCount();
        assertTrue(size == 0);

        mCommentsAdapter.addAll(HNTestUtils.getComments());
        size = mCommentsAdapter.getGroupCount();
        assertTrue(size == 6);
    }

    @Test
    public void verofiOnGroupExpanded() {
        mCommentsAdapter.addAll(HNTestUtils.getComments());

        mCommentsAdapter.onGroupExpanded(1);
        assertTrue(mCommentsAdapter.mExpandedGroups.size() == 1);

        mCommentsAdapter.onGroupExpanded(2);
        assertTrue(mCommentsAdapter.mExpandedGroups.size() == 2);

        mCommentsAdapter.onGroupExpanded(4);
        assertTrue(mCommentsAdapter.mExpandedGroups.size() == 3);
    }

    @Test
    public void verifyOnGroupCollapsed() {
        mCommentsAdapter.addAll(HNTestUtils.getComments());

        mCommentsAdapter.onGroupExpanded(1);
        mCommentsAdapter.onGroupExpanded(2);
        mCommentsAdapter.onGroupExpanded(4);
        assertTrue(mCommentsAdapter.mExpandedGroups.size() == 3);

        mCommentsAdapter.onGroupCollapsed(4);
        assertTrue(mCommentsAdapter.mExpandedGroups.size() == 2);

        mCommentsAdapter.onGroupCollapsed(1);
        assertTrue(mCommentsAdapter.mExpandedGroups.size() == 1);
    }

    @Test
    public void verifyGetGroupView() {
        mCommentsAdapter.addAll(HNTestUtils.getComment());
        View groupView = mCommentsAdapter.getGroupView(0, false, null, null);

        String textShown = ((TextView) groupView.findViewById(R.id.added_by_tv)).getText().toString();
        Assert.assertEquals(textShown, "test user");
    }

    @Test
    public void verifyGetGroupView_reuse_row() {
        LayoutInflater inflater = (LayoutInflater) RuntimeEnvironment.application
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.comment_view, null, false);
        GroupViewHolder viewHolder = new GroupViewHolder();
        viewHolder.mAddedBy = (TextView) view.findViewById(R.id.added_by_tv);
        viewHolder.mWhenAdded = (TextView) view.findViewById(R.id.when_added_tv);
        viewHolder.mAddedContent = (TextView) view.findViewById(R.id.content_tv);
        viewHolder.mExpandView = (TextView) view.findViewById(R.id.content_count_and_expanded_tv);
        view.setTag(viewHolder);

        mCommentsAdapter.addAll(HNTestUtils.getComment());
        View groupView = mCommentsAdapter.getGroupView(0, false, view, null);

        String textShown = ((TextView) groupView.findViewById(R.id.added_by_tv)).getText().toString();
        Assert.assertEquals(textShown, "test user");
    }

    @Test
    public void verifyGetGroupView_empty_views() {
        mCommentsAdapter.addAll(HNTestUtils.getCommentWithEmptyFields());
        View groupView = mCommentsAdapter.getGroupView(0, false, null, null);

        String t1 = ((TextView) groupView.findViewById(R.id.content_count_and_expanded_tv)).getText().toString();
        Assert.assertEquals(t1, "");

        String t2 = ((TextView) groupView.findViewById(R.id.content_tv)).getText().toString();
        Assert.assertEquals(t2, "");
    }

    @Test
    public void verifyGetChildView() {
        mCommentsAdapter.addAll(HNTestUtils.getComment());
        View groupView = mCommentsAdapter.getChildView(0, 0, false, null, null);

        String textShown = ((TextView) groupView.findViewById(R.id.added_by_tv)).getText().toString();
        Assert.assertEquals(textShown, "replied on test user comment");
    }

    @Test
    public void verifyGetChildView_reuse_row() {
        LayoutInflater inflater = (LayoutInflater) RuntimeEnvironment.application
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.reply_view, null);
        ChildViewHolder viewHolder = new ChildViewHolder();
        viewHolder.mLoaderView = (ProgressBar) view.findViewById(R.id.child_fetch_progress);
        viewHolder.mChildViewContainer = (ViewGroup) view.findViewById(R.id.reply_view);
        viewHolder.mAddedBy = (TextView) view.findViewById(R.id.added_by_tv);
        viewHolder.mWhenAdded = (TextView) view.findViewById(R.id.when_added_tv);
        viewHolder.mAddedContent = (TextView) view.findViewById(R.id.content_tv);
        viewHolder.mExpandView = (TextView) view.findViewById(R.id.content_count_and_expanded_tv);
        view.setTag(viewHolder);

        mCommentsAdapter.addAll(HNTestUtils.getComment());
        View groupView = mCommentsAdapter.getChildView(0, 0, false, view, null);

        String textShown = ((TextView) groupView.findViewById(R.id.added_by_tv)).getText().toString();
        Assert.assertEquals(textShown, "replied on test user comment");
    }

    @Test
    public void verifyGetChildView_empty_views() {
        mCommentsAdapter.addAll(HNTestUtils.getCommentWithEmptyFields());
        View groupView = mCommentsAdapter.getChildView(0, 0, false, null, null);

        String t1 = ((TextView) groupView.findViewById(R.id.content_count_and_expanded_tv)).getText().toString();
        Assert.assertEquals(t1, "");

        String t2 = ((TextView) groupView.findViewById(R.id.content_tv)).getText().toString();
        Assert.assertEquals(t2, "");
    }
}