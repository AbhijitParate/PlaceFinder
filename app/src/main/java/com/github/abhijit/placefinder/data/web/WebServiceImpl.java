package com.github.abhijit.placefinder.data.web;

import android.support.annotation.NonNull;

import com.github.abhijit.placefinder.data.web.models.PlaceDetails;
import com.github.abhijit.placefinder.data.web.models.Places;
import com.google.android.gms.maps.model.LatLng;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


class WebServiceImpl implements WebService {

    private static final String TAG = WebServiceImpl.class.getSimpleName();

    private Api api;

    WebServiceImpl() {
        api = RetroFitInstance.getRetrofit().create(Api.class);
    }

    @Override
    public Maybe<Places> getPlaces(final LatLng latLng, final int radius) {
        return Maybe.create(new MaybeOnSubscribe<Places>() {
            @Override
            public void subscribe(final MaybeEmitter<Places> e) {
                api.getPlaces(String.format("%s,%s", latLng.latitude, latLng.longitude), radius)
                        .enqueue(new WebCallback<Places>(e) {
                            @Override
                            String getErrorString(Places body) {
                                return body.getStatus();
                            }

                            @Override
                            boolean isStatusOK(Places places) {
                                return places != null && places.isStatusOk();
                            }
                        });
            }
        });
    }

    @Override
    public Maybe<Places> searchPlaces(final LatLng latLng, final int radius, final String query) {
        return Maybe.create(new MaybeOnSubscribe<Places>() {
            @Override
            public void subscribe(@NonNull final MaybeEmitter<Places> e) {
                api.searchPlaces(String.format("%s,%s", latLng.latitude, latLng.longitude), radius, query)
                        .enqueue(new WebCallback<Places>(e) {
                            @Override
                            String getErrorString(Places body) {
                                return body.getStatus();
                            }

                            @Override
                            boolean isStatusOK(Places places) {
                                return places != null && places.isStatusOk();
                            }
                        });
            }
        });
    }

    @Override
    public Maybe<Places> getNextPlaces(final String nextPageToken) {
        return Maybe.create(new MaybeOnSubscribe<Places>() {
            @Override
            public void subscribe(final MaybeEmitter<Places> e) {
                api.getNextPlaces(nextPageToken)
                        .enqueue(new WebCallback<Places>(e) {
                            @Override
                            String getErrorString(Places body) {
                                return body.getStatus();
                            }

                            @Override
                            boolean isStatusOK(Places places) {
                                return places != null && places.isStatusOk();
                            }
                        });
            }
        });
    }

    @Override
    public Maybe<PlaceDetails> getPlaceDetails(final String placeId) {
        return Maybe.create(new MaybeOnSubscribe<PlaceDetails>() {
            @Override
            public void subscribe(final MaybeEmitter<PlaceDetails> e) {
                api.getPlaceDetails(placeId)
                        .enqueue(new WebCallback<PlaceDetails>(e) {
                            @Override
                            String getErrorString(PlaceDetails body) {
                                return body.getStatus();
                            }

                            @Override
                            boolean isStatusOK(PlaceDetails placeDetails) {
                                return placeDetails != null && placeDetails.isStatusOk();
                            }
                        });
            }
        });
    }

    private abstract class WebCallback<T> implements Callback<T> {

        private MaybeEmitter<T> e;

        WebCallback(MaybeEmitter<T> e) {
            this.e = e;
        }

        @Override
        public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
            if (response.isSuccessful() && isStatusOK(response.body())) {
                e.onSuccess(response.body());
            } else {
                e.onError(new Throwable(getErrorString(response.body())));
            }
        }

        abstract String getErrorString(T body);

        abstract boolean isStatusOK(T t);

        @Override
        public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
            e.onError(t);
            t.printStackTrace();
        }
    }

    private static class RetroFitInstance {
        private static final String BASE_URL = "https://maps.googleapis.com";

        private static Retrofit getRetrofit() {
            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }
}
