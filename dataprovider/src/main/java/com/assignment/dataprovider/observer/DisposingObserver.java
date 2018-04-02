package com.assignment.dataprovider.observer;

import android.support.annotation.CallSuper;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Pankaj Kumar on 29/03/18.
 * pankaj@kloojj.com
 * EAT | DRINK | CODE
 */
public class DisposingObserver<T> implements Observer<T> {

    @Override
    @CallSuper
    public void onSubscribe(Disposable d) {
        DisposableManager.add(d);
    }

    @Override
    public void onNext(T next) {
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onComplete() {
    }
}