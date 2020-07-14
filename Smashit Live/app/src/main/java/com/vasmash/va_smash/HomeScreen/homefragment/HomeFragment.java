package com.vasmash.va_smash.HomeScreen.homefragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment;
import com.vasmash.va_smash.HomeScreen.CommentScreen.Fragment_Data_Send;
import com.vasmash.va_smash.HomeScreen.ModelClass.Homescreen_model;
import com.vasmash.va_smash.HomeScreen.homeadapters.FragmentInner;
import com.vasmash.va_smash.HomeScreen.homeadapters.Fragment_Callback;
import com.vasmash.va_smash.HomeScreen.homeadapters.Functions;
import com.vasmash.va_smash.HomeScreen.homeadapters.VideoAction_F;
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.ProfileScreen.ProfileActivity;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.SearchData;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;
import com.vasmash.va_smash.createcontent.CameraActivity;
import com.vasmash.va_smash.createcontent.filters.gpu.composer.GPUMp4Composer;
import com.vasmash.va_smash.createcontent.filters.gpu.egl.filter.GlWatermarkFilter;
import com.vasmash.va_smash.login.fragments.LoginFragment;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.shawnlin.numberpicker.NumberPicker;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.claimedtopnav;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.claimhometopnav;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.claimtext;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.clamiedget;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.clamlay;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.countDownTimer;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.create;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.earningpic;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.earningpoints;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.earningtxt;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.following;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.foryou;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.progressBarView;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.search;
import static com.vasmash.va_smash.HomeScreen.homeadapters.FragmentInner.postid;
import static com.vasmash.va_smash.VASmashAPIS.APIs.Catageriesapi;
import static com.vasmash.va_smash.VASmashAPIS.APIs.claimsmash_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.earningpost_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.fcmtoken_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.homeapi_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.homepagination_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.likesend_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.postreport_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.unlike_url;
import static com.yalantis.ucrop.UCropFragment.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements Player.EventListener ,Fragment_Data_Send{
    Dialog dialog;
    boolean is_visible_to_user;
    String claimtype,commentvideocount,videotype="null";
    ImageView fadecolor;
    RelativeLayout numberpickerly;
    LinearLayout catswitchlay;
    private RequestQueue mQueue;

    RecyclerView recyclerView;

    LinearLayoutManager layoutManager;

    static public FragmentInner homeadapter;
    ArrayList<Homescreen_model> tags;
    int currentPage=-1;
    int page_no;

    // static public SendMessageclaimed topnavclaimed;
    static public SendMessage1 sm;


    NumberPicker numberPicker;
    public static ArrayList<String> catidL;
    public static ArrayList<String> catnameL;
    int newval;
    public long dur;
    String climedhome="null";

    String unliketype,soundname,soundurl,soundid;

    private long mLastClickTime = 0;


    boolean catclik;
    public static String hometoken;
    ArrayList<Tags> des;

    //report
    EditText desE;
    Spinner reasonE;
    String selectedItemText;
    String[] reasonspinner = {"Select Reasons","Inappropriate Content", " Abusive  Content"};
    TextView homecommenttxt;
    ViewDialog viewDialog;
    ImageView refresh;
    LinearLayout refreshlay;

    int pastVisibleItems11, visibleItemCount11, totalItemCount11;
    Boolean loading11 = true;
    private boolean loading;
    ProgressBar p_bar;
    private boolean followpagination=false;
    private boolean catpagiation=false;
    private boolean allposts=false;

    String   defaultVideo;

    int progress = 0;
    private CountDownTimer countDownTimerhome;
    FFmpeg ffmpeg;

    String catid;
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home_, container, false);
        mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());

        recyclerView=view.findViewById(R.id.list);
        catswitchlay=view.findViewById(R.id.catswitchlay);
        refresh=view.findViewById(R.id.refresh);
        refreshlay=view.findViewById(R.id.refreshlay);

        viewDialog = new ViewDialog(getActivity());

        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(getActivity());
        hometoken = phoneauthshard.getString("token", "null");
        if (!hometoken.equals("null")) {
            //this is the firebase notifictaion send
            fcmtokensend();
        }
        recyclarview();
        loadFFMpegBinary();

        numberPicker = (NumberPicker)view. findViewById(R.id.number_picker);
        numberpickerly=view.findViewById(R.id.numberpickerly);
        fadecolor=view.findViewById(R.id.fadecolor);
        categoryapi(Catageriesapi);


        //this is the you clicking
        foryou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foryou.setBackgroundResource(R.drawable.homeback);
                following.setBackgroundResource(R.color.transparent);
                fadecolor.setVisibility(View.GONE);
                Release_Privious_Player();

                currentPage=1;
                followpagination=false;

                tags=new ArrayList<>();
                des=new ArrayList<>();
                loading=false;
                homeadapter = new FragmentInner(tags,des, getActivity());
                recyclerView.setAdapter(homeadapter);

                homeapi(homepagination_url+0);
                //Load_add();
            }
        });

        //this is the love clicking
        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hometoken.equals("null")){
                    popup();
                }else {
                    foryou.setBackgroundResource(R.color.transparent);
                    following.setBackgroundResource(R.drawable.homeback);
                    fadecolor.setVisibility(View.GONE);
                    Release_Privious_Player();
                    currentPage=1;
                    tags=new ArrayList<>();
                    des=new ArrayList<>();
                    loading=false;
                    homeadapter = new FragmentInner(tags,des, getActivity());
                    recyclerView.setAdapter(homeadapter);
                    followpagination=true;
                    // homeapi(homeapi_url + "/following");
                    homeapi(homeapi_url + "/following?skip=0");
                }
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
                homeapi(homepagination_url+0);
            }
        });

        earningpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // viewPager.setCurrentItem(3);

                if (hometoken.equals("null")){
                    popup();
                }else {
                    if (countDownTimer!=null) {
                        countDownTimer.cancel();
                    }
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivityForResult(intent, 1);
                }
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // viewPager.setCurrentItem(3);
                if (hometoken.equals("null")){
                    popup();
                }else {
                    if (countDownTimer!=null) {
                        countDownTimer.cancel();
                    }
                    Intent intent = new Intent(getActivity(), SearchData.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hometoken.equals("null")){
                    popup();
                }else {
                    Intent intent = new Intent(getActivity(), CameraActivity.class);
                    startActivity(intent);
                }

            }
        });

        return view;
    }
    public void fcmtokensend(){
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful()){
                    String firebasetoken=task.getResult().getToken();
                   // Log.d("firebasetoken",":::"+firebasetoken);
                    String loginS = "{\"token\":\"" + firebasetoken + "\",\"device\":\"" + "android" + "\"}";
                    //Log.d("ofterlogin firebase", "---" + loginS);
                    String url=fcmtoken_url;
                    JSONObject lstrmdt;
                    try {
                        lstrmdt = new JSONObject(loginS);
                       // Log.d("jsnresponse....", "---" + loginS);
                        JSONSenderVolleyearning(lstrmdt,url,currentPage);

                    } catch (JSONException ignored) {
                    }
                }else {

                }
            }
        });
    }
    // this is the scroll listener of recycler view which will tell the current item number
    public void recyclarview(){

        tags=new ArrayList<>();
        des=new ArrayList<>();

        layoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        SnapHelper snapHelper =  new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        homeadapter = new FragmentInner(tags,des, getActivity());
        recyclerView.setAdapter(homeadapter);

        // this is the scroll listener of recycler view which will tell the current item number
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState==1){
                    visibleItemCount11 = layoutManager.getChildCount();
                    totalItemCount11 = layoutManager.getItemCount();
                    pastVisibleItems11 = layoutManager.findFirstVisibleItemPosition();
                    //Log.d("visibleItemCount","::::"+visibleItemCount11+":::"+totalItemCount11+"::"+pastVisibleItems11+":::"+loading11);
                    if (!loading11) {
                        // if ((visibleItemCount11 + pastVisibleItems11) >= totalItemCount11 && visibleItemCount11 >= 0 && totalItemCount11 >= tags.size()) {
                       // Log.d("checkpage","::"+layoutManager.findLastCompletelyVisibleItemPosition()+":::"+tags.size());
                        if(layoutManager.findLastCompletelyVisibleItemPosition() == tags.size()-1){
                            loading11 = true;
                            loading=true;
                            //Log.d("printvall",":::;"+followpagination+"::"+catpagiation);
                            if (loading) {
                                tags.add(new Homescreen_model(1));
                                homeadapter.notifyItemInserted(tags.size() - 1);

                               // if (tags.size() != 0) {
                                    if (followpagination == true) {
                                        homeapi(homeapi_url + "/following?skip=" + (tags.size()-1));
                                    } else if (followpagination == false && catpagiation == false && allposts == false) {
                                        homeapi(homepagination_url + (tags.size()-1));
                                    } else if (catpagiation == true) {
                                        homeapi(homeapi_url + "/postsByCategory?skip=" + (tags.size()-1) + "&categoryId=" + catid);
                                    } else if (allposts == true) {
                                        homeapi(homeapi_url + "/allPosts?skip=" + (tags.size()-1));
                                    }
/*
                                } else {
                                    Toast.makeText(getActivity(), "No More Data", Toast.LENGTH_SHORT).show();
                                }
*/
                            }
                        }
                    }
                }
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
                    Release_Privious_Player();
                    Set_Player(currentPage);
                    //Log.d("currentpagecount","::::"+currentPage);
                }
            }
        });
        //first we will call this home api we will get the video content data
        homeapi(homepagination_url+0);
        //homeapi(homepagination_url+=10+"&skip="+0);

        //Load_add();

    }

    static public SimpleExoPlayer privious_player;
    public void Release_Privious_Player(){
        if(privious_player!=null) {
            privious_player.removeListener(this);
            privious_player.release();
        }
    }
    //this is the category switch data
    private void numberpicker(final ArrayList<String> catidL){

// Set divider color
        numberPicker.setDividerColor(ContextCompat.getColor(getActivity(), R.color.divider));
        numberPicker.setDividerColorResource(R.color.divider);

// Set formatter
        numberPicker.setFormatter(getActivity().getString(R.string.number_picker_formatter));
        numberPicker.setFormatter(R.string.number_picker_formatter);

// Set selected text color
        numberPicker.setSelectedTextColor(ContextCompat.getColor(getActivity(), R.color.selectedtext));
        numberPicker.setSelectedTextColorResource(R.color.selectedtext);

// Set selected text size
        numberPicker.setSelectedTextSize(getActivity().getResources().getDimension(R.dimen.selected_text_size));
        numberPicker.setSelectedTextSize(R.dimen.selected_text_size);


// Set text color
        numberPicker.setTextColor(ContextCompat.getColor(getActivity(), R.color.selectedtext));
        numberPicker.setTextColorResource(R.color.selectedtext);

// Set text size
        numberPicker.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size));
        numberPicker.setTextSize(R.dimen.text_size);


