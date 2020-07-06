package com.vasmash.va_smash.LaunchingScreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vasmash.va_smash.BottmNavigation.TopNavigationview;
import com.vasmash.va_smash.HomeScreen.homefragment.HashTagsDisplay;
import com.vasmash.va_smash.HomeScreen.homefragment.OriginalSoundDisplay;
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Trading;
import com.vasmash.va_smash.SearchClass.SearchVerticalData;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;
import com.vasmash.va_smash.login.fragments.LoginFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vasmash.va_smash.VASmashAPIS.APIs.dynamiclink_url;
//this is the launching class
public class Launching extends AppCompatActivity {


    ImageView imglogo;
    private String accesstoken;
    int version;
    AlertDialog.Builder builder;
    String token;
    public static boolean currentposlaunch;

    private static int SPLASH_TIME_OUT = 1500;

    private RequestQueue mQueue;
    ViewDialog viewDialog;

    ArrayList<Model_Trading> searchindividualmodel;
    ArrayList<Tags> des;
    String dynamiccondition="null";
    public static ArrayList<String> likeL;
    public static ArrayList<String> commentL;
    public static ArrayList<String> userlikesL;
    public static ArrayList<String> sharecountL;
    public static ArrayList<String> fileL;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               splashMethod();
            } else    {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO}, REQUEST_ID_MULTIPLE_PERMISSIONS);
                }else{
                    splashMethod();
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);

        if (Build.VERSION.SDK_INT >= 23) {
                            requestPermissions(new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.CAMERA,
                                    Manifest.permission.RECORD_AUDIO}, REQUEST_ID_MULTIPLE_PERMISSIONS);


        }else{
            splashMethod();
        }
    }

    private void splashMethod(){
        viewDialog = new ViewDialog(Launching.this);


        mQueue = Volley.newRequestQueue(Launching.this);

        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(Launching.this);
        token = phoneauthshard.getString("token", "null");

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(Launching.this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            Log.d("deeplink",":::"+deepLink);
                            String reflink=deepLink.toString();
                            try {
                                reflink=reflink.substring(reflink.lastIndexOf("?")+1);
                                Log.d("reflink",":::"+reflink);
                                String postiddynamic=reflink.substring(0,reflink.indexOf("-"));
                                String linkcondition=reflink.substring(reflink.indexOf("-")+1);
                                Log.d("linkcondition",":::"+linkcondition);

                                if (linkcondition.equals("true")) {
                                    if (token.equals("null")){
                                        Intent i = new Intent(Launching.this, TopNavigationview.class);
                                        startActivity(i);
                                        finish();
                                    }else {
                                        Log.d("linkcondition", ":::" + linkcondition);
                                        jsongetvastore(dynamiclink_url+postiddynamic);                                    }
                                }else if (linkcondition.equals("hashtags")){
                                    if (token.equals("null")){
                                        Intent i = new Intent(Launching.this, TopNavigationview.class);
                                        startActivity(i);
                                        finish();
                                    }else {
                                        dynamiccondition="true";
                                        Intent i = new Intent(Launching.this, HashTagsDisplay.class);
                                        i.putExtra("hashtag", postiddynamic);
                                        i.putExtra("dynamiclink",dynamiccondition);
                                        startActivity(i);
                                        finish();
                                    }
                                }else if (linkcondition.equals("originalsound")){
                                    if (token.equals("null")){
                                        Intent i = new Intent(Launching.this, TopNavigationview.class);
                                        startActivity(i);
                                        finish();
                                    }else {
                                        dynamiccondition="true";
                                        Intent i = new Intent(Launching.this, OriginalSoundDisplay.class);
                                        i.putExtra("soundid", postiddynamic);
                                        i.putExtra("name", "");
                                        i.putExtra("dynamiclink",dynamiccondition);
                                        startActivity(i);
                                        finish();
                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }else {
                            StartAnimations();
                        }
                    }
                })
                .addOnFailureListener(Launching.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("", "getDynamicLink:onFailure", e);
                    }
                });
    }

    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        imglogo = (ImageView) findViewById(R.id.btnlogo);
        imglogo.clearAnimation();
        imglogo.startAnimation(anim);
        imglogo.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                currentposlaunch=true;
                // This method will be executed once the timer is over
                // Start your app main activity
                if (token.equals("null")) {
                    Intent i = new Intent(Launching.this, TopNavigationview.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(Launching.this, TopNavigationview.class);
                    startActivity(i);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    public void sleep(){
        Handler handler;
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                StartAnimations();
                finish();
            }
        },2000);
    }


    private void jsongetvastore(String profiledetails_url) {
        viewDialog.showDialog();
        Log.d("jsonParseuser", "profile data" + profiledetails_url);
        searchindividualmodel = new ArrayList<>();
        des=new ArrayList<>();

        likeL=new ArrayList<>();
        commentL=new ArrayList<>();
        userlikesL=new ArrayList<>();
        sharecountL=new ArrayList<>();
        fileL=new ArrayList<>();
        commentL.clear();
        userlikesL.clear();
        sharecountL.clear();
        fileL.clear();
        likeL.clear();

        des.clear();
        searchindividualmodel.clear();
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, profiledetails_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("dynamic res", response.toString());
                        viewDialog.hideDialog();

                        if (response.length() != 0) {
                            // Iterate the inner "data" array
                            for (int j = 0; j < response.length() ; j++ ) {
                                Log.d("lengtharayyy", ":::" + j);
                                JSONObject lkks = null;
                                try {
                                    lkks = response.getJSONObject(j);
                                    Model_Trading searchhm = new Model_Trading();
                                    if (lkks.has("_id")) {
                                        searchhm.setId(lkks.getString("_id"));
                                    } else {
                                        searchhm.setId("");
                                    }
                                    if (lkks.has("file")) {
                                        searchhm.setImage(lkks.getString("file"));
                                        fileL.add(lkks.getString("file"));
                                    } else {
                                        searchhm.setImage("");
                                        fileL.add("");
                                    }
                                    if (lkks.has("likes")) {
                                        searchhm.setCount(lkks.getString("likes"));
                                        likeL.add(lkks.getString("likes"));
                                    } else {
                                        searchhm.setCount("");
                                        likeL.add("");
                                    }
                                    if (lkks.has("userLikes")) {
                                        searchhm.setLikescondition(lkks.getString("userLikes"));
                                        userlikesL.add(lkks.getString("userLikes"));
                                    } else {
                                        searchhm.setLikescondition("");
                                        userlikesL.add("");
                                    }
                                    if (lkks.has("type")) {
                                        searchhm.setType(lkks.getString("type"));
                                    } else {
                                        searchhm.setType("");

                                    }
                                    if (lkks.has("comments")) {
                                        searchhm.setComment(lkks.getString("comments"));
                                        commentL.add(lkks.getString("comments"));
                                    } else {
                                        searchhm.setComment("");
                                        commentL.add("");
                                    }
                                    if (lkks.has("shareCount")) {
                                        String shareCount = lkks.getString("shareCount");
                                        searchhm.setSharecount(shareCount);
                                        sharecountL.add(shareCount);
                                    } else {
                                        searchhm.setComment("");
                                        sharecountL.add("");
                                    }

                                    if (lkks.has("name")) {
                                        searchhm.setUsername(lkks.getString("name"));
                                    } else {
                                        searchhm.setUsername("");
                                    }
                                    if (lkks.has("description")) {
                                        String description = lkks.getString("description");
                                        searchhm.setDescription(description);
                                    } else {
                                        searchhm.setDescription("");
                                    }


                                    if (lkks.has("userId")) {
                                        JSONObject nameobj=lkks.getJSONObject("userId");
                                        String userid = nameobj.getString("_id");
                                        if(nameobj.has("name")) {
                                            String username = nameobj.getString("name");
                                            searchhm.setUsername(username);
                                        }
                                        String homeprofilePic = nameobj.getString("profilePic");

                                        Log.d("profilepicuser",":::"+homeprofilePic);
                                        searchhm.setProfilepic(homeprofilePic);
                                        searchhm.setUserid(userid);
                                        // sm.sendData1(homeprofilePic);

                                    }else {
                                        String username = "name";
                                        searchhm.setUsername(username);
                                        searchhm.setProfilepic("null");
                                        searchhm.setUserid("");
                                        //sm.sendData1("null");
                                    }

                                    if (lkks.has("tags")) {
                                        JSONArray tagsarray = lkks.getJSONArray("tags");
                                        Tags ts = new Tags();

                                        if (tagsarray.length() != 0) {
                                            for (int k1 = 0; k1 < tagsarray.length(); k1++) {
                                                JSONObject tagarray = tagsarray.getJSONObject(k1);

                                                String tagid = tagarray.getString("_id");
                                                if (tagarray.has("tag")) {
                                                    String tag = tagarray.getString("tag");
                                                    Log.d("tag:::::", "" + tag);
                                                    ts.setId(tagarray.getString("_id"));
                                                    ts.setName(tagarray.getString("tag"));
                                                    des.add(ts);
                                                } else {
                                                    String tag = " ";
                                                    ts.setId("");
                                                    ts.setName("no tags");

                                                    des.add(ts);
                                                }
                                            }
                                        } else {
                                            String tag = "..";
                                            ts.setId("");
                                            ts.setName("no tags");
                                            des.add(ts);
                                        }
                                    }
/*
                                            if (!userimg.equals("null")) {
                                                searchhm.setProfilepic(userimg);
                                            }else {
                                                searchhm.setProfilepic("null");
                                            }
*/
                                    searchindividualmodel.add(searchhm);
                                    SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(Launching.this);
                                    SharedPreferences.Editor editor = sharedPrefs.edit();
                                    Gson gson = new Gson();

                                    String json = gson.toJson(searchindividualmodel);
                                    editor.putString("arraydata", json);
                                    //  editor.putString("dess", String.valueOf(des));
                                    editor.putString("likecount", String.valueOf(likeL));
                                    editor.putString("comment", String.valueOf(commentL));
                                    editor.putString("userlike", String.valueOf(userlikesL));
                                    editor.putString("share", String.valueOf(sharecountL));
                                    editor.apply();

                                    dynamiccondition="true";
                                    Intent intent = new Intent(Launching.this, SearchVerticalData.class);
                                    intent.putExtra("clikpos",j);
                                    intent.putExtra("dynamiclink",dynamiccondition);
                                    intent.putStringArrayListExtra("fileL",(ArrayList<String>) fileL);
                                    startActivity(intent);
                                    //commentimg
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d("Error.Response1111111", error.toString());
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
                                            // Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case 404:
                                    try {
                                        String bodyerror = new String(error.networkResponse.data,"UTF-8");
                                        Log.d("bodyerror", "---" + bodyerror);
                                        JSONObject obj = new JSONObject(bodyerror);
                                        if (obj.has("errors")) {
                                            viewDialog.hideDialog();
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            //Toast.makeText(GACalculator.this, message, Toast.LENGTH_SHORT).show();

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

                headers.put("Content-Type","application/json");
                headers.put("Authorization",token);
                // Log.d("headresspro",":::::"+token);


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


}
