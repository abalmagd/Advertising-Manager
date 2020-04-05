package com.example.advertisingmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        if (SessionManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    public void moveToRegistration(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        finish();
        startActivity(intent);
    }

    public void moveToLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        finish();
        startActivity(intent);
    }
}
