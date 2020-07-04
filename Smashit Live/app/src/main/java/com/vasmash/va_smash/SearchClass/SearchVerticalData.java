package com.vasmash.va_smash.SearchClass;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.material.appbar.AppBarLayout;
import com.vasmash.va_smash.BottmNavigation.TopNavigationview;
import com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment;
import com.vasmash.va_smash.HomeScreen.CommentScreen.Fragment_Data_Send;
import com.vasmash.va_smash.HomeScreen.homefragment.HashTagsDisplay;
import com.vasmash.va_smash.HomeScreen.homefragment.HomeFragment;
import com.vasmash.va_smash.HomeScreen.homefragment.OriginalSoundDisplay;
import com.vasmash.va_smash.ProfileScreen.ProfileActivity;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Trading;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.claimtext;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.clamlay;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.earningpoints;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.earningtxt;
import static com.vasmash.va_smash.ProfileScreen.ProfileActivity.createvideosclick;
import static com.vasmash.va_smash.VASmashAPIS.APIs.deletepost_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.earningpost_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.likesend_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.postreport_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.unlike_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.visibility_url;

public class SearchVerticalData extends AppCompatActivity implements Player.EventListener, Fragment_Data_Send {

    private RequestQueue mQueue;

    RecyclerView recyclerView;

    LinearLayoutManager layoutManager;

    FragmentSearchVertical homeadapter;
    ArrayList<Model_Trading> tags;
    int currentPage=-1;
    String proftoken;
    TextView homecommenttxt;
    private long mLastClickTime = 0;

    Dialog dialog;

    //report
    EditText desE;
    Spinner reasonE;
    String selectedItemText,status;
    String[] reasonspinner = {"Select Reasons","Inappropriate Content", " Abusive  Content"};
    static public String commentsearchclick="null";
    ArrayList<Model_Trading> data_list;
    ArrayList<String> fileclickL;

    public static String soundname,soundid,soundurl,searchimage,searchcount,searchlikecondition,searchtypeval,searchcomment,postid,username;

