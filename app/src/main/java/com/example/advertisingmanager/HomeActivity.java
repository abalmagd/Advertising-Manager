package com.example.advertisingmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    String avatarURL;
    private ImageView img_avatar;
    private TextView tv_name;
    private TextView tv_email;
    private TextView tv_bio;
    private TextView tv_balance_count;
    private ProgressBar pb_profile;
    private NavigationView nav_view;
    private DrawerLayout drawer;
    private TextView navUsername;
    private TextView navEmail;
    private ImageView navImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        //Objects.requireNonNull(getSupportActionBar()).hide();
        ImageButton ib_menu = findViewById(R.id.ib_menu);
        nav_view = findViewById(R.id.navigation_drawer);
        drawer = findViewById(R.id.drawer_layout);

        img_avatar = findViewById(R.id.img_profile);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_bio = findViewById(R.id.tv_bio);
        tv_balance_count = findViewById(R.id.tv_balance_count);
        pb_profile = findViewById(R.id.pb_profile);

        View headerView = nav_view.getHeaderView(0);
        navUsername = headerView.findViewById(R.id.nav_name);
        navEmail = headerView.findViewById(R.id.nav_email);
        navImg = headerView.findViewById(R.id.nav_img);


        fetchProfileData("https://crew-project.herokuapp.com/advertisers/me");

        nav_view.setNavigationItemSelectedListener(this);
        ib_menu.setOnClickListener(v ->
        {
            drawer.openDrawer(nav_view, true);
        });
    }

    private void fetchProfileData(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final SessionManager manager = SessionManager.getInstance(this);
        Map<String, String> postParam = new HashMap<>();
        postParam.put("token", manager.getToken());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, url, new JSONObject(postParam), response ->
        {
            pb_profile.setVisibility(View.INVISIBLE);
            try {
                if (response.getBoolean("success")) {
                    Picasso
                            .get()
                            .load("https://o6ugproject.s3.amazonaws.com/" + response.getJSONObject("advertiser").getString("avatar"))
                            .noFade()
                            .into(img_avatar);

                    Picasso.get().load(response.getJSONObject("advertiser").getString("avatar")).noFade().into(navImg);

                    tv_name.setText(response.getJSONObject("advertiser").getString("name"));
                    navUsername.setText(response.getJSONObject("advertiser").getString("name"));
                    tv_email.setText(response.getJSONObject("advertiser").getString("email"));
                    navEmail.setText(response.getJSONObject("advertiser").getString("email"));
                    tv_bio.setText(response.getJSONObject("advertiser").getString("bio"));
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dark_mode:

                break;
            case R.id.acc_settings:
                Intent intent = new Intent(this, ChangeAccountDataActivity.class);
                intent.putExtra("username", tv_name.getText());
                intent.putExtra("email", tv_email.getText());
                intent.putExtra("bio", tv_bio.getText());
                intent.putExtra("avatar", avatarURL);
                startActivity(intent);
                break;
            case R.id.logout:
                SessionManager manager = SessionManager.getInstance(this);
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                manager.logout();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}