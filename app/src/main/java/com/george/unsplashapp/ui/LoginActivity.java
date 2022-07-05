package com.george.unsplashapp.ui;

import static com.george.unsplashapp.network.api.Keys.RESPONSE_URL;
import static com.george.unsplashapp.network.api.Keys.SECRET_KEY;
import static com.george.unsplashapp.network.api.Keys.UNSPLASH_ACCESS_KEY;
import static com.george.unsplashapp.network.api.Keys.USER_PREFERENCES;
import static com.george.unsplashapp.network.api.Keys.USER_SCOPE;
import static com.george.unsplashapp.network.api.Keys.USER_TOKEN;
import static com.george.unsplashapp.network.api.Keys.USER_TOKEN_TYPE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import com.george.unsplashapp.databinding.ActivityLoginBinding;
import com.george.unsplashapp.network.api.Keys;
import com.george.unsplashapp.network.api.UnsplashBaseClient;
import com.george.unsplashapp.network.api.UnsplashInterface;
import com.george.unsplashapp.network.models.Auth;
import com.george.unsplashapp.utils.Utils;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;

    public static final String TAG = "LoginActivity";

    final private String url = Keys.BASE_URL_AUTH + "/authorize?"
            + "client_id=" + UNSPLASH_ACCESS_KEY
            + "&response_type=code&scope=" + Keys.SCOPE
            + "&redirect_uri=" + RESPONSE_URL;

    UnsplashInterface unsplashInterface;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Utils utils = new Utils();
        sharedPreferences = getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
        unsplashInterface = UnsplashBaseClient.getBaseUnsplashClient().create(UnsplashInterface.class);

        String token = sharedPreferences.getString(USER_TOKEN, "");
        if(!token.equals("")) {
            startActivity(new Intent(this, MainActivity.class));
        }

        binding.codeBtn.setOnClickListener(v -> {
            Uri uri = Uri.parse(url);
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .setShowTitle(true)
                    .build();
            customTabsIntent.launchUrl(LoginActivity.this, uri);
        });

        binding.loginBtn.setOnClickListener(v -> {
            String code = Objects.requireNonNull(binding.userKeyEditText.getEditText()).getText().toString();
            binding.progressBarLogin.setVisibility(View.VISIBLE);

            if(code.isEmpty()) {
                binding.userKeyEditText.setError("Это поле не может быть пустым");
                binding.progressBarLogin.setVisibility(View.INVISIBLE);
                return;
            }

            if(!utils.isOnline(LoginActivity.this)) {
                Snackbar.make(binding.loginCoordinator, "Проверьте подключение к интернету", Snackbar.LENGTH_SHORT).show();
                binding.progressBarLogin.setVisibility(View.INVISIBLE);
                return;
            }

            getAccessToken(code);
        });
    }

    private void getAccessToken(String code) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        unsplashInterface.getToken(UNSPLASH_ACCESS_KEY, SECRET_KEY, RESPONSE_URL, code, "authorization_code")
                .enqueue(new Callback<Auth>() {
                    @Override
                    public void onResponse(@NonNull Call<Auth> call, @NonNull Response<Auth> response) {
                        Auth auth = response.body();
                        assert auth != null;
                        String accessToken = auth.getAccess_token();
                        String token_type = auth.getToken_type();
                        String scope = auth.getScope();
                        Log.i(TAG, "onResponse: token: " + accessToken);

                        editor.putString(USER_TOKEN, accessToken);
                        editor.putString(USER_TOKEN_TYPE, token_type);
                        editor.putString(USER_SCOPE, scope);
                        editor.putString(USER_SCOPE, scope);
                        editor.apply();

                        binding.progressBarLogin.setVisibility(View.INVISIBLE);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onFailure(@NonNull Call<Auth> call, @NonNull Throwable t) {
                        Log.e(TAG, "onFailure: " + t.getMessage());
                        binding.progressBarLogin.setVisibility(View.INVISIBLE);
                        Snackbar.make(binding.loginCoordinator, "Ошибка! " + t.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }
                });
    }


}