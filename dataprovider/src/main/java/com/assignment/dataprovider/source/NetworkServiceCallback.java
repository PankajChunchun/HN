package com.assignment.dataprovider.source;

import com.assignment.dataprovider.source.remote.NetworkError;

/**
 * Created by Pankaj Kumar on 01/04/18.
 * EAT | DRINK | CODE
 */
public interface NetworkServiceCallback {

    void onSuccess(Object response);

    void onError(NetworkError networkError);
}