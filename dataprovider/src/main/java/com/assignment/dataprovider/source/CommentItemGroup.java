package com.assignment.dataprovider.source;

import java.util.List;

/**
 * Created by Pankaj Kumar on 30/03/18.
 * EAT | DRINK | CODE
 */
public class CommentItemGroup {

    public CommentItem parent;
    public List<CommentItem> childs;

    public CommentItemGroup() {
    }

    public CommentItemGroup(final CommentItem parent, final List<CommentItem> childs) {
        this.parent = parent;
        this.childs = childs;
    }
}