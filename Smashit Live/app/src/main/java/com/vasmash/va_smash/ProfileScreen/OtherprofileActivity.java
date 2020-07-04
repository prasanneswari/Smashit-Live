package com.vasmash.va_smash.ProfileScreen;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;
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

import static com.vasmash.va_smash.ProfileScreen.ProfileActivity.createvideosclick;
import static com.vasmash.va_smash.VASmashAPIS.APIs.block_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.follow_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.getprofile_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.other_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.otherprofile_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.otherprofileuser_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.unblock_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.unfollow_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.usercreated_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.userreport_url;

public class OtherprofileActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    ArrayList<Model_Trading> searchindividualmodel;
    public static ArrayList<String> fileL;

    // private Profile_ContentAdapter mAdapter;
    private Adapter_TradingTabs mAdapter;
    ImageView otherprofile_image,displayimg;
    TextView followingclick,followersclick,otherlikes_count,otherprofile_username,otherfollowing_count,otherfollower_count;
    LinearLayout follow,unfollow,profilelay,displayimglay;
    String token, blocked="null",followcount,otherprofileimg="null";
    Dialog dialog;
    private RequestQueue mQueue;
    String posteduserid;

    private Toolbar toolbar;
    //report
    EditText desE;
    Spinner reasonE;
    String selectedItemText;
    String[] reasonspinner = {"Select Reasons","Inappropriate Content", " Abusive  Content"};

    MenuItem blockmenu,unblockmenu;
    //this is the loading animationview.
    ViewDialog viewDialog;

    int pastVisibleItems11, visibleItemCount11, totalItemCount11;
    Boolean loading11 = true;
    int currentPage=-1;
    int page_no;
    ProgressBar p_bar;
    private boolean loading;
    String otherusername,follower,follewing;
    boolean displayclick=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherprofile);
        if (getIntent().getExtras() != null) {
            posteduserid = getIntent().getStringExtra("posteduserid");
            Log.e("usid", posteduserid);
        }
        mQueue = Volley.newRequestQueue(this);
        otherprofile_image=(ImageView)findViewById(R.id.otherprofile_image);
        otherlikes_count=(TextView) findViewById(R.id.otherprofile_like_count);
        otherfollowing_count=(TextView) findViewById(R.id.otherprofile_following_count);
        otherfollower_count=(TextView) findViewById(R.id.otherprofile_follower_count);
        otherprofile_username=(TextView) findViewById(R.id.otherprofile_name);
        toolbar = findViewById(R.id.toolbar);
        p_bar=findViewById(R.id.p_bar);
        followersclick=findViewById(R.id.followersclick);
        followingclick=findViewById(R.id.followingclick);
        profilelay=findViewById(R.id.profilelay);
        displayimglay=findViewById(R.id.displayimglay);
        displayimg=findViewById(R.id.displayimg);

        viewDialog = new ViewDialog(this);

        //it checks where network available or not
        if (isNetworkAvailable()) {
            //Toast.makeText(Home.this, "Network connection is available", Toast.LENGTH_SHORT).show();
        } else if (!isNetworkAvailable()) {
            // Toast.makeText(Home.this, "Network connection is not available", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Network connection is not available", Snackbar.LENGTH_LONG);
            snackbar.show();
        }


        jsonotherdata(posteduserid);
        setSupportActionBar(toolbar);
        //   toolbar.setBackgroundResource(R.color.toolback);
        try
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        catch (NullPointerException e){}


        follow=findViewById(R.id.follow);
        unfollow=findViewById(R.id.unfollow);

        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(this);
        token = phoneauthshard.getString("token", "null");


        recyclerView = (RecyclerView) findViewById(R.id.other_content_recycleview);
        othercreated();

        //here it gives the user followers
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginS = "{\"userId\":\"" + posteduserid + "\"}";
                Log.d("jsnresponse follow", "---" + loginS);
                String url=follow_url;
                JSONObject lstrmdt;
                try {
                    lstrmdt = new JSONObject(loginS);
                    Log.d("jsnresponse....", "---" + loginS);
                    viewDialog.showDialog();
                    JSONSenderVolleylogin(lstrmdt,url);

                } catch (JSONException ignored) {
                }

            }
        });
        //here it gives the user unfollowing
        unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginS = "{\"followingId\":\"" + posteduserid + "\"}";
                Log.d("jsnresponse follow", "---" + loginS);
                String url=unfollow_url;
                JSONObject lstrmdt;
                try {
                    lstrmdt = new JSONObject(loginS);
                    Log.d("jsnresponse....", "---" + loginS);
                    viewDialog.showDialog();
                    JSONSenderVolleylogin(lstrmdt,url);

                } catch (JSONException ignored) {
                }
            }
        });

        followingclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherprofileActivity.this, Otherprofile_follow_following.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("username",otherusername);
                intent.putExtra("followingcount",follewing);
                intent.putExtra("followcount",follower);
                intent.putExtra("key","otherfollowing");
                intent.putExtra("userid",posteduserid);

                startActivityForResult(intent, 1);
            }
        });
        followersclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OtherprofileActivity.this, Otherprofile_follow_following.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("username",otherusername);
                intent.putExtra("followcount",follower);
                intent.putExtra("followingcount",follewing);
                intent.putExtra("key","otherfollower");
                intent.putExtra("userid",posteduserid);
                startActivityForResult(intent, 1);
            }
        });
        otherprofile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayclick=true;
                profilelay.setVisibility(View.GONE);
                displayimglay.setVisibility(View.VISIBLE);
                if (!otherprofileimg.equals("null")) {
                    Picasso.with(OtherprofileActivity.this).load(otherprofileimg).placeholder(R.drawable.uploadpictureold).into(displayimg);
                }
            }
        });
    }
    public void othercreated(){
        searchindividualmodel = new ArrayList<>();
        fileL=new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        // set a GridLayoutManager with 3 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        recyclerView.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        // recyclerView.setLayoutManager(new GridLayoutManager(ProfileActivity.this, 3));
        mAdapter = new Adapter_TradingTabs(OtherprofileActivity.this, searchindividualmodel,fileL);
        recyclerView.setAdapter(mAdapter);

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
                    Log.d("visibleItemCount","::::"+visibleItemCount11+":::"+totalItemCount11+"::"+pastVisibleItems11+":::"+loading11);

                    if (!loading11) {
                        // if ((visibleItemCount11 + pastVisibleItems11) >= totalItemCount11 && visibleItemCount11 >= 0 && totalItemCount11 >= tags.size()) {
                        Log.d("checkpage","::"+gridLayoutManager.findLastCompletelyVisibleItemPosition()+":::"+searchindividualmodel.size());
                        if(gridLayoutManager.findLastCompletelyVisibleItemPosition() == searchindividualmodel.size()-1){
                            loading11 = true;
                            loading=true;
                            jsongetvastore(otherprofileuser_url+posteduserid+"&limit=10&skip="+searchindividualmodel.size());

                        }
                    }
                }
            }
        });
        jsongetvastore(otherprofileuser_url+posteduserid+"&limit=10&skip=0");

    }

    private void jsongetvastore(String url) {
        Log.d("jsonParseuser", "store data" + url);
        if (loading){
            p_bar.setVisibility(View.VISIBLE);
        }else {
            viewDialog.showDialog();
        }
        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("followotherdata", response.toString());
                        if (loading){
                            p_bar.setVisibility(View.GONE);
                        }else {
                            viewDialog.hideDialog();
                        }
                        createvideosclick="false";
                        if (response.length() != 0) {
                            // Iterate the inner "data" array
                            try {
                                if (response.has("otherUserCreatedPostsList")) {
                                    JSONArray likes = response.getJSONArray("otherUserCreatedPostsList");
                                    if (likes.length() != 0){
                                        for (int k = 0; k < likes.length(); k++) {
                                            JSONObject lkks = likes.getJSONObject(k);
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
                                                String userid = lkks.getString("userId");
                                                searchhm.setUserid(userid);
                                            } else {
                                                searchhm.setUserid(" ");
                                            }
                                            if (lkks.has("userId")) {
                                                JSONArray nameobj = lkks.getJSONArray("userId");
                                                Log.d("ntringguseridd", "::::" + nameobj);
                                                for (int k1 = 0; k1 < nameobj.length(); k1++) {
                                                    JSONObject userarray = nameobj.getJSONObject(k1);
                                                    if (userarray.has("name")) {
                                                        String otherusername = userarray.getString("name");
                                                        searchhm.setUsername(otherusername);
                                                    }
                                                    if (userarray.has("profilePic")) {
                                                        String userimg = userarray.getString("profilePic");
                                                        Picasso.with(OtherprofileActivity.this).load(userimg).placeholder(R.drawable.uploadpictureold).into(otherprofile_image);
                                                        //searchhm.setProfilepic(userimg);
                                                        searchhm.setProfilepic(userimg);
                                                    } else {
                                                        //searchhm.setProfilepic("null");
                                                    }
                                                    String likeuserid = userarray.getString("_id");
                                                    searchhm.setUserid(likeuserid);
                                                }
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
                                            } else {
                                                searchhm.setSoundname("");
                                                searchhm.setSoundurl("");
                                                searchhm.setSoundpostid("");
                                                searchhm.setSounduserid("");
                                            }
                                            searchindividualmodel.add(searchhm);
                                        }
                                        mAdapter.notifyDataSetChanged();
                                        loading11 = false;
                                    }
                                    else {
                                        loading11 = true;
                                    }
                                }
                            } catch (JSONException e) {
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
                                        Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
                                            if (loading){
                                                p_bar.setVisibility(View.GONE);
                                            }else {
                                                viewDialog.hideDialog();
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
                }
            }
        });
    }
    public void JSONSenderVolleylogin(JSONObject lstrmdt, String url) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        Log.d("555555", "url" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONfollowand unfollow", "---" + response);
                        viewDialog.hideDialog();
                        try {
                            String message=response.getString("message");
                            String status=response.getString("status");
                            if (response.has("count")) {
                                followcount = response.getString("count");
                            }

                            if (status.equals("1")){
                                //jsongetvastore(posteduserid);
                                // popup(message);
                                follow.setVisibility(View.GONE);
                                unfollow.setVisibility(View.VISIBLE);
                                otherfollower_count.setText(followcount);
                            }else if (status.equals("2")){
                                // popup(message);
                                follow.setVisibility(View.VISIBLE);
                                unfollow.setVisibility(View.GONE);
                                otherfollower_count.setText(followcount);
                            }else if (status.equals("3")){
                                desE.getText().clear();
                                dialog.dismiss();
                                popup(message);
                                //  Toast.makeText(OtherprofileActivity.this, message, Toast.LENGTH_SHORT).show();
                            }else if (status.equals("4")){
                                Log.d("enteringgblock",":::");
                                popup(message);
                                blockmenu.setVisible(false);
                                unblockmenu.setVisible(true);
                                // Toast.makeText(OtherprofileActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            else if (status.equals("5")){
                                popup(message);
                                blockmenu.setVisible(true);
                                unblockmenu.setVisible(false);
                                // Toast.makeText(OtherprofileActivity.this, message, Toast.LENGTH_SHORT).show();
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
                                    //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
            }
        })
        {
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
            mQueue = Volley.newRequestQueue(OtherprofileActivity.this);
        }
        mQueue.add(req);
    }
    //this is the response popup
    public void popup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = OtherprofileActivity.this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.responsepopup, null);


        Button okbtn = (Button) layout.findViewById(R.id.okbtn);
        TextView message = (TextView) layout.findViewById(R.id.msg);
        message.setText(msg);
        okbtn.setOnClickListener(new View.OnClickListener() {
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
    //this is the other profile settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.reportmenu);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return onOptionsItemSelected(item);
                    }
                });
        return true;
    }

    //
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.report:
                //here it gives the user report
                reportpopup();
                return true;

            case R.id.block:
                //here it gives the user block
                String url = block_url;
                block(url);
                return true;
            case R.id.unblock:
                //here it gives the user unblock
                String url1 = unblock_url;
                block(url1);
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public boolean onPrepareOptionsMenu(Menu menu) {
        blockmenu = menu.findItem(R.id.block);
        unblockmenu = menu.findItem(R.id.unblock);
        blockmenu.setVisible(true);
        unblockmenu.setVisible(false);

        if (!blocked.equals("null")) {
            if (blocked.equals("true")) {
                blockmenu.setVisible(false);
                unblockmenu.setVisible(true);
            } else {
                blockmenu.setVisible(true);
                unblockmenu.setVisible(false);
            }
        }

        return true;
    }
    //this is the report popup
    public void reportpopup() {
        android.app.AlertDialog.Builder builder;
        final Context mContext = OtherprofileActivity.this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.reportpoup, null);

        Button nobtn = (Button) layout.findViewById(R.id.nobtn);
        Button reportbtn = (Button) layout.findViewById(R.id.reportbtn);
        reasonE = (Spinner) layout.findViewById(R.id.reason);
        desE = (EditText) layout.findViewById(R.id.des);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(OtherprofileActivity.this,android.R.layout.simple_spinner_item,reasonspinner);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        reasonE.setAdapter(aa);
        reasonE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //txt.setText("User Report");
        reportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginS = "{\"userId\":\"" + posteduserid + "\",\"reason\":\"" + selectedItemText + "\",\"description\":\"" + desE.getText().toString() + "\"}";
                Log.d("jsnresponse reason", "---" + loginS);
                String url = userreport_url;
                JSONObject lstrmdt;

                try {
                    lstrmdt = new JSONObject(loginS);
                    Log.d("jsnresponse....", "---" + loginS);
                    viewDialog.showDialog();
                    JSONSenderVolleylogin(lstrmdt,url);
                } catch (JSONException ignored) {
                }
            }
        });
        nobtn.setOnClickListener(new View.OnClickListener() {
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
    //this is the block popup
    public void block(String url){
        Log.d("url1111", "---" + url);
        String loginS = "{\"userId\":\"" + posteduserid + "\"}";
        Log.d("jsnresponse block", "---" + loginS);
        JSONObject lstrmdt;

        try {
            lstrmdt = new JSONObject(loginS);
            Log.d("jsnresponse....", "---" + loginS);
            viewDialog.showDialog();
            JSONSenderVolleylogin(lstrmdt,url);
        } catch (JSONException ignored) {
        }

    }

    public void finishActivity(View v){
/*
        if (countDownTimer != null) {
            countDownTimer.start();
            privious_player.setPlayWhenReady(true);
        }
*/
        finish();
    }
    //it checks where network available or not
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void jsonotherdata(String posteduserid) {
        Log.d("jsonParseuser", "other_url " + other_url+posteduserid);
        //  viewDialog.showDialog();
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, other_url+posteduserid, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //  viewDialog.hideDialog();
                        Log.d("JSONgabal", "---" + response);
                        if (response.length()!=0) {
                            try {
                                //JSONArray array = response.getJSONArray("stackingTransactions");
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jItem = response.getJSONObject(i);
                                    JSONObject ids=jItem.getJSONObject("_id");
                                    // String otheruserid=ids.getString("userId");
                                    otherusername=ids.getString("username");
                                    if (ids.has("profilePic")) {
                                        otherprofileimg = ids.getString("profilePic");
                                        Picasso.with(OtherprofileActivity.this).load(otherprofileimg).placeholder(R.drawable.uploadpictureold).into(otherprofile_image);
                                    }
                                    follower=ids.getString("followers");
                                    follewing=ids.getString("followings");
                                    String followingUser=ids.getString("followingUser");
                                    // String created=ids.getString("created");
                                    String totalLikes=ids.getString("totalLikes");
                                    Log.d("folloersss",":::"+follower);

                                    blocked=ids.getString("blocked");

                                    otherlikes_count.setText(totalLikes);

                                    if (followingUser.equals("false")){
                                        follow.setVisibility(View.VISIBLE);
                                        unfollow.setVisibility(View.GONE);
                                    }else {
                                        follow.setVisibility(View.GONE);
                                        unfollow.setVisibility(View.VISIBLE);
                                    }
                                    otherprofile_username.setText(otherusername);
                                    otherfollowing_count.setText(follewing);
                                    otherfollower_count.setText(follower);
                                }
                            } catch(JSONException e){
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
                                        Log.d("body", "---" + body);
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

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        if (displayclick==true){
            displayimglay.setVisibility(View.GONE);
            profilelay.setVisibility(View.VISIBLE);
            displayclick=false;
        }else {
            finish();
        }
        return;
    }

}
