package com.vasmash.va_smash.createcontent.cameraedit;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import com.squareup.picasso.Picasso;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.VASmashAPIS.APIs;
import com.vasmash.va_smash.createcontent.Sounds.Sound_catemodel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Abdul Haq (it.haq.life) on 11-07-2017.
 */

public class MyCanvas extends AppCompatActivity {

    private static final String TAG = MyCanvas.class.getSimpleName();
    public static final int PERM_RQST_CODE = 110;
    public StickerView stickerView;
    private TextSticker sticker;
    private EditText editText;
    private LinearLayout editTextLayout;
    private int textColor;
    private String fontype="Arial";
    public InputMethodManager keyboard;
    public LinearLayout selectSticker;
    private StickerAdapter stickerAdapter;
    private boolean loadSticker = true;
    private GridView stickersGrid;
    public File dir;
    public String defaultVideo;
    public String defaultSound;
    public String m_Text = null;
    private ArrayList<Integer> colorPickerColors;
    private List<String> forntPickerColors;
    private List<Bitmap> imageBitmaps1;
    private List<Bitmap> imageBitmaps2;
    private List<String> frametxt;
    private FrameAdapter frameAdapter;
    private ArrayList<Sticker_model> stickerurl;
    private RequestQueue mQueue;
    private  RecyclerView sticker_cate_recy;
    private List<Sound_catemodel> personUtils12;
    Sticker_cate_Adapter sticker_cate_adapter;
    String stickercatid="5ec665fcb02b36798ea55a20";

/*
    private Integer[]  sticker_images = {
            R.drawable.sticker1,R.drawable.sticker2,R.drawable.sticker3,R.drawable.sticker4,R.drawable.sticker5,R.drawable.sticker6,R.drawable.sticker7,R.drawable.sticker9,R.drawable.sticker10,R.drawable.sticker11,
            R.drawable.sticker12,R.drawable.sticker13,R.drawable.sticker14,R.drawable.sticker15,R.drawable.sticker16,R.drawable.sticker17,R.drawable.sticker18,R.drawable.sticker19,R.drawable.sticker20,R.drawable.sticker21,
            R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4, R.drawable.s5, R.drawable.s6, R.drawable.s7, R.drawable.s8, R.drawable.s9, R.drawable.s10, R.drawable.s11, R.drawable.s12, R.drawable.s13, R.drawable.s14, R.drawable.s15, R.drawable.s16, R.drawable.s17, R.drawable.s18, R.drawable.s19, R.drawable.s20,
            R.drawable.s21, R.drawable.s22, R.drawable.s23, R.drawable.s24
    };
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        editText = (EditText) findViewById(R.id.editText);
        editTextLayout = (LinearLayout) findViewById(R.id.editTextLayout);
        keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        selectSticker  = (LinearLayout) findViewById(R.id.select_sticker);
        stickersGrid = (GridView) findViewById(R.id.sticker_grid);
        sticker_cate_recy = (RecyclerView) findViewById(R.id.sticker_cate_recy);
        LinearLayoutManager layoutManager1= new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        sticker_cate_recy.setLayoutManager(layoutManager1);
        textColor = Color.WHITE;
        stickerlist(stickercatid);
        //  stickerAdapter = new StickerAdapter(this);

        stickerlistcate();
        RecyclerView addTextForntPickerRecyclerView = (RecyclerView) findViewById(R.id.add_text_fornt_picker_recycler_view);
        RecyclerView addFramePickerRecyclerView = (RecyclerView) findViewById(R.id.main_frame_image_rv);
        final RecyclerView addTextColorPickerRecyclerView = (RecyclerView)findViewById(R.id.add_text_color_picker_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MyCanvas.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager forntlayoutManager = new LinearLayoutManager(MyCanvas.this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerframe = new LinearLayoutManager(MyCanvas.this, LinearLayoutManager.HORIZONTAL, false);

        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextForntPickerRecyclerView.setLayoutManager(forntlayoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        addTextForntPickerRecyclerView.setHasFixedSize(true);
        addFramePickerRecyclerView.setLayoutManager(layoutManagerframe);
        addFramePickerRecyclerView.setHasFixedSize(true);
        addFramePickerRecyclerView.setPadding(20,20,20,20);

        colorPickerColors = new ArrayList<>();
        colorPickerColors.add(getResources().getColor(R.color.black));
        colorPickerColors.add(getResources().getColor(R.color.blue_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.brown_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.green_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.orange_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.red_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.red_orange_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.sky_blue_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.violet_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.white));
        colorPickerColors.add(getResources().getColor(R.color.yellow_color_picker));
        colorPickerColors.add(getResources().getColor(R.color.yellow_green_color_picker));
        forntPickerColors=new ArrayList<>();
        forntPickerColors.add("Arial");
        forntPickerColors.add("Lucida");
        forntPickerColors.add("Poppins");
        forntPickerColors.add("Bree Serif");

        TypedArray images2 = getResources().obtainTypedArray(R.array.photo_editor_frames1);
        TypedArray images1 = getResources().obtainTypedArray(R.array.photo_editor_frames2);
        imageBitmaps1 =new ArrayList<>();
        for (int i = 0; i < images1.length(); i++) {
            imageBitmaps1.add(decodeSampledBitmapFromResource(this.getResources(), images1.getResourceId(i, -1), 120, 120));
        }
        imageBitmaps2 =new ArrayList<>();
        for (int i = 0; i < images1.length(); i++) {
            imageBitmaps2.add(decodeSampledBitmapFromResource(this.getResources(), images2.getResourceId(i, -1), 120, 120));
        }
        frametxt =new ArrayList<>();
        frametxt.add("YouMuder");
        frametxt.add("Saucer");
        frametxt.add("Cheri");
        frametxt.add("Wonder land");
        frametxt.add("Depresion");

        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(MyCanvas.this, colorPickerColors);
        ForntPickerAdapter forntPickerAdapter=new ForntPickerAdapter(MyCanvas.this ,forntPickerColors);
        frameAdapter = new FrameAdapter(MyCanvas.this, imageBitmaps1,frametxt);

        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        addTextForntPickerRecyclerView.setAdapter(forntPickerAdapter);
        addFramePickerRecyclerView.setAdapter(frameAdapter);
        frameAdapter.setOnFrameClickListener(new FrameAdapter.OnFrameClickListener() {
            @Override
            public void onFrameClickListener(String frm) {
                Log.e("fram",frm);
                if (frm.equals("YouMuder")){
                    Typeface face1= Typeface.createFromAsset(getAssets(), "youmurdererbb_reg.ttf");
                    editText.setTypeface(face1);
                    fontype="YouMuder";

                }
                if (frm.equals("Saucer")){
                    Typeface face1= Typeface.createFromAsset(getAssets(), "saucerbb.ttf");
                    editText.setTypeface(face1);
                    fontype="Saucer";

                }
                if (frm.equals("Cheri")){
                    Typeface face1= Typeface.createFromAsset(getAssets(), "cheri.TTF");
                    editText.setTypeface(face1);
                    fontype="Cheri";

                }
                if (frm.equals("Wonder land")){
                    Typeface face1= Typeface.createFromAsset(getAssets(), "beyondwonderland.ttf");
                    editText.setTypeface(face1);
                    fontype="Wonder land";

                }
                if (frm.equals("Depresion")){
                    Typeface face1= Typeface.createFromAsset(getAssets(), "depressionist_revisited_2010.ttf");
                    editText.setTypeface(face1);
                    fontype="Depresion";

                }

            }
        });








        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                Log.e("color ", String.valueOf(colorCode));
                textColor=colorCode;
                editText.setTextColor(colorCode);

            }
        });
        forntPickerAdapter.setonForntPickerClickListener(new ForntPickerAdapter.OnForntPickerClickListener() {
            @Override
            public void onForntPickerClickListener(String forntimg) {
                Log.e("fornt",forntimg);
                if (forntimg.equals("Arial")){
                    Typeface face1= Typeface.createFromAsset(getAssets(), "boring_Sans_B_Bold_Italic_trial.ttf");
                    editText.setTypeface(face1);
                    fontype="Arial";

                }
                if (forntimg.equals("Lucida")){
                    Typeface face1= Typeface.createFromAsset(getAssets(), "handlerregular.ttf");
                    editText.setTypeface(face1);
                    fontype="Lucida";

                }
                if (forntimg.equals("Poppins")){
                    Typeface face1= Typeface.createFromAsset(getAssets(), "poppins_semibold.ttf");
                    editText.setTypeface(face1);
                    fontype="Poppins";

                }
                if (forntimg.equals("Bree Serif")){
                    Typeface face1= Typeface.createFromAsset(getAssets(), "arialbd.ttf");
                    editText.setTypeface(face1);
                    fontype="Bree Serif";

                }



            }
        });






        LinearGradient test = new LinearGradient(0.f, 0.f, 700.f, 0.0f,
                new int[] {0xFF000000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF,
                        0xFFFF0000, 0xFFFF00FF, 0xFFFFFF00, 0xFFFFFFFF}, null, Shader.TileMode.CLAMP);
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RectShape());
        shapeDrawable.getPaint().setShader(test);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar_font);
        final View colorSelected = (View) findViewById(R.id.colorSelected);
        seekBar.setProgressDrawable((Drawable)shapeDrawable);

        seekBar.setMax(256*7-1);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int r = 0;
                    int g = 0;
                    int b = 0;

                    if(progress < 256) {
                        b = progress;
                    } else if (progress < 256*2) {
                        g = progress%256;
                        b = 256 - progress%256;
                    } else if (progress < 256 * 4) {
                        r = progress%256;
                        g = 256 - progress%256;
                        b = 256 - progress%256;
                    } else if (progress < 256 * 5) {
                        r = 255;
                        g = 0;
                        b = progress%256;
                    } else if (progress < 256 * 6) {
                        r = 255;
                        g = progress%256;
                        b = 256 - progress%256;
                    } else if (progress < 256 * 7) {
                        r = 255;
                        g = 255;
                        b = progress%256;
                    }
                    colorSelected.setBackgroundColor(Color.argb(255, r, g, b));
                    textColor = Color.argb(255, r, g, b);
                    editText.setTextColor(textColor);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        stickersGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String sticker_bitmap = sticker_links[position];
                ImageView iv = (ImageView) view.findViewById(R.id.sticker_grid_item);
                Drawable drawable = iv.getDrawable();
                if (drawable != null) {
                    stickerView.addSticker(new DrawableSticker(drawable));
                    stickerOptions();
                }
            }
        });
    }

    public void setStickerView(int stickerV) {
        Log.i("setStickerView","Called");
        if (stickerV == 0) {
            stickerView = (StickerView) findViewById(R.id.sticker_view);
            StickerView SvVideo = (StickerView) findViewById(R.id.sticker_view1);
            SvVideo.setBackgroundColor(0);
        } else if (stickerV == 1) {
            stickerView = (StickerView) findViewById(R.id.sticker_view1);
            StickerView SvVideo = (StickerView) findViewById(R.id.sticker_view1);
            SvVideo.setBackgroundColor(0);
        }

        //currently you can config your own icons and icon event. the event you can custom
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_close_white),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());

        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_scale_white),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());

        stickerView.setIcons(Arrays.asList(deleteIcon, zoomIcon));

        stickerView.setBackgroundColor(Color.WHITE);
        stickerView.setLocked(false);
        stickerView.setConstrained(true);

        sticker = new TextSticker(this);

        stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            @Override
            public void onStickerAdded(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerAdded");
            }

            @Override
            public void onStickerClicked(@NonNull Sticker sticker) {
                //stickerView.removeAllSticker();
                if (sticker instanceof TextSticker) {
                    //((TextSticker) sticker).setTextColor(Color.RED);
                    //stickerView.replace(sticker);
                    //stickerView.invalidate();
                    String stext = ((TextSticker) sticker).getText();
                    editText.setText(stext);
                    stickerView.removeCurrentSticker();
                    editTextLayout.setVisibility(View.VISIBLE);
                    showKeyboard(true);
                    editText.requestFocus();
                }
                Log.d(TAG, "onStickerClicked");
            }

            @Override
            public void onStickerDeleted(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDeleted");
            }

            @Override
            public void onStickerDragFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerDragFinished");
            }

            @Override
            public void onStickerZoomFinished(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerZoomFinished");
            }

            @Override
            public void onStickerFlipped(@NonNull Sticker sticker) {
                Log.d(TAG, "onStickerFlipped");
            }

            @Override
            public void onStickerDoubleTapped(@NonNull Sticker sticker) {
                Log.d(TAG, "onDoubleTapped: double tap will be with two click");
            }
        });
    }

    public void showHideEditText() {

        int getEditTextVisibility = editTextLayout.getVisibility();
        if (getEditTextVisibility == View.VISIBLE) {
            String sText = editText.getText().toString();
            addText(sText, textColor,fontype);
            showKeyboard(false);
            editTextLayout.setVisibility(View.GONE);



        } else {
            editText.setText("");
            editText.setTextColor(Color.WHITE);
            editTextLayout.setVisibility(View.VISIBLE);
            showKeyboard(true);
            editText.requestFocus();

        }
    }

    public void showKeyboard(boolean show) {
        if (show) {
            keyboard.toggleSoftInput(InputMethodManager.SHOW_FORCED, 1);
        } else {
            keyboard.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
        Log.i("Keyboard function","triggered");
    }

    public void addText(String stickerText, int color, String font) {
        if (!stickerText.equals("")) {
            final TextSticker sticker = new TextSticker(this);
            m_Text=stickerText;

            frametxt.add(m_Text);
            frametxt.add(m_Text);
            frametxt.add(m_Text);
            frametxt.add(m_Text);
            frameAdapter.notifyDataSetChanged();

            sticker.setText(stickerText);
            sticker.setTextColor(color);
            sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER);
            sticker.resizeText();
            if (font.equals("Arial")){
                Typeface face1= Typeface.createFromAsset(getAssets(), "arialbd.ttf");
                sticker.setTypeface(face1);
                fontype="Arial";

            }
            if (font.equals("Lucida")){
                Typeface face1= Typeface.createFromAsset(getAssets(), "poppins_semibold.ttf");
                sticker.setTypeface(face1);
                fontype="Lucida";

            }
            if (font.equals("Poppins")){
                Typeface face1= Typeface.createFromAsset(getAssets(), "poppins_semibold.ttf");
                sticker.setTypeface(face1);
                fontype="Poppins";

            }
            if (font.equals("Bree Serif")){
                Typeface face1= Typeface.createFromAsset(getAssets(), "bree_serif.ttf");
                sticker.setTypeface(face1);
                fontype="Bree Serif";

            }

            if (font.equals("YouMuder")){
                Typeface face1= Typeface.createFromAsset(getAssets(), "youmurdererbb_reg.ttf");
                sticker.setTypeface(face1);
                fontype="YouMuder";

            }
            if (font.equals("Saucer")){
                Typeface face1= Typeface.createFromAsset(getAssets(), "saucerbb.ttf");
                sticker.setTypeface(face1);
                fontype="Saucer";

            }
            if (font.equals("Cheri")){
                Typeface face1= Typeface.createFromAsset(getAssets(), "cheri.TTF");
                sticker.setTypeface(face1);
                fontype="Cheri";

            }
            if (font.equals("Wonder land")){
                Typeface face1= Typeface.createFromAsset(getAssets(), "beyondwonderland.ttf");
                sticker.setTypeface(face1);
                fontype="Wonder land";

            }
            if (font.equals("Depresion")){
                Typeface face1= Typeface.createFromAsset(getAssets(), "depressionist_revisited-2010.ttf");
                sticker.setTypeface(face1);
                fontype="Depresion";

            }
            if (font.equals(null)) {
                Typeface face1= Typeface.createFromAsset(getAssets(), "arialbd.ttf");
                sticker.setTypeface(face1);
                fontype="Depresion";

            }

            stickerView.addSticker(sticker);
        }
    }

    public void stickerOptions() {
        if (selectSticker.getVisibility() == View.VISIBLE) {
            selectSticker.setVisibility(View.GONE);
        } else {
            if (loadSticker) {}
            stickersGrid.setAdapter(stickerAdapter);
            selectSticker.setVisibility(View.VISIBLE);
        }
    }

    public class StickerAdapter extends BaseAdapter {

        private Context activity;
        private LayoutInflater inflater;
        ArrayList<Sticker_model> stickerurl;

        private StickerAdapter(Context activity  , ArrayList<Sticker_model> stickerurl) {
            this.activity = activity;
            this.stickerurl=stickerurl;
        }

        @Override
        public int getCount() {
            return stickerurl.size();
        }

        @Override
        public Object getItem(int location) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.item_sticker_gridview, null);

            ImageView girdPhoto = (ImageView) convertView.findViewById(R.id.sticker_grid_item);

            Sticker_model stck = stickerurl.get(position);
            // set gird Photos
            //  girdPhoto.setImageResource(stck.getStickerurl());

            Log.e("stick",stck.getStickerurl());

            Picasso.with(activity).load(stck.getStickerurl()).into(girdPhoto);
            return convertView;
        }
    }

    public void testRemove(View view) {
        if (stickerView.removeCurrentSticker()) {
            Toast.makeText(this, "Remove current Sticker successfully!", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(this, "Remove current Sticker failed!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void removeAllStickers() {
        stickerView.removeAllStickers();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERM_RQST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //loadSticker();
        }
    }
    public Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }



    //    public void JSONSenderVolleychecked() {
//        // Log.d("---reqotpurl-----", "---" + login_url);
//
//        String AddS = "{\"categoryId\":\"" + categoryId + "\",\"contentLangId\":\"" + lang_id + "\",\"_id\":\"" + img_id + "\",\"userId\":\"" + userid + "\",\"description\":\"" + descp + "\",\"visibility\":"+ post_visibilty + "}";
//        Log.d("jsnresponse pernonal", "---" + AddS);
//        JSONObject lstrmdt;
//
//        try {
//            lstrmdt = new JSONObject(AddS);
//            Log.d("jsnresponse....", "---" + AddS);
//            JSONSenderVolleychecked(lstrmdt);
//        } catch (JSONException ignored) {
//        }
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, APIs.add_description_content,lstrmdt,
//
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("JSONSenderprofile", "---" + response);
//
//                        //  spinner.setVisibility(View.INVISIBLE);
//                        sendpost.setClickable(true);
//                        dialog.cancel();
//                        try {
//                            if (response.length()!=0) {
//                                String message = response.getString("message");
//                                if (message.equals("Post Added successfully")) {
////                                    viewDialog.hideDialog();
//
//                                    //  Toast.makeText(PostcontentActivity.this, message, Toast.LENGTH_SHORT).show();
//
//                                    dialog.cancel();
//                                    dialog("10");
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // spinner.setVisibility(View.INVISIBLE);
//                String body;
//                dialog.cancel();
//                //get status code here
//                //Log.d("statusCode", "---" + error.toString());
//                NetworkResponse response = error.networkResponse;
//                if(response != null && response.data != null){
//                    switch(response.statusCode){
//                        case 422:
//                            try {
//                                body = new String(error.networkResponse.data,"UTF-8");
//                                Log.d("body", "---" + body);
//                                JSONObject obj = new JSONObject(body);
//                                if (obj.has("errors")) {
////                                    viewDialog.hideDialog();
//                                    JSONObject errors = obj.getJSONObject("errors");
//                                    String message = errors.getString("message");
//                                    //Toast.makeText(PersonalInformation.this, message, Toast.LENGTH_SHORT).show();
//
//                                }
//
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            break;
//
//                    }
//                }
//                //do stuff with the body...
//            }
//        })
//
//        {
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<>();
//                //headers.put("Accept", "application/json");
//                headers.put("Content-Type", "application/json");
//                headers.put("Authorization",post);
//                //return (headers != null || headers.isEmpty()) ? headers : super.getHeaders();
//                return headers;
//            }
//        };
//        jsonObjReq.setTag("");
//        addToRequestQueue(jsonObjReq);
//    }
    public <T> void addToRequestQueue(Request<T> req) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(this);
        }
        mQueue.add(req);
        req.setRetryPolicy(new RetryPolicy() {
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

//                viewDialog.hideDialog();

            }
        });

    }

    public void stickerlistcate() {
//        spinner2.setVisibility(View.VISIBLE);

        mQueue = Volley.newRequestQueue(this.getApplicationContext());

        // prepare the Request
        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, APIs.Stickers, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("soundresponse1", response.toString());

//                        sounds_listview = (ListView) layout.findViewById(R.id.sounds_listview);


                        personUtils12 = new ArrayList<>();

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
                                    personUtils12.add(Sound_catemodel);


                                    Log.d("sticker", "createddateL1:::" + personUtils12);
                                    Log.d("sticker", "sticker:::" + _id + name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            sticker_cate_adapter = new Sticker_cate_Adapter(getApplicationContext(), personUtils12);
                            sticker_cate_recy.setAdapter(sticker_cate_adapter);

                            sticker_cate_adapter.setonStickercateClickListener(new Sticker_cate_Adapter.OnStickercateClickListener() {
                                @Override
                                public void onStickercateClickListener(Sound_catemodel code) {
                                    String sticker_cate_code=code.getSound_code();
                                    Log.e("sticker_cate_code",sticker_cate_code);
                                }
                            });

//                            sounds_cate_adapter.setonSoundcateClickListener(new Sounds_Cate_Adapter.OnSoundcateClickListener() {
//                                @Override
//                                public void onSoundcateClickListener(Sound_catemodel code) {
//
//                                    sound_cate_code=code.getSound_code();
//                                    Log.e("sound_cate_code",sound_cate_code);
//                                    soundlist(sound_cate_code);
//                                }
//                            });
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
    public void stickerlist(String stickerid) {
//        spinner2.setVisibility(View.VISIBLE);
        Log.d("stickerParseuser1", "store data1" + APIs.Stickerslist+stickerid);
        mQueue = Volley.newRequestQueue(this.getApplicationContext());

        // prepare the Request
        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, APIs.Stickerslist+stickerid, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // display response
                        Log.d("stickerresponse", response.toString());

//                        sounds_listview = (ListView) layout.findViewById(R.id.sounds_listview);






                        stickerurl = new ArrayList<>();





                        if (response.length() != 0) {
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject employee = response.getJSONObject(i);
                                    String iii=employee.getString("stickers");

                                    Log.d("iii", "createddateL:::" + iii);
                                    JSONArray js=new JSONArray(iii);

                                    for(int z = 0; z < js.length(); z++)
                                    {
                                        JSONObject sund = js.getJSONObject(z);

                                        Sticker_model sticker_model = new Sticker_model();
                                        String _id = sund.getString("_id");
                                        String name = sund.getString("url");
                                        boolean sel = false;

                                        sticker_model.setSticker_code(_id);
                                        sticker_model.setStickerurl(name);
                                        sticker_model.setSelected(sel);
                                        stickerurl.add(sticker_model);


                                        Log.d("sticker2", "createddateL:::" + stickerurl);
                                        Log.d("sticker2", "soundlst:::" + _id + name);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            stickerAdapter = new StickerAdapter(getApplicationContext(), stickerurl);

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
