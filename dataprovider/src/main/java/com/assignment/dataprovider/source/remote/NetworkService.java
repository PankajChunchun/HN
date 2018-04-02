package com.assignment.dataprovider.source.remote;

import com.assignment.dataprovider.source.CommentItem;
import com.assignment.dataprovider.source.NewsItem;
import io.reactivex.Observable;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Interface for Retrofit
 *
 * Created by Pankaj Kumar on 28/03/18.
 * EAT | DRINK | CODE
 */
public interface NetworkService {

    @GET("v0/topstories.json")
    Observable<List<Integer>> getNewsIdentifiers();

    @GET("v0/item/{news_id}.json")
    Observable<NewsItem> getNewsItemWithId(@Path(value = "news_id", encoded = true) int newsId);

    @GET("v0/item/{comment_id}.json")
    Observable<CommentItem> getCommentsWithId(@Path(value = "comment_id", encoded = true) int commentId);
}