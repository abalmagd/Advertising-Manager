package com.example.advertisingmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextView oPassHint;
    private TextView nPassHint;
    private EditText oPass;
    private EditText nPass;
    private ImageButton updateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        updateData = findViewById(R.id.updateData);
        oPass = findViewById(R.id.oPass);
        nPass = findViewById(R.id.nPass);
        oPassHint = findViewById(R.id.oPassHint);
        nPassHint = findViewById(R.id.nPassHint);

        oPassHint.setVisibility(View.INVISIBLE);
        nPassHint.setVisibility(View.INVISIBLE);

        dataValidation();
    }

    public void back(Boolean i) {
        if (i) {
            oPass.setFocusable(false);
            nPass.setFocusable(false);

            //makeChangeDataRequest("https://crew-project.herokuapp.com/advertisers/me");
        }
        else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked

                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();

                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Data won't be changed\nAre you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
        }
    }

    /*private void makeChangeDataRequest(String url)
    {
        RequestQueue queue = Volley.newRequestQueue(this);
        final SessionManager manager = SessionManager.getInstance(this);
        Map<String, String> postParam = new HashMap<>();
        postParam.put("name", nUsername.getText().toString());
        postParam.put("email", nEmail.getText().toString());
        postParam.put("bio", nBio.getText().toString());
        // TODO: Send Avatar

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PATCH, url, new JSONObject(postParam), response ->
        {
            try
            {
                if (response.getBoolean("success"))
                {
                    Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, HomeActivity.class));
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
    }*/

    private void dataValidation() {
        final boolean[] valid = new boolean[1];

        oPass.addTextChangedListener(new TextChangedListener<EditText>(oPass) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (oPass.getText().toString().equals("12345"/*TODO: Check on old pass*/)) {
                    //oPassHint.setTextColor(getResources().getColor(R.color.colorGreen_A400));
                    oPassHint.setText("Old Password is valid");
                    oPassHint.setVisibility(View.VISIBLE);


                    if (!nPassHint.getText().toString().equals("Must be at least\n4 characters long")) {
                        //updateData.setColorFilter(getResources().getColor(R.color.colorGreen_A400));
                        valid[0] = true;
                    }

                }
                else {
                    //oPassHint.setTextColor(getResources().getColor(R.color.colorRed_900));
                    oPassHint.setText("Incorrect Password");
                    oPassHint.setVisibility(View.VISIBLE);
                    //updateData.setColorFilter(getResources().getColor(R.color.colorRed_900));
                    valid[0] = false;
                }
            }
        });

        nPass.addTextChangedListener(new TextChangedListener<EditText>(nPass) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (nPass.getText().toString().length() >= 4) {
                    //nPassHint.setTextColor(getResources().getColor(R.color.colorGreen_A400));
                    nPassHint.setText("Password is valid");
                    nPassHint.setVisibility(View.VISIBLE);

                    if (!oPassHint.getText().toString().equals("Incorrect Password")) {
                        //updateData.setColorFilter(getResources().getColor(R.color.colorGreen_A400));
                        valid[0] = true;
                    }


                }
                else {
                    //nPassHint.setTextColor(getResources().getColor(R.color.colorRed_900));
                    nPassHint.setText("Must be at least\n4 characters long");
                    nPassHint.setVisibility(View.VISIBLE);
                    valid[0] = false;
                    //updateData.setColorFilter(getResources().getColor(R.color.colorRed_900));
                }
            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back(valid[0]);
            }
        });
    }
}