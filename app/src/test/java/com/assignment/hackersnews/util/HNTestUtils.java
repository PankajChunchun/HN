package com.assignment.hackersnews.util;

import com.assignment.dataprovider.source.CommentItem;
import com.assignment.dataprovider.source.CommentItemGroup;
import com.assignment.dataprovider.source.NewsItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Pankaj Kumar on 02/04/18.
 * EAT | DRINK | CODE
 */
public class HNTestUtils {

    public static List<NewsItem> getNewsItems() {
        ArrayList<NewsItem> items = new ArrayList<>(2);
        NewsItem item = new NewsItem("norvig", 8863,
                Arrays.asList(9224, 8952, 8917, 8884, 8887, 8943, 8869, 8940, 8908, 8958, 9005, 8873, 9671, 9067),
                1175714200, "story", 71, 104, "My YC app: Dropbox",
                "http://www.getdropbox.com/u/2/screencast.html");

        NewsItem item2 = new NewsItem("mark", 8864,
                Arrays.asList(9224),
                1175714200, "story", 71, 104, "Throw away your USB drive",
                "http://www.getdropbox.com/u/2/screencast.html");
        items.add(item);
        items.add(item2);
        return items;
    }

    public static NewsItem getNewsItem() {
        NewsItem item = new NewsItem("tom", 9876,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 71, 104, "Another news",
                "http://www.getdropbox.com/u/2/screencast.html");
        return item;
    }

    public static List<CommentItemGroup> getComment() {
        ArrayList<CommentItemGroup> items = new ArrayList<>(2);
        CommentItem item = new CommentItem("test user", 1,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 8863, "comment item0", false);
        CommentItem childItem1 = new CommentItem("replied on test user comment", 123,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 1, "comment item0", false);

        CommentItem childItem2 = new CommentItem("norvig", 456,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 1, "comment item0", false);

        items.add(new CommentItemGroup(item, Arrays.asList(childItem1, childItem2)));
        return items;
    }

    public static List<CommentItemGroup> getCommentWithEmptyFields() {
        ArrayList<CommentItemGroup> items = new ArrayList<>(2);
        CommentItem item = new CommentItem("test user", 1,
                null,
                1175714200, "story", 8863, null, false);
        CommentItem childItem1 = new CommentItem("replied on test user comment", 123,
                null,
                1175714200, "story", 1, "", false);

        CommentItem childItem2 = new CommentItem("norvig", 456,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 1, "comment item0", false);

        items.add(new CommentItemGroup(item, Arrays.asList(childItem1, childItem2)));
        return items;
    }

    public static List<CommentItemGroup> getComments() {
        ArrayList<CommentItemGroup> items = new ArrayList<>(2);
        CommentItem item = new CommentItem("norvig", 1,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 8863, "comment item0", false);

        CommentItem item1 = new CommentItem("norvig", 2,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 8863, "comment item1", false);


        CommentItem item2 = new CommentItem("norvig", 3,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 8863, "comment item2", false);


        CommentItem item3 = new CommentItem("norvig", 4,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 8863, "comment item3", false);


        CommentItem item4 = new CommentItem("norvig", 5,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 8863, "comment item4", false);


        CommentItem item5 = new CommentItem("norvig", 6,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 8863, "comment item5", false);

        CommentItem childItem1 = new CommentItem("norvig", 123,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 1, "comment item0", false);

        CommentItem childItem2 = new CommentItem("norvig", 456,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 1, "comment item0", false);

        CommentItem childItem3 = new CommentItem("norvig", 156,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", 2, "comment item0", false);

        items.add(new CommentItemGroup(item, Arrays.asList(childItem1, childItem2)));
        items.add(new CommentItemGroup(item1, Arrays.asList(childItem3)));
        items.add(new CommentItemGroup(item2, new ArrayList<CommentItem>(0)));
        items.add(new CommentItemGroup(item3, new ArrayList<CommentItem>(0)));
        items.add(new CommentItemGroup(item4, new ArrayList<CommentItem>(0)));
        items.add(new CommentItemGroup(item5, new ArrayList<CommentItem>(0)));
        return items;
    }

    public static List<CommentItem> getReplies(int commentId) {
        CommentItem childItem1 = new CommentItem("norvig", 123,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", commentId, "comment item0", false);

        CommentItem childItem2 = new CommentItem("norvig", 456,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", commentId, "comment item0", false);

        CommentItem childItem3 = new CommentItem("norvig", 156,
                Arrays.asList(8873, 9671, 9067),
                1175714200, "story", commentId, "comment item0", false);

        return Arrays.asList(childItem1, childItem2, childItem3);
    }
}