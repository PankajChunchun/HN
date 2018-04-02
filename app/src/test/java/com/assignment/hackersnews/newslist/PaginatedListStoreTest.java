package com.assignment.hackersnews.newslist;

import com.assignment.hackersnews.BuildConfig;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.*;
import org.junit.runner.*;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by Pankaj Kumar on 02/04/18.
 * EAT | DRINK | CODE
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, constants = BuildConfig.class)
public class PaginatedListStoreTest {

    private ArrayList<Integer> ids;
    private PaginatedListStore store;

    @Before
    public void setUp() {
        ids = getAllIds();
        store = PaginatedListStore.INSTANCE;
        store.setList(ids);
    }

    @Test
    public void verifyGetPageCount() {
        Assert.assertEquals(store.getPageCount(), 4);
    }

    @Test
    public void verifyGetNextPage() {
        Assert.assertEquals(store.getNextPage().size(), 25);
        Assert.assertEquals(store.getNextPage().size(), 25);
        Assert.assertEquals(store.getNextPage().size(), 25);
        Assert.assertEquals(store.getNextPage().size(), 25);
        Assert.assertNull(store.getNextPage());
    }

    @Test
    public void verifyGetPageCountSizeChange() {
        store.setList(getLessThanPageSize());
        Assert.assertEquals(store.getPageCount(), 1);
    }

    private static ArrayList<Integer> getAllIds() {
        ArrayList<Integer> items = new ArrayList<>();
        items.addAll(Arrays.asList(9224, 8952, 8917, 8884, 8887, 8943, 8869, 8940, 8908, 8958,
                9005, 8873, 9671, 9067, 9055, 8865, 8881, 8872, 8955, 10403, 8903, 8928, 9125,
                8998, 8901, 8902, 8907, 8894, 8870, 8878, 8980, 8934, 8876, 9224, 8952, 8917,
                8884, 8887, 8943, 8869, 8940, 8908, 8958, 9005, 8873, 9671, 9067, 9055, 8865,
                8881, 8872, 8955, 1040, 8903, 8928, 9125, 8998, 8901, 8902, 8907, 8894, 8870,
                8878, 8980, 8934, 8876, 9224, 8952, 8917, 8884, 8887, 8943, 8869, 8940, 8908,
                9005, 8873, 9671, 9067, 9055, 8865, 8881, 8872, 8955, 10403, 8903, 8928, 9125,
                8998, 8901, 8902, 8907, 8894, 8870, 8878, 8980, 8934, 8876, 8958, 8798));
        return items;
    }

    private static ArrayList<Integer> getLessThanPageSize() {
        ArrayList<Integer> items = new ArrayList<>();
        items.addAll(Arrays.asList(8998, 8901, 8902, 8907, 8894, 8870, 8878, 8980, 8934, 8876, 8958, 8798));
        return items;
    }
}