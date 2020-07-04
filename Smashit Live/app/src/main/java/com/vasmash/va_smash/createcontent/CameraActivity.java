package com.vasmash.va_smash.createcontent;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.opengl.GLException;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.iceteck.silicompressorr.SiliCompressor;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.VASmashAPIS.APIs;
import com.vasmash.va_smash.createcontent.Sounds.CreatesoundsActivity;
import com.vasmash.va_smash.createcontent.Sounds.Merge_Video_Audio;
import com.vasmash.va_smash.createcontent.Sounds.Sound_catemodel;
import com.vasmash.va_smash.createcontent.Sounds.Sound_modelclass;
import com.vasmash.va_smash.createcontent.Sounds.SoundsAdapter;
import com.vasmash.va_smash.createcontent.Sounds.Sounds_Cate_Adapter;
import com.vasmash.va_smash.createcontent.cameraedit.MyCanvas;
import com.vasmash.va_smash.createcontent.customVideoViews.BackgroundTask;
import com.vasmash.va_smash.createcontent.customVideoViews.BarThumb;
import com.vasmash.va_smash.createcontent.customVideoViews.CustomRangeSeekBar;
import com.vasmash.va_smash.createcontent.customVideoViews.OnRangeSeekBarChangeListener;
import com.vasmash.va_smash.createcontent.customVideoViews.OnVideoTrimListener;
import com.vasmash.va_smash.createcontent.customVideoViews.TileView;

import com.vasmash.va_smash.createcontent.filters.CameraRenderer;
import com.vasmash.va_smash.createcontent.filters.FilterActivity;
import com.vasmash.va_smash.createcontent.filters.gpu.FilterType;
import com.vasmash.va_smash.createcontent.filters.gpu.FiltersAdapter;
import com.vasmash.va_smash.createcontent.filters.gpu.SampleCameraGLView;
import com.vasmash.va_smash.createcontent.filters.gpu.camerarecorder.CameraRecordListener;
import com.vasmash.va_smash.createcontent.filters.gpu.camerarecorder.GPUCameraRecorder;
import com.vasmash.va_smash.createcontent.filters.gpu.camerarecorder.GPUCameraRecorderBuilder;
import com.vasmash.va_smash.createcontent.filters.gpu.camerarecorder.LensFacing;
import com.vasmash.va_smash.createcontent.utils.Utility;
import com.vasmash.va_smash.createcontent.videoeffects.VideoEffectsActivity;
import com.vasmash.va_smash.login.ModelClass.Languages;
import com.vasmash.va_smash.segmentprocessinbar.ProgressBarListener;
import com.vasmash.va_smash.segmentprocessinbar.SegmentedProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;

public class CameraActivity extends MyCanvas {

    private SurfaceView preview = null;
    private SurfaceHolder previewHolder = null;
    private Camera camera = null;
    private Camera.Parameters params;
    private boolean inPreview = false;
    private boolean cameraConfigured = false;
    private boolean isRecording;
    private boolean isFlashOn;
    private MediaRecorder mediaRecorder;
    private static int currentCameraId = 0;
    private Bitmap rotatedBitmap;
    private RelativeLayout captureMedia, camera_edit_control,cameraglass;
    private FrameLayout editMedia;
    private CircleProgressBar customButton;
    private ImageView flashButton;
    private ImageView uploadButton, next;
    private TextView uploadButtonTxt;
    private ImageView EditCaptureSwitchBtn;
    private LinearLayout editTextBody;
    private ImageView capturedImage, start_stop, take_picture, take_record;
    private VideoView videoView,captured_video1;
    private FrameLayout camera_controls, filter_controls, speed_control_layout, countdown_control, camera_timer_control, camera_sound_screen, effects_layout,camera_upload_edit;
    int VideoSeconds = 1;
    int noti_id;
    SeekBar customSeekbar;
    TextView progress;
    String ImageFile;

    LinearLayout switchCameraBtn, speed_control, filtercontrol, countdown_img,camera_icon_lay;
    RecyclerView recyclerView;
    Filter_Adapter mAdapter;
    SegmentedProgressBar video_progress;
    List<Filter_model> personUtilsList;
    String filternames_txt = "none";
    TextView filter_done;
    private int camera_selected = 0;
    int sec_passed = 0;
    TextView one, two, three, four, five, camera_sounds;
    ImageView one_view, two_view, three_view, four_view, five_view, uploadfile, correct, worng,createsounds;
    Boolean speed = false;
    CamcorderProfile profile;
    int speed_value = 3;
    public int counter = 3;
    File sdCard;
    LinearLayout correct_layout;
    FrameLayout camera_save_control;
    ImageView timer, flip, speedicon, filter, efect, sound_back;
    Uri uri;
    Uri uri2;
    Button camare_starttimer;
    int video = 1;
    int photo = 2;
    MediaPlayer mediaPlayer;
    //    Dialog dialog;
//    ListView sound_list_view;
    SoundsAdapter soundsAdapter;
    String soundeselected = null;
    ListView sounds_listview;
    RecyclerView camera_sounds_cate;

    ArrayList<Sound_modelclass> soundlist;

    Sounds_Cate_Adapter sounds_cate_adapter;
    ArrayList<Sound_catemodel> soundlist1;
    private List<Sound_catemodel> personUtils11;
    String sound_cate_code="5e8c825d8580495190c20e98";




    private RequestQueue mQueue;
    int preSelectedIndex = -1;
    DownloadRequest prDownloader;
    String SelectedAudio = "SelectedAudio.mp3";
    Dialog dialog;

    private ImageView txtVideoCancel;
    private ImageView txtVideoUpload;
    private TextView txtVideoEditTitle;
    private TextView txtVideoTrimSeconds;
    private RelativeLayout rlVideoView;
    private TileView tileView;
    private CustomRangeSeekBar mCustomRangeSeekBarNew;
    private VideoView mVideoView;
    private ImageView imgPlay;
    private SeekBar seekBarVideo;
    private TextView txtVideoLength;

    private int mDuration = 0;
    private int mTimeVideo = 0;
    private int mStartPosition = 0;
    private int mEndPosition = 0;
    // set your max video trim seconds
    private int mMaxDuration = 30;
    private Handler mHandler = new Handler();

    private ProgressDialog mProgressDialog;
    String srcFile;
    String dstFile;
    String upload_video_edit="0";
    boolean wasLon = false;
    String landscape="0";
    String defaultVideo1;
    String token;



    private SampleCameraGLView sampleGLView;
    protected com.vasmash.va_smash.createcontent.filters.gpu.camerarecorder.GPUCameraRecorder GPUCameraRecorder;
    private String filepath;
    private TextView recordBtn;
    protected LensFacing lensFacing = LensFacing.BACK;
    protected int cameraWidth = 720;
    protected int cameraHeight = 1280;
    protected int videoWidth = 720;
    protected int videoHeight = 1280;

    private boolean toggleClick = false;

    private RecyclerView  lv;
    private List<FilterType> personUtilss;
    FiltersAdapter filtersAdapter;

    RelativeLayout edit_imagelayout,edit_videolayout;




    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private FrameLayout container;
    private CameraRenderer renderer;
    private TextureView textureView;
    private int filterId = R.id.filter0;


    ListView camera_listview_lang;
    ArrayList<Languages> categ;
    Post_lang_Adapter post_lang_adapter;

    RelativeLayout camera_audio;
    TextView cam_audiotx,cam_lange;
    int preSelectedIndex1 = -1;
    String audio_lang_id="0";

//    GPUPlayerView gpuPlayerView;
//
//    public static int  select_postion=0;
//
//    final List<FilterType> filterTypes = FilterType.createFilterList();
//    Filters_Adapters adapter;
//    RecyclerView recylerviewfilter;




    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetPermission();

//        select_postion=0;
        captureMedia = (RelativeLayout) findViewById(R.id.camera_view);
        cameraglass = (RelativeLayout) findViewById(R.id.cameraglass);
        editMedia = (FrameLayout) findViewById(R.id.edit_media);
        camera_upload_edit = (FrameLayout) findViewById(R.id.camera_upload_edit);

        speed_control_layout = (FrameLayout) findViewById(R.id.speed_conrol);
        camera_save_control = (FrameLayout) findViewById(R.id.camera_save_control);
//        effects_layout = (FrameLayout) findViewById(R.id.camera_effects_layout);
        camera_edit_control = (RelativeLayout) findViewById(R.id.camera_edit_control);
        camera_icon_lay = (LinearLayout) findViewById(R.id.camera_icon_lay);


        customButton = (CircleProgressBar) findViewById(R.id.custom_progressBar);


        video_progress = (SegmentedProgressBar) findViewById(R.id.video_progress);
        start_stop = (ImageView) findViewById(R.id.start_stop);
        take_picture = (ImageView) findViewById(R.id.take_picture);
        next = (ImageView) findViewById(R.id.add_next);
        take_record = (ImageView) findViewById(R.id.take_video);
        take_picture.setImageResource(R.drawable.ic_photo_selected);
        video_progress.setVisibility(View.INVISIBLE);


        flip = (ImageView) findViewById(R.id.camera_flip);
        speedicon = (ImageView) findViewById(R.id.camera_speed);
        timer = (ImageView) findViewById(R.id.camera_timer);
        efect = (ImageView) findViewById(R.id.camera_effects);
        filter = (ImageView) findViewById(R.id.camera_filters);


        switchCameraBtn = (LinearLayout) findViewById(R.id.img_switch_camera);
        flashButton = (ImageView) findViewById(R.id.img_flash_control);
        speed_control = (LinearLayout) findViewById(R.id.img_speed_control);
        uploadButton = (ImageView) findViewById(R.id.upload_media);
        uploadButtonTxt = (TextView) findViewById(R.id.upload_media_txt);
        uploadButtonTxt.setText("");
        editTextBody = (LinearLayout) findViewById(R.id.editTextLayout);
        //selectSticker  = (LinearLayout) findViewById(R.id.select_sticker);
        ImageView addText = (ImageView) findViewById(R.id.add_text);
        ImageView addSticker = (ImageView) findViewById(R.id.add_stickers);
        isRecording = false;
        isFlashOn = false;

        EditCaptureSwitchBtn = (ImageView) findViewById(R.id.cancel_capture);
        capturedImage = (ImageView) findViewById(R.id.captured_image);
        videoView = (VideoView) findViewById(R.id.captured_video);


        customSeekbar = (SeekBar) findViewById(R.id.seekBar1);
        progress = (TextView) findViewById(R.id.textView1);


        txtVideoCancel = (ImageView) findViewById(R.id.txtVideoCancel);
        txtVideoUpload = (ImageView) findViewById(R.id.txtVideoUpload);
        txtVideoEditTitle = (TextView) findViewById(R.id.txtVideoEditTitle);
        txtVideoTrimSeconds = (TextView) findViewById(R.id.txtVideoTrimSeconds);
        rlVideoView = (RelativeLayout) findViewById(R.id.llVideoView);
        tileView = (TileView) findViewById(R.id.timeLineView);
        mCustomRangeSeekBarNew = ((CustomRangeSeekBar) findViewById(R.id.timeLineBar));
        mVideoView = (VideoView) findViewById(R.id.videoView);
        imgPlay = (ImageView) findViewById(R.id.imgPlay);
        seekBarVideo = (SeekBar) findViewById(R.id.seekBarVideo);
        txtVideoLength = (TextView) findViewById(R.id.txtVideoLength);

        edit_imagelayout = (RelativeLayout) findViewById(R.id.edit_imagelayout);
        edit_videolayout = (RelativeLayout) findViewById(R.id.edit_videolayout);


        captured_video1 = (VideoView) findViewById(R.id.captured_video1);


//        preview = (SurfaceView) findViewById(R.id.preview);
//        previewHolder = preview.getHolder();
//        previewHolder.addCallback(surfaceCallback);
//        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

//        setContentView(container = new FrameLayout(this));
//        setupCameraPreviewView();



