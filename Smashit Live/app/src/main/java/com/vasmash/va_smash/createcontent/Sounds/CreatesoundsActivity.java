package com.vasmash.va_smash.createcontent.Sounds;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vasmash.va_smash.R;
import com.vasmash.va_smash.createcontent.Sounds.soundfile.SoundFile;

import java.io.File;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;

public class CreatesoundsActivity extends Activity
        implements MarkerView.MarkerListener,
        WaveformView.WaveformListener {
    private long mLoadingLastUpdateTime;
    private boolean mLoadingKeepGoing;
    private long mRecordingLastUpdateTime;
    private boolean mRecordingKeepGoing;
    private double mRecordingTime;
    private boolean mFinishActivity;
    private TextView mTimerTextView;
    private AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    private SoundFile mSoundFile;
    private File mFile;
    private String mFilename;
    private String mArtist;
    private String mTitle;
    private int mNewFileKind;
    private boolean mWasGetContentIntent;
    private WaveformView mWaveformView;
    private MarkerView mStartMarker;
    private MarkerView mEndMarker;
    private TextView mStartText;
    private TextView mEndText;
    private TextView mInfo;
    private String mInfoContent;
    private ImageButton mPlayButton;
    private ImageButton mRewindButton;
    private ImageButton mFfwdButton;
    private boolean mKeyDown;
    private String mCaption = "";
    private int mWidth;
    private int mMaxPos;
    private int mStartPos;
    private int mEndPos;
    private boolean mStartVisible;
    private boolean mEndVisible;
    private int mLastDisplayedStartPos;
    private int mLastDisplayedEndPos;
    private int mOffset;
    private int mOffsetGoal;
    private int mFlingVelocity;
    private int mPlayStartMsec;
    private int mPlayEndMsec;
    private Handler mHandler;
    private boolean mIsPlaying;
    private SamplePlayer mPlayer;
    private boolean mTouchDragging;
    private float mTouchStart;
    private int mTouchInitialOffset;
    private int mTouchInitialStartPos;
    private int mTouchInitialEndPos;
    private long mWaveformTouchStartMsec;
    private float mDensity;
    private int mMarkerLeftInset;
    private int mMarkerRightInset;
    private int mMarkerTopOffset;
    private int mMarkerBottomOffset;
    File sounddir;
    private Thread mLoadSoundFileThread;
    private Thread mRecordAudioThread;
    private Thread mSaveSoundFileThread;
    Button createsound_save;

    // Result codes
    private static final int REQUEST_CODE_CHOOSE_CONTACT = 1;

    /**
     * This is a special intent action that means "edit a sound file".
     */
    public static final String EDIT = "com.ringdroid.action.EDIT";

    int stand=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createsounds);


        Log.e("stand", String.valueOf(stand));

        if(stand==0) {
            Intent intent_upload = new Intent();
            intent_upload.setType("audio/*");
            intent_upload.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent_upload, 1);

        }




    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){

        if(requestCode == 1){

            if(resultCode == RESULT_OK){

                //the selected audio.
                Uri uri = data.getData();
                File fl=new File(uri.toString());

                mFilename= ImageFilePath.getPath(CreatesoundsActivity.this,uri);
                System.out.println(mFilename);
                Log.e("soundfile", mFilename);
                stand=1;
                mWasGetContentIntent=false;
                mSoundFile = null;
                mKeyDown = false;
                mHandler = new Handler();

                loadGui();
                mHandler.postDelayed(mTimerRunnable, 100);
                loadFromFile();

                createsound_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onSave();
                        Log.e("Smashsounds","Smashsounds");
                    }
                });

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void waveformDraw() {
        mWidth = mWaveformView.getMeasuredWidth();
        if (mOffsetGoal != mOffset && !mKeyDown)
            updateDisplay();
        else if (mIsPlaying) {
            updateDisplay();
        } else if (mFlingVelocity != 0) {
            updateDisplay();
        }
    }

    public void waveformTouchStart(float x) {
        mTouchDragging = true;
        mTouchStart = x;
        mTouchInitialOffset = mOffset;
        mFlingVelocity = 0;
        mWaveformTouchStartMsec = getCurrentTime();
    }

    public void waveformTouchMove(float x) {
        mOffset = trap((int)(mTouchInitialOffset + (mTouchStart - x)));
        updateDisplay();
    }

    public void waveformTouchEnd() {
        mTouchDragging = false;
        mOffsetGoal = mOffset;

        long elapsedMsec = getCurrentTime() - mWaveformTouchStartMsec;
        if (elapsedMsec < 300) {
            if (mIsPlaying) {
                int seekMsec = mWaveformView.pixelsToMillisecs(
                        (int)(mTouchStart + mOffset));
                if (seekMsec >= mPlayStartMsec &&
                        seekMsec < mPlayEndMsec) {
                    mPlayer.seekTo(seekMsec);
                } else {
                    handlePause();
                }
            } else {
                onPlay((int)(mTouchStart + mOffset));
            }
        }
    }

    public void waveformFling(float vx) {
        mTouchDragging = false;
        mOffsetGoal = mOffset;
        mFlingVelocity = (int)(-vx);
        updateDisplay();
    }

    public void waveformZoomIn() {
        mWaveformView.zoomIn();
        mStartPos = mWaveformView.getStart();
        mEndPos = mWaveformView.getEnd();
        mMaxPos = mWaveformView.maxPos();
        mOffset = mWaveformView.getOffset();
        mOffsetGoal = mOffset;
        updateDisplay();
    }

    public void waveformZoomOut() {
        mWaveformView.zoomOut();
        mStartPos = mWaveformView.getStart();
        mEndPos = mWaveformView.getEnd();
        mMaxPos = mWaveformView.maxPos();
        mOffset = mWaveformView.getOffset();
        mOffsetGoal = mOffset;
        updateDisplay();
    }

    //
    // MarkerListener
    //

    public void markerDraw() {
    }

    public void markerTouchStart(MarkerView marker, float x) {
        mTouchDragging = true;
        mTouchStart = x;
        mTouchInitialStartPos = mStartPos;
        mTouchInitialEndPos = mEndPos;
    }

    public void markerTouchMove(MarkerView marker, float x) {
        float delta = x - mTouchStart;

        if (marker == mStartMarker) {
            mStartPos = trap((int)(mTouchInitialStartPos + delta));
            mEndPos = trap((int)(mTouchInitialEndPos + delta));
        } else {
            mEndPos = trap((int)(mTouchInitialEndPos + delta));
            if (mEndPos < mStartPos)
                mEndPos = mStartPos;
        }

        updateDisplay();
    }

    public void markerTouchEnd(MarkerView marker) {
        mTouchDragging = false;
        if (marker == mStartMarker) {
            setOffsetGoalStart();
        } else {
            setOffsetGoalEnd();
        }
    }

    public void markerLeft(MarkerView marker, int velocity) {
        mKeyDown = true;

        if (marker == mStartMarker) {
            int saveStart = mStartPos;
            mStartPos = trap(mStartPos - velocity);
            mEndPos = trap(mEndPos - (saveStart - mStartPos));
            setOffsetGoalStart();
        }

        if (marker == mEndMarker) {
            if (mEndPos == mStartPos) {
                mStartPos = trap(mStartPos - velocity);
                mEndPos = mStartPos;
            } else {
                mEndPos = trap(mEndPos - velocity);
            }

            setOffsetGoalEnd();
        }

        updateDisplay();
    }

    public void markerRight(MarkerView marker, int velocity) {
        mKeyDown = true;

        if (marker == mStartMarker) {
            int saveStart = mStartPos;
            mStartPos += velocity;
            if (mStartPos > mMaxPos)
                mStartPos = mMaxPos;
            mEndPos += (mStartPos - saveStart);
            if (mEndPos > mMaxPos)
                mEndPos = mMaxPos;

            setOffsetGoalStart();
        }

        if (marker == mEndMarker) {
            mEndPos += velocity;
            if (mEndPos > mMaxPos)
                mEndPos = mMaxPos;

            setOffsetGoalEnd();
        }

        updateDisplay();
    }

    public void markerEnter(MarkerView marker) {
    }

    public void markerKeyUp() {
        mKeyDown = false;
        updateDisplay();
    }

    public void markerFocus(MarkerView marker) {
        mKeyDown = false;
        if (marker == mStartMarker) {
            setOffsetGoalStartNoUpdate();
        } else {
            setOffsetGoalEndNoUpdate();
        }

        // Delay updaing the display because if this focus was in
        // response to a touch event, we want to receive the touch
        // event too before updating the display.
        mHandler.postDelayed(new Runnable() {
            public void run() {
                updateDisplay();
            }
        }, 100);
    }

    //
    // Static About dialog method, also called from RingdroidSelectActivity
    //

    public static void onAbout(final Activity activity) {
        String versionName = "";
        try {
            PackageManager packageManager = activity.getPackageManager();
            String packageName = activity.getPackageName();
            versionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            versionName = "unknown";
        }
        new AlertDialog.Builder(activity)
                .setTitle(R.string.about_title)
                .setMessage(activity.getString(R.string.about_text, versionName))
                .setPositiveButton(R.string.alert_ok_button, null)
                .setCancelable(false)
                .show();
    }

    private void loadGui() {
        // Inflate our UI from its XML layout description.
        setContentView(R.layout.activity_createsounds);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDensity = metrics.density;

        mMarkerLeftInset = (int)(46 * mDensity);
        mMarkerRightInset = (int)(48 * mDensity);
        mMarkerTopOffset = (int)(10 * mDensity);
        mMarkerBottomOffset = (int)(10 * mDensity);

        mStartText = (TextView)findViewById(R.id.starttext);
        mStartText.addTextChangedListener(mTextWatcher);
        mEndText = (TextView)findViewById(R.id.endtext);
        mEndText.addTextChangedListener(mTextWatcher);

        mPlayButton = (ImageButton)findViewById(R.id.play);
        mPlayButton.setOnClickListener(mPlayListener);
        mRewindButton = (ImageButton)findViewById(R.id.rew);
        mRewindButton.setOnClickListener(mRewindListener);
        mFfwdButton = (ImageButton)findViewById(R.id.ffwd);
        mFfwdButton.setOnClickListener(mFfwdListener);

        TextView markStartButton = (TextView) findViewById(R.id.mark_start);
        markStartButton.setOnClickListener(mMarkStartListener);
        TextView markEndButton = (TextView) findViewById(R.id.mark_end);
        markEndButton.setOnClickListener(mMarkEndListener);


        createsound_save=(Button)findViewById(R.id.createsound_save);

        createsound_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
                Log.e("Smashsounds","Smashsounds");
            }
        });
        enableDisableButtons();

        mWaveformView = (WaveformView)findViewById(R.id.waveform);
        mWaveformView.setListener(this);

        mInfo = (TextView)findViewById(R.id.info);
        mInfo.setText(mCaption);

        mMaxPos = 0;
        mLastDisplayedStartPos = -1;
        mLastDisplayedEndPos = -1;

        if (mSoundFile != null && !mWaveformView.hasSoundFile()) {
            mWaveformView.setSoundFile(mSoundFile);
            mWaveformView.recomputeHeights(mDensity);
            mMaxPos = mWaveformView.maxPos();
        }

        mStartMarker = (MarkerView)findViewById(R.id.startmarker);
        mStartMarker.setListener(this);
        mStartMarker.setAlpha(1f);
        mStartMarker.setFocusable(true);
        mStartMarker.setFocusableInTouchMode(true);
        mStartVisible = true;

        mEndMarker = (MarkerView)findViewById(R.id.endmarker);
        mEndMarker.setListener(this);
        mEndMarker.setAlpha(1f);
        mEndMarker.setFocusable(true);
        mEndMarker.setFocusableInTouchMode(true);
        mEndVisible = true;

        updateDisplay();
    }
    private void loadFromFile() {
        mFile = new File(mFilename);

        SongMetadataReader metadataReader = new SongMetadataReader(
                this, mFilename);
        mTitle = metadataReader.mTitle;
        mArtist = metadataReader.mArtist;

        String titleLabel = mTitle;
        if (mArtist != null && mArtist.length() > 0) {
            titleLabel += " - " + mArtist;
        }
        setTitle(titleLabel);

        mLoadingLastUpdateTime = getCurrentTime();
        mLoadingKeepGoing = true;
        mFinishActivity = false;
        mProgressDialog = new ProgressDialog(CreatesoundsActivity.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setTitle(R.string.progress_dialog_loading);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(
                new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        mLoadingKeepGoing = false;
                        mFinishActivity = true;
                    }
                });
        mProgressDialog.show();

        final SoundFile.ProgressListener listener =
                new SoundFile.ProgressListener() {
                    public boolean reportProgress(double fractionComplete) {
                        long now = getCurrentTime();
                        if (now - mLoadingLastUpdateTime > 100) {
                            mProgressDialog.setProgress(
                                    (int) (mProgressDialog.getMax() * fractionComplete));
                            mLoadingLastUpdateTime = now;
                        }
                        return mLoadingKeepGoing;
                    }
                };

        // Load the sound file in a background thread
        mLoadSoundFileThread = new Thread() {
            public void run() {
                try {
                    mSoundFile = SoundFile.create(mFile.getAbsolutePath(), listener);

                    if (mSoundFile == null) {
                        mProgressDialog.dismiss();
                        String name = mFile.getName().toLowerCase();
                        String[] components = name.split("\\.");
                        String err;
                        if (components.length < 2) {
                            err = getResources().getString(
                                    R.string.no_extension_error);
                        } else {
                            err = getResources().getString(
                                    R.string.bad_extension_error) + " " +
                                    components[components.length - 1];
                        }
                        final String finalErr = err;
                        Runnable runnable = new Runnable() {
                            public void run() {
                                showFinalAlert(new Exception(), finalErr);
                            }
                        };
                        mHandler.post(runnable);
                        return;
                    }
                    mPlayer = new SamplePlayer(mSoundFile);
                } catch (final Exception e) {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                    mInfoContent = e.toString();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mInfo.setText(mInfoContent);
                        }
                    });

                    Runnable runnable = new Runnable() {
                        public void run() {
                            showFinalAlert(e, getResources().getText(R.string.read_error));
                        }
                    };
                    mHandler.post(runnable);
                    return;
                }
                mProgressDialog.dismiss();
                if (mLoadingKeepGoing) {
                    Runnable runnable = new Runnable() {
                        public void run() {
                            finishOpeningSoundFile();
                        }
                    };
                    mHandler.post(runnable);
                } else if (mFinishActivity){
                    CreatesoundsActivity.this.finish();
                }
            }
        };
        mLoadSoundFileThread.start();
    }

