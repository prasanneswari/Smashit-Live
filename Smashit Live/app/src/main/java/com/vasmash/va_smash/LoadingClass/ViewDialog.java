package com.vasmash.va_smash.LoadingClass;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import com.airbnb.lottie.LottieAnimationView;
import com.vasmash.va_smash.R;

public class ViewDialog {

    Activity activity;
    Dialog dialog;
    LottieAnimationView animationView;
    public ViewDialog(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {

        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_loading_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
       // dialog.getWindow().setBackgroundDrawableResource(R.color.loadback);

/*
        ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);

        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);
        Glide.with(activity)
                .load(R.raw.loading)
                .placeholder(R.raw.loading)
                .centerCrop()
                .crossFade()
                .into(imageViewTarget);
*/

        animationView = (LottieAnimationView) dialog.findViewById(R.id.animation_view_1);
        animationView.setAnimation("data.json");
        animationView.loop(true);
        animationView.playAnimation();

        dialog.show();
    }

    public void hideDialog(){
        // animationView.cancelAnimation();
        dialog.dismiss();
    }

}
