package com.vasmash.va_smash.SettingClass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

import com.vasmash.va_smash.R;

public class EndUserSeetings extends AppCompatActivity {
    ScrollView scrollView;
    Button scrollbtn,scrollup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_user_seetings);
        scrollView = findViewById(R.id.scrollview);
        scrollbtn = findViewById(R.id.scrollbtn);
        scrollup=findViewById(R.id.scrollup);

        scrollbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(View.FOCUS_DOWN);
                scrollup.setVisibility(View.VISIBLE);
                scrollbtn.setVisibility(View.GONE);

            }
        });

        scrollup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.fullScroll(View.FOCUS_UP);
                scrollup.setVisibility(View.GONE);
                scrollbtn.setVisibility(View.VISIBLE);

            }
        });

    }

    public void finishActivity(View v){
        finish();
    }

}
