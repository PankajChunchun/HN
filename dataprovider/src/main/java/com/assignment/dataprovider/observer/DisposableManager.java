package com.assignment.dataprovider.observer;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Pankaj Kumar on 29/03/18.
 * pankaj@kloojj.com
 * EAT | DRINK | CODE
 */
public class DisposableManager {

    private static CompositeDisposable compositeDisposable;

    public static void add(Disposable disposable) {
        getCompositeDisposable().add(disposable);
    }

    public static void dispose() {
        getCompositeDisposable().dispose();
    }

    private static CompositeDisposable getCompositeDisposable() {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        return compositeDisposable;
    }

    private DisposableManager() {
    }
}