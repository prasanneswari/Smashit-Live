package com.vasmash.va_smash.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.BottmNavigation.TopNavigationview;
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.HomeScreen.homefragment.HomeFragment.killerplyer;
import static com.vasmash.va_smash.VASmashAPIS.APIs.activeuser_url;
//this is the setnewpasswod class
public class SetNewPassword extends AppCompatActivity {

    EditText newpwd,confirmpwd;
    Button save;
    RequestQueue sch_RequestQueue;
    String otpget,idget;
    LinearLayout rootlay;
    Dialog dialog;

    //this is the loader animationview
    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);
        newpwd=findViewById(R.id.newpwd);
        confirmpwd=findViewById(R.id.confirmpwd);
        save=findViewById(R.id.save);
        rootlay=findViewById(R.id.rootlay);
        sch_RequestQueue = Volley.newRequestQueue(getApplicationContext());
        viewDialog = new ViewDialog(this);

        Intent intent=getIntent();
        otpget=intent.getStringExtra("otp");
        idget=intent.getStringExtra("id");

        //this is the submit save post code
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newpwdS = newpwd.getText().toString();
                final String confirmpwdS = confirmpwd.getText().toString();

                if (newpwdS.equals(confirmpwdS)) {
                    if (newpwdS.isEmpty() || confirmpwdS.isEmpty()) {
                      //  Toast.makeText(SetNewPassword.this, "Please enter the All Fields", Toast.LENGTH_SHORT).show();
                        popup("Please enter the All Fields");
                    } else {

                        String loginS = "{\"id\":\"" + idget + "\",\"otp\":\"" + otpget + "\",\"password\":\"" + confirmpwdS + "\"}";
                        Log.d("jsnresponse login", "---" + loginS);
                        String url = activeuser_url;
                        JSONObject lstrmdt;

                        try {
                            lstrmdt = new JSONObject(loginS);
                            Log.d("jsnresponse....", "---" + loginS);
                            viewDialog.showDialog();
                            JSONSenderVolleylogin(lstrmdt, url);

                        } catch (JSONException ignored) {
                        }
                    }
                }else {
                    //dialogbox("password and confirm password is not matched");
                    popup("password and confirm password is not matched");

                }
            }
        });
        //this is the keybord hiding code
        rootlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager im=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(confirmpwd.getWindowToken(),0);
            }
        });

    }

    //this the jsonvolley post
    public void JSONSenderVolleylogin(JSONObject lstrmdt, String url) {
        Log.d("555555", "login" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONSenderVolleylogin", "---" + response);
                        viewDialog.hideDialog();

                        Toast.makeText(SetNewPassword.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                        try {
                            if (response.has("user")) {
                                JSONObject user = response.getJSONObject("user");
                                String _id = user.getString("_id");
                                String token = user.getString("token");
                                String profilePic = user.getString("profilePic");

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SetNewPassword.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("token", token);
                                editor.putString("id", _id);
                                editor.putString("userpic", profilePic);
                                editor.apply();
                                killerplyer();
                                Intent i = new Intent(SetNewPassword.this, TopNavigationview.class);
                                startActivity(i);
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
                                String id = null;
                                if (obj.has("id")) {
                                    id = obj.getString("id");
                                }

                                if (obj.has("errors")) {
                                    viewDialog.hideDialog();
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    //Toast.makeText(SetNewPassword.this, message, Toast.LENGTH_SHORT).show();
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

    //this is the response popup
    public void popup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = SetNewPassword.this;
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

}
