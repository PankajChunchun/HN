package com.assignment.hackersnews;

import com.assignment.dataprovider.source.remote.NetworkModule;
import com.assignment.hackersnews.commentlist.CommentListFragment;
import com.assignment.hackersnews.newslist.NewsListFragment;
import dagger.Component;
import javax.inject.Singleton;

/**
 * Created by Pankaj Kumar on 29/03/18.
 * pankaj@kloojj.com
 * EAT | DRINK | CODE
 */
@Singleton
@Component(modules = {NetworkModule.class,})
public interface Deps {

    void inject(NewsListFragment newsListFragment);
    void inject(CommentListFragment commentListFragment);
}