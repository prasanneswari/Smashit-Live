package com.vasmash.va_smash.ProfileScreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.Adapters.Adapter_TradingTabs;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Trading;
import com.vasmash.va_smash.SettingClass.AccountSettings;
import com.vasmash.va_smash.SettingClass.Settings_Fragment;
import com.vasmash.va_smash.VASmashAPIS.APIs;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;
import com.vasmash.va_smash.WalletScreen.WalletAmount;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.countDownTimer;
import static com.vasmash.va_smash.HomeScreen.homefragment.HomeFragment.privious_player;
import static com.vasmash.va_smash.VASmashAPIS.APIs.getprofile_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.usercreated_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.usersliked_url;

//this is the profile activity
public class ProfileActivity extends AppCompatActivity {
    ArrayList<Model_Trading> searchindividualmodel;
    public static ArrayList<String> fileL;

    private RecyclerView recyclerView,likedlist;
    public static Adapter_TradingTabs mAdapterlikes,createdadapter;
    ImageView profile_image,displayimg;
    TextView followingclick,followersclick,coinname,likes_count,craeatedcount,profile_username,following_count,follower_count,profilewallet,created,liked;
    String token,backprofile="null";
    LinearLayout walletbtn,profilelay;
    private Toolbar toolbar;
    View crateimg,likedimg;

    private RequestQueue mQueue;

    String countryCode,username="null",firstName="null",lastName="null",userimg="null",mobile,email,contryid,walletamount,contryname,city,dateofbirth,usercreated,totalLikes;
    int gender;
    //this is the loader animationview.
     ViewDialog viewDialog;
    //LottieAnimationView animationView;

    String likeusername="null",likeprofilePic="null",likeuserid="null",follower,follewing;
    static public String createvideosclick="null";

    int pastVisibleItems11, visibleItemCount11, totalItemCount11;
    Boolean loading11 = true;
    int currentPage=-1;
    int page_no;
    private boolean loading;
    ProgressBar p_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        profile_image=(ImageView)findViewById(R.id.profile_image);
        likes_count=(TextView) findViewById(R.id.likes_count);
        craeatedcount=findViewById(R.id.cratedcount);
        following_count=(TextView) findViewById(R.id.following_count);
        follower_count=(TextView) findViewById(R.id.follower_count);
        profile_username=(TextView) findViewById(R.id.profile_username);
        walletbtn=findViewById(R.id.walletbtn);
        toolbar = findViewById(R.id.toolbar);
        profilewallet=findViewById(R.id.profile_Wallet);
        created=findViewById(R.id.created);
        crateimg=findViewById(R.id.createimg);
        likedimg=findViewById(R.id.likedimg);
        liked=findViewById(R.id.liked);
        coinname=findViewById(R.id.coinname);
        followersclick=findViewById(R.id.followersclick);
        followingclick=findViewById(R.id.followingclick);
        profilelay=findViewById(R.id.profilelay);
        displayimg=findViewById(R.id.displayimg);

        viewDialog = new ViewDialog(ProfileActivity.this);
        //animationView = findViewById(R.id.animation_view_1);
        p_bar=findViewById(R.id.p_bar);

        setSupportActionBar(toolbar);
        //   toolbar.setBackgroundResource(R.color.toolback);
        try
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        catch (NullPointerException e){}

        mQueue = Volley.newRequestQueue(this);
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(this);
        token = phoneauthshard.getString("token", "null");

        recyclerView = findViewById(R.id.profile_content_recycleview);
        likedlist=findViewById(R.id.likedlist);

        jsonParsegawallet();
        createdpagination();

