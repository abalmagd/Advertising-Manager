package com.example.advertisingmanager;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
    private ImageView nAvatar;
    private Button updateData;
    private EditText nUsername;
    private EditText nEmail;
    private EditText nBio;
    private Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_account_data);
        nAvatar = findViewById(R.id.img_profile);
        nUsername = findViewById(R.id.oPass);
        nEmail = findViewById(R.id.nPass);
        nBio = findViewById(R.id.nBio);
        updateData = findViewById(R.id.updateData);

        //Hiding hint text views
        //unHint.setVisibility(View.INVISIBLE);
        //emailHint.setVisibility(View.INVISIBLE);

        // Adding underline to the text buttons
        //Button uploadImg = findViewById(R.id.btn_upload_img);
        //uploadImg.setPaintFlags(uploadImg.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        Button changePass = findViewById(R.id.btn_change_pass);
        changePass.setPaintFlags(changePass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        extras = getIntent().getExtras();
        if (extras != null) {
            nUsername.setText(extras.getString("username"));
            nEmail.setText(extras.getString("email"));
            nBio.setText(extras.getString("bio"));
            Picasso.get().load("https://o6ugproject.s3.amazonaws.com/" + extras.getString("avatar")).noFade().into(nAvatar);
        }

        dataValidation();
    }

    public void changePassword(View view) {
        Intent intent = new Intent(this, ChangePasswordActivity.class);
        intent.putExtra("n_username", nUsername.getText());
        intent.putExtra("n_email", nEmail.getText());
        intent.putExtra("n_bio", nBio.getText());
        startActivity(intent);
    }

    public void back(Boolean i) {
        if (i) {
            nUsername.setFocusable(false);
            nEmail.setFocusable(false);
            nBio.setFocusable(false);

            makeChangeDataRequest("https://crew-project.herokuapp.com/advertisers/me");
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

    private void dataValidation() {
        final boolean[] valid = new boolean[1];

        String unRegex = "^[a-zA-Z0-9_-]{3,16}$";
        String emailRegex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

        nUsername.addTextChangedListener(new TextChangedListener<EditText>(nUsername) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (nUsername.getText().toString().matches(unRegex)) {
                    //unHint.setTextColor(getResources().getColor(R.color.colorGreen_A400));
                    //unHint.setText("Username is valid");
                    //unHint.setVisibility(View.VISIBLE);

                    //if(!emailHint.getText().toString().equals("Please enter a valid Email")) {
                    //updateData.setColorFilter(getResources().getColor(R.color.colorGreen_A400));
                    //valid[0] = true;
                    //}


                }
                else {
                    //unHint.setTextColor(getResources().getColor(R.color.colorRed_900));
                    //unHint.setText("Username should be 3-16 characters long\nand can only include characters");
                    //unHint.setVisibility(View.VISIBLE);
                    //valid[0] = false;
                    //updateData.setColorFilter(getResources().getColor(R.color.colorRed_900));
                }
            }
        });

        nEmail.addTextChangedListener(new TextChangedListener<EditText>(nEmail) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                if (nEmail.getText().toString().matches(emailRegex)) {
                    //emailHint.setTextColor(getResources().getColor(R.color.colorGreen_A400));
                    //emailHint.setText("Email is valid");
                    //emailHint.setVisibility(View.VISIBLE);


                    //if(!unHint.getText().toString().equals("Username should be 3-16 characters long\nand can only include characters")) {
                    //updateData.setColorFilter(getResources().getColor(R.color.colorGreen_A400));
                    //valid[0] = true;
                    //}

                }
                else {
                    //emailHint.setTextColor(getResources().getColor(R.color.colorRed_900));
                    //emailHint.setText("Please enter a valid Email");
                    //emailHint.setVisibility(View.VISIBLE);
                    //updateData.setColorFilter(getResources().getColor(R.color.colorRed_900));
                    //valid[0] = false;
                }
            }
        });

        nBio.addTextChangedListener(new TextChangedListener<EditText>(nBio) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                //if(!unHint.getText().toString().equals("Username should be 3-16 characters long\nand can only include characters") && !emailHint.getText().toString().equals("Please enter a valid Email"))
                //updateData.setColorFilter(getResources().getColor(R.color.colorGreen_A400));
            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back(valid[0]);
            }
        });
    }

    private void makeChangeDataRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        final SessionManager manager = SessionManager.getInstance(this);
        Map<String, String> postParam = new HashMap<>();
        postParam.put("name", nUsername.getText().toString());
        postParam.put("email", nEmail.getText().toString());
        postParam.put("bio", nBio.getText().toString());
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
        startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), 1);
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
                nAvatar.setImageBitmap(bitmap);
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