//    private void recordAudio() {
//        mFile = null;
//        mTitle = null;
//        mArtist = null;
//
//        mRecordingLastUpdateTime = getCurrentTime();
//        mRecordingKeepGoing = true;
//        mFinishActivity = false;
//        AlertDialog.Builder adBuilder = new AlertDialog.Builder(RingdroidEditActivity.this);
//        adBuilder.setTitle(getResources().getText(R.string.progress_dialog_recording));
//        adBuilder.setCancelable(true);
//        adBuilder.setNegativeButton(
//                getResources().getText(R.string.progress_dialog_cancel),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        mRecordingKeepGoing = false;
//                        mFinishActivity = true;
//                    }
//                });
//        adBuilder.setPositiveButton(
//                getResources().getText(R.string.progress_dialog_stop),
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        mRecordingKeepGoing = false;
//                    }
//                });
//        // TODO(nfaralli): try to use a FrameLayout and pass it to the following inflate call.
//        // Using null, android:layout_width etc. may not work (hence text is at the top of view).
//        // On the other hand, if the text is big enough, this is good enough.
//        adBuilder.setView(getLayoutInflater().inflate(R.layout.record_audio, null));
//        mAlertDialog = adBuilder.show();
//        mTimerTextView = (TextView)mAlertDialog.findViewById(R.id.record_audio_timer);
//
//        final SoundFile.ProgressListener listener =
//                new SoundFile.ProgressListener() {
//                    public boolean reportProgress(double elapsedTime) {
//                        long now = getCurrentTime();
//                        if (now - mRecordingLastUpdateTime > 5) {
//                            mRecordingTime = elapsedTime;
//                            // Only UI thread can update Views such as TextViews.
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    int min = (int)(mRecordingTime/60);
//                                    float sec = (float)(mRecordingTime - 60 * min);
//                                    mTimerTextView.setText(String.format("%d:%05.2f", min, sec));
//                                }
//                            });
//                            mRecordingLastUpdateTime = now;
//                        }
//                        return mRecordingKeepGoing;
//                    }
//                };
//
//        // Record the audio stream in a background thread
//        mRecordAudioThread = new Thread() {
//            public void run() {
//                try {
//                    mSoundFile = SoundFile.record(listener);
//                    if (mSoundFile == null) {
//                        mAlertDialog.dismiss();
//                        Runnable runnable = new Runnable() {
//                            public void run() {
//                                showFinalAlert(
//                                        new Exception(),
//                                        getResources().getText(R.string.record_error)
//                                );
//                            }
//                        };
//                        mHandler.post(runnable);
//                        return;
//                    }
//                    mPlayer = new SamplePlayer(mSoundFile);
//                } catch (final Exception e) {
//                    mAlertDialog.dismiss();
//                    e.printStackTrace();
//                    mInfoContent = e.toString();
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            mInfo.setText(mInfoContent);
//                        }
//                    });
//
//                    Runnable runnable = new Runnable() {
//                        public void run() {
//                            showFinalAlert(e, getResources().getText(R.string.record_error));
//                        }
//                    };
//                    mHandler.post(runnable);
//                    return;
//                }
//                mAlertDialog.dismiss();
//                if (mFinishActivity){
//                    CreatesoundsActivity.this.finish();
//                } else {
//                    Runnable runnable = new Runnable() {
//                        public void run() {
//                            finishOpeningSoundFile();
//                        }
//                    };
//                    mHandler.post(runnable);
//                }
//            }
//        };
//        mRecordAudioThread.start();
//    }

    private void finishOpeningSoundFile() {
        mWaveformView.setSoundFile(mSoundFile);
        mWaveformView.recomputeHeights(mDensity);

        mMaxPos = mWaveformView.maxPos();
        mLastDisplayedStartPos = -1;
        mLastDisplayedEndPos = -1;

        mTouchDragging = false;

        mOffset = 0;
        mOffsetGoal = 0;
        mFlingVelocity = 0;
        resetPositions();
        if (mEndPos > mMaxPos)
            mEndPos = mMaxPos;

        mCaption =
                mSoundFile.getFiletype() + ", " +
                        mSoundFile.getSampleRate() + " Hz, " +
                        mSoundFile.getAvgBitrateKbps() + " kbps, " +
                        formatTime(mMaxPos) + " " +
                        getResources().getString(R.string.time_seconds);
        mInfo.setText(mCaption);

        updateDisplay();
    }

    private synchronized void updateDisplay() {
        if (mIsPlaying) {
            int now = mPlayer.getCurrentPosition();
            int frames = mWaveformView.millisecsToPixels(now);
            mWaveformView.setPlayback(frames);
            setOffsetGoalNoUpdate(frames - mWidth / 2);
            if (now >= mPlayEndMsec) {
                handlePause();
            }
        }

        if (!mTouchDragging) {
            int offsetDelta;

            if (mFlingVelocity != 0) {
                offsetDelta = mFlingVelocity / 30;
                if (mFlingVelocity > 80) {
                    mFlingVelocity -= 80;
                } else if (mFlingVelocity < -80) {
                    mFlingVelocity += 80;
                } else {
                    mFlingVelocity = 0;
                }

                mOffset += offsetDelta;

                if (mOffset + mWidth / 2 > mMaxPos) {
                    mOffset = mMaxPos - mWidth / 2;
                    mFlingVelocity = 0;
                }
                if (mOffset < 0) {
                    mOffset = 0;
                    mFlingVelocity = 0;
                }
                mOffsetGoal = mOffset;
            } else {
                offsetDelta = mOffsetGoal - mOffset;

                if (offsetDelta > 10)
                    offsetDelta = offsetDelta / 10;
                else if (offsetDelta > 0)
                    offsetDelta = 1;
                else if (offsetDelta < -10)
                    offsetDelta = offsetDelta / 10;
                else if (offsetDelta < 0)
                    offsetDelta = -1;
                else
                    offsetDelta = 0;

                mOffset += offsetDelta;
            }
        }

        mWaveformView.setParameters(mStartPos, mEndPos, mOffset);
        mWaveformView.invalidate();

        mStartMarker.setContentDescription(
                getResources().getText(R.string.start_marker) + " " +
                        formatTime(mStartPos));
        mEndMarker.setContentDescription(
                getResources().getText(R.string.end_marker) + " " +
                        formatTime(mEndPos));

        int startX = mStartPos - mOffset - mMarkerLeftInset;
        if (startX + mStartMarker.getWidth() >= 0) {
            if (!mStartVisible) {
                // Delay this to avoid flicker
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        mStartVisible = true;
                        mStartMarker.setAlpha(1f);
                    }
                }, 0);
            }
        } else {
            if (mStartVisible) {
                mStartMarker.setAlpha(0f);
                mStartVisible = false;
            }
            startX = 0;
        }

        int endX = mEndPos - mOffset - mEndMarker.getWidth() + mMarkerRightInset;
        if (endX + mEndMarker.getWidth() >= 0) {
            if (!mEndVisible) {
                // Delay this to avoid flicker
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        mEndVisible = true;
                        mEndMarker.setAlpha(1f);
                    }
                }, 0);
            }
        } else {
            if (mEndVisible) {
                mEndMarker.setAlpha(0f);
                mEndVisible = false;
            }
            endX = 0;
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(
                startX,
                mMarkerTopOffset,
                -mStartMarker.getWidth(),
                -mStartMarker.getHeight());
        mStartMarker.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(
                endX,
                mWaveformView.getMeasuredHeight() - mEndMarker.getHeight() - mMarkerBottomOffset,
                -mStartMarker.getWidth(),
                -mStartMarker.getHeight());
        mEndMarker.setLayoutParams(params);
    }

    private Runnable mTimerRunnable = new Runnable() {
        public void run() {
            // Updating an EditText is slow on Android.  Make sure
            // we only do the update if the text has actually changed.
            if (mStartPos != mLastDisplayedStartPos &&
                    !mStartText.hasFocus()) {
                mStartText.setText(formatTime(mStartPos));
                mLastDisplayedStartPos = mStartPos;
            }

            if (mEndPos != mLastDisplayedEndPos &&
                    !mEndText.hasFocus()) {
                mEndText.setText(formatTime(mEndPos));
                mLastDisplayedEndPos = mEndPos;
            }

            mHandler.postDelayed(mTimerRunnable, 100);
        }
    };

    private void enableDisableButtons() {
        if (mIsPlaying) {
            mPlayButton.setImageResource(android.R.drawable.ic_media_pause);
            mPlayButton.setContentDescription(getResources().getText(R.string.stop));
        } else {
            mPlayButton.setImageResource(android.R.drawable.ic_media_play);
            mPlayButton.setContentDescription(getResources().getText(R.string.play));
        }
    }

    private void resetPositions() {
        mStartPos = mWaveformView.secondsToPixels(0.0);
        mEndPos = mWaveformView.secondsToPixels(15.0);
    }

    private int trap(int pos) {
        if (pos < 0)
            return 0;
        if (pos > mMaxPos)
            return mMaxPos;
        return pos;
    }

    private void setOffsetGoalStart() {
        setOffsetGoal(mStartPos - mWidth / 2);
    }

    private void setOffsetGoalStartNoUpdate() {
        setOffsetGoalNoUpdate(mStartPos - mWidth / 2);
    }

    private void setOffsetGoalEnd() {
        setOffsetGoal(mEndPos - mWidth / 2);
    }

    private void setOffsetGoalEndNoUpdate() {
        setOffsetGoalNoUpdate(mEndPos - mWidth / 2);
    }

    private void setOffsetGoal(int offset) {
        setOffsetGoalNoUpdate(offset);
        updateDisplay();
    }

    private void setOffsetGoalNoUpdate(int offset) {
        if (mTouchDragging) {
            return;
        }

        mOffsetGoal = offset;
        if (mOffsetGoal + mWidth / 2 > mMaxPos)
            mOffsetGoal = mMaxPos - mWidth / 2;
        if (mOffsetGoal < 0)
            mOffsetGoal = 0;
    }

    private String formatTime(int pixels) {
        if (mWaveformView != null && mWaveformView.isInitialized()) {
            return formatDecimal(mWaveformView.pixelsToSeconds(pixels));
        } else {
            return "";
        }
    }

    private String formatDecimal(double x) {
        int xWhole = (int)x;
        int xFrac = (int)(100 * (x - xWhole) + 0.5);

        if (xFrac >= 100) {
            xWhole++; //Round up
            xFrac -= 100; //Now we need the remainder after the round up
            if (xFrac < 10) {
                xFrac *= 10; //we need a fraction that is 2 digits long
            }
        }

        if (xFrac < 10)
            return xWhole + ".0" + xFrac;
        else
            return xWhole + "." + xFrac;
    }

    private synchronized void handlePause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        mWaveformView.setPlayback(-1);
        mIsPlaying = false;
        enableDisableButtons();
    }

    private synchronized void onPlay(int startPosition) {
        if (mIsPlaying) {
            handlePause();
            return;
        }

        if (mPlayer == null) {
            // Not initialized yet
            return;
        }

        try {
            mPlayStartMsec = mWaveformView.pixelsToMillisecs(startPosition);
            if (startPosition < mStartPos) {
                mPlayEndMsec = mWaveformView.pixelsToMillisecs(mStartPos);
            } else if (startPosition > mEndPos) {
                mPlayEndMsec = mWaveformView.pixelsToMillisecs(mMaxPos);
            } else {
                mPlayEndMsec = mWaveformView.pixelsToMillisecs(mEndPos);
            }
            mPlayer.setOnCompletionListener(new SamplePlayer.OnCompletionListener() {
                @Override
                public void onCompletion() {
                    handlePause();
                }
            });
            mIsPlaying = true;

            mPlayer.seekTo(mPlayStartMsec);
            mPlayer.start();
            updateDisplay();
            enableDisableButtons();
        } catch (Exception e) {
            showFinalAlert(e, R.string.play_error);
            return;
        }
    }

    /**
     * Show a "final" alert dialog that will exit the activity
     * after the user clicks on the OK button.  If an exception
     * is passed, it's assumed to be an error condition, and the
     * dialog is presented as an error, and the stack trace is
     * logged.  If there's no exception, it's a success message.
     */
    private void showFinalAlert(Exception e, CharSequence message) {
        CharSequence title;
        if (e != null) {
            Log.e("Ringdroid", "Error: " + message);
            Log.e("Ringdroid", getStackTrace(e));
            title = getResources().getText(R.string.alert_title_failure);
            setResult(RESULT_CANCELED, new Intent());
        } else {
            Log.v("Ringdroid", "Success: " + message);
            title = getResources().getText(R.string.alert_title_success);
        }

        new AlertDialog.Builder(CreatesoundsActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(
                        R.string.alert_ok_button,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                finish();
                            }
                        })
                .setCancelable(false)
                .show();
    }

    private void showFinalAlert(Exception e, int messageResourceId) {
        showFinalAlert(e, getResources().getText(messageResourceId));
    }

    private String makeRingtoneFilename(CharSequence title, String extension) {
        String subdir;
        sounddir = new File(CreatesoundsActivity.this.getFilesDir().getAbsolutePath(), "VA_Smash");

        if (!sounddir.exists()) {
            sounddir.mkdirs();
        }
        String externalRootDir = sounddir+ "/Smash";

//        switch(mNewFileKind) {
//            default:
//            case FileSaveDialog.FILE_KIND_MUSIC:
//                // TODO(nfaralli): can directly use Environment.getExternalStoragePublicDirectory(
//                // Environment.DIRECTORY_MUSIC).getPath() instead
//                subdir = "media/audio/music/";
//                break;
//            case FileSaveDialog.FILE_KIND_ALARM:
//                subdir = "media/audio/alarms/";
//                break;
//            case FileSaveDialog.FILE_KIND_NOTIFICATION:
//                subdir = "media/audio/notifications/";
//                break;
//            case FileSaveDialog.FILE_KIND_RINGTONE:
//                subdir = "media/audio/ringtones/";
//                break;
//        }
        String parentdir = externalRootDir;

        // Create the parent directory
        File parentDirFile = new File(parentdir);
        parentDirFile.mkdirs();

        // If we can't write to that special path, try just writing
        // directly to the sdcard
        if (!parentDirFile.isDirectory()) {
            parentdir = externalRootDir;
        }

        // Turn the title into a filename
        String filename = "";
        for (int i = 0; i < title.length(); i++) {
            if (Character.isLetterOrDigit(title.charAt(i))) {
                filename += title.charAt(i);
            }
        }

        // Try to make the filename unique
        String path = null;
        for (int i = 0; i < 100; i++) {
            String testPath;
            if (i > 0)
                testPath = parentdir + filename + i + extension;
            else
                testPath = parentdir + filename + extension;

            try {
                RandomAccessFile f = new RandomAccessFile(new File(testPath), "r");
                f.close();
            } catch (Exception e) {
                // Good, the file didn't exist
                path = testPath;
                break;
            }
        }

        return path;
    }

    private void saveRingtone(final String title) {
        Log.e("Smashsounds1",title);
        double startTime = mWaveformView.pixelsToSeconds(mStartPos);
        double endTime = mWaveformView.pixelsToSeconds(mEndPos);
        final int startFrame = mWaveformView.secondsToFrames(startTime);
        final int endFrame = mWaveformView.secondsToFrames(endTime);
        final int duration = (int)(endTime - startTime + 0.5);

        // Create an indeterminate progress dialog
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle(R.string.progress_dialog_saving);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        // Save the sound file in a background thread
        mSaveSoundFileThread = new Thread() {
            public void run() {
                // Try AAC first.
                String outPath = makeRingtoneFilename(title, ".mp3");
                if (outPath == null) {
                    Runnable runnable = new Runnable() {
                        public void run() {
                            showFinalAlert(new Exception(), R.string.no_unique_filename);
                        }
                    };
                    mHandler.post(runnable);
                    return;
                }
                File outFile = new File(outPath);
                Boolean fallbackToWAV = false;
                try {
                    // Write the new file
                    mSoundFile.WriteFile(outFile,  startFrame, endFrame - startFrame);
                } catch (Exception e) {
                    // log the error and try to create a .wav file instead
                    if (outFile.exists()) {
                        outFile.delete();
                    }
                    StringWriter writer = new StringWriter();
                    e.printStackTrace(new PrintWriter(writer));
                    Log.e("Ringdroid", "Error: Failed to create " + outPath);
                    Log.e("Ringdroid", writer.toString());
                    fallbackToWAV = true;
                }

                // Try to create a .wav file if creating a .m4a file failed.
                if (fallbackToWAV) {
                    outPath = makeRingtoneFilename(title, ".mp3");
                    if (outPath == null) {
                        Runnable runnable = new Runnable() {
                            public void run() {
                                showFinalAlert(new Exception(), R.string.no_unique_filename);
                            }
                        };
                        mHandler.post(runnable);
                        return;
                    }
                    outFile = new File(outPath);
                    try {
                        // create the .wav file
                        mSoundFile.WriteWAVFile(outFile, startFrame, endFrame - startFrame);
                    } catch (Exception e) {
                        // Creating the .wav file also failed. Stop the progress dialog, show an
                        // error message and exit.
                        mProgressDialog.dismiss();
                        if (outFile.exists()) {
                            outFile.delete();
                        }
                        mInfoContent = e.toString();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                mInfo.setText(mInfoContent);
                            }
                        });

                        CharSequence errorMessage;
                        if (e.getMessage() != null
                                && e.getMessage().equals("No space left on device")) {
                            errorMessage = getResources().getText(R.string.no_space_error);
                            e = null;
                        } else {
                            errorMessage = getResources().getText(R.string.write_error);
                        }
                        final CharSequence finalErrorMessage = errorMessage;
                        final Exception finalException = e;
                        Runnable runnable = new Runnable() {
                            public void run() {
                                showFinalAlert(finalException, finalErrorMessage);
                            }
                        };
                        mHandler.post(runnable);
                        return;
                    }
                }

                // Try to load the new file to make sure it worked
                try {
                    final SoundFile.ProgressListener listener =
                            new SoundFile.ProgressListener() {
                                public boolean reportProgress(double frac) {
                                    // Do nothing - we're not going to try to
                                    // estimate when reloading a saved sound
                                    // since it's usually fast, but hard to
                                    // estimate anyway.
                                    return true;  // Keep going
                                }
                            };
                    SoundFile.create(outPath, listener);
                } catch (final Exception e) {
                    mProgressDialog.dismiss();
                    e.printStackTrace();
                    mInfoContent = e.toString();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            mInfo.setText(mInfoContent);
                        }
                    });

                    Runnable runnable = new Runnable() {
                        public void run() {
                            showFinalAlert(e, getResources().getText(R.string.write_error));
                        }
                    };
                    mHandler.post(runnable);
                    return;
                }

                mProgressDialog.dismiss();

                final String finalOutPath = outPath;
                Runnable runnable = new Runnable() {
                    public void run() {

                        Intent intent=new Intent(CreatesoundsActivity.this, PostSoundActivity.class);
                        intent.putExtra("path",finalOutPath);
                        startActivity(intent);
                        Log.e("finalOutPath",finalOutPath);

//                        afterSavingRingtone(title,
//                                finalOutPath,
//                                duration);
                    }
                };
                mHandler.post(runnable);
            }
        };
        mSaveSoundFileThread.start();
    }
