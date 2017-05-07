package com.example.notices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class register extends Activity {

    private static final String TAG = register.class.getSimpleName();
    private EditText name;
    private EditText year;
    private EditText myClass;
    private EditText rollno;
    private EditText email;
    private EditText password;
    private EditText password2;
    private Button signUp;
    private ProgressDialog pDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = (EditText) findViewById(R.id.name);
        year = (EditText) findViewById(R.id.year);
        myClass = (EditText) findViewById(R.id.myClass);
        rollno = (EditText) findViewById(R.id.rollno);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);
        signUp = (Button) findViewById(R.id.signUp);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        signUp.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String Sname = name.getText().toString().trim();
                        String Semail = email.getText().toString().trim();
                        String Syear = year.getText().toString().trim();
                        String SmyClass = myClass.getText().toString().trim();
                        String Srollno = rollno.getText().toString().trim();
                        String Spassword = password.getText().toString().trim();
                        String Spassword2 = password2.getText().toString().trim();

                        if (Sname.isEmpty() && Syear.isEmpty() && SmyClass.isEmpty() && Srollno.isEmpty() && Semail.isEmpty() && Spassword.isEmpty() && Spassword2.isEmpty()) {
                            Toast.makeText(getApplicationContext(),
                                    "Please enter your details!", Toast.LENGTH_LONG)
                                    .show();
                        }
                        else if(!Spassword.equals(Spassword2)) {
                            Toast.makeText(getApplicationContext(),
                                    "Passwords don't match", Toast.LENGTH_LONG)
                                    .show();
                        }
                        else {
                            registerUser(Sname, Syear, SmyClass, Srollno, Semail, Spassword);
                        }
                    }
                });
    }


    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     */
    private void registerUser(final String name, final String year, final String myClass, final String rollno, final String email,
                              final String password) {
        // Tag used to cancel the request
        String tag_string_req = "req_register";

        pDialog.setMessage("Registering ...");
        showDialog();

//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                AppConfig.URL_STUDENT_REGISTER, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.v(TAG, "Register Response: " + response.toString());
//                hideDialog();
//
//                try {
//                    JSONObject jObj = new JSONObject(response);
//                    boolean error = jObj.getBoolean("error");
//                    if (!error) {
//                        // User successfully stored in MySQL
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
//                        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
//                        startActivity(i);
//                        finish();
//                    } else {
//
//                        // Error occurred in registration. Get the error
//                        // message
//                        String errorMsg = jObj.getString("error_msg");
//                        Toast.makeText(getApplicationContext(),
//                                errorMsg, Toast.LENGTH_LONG).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "Registration Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_LONG).show();
//                hideDialog();
//            }
//        }) {
//
//            @Override
//            protected Map<String, String> getParams() {
//                // Posting params to register url
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("name", name);
//                params.put("year", year);
//                params.put("branch", myClass);
//                params.put("rollno", rollno);
//                params.put("email", email);
//                params.put("password", password);
//
//                return params;
//            }
//
//        };
//
//        // Adding request to request queue
//        AppController.getInstance(getApplicationContext()).addToRequestQueue(strReq, tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}