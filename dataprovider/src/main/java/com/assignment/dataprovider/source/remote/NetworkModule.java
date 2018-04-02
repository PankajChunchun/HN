package com.assignment.dataprovider.source.remote;

import android.util.Log;
import com.assignment.dataprovider.BuildConfig;
import dagger.Module;
import dagger.Provides;
import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.inject.Singleton;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Configuration class to connect with network using network library what is being used.
 * This file can be changed if networking library needs to change, like using volley instead Retrofit.
 *
 * Created by Pankaj Kumar on 28/03/18.
  * EAT | DRINK | CODE
 */
@Module
public class NetworkModule {

    File cacheFile;

    public NetworkModule(File cacheFile) {
        this.cacheFile = cacheFile;
    }

    @Provides
    @Singleton
    public Retrofit provideCall() {
        Cache cache = null;
        try {
            cache = new Cache(cacheFile, 10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkHttpClient.Builder builder = new Builder();
        try {
            builder.sslSocketFactory(new TLSSocketFactory(), provideX509TrustManager());
        } catch (KeyManagementException e) {
            // e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // e.printStackTrace();
        }

        OkHttpClient okHttpClient = builder
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        // Customize the request
                        Request request = original.newBuilder()
                                .header("Content-Type", "application/json")
                                .removeHeader("Pragma")
                                .header("Cache-Control", String.format("max-age=%d", BuildConfig.CACHETIME))
                                .build();

                        okhttp3.Response response = chain.proceed(request);
                        response.cacheResponse();
                        // Customize or return the response
                        return response;
                    }
                })
                .cache(cache)

                .build();

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASEURL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public NetworkService providesNetworkService(
            Retrofit retrofit) {
        return retrofit.create(NetworkService.class);
    }

    @Provides
    @Singleton
    @SuppressWarnings("unused")
    public NetworkServiceManager providesService(
            NetworkService networkService) {
        return new NetworkServiceManager(networkService);
    }

    public X509TrustManager provideX509TrustManager() {
        try {
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            factory.init((KeyStore) null);
            TrustManager[] trustManagers = factory.getTrustManagers();
            return (X509TrustManager) trustManagers[0];
        } catch (NoSuchAlgorithmException | KeyStoreException exception) {
            Log.e(getClass().getSimpleName(), "not trust manager available", exception);
        }

        return null;
    }
}