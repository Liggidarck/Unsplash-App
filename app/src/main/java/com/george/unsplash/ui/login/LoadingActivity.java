package com.george.unsplash.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.george.unsplash.databinding.ActivityLoadingBinding;
import com.george.unsplash.localdata.PreferencesViewModel;
import com.george.unsplash.ui.main.MainActivity;

public class LoadingActivity extends AppCompatActivity {

    ActivityLoadingBinding binding;
    PreferencesViewModel preferencesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoadingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferencesViewModel = new ViewModelProvider(this).get(PreferencesViewModel.class);

        String token = preferencesViewModel.getToken();
        if(!token.equals("")) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();


    }
}