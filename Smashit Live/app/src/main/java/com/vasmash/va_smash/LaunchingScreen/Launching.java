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
    String token;
    public static boolean currentposlaunch;

    private static int SPLASH_TIME_OUT = 1500;

    ViewDialog viewDialog;
    String dynamiccondition="null";
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
                           // Log.d("deeplink",":::"+deepLink);
                            String reflink=deepLink.toString();
                            try {
                                reflink=reflink.substring(reflink.lastIndexOf("?")+1);
                                //Log.d("reflink",":::"+reflink);
                                String postiddynamic=reflink.substring(0,reflink.indexOf("-"));
                                String linkcondition=reflink.substring(reflink.indexOf("-")+1);
                                //Log.d("linkcondition",":::"+linkcondition);

                                if (linkcondition.equals("hashtags")){
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
                       // Log.w("", "getDynamicLink:onFailure", e);
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


}
