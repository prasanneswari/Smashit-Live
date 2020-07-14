package com.vasmash.va_smash.HomeScreen.homeadapters;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.createcontent.filters.gpu.composer.FillMode;
import com.vasmash.va_smash.createcontent.filters.gpu.composer.GPUMp4Composer;
import com.vasmash.va_smash.createcontent.filters.gpu.egl.filter.GlFilter;
import com.vasmash.va_smash.createcontent.filters.gpu.egl.filter.GlWatermarkFilter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.claimedtopnav;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.claimhometopnav;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.clamiedget;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.countDownTimer;
import static com.vasmash.va_smash.BottmNavigation.TopNavigationview.progressBarView;
import static com.vasmash.va_smash.HomeScreen.homefragment.HomeFragment.privious_player;
import static com.vasmash.va_smash.SearchClass.SearchVerticalData.commentsearchclick;
import static com.vasmash.va_smash.VASmashAPIS.APIs.share_url;
import static com.yalantis.ucrop.UCropFragment.TAG;

public class VideoAction_F extends BottomSheetDialogFragment implements View.OnClickListener {

    View view;
    Context context;
    RecyclerView recyclerView;

    Fragment_Callback fragment_callback;

    String video_url,postid,username;

    private RequestQueue mQueue;
    private String proftoken;

    ProgressBar progressBar;
    private Uri shareVideoPath;
    private boolean isShareVideoComplete=false;

    String   defaultVideo;

    int progress = 0;
    CountDownTimer countDownTimer;
    FFmpeg ffmpeg;

    @SuppressLint("ValidFragment")
    public VideoAction_F(String url,String postid1,String username11, Fragment_Callback fragment_callback) {
        this.video_url=url;
       this.postid=postid1;
        this.fragment_callback=fragment_callback;
        this.username=username11;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_video_action, container, false);
        context=getContext();