        noti_id = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(this);
        token = phoneauthshard.getString("token", "null");


//        arfragment =(ArFragment)getSupportFragmentManager().findFragmentById(R.id.camera_arfragment);
//        arfragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) ->{
//                    Anchor anchor = hitResult.createAnchor();
//                    ModelRenderable.builder()
//                            .setSource(this, R.raw.project)
//                            .build()
//                            .thenAccept(modelRenderable ->
//                                    addmodeltoanchor(anchor,modelRenderable))
//                            .exceptionally(throwable -> {
//                                AlertDialog.Builder builder=new AlertDialog.Builder(this);
//                                builder.setMessage(throwable.getMessage()).show();
//                                return null;
//                            });
//                }
//
//                );



//        efect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
////                Intent i=new Intent(CameraActivity.this, ARcoreActivity.class);
////                startActivity(i);
////                effects_layout.setVisibility(View.VISIBLE);
//            }
//        });





//            Create folder
        sdCard = Environment.getExternalStorageDirectory();
        dir = new File(CameraActivity.this.getFilesDir().getAbsolutePath(), "VA_Smash");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        defaultVideo = dir + "/Smash.mp4";
        File createDefault = new File(defaultVideo);
        if (!createDefault.isFile()) {
            try {
                FileWriter writeDefault = new FileWriter(createDefault);
                writeDefault.append("yy");
                writeDefault.close();
                writeDefault.flush();
            } catch (Exception ex) {
            }
        }

        Log.e("Smash111111111111111",defaultVideo);
        txtVideoCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCaptureSwitch();
            }
        });
        txtVideoUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadeditedvideo();
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                onVideoPrepared(mp);
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onVideoCompleted();
            }
        });

        // handle your range seekbar changes
        mCustomRangeSeekBarNew.addOnRangeSeekBarListener(new OnRangeSeekBarChangeListener() {
            @Override
            public void onCreate(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                // Do nothing
            }

            @Override
            public void onSeek(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                onSeekThumbs(index, value);
            }

            @Override
            public void onSeekStart(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                if (mVideoView != null) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    seekBarVideo.setProgress(0);
                    mVideoView.seekTo(mStartPosition * 1000);
                    mVideoView.pause();
                    imgPlay.setBackgroundResource(R.drawable.ic_play);
                }
            }

            @Override
            public void onSeekStop(CustomRangeSeekBar customRangeSeekBarNew, int index, float value) {
                onStopSeekThumbs();
            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mVideoView.isPlaying()) {
                    if (mVideoView != null) {
                        mVideoView.pause();
                        imgPlay.setBackgroundResource(R.drawable.ic_play);
                    }
                } else {
                    if (mVideoView != null) {
                        mVideoView.start();
                        imgPlay.setBackgroundResource(R.drawable.ic_pause);
                        if (seekBarVideo.getProgress() == 0) {
                            txtVideoLength.setText("00:00");
                            updateProgressBar();
                        }
                    }
                }
            }
        });

        // handle changes on seekbar for video play
        seekBarVideo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                if (mVideoView != null) {
                    mHandler.removeCallbacks(mUpdateTimeTask);
                    seekBarVideo.setMax(mTimeVideo * 1000);
                    seekBarVideo.setProgress(0);
                    mVideoView.seekTo(mStartPosition * 1000);
                    mVideoView.pause();
                    imgPlay.setBackgroundResource(R.drawable.ic_play);
                }
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                mVideoView.seekTo((mStartPosition * 1000) - seekBarVideo.getProgress());
            }
        });



//       sounds applied to app
        camera_sound_screen = (FrameLayout) findViewById(R.id.camera_sounds_screen);
        sound_back = (ImageView) findViewById(R.id.sound_back);
        camera_sounds = (TextView) findViewById(R.id.camera_sounds);
        sounds_listview = (ListView) findViewById(R.id.sounds_listview);

        camera_listview_lang = (ListView) findViewById(R.id.camera_listview_langes);
        camera_audio=findViewById(R.id.camera_audio);
        cam_audiotx=findViewById(R.id.cam_audiotx);
        cam_lange=findViewById(R.id.cam_lange);

        cam_audiotx.setBackground(getResources().getDrawable(R.drawable.empty));
        cam_lange.setBackground(getResources().getDrawable(R.drawable.homeback));
        camera_audio.setVisibility(View.INVISIBLE);
        camera_listview_lang.setVisibility( View.VISIBLE);
        langapi();


        camera_sounds_cate= (RecyclerView) findViewById(R.id.camera_sounds_cate);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        camera_sounds_cate.setLayoutManager(layoutManager);


        createsounds=(ImageView) findViewById(R.id.createsounds);
        createsounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CameraActivity.this, CreatesoundsActivity.class);
                startActivity(i);
            }
        });





















        cam_lange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cam_audiotx.setBackground(getResources().getDrawable(R.drawable.empty));
                cam_lange.setBackground(getResources().getDrawable(R.drawable.homeback));
                camera_audio.setVisibility(View.INVISIBLE);
                camera_listview_lang.setVisibility( View.VISIBLE);
                langapi();



            }
        });
        cam_audiotx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cam_lange.setBackground(getResources().getDrawable(R.drawable.empty));
                cam_audiotx.setBackground(getResources().getDrawable(R.drawable.homeback));
                camera_listview_lang.setVisibility(View.INVISIBLE);
                camera_audio.setVisibility( View.VISIBLE);

            }
        });


        camera_listview_lang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categ.get(position);
                String value=categ.get(position).getLang_name().toString();
                String valueid=categ.get(position).getLang_code().toString();


                audio_lang_id=value;
                Log.e("wwwwww",audio_lang_id+" "+valueid);
                // Toast.makeText(PostcontentActivity.this, lang_id+" "+valueid, Toast.LENGTH_SHORT).show();
                Languages model = categ.get(position); //changed it to model because viewers will confused about it

                model.setSelected(true);

                categ.set(position, model);

                if (preSelectedIndex1 > -1){

                    Languages preRecord1 = categ.get(preSelectedIndex1);
                    preRecord1.setSelected(false);

                    categ.set(preSelectedIndex1, preRecord1);



                }

                preSelectedIndex1 = position;

                //now update adapter so we are going to make a update method in adapter
                //now declare adapter final to access in inner method

                post_lang_adapter.updateRecords(categ);
                cam_lange.setBackground(getResources().getDrawable(R.drawable.empty));
                cam_audiotx.setBackground(getResources().getDrawable(R.drawable.homeback));
                camera_listview_lang.setVisibility(View.INVISIBLE);
                camera_audio.setVisibility( View.VISIBLE);
                soundlistcate();
                jsongetsoundtrends();
            }
        });











        sound_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_sound_screen.setVisibility(View.INVISIBLE);
                if (mediaPlayer == null) {
                }
                else {

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();

                    }
                }
            }


        });


//        sounds_cate_adapterRecy.setonSounds_cate(new Sounds_Cate_AdapterRecy.OnSounds_Cate() {
//            @Override
//            public void onSounds_cate(Sound_catemodel code) {
//
//            }
//        });

//        sounds_cate_adapterRecy.setonSounds_cate_adapterRecy(new Sounds_Cate_AdapterRecy.OnSounds_Cate_AdapterRecy() {
//            @Override
//            public void onSounds_cate_adapterRecy(Sound_catemodel code) {
//
//            }
//        });
//        sounds_cate_adapterRecy.setonSounds_cate_adapterRecy(new Sounds_Cate_AdapterRecy.OnSounds_Cate_AdapterRecy() {
//            @Override
//            public void onSounds_cate_adapterRecy(Sound_catemodel code) {
//                sound_cate_code=code.getSound_code();
//                Log.e("sound_cate_code",sound_cate_code);
//            }
//        });



//                setonSounds_cate_adapterRecy(new Sounds_Cate_AdapterRecy.OnSounds_Cate_AdapterRecy() {
//            @Override
//            public void onSounds_cate_adapterRecy(Sound_catemodel filternames) {
//                sound_cate_code=filternames.getSound_code();
//                Log.e("sound_cate_code",sound_cate_code);
//
//            }
//        });


        camera_sounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera_selected == 0) {

                    Toast.makeText(CameraActivity.this, "This is work only in video mode",
                            Toast.LENGTH_LONG).show();

                }
                if (camera_selected == 1) {


                    camera_sound_screen.setVisibility(View.VISIBLE);
                }

            }
        });








        sounds_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                soundlist.get(position);
                String value = soundlist.get(position).getSound_name().toString();
                String valueid = soundlist.get(position).getSound_code().toString();
                Boolean checkvalue = soundlist.get(position).isSelected();
                Log.e("checkvalue", String.valueOf(checkvalue));

                Down_load_mp3(valueid);


                AlertDialog.Builder builder;

                LayoutInflater inflater = (LayoutInflater) CameraActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.sound_play, null);
                TextView pop_text = (TextView) layout.findViewById(R.id.pop_text);
                TextView pop_select = (TextView) layout.findViewById(R.id.pop_select);
                ImageView pop_play_btn = (ImageView) layout.findViewById(R.id.pop_play_btn);
                ImageView pop_pause_btn = (ImageView) layout.findViewById(R.id.pop_pause_btn);
                ImageView pop_close_btn = (ImageView) layout.findViewById(R.id.pop_close_btn);
                RelativeLayout pop_lay = (RelativeLayout) layout.findViewById(R.id.pop_lay);
                pop_lay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(mediaPlayer==null){
                            dialog.dismiss();
                        }else {
                            mediaPlayer.stop();
                            dialog.dismiss();
                        }
                    }
                });

                if (checkvalue == false) {
                    pop_select.setText("Select");

                } else {
                    pop_select.setText("Unselect");
                }
                pop_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(pop_text.getText().toString().equals("play")){
                            pop_text.setText("Playing");
                            Uri ur = Uri.parse(defaultSound + "/" + SelectedAudio);
                            Log.e("defaultSound", defaultSound);
                            mediaPlayer = MediaPlayer.create(CameraActivity.this, ur);
                            mediaPlayer.start();
                            pop_pause_btn.setVisibility(View.VISIBLE);
                            pop_play_btn.setVisibility(View.INVISIBLE);

                        }
                        else if(pop_text.getText().toString().equals("Playing")){
                            pop_text.setText("play");
                            mediaPlayer.pause();
                            pop_pause_btn.setVisibility(View.INVISIBLE);
                            pop_play_btn.setVisibility(View.VISIBLE);
                        }
                    }
                });
                pop_play_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop_text.setText("Playing");
                        Uri ur = Uri.parse(defaultSound + "/" + SelectedAudio);
                        Log.e("defaultSound", defaultSound);
                        mediaPlayer = MediaPlayer.create(CameraActivity.this, ur);
                        mediaPlayer.start();
                        pop_pause_btn.setVisibility(View.VISIBLE);
                        pop_play_btn.setVisibility(View.INVISIBLE);
                    }
                });
                pop_pause_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop_text.setText("play");
                        mediaPlayer.pause();
                        pop_pause_btn.setVisibility(View.INVISIBLE);
                        pop_play_btn.setVisibility(View.VISIBLE);
                    }
                });
                pop_close_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        mediaPlayer.stop();
                    }
                });
                pop_select.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (checkvalue == false) {
                            camera_sounds.setText(value);
                            pop_select.setText("Un select");
                            if(mediaPlayer==null){

                            }else {
                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.stop();

                                }}
                            dialog.dismiss();
                            Log.e("wwwwww", value + " " + valueid);
