package com.assignment.dataprovider.source;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pankaj Kumar on 27/03/18.
 * EAT | DRINK | CODE
 */
public class NewsItem extends Item implements Parcelable, Comparable<NewsItem> {

    @SerializedName("descendants")
    private int descendants;

    @SerializedName("score")
    private int score;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    public NewsItem() {

    }

    public NewsItem(final String by, final int id, final List<Integer> kids,
            final int time, final String type, final int descendants, final int score,
            final String title, final String url) {
        this.by = by;
        this.id = id;
        this.kids = kids;
        this.time = time;
        this.type = type;
        this.descendants = descendants;
        this.score = score;
        this.title = title;
        this.url = url;
    }

    public int getDescendants() {
        return descendants;
    }

    public int getScore() {
        return score;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getBy() {
        return by;
    }

    public int getId() {
        return id;
    }

    public List<Integer> getKids() {
        return kids;
    }

    public int getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public void setKids(final List<Integer> kids) {
        this.kids = kids;
    }

    @Override
    public String toString() {
        return "NewsItem{" +
                "descendants=" + descendants +
                ", score=" + score +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", by='" + by + '\'' +
                ", id=" + id +
                ", kids=" + kids +
                ", time=" + time +
                ", type='" + type + '\'' +
                '}';
    }

    protected NewsItem(Parcel in) {
        by = in.readString();
        id = in.readInt();
        if (in.readByte() == 0x01) {
            kids = new ArrayList<>();
            in.readList(kids, Integer.class.getClassLoader());
        } else {
            kids = null;
        }
        time = in.readInt();
        type = in.readString();
        descendants = in.readInt();
        score = in.readInt();
        title = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(by);
        dest.writeInt(id);
        if (kids == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(kids);
        }
        dest.writeInt(time);
        dest.writeString(type);
        dest.writeInt(descendants);
        dest.writeInt(score);
        dest.writeString(title);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Creator<NewsItem> CREATOR = new Creator<NewsItem>() {
        @Override
        public NewsItem createFromParcel(Parcel in) {
            return new NewsItem(in);
        }

        @Override
        public NewsItem[] newArray(int size) {
            return new NewsItem[size];
        }
    };

    @Override
    public int compareTo(NewsItem compareTo) {

        //ascending order
        // return this.id - compareTo.getId();

        //descending order. to keep latest item on top
        return compareTo.getId() - this.id;
    }
}