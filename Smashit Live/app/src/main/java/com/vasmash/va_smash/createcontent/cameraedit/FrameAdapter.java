package com.vasmash.va_smash.createcontent.cameraedit;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vasmash.va_smash.R;

import java.util.List;

;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.ViewHolder> {
    private List<Bitmap> imageBitmaps1;
    private List<String> frametxt;
    private LayoutInflater inflater;
    private FrameAdapter.OnFrameClickListener onFrameClickListener;

    public FrameAdapter(@NonNull Context context, @NonNull List<Bitmap> imageBitmaps1, List<String> frametxt) {
        this.inflater = LayoutInflater.from(context);
        this.imageBitmaps1 = imageBitmaps1;
        this.frametxt=frametxt;
    }

    @Override
    public FrameAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_photo_edit_frame_item_list, parent, false);
        return new FrameAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FrameAdapter.ViewHolder holder, int position) {
        holder.imageView1.setImageBitmap(imageBitmaps1.get(position));
        holder.frametx.setText(frametxt.get(position));
    }

    @Override
    public int getItemCount() {
        return imageBitmaps1.size();
    }

    public void setOnFrameClickListener(FrameAdapter.OnFrameClickListener onFrameClickListener) {
        this.onFrameClickListener = onFrameClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView1,imageView2;
        TextView frametx;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView1 = (ImageView) itemView.findViewById(R.id.main_frameimage1);
            frametx=(TextView)itemView.findViewById(R.id.main_frametext);
            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onFrameClickListener != null)
                        onFrameClickListener.onFrameClickListener(frametxt.get(getAdapterPosition()));

                }
            });
        }
    }

    public interface OnFrameClickListener {
        void onFrameClickListener(String frm);
    }
}