    String dynamiclink;
    int position;
    SimpleExoPlayer player;
    ImageView likes,unlike,lottieAnimationView,commenticon;
    TextView liketxt;
    String post_visibilty;
    private Toolbar toolbar;
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_vertical_data);

        mQueue = Volley.newRequestQueue(getApplicationContext());

        toolbar = findViewById(R.id.toolbar);
        appBarLayout=findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);
        //   toolbar.setBackgroundResource(R.color.toolback);
        try
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        catch (NullPointerException e){}

        if (createvideosclick.equals("true")){
            appBarLayout.setVisibility(View.VISIBLE);
            //createvideosclick="false";
        }else {
            appBarLayout.setVisibility(View.GONE);
        }

        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(SearchVerticalData.this);
        proftoken = phoneauthshard.getString("token", "null");

        recyclerView=findViewById(R.id.list);
        //here get the data from profile activity,otherprofile,hashtags and search data.
        if (getIntent().getExtras() != null) {
            Intent bundle = getIntent();
            position = bundle.getIntExtra("clikpos", 0);
            dynamiclink = bundle.getStringExtra("dynamiclink");
            fileclickL = bundle.getStringArrayListExtra("fileL");
            Log.d("fileclickL",":::"+fileclickL);

        }
        //here get the sharedpreference data from profile activity,otherprofile,hashtags and search data.
        SharedPreferences ph = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = ph.getString("arraydata", null);

        Type type=new TypeToken<ArrayList<Model_Trading>>(){}.getType();

        data_list=gson.fromJson(json,type);
        recyclarview(position);

    }
    public void recyclarview(int position){
        layoutManager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        SnapHelper snapHelper =  new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        homeadapter = new FragmentSearchVertical(data_list/*,deslist*//*shareclickL*/, this);
        recyclerView.setAdapter(homeadapter);

        // this is the scroll listener of recycler view which will tell the current item number
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
                int page_no=scrollOffset / height;

                if(page_no!=currentPage ){
                    currentPage=page_no;
                    Release_Privious_Player();
                    Set_Player(currentPage);
                }
            }
        });

        recyclerView.scrollToPosition(position);
    }
    SimpleExoPlayer privious_player;
    public void Release_Privious_Player(){
        if(privious_player!=null) {
            privious_player.removeListener(this);
            privious_player.release();
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
        Log.d("repeatemode","::::"+reason);
        //here it shows the complete video and it will call post api. Here it gives 1% for 1 video watching.
        String loginS = "{\"postId\":\"" + postid + "\"}";
        Log.d("jsnresponse earning ", "---" + loginS);
        String url = earningpost_url;
        JSONObject lstrmdt;
        try {
            lstrmdt = new JSONObject(loginS);
            Log.d("jsnresponse....", "---" + loginS);
            JSONSenderVolleylikessend(lstrmdt,url,currentPage, liketxt, likes, unlike);

        } catch (JSONException ignored) {
        }
    }
    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
    @Override
    public void onSeekProcessed() {

    }
    //here it set the player data
    @SuppressLint("ClickableViewAccessibility")
    public void Set_Player(final int currentPage){
        final Model_Trading item= data_list.get(currentPage);

        Log.d("currentPage","currentPage"+currentPage);
        searchimage = fileclickL.get(currentPage);
        //searchimage = data_list.get(currentPage).getImage();
        Log.d("searchimage","searchimage"+searchimage);
        searchtypeval = item.getType();
        postid = item.getId();
        //searchcount = likeclickL.get(currentPage);
        searchcount = data_list.get(currentPage).getCount();
        searchcomment = data_list.get(currentPage).getComment();
        //searchcomment = commentclickL.get(currentPage);
        //searchlikecondition = userlikeclickL.get(currentPage);
        searchlikecondition = data_list.get(currentPage).getLikescondition();

        soundname = item.getSoundname();
        soundurl = item.getSoundurl();
        soundid = item.getSoundid();


        View layout=layoutManager.findViewByPosition(currentPage);
        final PlayerView playerView=layout.findViewById(R.id.home_video);
        final ImageView imageview=layout.findViewById(R.id.home_imageview);
        likes=layout.findViewById(R.id.likes);
        unlike=layout.findViewById(R.id.unlikes);
        liketxt=layout.findViewById(R.id.liketxt);
        lottieAnimationView = layout.findViewById(R.id.loteanimation);
        commenticon=layout.findViewById(R.id.commenticon);
        homecommenttxt=layout.findViewById(R.id.comment);
        final RelativeLayout mainlayout = layout.findViewById(R.id.root);
        TextView description=layout.findViewById(R.id.homecontent);
        LinearLayout soundlay=layout.findViewById(R.id.soundlay);
        TextView song = layout.findViewById(R.id.song);


        LoadControl loadControl = new DefaultLoadControl.Builder()
                .setAllocator(new DefaultAllocator(true, 16))
                .setBufferDurationsMs(HomeFragment.VideoPlayerConfig.MIN_BUFFER_DURATION,
                        HomeFragment.VideoPlayerConfig.MAX_BUFFER_DURATION,
                        HomeFragment.VideoPlayerConfig.MIN_PLAYBACK_START_BUFFER,
                        HomeFragment.VideoPlayerConfig.MIN_PLAYBACK_RESUME_BUFFER)
                .setTargetBufferBytes(-1)
                .setPrioritizeTimeOverSizeThresholds(true).createDefaultLoadControl();

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        //DefaultTrackSelector trackSelector = new DefaultTrackSelector();

        player = ExoPlayerFactory.newSimpleInstance(SearchVerticalData.this, trackSelector,loadControl);
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(SearchVerticalData.this,
                Util.getUserAgent(SearchVerticalData.this, "TikTok"));

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse(searchimage));

        player.prepare(videoSource);

        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        player.addListener(this);

        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
                long realDurationMillis = player.getDuration();
                Log.d("timeinmills",":::::"+realDurationMillis);

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
                    Log.e("height", "::::" + height);
                    if(searchimage.contains(".mp4")) {
                        Log.e("if mp4","");
                        if (height > 900) {
                            Log.e("if mp4 if ","1000>");
                            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
                        } else {
                            Log.e("if mp4 if ","1000<");
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

        liketxt.setText(searchcount);
        homecommenttxt.setText(searchcomment);


        if (soundurl!=null){
            soundlay.setVisibility(View.VISIBLE);
            song.setText(soundname +"-"+ soundurl);
            song.setSelected(true);
        }else {
            soundlay.setVisibility(View.INVISIBLE);
            song.setText("Original Sound - Ski_ssj" + "  " + "Original Sound - Ski_ssj");
            song.setSelected(true);
        }

        song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchVerticalData.this, OriginalSoundDisplay.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("name",soundname);
                intent.putExtra("soundid",soundid);
                intent.putExtra("dynamiclink","null");
                startActivity(intent);
            }
        });

        description.setText(item.getDescription());
        //this is hashtag clicking.
        HashTagHelper.Creator.create(getResources().getColor(R.color.dark_grey), new HashTagHelper.OnHashTagClickListener() {
            @Override
            public void onHashTagClicked(String hashTag) {
                onPause();
                OpenHashtag(hashTag,item);
            }
        }).handle(description);


        if(searchtypeval.equals("0")){
            Picasso.with(SearchVerticalData.this).load(searchimage).into(imageview);
            //this is image setlistener
            imageview.setOnTouchListener(new View.OnTouchListener() {
                private GestureDetector gestureDetector = new GestureDetector(SearchVerticalData.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        super.onFling(e1, e2, velocityX, velocityY);
                        float deltaX = e1.getX() - e2.getX();
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
                        Log.d("longpresss",":::::");
                        reportpopup(postid, liketxt, likes, unlike,lottieAnimationView);
                    }
                    //it select the like while clcikng the double tap
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        int x = (int) e.getX()-100;
                        int y = (int) e.getY()-100;
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT);
                        final ImageView iv = new ImageView(SearchVerticalData.this);
                        lp.setMargins(x, y, 0, 0);
                        iv.setLayoutParams(lp);
                        if (searchlikecondition.equals("true")) {
                            iv.setImageDrawable(getResources().getDrawable(
                                    R.drawable.doubletaplike));
                        } else {

                            String loginS = "{\"postId\":\"" + postid + "\"}";
                            Log.d("jsnresponse login", "---" + loginS);
                            String url = likesend_url;
                            JSONObject lstrmdt;

                            try {
                                lstrmdt = new JSONObject(loginS);
                                Log.d("jsnresponse....", "---" + loginS);
                                JSONSenderVolleylikessend(lstrmdt, url, currentPage, liketxt, likes, unlike);

                                iv.setImageDrawable(getResources().getDrawable(
                                        R.drawable.doubletaplike));
                                searchlikecondition="true";

                            } catch (JSONException ignored) {
                            }
                        }

                        mainlayout.addView(iv);
                        Animation fadeoutani = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoomin);

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
        else{
            playerView.setVisibility(View.VISIBLE);
            playerView.setPlayer(player);

            Log.d("plaerduration",":::::");
        }

        player.setPlayWhenReady(true);
        privious_player=player;


        if (searchlikecondition.equals("true")){
            unlike.setVisibility(View.VISIBLE);
            likes.setVisibility(View.GONE);

        }else {
            unlike.setVisibility(View.GONE);
            likes.setVisibility(View.VISIBLE);
        }
        //click the like icon to call the likepost method
        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proftoken.equals("null")){
                    // popup();
                }else {
                    String loginS = "{\"postId\":\"" + postid + "\"}";
                    Log.d("jsnresponse login", "---" + loginS);
                    String url = likesend_url;
                    JSONObject lstrmdt;

                    try {
                        lstrmdt = new JSONObject(loginS);
                        Log.d("jsnresponse....", "---" + loginS);
                        JSONSenderVolleylikessend(lstrmdt, url, currentPage,liketxt,likes,unlike);
                        searchlikecondition="true";

                    } catch (JSONException ignored) {
                    }
                }
            }
        });
        //click the unlike icon to call the unlikepost method
        unlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (proftoken.equals("null")){
                    //  popup();
                }else {
                    String loginS = "{\"_id\":\"" + postid + "\"}";
                    Log.d("jsnresponse login", "---" + loginS);
                    String url = unlike_url;
                    JSONObject lstrmdt;

                    try {
                        lstrmdt = new JSONObject(loginS);
                        Log.d("jsnresponse....", "---" + loginS);

                        JSONSenderVolleylikessend(lstrmdt, url, currentPage, liketxt, likes, unlike);
                        searchlikecondition="false";

                    } catch (JSONException ignored) {
                    }
                }

            }
        });
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

