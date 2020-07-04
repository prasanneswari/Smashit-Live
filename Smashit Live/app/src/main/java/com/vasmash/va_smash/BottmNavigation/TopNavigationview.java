package com.vasmash.va_smash.BottmNavigation;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.vasmash.va_smash.HomeScreen.homefragment.HomeFragment;
import com.vasmash.va_smash.LoadingClass.ViewDialog;
import com.vasmash.va_smash.ProfileScreen.ProfileActivity;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.SearchData;
import com.vasmash.va_smash.createcontent.CameraActivity;
import com.vasmash.va_smash.login.fragments.LoginFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.animation.Animation.RELATIVE_TO_SELF;
import static com.vasmash.va_smash.HomeScreen.homefragment.HomeFragment.killerplyer;
import static com.vasmash.va_smash.VASmashAPIS.APIs.claimsmash_url;
import static com.vasmash.va_smash.VASmashAPIS.APIs.earningpoints_url;

public class TopNavigationview extends AppCompatActivity implements  HomeFragment.SendMessage1 {
    Dialog dialog;
    private ViewPager viewPager;
    MenuItem prevMenuItem;
    static public ImageView search,create;
    static public CircleImageView earningpic;
    static public TextView earningpoints,foryou,following,earningtxt,claimtext;
    static public LinearLayout clamlay;
    RelativeLayout linecreate;

    int myProgress = 0;
    static public int endTime = 0;//250
    static public int progress;

    static public ProgressBar progressBarView;
    static public CountDownTimer countDownTimer;
    private RequestQueue mQueue;
    String token,profilepic="null";
    static public String guestvalues="null";
    static public String coinsPerVid="null";
    static public String clamiedget="null";
    int millis;
    static public String claimedtopnav="null",claimhometopnav="null";
    String  backprofile="null";

    String profilePic="null";
    ViewDialog viewDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this will remove title
        setContentView(R.layout.activity_top_navigationview);
        earningpic=findViewById(R.id.earningpic);
        search=findViewById(R.id.searchimg);
        create=findViewById(R.id.create);
        linecreate=findViewById(R.id.lin_id);
        earningpoints=findViewById(R.id.earningpionts);
        foryou=findViewById(R.id.foryou);
        following=findViewById(R.id.following);
        clamlay=findViewById(R.id.barlay);
        earningtxt=findViewById(R.id.earningtxt);
        claimtext=findViewById(R.id.claimtxt);
        mQueue = Volley.newRequestQueue(getApplicationContext());
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(this);
        token = phoneauthshard.getString("token", "null");
        profilepic = phoneauthshard.getString("userpic", "null");

        clamlay.setBackgroundResource(R.drawable.roundedbtn);
        viewDialog = new ViewDialog(TopNavigationview.this);


        if (token.equals("null")){
            // popup();up
        }else {
            jsonearningpoints(earningpoints_url);
        }
/*
        if (profilepic.equals("null")){
            earningpic.setImageResource(R.drawable.uploadpictureold);
            //Picasso.with(TopNavigationview.this).load(R.drawable.uploadpictureold).into(earningpic);
        }else {
            Picasso.with(TopNavigationview.this).load(profilepic).placeholder(R.drawable.uploadpictureold).into(earningpic);
        }
*/

