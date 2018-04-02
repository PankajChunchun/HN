package com.assignment.hackersnews.commentlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.assignment.dataprovider.source.NewsItem;
import com.assignment.hackersnews.BaseActivity;
import com.assignment.hackersnews.R;
import com.assignment.hackersnews.util.Constants;

/**
 * {@link android.app.Activity} which holds {@link CommentListFragment}
 *
 * Created by Pankaj Kumar on 28/03/18.
 * EAT | DRINK | CODE
 */
public class CommentListActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list_activity_layout);

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }

            CommentListFragment commentList = new CommentListFragment();
            commentList.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, commentList,
                            CommentListFragment.class.getSimpleName()).commit();
        }
    }

    public static void show(Context context, NewsItem newsItem) {
        Intent intent = new Intent(context, CommentListActivity.class);
        Bundle args = new Bundle();
        args.putParcelable(Constants.KEY_ARGS_ARTICLE, newsItem);
        intent.putExtras(args);
        context.startActivity(intent);
    }
}