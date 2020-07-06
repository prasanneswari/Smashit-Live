package com.vasmash.va_smash.HomeScreen.homeadapters;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;

import com.vasmash.va_smash.R;

public class Functions {
    public static Dialog determinant_dialog;
    public static ProgressBar determinant_progress;

    public static void Show_determinent_loader(Context context, boolean outside_touch, boolean cancleable) {

        determinant_dialog = new Dialog(context);
        determinant_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        determinant_dialog.setContentView(R.layout.item_determinant_progress_layout);
        determinant_dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.d_round_white_background));

        determinant_progress=determinant_dialog.findViewById(R.id.pbar);

        if(!outside_touch)
            determinant_dialog.setCanceledOnTouchOutside(false);

        if(!cancleable)
            determinant_dialog.setCancelable(false);

        determinant_dialog.show();

    }

    public static void Show_loading_progress(int progress){
        if(determinant_progress!=null ){
            determinant_progress.setProgress(progress);
        }
    }
    public static void cancel_determinent_loader(){
        if(determinant_dialog!=null){
            determinant_progress=null;
            determinant_dialog.cancel();
        }
    }

}
