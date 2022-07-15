package com.george.unsplash.network.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.george.unsplash.network.api.UnsplashInterface;
import com.george.unsplash.network.api.UnsplashTokenClient;
import com.george.unsplash.network.models.Statistic.Statistic;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatisticRepository {

    UnsplashInterface unsplashInterface;
    private final MutableLiveData<Statistic> statistic = new MutableLiveData<>();

    public StatisticRepository(String token){
        unsplashInterface = UnsplashTokenClient.getUnsplashTokenClient(token).create(UnsplashInterface.class);
    }

    public MutableLiveData<Statistic> getStatistic(String username) {
        unsplashInterface.getUserStatistic(username).enqueue(new Callback<Statistic>() {
            @Override
            public void onResponse(@NonNull Call<Statistic> call, @NonNull Response<Statistic> response) {
                if(response.code() == 200) {
                    statistic.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Statistic> call, @NonNull Throwable t) {
                statistic.postValue(null);
            }
        });


        return statistic;
    }

}