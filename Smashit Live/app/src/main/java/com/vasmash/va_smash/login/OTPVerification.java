package com.vasmash.va_smash.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import static com.vasmash.va_smash.VASmashAPIS.APIs.Submitotp;
import static com.vasmash.va_smash.VASmashAPIS.APIs.resendotp_url;
//this is the otp verification class
public class OTPVerification extends AppCompatActivity {
    Dialog dialog;

    LinePinField linePinField ;
    TextView regtime,resendotp,otptxt,resendstop;
    Button regbtn;
    RequestQueue sch_RequestQueue;
    String otpenterd ="null";
    String id;
    public int counter;
    SharedPreferences pref;
    String number;


    String output;
    MyCount counter1;

    // Change by Lucifer
    long seconds;
    //this is the loader animationview
    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        linePinField = findViewById(R.id.regotp);
        regtime = findViewById(R.id.regtime);
        regbtn = findViewById(R.id.regbtn);
        resendotp = findViewById(R.id.resendotp);
        otptxt=findViewById(R.id.otptxt);
        resendstop=findViewById(R.id.resnedstop);
        viewDialog = new ViewDialog(this);


        pref = getApplicationContext().getSharedPreferences("loginid", 0);

        sch_RequestQueue = Volley.newRequestQueue(getApplicationContext());

        counter1 = new MyCount(3600000,1000);
        counter1.start();


        Intent intent=getIntent();
        number=intent.getStringExtra("number");
        otptxt.setText("OTP sent to" + " " + number + " " + " Please check and enter");

        //this is the enter otp pin
        linePinField.setOnTextCompleteListener(new PinField.OnTextCompleteListener() {
            @Override
            public boolean onTextComplete (@NotNull String enteredText) {
                // Toast.makeText(OTPVerification.this,enteredText,Toast.LENGTH_SHORT).show();
                otpenterd=enteredText;
                return true; // Return true to keep the keyboard open else return false to close the keyboard
            }
        });

        //this ia the send otp post
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String ootp=edtotp.getText().toString().trim();
                String idS=pref.getString("id", null);

                if (otpenterd.isEmpty() && otpenterd.equals("null") ) {
                    //Toast.makeText(RequestOTP.this, "Please enter the All Fields", Toast.LENGTH_SHORT).show();
                    popup("Please enter OTP");
                } else {

                    String stotp = "{\"id\":\"" + idS + "\",\"otp\":\"" + otpenterd + "\"}";
                   // Log.d("jsnresponse pernonal", "---" + stotp);
                    String url = Submitotp;
                    JSONObject sbmtotp;
                    try {
                        sbmtotp = new JSONObject(stotp);
                       // Log.d("jsnresponse....", "---" + stotp);
                        viewDialog.showDialog();
                        submitotp(sbmtotp, url);

                    } catch (JSONException ignored) {
                    }
                }

            }

        });
        //this is the resend submit post
        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linePinField.getText().clear();
                counter=0;

                counter1 = new MyCount(3600000,1000);
                counter1.start();


                String loginS = "{\"username\":\"" + number + "\"}";
               // Log.d("jsnresponse loginotp", "---" + loginS);
                String url=resendotp_url;
                JSONObject lstrmdt;

                try {
                    lstrmdt = new JSONObject(loginS);
                   // Log.d("jsnresponse....", "---" + loginS);
                    viewDialog.showDialog();
                    submitotp(lstrmdt, url);
                } catch (JSONException ignored) {
                }
            }
        });


    }

    private void submitotp(JSONObject lstrmdt, String url) {

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                      //  Log.d("JSONSenderVolleylogin", "---" + response);
                        viewDialog.hideDialog();

                        try {
                            id = response.getString("id");
                            String msg=response.getString("message");
                            //dialogbox(msg);
                           // Log.d("otp",id+" "+msg);
                            if (response.has("status")) {
                                String status = response.getString("status");
                                if (status.equals("1")) {
                                    Intent i = new Intent(OTPVerification.this, RegisterformActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            }else {
                                popup(msg);
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
                        case 400:
                            try {
                                body = new String(error.networkResponse.data,"UTF-8");
                               // Log.d("body", "---" + body);
                                JSONObject obj1 = new JSONObject(body);
                                if (obj1.has("errors")) {
                                    viewDialog.hideDialog();
                                    id = obj1.getString("id");
                                    JSONObject errors = obj1.getJSONObject("errors");
                                    String message = errors.getString("message");
                                   // Log.d("msg",  message);
                                    popup(message);
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;


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
            sch_RequestQueue = Volley.newRequestQueue(OTPVerification.this);
        }
        sch_RequestQueue.add(req);
    }
    public void finishActivity(View v){
        finish();
    }

    //this is the resend timer
    public String formatTime(long millis)
    {
        output = "";
        seconds = millis / 1000;

        seconds = seconds % 60;

        String secondsD = String.valueOf(seconds);

        //output = hoursD+" : "+minutesD + " : " + secondsD;
        output =  " "+secondsD +" "+"sec";


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
        final Context mContext = OTPVerification.this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.responsepopup, null);


        Button okbtn = (Button) layout.findViewById(R.id.okbtn);
        TextView message = (TextView) layout.findViewById(R.id.msg);
        message.setText(msg);

        okbtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linePinField.getText().clear();
                        dialog.dismiss();
                    }
                });


        builder = new android.app.AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();

    }



}