//                            Toast.makeText(CameraActivity.this, value + " " + valueid, Toast.LENGTH_SHORT).show();
                            Sound_modelclass model = soundlist.get(position); //changed it to model because viewers will confused about it

                            model.setSelected(true);

                            soundlist.set(position, model);

                            if (preSelectedIndex > -1) {

                                Sound_modelclass preRecord = soundlist.get(preSelectedIndex);
                                preRecord.setSelected(false);

                                soundlist.set(preSelectedIndex, preRecord);

                            }

                            preSelectedIndex = position;

                            //now update adapter so we are going to make a update method in adapter
                            //now declare adapter final to access in inner method

                            soundsAdapter.updateRecords(soundlist);
                        } else if (checkvalue == true) {
                            pop_select.setText("select");
                            dialog.dismiss();
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.stop();

                            }
                            camera_sounds.setText("sounds  ");
                            Sound_modelclass model = soundlist.get(position);
                            model.setSelected(false);

                            soundlist.set(position, model);

                            if (preSelectedIndex > -1) {

                                Sound_modelclass preRecord = soundlist.get(preSelectedIndex);
                                preRecord.setSelected(false);

                                soundlist.set(preSelectedIndex, preRecord);

                            }

                            preSelectedIndex = position;

                            //now update adapter so we are going to make a update method in adapter
                            //now declare adapter final to access in inner method

                            soundsAdapter.updateRecords(soundlist);
                        }

                    }
                });


                builder = new AlertDialog.Builder(CameraActivity.this);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                builder.setView(layout);
                dialog = builder.create();
                dialog.setCancelable(false);
                dialog.show();

//                  if(checkvalue==false){
//
//
//
//
////                   AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CameraActivity.this);
////                   alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
////                           alertDialogBuilder.setPositiveButton("Play",
////                                   new DialogInterface.OnClickListener() {
////                                       @Override
////                                       public void onClick(DialogInterface arg0, int arg1) {
////                                           Uri ur=Uri.parse(defaultSound+"/"+SelectedAudio);
////                                           Log.e("defaultSound",defaultSound);
////                                           mediaPlayer = MediaPlayer.create(CameraActivity.this, ur);
////                                           mediaPlayer.start();
////                                       }
////                                   });
////
////                   alertDialogBuilder.setNegativeButton("select",new DialogInterface.OnClickListener() {
////                       @Override
////                       public void onClick(DialogInterface dialog, int which) {
////
////
////                           camera_sounds.setText(value);
////
////                           Log.e("wwwwww",value+" "+valueid);
////                           Toast.makeText(CameraActivity.this, value+" "+valueid, Toast.LENGTH_SHORT).show();
////                           Sound_modelclass model = soundlist.get(position); //changed it to model because viewers will confused about it
////
////                           model.setSelected(true);
////
////                           soundlist.set(position, model);
////
////                           if (preSelectedIndex > -1){
////
////                               Sound_modelclass preRecord = soundlist.get(preSelectedIndex);
////                               preRecord.setSelected(false);
////
////                               soundlist.set(preSelectedIndex, preRecord);
////
////                           }
////
////                           preSelectedIndex = position;
////
////                           //now update adapter so we are going to make a update method in adapter
////                           //now declare adapter final to access in inner method
////
////                           soundsAdapter.updateRecords(soundlist);
////                       }
////                   });
////
////                   AlertDialog alertDialog = alertDialogBuilder.create();
////                   alertDialog.show();
////
//
//
//
//
//               }
//               else{
//                   camera_sounds.setText("sounds  ");
//                   Sound_modelclass model = soundlist.get(position);
//                   model.setSelected(false);
//
//                   soundlist.set(position, model);
//
//                   if (preSelectedIndex > -1){
//
//                       Sound_modelclass preRecord = soundlist.get(preSelectedIndex);
//                       preRecord.setSelected(false);
//
//                       soundlist.set(preSelectedIndex, preRecord);
//
//                   }
//
//                   preSelectedIndex = position;
//
//                   //now update adapter so we are going to make a update method in adapter
//                   //now declare adapter final to access in inner method
//
//                   soundsAdapter.updateRecords(soundlist);
//               }


            }
        });


    //    Upload files
        camera_controls = (FrameLayout) findViewById(R.id.camera_controls);
        camera_timer_control = (FrameLayout) findViewById(R.id.camera_timer_control);
        filter_controls = (FrameLayout) findViewById(R.id.filter_layout);
        countdown_control = (FrameLayout) findViewById(R.id.countdown_control);
        filtercontrol = (LinearLayout) findViewById(R.id.img_filter_control);
        countdown_img = (LinearLayout) findViewById(R.id.img_timer_camera);
        filter_done = (TextView) findViewById(R.id.filter_done);
        correct_layout = (LinearLayout) findViewById(R.id.camera_correct_layout);
        uploadfile = (ImageView) findViewById(R.id.camera_folder);


        uploadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (camera_selected == 0) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, photo);
                }
                if (camera_selected == 1) {
                    upload_video_edit="1";
                    landscape="0";
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("video/*");
                    startActivityForResult(intent, video);
                }


            }
        });

        //  preview selection

        dir = new File(CameraActivity.this.getFilesDir().getAbsolutePath(), "VA_Smash");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        defaultVideo1 = dir + "/Smashfinaloutput.mp4";
        File createDefault1 = new File(defaultVideo);
        if (!createDefault1.isFile()) {
            try {
                FileWriter writeDefault = new FileWriter(createDefault1);
                writeDefault.append("yy");
                writeDefault.close();
                writeDefault.flush();
            } catch (Exception ex) {
            }
        }




        correct = (ImageView) findViewById(R.id.camera_correct);
        worng = (ImageView) findViewById(R.id.camera_worng);
        correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera_selected==0) {
                    camera_save_control.setVisibility(View.INVISIBLE);
                    camera_edit_control.setVisibility(View.VISIBLE);
                }
                else if(camera_selected==1){

//                    Intent intent = new Intent(CameraActivity.this, VideoEffectsActivity.class);
//
//                    intent.putExtra("cam", camera_selected);
//                    intent.putExtra("path", defaultVideo);
//                    startActivity(intent);
//                    if (!camera_sounds.getText().toString().equals("sounds  " )){
//                        Merge_withAudio();
//                    }
//                    finish();
                    Intent intent = new Intent(CameraActivity.this, VideoEffectsActivity.class);
                    intent.putExtra("cam", camera_selected);
                    intent.putExtra("path", defaultVideo);
                    startActivity(intent);
                    if (!camera_sounds.getText().toString().equals("sounds  ")){
                        Merge_withAudio();
                    }
                    finish();
                }
            }
        });
        worng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCaptureSwitch();
                landscape="0";
                camera_icon_lay.setVisibility(View.VISIBLE);

            }
        });



      // Filter selection

        recyclerView = (RecyclerView) findViewById(R.id.filters_recyclerview);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));


//        adapter = new Filters_Adapters(this, filterTypes, new Filters_Adapters.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int postion, FilterType item) {
//                select_postion=postion;
//                gpuPlayerView.setGlFilter(FilterType.createGlFilter(filterTypes.get(postion), getApplicationContext()));
//                adapter.notifyDataSetChanged();
//            }
//        });
//        recylerviewfilter.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recylerviewfilter.setAdapter(adapter);
//
//
//
//        gpuPlayerView = new GPUPlayerView(this);
//        gpuPlayerView.setPlayerScaleType(PlayerScaleType.RESIZE_NONE);
//
////        gpuPlayerView.setSimpleExoPlayer(player);
//        gpuPlayerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//
//        ((MovieWrapperView) findViewById(R.id.layout_movie_wrapper)).addView(gpuPlayerView);
//
//        gpuPlayerView.onResume();

        personUtilsList = new ArrayList<>();
        Filter_model filtersss = new Filter_model("filter0", R.drawable.filter1);
        personUtilsList.add(filtersss);
        filtersss = new Filter_model("filter1", R.drawable.filter2);
        personUtilsList.add(filtersss);

        filtersss = new Filter_model("filter3", R.drawable.filter4);
        personUtilsList.add(filtersss);
        filtersss = new Filter_model("filter4", R.drawable.filter5);
        personUtilsList.add(filtersss);
        filtersss = new Filter_model("filter5", R.drawable.filter3);
        personUtilsList.add(filtersss);
        filtersss = new Filter_model("filter6", R.drawable.filter1);
        personUtilsList.add(filtersss);
//        filtersss=new Filter_model("Whiteboard", R.drawable.filter4);
//        personUtilsList.add(filtersss);
//        filtersss=new Filter_model("Whiteboard", R.drawable.filter2);
//        personUtilsList.add(filtersss);


        mAdapter = new Filter_Adapter(CameraActivity.this, personUtilsList);

        recyclerView.setAdapter(mAdapter);






//
//
        mAdapter.setonFilterClickListener(new Filter_Adapter.OnFilterClickListener() {
            @Override
            public void onFilterClickListener(Filter_model filternames) {
                Log.e("color ", filternames.getFiltername());
                filternames_txt = filternames.getFiltername();
                if (renderer != null)
                    renderer.setSelectedFilter(Integer.parseInt(filternames_txt));

//                String Aqua="Aqua";
//                String none="none";
//                String Black="Black";
//                String Negative="Negative";
//                String Posterize="Posterize";
//                String Sepia="Sepia";
//                String Whiteboard="Whiteboard";

//
//                try {
//
//                    params = camera.getParameters();
//
//                    switch (filternames.getFiltername()) {
//                        case "none":
//                            params.setColorEffect(Camera.Parameters.EFFECT_NONE);
//                            camera.setParameters(params);
//                            break;
//                        case "Aqua":
//                            params.setColorEffect(Camera.Parameters.EFFECT_AQUA);
//                            camera.setParameters(params);
//                            break;
//                        case "Black":
//                            params.setColorEffect(Camera.Parameters.EFFECT_MONO);
//                            camera.setParameters(params);
//                            break;
//                        case "Negative":
//                            params.setColorEffect(Camera.Parameters.EFFECT_NEGATIVE);
//                            camera.setParameters(params);
//                            break;
//                        case "Posterize":
//                            params.setColorEffect(Camera.Parameters.EFFECT_POSTERIZE);
//                            camera.setParameters(params);
//                            break;
//                        case "Sepia":
//                            params.setColorEffect(Camera.Parameters.EFFECT_SEPIA);
//                            camera.setParameters(params);
//                            break;
////                        case "Whiteboard":
////                            params.setColorEffect(Camera.Parameters.EFFECT_BLACKBOARD);
////                            camera.setParameters(params);
////                            break;
//
//                    }
//                } catch (Exception ex) {
//                    Log.d("filters", ex.getMessage());
//                }


//                if (filternames_txt == "none"){
//
//                    params.setColorEffect(Camera.Parameters.EFFECT_NONE);
//
//                }
//               else if (filternames_txt .equals("Aqua") ){
//
//                    params.setColorEffect(Camera.Parameters.EFFECT_AQUA);
//
//                }
//                else if (filternames_txt.equals("Black")){
//
//                    params.setColorEffect(Camera.Parameters.EFFECT_WHITEBOARD);
//
//                }
//                else if (filternames_txt .equals("Negative")){
//
//                    params.setColorEffect(Camera.Parameters.EFFECT_NEGATIVE);
//
//                }
//                else if (filternames_txt.equals("Posterize")){
//                    params.setColorEffect(Camera.Parameters.EFFECT_POSTERIZE);
//
//                }
//                else if (filternames_txt .equals("Sepia")){
//
//                    params.setColorEffect(Camera.Parameters.EFFECT_SEPIA);
//
//                }
//                else if (filternames_txt.equals("Whiteboard")){
//
//                    params.setColorEffect(Camera.Parameters.EFFECT_SOLARIZE);
//
//                }
//                camera.setParameters(params);
            }
//
//
        });

        lv = findViewById(R.id.filter_list);

        final List<FilterType> filterTypes = FilterType.createFilterList();
        lv.setHasFixedSize(true);

        lv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        filtersAdapter = new FiltersAdapter(CameraActivity.this, filterTypes);
        lv.setAdapter(filtersAdapter);


        filtersAdapter.setonFiltersClickListener(new FiltersAdapter.OnFiltersClickListener() {
            @Override
            public void onFiltersClickListener(View v, int postoin) {
                if (GPUCameraRecorder != null) {
                    GPUCameraRecorder.setFilter(FilterType.createGlFilter(filterTypes.get(postoin),getApplicationContext()));
                }
            }
        });