//
//    private void afterSavingRingtone(CharSequence title,
//                                     String outPath,
//                                     int duration) {
//        File outFile = new File(outPath);
//        long fileSize = outFile.length();
//        if (fileSize <= 512) {
//            outFile.delete();
//            new AlertDialog.Builder(this)
//                    .setTitle(R.string.alert_title_failure)
//                    .setMessage(R.string.too_small_error)
//                    .setPositiveButton(R.string.alert_ok_button, null)
//                    .setCancelable(false)
//                    .show();
//            return;
//        }
//
//        // Create the database record, pointing to the existing file path
//        String mimeType;
//        if (outPath.endsWith(".m4a")) {
//            mimeType = "audio/mp4a-latm";
//        } else if (outPath.endsWith(".wav")) {
//            mimeType = "audio/wav";
//        } else {
//            // This should never happen.
//            mimeType = "audio/mpeg";
//        }
//
//        String artist = "" + getResources().getText(R.string.artist_name);
//
//        ContentValues values = new ContentValues();
//        values.put(MediaStore.MediaColumns.DATA, outPath);
//        values.put(MediaStore.MediaColumns.TITLE, title.toString());
//        values.put(MediaStore.MediaColumns.SIZE, fileSize);
//        values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
//
//        values.put(MediaStore.Audio.Media.ARTIST, artist);
//        values.put(MediaStore.Audio.Media.DURATION, duration);
//
//        values.put(MediaStore.Audio.Media.IS_RINGTONE,
//                mNewFileKind == FileSaveDialog.FILE_KIND_RINGTONE);
//        values.put(MediaStore.Audio.Media.IS_NOTIFICATION,
//                mNewFileKind == FileSaveDialog.FILE_KIND_NOTIFICATION);
//        values.put(MediaStore.Audio.Media.IS_ALARM,
//                mNewFileKind == FileSaveDialog.FILE_KIND_ALARM);
//        values.put(MediaStore.Audio.Media.IS_MUSIC,
//                mNewFileKind == FileSaveDialog.FILE_KIND_MUSIC);
//
//        // Insert it into the database
//        Uri uri = MediaStore.Audio.Media.getContentUriForPath(outPath);
//        final Uri newUri = getContentResolver().insert(uri, values);
//        setResult(RESULT_OK, new Intent().setData(newUri));
//
//        // If Ringdroid was launched to get content, just return
//        if (mWasGetContentIntent) {
//            finish();
//            return;
//        }
//
//        // There's nothing more to do with music or an alarm.  Show a
//        // success message and then quit.
//        if (mNewFileKind == FileSaveDialog.FILE_KIND_MUSIC ||
//                mNewFileKind == FileSaveDialog.FILE_KIND_ALARM) {
//            Toast.makeText(this,
//                    R.string.save_success_message,
//                    Toast.LENGTH_SHORT)
//                    .show();
//            finish();
//            return;
//        }
//
//        // If it's a notification, give the user the option of making
//        // this their default notification.  If they say no, we're finished.
//        if (mNewFileKind == FileSaveDialog.FILE_KIND_NOTIFICATION) {
//            new AlertDialog.Builder(RingdroidEditActivity.this)
//                    .setTitle(R.string.alert_title_success)
//                    .setMessage(R.string.set_default_notification)
//                    .setPositiveButton(R.string.alert_yes_button,
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog,
//                                                    int whichButton) {
//                                    RingtoneManager.setActualDefaultRingtoneUri(
//                                            RingdroidEditActivity.this,
//                                            RingtoneManager.TYPE_NOTIFICATION,
//                                            newUri);
//                                    finish();
//                                }
//                            })
//                    .setNegativeButton(
//                            R.string.alert_no_button,
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    finish();
//                                }
//                            })
//                    .setCancelable(false)
//                    .show();
//            return;
//        }
//
//        // If we get here, that means the type is a ringtone.  There are
//        // three choices: make this your default ringtone, assign it to a
//        // contact, or do nothing.
//
//        final Handler handler = new Handler() {
//            public void handleMessage(Message response) {
//                int actionId = response.arg1;
//                switch (actionId) {
//                    case R.id.button_make_default:
//                        RingtoneManager.setActualDefaultRingtoneUri(
//                                CreatesoundsActivity.this,
//                                RingtoneManager.TYPE_RINGTONE,
//                                newUri);
//                        Toast.makeText(
//                                CreatesoundsActivity.this,
//                                R.string.default_ringtone_success_message,
//                                Toast.LENGTH_SHORT)
//                                .show();
//                        finish();
//                        break;
//                    case R.id.button_choose_contact:
//                        chooseContactForRingtone(newUri);
//                        break;
//                    default:
//                    case R.id.button_do_nothing:
//                        finish();
//                        break;
//                }
//            }
//        };
//        Message message = Message.obtain(handler);
//        AfterSaveActionDialog dlog = new AfterSaveActionDialog(
//                this, message);
//        dlog.show();
//    }

    private void chooseContactForRingtone(Uri uri) {
        try {
            Intent intent = new Intent(Intent.ACTION_EDIT, uri);
            intent.setClassName(
                    "com.ringdroid",
                    "com.ringdroid.ChooseContactActivity");
            startActivityForResult(intent, REQUEST_CODE_CHOOSE_CONTACT);
        } catch (Exception e) {
            Log.e("Ringdroid", "Couldn't open Choose Contact window");
        }
    }

    private void onSave() {
        if (mIsPlaying) {
            handlePause();
        }
        saveRingtone("Smashsounds");
        Log.e("Smashsounds2","Smashsounds");
//        final Handler handler = new Handler() {
//            public void handleMessage(Message response) {
//                CharSequence newTitle = (CharSequence)response.obj;
//                mNewFileKind = response.arg1;
//                saveRingtone("Smashsounds");
//                Log.e("Smashsounds2","Smashsounds");
//            }
//        };
//        Message message = Message.obtain(handler);
//        FileSaveDialog dlog = new FileSaveDialog(
//                this, getResources(), mTitle, message);
//        dlog.show();
    }

    private View.OnClickListener mPlayListener = new View.OnClickListener() {
        public void onClick(View sender) {
            onPlay(mStartPos);
        }
    };

    private View.OnClickListener mRewindListener = new View.OnClickListener() {
        public void onClick(View sender) {
            if (mIsPlaying) {
                int newPos = mPlayer.getCurrentPosition() - 5000;
                if (newPos < mPlayStartMsec)
                    newPos = mPlayStartMsec;
                mPlayer.seekTo(newPos);
            } else {
                mStartMarker.requestFocus();
                markerFocus(mStartMarker);
            }
        }
    };

    private View.OnClickListener mFfwdListener = new View.OnClickListener() {
        public void onClick(View sender) {
            if (mIsPlaying) {
                int newPos = 5000 + mPlayer.getCurrentPosition();
                if (newPos > mPlayEndMsec)
                    newPos = mPlayEndMsec;
                mPlayer.seekTo(newPos);
            } else {
                mEndMarker.requestFocus();
                markerFocus(mEndMarker);
            }
        }
    };

    private View.OnClickListener mMarkStartListener = new View.OnClickListener() {
        public void onClick(View sender) {
            if (mIsPlaying) {
                mStartPos = mWaveformView.millisecsToPixels(
                        mPlayer.getCurrentPosition());
                updateDisplay();
            }
        }
    };

    private View.OnClickListener mMarkEndListener = new View.OnClickListener() {
        public void onClick(View sender) {
            if (mIsPlaying) {
                mEndPos = mWaveformView.millisecsToPixels(
                        mPlayer.getCurrentPosition());
                updateDisplay();
                handlePause();
            }
        }
    };

    private TextWatcher mTextWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        public void onTextChanged(CharSequence s,
                                  int start, int before, int count) {
        }

        public void afterTextChanged(Editable s) {
            if (mStartText.hasFocus()) {
                try {
                    mStartPos = mWaveformView.secondsToPixels(
                            Double.parseDouble(
                                    mStartText.getText().toString()));
                    updateDisplay();
                } catch (NumberFormatException e) {
                }
            }
            if (mEndText.hasFocus()) {
                try {
                    mEndPos = mWaveformView.secondsToPixels(
                            Double.parseDouble(
                                    mEndText.getText().toString()));
                    updateDisplay();
                } catch (NumberFormatException e) {
                }
            }
        }
    };

    private long getCurrentTime() {
        return System.nanoTime() / 1000000;
    }

    private String getStackTrace(Exception e) {
        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }

}
