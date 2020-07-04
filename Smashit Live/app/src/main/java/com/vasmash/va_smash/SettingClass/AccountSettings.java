package com.vasmash.va_smash.SettingClass;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.vasmash.va_smash.R;


public class AccountSettings extends AppCompatActivity {

    LinearLayout privacylay,termslay,copyrightlay,cookieslay,contentlay,notificationlay,enduserlay;
    Button contenbtn,notificationbtn,privacybtn,termsbtn,enduserbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);
        privacylay=findViewById(R.id.privacylay);
        termslay=findViewById(R.id.temslay);
        copyrightlay=findViewById(R.id.copyrightlay);
        cookieslay=findViewById(R.id.cookieslay);
        contentlay=findViewById(R.id.contentlay);
        notificationlay=findViewById(R.id.notifictaionlay);
        contenbtn=findViewById(R.id.contentbtn);
        notificationbtn=findViewById(R.id.notificationbtn);
        privacybtn=findViewById(R.id.privacbtn);
        termsbtn=findViewById(R.id.termsbtn);
        enduserlay=findViewById(R.id.enduserlay);
        enduserbtn=findViewById(R.id.enduserbtn);


        contentlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(AccountSettings.this, ContentPreference.class);
                settingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingintent);
            }
        });

        contenbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(AccountSettings.this, ContentPreference.class);
                settingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingintent);

            }
        });

        notificationlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(AccountSettings.this, NotificationSettings.class);
                settingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingintent);
            }
        });

        notificationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(AccountSettings.this, NotificationSettings.class);
                settingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingintent);

            }
        });

        termslay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(AccountSettings.this, TermsofUse.class);
                settingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingintent);
            }
        });

        termsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(AccountSettings.this, TermsofUse.class);
                settingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingintent);

            }
        });


        privacylay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(AccountSettings.this, PrivacyPolicy.class);
                settingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingintent);
            }
        });

        privacybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(AccountSettings.this, PrivacyPolicy.class);
                settingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingintent);

            }
        });


        enduserlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(AccountSettings.this, EndUserSeetings.class);
                settingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingintent);
            }
        });

        enduserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingintent = new Intent(AccountSettings.this, EndUserSeetings.class);
                settingintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(settingintent);

            }
        });

    }
    public void finishActivity(View v){
        finish();
    }

}