/*
                privious_player.setPlayWhenReady(false);
                if (countDownTimer!=null) {
                    countDownTimer.cancel();
                }
*/
                //  Fragment_Data_Send fragment_data_send= (Fragment_Data_Send) context;
                OpenComment();
            }
        });


        playerView.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(SearchVerticalData.this, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    super.onFling(e1, e2, velocityX, velocityY);
                    float deltaX = e1.getX() - e2.getX();
                    return true;
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    super.onSingleTapUp(e);
                    if(!player.getPlayWhenReady()){
                        privious_player.setPlayWhenReady(true);
                    }else{
                        privious_player.setPlayWhenReady(false);
                    }


                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);
                    // Show_video_option(item);
                    reportpopup(postid, liketxt, likes, unlike,lottieAnimationView);
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    int x = (int) e.getX()-100;
                    int y = (int) e.getY()-100;
                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    final ImageView iv = new ImageView(SearchVerticalData.this);
                    lp.setMargins(x, y, 0, 0);
                    iv.setLayoutParams(lp);
                    if (searchlikecondition.equals("true")) {
                        iv.setImageDrawable(getResources().getDrawable(
                                R.drawable.doubletaplike));
                    } else {

                        String loginS = "{\"postId\":\"" + postid + "\"}";
                        Log.d("jsnresponse login", "---" + loginS);
                        String url = likesend_url;
                        JSONObject lstrmdt;

                        try {
                            lstrmdt = new JSONObject(loginS);
                            Log.d("jsnresponse....", "---" + loginS);
                            JSONSenderVolleylikessend(lstrmdt, url, currentPage, liketxt, likes, unlike);

                            iv.setImageDrawable(getResources().getDrawable(
                                    R.drawable.doubletaplike));
                            searchlikecondition="true";

                        } catch (JSONException ignored) {
                        }
                    }

                    mainlayout.addView(iv);
                    Animation fadeoutani = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoomin);

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

    @Override
    public void onResume() {
        super.onResume();
        if((privious_player!=null)){
            privious_player.setPlayWhenReady(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(privious_player!=null){
            privious_player.setPlayWhenReady(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(privious_player!=null){
            privious_player.setPlayWhenReady(false);
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
    // this will open the comment screen
    private void OpenComment() {
        commentsearchclick="true";
        Fragment_Data_Send fragment_data_send=this;
        CommentsFragment fragment = new CommentsFragment(searchcomment,postid,searchtypeval,fragment_data_send);
        fragment.show(((AppCompatActivity) this).getSupportFragmentManager(), "bottom_sheet");

    }

    private void OpenHashtag(String tag, Model_Trading item) {

        String tagsname=tag;
        Intent intent = new Intent(SearchVerticalData.this, HashTagsDisplay.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("hashtag",tagsname);
        intent.putExtra("dynamiclink","null");
        startActivity(intent);
    }

    public void JSONSenderVolleylikessend(JSONObject lstrmdt, String url, final int position, final TextView liketxt, final ImageView likes, final ImageView unlike) {
        // Log.d("---reqotpurl-----", "---" + login_url);
        Log.d("555555", "login" + url);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest (Request.Method.POST, url,lstrmdt,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("JSONSenderVolleylogin", "---" + response);
                        try {
                            if (response.length()!=0) {
                                String message = response.getString("message");
                                if (response.has("status")) {
                                    status = response.getString("status");
                                    if (status.equals("3")) {
                                        desE.getText().clear();
                                        dialog.dismiss();
                                        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                                        fallowpopup(message);
                                    }
                                    else if (status.equals("1")) {
                                        String amount = response.getString("vidAmount");
                                        String claim = response.getString("claim");
                                        clamlay.setBackgroundResource(R.drawable.roundedbtn);
                                        earningpoints.setText(amount+""+"%");
                                        claimtext.setVisibility(View.GONE);
                                        earningtxt.setVisibility(View.VISIBLE);
                                        earningtxt.setText("Earnings");
                                        // claimedL.set(position,"true");
                                    }
                                }
                                else if (response.has("count")) {
                                    String count = response.getString("count");
                                    if (message.equals("liked successfully")) {
                                        //likeclickL.set(position, count);
                                        data_list.get(position).setCount(count);
                                        data_list.get(position).setLikescondition("true");
                                        liketxt.setText(count);
                                        unlike.setVisibility(View.VISIBLE);
                                        likes.setVisibility(View.GONE);
                                    } else if (message.equals("Unliked successfully")) {
                                        //likeclickL.set(position, count);
                                        // userlikeclickL.set(position,"false");
                                        data_list.get(position).setCount(count);
                                        data_list.get(position).setLikescondition("false");

                                        liketxt.setText(count);
                                        likes.setVisibility(View.VISIBLE);
                                        unlike.setVisibility(View.GONE);
                                    }
                                }
                                else if (message.equals("Post Updated Successfully")){
                                    dialog.dismiss();
                                }
                                else if (message.equals("Post Deleted successfully")){
                                    fileclickL.remove(currentPage);
                                    data_list.remove(currentPage);
                                    Log.d("remoefile","::::"+fileclickL);
/*
                                    Intent intent=new Intent(SearchVerticalData.this, ProfileActivity.class);
                                    startActivity(intent);
*/
                                    homeadapter.notifyDataSetChanged();
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
                                Log.d("body", "---" + body);
                                JSONObject obj = new JSONObject(body);
                                String id = null;
                                if (obj.has("id")) {
                                    id = obj.getString("id");
                                }

                                if (obj.has("errors")) {
                                    JSONObject errors = obj.getJSONObject("errors");
                                    String message = errors.getString("message");
                                    // Toast.makeText(SearchVerticalData.this, message, Toast.LENGTH_SHORT).show();

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
                headers.put("Authorization",proftoken);

                //return (headers != null || headers.isEmpty()) ? headers : super.getHeaders();
                return headers;
            }
        };
        jsonObjReq.setTag("");
        addToRequestQueue(jsonObjReq);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(SearchVerticalData.this);
        }
        mQueue.add(req);

    }

    public void reportpopup(String postid, TextView liketxt, ImageView likes, ImageView unlike, ImageView lottieAnimationView) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.reportpoup, null);

        Button nobtn = (Button) layout.findViewById(R.id.nobtn);
        Button reportbtn = (Button) layout.findViewById(R.id.reportbtn);
        reasonE = (Spinner) layout.findViewById(R.id.reason);
        desE = (EditText) layout.findViewById(R.id.des);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,reasonspinner);
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
                String loginS = "{\"postId\":\"" + postid + "\",\"reason\":\"" + selectedItemText + "\",\"description\":\"" + desE.getText().toString() + "\"}";
                Log.d("jsnresponse reason", "---" + loginS);
                String url = postreport_url;
                JSONObject lstrmdt;

                try {
                    lstrmdt = new JSONObject(loginS);
                    Log.d("jsnresponse....", "---" + loginS);

                    JSONSenderVolleylikessend(lstrmdt,url,currentPage, liketxt, likes, unlike);

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


    public void fallowpopup(String msg) {
        android.app.AlertDialog.Builder builder;
        final Context mContext = this;
        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.responsepopup, null);


        Button okbtn = (Button) layout.findViewById(R.id.okbtn);
        TextView message = (TextView) layout.findViewById(R.id.msg);
        message.setText(msg);

        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
/*
                Intent intent=new Intent(getActivity(), OtherprofileActivity.class);
                startActivity(intent);
*/
            }
        });


        builder = new android.app.AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();

    }

    public void finishActivity(View v) {
        Log.d("dynamiclink",":::"+dynamiclink);
        if (dynamiclink != null) {
            Intent intent = new Intent(SearchVerticalData.this, TopNavigationview.class);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            finish();

        }
    }

    @Override
    public void onDataSent(String yourData) {
        Log.d("cmmentdata",":::"+yourData);
        homecommenttxt.setText(yourData);
        // commentclickL.set(currentPage, yourData);
        data_list.get(currentPage).setComment(yourData);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.userdelete);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        return onOptionsItemSelected(item);
                    }
                });
        return true;
    }

    //this is the usersettings
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //this is the delete the user post
            case R.id.delete:
