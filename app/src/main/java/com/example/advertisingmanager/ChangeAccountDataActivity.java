package com.example.advertisingmanager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ChangeAccountDataActivity extends AppCompatActivity {

    Bundle extras;
    private ImageView img_avatar;
    private EditText et_name;
    private EditText et_email;
    private EditText et_bio;
    private Switch sw_darkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account_data);
        img_avatar = findViewById(R.id.img_avatar);
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_bio = findViewById(R.id.et_bio);
        sw_darkMode = findViewById(R.id.sw_dark);

        // Adding underline to the text buttons
        TextView tv_upload_image = findViewById(R.id.tv_upload_image);
        tv_upload_image.setPaintFlags(tv_upload_image.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        extras = getIntent().getExtras();
        if (extras != null) {
            et_name.setText(extras.getString("username"));
            et_email.setText(extras.getString("email"));
            et_bio.setText(extras.getString("bio"));
            Picasso.get().load("https://o6ugproject.s3.amazonaws.com/"
                    + extras.getString("avatar")).into(img_avatar);
        }

        // Validate the input fields and
        dataValidation();

        sw_darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                /*setTheme(R.style.DarkMode);
                finish();*/
            }
        });
    }

    public void confirm(View view) {
        /*Drawable[] et_nameCompoundDrawables = et_name.getCompoundDrawables();
        Drawable et_nameRightCompoundDrawable = et_nameCompoundDrawables[3];

        Drawable[] et_emailCompoundDrawables = et_name.getCompoundDrawables();
        Drawable et_emailRightCompoundDrawable = et_nameCompoundDrawables[3];

        if(et_nameRightCompoundDrawable == ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.ic_check_green)
                &&
                et_nameRightCompoundDrawable == ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.ic_check_green)) {
        }*/
    }

    public void back(View view) {
        finish();
    }

    public void changePassword(View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("password", extras.getString("password"));
        startActivity(intent);
    }

    private void dataValidation() {

        // Alphanumeric string that may include _ and – having a length of 3 to 16 characters.
        String nameRegex = "^[a-zA-Z0-9_-]{3,16}$";
        String emailRegex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

        et_name.addTextChangedListener(new TextChangedListener<EditText>(et_name) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (et_name.getText().toString().matches(nameRegex)) {

                    et_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0,
                            R.drawable.ic_check_green, 0);
                }
                else {
                    et_name.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_person, 0,
                            R.drawable.ic_false, 0);
                }
            }
        });

        et_email.addTextChangedListener(new TextChangedListener<EditText>(et_email) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (et_email.getText().toString().matches(emailRegex)) {

                    et_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email, 0,
                            R.drawable.ic_check_green, 0);
                }
                else {
                    et_email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_email, 0,
                            R.drawable.ic_false, 0);
                }
            }
        });
    }

    private void makeChangeDataRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final SessionManager manager = SessionManager.getInstance(this);
        Map<String, String> postParam = new HashMap<>();
        postParam.put("name", et_name.getText().toString());
        postParam.put("email", et_email.getText().toString());
        postParam.put("bio", et_bio.getText().toString());
        // TODO: Send Avatar

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PATCH, url, new JSONObject(postParam), response -> {
            try {
                if (response.getBoolean("success")) {
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, HomeActivity.class));
                }
                else {
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
                HashMap<String, String> auth = new HashMap<String, String>();
                auth.put("Authorization", manager.getToken());
                return auth;
            }
        };

        // Adding request to request queue
        queue.add(jsonObjReq);
    }

    public void selectImage(View view) {
        startActivityForResult(new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                img_avatar.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}