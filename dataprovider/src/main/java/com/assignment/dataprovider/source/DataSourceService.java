package com.assignment.dataprovider.source;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import java.util.List;

/**
 * Data source service interface which can be implemented by each data providers
 * like network or local datasource.
 *
 * Created by Pankaj Kumar on 01/04/18.
 * EAT | DRINK | CODE
 */
public interface DataSourceService {

    Disposable getNewsIdentifiers(final NetworkServiceCallback callback);

    Disposable getNewsWithIds(@NonNull final List<Integer> newsIds,
            @NonNull final NetworkServiceCallback callback);

    Disposable getCommentWithIds(@NonNull final List<Integer> commentIds,
            @NonNull final NetworkServiceCallback callback);
}