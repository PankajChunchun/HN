package com.assignment.hackersnews.newslist;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.assignment.dataprovider.source.NewsItem;
import com.assignment.hackersnews.R;
import com.assignment.hackersnews.util.Utils;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class for {@link RecyclerView} which shows news list.
 * It also handles pull-to-refresh and pagination.
 *
 * Created by Pankaj Kumar on 28/03/18.
 * EAT | DRINK | CODE
 */
public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<NewsItem> mNewsItemList;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private String mErrorMsg;
    private OnAdapterActionCallback mCallback;
    private RecyclerView mRecyclerView;
    private long mCurrentTimeMillis;

    public static final class RowType {

        static final int LOAD_MORE = 0;
        static final int NEWS_ROW = 1;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView mRowIdTv, mTitleTv, mAddedByTv, mSubDetailsTv;
        ImageButton mUpvoteBtn;

        public NewsViewHolder(View view) {
            super(view);
            mRowIdTv = (TextView) view.findViewById(R.id.news_row_id_tv);
            mTitleTv = (TextView) view.findViewById(R.id.title_tv);
            mAddedByTv = (TextView) view.findViewById(R.id.added_by_tv);
            mSubDetailsTv = (TextView) view.findViewById(R.id.sub_details_tv);
            mUpvoteBtn = (ImageButton) view.findViewById(R.id.upvote_news_btn);
        }
    }

    public NewsListAdapter(Context context) {
        this.mContext = context;
        this.mNewsItemList = new ArrayList<NewsItem>(0);
        mCurrentTimeMillis = System.currentTimeMillis();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mRecyclerView = recyclerView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case RowType.LOAD_MORE:
                View viewWebPageRow = inflater.inflate(R.layout.load_more_view, parent, false);
                viewHolder = new LoadMoreVH(viewWebPageRow);
                break;
            case RowType.NEWS_ROW:
                View viewItem = inflater.inflate(R.layout.news_list_row_view, parent, false);
                viewHolder = new NewsViewHolder(viewItem);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (getItemViewType(position)) {

            case RowType.LOAD_MORE:
                LoadMoreVH loadingVH = (LoadMoreVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            mErrorMsg != null ?
                                    mErrorMsg :
                                    mContext.getResources().getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;

            case RowType.NEWS_ROW:
                NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
                final NewsItem item = mNewsItemList.get(position);
                newsViewHolder.mRowIdTv.setText(mContext.getResources()
                        .getString(R.string.news_row_identifier, position + 1));
                try {
                    URL url = new URL(item.getUrl());
                    String host = url.getHost();
                    if (!TextUtils.isEmpty(host)) {
                        newsViewHolder.mAddedByTv.setVisibility(View.VISIBLE);
                        newsViewHolder.mAddedByTv.setText(mContext.getResources()
                                .getString(R.string.news_added_by, host));
                    } else {
                        newsViewHolder.mAddedByTv.setVisibility(View.GONE);
                    }

                } catch (MalformedURLException e) {
                    newsViewHolder.mAddedByTv.setVisibility(View.GONE);
                }

                newsViewHolder.mTitleTv.setText(item.getTitle());
                newsViewHolder.mSubDetailsTv.setText(mContext.getResources()
                        .getString(R.string.news_sub_details, item.getScore(), item.getBy(),
                                Utils.getTimeAgo(mCurrentTimeMillis, item.getTime()),
                                item.getDescendants()));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCallback != null) {
                            mCallback.onShowDetails(item);
                        }
                    }
                });

                break;
        }
    }

    public class LoadMoreVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadMoreVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    if (mCallback != null) {
                        mCallback.retryPageLoad();
                    }

                    break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mNewsItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == mNewsItemList.size() - 1 && isLoadingAdded) ?
                RowType.LOAD_MORE : RowType.NEWS_ROW;
    }

    public NewsItem getItem(int positionInAdapter) {
        return mNewsItemList.get(positionInAdapter);
    }

    public void refresh(List<NewsItem> latestNews) {
        // Remove all views
        int size = mNewsItemList.size();
        this.mNewsItemList.clear();
        notifyItemRangeRemoved(0, size);
        // Add refreshed data
        this.mNewsItemList.addAll(latestNews);
        notifyDataSetChanged();
    }

    /**
     * Add 1 row at last and notify adapter about last added item
     *
     * @param newsItem - {@link NewsItem}
     */
    public void add(NewsItem newsItem) {
        this.mNewsItemList.add(newsItem);
        notifyItemInserted(this.mNewsItemList.size() - 1);
    }

    public void addAll(List<NewsItem> newsItems) {
        for (NewsItem item : newsItems) {
            add(item);
        }
    }

    public List<NewsItem> getNewsItemList() {
        return mNewsItemList;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new NewsItem());
    }

    public void removeLoadingFooter() {
        if (!isLoadingAdded) {
            return;
        }
        isLoadingAdded = false;

        final int position = mNewsItemList.size() - 1;
        NewsItem newsItem = getItem(position);

        //if (newsItem != null) {
        mNewsItemList.remove(position);
        if (mRecyclerView != null) {
            mRecyclerView.post(new Runnable() {
                public void run() {
                    notifyItemRemoved(position);
                }
            });
        }
        //}
    }

    /**
     * Displays Pagination retry footer view along with appropriate mErrorMsg
     *
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(mNewsItemList.size() - 1);

        if (errorMsg != null) {
            this.mErrorMsg = errorMsg;
        }
    }

    public void setAdapterActionCallback(final OnAdapterActionCallback callback) {
        mCallback = callback;
    }

    public interface OnAdapterActionCallback {

        void retryPageLoad();

        void onShowDetails(NewsItem newsItem);
    }
}