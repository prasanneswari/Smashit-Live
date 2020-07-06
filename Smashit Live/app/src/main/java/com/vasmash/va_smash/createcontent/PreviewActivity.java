package com.vasmash.va_smash.createcontent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.createcontent.videoeffects.VideoEffectsActivity;

import java.io.File;
import java.io.FileWriter;

public class PreviewActivity extends AppCompatActivity {
    private FFmpeg ffmpeg;
    private static final String TAG = "Smashslow";
    VideoView previewvideo;
    Uri uri;
    String paths;
    int speed;
    public File dir;
    String defaultVideoout;
    ImageView preview_worng,preview_correct,preview_reverse;
    private ProgressDialog progressDialog;
    String reverse="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        previewvideo=findViewById(R.id.previewvideo);
        preview_correct=findViewById(R.id.preview_correct);
        preview_worng=findViewById(R.id.preview_worng);
        preview_reverse=findViewById(R.id.preview_reverse);
        progressDialog = new ProgressDialog(this);
        loadFFMpegBinary();

        dir = new File(PreviewActivity.this.getFilesDir().getAbsolutePath(), "VA_Smash");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        defaultVideoout = dir + "/Smashspeed.mp4";
        File createDefault = new File(defaultVideoout);
        if (!createDefault.isFile()) {
            try {
                FileWriter writeDefault = new FileWriter(createDefault);
                writeDefault.append("yy");
                writeDefault.close();
                writeDefault.flush();
            } catch (Exception ex) {
            }
        }

        paths=getIntent().getStringExtra("path");
        speed= getIntent().getIntExtra("Speed",0);
        Log.e("preview" , paths+" "+speed );

        if (speed == 1)
        {
            String[] complexCommand = { "-y", "-i", paths, "-filter_complex", "[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", defaultVideoout};
            execFFmpegBinary(complexCommand);
            progressDialog.show();
            Log.e("preview" , "1"+" "+defaultVideoout);
        }
        else if (speed == 2)
        {
            progressDialog.show();
            String[] complexCommand = {"-y", "-i", paths, "-filter_complex", "[0:v]setpts=4.0*PTS[v];[0:a]atempo=0.7[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", defaultVideoout};
            execFFmpegBinary(complexCommand);
            Log.e("preview" , "2"+" "+defaultVideoout);

        }
        else if (speed == 3)
        {
            Log.e("preview" , "3"+" "+defaultVideoout);
            uri = Uri.parse(paths);
            previewvideo.setVisibility(View.VISIBLE);
            previewvideo.setVideoURI(uri);
            previewvideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            previewvideo.start();

        }
        else if (speed == 4)
        {
            Log.e("preview" , "4"+" "+defaultVideoout);
            progressDialog.show();
            String[] complexCommand = {"-y", "-i", paths, "-filter_complex", "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", defaultVideoout};
            execFFmpegBinary(complexCommand);
        }
        else if (speed == 5)
        {
            Log.e("preview" , "5"+" "+defaultVideoout);
            progressDialog.show();
            String[] complexCommand = {"-y", "-i", paths, "-filter_complex", "[0:v]setpts=0.3*PTS[v];[0:a]atempo=2.0[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", defaultVideoout};
            execFFmpegBinary(complexCommand);
        }






        preview_reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                progressDialog.show();
                String command[] = { "-y", "-i", paths, "-vf", "reverse", "-af", "areverse", defaultVideoout};
                execFFmpegBinary(command);
                reverse="1";



            }
        });


        preview_correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewActivity.this, VideoEffectsActivity.class);
                intent.putExtra("cam", "1");
                intent.putExtra("path", defaultVideoout);
                startActivity(intent);
            }
        });

        preview_worng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }




    private void execFFmpegBinary(final String[] command) {

        Log.e("preview" , String.valueOf(command));




        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                    Log.d(TAG, "FAILED with output : " + s);
                }

                @Override
                public void onSuccess(String s) {

                    progressDialog.hide();
                    Log.d(TAG, "SUCCESS with output : " + s);
                    if (reverse.equals("0")){
                        uri = Uri.parse(defaultVideoout);
                        previewvideo.setVisibility(View.VISIBLE);
                        previewvideo.setVideoURI(uri);
                        previewvideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                            }
                        });
                        previewvideo.start();

                    }
                    else {
                        uri = Uri.parse(defaultVideoout);
                        previewvideo.setVisibility(View.VISIBLE);
                        previewvideo.setVideoURI(uri);
                        previewvideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.setLooping(true);
                            }
                        });
                        previewvideo.start();
                        preview_reverse.setImageDrawable(getResources().getDrawable(R.drawable.reverse_white));

                    }


                }

                @Override
                public void onProgress(String s) {
                    Log.d(TAG, "Started command onProgress: ffmpeg " + command);

                }

                @Override
                public void onStart() {
                    Log.d(TAG, "Started command onStart: ffmpeg " + command);


                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished command onFinish : ffmpeg " + command);
                    progressDialog.hide();


                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            Log.d(TAG, "Finished command : ffmpeg " + command);
            progressDialog.hide();
        }
    }

    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
                Log.d(TAG, "ffmpeg : era nulo");
                ffmpeg = FFmpeg.getInstance(this);
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {

                }

                @Override
                public void onSuccess() {
                    Log.d(TAG, "ffmpeg : correct Loaded");
                }
            });
        } catch (FFmpegNotSupportedException e) {

        } catch (Exception e) {
            Log.d(TAG, "EXception no controlada : " + e);
        }
    }

}
