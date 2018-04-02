package com.github.abhijit.placefinder.data.web.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Places {
    @SerializedName("results")
    private List<Result> results;
    @SerializedName("next_page_token")
    private String nextPageToken;
    @SerializedName("status")
    private String status;

    public List<Result> getResults() {
        return results != null ? results : new ArrayList<Result>(0);
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public boolean isStatusOk() {
        return "OK".equals(status);
    }

    public String getStatus() {
        return status;
    }

    public static class Result implements Parcelable {
        public static final Creator<Result> CREATOR = new Creator<Result>() {
            @Override
            public Result createFromParcel(Parcel in) {
                return new Result(in);
            }

            @Override
            public Result[] newArray(int size) {
                return new Result[size];
            }
        };
        @SerializedName("geometry")
        private Geometry geometry;
        @SerializedName("icon")
        private String icon;
        @SerializedName("id")
        private String id;
        @SerializedName("name")
        private String name;
        @SerializedName("opening_hours")
        private OpeningHours openingHours;
        @SerializedName("photos")
        private List<Photo> photos;
        @SerializedName("place_id")
        private String placeId;
        @SerializedName("rating")
        private Double rating;
        @SerializedName("reference")
        private String reference;
        @SerializedName("scope")
        private String scope;
        @SerializedName("types")
        private List<String> types;
        @SerializedName("vicinity")
        private String vicinity;
        @SerializedName("price_level")
        private Integer priceLevel;

        public String getPlaceId() {
            return placeId;
        }

        public String getName() {
            return name;
        }

        public Double getRating() {
            return rating;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        protected Result(Parcel in) {
            geometry = in.readParcelable(Geometry.class.getClassLoader());
            icon = in.readString();
            id = in.readString();
            name = in.readString();
            openingHours = in.readParcelable(OpeningHours.class.getClassLoader());
            photos = in.createTypedArrayList(Photo.CREATOR);
            placeId = in.readString();
            if (in.readByte() == 0) {
                rating = null;
            } else {
                rating = in.readDouble();
            }
            reference = in.readString();
            scope = in.readString();
            types = in.createStringArrayList();
            vicinity = in.readString();
            if (in.readByte() == 0) {
                priceLevel = null;
            } else {
                priceLevel = in.readInt();
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(geometry, flags);
            dest.writeString(icon);
            dest.writeString(id);
            dest.writeString(name);
            dest.writeParcelable(openingHours, flags);
            dest.writeTypedList(photos);
            dest.writeString(placeId);
            if (rating == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeDouble(rating);
            }
            dest.writeString(reference);
            dest.writeString(scope);
            dest.writeStringList(types);
            dest.writeString(vicinity);
            if (priceLevel == null) {
                dest.writeByte((byte) 0);
            } else {
                dest.writeByte((byte) 1);
                dest.writeInt(priceLevel);
            }
        }

        public static class OpeningHours implements Parcelable {
            public static final Creator<OpeningHours> CREATOR = new Creator<OpeningHours>() {
                @Override
                public OpeningHours createFromParcel(Parcel in) {
                    return new OpeningHours(in);
                }

                @Override
                public OpeningHours[] newArray(int size) {
                    return new OpeningHours[size];
                }
            };
            @SerializedName("open_now")
            private Boolean openNow;
            @SerializedName("weekday_text")
            private List<Object> weekdayText;

            protected OpeningHours(Parcel in) {
                byte tmpOpenNow = in.readByte();
                openNow = tmpOpenNow == 0 ? null : tmpOpenNow == 1;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeByte((byte) (openNow == null ? 0 : openNow ? 1 : 2));
            }
        }
    }

}