package com.vasmash.va_smash.HomeScreen.homeadapters;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.daasuu.gpuv.egl.filter.GlWatermarkFilter;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vasmash.va_smash.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vasmash.va_smash.VASmashAPIS.APIs.share_url;

public class VideoAction_F extends BottomSheetDialogFragment implements View.OnClickListener {

    View view;
    Context context;
    RecyclerView recyclerView;

    Fragment_Callback fragment_callback;

    String video_url,postid;

    private RequestQueue mQueue;
    private String proftoken;

    ProgressBar progressBar;
    private Uri shareVideoPath;
    private boolean isShareVideoComplete=false;

    @SuppressLint("ValidFragment")
    public VideoAction_F(String url,String postid1, Fragment_Callback fragment_callback) {
        this.video_url=url;
       this.postid=postid1;
        this.fragment_callback=fragment_callback;
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

        Log.d("Save_Video",":::"+video_url+":::"+resolveInfo+"::::"+Environment.getExternalStorageDirectory() +"/Tittic/"+"  :::: "+ postid+"no_watermark"+".mp4");
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
                Toast.makeText(getActivity(), "onDownloadComplete", Toast.LENGTH_SHORT).show();
               // Applywatermark(video_url,resolveInfo);
                Functions.cancel_determinent_loader();
                jsoneashare(postid);
                Scan_file(resolveInfo);

            }

            @Override
            public void onError(Error error) {
                //  Delete_file_no_watermark(item);
                Log.e("onError","*** "+error.getServerErrorMessage());
                Log.e("onError","*** "+error.getConnectionException());
                Log.e("onError","*** "+error.getResponseCode());
                Toast.makeText(getActivity(), "Error"+error.getConnectionException(), Toast.LENGTH_SHORT).show();
                Functions.cancel_determinent_loader();
            }
        });
    }

    public void Applywatermark(String video_url, ResolveInfo resolveInfo){
        Bitmap myLogo = ((BitmapDrawable)getActivity().getResources().getDrawable(R.drawable.backicon)).getBitmap();
        Bitmap bitmap_resize=Bitmap.createScaledBitmap(myLogo, 50, 50, false);
        GlWatermarkFilter filter=new GlWatermarkFilter(bitmap_resize, GlWatermarkFilter.Position.LEFT_TOP);
        new GPUMp4Composer(Environment.getExternalStorageDirectory() +"/Tittic/"+postid+"no_watermark"+".mp4",
                Environment.getExternalStorageDirectory() +"/Tittic/"+postid+".mp4")
                .filter(filter)
                .listener(new GPUMp4Composer.Listener() {
                    @Override
                    public void onProgress(double progress) {
                        Log.d("resp",""+(int) (progress*100));
                        Functions.Show_loading_progress((int)((progress*100)/2)+50);
                    }
                    @Override
                    public void onCompleted() {
                        Log.d("Watermark","onCompleted");
                                // Toast.makeText(getActivity(), "watermark onCompleted", Toast.LENGTH_SHORT).show();
                        Functions.cancel_determinent_loader();
                       // Delete_file_no_watermark(video_url);
                        Scan_file(resolveInfo);
                    }
                    @Override
                    public void onCanceled() {
                        Log.d("resp", "onCanceled");
                    }
                    @Override
                    public void onFailed(Exception exception) {
                        Log.d("resp",exception.toString());
                        Log.d("resp",exception.getMessage());
                        Log.d("resp",""+exception.getCause());
                        Log.d("resp",""+exception.getStackTrace());
                        try {
                            Delete_file_no_watermark(video_url);
                            Functions.cancel_determinent_loader();
                            Toast.makeText(getActivity(), "Try Again", Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                        }
/*
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Delete_file_no_watermark(item);
                                    Functions.cancel_determinent_loader();
                                    Toast.makeText(context, "Try Again", Toast.LENGTH_SHORT).show();

                                }catch (Exception e){

                                }
                            }
                        });
*/
                    }
                })
                .start();
    }

    public void Scan_file(ResolveInfo resolveInfo){
        MediaScannerConnection.scanFile(getActivity(),
                new String[] { Environment.getExternalStorageDirectory() +"/Tittic/"+postid+".mp4" },
                null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                      //  Toast.makeText(getActivity(), "onScanCompleted  path  "+path, Toast.LENGTH_SHORT).show();
                        Log.i("ExternalStorage", "Scanned " + path + ":");
                        Log.i("ExternalStorage", "-> uri=" + uri);



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
        Log.d("first log","Loading file " + filePath);

        // This returns us content://media/external/videos/media (or something like that)
        // I pass in "external" because that's the MediaStore's name for the external
        // storage on my device (the other possibility is "internal")
        Uri videosUri = MediaStore.Video.Media.getContentUri("external");

        Log.d("second log","videosUri = " + videosUri.toString());

        String[] projection = {MediaStore.Video.VideoColumns._ID};

        // TODO This will break if we have no matching item in the MediaStore.
        Cursor cursor = contentResolver.query(videosUri, projection, MediaStore.Video.VideoColumns.DATA + " LIKE ?", new String[] { filePath }, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(projection[0]);
        videoId = cursor.getLong(columnIndex);

        Log.d("third log","Video ID is " + videoId);
        cursor.close();
        if (videoId != -1 ) videoUriStr = videosUri.toString() + "/" + videoId;
        return videoUriStr;
    }
    public void Delete_file_no_watermark(String video_url){
        File file=new File(Environment.getExternalStorageDirectory() +"/Tittic/"+video_url/*+"no_watermark"+".mp4"*/);
/*
        if(file.exists()){
            file.delete();
        }
*/
        Log.d("filedelete","::::"+file);
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
        Log.d("earning share", share_url);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, share_url+postid, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Responses share", response.toString());
                        if (response.length() != 0) {
                            // Iterate the inner "data" array
                            try {
                                String sharepoints=response.getString("count");
/*
                                mainmodels.get(position).setShare(sharepoints);
                                holder.sharetxt.setText(sharepoints);
*/

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
                                        Log.d("body", "---" + body);
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
                                        Log.d("bodyerror", "---" + bodyerror);
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

}
