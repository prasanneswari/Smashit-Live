package com.vasmash.va_smash.VaContentScreen.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vasmash.va_smash.R;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Model_Content;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Tags;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter_Content extends RecyclerView.Adapter<Adapter_Content.ViewHolder> {

    ArrayList<Model_Content> mainmodels;
    ArrayList<Tags> tags;
    ArrayList<String> tagL;
    Context context;
  //  GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;
    String array="null";

    public Adapter_Content(ArrayList<Model_Content> mainmodels,ArrayList<Tags> tagL, Context context) {
        this.mainmodels = mainmodels;
        this.tags = tagL;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter__content, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.contentname.setText(mainmodels.get(position).getContentname());
        holder.contentdate.setText(mainmodels.get(position).getContenttime());
       // holder.contentdata.setText(mainmodels.get(position).getContentdata());
        holder.contentcatogry.setText(mainmodels.get(position).getContentcatogry());

        holder.contentdata.setText(holder.contentdata.getText() + " " +tags.get(position).getName());


        // holder.contentlike.setText(mainmodels.get(position).getContentlike());
       // holder.profileimage.setImageResource(mainmodels.get(position).getProfileimg());
      //  holder.contentimage.setImageResource(mainmodels.get(position).getContentimage());


        Log.d("imagelist",":::"+mainmodels.get(position).getContentimage());

       /* if (mainmodels.get(position).getContentimage().isEmpty()){
           // holder.flagimg.setVisibility(View.GONE);
        }else {
           // holder.flagimg.setVisibility(View.VISIBLE);
            requestBuilder = Glide.with(context)
                    .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                    .from(Uri.class)
                    .as(SVG.class)
                    .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                    .sourceEncoder(new StreamEncoder())
                    .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                    .decoder(new SvgDecoder())
                    .listener(new SvgSoftwareLayerSetter<Uri>());


           *//* Log.d("flag array", "::::" + flagarrayL.size());
            Log.d("poooooooooo", "::::" + position);*//*

            Uri uri = Uri.parse(mainmodels.get(position).getContentimage());
            requestBuilder
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    // SVG cannot be serialized so it's not worth to cache it
                    .load(uri)
                    .into(holder.contentimage);
        }

*/

        Picasso.with(context).load(mainmodels.get(position).getContentimage()).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.contentimage);


    }

    @Override
    public int getItemCount() {
        return mainmodels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentname,contentdate,contentdata,contentlike,follow,clicklike,clickcomment,clickshare,contentcatogry;
        ImageView profileimage,contentimage,settingimage,tagimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentname = itemView.findViewById(R.id.contentname);
            contentdate = itemView.findViewById(R.id.contenttime);
            contentdata = itemView.findViewById(R.id.contentdata);
            contentimage = itemView.findViewById(R.id.contentimage);
            contentcatogry=itemView.findViewById(R.id.contentcatogry);


        }
    }
}
