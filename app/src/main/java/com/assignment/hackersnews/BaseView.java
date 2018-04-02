package com.assignment.hackersnews;

/**
 * Base view class for each views
 *
 * Created by Pankaj Kumar on 28/03/18.
 * EAT | DRINK | CODE
 */
public interface BaseView<T> {

    void setPresenter(T presenter);
}