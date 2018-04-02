package com.assignment.dataprovider.source;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Created by Pankaj Kumar on 28/03/18.
 * EAT | DRINK | CODE
 */
public class Item {

    @SerializedName("by")
    protected String by;

    @SerializedName("id")
    protected int id;

    @SerializedName("kids")
    protected List<Integer> kids = null;

    @SerializedName("time")
    protected int time;

    @SerializedName("type")
    protected String type;
}