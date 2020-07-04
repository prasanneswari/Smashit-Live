package com.vasmash.va_smash.HomeScreen.CommentScreen.CommentAdapters;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vasmash.va_smash.R;

import static com.vasmash.va_smash.HomeScreen.CommentScreen.CommentsFragment.commenttxt;

public class Adapter_CommentImg extends RecyclerView.Adapter<Adapter_CommentImg.ViewHolder>{

    int[] SubjectValues;
    Context context;
    View view1;
    ViewHolder viewHolder1;

    public Adapter_CommentImg(Context context1,int[] SubjectValues1){

        SubjectValues = SubjectValues1;
        context = context1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView commentimgadap;

        public ViewHolder(View v){

            super(v);

            commentimgadap = v.findViewById(R.id.commentimgadap);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        view1 = LayoutInflater.from(context).inflate(R.layout.activity_adapter__comment_img,parent,false);

        viewHolder1 = new ViewHolder(view1);

        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){

        holder.commentimgadap.setImageResource(SubjectValues[position]);
        Bitmap image = BitmapFactory.decodeResource(context.getResources(), SubjectValues[position]);// This will convert drawbale image into
        holder.commentimgadap.getResources().getResourceName(SubjectValues[position]);

/*
        holder.commentimgadap.setImageBitmap(Bitmap.createScaledBitmap(image, 330, 500, false));
        holder.commentimgadap.setTag("yourImageName");
*/
        holder.commentimgadap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Log.v("Image name:", String.valueOf(holder.commentimgadap.getTag(Integer.parseInt("yourImageName"))));
              //  Log.d("imagebitmap","::"+holder.commentimgadap.getResources().getResourceName(SubjectValues[position]));

                addImageBetweentext(holder.commentimgadap.getDrawable());
            }
        });
    }

    @Override
    public int getItemCount(){

        return SubjectValues.length;
    }

    private void addImageBetweentext(Drawable drawable) {
        drawable .setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        int selectionCursor = commenttxt.getSelectionStart();
        commenttxt.getText().insert(selectionCursor, ".");
        selectionCursor = commenttxt.getSelectionStart();

        SpannableStringBuilder builder = new SpannableStringBuilder(commenttxt.getText());
        builder.setSpan(new ImageSpan(drawable), selectionCursor - ".".length(), selectionCursor,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        commenttxt.setText(builder);
        commenttxt.setSelection(selectionCursor);
    }

}
