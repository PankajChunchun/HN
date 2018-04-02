package com.assignment.hackersnews.newslist;

import com.assignment.dataprovider.BuildConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Local store which is being used to handle pagination for list of ids.
 * It holds all ids and on request, calculates next list of ids for which
 * NEWS details needs to be fetched.
 *
 * Created by Pankaj Kumar on 30/03/18.
 * EAT | DRINK | CODE
 */
public enum PaginatedListStore {
    INSTANCE;

    /**
     * Default page size
     */
    public static final int DEFAULT_PAGE_SIZE = BuildConfig.LIMIT;

    /**
     * Page size
     */
    private int mPageSize = DEFAULT_PAGE_SIZE;

    /**
     * List of ids on pagination will be performed
     */
    private ArrayList<Integer> mIds;


    /**
     * Current page
     */
    private int mCurrentPage = -1;

    /**
     * Maximum number of pages
     */
    private int mMaxPages;

    public ArrayList<Integer> getList() {
        return mIds;
    }

    public void setList(final ArrayList<Integer> listOfId) {
        this.mIds = listOfId;
        this.mCurrentPage = -1;
        this.mMaxPages = 1;
        calculatePages();
    }

    private void calculatePages() {
        if (mPageSize > 0) {
            // calculate how many pages there are
            if (mIds.size() % mPageSize == 0) {
                mMaxPages = mIds.size() / mPageSize;
            } else {
                mMaxPages = (mIds.size() / mPageSize) + 1;
            }
        }
    }

    public List<Integer> getNextPage() {
        mCurrentPage = mCurrentPage + 1;
        if (mCurrentPage >= mMaxPages) {
            return null;
        }
        int startIndex = (mCurrentPage - 1) * mPageSize;
        if (startIndex < 0) {
            startIndex = 0;
        }

        int endIndex = startIndex + mPageSize;
        if (endIndex > mIds.size()) {
            endIndex = mIds.size();
        }

        return mIds.subList(startIndex, endIndex);
    }

    public int getPageCount() {
        return mMaxPages;
    }

    /**
     * Sort list to get latest news first
     */
    public void sort() {
        Collections.sort(mIds, Collections.reverseOrder());
    }
}