        //it checks where network available or not
        if (isNetworkAvailable()) {
            //Toast.makeText(Home.this, "Network connection is available", Toast.LENGTH_SHORT).show();
        } else if (!isNetworkAvailable()) {
            //viewDialog.hideDialog();
            // Toast.makeText(Home.this, "Network connection is not available", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Network connection is not available", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        // here it gives  wallet balance onclicking to wallet amount class
        walletbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, WalletAmount.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        //here it shows user created videios
        created.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileL.clear();
                searchindividualmodel.clear();

                likedlist.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                crateimg.setVisibility(View.VISIBLE);
                likedimg.setVisibility(View.GONE);
                createdpagination();
            }
        });
        //here it shows liked created videios
        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileL.clear();
                searchindividualmodel.clear();

                likedlist.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                crateimg.setVisibility(View.GONE);
                likedimg.setVisibility(View.VISIBLE);
                likedrecyclar();
            }
        });
        followingclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, Userfollow_following.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("username",username);
                intent.putExtra("followingcount",follewing);
                intent.putExtra("followcount",follower);
                intent.putExtra("key","following");
                startActivityForResult(intent, 1);
            }
        });
        followersclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, Userfollow_following.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("username",username);
                intent.putExtra("followcount",follower);
                intent.putExtra("followingcount",follewing);
                intent.putExtra("key","follower");
                startActivityForResult(intent, 1);
            }
        });
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, Profile_Fragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("username",username);
                intent.putExtra("firstName",firstName);
                intent.putExtra("lastName",lastName);
                intent.putExtra("userimg",userimg);
                intent.putExtra("mobile",mobile);
                intent.putExtra("countryCode",countryCode);
                intent.putExtra("email",email);
                intent.putExtra("gender",gender);
                intent.putExtra("contryid",contryid);
                intent.putExtra("dob",dateofbirth);
                intent.putExtra("contryname",contryname);
                intent.putExtra("city",city);
                //startActivity(intent);
                startActivityForResult(intent, 1);

            }
        });
    }
    public void createdpagination(){
        searchindividualmodel = new ArrayList<>();
        fileL=new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        // set a GridLayoutManager with 3 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        // recyclerView.setLayoutManager(new GridLayoutManager(ProfileActivity.this, 3));

        createdadapter = new Adapter_TradingTabs(ProfileActivity.this, searchindividualmodel,fileL);
        recyclerView.setAdapter(createdadapter);

        loading11 = false;
        loading=false;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                   // Log.d("visibleItemCount","::::"+visibleItemCount11+":::"+totalItemCount11+"::"+pastVisibleItems11+":::"+loading11);

                    if (!loading11) {
                        // if ((visibleItemCount11 + pastVisibleItems11) >= totalItemCount11 && visibleItemCount11 >= 0 && totalItemCount11 >= tags.size()) {
                        //Log.d("checkpage","::"+gridLayoutManager.findLastCompletelyVisibleItemPosition()+":::"+searchindividualmodel.size());
                        if(gridLayoutManager.findLastCompletelyVisibleItemPosition() == searchindividualmodel.size()-1){
                            loading11 = true;
                            loading=true;
                            jsongetvastore(usercreated_url+"?skip="+searchindividualmodel.size());
                        }
                    }
                }
            }
        });
        jsongetvastore(usercreated_url+"?skip=0");
    }

    public void likedrecyclar(){
        searchindividualmodel = new ArrayList<>();
        fileL=new ArrayList<>();

        //commentimg
        likedlist.setHasFixedSize(true);
        // use a linear layout manager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        likedlist.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView

        // likedlist.setLayoutManager(new GridLayoutManager(ProfileActivity.this, 3));

        mAdapterlikes = new Adapter_TradingTabs(ProfileActivity.this, searchindividualmodel,fileL);
        likedlist.setAdapter(mAdapterlikes);
        loading11 = false;
        loading=false;

        likedlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                   // Log.d("visibleItemCount","::::"+visibleItemCount11+":::"+totalItemCount11+"::"+pastVisibleItems11+":::"+loading11);

                    if (!loading11) {
                        // if ((visibleItemCount11 + pastVisibleItems11) >= totalItemCount11 && visibleItemCount11 >= 0 && totalItemCount11 >= tags.size()) {
                       // Log.d("checkpage","::"+gridLayoutManager.findLastCompletelyVisibleItemPosition()+":::"+searchindividualmodel.size());
                        if(gridLayoutManager.findLastCompletelyVisibleItemPosition() == searchindividualmodel.size()-1){
                            loading11 = true;
                            loading=true;
                            jsonliked(usersliked_url+"?skip="+searchindividualmodel.size());

                        }
                    }
                }
            }
        });
        jsonliked(usersliked_url+"?skip=0");

    }


    private void jsongetvastore(String usercreated_url) {
        if (loading){
            p_bar.setVisibility(View.VISIBLE);
        }else {
            //loader();
            viewDialog.showDialog();
        }

        //Log.d("jsonParseuser", "profile data" + usercreated_url);
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, usercreated_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        //Log.d("Responsestoredata0000", response.toString());
                        if (loading){
                            p_bar.setVisibility(View.GONE);
                        }else {
/*
                            animationView.cancelAnimation();
                            animationView.setVisibility(View.GONE);
*/
                            viewDialog.hideDialog();

                        }

                        if (response.length() != 0) {
                            // Iterate the inner "data" array
                            try {
                                if (response.has("userCreatedPostsList")) {

                                    JSONArray object = response.getJSONArray("userCreatedPostsList");
                                    if (object.length() != 0){
                                        for (int j = 0; j < object.length(); j++) {
                                           // Log.d("lengtharayyy", ":::" + j);
                                            JSONObject lkks = null;
                                            createvideosclick = "true";
                                            lkks = object.getJSONObject(j);
                                            Model_Trading searchhm = new Model_Trading();

                                            if (lkks.has("_id")) {
                                                searchhm.setId(lkks.getString("_id"));
                                            } else {
                                                searchhm.setId("");
                                            }
                                            if (lkks.has("visibility")) {
                                                searchhm.setVisibility(lkks.getString("visibility"));
                                            } else {
                                                searchhm.setVisibility("");
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
                                               // Log.d("descriptionnnprofii", ":::" + description);
                                            } else {
                                                searchhm.setDescription("");
                                            }
                                            if (lkks.has("userId")) {
                                                JSONObject userobj=lkks.getJSONObject("userId");
                                                String likeusername = userobj.getString("name");
                                                String likeprofilePic = userobj.getString("profilePic");
                                                String likeuserid = userobj.getString("_id");

                                                searchhm.setUsername(likeusername);
                                                searchhm.setProfilepic(likeprofilePic);
                                                searchhm.setUserid(likeuserid);
                                            } else {
                                                searchhm.setUsername("");
                                                searchhm.setProfilepic("");
                                                searchhm.setUserid("");
                                            }
                                            if (lkks.has("soundId")) {
                                                JSONArray sounds = lkks.getJSONArray("soundId");
                                               // Log.d("soundsss","::::"+sounds);
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
                                            } else {
                                                searchhm.setSoundname("");
                                                searchhm.setSoundurl("");
                                                searchhm.setSoundpostid("");
                                                searchhm.setSounduserid("");
                                            }
                                            //likes_count.setText(lik);
                                            searchindividualmodel.add(searchhm);

                                        }
                                        createdadapter.notifyDataSetChanged();
                                        loading11 = false;
                                    }else {
                                        loading = true;
                                    }
                                }
                            }
                            catch(JSONException e) {
                                e.printStackTrace();
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
                                       // Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
                                            if (loading){
                                                p_bar.setVisibility(View.GONE);
                                            }else {
/*
                                                animationView.cancelAnimation();
                                                animationView.setVisibility(View.GONE);
*/
                                                viewDialog.hideDialog();

                                            }

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
                                       // Log.d("bodyerror", "---" + bodyerror);
                                        JSONObject obj = new JSONObject(bodyerror);
                                        if (obj.has("errors")) {
                                            if (loading){
                                                p_bar.setVisibility(View.GONE);
                                            }else {
/*
                                                animationView.cancelAnimation();
                                                animationView.setVisibility(View.GONE);
*/
                                                viewDialog.hideDialog();

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
                if (loading){
                    p_bar.setVisibility(View.GONE);
                }else {
/*
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.GONE);
*/
                    viewDialog.hideDialog();
                }

            }
        });
    }

    private void jsonliked(String profiledetails_url) {
       // Log.d("jsonParseuser", "profile data" + profiledetails_url);
        if (loading){
            p_bar.setVisibility(View.VISIBLE);
        }else {
            viewDialog.showDialog();

            // loader();
        }
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, profiledetails_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                       // Log.d("Responliked0000", response.toString());
                        if (loading){
                            p_bar.setVisibility(View.GONE);
                        }else {
                            viewDialog.hideDialog();
/*
                            animationView.cancelAnimation();
                            animationView.setVisibility(View.GONE);
*/
                        }

                        createvideosclick="false";
                        if (response.length() != 0) {
                            // Iterate the inner "data" array
                            for (int j = 0; j < response.length() ; j++ ) {
                                JSONObject jItem = null;
                                try {
                                    jItem = response.getJSONObject(j);
                                    //Log.d("response2222","jItem:::"+jItem);
                                    if (jItem.has("_id")) {
                                        JSONArray nameobj=jItem.getJSONArray("_id");
                                        for (int k1 = 0; k1 < nameobj.length(); k1++) {
                                            JSONObject userarray = nameobj.getJSONObject(k1);
                                            likeusername = userarray.getString("name");
                                            likeprofilePic = userarray.getString("profilePic");
                                            likeuserid = userarray.getString("_id");
                                        }
                                    }
                                    if (jItem.has("posts")) {
                                        JSONArray arrayres = jItem.getJSONArray("posts");
                                        for (int k = 0; k < arrayres.length() ; k++ ) {
                                            JSONObject likeobj = arrayres.getJSONObject(k);
                                            Model_Trading searchhm = new Model_Trading();
                                            if (likeobj.has("_id")) {
                                                searchhm.setId(likeobj.getString("_id"));
                                            } else {
                                                searchhm.setId("");
                                            }
                                            if (likeobj.has("file")) {
                                                searchhm.setImage(likeobj.getString("file"));
                                                fileL.add(likeobj.getString("file"));
                                            } else {
                                                searchhm.setImage("");
                                                fileL.add("");
                                            }
                                            if (likeobj.has("likes")) {
                                                searchhm.setCount(likeobj.getString("likes"));
                                            } else {
                                                searchhm.setCount("");
                                            }
                                            if (likeobj.has("userLikes")) {
                                                searchhm.setLikescondition(likeobj.getString("userLikes"));
                                            } else {
                                                searchhm.setLikescondition("");
                                            }
                                            if (likeobj.has("type")) {
                                                searchhm.setType(likeobj.getString("type"));
                                            } else {
                                                searchhm.setType("");
                                            }
                                            if (likeobj.has("comments")) {
                                                searchhm.setComment(likeobj.getString("comments"));
                                            } else {
                                                searchhm.setComment("");
                                            }
                                            if (likeobj.has("shareCount")) {
                                                String shareCount = likeobj.getString("shareCount");
                                                searchhm.setSharecount(shareCount);
                                            } else {
                                                searchhm.setComment("");
                                            }

                                            if (likeobj.has("name")) {
                                                searchhm.setUsername(likeobj.getString("name"));
                                            } else {
                                                searchhm.setUsername("");
                                            }

                                            if (likeobj.has("thumb")) {
                                                searchhm.setGifimg(likeobj.getString("thumb"));
                                               // Log.d("thumbs",":::"+likeobj.getString("thumb"));
                                            } else {
                                                searchhm.setGifimg(" ");
                                            }
                                            if (likeobj.has("description")) {
                                                String description = likeobj.getString("description");
                                                searchhm.setDescription(description);
                                            } else {
                                                searchhm.setDescription("");
                                            }

                                            if (likeobj.has("soundId")) {
                                                JSONArray sounds = likeobj.getJSONArray("soundId");
                                                //Log.d("soundsss","::::"+sounds);
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
                                            searchhm.setUsername(likeusername);
                                            searchhm.setProfilepic(likeprofilePic);
                                            searchhm.setUserid(likeuserid);

                                            searchindividualmodel.add(searchhm);
                                        }
                                        mAdapterlikes.notifyDataSetChanged();
                                        loading11 = false;

                                    }

                                } catch(JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }else {
                            // Toast.makeText(ProfileActivity.this, "No Data", Toast.LENGTH_SHORT).show();
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
                                       // Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
                                            if (loading){
                                                p_bar.setVisibility(View.GONE);
                                            }else {
                                                viewDialog.hideDialog();
/*
                                                animationView.cancelAnimation();
                                                animationView.setVisibility(View.GONE);
*/
                                            }

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
                                       // Log.d("bodyerror", "---" + bodyerror);
                                        JSONObject obj = new JSONObject(bodyerror);
                                        if (obj.has("errors")) {
                                            if (loading){
                                                p_bar.setVisibility(View.GONE);
                                            }else {
                                                viewDialog.hideDialog();
/*
                                                animationView.cancelAnimation();
                                                animationView.setVisibility(View.GONE);
*/
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
                if (loading){
                    p_bar.setVisibility(View.GONE);
                }else {
                    viewDialog.hideDialog();
/*
                    animationView.cancelAnimation();
                    animationView.setVisibility(View.GONE);
*/
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.popupmenu);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return onOptionsItemSelected(item);
                    }
                });
        return true;
    }

    //this is related to settings
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.editprofile:
                //  Log.d("countryadapter",":::::"+contryname);
                // Toast.makeText(this, "Action Refresh selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileActivity.this, Profile_Fragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("username",username);
                intent.putExtra("firstName",firstName);
                intent.putExtra("lastName",lastName);
                intent.putExtra("userimg",userimg);
                intent.putExtra("mobile",mobile);
                intent.putExtra("countryCode",countryCode);
                intent.putExtra("email",email);
                intent.putExtra("gender",gender);
                intent.putExtra("contryid",contryid);
                intent.putExtra("dob",dateofbirth);
                intent.putExtra("contryname",contryname);
                intent.putExtra("city",city);

                //startActivity(intent);
                startActivityForResult(intent, 1);

                return true;

            case R.id.menusettings:
                Intent settingintent = new Intent(ProfileActivity.this, AccountSettings.class);
                settingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingintent);
                return true;

            case R.id.menusignout:
                Intent signoutintent = new Intent(ProfileActivity.this, Settings_Fragment.class);
                signoutintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(signoutintent);
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.d("profilfinish", ":::" +resultCode);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                //Log.d("s1111", ":::" + backprofile);
                backprofile = data.getStringExtra("editTextValue");
                if (!backprofile.equals("null")) {
                    Picasso.with(ProfileActivity.this).load(backprofile).placeholder(R.drawable.uploadpiclight).into(profile_image);
                    //Log.d("strdit", ":::" + backprofile);
                }else {
                    //Log.d("elsepic", ":::" + backprofile);
                    Picasso.with(ProfileActivity.this).load(userimg).placeholder(R.drawable.uploadpiclight).into(profile_image);
                }
            }
        }
    }
    public void finishActivity(View v){
        if (countDownTimer != null) {
            countDownTimer.start();
            privious_player.setPlayWhenReady(true);
        }
        if (backprofile.equals("null")) {
            Intent intent = new Intent();
            intent.putExtra("topback", userimg);
            setResult(RESULT_OK, intent);
        } else {
            Intent intent = new Intent();
            intent.putExtra("topback", backprofile);
            setResult(RESULT_OK, intent);
        }
        finish();

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
            finish();
        return;
    }


    private void jsonParsegawallet() {
        //Log.d("jsonParseuser", "getprofile_url " + getprofile_url);
        //  viewDialog.showDialog();

        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, getprofile_url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //  viewDialog.hideDialog();

                        //Log.d("JSONgabal", "---" + response);

                        if (response.length()!=0) {
                            try {

                                //JSONArray array = response.getJSONArray("stackingTransactions");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jItem = response.getJSONObject(i);
/*
                                    String amount = rec.getString("amount");
                                    JSONObject coinId=rec.getJSONObject("coinId");
                                    String symbol=coinId.getString("symbol");
                                    coinname.setText(symbol);
                                    profilewallet.setText(amount);
*/
                                    JSONObject ids = jItem.getJSONObject("_id");
                                    //String userid = ids.getString("userId");
                                    if (ids.has("username")) {
                                        username = ids.getString("username");
                                    }
                                    if (ids.has("firstName")) {
                                        firstName = ids.getString("firstName");
                                    }
                                    if (ids.has("lastName")) {
                                        lastName = ids.getString("lastName");
                                    }

                                    if (ids.has("profilePic")) {
                                        userimg = ids.getString("profilePic");
                                        Picasso.with(ProfileActivity.this).load(userimg).placeholder(R.drawable.uploadpiclight).into(profile_image);
                                    }
                                    follower = ids.getString("followers");
                                    follewing = ids.getString("followings");
                                    walletamount = ids.getString("earnedAmount");
                                    if (ids.has("mobile")) {
                                        mobile = ids.getString("mobile");
                                    }else {
                                        mobile = "";
                                    }
                                    if (ids.has("countryCode")) {
                                        countryCode = ids.getString("countryCode");
                                    }else {
                                        countryCode ="";
                                    }
                                    if (ids.has("email")) {
                                        email = ids.getString("email");
                                    }
                                    if (ids.has("gender")) {
                                        gender = ids.getInt("gender");
                                       // Log.d("gender111",":::"+gender);
                                    }
                                    if (ids.has("city")) {
                                        city = ids.getString("city");
                                    }
                                    if (ids.has("created")) {
                                        usercreated = ids.getString("created");
                                        craeatedcount.setText(usercreated);
                                    }
                                    if (ids.has("totalLikes")) {
                                        totalLikes = ids.getString("totalLikes");
                                        likes_count.setText(totalLikes);
                                    }
                                    if (ids.has("dob")) {
                                        String datesplitdep = ids.getString("dob");
                                        if (!datesplitdep.equals("null")) {
                                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                                            Date localdate = dateFormat.parse(datesplitdep);
                                            DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
                                            dateofbirth = formatter.format(localdate);
                                        }

                                    }
                                    profile_username.setText(username);
                                    following_count.setText(follewing);
                                    follower_count.setText(follower);
                                    // profilewallet.setText(walletamount);

                                    if (ids.has("countryId")) {
                                        JSONObject contry = ids.getJSONObject("countryId");
                                        contryid = contry.getString("_id");
                                        contryname = contry.getString("name");
                                       // Log.e("contryname111", contryname);
                                    }

                                    if (ids.has("walletId")) {
                                        JSONObject walletId = ids.getJSONObject("walletId");
                                        String amount = walletId.getString("amount");
                                        profilewallet.setText(amount);
                                    }
                                    JSONArray coinId=ids.getJSONArray("coinId");
                                    for (int k=0;k<coinId.length();k++){
                                        JSONObject coinobj=coinId.getJSONObject(k);
                                        String symbol=coinobj.getString("symbol");
                                        coinname.setText(symbol);
                                    }
                                }
                            } catch(JSONException e){
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
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
                                        //Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            // Toast.makeText(RequestFragment.this, message, Toast.LENGTH_SHORT).show();
                                            // viewDialog.hideDialog();

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

                System.out.println("headddddd"+headers);

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
                // viewDialog.hideDialog();
            }
        });

    }
    //it checks where network available or not
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager. getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

/*
    public void loader(){
        //lotte loader
        animationView.setAnimation("data.json");
        animationView.loop(true);
        animationView.playAnimation();
        animationView.setVisibility(View.VISIBLE);
    }
*/

}
