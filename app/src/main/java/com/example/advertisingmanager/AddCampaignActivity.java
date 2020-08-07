package com.example.advertisingmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class AddCampaignActivity extends AppCompatActivity {

    //private float maxBudget;
    private EditText et_name,
            et_link,
            et_budget,
            et_banner_size,
            et_click_price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_campaign);
        et_name = findViewById(R.id.et_name);
        et_link = findViewById(R.id.et_link);
        et_budget = findViewById(R.id.et_budget);
        et_banner_size = findViewById(R.id.et_banner_size);
        et_click_price = findViewById(R.id.et_click_price);

        /*extras = getIntent().getExtras();
        Log.e("budget", extras.getFloat("budget") + "");
        maxBudget = extras.getFloat("budget");*/
    }

    public void confirm(View view) {
        if (!(isEmpty(et_name) && isEmpty(et_link) && isEmpty(et_budget)
                && isEmpty(et_banner_size) && isEmpty(et_click_price))) {
            makeChangeDataRequest();
        }

        else
            Toast.makeText(this, "Either a field is empty or budget max reached", Toast.LENGTH_SHORT).show();
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public void back(View view) {
        finish();
    }

    private void makeChangeDataRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);
        final SessionManager manager = SessionManager.getInstance(this);
        Map<String, String> postParam = new HashMap<>();
        postParam.put("name", et_name.getText().toString());
        postParam.put("link", et_link.getText().toString());
        postParam.put("budget", et_budget.getText().toString());
        postParam.put("banner_size", et_banner_size.getText().toString());
        postParam.put("click_price", et_click_price.getText().toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                "https://stark-ridge-68501.herokuapp.com/campaigns/",
                new JSONObject(postParam), response -> {
            try {
                if (response.getBoolean("success")) {
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                    finish();
                    startActivity(new Intent(this, HomeActivity.class));
                } else {
                    Log.e("error", response.getString("error"));
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            VolleyLog.d("Error: ", error.getMessage());
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            // Passing request headers

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> auth = new HashMap<>();
                auth.put("Authorization", manager.getToken());
                return auth;
            }
        };

        // Adding request to request queue
        queue.add(jsonObjReq);
    }
}