// Set typeface
        numberPicker.setTypeface(Typeface.create(getActivity().getString(R.string.font_family), Typeface.NORMAL));
        numberPicker.setTypeface(getActivity().getString(R.string.font_family), Typeface.NORMAL);
        numberPicker.setTypeface(getActivity().getString(R.string.font_family));
        numberPicker.setTypeface(R.string.font_family, Typeface.NORMAL);
        numberPicker.setTypeface(R.string.font_family);

        //final String[] values = {"Politics", "News & Entertainment", "Games", "Lifestyle", "Education", "Sports", "Travel", "Science & Technology", "Health & Fitness", "Food", "Fashion", "Comedy", "Art", "Test","R1","R2","R3","Animal","Nature","Music"};
        final String[] values = catnameL.toArray(new String[0]);
        numberPicker.setDisplayedValues(null);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(values.length-1);
        numberPicker.setValue(5);
        numberPicker.setDisplayedValues(values);
        numberPicker.setWrapSelectorWheel(true);


// Set fading edge enabled
        numberPicker.setFadingEdgeEnabled(true);

// Set scroller enabled
        numberPicker.setScrollerEnabled(true);

// Set wrap selector wheel
        numberPicker.setWrapSelectorWheel(true);

// OnClickListener
        numberPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Log.d("", "Click on current value"+catnameL);

                for (int i = 0; i< catidL.size(); i++){
                    fadecolor.setVisibility(View.VISIBLE);
                    // Log.d("newval111",":::"+numberPicker.getValue());

                    if (numberPicker.getValue()==i){
                        catid= catidL.get(i);
                       // Log.d("catiddddd",":::"+catid);
                        catclik=true;
                        foryou.setBackgroundResource(R.color.transparent);
                        following.setBackgroundResource(R.color.transparent);
                        Release_Privious_Player();
                        currentPage=1;

                        tags=new ArrayList<>();
                        des=new ArrayList<>();

                        homeadapter = new FragmentInner(tags,des,getActivity());
                        recyclerView.setAdapter(homeadapter);
                        catpagiation=true;
                        loading=false;
                        // homeapi(homeapi_url+"?categoryId="+catid);
                        homeapi(homeapi_url+"/postsByCategory?skip=0&categoryId="+catid);

                    }
                }
            }
        });

