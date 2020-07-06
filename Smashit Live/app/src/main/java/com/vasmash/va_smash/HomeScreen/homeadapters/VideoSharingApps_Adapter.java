package com.vasmash.va_smash.HomeScreen.homeadapters;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.vasmash.va_smash.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoSharingApps_Adapter extends RecyclerView.Adapter<VideoSharingApps_Adapter.CustomViewHolder > {

    public Context context;
    private OnItemClickListener listener;
    private List<ResolveInfo> dataList;

    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon, ResolveInfo item, View view);
    }

    public VideoSharingApps_Adapter(Context context,   List<ResolveInfo> dataList, OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_videosharingapps_layout,null);
       CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }



    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {
        final ResolveInfo item= dataList.get(i);
        holder.setIsRecyclable(false);

        try {

            holder.bind(i,item,listener);
            holder.image.setImageDrawable(item.loadIcon(context.getPackageManager()));


        }catch (Exception e){

        }
    }
    class CustomViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;


        public CustomViewHolder(View view) {
            super(view);
            image=view.findViewById(R.id.image);
        }

        public void bind(final int postion,final ResolveInfo item, final VideoSharingApps_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion,item,v);
                }
            });
        }
    }
}
