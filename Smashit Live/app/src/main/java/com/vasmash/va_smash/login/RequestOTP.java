package com.vasmash.va_smash.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.poovam.pinedittextfield.LinePinField;
import com.poovam.pinedittextfield.PinField;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.VASmashAPIS.APIs.forgetotp_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.resendotp_url;
//this is the request otp class
public class RequestOTP extends AppCompatActivity {
    Dialog dialog;
    LinePinField enterotp;
    Button sendotpbtn;

    TextView resendotp,regtime,otptxt,resendstop;
    String otpenterd="null";
    public int counter;
    String clickid,number;

    RequestQueue sch_RequestQueue;

    String output;
    MyCount counter1;

    // Change by Lucifer
    long seconds;
    //this is the loader animationview
    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_otp);
        enterotp=findViewById(R.id.enterotp);
        sendotpbtn=findViewById(R.id.sendotpbtn);
        resendotp=findViewById(R.id.resendotp);
        regtime = findViewById(R.id.regtime);
        otptxt=findViewById(R.id.otptxt);
        resendstop=findViewById(R.id.resnedstop);
        sch_RequestQueue = Volley.newRequestQueue(getApplicationContext());
        viewDialog = new ViewDialog(this);


        counter1 = new MyCount (3600000,1000);
        counter1.start();

        Intent intent=getIntent();
        clickid=intent.getStringExtra("id");
        number=intent.getStringExtra("number");

        otptxt.setText("OTP sent to" + " " + number + " " + " Please check and enter");

/*
        if (number.length() < 9 || number.length() > 13){
            otptxt.setText("OTP sent to" + " " + number + " " + " check your Phone");
        }else {
            otptxt.setText("OTP sent to" + " " + number + " " + " check your email");
        }
*/
        //this is the enter otp pin
        enterotp.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete (@NotNull String enteredText) {
                // Toast.makeText(OTPVerification.this,enteredText,Toast.LENGTH_SHORT).show();
                otpenterd=enteredText;
                return true; // Return true to keep the keyboard open else return false to close the keyboard
            }
        });

        //this ia the send otp post
        sendotpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final String otpS = enterotp.getText().toString();

                if (otpenterd.isEmpty() && otpenterd.equals("null") ) {
                    //Toast.makeText(RequestOTP.this, "Please enter the All Fields", Toast.LENGTH_SHORT).show();
                    popup("Please enter OTP");
                } else {

                    String loginS = "{\"id\":\"" + clickid + "\",\"otp\":\"" + otpenterd + "\"}";
                    Log.d("jsnresponse loginotp", "---" + loginS);
                    String url=forgetotp_url;
                    JSONObject lstrmdt;

                    try {
                        lstrmdt = new JSONObject(loginS);
                        Log.d("jsnresponse....", "---" + loginS);
                        viewDialog.showDialog();
                        JSONSenderVolleylogin(lstrmdt, url);

                    } catch (JSONException ignored) {
                    }
                }
            }
        });
        //this is the resend submit post
        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final String otpS = enterotp.getText().toString();
                enterotp.getText().clear();
                counter=0;
                counter1 = new MyCount (3600000,1000);
                counter1.start();


                String loginS = "{\"username\":\"" + number + "\"}";
                Log.d("jsnresponse loginotp", "---" + loginS);
                String url=resendotp_url;
                JSONObject lstrmdt;

                try {
                    lstrmdt = new JSONObject(loginS);
                    Log.d("jsnresponse....", "---" + loginS);
                    viewDialog.showDialog();
                    JSONSenderVolleylogin(lstrmdt, url);

                } catch (JSONException ignored) {
                }
            }
        });

    }

    public void JSONSenderVolleylogin(JSONObject lstrmdt, String url) {
        Log.d("555555", "login" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONSenderVolleylogin", "---" + response);
                        viewDialog.hideDialog();
                        try {
                            if (response.has("message")){
                                String id = response.getString("id");
                                String message=response.getString("message");
                                // Toast.makeText(RequestOTP.this, message, Toast.LENGTH_SHORT).show();
                                if (message.equals("Login OTP sent")){
                                   // dialogbox(message);
                                    popup(message);
                                }else {
                                    Intent i = new Intent(RequestOTP.this, SetNewPassword.class);
                                    i.putExtra("otp", otpenterd);
                                    i.putExtra("id", clickid);
                                    startActivity(i);
                                }
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
                                Log.d("body", "---" + body);
                                JSONObject obj = new JSONObject(body);

                                if (obj.has("errors")) {
                                    viewDialog.hideDialog();
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    //Toast.makeText(RequestOTP.this, message, Toast.LENGTH_SHORT).show();
                                    popup(message);
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
    //this is the resend timer
    public String formatTime(long millis)
    {
        output = "";
        seconds = millis / 1000;

        seconds = seconds % 60;

        String secondsD = String.valueOf(seconds);

        //output = hoursD+" : "+minutesD + " : " + secondsD;
        output = " "+secondsD +" "+"sec";


        return output;
    }

    public class MyCount extends CountDownTimer
    {
        Context mContext;

        public MyCount(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }


        public void onTick (long millisUntilFinished)
        {
            regtime.setText ( formatTime(millisUntilFinished));

            if ( seconds == 0 )
            {
               // Toast.makeText( getApplicationContext(), "Done", Toast.LENGTH_LONG ).show();
                counter1.cancel();
                resendstop.setVisibility(View.GONE);
                resendotp.setVisibility(View.VISIBLE);


            }else {
                resendstop.setVisibility(View.VISIBLE);
                resendotp.setVisibility(View.GONE);

            }
        }

        public void onFinish() {}

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        counter1.cancel();
    }

    public void popup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = RequestOTP.this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.responsepopup, null);


        Button okbtn = (Button) layout.findViewById(R.id.okbtn);
        TextView message = (TextView) layout.findViewById(R.id.msg);
        message.setText(msg);

        okbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        enterotp.getText().clear();
                        dialog.dismiss();
                    }
                });


        builder = new android.app.AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();

    }

    public void finishActivity(View v){
        finish();
    }


}
