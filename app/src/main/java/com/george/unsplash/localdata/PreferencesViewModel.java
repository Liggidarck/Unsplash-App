package com.george.unsplash.localdata;

import static com.george.unsplash.utils.Keys.USER_PREFERENCES;
import static com.george.unsplash.utils.Keys.USER_TOKEN;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class PreferencesViewModel extends AndroidViewModel {

    final SharedPreferences sharedPreferences;
    final SharedPreferences.Editor editor;

    public PreferencesViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setToken() {

    }

    public String getToken() {
        return sharedPreferences.getString(USER_TOKEN, "");
    }

}
