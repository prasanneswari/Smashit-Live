package com.vasmash.va_smash.SearchClass.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.vasmash.va_smash.HomeScreen.homefragment.OriginalSoundDisplay;
import com.vasmash.va_smash.ProfileScreen.OtherprofileActivity;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Searchlatest;
import com.vasmash.va_smash.createcontent.CameraActivity;
import com.vasmash.va_smash.createcontent.Sounds.Sound_modelclass;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Adapter_SoundFragment extends RecyclerView.Adapter<Adapter_SoundFragment.ViewHolder> {

    ArrayList<Model_Searchlatest> mainmodels;
    Context context;
    MediaPlayer mediaPlayer;
    String SelectedAudio = "SelectedAudio.mp3";
    Dialog dialog;
    public File dir;
    public String defaultSound;
    DownloadRequest prDownloader;


    public Adapter_SoundFragment(ArrayList<Model_Searchlatest> mainmodels, Context context) {
        this.mainmodels = mainmodels;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter__sound_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.soundname.setText(mainmodels.get(position).getSongname());
        holder.soundusername.setText(mainmodels.get(position).getSongusername());
        if (mainmodels.get(position).getSongduration()!=null) {
            holder.soundduration.setText(mainmodels.get(position).getSongduration());
        }
        holder.soundviews.setText(mainmodels.get(position).getSongviews()+" "+"Posts");


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String songname = mainmodels.get(position).getSongname();
                String songid = mainmodels.get(position).getSongid();
                Intent intent = new Intent(context, OriginalSoundDisplay.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("name",songname);
                intent.putExtra("soundid",songid);
                context.startActivity(intent);
            }

        });
    }


    @Override
    public int getItemCount() {
        return mainmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView soundname, soundusername,soundduration,soundviews;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            soundname = itemView.findViewById(R.id.songname);
            soundusername = itemView.findViewById(R.id.soundusername);
            soundduration = itemView.findViewById(R.id.soundduration);
            soundviews = itemView.findViewById(R.id.soundviews);

        }
    }


    public void Down_load_mp3(String url) {

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();


        dir = new File(context.getFilesDir().getAbsolutePath(), "VA_Smash");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        defaultSound = dir.toString();
        File createDefault = new File(defaultSound);
        if (!createDefault.isFile()) {
            try {
                FileWriter writeDefault = new FileWriter(createDefault);
                writeDefault.append("yy");
                writeDefault.close();
                writeDefault.flush();
            } catch (Exception ex) {
            }
        }

        prDownloader = PRDownloader.download(url, defaultSound, SelectedAudio)
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

                    }
                });

        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                progressDialog.dismiss();
                //Log.e("sound", "done");
//                Intent output = new Intent();
//                output.putExtra("isSelected","yes");
//                output.putExtra("sound_name",sound_name);
//                output.putExtra("sound_id",id);
//                getActivity().setResult(RESULT_OK, output);
//                getActivity().finish();
//                getActivity().overridePendingTransition(R.anim.in_from_top, R.anim.out_from_bottom);
            }

            @Override
            public void onError(Error error) {
                progressDialog.dismiss();
               // Log.e("sound", String.valueOf(error));
            }


        });

    }

}