// OnValueChangeListener
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Log.d("", String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
                fadecolor.setVisibility(View.GONE);
                newval= newVal;
            }
        });
    }

    public void homeapi(String url){
        //Log.d("home api::::", url+loading);
        if (loading){
           // p_bar.setVisibility(View.VISIBLE);
        }else {
            viewDialog.showDialog();
        }
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                       // Log.d("Response homeee", response.toString());
                        if (loading){
                           // p_bar.setVisibility(View.GONE);
                            if(tags!=null){
                                if(tags.size()>0){
                                    tags.remove(tags.size()-1);
                                }
                            }

                        }else {
                            viewDialog.hideDialog();
                        }
                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);
                                    Homescreen_model hm=new Homescreen_model();
                                    if (employee.has("_id")) {
                                        String _id = employee.getString("_id");
                                        hm.setPostid(_id);
                                    }else {
                                        hm.setPostid("");
                                    }
                                    if (employee.has("file")) {
                                        String name = employee.getString("file");
                                        hm.setFile(name);
                                    }else {
                                        hm.setFile("");
                                    }
                                    if (employee.has("type")) {
                                        String type = employee.getString("type");
                                        hm.setType(type);
                                    }else {
                                        hm.setType("");
                                    }
                                    if (employee.has("likes")) {
                                        String likes = employee.getString("likes");
                                        hm.setLikes(likes);
                                    }else {
                                        hm.setLikes("");
                                    }
                                    if (employee.has("comments")) {
                                        String comments = employee.getString("comments");
                                        hm.setComments(comments);

                                    }else {
                                        hm.setComments(" ");
                                    }
                                    if (employee.has("userLikes")) {
                                        String userLikes = employee.getString("userLikes");
                                        hm.setLiketype(userLikes);
                                    }else {
                                        hm.setLiketype("");
                                    }
                                    if (employee.has("claimed")) {
                                        String claimed = employee.getString("claimed");
                                        hm.setClaim(claimed);
                                    }else {
                                        hm.setClaim("");
                                    }
                                    if (employee.has("description")) {
                                        String description = employee.getString("description");
                                        hm.setDescription(description);
                                    }else {
                                        hm.setDescription("");
                                    }
                                    if (employee.has("shareCount")) {
                                        String shareCount = employee.getString("shareCount");
                                        hm.setShare(shareCount);

                                    }else {
                                        hm.setShare("");
                                    }

                                    if (employee.has("userId")) {
                                        JSONObject nameobj=employee.getJSONObject("userId");
                                        String userid = nameobj.getString("_id");
                                        if(nameobj.has("name")) {
                                            String username = nameobj.getString("name");
                                            hm.setUsername(username);
                                        }
                                        String homeprofilePic = nameobj.getString("profilePic");

                                       // Log.d("profilepicuser",":::"+homeprofilePic);
                                        hm.setUserprofilepic(homeprofilePic);
                                        hm.setUserid(userid);
                                        // sm.sendData1(homeprofilePic);
                                    }else {
                                        String username = "name";
                                        hm.setUsername(username);
                                        hm.setUserprofilepic("null");
                                        hm.setUserid("");
                                        //sm.sendData1("null");
                                    }
                                    if (employee.has("tags")) {
                                        JSONArray tagsarray = employee.getJSONArray("tags");
                                        Tags ts=new Tags();

                                        if (tagsarray.length()!=0) {
                                            for (int k = 0; k < tagsarray.length(); k++) {
                                                JSONObject tagarray = tagsarray.getJSONObject(k);

                                                String tagid = tagarray.getString("_id");
                                                if (tagarray.has("tag")) {
                                                    String tag = tagarray.getString("tag");
                                                   // Log.d("tag:::::",""+tag);

                                                    ts.setId(tagarray.getString("_id"));
                                                    ts.setName(tagarray.getString("tag"));
                                                    des.add(ts);
                                                }else {
                                                    String tag = " ";
                                                    ts.setId("");
                                                    ts.setName("no tag");
                                                    des.add(ts);
                                                }
                                            }
                                        }else {
                                            String tag = "..";
                                            ts.setId("");
                                            ts.setName("no tag");
                                            des.add(ts);
                                        }
                                    }
                                    if (employee.has("soundId")) {
                                        //JSONArray sounds = employee.getJSONArray("soundId");
                                        //Log.d("soundsss","::::"+sounds);
                                       // for (int k1 = 0; k1 < sounds.length(); k1++) {
                                            JSONObject soundsobj = employee.getJSONObject("soundId");
                                            if(soundsobj.has("_id")) {
                                                String soundid = soundsobj.getString("_id");
                                                hm.setSoundid(soundid);
                                            }
                                            if (soundsobj.has("name")) {
                                                String soundname = soundsobj.getString("name");
                                                hm.setSoundname(soundname);
                                            }
                                            if (soundsobj.has("url")) {
                                                String soundurl = soundsobj.getString("url");
                                                hm.setSoundurl(soundurl);
                                            }
                                            if (soundsobj.has("postId")) {
                                                String soundpostId = soundsobj.getString("postId");
                                                hm.setSoundpostid(soundpostId);
                                            }
                                            if (soundsobj.has("userId")) {
                                                String sounduserId = soundsobj.getString("userId");
                                                hm.setSounduserid(sounduserId);
                                            }
                                       // }
                                    }else {
                                        hm.setSoundname("");
                                        hm.setSoundurl("");
                                        hm.setSoundpostid("");
                                        hm.setSounduserid("");
                                    }
                                    tags.add(hm);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            homeadapter.notifyDataSetChanged();
                            loading11 = false;
                            loading=false;
                        }else {
                            allposts=true;
                            homeapi(homeapi_url + "/allPosts?skip=0");
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
                                       // Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
                                            if (loading){
                                               // p_bar.setVisibility(View.GONE);
                                            }else {
                                                viewDialog.hideDialog();
                                            }
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            if (obj.has("status")) {
                                                String status = errors.getString("status");
                                                //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                                if (status.equals("1")) {
                                                    fallowpopup(message);
                                                }
                                            }
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
                if (!hometoken.equals("null")) {
                    headers.put("Authorization", hometoken);
                    System.out.println("headddddd" + headers);
                }

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
                  //  p_bar.setVisibility(View.GONE);
                }else {
                    viewDialog.hideDialog();
                }
            }
        });
    }
    //this is the catogry api data
    public void categoryapi(String ul) {
        catnameL=new ArrayList<>();
        catidL=new ArrayList<>();
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, ul, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        //Log.d("Response", response.toString());
                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);

                                    String _id = employee.getString("_id");
                                    String name = employee.getString("name");

                                    catidL.add(_id);
                                    catnameL.add(name);
                                   // Log.d("listtttt",":::"+catnameL);
                                    numberpickerly.setVisibility(View.VISIBLE);
                                    catswitchlay.setVisibility(View.VISIBLE);
                                    numberpicker(catidL);

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
                                       // Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
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
                });

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

            }
        });

    }
    //here we will get the comments updated respnse
    @Override
    public void onDataSent(String yourData) {
        //Log.d("cmmentdata",":::"+yourData);
        homecommenttxt.setText(yourData);
        tags.get(currentPage).setComments(yourData);
    }
    public interface SendMessage1 {
        void sendData1(String message, String claimed, String climedhome);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sm = (SendMessage1) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Error in retrieving data. Please try again");
        }
    }
    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                //Player is in state State buffering show some loading progress
                //showProgress();
               // Log.d("playbackstate",":::"+Player.STATE_BUFFERING);

                //p_bar.setVisibility(View.VISIBLE);
                if (countDownTimer!=null) {
                    countDownTimer.cancel();
                }
                break;
            case Player.STATE_READY:
                //Player is ready to Play. Remove loading progress
                // hideProgress();
                //p_bar.setVisibility(View.GONE);
                if (countDownTimer!=null) {
                    countDownTimer.start();
                }
                break;
        }
    }
    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }
    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }
    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }
    @Override
    public void onPositionDiscontinuity(int reason) {
        //here it shows the complete video and it will call post api. Here it gives 1% for 1 video watching.
       // Log.d("repeatemode","::::"+reason);
        if (hometoken.equals("null")){
            // popup();
        }else {
            // jsonearningpoints(getamount_url,currentPage);
            progressBarView.setProgress(100);
            String loginS = "{\"postId\":\"" + postid + "\"}";
            //Log.d("jsnresponse earning ", "---" + loginS);
            String url = earningpost_url;
            JSONObject lstrmdt;
            try {
                lstrmdt = new JSONObject(loginS);
                //Log.d("jsnresponse....", "---" + loginS);
                JSONSenderVolleyearning(lstrmdt, url ,currentPage);

            } catch (JSONException ignored) {
            }
        }

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
    @Override
    public void onSeekProcessed() {

    }
    public class VideoPlayerConfig {
        //Minimum Video you want to buffer while Playing
        public static final int MIN_BUFFER_DURATION = 2000;
        //Max Video you want to buffer during PlayBack
        public static final int MAX_BUFFER_DURATION = 5000;
        //Min Video you want to buffer before start Playing it
        public static final int MIN_PLAYBACK_START_BUFFER = 1500;
        //Min video You want to buffer when user resumes video
        public static final int MIN_PLAYBACK_RESUME_BUFFER = 2000;
    }

    //here it set the player data
    @SuppressLint("ClickableViewAccessibility")
    public void Set_Player(final int currentPage){
       // Log.d("current possss",":::::"+tags.size()+"::::"+tags.get(currentPage).getFile());
        if (tags.size()!=0 && tags.get(currentPage).getFile()!=null) {
            Homescreen_model item = tags.get(currentPage);
            loading11 = false;
            unliketype = item.getLiketype();
            claimtype = item.getClaim();
            videotype=item.getType();
            commentvideocount=item.getComments();
            soundname = item.getSoundname();
            soundurl = item.getSoundurl();
            soundid = item.getSoundid();

            View layout = layoutManager.findViewByPosition(currentPage);
            final PlayerView playerView = layout.findViewById(R.id.home_video);
            final ImageView imageview = layout.findViewById(R.id.home_imageview);
            final ImageView likes = layout.findViewById(R.id.likes);
            final ImageView unlike = layout.findViewById(R.id.unlikes);
            final TextView liketxt = layout.findViewById(R.id.liketxt);
            final ImageView lottieAnimationView = layout.findViewById(R.id.loteanimation);
            homecommenttxt=layout.findViewById(R.id.comment);
            final RelativeLayout mainlayout = layout.findViewById(R.id.root);
            final ImageView commenticon=layout.findViewById(R.id.commenticon);
            TextView description=layout.findViewById(R.id.homecontent);
            LinearLayout soundlay=layout.findViewById(R.id.soundlay);
            TextView song = layout.findViewById(R.id.song);
            p_bar=layout.findViewById(R.id.p_bar);
            ImageView shareimg=(ImageView) layout.findViewById(R.id.shareimg);
            TextView sharetxt=layout.findViewById(R.id.share);
            ImageView vastore=layout.findViewById(R.id.vastore);



            LoadControl loadControl = new DefaultLoadControl.Builder()
                    .setAllocator(new DefaultAllocator(true, 16))
                    .setBufferDurationsMs(VideoPlayerConfig.MIN_BUFFER_DURATION,
                            VideoPlayerConfig.MAX_BUFFER_DURATION,
                            VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                            VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER)
                    .setTargetBufferBytes(-1)
                    .setPrioritizeTimeOverSizeThresholds(true).createDefaultLoadControl();

            //here it sets the data for player
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
            //DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            final SimpleExoPlayer player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector,loadControl);
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                    Util.getUserAgent(getActivity(), "TikTok"));
            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(item.getFile()));


            player.prepare(videoSource);
            player.setRepeatMode(Player.REPEAT_MODE_ALL);
            player.addListener(this);

            player.addListener(new ExoPlayer.EventListener() {
                @Override
                public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
                    dur = player.getDuration();
                   // Log.e("dur", "::::" + dur+":::"+hometoken);
                    if (dur > 0) {
                        sm.sendData1(String.valueOf(dur), claimtype, climedhome);
                       // Log.e("duration", String.valueOf(sm));
                        } else {
                        progressBarView.setProgress(0);
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                    }
                }
                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }
                @Override
                public void onLoadingChanged(boolean isLoading) {

                }
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                    if (playbackState == Player.STATE_IDLE || playbackState == Player.STATE_ENDED ||
                            !playWhenReady) {

                        playerView.setKeepScreenOn(true);
                    } else { // STATE_IDLE, STATE_ENDED
                        // This prevents the screen from getting dim/lock
                        playerView.setKeepScreenOn(true);
                    }
                    if (playbackState == Player.STATE_READY){
                        int height=0;
                        if(player.getVideoFormat()!=null){
                            height = player.getVideoFormat().height;
                        }
                       // Log.e("height", "::::" + height);
                        if(item.getFile().contains(".mp4")) {
                           // Log.e("if mp4","");
                            if (height > 900) {
                                //Log.e("if mp4 if ","1000>");
                                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT);
                            } else {
                               // Log.e("if mp4 if ","1000<");
                                playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIT);
                            }
                        }
                    }

                }
                @Override
                public void onRepeatModeChanged(int repeatMode) {
                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }
                @Override
                public void onPlayerError(ExoPlaybackException error) {
                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }
                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }
                @Override
                public void onSeekProcessed() {

                }
            });

            description.setText(item.getDescription());
            //this is hashtag clicking.
            HashTagHelper.Creator.create(getResources().getColor(R.color.dark_grey), new HashTagHelper.OnHashTagClickListener() {
                @Override
                public void onHashTagClicked(String hashTag) {
                    onPause();
                    if (!hometoken.equals("null")) {
                        OpenHashtag(hashTag, item);
                    }
                }
            }).handle(description);


            liketxt.setText(tags.get(currentPage).getLikes());
            homecommenttxt.setText(commentvideocount);
            sharetxt.setText(item.getShare());
            if (soundurl!=null){
                soundlay.setVisibility(View.VISIBLE);
                song.setText(soundname + "-" + "\uD83C\uDFB5Use Audio\uD83C\uDFB5"+" "+" "+soundname+ "-" +"\uD83C\uDFB5Use Audio\uD83C\uDFB5 .");
                song.setSelected(true);
            }else {
                soundlay.setVisibility(View.INVISIBLE);
                song.setText("Original Sound - Ski_ssj" + "  " + "Original Sound - Ski_ssj");
                song.setSelected(true);
            }
            song.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), OriginalSoundDisplay.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("name",soundname);
                    intent.putExtra("soundid",soundid);
                    intent.putExtra("dynamiclink","null");
                    startActivity(intent);
                }
            });
            if (item.getType().equals("0")) {
                // Picasso.with(getActivity()).load(item.getFile()).into(imageview);
                Glide.with(getActivity())
                        .load(item.getFile())
                        .into(imageview);

                sm.sendData1("nodata", "", climedhome);
               // Log.e("duration", String.valueOf(sm));
                //this is image setlistener
                imageview.setOnTouchListener(new View.OnTouchListener() {
                    private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                            super.onFling(e1, e2, velocityX, velocityY);
                            float deltaX = e1.getX() - e2.getX();
                            float deltaXAbs = Math.abs(deltaX);
                            // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe

                            return true;
                        }
                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {
                            super.onSingleTapUp(e);
                            return true;
                        }
                        //here it displays the reportpopup while clicks the longpress
                        @Override
                        public void onLongPress(MotionEvent e) {
                            super.onLongPress(e);
                            // Show_video_option(item);
                           // Log.d("longpresss",":::::");
                            if (hometoken.equals("null")){
                                popup();
                            }else {
                                String postid = item.getPostid();
                                reportpopup(postid);
                            }
                        }
                        //it select the like while clcikng the double tap
                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            if (hometoken.equals("null")) {
                                //popup();
                            } else {

                                int x = (int) e.getX()-100;
                                int y = (int) e.getY()-100;
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                                final ImageView iv = new ImageView(getActivity());
                                lp.setMargins(x, y, 0, 0);
                                iv.setLayoutParams(lp);
                                if (unliketype.equals("true")) {
                                    iv.setImageDrawable(getResources().getDrawable(
                                            R.drawable.doubletaplike));
                                } else {

                                    String loginS = "{\"postId\":\"" + item.getPostid() + "\"}";
                                   // Log.d("jsnresponse login", "---" + loginS);
                                    String url = likesend_url;
                                    JSONObject lstrmdt;

                                    try {
                                        lstrmdt = new JSONObject(loginS);
                                        //Log.d("jsnresponse....", "---" + loginS);
                                        JSONSenderVolleylikessend(lstrmdt, url, currentPage, liketxt, likes, unlike,lottieAnimationView);

                                        iv.setImageDrawable(getResources().getDrawable(
                                                R.drawable.doubletaplike));
                                        unliketype="true";

                                    } catch (JSONException ignored) {
                                    }
                                }

                                mainlayout.addView(iv);
                                Animation fadeoutani = AnimationUtils.loadAnimation(getActivity(),R.anim.zoomin);

                                fadeoutani.setAnimationListener(new Animation.AnimationListener() {
                                    @Override
                                    public void onAnimationStart(Animation animation) {

                                    }
                                    @Override
                                    public void onAnimationEnd(Animation animation) {

                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mainlayout.removeView(iv);

                                            }
                                        }, 10);

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animation animation) {

                                    }
                                });
                                iv.startAnimation(fadeoutani);

                            }

                            return super.onDoubleTap(e);

                        }
                    });

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        gestureDetector.onTouchEvent(event);
                        return true;
                    }
                });
            } else {
                playerView.setVisibility(View.VISIBLE);
                playerView.setPlayer(player);
               // Log.d("plaerduration", ":::::");
            }
            player.setPlayWhenReady(true);
            privious_player = player;

            if (unliketype.equals("true")) {
                unlike.setVisibility(View.VISIBLE);
                likes.setVisibility(View.GONE);
            }
            else {
                unlike.setVisibility(View.GONE);
                likes.setVisibility(View.VISIBLE);
            }
            //click the like icon to call the likepost method
            likes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hometoken.equals("null")) {
                        popup();
                    } else {
                        String loginS = "{\"postId\":\"" + item.getPostid() + "\"}";
                       // Log.d("jsnresponse login", "---" + loginS);
                        String url = likesend_url;
                        JSONObject lstrmdt;
                        try {
                            lstrmdt = new JSONObject(loginS);
                            //Log.d("jsnresponse....", "---" + loginS);
                            JSONSenderVolleylikessend(lstrmdt, url, currentPage, liketxt, likes, unlike, lottieAnimationView);
                            unliketype="true";

                        } catch (JSONException ignored) {
                        }
                    }
                }
            });
            //click the unlike icon to call the unlikepost method
            unlike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hometoken.equals("null")) {
                        popup();
                    } else {
                        String loginS = "{\"_id\":\"" + item.getPostid() + "\"}";
                       // Log.d("jsnresponse login", "---" + loginS);
                        String url = unlike_url;
                        JSONObject lstrmdt;

                        try {
                            lstrmdt = new JSONObject(loginS);
                            //Log.d("jsnresponse....", "---" + loginS);

                            JSONSenderVolleylikessend(lstrmdt, url, currentPage, liketxt, likes, unlike,lottieAnimationView);
                            unliketype="false";

                        } catch (JSONException ignored) {
                        }
                    }

                }
            });
            //comment

            //click the comment icon to intent the comment class
            commenticon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