//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (GPUCameraRecorder != null) {
//                    GPUCameraRecorder.setFilter(FilterType.createGlFilter(filterTypes.get(position),getApplicationContext()));
//                }
//            }
//        });



        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_controls.setVisibility(View.INVISIBLE);
                filter_controls.setVisibility(View.VISIBLE);

//                Intent intent = new Intent(CameraActivity.this, FilterActivity.class);
//
//
//                startActivity(intent);

//
            }
        });


        filter_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_controls.setVisibility(View.VISIBLE);
                filter_controls.setVisibility(View.INVISIBLE);
            }
        });
//        Camera.Parameters campar=camera.getParameters();
//        params.getSupportedColorEffects(params.EFFECT_NEGATIVE,t);


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //perform upload function here
            }
        });

//        customButton.setOnTouchListener(new View.OnTouchListener() {
//
//            private Timer timer = new Timer();
//            private long LONG_PRESS_TIMEOUT = 1000;
//            private boolean wasLong = false;
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.d(getClass().getName(), "touch event: " + event.toString());
//
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    // touch & hold started
//                    timer.schedule(new TimerTask() {
//                        @Override
//                        public void run() {
//                            wasLong = true;
//                            // touch & hold was long
//                            Log.i("Click", "touch & hold was long");
//                            VideoCountDown.start();
//                            try {
//                                startRecording();
//                            } catch (IOException e) {
//                                String message = e.getMessage();
//                                Log.i(null, "Problem " + message);
//                                mediaRecorder.release();
//                                e.printStackTrace();
//                            }
//                        }
//                    }, LONG_PRESS_TIMEOUT);
//                    return true;
//                }
//
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    // touch & hold stopped
//                    timer.cancel();
//                    if (!wasLong) {
//                        // touch & hold was short
//                        Log.i("Click", "touch & hold was short");
//                        if (isFlashOn && currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
//                            params = camera.getParameters();
//                            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
//                            camera.setParameters(params);
//
//                            camera.autoFocus(new Camera.AutoFocusCallback() {
//
//                                @Override
//                                public void onAutoFocus(final boolean success, final Camera camera) {
//                                    takePicture();
//                                }
//                            });
//
//                        } else {
//                            takePicture();
//                        }
//                    } else {
//                        stopRecording();
//                        VideoCountDown.cancel();
//                        VideoSeconds = 1;
//                        customButton.setProgressWithAnimation(0);
//                        wasLong = false;
//                    }
//                    timer = new Timer();
//                    return true;
//                }
//
//                return false;
//            }
//        });


        cameraglass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (speed_control_layout.getVisibility() == View.VISIBLE) {
                    camera_controls.setVisibility(View.VISIBLE);
                    speed_control_layout.setVisibility(View.INVISIBLE);


                }
                if (filter_controls.getVisibility() == View.VISIBLE) {
                    camera_controls.setVisibility(View.VISIBLE);
                    filter_controls.setVisibility(View.INVISIBLE);


                }
                if (camera_timer_control.getVisibility() == View.VISIBLE) {
                    camera_controls.setVisibility(View.VISIBLE);
                    camera_timer_control.setVisibility(View.INVISIBLE);
                    filter.setClickable(true);
                    speedicon.setClickable(true);
                }


                return false;
            }
        });
//
//        preview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FocusCamera();
//            }
//        });

        flip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                switchCamera();
                releaseCamera();
                if (lensFacing == LensFacing.BACK) {
                    lensFacing = LensFacing.FRONT;
                } else {
                    lensFacing = LensFacing.BACK;
                }
                toggleClick = true;
            }
        });

        EditCaptureSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCaptureSwitch();
            }
        });

        editTextBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHideEditText();
            }
        });
        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHideEditText();
            }
        });
        addSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stickerOptions();
            }
        });
        editMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //StickerView.invalidate();
            }
        });


        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                take_picture.setImageResource(R.drawable.ic_photo_selected);
                take_record.setImageResource(R.drawable.ic_video);
                camera_selected = 0;
                video_progress.setVisibility(View.INVISIBLE);


            }
        });
        take_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                take_picture.setImageResource(R.drawable.ic_group_camera);
                take_record.setImageResource(R.drawable.ic_video_selected);
                camera_selected = 1;
                video_progress.setVisibility(View.VISIBLE);


            }
        });

        video_progress.enableAutoProgressView(30000);
        video_progress.setDividerColor(Color.WHITE);
        video_progress.setDividerEnabled(true);
        video_progress.setDividerWidth(4);
        video_progress.setShader(new int[]{Color.CYAN, Color.CYAN, Color.CYAN});

        video_progress.SetListener(new ProgressBarListener() {
            @Override
            public void TimeinMill(long mills) {
                sec_passed = (int) (mills / 1000);

                if (sec_passed > 30) {
                    try {
                        startRecording();
                        video_progress.resume();
                        VideoCountDown.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


//       start video recording
        start_stop.setOnClickListener(new View.OnClickListener() {
            Timer timert = new Timer();
            final long LONG_PRESS_TIMEOUTt = 1;


            @Override
            public void onClick(View v) {

                Log.e("start", String.valueOf(camera_selected));
                if (camera_selected == 0) {
//                    takePicture();


                    captureBitmap(bitmap -> {
                        new Handler().post(() -> {
                           rotatedBitmap=bitmap;
                            if (rotatedBitmap != null) {
                                setStickerView(0);
                                capturedImage.setVisibility(View.VISIBLE);
//                    Picasso.with(CameraActivity.this).load(rotatedBitmap).into(capturedImage);
                                capturedImage.setScaleType(ImageView.ScaleType.FIT_XY);
                                capturedImage.setImageBitmap(rotatedBitmap);
                                editMedia.setVisibility(View.VISIBLE);
                                captureMedia.setVisibility(View.GONE);
                                camera_edit_control.setVisibility(View.INVISIBLE);
                                camera_save_control.setVisibility(View.VISIBLE);
                                edit_imagelayout.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(CameraActivity.this, "Failed to Capture the picture. kindly Try Again:",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    });
                    Log.e("start", "picture");
                }
                if (camera_selected == 1) {
                    Log.e("waslon1", String.valueOf(wasLon));
                    if (wasLon == false) {
                        wasLon = true;
                        // touch & hold was long
                        Log.i("Click", "touch & hold was long");
                        VideoCountDown.start();
                        video_progress.resume();
                        Log.e("waslon2", String.valueOf(wasLon));
//                        try {
//                            startRecording();
                            GPUCameraRecorder.start(defaultVideo);
                            Log.e("pathssssss",defaultVideo);
                            camera_icon_lay.setVisibility(View.INVISIBLE);
                            if (camera_sounds.getText().toString().equals("sounds  ")) {

                            } else {
                                Uri ur = Uri.parse(defaultSound + "/" + SelectedAudio);
                                Log.e("defaultSound", defaultSound);
                                mediaPlayer = MediaPlayer.create(CameraActivity.this, ur);
                                mediaPlayer.start();
                            }
//                        } catch (IOException e) {
//                            String message = e.getMessage();
//                            Log.i(null, "Problem " + message);
//                            mediaRecorder.release();
//                            e.printStackTrace();
//                        }

                    } else {
                        wasLon = false;
//                        stopRecording();
                        GPUCameraRecorder.stop();
                        if (camera_sounds.getText().toString().equals("sounds  ")) {

                        } else {
                            mediaPlayer.pause();
                        }

                        VideoCountDown.cancel();
                        VideoSeconds = 1;
                        video_progress.cancel();
                        video_progress.reset();
//                        playVideo();
                        Intent intent = new Intent(CameraActivity.this, PreviewActivity.class);
                        intent.putExtra("path", defaultVideo);
                        intent.putExtra("Speed", speed_value);


                        startActivity(intent);
                        Log.e("waslon3", String.valueOf(wasLon));
                    }
                }

            }
        });
        dir = new File(CameraActivity.this.getFilesDir().getAbsolutePath(), "VA_Smash");

        if (!dir.exists()) {
            dir.mkdirs();
        }
        String ImageFileoutput = "/Smashimgoutput" + ".jpg"; //".png";
        Log.d("", "Smashimg " + ImageFileoutput);
        File file = new File(dir, ImageFileoutput);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                rotatedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
                int cam = camera_selected;
                if (cam == 0) {
                    Intent intent = new Intent(CameraActivity.this, PostcontentActivity.class);
                    // your bitmap
                    ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                    stickerView.createBitmap().compress(Bitmap.CompressFormat.JPEG, 50, _bs);
                    intent.putExtra("byteArray", _bs.toByteArray());
                    intent.putExtra("cam", cam);
                    startActivity(intent);
                    finish();
                }
                if (cam == 1) {

                    Intent intent = new Intent(CameraActivity.this, PostcontentActivity.class);

                    intent.putExtra("cam", cam);
                    intent.putExtra("path", defaultVideo);
                    startActivity(intent);
                    if (!camera_sounds.getText().toString().equals("sounds  ")){
                        Merge_withAudio();
                    }
                    finish();

                }


//                Intent intent=new Intent(CameraActivity.this, PostcontentActivity.class);
////                intent.putExtra("file",byteArray);
//                startActivity(intent);
//                File sdCard = Environment.getExternalStorageDirectory();
//                dir = new File(sdCard.getAbsolutePath() + "/Opendp");
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//
//                String timeStamp = new SimpleDateFormat("ddMMyyHHmm").format(new Date());
//                String ImageFile = "opendp-" + timeStamp + ".jpg"; //".png";
//                File file = new File(dir, ImageFile);
//
//                try {
//                    FileOutputStream fos = new FileOutputStream(file);
//                    stickerView.createBitmap().compress(Bitmap.CompressFormat.PNG, 90, fos);
//                    Bitmap myBitmap = BitmapFactory.decodeFile();
//                    outputimage.setImageBitmap(myBitmap);
////                    Intent intent=new Intent(CameraActivity.this, PostcontentActivity.class);
////                    intent.putExtra("picture", myBitmap);
////                    startActivity(intent);
//                    refreshGallery(file);
//
//                } catch (FileNotFoundException e) {
//                    Log.d("", "File not found: " + e.getMessage());
//                }

            }
        });
        one = (TextView) findViewById(R.id.one_txt_speed);
        two = (TextView) findViewById(R.id.two_txt_speed);
        three = (TextView) findViewById(R.id.three_txt_speed);
        four = (TextView) findViewById(R.id.four_txt_speed);
        five = (TextView) findViewById(R.id.five_txt_speed);


        one_view = (ImageView) findViewById(R.id.one_speed);
        two_view = (ImageView) findViewById(R.id.two_speed);
        three_view = (ImageView) findViewById(R.id.three_speed);
        four_view = (ImageView) findViewById(R.id.four_speed);
        five_view = (ImageView) findViewById(R.id.five_speed);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_view.setVisibility(View.VISIBLE);
                two_view.setVisibility(View.INVISIBLE);
                three_view.setVisibility(View.INVISIBLE);
                four_view.setVisibility(View.INVISIBLE);
                five_view.setVisibility(View.INVISIBLE);
                one.setTextSize(25);
                two.setTextSize(20);
                three.setTextSize(20);
                four.setTextSize(20);
                five.setTextSize(20);
                speed_value = 1;


            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_view.setVisibility(View.INVISIBLE);
                two_view.setVisibility(View.VISIBLE);
                three_view.setVisibility(View.INVISIBLE);
                four_view.setVisibility(View.INVISIBLE);
                five_view.setVisibility(View.INVISIBLE);
                one.setTextSize(20);
                two.setTextSize(25);
                three.setTextSize(20);
                four.setTextSize(20);
                five.setTextSize(20);
                speed_value = 2;
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_view.setVisibility(View.INVISIBLE);
                two_view.setVisibility(View.INVISIBLE);
                three_view.setVisibility(View.VISIBLE);
                four_view.setVisibility(View.INVISIBLE);
                five_view.setVisibility(View.INVISIBLE);
                one.setTextSize(20);
                two.setTextSize(20);
                three.setTextSize(25);
                four.setTextSize(20);
                five.setTextSize(20);
                speed_value = 3;

            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_view.setVisibility(View.INVISIBLE);
                two_view.setVisibility(View.INVISIBLE);
                three_view.setVisibility(View.INVISIBLE);
                four_view.setVisibility(View.VISIBLE);
                five_view.setVisibility(View.INVISIBLE);
                one.setTextSize(20);
                two.setTextSize(20);
                three.setTextSize(20);
                four.setTextSize(25);
                five.setTextSize(20);
                speed_value = 4;
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_view.setVisibility(View.INVISIBLE);
                two_view.setVisibility(View.INVISIBLE);
                three_view.setVisibility(View.INVISIBLE);
                four_view.setVisibility(View.INVISIBLE);
                five_view.setVisibility(View.VISIBLE);
                one.setTextSize(20);
                two.setTextSize(20);
                three.setTextSize(20);
                four.setTextSize(20);
                five.setTextSize(25);
                speed_value = 5;

            }
        });


        speedicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera_selected == 1) {
                    if (speed == false) {
                        speed = true;
                        speed_control_layout.setVisibility(View.VISIBLE);
                    } else {
                        speed = false;
                        speed_control_layout.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });


        customSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


                progress.setText(String.valueOf(i));
                counter = Integer.parseInt(progress.getText().toString());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        camare_starttimer = (Button) findViewById(R.id.camare_starttimer);
        final TextView count = (TextView) findViewById(R.id.count);
        final int sec = counter * 1000;
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                camera_timer_control.setVisibility(View.VISIBLE);
                camera_controls.setVisibility(View.INVISIBLE);
                filter.setClickable(false);
                speedicon.setClickable(false);

//
            }
        });
        camare_starttimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                countdown_control.setVisibility(View.VISIBLE);
                new CountDownTimer(counter * 1000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        count.setText(String.valueOf(counter));
                        counter--;

                    }

                    public void onFinish() {
                        Toast.makeText(getApplicationContext(), "count down done",
                                Toast.LENGTH_SHORT).show();
                        countdown_control.setVisibility(View.INVISIBLE);
                        camera_timer_control.setVisibility(View.INVISIBLE);

                        Timer timert = new Timer();
                        final long LONG_PRESS_TIMEOUTt = 1;


                        Log.e("start", String.valueOf(camera_selected));
                        if (camera_selected == 0) {
                            takePicture();
                            counter = 3;
                            Log.e("start", "picture");
                        }
                        if (camera_selected == 1) {
                            Log.e("waslon1", String.valueOf(wasLon));
                            camera_controls.setVisibility(View.VISIBLE);
                            if (wasLon == false) {
                                wasLon = true;
                                // touch & hold was long
                                Log.i("Click", "touch & hold was long");
                                VideoCountDown.start();
                                video_progress.resume();
                                counter = 3;
                                Log.e("waslon2", String.valueOf(wasLon));
                                try {
                                    startRecording();
                                    if (camera_sounds.getText().toString().equals("sounds  ")) {

                                    } else {
                                        Uri ur = Uri.parse(defaultSound + "/" + SelectedAudio);
                                        Log.e("defaultSound", defaultSound);
                                        mediaPlayer = MediaPlayer.create(CameraActivity.this, ur);
                                        mediaPlayer.start();
                                    }
                                    counter = 3;
                                } catch (IOException e) {
                                    String message = e.getMessage();
                                    Log.i(null, "Problem " + message);
                                    mediaRecorder.release();
                                    e.printStackTrace();
                                }

                            } else {
                                wasLon = false;
                                stopRecording();
                                if (camera_sounds.getText().toString().equals("sounds  ")) {

                                } else {
                                    mediaPlayer.pause();
                                }
                                VideoCountDown.cancel();
                                VideoSeconds = 1;
                                video_progress.pause();
                                video_progress.addDivider();
                                playVideo();



                                Log.e("waslon3", String.valueOf(wasLon));
                            }
                        }
                    }

                }.start();
            }
        });


    }

