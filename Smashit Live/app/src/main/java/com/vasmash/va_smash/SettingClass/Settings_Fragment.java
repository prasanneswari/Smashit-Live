package com.vasmash.va_smash.SettingClass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.vasmash.va_smash.BottmNavigation.TopNavigationview;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.login.fragments.LoginFragment;

import static com.vasmash.va_smash.HomeScreen.homefragment.HomeFragment.killerplyer;

public class Settings_Fragment extends AppCompatActivity {

    Button signout;
    GoogleSignInClient mGoogleSignInClient;
    // TODO: Rename and change types and number of parameters
    public static Settings_Fragment newInstance() {
        Settings_Fragment fragment = new Settings_Fragment();
        return fragment;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings_);

        signout=findViewById(R.id.signout);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(Settings_Fragment.this);
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.clear();
        editor.apply();


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });
    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        killerplyer();
                        LoginManager.getInstance().logOut();
                        Intent intent = new Intent(Settings_Fragment.this, TopNavigationview.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
    }
    public void finishActivity(View v){
        finish();
    }

}
