package com.vasmash.va_smash.createcontent.filters.gpu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vasmash.va_smash.R;
import com.vasmash.va_smash.createcontent.Filter_model;
import com.vasmash.va_smash.createcontent.filters.gpu.player.Filtermodelimg;

import java.util.List;



public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.ViewHolder> {
    private Context context;
    private List<FilterType> personUtilss;
    private List<Filtermodelimg> personUtilssimg;
    private FiltersAdapter.OnFiltersClickListener onFiltersClickListener;

    public FiltersAdapter(Context context, List personUtils ,List personUtilssimg) {
        this.context = context;
        this.personUtilss = personUtils;
        this.personUtilssimg = personUtilssimg;
    }

    @Override
    public FiltersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filters_recyclerview_items, parent, false);
        FiltersAdapter.ViewHolder viewHolder = new FiltersAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FiltersAdapter.ViewHolder holder, int position) {
        holder.itemView.setTag(personUtilss.get(position));
        holder.itemView.setTag(personUtilssimg.get(position));

        FilterType pu = personUtilss.get(position);
        Filtermodelimg puimg=personUtilssimg.get(position);
        Log.e("filter",pu.name());
        holder.filtername.setText(pu.name());


//        holder.filterimange.setImageDrawable(pu.getFilterimage());

        Picasso.with(holder.itemView.getContext()).load(puimg.getFilterimage()).into(holder.filterimange);
    }

    @Override
    public int getItemCount() {
        return personUtilss.size();
    }
    public void setonFiltersClickListener(FiltersAdapter.OnFiltersClickListener onFiltersClickListener) {
        this.onFiltersClickListener = onFiltersClickListener;
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
                    if (onFiltersClickListener != null)
                        onFiltersClickListener.onFiltersClickListener(v,getPosition());
                }
            });

        }
    }
    public interface OnFiltersClickListener {
        void onFiltersClickListener(View v,int postoin);
    }
}

//public class FiltersAdapter extends ArrayAdapter<FilterType> {
//
//    static class ViewHolder {
//        public TextView text;
//    }
//
//    private final Context context;
//    private final List<FilterType> values;
//    private boolean isWhite = false;
//
//    public FiltersAdapter(Context context, int resource, List<FilterType> objects) {
//        super(context, resource, objects);
//        this.context = context;
//        values = objects;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        View rowView = convertView;
//        // reuse views
//        if (rowView == null) {
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                rowView = inflater.inflate(R.layout.filters_recyclerview_items, null);
//            // configure view holder
//            ViewHolder viewHolder = new ViewHolder();
//            viewHolder.text = rowView.findViewById(R.id.filternm);
//            rowView.setTag(viewHolder);
//        }
//
//        ViewHolder holder = (ViewHolder) rowView.getTag();
//        String s = values.get(position).name();
//        holder.text.setText(s);
//
//        return rowView;
//    }
//
//    public FiltersAdapter whiteMode() {
//        isWhite = true;
//        return this;
//    }
//
//
//}

