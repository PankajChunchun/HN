package com.assignment.dataprovider.source;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pankaj Kumar on 28/03/18.
 * EAT | DRINK | CODE
 */
public class CommentItem extends Item implements Parcelable, Comparable<CommentItem> {

    @SerializedName("parent")
    public int parent;

    @SerializedName("text")
    public String text;

    private transient boolean isEmptyView;

    public CommentItem() {

    }

    public CommentItem(final String by, final int id, final List<Integer> kids, final int time,
            final String type, final int parent, final String text, final boolean isEmptyView) {
        this.by = by;
        this.id = id;
        this.kids = kids;
        this.time = time;
        this.type = type;
        this.parent = parent;
        this.text = text;
        this.isEmptyView = isEmptyView;
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

    public int getParent() {
        return parent;
    }

    public String getText() {
        return text;
    }

    public boolean isEmptyView() {
        return isEmptyView;
    }

    public void setEmptyView(final boolean emptyView) {
        isEmptyView = emptyView;
    }

    @Override
    public String toString() {
        return "CommentItem{" +
                "by='" + by + '\'' +
                ", id=" + id +
                ", kids=" + kids +
                ", parent=" + parent +
                ", text='" + text + '\'' +
                ", time=" + time +
                ", type='" + type + '\'' +
                '}';
    }

    protected CommentItem(Parcel in) {
        by = in.readString();
        id = in.readInt();
        if (in.readByte() == 0x01) {
            kids = new ArrayList<>();
            in.readList(kids, Integer.class.getClassLoader());
        } else {
            kids = null;
        }
        parent = in.readInt();
        text = in.readString();
        time = in.readInt();
        type = in.readString();
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
        dest.writeInt(parent);
        dest.writeString(text);
        dest.writeInt(time);
        dest.writeString(type);
    }

    @SuppressWarnings("unused")
    public static final Creator<CommentItem> CREATOR = new Creator<CommentItem>() {
        @Override
        public CommentItem createFromParcel(Parcel in) {
            return new CommentItem(in);
        }

        @Override
        public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };

    @Override
    public int compareTo(CommentItem compareTo) {

        //ascending order
        // return this.id - compareTo.getId();

        //descending order. to keep latest item on top
        return compareTo.getId() - this.id;
    }
}