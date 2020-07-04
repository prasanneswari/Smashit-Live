package com.vasmash.va_smash.VaContentScreen.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vasmash.va_smash.R;
import com.vasmash.va_smash.VaContentScreen.ModeClass.Vastore_content_model;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VastoreAdapter extends RecyclerView.Adapter<VastoreAdapter.MyViewHolder> {

    private List<Vastore_content_model> moviesList;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView vastore_img;
        public TextView hashtag,productname,productdescription;


        public MyViewHolder(View view) {
            super(view);
            vastore_img = (ImageView) view.findViewById(R.id.store_img);
            hashtag =(TextView)view.findViewById(R.id.store_hashtag);
            productname =(TextView)view.findViewById(R.id.store_productname);
            productdescription =(TextView)view.findViewById(R.id.store_productdescpt);


        }
    }


    public VastoreAdapter(List<Vastore_content_model> moviesList ,Context context) {
        this.moviesList = moviesList;
        this.context = context;
    }

    @Override
    public VastoreAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vastore_content_item, parent, false);

        return new VastoreAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VastoreAdapter.MyViewHolder holder, int position) {
        final Vastore_content_model movie = moviesList.get(position);
//        holder.vastore_img.setImageResource(movie.getProductimg());
//       holder.hashtag.setText("#"+movie.getTags());


        String imgurl=movie.getProductimg();
        Picasso.with(holder.itemView.getContext()).load(imgurl).into(holder.vastore_img);
        holder.productname.setText(movie.getProductname());


        holder.vastore_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String producturl=movie.getPruducturl();
                Log.e("products",producturl);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(producturl));
                context.startActivity(i);
            }
        });
//       holder.productdescription.setText(movie.getProductdescp());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}

