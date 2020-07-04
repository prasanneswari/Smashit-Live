package com.vasmash.va_smash.ProfileScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.vasmash.va_smash.ProfileScreen.Adapter.Adapter_follow;
import com.vasmash.va_smash.ProfileScreen.Adapter.Adapter_following;
import com.vasmash.va_smash.ProfileScreen.Model_Class.Model_userfollow_unfollow;
import com.vasmash.va_smash.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.VASmashAPIS.APIs.otherfollower_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.otherollowing_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.userfollower_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.userfollowing_url;

public class Otherprofile_follow_following extends AppCompatActivity {

    String username,followcount,followingcount,follow_following_key,otheruserid;
    static public Button followingbtn,followersbtn;
    RecyclerView followlist,followinglist;
    TextView usernameintent;
    EditText editsearch;
    ImageView searchclose;
    public static String othertoken;

    private RequestQueue mQueue;
    ArrayList<Model_userfollow_unfollow> userfollowL;
    ArrayList<Model_userfollow_unfollow> userfollowingL;

    Adapter_following adapterfollowing;
    Adapter_follow adapterfollow;
    LottieAnimationView animationView;

    int pastVisibleItems11, visibleItemCount11, totalItemCount11;
    Boolean loading11 = true;
    int currentPage=-1;
    int page_no;
    private boolean loading;
    ProgressBar p_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userfollow_following);
        usernameintent=findViewById(R.id.username);
        followlist=findViewById(R.id.followlist);
        followinglist=findViewById(R.id.followinglist);
        followingbtn=findViewById(R.id.followingbtn);
        followersbtn=findViewById(R.id.followersbtn);
        animationView = findViewById(R.id.animation_view_1);
        editsearch= findViewById(R.id.searchedit);
        searchclose=findViewById(R.id.serchclose);
        p_bar=findViewById(R.id.p_bar);


        mQueue = Volley.newRequestQueue(this);
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(this);
        othertoken = phoneauthshard.getString("token", "null");

        if (getIntent().getExtras() != null) {
            Intent intent = getIntent();
            username = intent.getStringExtra("username");
            followingcount = intent.getStringExtra("followingcount");
            followcount = intent.getStringExtra("followcount");
            follow_following_key = intent.getStringExtra("key");
            otheruserid=intent.getStringExtra("userid");
            usernameintent.setText(username);
            followingbtn.setText("Following"+" "+followingcount);
            followersbtn.setText("Followers"+" "+followcount);
        }
        if (follow_following_key.equals("otherfollowing")) {
            followingbtn.setBackgroundResource(R.drawable.homeback);
            followersbtn.setBackgroundResource(R.color.transparent);
            followinglist.setVisibility(View.VISIBLE);
            followlist.setVisibility(View.GONE);
            userfollowingL = new ArrayList<>();
            userfollowingL.clear();
            jsonfollow_following(otherollowing_url+otheruserid);

            //followingpagination();
        }else if (follow_following_key.equals("otherfollower")){
            followersbtn.setBackgroundResource(R.drawable.homeback);
            followingbtn.setBackgroundResource(R.color.transparent);

            followinglist.setVisibility(View.GONE);
            followlist.setVisibility(View.VISIBLE);
            //followergpagination();
            userfollowL = new ArrayList<>();

            userfollowL.clear();
            jsonfollow_followers(otherfollower_url+otheruserid);

        }

        followersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followersbtn.setBackgroundResource(R.drawable.homeback);
                followingbtn.setBackgroundResource(R.color.transparent);

                followinglist.setVisibility(View.GONE);
                followlist.setVisibility(View.VISIBLE);
                userfollowL = new ArrayList<>();
                userfollowL.clear();
                jsonfollow_followers(otherfollower_url+otheruserid);
                //  followergpagination();
            }
        });
        followingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followingbtn.setBackgroundResource(R.drawable.homeback);
                followersbtn.setBackgroundResource(R.color.transparent);
                followinglist.setVisibility(View.VISIBLE);
                followlist.setVisibility(View.GONE);
                userfollowingL = new ArrayList<>();
                userfollowingL.clear();
                jsonfollow_following(otherollowing_url+otheruserid);
                //followingpagination();
            }
        });

        searchclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editsearch.getText().clear();
            }
        });

    }


    private void jsonfollow_following(String url) {
        Log.d("jsonParseuser", "user_follow_folloewing" + url);
        loader();
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Responsefollow", response.toString());
                        animationView.cancelAnimation();
                        animationView.setVisibility(View.GONE);

                        if (response.length() != 0) {
                            // Iterate the inner "data" array
                            try {
                                JSONArray following = response.getJSONArray("following");
                                if (following.length() != 0) {
                                    for (int k = 0; k < following.length(); k++) {
                                        Model_userfollow_unfollow userfollow_unfollow = new Model_userfollow_unfollow();

                                        JSONObject followobj = following.getJSONObject(k);
                                        userfollow_unfollow.setPostid(followobj.getString("_id"));
                                        userfollow_unfollow.setFollowinguser(followobj.getString("isFollowing"));
                                        userfollow_unfollow.setProfiletype("otherprofile");
                                        userfollow_unfollow.setUserprofilefollow("otherfollowing");

                                        if (followobj.has("followingId")) {
                                            JSONObject followingId = followobj.getJSONObject("followingId");
                                            userfollow_unfollow.setUserpic(followingId.getString("profilePic"));
                                            userfollow_unfollow.setName(followingId.getString("name"));
                                            userfollow_unfollow.setUserid(followingId.getString("_id"));
                                        }
                                        userfollowingL.add(userfollow_unfollow);
                                    }
                                    LinearLayoutManager layoutfollow = new LinearLayoutManager(Otherprofile_follow_following.this, LinearLayoutManager.VERTICAL, false);
                                    followinglist.setLayoutManager(layoutfollow);
                                    followinglist.setHasFixedSize(false);

                                    adapterfollowing = new Adapter_following(userfollowingL, Otherprofile_follow_following.this);
                                    followinglist.setAdapter(adapterfollowing);

                                    editsearch.addTextChangedListener(new TextWatcher() {

                                        public void afterTextChanged(Editable s) {
                                            Log.d("edittext","::::"+adapterfollowing.getFilter());
                                            adapterfollowing.getFilter().filter(s.toString());
                                            adapterfollowing.notifyDataSetChanged();
                                            followinglist.setAdapter(adapterfollowing);
                                        }

                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }

                                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        }
                                    });

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {

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
                //headers.put("Content-Type","application/json");
                headers.put("Authorization",othertoken);
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
                animationView.cancelAnimation();
                animationView.setVisibility(View.GONE);
            }
        });
    }


    private void jsonfollow_followers(String url) {
        Log.d("jsonParseuser", "user_follow_followers" + url);
        loader();

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Responsefollow", response.toString());
                        animationView.cancelAnimation();
                        animationView.setVisibility(View.GONE);
                        if (response.length() != 0) {
                            // Iterate the inner "data" array

                            try {
                                JSONArray followers = response.getJSONArray("followers");
                                if (followers.length() != 0){
                                    for (int k = 0; k < followers.length(); k++) {
                                        Model_userfollow_unfollow userfollow_unfollow = new Model_userfollow_unfollow();

                                        JSONObject followersobj = followers.getJSONObject(k);
                                        userfollow_unfollow.setPostid(followersobj.getString("_id"));
                                        userfollow_unfollow.setFollowinguser(followersobj.getString("isFollowing"));
                                        userfollow_unfollow.setProfiletype("otherprofile");
                                        userfollow_unfollow.setUserprofilefollow("otherfollower");

                                        if (followersobj.has("userId")) {
                                            JSONObject userId = followersobj.getJSONObject("userId");
                                            userfollow_unfollow.setUserpic(userId.getString("profilePic"));
                                            userfollow_unfollow.setName(userId.getString("name"));
                                            userfollow_unfollow.setUserid(userId.getString("_id"));
                                        }
                                        userfollowL.add(userfollow_unfollow);
                                    }
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(Otherprofile_follow_following.this, LinearLayoutManager.VERTICAL, false);
                                    followlist.setLayoutManager(layoutManager);
                                    followlist.setHasFixedSize(false);

                                    adapterfollow = new Adapter_follow(userfollowL, Otherprofile_follow_following.this);
                                    followlist.setAdapter(adapterfollow);

                                    editsearch.addTextChangedListener(new TextWatcher() {

                                        public void afterTextChanged(Editable s) {
                                            adapterfollow.getFilter().filter(s.toString());
                                            adapterfollow.notifyDataSetChanged();
                                            followlist.setAdapter(adapterfollow);
                                        }

                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        }

                                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }else {

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
                //headers.put("Content-Type","application/json");
                headers.put("Authorization",othertoken);
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
                animationView.cancelAnimation();
                animationView.setVisibility(View.GONE);
            }
        });
    }

    public void finishActivity(View v){
        finish();
    }

    public void loader(){
        //lotte loader
        animationView.setAnimation("data.json");
        animationView.loop(true);
        animationView.playAnimation();
        animationView.setVisibility(View.VISIBLE);
    }

}