//    private void addmodeltoanchor(Anchor anchor, ModelRenderable modelRenderable) {
//        AnchorNode anchorNode=new AnchorNode(anchor);
//        TransformableNode transformableNode=new TransformableNode(arfragment.getTransformationSystem());
//        transformableNode.setParent(anchorNode);
//        transformableNode.setRenderable(modelRenderable);
//        arfragment.getArSceneView().getScene().addChild(anchorNode);
//        transformableNode.select();
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file

            if (requestCode == video) {
                if (upload_video_edit.equals("1")){
                    uri2 = data.getData();
                    srcFile = getPath(uri2);
                    File myFile = new File(CameraActivity.this.getFilesDir().getAbsolutePath());
                    defaultVideo = myFile.getAbsolutePath();
                    Log.e("ddddddd", defaultVideo);
                    camera_upload_edit.setVisibility(View.VISIBLE);


                    Log.e("ddddddd", srcFile);
                    tileView.post(new Runnable() {
                        @Override
                        public void run() {
                            setBitmap(Uri.parse(srcFile));
                            mVideoView.setVideoURI(Uri.parse(srcFile));
                        }
                    });

                }
                else {
                    Uri uri = data.getData();
                    srcFile = getPath(uri);
                    File myFile = new File(CameraActivity.this.getFilesDir().getAbsolutePath());
                    defaultVideo = myFile.getAbsolutePath();
                    //Log.e("ddddddd", defaultVideo);

                    captured_video1.setVisibility(View.VISIBLE);
                    edit_videolayout.setVisibility(View.VISIBLE);
                    editMedia.setVisibility(View.VISIBLE);
                    captureMedia.setVisibility(View.GONE);
                    camera_save_control.setVisibility(View.VISIBLE);
                    camera_edit_control.setVisibility(View.INVISIBLE);
                    captured_video1.setVideoURI(uri);
                    captured_video1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                        }
                    });
                    captured_video1.start();
                    preview.setVisibility(View.INVISIBLE);
                    setStickerView(1);