        progressBarView = (ProgressBar) findViewById(R.id.view_progress_bar);
        /*Animation*/
        RotateAnimation makeVertical = new RotateAnimation(0, -90, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        makeVertical.setFillAfter(true);
        progressBarView.startAnimation(makeVertical);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(0);

        Intent intent=getIntent();
        guestvalues=intent.getStringExtra("guest");


        viewPager = findViewById(R.id.view_pager);
        GooglePlusFragmentPageAdapter adapter = new GooglePlusFragmentPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (isNetworkAvailable()) {
            viewPager.setOffscreenPageLimit(adapter.getCount() - 1);
            viewPager.setCurrentItem(0);
            //Toast.makeText(Home.this, "Network connection is available", Toast.LENGTH_SHORT).show();
        } else if (!isNetworkAvailable()) {
            // Toast.makeText(Home.this, "Network connection is not available", Toast.LENGTH_SHORT).show();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Network connection is not available", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        foryou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        linecreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void sendData1(String message, String claimed, String climedhome) {
        claimedtopnav=claimed;
        claimhometopnav=climedhome;
        Log.d("message",":::"+message+":::"+claimedtopnav+":::"+claimhometopnav);

        if (message.equals("nodata")){
            progressBarView.setProgress(0);
            if (countDownTimer!=null) {
                countDownTimer.cancel();
            }
        }else if (claimedtopnav.equals("true")) {
            Log.d("entring claimed","::");
            progressBarView.setProgress(100);
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
        }
        else if (clamiedget.equals("true")){
            Log.d("entring claimedget","::");
            progressBarView.setProgress(100);
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }else if (claimhometopnav.equals("false")) {
                Log.d("entring claimedhome","::");
                progressBarView.setProgress(0);
                if (countDownTimer != null) {
                    countDownTimer.start();
                }
            }
            else if (claimhometopnav.equals("true")) {
                Log.d("entring claimedhometop","::");
                progressBarView.setProgress(100);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
        }
        else {
//            countDownTimer.start();
            Log.d("entring countertime","::");
            millis = Integer.parseInt(message);
            int seconds = (millis / 1000) % 60;
            Log.d("seconds", ":::" + seconds+"::::"+millis);
            if (!token.equals("null")) {
                progressBarView.setProgress(0);
                fn_countdown(millis);
            }
        }
    }

    private static class GooglePlusFragmentPageAdapter extends FragmentPagerAdapter {
        public GooglePlusFragmentPageAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
    private void fn_countdown(int message) {
        int dur= message;
        Log.d("videocount",":::"+dur);
        if (dur>0) {
            myProgress = 0;
            try {
                countDownTimer.cancel();
            } catch (Exception e) {

            }
            String timeInterval = String.valueOf(dur);
            progress = 0;
            endTime = Integer.parseInt(timeInterval); // up to finish time

            countDownTimer = new CountDownTimer(endTime /** 1000*/ , 2000) {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onTick(long millisUntilFinished) {
                   // Log.d("entering ontick","::::"+endTime);
                    setProgress(progress, endTime/1000);
                    progress = progress + 1;
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                    int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);

                    String newtime = hours + ":" + minutes + ":" + seconds;
                   Log.d("hoursss",":::::"+newtime);
                    String earningpointS=earningpoints.getText().toString();
                    //splitString(earningpointS);

                    StringBuffer alpha = new StringBuffer(),
                            num = new StringBuffer(), special = new StringBuffer();
                    for (int i=0; i<earningpointS.length(); i++)
                    {
                        if (Character.isDigit(earningpointS.charAt(i)))
                            num.append(earningpointS.charAt(i));
                        else if(Character.isAlphabetic(earningpointS.charAt(i)))
                            alpha.append(earningpointS.charAt(i));
                        else
                            special.append(earningpointS.charAt(i));
                    }


                    if (newtime.equals("0:0:0")) {
/*
                        if (token.equals("null")){
                            // popup();
                        }else {
                            jsonearningpoints(getamount_url);
                            progressBarView.setProgress(100);
                        }
*/

                    }
                }
                @Override
                public void onFinish() {
                    setProgress(progress, endTime);
                }
            };
            countDownTimer.start();
        }

    }

    public void setProgress(int startTime, int endTime) {
        progressBarView.setMax(endTime);
        progressBarView.setSecondaryProgress(endTime);
        progressBarView.setProgress(startTime);

    }

    private void jsonearningpoints(String earningpoints_url) {
       // viewDialog.showDialog();

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, earningpoints_url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Responses earning", response.toString());
                        //viewDialog.hideDialog();

                        if (response.length() != 0) {
                            // Iterate the inner "data" array

                            try {
                                String status=response.getString("status");
                                Log.d("statuscode",":::"+status);

                                if (status.equals("2")){
                                    String message=response.getString("message");
                                    String amount=response.getString("vidAmount");
                                    clamlay.setBackgroundResource(R.drawable.roundedbtn);
                                    earningpoints.setText(amount+""+"%");
                                    claimtext.setVisibility(View.GONE);
                                    earningtxt.setVisibility(View.VISIBLE);
                                    earningtxt.setText("Earnings");
                                    //if (message.equals("Balance Claimed Successfully")){
                                        fn_countdown(millis);
                                        clamiedget="false";
                                   // }
                                   // Toast.makeText(TopNavigationview.this, message, Toast.LENGTH_SHORT).show();

                                }
                                else if (status.equals("1")){
                                    coinsPerVid = response.getString("vidAmount");
                                    clamiedget = response.getString("claim");
                                    Log.d("coinsPerVid", "::::" + clamiedget);

                                    if (response.has("user")) {
                                        JSONObject object = response.getJSONObject("user");
                                        profilePic = object.getString("profilePic");
                                        if (profilePic.equals("null")){
                                            earningpic.setImageResource(R.drawable.uploadpictureold);
                                        }else {
                                            Picasso.with(TopNavigationview.this)
                                                    .load(profilePic)
                                                    .placeholder(R.drawable.uploadpictureold)
                                                    .into(earningpic);
                                        }
                                    }
                                    if (clamiedget.equals("true")) {
                                        clamlay.setBackgroundResource(R.drawable.clamback);
                                        progressBarView.setProgress(100);
                                        if (countDownTimer!=null) {
                                            countDownTimer.cancel();
                                        }
                                        earningpoints.setText(coinsPerVid+""+"%");
                                        claimtext.setVisibility(View.VISIBLE);
                                        earningtxt.setVisibility(View.GONE);
                                        claimtext.setText("Claim");

                                        clamlay.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                jsonearningpoints(claimsmash_url);
                                            }
                                        });
                                        claimtext.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                jsonearningpoints(claimsmash_url);
                                            }
                                        });
                                        earningpoints.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                jsonearningpoints(claimsmash_url);
                                            }
                                        });
                                    } else {
                                        clamlay.setBackgroundResource(R.drawable.roundedbtn);
                                        earningpoints.setText(coinsPerVid+""+"%");
                                        claimtext.setVisibility(View.GONE);
                                        earningtxt.setVisibility(View.VISIBLE);
                                        earningtxt.setText("Earnings");

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
                                            //viewDialog.hideDialog();
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                           // Toast.makeText(TopNavigationview.this, message, Toast.LENGTH_SHORT).show();
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
                                            //viewDialog.hideDialog();
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                           // Toast.makeText(TopNavigationview.this, message, Toast.LENGTH_SHORT).show();

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
               // viewDialog.hideDialog();
            }
        });

    }

    public void popup() {

        android.app.AlertDialog.Builder builder;
        final Context mContext = TopNavigationview.this;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.guestpopup, null);

        Button okbtn = (Button) layout.findViewById(R.id.okbtn);
        Button regbtn = (Button) layout.findViewById(R.id.register);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killerplyer();
                Intent intent = new Intent(TopNavigationview.this, LoginFragment.class);
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
        dialog.show();
    }

    int doubleBackToExitPressed = 1;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressed == 2) {
            finishAffinity();
            System.exit(0);
        }
        else {
            doubleBackToExitPressed++;
            Toast.makeText(TopNavigationview.this, "Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressed=1;
            }
        }, 2000);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                backprofile = data.getStringExtra("topback");
                Log.d("topbakimg", ":::" + backprofile);
                if (!backprofile.equals("null")) {
                    Log.d("strdit111", ":::" + backprofile);
                    Picasso.with(TopNavigationview.this).load(backprofile).placeholder(R.drawable.uploadpictureold).into(earningpic);
                }else {
                    Picasso.with(TopNavigationview.this).load(profilePic).placeholder(R.drawable.uploadpictureold).into(earningpic);
                }
            }
        }
    }
}
