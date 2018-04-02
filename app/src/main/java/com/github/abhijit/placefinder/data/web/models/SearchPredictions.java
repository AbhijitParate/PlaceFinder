package com.github.abhijit.placefinder.data.web.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchPredictions {

    @SerializedName("status")
    private String status;

    @SerializedName("predictions")
    private List<Prediction> predictions;

    public boolean isStatusOK(){
        return "OK".equalsIgnoreCase(status);
    }

    public String getStatus() {
        return status;
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public final static class Prediction implements Parcelable {

        @SerializedName("description")
        private String description;//": "San Francisco, CA, USA";

        @SerializedName("place_id")
        private String placeId;//": "ChIJIQBpAG2ahYAR_6128GcTUEo",

        protected Prediction(Parcel in) {
            description = in.readString();
            placeId = in.readString();
        }

        public static final Creator<Prediction> CREATOR = new Creator<Prediction>() {
            @Override
            public Prediction createFromParcel(Parcel in) {
                return new Prediction(in);
            }

            @Override
            public Prediction[] newArray(int size) {
                return new Prediction[size];
            }
        };

        public String getDescription() {
            return description;
        }

        public String getPlaceId() {
            return placeId;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(description);
            dest.writeString(placeId);
        }
    }

}