//            String uriString = uri.toString();
//            File myFile = new File(uriString);
//            String path = myFile.getAbsolutePath();
//            String displayName = String.valueOf(Calendar.getInstance().getTimeInMillis()+".mp4");
//            Log.d("ooooooo",displayName);
//            uploadPDF(displayName,uri);
                }
            }


            if (requestCode == photo) {
                Uri uri = data.getData();
                String path = uri.toString();

                File myFile = new File(CameraActivity.this.getFilesDir().getAbsolutePath());
                defaultVideo = myFile.getAbsolutePath();
                Log.e("ddddddd", String.valueOf(uri));
                try {
                    rotatedBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                } catch (Exception e) {
                    //handle exception
                }
//                if (rotatedBitmap != null) {
                setStickerView(0);
                edit_imagelayout.setVisibility(View.VISIBLE);
                capturedImage.setVisibility(View.VISIBLE);
                capturedImage.setImageBitmap(rotatedBitmap);
                editMedia.setVisibility(View.VISIBLE);
                captureMedia.setVisibility(View.GONE);
                camera_save_control.setVisibility(View.VISIBLE);
                camera_edit_control.setVisibility(View.INVISIBLE);
                Log.i("Image bitmap", rotatedBitmap.toString() + "-");
//                } else {
//                    Toast.makeText(CameraActivity.this, "Failed to Capture the picture. kindly Try Again:",
//                            Toast.LENGTH_LONG).show();
//                }


            }


        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    @SuppressLint("NewApi")
    public void GetPermission() {

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!hasPermission(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
            finish();
        }
    }

    public static boolean hasPermission(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    CountDownTimer VideoCountDown = new CountDownTimer(30000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            VideoSeconds++;
            int VideoSecondsPercentage = VideoSeconds * 10;
            customButton.setProgressWithAnimation(VideoSecondsPercentage);
        }

        @Override
        public void onFinish() {
            stopRecording();
            customButton.setProgress(0);
            VideoSeconds = 0;
        }
    };

    public void FocusCamera() {
        if (camera.getParameters().getFocusMode().equals(
                Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
        } else {
            camera.autoFocus(new Camera.AutoFocusCallback() {

                @Override
                public void onAutoFocus(final boolean success, final Camera camera) {
                }
            });
        }
    }


    //take a picture

    private void takePicture() {
        params = camera.getParameters();
        List<Camera.Size> sizes = params.getSupportedPictureSizes();

        List<Integer> list = new ArrayList<Integer>();
        for (Camera.Size size : params.getSupportedPictureSizes()) {
            //Log.i("ASDF", "Supported Picture: " + size.width + "x" + size.height);
            list.add(size.height);
        }

        Camera.Size cs = sizes.get(closest(900, list));
        //Log.i("Width x Height", cs.width+"x"+cs.height);
        params.setPictureSize(cs.width, cs.height); //1920, 1080

        //params.setRotation(90);
        camera.setParameters(params);
        camera.takePicture(null, null, new Camera.PictureCallback() {

            @Override
            public void onPictureTaken(byte[] data, final Camera camera) {
                Bitmap bitmap;
                Matrix matrix = new Matrix();

                //if (bitmap.getWidth() > bitmap.getHeight()) {
                if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    matrix.postRotate(90);
                } else {
                    Matrix matrixMirrory = new Matrix();
                    float[] mirrory = {-1, 0, 0, 0, 1, 0, 0, 0, 1};
                    matrixMirrory.setValues(mirrory);
                    matrix.postConcat(matrixMirrory);
                    matrix.postRotate(90);
                }
                bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                /*} else {
                    rotatedBitmap = bitmap;
                }*/

                if (rotatedBitmap != null) {
                    setStickerView(0);
                    capturedImage.setVisibility(View.VISIBLE);
//                    Picasso.with(CameraActivity.this).load(rotatedBitmap).into(capturedImage);
                    capturedImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    capturedImage.setImageBitmap(rotatedBitmap);
                    editMedia.setVisibility(View.VISIBLE);
                    captureMedia.setVisibility(View.GONE);
                    camera_edit_control.setVisibility(View.INVISIBLE);
                    camera_save_control.setVisibility(View.VISIBLE);
                    edit_imagelayout.setVisibility(View.VISIBLE);



                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    camera.setParameters(params);
                    //Log.i("Image bitmap", rotatedBitmap.toString()+"-");
                } else {
                    Toast.makeText(CameraActivity.this, "Failed to Capture the picture. kindly Try Again:",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    protected void startRecording() throws IOException {
        if (camera == null) {
            camera = Camera.open(currentCameraId);
            //Log.i("Camera", "Camera open");
        }
        params = camera.getParameters();

        if (isFlashOn && currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
//            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

            camera.setParameters(params);
//            SurfaceTexture surfaceTexture = new SurfaceTexture(0);
//            camera.setPreviewTexture(surfaceTexture);
        }

        mediaRecorder = new MediaRecorder();
        camera.lock();
        camera.unlock();
        // Please maintain sequence of following code.
        // If you change sequence it will not work.
        mediaRecorder.setCamera(camera);
//
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setPreviewDisplay(previewHolder.getSurface());
        if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            mediaRecorder.setOrientationHint(270);
        } else {
            mediaRecorder.setOrientationHint(setCameraDisplayOrientation(this, currentCameraId, camera));
        }


        // Log.e("speed", String.valueOf(speed_value));


        if (speed_value == 1) {
//            profile = CamcorderProfile.get(currentCameraId,CamcorderProfile.QUALITY_HIGH_SPEED_480P);
//            profile.videoFrameWidth = 640;
//            profile.videoFrameHeight = 480;
//            profile.videoCodec = MediaRecorder.VideoEncoder.H264;
//            profile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;
//            profile.videoBitRate= 15;

            mediaRecorder.setCaptureRate(40f);
//            mediaRecorder.setCaptureRate(profile.videoFrameRate / 6.0f);
//            mediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
//            mediaRecorder.setVideoFrameRate(30);
//            mediaRecorder.setProfile(profile);


//            mediaRecorder.setVideoEncodingBitRate(3000000);
//            mediaRecorder.setVideoFrameRate(20);

        } else if (speed_value == 2) {
//            profile = CamcorderProfile.get(currentCameraId,CamcorderProfile.QUALITY_HIGH);
//            mediaRecorder.setCaptureRate(profile.videoFrameRate / 0.25f);
//            mediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
//            mediaRecorder.setProfile(profile);
//            mediaRecorder.setVideoEncodingBitRate(3000000);
//            mediaRecorder.setVideoFrameRate(25);
            mediaRecorder.setCaptureRate(35f);
        } else if (speed_value == 3) {
//            profile = CamcorderProfile.get(currentCameraId,CamcorderProfile.QUALITY_LOW);
//            mediaRecorder.setCaptureRate(profile.videoFrameRate / 0.25f);
//            mediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
//            mediaRecorder.setProfile(profile);
//            mediaRecorder.setVideoEncodingBitRate(3000000);
//            mediaRecorder.setVideoFrameRate(30);
            mediaRecorder.setCaptureRate(30f);
        } else if (speed_value == 4) {
//            profile = CamcorderProfile.get(currentCameraId,CamcorderProfile.QUALITY_LOW);
//            mediaRecorder.setCaptureRate(profile.videoFrameRate / 0.25f);
//            mediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
//            mediaRecorder.setProfile(profile);
//            mediaRecorder.setVideoEncodingBitRate(3000000);
//            mediaRecorder.setVideoFrameRate(150);
            mediaRecorder.setCaptureRate(25f);
        } else if (speed_value == 5) {
//            profile = CamcorderProfile.get(currentCameraId,CamcorderProfile.QUALITY_HIGH_SPEED_480P);
//            mediaRecorder.setCaptureRate(profile.videoFrameRate / 0.25f);
//            mediaRecorder.setVideoEncodingBitRate(profile.videoBitRate);
//            mediaRecorder.setProfile(profile);
//            mediaRecorder.setVideoEncodingBitRate(3000000);
//            mediaRecorder.setVideoFrameRate(240);
            mediaRecorder.setCaptureRate(20f);

        }


        mediaRecorder.setVideoEncodingBitRate(3000000);
        mediaRecorder.setVideoFrameRate(30);


        List<Integer> list = new ArrayList<Integer>();
        List<Camera.Size> VidSizes = params.getSupportedVideoSizes();
        if (VidSizes == null) {
            // Log.i("Size length", "is null");
            mediaRecorder.setVideoSize(640, 1280);
        } else {
            //Log.i("Size length", "is NOT null");
            for (Camera.Size sizesx : params.getSupportedVideoSizes()) {
                //Log.i("ASDF", "Supported Video: " + sizesx.width + "x" + sizesx.height);
                list.add(sizesx.height);
            }
            Camera.Size cs = VidSizes.get(closest(1280, list));
            //Log.i("Width x Height", cs.width + "x" + cs.height);
            mediaRecorder.setVideoSize(cs.width, cs.height);
        }

        mediaRecorder.setOutputFile(defaultVideo);
        //Log.e("dir", defaultVideo);
        mediaRecorder.prepare();
        isRecording = true;
        mediaRecorder.start();
    }


    // stop recording video

    public void stopRecording() {
        if (isRecording) {
            try {
//                params = camera.getParameters();
//                params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
//                camera.setParameters(params);

                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;
                playVideo();
                Log.e("isrecord", String.valueOf(mediaRecorder) + isRecording);
            } catch (RuntimeException stopException) {
                Log.i("Stop Recoding", "Too short video");
                takePicture();
            }
            camera.lock();
        } else {
            Log.i("Stop Recoding", "isRecording is true");
        }
    }

   // play recorded video

    public void playVideo() {
        edit_videolayout.setVisibility(View.VISIBLE);

        editMedia.setVisibility(View.VISIBLE);
        captureMedia.setVisibility(View.GONE);
        camera_save_control.setVisibility(View.VISIBLE);
        camera_edit_control.setVisibility(View.INVISIBLE);

        uri = Uri.parse(defaultVideo);
        if(landscape=="0"){
            Log.e("11111",landscape);
            videoView.setVisibility(View.VISIBLE);
            videoView.setVideoURI(uri);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            videoView.start();
           // preview.setVisibility(View.INVISIBLE);
            setStickerView(1);
        }
        else{
            Log.e("11111",landscape);
            captured_video1.setVisibility(View.VISIBLE);
            captured_video1.setVideoURI(uri);
            captured_video1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setLooping(true);
                }
            });
            captured_video1.start();
            preview.setVisibility(View.INVISIBLE);
            setStickerView(1);
        }
    }



   // save the video in draft


    public void saveMedia() throws IOException {
        if (!videoView.isShown()) {
            // Toast.makeText(this, "Saving...", Toast.LENGTH_SHORT).show();
            File sdCard = Environment.getExternalStorageDirectory();
            dir = new File(CameraActivity.this.getFilesDir().getAbsolutePath(), "VA_Smash");

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String timeStamp = new SimpleDateFormat("ddMMyyHHmm").format(new Date());
            ImageFile = "/Smashimg" + ".jpg"; //".png";
            Log.d("", "Smashimg " + ImageFile);
            File file = new File(dir, ImageFile);

            try {
                FileOutputStream fos = new FileOutputStream(file);
                stickerView.createBitmap().compress(Bitmap.CompressFormat.PNG, 90, fos);
                refreshGallery(file);
                // Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                // Toast.makeText(this, "Error saving!", Toast.LENGTH_LONG).show();
                Log.d("", "File not found: " + e.getMessage());
            }
        } else {
            if (defaultVideo != null) {
                String timeStamp = new SimpleDateFormat("ddMMyyHHmm").format(new Date());
                String VideoFile = "opendp-" + timeStamp + ".mp4";

                File from = new File(defaultVideo);
                File to = new File(dir, VideoFile);

                InputStream in = new FileInputStream(from);
                OutputStream out = new FileOutputStream(to);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                refreshGallery(to);

                dir = new File(CameraActivity.this.getFilesDir().getAbsolutePath(), "VA_Smash");

                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String ImageFileoutput = "/Smashimgoutput" + ".jpg"; //".png";
                Log.d("", "Smashimg " + ImageFileoutput);
                File file = new File(dir, ImageFileoutput);

                String filePath = SiliCompressor.with(CameraActivity.this).compress(ImageFile, new File(ImageFileoutput));

                //  Toast.makeText(this, "Saved!", Toast.LENGTH_LONG).show();
            } else {
                // Toast.makeText(this, "Error saving!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void refreshGallery(File file) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(Uri.fromFile(file));
        sendBroadcast(mediaScanIntent);
    }

    public void FlashControl(View v) {
        Log.i("Flash", "Flash button clicked!");
        boolean hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (!hasFlash) {
            AlertDialog alert = new AlertDialog.Builder(CameraActivity.this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
            return;
        } else {

            if (!isFlashOn) {
                isFlashOn = true;
                flashButton.setImageResource(R.drawable.ic_flash_on_shadow);
                Log.i("Flash", "Flash On");

            } else {
                isFlashOn = false;
                flashButton.setImageResource(R.drawable.ic_flash_off_shadow);
                Log.i("Flash", "Flash Off");
            }
        }
    }

    public void switchCamera() {
        if (!isRecording) {
            if (camera.getNumberOfCameras() != 1) {
                camera.release();
                if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                } else {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                camera = Camera.open(currentCameraId);
                try {
                    camera.setPreviewDisplay(previewHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                startPreview();
            }
        } else {
            Log.i("Switch Camera", "isRecording true");
        }
    }

    public void EditCaptureSwitch() {
        preview.setVisibility(View.VISIBLE);
        captureMedia.setVisibility(View.VISIBLE);
        camera_controls.setVisibility(View.VISIBLE);
        //capturedImage.setImageResource(android.R.color.transparent);
        startPreview(); //onResume();
        capturedImage.setVisibility(View.GONE);
        edit_imagelayout.setVisibility(View.GONE);
        editMedia.setVisibility(View.GONE);
        videoView.setVisibility(View.GONE);
        edit_videolayout.setVisibility(View.GONE);
        recreate();
    }

    @Override
    public void onBackPressed() {
        landscape="0";
        if (selectSticker.getVisibility() == View.VISIBLE) {
            stickerOptions();
        } else if (editTextBody.getVisibility() == View.VISIBLE) {
            showHideEditText();
        } else if (editMedia.getVisibility() == View.VISIBLE) {
            EditCaptureSwitch();
            removeAllStickers();
        } else {
            finish();
        }
    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();


        setUpCamera();
//        camera = Camera.open(currentCameraId);
//        try {
//            camera.setPreviewDisplay(previewHolder);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        startPreview();
//        //FocusCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releaseCamera();

    }
    //    @Override
//    public void onPause() {
//        if (inPreview) {
//            camera.stopPreview();
//        }
//
//        camera.release();
//        camera = null;
//        inPreview = false;
//
//        super.onPause();
//    }

    public int closest(int of, List<Integer> in) {
        int min = Integer.MAX_VALUE;
        int closest = of;
        int position = 0;
        int i = 0;

        for (int v : in) {
            final int diff = Math.abs(v - of);
            i++;

            if (diff < min) {
                min = diff;
                closest = v;
                position = i;
            }
        }
        int rePos = position - 1;
        Log.i("Value", closest + "-" + rePos);
        return rePos;
    }

    private void initPreview() {
        if (camera != null && previewHolder.getSurface() != null) {
            try {
                camera.stopPreview();
                camera.setPreviewDisplay(previewHolder);
            } catch (Throwable t) {
                //Log.e("Preview:surfaceCallback", "Exception in setPreviewDisplay()", t);
                // Toast.makeText(CameraActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }

            if (!cameraConfigured) {

                Camera.Parameters parameters = camera.getParameters();
                List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();

                List<Integer> list = new ArrayList<Integer>();
                for (int i = 0; i < sizes.size(); i++) {
                    //Log.i("ASDF", "Supported Preview: " + sizes.get(i).width + "x" + sizes.get(i).height);
                    list.add(sizes.get(i).width);
                }
                Camera.Size cs = sizes.get(closest(1920, list));

                //Log.i("Width x Height", cs.width + "x" + cs.height);

                parameters.setPreviewSize(cs.width, cs.height);
                camera.setParameters(parameters);
                cameraConfigured = true;
            }
        }
    }

    private void startPreview() {
        if (cameraConfigured && camera != null) {
            camera.setDisplayOrientation(setCameraDisplayOrientation(this, currentCameraId, camera));
            camera.startPreview();
            inPreview = true;
        }
    }

    private int setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        public void surfaceCreated(SurfaceHolder holder) {
            // no-op -- wait until surfaceChanged()
        }

        public void surfaceChanged(SurfaceHolder holder,
                                   int format, int width,
                                   int height) {
            initPreview();
            startPreview();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            // no-op
        }
    };
//    public void sounddialog(){
//
//        AlertDialog.Builder builder;
//        final Context mContext = this;
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.sounds_list,(ViewGroup) findViewById(R.id.root));
//
//
////            TextView id=(TextView)findViewById(R.id.catog_id);
////            TextView city=(TextView)findViewById(R.id.gridtxt);
////            ImageView icon=(ImageView)findViewById(R.id.gridimg) ;
//
//
//
//
//
//
//        sound_list = (ListView) layout.findViewById(R.id.sounds_listview);
//
//
//        soundlist=new ArrayList<>();
//        soundlist.add("sound 1");
//        soundlist.add("sound 2");
//        soundlist.add("sound 3");
//        soundlist.add("sound 4");
//        soundlist.add("sound 5");
//
//
//
//        soundsAdapter = new SoundsAdapter(this, soundlist);
//        sound_list.setAdapter(soundsAdapter);
//
//
//        sound_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                mediaPlayer = MediaPlayer.create(CameraActivity.this, R.raw.one);
//                Log.e("sound",soundlist.get(position));
//                soundeselected=soundlist.get(position);
////                if (soundlist.get(position).equals("sound 1")){
////                    if(!mediaPlayer.isPlaying()) {
////                        mediaPlayer = MediaPlayer.create(CameraActivity.this, R.raw.one);
////                        mediaPlayer.start();
////                    }
////                    else {
////                        mediaPlayer.stop();
////                    }
////
////                }
////                else if (soundlist.get(position).equals("sound 2")){
////                    if(!mediaPlayer.isPlaying()) {
////                    mediaPlayer=MediaPlayer.create(CameraActivity.this,R.raw.two);
////                    mediaPlayer.start();}
////                    else {
////                        mediaPlayer.stop();
////                    }
////                }
////                else if (soundlist.get(position).equals("sound 3")){
////                    if(!mediaPlayer.isPlaying()) {
////                    mediaPlayer=MediaPlayer.create(CameraActivity.this,R.raw.three);
////                    mediaPlayer.start();}
////                    else {
////                        mediaPlayer.stop();
////                    }
////                }
////                else if (soundlist.get(position).equals("sound 4")){
////                    if(!mediaPlayer.isPlaying()) {
////                    mediaPlayer=MediaPlayer.create(CameraActivity.this,R.raw.one);
////                    mediaPlayer.start();}
////                    else {
////                        mediaPlayer.stop();
////                    }
////                }
////                else if (soundlist.get(position).equals("sound 5")){
////                    if(!mediaPlayer.isPlaying()) {
////                    mediaPlayer=MediaPlayer.create(CameraActivity.this,R.raw.two);
////                    mediaPlayer.start();}
////                    else {
////                        mediaPlayer.stop();
////                    }
////                }
////                else {
////
////
////                    mediaPlayer.stop();
////                }
//
//                camera_sounds.setText(soundeselected);
//
//                dialog.cancel();
//            }
//        });
//
//        builder = new AlertDialog.Builder(mContext);
//        builder.setView(layout);
//        dialog = builder.create();
//        dialog.show();
//
//    }


    //sounds api
    private void jsongetsoundtrends() {
        Log.d("soundid", "store data" + APIs.Soundapitrends+audio_lang_id+"&skip=10");
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, APIs.Soundapitrends+audio_lang_id+"&skip=10", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Responsestoredata0000", response.toString());

                        soundlist = new ArrayList<>();

                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);
                                    String iii=employee.getString("contentLangId");

                                    Log.d("iii", "createddateL:::" + iii);
                                    JSONArray js=new JSONArray(iii);

                                    for(int z = 0; z < js.length(); z++)
                                    {
                                        JSONObject sund = js.getJSONObject(z);

                                        Sound_modelclass sound_modelclass = new Sound_modelclass();
                                        String _id = sund.getString("url");
                                        String name = sund.getString("name");
                                        boolean sel = false;

                                        sound_modelclass.setSound_code(_id);
                                        sound_modelclass.setSound_name(name);
                                        sound_modelclass.setSelected(sel);
                                        soundlist.add(sound_modelclass);


                                        Log.d("sound1", "createddateL:::" + soundlist);
                                        Log.d("sound1", "soundlst:::" + _id + name);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            soundsAdapter = new SoundsAdapter(CameraActivity.this, soundlist);
                            sounds_listview.setAdapter(soundsAdapter);
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

                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            Toast.makeText(CameraActivity.this, message, Toast.LENGTH_SHORT).show();
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

                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            //Toast.makeText(GACalculator.this, message, Toast.LENGTH_SHORT).show();

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

                headers.put("Content-Type","application/json");
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

            }
        });

    }
    private void jsongetsound(String soundid) {
        Log.d("soundid", "store data" + APIs.Soundapicateid+audio_lang_id+"&categoryId="+soundid+"&skip=10");
        // prepare the Request
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, APIs.Soundapicateid+soundid, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Responsestoredata0000", response.toString());

                        soundlist = new ArrayList<>();

                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);
                                    String iii=employee.getString("sounds");

                                    Log.d("iii", "createddateL:::" + iii);
                                    JSONArray js=new JSONArray(iii);

                                    for(int z = 0; z < js.length(); z++)
                                    {
                                        JSONObject sund = js.getJSONObject(z);

                                        Sound_modelclass sound_modelclass = new Sound_modelclass();
                                        String _id = sund.getString("url");
                                        String name = sund.getString("name");
                                        boolean sel = false;

                                        sound_modelclass.setSound_code(_id);
                                        sound_modelclass.setSound_name(name);
                                        sound_modelclass.setSelected(sel);
                                        soundlist.add(sound_modelclass);


                                        Log.d("sound1", "createddateL:::" + soundlist);
                                        Log.d("sound1", "soundlst:::" + _id + name);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            soundsAdapter = new SoundsAdapter(CameraActivity.this, soundlist);
                            sounds_listview.setAdapter(soundsAdapter);
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

                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            Toast.makeText(CameraActivity.this, message, Toast.LENGTH_SHORT).show();
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

                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            //Toast.makeText(GACalculator.this, message, Toast.LENGTH_SHORT).show();

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

                headers.put("Content-Type","application/json");
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

            }
        });

    }


    public void soundlistcate() {
//        spinner2.setVisibility(View.VISIBLE);

        mQueue = Volley.newRequestQueue(CameraActivity.this.getApplicationContext());

        // prepare the Request
        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, APIs.Soundapicate, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("soundresponse1", response.toString());

//                        sounds_listview = (ListView) layout.findViewById(R.id.sounds_listview);


                        personUtils11 = new ArrayList<>();

                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);

                                    Sound_catemodel Sound_catemodel = new Sound_catemodel();
                                    String _id = employee.getString("_id");
                                    String name = employee.getString("name");
                                    boolean sel = false;

                                    Sound_catemodel.setSound_code(_id);
                                    Sound_catemodel.setSound_cate(name);
                                    Sound_catemodel.setSelected(sel);
                                    personUtils11.add(Sound_catemodel);


                                    Log.d("sound", "createddateL1:::" + personUtils11);
                                    Log.d("sound", "soundlst1:::" + _id + name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            sounds_cate_adapter = new Sounds_Cate_Adapter(CameraActivity.this, personUtils11);
                            camera_sounds_cate.setAdapter(sounds_cate_adapter);

                            sounds_cate_adapter.setonSoundcateClickListener(new Sounds_Cate_Adapter.OnSoundcateClickListener() {
                                @Override
                                public void onSoundcateClickListener(Sound_catemodel code) {

                                    sound_cate_code=code.getSound_code();
                                    Log.e("sound_cate_code",sound_cate_code);
                                    jsongetsound(sound_cate_code);
                                }
                            });
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d("Error.Response", error.toString());
                        String body;
                        //get status code here
                        //Log.d("statusCode", "---" + error.toString());
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 422:
                                    try {
                                        body = new String(error.networkResponse.data, "UTF-8");
                                        Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            //Toast.makeText(AddUsers.this, message, Toast.LENGTH_SHORT).show();


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
        );

        // add it to the RequestQueue
        mQueue.add(getRequest);


    }

    //download sound

    public void Down_load_mp3(String url) {

        final ProgressDialog progressDialog = new ProgressDialog(CameraActivity.this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();


        dir = new File(CameraActivity.this.getFilesDir().getAbsolutePath(), "VA_Smash");
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
                Log.e("sound", "done");
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
                Log.e("sound", String.valueOf(error));
            }


        });

    }

    //merge sound with video

    public void Merge_withAudio() {

        String root = Environment.getExternalStorageDirectory().toString();
        String audio_file;
        audio_file = defaultSound + SelectedAudio;

        String video = defaultVideo+"/VA_Smash/Smash.mp4";
        String finaloutput = defaultVideo+"/VA_Smash/Smash.mp4";

        Merge_Video_Audio merge_video_audio = new Merge_Video_Audio(CameraActivity.this);
        merge_video_audio.doInBackground(audio_file, video, finaloutput);

    }

    @Override
    protected void onDestroy() {

        if (mediaPlayer == null) {

        } else {
            clearMediaPlayer();
        }
        super.onDestroy();
    }

    private void clearMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.release();
