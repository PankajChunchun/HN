package com.assignment.hackersnews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.assignment.dataprovider.source.remote.NetworkModule;
import java.io.File;

/**
 * Base activity for each activity. Common codes can be placed here.
 *
 * Created by Pankaj Kumar on 29/03/18.
 * EAT | DRINK | CODE
 */
public class BaseActivity extends AppCompatActivity {

    protected Deps deps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getCacheDir(), "responses");
        deps = DaggerDeps.builder().networkModule(new NetworkModule(cacheFile)).build();
    }

    public Deps getDeps() {
        return deps;
    }
}