/*
              CommentsFragment bottomSheetDialogFragment = new CommentsFragment();
                bottomSheetDialogFragment.show(context, bottomSheetDialogFragment.getTag());
*/

//                getcomments=dataObjectList.get(position).getComments();
//                postid=dataObjectList.get(position).getPostid();

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    if (hometoken.equals("null")){
                        popup();
                    }else {
                        privious_player.setPlayWhenReady(false);
                        if (countDownTimer!=null) {
                            countDownTimer.cancel();
                        }
                        //  Fragment_Data_Send fragment_data_send= (Fragment_Data_Send) context;
                        OpenComment(item);

                    }
                }
            });

            shareimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hometoken.equals("null")){
                        popup();
                    }else {
                        privious_player.setPlayWhenReady(false);
                        if (countDownTimer!=null) {
                            countDownTimer.cancel();
                        }

                        final VideoAction_F fragment = new VideoAction_F(item.getFile(), item.getPostid(), item.getUsername(),new Fragment_Callback() {
                            @Override
                            public void Responce(Bundle bundle) {
                                if (bundle.getString("action").equals("save")) {
                                    Save_Video(item);
                                }
                            }

                            @Override
                            public void onDataSent(String sharecount) {
                                sharetxt.setText(sharecount);
                                tags.get(currentPage).setShare(sharecount);
                            }
                        });
                        fragment.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), "");
                        //Save_Video(item);
                    }
                }
            });

            //vastore click
            vastore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                        return;
                    }
                    mLastClickTime = SystemClock.elapsedRealtime();

                    if (hometoken.equals("null")) {
                        popup();
                    } else {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        popupreport("Coming Soon");
/*
                        Intent intent = new Intent(context, VAStoreActivity.class);
                        context.startActivity(intent);
*/
                    }
                }
            });

            playerView.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        super.onFling(e1, e2, velocityX, velocityY);
                        float deltaX = e1.getX() - e2.getX();
                        float deltaXAbs = Math.abs(deltaX);
                        // Only when swipe distance between minimal and maximal distance value then we treat it as effective swipe

                        return true;
                    }

                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        super.onSingleTapUp(e);
                        if (hometoken.equals("null")){
                            if (!player.getPlayWhenReady()) {
                                privious_player.setPlayWhenReady(true);
                            }else {
                                privious_player.setPlayWhenReady(false);
                                //Log.d("click stop", "::::");
                            }
                        }else {
                            if (!player.getPlayWhenReady()) {
                                privious_player.setPlayWhenReady(true);
                                //Log.d("click start", "::::");
                                if (claimedtopnav.equals("true")) {
                                    progressBarView.setProgress(100);
                                    if (countDownTimer != null) {
                                        countDownTimer.cancel();
                                    }
                                }else if (clamiedget.equals("true")) {
                                    progressBarView.setProgress(100);
                                    if (countDownTimer != null) {
                                        countDownTimer.cancel();
                                    }
                                }
                                else if (claimhometopnav.equals("false")) {
                                    progressBarView.setProgress(0);
                                    if (countDownTimer != null) {
                                        countDownTimer.start();
                                    }
                                } else if (claimhometopnav.equals("true")) {
                                    progressBarView.setProgress(100);
                                    if (countDownTimer != null) {
                                        countDownTimer.cancel();
                                    }
                                } else {
                                    if (countDownTimer != null) {
                                        countDownTimer.start();
                                    }
                                }
                            } else {
                                privious_player.setPlayWhenReady(false);
                                //Log.d("click stop", "::::");
                                if (claimedtopnav.equals("true")) {
                                    progressBarView.setProgress(100);
                                    if (countDownTimer != null) {
                                        countDownTimer.cancel();
                                    }
                                } else if (clamiedget.equals("true")) {
                                    progressBarView.setProgress(100);
                                    if (countDownTimer != null) {
                                        countDownTimer.cancel();
                                    }
                                } else if (claimhometopnav.equals("false")) {
                                    progressBarView.setProgress(0);
                                    if (countDownTimer != null) {
                                        countDownTimer.start();
                                    }
                                } else if (claimhometopnav.equals("true")) {
                                    progressBarView.setProgress(100);
                                    if (countDownTimer != null) {
                                        countDownTimer.cancel();
                                    }
                                } else {
                                    if (countDownTimer != null) {
                                        countDownTimer.cancel();
                                    }
                                }
                            }

                        }
                        return true;
                    }

                    @Override
                    public void onLongPress(MotionEvent e) {
                        super.onLongPress(e);
                        // Show_video_option(item);
                        //Log.d("longpresss",":::::");
                        if (hometoken.equals("null")){
                            popup();
                        }else {
                            String postid = item.getPostid();
                            reportpopup(postid);
                        }
                    }

                    @Override
                    public boolean onDoubleTap(MotionEvent e) {

                        if (hometoken.equals("null")) {
                            //popup();
                        } else {

                            int x = (int) e.getX()-100;
                            int y = (int) e.getY()-100;
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT);
                            final ImageView iv = new ImageView(getActivity());
                            lp.setMargins(x, y, 0, 0);
                            iv.setLayoutParams(lp);
                            if (unliketype.equals("true")) {
                                iv.setImageDrawable(getResources().getDrawable(
                                        R.drawable.doubletaplike));
                            } else {

                                String loginS = "{\"postId\":\"" + item.getPostid() + "\"}";
                                //Log.d("jsnresponse login", "---" + loginS);
                                String url = likesend_url;
                                JSONObject lstrmdt;

                                try {
                                    lstrmdt = new JSONObject(loginS);
                                    //Log.d("jsnresponse....", "---" + loginS);
                                    JSONSenderVolleylikessend(lstrmdt, url, currentPage, liketxt, likes, unlike,lottieAnimationView);

                                    iv.setImageDrawable(getResources().getDrawable(
                                            R.drawable.doubletaplike));
                                    unliketype="true";

                                } catch (JSONException ignored) {
                                }
                            }

                            mainlayout.addView(iv);
                            Animation fadeoutani = AnimationUtils.loadAnimation(getActivity(),R.anim.zoomin);

                            fadeoutani.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }
                                @Override
                                public void onAnimationEnd(Animation animation) {

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mainlayout.removeView(iv);

                                        }
                                    }, 10);

                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                            iv.startAnimation(fadeoutani);

                        }

                        return super.onDoubleTap(e);

                    }
                });

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
        }

