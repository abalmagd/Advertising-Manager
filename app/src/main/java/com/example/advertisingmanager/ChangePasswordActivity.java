package com.example.advertisingmanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    Bundle extras;
    private EditText et_oldPassword;
    private EditText et_newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        et_oldPassword = findViewById(R.id.et_old_password);
        et_newPassword = findViewById(R.id.et_new_password);

        dataValidation();
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

    private void makeChangeDataRequest(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        final SessionManager manager = SessionManager.getInstance(this);
        Map<String, String> postParam = new HashMap<>();
        postParam.put("password", et_newPassword.getText().toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PATCH, url, new JSONObject(postParam), response ->
        {
            try
            {
                if (response.getBoolean("success"))
                {
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, ChangeAccountDataActivity.class));
                } else
                {
                    Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e)
            {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }, error ->
        {
            VolleyLog.d("Error: ", error.getMessage());
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
        })
        {
            // Passing request headers

            @Override
            public Map<String, String> getHeaders()
            {
                HashMap<String, String> auth = new HashMap<String, String>();
                auth.put("Authorization", manager.getToken());
                return auth;
            }
        };

        // Adding request to request queue
        queue.add(jsonObjReq);
    }

    private void dataValidation() {
        et_oldPassword.addTextChangedListener(new TextChangedListener<EditText>(et_oldPassword) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (et_oldPassword.getText().toString().equals(extras.getString("password"))) {
                    et_oldPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.ic_check_green, 0);
                }
                else {
                    et_oldPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                            R.drawable.ic_false, 0);
                }
            }
        });

        et_newPassword.addTextChangedListener(new TextChangedListener<EditText>(et_newPassword) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (et_newPassword.getText().toString().length() >= 4) {
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