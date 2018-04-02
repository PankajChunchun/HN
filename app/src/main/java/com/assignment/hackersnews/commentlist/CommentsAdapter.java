package com.assignment.hackersnews.commentlist;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.assignment.dataprovider.source.CommentItem;
import com.assignment.dataprovider.source.CommentItemGroup;
import com.assignment.hackersnews.R;
import com.assignment.hackersnews.util.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * {@link BaseExpandableListAdapter} which handles comment and reply on comment list
 *
 * Created by Pankaj Kumar on 30/03/18.
 * EAT | DRINK | CODE
 */
public class CommentsAdapter extends BaseExpandableListAdapter {

    private ArrayList<CommentItemGroup> mDataSet;
    private Context mContext;
    private long mCurrentTimeMillis;
    Set<Integer> mExpandedGroups;

    CommentsAdapter(Context context) {
        mContext = context;
        mDataSet = new ArrayList<>(0);
        mCurrentTimeMillis = System.currentTimeMillis();
        mExpandedGroups = new HashSet<>(0);
    }

    @Override
    public CommentItem getChild(int groupPosition, int childPosition) {
        if (mDataSet.get(groupPosition).childs != null) {
            return mDataSet.get(groupPosition).childs.get(childPosition);
        } else {
            return null;
        }
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View view, ViewGroup parent) {

        ChildViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.reply_view, null);
            viewHolder = new ChildViewHolder();
            viewHolder.mLoaderView = (ProgressBar) view.findViewById(R.id.child_fetch_progress);
            viewHolder.mChildViewContainer = (ViewGroup) view.findViewById(R.id.reply_view);
            viewHolder.mAddedBy = (TextView) view.findViewById(R.id.added_by_tv);
            viewHolder.mWhenAdded = (TextView) view.findViewById(R.id.when_added_tv);
            viewHolder.mAddedContent = (TextView) view.findViewById(R.id.content_tv);
            viewHolder.mExpandView = (TextView) view.findViewById(R.id.content_count_and_expanded_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) view.getTag();
        }

        CommentItem item = mDataSet.get(groupPosition).childs.get(childPosition);

        if (item.isEmptyView()) {
            // Show loader
            viewHolder.mChildViewContainer.setVisibility(View.GONE);
            viewHolder.mLoaderView.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mChildViewContainer.setVisibility(View.VISIBLE);
            viewHolder.mLoaderView.setVisibility(View.GONE);
            viewHolder.mAddedBy.setText(item.getBy());

            if (item.getKids() != null && item.getKids().size() > 0) {
                viewHolder.mExpandView.setText(mContext.getResources().getString(R.string.comment_header_count,
                        item.getKids().size() + 1));
            } else {
                viewHolder.mExpandView.setText("");
            }

            viewHolder.mWhenAdded.setText(Utils.getTimeAgo(mCurrentTimeMillis, item.getTime()));
            if (TextUtils.isEmpty(item.text)) {
                viewHolder.mAddedContent.setText("");
            } else {
                viewHolder.mAddedContent.setText(Utils.fromHtml(item.text));
            }
        }

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (mDataSet.get(groupPosition).childs != null) {
            return mDataSet.get(groupPosition).childs.size();
        } else {
            return 0;
        }
    }

    @Override
    public CommentItem getGroup(int groupPosition) {
        return mDataSet.get(groupPosition).parent;
    }

    @Override
    public int getGroupCount() {
        return mDataSet.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isLastChild, View view,
            ViewGroup parent) {

        GroupViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.comment_view, null);
            viewHolder = new GroupViewHolder();
            viewHolder.mAddedBy = (TextView) view.findViewById(R.id.added_by_tv);
            viewHolder.mWhenAdded = (TextView) view.findViewById(R.id.when_added_tv);
            viewHolder.mAddedContent = (TextView) view.findViewById(R.id.content_tv);
            viewHolder.mExpandView = (TextView) view.findViewById(R.id.content_count_and_expanded_tv);
            view.setTag(viewHolder);
        } else {
            viewHolder = (GroupViewHolder) view.getTag();
        }

        CommentItem item = mDataSet.get(groupPosition).parent;
        viewHolder.mAddedBy.setText(item.getBy());
        if (item.getKids() != null && item.getKids().size() > 0) {
            viewHolder.mExpandView.setText(mContext.getResources().getString(R.string.comment_header_count,
                    item.getKids().size() + 1));
        } else {
            viewHolder.mExpandView.setText("");
        }

        viewHolder.mWhenAdded.setText(Utils.getTimeAgo(mCurrentTimeMillis, item.getTime()));
        if (TextUtils.isEmpty(item.text)) {
            viewHolder.mAddedContent.setText("");
        } else {
            viewHolder.mAddedContent.setText(Utils.fromHtml(item.text));
        }
        return view;
    }

    @Override
    public void onGroupExpanded(final int groupPosition) {
        super.onGroupExpanded(groupPosition);

        mExpandedGroups.add(groupPosition);
    }

    @Override
    public void onGroupCollapsed(final int groupPosition) {
        super.onGroupCollapsed(groupPosition);

        mExpandedGroups.remove(groupPosition);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public ArrayList<CommentItemGroup> getList() {
        return mDataSet;
    }

    static class GroupViewHolder {

        TextView mAddedBy;
        TextView mWhenAdded;
        TextView mAddedContent;
        TextView mExpandView;
    }

    static class ChildViewHolder {

        ViewGroup mChildViewContainer;
        ProgressBar mLoaderView;
        TextView mAddedBy;
        TextView mWhenAdded;
        TextView mAddedContent;
        TextView mExpandView;
    }

    public void reset(List<CommentItemGroup> newDataSet) {
        mDataSet.clear();
        mDataSet.addAll(newDataSet);
        notifyDataSetInvalidated();
    }

    public void updateChild(int parentPosition, List<CommentItem> newChilds) {
        mDataSet.get(parentPosition).childs = newChilds;
        notifyDataSetChanged();
    }

    public void addAll(List<CommentItemGroup> newDataSet) {
        mDataSet.addAll(newDataSet);
        notifyDataSetChanged();
    }

    /**
     * Get all expanded groups. Result can be used while restoring same state after orientation change.
     */
    public Set<Integer> getExpandedGroups() {
        return mExpandedGroups;
    }
}