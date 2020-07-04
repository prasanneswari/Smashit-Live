package com.vasmash.va_smash.HomeScreen.homefragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.vasmash.va_smash.BottmNavigation.TopNavigationview;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_TradingTabs;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Trading;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;
import com.vasmash.va_smash.createcontent.CameraActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.ProfileScreen.ProfileActivity.createvideosclick;
import static com.vasmash.va_smash.VASmashAPIS.APIs.hashtags_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.originalsound_url;

public class OriginalSoundDisplay extends AppCompatActivity {

    TextView originalosund,viewstxt;
    EditText usesound;
    RecyclerView soundlist;
    LinearLayout soundlay;
    ArrayList<Model_Trading> searchindividualmodel;
    private Adapter_TradingTabs mAdapter;
    Dialog dialog;
    private RequestQueue mQueue;
    String token;
    String likeusername="null",likeprofilePic="null",likeuserid="null";
    //this is the loading animation
    LottieAnimationView animationView;
    public static ArrayList<String> fileL;
    String soundid, soundurl,dynamiclink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original_sound_display);
        originalosund=findViewById(R.id.songname);
        soundlist=findViewById(R.id.songlist);
        usesound=findViewById(R.id.songvideo);
        viewstxt=findViewById(R.id.views);
        animationView = findViewById(R.id.animation_view_1);
        soundlay=findViewById(R.id.soundlay);

        mQueue = Volley.newRequestQueue(this);
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(this);
        token = phoneauthshard.getString("token", "null");

        //here this gives the data from home page on hashtag cliking.
        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            String songname = intent.getStringExtra("name");
            soundid = intent.getStringExtra("soundid");
            dynamiclink = intent.getStringExtra("dynamiclink");

            Log.d("dynamiclinkss",":::"+dynamiclink);
            jsonhashtags(soundid);
        }

        usesound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                intent.putExtra("soundurl",soundurl);
                startActivity(intent);
            }
        });

        soundlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createreflink(soundid);
            }
        });
    }
    //this is the json hashtags response
    private void jsonhashtags(String hashtagname) {
        Log.d("jsonParseuser", "sound" + originalsound_url+hashtagname);
        loader();
        searchindividualmodel = new ArrayList<>();
        fileL=new ArrayList<>();
        fileL.clear();

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, originalsound_url+hashtagname, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("sound data", response.toString());
                        animationView.cancelAnimation();
                        animationView.setVisibility(View.GONE);

                        if (response.length() != 0) {
                            // Iterate the inner "data" array
                            for (int j = 0; j < response.length(); j++) {
                                Model_Trading searchhm = new Model_Trading();

                                JSONObject sounds = null;
                                try {
                                    sounds = response.getJSONObject(j);
                                    soundurl=sounds.getString("url");
                                    String views=sounds.getString("views");
                                    viewstxt.setText(views+" "+"Views");
                                    Log.d("views", views);
                                    searchhm.setSoundurl(soundurl);
                                    searchhm.setSoundpostid(soundurl);

                                    if (sounds.has("userId")) {
                                        JSONObject userarray = sounds.getJSONObject("userId");
                                        String  likeusername = userarray.getString("name");
                                        String likeprofilePic = userarray.getString("profilePic");
                                        String likeuserid = userarray.getString("_id");
                                        searchhm.setUsername(likeusername);
                                        searchhm.setProfilepic(likeprofilePic);
                                        searchhm.setUserid(likeuserid);
                                        if (userarray.has("username")) {
                                            String soundname = userarray.getString("username");
                                            searchhm.setSoundname(soundname);
                                        }
                                    }

                                    JSONArray postarray=sounds.getJSONArray("posts");
                                    for (int k=0;k<postarray.length();k++) {

                                        JSONObject lkks = postarray.getJSONObject(j);
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
                                        } else {
                                            searchhm.setCount("");
                                        }
                                        if (lkks.has("userLikes")) {
                                            searchhm.setLikescondition(lkks.getString("userLikes"));
                                        } else {
                                            searchhm.setLikescondition("");
                                        }
                                        if (lkks.has("type")) {
                                            searchhm.setType(lkks.getString("type"));
                                        } else {
                                            searchhm.setType("");
                                        }
                                        if (lkks.has("comments")) {
                                            searchhm.setComment(lkks.getString("comments"));
                                        } else {
                                            searchhm.setComment("");
                                        }
                                        if (lkks.has("shareCount")) {
                                            String shareCount = lkks.getString("shareCount");
                                            searchhm.setSharecount(shareCount);
                                        } else {
                                            searchhm.setComment("");
                                        }

                                        if (lkks.has("thumb")) {
                                            searchhm.setGifimg(lkks.getString("thumb"));
                                        } else {
                                            searchhm.setGifimg(" ");
                                        }

                                        if (lkks.has("description")) {
                                            String description = lkks.getString("description");
                                            searchhm.setDescription(description);
                                        } else {
                                            searchhm.setDescription("");
                                        }

                                        if (lkks.has("audioId")){
                                            String soundid = lkks.getString("audioId");
                                            searchhm.setSoundid(soundid);
                                        }else {
                                            searchhm.setSoundid("");
                                        }
                                        if (lkks.has("userId")) {
                                            String sounduserId = lkks.getString("userId");
                                            searchhm.setSounduserid(sounduserId);
                                        }else {
                                            searchhm.setSounduserid("");
                                        }

                                        }
                                    soundlist.setHasFixedSize(true);
                                    // set a GridLayoutManager with 3 number of columns
                                    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                                    gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
                                    soundlist.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

                                    searchindividualmodel.add(searchhm);
                                    mAdapter = new Adapter_TradingTabs(OriginalSoundDisplay.this, searchindividualmodel,fileL);
                                    soundlist.setAdapter(mAdapter);

                                }catch (Exception e){
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
                                            animationView.cancelAnimation();
                                            animationView.setVisibility(View.GONE);
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            //  Toast.makeText(OtherprofileActivity.this, message, Toast.LENGTH_SHORT).show();
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
                                            animationView.cancelAnimation();
                                            animationView.setVisibility(View.GONE);
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
                animationView.cancelAnimation();
                animationView.setVisibility(View.GONE);
            }
        });
    }


    public void createreflink(String  soundis){
        Log.d("entringlink",":::");

        Task<ShortDynamicLink> dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://sh.vasmash.com"+"?"+soundis+"-"+"originalsound"))
                .setDomainUriPrefix("https://sh.vasmash.com")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .setIosParameters(new DynamicLink.IosParameters.Builder("com.example.smash").build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("Example of a Dynamic Link")
                                .setDescription("This link works whether the app is installed or not!")
                                .build())
                .buildShortDynamicLink().addOnCompleteListener((Activity) this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Log.d("shortlink",":::"+shortLink);
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Intent i = new Intent(android.content.Intent.ACTION_SEND);
                            i.setType("text/plain");
                            String sub="\n Hi, have a look. I found this crazy stuff in VA-Smash!!\n";
                            String shareMessage= sub+" " +"\n"+ shortLink +"\n"+" "+ "\n I am enjoying and earning. Install to join me.\n\n";
                            i.putExtra(android.content.Intent.EXTRA_TEXT, shareMessage.toString());
                            startActivity(Intent.createChooser(i, "Share Via"));

                        } else {
                            Log.d("error",":::"+task);
                            // Error
                            // ...
                        }
                    }
                });

    }


    public void finishActivity(View v){
        if (dynamiclink.equals("null")) {
            finish();
        } else {
            Intent intent = new Intent(OriginalSoundDisplay.this, TopNavigationview.class);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }
    //this is the loader class
    public void loader(){
        //lotte loader
        animationView.setAnimation("data.json");
        animationView.loop(true);
        animationView.playAnimation();
        animationView.setVisibility(View.VISIBLE);
    }


}
