package com.vasmash.va_smash.SearchClass.Adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Trading;
import com.vasmash.va_smash.SearchClass.SearchVerticalData;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_TradingTabs extends RecyclerView.Adapter<Adapter_TradingTabs.ViewHolder>{

    ArrayList<Model_Trading> dataObjectList;
    ArrayList<String> fileL;

    Context context;
    View view1;
    ViewHolder viewHolder1;

    public Adapter_TradingTabs(Context context1, ArrayList<Model_Trading> dataObjectList,ArrayList<String> fileL){

        this.dataObjectList = dataObjectList;
        this.context = context1;
        this.fileL=fileL;
        //Log.d("adaptertrading",":::"+fileL);

    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView likeimg,image,playimg;
        TextView count;
        VideoView videosearch;

        public ViewHolder(View v){

            super(v);

            videosearch = v.findViewById(R.id.video);
            likeimg = v.findViewById(R.id.likeimg);
            count = v.findViewById(R.id.count);
            image = v.findViewById(R.id.image);
            playimg=v.findViewById(R.id.play);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int position){
        view1 = LayoutInflater.from(context).inflate(R.layout.activity_adapter__trading_tabs,parent,false);

        viewHolder1 = new ViewHolder(view1);



        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){

        holder.count.setText(dataObjectList.get(position).getCount());

        String searchtype=dataObjectList.get(position).getType();


        //Log.d("searchtypeeee",":::"+searchtype+":::::"+dataObjectList.get(position).getImage());
        if(searchtype.equals("0")){
//            displayImage.setImageResource(Integer.parseInt(this.dataObjectList.get(position).getFile()));
            Picasso.with(context).load(dataObjectList.get(position).getImage()).into(holder.image);
            holder.playimg.setVisibility(View.GONE);

        }
        else{
            holder.playimg.setVisibility(View.VISIBLE);
            holder.playimg.setImageResource(R.drawable.playimg);
            Glide.with(context).load(dataObjectList.get(position).getGifimg()).into(new GlideDrawableImageViewTarget(holder.image));
        }

        if (dataObjectList.get(position).getLikescondition().equals("true")){
            holder.likeimg.setBackgroundResource(R.drawable.likesearch);
        }else {
            holder.likeimg.setBackgroundResource(R.drawable.commentunlike);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                Gson gson = new Gson();
                String json = gson.toJson(dataObjectList);
                editor.putString("arraydata", json);
                editor.commit();

                //Log.d("tradingadapter",":::"+fileL);
                Intent intent = new Intent(context, SearchVerticalData.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("clikpos",position);
                intent.putStringArrayListExtra("fileL",(ArrayList<String>) fileL);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount(){
        return dataObjectList.size();
    }
}
