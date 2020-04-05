package com.example.advertisingmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_username;
    private EditText et_password;
    private EditText et_name;
    private EditText et_bio;
    private ProgressBar pb_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_pass);
        et_name = findViewById(R.id.et_name);
        et_bio = findViewById(R.id.et_bio);
        pb_loading = findViewById(R.id.progressBar);
    }

    public void register(View view) {
        validation(et_username.getText().toString(),
                et_password.getText().toString(),
                et_bio.getText().toString(),
                et_name.getText().toString());
    }

    public void moveToLogin(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void validation(String email, String password, String bio, String name) {

        if (bio.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Don't leave empty fields", Toast.LENGTH_SHORT).show();
        }
        else {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (password.length() >= 5) {
                    makeRegisterReq();
                }
                else
                    Toast.makeText(getApplicationContext(), "Password must be at least 5 characters long!", Toast.LENGTH_SHORT).show();

            }
            else
                Toast.makeText(getApplicationContext(), "Must be a valid email!", Toast.LENGTH_SHORT).show();

        }
    }

    private void makeRegisterReq() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final SessionManager manager = SessionManager.getInstance(this);
        pb_loading.setVisibility(View.VISIBLE);
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("name", et_name.getText().toString());
        postParam.put("email", et_username.getText().toString());
        postParam.put("bio", et_bio.getText().toString());
        postParam.put("password", et_password.getText().toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://crew-project.herokuapp.com/advertisers/register",
                new JSONObject(postParam), response -> {
            pb_loading.setVisibility(View.INVISIBLE);
            try {
                if (response.getBoolean("success")) {
                    manager.logIn(response.getString("token"));
                    finish();
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            pb_loading.setVisibility(View.VISIBLE);
            VolleyLog.d("Error: ", error.getMessage());
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            // Passing request headers

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        // Adding request to request queue
        queue.add(jsonObjReq);
    }
}