//        mediaPlayer = null;
    }




    private void setBitmap(Uri mVideoUri) {
        tileView.setVideo(mVideoUri);
        Log.e("mVideoUri", String.valueOf(mVideoUri));
    }

    private void onVideoPrepared(@NonNull MediaPlayer mp) {
        // Adjust the size of the video
        // so it fits on the screen
        //TODO manage proportion for video
        /*int videoWidth = mp.getVideoWidth();
        int videoHeight = mp.getVideoHeight();
        float videoProportion = (float) videoWidth / (float) videoHeight;
        int screenWidth = rlVideoView.getWidth();
        int screenHeight = rlVideoView.getHeight();
        float screenProportion = (float) screenWidth / (float) screenHeight;
        ViewGroup.LayoutParams lp = mVideoView.getLayoutParams();

        if (videoProportion > screenProportion) {
            lp.width = screenWidth;
            lp.height = (int) ((float) screenWidth / videoProportion);
        } else {
            lp.width = (int) (videoProportion * (float) screenHeight);
            lp.height = screenHeight;
        }
        mVideoView.setLayoutParams(lp);*/

        mDuration = mVideoView.getDuration() / 1000;
        setSeekBarPosition();
    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (seekBarVideo.getProgress() >= seekBarVideo.getMax()) {
                seekBarVideo.setProgress((mVideoView.getCurrentPosition() - mStartPosition * 1000));
                txtVideoLength.setText(milliSecondsToTimer(seekBarVideo.getProgress()) + "");
                mVideoView.seekTo(mStartPosition * 1000);
                mVideoView.pause();
                seekBarVideo.setProgress(0);
                txtVideoLength.setText("00:00");
                imgPlay.setBackgroundResource(R.drawable.ic_play);
            } else {
                seekBarVideo.setProgress((mVideoView.getCurrentPosition() - mStartPosition * 1000));
                txtVideoLength.setText(milliSecondsToTimer(seekBarVideo.getProgress()) + "");
                mHandler.postDelayed(this, 100);
            }
        }
    };

    private void setSeekBarPosition() {

        if (mDuration >= mMaxDuration) {
            mStartPosition = 0;
            mEndPosition = mMaxDuration;

            mCustomRangeSeekBarNew.setThumbValue(0, (mStartPosition * 100) / mDuration);
            mCustomRangeSeekBarNew.setThumbValue(1, (mEndPosition * 100) / mDuration);

        } else {
            mStartPosition = 0;
            mEndPosition = mDuration;
        }


        mTimeVideo = mDuration;
        mCustomRangeSeekBarNew.initMaxWidth();
        seekBarVideo.setMax(mMaxDuration * 1000);
        mVideoView.seekTo(mStartPosition * 1000);

        String mStart = mStartPosition + "";
        if (mStartPosition < 10)
            mStart = "0" + mStartPosition;

        int startMin = Integer.parseInt(mStart) / 60;
        int startSec = Integer.parseInt(mStart) % 60;

        String mEnd = mEndPosition + "";
        if (mEndPosition < 10)
            mEnd = "0" + mEndPosition;

        int endMin = Integer.parseInt(mEnd) / 60;
        int endSec = Integer.parseInt(mEnd) % 60;

        txtVideoTrimSeconds.setText(String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));
    }

    /**
     * called when playing video completes
     */
    private void onVideoCompleted() {
        mHandler.removeCallbacks(mUpdateTimeTask);
        seekBarVideo.setProgress(0);
        mVideoView.seekTo(mStartPosition * 1000);
        mVideoView.pause();
        imgPlay.setBackgroundResource(R.drawable.ic_play);
    }

    /**
     * Handle changes of left and right thumb movements
     *
     * @param index index of thumb
     * @param value value
     */
    private void onSeekThumbs(int index, float value) {
        switch (index) {
            case BarThumb.LEFT: {
                mStartPosition = (int) ((mDuration * value) / 100L);
                mVideoView.seekTo(mStartPosition * 1000);
                break;
            }
            case BarThumb.RIGHT: {
                mEndPosition = (int) ((mDuration * value) / 100L);
                break;
            }
        }
        mTimeVideo = (mEndPosition - mStartPosition);
        seekBarVideo.setMax(mTimeVideo * 1000);
        seekBarVideo.setProgress(0);
        mVideoView.seekTo(mStartPosition * 1000);

        String mStart = mStartPosition + "";
        if (mStartPosition < 10)
            mStart = "0" + mStartPosition;

        int startMin = Integer.parseInt(mStart) / 60;
        int startSec = Integer.parseInt(mStart) % 60;

        String mEnd = mEndPosition + "";
        if (mEndPosition < 10)
            mEnd = "0" + mEndPosition;
        int endMin = Integer.parseInt(mEnd) / 60;
        int endSec = Integer.parseInt(mEnd) % 60;

        txtVideoTrimSeconds.setText(String.format(Locale.US, "%02d:%02d - %02d:%02d", startMin, startSec, endMin, endSec));

    }

    private void onStopSeekThumbs() {
//        mMessageHandler.removeMessages(SHOW_PROGRESS);
//        mVideoView.pause();
//        mPlayView.setVisibility(View.VISIBLE);
    }


    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString;
        String minutesString;


        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }

        finalTimerString = finalTimerString + minutesString + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }


    public void uploadeditedvideo(){
        if (mVideoView.isPlaying()) {

            mVideoView.stopPlayback();
        }
        int diff = mEndPosition - mStartPosition;
        if (diff < 3) {
            Toast.makeText(CameraActivity.this, getString(R.string.video_length_validation), Toast.LENGTH_LONG).show();
        } else {
            MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(CameraActivity.this, Uri.parse(srcFile));
            Log.d("Generated file path", "Generated file path " + srcFile+"   "+defaultVideo+"/VA_Smash/Smash.mp4");
            final File file = new File(srcFile);

            //notify that video trimming started
            if (mOnVideoTrimListener != null)
                mOnVideoTrimListener.onTrimStarted();
            Log.d("defaultVideoqqqqqqqq",  defaultVideo+"/VA_Smash/Smash.mp4"+"   "+file);
            BackgroundTask.execute(
                    new BackgroundTask.Task("", 0L, "") {
                        @Override
                        public void execute() {
                            try {

                                Utility.startTrim(file, defaultVideo, mStartPosition * 1000, mEndPosition * 1000, mOnVideoTrimListener);
                                Log.d("defaultVideoqqqqqqqq",  defaultVideo+"   "+file);



                            } catch (final Throwable e) {
                                Thread.getDefaultUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                            }
                        }
                    }
            );







        }



    }




    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getPath(Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(this, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(this, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return "";
    }

    private String getDataColumn(Context context, Uri uri, String selection,
                                 String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            int currentApiVersion = Build.VERSION.SDK_INT;
            //TODO changes to solve gallery video issue
            if (currentApiVersion > Build.VERSION_CODES.M && uri.toString().contains(getString(R.string.app_provider))) {
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (cursor.getString(column_index) != null) {
                        String state = Environment.getExternalStorageState();
                        File file;
                        if (Environment.MEDIA_MOUNTED.equals(state)) {
                            file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", cursor.getString(column_index));
                        } else {
                            file = new File(context.getFilesDir(), cursor.getString(column_index));
                        }
                        return file.getAbsolutePath();
                    }
                    return "";
                }
            } else {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                        null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int column_index = cursor.getColumnIndexOrThrow(column);
                    if (cursor.getString(column_index) != null) {
                        return cursor.getString(column_index);
                    }
                    return "";
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return "";
    }

    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }





    OnVideoTrimListener mOnVideoTrimListener = new OnVideoTrimListener() {
        @Override
        public void onTrimStarted() {
            // Create an indeterminate progress dialog
            mProgressDialog = new ProgressDialog(CameraActivity.this);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setTitle("Saving....");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        public void getResult(Uri uri) {
            mProgressDialog.dismiss();

            Log.e("getResult", String.valueOf(uri));


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    edit_videolayout.setVisibility(View.VISIBLE);
                    videoView.setVisibility(View.VISIBLE);
                    editMedia.setVisibility(View.VISIBLE);
                    captureMedia.setVisibility(View.GONE);
                    camera_save_control.setVisibility(View.VISIBLE);
                    camera_edit_control.setVisibility(View.INVISIBLE);
                    videoView.setVideoURI(uri);
                    camera_upload_edit.setVisibility(View.INVISIBLE);

                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setLooping(true);
                        }
                    });
                    videoView.start();
                    preview.setVisibility(View.INVISIBLE);
                    setStickerView(1);

                }
            });


        }

        @Override
        public void cancelAction() {
            mProgressDialog.dismiss();
        }

        @Override
        public void onError(String message) {
            mProgressDialog.dismiss();
        }
    };

    void setupCameraPreviewView() {
        renderer = new CameraRenderer(this);
        textureView = new TextureView(this);
        container.addView(textureView);
        textureView.setSurfaceTextureListener(renderer);

        // Show original frame when touch the view
        textureView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        renderer.setSelectedFilter(R.id.filter0);
                        break;

                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        renderer.setSelectedFilter(filterId);
                        break;
                }
                return true;
            }
        });

        textureView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                renderer.onSurfaceTextureSizeChanged(null, v.getWidth(), v.getHeight());
            }
        });

    }








    private void releaseCamera() {
        if (sampleGLView != null) {
            sampleGLView.onPause();
        }

        if (GPUCameraRecorder != null) {
            GPUCameraRecorder.stop();
            GPUCameraRecorder.release();
            GPUCameraRecorder = null;
        }

        if (sampleGLView != null) {
            ((FrameLayout) findViewById(R.id.wrap_view)).removeView(sampleGLView);
            sampleGLView = null;
        }
    }


    private void setUpCameraView() {
        runOnUiThread(() -> {
            FrameLayout frameLayout = findViewById(R.id.wrap_view);
            frameLayout.removeAllViews();
            sampleGLView = null;
            sampleGLView = new SampleCameraGLView(getApplicationContext());
            sampleGLView.setTouchListener((event, width, height) -> {
                if (GPUCameraRecorder == null) return;
                GPUCameraRecorder.changeManualFocusPoint(event.getX(), event.getY(), width, height);
            });
            frameLayout.addView(sampleGLView);
        });
    }


    private void setUpCamera() {
        setUpCameraView();

        GPUCameraRecorder = new GPUCameraRecorderBuilder(this,sampleGLView)
                //.recordNoFilter(true)
                .cameraRecordListener(new CameraRecordListener() {
                    @Override
                    public void onGetFlashSupport(boolean flashSupport) {
                        runOnUiThread(() -> {
                            findViewById(R.id.img_flash_control).setEnabled(flashSupport);
                        });
                    }

                    @Override
                    public void onRecordComplete() {
                        exportMp4ToGallery(getApplicationContext(), filepath);
                    }

                    @Override
                    public void onRecordStart() {
                        runOnUiThread(() -> {
                            lv.setVisibility(View.GONE);
                        });
                    }

                    @Override
                    public void onError(Exception exception) {
                        Log.e("GPUCameraRecorder", exception.toString());
                    }

                    @Override
                    public void onCameraThreadFinish() {
                        if (toggleClick) {
                            runOnUiThread(() -> {
                                setUpCamera();
                            });
                        }
                        toggleClick = false;
                    }


                    public void onVideoFileReady() {

                    }
                })
                .videoSize(videoWidth, videoHeight)
                .cameraSize(cameraWidth, cameraHeight)
                .lensFacing(lensFacing)
                .build();


    }

