package com.github.abhijit.placefinder.data.web.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceDetails {
    @SerializedName("result")
    private Result result;

    @SerializedName("status")
    private String status;

    public Result getResult() {
        return result;
    }

    public boolean isStatusOk() {
        return "OK".equals(status);
    }

    public String getStatus() {
        return status;
    }

    public static class Result {
        @SerializedName("name")
        private String name;
        @SerializedName("formatted_address")
        private String address;
        @SerializedName("formatted_phone_number")
        private String phone;
        @SerializedName("photos")
        private List<Photo> photos;
        @SerializedName("rating")
        private Double rating;
        @SerializedName("reviews")
        private List<Review> review;

        @SerializedName("geometry")
        private Geometry geometry;

        public String getName() {
            return name;
        }

        public String getAddress() {
            return address;
        }

        public String getPhone() {
            return phone;
        }

        public Double getRating() {
            return rating;
        }

        public List<Photo> getPhotos() {
            return photos;
        }

        public List<Review> getReview() {
            return review;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public static class Review {

            @SerializedName("author_name")
            private String authorName; //": "Charles Rosendahl",

            @SerializedName("profile_photo_url")
            private String profilePhotoUrl; //": "https://lh5.googleusercontent.com/-JKoCDPftT9c/AAAAAAAAAAI/AAAAAAAAAAA/E6eVDUT-OQw/s128-c0x00000000-cc-rp-mo/photo.jpg",

            @SerializedName("rating")
            private String rating; //": 4,

            @SerializedName("relative_time_description")
            private String timestamp; //": "2 weeks ago",

            @SerializedName("text")
            private String text; //": "Remodeled lobby, free WiFi, fireplace, direct tv with a remote, and a nice snack bar with liquor and coffee.",

            public String getAuthorName() {
                return authorName;
            }

            public String getProfilePhotoUrl() {
                return profilePhotoUrl;
            }

            public String getRating() {
                return rating;
            }

            public String getTimestamp() {
                return timestamp;
            }

            public String getText() {
                return text;
            }
        }

    }
}
