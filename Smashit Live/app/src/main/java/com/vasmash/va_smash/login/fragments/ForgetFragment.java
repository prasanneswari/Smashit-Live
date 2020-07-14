package com.vasmash.va_smash.login.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.login.RequestOTP;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vasmash.va_smash.VASmashAPIS.APIs.forgetpwd_url;


//this is the Forget Class
public class ForgetFragment extends AppCompatActivity {
    Dialog dialog;

    EditText requestotp;
    Button submit;
    RequestQueue sch_RequestQueue;
    LinearLayout rootlay;
    //this is the loader animationview
    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_forget);

        requestotp=findViewById(R.id.requestotp);
        submit=findViewById(R.id.regst_btn_otp);
        rootlay=findViewById(R.id.rootlay);

        sch_RequestQueue = Volley.newRequestQueue(getApplicationContext());
        viewDialog = new ViewDialog(this);

        //this is the otp post submit code
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String mailS = requestotp.getText().toString();
                //this is the mobile validation condition
                if (isValidMobile(mailS)) {
                    String loginS = "{\"username\":\"" + mailS + "\",\"countryCode\":\"" + " " + "\"}";
                    //Log.d("jsnresponse loginotp", "---" + loginS);
                    String url=forgetpwd_url;
                    JSONObject lstrmdt;

                    try {
                        lstrmdt = new JSONObject(loginS);
                        //Log.d("jsnresponse....", "---" + loginS);
                        viewDialog.showDialog();
                        JSONSenderVolleylogin(lstrmdt, url);

                    } catch (JSONException ignored) {

                    }
                }
                //this is the email validation condition
                else if (isEmailValid(mailS)){
                    String loginS = "{\"username\":\"" + mailS + "\",\"countryCode\":\"" + " " + "\"}";
                    //Log.d("jsnresponse loginotp", "---" + loginS);
                    String url=forgetpwd_url;
                    JSONObject lstrmdt;
                    try {
                        lstrmdt = new JSONObject(loginS);
                        //Log.d("jsnresponse....", "---" + loginS);
                        viewDialog.showDialog();
                        JSONSenderVolleylogin(lstrmdt, url);

                    } catch (JSONException ignored) {
                    }
                }else{
                    if (mailS.isEmpty()) {
                        //Toast.makeText(RegisterFragment.this, "Please enter the All Fields", Toast.LENGTH_SHORT).show();
                        popup("Please enter email Id/mobile number");
                    }else{
                        String regexStr = "^[0-9]*$";
                        if (requestotp.getText().toString().trim().matches(regexStr)) {
                            //write code here for success
                            popup("Please enter valid mobile number");
                        } else {
                            popup("Please enter valid email Id");
                        }
                    }
                }
            }
        });

        //this is the keybord hiding code
        rootlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager im=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(requestotp.getWindowToken(),0);

            }
        });
    }
    public void JSONSenderVolleylogin(JSONObject lstrmdt, String url) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        //Log.d("555555", "login" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // Log.d("JSONSenderVolleylogin", "---" + response);
                        viewDialog.hideDialog();
                        // Toast.makeText(getActivity(), "Login Successful", Toast.LENGTH_SHORT).show();
                        try {
                            if (response.length()!=0) {
                                String _id = response.getString("id");
                                String message = response.getString("message");
                               // Toast.makeText(ForgetFragment.this, message, Toast.LENGTH_SHORT).show();

                                //if (message.equals("Please Verify OTP sent to Mobile Number")) {
                                Intent intent = new Intent(ForgetFragment.this, RequestOTP.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("id", _id);
                                intent.putExtra("number", requestotp.getText().toString());
                                startActivity(intent);
                                // }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body;
                //get status code here
                //Log.d("statusCode", "---" + error.toString());
                NetworkResponse response = error.networkResponse;
                if(response != null && response.data != null){
                    switch(response.statusCode){
                        case 422:
                            try {
                                body = new String(error.networkResponse.data,"UTF-8");
                               // Log.d("body", "---" + body);
                                JSONObject obj = new JSONObject(body);
                                String id = null;
                                if (obj.has("id")) {
                                    id = obj.getString("id");
                                }
                                if (obj.has("errors")) {
                                    viewDialog.hideDialog();
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                  //  Toast.makeText(ForgetFragment.this, message, Toast.LENGTH_SHORT).show();
                                    popup(message);
                                    requestotp.getText().clear();

                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                //do stuff with the body...
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                //return (headers != null || headers.isEmpty()) ? headers : super.getHeaders();
                return headers;
            }
        };
        jsonObjReq.setTag("");
        addToRequestQueue(jsonObjReq);
        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                viewDialog.hideDialog();
            }
        });

    }

    public <T> void addToRequestQueue(Request<T> req) {
        if (sch_RequestQueue == null) {
            sch_RequestQueue = Volley.newRequestQueue(this);
        }
        sch_RequestQueue.add(req);
    }

    //this is the response popup code
    public void popup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.responsepopup, null);


        Button okbtn = (Button) layout.findViewById(R.id.okbtn);
        TextView message = (TextView) layout.findViewById(R.id.msg);
        message.setText(msg);

        okbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


        builder = new android.app.AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();

    }

    //this is the mobile validation code
    public static boolean isValidMobile(String phone) {
        boolean check = false;
        if (!Pattern.matches("[a-zA-Z]+", phone)) {
            if (phone.length() < 5 || phone.length() > 13) {
                // if(phone.length() != 10) {
                check = false;
                // txtPhone.setError("Not Valid Number");
            } else {
                check = android.util.Patterns.PHONE.matcher(phone).matches();
            }
        } else {
            check = false;
        }
        return check;
    }
    //this is the mail validation code
    public static boolean isEmailValid(String email) {
        boolean check;
        Pattern p;
        Matcher m;
        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        p = Pattern.compile(EMAIL_STRING);
        m = p.matcher(email);
        check = m.matches();

        return check;
    }
}

