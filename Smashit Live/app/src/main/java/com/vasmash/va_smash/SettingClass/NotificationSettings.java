package com.vasmash.va_smash.SettingClass;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
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
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.VASmashAPIS.APIs.getnotification_url;

public class NotificationSettings extends AppCompatActivity {

    Switch receiveS,otherS,likesS,commentS,followS,updateS;
    Button submitbtn;
    private RequestQueue mQueue;
    String token;
    ViewDialog viewDialog;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);
        receiveS=findViewById(R.id.receiveswitch);
        otherS=findViewById(R.id.otherswitch);
        likesS=findViewById(R.id.likeswitch);
        commentS=findViewById(R.id.commentswitch);
        followS=findViewById(R.id.followswitch);
        updateS=findViewById(R.id.updateswitch);
        submitbtn=findViewById(R.id.submitbtn);
        viewDialog = new ViewDialog(this);

        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(NotificationSettings.this);
        token = phoneauthshard.getString("token", "null");
        getnotification();


        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String switch1, switch2,switch3,switch4,switch5,switch6;
                if (receiveS.isChecked())
                    switch1 = "true";
                else
                    switch1 = "false";
                if (otherS.isChecked())
                    switch2 = "true";
                else
                    switch2 = "false";
                if (likesS.isChecked())
                    switch3 = "true";
                else
                    switch3 = "false";

                if (commentS.isChecked())
                    switch4 = "true";
                else
                    switch4 = "false";

                if (followS.isChecked())
                    switch5 = "true";
                else
                    switch5 = "false";

                if (updateS.isChecked())
                    switch6 = "true";
                else
                    switch6 = "false";

                String AddS = "{\"receiveChatNotification\":" + switch1 + ",\"anyOtherRelatedActivity\":" + switch2 + " ,\"likesMyActivity\":" + switch3 + ",\"commentMyActivity\":" + switch4 + ",\"followsMe\":" + switch5 + ",\"occasionalUpdates\":" + switch6 + "}";
                Log.d("jsnresponse notifi", "---" + AddS);
                JSONObject update;
                try {
                    update = new JSONObject(AddS);
                    Log.d("register", "---" + AddS);
                    viewDialog.showDialog();
                    updateregst(update);
                } catch (JSONException ignored) {
                }


            }
        });

    }

    public void getnotification() {
        viewDialog.showDialog();
        mQueue = Volley.newRequestQueue(NotificationSettings.this.getApplicationContext());
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, getnotification_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());
                        viewDialog.hideDialog();

                        try {
                            JSONObject object=response.getJSONObject("prefferences");
                            String receiveChatNotification=object.getString("receiveChatNotification");
                            String anyOtherRelatedActivity=object.getString("anyOtherRelatedActivity");
                            String likesMyActivity=object.getString("likesMyActivity");
                            String commentMyActivity=object.getString("commentMyActivity");
                            String followsMe=object.getString("followsMe");
                            String occasionalUpdates=object.getString("occasionalUpdates");

                            Log.d("receiveChatNotification","::"+receiveChatNotification);

                            if (receiveChatNotification.equals("true")){
                                //receiveS.isChecked();
                                receiveS.setChecked(true);
                            }else {
                                receiveS.setChecked(false);
                            }
                             if (anyOtherRelatedActivity.equals("true")){
                                 otherS.setChecked(true);
                             }else {
                                 otherS.setChecked(false);
                             }
                             if (likesMyActivity.equals("true")){
                                 likesS.setChecked(true);
                             }else{
                                 likesS.setChecked(false);
                             }
                             if (commentMyActivity.equals("true")){
                                 commentS.setChecked(true);
                             }else {
                                 commentS.setChecked(false);
                             }
                             if (followsMe.equals("true")){
                                 followS.setChecked(true);
                             }else {
                                 followS.setChecked(false);
                             }
                             if (occasionalUpdates.equals("true")){
                                 updateS.setChecked(true);
                             }else {
                                 updateS.setChecked(false);
                             }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d("Error.Response", error.toString());
                        String body;
                        //get status code here
                        //Log.d("statusCode", "---" + error.toString());
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 422:
                                    try {
                                        body = new String(error.networkResponse.data, "UTF-8");
                                        Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
                                            viewDialog.hideDialog();
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            //Toast.makeText(AddUsers.this, message, Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }
                    }
                }
        )
        {
            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                //Log.d("", "volleyError" + volleyError.getMessage());
                return super.parseNetworkError(volleyError);
            }

            @Override
            protected Map<String, String> getParams() {
                //Log.d("paamssssssss","" +params);

                return new HashMap<>();
            }

            @Override
            public Map<String, String> getHeaders() {

                //  Authorization: Basic $auth
                HashMap<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization",token);

                return headers;
            }
        };
        // add it to the RequestQueue
        mQueue.add(getRequest);
        getRequest.setRetryPolicy(new RetryPolicy() {
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

    public void updateregst(JSONObject update) {

        final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, getnotification_url, update,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("jsonnotifivation", "---" + response);
                        viewDialog.hideDialog();
                        try {
                            String message=response.getString("message");
                            if (message.equals("Preferences Updated Successfully")){
                                Toast.makeText(NotificationSettings.this, message, Toast.LENGTH_SHORT).show();
                               // getnotification();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body;
                NetworkResponse response = error.networkResponse;
                if (response != null && response.data != null) {
                    switch (response.statusCode) {
                        case 400:
                            try {
                                body = new String(error.networkResponse.data, "UTF-8");
                                Log.d("body", "---" + body);
                                JSONObject obj1 = new JSONObject(body);
                                if (obj1.has("errors")) {
                                    viewDialog.hideDialog();
                                    JSONObject errors = obj1.getJSONObject("errors");
                                    String message = errors.getString("message");
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
                                String body1 = new String(error.networkResponse.data, "UTF-8");
                                Log.d("body", "---" + body1);
                                JSONObject obj = new JSONObject(body1);
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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json");
                headers.put("Authorization",token);
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
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        mQueue.add(req);
    }

    public void popup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = NotificationSettings.this;
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


    public void finishActivity(View v){
        finish();
    }

}
