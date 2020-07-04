package com.vasmash.va_smash.LoadingClass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.vasmash.va_smash.R;

public class Refreshload extends AppCompatActivity {
    static public ImageView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refreshload);
        refresh=findViewById(R.id.refresh);

    }
   static public void refresh() {
        refresh.clearAnimation();
        RotateAnimation anim = new RotateAnimation(30, 360, refresh.getWidth()/2, refresh.getHeight()/2);
        anim.setFillAfter(true);
        anim.setRepeatCount(0);
        anim.setDuration(1000);
        refresh.startAnimation(anim);
    }

}
