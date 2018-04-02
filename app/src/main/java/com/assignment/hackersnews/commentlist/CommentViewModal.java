package com.assignment.hackersnews.commentlist;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import com.assignment.dataprovider.source.CommentItemGroup;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * {@link ViewModel} which is being used to cache comments and reuse during orientation change,
 * which was fetched from network
 *
 * Created by Pankaj Kumar on 31/03/18.
 * EAT | DRINK | CODE
 */
public class CommentViewModal extends ViewModel {

    public MutableLiveData<ArrayList<CommentItemGroup>> mDataSet
            = new MutableLiveData<ArrayList<CommentItemGroup>>();

    public Set<Integer> mExpandedParents = new HashSet<>();
}