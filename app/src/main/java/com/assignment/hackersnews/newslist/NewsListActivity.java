package com.assignment.hackersnews.newslist;

import android.os.Bundle;
import com.assignment.hackersnews.BaseActivity;
import com.assignment.hackersnews.R;

/**
 * {@link android.support.v7.app.AppCompatActivity} which contains {@link NewsListFragment}
 * to show News list.
 *
 * Created by Pankaj Kumar on 28/03/18.
 * EAT | DRINK | CODE
 */
public class NewsListActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list_activity_layout);

        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            NewsListFragment newsListFragment = new NewsListFragment();
            newsListFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, newsListFragment,
                            NewsListFragment.class.getSimpleName()).commit();
        }
    }
}