package com.example.advertisingmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText et_oldPassword;
    private EditText et_newPassword;
    Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        et_oldPassword = findViewById(R.id.et_old_password);
        et_newPassword = findViewById(R.id.et_new_password);

        SessionManager manager = SessionManager.getInstance(this);

        myContext = getApplicationContext();
        dataValidation();

    }


    public void confirm(View view) {
        if(et_newPassword.getText().toString().length() >= 5)
            makeChangeDataRequest("https://crew-project.herokuapp.com/advertisers/me");
        else
            Toast.makeText(this, "Invalid Data", Toast.LENGTH_SHORT).show();
    }

    public void back(View view) {
        finish();
    }

    private void makeChangeDataRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final SessionManager manager = SessionManager.getInstance(this);
        Map<String, String> postParam = new HashMap<>();
        postParam.put("oldPassword", et_oldPassword.getText().toString());
        postParam.put("password", et_newPassword.getText().toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PATCH,
                url,
                new JSONObject(postParam), response ->
        {
            try {
                if (response.getBoolean("success")) {
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error ->
        {
            VolleyLog.d("Error: ", error.getMessage());
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            // Passing request headers

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> auth = new HashMap<String, String>();
                auth.put("Authorization", manager.getToken());
                return auth;
            }
        };

        // Adding request to request queue
        queue.add(jsonObjReq);
    }

    private void dataValidation() {

        et_newPassword.addTextChangedListener(new TextChangedListener<EditText>(et_newPassword) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (et_newPassword.getText().toString().length() >= 5) {
                    et_newPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.ic_check_green, 0);
                }
                else {
                    et_newPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.ic_false, 0);
                }
            }
        });
    }
}