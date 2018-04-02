package com.assignment.dataprovider.source.remote;

import android.util.Log;
import com.assignment.dataprovider.source.DataSourceService;
import com.assignment.dataprovider.source.NetworkServiceCallback;
import com.assignment.dataprovider.source.NewsItem;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Network manager class which is using network service class who will do
 * actual network call (in current Retrofit)
 *
 * Created by Pankaj Kumar on 28/03/18.
 * EAT | DRINK | CODE
 */
public class NetworkServiceManager implements DataSourceService {

    private static final String TAG = NetworkServiceManager.class.getSimpleName();
    private final NetworkService mNetworkService;

    public NetworkServiceManager(NetworkService networkService) {
        this.mNetworkService = networkService;
    }

    /**
     * Get list of news id which would be shown on request (user request)
     *
     * @param callback - {@link com.assignment.dataprovider.source.NetworkServiceCallback}
     * @return - {@link Disposable} which can be used to observe dataset
     */
    @Override
    public Disposable getNewsIdentifiers(final NetworkServiceCallback callback) {

        return mNetworkService.getNewsIdentifiers().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<Integer>>() {
                    @Override
                    public void onNext(List<Integer> newsArticleIds) {
                        Log.d(TAG, "Onsucess " + newsArticleIds);
                        Collections.sort(newsArticleIds);
                        callback.onSuccess(newsArticleIds);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError " + e.getCause());
                        // e.printStackTrace();
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete ");
                    }
                });
    }

    /**
     * Get news articles for given set of id
     *
     * @param newsIds  - List of news id for which news article would be shown
     * @param callback - {@link NetworkServiceCallback}
     * @return {@link Disposable} which can be used to observe dataset
     */
    @Override
    public Disposable getNewsWithIds(@NonNull final List<Integer> newsIds,
            @NonNull final NetworkServiceCallback callback) {

        List<Observable<?>> observables = new ArrayList<>(newsIds.size());
        for (Integer id : newsIds) {
            observables.add(mNetworkService.getNewsItemWithId(id).subscribeOn(Schedulers.io()));
        }

        return Observable.merge(observables).toList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Object>() {
                    @Override
                    public void onSuccess(final Object o) {
                        ArrayList<NewsItem> items = (ArrayList) o;
                        Collections.sort(items);
                        callback.onSuccess(items);
                    }

                    @Override
                    public void onError(final Throwable e) {
                        callback.onError(new NetworkError(e));
                    }
                });
    }

    /**
     * Get comments or Reply on comment for given list of ids
     *
     * @param commentIds - List of comment or reply id
     * @param callback   - {@link NetworkServiceCallback}
     * @return {@link Disposable} which can be used to observe dataset
     */
    @Override
    public Disposable getCommentWithIds(@NonNull final List<Integer> commentIds,
            @NonNull final NetworkServiceCallback callback) {

        List<Observable<?>> observables = new ArrayList<>(commentIds.size());
        for (Integer id : commentIds) {
            observables.add(mNetworkService.getCommentsWithId(id).subscribeOn(Schedulers.io()));
        }

        return Observable.merge(observables).toList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<Object>() {
                    @Override
                    public void onSuccess(final Object o) {
                        callback.onSuccess(o);
                    }

                    @Override
                    public void onError(final Throwable e) {
                        callback.onError(new NetworkError(e));
                    }
                });
    }
}