package com.vasmash.va_smash.createcontent.cameraedit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vasmash.va_smash.R;
import com.vasmash.va_smash.createcontent.CameraActivity;
import com.vasmash.va_smash.createcontent.Sounds.Sound_catemodel;

import java.util.List;

public class Sticker_cate_Adapter extends RecyclerView.Adapter<Sticker_cate_Adapter.ViewHolder> {


    private Context context;
    private List<Sound_catemodel> personUtils12;
    private Sticker_cate_Adapter.OnStickercateClickListener onStickercateClickListener;
    int preSelectedIndex = -1;
    CameraActivity cameraActivity;
    String code;

    public Sticker_cate_Adapter(Context context, List personUtils) {
        this.context = context;
        this.personUtils12 = personUtils;

    }

    @Override
    public Sticker_cate_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sounds_cate_model, parent, false);
        Sticker_cate_Adapter.ViewHolder viewHolder = new Sticker_cate_Adapter.ViewHolder(v);
//        Log.e("dfadfa",code);
//        cameraActivity.soundlist(code);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Sticker_cate_Adapter.ViewHolder holder, int position) {
        holder.itemView.setTag(personUtils12.get(position));

        Sound_catemodel pu = personUtils12.get(position);
        //Log.e("filter",pu.getSound_cate()+" "+pu.getSound_code()+" "+pu.isSelected());
        holder.catename.setText(pu.getSound_cate());



        if (pu.isSelected()==true) {
            holder.cateimg.setBackgroundResource(R.drawable.continubuttom);
        }
        else {
            holder.cateimg.setBackgroundResource(R.drawable.sound_normal);
        }

        holder.catename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personUtils12.get(position);
                Sound_catemodel model = personUtils12.get(position);
                code =pu.getSound_code();

              //  Log.e("filter",code);

                personUtils12.get(position).setSelected(true);

                personUtils12.set(position, model);
//                cameraActivity.soundlist(code);
                if (preSelectedIndex > -1){

                    Sound_catemodel preRecord = personUtils12.get(preSelectedIndex);
                    preRecord.setSelected(false);

                    personUtils12.set(preSelectedIndex, preRecord);

                }

                preSelectedIndex = position;

                //now update adapter so we are going to make a update method in adapter
                //now declare adapter final to access in inner method



            }
        });
    }

    @Override
    public int getItemCount() {
        return personUtils12.size();
    }

    public void setonStickercateClickListener(Sticker_cate_Adapter.OnStickercateClickListener onStickercateClickListener) {
        this.onStickercateClickListener = onStickercateClickListener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView catename,catecode;
        public ImageView cateimg;


        public ViewHolder(View itemView) {
            super(itemView);


            catename = (TextView) itemView.findViewById(R.id.sound_cate);
            catecode = (TextView) itemView.findViewById(R.id.sound_cate_id);
            cateimg = (ImageView) itemView.findViewById(R.id.sound_cate_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onStickercateClickListener != null)
                        onStickercateClickListener.onStickercateClickListener(personUtils12.get(getAdapterPosition()));


                    updateRecords(personUtils12);
                }
            });

        }
    }
    public interface OnStickercateClickListener {
        void onStickercateClickListener(Sound_catemodel code);
    }

    public void updateRecords(List<Sound_catemodel> personUtils12 ){
        this.personUtils12 = personUtils12;

        notifyDataSetChanged();
    }
}
