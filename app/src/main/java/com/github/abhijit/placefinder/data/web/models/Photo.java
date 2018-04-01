package com.github.abhijit.placefinder.data.web.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Photo implements Parcelable {
    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
    @SerializedName("html_attributions")
    private List<String> htmlAttributions;
    @SerializedName("photo_reference")
    private String photoReference;
    @SerializedName("height")
    private Integer height;
    @SerializedName("width")
    private Integer width;

    protected Photo(Parcel in) {
        htmlAttributions = in.createStringArrayList();
        photoReference = in.readString();
        if (in.readByte() == 0) {
            height = null;
        } else {
            height = in.readInt();
        }
        if (in.readByte() == 0) {
            width = null;
        } else {
            width = in.readInt();
        }
    }

    public String getPhotoReference() {
        return photoReference;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(htmlAttributions);
        dest.writeString(photoReference);
        if (height == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(height);
        }
        if (width == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(width);
        }
    }
}