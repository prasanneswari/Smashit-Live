package com.vasmash.va_smash.HomeScreen.homefragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.vasmash.va_smash.HomeScreen.homeadapters.FragmentInner;
import com.vasmash.va_smash.ProfileScreen.OtherprofileActivity;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_TradingTabs;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Trading;
import com.vasmash.va_smash.SearchClass.SearchVerticalData;
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

public class HashTagsDisplay extends AppCompatActivity {

    TextView hashtags,tagviews;
    ImageView hashvideo;
    RecyclerView hashtagslist;
    LinearLayout hashaglay;
    ArrayList<Model_Trading> searchindividualmodel;
    private Adapter_TradingTabs mAdapter;
    Dialog dialog;
    private RequestQueue mQueue;
    String token;
    String likeusername="null",likeprofilePic="null",likeuserid="null";
    //this is the loading animation
    LottieAnimationView animationView;
    public static ArrayList<String> fileL;

    int pastVisibleItems11, visibleItemCount11, totalItemCount11;
    Boolean loading11 = true;
    int currentPage=-1;
    int page_no;
    ProgressBar p_bar;
    private boolean loading;
    String hashtagname,dynamiclink;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hash_tags_display);
        hashtags=findViewById(R.id.hashtags);
        hashtagslist=findViewById(R.id.hashtagslist);
        hashvideo=findViewById(R.id.hashvideo);
        tagviews=findViewById(R.id.views);
        animationView = findViewById(R.id.animation_view_1);
        p_bar=findViewById(R.id.p_bar);
        hashaglay=findViewById(R.id.hashtaglay);

        mQueue = Volley.newRequestQueue(this);
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(this);
        token = phoneauthshard.getString("token", "null");

        //here this gives the data from home page on hashtag cliking.
        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            hashtagname = intent.getStringExtra("hashtag");
            dynamiclink = intent.getStringExtra("dynamiclink");

            Log.d("hashnameitent", ":::" + hashtagname);
            hashtags.setText("#"+""+hashtagname);
            hashtagspagination(hashtagname);
        }

        hashvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });
        hashaglay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createreflink(hashtagname);

            }
        });


    }

    public void hashtagspagination(String hashtagname){
        searchindividualmodel = new ArrayList<>();
        fileL=new ArrayList<>();

        hashtagslist.setHasFixedSize(true);
        // set a GridLayoutManager with 3 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        hashtagslist.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        mAdapter = new Adapter_TradingTabs(HashTagsDisplay.this, searchindividualmodel,fileL);
        hashtagslist.setAdapter(mAdapter);

        loading11 = false;
        loading=false;

        hashtagslist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //here we find the current item number
                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
                final int height = recyclerView.getHeight();
                page_no=scrollOffset / height;

                if(page_no!=currentPage ){
                    currentPage=page_no;
                }

                if (dy > 0)
                {
                    visibleItemCount11 = gridLayoutManager.getChildCount();
                    totalItemCount11 = gridLayoutManager.getItemCount();
                    pastVisibleItems11 = gridLayoutManager.findFirstVisibleItemPosition();
                    Log.d("visibleItemCount","::::"+visibleItemCount11+":::"+totalItemCount11+"::"+pastVisibleItems11+":::"+loading11);

                    if (!loading11) {
                        // if ((visibleItemCount11 + pastVisibleItems11) >= totalItemCount11 && visibleItemCount11 >= 0 && totalItemCount11 >= tags.size()) {
                        Log.d("checkpage","::"+gridLayoutManager.findLastCompletelyVisibleItemPosition()+":::"+searchindividualmodel.size());
                        if(gridLayoutManager.findLastCompletelyVisibleItemPosition() == searchindividualmodel.size()-1){
                            loading11 = true;
                            loading=true;
                            jsonhashtags(hashtags_url+hashtagname+"&limit=10&skip="+searchindividualmodel.size());

                        }
                    }
                }
            }
        });
        jsonhashtags(hashtags_url+hashtagname+"&limit=10&skip=0");

    }

    //this is the json hashtags response
    private void jsonhashtags(String url) {
        Log.d("jsonParseuser", "hashtags" + url);
        if (loading){
            p_bar.setVisibility(View.VISIBLE);
        }else {
            loader();
        }

        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("follow data", response.toString());
                        if (loading){
                            p_bar.setVisibility(View.GONE);
                        }else {
                            animationView.cancelAnimation();
                            animationView.setVisibility(View.GONE);
                        }

                        createvideosclick="false";

                        if (response.length() != 0) {
                            // Iterate the inner "data" array
                            for (int j = 0; j < response.length() ; j++ ) {
                                JSONObject lkks = null;
                                try {
                                    lkks = response.getJSONObject(j);
                                    Log.d("response2222","jItem:::"+lkks);
                                    tagviews.setText(j+1+" "+"Views");
/*
                                    if (response.has("posts")) {
                                        JSONArray likes = response.getJSONArray("posts");
                                        Log.e("drawrep", String.valueOf(likes));
                                        for (int k = 0; k < likes.length(); k++) {
                                            JSONObject lkks = likes.getJSONObject(k);
*/
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

                                            if (lkks.has("name")) {
                                                searchhm.setUsername(lkks.getString("name"));
                                            } else {
                                                searchhm.setUsername("");
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

                                            if (lkks.has("userId")) {
                                                JSONObject userarray = lkks.getJSONObject("userId");
                                                likeusername = userarray.getString("name");
                                                likeprofilePic = userarray.getString("profilePic");
                                                likeuserid = userarray.getString("_id");
                                                searchhm.setUsername(likeusername);
                                                searchhm.setProfilepic(likeprofilePic);
                                                searchhm.setUserid(likeuserid);

                                            }

                                    if (lkks.has("soundId")) {
                                        JSONArray sounds = lkks.getJSONArray("soundId");
                                        Log.d("soundsss","::::"+sounds);
                                        for (int k1 = 0; k1 < sounds.length(); k1++) {
                                            JSONObject soundsobj = sounds.getJSONObject(k1);
                                            if(soundsobj.has("_id")) {
                                                String soundid = soundsobj.getString("_id");
                                                searchhm.setSoundid(soundid);
                                            }
                                            if (soundsobj.has("name")) {
                                                String soundname = soundsobj.getString("name");
                                                searchhm.setSoundname(soundname);
                                            }
                                            if (soundsobj.has("url")) {
                                                String soundurl = soundsobj.getString("url");
                                                searchhm.setSoundurl(soundurl);
                                            }
                                            if (soundsobj.has("postId")) {
                                                String soundpostId = soundsobj.getString("postId");
                                                searchhm.setSoundpostid(soundpostId);
                                            }
                                            if (soundsobj.has("userId")) {
                                                String sounduserId = soundsobj.getString("userId");
                                                searchhm.setSounduserid(sounduserId);
                                            }
                                        }
                                    }else {
                                        searchhm.setSoundname("");
                                        searchhm.setSoundurl("");
                                        searchhm.setSoundpostid("");
                                        searchhm.setSounduserid("");
                                    }
                                    searchindividualmodel.add(searchhm);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            mAdapter.notifyDataSetChanged();
                            loading11 = false;
                        }else {
                            loading11 = true;
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
                                            if (loading){
                                                p_bar.setVisibility(View.GONE);
                                            }else {
                                                animationView.cancelAnimation();
                                                animationView.setVisibility(View.GONE);
                                            }
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
                                            if (loading){
                                                p_bar.setVisibility(View.GONE);
                                            }else {
                                                animationView.cancelAnimation();
                                                animationView.setVisibility(View.GONE);
                                            }
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
                if (loading){
                    p_bar.setVisibility(View.GONE);
                }else {
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.GONE);
                }
            }
        });
    }



    public void createreflink(String  hashtagname){
        Log.d("entringlink",":::");

        Task<ShortDynamicLink> dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://sh.vasmash.com"+"?"+hashtagname+"-"+"hashtags"))
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
            Intent intent = new Intent(HashTagsDisplay.this, TopNavigationview.class);
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
