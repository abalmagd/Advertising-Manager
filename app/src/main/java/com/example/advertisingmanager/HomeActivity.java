package com.example.advertisingmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
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

public class HomeActivity extends AppCompatActivity {

    String avatarURL;
    private ImageView img_avatar;
    private TextView tv_name;
    private TextView tv_email;
    private TextView tv_bio;
    private TextView tv_balance_count;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        img_avatar = findViewById(R.id.img_avatar);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_bio = findViewById(R.id.tv_bio);
        tv_balance_count = findViewById(R.id.tv_balance_count);

        fetchProfileData("https://crew-project.herokuapp.com/advertisers/me");

    }

    public void logout(View view) {
        SessionManager manager = new SessionManager();
        manager.logout();
        finish();
        startActivity(new Intent(this, WelcomeScreenActivity.class));
    }

    public void settings(View view) {
        Intent intent = new Intent(this, ChangeAccountDataActivity.class);
        intent.putExtra("username", tv_name.getText());
        intent.putExtra("email", tv_email.getText());
        intent.putExtra("bio", tv_bio.getText());
        intent.putExtra("avatar", avatarURL);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    private void fetchProfileData(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final SessionManager manager = SessionManager.getInstance(this);
        Map<String, String> postParam = new HashMap<>();
        postParam.put("token", manager.getToken());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(postParam), response ->
        {
            try {
                if (response.getBoolean("success")) {
                    Picasso
                            .get()
                            .load("https://o6ugproject.s3.amazonaws.com/"
                                    + response.getJSONObject("advertiser")
                                    .getString("avatar"))
                            .noFade()
                            .into(img_avatar);

                    tv_name.setText(response.getJSONObject("advertiser").getString("name"));
                    tv_email.setText(response.getJSONObject("advertiser").getString("email"));
                    tv_bio.setText(response.getJSONObject("advertiser").getString("bio"));
                    password = response.getJSONObject("advertiser").getString("password");
                    avatarURL = response.getJSONObject("advertiser").getString("avatar");
                    tv_balance_count.setText(" " + response.getJSONObject("advertiser").getString("balance") + "LE");

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
}