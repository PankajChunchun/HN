package com.assignment.hackersnews.newslist;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.assignment.dataprovider.source.NewsItem;
import java.util.List;

/**
 * {@link ViewModel} which holds NEWS list to reuse while orientation change.
 *
 * Created by Pankaj Kumar on 31/03/18.
 * EAT | DRINK | CODE
 */
public class NewsListViewModal extends ViewModel {

    public MutableLiveData<List<NewsItem>> mNewsList = new MutableLiveData<List<NewsItem>>();
}