/*
        swipe_count++;
        if(swipe_count>4){
            Show_add();
            swipe_count=0;
        }
*/

    }
/*
    public void Show_add(){
        if(mInterstitialAd.isLoaded()){
            mInterstitialAd.show();
        }
    }
*/


    public boolean is_fragment_exits() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        is_visible_to_user=isVisibleToUser;
/*
        if(privious_player!=null && isVisibleToUser){
            privious_player.setPlayWhenReady(true);
        }else if(privious_player!=null && !isVisibleToUser){
            privious_player.setPlayWhenReady(false);
        }
*/
        if(privious_player!=null && isVisibleToUser){
            privious_player.setPlayWhenReady(true);
            if (claimedtopnav.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }else if (clamiedget.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
            else if (claimhometopnav.equals("false")) {
                progressBarView.setProgress(0);
                if (countDownTimer != null) {
                    countDownTimer.start();
                }
            } else if (claimhometopnav.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            } else {
                if (countDownTimer != null) {
                    countDownTimer.start();
                }
            }
        }else if(privious_player!=null && !isVisibleToUser) {
            privious_player.setPlayWhenReady(false);
            if (claimedtopnav.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            } else if (clamiedget.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            } else if (claimhometopnav.equals("false")) {
                progressBarView.setProgress(0);
                if (countDownTimer != null) {
                    countDownTimer.start();
                }
            } else if (claimhometopnav.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            } else {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
        }
    }


    // this is lifecyle of the Activity which is importent for play,pause video or relaese the player
    @Override
    public void onResume() {
        super.onResume();

        if((privious_player!=null && is_visible_to_user) && !is_fragment_exits() ) {
            privious_player.setPlayWhenReady(true);

            if (claimedtopnav.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            } else if (clamiedget.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            } else if (claimhometopnav.equals("false")) {
                progressBarView.setProgress(0);
                if (countDownTimer != null) {
                    countDownTimer.start();
                }
            } else if (claimhometopnav.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            } else {
                if (videotype.equals("null")){
                    if (countDownTimer != null) {
                        countDownTimer.start();
                    }
                }else {
                    if (videotype.equals("0")) {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                    } else {
                        if (countDownTimer != null) {
                            countDownTimer.start();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(privious_player!=null) {
            privious_player.setPlayWhenReady(false);
            if (claimedtopnav.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            } else if (clamiedget.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            } else if (claimhometopnav.equals("false")) {
                progressBarView.setProgress(0);
                if (countDownTimer != null) {
                    countDownTimer.start();
                }
            } else if (claimhometopnav.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            } else {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if(privious_player!=null){
            privious_player.setPlayWhenReady(false);
        }

        if(privious_player!=null) {
            privious_player.setPlayWhenReady(false);
            if (clamiedget.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            } else if (claimhometopnav.equals("false")) {
                progressBarView.setProgress(0);
                if (countDownTimer != null) {
                    countDownTimer.start();
                }
            } else if (claimhometopnav.equals("true")) {
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            } else {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(privious_player!=null){
            privious_player.stop();
            privious_player.release();
        }
    }

    static public void killerplyer(){
        if (privious_player!=null){
            privious_player.release();
        }
    }
    private void OpenHashtag(String tag, Homescreen_model item) {
        String userprofilepic=item.getUserprofilepic();
        //Log.d("tag","::::"+tag);

        String tagsname=tag;
        Intent intent = new Intent(getActivity(), HashTagsDisplay.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("hashtag",tagsname);
        intent.putExtra("dynamiclink","null");
        startActivity(intent);
    }

    // this will open the comment screen
    private void OpenComment(Homescreen_model item) {
        String commentuser = tags.get(currentPage).getComments();
        String postid = item.getPostid();
        String type = item.getType();

        Fragment_Data_Send fragment_data_send=this;
        CommentsFragment fragment = new CommentsFragment(commentuser,postid,type,fragment_data_send);
        fragment.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), "bottom_sheet");
    }

    public void JSONSenderVolleylikessend(JSONObject lstrmdt, String url, final int position, final TextView liketxt, final ImageView likes, final ImageView unlike, final ImageView lottieAnimationView) {
       // Log.d("likedurll", "---" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       // Log.d("JSONSenderVolleylogin", "---" + response);
                        try {
                            if (response.length()!=0) {
                                String message = response.getString("message");

                                if (response.has("status")) {
                                    String count = response.getString("count");
                                    String status = response.getString("status");

                                    if (status.equals("4")) {
                                        tags.get(position).setLikes(count);
                                        tags.get(position).setLiketype("true");
                                        liketxt.setText(count);
                                        unlike.setVisibility(View.VISIBLE);
                                        likes.setVisibility(View.GONE);
                                    } else if (status.equals("5")) {
                                        // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                        tags.get(position).setLikes(count);
                                        tags.get(position).setLiketype("false");
                                        liketxt.setText(count);
                                        likes.setVisibility(View.VISIBLE);
                                        unlike.setVisibility(View.GONE);
                                    }
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
                                //Log.d("body", "---" + body);
                                JSONObject obj = new JSONObject(body);
                                String id = null;
                                if (obj.has("id")) {
                                    id = obj.getString("id");
                                }

                                if (obj.has("errors")) {
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

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
                headers.put("Authorization",hometoken);

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

            }
        });

    }

    private void jsonearningpoints(String earningpoints_url, final int pos) {
        // prepare the Request
       // Log.d("earning points", earningpoints_url);
        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, earningpoints_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        //Log.d("Responses earning", response.toString());
                        if (response.length() != 0) {
                            // Iterate the inner "data" array
                            try {
                                String message=response.getString("message");
                                String status=response.getString("status");
                                if (status.equals("2")) {
                                    String amount=response.getString("vidAmount");
                                    clamlay.setBackgroundResource(R.drawable.roundedbtn);
                                    earningpoints.setText(amount+""+"%");
                                    claimtext.setVisibility(View.GONE);
                                    earningtxt.setVisibility(View.VISIBLE);
                                    earningtxt.setText("Earnings");
                                    //  if (message.equals("Balance Claimed Successfully")){
                                    climedhome="false";
                                    // fn_countdown((int) dur);
                                    //  }
                                    //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

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
                                        //Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                                        //Log.d("bodyerror", "---" + bodyerror);
                                        JSONObject obj = new JSONObject(bodyerror);
                                        if (obj.has("errors")) {
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

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
                headers.put("Authorization",hometoken);

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

            }
        });
    }
    public void JSONSenderVolleyearning(JSONObject lstrmdt, String url , final int pos) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        //Log.d("555555", "login" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("JSONSenderVolleyclaim", "---" + response);
                        try {
                            if (response.length()!=0) {
                                String message = response.getString("message");
                                if (response.has("status")) {
                                    String status = response.getString("status");
                                    if (status.equals("1")) {
                                        String amount = response.getString("vidAmount");
                                        String claim = response.getString("claim");

                                        // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                        if (claim.equals("true")) {
                                            clamlay.setBackgroundResource(R.drawable.clamback);
                                            climedhome = "true";
                                            // claimedL.set(pos, "false");
                                            tags.get(pos).setClaim("false");
                                            progressBarView.setProgress(100);
                                            if (countDownTimer != null) {
                                                countDownTimer.cancel();
                                            }
                                            earningpoints.setText(amount + "" + "%");
                                            claimtext.setVisibility(View.VISIBLE);
                                            earningtxt.setVisibility(View.GONE);
                                            claimtext.setText("Claim");
                                            clamlay.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    jsonearningpoints(claimsmash_url, pos);
                                                }
                                            });
                                            earningtxt.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    jsonearningpoints(claimsmash_url, pos);
                                                }
                                            });
                                            earningpoints.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    jsonearningpoints(claimsmash_url, pos);
                                                }
                                            });
                                        }
                                        else {
                                            if (message.equals("Balance Updated")){
                                                clamlay.setBackgroundResource(R.drawable.roundedbtn);
                                                earningpoints.setText(amount + "" + "%");
                                                claimtext.setVisibility(View.GONE);
                                                earningtxt.setVisibility(View.VISIBLE);
                                                earningtxt.setText("Earnings");
                                                // claimedL.set(pos, "true");
                                                tags.get(pos).setClaim("true");
                                                progressBarView.setProgress(100);
                                                if (countDownTimer != null) {
                                                    countDownTimer.cancel();
                                                }
                                            }else {
                                                clamlay.setBackgroundResource(R.drawable.roundedbtn);
                                                earningpoints.setText(amount + "" + "%");
                                                claimtext.setVisibility(View.GONE);
                                                earningtxt.setVisibility(View.VISIBLE);
                                                earningtxt.setText("Earnings");
                                                // claimedL.set(pos, "true");
                                                tags.get(pos).setClaim("true");
                                                progressBarView.setProgress(0);
                                                if (countDownTimer != null) {
                                                    countDownTimer.start();
                                                }
                                            }
                                        }
                                    } else if (status.equals("3")) {
                                        desE.getText().clear();
                                        dialog.dismiss();
                                        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                        popupreport(message);
                                    }
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
                                //Log.d("body", "---" + body);
                                JSONObject obj = new JSONObject(body);
                                String id = null;
                                if (obj.has("id")) {
                                    id = obj.getString("id");
                                }
                                if (obj.has("errors")) {
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    // Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
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
                headers.put("Authorization",hometoken);

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

            }
        });

    }
    public <T> void addToRequestQueue(Request<T> req) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(getActivity());
        }
        mQueue.add(req);
    }
    //this is the guest popup
    public void popup() {
        android.app.AlertDialog.Builder builder;
        final Context mContext = getActivity();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.guestpopup, null);

        Button okbtn = (Button) layout.findViewById(R.id.okbtn);
        Button regbtn = (Button) layout.findViewById(R.id.register);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), LoginFragment.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });


        builder = new android.app.AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.d_round_white_background));
        dialog.show();
    }

    public void popupreport(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = getActivity();
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
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
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.d_round_white_background));
        dialog.show();

    }
    //this is the response popup
    public void fallowpopup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = getActivity();
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.responsepopup, null);


        Button okbtn = (Button) layout.findViewById(R.id.okbtn);
        TextView message = (TextView) layout.findViewById(R.id.msg);
        message.setText(msg);

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foryou.setBackgroundResource(R.drawable.homeback);
                following.setBackgroundResource(R.color.transparent);
                fadecolor.setVisibility(View.GONE);
                tags=new ArrayList<>();
                des=new ArrayList<>();
                homeadapter = new FragmentInner(tags,des,getActivity());
                recyclerView.setAdapter(homeadapter);

                homeapi(homepagination_url+0);
                //Load_add();

                dialog.dismiss();
            }
        });


        builder = new android.app.AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.d_round_white_background));
        dialog.show();

    }
    //this is the report popup
    public void reportpopup(final String postid) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = getActivity();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.reportpoup, null);

        Button nobtn = (Button) layout.findViewById(R.id.nobtn);
        Button reportbtn = (Button) layout.findViewById(R.id.reportbtn);
        reasonE = (Spinner) layout.findViewById(R.id.reason);
        desE = (EditText) layout.findViewById(R.id.des);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,reasonspinner);
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

        reportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItemText.equals("Select Reasons")){
                   // popupreport("please select the reasons");
                    Toast.makeText(getActivity(), "please select reasons", Toast.LENGTH_SHORT).show();

                }else {
                    String loginS = "{\"postId\":\"" + postid + "\",\"reason\":\"" + selectedItemText + "\",\"description\":\"" + desE.getText().toString() + "\"}";
                    //Log.d("jsnresponse reason", "---" + loginS);
                    String url = postreport_url;
                    JSONObject lstrmdt;

                    try {
                        lstrmdt = new JSONObject(loginS);
                        //Log.d("jsnresponse....", "---" + loginS);

                        JSONSenderVolleyearning(lstrmdt, url, currentPage);

                    } catch (JSONException ignored) {
                    }
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
        dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.d_round_white_background));
        dialog.show();
    }

    public void refresh() {
        refresh.clearAnimation();
        RotateAnimation anim = new RotateAnimation(30, 360, refresh.getWidth()/2, refresh.getHeight()/2);
        anim.setFillAfter(true);
        anim.setRepeatCount(0);
        anim.setDuration(1000);
        refresh.startAnimation(anim);
    }


    public void Save_Video(Homescreen_model item){
        Functions.Show_determinent_loader(getActivity(),false,false);
        PRDownloader.initialize(getActivity().getApplicationContext());
        DownloadRequest prDownloader= PRDownloader.download(item.getFile(), Environment.getExternalStorageDirectory() +"/Tittic/", item.getPostid()/*+"no_watermark"*/+".mp4")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        int prog=(int)((progress.currentBytes*100)/progress.totalBytes);
                        Functions.Show_loading_progress(prog/2);

                    }
                });
        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                Functions.cancel_determinent_loader();
                //Delete_file_no_watermark(item);
               // Scan_file(item);
                // Applywatermark(item);
                Functions.cancel_determinent_loader();
                Functions.Show_determinent_loader(getActivity(),false,false);
                //  Scan_file(resolveInfo);
                String path=Environment.getExternalStorageDirectory() +"/Tittic/"+item.getPostid()+".mp4";
                // defaultVideo=Environment.getExternalStorageDirectory() +"/Tittic/watermark/"+postid+".mp4";

                try {
                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + item.getPostid()+".mp4");

                    defaultVideo=f.getAbsolutePath();
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // String[] imageCommand =  { "-y", "-i", path, "-y", "-i", saveAppIcon(), "-filter_complex", "overlay=x=20:y=20", "-strict", "experimental", defaultVideo};
                ///String[]  imageCommand = {"-i",  path,  "-vf", "drawtext=fontfile=/system/fonts/DroidSans.ttf:text='SiteName hulluway':fontsize=30:fontcolor=white: x=20:y=20", "-acodec", "copy", "-y",  defaultVideo};

                String[] imageCommand =  { "-y", "-i", path, "-y", "-i", saveAppIcon(), "-filter_complex", "overlay=x=20:y=20,drawtext=fontfile=/system/fonts/DroidSans.ttf:text='"+item.getUsername()+"':fontsize=18:fontcolor=white: x=20:y=80", "-strict", "experimental","-preset", "ultrafast", defaultVideo};


                execFFmpegBinary(imageCommand,new File(path),item);

            }
            @Override
            public void onError(Error error) {
                Delete_file_no_watermark(item);
                //Log.d("error","::::"+error);
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                Functions.cancel_determinent_loader();
            }
        });
    }


    public void Scan_file(Homescreen_model item){
        MediaScannerConnection.scanFile(getActivity(),
                new String[] { defaultVideo },
                null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                       // Log.i("ExternalStorage", "Scanned " + path + ":");
                       // Log.i("ExternalStorage", "-> uri=" + uri);


                    }
                });
    }

    public void Delete_file_no_watermark(Homescreen_model item){
        File file=new File(Environment.getExternalStorageDirectory() +"/SmashitLive/"+item.getPostid()+"no_watermark"+".mp4");
        if(file.exists()){
            file.delete();
        }
    }


    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
                //Log.d("", "ffmpeg : era nulo");
                ffmpeg = FFmpeg.getInstance(getActivity());
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {

                }

                @Override
                public void onSuccess() {
                   // Log.d(TAG, "ffmpeg : correct Loaded");
                }
            });
        } catch (FFmpegNotSupportedException e) {

        } catch (Exception e) {
           // Log.d(TAG, "EXception no controlada : " + e);
        }
    }


    private void execFFmpegBinary(final String[] command, File file, Homescreen_model item) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//use one of overloaded setDataSource() functions to set your data source
            retriever.setDataSource(getActivity(), Uri.fromFile(file));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long totalDur = Long.parseLong(time );
            retriever.release();
            if (totalDur != 0) {
/*
                            float progress = (Integer.parseInt(matchSplit[0]) * 3600 +
                                    Integer.parseInt(matchSplit[1]) * 60 +
                                    Float.parseFloat(matchSplit[2])) / totalDur;
                            float showProgress = (progress * 1000);
                            Log.d(TAG, "=======PROGRESS======== "+progress+" showProgress  **** " + showProgress);
                            Functions.Show_loading_progress((int)(showProgress));
*/


                String timeInterval = String.valueOf(totalDur);
                int endTime = Integer.parseInt(timeInterval); // up to finish time
               // Log.d("onTick progress","::::"+endTime);


                countDownTimer = new CountDownTimer(endTime /** 1000*/, 1000) {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onTick(long millisUntilFinished) {
                       // Log.d("onTick progress","::::"+progress);
                        //setProgress(progress, endTime / 1000);
                        Functions.Show_loading_progress(progress);
                        progress=progress+1;
                    }
                    @Override
                    public void onFinish() {
                        //  Functions.Show_loading_progress((int)(endTime));
                        if (countDownTimerhome!=null){
                            countDownTimerhome.cancel();
                        }
                    }
                };
            }
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                   // Log.d("tag", "FAILED with output : " + s);
                    Functions.cancel_determinent_loader();
                }

                @Override
                public void onSuccess(String s) {
                    //Log.d("tag", "sucess with output : " + s);
                    Functions.cancel_determinent_loader();
                    if (countDownTimerhome!=null){
                        countDownTimerhome.cancel();
                    }
                    privious_player.setPlayWhenReady(true);
                    if (countDownTimer != null) {
                        countDownTimer.start();
                    }
                    Toast.makeText(getActivity(), "Saved Successfully", Toast.LENGTH_SHORT).show();

                    Scan_file(item);

                }

                @Override
                public void onProgress(String message) {
                    countDownTimer.start();
/*
                    Log.d("tag", "onProgress  progress1 " + message);
                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(command));
                    Log.d(TAG, "progress : " + message);
*/
                    Pattern timePattern = Pattern.compile("(?<=time=)[\\d:.]*");
                    Scanner sc = new Scanner(message);

                    String match = sc.findWithinHorizon(timePattern, 0);
                    if (match != null) {
                        String[] matchSplit = match.split(":");
                        // int totalDur=25;

                    }
                 /*   int start = message.indexOf("time=");
                    int end = message.indexOf(" bitrate");
                    if (start != -1 && end != -1) {
                        String duration = message.substring(start+5, end);
                        if (duration != "") {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                              //  dialog.setProgress((int)sdf.parse("1970-01-01 "+ duration).getTime());
                              //  Functions.Show_loading_progress((int)((progress*100)/2)+50);
                                Functions.Show_loading_progress((int)sdf.parse("1970-01-01 "+ duration).getTime());


                            }catch (ParseException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }*/


                }

                @Override
                public void onStart() {
                    //Log.d("tag", "Started command : ffmpeg " + command);



                }

                @Override
                public void onFinish() {
                    //Log.d("tag", "Finished command : ffmpeg " + command);


                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

    private String saveAppIcon(){
        String imagePath=null;
        try{
            Bitmap myLogo = ((BitmapDrawable)getActivity().getResources().getDrawable(R.drawable.watermarkicon)).getBitmap();
            Bitmap bitmap_resize=Bitmap.createScaledBitmap(myLogo, 150, 60, false);


            // Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.launchicon);

            //replace "R.drawable.bubble_green" with the image resource you want to share from drawable

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap_resize.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

            // you can create a new file name "test.jpg" in sdcard folder.
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "test.png");

            imagePath=f.getAbsolutePath();

            f.createNewFile();

            // write the bytes in file
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            // remember close de FileOutput
            fo.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Log.d("","imagePath  "+imagePath);
        return imagePath;


    }



}
