package com.vasmash.va_smash.createcontent.videoeffects;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.FFmpegExecuteResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpegLoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.createcontent.CameraActivity;
import com.vasmash.va_smash.createcontent.PostcontentActivity;
import com.vasmash.va_smash.createcontent.PreviewActivity;
import com.vasmash.va_smash.createcontent.videoeffects.videophotoeditor.OnPhotoEditorListener;
import com.vasmash.va_smash.createcontent.videoeffects.videophotoeditor.PhotoEditor;
import com.vasmash.va_smash.createcontent.videoeffects.videophotoeditor.SaveSettings;
import com.vasmash.va_smash.createcontent.videoeffects.videophotoeditor.TextStyleBuilder;
import com.vasmash.va_smash.createcontent.videoeffects.videophotoeditor.ViewType;
import com.vasmash.va_smash.createcontent.videoeffects.videophotoeditor.utils.DimensionData;
import com.vasmash.va_smash.databinding.ActivityVideoEffectsBinding;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION;
import static com.vasmash.va_smash.createcontent.videoeffects.videophotoeditor.utils.Utils.getScaledDimension;

public class VideoEffectsActivity extends AppCompatActivity implements OnPhotoEditorListener, PropertiesBSFragment.Properties,
        View.OnClickListener,
        StickerBSFragment.StickerListener {


    //private ActivityPreviewVideoBinding binding;
    private ActivityVideoEffectsBinding binding;
    private static final String TAG = VideoEffectsActivity.class.getSimpleName();
    private static final int CAMERA_REQUEST = 52;
    private static final int PICK_REQUEST = 53;
    private PhotoEditor mPhotoEditor;
    private String globalVideoUrl = "";
    private PropertiesBSFragment propertiesBSFragment;
    private StickerBSFragment mStickerBSFragment;
    private MediaPlayer mediaPlayer;
    private String videoPath = "";
    private String imagePath = "";
    private ArrayList<String> exeCmd;
    FFmpeg fFmpeg;
    private String[] newCommand;
    private ProgressDialog progressDialog;

    private int originalDisplayWidth;
    private int originalDisplayHeight;
    private int newCanvasWidth, newCanvasHeight;
    private int DRAW_CANVASW = 0;
    private int DRAW_CANVASH = 0;

    int cam = 1;
    Boolean touched=false;

    // String path;

    private MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_effects);
        initViews();
