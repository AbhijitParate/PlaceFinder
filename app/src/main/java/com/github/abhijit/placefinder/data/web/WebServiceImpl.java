package com.github.abhijit.placefinder.data.web;

import android.support.annotation.NonNull;

import com.github.abhijit.placefinder.data.web.models.PlaceDetails;
import com.github.abhijit.placefinder.data.web.models.Places;

import io.reactivex.Completable;
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
    public Maybe<Places> getPlaces(final double lat, final double lng, final int radius) {
        return Maybe.create(new MaybeOnSubscribe<Places>() {
            @Override
            public void subscribe(final MaybeEmitter<Places> e) {
                api.getPlaces(lat + "," + lng, radius)
                        .enqueue(new Callback<Places>() {
                                     @Override
                                     public void onResponse(@NonNull Call<Places> call,
                                                            @NonNull Response<Places> response) {
                                         e.onSuccess(response.body());
                                         e.onComplete();
                                     }

                                     @Override
                                     public void onFailure(@NonNull Call<Places> call,
                                                           @NonNull Throwable t) {
                                         e.onError(t);
                                     }
                                 }
                        );
            }
        });
    }

    @Override
    public Maybe<Places> searchPlaces(final double lat, final double lng, final int radius, final String query) {
        return Maybe.create(new MaybeOnSubscribe<Places>() {
            @Override
            public void subscribe(@NonNull final MaybeEmitter<Places> e) {
                api.searchPlaces(String.format("%s,%s", lat, lng), radius, query)
                        .enqueue(new Callback<Places>() {
                                     @Override
                                     public void onResponse(@NonNull Call<Places> call,
                                                            @NonNull Response<Places> response) {
                                         e.onSuccess(response.body());
                                         e.onComplete();
                                     }

                                     @Override
                                     public void onFailure(@NonNull Call<Places> call,
                                                           @NonNull Throwable t) {
                                         e.onError(t);
                                     }
                                 }
                        );
            }
        });
    }

    @Override
    public Maybe<Places> getNextPlaces(final String nextPageToken) {
        return Maybe.create(new MaybeOnSubscribe<Places>() {
            @Override
            public void subscribe(final MaybeEmitter<Places> e) {
                api.getNextPlaces(nextPageToken)
                        .enqueue(new Callback<Places>() {
                            @Override
                            public void onResponse(@NonNull Call<Places> call,
                                                   @NonNull Response<Places> response) {
                                if (response.isSuccessful()) {
                                    e.onSuccess(response.body());
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Places> call,
                                                  @NonNull Throwable t) {
                                e.onError(t);
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
                        .enqueue(new Callback<PlaceDetails>() {
                            @Override
                            public void onResponse(@NonNull Call<PlaceDetails> call,
                                                   @NonNull Response<PlaceDetails> response) {
                                if (response.isSuccessful()){
                                    e.onSuccess(response.body());
                                }

                            }

                            @Override
                            public void onFailure(@NonNull Call<PlaceDetails> call,
                                                  @NonNull Throwable t) {
                                e.onError(t);
                            }
                        });
            }
        });
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
