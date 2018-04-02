package com.assignment.hackersnews.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import org.robolectric.Robolectric;

/**
 * Created by Pankaj Kumar on 02/04/18.
 * EAT | DRINK | CODE
 */
public class RobolectricUtils {

    /**
     * Starts {@link Fragment}.
     */
    public static void startFragment(Fragment fragment) {
        FragmentActivity activity = Robolectric.buildActivity(FragmentActivity.class)
                .create()
                .start()
                .resume()
                .get();

        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, "TAG");
        fragmentTransaction.commit();
    }
}