package com.vasmash.va_smash.createcontent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vasmash.va_smash.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Filter_Adapter extends RecyclerView.Adapter<Filter_Adapter.ViewHolder> {
    private Context context;
    private List<Filter_model> personUtils;
    private Filter_Adapter.OnFilterClickListener onFilterClickListener;

    public Filter_Adapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils = personUtils;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filters_recyclerview_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemView.setTag(personUtils.get(position));

        Filter_model pu = personUtils.get(position);
        Log.e("filter",pu.getFiltername()+" "+pu.getFilterimage());
        holder.filtername.setText(pu.getFiltername());
//        holder.filterimange.setImageDrawable(pu.getFilterimage());

        Picasso.with(holder.itemView.getContext()).load(pu.getFilterimage()).into(holder.filterimange);
    }

    @Override
    public int getItemCount() {
        return personUtils.size();
    }
    public void setonFilterClickListener(OnFilterClickListener onFilterClickListener) {
        this.onFilterClickListener = onFilterClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView filtername;
        public ImageView filterimange;

        public ViewHolder(View itemView) {
            super(itemView);

            filtername = (TextView) itemView.findViewById(R.id.filternm);
            filterimange = (ImageView) itemView.findViewById(R.id.filterimg);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onFilterClickListener != null)
                        onFilterClickListener.onFilterClickListener(personUtils.get(getAdapterPosition()));
                }
            });

        }
    }
    public interface OnFilterClickListener {
        void onFilterClickListener(Filter_model filternames);
    }
}
