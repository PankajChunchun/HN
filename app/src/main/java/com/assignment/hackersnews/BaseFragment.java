package com.assignment.hackersnews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.assignment.dataprovider.source.remote.NetworkModule;
import java.io.File;

/**
 * Base {@link Fragment} class where common methods for each fragments can be placed.
 *
 * Created by Pankaj Kumar on 30/03/18.
 * EAT | DRINK | CODE
 */
public class BaseFragment extends Fragment {

    protected Deps deps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        File cacheFile = new File(getActivity().getCacheDir(), "responses");
        deps = DaggerDeps.builder().networkModule(new NetworkModule(cacheFile)).build();
    }

    public Deps getDeps() {
        return deps;
    }
}