//        Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
//        Glide.with(this).load(getIntent().getStringExtra("DATA")).into(binding.ivImage.getSource());
        Glide.with(this).load(R.drawable.trans).centerCrop().into(binding.ivImage.getSource());
        cam = getIntent().getIntExtra("cam", 1);
        String path=getIntent().getStringExtra("path");
        System.out.println("videoeffectsactivity path  "+path);
        videoPath = path;
        System.out.println("videoeffectsactivity videoPath  "+videoPath);
        // videoPath = getIntent().getStringExtra("DATA");
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(videoPath);
        String metaRotation = retriever.extractMetadata(METADATA_KEY_VIDEO_ROTATION);
        int rotation = metaRotation == null ? 0 : Integer.parseInt(metaRotation);
        if (rotation == 90 || rotation == 270) {
            DRAW_CANVASH = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            DRAW_CANVASW = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        } else {
            DRAW_CANVASW = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            DRAW_CANVASH = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        }
        setCanvasAspectRatio();

        binding.videoSurface.getLayoutParams().width = newCanvasWidth;
        binding.videoSurface.getLayoutParams().height = newCanvasHeight;

        binding.ivImage.getLayoutParams().width = newCanvasWidth;
        binding.ivImage.getLayoutParams().height = newCanvasHeight;

       // Log.d(">>", "width>> " + newCanvasWidth + "height>> " + newCanvasHeight + " rotation >> " + rotation);
    }

    private void initViews() {
        fFmpeg = FFmpeg.getInstance(this);
        progressDialog = new ProgressDialog(this);
        mStickerBSFragment = new StickerBSFragment();
        mStickerBSFragment.setStickerListener(this);
        propertiesBSFragment = new PropertiesBSFragment();
        propertiesBSFragment.setPropertiesChangeListener(this);
        mPhotoEditor = new PhotoEditor.Builder(this, binding.ivImage)
                .setPinchTextScalable(true) // set flag to make text scalable when pinch
                .setDeleteView(binding.imgDelete)
                //.setDefaultTextTypeface(mTextRobotoTf)
                //.setDefaultEmojiTypeface(mEmojiTypeFace)
                .build(); // build photo editor sdk

        mPhotoEditor.setOnPhotoEditorListener(this);

        binding.imgClose.setOnClickListener(this);
        binding.nextll.setOnClickListener(this);
        binding.imgDraw.setOnClickListener(this);
        binding.imgText.setOnClickListener(this);
        binding.imgUndo.setOnClickListener(this);
        binding.imgSticker.setOnClickListener(this);

        binding.videoSurface.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
//                activityHomeBinding.videoSurface.getLayoutParams().height=640;
//                activityHomeBinding.videoSurface.getLayoutParams().width=720;
                Surface surface = new Surface(surfaceTexture);

                try {
                    mediaPlayer = new MediaPlayer();
//                    mediaPlayer.setDataSource("http://daily3gp.com/vids/747.3gp");

                   // Log.d("VideoPath>>", videoPath);
                    mediaPlayer.setDataSource(videoPath);
                    mediaPlayer.setSurface(surface);
                    mediaPlayer.prepare();
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                  /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mediaPlayer.setAudioAttributes(
                                new AudioAttributes
                                        .Builder()
                                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                        .build());
                    }else{
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    }*/
                    mediaPlayer.start();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

            }
        });

        exeCmd = new ArrayList<>();
        try {
            fFmpeg.loadBinary(new FFmpegLoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                   // Log.d("binaryLoad", "onFailure");

                }

                @Override
                public void onSuccess() {
                  //  Log.d("binaryLoad", "onSuccess");
                }

                @Override
                public void onStart() {
                  //  Log.d("binaryLoad", "onStart");

                }

                @Override
                public void onFinish() {
                  //  Log.d("binaryLoad", "onFinish");

                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
        }


    }

    public void executeCommand(String[] command, final String absolutePath) {
        try {
            fFmpeg.execute(command, new FFmpegExecuteResponseHandler() {
                @Override
                public void onSuccess(String s) {
                   // Log.d("CommandExecute", "onSuccess" + "  " + s);
                    // Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                  /*  Intent i = new Intent(VideoEffectsActivity.this, VideoEffectsActivity.class);
                    i.putExtra("DATA", absolutePath);
                    startActivity(i);*/

                    Intent intent = new Intent(VideoEffectsActivity.this, PostcontentActivity.class);

                    intent.putExtra("cam", cam);
                    intent.putExtra("path", absolutePath);
                    startActivity(intent);
                   /* if (!camera_sounds.getText().toString().equals("sounds  ")){
                        Merge_withAudio();
                    }
                   */
                    finish();

                }

                @Override
                public void onProgress(String s) {
                    // progressDialog.setMessage(s);
                   // Log.d("CommandExecute", "onProgress" + "  " + s);

                }

                @Override
                public void onFailure(String s) {
                   // Log.d("CommandExecute", "onFailure" + "  " + s);
                    progressDialog.hide();

                }

                @Override
                public void onStart() {
                    // progressDialog.setTitle("Preccesing");
                    //  progressDialog.setMessage("Starting");
                    progressDialog.show();
                }

                @Override
                public void onFinish() {
                    progressDialog.hide();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            e.printStackTrace();
        }
    }

    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgClose:
                releaseMediaPlayer();
                onBackPressed();
                break;
            case R.id.nextll:
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                    }
                }
                if (touched==true){
                    saveImage();
                }
                else {
                    Intent intent = new Intent(VideoEffectsActivity.this, PostcontentActivity.class);

                    intent.putExtra("cam", cam);
                    intent.putExtra("path", videoPath);
                    startActivity(intent);
                }

                break;
            case R.id.imgDraw:
                setDrawingMode();
                touched=true;
                break;
            case R.id.imgText:
                touched=true;
                TextEditorDialogFragment textEditorDialogFragment = TextEditorDialogFragment.show(this, 0);
                textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {

                    @Override
                    public void onDone(String inputText, int colorCode, int position) {
                        final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                        styleBuilder.withTextColor(colorCode);
                        Typeface typeface = ResourcesCompat.getFont(VideoEffectsActivity.this, TextEditorDialogFragment.getDefaultFontIds(VideoEffectsActivity.this).get(position));
                        styleBuilder.withTextFont(typeface);
                        mPhotoEditor.addText(inputText, styleBuilder, position);
                    }
                });
                break;
            case R.id.imgUndo:
               // Log.d("canvas>>", mPhotoEditor.undoCanvas() + "");
                mPhotoEditor.clearBrushAllViews();
                break;
            case R.id.imgSticker:
                touched=true;
                mStickerBSFragment.show(getSupportFragmentManager(), mStickerBSFragment.getTag());
                break;

        }
    }

    private void setCanvasAspectRatio() {

        originalDisplayHeight = getDisplayHeight();
        originalDisplayWidth = getDisplayWidth();

        DimensionData displayDiamenion =
                getScaledDimension(new DimensionData((int) DRAW_CANVASW, (int) DRAW_CANVASH),
                        new DimensionData(originalDisplayWidth, originalDisplayHeight));
        newCanvasWidth = displayDiamenion.width;
        newCanvasHeight = displayDiamenion.height;

    }


    private void setDrawingMode() {
        if (mPhotoEditor.getBrushDrawableMode()) {
            mPhotoEditor.setBrushDrawingMode(false);
            binding.imgDraw.setBackgroundColor(ContextCompat.getColor(this, R.color.black_trasp));
        } else {
            mPhotoEditor.setBrushDrawingMode(true);
            binding.imgDraw.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
            propertiesBSFragment.show(getSupportFragmentManager(), propertiesBSFragment.getTag());
        }
    }

    // @SuppressLint("MissingPermission")
    private void saveImage() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            File file = new File(Environment.getExternalStorageDirectory()
                                    + File.separator + ""
                                    + System.currentTimeMillis() + ".png");
                            try {
                                file.createNewFile();

                                SaveSettings saveSettings = new SaveSettings.Builder()
                                        .setClearViewsEnabled(true)
                                        .setTransparencyEnabled(false)
                                        .build();

                                if (ActivityCompat.checkSelfPermission(VideoEffectsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }else{
                                    mPhotoEditor.saveAsFile(file.getAbsolutePath(), saveSettings, new PhotoEditor.OnSaveListener() {
                                        @Override
                                        public void onSuccess(@NonNull String imagePath) {
                                            VideoEffectsActivity.this.imagePath = imagePath;
/*
                                            Log.d("imagePath>>", imagePath);
                                            Log.d("imagePath2>>", Uri.fromFile(new File(imagePath)).toString());
*/
                                            binding.ivImage.getSource().setImageURI(Uri.fromFile(new File(imagePath)));
                                            //  Toast.makeText(PreviewVideoActivity.this, "Saved successfully...", Toast.LENGTH_SHORT).show();
                                            applayWaterMark();
                                        }

                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            Toast.makeText(VideoEffectsActivity.this, "Saving Failed...", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                            } catch (IOException e) {
                                e.printStackTrace();

                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


    }

    private void applayWaterMark() {

//        imagePath = generatePath(Uri.fromFile(new File(imagePath)),PreviewVideoActivity.this);

//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(videoPath);
//        int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
//        int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        /*if (width > height) {
            int tempWidth = width;
            width = height;
            height = tempWidth;
        }*/

//        Log.d(">>", "width>> " + width + "height>> " + height);
//        retriever.release();

        File output = new File(Environment.getExternalStorageDirectory()
                + File.separator + ""
                + System.currentTimeMillis() + ".mp4");
        try {
            output.createNewFile();

            exeCmd.add("-y");
            exeCmd.add("-i");
            exeCmd.add(videoPath);
//            exeCmd.add("-framerate 30000/1001 -loop 1");
            exeCmd.add("-i");
            exeCmd.add(imagePath);
            exeCmd.add("-filter_complex");
//            exeCmd.add("-strict");
//            exeCmd.add("-2");
//            exeCmd.add("-map");
//            exeCmd.add("[1:v]scale=(iw+(iw/2)):(ih+(ih/2))[ovrl];[0:v][ovrl]overlay=x=(main_w-overlay_w)/2:y=(main_h-overlay_h)/2");
//            exeCmd.add("[1:v]scale=720:1280:1823[ovrl];[0:v][ovrl]overlay=x=0:y=0");
            exeCmd.add("[1:v]scale=" + DRAW_CANVASW + ":" + DRAW_CANVASH + "[ovrl];[0:v][ovrl]overlay=x=0:y=0");
            exeCmd.add("-c:v");
            exeCmd.add("libx264");
            exeCmd.add("-preset");
            exeCmd.add("ultrafast");
            exeCmd.add(output.getAbsolutePath());


            newCommand = new String[exeCmd.size()];
            for (int j = 0; j < exeCmd.size(); j++) {
                newCommand[j] = exeCmd.get(j);
            }


            for (int k = 0; k < newCommand.length; k++) {
               // Log.d("CMD==>>", newCommand[k] + "");
            }

//            newCommand = new String[]{"-i", videoPath, "-i", imagePath, "-preset", "ultrafast", "-filter_complex", "[1:v]scale=2*trunc(" + (width / 2) + "):2*trunc(" + (height/ 2) + ") [ovrl], [0:v][ovrl]overlay=0:0" , output.getAbsolutePath()};
            executeCommand(newCommand, output.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStickerClick(Bitmap bitmap) {
        mPhotoEditor.setBrushDrawingMode(false);
        binding.imgDraw.setBackgroundColor(ContextCompat.getColor(this, R.color.black_trasp));
        mPhotoEditor.addImage(bitmap);
    }

    @Override
    public void onEditTextChangeListener(final View rootView, String text, int colorCode, final int position) {
        TextEditorDialogFragment textEditorDialogFragment =
                TextEditorDialogFragment.show(this, text, colorCode, position);
        textEditorDialogFragment.setOnTextEditorListener(new TextEditorDialogFragment.TextEditor() {
            @Override
            public void onDone(String inputText, int colorCode, int position) {
                final TextStyleBuilder styleBuilder = new TextStyleBuilder();
                styleBuilder.withTextColor(colorCode);
                Typeface typeface = ResourcesCompat.getFont(VideoEffectsActivity.this, TextEditorDialogFragment.getDefaultFontIds(VideoEffectsActivity.this).get(position));
                styleBuilder.withTextFont(typeface);
                mPhotoEditor.editText(rootView, inputText, styleBuilder, position);
            }
        });
    }

    public String generatePath(Uri uri, Context context) {
        String filePath = null;
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat) {
            filePath = generateFromKitkat(uri, context);
        }

        if (filePath != null) {
            return filePath;
        }

        Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DATA}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                filePath = cursor.getString(columnIndex);
            }
            cursor.close();
        }
        return filePath == null ? uri.getPath() : filePath;
    }

    @TargetApi(19)
    private String generateFromKitkat(Uri uri, Context context) {
        String filePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            String wholeID = DocumentsContract.getDocumentId(uri);

            String id = wholeID.split(":")[1];

            String[] column = {MediaStore.Video.Media.DATA};
            String sel = MediaStore.Video.Media._ID + "=?";

            Cursor cursor = context.getContentResolver().
                    query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);


            int columnIndex = cursor.getColumnIndex(column[0]);

            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();
        }
        return filePath;
    }

    private int getDisplayWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private int getDisplayHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    @Override
    public void onAddViewListener(ViewType viewType, int numberOfAddedViews) {
        //Log.d(TAG, "onAddViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onRemoveViewListener(ViewType viewType, int numberOfAddedViews) {
       // Log.d(TAG, "onRemoveViewListener() called with: viewType = [" + viewType + "], numberOfAddedViews = [" + numberOfAddedViews + "]");
    }

    @Override
    public void onStartViewChangeListener(ViewType viewType) {
       // Log.d(TAG, "onStartViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onStopViewChangeListener(ViewType viewType) {
       // Log.d(TAG, "onStopViewChangeListener() called with: viewType = [" + viewType + "]");
    }

    @Override
    public void onColorChanged(int colorCode) {
        mPhotoEditor.setBrushColor(colorCode);
    }

    @Override
    public void onOpacityChanged(int opacity) {

    }

    @Override
    public void onBrushSizeChanged(int brushSize) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VideoEffectsActivity.this, CameraActivity.class);
        startActivity(intent);
        finish();
    }
}