//    private void changeFilter(Filters filters) {
//        GPUCameraRecorder.setFilter(Filters.getFilterInstance(filters, getApplicationContext()));
//    }


    private interface BitmapReadyCallbacks {
        void onBitmapReady(Bitmap bitmap);
    }

    private void captureBitmap(final BitmapReadyCallbacks bitmapReadyCallbacks) {
        sampleGLView.queueEvent(() -> {
            EGL10 egl = (EGL10) EGLContext.getEGL();
            GL10 gl = (GL10) egl.eglGetCurrentContext().getGL();
            Bitmap snapshotBitmap = createBitmapFromGLSurface(sampleGLView.getMeasuredWidth(), sampleGLView.getMeasuredHeight(), gl);

            runOnUiThread(() -> {
                bitmapReadyCallbacks.onBitmapReady(snapshotBitmap);
            });
        });
    }

    private Bitmap createBitmapFromGLSurface(int w, int h, GL10 gl) {

        int bitmapBuffer[] = new int[w * h];
        int bitmapSource[] = new int[w * h];
        IntBuffer intBuffer = IntBuffer.wrap(bitmapBuffer);
        intBuffer.position(0);

        try {
            gl.glReadPixels(0, 0, w, h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, intBuffer);
            int offset1, offset2, texturePixel, blue, red, pixel;
            for (int i = 0; i < h; i++) {
                offset1 = i * w;
                offset2 = (h - i - 1) * w;
                for (int j = 0; j < w; j++) {
                    texturePixel = bitmapBuffer[offset1 + j];
                    blue = (texturePixel >> 16) & 0xff;
                    red = (texturePixel << 16) & 0x00ff0000;
                    pixel = (texturePixel & 0xff00ff00) | red | blue;
                    bitmapSource[offset2 + j] = pixel;
                }
            }
        } catch (GLException e) {
            Log.e("CreateBitmap", "createBitmapFromGLSurface: " + e.getMessage(), e);
            return null;
        }

        return Bitmap.createBitmap(bitmapSource, w, h, Bitmap.Config.ARGB_8888);
    }

    public void saveAsPngImage(Bitmap bitmap, String filePath) {
        try {
            File file = new File(filePath);
            FileOutputStream outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void exportMp4ToGallery(Context context, String filePath) {
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + filePath)));
    }

    public static String getVideoFilePath() {
        return getAndroidMoviesFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "GPUCameraRecorder.mp4";
    }

    public static File getAndroidMoviesFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }

    private static void exportPngToGallery(Context context, String filePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(filePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static String getImageFilePath() {
        return getAndroidImageFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "GPUCameraRecorder.png";
    }

    public static File getAndroidImageFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }



    public void langapi() {
        //spinner2.setVisibility(View.VISIBLE);
//        viewDialog.showDialog();

        mQueue = Volley.newRequestQueue(CameraActivity.this.getApplicationContext());

        // prepare the Request
        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, APIs.Languagesapi, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("Response", response.toString());

//                        viewDialog.hideDialog();

                        categ = new ArrayList<>();
                        //  spinner2.setVisibility(View.INVISIBLE);
                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);

                                    Languages langu = new Languages();
                                    String _id = employee.getString("_id");
                                    String name = employee.getString("name");
                                    boolean sel=false;

                                    langu.setLang_name(_id);
                                    langu.setLang_code(name);
                                    langu.setSelected(sel);
                                    categ.add(langu);





                                    Log.d("Response", "createddateL:::" + categ);
                                    Log.d("Response", "createddateL:::" + _id + name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            post_lang_adapter= new Post_lang_Adapter(CameraActivity.this, categ);
                            camera_listview_lang.setAdapter(post_lang_adapter);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Log.d("Error.Response", error.toString());
                        String body;
                        //get status code here
                        //Log.d("statusCode", "---" + error.toString());
                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null) {
                            switch (response.statusCode) {
                                case 422:
                                    try {
                                        body = new String(error.networkResponse.data, "UTF-8");
                                        Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            //Toast.makeText(AddUsers.this, message, Toast.LENGTH_SHORT).show();


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
        );

        // add it to the RequestQueue
        mQueue.add(getRequest);


    }


}