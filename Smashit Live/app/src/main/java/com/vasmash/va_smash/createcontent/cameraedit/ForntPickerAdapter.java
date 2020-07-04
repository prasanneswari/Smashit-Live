package com.vasmash.va_smash.createcontent.cameraedit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vasmash.va_smash.R;

import java.util.List;

public class ForntPickerAdapter extends RecyclerView.Adapter<ForntPickerAdapter.ViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<String> forntPickerColors;
    private ForntPickerAdapter.OnForntPickerClickListener onForntPickerClickListener;

    public ForntPickerAdapter(@NonNull Context context, @NonNull List<String> forntPickerColors) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.forntPickerColors = forntPickerColors;
    }

    @Override
    public ForntPickerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fornt_picker_item_list, parent, false);
        return new ForntPickerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForntPickerAdapter.ViewHolder holder, int position) {
//        buildColorPickerView(holder.forntPickerView, forntPickerColors.get(position));
        holder.forntPickerView.setText(forntPickerColors.get(position));

    }

    @Override
    public int getItemCount() {
        return forntPickerColors.size();
    }

//    private void buildColorPickerView(View view, String colorCode) {
//
//
//
//    }

    public void setonForntPickerClickListener(OnForntPickerClickListener onForntPickerClickListener) {
        this.onForntPickerClickListener = onForntPickerClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView forntPickerView;


        public ViewHolder(View itemView) {
            super(itemView);
            forntPickerView = (TextView) itemView.findViewById(R.id.fornt_image);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onForntPickerClickListener != null)
                        onForntPickerClickListener.onForntPickerClickListener(forntPickerColors.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnForntPickerClickListener {
        void onForntPickerClickListener(String forntimg);
    }
}