/*
                Intent settingintent = new Intent(ProfileActivity.this, AccountSettings.class);
                settingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingintent);
*/
                String loginS = "{\"_id\":\"" + postid + "\"}";
                Log.d("jsnresponse login", "---" + loginS);
                String url = deletepost_url;
                JSONObject lstrmdt;

                try {
                    lstrmdt = new JSONObject(loginS);
                    Log.d("jsnresponse....", "---" + loginS);
                    JSONSenderVolleylikessend(lstrmdt, url, currentPage,liketxt,likes,unlike);

                } catch (JSONException ignored) {
                }
                return true;
            //this is the user change the privacy
            case R.id.viewsmenu:
                popupviews();
                return true;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void popupviews() {

        android.app.AlertDialog.Builder builder;
        final Context mContext = SearchVerticalData.this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.viewslistpopup, null);

        CheckBox check0=(CheckBox)layout.findViewById(R.id.checkBox0);
        CheckBox check1=(CheckBox)layout.findViewById(R.id.checkBox1);
        CheckBox check2=(CheckBox)layout.findViewById(R.id.checkBox2);
        ImageView closebtn=layout.findViewById(R.id.close);
/*
        check0.setChecked(true);
        post_visibilty="0";
*/


        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        check0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    check1.setChecked(false);
                    check2.setChecked(false);
                    post_visibilty="0";
                    status="Followers";
                    viewspost(post_visibilty);
                }
            }
        });
        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    check0.setChecked(false);
                    check2.setChecked(false);
                    post_visibilty="1";
                    status="Public";
                    viewspost(post_visibilty);

                }
            }
        });
        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    check1.setChecked(false);
                    check0.setChecked(false);
                    post_visibilty="2";
                    status="Private";
                    viewspost(post_visibilty);
                }
            }
        });

        builder = new android.app.AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
        dialog.show();

    }
    public void viewspost(String post_visibilty){
        String loginS = "{\"_id\":\"" + postid + "\",\"visibility\":\""+post_visibilty+"\"}";
        Log.d("jsnresponse visibility", "---" + loginS);
        String url = visibility_url;
        JSONObject lstrmdt;

        try {
            lstrmdt = new JSONObject(loginS);
            Log.d("jsnresponse....", "---" + loginS);
            JSONSenderVolleylikessend(lstrmdt, url, currentPage,liketxt,likes,unlike);

        } catch (JSONException ignored) {
        }

    }

}