        progressBar=view.findViewById(R.id.progress_bar);
        view.findViewById(R.id.save_video_layout).setOnClickListener(this);
        mQueue = Volley.newRequestQueue(context.getApplicationContext());
        SharedPreferences phoneauthshard = PreferenceManager.getDefaultSharedPreferences(context);
        proftoken = phoneauthshard.getString("token", "null");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Get_Shared_app();
            }
        },1000);

        loadFFMpegBinary();


        return view;
    }

    VideoSharingApps_Adapter adapter;
    public void Get_Shared_app(){
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        final GridLayoutManager layoutManager = new GridLayoutManager(context, 5);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    PackageManager pm=getActivity().getPackageManager();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "https://google.com");

                    List<ResolveInfo> launchables=pm.queryIntentActivities(intent, 0);

                    Collections.sort(launchables, new ResolveInfo.DisplayNameComparator(pm));

                    adapter=new VideoSharingApps_Adapter(context, launchables, new VideoSharingApps_Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int positon, ResolveInfo item, View view) {
                            if(isShareVideoComplete){
                                ActivityInfo activity = item.activityInfo;
                                Log.d("shareiemselected",":::"+activity.name);

                                ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                                Intent i = new Intent(Intent.ACTION_MAIN);

                                i.addCategory(Intent.CATEGORY_LAUNCHER);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                                i.setComponent(name);
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                // Uri videoUri = Uri.parse(path);
                                intent.setType("video/*");
                                intent.putExtra(Intent.EXTRA_STREAM,shareVideoPath);
                                intent.setComponent(name);
                                startActivity(intent);
                            }else {
                                Open_App(item);
                            }
                        }
                    });

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
                catch (Exception e){

                }
            }
        }).start();
    }

    public void Open_App(ResolveInfo resolveInfo) {
        Log.d("shareapp","::::"+resolveInfo);

        try {
/*
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, videoUri);
            intent.setComponent(name);
            startActivity(intent);
*/
            Save_Video(video_url,resolveInfo);

        }catch (Exception e){

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_video_layout:
                Bundle bundle=new Bundle();
                bundle.putString("action","save");
                dismiss();
                fragment_callback.Responce(bundle);
                break;
        }
    }


    public void Save_Video(String video_url, ResolveInfo resolveInfo){

       // Log.d("Save_Video",":::"+video_url+":::"+resolveInfo+"::::"+Environment.getExternalStorageDirectory() +"/SmashitLive/"+"  :::: "+ postid+"no_watermark"+".mp4");
        Functions.Show_determinent_loader(getActivity(),false,false);
        PRDownloader.initialize(getActivity().getApplicationContext());
        DownloadRequest prDownloader= PRDownloader.download(video_url, Environment.getExternalStorageDirectory() +"/Tittic/", postid+".mp4")
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
                        int prog=(int)((progress.currentBytes*100)/progress.totalBytes);
                        Functions.Show_loading_progress(prog/2);
                    }
                });
        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                //Toast.makeText(getActivity(), "onDownloadComplete", Toast.LENGTH_SHORT).show();
               // Applywatermark(video_url,resolveInfo);
                Functions.cancel_determinent_loader();
                Functions.Show_determinent_loader(getActivity(),false,false);

                jsoneashare(postid);
              //  Scan_file(resolveInfo);
                String path=Environment.getExternalStorageDirectory() +"/Tittic/"+postid+".mp4";
                // defaultVideo=Environment.getExternalStorageDirectory() +"/Tittic/watermark/"+postid+".mp4";

                try {
                    File f = new File(Environment.getExternalStorageDirectory() + File.separator + postid+".mp4");

                    defaultVideo=f.getAbsolutePath();
                    f.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // String[] imageCommand =  { "-y", "-i", path, "-y", "-i", saveAppIcon(), "-filter_complex", "overlay=x=20:y=20", "-strict", "experimental", defaultVideo};
                ///String[]  imageCommand = {"-i",  path,  "-vf", "drawtext=fontfile=/system/fonts/DroidSans.ttf:text='SiteName hulluway':fontsize=30:fontcolor=white: x=20:y=20", "-acodec", "copy", "-y",  defaultVideo};

                String[] imageCommand =  { "-y", "-i", path, "-y", "-i", saveAppIcon(), "-filter_complex", "overlay=x=20:y=20,drawtext=fontfile=/system/fonts/DroidSans.ttf:text='"+"@"+username+"':fontsize=18:fontcolor=white: x=20:y=80", "-strict", "experimental","-preset", "ultrafast", defaultVideo};

                execFFmpegBinary(imageCommand,resolveInfo,new File(path));
            }

            @Override
            public void onError(Error error) {
                Delete_file_no_watermark(video_url);
/*
                Log.e("onError","*** "+error.getServerErrorMessage());
                Log.e("onError","*** "+error.getConnectionException());
                Log.e("onError","*** "+error.getResponseCode());
*/
                Toast.makeText(getActivity(), "Error"+error.getConnectionException(), Toast.LENGTH_SHORT).show();
                Functions.cancel_determinent_loader();
            }
        });
    }

    //ttry with sound video

    public void Scan_file(ResolveInfo resolveInfo){
        MediaScannerConnection.scanFile(getActivity(),
                new String[] { defaultVideo },
                null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                      //  Toast.makeText(getActivity(), "onScanCompleted  path  "+path, Toast.LENGTH_SHORT).show();
                       // Log.i("ExternalStorage", "Scanned " + path + ":");
                       // Log.i("ExternalStorage", "-> uri=" + uri);

                        ActivityInfo activity = resolveInfo.activityInfo;
                        ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                        Intent i = new Intent(Intent.ACTION_MAIN);

                        i.addCategory(Intent.CATEGORY_LAUNCHER);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        i.setComponent(name);
                        String newPath=  getVideoContentUriFromFilePath(getActivity(),path);
                        isShareVideoComplete=true;
                        shareVideoPath= Uri.parse(newPath);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                       // Uri videoUri = Uri.parse(path);
                        intent.setType("video/*");
                        intent.putExtra(Intent.EXTRA_STREAM,shareVideoPath);
                        intent.setComponent(name);
                        startActivity(intent);
                        //Delete_file_no_watermark(video_url);

                   /*   String newPath=  getVideoContentUriFromFilePath(getActivity(),path);
                        Intent intent = new Intent(Intent.ACTION_SEND);
                       // intent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Title");
                        //intent.setAction(Intent.ACTION_SEND);
                        intent.setType("video/mp4");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(newPath));
                        try {
                            startActivity(Intent.createChooser(intent,"Upload video via:"));
                        } catch (android.content.ActivityNotFoundException ex) {

                        }*/
                    }
                });
    }
    public static String getVideoContentUriFromFilePath(Context ctx, String filePath) {

        ContentResolver contentResolver = ctx.getContentResolver();
        String videoUriStr = null;
        long videoId = -1;
        //Log.d("first log","Loading file " + filePath);

        // This returns us content://media/external/videos/media (or something like that)
        // I pass in "external" because that's the MediaStore's name for the external
        // storage on my device (the other possibility is "internal")
        Uri videosUri = MediaStore.Video.Media.getContentUri("external");

       // Log.d("second log","videosUri = " + videosUri.toString());

        String[] projection = {MediaStore.Video.VideoColumns._ID};

        // TODO This will break if we have no matching item in the MediaStore.
        Cursor cursor = contentResolver.query(videosUri, projection, MediaStore.Video.VideoColumns.DATA + " LIKE ?", new String[] { filePath }, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(projection[0]);
        videoId = cursor.getLong(columnIndex);

        //Log.d("third log","Video ID is " + videoId);
        cursor.close();
        if (videoId != -1 ) videoUriStr = videosUri.toString() + "/" + videoId;
        return videoUriStr;
    }
    public void Delete_file_no_watermark(String video_url){
        File file=new File(defaultVideo);
/*
        if(file.exists()){
            file.delete();
        }
*/
       // Log.d("filedelete","::::"+file);
        if (file.exists()) {
            if (file.delete()) {
                System.out.println("file Deleted :" );
            } else {
                System.out.println("file not Deleted :" );
            }
        }

    }

    private void jsoneashare(String postid) {
        // prepare the Request
        //Log.d("earning share", share_url);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, share_url+postid, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        //Log.d("Responses share", response.toString());
                        if (response.length() != 0) {
                            // Iterate the inner "data" array
                            try {
                                String sharepoints=response.getString("count");
                                if(fragment_callback!=null)
                                    fragment_callback.onDataSent(String.valueOf(sharepoints));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
                                        //Log.d("body", "---" + body);
                                        JSONObject obj = new JSONObject(body);
                                        if (obj.has("errors")) {
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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
                                        //Log.d("bodyerror", "---" + bodyerror);
                                        JSONObject obj = new JSONObject(bodyerror);
                                        if (obj.has("errors")) {
                                            JSONObject errors = obj.getJSONObject("errors");
                                            String message = errors.getString("message");
                                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

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
                headers.put("Authorization",proftoken);

                return headers;
            }
        };

        // add it to the RequestQueue
        mQueue.add(getRequest);
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        super.onCancel(dialog);
        // Log.d("cncelss","::::");

        if (!commentsearchclick.equals("null")){
            dialog.dismiss();
        }else {
            //Log.d("entrincommentfinish","::::");
            if (claimedtopnav.equals("true")) {
                progressBarView.setProgress(100);
                privious_player.setPlayWhenReady(true);

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    dialog.dismiss();
                }
            } else if (clamiedget.equals("true")) {
                progressBarView.setProgress(100);
                privious_player.setPlayWhenReady(true);

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    dialog.dismiss();
                }
            } else if (claimhometopnav.equals("false")) {
                progressBarView.setProgress(0);
                privious_player.setPlayWhenReady(true);

                if (countDownTimer != null) {
                    countDownTimer.start();
                    dialog.dismiss();
                }
            } else if (claimhometopnav.equals("true")) {
                progressBarView.setProgress(100);
                privious_player.setPlayWhenReady(true);

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    dialog.dismiss();
                }
            } else {
                //Log.d("entringelse","::::");
                    if (countDownTimer != null) {
                        countDownTimer.start();
                        dialog.dismiss();
                        privious_player.setPlayWhenReady(true);
                    } else {
                        dialog.dismiss();
                    }
            }
        }
        // Toast.makeText("tag", "CANCEL", Toast.LENGTH_SHORT).show();
    }


    private void loadFFMpegBinary() {
        try {
            if (ffmpeg == null) {
               // Log.d("", "ffmpeg : era nulo");
                ffmpeg = FFmpeg.getInstance(getActivity());
            }
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {

                }

                @Override
                public void onSuccess() {
                    //Log.d(TAG, "ffmpeg : correct Loaded");
                }
            });
        } catch (FFmpegNotSupportedException e) {

        } catch (Exception e) {
            //Log.d(TAG, "EXception no controlada : " + e);
        }
    }


    private void execFFmpegBinary(final String[] command,ResolveInfo resolveInfo,File file) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//use one of overloaded setDataSource() functions to set your data source
            retriever.setDataSource(getActivity(), Uri.fromFile(file));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long totalDur = Long.parseLong(time );
            retriever.release();
            if (totalDur != 0) {
/*
                            float progress = (Integer.parseInt(matchSplit[0]) * 3600 +
                                    Integer.parseInt(matchSplit[1]) * 60 +
                                    Float.parseFloat(matchSplit[2])) / totalDur;
                            float showProgress = (progress * 1000);
                            Log.d(TAG, "=======PROGRESS======== "+progress+" showProgress  **** " + showProgress);
                            Functions.Show_loading_progress((int)(showProgress));
*/


                String timeInterval = String.valueOf(totalDur);
                int endTime = Integer.parseInt(timeInterval); // up to finish time
              //  Log.d("onTick progress","::::"+endTime);


                countDownTimer = new CountDownTimer(20000, 1000) {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onTick(long millisUntilFinished) {
                       // Log.d("onTick progress","::::"+progress);
                        //setProgress(progress, endTime / 1000);
                        Functions.Show_loading_progress(progress);
                        progress=progress+1;
                    }
                    @Override
                    public void onFinish() {
                        //  Functions.Show_loading_progress((int)(endTime));
/*
                        if (countDownTimer!=null){
                            countDownTimer.cancel();
                        }
*/
                    }
                };
            }
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                @Override
                public void onFailure(String s) {
                   // Log.d("tag", "FAILED with output : " + s);
                    Functions.cancel_determinent_loader();
                }

                @Override
                public void onSuccess(String s) {
                   // Log.d("tag", "sucess with output : " + s);
                    Functions.cancel_determinent_loader();
                    if (countDownTimer!=null){
                        countDownTimer.cancel();
                    }
                    Scan_file(resolveInfo);

                }

                @Override
                public void onProgress(String message) {
                    countDownTimer.start();
/*
                    Log.d("tag", "onProgress  progress1 " + message);
                    Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(command));
                    Log.d(TAG, "progress : " + message);
*/
                    Pattern timePattern = Pattern.compile("(?<=time=)[\\d:.]*");
                    Scanner sc = new Scanner(message);

                    String match = sc.findWithinHorizon(timePattern, 0);
                    if (match != null) {
                        String[] matchSplit = match.split(":");
                        // int totalDur=25;

                    }
                 /*   int start = message.indexOf("time=");
                    int end = message.indexOf(" bitrate");
                    if (start != -1 && end != -1) {
                        String duration = message.substring(start+5, end);
                        if (duration != "") {
                            try {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                              //  dialog.setProgress((int)sdf.parse("1970-01-01 "+ duration).getTime());
                              //  Functions.Show_loading_progress((int)((progress*100)/2)+50);
                                Functions.Show_loading_progress((int)sdf.parse("1970-01-01 "+ duration).getTime());


                            }catch (ParseException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }*/


                }

                @Override
                public void onStart() {
                   // Log.d("tag", "Started command : ffmpeg " + command);



                }

                @Override
                public void onFinish() {
                    //Log.d("tag", "Finished command : ffmpeg " + command);


                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

    private String saveAppIcon(){
        String imagePath=null;
        try{
            Bitmap myLogo = ((BitmapDrawable)getActivity().getResources().getDrawable(R.drawable.watermarkicon)).getBitmap();
            Bitmap bitmap_resize=Bitmap.createScaledBitmap(myLogo, 150, 60, false);

            // Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.launchicon);

            //replace "R.drawable.bubble_green" with the image resource you want to share from drawable

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap_resize.compress(Bitmap.CompressFormat.WEBP, 40, bytes);

            // you can create a new file name "test.jpg" in sdcard folder.
            File f = new File(Environment.getExternalStorageDirectory() + File.separator + "test.png");

            imagePath=f.getAbsolutePath();

            f.createNewFile();

            // write the bytes in file
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());

            // remember close de FileOutput
            fo.close();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //Log.d("","imagePath  "+imagePath);
        return imagePath;
    }

}
