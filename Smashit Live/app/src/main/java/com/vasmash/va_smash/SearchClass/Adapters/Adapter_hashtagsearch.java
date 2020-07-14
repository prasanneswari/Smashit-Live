package com.vasmash.va_smash.SearchClass.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vasmash.va_smash.HomeScreen.homefragment.HashTagsDisplay;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.SearchClass.ModelClass.Model_Searchlatest;
import com.vasmash.va_smash.SearchClass.SearchVerticalData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adapter_hashtagsearch extends RecyclerView.Adapter<Adapter_hashtagsearch.ViewHolder> {

    ArrayList<Model_Searchlatest> mainmodels;
    Context context;

    public Adapter_hashtagsearch(ArrayList<Model_Searchlatest> mainmodels, Context context) {
        this.mainmodels = mainmodels;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_hashtagsearch, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.hahstagsname.setText(mainmodels.get(position).getHashtagsname());
        holder.hashtagviews.setText(mainmodels.get(position).getHashtagsviews()+" "+"Posts");
        holder.hahstagsname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HashTagsDisplay.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("hashtag",mainmodels.get(position).getHashtagsname());
                context.startActivity(intent);

            }
        });
    }
    @Override
    public int getItemCount() {
        return mainmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView hahstagsname, hashtagviews;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hahstagsname = itemView.findViewById(R.id.hashtagname);
            hashtagviews = itemView.findViewById(R.id.views);
        }
    }
}