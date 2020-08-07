package com.example.advertisingmanager;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    String t;
    private EditText et_username;
    private EditText et_password;
    private ProgressBar pb_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        pb_loading = findViewById(R.id.pb_loading);
    }

    public void moveToRegistration(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }

    public void login(View view) {
        t = et_password.getText().toString();
        validation(et_username.getText().toString(), et_password.getText().toString());
    }

    private void validation(String username, String password) {
        String emailRegex = "^[\\w-_.+]*[\\w-_.]@([\\w]+\\.)+[\\w]+[\\w]$";

        if (username.matches(emailRegex)) {
            if (password.length() != 0) {
                et_username.setEnabled(false);
                et_password.setEnabled(false);
                makeLoginReq();
            }
            else
                Toast.makeText(getApplicationContext(),
                        "Password field is blank.",
                        Toast.LENGTH_SHORT).show();

        }
        else
            Toast.makeText(getApplicationContext(), "Must be a valid email!",
                    Toast.LENGTH_SHORT).show();
    }


    private void makeLoginReq() {
        pb_loading.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        final SessionManager manager = SessionManager.getInstance(this);

        Map<String, String> postParam = new HashMap<>();
        postParam.put("email", et_username.getText().toString());
        postParam.put("password", et_password.getText().toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://stark-ridge-68501.herokuapp.com/advertisers/login",
                new JSONObject(postParam),
                response ->
                {
                    pb_loading.setVisibility(View.INVISIBLE);
                    try {
                        if (response.getBoolean("success")) {
                            manager.logIn(response.getString("token"));
                            finish();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), response.getString("message"),
                                    Toast.LENGTH_LONG).show();
                            et_username.setEnabled(true);
                            et_password.setEnabled(true);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        et_username.setEnabled(true);
                        et_password.setEnabled(true);
                    }
                }, error ->
        {
            pb_loading.setVisibility(View.INVISIBLE);
            VolleyLog.d("Error: ", error.getMessage());
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            et_username.setEnabled(true);
            et_password.setEnabled(true);
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
