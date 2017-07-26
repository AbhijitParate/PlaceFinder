package com.github.abhijit.placefinder.data.placesclient;

import com.github.abhijit.placefinder.retrofit.Api;
import com.github.abhijit.placefinder.retrofit.RetroFitInstance;
import com.github.abhijit.placefinder.retrofit.models.Places;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import io.reactivex.annotations.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlacesClientImpl implements PlacesClient {

    private static final String TAG = PlacesClientImpl.class.getSimpleName();

    private static PlacesClientImpl client;
    private Api api;

    private PlacesClientImpl() {
        if (api == null) {
            api = RetroFitInstance.getRetrofit().create(Api.class);
        }
    }

    public static PlacesClientImpl getClient() {
        if (client == null){
            client = new PlacesClientImpl();
        }
        return client;
    }

    @Override
    public Maybe<Places> getPlaces(final double lat, final double lng, final int radius) {
        return Maybe.create(new MaybeOnSubscribe<Places>() {
            @Override
            public void subscribe(@NonNull final MaybeEmitter<Places> e) throws Exception {
                api.getPlaces(lat + "," + lng, radius)
                        .enqueue( new Callback<Places>() {
                            @Override
                            public void onResponse(@android.support.annotation.NonNull Call<Places> call, @android.support.annotation.NonNull Response<Places> response) {
                                e.onSuccess(response.body());
                                e.onComplete();
                            }

                            @Override
                            public void onFailure(@android.support.annotation.NonNull Call<Places> call, @android.support.annotation.NonNull Throwable t) {
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
            public void subscribe(@NonNull final MaybeEmitter<Places> e) throws Exception {
                api.searchPlaces(lat + "," + lng, radius, query)
                        .enqueue( new Callback<Places>() {
                                      @Override
                                      public void onResponse(@android.support.annotation.NonNull Call<Places> call, @android.support.annotation.NonNull Response<Places> response) {
                                          e.onSuccess(response.body());
                                          e.onComplete();
                                      }

                                      @Override
                                      public void onFailure(@android.support.annotation.NonNull Call<Places> call, @android.support.annotation.NonNull Throwable t) {
                                          e.onError(t);
                                      }
                                  }
                        );
            }
        });
    }
}
